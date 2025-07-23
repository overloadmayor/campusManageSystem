package com.me.mall.service.system;

import com.me.mall.dto.system.DictionaryDto;
import com.me.mall.vo.system.DictionaryDetailVo;

import java.util.List;

public interface DictionaryService {
    //新增字典
    Integer insert(DictionaryDto dto);

    //编辑字典
    Integer update(DictionaryDto dto);

    //查询字典列表
    List<DictionaryDetailVo> list(String code);
}
