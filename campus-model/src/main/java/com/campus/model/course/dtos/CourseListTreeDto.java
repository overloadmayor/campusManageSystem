package com.campus.model.course.dtos;

import com.campus.model.common.dtos.PageRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CourseListTreeDto extends PageRequestDto {
    private Long courseId;

    private String name;

    private Double credit;

    private Long lessonId;

    private Long teacherId;

    private Integer startWeek;

    private Integer endWeek;

    private Integer selected;

    private Integer capacity;

    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate term;

    private Integer targetGrade;

    public boolean checkEmpty(){
        return courseId==null&&name==null&&credit==null;
    }
}
