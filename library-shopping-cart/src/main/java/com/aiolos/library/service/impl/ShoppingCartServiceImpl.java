package com.aiolos.library.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aiolos.common.enums.BookStatus;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.enums.ShoppingCartStatus;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.utils.CustomizeBeanUtil;
import com.aiolos.library.dao.ShoppingCartDao;
import com.aiolos.library.pojo.ShoppingCart;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import com.aiolos.library.service.BaseService;
import com.aiolos.library.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 3:26 上午
 */
@Slf4j
@Service
public class ShoppingCartServiceImpl extends BaseService implements ShoppingCartService {

    private final ShoppingCartDao shoppingCartDao;

    public ShoppingCartServiceImpl(ShoppingCartDao shoppingCartDao) {
        this.shoppingCartDao = shoppingCartDao;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public ShoppingCart add(ShoppingCartInsertBO shoppingCartInsertBO) throws CustomizeException {

        ShoppingCart shoppingCart = BeanUtil.copyProperties(shoppingCartInsertBO, ShoppingCart.class);
        shoppingCart.setId(snowflake.nextIdStr());
        shoppingCart.setStatus(BookStatus.ON_THE_SHELVES.getType());
        shoppingCart.setGmtCreate(new Date());
        shoppingCart.setGmtModified(new Date());
        int resultCount = shoppingCartDao.insert(shoppingCart);
        if (resultCount != 1) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.ADD_TO_CART_FAILED);
            }
        }

        return shoppingCart;
    }

    @Override
    public List<ShoppingCart> getByUserId(String userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("userId", userId);
        wrapper.eq("status", ShoppingCartStatus.NORMAL.getType());
        return shoppingCartDao.selectList(wrapper);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void update(List<ShoppingCartUpdateBO> shoppingCartUpdateBOs) throws CustomizeException {

        List<ShoppingCart> shoppingCarts = CustomizeBeanUtil.copyListProperties(shoppingCartUpdateBOs, ShoppingCart::new);
        shoppingCarts.forEach(cart -> {
            cart.setGmtModified(new Date());
        });

        boolean result = updateBatchById(shoppingCarts);
        if (!result) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.UPDATE_CART_FAILED);
            }
        }
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void del(List<ShoppingCartDeleteBO> shoppingCartDeleteBOs) throws CustomizeException {

        List<ShoppingCart> shoppingCarts = CustomizeBeanUtil.copyListProperties(shoppingCartDeleteBOs, ShoppingCart::new);
        shoppingCarts.forEach(cart -> {
            cart.setStatus(ShoppingCartStatus.DELETED.getType());
            cart.setGmtModified(new Date());
        });

        boolean result = updateBatchById(shoppingCarts);
        if (!result) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.DELETE_FAILED);
            }
        }
    }
}
