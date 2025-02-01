package com.campus.teachergateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //开启注册中心
public class TeacherGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeacherGatewayApplication.class, args);
    }

}
