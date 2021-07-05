package com.aiolos.library.controller.order;

import com.aiolos.common.exception.CustomizeException;
import com.aiolos.common.response.CommonResponse;
import com.aiolos.library.pojo.bo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Aiolos
 * @date 2021/3/21 4:20 下午
 */
@Api(tags = "订单相关功能接口")
@RequestMapping("/order")
public interface OrderControllerApi {

    @ApiOperation(value = "下单", httpMethod = "POST")
    @PostMapping
    CommonResponse add(@ApiParam(value = "订单信息，包含N个书籍对象信息") @Valid @RequestBody OrderInsertBO orderInsertBO, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "查看用户的某个订单", httpMethod = "GET")
    @GetMapping("/get/{orderNo}")
    CommonResponse get(@ApiParam(value = "订单号") @PathVariable("orderNo") Long orderNo, @RequestHeader("token") String token);

    @ApiOperation(value = "查看用户的所有订单", httpMethod = "GET")
    @GetMapping("/getByUser")
    CommonResponse getByUser(@ApiParam(value = "用户token") @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "修改订单信息", httpMethod = "PUT")
    @PutMapping
    CommonResponse update(@Valid @RequestBody OrderUpdateBO orderUpdateBO, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "删除订单", httpMethod = "DELETE")
    @DeleteMapping
    CommonResponse del(@Valid @RequestBody OrderDeleteBO orderDeleteBO, @RequestHeader("token") String token) throws CustomizeException;

    @ApiOperation(value = "确认收货", httpMethod = "PUT")
    @PutMapping("/receipt")
    CommonResponse receipt(@Valid @RequestBody OrderReceiptBO orderReceiptBO, @RequestHeader("token") String token) throws CustomizeException;
}
