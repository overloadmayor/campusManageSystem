package com.campus.ai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
//import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatModel;
import org.springframework.ai.zhipuai.ZhiPuAiChatOptions;
import org.springframework.ai.zhipuai.api.ZhiPuAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Autowired
    private JdbcChatMemoryRepository chatMemoryRepository;
    @Autowired
    private ZhiPuAiChatModel zhiPuAiChatModel;

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .maxMessages(10)
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatMemory chatMemory) {

        ChatClient.Builder chatClientBuilder = ChatClient.builder(zhiPuAiChatModel);
        return chatClientBuilder
                .defaultSystem("你是一个聪明的智能助理,名为默心,根据用户提问回答.若你认为用户有让你生成画像的意图,请回复且仅回复'[IG]'")
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultOptions(
                        ZhiPuAiChatOptions.builder()
                        .model(ZhiPuAiApi.ChatModel.GLM_3_Turbo.getValue())
//                        .temperature(0.5)
                        .maxTokens(5000)
                        .build()
                )
                .build();
    }
}
