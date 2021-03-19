package com.aiolos.library.config;

import com.aiolos.library.interceptors.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Aiolos
 * @date 2021/3/19 6:02 下午
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        // 注册拦截器，配置哪些接口需要对应的拦截器拦截
        registry.addInterceptor(userInterceptor())
                .addPathPatterns("/user/getSMSCode");
    }
}
