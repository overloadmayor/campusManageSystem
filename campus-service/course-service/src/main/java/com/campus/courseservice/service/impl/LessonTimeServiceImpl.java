package com.campus.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.campus.common.constants.CourseConstants;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.course.pojos.Lesson;
import com.campus.model.course.pojos.LessonTime;
import com.campus.courseservice.mapper.LessonTimeMapper;
import com.campus.courseservice.service.ILessonTimeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.model.course.pojos.LessonTimePart;
import com.campus.model.course.pojos.MajorCourse;
import com.campus.model.course.vo.LessonTimeVo;
import com.campus.model.teacher.dtos.TeacherLessonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-21
 */
@Service
public class LessonTimeServiceImpl extends ServiceImpl<LessonTimeMapper, LessonTime> implements ILessonTimeService {
    @Autowired
    private LessonTimeMapper lessonTimeMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public ResponseResult calAvailableTime(String uid, Integer status) {
        String lesson = stringRedisTemplate.opsForValue().get(CourseConstants.ADD_LESSON_TOPIC + uid);
        //获取课次申请，其中有课程号
        TeacherLessonDto dto = JSON.parseObject(lesson, TeacherLessonDto.class);
        //根据课程号获取面向专业和必修或选修
        List<MajorCourse> majorCourses = Db.lambdaQuery(MajorCourse.class).eq(MajorCourse::getCourseId, dto.getCourseId()).list();
        Set<LessonTimePart> ban = new HashSet<>();
        Set<LessonTimePart> notRequired = new HashSet<>();
        for (MajorCourse majorCours : majorCourses) {
            selectLessonTimePart(majorCours, ban, notRequired,status);
        }
        LessonTimeVo lessonTimeVo = new LessonTimeVo();
        notRequired.removeAll(ban);
        lessonTimeVo.setBan(ban);
        lessonTimeVo.setNotRequired(notRequired);
        return ResponseResult.okResult(lessonTimeVo);
    }

    private void selectLessonTimePart(MajorCourse majorCourse, Set<LessonTimePart> ban
            , Set<LessonTimePart> notRequired,Integer status) {
        //找出这个专业其他的课程
        List<MajorCourse> Courses = Db.lambdaQuery(MajorCourse.class).eq(MajorCourse::getMajorId,
                majorCourse.getMajorId()).list();
        if (majorCourse.getRequired() == 1) {
            List<Long> courseIds = Courses.stream().map(MajorCourse::getCourseId).collect(Collectors.toList());
            addQueue(ban, courseIds,status);
        } else {
            List<Long> courseIds =
                    Courses.stream().filter(major -> major.getRequired() == 1).map(MajorCourse::getCourseId).collect(Collectors.toList());
            addQueue(ban, courseIds,status);
            courseIds =
                    Courses.stream().filter(major -> major.getRequired() == 0).map(MajorCourse::getCourseId).collect(Collectors.toList());
            addQueue(notRequired, courseIds,status);
        }

    }

    private void addQueue(Set<LessonTimePart> ban, List<Long> courseIds,Integer status) {
        if(courseIds.isEmpty())return ;
        List<Long> lessonIds = Db.lambdaQuery(Lesson.class)
                .in(Lesson::getCourseId, courseIds).list().stream().map(Lesson::getId).collect(Collectors.toList());
        if(lessonIds.isEmpty())return ;
        List<LessonTime> lessonTimes = lambdaQuery()
                .eq(status==1,LessonTime::getStatus,status!=2)
                .eq(status==2,LessonTime::getStatus,status!=1)
        .in(LessonTime::getLessonId, lessonIds).list();
        for (LessonTime lessonTime : lessonTimes) {
            LessonTimePart lessonTimePart = new LessonTimePart();
            lessonTimePart.setBeginTime(lessonTime.getBeginTime());
            lessonTimePart.setEndTime(lessonTime.getEndTime());
            lessonTimePart.setDayOfWeek(lessonTime.getDayOfWeek());
            ban.add(lessonTimePart);
        }
    }
}
