package com.aiolos.library.dao;

import com.aiolos.library.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
    List<ShoppingCart> searchByBookIds(@Param("bookIds") List<Long> bookIds, @Param("userId") Long userId);
}