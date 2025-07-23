package com.campus.ai.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.easysdk.factory.Factory;
import com.campus.ai.common.dtos.ResponseResult;
import com.campus.ai.config.AliPayConfig;
import com.campus.ai.entity.AiOrders;
import com.campus.ai.entity.AiUserRestPay;
import com.campus.ai.entity.AliPay;
import com.campus.ai.exception.AIException;
import com.campus.ai.mapper.AiOrdersMapper;
import com.campus.ai.mapper.AiUserRestPayMapper;
import com.campus.ai.service.AliPayService;
import com.campus.ai.utils.UserThreadLocalUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class AliPayServiceImpl implements AliPayService {

    @Resource
    private AliPayConfig aliPayConfig;
    @Resource
    private AiUserRestPayMapper aiUserRestPayMapper;
    @Resource
    private AiOrdersMapper aiOrdersMapper;
    @Resource
    private TransactionTemplate transactionTemplate;

    private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";

    @Override
    public void pay(AliPay aliPay, HttpServletResponse httpResponse)throws Exception {
        //插入aiOrders表
        Long user = UserThreadLocalUtil.getUser();
        String outTradeNo = user.toString()+UUID.randomUUID().toString();
        aliPay.setSubject("ai交易");
        aliPay.setTraceNo(outTradeNo);
        AiOrders aiOrders = new AiOrders();
        aiOrders.setOutTradeNo(outTradeNo);
        aiOrders.setStuId(user);
        aiOrders.setSubject(aliPay.getSubject());
        aiOrders.setTotalAmount(new BigDecimal(aliPay.getTotalAmount()));
        aiOrders.setStatus(0);
        aiOrdersMapper.insert(aiOrders);

        //发送支付请求
        AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";

        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request,"GET").getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @Override
    public String payNotify(HttpServletRequest request) throws Exception{
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");
            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
                //获取订单
                AiOrders outTradeNo = aiOrdersMapper.getByTradeNo(params.get("out_trade_no"));
                if(outTradeNo==null){
                    return "fail";
                }
                //更新订单支付状态
                AiOrders aiOrders = new AiOrders();
                aiOrders.setOutTradeNo(tradeNo);
                //更新为已支付
                aiOrders.setStatus(1);
                //更新用户剩余次数
                AiUserRestPay aiUserRestPay = aiUserRestPayMapper.getByStuId(outTradeNo.getStuId());
                if(aiUserRestPay==null){
                    return "fail";
                }
                BigDecimal rest=aiUserRestPay.getBalance().add(
                        new BigDecimal(params.get("total_amount"))
                );
                //更新用户剩余次数
                AiUserRestPay newBalance = new AiUserRestPay();
                newBalance.setStuId(outTradeNo.getStuId());
                newBalance.setBalance(rest);
                //操作数据库
                transactionTemplate.execute((transactionStatus)->{
                    try {
                        aiOrdersMapper.updateStatusByTradeNo(aiOrders);
                        aiUserRestPayMapper.updateByStuId(newBalance);
                    }catch (Exception e){
                        transactionStatus.setRollbackOnly();
                        throw new AIException(e.getMessage());
                    }
                    return transactionStatus;
                });
            }
        }
        return "success";
    }

    @Override
    public ResponseResult queryBalance() {
        Long user = UserThreadLocalUtil.getUser();
        AiUserRestPay aiUserRestPay = aiUserRestPayMapper.getByStuId(user);

        return ResponseResult.okResult(aiUserRestPay.getBalance());
    }
}
