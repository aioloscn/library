package com.aiolos.library.service;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.library.pojo.ShoppingCart;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import com.aiolos.library.pojo.vo.ShoppingCartBookVO;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 3:26 上午
 */
public interface ShoppingCartService {

    /**
     * 添加到购物车
     * @param shoppingCartInsertBO
     * @return
     */
    ShoppingCart add(ShoppingCartInsertBO shoppingCartInsertBO) throws CustomizeException;

    /**
     * 根据用户主键查询用户的购物车信息
     * @param userId
     * @return
     */
    List<ShoppingCartBookVO> getByUserId(Long userId);

    /**
     * 根据主键集合批量修改
     * @param shoppingCartUpdateBOs
     * @throws CustomizeException
     */
    void update(List<ShoppingCartUpdateBO> shoppingCartUpdateBOs) throws CustomizeException;

    /**
     * 根据主键集合逻辑删除购物车中对应的商品
     * @param shoppingCartDeleteBOs
     * @return
     */
    void del(List<ShoppingCartDeleteBO> shoppingCartDeleteBOs) throws CustomizeException;
}
