package com.me.mall.service.customer;

import com.me.mall.dto.customer.CustomerLinkManSelectDto;
import com.me.mall.dto.customer.CustomerLinkmanDto;
import com.me.mall.vo.customer.CustomerLinkManVo;

import java.util.List;

public interface CustomerLinkmanService {
    List<CustomerLinkManVo> getCustomerLinkManInfoByFilter(CustomerLinkManSelectDto dto);

    Integer updateCustomeLinkmanInfo(CustomerLinkmanDto dto);

    Integer deleteCustomerLinkmanInfo(Long id);

    Integer inertCustomeLinkmanInfo(CustomerLinkmanDto dto);
}
