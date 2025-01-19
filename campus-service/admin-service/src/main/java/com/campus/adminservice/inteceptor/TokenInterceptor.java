package com.campus.adminservice.inteceptor;


import com.campus.model.admin.pojos.Teacher;
import com.campus.utils.thread.AdminThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AdminThreadLocalUtil.clear();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId=request.getHeader("userId");

        if(userId!=null){
            Teacher teacher=new Teacher();
            teacher.setId(Integer.valueOf(userId).longValue());
            AdminThreadLocalUtil.setUser(teacher);
        }
        return true;
    }


}
