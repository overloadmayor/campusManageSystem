package com.campus.courseservice.service;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.course.dtos.CourseApplyDto;
import com.campus.model.course.dtos.CourseListTreeDto;
import com.campus.model.course.pojos.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-19
 */
public interface ICourseService extends IService<Course> {

    ResponseResult checkAddCourse();

    ResponseResult checkCourse(Long id);

    ResponseResult listAll(Course course);

    ResponseResult applyCourse(CourseApplyDto courseApplyDto);

    ResponseResult getCourseByMajors(List<Long> majors);

    ResponseResult treeList(CourseListTreeDto course);

    ResponseResult getCourseInfo(List<Long> ids);

    ResponseResult treeListInSchema(CourseListTreeDto course);

    ResponseResult check(Long lessonId);

    ResponseResult backLessonCheck(Long id);
}
