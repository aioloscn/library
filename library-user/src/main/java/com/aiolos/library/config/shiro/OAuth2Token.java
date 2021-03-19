package com.aiolos.library.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author Aiolos
 * @date 2021/3/19 11:50 上午
 */
public class OAuth2Token implements AuthenticationToken {

    private String token;

    public OAuth2Token(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
