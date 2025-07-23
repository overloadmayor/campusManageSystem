package com.campus.ai.mapper;

import com.campus.ai.entity.AIChatConversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AiChatConversationMapper {
    List<AIChatConversation> getByUserIdAndConversationId(@Param("stuId") Long user,
                                                          @Param("conversationId") String conversationId);

    int insert(@Param("conversation") AIChatConversation aIChatConversation);

    int updateById(AIChatConversation conversation);
}
