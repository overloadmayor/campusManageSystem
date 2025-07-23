package com.me.mall.dao.customer;

import com.me.mall.dto.customer.CustomerLinkmanDto;
import com.me.mall.vo.customer.CustomerLinkManVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerLinkmanDao {
    List<CustomerLinkManVo> getCustomerLinkmanInfoByFilter(@Param("companyId") Long companyId,
                                                           @Param("customerId") Long customerId,
                                                           @Param("name") String linkmanName,
                                                           @Param("delete") Integer delete);

    Integer updateCustomerLinkmanInfo(CustomerLinkmanDto dto);

    Integer deleteCustomerLinkmanInfo(@Param("id") Long id, @Param("delete") Integer i);
}
