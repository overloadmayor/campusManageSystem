package com.campus.ai.config;

import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import redis.clients.jedis.JedisPooled;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class CommonConfig {

    @Autowired
    private EmbeddingModel embeddingModel;
    @Autowired
    private ChatMemoryStore redisChatMemoryStore;
    @Autowired
    private RedisEmbeddingStore redisEmbeddingStore;

    //    @Bean
//    public ConsultantService consultantService() {
//        ConsultantService service = AiServices.builder(ConsultantService.class)
//                .chatModel(model)
//                .build();
//        return service;
//    }
    //构建会话记忆对象
    @Bean
    public ChatMemory chatMemory() {
        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
        return memory;
    }

//    @Bean
//    public OpenAiImageModel imageModel() {
//        return OpenAiImageModel.builder()
//                //获取环境变量
//                .apiKey(System.getenv("AI_DASHSCOPE_API_KEY"))
//                .modelName("qwen-image")
//                .baseUrl(" https://dashscope.aliyuncs.com/compatible-mode/v1")
//                .logRequests(true)
//                .logResponses(true)
//                .build();
//    }

    //构建会话记忆提供对象
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return new ChatMemoryProvider() {
            @Override
            public ChatMemory get(Object memoryId) {
                return MessageWindowChatMemory
                        .builder()
                        .id(memoryId)
                        .maxMessages(20)
                        .chatMemoryStore(redisChatMemoryStore)
                        .build();
            }
        };
    }

    //构建向量数据库操作对象
//    @Bean
    public EmbeddingStore store() {
        //加载文档进内存
//        List<Document> documents = ClassPathDocumentLoader.loadDocuments("content");
//        List<Document> documents = ClassPathDocumentLoader.loadDocuments("content");
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("content",
                new ApachePdfBoxDocumentParser());
        //构建向量数据库操作对象 内存的向量数据库
//        InMemoryEmbeddingStore redisEmbeddingStore = new InMemoryEmbeddingStore();
        //构建文档分割器对象
        DocumentSplitter ds = DocumentSplitters.recursive(500, 100);

        //文本切割，向量化，存储到向量数据库
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(redisEmbeddingStore)
                .documentSplitter(ds)
                .embeddingModel(embeddingModel)
                .build();
        embeddingStoreIngestor.ingest(documents);
        return redisEmbeddingStore;
    }

    //构建向量数据库检索对象
    @Bean
    public ContentRetriever contentRetriever(/*@Autowired EmbeddingStore store*/) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(redisEmbeddingStore)
                .embeddingModel(embeddingModel)
                .minScore(0.5)
                .maxResults(5)
                .build();
    }
}