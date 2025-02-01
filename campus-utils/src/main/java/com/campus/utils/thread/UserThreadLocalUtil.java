package com.campus.utils.thread;


import com.campus.model.admin.pojos.Teacher;

public class UserThreadLocalUtil {
    private final static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static Long getUser() {
        return threadLocal.get();
    }
    public static void setUser(Long id) {
        threadLocal.set(id);
    }
    public static void clear() {
        threadLocal.remove();
    }
}
