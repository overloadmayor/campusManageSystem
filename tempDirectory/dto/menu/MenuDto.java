package com.me.mall.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MenuDto {
    @ApiModelProperty(value = "菜单主键")
    private Long menuId;

    @ApiModelProperty(value = "菜单唯一编码（M00001形式）")
    private String menuCode;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @ApiModelProperty(value = "1菜单  2按钮")
    private Integer menuType;

    @ApiModelProperty(value = "菜单路径")
    private String menuUrl;

    @ApiModelProperty(value = "图标")
    private String iconClass;

    @ApiModelProperty(value = "图标路径")
    private String iconUrl;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "状态（0禁用 1启用）")
    private Integer enable;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}
