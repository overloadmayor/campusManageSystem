package com.campus.apis.teacher;

import com.campus.apis.config.DefaultFeignConfig;
import com.campus.apis.course.fallback.ICourseClientFallbackFactory;
import com.campus.apis.teacher.fallback.ITeacherClientFallbackFactory;
import com.campus.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value="campus-teacher-service",fallbackFactory = ITeacherClientFallbackFactory.class,
        configuration =
        DefaultFeignConfig.class)
public interface ITeacherClient {
    @GetMapping("/teacher/name/{id}")
    public ResponseResult getTeacher(@PathVariable Long id);
}
