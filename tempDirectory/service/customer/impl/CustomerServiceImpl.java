package com.me.mall.service.customer.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.me.mall.common.api.CommonPage;
import com.me.mall.common.api.CommonResult;
import com.me.mall.dao.customer.CustomerDao;
import com.me.mall.dto.customer.CustomerDto;
import com.me.mall.dto.customer.CustomerSelectDto;

import com.me.mall.mapper.CustomerMapper;
import com.me.mall.vo.customer.CustomerListVo;
import com.me.mall.vo.customer.CustomerVo;
import com.me.mall.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 根据id查询客户信息
     * @param id
     * @return
     */
    @Override
    public CustomerVo getCustomerInfoById(Long id) {
        CustomerVo customerInfo = customerDao.getCustomerInfoById(id);
        return customerInfo;
    }

    /**
     * 根据编码，名称，创建人查询客户信息
     * @param selectDto
     * @return
     */
    @Override
    public CommonResult getCustomerInfoByFilter(CustomerSelectDto selectDto) {
//        CompanyUserVo user = companyUserService.getCurrentUser();
        //参数校验
        if(!selectDto.checkParam()){
            return CommonResult.failed("参数异常");
        }
        PageHelper.startPage(selectDto.getPage(),selectDto.getPageSize());

        Page page = (Page) customerDao.getCustomerInfoByFilter(selectDto.getCode(),
                selectDto.getName(),
                selectDto.getCreateUserId());
        List<CustomerListVo> result = page.getResult();
        return CommonResult.success(CommonPage.restPage(result));

    }

    @Override
    public CommonResult<Integer> updateCustomerInfo(CustomerDto updateDto) {
        Integer res = customerDao.updateCustomer(updateDto);
        return CommonResult.success(res);
    }

    @Override
    public CommonResult<Integer> deleteCustomerInfo(Long id) {
        Integer res = customerDao.deleteCustomer(id);
        return CommonResult.success(res);
    }


}
