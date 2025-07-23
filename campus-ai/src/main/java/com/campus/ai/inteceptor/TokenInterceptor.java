package com.campus.ai.inteceptor;



import com.campus.ai.utils.StuJwtUtil;
import com.campus.ai.utils.UserThreadLocalUtil;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.servlet.HandlerInterceptor;



public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocalUtil.clear();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("Authorization");
        // 若Authorization为空，返回401未授权
        if (StringUtils.isEmpty(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // 可根据需求添加响应内容（如JSON提示信息）
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未提供Authorization令牌\"}");
            return false; // 终止请求链，不再继续处理
        }

        try {
            Claims claimsBody= StuJwtUtil.getClaimsBody(token);
            int result=StuJwtUtil.verifyToken(claimsBody);
            if(result==1||result==2){
                // 处理userId格式错误的情况（可选）
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":400,\"message\":\"Authorization令牌格式错误\"}");
                return false;
            }
            Object userId=claimsBody.get("id");
            //存储header中
            UserThreadLocalUtil.setUser(Long.valueOf(userId.toString()));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"Authorization令牌格式错误\"}");
            return false;
        }

        return true;
    }


}
