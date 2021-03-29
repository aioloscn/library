package com.aiolos.library.aop;

import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.config.ThreadLocalToken;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aiolos
 * @date 2021/3/18 11:08 下午
 */
@Slf4j
@Aspect
@Component
public class TokenAspect {

    private final ThreadLocalToken threadLocalToken;

    private final static int longerTime = 3000;
    private final static int longTime = 2000;

    public TokenAspect(ThreadLocalToken threadLocalToken) {
        this.threadLocalToken = threadLocalToken;
    }

    @Around("execution(public * com.aiolos.library.controller.*.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        Map<String, Object> params = getArgsNameAndValue(point);
        log.info("======================= 开始执行 {}.{} =======================\n", point.getTarget().getClass(), point.getSignature().getName());
        params.entrySet().stream().forEach(entry -> {
            log.info("======================= argName: {}, argValue: {} =======================\n", entry.getKey(), entry.getValue());
        });

        long start = System.currentTimeMillis();

        Object resp = point.proceed();

        long end = System.currentTimeMillis();
        long takeTime = end - start;

        if (takeTime > longerTime) {
            log.error("当前执行耗时：{}", takeTime);
        } else if (takeTime > longTime) {
            log.warn("当前执行耗时：{}", takeTime);
        } else {
            log.info("当前执行耗时：{}", takeTime);
        }

        // 注册和登录时已经put token，防止其他情况下遗漏在请求结束后再put一次
        String token = threadLocalToken.getToken();
        if (resp instanceof CommonResponse && token != null) {
            ((CommonResponse) resp).put("token", token);
            threadLocalToken.clear();
        }
        return resp;
    }

    /**
     * 获取参数Map集合
     * @param point
     * @return
     */
    private Map<String, Object> getArgsNameAndValue(ProceedingJoinPoint point) {

        Map<String, Object> params = new HashMap<>();
        String[] paramNames = ((CodeSignature) point.getSignature()).getParameterNames();
        Object[] paramValues = point.getArgs();
        for (int i = 0; i < paramNames.length; i++) {
            params.put(paramNames[i], paramValues[i]);
        }

        return params;
    }
}
