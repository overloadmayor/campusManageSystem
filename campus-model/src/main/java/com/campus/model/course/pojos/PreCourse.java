package com.campus.model.course.pojos;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
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
 * @since 2025-01-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pre_course")
@ApiModel(value="PreCourse对象", description="")
public class PreCourse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "pre_id")
    private Long preId;

    @TableField("after_id")
    private Long afterId;


}
