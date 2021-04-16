package com.aiolos.library.controller.shoppingcart;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 3:32 上午
 */
@Api(tags = "购物车CRUD")
@RequestMapping("/shoppingCart")
public interface ShoppingCartControllerApi {

    @ApiOperation(value = "商品添加到购物车", httpMethod = "POST")
    @PostMapping
    CommonResponse add(@Valid @RequestBody ShoppingCartInsertBO shoppingCartInsertBO, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "查询购物车", httpMethod = "GET")
    @GetMapping
    CommonResponse getByUser(@ApiParam(value = "用户的主键", required = true) @RequestHeader("token") String token);

    @ApiOperation(value = "根据用户信息和书籍Id获取部分购物车信息", notes = "用于全选/部分购物车信息跳转到结算界面", httpMethod = "GET")
    @GetMapping("/getByBookIds")
    CommonResponse getByBookIds(@ApiParam(value = "书籍主键数组") @RequestParam(required = false) Long[] bookIds, @RequestHeader("token") String token);

    @ApiOperation(value = "修改购物车信息", httpMethod = "PUT")
    @PutMapping
    CommonResponse update(@Valid @RequestBody List<ShoppingCartUpdateBO> shoppingCartUpdateBOs, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "删除购物车中的商品", notes = "批量删除某个用户的N个购物车信息", httpMethod = "DELETE")
    @DeleteMapping
    CommonResponse del(@Valid @RequestBody List<ShoppingCartDeleteBO> shoppingCartDeleteBOs, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "删除书籍对应的所有购物车信息", notes = "用于前端合并展示的购物车信息删除", httpMethod = "DELETE")
    @DeleteMapping("/deleteByBookId/{bookId}")
    CommonResponse deleteByBookId(@PathVariable("bookId") Long bookId, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "下单完成后删除购物车信息", notes = "用户Order模块远程调用", httpMethod = "DELETE")
    @DeleteMapping("/deleteByBookIds")
    CommonResponse deleteByBookIds(@ApiParam(value = "书籍主键数组") @RequestParam(required = false) Long[] bookIds, @RequestHeader("token") String token) throws CustomizeException;
}
