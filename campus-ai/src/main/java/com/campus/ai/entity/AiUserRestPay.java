package com.campus.ai.entity;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class AiUserRestPay {
    private Long stuId;

    private BigDecimal balance;
}