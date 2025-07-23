package com.me.mall.dto.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CompanyUserDto {
    @ApiModelProperty(value = "用户手机号")
    private String userPhone;

    @ApiModelProperty(value = "手机号验证码")
    private String authCode;

    @ApiModelProperty(value = "密码，保留字段暂不使用")
    private String userPassword;

    @ApiModelProperty(value = "登陆方式 1手机号+验证码 2 3...")
    private Integer loginType;
}
