package com.campus.ai.mapper;

import com.campus.ai.entity.AiChatDetailConversation;

import java.util.List;

public interface AiChatDetailConversationMapper {

    /**
     * 新增对话记录
     * @param conversion 对话记录实体
     * @return 影响行数
     */
    int insert(AiChatDetailConversation conversion);

    /**
     * 逻辑删除对话记录
     * @param memoryId 记录ID
     * @return 影响行数
     */
    int updateToDeleted(String memoryId);

    /**
     * 更新对话记录（仅更新未删除的数据）
     * @param conversion 对话记录实体
     * @return 影响行数
     */
    int insertOrUpdate(AiChatDetailConversation conversion);

    /**
     * 根据ID查询对话记录（仅查询未删除的数据）
     * @param memoryId 记录ID
     * @return 对话记录实体
     */
    AiChatDetailConversation selectByMemoryId(String memoryId);

    /**
     * 查询所有未删除的对话记录
     * @return 对话记录列表
     */
    List<AiChatDetailConversation> selectAll();

    /**
     * 查询所有对话记录（包括已删除的）
     * @return 对话记录列表
     */
    List<AiChatDetailConversation> selectAllWithDeleted();
}
    