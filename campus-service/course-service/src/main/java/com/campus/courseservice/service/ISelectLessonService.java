package com.campus.courseservice.service;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.selectLesson.dtos.SetTimeDto;
import com.campus.model.selectLesson.pojos.SelectLesson;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-02-01
 */

public interface ISelectLessonService extends IService<SelectLesson> {

    ResponseResult setTime(SetTimeDto setTimeDto);

    ResponseResult getTime();

    ResponseResult listByStuId();

    ResponseResult selectLesson(Long id);

    void selectLessonAndSave(SelectLesson selectLesson);

    void handleSelectLesson(SelectLesson selectLesson);

    void backLessonAndSave(SelectLesson selectLesson);

    ResponseResult backLesson(Long id);
}
