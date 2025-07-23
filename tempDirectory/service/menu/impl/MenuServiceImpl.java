package com.me.mall.service.menu.impl;

import com.github.pagehelper.PageHelper;
import com.me.mall.dao.menu.MenuDao;
import com.me.mall.dto.company.CompanyDto;
import com.me.mall.dto.menu.MenuDto;
import com.me.mall.dto.menu.MenuSelectDto;
import com.me.mall.mapper.MenuMapper;
import com.me.mall.model.Menu;
import com.me.mall.model.MenuExample;
import com.me.mall.security.vo.CompanyUserVo;
import com.me.mall.service.company.CompanyUserService;
import com.me.mall.service.menu.MenuService;
import com.me.mall.vo.menu.MenuVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单服务层
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Resource
    private MenuDao menuDao;

    @Resource
    private CompanyUserService companyUserService;

    /**
     * 新增菜单
     * @param dto 菜单dto入参
     * @return 新增是否成功标识
     */
    @Override
    public Integer insert(MenuDto dto) {
        //1. 校验入参
        //2. 按规则生成最新菜单编码
        //3. 新增数据
        Menu menu = new Menu();
        menu.setMenuCode(this.getMenuCode());
        //4. 返回成功标识

        return null;
    }

    /**
     * @return 返回最新的菜单编码
     */
    private synchronized String getMenuCode() {
        String pre = "MN";
        String result = pre + "00000001";
        MenuExample example = new MenuExample();
        example.createCriteria().andMenuCodeLike(pre + "%");
        example.setOrderByClause("menu_code desc");

        PageHelper.startPage(1, 1);
        List<Menu> bdmMenus = menuMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(bdmMenus)) {
            Menu bdmMenu = bdmMenus.get(0);
            result = bdmMenu.getMenuCode();
            List<Menu> menus;
            while (true) {
                result = result.substring(pre.length());
                result = pre + (String.format("%0" + result.length() + "d", Integer.parseInt(result) + 1));

                example = new MenuExample();
                example.createCriteria().andMenuCodeEqualTo(result);
                menus = menuMapper.selectByExample(example);
                if (!CollectionUtils.isEmpty(menus)) {
                    continue;
                }
                break;
            }
        }
        return result;
    }

    /**
     * 编辑菜单
     * @param dto 菜单dto入参
     * @return 编辑是否成功标识
     */
    @Override
    public Integer update(MenuDto dto) {
        //1. 校验入参
        //2. 编辑数据
        //3. 返回成功标识

        return null;
    }

    /**
     * 获取当前用户菜单接口
     * @return 权限菜单集
     */
    @Override
    public List<MenuVo> list() {
        //1. 获取当前登陆人信息
        CompanyUserVo user = companyUserService.getCurrentUser();
        if (user.getUserId() <= 0){
            return new ArrayList<>();
        }

        //2. 校验是否管理员（用户表的管理员字段）
        //a. 是则返回所有启用菜 b. 否则查看当前企业角色，查询企业角色菜单权限
        List<MenuVo> menuVoList =  menuVoList = list_listMenuVos(user);

        //3. 递归组装菜单层级结构 返回数据
        return list_menuChildren(menuVoList, 0L);
    }

    /**
     * @param user 当前登陆用户
     * @return 返回用户菜单权限
     */
    private List<MenuVo> list_listMenuVos(CompanyUserVo user) {
        List<MenuVo> menuVoList = new ArrayList<>();
        MenuSelectDto menuSelectDto = new MenuSelectDto();
        if (user.getIsSuper() != null && user.getIsSuper() == 1){
            menuVoList =  menuDao.listMenu(menuSelectDto);
        }
        if (user.getIsSuper() == null || user.getIsSuper() != 1){
            menuSelectDto.setRoleIdList(user.getRoleIdList());
            menuVoList =  menuDao.listMenu(menuSelectDto);
        }
        return menuVoList;
    }

    /**
     * 递归菜单
     * @param menuVoList 输入菜单集
     * @param parentId 父ID
     * @return 子菜单
     */
    private List<MenuVo> list_menuChildren(List<MenuVo> menuVoList, Long parentId) {
        List<MenuVo> endList = new ArrayList<>();
        for (MenuVo menu : menuVoList) {
            // 匹配当前父级ID
            if (menu.getParentId().equals(parentId)) {
                // 复制当前菜单对象（避免引用同一对象导致的副作用）
                MenuVo temp = new MenuVo();
                BeanUtils.copyProperties(menu, temp);
                // 递归获取子菜单（以当前菜单ID作为新的父级ID）
                List<MenuVo> children = list_menuChildren(menuVoList, temp.getMenuId());
                temp.setChildren(children); // 设置子菜单
                endList.add(temp);
            }
        }
        return endList;
    }
}
