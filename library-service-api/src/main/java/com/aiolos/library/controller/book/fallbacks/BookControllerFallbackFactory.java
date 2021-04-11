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
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

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
            @Override
            public CommonResponse addBooks(@Valid List<BookInsertBO> bookInsertBOs) throws CustomizeException {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public PagedResult getAllBooks(String keyword, Integer category, Integer status, Integer page, Integer pageSize, Integer sort) {
                log.error("执行getAllBooks方法失败，进入降级服务");
                return null;
            }

            @Override
            public CommonResponse list(String keyword, Integer category, Integer status, Integer page, Integer pageSize, Integer sort) {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public CommonResponse getById(Long id) {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public List<Book> getBatchIds(List<Long> ids) {
                log.error("执行getBatchIds方法失败，进入降级服务");
                return new LinkedList<>();
            }

            @Override
            public CommonResponse updateBooks(@Valid List<BookUpdateBO> bookUpdateBOs) throws CustomizeException {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public CommonResponse deleteBooks(List<Long> ids) throws CustomizeException {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }

            @Override
            public CommonResponse getSimilarRecommended(Integer classification) {
                return CommonResponse.error(ErrorEnum.FEIGN_FALLBACK_EXCEPTION);
            }
        };
    }
}
