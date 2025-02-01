package com.campus.majoranddeptservice.service.impl;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.dept.pojos.Dept;
import com.campus.majoranddeptservice.mapper.DeptMapper;
import com.campus.majoranddeptservice.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-17
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    @Override
    public ResponseResult add(Dept dept) {
        save(dept);
        return ResponseResult.okResult(dept.getId());
    }
}
