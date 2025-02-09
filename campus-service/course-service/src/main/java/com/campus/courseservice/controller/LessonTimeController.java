package com.campus.courseservice.controller;

import com.campus.courseservice.service.ILessonTimeService;
import com.campus.model.common.dtos.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lessonTime")
public class LessonTimeController {
    @Autowired
    private ILessonTimeService lessonTimeService;

    @GetMapping
    public ResponseResult getLessonTime(@RequestParam String uid,@RequestParam Integer status){
        return lessonTimeService.calAvailableTime(uid,status);
    }

    @GetMapping("{ids}")
    public ResponseResult getLessonTimes(@PathVariable("ids") List<Long> ids){
        return lessonTimeService.getLessonTimes(ids);
    }
}
