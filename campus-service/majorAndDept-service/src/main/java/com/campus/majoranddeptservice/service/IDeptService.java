package com.campus.majoranddeptservice.service;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.dept.pojos.Dept;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-17
 */
public interface IDeptService extends IService<Dept> {

    ResponseResult add(Dept dept);
}
