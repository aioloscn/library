package com.aiolos.library.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aiolos.common.enums.BookStatus;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.enums.ShoppingCartStatus;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.utils.CustomizeBeanUtil;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.dao.ShoppingCartDao;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.ShoppingCart;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import com.aiolos.library.pojo.vo.ShoppingCartBookVO;
import com.aiolos.library.service.BaseService;
import com.aiolos.library.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Aiolos
 * @date 2021/3/20 3:26 上午
 */
@Slf4j
@Service
public class ShoppingCartServiceImpl extends BaseService implements ShoppingCartService {

    private final ShoppingCartDao shoppingCartDao;
    private final BookControllerApi bookControllerApi;

    public ShoppingCartServiceImpl(ShoppingCartDao shoppingCartDao, BookControllerApi bookControllerApi) {
        this.shoppingCartDao = shoppingCartDao;
        this.bookControllerApi = bookControllerApi;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public ShoppingCart add(ShoppingCartInsertBO shoppingCartInsertBO) throws CustomizeException {

        ShoppingCart shoppingCart = BeanUtil.copyProperties(shoppingCartInsertBO, ShoppingCart.class);
        shoppingCart.setId(idWorker.nextId());
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
    public List<ShoppingCartBookVO> getByUserId(Long userId) {
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper();
        wrapper.eq("user_id", userId);
        wrapper.eq("status", ShoppingCartStatus.NORMAL.getType());
        List<ShoppingCart> shoppingCarts = shoppingCartDao.selectList(wrapper);
        // 合并购物车中同一本书，并合计购买数和金额
        List<ShoppingCart> newShoppingCarts = new LinkedList<>();
        shoppingCarts.parallelStream().collect(Collectors.groupingBy(c -> (c.getBookId()), Collectors.toList())).forEach(
                (q, transfer) -> {
                    transfer.stream().reduce((a, b) -> new ShoppingCart(
                            a.getId(), a.getBookId(), a.getUserId(),
                            a.getQuantity() + b.getQuantity(),
                            a.getAmount().add(b.getAmount()),
                            a.getStatus(), a.getGmtCreate(), a.getGmtModified())).ifPresent(newShoppingCarts::add);
                }
        );

        List<Long> bookIds = newShoppingCarts.stream().map(ShoppingCart::getBookId).distinct().collect(Collectors.toList());
        List<Book> books = bookControllerApi.getBatchIds(bookIds);
        List<ShoppingCartBookVO> shoppingCartBookVOs = CustomizeBeanUtil.copyListProperties(newShoppingCarts, ShoppingCartBookVO::new);
        shoppingCartBookVOs.forEach(c -> {
            c.setBookIdStr(c.getBookId().toString());
            books.forEach(b -> {
                if (c.getBookId().equals(b.getId())) {
                    c.setBook(b);
                }
            });
        });
        return shoppingCartBookVOs;
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
        AtomicInteger affectCount = new AtomicInteger();

        shoppingCarts.forEach(cart -> {
            cart.setStatus(ShoppingCartStatus.DELETED.getType());
            cart.setGmtModified(new Date());

            UpdateWrapper<ShoppingCart> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", cart.getId());
            affectCount.addAndGet(shoppingCartDao.update(cart, updateWrapper));
        });

        if (affectCount.intValue() != shoppingCarts.size()) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.DELETE_FAILED);
            }
        }
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void deleteByBookId(Long bookId, Long userId) throws CustomizeException {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setStatus(ShoppingCartStatus.DELETED.getType());
        shoppingCart.setGmtModified(new Date());

        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("book_id", bookId);
        updateWrapper.eq("user_id", userId);
        int affectCount = shoppingCartDao.update(shoppingCart, updateWrapper);
        if (affectCount == 0) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.DELETE_FAILED);
            }
        }
    }
}
