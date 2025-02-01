package com.campus.majoranddeptservice.service;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.major.pojos.Major;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.model.majorAndDept.pojos.DeptMajor;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-17
 */
public interface IMajorService extends IService<Major> {

    ResponseResult add(Major major);

    ResponseResult listDeptMajor();
}
