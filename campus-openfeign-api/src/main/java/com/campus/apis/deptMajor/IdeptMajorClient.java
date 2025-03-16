package com.campus.apis.deptMajor;

import com.campus.apis.config.DefaultFeignConfig;
import com.campus.apis.deptMajor.fallback.IDeptMajorClientFallbackFactory;
import com.campus.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;


@FeignClient(value="campus-deptMajor-service",fallbackFactory =
        IDeptMajorClientFallbackFactory.class,
        configuration =
        DefaultFeignConfig.class)
public interface IdeptMajorClient {
    @GetMapping("/dept/deptMajor")
    public ResponseResult getDeptMajor();
}
