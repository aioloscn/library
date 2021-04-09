package com.aiolos.library;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Aiolos
 * @date 2021/3/21 4:31 下午
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.aiolos")
@MapperScan(basePackages = "com.aiolos.library.dao")
@EnableFeignClients(basePackages = "com.aiolos")
@EnableHystrix
@EnableEurekaClient
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
