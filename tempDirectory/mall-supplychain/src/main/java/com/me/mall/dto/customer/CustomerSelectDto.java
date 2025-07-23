package com.me.mall.dto.customer;

import com.me.mall.common.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSelectDto extends PageParamDTO {

    @ApiModelProperty(value = "客户名称（企查查对接字段）")
    private String name;

    @ApiModelProperty(value = "创建人ID（关联用户表）")
    private Long createUserId;

    @ApiModelProperty(value = "客户编码(KH-2025-03-04-0001形式自动生成)")
    private String code;

}
