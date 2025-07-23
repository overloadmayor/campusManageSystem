package com.campus.ai.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class AiOrders {
    private Long id;

    private String outTradeNo;

    private Long stuId;

    private String subject;

    private BigDecimal totalAmount;

    private Timestamp createTime;

    private Integer status;
}