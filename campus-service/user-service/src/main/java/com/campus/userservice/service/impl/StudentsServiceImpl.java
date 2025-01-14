package com.campus.userservice.service.impl;

import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.model.user.dtos.StudentLoginDto;
import com.campus.model.user.vos.StudentLoginVo;
import com.campus.userservice.mapper.StudentsMapper;
import com.campus.userservice.service.IStudentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.utils.common.AppJwtUtil;
import com.campus.utils.common.RSAUtil;
import org.springframework.stereotype.Service;
import com.campus.model.user.pojos.Students;
import org.springframework.util.DigestUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2025-01-13
 */
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements IStudentsService {

    @Override
    public ResponseResult login(StudentLoginDto studentLoginDto) {
        StudentLoginVo studentLoginVo = new StudentLoginVo();
        Students one = lambdaQuery().eq(Students::getId, studentLoginDto.getId()).one();
        if(one==null){
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        String password= one.getPassword();
        String passwordDto=null;
        try {
            passwordDto= RSAUtil.rsaDecrypt(studentLoginDto.getPassword());

            passwordDto=DigestUtils.md5DigestAsHex(passwordDto.getBytes());
        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }
        if(!password.equals(passwordDto)){
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        String token= AppJwtUtil.getToken(one.getId());
        one.setPassword(null);
        studentLoginVo.setToken(token);
        studentLoginVo.setInfo(one);
        return ResponseResult.okResult(studentLoginVo);
    }
}
