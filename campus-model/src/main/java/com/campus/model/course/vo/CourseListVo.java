package com.campus.model.course.vo;

import com.campus.model.course.pojos.Course;
import lombok.Data;

@Data
public class CourseListVo {
    private Long id;

    private String name;

    private Double credit;
}
