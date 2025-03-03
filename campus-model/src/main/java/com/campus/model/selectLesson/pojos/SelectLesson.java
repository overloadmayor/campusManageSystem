package com.campus.model.selectLesson.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.campus.model.course.pojos.LessonAndTime;
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
 * @since 2025-02-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("select_lesson")
@ApiModel(value="SelectLesson对象", description="")
public class SelectLesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long lessonId;

    private Long studentId;

    @TableField(exist = false)
    private Integer capacity;

    @TableField(exist = false)
    private List<String> lessonTimes;

    //1 选课 0 退课
    @TableField(exist = false)
    private Integer option;

    @TableField(exist = false)
    private String uuid;
}
