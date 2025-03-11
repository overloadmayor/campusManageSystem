package com.campus.courseservice.controller;

import com.campus.courseservice.service.ILessonTimeService;
import com.campus.model.common.dtos.ResponseResult;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/lessonTime")
public class LessonTimeController {
    @Autowired
    private ILessonTimeService lessonTimeService;

    @GetMapping
    public ResponseResult getLessonTime(@RequestParam String uid, @RequestParam Integer status,
                                        @RequestParam @DateTimeFormat(pattern ="yyyy-MM") LocalDate term) {
        return lessonTimeService.calAvailableTime(uid, status, term);
    }

    @GetMapping("{ids}")
    public ResponseResult getLessonTimes(@PathVariable("ids") List<Long> ids) {
        return lessonTimeService.getLessonTimes(ids);
    }
}
