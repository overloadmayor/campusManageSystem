package com.campus.userservice.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.model.common.dtos.ResponseResult;
import com.campus.model.user.dtos.StudentLoginDto;
import com.campus.model.user.dtos.StudentPageDto;
import com.campus.model.user.pojos.SendMQPojos;
import com.campus.model.user.pojos.Students;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2025-01-13
 */

public interface IStudentsService extends IService<Students> {

    ResponseResult login(StudentLoginDto studentLoginDto);

    ResponseResult add(Students students);

    ResponseResult listAll(StudentPageDto studentPageDto);

    ResponseResult getInfo();

    ResponseResult getInfosByIds(List<Long> ids);

    void batchInsert(SendMQPojos pojo);

    ResponseResult handleFileUpload(HttpServletRequest request);

    ResponseResult checkUploadDetailInfos();
}
