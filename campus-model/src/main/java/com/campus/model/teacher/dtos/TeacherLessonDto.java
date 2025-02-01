package com.campus.model.teacher.dtos;

import com.campus.model.course.pojos.MajorCourse;
import lombok.Data;

import java.util.List;

@Data
public class TeacherLessonDto {
    private Long courseId;
    private Integer targetGrade;
    private String uid;
    private Long teacherId;

}
