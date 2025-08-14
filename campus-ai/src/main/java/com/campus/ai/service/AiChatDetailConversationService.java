package com.campus.ai.service;

import com.campus.ai.entity.AiChatDetailConversation;

import java.util.List;

public interface AiChatDetailConversationService {

    /**
     * 新增对话记录
     * @param conversion 对话记录实体
     * @return 操作结果（true成功，false失败）
     */
    boolean addConversion(AiChatDetailConversation conversion);

    /**
     * 逻辑删除对话记录
     * @param memoryId 记录ID
     * @return 操作结果（true成功，false失败）
     */
    boolean deleteConversion(String memoryId);

    /**
     * 更新对话记录
     * @param conversion 对话记录实体
     * @return 操作结果（true成功，false失败）
     */
    boolean updateConversion(AiChatDetailConversation conversion);

    /**
     * 根据ID查询对话记录
     * @param memoryId 记录ID
     * @return 对话记录实体
     */
    AiChatDetailConversation getConversionByMemoryId(String memoryId);

    /**
     * 查询所有未删除的对话记录
     * @return 对话记录列表
     */
    List<AiChatDetailConversation> getAllConversions();

    /**
     * 查询所有对话记录（包括已删除的）
     * @return 对话记录列表
     */
    List<AiChatDetailConversation> getAllWithDeleted();
}
    