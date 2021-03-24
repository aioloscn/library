package com.aiolos.library.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Aiolos
 * @date 2021/3/20 7:10 下午
 */
@Data
@ApiModel(value = "要删除的订单对象", description = "一个订单包含N个商品，删除时批量删除该订单下的所有商品订单信息")
public class OrderDeleteBO {

    @ApiModelProperty(value = "订单号", required = true)
    @NotNull(message = "订单号不能为空")
    public Long orderNo;

    @ApiModelProperty(value = "用户的主键，不用传")
    public Long userId;
}
