package com.aiolos.library.interceptors;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.exception.TokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Aiolos
 * @date 2021/6/27 5:18 下午
 */
@Slf4j
public class BaseInterceptor {

    @Autowired
    public StringRedisTemplate redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

    /**
     * 验证用户token
     * @param token
     * @return
     * @throws CustomizeException
     */
    public boolean verifyUserIdToken(String token) throws CustomizeException {
        if (StringUtils.isNotBlank(token)) {
            log.info("验证token: {}", token);
            if (!redis.hasKey(token)) {
                // 不存在该token，前端删除存储的token
                throw new TokenInvalidException(ErrorEnum.TOKEN_INVALID);
            }
        } else {
            throw new TokenInvalidException(ErrorEnum.USER_NOT_LOGGED_IN);
        }
        return true;
    }
}
