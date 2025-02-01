package com.campus.adminservice.service;

import com.campus.model.admin.dtos.TeacherLoginDto;
import com.campus.model.admin.pojos.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;
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

    ResponseResult adminLogin(TeacherLoginDto teacherLoginDto);

    ResponseResult updateAdmin(Teacher teacher);

}
