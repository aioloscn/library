package com.aiolos.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Aiolos
 * @date 2021/3/19 6:56 上午
 */
public class BaseController {

    @Value("${library.jwt.cache-expire}")
    private int cacheExpire;

    @Autowired
    public StringRedisTemplate redis;

    public static final int SMSCODE_EXPIRE_TIME = 60;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

    public void saveCacheToken(String userId, String token) {
        redis.opsForValue().set(userId, token, cacheExpire, TimeUnit.DAYS);
    }
}
