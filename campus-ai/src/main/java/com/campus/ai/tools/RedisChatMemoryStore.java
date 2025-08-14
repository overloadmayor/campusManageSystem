package com.campus.ai.tools;

import com.campus.ai.entity.AiChatDetailConversation;
import com.campus.ai.service.AiChatDetailConversationService;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class RedisChatMemoryStore implements ChatMemoryStore {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AiChatDetailConversationService aiChatDetailConversationService;


    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String json = stringRedisTemplate.opsForValue().get(memoryId.toString());
        if(json==null){
            AiChatDetailConversation conversationion =
                    aiChatDetailConversationService.getConversionByMemoryId(memoryId.toString());
            if(conversationion!=null){
                json=conversationion.getContent();
                stringRedisTemplate.opsForValue().set(memoryId.toString(), json,
                        Duration.ofDays(1));
            }
        }
        return ChatMessageDeserializer.messagesFromJson(json);
    }

    @Override
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String json = ChatMessageSerializer.messagesToJson(messages);
        stringRedisTemplate.opsForValue().set(memoryId.toString(), json,
                Duration.ofDays(1));
        AiChatDetailConversation aiChatDetailConversation = new AiChatDetailConversation();
        aiChatDetailConversation.setMemoryId(memoryId.toString());
        aiChatDetailConversation.setContent(json);
        // 异步更新数据库
        asyncUpdateConversation(aiChatDetailConversation);
    }

    @Override
    public void deleteMessages(Object memoryId) {
        stringRedisTemplate.delete(memoryId.toString());
        // 异步删除数据库中的数据
        asyncDeleteConversation(memoryId.toString());
    }

    /**
     * 异步更新对话记录
     */
    @Async("asyncExecutor")
    protected void asyncUpdateConversation(AiChatDetailConversation conversation) {
        aiChatDetailConversationService.updateConversion(conversation);
        CompletableFuture.completedFuture(null);
    }

    /**
     * 异步删除对话记录
     */
    @Async("asyncExecutor")
    protected void asyncDeleteConversation(String memoryId) {
        aiChatDetailConversationService.deleteConversion(memoryId);
        CompletableFuture.completedFuture(null);
    }
}
