package com.campus.teacherservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.model.admin.dtos.TeacherLoginDto;
import com.campus.model.teacher.dtos.TeacherCourseDto;
import com.campus.model.teacher.dtos.TeacherLessonDto;
import com.campus.model.teacher.dtos.TeacherPageDto;
import com.campus.model.admin.pojos.Teacher;
import com.campus.model.common.dtos.ResponseResult;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-15
 */
public interface ITeacherService extends IService<Teacher> {

    ResponseResult addTeacher(Teacher teacher);

    ResponseResult listAll(TeacherPageDto teacherPageDto);

    ResponseResult teacherLogin(TeacherLoginDto teacher);

    ResponseResult askForCourses(TeacherCourseDto teacherCourseDto);

    ResponseResult askForLessons(TeacherLessonDto teacherlessonDto);
}
