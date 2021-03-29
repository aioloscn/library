package com.aiolos.library.service;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.utils.PagedResult;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.bo.BookInsertBO;
import com.aiolos.library.pojo.bo.BookUpdateBO;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 6:58 上午
 */
public interface BookService {

    void add(List<BookInsertBO> bookInsertBOs) throws CustomizeException;

    PagedResult getAllBooks(String keyword, Integer category, Integer page, Integer pageSize);

    Book getById(Long id);

    List<Book> searchBatchIds(List<Long> ids);

    void update(List<BookUpdateBO> bookUpdateBOs) throws CustomizeException;

    void del(List<Long> ids) throws CustomizeException;
}
