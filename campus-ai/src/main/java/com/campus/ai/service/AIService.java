package com.campus.ai.service;

import com.campus.ai.common.dtos.ResponseResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

public interface AIService {

    ResponseResult<String> chatImages(String input);

    Flux<String> chat(String input, String conversationId);
}
