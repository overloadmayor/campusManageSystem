package com.me.mall.dto.company;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CompanyDto {
    @ApiModelProperty(value = "企业唯一主键，非null，自增")
    private Long companyId;

    @ApiModelProperty(value = "企业唯一编码（SDYC-2025-03-04-0001形式自动生成）")
    private String companyCode;

    @ApiModelProperty(value = "客户名称")
    private String companyName;

    @ApiModelProperty(value = "客户企业全称，营业执照全称")
    private String fullName;

    @ApiModelProperty(value = "统一信用代码")
    private String licenseNumber;

    @ApiModelProperty(value = "公司地址")
    private String address;

    @ApiModelProperty(value = "联系人名称")
    private String linkmanName;

    @ApiModelProperty(value = "联系人电话")
    private String linkmanPhone;

    @ApiModelProperty(value = "联系人邮箱")
    private String linkmanEmail;

    @ApiModelProperty(value = "法人")
    private String legalPerson;

    @ApiModelProperty(value = "法人身份证")
    private String legalPersonCardId;

    @ApiModelProperty(value = "开户行银行账号")
    private String bankNumber;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "营业执照图片")
    private String licensePic;

    @ApiModelProperty(value = "省（对应表area）")
    private Integer province;

    @ApiModelProperty(value = "市")
    private Integer city;

    @ApiModelProperty(value = "区")
    private Integer region;

    @ApiModelProperty(value = "企业描述")
    private String remark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "0禁用 1启用 （默认值1）")
    private Integer enable;

    @ApiModelProperty(value = "备用字段1")
    private String userdef1;

    @ApiModelProperty(value = "备用字段2")
    private String userdef2;

    @ApiModelProperty(value = "备用字段3")
    private String userdef3;

    @ApiModelProperty(value = "备用字段4")
    private String userdef4;

    @ApiModelProperty(value = "备用字段5")
    private String userdef5;
}
