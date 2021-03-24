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
@ApiModel(value = "要删除的购物车信息对象")
public class ShoppingCartDeleteBO {

    @ApiModelProperty(value = "购物车单条数据的主键", required = true)
    @NotNull(message = "缺少主键信息")
    public Long id;

    @ApiModelProperty(value = "用户的主键，不用传")
    public Long userId;
}
