package com.aiolos.library.config.shiro;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 处理认证token字符串
 * @author Aiolos
 * @date 2021/3/18 8:28 下午
 */
@Component
@Slf4j
public class JwtUtil {

    @Value("${library.jwt.secret}")
    private String secret;

    @Value("${library.jwt.expire}")
    private int expire;

    public String createToken(String userId) {

        DateTime expireDatetime = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, expire);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create();
        String token = builder.withClaim("userId", userId).withExpiresAt(expireDatetime).sign(algorithm);
        return token;
    }

    public String getUserId(String token) {

        DecodedJWT jwt = JWT.decode(token);
        String userId = jwt.getClaim("userId").asString();
        return userId;
    }

    public void verifyToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
}
