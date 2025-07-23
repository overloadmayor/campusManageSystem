package com.me.mall.dao.menu;

import com.me.mall.dto.menu.MenuSelectDto;
import com.me.mall.vo.menu.MenuVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDao {
    //获取菜单
    List<MenuVo> listMenu(@Param("param") MenuSelectDto param);
}
