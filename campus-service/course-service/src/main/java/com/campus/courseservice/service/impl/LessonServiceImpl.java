package com.campus.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.campus.common.constants.CourseConstants;
import com.campus.common.util.CacheService;
import com.campus.courseservice.service.ILessonTimeService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.course.dtos.LessonAddDto;
import com.campus.model.course.pojos.Lesson;
import com.campus.courseservice.mapper.LessonMapper;
import com.campus.courseservice.service.ILessonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.model.course.pojos.LessonTime;
import com.campus.model.teacher.dtos.TeacherCourseDto;
import com.campus.model.teacher.dtos.TeacherLessonDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-21
 */
@Service
public class LessonServiceImpl extends ServiceImpl<LessonMapper, Lesson> implements ILessonService {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ILessonTimeService lessonTimeService;

    private static final DefaultRedisScript<String> APPLY_LESSON_SCRIPT;

    static {
        APPLY_LESSON_SCRIPT = new DefaultRedisScript<>();
        APPLY_LESSON_SCRIPT.setLocation(new ClassPathResource("applyLesson.lua"));
        APPLY_LESSON_SCRIPT.setResultType(String.class);
    }

    @Override
    public ResponseResult checkAddLesson() {
        Set<String> scan = cacheService.scan(CourseConstants.ADD_LESSON_TOPIC + "*");
        List<TeacherLessonDto> dtoList = new ArrayList<>();
        for (String key : scan) {
            String value = cacheService.get(key);
            TeacherLessonDto dto = JSON.parseObject(value, TeacherLessonDto.class);
            dtoList.add(dto);
        }
        return ResponseResult.okResult(dtoList);
    }

    @Override
    @Transactional
    public ResponseResult addLesson(LessonAddDto lessonAddDto) {
        if (lessonAddDto.getTerm() == null ||
                (!lessonAddDto.getTerm().getMonth().equals(Month.MARCH)&&!lessonAddDto.getTerm().getMonth().equals(Month.SEPTEMBER))
        ){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"课程不合法");
        }
            String result = stringRedisTemplate.execute(
                    APPLY_LESSON_SCRIPT,
                    Collections.emptyList(),
                    CourseConstants.ADD_LESSON_TOPIC + lessonAddDto.getUid()
            );
        if (result == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        TeacherLessonDto dto = JSON.parseObject(result, TeacherLessonDto.class);
        Lesson lesson = new Lesson();
        BeanUtils.copyProperties(lessonAddDto, lesson);
        lesson.setTeacherId(dto.getTeacherId());
        lesson.setCourseId(dto.getCourseId());
        lesson.setTargetGrade(dto.getTargetGrade());
        lesson.setTerm(lessonAddDto.getTerm().atDay(1));
        save(lesson);
        for (LessonTime lessonTime : lessonAddDto.getTime()) {
            lessonTime.setLessonId(lesson.getId());
            lessonTimeService.save(lessonTime);
        }
        stringRedisTemplate.delete(CourseConstants.COURSE_DETAIL + lesson.getCourseId());
        return ResponseResult.okResult(lesson.getId());
    }
}
