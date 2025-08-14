package com.campus.ai.controller;


import com.campus.ai.common.dtos.ResponseResult;
import com.campus.ai.service.AIService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ai")
public class AIController {
    @Autowired
    private AIService aiService;

    /**
     * ChatClient 简单调用
     */
    @GetMapping(value = "/chat",produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam(value = "input", defaultValue = "hello") String input,
                       @RequestParam(value = "conversationId") String conversationId) {
        return aiService.chat(input,conversationId);
    }

    /**
     * ChatClient 图像生成
     */
    @GetMapping("/images")
    public ResponseResult chatImages(@RequestParam(value = "input") String input) {
        return aiService.chatImages(input);
    }


}
