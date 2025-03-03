package com.campus.utils.common;


import java.util.Random;

public class RedisExpireUtil {
    public static long RandomTime(){
        long num = new Random().nextInt(11) + 5;
        return num;
    }
}
