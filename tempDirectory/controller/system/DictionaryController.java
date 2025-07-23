package com.me.mall.controller.system;

import com.me.mall.common.api.CommonPage;
import com.me.mall.common.api.CommonResult;
import com.me.mall.dto.system.DictionaryDto;
import com.me.mall.service.system.DictionaryService;
import com.me.mall.vo.system.DictionaryDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典
 */
@RestController
@Api(tags = "字典")
public class DictionaryController {

    @Resource
    private DictionaryService dictionaryService;

    @PostMapping("/dictionary/insert")
    @ApiOperation(value = "新增字典")
    @ResponseBody
    public CommonResult<Integer> insert(@RequestBody DictionaryDto dto){
        return CommonResult.success(dictionaryService.insert(dto));
    }

    @PostMapping("/dictionary/update")
    @ApiOperation(value = "编辑字典")
    @ResponseBody
    public CommonResult<Integer> update(@RequestBody DictionaryDto dto){
        return CommonResult.success(dictionaryService.update(dto));
    }

    @GetMapping(value = "/dictionary/list")
    @ApiOperation("查询字典列表")
    @ResponseBody
    public CommonResult<List<DictionaryDetailVo>> list(@RequestParam(value = "code") String code){
        return CommonResult.success(dictionaryService.list(code));
    }

}
