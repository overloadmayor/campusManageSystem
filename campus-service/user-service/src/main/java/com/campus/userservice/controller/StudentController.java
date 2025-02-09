package com.campus.userservice.controller;

import com.campus.common.annotation.Admin;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.user.dtos.StudentLoginDto;
import com.campus.model.user.dtos.StudentPageDto;
import com.campus.model.user.pojos.Students;
import com.campus.userservice.service.IStudentsService;
import com.campus.utils.thread.UserThreadLocalUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class StudentController {
    @Autowired
    private IStudentsService studentsService;

    @PostMapping("studentLogin")
    public ResponseResult studentLogin(@RequestBody StudentLoginDto studentLoginDto){
        return studentsService.login(studentLoginDto);
    }

    @Admin
    @PostMapping("student")
    public ResponseResult addStudent(@RequestBody Students students){
        return studentsService.add(students);
    }

    @Admin
    @GetMapping("student")
    public ResponseResult getStudents(StudentPageDto studentPageDto){
        return studentsService.listAll(studentPageDto);
    }

    @GetMapping("getInfo")
    public ResponseResult getInfo(@RequestParam(required = false) Long StuId){
        if(StuId!=null){
            UserThreadLocalUtil.setUser(StuId);
        }
        return studentsService.getInfo();
    }
}
