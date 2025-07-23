package com.campus.ai.mapper;

import com.campus.ai.entity.AiUserRestPay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AiUserRestPayMapper {

    AiUserRestPay getByStuId(@Param("stuId") Long stuId);

    Integer updateByStuId(AiUserRestPay newBalance);

    Integer minusBalanceByStuId(Long stuId);
}
