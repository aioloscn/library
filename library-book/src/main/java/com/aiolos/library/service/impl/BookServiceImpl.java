package com.aiolos.library.service.impl;

import com.aiolos.common.enums.BookStatus;
import com.aiolos.library.dao.BookDao;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 6:58 上午
 */
@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Override
    public Book getById(String id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", id);
        wrapper.eq("status", BookStatus.ON_THE_SHELVES.getType());
        return bookDao.selectOne(wrapper);
    }

    @Override
    public List<Book> searchBatchIds(List<String> ids) {
        return bookDao.selectBatchIds(ids);
    }
}
