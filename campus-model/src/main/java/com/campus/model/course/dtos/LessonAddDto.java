package com.campus.model.course.dtos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.campus.model.course.pojos.Lesson;
import com.campus.model.course.pojos.LessonTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Data
public class LessonAddDto {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long courseId;

    private Long teacherId;

    private Integer startWeek;

    private Integer endWeek;

    private Integer selected;

    private Integer capacity;

    private Integer targetGrade;
    @JsonFormat(pattern = "yyyy-MM")
    private YearMonth term;
    private String uid;
    private List<LessonTime> time;
}
