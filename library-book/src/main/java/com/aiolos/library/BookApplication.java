package com.aiolos.library;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Aiolos
 * @date 2021/3/19 5:53 上午
 */
@SpringBootApplication
@MapperScan(basePackages = "com.aiolos.library.dao")
@ComponentScan(basePackages = "com.aiolos")
public class BookApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookApplication.class, args);
    }
}
