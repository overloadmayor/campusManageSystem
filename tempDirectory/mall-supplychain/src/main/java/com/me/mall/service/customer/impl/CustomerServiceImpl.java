package com.me.mall.service.customer.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.me.mall.common.api.CommonPage;
import com.me.mall.common.api.CommonResult;
import com.me.mall.dao.customer.CustomerDao;
import com.me.mall.dto.customer.CustomerDto;
import com.me.mall.dto.customer.CustomerSelectDto;

import com.me.mall.mapper.CustomerMapper;
import com.me.mall.security.vo.CompanyUserVo;
import com.me.mall.service.company.CompanyUserService;
import com.me.mall.vo.customer.CustomerListVo;
import com.me.mall.vo.customer.CustomerVo;
import com.me.mall.service.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private CompanyUserService companyUserService;

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
    public List<CustomerListVo> getCustomerInfoByFilter(CustomerSelectDto selectDto) {
//        CompanyUserVo user = companyUserService.getCurrentUser();
//        if(user==null){
//            throw new BadCredentialsException("用户状态异常");
//        }
        //参数校验
        if(!selectDto.checkParam()){
            throw new BadCredentialsException("参数异常");
        }
        PageHelper.startPage(selectDto.getPage(),selectDto.getPageSize());

        Page page = (Page) customerDao.getCustomerInfoByFilter(selectDto.getCode(),
                selectDto.getName(),
                selectDto.getCreateUserId(),1);
        List<CustomerListVo> result = page.getResult();
        return result;
    }

    @Override
    public Integer updateCustomerInfo(CustomerDto updateDto) {
        Integer res = customerDao.updateCustomer(updateDto);
        return res;
    }

    @Override
    public Integer deleteCustomerInfo(Long id) {
        Integer res = customerDao.deleteCustomer(id,0);
        return res;
    }


}
