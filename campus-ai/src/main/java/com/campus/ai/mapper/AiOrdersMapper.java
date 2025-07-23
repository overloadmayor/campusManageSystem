package com.campus.ai.mapper;

import com.campus.ai.entity.AiOrders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiOrdersMapper {
    Integer insert(@Param("orders") AiOrders aiOrders);

    Integer updateStatusByTradeNo(AiOrders aiOrders);

    AiOrders getByTradeNo(@Param("tradeNo") String outTradeNo);
}
