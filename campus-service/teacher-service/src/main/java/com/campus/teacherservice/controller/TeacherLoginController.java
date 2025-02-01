package com.campus.teacherservice.controller;

import com.campus.model.admin.dtos.TeacherLoginDto;
import com.campus.model.admin.pojos.Teacher;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.teacherservice.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teacher")
public class TeacherLoginController {
    @Autowired
    private ITeacherService teacherService;

    @PostMapping("teacherLogin")
    public ResponseResult teacherLogin(@RequestBody TeacherLoginDto teacher) {
        return teacherService.teacherLogin(teacher);
    }
}
