package com.campus.model.user.pojos;

import cn.idev.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = false)
@AllArgsConstructor
@NoArgsConstructor
public class StudentExcel implements Serializable {
    private static final long serialVersionUID = 19999L;

    @ExcelProperty("学号")
    private Long id;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("性别")
    private String sex;

    @ExcelProperty("年级")
    private Integer grade;

    @ExcelProperty("学分")
    private Integer credit;

    @ExcelProperty("系别")
    private String deptName;

    @ExcelProperty("专业")
    private String majorName;
}
