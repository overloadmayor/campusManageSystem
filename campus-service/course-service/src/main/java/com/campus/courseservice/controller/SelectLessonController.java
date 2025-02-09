package com.campus.courseservice.controller;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.selectLesson.dtos.SetTimeDto;
import com.campus.courseservice.service.ISelectLessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/selectLesson")
public class SelectLessonController {
    @Autowired
    private ISelectLessonService selectLessonService;

    @PutMapping("setTime")
    public ResponseResult setTime(@RequestBody SetTimeDto setTimeDto) {
        return selectLessonService.setTime(setTimeDto);
    }

    @GetMapping("getTime")
    public ResponseResult getTime() {
        return selectLessonService.getTime();
    }

    @GetMapping("list")
    public ResponseResult listByStudentId() {
        return selectLessonService.listByStuId();
    }


}
