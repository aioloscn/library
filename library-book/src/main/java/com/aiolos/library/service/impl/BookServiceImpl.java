package com.aiolos.library.service.impl;

import com.aiolos.common.enums.BookStatus;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.utils.CustomizeBeanUtil;
import com.aiolos.library.dao.BookDao;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.bo.BookInsertBO;
import com.aiolos.library.pojo.bo.BookUpdateBO;
import com.aiolos.library.service.BaseService;
import com.aiolos.library.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 6:58 上午
 */
@Slf4j
@Service
public class BookServiceImpl extends BaseService implements BookService {

    private final BookDao bookDao;

    public BookServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void add(List<BookInsertBO> bookInsertBOs) throws CustomizeException {
        List<Book> books = CustomizeBeanUtil.copyListProperties(bookInsertBOs, Book::new);
        books.forEach(book -> {
            book.setId(idWorker.nextId());
            book.setSalesVolume(0);
            book.setStatus(BookStatus.NOT_ON_THE_SHELVES.getType());
            book.setGmtCreate(new Date());
            book.setGmtModified(new Date());
        });
        boolean result = saveBatch(books);
        if (!result) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.ADD_BOOK_FAILED);
            }
        }
    }

    @Override
    public Book getById(Long id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("id", id);
        wrapper.eq("status", BookStatus.ON_THE_SHELVES.getType());
        return bookDao.selectOne(wrapper);
    }

    @Override
    public List<Book> searchBatchIds(List<Long> ids) {
        return bookDao.selectBatchIds(ids);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void update(List<BookUpdateBO> bookUpdateBOs) throws CustomizeException {

        List<Book> books = CustomizeBeanUtil.copyListProperties(bookUpdateBOs, Book::new);
        books.forEach(book -> {
            book.setGmtModified(new Date());
        });
        boolean result = updateBatchById(books);
        if (!result) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.UPDATE_BOOK_FAILED);
            }
        }
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void del(List<Long> ids) throws CustomizeException {
        boolean result = removeByIds(ids);
        if (!result) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.DELETE_BOOK_FAILED);
            }
        }
    }
}
