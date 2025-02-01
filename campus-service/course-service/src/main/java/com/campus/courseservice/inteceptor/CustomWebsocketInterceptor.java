package com.campus.courseservice.inteceptor;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.campus.model.common.enums.AppHttpCodeEnum;
import com.campus.utils.common.AdminJwtUtil;
import com.campus.utils.thread.UserThreadLocalUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

import java.util.Map;

@Component
public class CustomWebsocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        ServletServerHttpRequest serverHttpRequest = (ServletServerHttpRequest) request;
        //获取参数
        String token = serverHttpRequest.getServletRequest().getParameter("Authorization");
        // 将 token 放入 WebSocketSession 的属性中
        attributes.put("token", token);
        if (StringUtils.isBlank(token)) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }

        try {
            Claims claimsBody = AdminJwtUtil.getClaimsBody(token);
            int result = AdminJwtUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
            Integer userId = (Integer)claimsBody.get("id");
            UserThreadLocalUtil.setUser(userId.longValue());
            // 鉴权逻辑
//            if(UserThreadLocalUtil.getUser()==null){
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return false;
//            }
//            if (!UserThreadLocalUtil.getUser().equals( userId.longValue())) {
//                response.setStatusCode(HttpStatus.UNAUTHORIZED);
//                return false;
//            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }


        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
// 握手成功后的逻辑
    }
}