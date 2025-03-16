package com.campus.userservice.service.impl;

import cn.idev.excel.FastExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.apis.deptMajor.IdeptMajorClient;
import com.campus.common.constants.UserConstants;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.major.pojos.Major;
import com.campus.model.majorAndDept.pojos.DeptMajor;
import com.campus.model.user.dtos.StudentLoginDto;
import com.campus.model.user.dtos.StudentPageDto;
import com.campus.model.user.pojos.BatchNotification;
import com.campus.model.user.pojos.SendMQPojos;
import com.campus.model.user.vos.StudentLoginVo;
import com.campus.userservice.listener.BatchDataListener;
import com.campus.userservice.mapper.StudentsMapper;
import com.campus.userservice.service.IStudentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.utils.common.RedisExpireUtil;
import com.campus.utils.common.StuJwtUtil;
import com.campus.utils.common.RSAUtil;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.campus.model.user.pojos.Students;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-13
 */
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements IStudentsService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private StudentsMapper studentsMapper;
    @Autowired
    private IdeptMajorClient deptMajorClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Qualifier("redisTemplate")
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseResult login(StudentLoginDto studentLoginDto) {
        StudentLoginVo studentLoginVo = new StudentLoginVo();
        Students one = lambdaQuery().eq(Students::getId, studentLoginDto.getId()).one();
        if (one == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String password = one.getPassword();
        String passwordDto = null;
        try {
            passwordDto = RSAUtil.rsaDecrypt(studentLoginDto.getPassword());

            passwordDto = DigestUtils.md5DigestAsHex(passwordDto.getBytes());
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        if (!password.equals(passwordDto)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        String token = StuJwtUtil.getToken(one.getId());
        one.setPassword(null);
        studentLoginVo.setToken(token);
        studentLoginVo.setInfo(one);
        return ResponseResult.okResult(studentLoginVo);
    }

    @Override
    public ResponseResult add(Students students) {
        Students one = lambdaQuery().eq(Students::getId, students.getId()).one();
        if (one != null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }
        students.setPassword(DigestUtils.md5DigestAsHex(UserConstants.DEFAULT_PASSWORD.getBytes()));
        boolean save = save(students);
        if (save) {
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        } else {
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
    }

    @Override
    public ResponseResult listAll(StudentPageDto students) {
//        ResponseResult responseResult = deptMajorClient.getDeptMajor();
//        if(responseResult.getCode()!=200){
//            return responseResult;
//        }
//        String jsonString= JSON.toJSONString(responseResult.getData());
//        List<DeptMajor> deptMajors = JSON.parseArray(jsonString, DeptMajor.class);
        students.checkParam();
        Page<Students> page = Page.of(students.getPage(), students.getPageSize());
        Page<Students> res = lambdaQuery().eq(students.getId() != null, Students::getId,
                        students.getId())
                .eq(students.getDept() != null, Students::getDept, students.getDept())
                .eq(students.getGrade() != null, Students::getGrade, students.getGrade())
                .eq(students.getMajor() != null, Students::getMajor, students.getMajor())
                .like(students.getName() != null, Students::getName, "%" + students.getName() + "%")
                .eq(students.getSex() != null, Students::getSex, students.getSex())
                .page(page);
        res.getRecords().stream().forEach(stu -> stu.setPassword(null));

        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult getInfo() {
        Long user = UserThreadLocalUtil.getUser();
        Students student = null;
        String userJson = stringRedisTemplate.opsForValue().get(UserConstants.USER_INFO + user);
        if (userJson != null) {
            student = JSON.parseObject(userJson, Students.class);
        } else {
            student = getById(user);
            if (student == null) {
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            student.setPassword(null);
            stringRedisTemplate.opsForValue().set(UserConstants.USER_INFO + user,
                    JSON.toJSONString(student), RedisExpireUtil.RandomTime(), TimeUnit.MINUTES);
        }
        return ResponseResult.okResult(student);
    }

    @Override
    public ResponseResult getInfosByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseResult.errorResult(null);
        }
        List<Students> students = studentsMapper.getInfosByIds(ids);

        return ResponseResult.okResult(students);
    }

    @Override
    public void batchInsert(SendMQPojos pojo) {
        String filename = pojo.getFileTempName();
        String fileOriginName = pojo.getFileOriginName();
        long time = pojo.getTime();
        Map<String, Long> majorMap = new HashMap<String, Long>();
        Map<String, Long> deptMap = new HashMap<String, Long>();
        Map<Long, Long> majorToDept = new HashMap<Long, Long>();
        ResponseResult deptMajorResult = deptMajorClient.getDeptMajor();
        if (deptMajorResult.getCode() != AppHttpCodeEnum.SUCCESS.getCode()) {
            return;
        }
        List<DeptMajor> deptMajors = JSON.parseArray(JSON.toJSONString(deptMajorResult.getData()), DeptMajor.class);
        for (DeptMajor deptMajor : deptMajors) {
            deptMap.put(deptMajor.getName(), deptMajor.getId());
            for (Major major : deptMajor.getMajors()) {
                majorMap.put(major.getName(), major.getId());
                majorToDept.put(major.getId(), deptMajor.getId());
            }
        }
        File filePath = new File(UserConstants.UPLOAD_DIRECTORY, filename);

//        System.out.println(filePath.exists());
        try (InputStream fileInputStream = new FileInputStream(filePath)) {
            FastExcel.read(fileInputStream, Students.class, new BatchDataListener(majorMap,
                    deptMap, majorToDept, this, stringRedisTemplate, pojo))
                    .sheet()
                    .doRead();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 上传excel文件
     *
     * @param request
     * @return
     */
    @Override
    public ResponseResult handleFileUpload(HttpServletRequest request) {
        // 从请求头中获取文件名和块索引
        String fileName = request.getHeader("X-File-Name"); // 确定文件存储路径
        String fileOriginName = request.getHeader("X-Original-File-Name"); //确定源文件名
        if (StringUtils.isBlank(fileName)) {
            fileName = UUID.randomUUID().toString() + ".xlsx";
        }
        String uploadDirectory = UserConstants.UPLOAD_DIRECTORY;
        String isEnd = request.getHeader("X-End-Chunk");
        File uploadDir = new File(uploadDirectory);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String filePath = uploadDirectory + File.separator + fileName; // 使用追加模式写入文件
        try (OutputStream out = new FileOutputStream(filePath, true)) {
            InputStream inputStream = request.getInputStream();
            byte[] buffer = new byte[1 << 19];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseResult.errorResult(501, "上传异常");
        }
        if ("true".equals(isEnd)) {
            SendMQPojos pojo = new SendMQPojos();
            long time = System.currentTimeMillis();
            pojo.setTime(time);
            pojo.setFileOriginName(fileOriginName);
            pojo.setFileTempName(fileName);
            pojo.setUserId(UserThreadLocalUtil.getUser());
            BatchNotification notification = new BatchNotification();
            BeanUtils.copyProperties(pojo, notification);
            notification.setStatus(0);
            rabbitTemplate.convertAndSend(UserConstants.USER_BATCH_INSERT_EXCHANGE,
                    UserConstants.USER_BATCH_INSERT_ROUTINGKEY, pojo);
            stringRedisTemplate.opsForHash().put(UserConstants.Admin_UPLOAD_NOTIFICATION + UserThreadLocalUtil.getUser(),
                    String.valueOf(time), JSON.toJSONString(notification));
            return ResponseResult.okResult("ok");
        }
        return ResponseResult.okResult(fileName);
    }

    @Override
    public ResponseResult checkUploadDetailInfos() {
        Long user = UserThreadLocalUtil.getUser();
        Map<Object, Object> map =
                stringRedisTemplate.opsForHash().entries(UserConstants.Admin_UPLOAD_NOTIFICATION + user);
        List<BatchNotification> batchNotifications = new ArrayList<BatchNotification>();
        for (Object value : map.values()) {
            BatchNotification batchNotification = JSON.parseObject(value.toString(), BatchNotification.class);
            batchNotifications.add(batchNotification);
        }
        return ResponseResult.okResult(batchNotifications);


    }
}
