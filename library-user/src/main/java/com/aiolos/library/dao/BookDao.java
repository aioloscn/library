package com.aiolos.library.dao;

import com.aiolos.library.pojo.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDao extends BaseMapper<Book> {
}