package com.campus.ai.service;


import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(

        //手动配置
        wiringMode = AiServiceWiringMode.EXPLICIT,
        //注入模型
        chatModel = "openAiChatModel",
        //注入流式模型
        streamingChatModel = "openAiStreamingChatModel",
        //配置会话记忆
//        chatMemory = "chatMemory"
        //配置会话记忆提供器
        chatMemoryProvider = "chatMemoryProvider",
        //配置向量数据库检索对象
        contentRetriever = "contentRetriever",
        //配置调用工具
        tools = "chatTools"
)
//@AiService
public interface ConsultantService {
//    //用于聊天的方法
//    public String chat(String message);
    //用于流式调用的方法
//    @SystemMessage("你是一名ai志愿咨询助手，解决用户的问题")
//    @UserMessage("你是一名ai志愿咨询助，{{msg}}")
    @SystemMessage(fromResource = "system-prompt.txt")
    public Flux<String> chat(@UserMessage String message,
                             @MemoryId String memoryId);

}
