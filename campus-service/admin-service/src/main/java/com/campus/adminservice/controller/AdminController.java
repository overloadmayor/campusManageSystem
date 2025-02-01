package com.campus.adminservice.controller;

import com.campus.adminservice.service.ITeacherService;
import com.campus.model.admin.dtos.TeacherLoginDto;
import com.campus.model.admin.pojos.Teacher;
import com.campus.model.common.dtos.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@ApiOperation(value = "管理员登陆",tags="管理员登陆")
public class AdminController {
    @Autowired
    private ITeacherService teacherService;

    @PostMapping("adminLogin")
    public ResponseResult Login(@RequestBody TeacherLoginDto teacherLoginDto) {
        return teacherService.adminLogin(teacherLoginDto);
    }

    @PutMapping("update")
    public ResponseResult update(@RequestBody Teacher teacher) {
        return teacherService.updateAdmin(teacher);
    }
}
