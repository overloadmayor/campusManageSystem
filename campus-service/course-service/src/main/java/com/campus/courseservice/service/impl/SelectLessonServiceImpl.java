package com.campus.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.campus.apis.course.ICourseClient;
import com.campus.common.constants.CourseConstants;
import com.campus.common.constants.SelectLessonConstants;
import com.campus.courseservice.service.ILessonTimeService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.course.pojos.LessonTime;
import com.campus.model.selectLesson.dtos.SetTimeDto;
import com.campus.model.selectLesson.pojos.SelectLesson;
import com.campus.courseservice.mapper.SelectLessonMapper;
import com.campus.courseservice.service.ISelectLessonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-02-01
 */
@Service
@Primary
public class SelectLessonServiceImpl extends ServiceImpl<SelectLessonMapper, SelectLesson> implements ISelectLessonService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SelectLessonMapper selectLessonMapper;
    @Autowired
    private ILessonTimeService lessonTimeService;


    @Override
    public ResponseResult setTime(SetTimeDto setTimeDto) {
        if(setTimeDto==null||setTimeDto.getBeginTime()==null||setTimeDto.getEndTime()==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        stringRedisTemplate.opsForValue().set(SelectLessonConstants.SELECT_LESSON_TIME,
                JSON.toJSONString(setTimeDto));
        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
    }

    @Override
    public ResponseResult getTime() {
        String startTime = stringRedisTemplate.opsForValue().get(SelectLessonConstants.SELECT_LESSON_TIME);
        if(startTime==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"未设置抢课时间");
        }
        return ResponseResult.okResult(JSON.parseObject(startTime, SetTimeDto.class));
    }

    //查询学生课程表
    @Override
    public ResponseResult listByStuId() {
        Long user = UserThreadLocalUtil.getUser();
        List<Long> lessonIds = selectLessonMapper.getByStuId(user, CourseConstants.getTerm());
        List<Long> rest=new ArrayList<>();
        List<LessonTime> result=new ArrayList<>();
        for (Long id : lessonIds) {
            String lessonJson =
                    stringRedisTemplate.opsForValue().get(CourseConstants.LESSON_TIME + id);
            if(lessonJson==null){
                rest.add(id);
            }else{
                List<LessonTime> times = JSON.parseArray(lessonJson, LessonTime.class);
                result.addAll(times);
            }
        }
        if(!rest.isEmpty()) {
            ResponseResult clientResult = lessonTimeService.getLessonTimes(rest);
            if(clientResult.getCode()!=AppHttpCodeEnum.SUCCESS.getCode()){
                return clientResult;
            }
            String jsonString = JSON.toJSONString(clientResult.getData());
            List<LessonTime> lessonTimes = JSON.parseArray(jsonString, LessonTime.class);

            if(lessonTimes!=null){
                result.addAll(lessonTimes);
            }
        }
        return ResponseResult.okResult(result);
    }



}
