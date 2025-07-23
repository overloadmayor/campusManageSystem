package com.me.mall.dao.customer;

import com.me.mall.dto.customer.CustomerDto;
import com.me.mall.dto.customer.CustomerSelectDto;
import com.me.mall.model.Customer;
import com.me.mall.vo.customer.CustomerListVo;
import com.me.mall.vo.customer.CustomerVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDao {

    CustomerVo getCustomerInfoById(@Param("id")Long id);


    List<CustomerListVo> getCustomerInfoByFilter(@Param("code") String code,
                                                 @Param("name") String name,
                                                 @Param("createUserId") Long createUserId);

    int updateCustomer(CustomerDto updateDto);

    int deleteCustomer(@Param("id") Long id);
}
