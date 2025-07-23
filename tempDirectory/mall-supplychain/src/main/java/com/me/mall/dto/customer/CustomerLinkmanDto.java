package com.me.mall.dto.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.me.mall.model.CustomerLinkman;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class CustomerLinkmanDto{
    private Long id;

    @ApiModelProperty(value = "公司ID")
    private Long companyId;

    @ApiModelProperty(value = "客户ID")
    private Long customerId;

    @ApiModelProperty(value = "联系人名字")
    private String linkmanName;

    @ApiModelProperty(value = "联系人性别")
    private Integer linkmanSex;

    @ApiModelProperty(value = "手机号码")
    private String cellPhoneNumber;

    @ApiModelProperty(value = "固定电话号码")
    private String landlineNumber;

    @ApiModelProperty(value = "电子邮件地址")
    private String email;

    @ApiModelProperty(value = "联系人职务")
    private String linkmanDuty;

    @ApiModelProperty(value = "客服热线")
    private String hotline;

    @ApiModelProperty(value = "传真号码")
    private String faxNo;

    @ApiModelProperty(value = "联系人qq")
    private String linkmanQq;

    @ApiModelProperty(value = "联系人微信")
    private String linkmanWx;

    @ApiModelProperty(value = "地址")
    private String linkmanAddress;

    @ApiModelProperty(value = "描述")
    private String linkmanMark;

    @ApiModelProperty(value = "是否主要联系人")
    private Integer isMainly;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @ApiModelProperty(value="删除状态")
    private Integer deleteStatus;

    @ApiModelProperty(value = "出生日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDay;

    @ApiModelProperty(value = "画像集合 customer_portrait")
    private String portraitIds;


}
