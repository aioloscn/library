package com.aiolos.library.controller;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.bo.BookInsertBO;
import com.aiolos.library.pojo.bo.BookUpdateBO;
import com.aiolos.library.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    public CommonResponse add(@Valid List<BookInsertBO> bookInsertBOs) throws CustomizeException {
        bookService.add(bookInsertBOs);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse getById(Long id) {
        return CommonResponse.ok(bookService.getById(id));
    }

    @Override
    public List<Book> getBatchIds(List<Long> ids) {
        if (ids == null || ids.size() == 0) {
            return null;
        }
        return bookService.searchBatchIds(ids);
    }

    @Override
    public CommonResponse update(List<BookUpdateBO> bookUpdateBOs) throws CustomizeException {

        // 查询书籍是否存在
        List<Long> boIds = bookUpdateBOs.stream().map(BookUpdateBO::getId).collect(Collectors.toList());
        List<Book> books = bookService.searchBatchIds(boIds);
        List<Long> respIds = books.stream().map(Book::getId).collect(Collectors.toList());
        List<Long> notExistBookIds = new LinkedList<>();

        boIds.forEach(id -> {
            if (!respIds.contains(id)) {
                notExistBookIds.add(id);
            }
        });

        if (notExistBookIds.size() > 0) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST.getErrCode(), "书籍编号" + notExistBookIds.toString() + ErrorEnum.BOOK_DOES_NOT_EXIST.getErrMsg());
        }
        bookService.update(bookUpdateBOs);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse del(List<Long> ids) throws CustomizeException {
        bookService.del(ids);
        return CommonResponse.ok();
    }
}
