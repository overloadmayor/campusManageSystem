package com.campus.courseservice.controller;

import com.campus.courseservice.service.ICourseService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.course.dtos.CourseApplyDto;
import com.campus.model.course.dtos.CourseListTreeDto;
import com.campus.model.course.pojos.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private ICourseService courseService;

    @GetMapping("check")
    public ResponseResult checkAddCourse(){
        return courseService.checkAddCourse();
    }

    @GetMapping("{id}")
    public ResponseResult getCourseById(@PathVariable("id") Long id){
        return courseService.checkCourse(id);
    }

    @GetMapping("list")
    public ResponseResult getCourseList(Course course){
        return courseService.listAll(course);
    }

    @PutMapping("apply")
    public ResponseResult applyCourse(@RequestBody CourseApplyDto courseApplyDto){
        return courseService.applyCourse(courseApplyDto);
    }

    @GetMapping("together")
    public ResponseResult getCourseTogether(@RequestParam(required = false) List<Long> majors){
        if(majors == null||majors.isEmpty()){
            return courseService.listAll(new Course());
        }
        return courseService.getCourseByMajors(majors);
    }

    @GetMapping("treeList")
    public ResponseResult getCourseTreeList(CourseListTreeDto course){
        return courseService.treeList(course);
    }

    @GetMapping("name/{ids}")
    public ResponseResult getCourseName(@PathVariable("ids") List<Long> ids){
        return courseService.getCourseInfo(ids);
    }
}
