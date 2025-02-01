package com.campus.apis.course;

import com.campus.apis.config.DefaultFeignConfig;

import com.campus.apis.course.fallback.ICourseClientFallbackFactory;
import com.campus.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="campus-course-service",fallbackFactory = ICourseClientFallbackFactory.class,configuration =
        DefaultFeignConfig.class)
public interface ICourseClient {
    @GetMapping("/course/{id}")
    public ResponseResult getCourseById(@PathVariable("id") Long id);
}
