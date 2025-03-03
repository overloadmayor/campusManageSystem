package com.campus.teacherservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.campus.apis.course.ICourseClient;
import com.campus.common.constants.CourseConstants;
import com.campus.common.constants.TeacherConstants;
import com.campus.model.admin.dtos.TeacherLoginDto;
import com.campus.model.admin.vos.TeacherLoginVo;
import com.campus.model.teacher.dtos.TeacherCourseDto;
import com.campus.model.teacher.dtos.TeacherLessonDto;
import com.campus.model.teacher.dtos.TeacherPageDto;
import com.campus.model.admin.pojos.Teacher;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.teacherservice.mapper.TeacherMapper;
import com.campus.teacherservice.service.ITeacherService;
import com.campus.utils.common.RSAUtil;
import com.campus.utils.common.RedisExpireUtil;
import com.campus.utils.common.TeacherJwtUtil;
import com.campus.utils.thread.UserThreadLocalUtil;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-15
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ICourseClient courseClient;


    @Override
    public ResponseResult addTeacher(Teacher teacher) {
        Teacher one = lambdaQuery().eq(Teacher::getId, teacher.getId()).one();
        if(one!=null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_EXIST);
        }
        teacher.setPassword(DigestUtils.md5DigestAsHex(TeacherConstants.DEFAULT_PASSWORD.getBytes()));
        boolean save = save(teacher);
        if(save){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }else{
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
    }

    @Override
    public ResponseResult listAll(TeacherPageDto teacher) {
        teacher.checkParam();
        Page<Teacher> page = Page.of(teacher.getPage(), teacher.getPageSize());
        Page<Teacher> res = lambdaQuery().eq(teacher.getId() != null, Teacher::getId,
                        teacher.getId())
                .eq(teacher.getAdmin() != null, Teacher::getAdmin, teacher.getAdmin())
                .eq(teacher.getDept() != null, Teacher::getDept, teacher.getDept())
                .like(teacher.getName() != null, Teacher::getName, "%" + teacher.getName() + "%")
                .eq(teacher.getSex() != null, Teacher::getSex, teacher.getSex())
                .eq(teacher.getTitle() != null, Teacher::getTitle, teacher.getTitle())
                .page(page);
        res.getRecords().stream().forEach(stu->stu.setPassword(null));
        return ResponseResult.okResult(res);

    }

    @Override
    public ResponseResult teacherLogin(TeacherLoginDto teacherLoginDto) {
        TeacherLoginVo teacherLoginVo = new TeacherLoginVo();
        Teacher one = lambdaQuery().eq(Teacher::getId, teacherLoginDto.getId()).one();
        if(one==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String password= one.getPassword();
        String passwordDto=null;
        try {
            passwordDto= RSAUtil.rsaDecrypt(teacherLoginDto.getPassword());

            passwordDto= DigestUtils.md5DigestAsHex(passwordDto.getBytes());
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        if(!password.equals(passwordDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        String token= TeacherJwtUtil.getToken(one.getId());
        one.setPassword(null);
        teacherLoginVo.setToken(token);
        teacherLoginVo.setInfo(one);
        return ResponseResult.okResult(teacherLoginVo);
    }

    @Override
    public ResponseResult askForCourses(TeacherCourseDto teacherCourseDto) {
        if(teacherCourseDto.getCredit()<=0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        String uid = UUID.randomUUID().toString();
        teacherCourseDto.setUid(uid);
        stringRedisTemplate.opsForValue().set(CourseConstants.ADD_COURSE_TOPIC+uid,
                JSON.toJSONString(teacherCourseDto));
        String msg = "askForAdd";
        rabbitTemplate.convertAndSend(CourseConstants.ADD_COURSE_EXCHANGE,CourseConstants.ADD_COURSE_ROUTINGKEY,
                msg);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult askForLessons(TeacherLessonDto teacherlessonDto) {
        //判断课程是否存在
        ResponseResult course = courseClient.getCourseById(teacherlessonDto.getCourseId());
        if(course.getCode()!=200){
            return course;
        }
        teacherlessonDto.setTeacherId(UserThreadLocalUtil.getUser());
        String uid = UUID.randomUUID().toString();
        teacherlessonDto.setUid(uid);
        stringRedisTemplate.opsForValue().set(CourseConstants.ADD_LESSON_TOPIC+uid,
                JSON.toJSONString(teacherlessonDto));
        String msg = "askForAdd";

        rabbitTemplate.convertAndSend(CourseConstants.ADD_LESSON_EXCHANGE,
                CourseConstants.ADD_LESSON_ROUTINGKEY,
                msg);
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult getTeacherName(Long id) {
        String teachername=null;
        String namejson = stringRedisTemplate.opsForValue().get(TeacherConstants.TEACHER_KEY + id);
        if(namejson==null){
            Teacher teacher = getById(id);
            if(teacher==null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
            }
            teachername=teacher.getName();
            stringRedisTemplate.opsForValue().set(TeacherConstants.TEACHER_KEY+id,
                    JSON.toJSONString(teachername),
                    RedisExpireUtil.RandomTime(), TimeUnit.MINUTES);
        }else{
            teachername=JSON.parseObject(namejson, String.class);
        }
        return ResponseResult.okResult(teachername);
    }


}
