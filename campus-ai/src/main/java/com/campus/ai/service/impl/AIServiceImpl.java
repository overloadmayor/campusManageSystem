package com.campus.ai.service.impl;

import com.campus.ai.common.dtos.ResponseResult;
import com.campus.ai.common.enums.AppHttpCodeEnum;
import com.campus.ai.entity.AIChatConversation;
import com.campus.ai.entity.AiChatDetailConversation;
import com.campus.ai.entity.AiUserRestPay;
import com.campus.ai.exception.AIException;
import com.campus.ai.mapper.AiChatConversationMapper;
import com.campus.ai.mapper.AiUserRestPayMapper;
import com.campus.ai.service.AIService;
import com.campus.ai.service.ConsultantService;

import com.campus.ai.tools.ChatTools;
import com.campus.ai.utils.UserThreadLocalUtil;
import dev.langchain4j.model.openai.OpenAiImageModel;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AiChatConversationMapper aiChatConversationMapper;
    @Autowired
    private AiUserRestPayMapper aiUserRestPayMapper;
    @Autowired
    private ConsultantService consultantService;
    @Autowired
    private OpenAiImageModel openAiImageModel;


    public static final String AICHAT="AICHAT:";

    @Override
    public ResponseResult chatImages(String input) {
        if(input == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"请输入图片描述");
        }

        //扣减余额
        AiUserRestPay stu = aiUserRestPayMapper.getByStuId(UserThreadLocalUtil.getUser());
        if(stu.getBalance().compareTo(BigDecimal.ONE)<0){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"余额不足");
        }
        aiUserRestPayMapper.minusBalanceByStuId(stu.getStuId());

        String url = openAiImageModel.generate(input).content().url().toString();

        return ResponseResult.okResult(url);
    }

    @Override
    public Flux<String> chat(String input, String conversationId) {
        //检查参数
        check_params(input, conversationId);
        //更新数据库
        updateDB(conversationId);
        return consultantService.chat(input,conversationId);

    }

    private void updateDB(String conversationId) {
        //扣减余额
        AiUserRestPay stu = aiUserRestPayMapper.getByStuId(UserThreadLocalUtil.getUser());
        if(stu.getBalance().compareTo(BigDecimal.ONE)<0){
            throw new AIException("余额不足");
        }
        aiUserRestPayMapper.minusBalanceByStuId(stu.getStuId());


        // 从redis中获取会话内容
        Long user = UserThreadLocalUtil.getUser();
        Boolean exist = stringRedisTemplate.opsForSet().isMember(
                AICHAT + user, conversationId
        );

        //查询数据库
        if(!exist){
            List<AIChatConversation> conversationList=
                    aiChatConversationMapper.getByUserIdAndConversationId(user,
                            conversationId);

            if(conversationList==null||conversationList.isEmpty()){
                //插入数据库
                AIChatConversation conversation = new AIChatConversation();
                conversation.setStuId(user);
                conversation.setConversationId(conversationId);
                aiChatConversationMapper.insert(conversation);
            }else{
                //更新时间
                AIChatConversation conversation = conversationList.get(0);
                conversation.setUpdateTime(new Date());
                // 已存在会话，异步更新时间戳
                CompletableFuture.runAsync(() -> updateConversationTime(conversationId));
            }
            // 会话存在，将会话内容放入redis
            stringRedisTemplate.expire(AICHAT + user, 10, TimeUnit.MINUTES);
            stringRedisTemplate.opsForSet().add(AICHAT + user, conversationId);
        }
    }

    private static void check_params(String input, String conversationId) {
        if(input ==null){
            throw new AIException("请输入聊天内容");
        }
        if(conversationId ==null){
            throw new AIException("请输入会话id");
        }
    }

    @Async("asyncExecutor")
    public void updateConversationTime(String conversationId) {
        AIChatConversation conversation = new AIChatConversation();
        conversation.setConversationId(conversationId);
        conversation.setUpdateTime(new Date());
        aiChatConversationMapper.updateById(conversation);
    }
}
