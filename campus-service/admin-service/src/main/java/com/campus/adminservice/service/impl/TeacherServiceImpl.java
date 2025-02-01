package com.campus.adminservice.service.impl;

import com.campus.common.constants.AdminConstants;
import com.campus.model.admin.dtos.TeacherLoginDto;
import com.campus.model.admin.pojos.Teacher;
import com.campus.adminservice.mapper.TeacherMapper;
import com.campus.adminservice.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.model.admin.vos.TeacherLoginVo;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;

import com.campus.utils.common.AdminJwtUtil;

import com.campus.utils.common.RSAUtil;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-15
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    @Override
    public ResponseResult adminLogin(TeacherLoginDto teacherLoginDto) {
        TeacherLoginVo teacherLoginVo = new TeacherLoginVo();
        Teacher one = lambdaQuery().eq(Teacher::getId, teacherLoginDto.getId()).one();
        if(one==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        if(one.getAdmin().equals(AdminConstants.NO_ADMIN)){
            return ResponseResult.errorResult(AppHttpCodeEnum.USER_INVALID);
        }
        String password= one.getPassword();
        String passwordDto=null;
        try {
            passwordDto= RSAUtil.rsaDecrypt(teacherLoginDto.getPassword());

            passwordDto= DigestUtils.md5DigestAsHex(passwordDto.getBytes());
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        if(!password.equals(passwordDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        String token= AdminJwtUtil.getToken(one.getId());
        one.setPassword(null);
        teacherLoginVo.setToken(token);
        teacherLoginVo.setInfo(one);
        return ResponseResult.okResult(teacherLoginVo);
    }


    @Override
    public ResponseResult updateAdmin(Teacher teacher) {
        if(UserThreadLocalUtil.getUser().equals(teacher.getId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        boolean update = lambdaUpdate().eq(Teacher::getId, teacher.getId())
                .set(Teacher::getAdmin, teacher.getAdmin()).update();
        if(update)
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        else{
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,"操作失败");
        }
    }




}
