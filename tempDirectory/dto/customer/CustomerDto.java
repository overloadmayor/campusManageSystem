package com.me.mall.dto.customer;

import com.me.mall.model.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CustomerDto  {
    @ApiModelProperty(value = "客户ID（主键）")
    private Long id;

    @ApiModelProperty(value = "所属公司ID")
    private Long companyId;

    @ApiModelProperty(value = "客户编码(KH-2025-03-04-0001形式自动生成)")
    private String code;

    @ApiModelProperty(value = "客户类型（关联字典表 type=1直销 2经销）")
    private Integer type;

    @ApiModelProperty(value = "客户名称（企查查对接字段）")
    private String name;

    @ApiModelProperty(value = "省份ID（关联地区表）")
    private Integer province;

    @ApiModelProperty(value = "城市ID（关联地区表）")
    private Integer city;

    @ApiModelProperty(value = "区县ID（关联地区表）")
    private Integer region;

    @ApiModelProperty(value = "客户状态（1未激活 2活跃 3沉睡）")
    private Integer status;

    @ApiModelProperty(value = "客户等级（关联字典表）")
    private Integer level;

    @ApiModelProperty(value = "客户规模（关联字典表）")
    private Integer scale;

    @ApiModelProperty(value = "客户忠诚度（关联字典表）")
    private Integer loyalty;

    @ApiModelProperty(value = "总成交金额")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "直销金额")
    private BigDecimal directTotalAmount;

    @ApiModelProperty(value = "经销金额")
    private BigDecimal pickTotalAmount;

    @ApiModelProperty(value = "成交均价（需业务计算更新）")
    private BigDecimal avgAmount;

    @ApiModelProperty(value = "创建人ID（关联用户表）")
    private Long createUserId;

    @ApiModelProperty(value = "最近沟通时间")
    private Date lastContactTime;

    @ApiModelProperty(value = "最近下单时间")
    private Date lastOrderTime;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "手机号码")
    private String mobile;

    @ApiModelProperty(value = "座机号码")
    private String telephone;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "客户标签ID集合（JSON数组格式）")
    private String customerTagsIds;

    @ApiModelProperty(value = "参与项目ID集合（JSON数组格式）")
    private String projectsId;

    @ApiModelProperty(value = "是否启用")
    private Integer enable;

}
