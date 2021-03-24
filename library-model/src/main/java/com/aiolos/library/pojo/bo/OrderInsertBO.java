package com.aiolos.library.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author Aiolos
 * @date 2021/3/20 3:36 上午
 */
@Data
@ApiModel(value = "提交的新订单对象", description = "前端可以传一个该对象的集合来批量新增")
public class OrderInsertBO {

    @Data
    @ApiModel(value = "提交的订单中每样商品的购买详情")
    public class OrderBookInsertBO {

        @ApiModelProperty(value = "书籍的主键",required = true)
        @NotNull(message = "缺少书籍的主键信息")
        public Long bookId;

        @ApiModelProperty(value = "该书籍的购买数量", required = true)
        @NotNull(message = "书籍的购买数量不能为空")
        @Min(value = 1, message = "书籍购买数量不能小于1")
        public Integer quantity;

        @ApiModelProperty(value = "购买该书籍的总金额", required = true)
        @NotNull(message = "购买书籍金额不能为空")
        @Min(value = 0, message = "金额数不规范")
        public BigDecimal amount;
    }

    @ApiModelProperty(value = "书籍对象", required = true)
    @NotEmpty(message = "书籍信息不能为空")
    public List<OrderBookInsertBO> orderBookInsertBOs;

    @ApiModelProperty(value = "用户的主键，不用填，程序内根据token填充")
    public Long userId;

    @ApiModelProperty(value = "订单中包含的商品的总数量", required = true)
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量不能小于1")
    public Integer totalQuantity;

    @ApiModelProperty(value = "订单中包含的商品的总金额", required = true)
    @NotNull(message = "金额数不能为空")
    @Min(value = 0, message = "金额数不规范")
    public BigDecimal totalAmount;

    @ApiModelProperty(value = "送货地址")
    public String shippingAddress;
}
