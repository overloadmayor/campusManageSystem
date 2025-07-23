package com.me.mall.vo.system;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DictionaryDetailVo {
    private Integer id;

    @ApiModelProperty(value = "字典主表ID")
    private Integer dictId;

    @ApiModelProperty(value = "字典值名字")
    private String dictValue;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "字典是否可用(0-不可用，1-可用)")
    private Integer enabled;

    @ApiModelProperty(value = "关键字")
    private Integer keyValue;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "字典主表名称")
    private String dictName;

    @ApiModelProperty(value = "类型")
    private String type;
}
