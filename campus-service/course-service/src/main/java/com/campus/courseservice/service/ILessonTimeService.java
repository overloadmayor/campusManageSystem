package com.campus.courseservice.service;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.course.pojos.LessonTime;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-21
 */
public interface ILessonTimeService extends IService<LessonTime> {

    ResponseResult calAvailableTime(String uid, Integer status);


    ResponseResult getLessonTimes(List<Long> ids);
}
