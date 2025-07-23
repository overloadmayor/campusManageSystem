package com.me.mall.service.system.impl;

import com.me.mall.dto.system.DictionaryDto;
import com.me.mall.mapper.SystemDictionaryDetailMapper;
import com.me.mall.mapper.SystemDictionaryMapper;
import com.me.mall.model.SystemDictionary;
import com.me.mall.model.SystemDictionaryDetail;
import com.me.mall.model.SystemDictionaryDetailExample;
import com.me.mall.model.SystemDictionaryExample;
import com.me.mall.service.system.DictionaryService;
import com.me.mall.vo.system.DictionaryDetailVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DictionaryServiceImpl implements DictionaryService {

    @Resource
    private SystemDictionaryMapper systemDictionaryMapper;

    @Resource
    private SystemDictionaryDetailMapper systemDictionaryDetailMapper;

    /**
     * 新增字典
     */
    @Override
    public Integer insert(DictionaryDto dto) {
        return 0;
    }

    /**
     * 编辑字典
     */
    @Override
    public Integer update(DictionaryDto dto) {
        return 0;
    }

    /**
     * @param code 字典编码
     * @return 字典列表
     */
    @Override
    public List<DictionaryDetailVo> list(String code) {
        if (ObjectUtils.isEmpty(code)){
            return Collections.emptyList();
        }

        //字典主表
        SystemDictionaryExample systemDictionaryExample = new SystemDictionaryExample();
        systemDictionaryExample.createCriteria().andCodeEqualTo(code);
        List<SystemDictionary> dictionaryList = systemDictionaryMapper.selectByExample(systemDictionaryExample);
        if (dictionaryList.isEmpty()){
            return Collections.emptyList();
        }
        SystemDictionary dictionary = dictionaryList.get(0);

        //字典明细
        SystemDictionaryDetailExample detailExample = new SystemDictionaryDetailExample();
        detailExample.createCriteria().andDictIdEqualTo(dictionary.getId());
        List<SystemDictionaryDetail> detailList = systemDictionaryDetailMapper.selectByExample(detailExample);
        if (detailList.isEmpty()){
            return Collections.emptyList();
        }

        //组装数据
        List<DictionaryDetailVo> resultList = new ArrayList<>();
        detailList.forEach(
                detail -> {
                    DictionaryDetailVo detailVo = new DictionaryDetailVo();
                    BeanUtils.copyProperties(detail, detailVo);
                    resultList.add(detailVo);
                }
        );
        return resultList;
    }
}
