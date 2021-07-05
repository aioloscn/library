package com.aiolos.library.config.shiro;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.library.config.JwtUtil;
import com.aiolos.library.pojo.User;
import com.aiolos.library.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 拦截Shiro拦截的请求，并且检查是否有认证与授权
 * @author Aiolos
 * @date 2021/3/19 12:10 下午
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public OAuth2Realm(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 认证(验证登录时调用)
     * Filter类onAccessDenied方法之后执行
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        // 从令牌中获取userId，然后检查该账户是否有相应请求的权限
        String token = (String) authenticationToken.getPrincipal();
        Long userId = jwtUtil.getUserId(token);
        User user = userService.searchById(userId);

        if (user == null) {
            throw new LockedAccountException(ErrorEnum.ACCOUNT_FROZEN.getErrMsg());
        }

        // 往info对象中添加用户信息、token字符串
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, token, getName());
        return info;
    }

    /**
     * 授权(验证权限时调用)
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        // 在认证方法中封装了用户的信息，所以在这里可以得到
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<String> userPermissions = userService.searchUserPermissions(user.getId());

        // 把权限列表添加到info对象中
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(userPermissions);
        return info;
    }
}