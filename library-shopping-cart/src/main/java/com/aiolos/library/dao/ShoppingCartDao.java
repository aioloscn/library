package com.aiolos.library.dao;

import com.aiolos.library.pojo.ShoppingCart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}