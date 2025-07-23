package com.campus.ai.entity;

import lombok.Data;

import java.util.Date;

@Data
public class AIChatConversation {
    private Long id;

    private Long stuId;

    private String conversationId;

    private Date createTime;

    private Date updateTime;
}