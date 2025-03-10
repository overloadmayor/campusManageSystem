package com.campus.userservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.common.constants.UserConstants;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.user.dtos.StudentLoginDto;
import com.campus.model.user.dtos.StudentPageDto;
import com.campus.model.user.vos.StudentLoginVo;
import com.campus.userservice.mapper.StudentsMapper;
import com.campus.userservice.service.IStudentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.utils.common.RedisExpireUtil;
import com.campus.utils.common.StuJwtUtil;
import com.campus.utils.common.RSAUtil;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import com.campus.model.user.pojos.Students;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-13
 */
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements IStudentsService {
//    @Autowired
//    private IdeptMajorClient deptMajorClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public ResponseResult login(StudentLoginDto studentLoginDto) {
        StudentLoginVo studentLoginVo = new StudentLoginVo();
        Students one = lambdaQuery().eq(Students::getId, studentLoginDto.getId()).one();
        if(one==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String password= one.getPassword();
        String passwordDto=null;
        try {
            passwordDto= RSAUtil.rsaDecrypt(studentLoginDto.getPassword());

            passwordDto=DigestUtils.md5DigestAsHex(passwordDto.getBytes());
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        if(!password.equals(passwordDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        String token= StuJwtUtil.getToken(one.getId());
        one.setPassword(null);
        studentLoginVo.setToken(token);
        studentLoginVo.setInfo(one);
        return ResponseResult.okResult(studentLoginVo);
    }

    @Override
    public ResponseResult add(Students students) {
        Students one = lambdaQuery().eq(Students::getId, students.getId()).one();
        if(one!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }
        students.setPassword(DigestUtils.md5DigestAsHex(UserConstants.DEFAULT_PASSWORD.getBytes()));
        boolean save = save(students);
        if(save){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }else{
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
        Page<Students> page=Page.of(students.getPage(),students.getPageSize());
        Page<Students> res = lambdaQuery().eq(students.getId() != null, Students::getId,
                        students.getId())
                .eq(students.getDept() != null, Students::getDept, students.getDept())
                .eq(students.getGrade() != null, Students::getGrade, students.getGrade())
                .eq(students.getMajor() != null, Students::getMajor, students.getMajor())
                .like(students.getName()!=null,Students::getName,"%"+students.getName()+"%")
                .eq(students.getSex()!=null,Students::getSex,students.getSex())
                .page(page);
        res.getRecords().stream().forEach(stu->stu.setPassword(null));

        return ResponseResult.okResult(res);
    }

    @Override
    public ResponseResult getInfo() {
        Long user = UserThreadLocalUtil.getUser();
        Students student=null;
        String userJson = stringRedisTemplate.opsForValue().get(UserConstants.USER_INFO + user);
        if(userJson!=null){
            student= JSON.parseObject(userJson, Students.class);
        }
        else{
            student = getById(user);
            if(student==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            student.setPassword(null);
            stringRedisTemplate.opsForValue().set(UserConstants.USER_INFO + user,
                    JSON.toJSONString(student), RedisExpireUtil.RandomTime(), TimeUnit.MINUTES);
        }
        return ResponseResult.okResult(student);
    }
}
