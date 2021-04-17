package com.aiolos.library.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.common.utils.CustomizeBeanUtil;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.controller.shoppingcart.ShoppingCartControllerApi;
import com.aiolos.library.controller.user.UserControllerApi;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.ShoppingCart;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateQuantityBO;
import com.aiolos.library.pojo.vo.ShoppingCartBookVO;
import com.aiolos.library.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Aiolos
 * @date 2021/3/20 4:21 上午
 */
@Slf4j
@RestController
public class ShoppingCartController extends BaseController implements ShoppingCartControllerApi {

    private final UserControllerApi userControllerApi;
    private final BookControllerApi bookControllerApi;
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(UserControllerApi userControllerApi, BookControllerApi bookControllerApi, ShoppingCartService shoppingCartService) {
        this.userControllerApi = userControllerApi;
        this.bookControllerApi = bookControllerApi;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    public CommonResponse add(ShoppingCartInsertBO shoppingCartInsertBO, String token) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        shoppingCartInsertBO.setUserId(jwtUtil.getUserId(token));

        // 查询书籍是否存在
        CommonResponse bookResp = bookControllerApi.getById(shoppingCartInsertBO.getBookId());
        if (bookResp.getCode() == null || bookResp.getCode() != HttpStatus.HTTP_OK) {
            return bookResp;
        }
        checkIfTheBookExists(bookResp);

        Book book = BeanUtil.copyProperties(bookResp.getData(), Book.class);
        BigDecimal amount = new BigDecimal(shoppingCartInsertBO.getQuantity()).multiply(book.getSellingPrice());
        shoppingCartInsertBO.setAmount(amount);
        ShoppingCart shoppingCart = shoppingCartService.add(shoppingCartInsertBO);
        return CommonResponse.ok(shoppingCart);
    }

    @Override
    public CommonResponse getByUser(String token) {
        List<ShoppingCartBookVO> shoppingCartBookVOs = shoppingCartService.getByUserId(jwtUtil.getUserId(token));
        return CommonResponse.ok(shoppingCartBookVOs);
    }

    @Override
    public CommonResponse getByBookIds(Long[] bookIds, String token) {
        List<ShoppingCartBookVO> shoppingCartBookVOs;
        if (ObjectUtil.isNull(bookIds) || bookIds.length == 0) {
            shoppingCartBookVOs = shoppingCartService.getByUserId(jwtUtil.getUserId(token));
        } else {
            shoppingCartBookVOs = shoppingCartService.getByBookIds(Arrays.asList(bookIds), jwtUtil.getUserId(token));
        }
        return CommonResponse.ok(shoppingCartBookVOs);
    }

    @Override
    public CommonResponse update(List<ShoppingCartUpdateBO> shoppingCartUpdateBOs, String token) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        Long userId = jwtUtil.getUserId(token);
        shoppingCartUpdateBOs.forEach(c -> c.setUserId(userId));

        // 查询书籍是否存在
        List<Long> boIds = shoppingCartUpdateBOs.stream().map(ShoppingCartUpdateBO::getBookId).collect(Collectors.toList());
        List<Long> notExistBookIds = new LinkedList<>();
        List<Book> bookResp = bookControllerApi.getBatchIds(boIds);
        List<Long> respIds = bookResp.stream().map(Book::getId).collect(Collectors.toList());

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

        shoppingCartService.update(shoppingCartUpdateBOs);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse updateBookQuantity(@Valid ShoppingCartUpdateQuantityBO shoppingCartUpdateQuantityBO, String token) throws CustomizeException {
        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        Long userId = jwtUtil.getUserId(token);
        List<ShoppingCartBookVO> shoppingCartBookVOs = shoppingCartService.getByBookIds(new ArrayList(Collections.singleton(shoppingCartUpdateQuantityBO.getBookId())), userId);
        if (shoppingCartBookVOs == null || shoppingCartBookVOs.size() == 0) {
            return CommonResponse.error(ErrorEnum.UPDATE_CART_FAILED);
        }

        int total = shoppingCartBookVOs.stream().mapToInt(book -> book.getQuantity()).sum();
        if (shoppingCartUpdateQuantityBO.getQuantity().equals(total)) {
            return CommonResponse.ok();
        } else if (shoppingCartUpdateQuantityBO.getQuantity() > total) {
            // TODO 从redis中获取
            List<Book> books = bookControllerApi.getBatchIds(Collections.singletonList(shoppingCartUpdateQuantityBO.getBookId()));
            if (books == null || books.size() == 0) {
                return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST);
            }
            Book book = books.get(0);
            ShoppingCartBookVO shoppingCartBookVO = shoppingCartBookVOs.get(0);
            ShoppingCart shoppingCart = BeanUtil.copyProperties(shoppingCartBookVO, ShoppingCart.class);
            Integer newQuantity = shoppingCartUpdateQuantityBO.getQuantity() - total + shoppingCart.getQuantity();
            shoppingCart.setQuantity(newQuantity);
            shoppingCart.setAmount(new BigDecimal(newQuantity).multiply(book.getSellingPrice()));
            shoppingCart.setGmtModified(new Date());
            shoppingCartService.update(shoppingCart);
        } else {
            List<ShoppingCart> shoppingCarts = CustomizeBeanUtil.copyListProperties(shoppingCartBookVOs, ShoppingCart::new);
            List<Long> bookIds = shoppingCarts.stream().map(ShoppingCart::getBookId).collect(Collectors.toList());
            // TODO 从redis中获取
            List<Book> books = bookControllerApi.getBatchIds(bookIds);
            for (ShoppingCart cart : shoppingCarts) {
                Integer newQuantity = cart.getQuantity() < total ? 0 : cart.getQuantity() - total;
                total = total < cart.getQuantity() ? 0 : total - cart.getQuantity();
                cart.setQuantity(newQuantity);
                if (total != 0) {
                    List<Book> bookList = books.stream().filter(book -> book.getId().equals(cart.getBookId())).collect(Collectors.toList());
                    if (bookList == null || bookList.size() == 0) {
                        try {
                            throw new RuntimeException();
                        } catch (Exception e) {
                            throw new CustomizeException(ErrorEnum.BOOK_DOES_NOT_EXIST);
                        }
                    }
                    Book book = bookList.get(0);
                    cart.setAmount(new BigDecimal(newQuantity).multiply(book.getSellingPrice()));
                } else {
                    cart.setAmount(BigDecimal.ZERO);
                }
                cart.setGmtModified(new Date());
                shoppingCartService.update(cart);
                if (total == 0) {
                    break;
                }
            }
        }

        return CommonResponse.ok();
    }

    @Override
    public CommonResponse del(List<ShoppingCartDeleteBO> shoppingCartDeleteBOs, String token) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        Long userId = jwtUtil.getUserId(token);
        shoppingCartDeleteBOs.forEach(c -> c.setUserId(userId));

        shoppingCartService.del(shoppingCartDeleteBOs);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse deleteByBookId(Long bookId, String token) throws CustomizeException {
        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        Long userId = jwtUtil.getUserId(token);
        shoppingCartService.deleteByBookId(bookId, userId);
        return CommonResponse.ok();
    }

    @Override
    public CommonResponse deleteByBookIds(Long[] bookIds, String token) throws CustomizeException {
        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        if (userResp.getCode() == null || userResp.getCode() != HttpStatus.HTTP_OK) {
            return userResp;
        }
        checkIfTheUserExists(userResp);
        Long userId = jwtUtil.getUserId(token);
        if (ObjectUtil.isNull(bookIds) || bookIds.length == 0) {
            List<Long> bookIdList = shoppingCartService.getCartBookIdsByUserId(userId);
            for (Long bookId : bookIdList) {
                shoppingCartService.deleteByBookId(bookId, userId);
            }
        }
        for (Long bookId : bookIds) {
            shoppingCartService.deleteByBookId(bookId, userId);
        }
        return CommonResponse.ok();
    }
}
