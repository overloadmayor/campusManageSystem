package com.me.mall.service.system;

import com.me.mall.common.api.CommonResult;
import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    CommonResult uploadOss(MultipartFile file, String dir);
}
