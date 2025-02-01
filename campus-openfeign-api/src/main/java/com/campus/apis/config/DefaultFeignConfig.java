//com.hmall.api.config.DefaultFeignConfig
package com.campus.apis.config;

import com.campus.apis.course.fallback.ICourseClientFallbackFactory;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class DefaultFeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public ICourseClientFallbackFactory iCourseClientFallbackFactory() {
        return new ICourseClientFallbackFactory();
    }
}
