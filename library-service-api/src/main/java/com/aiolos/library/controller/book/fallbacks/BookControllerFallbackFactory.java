package com.aiolos.library.controller.book.fallbacks;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.common.utils.PagedResult;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.bo.BookInsertBO;
import com.aiolos.library.pojo.bo.BookUpdateBO;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aiolos
 * @date 2021/4/12 3:23 上午
 */
@Slf4j
@Component
public class BookControllerFallbackFactory implements FallbackFactory<BookControllerApi> {
    @Override
    public BookControllerApi create(Throwable throwable) {
        return new BookControllerApi() {

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
            public CommonResponse addBooks(@Valid List<BookInsertBO> bookInsertBOs) throws CustomizeException {
                return getCause();
            }

            @Override
            public PagedResult getAllBooks(String keyword, Integer category, Integer status, Integer page, Integer pageSize, Integer sort) {
                log.error("执行getAllBooks方法失败，进入降级服务");
                return null;
            }

            @Override
            public CommonResponse list(String keyword, Integer category, Integer status, Integer page, Integer pageSize, Integer sort) {
                return getCause();
            }

            @Override
            public CommonResponse getById(Long id) {
                return getCause();
            }

            @Override
            public List<Book> getBatchIds(List<Long> ids) {
                log.error("执行getBatchIds方法失败，进入降级服务");
                return new LinkedList<>();
            }

            @Override
            public CommonResponse updateBooks(@Valid List<BookUpdateBO> bookUpdateBOs) throws CustomizeException {
                return getCause();
            }

            @Override
            public CommonResponse deleteBooks(List<Long> ids) throws CustomizeException {
                return getCause();
            }

            @Override
            public CommonResponse getSimilarRecommended(Integer classification) {
                return getCause();
            }
        };
    }
}
