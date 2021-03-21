package com.aiolos.library.controller;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 6:57 上午
 */
@Slf4j
@RestController
public class BookController extends BaseController implements BookControllerApi {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Override
    public CommonResponse getById(String id) {
        return CommonResponse.ok(bookService.getById(id));
    }

    @Override
    public CommonResponse searchBatchIds(List<String> ids) {
        List<String> notExistIds = new LinkedList<>();
        ids.forEach(id -> {
            if (StringUtils.isBlank(id)) {
                notExistIds.add(id);
            }
        });
        String msg = notExistIds.size() > 0 ? notExistIds.toString() + " " + ErrorEnum.BOOK_DOES_NOT_EXIST : StringUtils.EMPTY;
        return CommonResponse.ok(msg, bookService.searchBatchIds(ids));
    }
}
