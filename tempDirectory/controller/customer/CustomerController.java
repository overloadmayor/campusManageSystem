package com.me.mall.controller.customer;

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
@RequestMapping(value = "/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/getCustomerInfoById")
    @ApiOperation("获取客户信息")
    @ResponseBody
    public CommonResult<CustomerVo> getCustomerInfoById(@RequestParam(value = "id") Long id){
        return CommonResult.success(customerService.getCustomerInfoById(id));
    }

    @GetMapping(value = "/getCustomerInfoByFilter")
    @ApiOperation("通过过滤器分页查询客户信息")
    public CommonResult getCustomerInfoByFilter(CustomerSelectDto selectDto){
        CommonResult customerInfoByFilter = customerService.getCustomerInfoByFilter(selectDto);
        return customerInfoByFilter;
    }

    @PostMapping(value ="/updateCustomerInfo")
    @ApiOperation("更新客户信息")
    public CommonResult<Integer> updateCustomerInfo(@RequestBody CustomerDto updateDto){
        return customerService.updateCustomerInfo(updateDto);
    }

    @GetMapping(value="/deleteCustomerInfo")
    @ApiOperation("删除客户信息")
    public CommonResult<Integer> deleteCustomerInfo(@RequestParam(value = "id") Long id){
        return customerService.deleteCustomerInfo(id);
    }
}
