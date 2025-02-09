package com.campus.apis.user;

import com.campus.apis.config.DefaultFeignConfig;
import com.campus.apis.teacher.fallback.ITeacherClientFallbackFactory;
import com.campus.apis.user.fallback.IUserClientFallbackFactory;
import com.campus.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="campus-user-service",fallbackFactory = IUserClientFallbackFactory.class,
        configuration =
                DefaultFeignConfig.class)
public interface IUserClient {
    @GetMapping("/user/getInfo")
    public ResponseResult getInfo(@RequestParam(required = false) Long StuId);
}
