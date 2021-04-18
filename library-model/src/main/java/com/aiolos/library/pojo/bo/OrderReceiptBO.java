package com.aiolos.library.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Aiolos
 * @date 2021/4/18 2:42 下午
 */
@Data
@ApiModel(value = "订单确认收货的对象")
public class OrderReceiptBO {

    @ApiModelProperty(value = "订单号", required = true)
    @NotNull(message = "订单号不能为空")
    private Long orderNo;
}
