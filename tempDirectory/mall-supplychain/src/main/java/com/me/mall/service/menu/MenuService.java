package com.me.mall.service.menu;

import com.me.mall.dto.company.CompanyDto;
import com.me.mall.dto.menu.MenuDto;
import com.me.mall.vo.menu.MenuVo;

import java.util.List;

public interface MenuService {
    //新增菜单
    Integer insert(MenuDto dto);

    //编辑菜单
    Integer update(MenuDto dto);

    //获取当前用户菜单接口
    List<MenuVo> list();
}
