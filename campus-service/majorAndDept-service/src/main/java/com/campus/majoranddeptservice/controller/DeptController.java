package com.campus.majoranddeptservice.controller;

import com.campus.majoranddeptservice.service.IDeptService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.dept.pojos.Dept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private IDeptService deptService;

    @PostMapping("add")
    public ResponseResult addDept(@RequestBody Dept dept) {
        return deptService.add(dept);
    }

    @GetMapping("listAll")
    public ResponseResult listDeptAll() {
        return ResponseResult.okResult(deptService.list());
    }
}
