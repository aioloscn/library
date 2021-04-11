package com.aiolos.library.pojo.vo;

import com.aiolos.library.pojo.Book;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Aiolos
 * @date 2021/4/12 5:05 上午
 */
@Data
public class ShoppingCartBookVO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    /**
     * 书籍主键
     */
    @Column(name = "book_id")
    private Long bookId;

    private Book book;

    /**
     * 用户主键
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 商品数量
     */
    private Integer quantity;

    /**
     * 商品金额
     */
    private BigDecimal amount;

    /**
     * 状态: 0: 已删除, 1: 正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    private Date gmtModified;
}
