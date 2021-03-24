package com.aiolos.library.controller;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.config.JwtUtil;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @author Aiolos
 * @date 2021/3/19 6:56 上午
 */
public class BaseController {

    @Value("${library.jwt.cache-expire}")
    private int cacheExpire;

    @Autowired
    public JwtUtil jwtUtil;

    @Qualifier("redisTemplate")
    @Autowired
    public RedisTemplate redis;

    public static final int SMSCODE_EXPIRE_TIME = 60;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

    public void saveCacheToken(long userId, String token) {
        redis.opsForValue().set(userId, token, cacheExpire, TimeUnit.DAYS);
    }

    /**
     * 查询用户是否存在
     * @param userResp
     * @throws CustomizeException
     */
    public void checkIfTheUserExists(CommonResponse userResp) throws CustomizeException {
        if (userResp == null || userResp.getData() == null || !(userResp.getData() instanceof User)) {
            throw new CustomizeException(ErrorEnum.USER_DOES_NOT_EXIST);
        }
    }

    /**
     * 查询书籍是否存在
     * @param bookResp
     */
    public void checkIfTheBookExists(CommonResponse bookResp) throws CustomizeException {
        if (bookResp == null || bookResp.getData() == null || !(bookResp.getData() instanceof Book)) {
            throw new CustomizeException(ErrorEnum.BOOK_DOES_NOT_EXIST);
        }
    }
}
