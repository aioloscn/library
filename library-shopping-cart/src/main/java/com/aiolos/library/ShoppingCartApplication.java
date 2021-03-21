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
@EnableFeignClients(basePackages = "com.aiolos")
@EnableHystrix      // 服务调用者断路器开启，服务提供者已经写了全局服务熔断降级方法，加上这个注解才可用
public class ShoppingCartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoppingCartApplication.class, args);
    }
}
