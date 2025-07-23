package com.me.mall.controller.customer;

import com.me.mall.common.api.CommonPage;
import com.me.mall.common.api.CommonResult;
import com.me.mall.dto.customer.CustomerDto;
import com.me.mall.dto.customer.CustomerSelectDto;
import com.me.mall.model.Customer;
import com.me.mall.vo.customer.CustomerVo;
import com.me.mall.service.customer.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 企业客户控制层
 */
@RestController
@Api(value = "企业客户")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/customer/getCustomerInfoById")
    @ApiOperation("获取客户信息")
    @ResponseBody
    public CommonResult<CustomerVo> getCustomerInfoById(@RequestParam(value = "id") Long id){
        return CommonResult.success(customerService.getCustomerInfoById(id));
    }

    @GetMapping(value = "/customer/getCustomerInfoByFilter")
    @ApiOperation("通过过滤器分页查询客户信息")
    public CommonResult getCustomerInfoByFilter(CustomerSelectDto selectDto){
        return CommonResult.success(CommonPage.restPage(customerService.getCustomerInfoByFilter(selectDto)));
    }

    @PostMapping(value ="/customer/updateCustomerInfo")
    @ApiOperation("更新客户信息")
    public CommonResult<Integer> updateCustomerInfo(@RequestBody CustomerDto updateDto){
        return CommonResult.success(customerService.updateCustomerInfo(updateDto));
    }

    @GetMapping(value="/customer/deleteCustomerInfo")
    @ApiOperation("删除客户信息")
    public CommonResult<Integer> deleteCustomerInfo(@RequestParam(value = "id") Long id){
        return CommonResult.success(customerService.deleteCustomerInfo(id));
    }
}
