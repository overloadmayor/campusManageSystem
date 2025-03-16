package com.campus.userservice.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.campus.common.constants.UserConstants;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.userservice.service.IStudentsService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class FileUploadController {

    @Autowired
    private IStudentsService studentsService;

    @PostMapping("uploadStuInfos")
    public ResponseResult handleFileUpload(HttpServletRequest request) throws IOException {
        return studentsService.handleFileUpload(request);
    }

    @GetMapping("uploadDetailInfos")
    public ResponseResult checkUploadDetailInfos(){
        return studentsService.checkUploadDetailInfos();
    }
}