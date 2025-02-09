package com.campus.apis.user.fallback;



import com.campus.apis.user.IUserClient;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import org.springframework.cloud.openfeign.FallbackFactory;


public class IUserClientFallbackFactory implements FallbackFactory<IUserClient> {
    @Override
    public IUserClient create(Throwable cause) {
        return new IUserClient() {
            @Override
            public ResponseResult getInfo(Long StuId) {
                return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,"获取学生信息失败");
            }

        };
    }
}
