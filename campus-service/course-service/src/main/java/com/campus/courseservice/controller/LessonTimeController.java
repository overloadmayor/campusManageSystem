package com.campus.courseservice.controller;

import com.campus.courseservice.service.ILessonTimeService;
import com.campus.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lessonTime")
public class LessonTimeController {
    @Autowired
    private ILessonTimeService lessonTimeService;

    @GetMapping
    private ResponseResult getLessonTime(@RequestParam String uid,@RequestParam Integer status){
        return lessonTimeService.calAvailableTime(uid,status);
    }
}
