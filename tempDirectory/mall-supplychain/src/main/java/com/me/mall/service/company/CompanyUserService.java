package com.me.mall.service.company;

import com.me.mall.dto.company.CompanyDto;
import com.me.mall.dto.company.CompanyUserDto;
import com.me.mall.security.vo.CompanyUserVo;
import com.me.mall.vo.company.CompanyUserAllVo;

public interface CompanyUserService {

    //用户登录
    String login(CompanyUserDto dto);

    //获取当前登陆用户信息
    CompanyUserVo getCurrentUser();

    //"获取当前登陆 用户信息+企业信息+...
    CompanyUserAllVo getCurrentUserAll();

    //企业注册
    Integer insert(CompanyDto dto);
}
