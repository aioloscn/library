package com.aiolos.library.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.enums.OrderStatus;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.utils.CustomizeBeanUtil;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.dao.OrderFormDao;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.OrderForm;
import com.aiolos.library.pojo.bo.OrderDeleteBO;
import com.aiolos.library.pojo.bo.OrderInsertBO;
import com.aiolos.library.pojo.bo.OrderUpdateBO;
import com.aiolos.library.pojo.vo.OrderFormVO;
import com.aiolos.library.service.BaseService;
import com.aiolos.library.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Aiolos
 * @date 2021/3/21 4:23 下午
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseService implements OrderService {

    private final OrderFormDao orderFormDao;
    private final BookControllerApi bookControllerApi;

    public OrderServiceImpl(OrderFormDao orderFormDao, BookControllerApi bookControllerApi) {
        this.orderFormDao = orderFormDao;
        this.bookControllerApi = bookControllerApi;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void add(OrderInsertBO orderInsertBO) throws CustomizeException {

        // 生成订单号
        Long orderNo = idWorker.nextId();

        OrderForm orderForm = BeanUtil.copyProperties(orderInsertBO, OrderForm.class);
        orderForm.setOrderNo(orderNo);
        orderForm.setStatus(OrderStatus.PAID.getType());
        orderForm.setGmtCreate(new Date());
        orderForm.setGmtModified(new Date());

        // 取出订单中的每一个书籍对象各生成一条订单数据
        List<OrderInsertBO.OrderBookInsertBO> orderBooks = orderInsertBO.getOrderBookInsertBOs();
        AtomicInteger resultCount = new AtomicInteger();
        orderBooks.forEach(book -> {
            Long id = idWorker.nextId();
            OrderForm copyOrder = SerializationUtils.clone(orderForm);
            copyOrder.setId(id);
            copyOrder.setBookId(book.getBookId());
            copyOrder.setQuantity(book.getQuantity());
            copyOrder.setAmount(book.getAmount());
            resultCount.addAndGet(orderFormDao.insert(copyOrder));
        });

        if (resultCount.intValue() != orderBooks.size()) {
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
        wrapper.eq("order_no", orderNo);
        wrapper.eq("user_id", userId);
        wrapper.ne("status", OrderStatus.DELETED.getType());
        return orderFormDao.selectList(wrapper);
    }

    @Override
    public List<OrderFormVO> getOrderByUser(Long userId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.ne("status", OrderStatus.DELETED.getType());
        List<OrderForm> orders = orderFormDao.selectList(queryWrapper);
        List<OrderFormVO> orderFormVOs = CustomizeBeanUtil.copyListProperties(orders, OrderFormVO::new);
        List<Long> bookIds = orders.stream().map(OrderForm::getBookId).collect(Collectors.toList());
        List<Book> books = bookControllerApi.getBatchIds(bookIds);
        orderFormVOs.forEach(o -> {
            o.setIdStr(o.getId().toString());
            o.setBookIdStr(o.getBookId().toString());
            o.setOrderNoStr(o.getOrderNo().toString());
            if (o.getStatus().equals(OrderStatus.UNPAID.getType())) {
                o.setOrderStatus(OrderStatus.UNPAID.getValue());
            } else if (o.getStatus().equals(OrderStatus.PAID.getType())) {
                o.setOrderStatus(OrderStatus.PAID.getValue());
            } else if (o.getStatus().equals(OrderStatus.RECEIVED.getType())) {
                o.setOrderStatus(OrderStatus.RECEIVED.getValue());
            } else {
                o.setOrderStatus(OrderStatus.DELETED.getValue());
            }
            books.forEach(b -> {
                if (o.getBookId().equals(b.getId())) {
                    o.setBook(b);
                }
            });
        });
        return orderFormVOs;
    }

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void update(OrderUpdateBO orderUpdateBO) throws CustomizeException {

        // 取出订单中的每一个书籍对象修改每一条订单数据
        List<OrderUpdateBO.OrderBookUpdateBO> orderBooks = orderUpdateBO.getOrderBookUpdateBOs();
        OrderForm orderForm = BeanUtil.copyProperties(orderUpdateBO, OrderForm.class);
        UpdateWrapper wrapper = new UpdateWrapper();
        wrapper.eq("order_no", orderUpdateBO.getOrderNo());

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
        queryWrapper.eq("order_no", orderDeleteBO.getOrderNo());
        List<OrderForm> orders = orderFormDao.selectList(queryWrapper);

        // where条件
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("order_no", orderDeleteBO.getOrderNo());
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

    @Transactional(propagation = Propagation.NESTED, rollbackFor = CustomizeException.class)
    @Override
    public void receipt(Long orderNo, Long userId) throws CustomizeException {
        UpdateWrapper updateWrapper = new UpdateWrapper();
        updateWrapper.eq("order_no", orderNo);
        OrderForm orderForm = new OrderForm();
        orderForm.setStatus(OrderStatus.RECEIVED.getType());
        orderForm.setGmtModified(new Date());
        int affectedCount = orderFormDao.update(orderForm, updateWrapper);
        if (affectedCount == 0) {
            try {
                throw new RuntimeException();
            } catch (Exception e) {
                throw new CustomizeException(ErrorEnum.RECEIPT_FAILED);
            }
        }
    }
}
