package com.campus.majoranddeptservice.controller;

import com.campus.majoranddeptservice.service.IMajorService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.majorAndDept.pojos.DeptMajor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeptMajorController {
    @Autowired
    private IMajorService majorService;

    @GetMapping("/dept/deptMajor")
    public ResponseResult getDeptMajor() {
        return majorService.listDeptMajor();
    }


}
