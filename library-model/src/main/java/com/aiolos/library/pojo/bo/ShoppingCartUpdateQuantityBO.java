package com.aiolos.library.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author Aiolos
 * @date 2021/4/17 10:38 下午
 */
@Data
@ApiModel(value = "购物车修改单个商品数量的对象")
public class ShoppingCartUpdateQuantityBO {

    @ApiModelProperty(value = "书籍主键", required = true)
    @NotNull(message = "书籍主键不能为空")
    private Long bookId;

    @ApiModelProperty(value = "新的商品数量", required = true)
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量不能小于1")
    private Integer quantity;
}
