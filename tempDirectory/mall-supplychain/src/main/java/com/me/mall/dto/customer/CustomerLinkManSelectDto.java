package com.me.mall.dto.customer;

import com.me.mall.common.domain.PageParamDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

@Data
public class CustomerLinkManSelectDto extends PageParamDTO {
    @NotNull(message = "公司ID不能为空")
    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @NotNull(message = "客户ID不能为空")
    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "联系人名字")
    private String linkmanName;


}
