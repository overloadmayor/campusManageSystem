package com.campus.courseservice.service;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.course.dtos.LessonAddDto;
import com.campus.model.course.pojos.Lesson;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-21
 */
public interface ILessonService extends IService<Lesson> {

    ResponseResult checkAddLesson();

    ResponseResult addLesson(LessonAddDto lessonAddDto);
}
