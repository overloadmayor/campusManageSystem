package com.me.mall.controller.company;

import com.me.mall.common.api.CommonResult;
import com.me.mall.dto.company.CompanyDto;
import com.me.mall.dto.company.CompanyUserDto;
import com.me.mall.security.vo.CompanyUserVo;
import com.me.mall.service.company.CompanyUserService;
import com.me.mall.vo.company.CompanyUserAllVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 企业用户控制层
 */
@RestController
@Api(value = "企业用户")
public class CompanyUserController {

    @Resource
    private CompanyUserService companyUserService;

    @PostMapping("/companyUser/login")
    @ApiOperation(value = "用户登录接口")
    @ResponseBody
    public CommonResult<String> login(@RequestBody CompanyUserDto dto){
        return CommonResult.success(companyUserService.login(dto));
    }

    @GetMapping("/companyUser/getCurrentUser")
    @ApiOperation(value = "获取当前登陆用户信息接口")
    @ResponseBody
    public CommonResult<CompanyUserVo> getCurrentUser(){
        return CommonResult.success(companyUserService.getCurrentUser());
    }

    @GetMapping("/companyUser/getCurrentUserAll")
    @ApiOperation(value = "获取当前登陆 用户信息+企业信息+... 接口")
    @ResponseBody
    public CommonResult<CompanyUserAllVo> getCurrentUserAll(){
        return CommonResult.success(companyUserService.getCurrentUserAll());
    }

    @PostMapping("/companyUser/insert")
    @ApiOperation(value = "企业注册接口")
    @ResponseBody
    public CommonResult<Integer> insert(@RequestBody CompanyDto dto){
        return CommonResult.success(companyUserService.insert(dto));
    }
}
