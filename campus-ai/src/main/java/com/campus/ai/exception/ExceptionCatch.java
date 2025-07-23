package com.campus.ai.exception;

import com.campus.ai.common.dtos.ResponseResult;
import com.campus.ai.common.enums.AppHttpCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;

@ControllerAdvice  //控制器增强类
//@Slf4j
public class ExceptionCatch {

    private static final Logger log = LoggerFactory.getLogger(ExceptionCatch.class);

    @ExceptionHandler(SQLException.class)
    @ResponseBody
    public ResponseResult exception(SQLException e){
        e.printStackTrace();
        log.error("catch exception:{}",e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR,e.getMessage());
    }

    /**
     * 处理ai异常
     * @param e
     * @return
     */
    @ExceptionHandler(AIException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED) // 直接设置HTTP状态码为501
    @ResponseBody
    public ResponseResult exception(AIException e){
        log.error("catch exception:{}",e);
        return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID,e.getMessage());
    }

    /**
     * 处理不可控异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        e.printStackTrace();
        log.error("catch exception:{}",e.getMessage());
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }

    /**
     * 处理可控异常  自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult exception(CustomException e){
        log.error("catch exception:{}",e);
        return ResponseResult.errorResult(e.getAppHttpCodeEnum());
    }
}
