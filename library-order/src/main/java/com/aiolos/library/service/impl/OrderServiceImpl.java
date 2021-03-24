package com.aiolos.library.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.enums.OrderStatus;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.library.dao.OrderFormDao;
import com.aiolos.library.pojo.OrderForm;
import com.aiolos.library.pojo.bo.OrderDeleteBO;
import com.aiolos.library.pojo.bo.OrderInsertBO;
import com.aiolos.library.pojo.bo.OrderUpdateBO;
import com.aiolos.library.service.BaseService;
import com.aiolos.library.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/21 4:23 下午
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    private final OrderFormDao orderFormDao;

    public OrderServiceImpl(OrderFormDao orderFormDao) {
        this.orderFormDao = orderFormDao;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void add(OrderInsertBO orderInsertBO) throws CustomizeException {

        // 生成订单号
        Long orderNo = idWorker.nextId();

        List<OrderForm> orderForms = new ArrayList<>();
        OrderForm orderForm = BeanUtil.copyProperties(orderInsertBO, OrderForm.class);
        orderForm.setOrderNo(orderNo);
        orderForm.setStatus(OrderStatus.UNPAID.getType());
        orderForm.setGmtCreate(new Date());
        orderForm.setGmtModified(new Date());

        // 取出订单中的每一个书籍对象各生成一条订单数据
        List<OrderInsertBO.OrderBookInsertBO> orderBooks = orderInsertBO.getOrderBookInsertBOs();
        orderBooks.forEach(book -> {
            Long id = idWorker.nextId();
            orderForm.setId(id);
            orderForm.setBookId(book.getBookId());
            orderForm.setQuantity(book.getQuantity());
            orderForm.setAmount(book.getAmount());
            orderForms.add(orderForm);
        });

        boolean result = saveBatch(orderForms);
        if (!result) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.ADD_ORDER_FAILED);
            }
        }
    }

    @Override
    public List<OrderForm> getOrderByUser(Long orderNo, Long userId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("orderNo", orderNo);
        wrapper.eq("userId", userId);
        return orderFormDao.selectList(wrapper);
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void update(OrderUpdateBO orderUpdateBO) throws CustomizeException {

        // 取出订单中的每一个书籍对象修改每一条订单数据
        List<OrderUpdateBO.OrderBookUpdateBO> orderBooks = orderUpdateBO.getOrderBookUpdateBOs();
        OrderForm orderForm = BeanUtil.copyProperties(orderUpdateBO, OrderForm.class);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("orderNo", orderUpdateBO.getOrderNo());

        // 只有orderNo没有每个订单的id，所以循环update
        for (OrderUpdateBO.OrderBookUpdateBO book : orderBooks) {
            orderForm.setBookId(book.getBookId());
            orderForm.setQuantity(book.getQuantity());
            orderForm.setAmount(book.getAmount());
            orderForm.setGmtModified(new Date());
            int updateCount = orderFormDao.update(orderForm, wrapper);
            if (updateCount != 1) {
                try {
                    throw new RuntimeException();
                } catch (Exception e) {
                    throw new CustomizeException(ErrorEnum.UPDATE_ORDER_FAILED, "订单号: " + orderForm.getOrderNo() + ErrorEnum.UPDATE_ORDER_FAILED);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void del(OrderDeleteBO orderDeleteBO) throws CustomizeException {

        // 根据orderNo查询有多少条订单明细，修改的数量要和订单明细数一致
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("orderNo", orderDeleteBO.getOrderNo());
        List<OrderForm> orders = orderFormDao.selectList(queryWrapper);

        // where条件
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("orderNo", orderDeleteBO.getOrderNo());
        // 要修改的entity
        OrderForm orderForm = new OrderForm();
        orderForm.setStatus(OrderStatus.DELETED.getType());
        orderForm.setGmtModified(new Date());
        int updateCount = orderFormDao.update(orderForm, updateWrapper);
        if (updateCount != orders.size()) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.DELETE_ORDER_FAILED);
            }
        }
    }
}
