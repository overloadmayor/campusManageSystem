package com.campus.model.course.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.YearMonth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
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
@TableName("lesson")
@ApiModel(value="Lesson对象", description="")
public class Lesson implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long courseId;

    @TableField(exist = false)
    private Boolean selectable;

    private Long teacherId;

    private Integer startWeek;

    private Integer endWeek;

    private Integer selected;

    private Integer capacity;

    @JsonFormat(pattern = "yyyy-MM")
    private LocalDate term;

    private Integer targetGrade;


}
