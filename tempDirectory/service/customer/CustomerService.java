package com.me.mall.service.customer;

import com.me.mall.common.api.CommonResult;
import com.me.mall.dto.customer.CustomerDto;
import com.me.mall.dto.customer.CustomerSelectDto;
import com.me.mall.model.Customer;
import com.me.mall.vo.customer.CustomerVo;

import java.util.List;

public interface CustomerService {

    CustomerVo getCustomerInfoById(Long id);

    CommonResult getCustomerInfoByFilter(CustomerSelectDto selectDto);

    CommonResult<Integer> updateCustomerInfo(CustomerDto updateDto);

    CommonResult<Integer> deleteCustomerInfo(Long id);
}
