package com.campus.model.course.pojos;

import lombok.Data;

import java.time.LocalTime;

@Data
public class LessonTimePart {
    private LocalTime beginTime;
    private LocalTime endTime;
    private Integer dayOfWeek;
}
