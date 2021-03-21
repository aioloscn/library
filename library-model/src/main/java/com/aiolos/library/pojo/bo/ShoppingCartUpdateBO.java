package com.aiolos.library.pojo.bo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Aiolos
 * @date 2021/3/20 3:36 上午
 */
@Data
public class ShoppingCartUpdateBO {

    @NotBlank(message = "缺少主键信息")
    public String id;

    @NotBlank(message = "书籍信息不能为空")
    public String bookId;

    @NotBlank(message = "用户信息不能为空")
    public String userId;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量不能小于1")
    public Integer totalQuantity;

    @NotNull(message = "金额数不能为空")
    @Min(value = 1, message = "金额数不规范")
    public BigDecimal totalAmount;
}
