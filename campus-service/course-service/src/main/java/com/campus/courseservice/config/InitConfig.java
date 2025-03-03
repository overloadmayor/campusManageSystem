package com.campus.courseservice.config;

import com.campus.common.constants.CourseConstants;
import com.campus.courseservice.mapper.SelectLessonMapper;
import com.campus.courseservice.service.ILessonService;
import com.campus.model.course.pojos.Lesson;
import com.campus.model.selectLesson.pojos.SelectCourseAndStu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InitConfig {
    @Autowired
    private ILessonService lessonService;
    @Autowired
    private SelectLessonMapper selectLessonMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @PostConstruct
    public void init() {
        List<Lesson> lessons = lessonService
                .lambdaQuery()
                .eq(Lesson::getTerm,CourseConstants.getTerm())
                .list();
        for (Lesson lesson : lessons) {
            String num = stringRedisTemplate.opsForValue().get(CourseConstants.LESSON_SELECTED + lesson.getId());
            if (num == null) {
                stringRedisTemplate.opsForValue().set(CourseConstants.LESSON_SELECTED + lesson.getId(),lesson.getSelected().toString());
            }
        }
        Map<Long, List<SelectCourseAndStu>> stuMap =
                selectLessonMapper.SelectedCourseAndStu(CourseConstants.getTerm())
                .stream().collect(Collectors.groupingBy(SelectCourseAndStu::getCourseId));
        stuMap.forEach((k, list) -> {
            List<String> userIds=new ArrayList<>();
            for (SelectCourseAndStu selectCourseAndStu : list) {
                userIds.add(selectCourseAndStu.getUserId().toString());
            }
            stringRedisTemplate.opsForSet().add(CourseConstants.COURSE_SELECT_STU+k.toString(),
                    userIds.toArray(new String[userIds.size()]));
        });
    }
}
