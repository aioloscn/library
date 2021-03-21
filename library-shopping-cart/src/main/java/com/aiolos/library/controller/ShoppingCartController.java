package com.aiolos.library.controller;

import com.aiolos.common.enums.ErrorEnum;
import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.controller.book.BookControllerApi;
import com.aiolos.library.controller.shoppingcart.ShoppingCartControllerApi;
import com.aiolos.library.controller.user.UserControllerApi;
import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.ShoppingCart;
import com.aiolos.library.pojo.User;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import com.aiolos.library.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

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
    public CommonResponse add(ShoppingCartInsertBO shoppingCartInsertBO) throws CustomizeException {

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.getById(shoppingCartInsertBO.getUserId());
        if (userResp == null || userResp.getData() == null || !(userResp.getData() instanceof User)) {
            return CommonResponse.error(ErrorEnum.USER_DOES_NOT_EXIST);
        }

        // 查询书籍是否存在
        CommonResponse bookResp = bookControllerApi.getById(shoppingCartInsertBO.getBookId());
        if (bookResp == null || userResp.getData() == null || !(bookResp.getData() instanceof Book)) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST);
        }

        ShoppingCart shoppingCart = shoppingCartService.add(shoppingCartInsertBO);
        return CommonResponse.ok(shoppingCart);
    }

    @Override
    public CommonResponse getByUserId(String userId) {
        List<ShoppingCart> shoppingCart = shoppingCartService.getByUserId(userId);
        return CommonResponse.ok(shoppingCart);
    }

    @Override
    public CommonResponse update(List<ShoppingCartUpdateBO> shoppingCartUpdateBOs) throws CustomizeException {

        List<String> userIds = new LinkedList<>();
        List<String> bookIds = new LinkedList<>();
        shoppingCartUpdateBOs.forEach(bo -> {
            userIds.add(bo.getUserId());
            bookIds.add(bo.getBookId());
        });

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.searchBatchIds(userIds);
        if (userResp == null || userResp.getData() == null || !(userResp.getData() instanceof List) || ((List<User>) userResp.getData()).size() == 0) {
            return CommonResponse.error(ErrorEnum.USER_DOES_NOT_EXIST);
        }

        // 查询书籍是否存在
        CommonResponse bookResp = bookControllerApi.searchBatchIds(bookIds);
        if (bookResp == null || bookResp.getData() == null || !(bookResp.getData() instanceof List) || ((List<Book>) bookResp.getData()).size() == 0) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST);
        }

        shoppingCartService.update(shoppingCartUpdateBOs);
        return null;
    }

    @Override
    public CommonResponse del(List<ShoppingCartDeleteBO> shoppingCartDeleteBOs) throws CustomizeException {

        List<String> userIds = new LinkedList<>();
        List<String> bookIds = new LinkedList<>();
        shoppingCartDeleteBOs.forEach(bo -> {
            userIds.add(bo.getUserId());
            bookIds.add(bo.getBookId());
        });

        // 查询用户是否存在
        CommonResponse userResp = userControllerApi.searchBatchIds(userIds);
        if (userResp == null || userResp.getData() == null || !(userResp.getData() instanceof List) || ((List<User>) userResp.getData()).size() == 0) {
            return CommonResponse.error(ErrorEnum.USER_DOES_NOT_EXIST);
        }

        // 查询书籍是否存在
        CommonResponse bookResp = bookControllerApi.searchBatchIds(bookIds);
        if (bookResp == null || bookResp.getData() == null || !(bookResp.getData() instanceof List) || ((List<Book>) bookResp.getData()).size() == 0) {
            return CommonResponse.error(ErrorEnum.BOOK_DOES_NOT_EXIST);
        }

        shoppingCartService.del(shoppingCartDeleteBOs);
        return CommonResponse.ok();
    }
}
