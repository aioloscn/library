package com.aiolos.library.controller;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.enums.UserStatus;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.common.utils.IPUtils;
import com.aiolos.common.utils.SMSUtils;
import com.aiolos.library.config.JwtUtil;
import com.aiolos.library.controller.user.UserControllerApi;
import com.aiolos.library.pojo.User;
import com.aiolos.library.pojo.bo.RegisterLoginBO;
import com.aiolos.library.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author Aiolos
 * @date 2021/3/19 6:19 上午
 */
@Slf4j
@RestController
public class UserController extends BaseController implements UserControllerApi {

    private final SMSUtils smsUtils;

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public UserController(SMSUtils smsUtils, JwtUtil jwtUtil, UserService userService) {
        this.smsUtils = smsUtils;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }


    @Override
    public CommonResponse getSMSCode(String phone, HttpServletRequest request) throws CustomizeException {

        if (StringUtils.isBlank(phone)) {
            return CommonResponse.error(ErrorEnum.PHONE_INCORRECT);
        }

        // 获得用户ip
        String userIp = IPUtils.getRequestIp(request);

        // 根据用户的ip进行限制，限制用户在60秒内只能获得一次验证码
        redis.opsForValue().set(MOBILE_SMSCODE + ":" + userIp, userIp, SMSCODE_EXPIRE_TIME, TimeUnit.SECONDS);

        String code = String.valueOf((int) ((1 + Math.random()) * 1000000)).substring(1);
        log.info("code: {}", code);
        smsUtils.sendSMS(phone, code);

        // 验证码保存5分钟
        redis.opsForValue().set(MOBILE_SMSCODE + ":" + phone, code, 60 * 5, TimeUnit.SECONDS);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse login(RegisterLoginBO registerLoginBO) throws CustomizeException {

        String phone = registerLoginBO.getPhone();
        String smsCode = registerLoginBO.getCode();

        // 1. 校验验证码是否匹配
        String redisSMSCode = redis.opsForValue().get(MOBILE_SMSCODE + ":" + phone);
        if (StringUtils.isBlank(redisSMSCode)) {
            return CommonResponse.error(ErrorEnum.SMS_CODE_EXPIRED);
        }
        if (!redisSMSCode.equalsIgnoreCase(smsCode)) {
            return CommonResponse.error(ErrorEnum.SMS_CODE_INCORRECT);
        }

        // 2. 查询数据库，判断该用户是否已注册
        User user = userService.queryPhoneIsExists(phone);
        if (user != null && !user.getStatus().equals(UserStatus.ACTIVE.getType())) {
            // 如果用户已注册但不是激活状态，抛出异常，不予登录
            return CommonResponse.error(ErrorEnum.ACCOUNT_FROZEN);
        } else if (user == null) {
            user = userService.create(phone);
        }

        // 3. 保存用户分布式会话的相关操作
        // TODO 获取角色权限Set列表

        // 保存token前查看下redis中是否已存在，已存在则不重新set
        String token = redis.opsForValue().get(user.getId().toString());
        if (StringUtils.isBlank(token)) {
            token = jwtUtil.createToken(user.getId());
            saveCacheToken(user.getId(), token);
        }

        // 4. 用户登录或注册成功以后，需要删除redis中的短信验证码，验证码只能使用一次
        redis.delete(MOBILE_SMSCODE + ":" + phone);

        return CommonResponse.ok().put("token", token);
    }

    @Override
    public CommonResponse getByToken(String token) {
        return CommonResponse.ok(userService.searchById(jwtUtil.getUserId(token)));
    }

    @Override
    public CommonResponse logout(String token) {
        Long userId = jwtUtil.getUserId(token);
        redis.delete(String.valueOf(userId));
        return CommonResponse.ok();
    }
}
