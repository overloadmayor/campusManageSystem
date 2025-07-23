package com.campus.ai;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.campus.ai.mapper")
public class AIServiceApplicatioin {

    public static void main(String[] args) {
        SpringApplication.run(AIServiceApplicatioin.class, args);
    }

}
