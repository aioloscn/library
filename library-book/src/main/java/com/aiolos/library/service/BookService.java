package com.aiolos.library.service;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.common.utils.PagedResult;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.bo.BookInsertBO;
import com.aiolos.library.pojo.bo.BookUpdateBO;
import com.aiolos.library.pojo.vo.AllBooksVO;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 6:58 上午
 */
public interface BookService {

    void add(List<BookInsertBO> bookInsertBOs) throws CustomizeException;

    PagedResult getAllBooks(String keyword, Integer category, Integer status, Integer page, Integer pageSize, Integer sort);

    AllBooksVO getById(Long id);

    List<Book> searchBatchIds(List<Long> ids);

    void update(List<BookUpdateBO> bookUpdateBOs) throws CustomizeException;

    void del(List<Long> ids) throws CustomizeException;

    List<AllBooksVO> getSimilarRecommended(Integer classification);
}
