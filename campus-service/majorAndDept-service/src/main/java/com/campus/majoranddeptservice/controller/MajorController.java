package com.campus.majoranddeptservice.controller;

import com.campus.common.annotation.Admin;
import com.campus.majoranddeptservice.service.IMajorService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.major.pojos.Major;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/major")
public class MajorController {
    @Autowired
    private IMajorService majorService;

    @PostMapping("add")
    public ResponseResult addMajor(@RequestBody Major major) {
        return majorService.add(major);
    }

    @GetMapping("listAll")
    public ResponseResult listMajorAll() {
        return ResponseResult.okResult(majorService.list());
    }
}
