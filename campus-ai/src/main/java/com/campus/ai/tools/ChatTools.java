package com.campus.ai.tools;

import com.campus.ai.entity.Reservation;
import com.campus.ai.service.AIService;
import com.campus.ai.service.ReservationService;
import dev.langchain4j.agent.tool.P;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ChatTools {
    @Autowired
    private ReservationService reservationService;

    //1.工具方法：添加预约信息
    @Tool("预约志愿填报服务,用户可以通过该服务预约志愿填报")
    public void addReservation(
            @P("考生姓名") String name,
            @P("考生性别") String gender,
            @P("考生手机号") String phone,
            @P("预约沟通时间，格式为:yyyy-MM-dd HH:mm:ss") String communicationTime,
            @P("考生所在省份") String province,
            @P("考生预估分数") Integer estimatedScore
    ) {
        Reservation reservation = new Reservation();
        reservation.setName(name);
        reservation.setPhone(phone);
        reservation.setGender(gender);
        //转换日期格式
        LocalDateTime localDateTime = LocalDateTime.parse(communicationTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        reservation.setCommunicationTime(localDateTime);
        reservation.setProvince(province);
        reservation.setEstimatedScore(estimatedScore);
        reservationService.insert(reservation);
    }

    //2.工具方法：查询预约信息
    //2.工具方法：查询预约信息
    @Tool("根据考生手机号查询预约单")
    public Reservation findReservation(@P("考生手机号") String phone){
        return reservationService.findByPhone(phone);
    }

}
