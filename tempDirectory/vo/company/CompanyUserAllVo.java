package com.me.mall.vo.company;

import com.me.mall.security.vo.CompanyUserVo;
import com.me.mall.security.vo.CompanyVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CompanyUserAllVo {
    @ApiModelProperty("用户相关信息")
    private CompanyUserVo userVo;

    @ApiModelProperty(value = "企业相关信息")
    private CompanyVo companyVo;
}
