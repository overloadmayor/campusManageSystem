package com.campus.model.teacher.dtos;

import com.campus.model.course.pojos.MajorCourse;
import lombok.Data;

import java.util.List;

@Data
public class TeacherCourseDto {
    private String uid;
    private String name;
    private Double credit;
    private List<Integer> preIds;
    private List<MajorCourse> majors;
}
