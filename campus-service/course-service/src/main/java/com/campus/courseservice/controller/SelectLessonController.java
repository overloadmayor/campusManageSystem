package com.campus.courseservice.controller;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.course.dtos.AdminStuPageDto;
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

    @PutMapping("{id}")
    public ResponseResult selectLesson(@PathVariable Long id) {
        return selectLessonService.selectLesson(id);
    }

    @PutMapping("back/{id}")
    public ResponseResult backLesson(@PathVariable Long id) {
        return selectLessonService.backLesson(id);
    }

    @GetMapping("getStusByLessonId")
    public ResponseResult getStusByLessonId(AdminStuPageDto dto) {
        dto.checkParam();
        return selectLessonService.getStusByLessonId(dto.getId(),dto.getPage(),dto.getPageSize());
    }

    @GetMapping("listAllStus")
    public ResponseResult listAllStus(@RequestParam Long lessonId) {
        return selectLessonService.getAllStusByLessonId(lessonId);
    }
}
