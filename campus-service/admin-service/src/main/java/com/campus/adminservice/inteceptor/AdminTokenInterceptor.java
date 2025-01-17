package com.campus.adminservice.inteceptor;


import com.campus.model.admin.pojos.Teacher;
import com.campus.utils.thread.AdminThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminTokenInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        AdminThreadLocalUtil.clear();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId=request.getHeader("userId");
        String isAdmin=request.getHeader("isAdmin");
        if(userId!=null&&isAdmin!=null){
            Teacher teacher=new Teacher();
            teacher.setId(Integer.valueOf(userId).longValue());
            teacher.setAdmin(Integer.valueOf(isAdmin).intValue());
            AdminThreadLocalUtil.setUser(teacher);
        }
        return true;
    }


}
