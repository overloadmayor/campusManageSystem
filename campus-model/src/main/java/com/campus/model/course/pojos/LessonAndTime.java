package com.campus.model.course.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class LessonAndTime extends Lesson{
    private List<LessonTime> lessonTimes;
}
