package com.aiolos.library.config;

import org.springframework.stereotype.Component;

/**
 * @author Aiolos
 * @date 2021/3/19 10:43 上午
 */
@Component
public class ThreadLocalToken {

    private ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public void setToken(String token) {
        threadLocal.set(token);
    }

    public String getToken() {
        return threadLocal.get();
    }

    public void clear() {
        threadLocal.remove();
    }
}
