package com.aiolos.library.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Aiolos
 * @date 2021/3/20 3:36 上午
 */
@Data
@ApiModel(value = "购物车新增的对象")
public class ShoppingCartInsertBO {

    @ApiModelProperty(value = "书籍的主键", required = true)
    @NotNull(message = "书籍信息不能为空")
    public Long bookId;

    @ApiModelProperty(value = "用户的主键, 不用传")
    public Long userId;

    @ApiModelProperty(value = "书籍购买数量", required = true)
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量不能小于1")
    public Integer quantity;

    @ApiModelProperty(value = "书籍购买的金额", required = true)
    @NotNull(message = "金额数不能为空")
    @Min(value = 0, message = "金额数不规范")
    public BigDecimal qmount;
}
