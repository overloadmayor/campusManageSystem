package com.me.mall.controller.customer;

import com.me.mall.common.api.CommonPage;
import com.me.mall.common.api.CommonResult;
import com.me.mall.dto.customer.CustomerLinkManSelectDto;
import com.me.mall.dto.customer.CustomerLinkmanDto;
import com.me.mall.service.customer.CustomerLinkmanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "客户联系人")
public class CustomerLinkmanController {
    @Autowired
    private CustomerLinkmanService customerLinkmanService;

    @GetMapping(value = "/customerLinkman/getCustomerLinkmanInfo")
    @ApiOperation("分页过滤获取客户联系人信息")
    @ResponseBody
    public CommonResult getCustomerLinkManInfo(@Validated CustomerLinkManSelectDto dto) {
        return CommonResult.success(CommonPage.restPage(customerLinkmanService.getCustomerLinkManInfoByFilter(dto)));
    }

    @PostMapping(value = "/customerLinkman/updateCustomerLinkmanInfo")
    @ApiOperation("更新客户联系人信息")
    public CommonResult<Integer> updateCustomerInfo(@RequestBody CustomerLinkmanDto dto) {
        return CommonResult.success(customerLinkmanService.updateCustomeLinkmanInfo(dto));
    }

    @PostMapping(value = "/customerLinkman/addCustomerLinkmanInfo")
    @ApiOperation("新增客户联系人信息")
    public CommonResult<Integer> insertCustomerInfo(@RequestBody CustomerLinkmanDto dto) {
        return CommonResult.success(customerLinkmanService.inertCustomeLinkmanInfo(dto));
    }

    @GetMapping(value = "/customerLinkman/deleteCustomerLinkmanInfo")
    @ApiOperation("删除客户联系人信息")
    public CommonResult<Integer> deleteCustomerLinkmanInfo(@RequestParam(value = "id") Long id) {
        return CommonResult.success(customerLinkmanService.deleteCustomerLinkmanInfo(id));
    }

}
