package com.aiolos.library.config.shiro;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.library.config.ThreadLocalToken;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 拦截请求，判断哪些请求应该被Shiro处理
 * options请求直接放行
 * 默认为单例，全局使用这一个对象的话往ThreadLocal中保存数据会冲突
 * 添加@Scope("prototype")注解使其为多例
 * @author Aiolos
 * @date 2021/3/19 11:25 上午
 */
@Component
@Scope("prototype")
@Slf4j
public class OAuth2Filter extends AuthenticatingFilter {

    private final ThreadLocalToken threadLocalToken;

    private final JwtUtil jwtUtil;

    private final RedisTemplate redisTemplate;

    @Value("${library.jwt.cache-expire}")
    private int cacheExpire;

    public OAuth2Filter(ThreadLocalToken threadLocalToken, JwtUtil jwtUtil, RedisTemplate redisTemplate) {
        this.threadLocalToken = threadLocalToken;
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 拦截请求之后，用于把令牌字符串封装成令牌对象
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 获取请求token
        String token = getRequestToken((HttpServletRequest) servletRequest);

        if (StringUtils.isBlank(token)) {
            return null;
        }
        return new OAuth2Token(token);
    }

    /**
     * 拦截请求，判断请求是否需要被Shiro处理
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest req = (HttpServletRequest) request;
        // Ajax提交application/json数据的时候，会先发出Options请求
        // 这里先放行Options请求，不需要Shiro处理
        if (req.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return true;
        }

        // 除了Options请求之外，所有请求都要被Shiro处理
        return false;
    }

    /**
     * 该方法用于处理所有应该被Shiro处理的请求
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        // 允许跨域
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("origin"));

        threadLocalToken.clear();
        String token = getRequestToken(req);
        if (StringUtils.isBlank(token)) {
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print(ErrorEnum.TOKEN_INVALID.getErrMsg());
            return false;
        }

        try {
            jwtUtil.verifyToken(token);
        } catch (TokenExpiredException e) {
            // 检查redis中是否缓存了该令牌，如果有的话代表客户端保存的令牌已经过期了，服务端的令牌还没过期
            if (redisTemplate.hasKey(token)) {
                redisTemplate.delete(token);
                String userId = jwtUtil.getUserId(token);
                token = jwtUtil.createToken(userId);
                redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
                threadLocalToken.setToken(token);
            } else {
                // 客户端和服务端令牌都过期的情况
                resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
                resp.getWriter().print(ErrorEnum.TOKEN_IS_EXPIRED.getErrMsg());
                return false;
            }
        } catch (Exception e) {
            // 无效的令牌
            resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
            resp.getWriter().print(ErrorEnum.TOKEN_INVALID.getErrMsg());
            return false;
        }

        // 令牌正常且没有过期
        // 无法直接直接realm类，通过executeLogin这个方法间接的让Shiro执行realm类
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * Shiro执行realm里面的认证方法的时候判定用户没有登录或者用户登录失败就会执行这个方法
     * @param token
     * @param e
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest servletRequest, ServletResponse servletResponse) {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        // 允许跨域
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Origin", req.getHeader("origin"));
        resp.setStatus(HttpStatus.SC_UNAUTHORIZED);
        try {
            resp.getWriter().print(e.getMessage());
        } catch (IOException ioException) {
            log.error(ioException.getMessage());
        }
        return false;
    }

    private String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        return token;
    }
}
