package com.aiolos.library.service;

import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.pojo.Book;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 6:58 上午
 */
public interface BookService {
    Book getById(String id);

    List<Book> searchBatchIds(List<String> ids);
}
