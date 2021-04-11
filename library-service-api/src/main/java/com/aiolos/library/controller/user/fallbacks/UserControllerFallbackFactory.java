package com.aiolos.library.controller.user.fallbacks;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.controller.user.UserControllerApi;
import com.aiolos.library.pojo.bo.RegisterLoginBO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
            @Override
            public CommonResponse getSMSCode(String phone, HttpServletRequest request) throws CustomizeException {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public CommonResponse login(@Valid RegisterLoginBO registerLoginBO) throws CustomizeException {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public CommonResponse getByToken(String token) {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public CommonResponse logout(String token) {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }
        };
    }
}
