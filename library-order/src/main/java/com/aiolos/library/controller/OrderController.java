package com.aiolos.library.controller;

import cn.hutool.http.HttpStatus;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.controller.order.OrderControllerApi;
import com.aiolos.library.controller.user.UserControllerApi;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.bo.OrderDeleteBO;
import com.aiolos.library.pojo.bo.OrderInsertBO;
import com.aiolos.library.pojo.bo.OrderReceiptBO;
import com.aiolos.library.pojo.bo.OrderUpdateBO;
import com.aiolos.library.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Aiolos
 * @date 2021/3/21 4:21 下午
 */
@Slf4j
@RestController
public class OrderController extends BaseController implements OrderControllerApi {

    private final UserControllerApi userControllerApi;
    private final BookControllerApi bookControllerApi;
    private final OrderService orderService;

    public OrderController(UserControllerApi userControllerApi, BookControllerApi bookControllerApi, OrderService orderService) {
        this.userControllerApi = userControllerApi;
        this.bookControllerApi = bookControllerApi;
        this.orderService = orderService;
    }

    @Override
    public CommonResponse add(OrderInsertBO orderInsertBO, String token) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        orderInsertBO.setUserId(jwtUtil.getUserId(token));

        // 查询书籍是否存在
        List<Long> boIds = orderInsertBO.getOrderBookInsertBOs().stream().map(OrderInsertBO.OrderBookInsertBO::getBookId).collect(Collectors.toList());
        List<Book> bookResp = bookControllerApi.getBatchIds(boIds);
        List<Long> respIds = bookResp.stream().map(Book::getId).collect(Collectors.toList());
        List<Long> notExistBookIds = new LinkedList<>();

        if (bookResp == null || bookResp.size() == 0) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST.getErrCode(), boIds.toString() + ErrorEnum.BOOK_DOES_NOT_EXIST.getErrMsg());
        } else {
            boIds.forEach(id -> {
                if (!respIds.contains(id)) {
                    notExistBookIds.add(id);
                }
            });
        }

        if (notExistBookIds.size() > 0) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST.getErrCode(), "书籍编号" + notExistBookIds.toString() + ErrorEnum.BOOK_DOES_NOT_EXIST.getErrMsg());
        }

        orderService.add(orderInsertBO);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse get(Long orderNo, String token) {
        return CommonResponse.ok(orderService.getOrderByUser(orderNo, jwtUtil.getUserId(token)));
    }

    @Override
    public CommonResponse getByUser(String token) throws CustomizeException {
        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        return CommonResponse.ok(orderService.getOrderByUser(jwtUtil.getUserId(token)));
    }

    @Override
    public CommonResponse update(OrderUpdateBO orderUpdateBO, String token) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        orderUpdateBO.setUserId(jwtUtil.getUserId(token));

        // 查询书籍是否存在
        List<Long> boIds = orderUpdateBO.getOrderBookUpdateBOs().stream().map(OrderUpdateBO.OrderBookUpdateBO::getBookId).collect(Collectors.toList());
        List<Book> bookResp = bookControllerApi.getBatchIds(boIds);
        List<Long> respIds = bookResp.stream().map(Book::getId).collect(Collectors.toList());
        List<Long> notExistBookIds = new LinkedList<>();

        if (bookResp == null || bookResp.size() == 0) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST.getErrCode(), boIds.toString() + ErrorEnum.BOOK_DOES_NOT_EXIST.getErrMsg());
        } else {
            boIds.forEach(id -> {
                if (!respIds.contains(id)) {
                    notExistBookIds.add(id);
                }
            });
        }

        if (notExistBookIds.size() > 0) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST.getErrCode(), "书籍编号" + notExistBookIds.toString() + ErrorEnum.BOOK_DOES_NOT_EXIST.getErrMsg());
        }

        orderService.update(orderUpdateBO);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse del(OrderDeleteBO orderDeleteBO, String token) throws CustomizeException {
        CommonResponse userResp = userControllerApi.getByToken(token);
        checkIfTheUserExists(userResp);
        orderDeleteBO.setUserId(jwtUtil.getUserId(token));
        orderService.del(orderDeleteBO);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse receipt(OrderReceiptBO orderReceiptBO, String token) throws CustomizeException {
        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        orderService.receipt(orderReceiptBO.getOrderNo(), jwtUtil.getUserId(token));
        return CommonResponse.ok();
    }
}
