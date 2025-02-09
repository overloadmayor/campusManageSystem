package com.campus.teacherservice.controller;

import com.campus.model.teacher.dtos.TeacherCourseDto;
import com.campus.model.teacher.dtos.TeacherLessonDto;
import com.campus.model.teacher.dtos.TeacherPageDto;
import com.campus.model.admin.pojos.Teacher;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.teacherservice.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private ITeacherService teacherService;

    @PostMapping("add")
    public ResponseResult addTeacher(@RequestBody Teacher teacher) {
        return teacherService.addTeacher(teacher);
    }

    @GetMapping("page")
    public ResponseResult getTeacherPage(TeacherPageDto teacherPageDto) {
        return teacherService.listAll(teacherPageDto);
    }

    @PostMapping("askForCourses")
    public ResponseResult askForCourses(@RequestBody TeacherCourseDto teacherCourseDto){
        return teacherService.askForCourses(teacherCourseDto);
    }

    @PostMapping("askForLessons")
    public ResponseResult askForLessons(@RequestBody TeacherLessonDto teacherlessonDto){
        return teacherService.askForLessons(teacherlessonDto);
    }


    @GetMapping("name/{id}")
    public ResponseResult getTeacher(@PathVariable Long id) {
        return teacherService.getTeacherName(id);
    }
}
