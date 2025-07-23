package com.me.mall.dto.system;

import com.me.mall.model.SystemDictionary;
import com.me.mall.model.SystemDictionaryDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DictionaryDto {

    @ApiModelProperty(value = "字典主表")
    private SystemDictionary dictionary;

    @ApiModelProperty(value = "字典明细")
    private List<SystemDictionaryDetail> dictionaryDetailList;
}
