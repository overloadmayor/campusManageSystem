package com.me.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis相关配置
 * Created by me on 2018/4/26.
 */
@Configuration
@MapperScan({"com.me.mall.mapper","com.me.mall.dao"})
public class MyBatisConfig {
}
