package com.aiolos.library.controller.user.fallbacks;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.controller.user.UserControllerApi;
import com.aiolos.library.pojo.bo.RegisterLoginBO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aiolos
 * @date 2021/4/12 1:50 上午
 */
@Slf4j
@Component
public class UserControllerFallbackFactory implements FallbackFactory<UserControllerApi> {
    @Override
    public UserControllerApi create(Throwable throwable) {
        return new UserControllerApi() {

            private CommonResponse getCause() {
                log.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION + ": " + throwable.getMessage());
                Pattern pattern = Pattern.compile("(?<=\"msg\":\")(.*?)(?=\")");
                Matcher matcher = pattern.matcher(throwable.getMessage());
                String cause = StringUtils.EMPTY;
                if (matcher.find()) {
                    cause = matcher.group(0);
                }
                log.error("Connection refused, enter the degraded method of the service caller");
                return CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), cause);
            }

            @Override
            public CommonResponse getSMSCode(String phone, HttpServletRequest request) throws CustomizeException {
                return getCause();
            }

            @Override
            public CommonResponse login(@Valid RegisterLoginBO registerLoginBO) throws CustomizeException {
                return getCause();
            }

            @Override
            public CommonResponse getByToken(String token) {
                return getCause();
            }

            @Override
            public CommonResponse getUserPermissions(Long userId) {
                return getCause();
            }

            @Override
            public CommonResponse logout(String token) {
                return getCause();
            }
        };
    }
}
