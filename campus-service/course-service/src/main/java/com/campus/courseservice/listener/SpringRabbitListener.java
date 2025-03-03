package com.campus.courseservice.listener;

import com.alibaba.fastjson.JSON;
import com.campus.common.constants.CourseConstants;
import com.campus.courseservice.service.ISelectLessonService;
import com.campus.courseservice.service.impl.SelectLessonServiceImpl;
import com.campus.courseservice.websocket.WebSocketServer;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.selectLesson.pojos.SelectLesson;
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
    private WebSocketServer webSocketServer;
    @Autowired
    private ISelectLessonService selectLessonService;
    /**
     * 收到添加课程申请
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="course.queue1",durable = "true"),
            exchange = @Exchange(name=CourseConstants.ADD_COURSE_EXCHANGE,type= ExchangeTypes.TOPIC),
            key={CourseConstants.ADD_COURSE_ROUTINGKEY}
    ))
    public void listenCourseMessage(String message) {
        String jsonString = JSON.toJSONString(ResponseResult.okResult(1,null));
        webSocketServer.sendToAllClient(jsonString);
    }

    /**
     * 收到添加课次申请
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="lesson.queue1",durable = "true"),
            exchange = @Exchange(name=CourseConstants.ADD_LESSON_EXCHANGE,type=
                    ExchangeTypes.TOPIC),
            key={CourseConstants.ADD_LESSON_ROUTINGKEY}
    ))
    public void listenLessonMessage(String message) {
        String jsonString = JSON.toJSONString(ResponseResult.okResult(2,null));
        webSocketServer.sendToAllClient(jsonString);
    }

    /**
     * 收到选课申请
     *
     */
    @RabbitListener(bindings = @QueueBinding(
            value=@Queue(name="selectLesson.queue1",durable = "true"),
            exchange = @Exchange(name=CourseConstants.SELECT_LESSON_EXCHANGE,type=
                    ExchangeTypes.TOPIC),
            key={CourseConstants.SELECT_LESSON_ROUTINGKEY}
    ))
    public void listenSelectLessonMessage(SelectLesson selectLesson) {
        System.out.println("listenSelectLessonMessage");
        selectLessonService.handleSelectLesson(selectLesson);
    }
}