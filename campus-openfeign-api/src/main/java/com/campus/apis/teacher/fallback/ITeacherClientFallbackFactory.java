package com.campus.apis.teacher.fallback;


import com.campus.apis.teacher.ITeacherClient;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import org.springframework.cloud.openfeign.FallbackFactory;


public class ITeacherClientFallbackFactory implements FallbackFactory<ITeacherClient> {
    @Override
    public ITeacherClient create(Throwable cause) {
        return new ITeacherClient() {
            @Override
            public ResponseResult getTeacher(Long id) {
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取教师失败");
            }

        };
    }
}
