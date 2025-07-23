package com.me.mall;


import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


//@MapperScan({"com.me.mall.mapper","com.me.mall.common"})
@ComponentScan({"com.me.mall", "com.me.mall.security"})
@SpringBootApplication()
@EnableEncryptableProperties
public class MallSupplyApplication {
	public static void main(String[] args) {
		SpringApplication.run(MallSupplyApplication.class, args);
	}
}
