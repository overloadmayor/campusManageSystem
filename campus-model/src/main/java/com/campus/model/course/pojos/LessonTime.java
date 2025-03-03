package com.campus.model.course.pojos;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-01-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("lesson_time")
@ApiModel(value="LessonTime对象", description="")
public class LessonTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    private String lessonName;

    private Long lessonId;

    private LocalTime beginTime;

    @ApiModelProperty(value = "1-7对应星期一到星期日")
    private Integer dayOfWeek;

    private LocalTime endTime;

    @ApiModelProperty(value = "0表示每周都上，1单周，2双周")
    private Integer status;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

}
