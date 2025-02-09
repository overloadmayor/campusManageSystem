package com.campus.model.course.vo;


import com.campus.model.course.pojos.LessonAndTime;
import com.campus.model.course.pojos.MajorCourse;
import com.campus.model.course.pojos.PreCourse;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class CourseListTreeVo {
    private Long id;

    private String name;

    private Double credit;

    private List<Long> preCourseIds;

    private List<MajorCourse> majorCourses;
    
    private List<LessonAndTime> lessonList;
}
