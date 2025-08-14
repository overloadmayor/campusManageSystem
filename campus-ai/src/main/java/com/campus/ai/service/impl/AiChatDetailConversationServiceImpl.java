package com.campus.ai.service.impl;

import com.campus.ai.entity.AiChatDetailConversation;
import com.campus.ai.mapper.AiChatDetailConversationMapper;
import com.campus.ai.service.AiChatDetailConversationService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AiChatDetailConversationServiceImpl implements AiChatDetailConversationService {

    @Resource
    private AiChatDetailConversationMapper conversionMapper;

    @Override
    public boolean addConversion(AiChatDetailConversation conversion) {
        conversion.setDelete(0);
        if (conversion.getCreateTime() == null) {
            conversion.setCreateTime(LocalDateTime.now());
        }
        return conversionMapper.insert(conversion) > 0;
    }

    @Override
    public boolean deleteConversion(String memoryId) {
        return conversionMapper.updateToDeleted(memoryId) > 0;
    }

    @Override
    public boolean updateConversion(AiChatDetailConversation conversion) {
        return conversionMapper.insertOrUpdate(conversion) > 0;
    }

    @Override
    public AiChatDetailConversation getConversionByMemoryId(String memoryId) {
        return conversionMapper.selectByMemoryId(memoryId);
    }

    @Override
    public List<AiChatDetailConversation> getAllConversions() {
        return conversionMapper.selectAll();
    }

    @Override
    public List<AiChatDetailConversation> getAllWithDeleted() {
        return conversionMapper.selectAllWithDeleted();
    }
}
    