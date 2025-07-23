package com.me.mall.dto.menu;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class MenuSelectDto {
    @ApiModelProperty(value = "菜单状态（0禁用 1启用） 默认赋值1")
    private Integer enable = 1;

    @ApiModelProperty(value = "用户角色集")
    private List<Long> roleIdList;
}
