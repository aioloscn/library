package com.aiolos.library.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Aiolos
 * @date 2021/3/20 7:10 下午
 */
@Data
public class ShoppingCartDeleteBO {

    @NotBlank(message = "缺少主键信息")
    public String id;

    @NotBlank(message = "书籍信息不能为空")
    public String bookId;

    @NotBlank(message = "用户信息不能为空")
    public String userId;
}
