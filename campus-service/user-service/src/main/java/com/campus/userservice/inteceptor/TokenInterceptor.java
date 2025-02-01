package com.campus.userservice.inteceptor;


import com.campus.model.admin.pojos.Teacher;
import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserThreadLocalUtil.clear();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId=request.getHeader("userId");
        if(userId!=null){
            UserThreadLocalUtil.setUser(Integer.valueOf(userId).longValue());
        }
        return true;
    }


}
