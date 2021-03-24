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

    @ApiOperation(value = "修改购物车信息", httpMethod = "PUT")
    @PutMapping
    CommonResponse update(@Valid @RequestBody List<ShoppingCartUpdateBO> shoppingCartUpdateBOs, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "删除购物车中的商品", httpMethod = "DELETE")
    @DeleteMapping
    CommonResponse del(@Valid @RequestBody List<ShoppingCartDeleteBO> shoppingCartDeleteBOs, @RequestHeader("token") String token) throws CustomizeException;
}
