package com.campus.majoranddeptservice.service.impl;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.dept.pojos.Dept;
import com.campus.model.major.pojos.Major;
import com.campus.majoranddeptservice.mapper.MajorMapper;
import com.campus.majoranddeptservice.service.IMajorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.model.majorAndDept.pojos.DeptMajor;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.toolkit.Db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-17
 */
@Service

public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements IMajorService {

    @Override
    public ResponseResult add(Major major) {
        save(major);
        return ResponseResult.okResult(major.getId());
    }

    @Override
    public ResponseResult listDeptMajor() {
        List<DeptMajor> deptMajorList=new ArrayList<>();
        List<Dept> list = Db.lambdaQuery(Dept.class).list();
        Map<Long, List<Major>> majors = lambdaQuery().list().stream().collect(Collectors.groupingBy(Major::getDeptId));
        for (Dept dept : list) {
            DeptMajor deptMajor = new DeptMajor();
            deptMajor.setId(dept.getId());
            deptMajor.setName(dept.getName());
            deptMajor.setMajors(majors.get(dept.getId()));
            deptMajorList.add(deptMajor);
        }
        return ResponseResult.okResult(deptMajorList);
    }


}
