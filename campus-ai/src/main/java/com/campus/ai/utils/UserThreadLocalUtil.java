package com.campus.ai.utils;


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
