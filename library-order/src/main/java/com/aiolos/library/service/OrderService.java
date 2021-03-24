package com.aiolos.library.service;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.pojo.OrderForm;
import com.aiolos.library.pojo.bo.OrderDeleteBO;
import com.aiolos.library.pojo.bo.OrderInsertBO;
import com.aiolos.library.pojo.bo.OrderUpdateBO;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/21 4:22 下午
 */
public interface OrderService {

    /**
     * 下单
     * @param orderInsertBO
     * @throws CustomizeException
     */
    void add(OrderInsertBO orderInsertBO) throws CustomizeException;

    /**
     * 查询当前用户的某个订单
     * @param orderNo   订单号
     * @param userId    用户主键
     * @return
     */
    List<OrderForm> getOrderByUser(Long orderNo, Long userId);

    void update(OrderUpdateBO orderUpdateBO) throws CustomizeException;

    void del(OrderDeleteBO orderDeleteBO) throws CustomizeException;
}
