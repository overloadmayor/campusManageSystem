package com.me.mall.service.system.impl;

import com.aliyuncs.exceptions.ClientException;
import com.me.mall.common.api.CommonResult;
import com.me.mall.common.util.AliyunOssUtil;
import com.me.mall.service.system.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class OssServiceImpl implements OssService {
    /**
     * 上传文件到oss
     * @param file 文件
     * @param dir 文件路径
     * @return
     */
    @Override
    public CommonResult uploadOss(MultipartFile file, String dir) {
        String restorePath=null;
        try {
            restorePath=AliyunOssUtil.uploadOss(file,dir);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.failed("上传oss失败");
        }
        return CommonResult.success(restorePath);
    }
}
