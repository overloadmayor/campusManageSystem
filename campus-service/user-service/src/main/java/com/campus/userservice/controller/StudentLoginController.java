package com.campus.userservice.controller;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.user.dtos.StudentLoginDto;
import com.campus.userservice.service.IStudentsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@ApiOperation(value = "学生登陆",tags="学生登陆")
public class StudentLoginController {
    @Autowired
    private IStudentsService studentsService;

    @PostMapping("studentLogin")
    public ResponseResult studentLogin(@RequestBody StudentLoginDto studentLoginDto){
        return studentsService.login(studentLoginDto);
    }


}
