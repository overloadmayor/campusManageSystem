package com.campus.apis.deptMajor.fallback;

import com.campus.apis.deptMajor.IdeptMajorClient;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import feign.hystrix.FallbackFactory;

public class IDeptMajorClientFallbackFactory implements FallbackFactory<IdeptMajorClient> {
    @Override
    public IdeptMajorClient create(Throwable cause) {
        return new IdeptMajorClient() {
            @Override
            public ResponseResult getDeptMajor() {
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取系别结构失败");
            }
        };
    }
}
