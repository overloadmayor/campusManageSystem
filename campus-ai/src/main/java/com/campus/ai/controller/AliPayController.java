package com.campus.ai.controller;

import com.campus.ai.common.dtos.ResponseResult;
import com.campus.ai.entity.AliPay;
import com.campus.ai.service.AliPayService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("alipay")
public class AliPayController {

   @Resource
   private AliPayService aliPayService;


    @GetMapping("/pay") // http://8.138.246.2:6004/alipay/pay?totalAmount=100
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {

        aliPayService.pay(aliPay,httpResponse);
    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {

        return aliPayService.payNotify(request);
    }

    @GetMapping("/queryBalance")
    public ResponseResult queryOrder() {
        return aliPayService.queryBalance();
    }
}
