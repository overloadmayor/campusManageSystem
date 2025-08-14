package com.campus.ai.mapper;


import com.campus.ai.entity.Reservation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReservationMapper {

    //1.添加预约信息
    @Insert("insert into reservation(name,gender,phone,communication_time,province,estimated_score) values(#{name},#{gender},#{phone},#{communicationTime},#{province},#{estimatedScore})")
    void insert(Reservation reservation);
    //2.根据手机号查询预约信息
    @Select("select name, gender, phone, communication_time, province, estimated_score from reservation where phone=#{phone}")
    Reservation findByPhone(String phone);

}
