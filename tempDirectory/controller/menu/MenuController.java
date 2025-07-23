package com.me.mall.controller.menu;

import com.me.mall.common.api.CommonResult;
import com.me.mall.dto.company.CompanyDto;
import com.me.mall.dto.menu.MenuDto;
import com.me.mall.service.menu.MenuService;
import com.me.mall.vo.menu.MenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单控制层
 */
@RestController
@Api(value = "菜单")
public class MenuController {

    @Resource
    private MenuService menuService;

    @PostMapping("/menu/insert")
    @ApiOperation(value = "新增菜单")
    @ResponseBody
    public CommonResult<Integer> insert(@RequestBody MenuDto dto){
        return CommonResult.success(menuService.insert(dto));
    }

    @PostMapping("/menu/update")
    @ApiOperation(value = "编辑菜单")
    @ResponseBody
    public CommonResult<Integer> update(@RequestBody MenuDto dto){
        return CommonResult.success(menuService.update(dto));
    }

    @GetMapping("/menu/list")
    @ApiOperation(value = "获取当前用户菜单接口")
    @ResponseBody
    public CommonResult<List<MenuVo>> list(){
        return CommonResult.success(menuService.list());
    }
}
