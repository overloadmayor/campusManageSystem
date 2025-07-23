package com.me.mall.controller.system;

import com.aliyuncs.exceptions.ClientException;
import com.me.mall.common.api.CommonResult;
import com.me.mall.common.util.AliyunOssUtil;
import com.me.mall.service.system.OssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * oss对象存储
 */
@RestController
@Api(tags = "oss对象存储")
public class AliyunOssController {
    @Resource
    private OssService ossService;

    @PostMapping("/oss/upload")
    @ApiOperation(value = "存储文件")
    public CommonResult uploadOss(@RequestPart("imgFile") MultipartFile file,
                                       @RequestParam("dir") String dir) {

        return ossService.uploadOss(file,dir);

    }
}
