package com.campus.userservice.listener;


import com.campus.common.constants.UserConstants;

import com.campus.model.selectLesson.pojos.SelectLesson;
import com.campus.model.user.pojos.SendMQPojos;
import com.campus.userservice.service.IStudentsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringRabbitListener {
    @Autowired
    private IStudentsService studentsService;
    /**
     * 收到批量导入学生信息申请
     *
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="batch.queue",durable = "true"),
            exchange = @Exchange(name= UserConstants.USER_BATCH_INSERT_EXCHANGE,type=
                    ExchangeTypes.TOPIC),
            key={UserConstants.USER_BATCH_INSERT_ROUTINGKEY}
    ))
    public void listenSelectLessonMessage(SendMQPojos pojo) {
        studentsService.batchInsert(pojo);
    }
}