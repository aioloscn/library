package com.aiolos.library;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Aiolos
 * @date 2021/3/19 5:53 上午
 */
@SpringBootApplication
@MapperScan(basePackages = "com.aiolos.library.dao")
@ComponentScan(basePackages = "com.aiolos")
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
