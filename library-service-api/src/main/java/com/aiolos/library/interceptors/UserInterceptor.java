package com.aiolos.library.interceptors;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.utils.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aiolos
 * @date 2021/3/19 5:55 下午
 */
public class UserInterceptor implements HandlerInterceptor {

    @Qualifier("redisTemplate")
    @Autowired
    public RedisTemplate redis;

    public static final String MOBILE_SMSCODE = "mobile:smscode";

    /**
     * 拦截请求，访问controller之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userIp = IPUtils.getRequestIp(request);
        // 查看60秒内是否给用户发送过验证码
        Boolean keyIsExists = redis.hasKey(MOBILE_SMSCODE + ":" + userIp);
        if (keyIsExists) {
            throw new CustomizeException(ErrorEnum.REPEAT_SENDING_SMS_CODE);
        }
        return true;
    }
}
