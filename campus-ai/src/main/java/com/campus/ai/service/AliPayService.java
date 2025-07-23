package com.campus.ai.service;

import com.campus.ai.common.dtos.ResponseResult;
import com.campus.ai.entity.AliPay;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AliPayService {
    void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception;

    String payNotify(HttpServletRequest request) throws Exception;

    ResponseResult queryBalance();
}
