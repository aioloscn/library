package com.aiolos.library.controller;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.controller.shoppingcart.ShoppingCartControllerApi;
import com.aiolos.library.controller.user.UserControllerApi;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.ShoppingCart;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import com.aiolos.library.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
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
        checkIfTheUserExists(userResp);
        shoppingCartInsertBO.setUserId(jwtUtil.getUserId(token));

        // 查询书籍是否存在
        CommonResponse bookResp = bookControllerApi.getById(shoppingCartInsertBO.getBookId());
        checkIfTheBookExists(bookResp);

        ShoppingCart shoppingCart = shoppingCartService.add(shoppingCartInsertBO);
        return CommonResponse.ok(shoppingCart);
    }

    @Override
    public CommonResponse getByUser(String token) {
        List<ShoppingCart> shoppingCart = shoppingCartService.getByUserId(jwtUtil.getUserId(token));
        return CommonResponse.ok(shoppingCart);
    }

    @Override
    public CommonResponse update(List<ShoppingCartUpdateBO> shoppingCartUpdateBOs, String token) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
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
    public CommonResponse del(List<ShoppingCartDeleteBO> shoppingCartDeleteBOs, String token) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getByToken(token);
        checkIfTheUserExists(userResp);
        Long userId = jwtUtil.getUserId(token);
        shoppingCartDeleteBOs.forEach(c -> c.setUserId(userId));

        shoppingCartService.del(shoppingCartDeleteBOs);
        return CommonResponse.ok();
    }
}
