package com.campus.apis.course.fallback;

import com.campus.apis.course.ICourseClient;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


public class ICourseClientFallbackFactory implements FallbackFactory<ICourseClient> {
    @Override
    public ICourseClient create(Throwable cause) {
        return new ICourseClient() {
            @Override
            public ResponseResult getCourseById(Long id) {
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取课程失败");
            }

            @Override
            public ResponseResult getLessonTimes(List<Long> ids) {
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取课程时间失败");
            }
        };
    }
}
