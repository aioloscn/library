package com.aiolos.library.controller.shoppingcart;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.pojo.bo.ShoppingCartDeleteBO;
import com.aiolos.library.pojo.bo.ShoppingCartInsertBO;
import com.aiolos.library.pojo.bo.ShoppingCartUpdateBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 3:32 上午
 */
@Api(value = "购物车CRUD")
@RequestMapping("/shoppingCart")
public interface ShoppingCartControllerApi {

    @ApiOperation(value = "商品添加到购物车", httpMethod = "PUT")
    @PutMapping
    CommonResponse add(@RequestBody ShoppingCartInsertBO shoppingCartInsertBO) throws CustomizeException;

    @ApiOperation(value = "查询购物车", httpMethod = "GET")
    @GetMapping("/{userId}")
    CommonResponse getByUserId(@PathVariable("userId") String userId);

    @ApiOperation(value = "修改购物车信息", httpMethod = "UPDATE")
    @PutMapping
    CommonResponse update(@RequestBody List<ShoppingCartUpdateBO> shoppingCartUpdateBOs) throws CustomizeException;

    @ApiOperation(value = "删除购物车中的商品", httpMethod = "DELETE")
    @DeleteMapping
    CommonResponse del(@RequestBody List<ShoppingCartDeleteBO> shoppingCartDeleteBOs) throws CustomizeException;
}
