package com.campus.courseservice.controller;

import com.campus.courseservice.service.ILessonService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.course.dtos.LessonAddDto;
import com.campus.model.course.pojos.Lesson;
import com.campus.model.teacher.dtos.TeacherLessonDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    @Autowired
    private ILessonService lessonService;

    @GetMapping("check")
    public ResponseResult checkLesson() {
        return lessonService.checkAddLesson();
    }

    @PostMapping("add")
    public ResponseResult addLesson(@RequestBody LessonAddDto lessonAddDto) {
        return lessonService.addLesson(lessonAddDto);
    }

    @GetMapping("getByTeacher")
    public ResponseResult getLessonByTeacher(@RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth term) {
        return lessonService.getLessonsByTeacher(LocalDate.of(term.getYear(),
                term.getMonth(),1));
    }


}
