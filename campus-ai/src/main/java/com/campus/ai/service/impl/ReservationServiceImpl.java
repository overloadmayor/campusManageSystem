package com.campus.ai.service.impl;


import com.campus.ai.entity.Reservation;
import com.campus.ai.exception.AIException;
import com.campus.ai.mapper.ReservationMapper;
import com.campus.ai.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    //1.添加预约信息的方法
    @Override
    public void insert(Reservation reservation) {
        //1.判断预约信息是否存在
        if(reservationMapper.findByPhone(reservation.getPhone())!=null){
            //1.1如果存在,抛出异常
            throw new AIException("手机号已存在，请勿重复预约");
        }
        //2.添加预约信息
        reservationMapper.insert(reservation);
    }

    //2.查询预约信息的方法(根据手机号查询)
    @Override
    public Reservation findByPhone(String phone) {
        //1.根据手机号查询预约信息
        Reservation reservation = reservationMapper.findByPhone(phone);
        //2.判断预约信息是否存在
        if(reservation==null){
            //2.1如果不存在,抛出异常
            throw new AIException("预约信息不存在");
        }
        //2.2如果存在,返回预约信息
        return reservation;
    }
}
