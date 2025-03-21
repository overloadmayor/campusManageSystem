package com.campus.model.user.pojos;

import cn.idev.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-01-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = false)
@TableName("students")
@ApiModel(value="Students对象", description="")
@AllArgsConstructor
@NoArgsConstructor
public class Students implements Serializable {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("学号")
    @TableId(value = "id")
    private Long id;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("年级")
    private Integer grade;

    private Long dept;

    private Long major;

    @ExcelProperty("学分")
    private Integer credit;

    private String password;

    @ExcelProperty("系别")
    @TableField(exist = false)
    private String deptName;

    @ExcelProperty("专业")
    @TableField(exist = false)
    private String majorName;
}
