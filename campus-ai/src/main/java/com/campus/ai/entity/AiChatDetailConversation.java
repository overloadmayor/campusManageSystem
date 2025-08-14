package com.campus.ai.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AiChatDetailConversation {
    private String memoryId;
    private String content;
    private LocalDateTime createTime;
    private Integer delete; // 0-未删除，1-已删除

}