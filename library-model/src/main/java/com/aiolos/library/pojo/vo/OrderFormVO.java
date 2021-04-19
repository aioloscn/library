package com.aiolos.library.pojo.vo;

import com.aiolos.library.pojo.Book;
import com.aiolos.library.pojo.helper.LongJsonDeserializer;
import com.aiolos.library.pojo.helper.LongJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Aiolos
 * @date 2021/4/18 1:10 下午
 */
@Data
public class OrderFormVO {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long id;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long orderNo;

    private Book book;

    /**
     * 书籍主键
     */
    @Column(name = "book_id")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
    private Long bookId;

    /**
     * 用户主键
     */
    @Column(name = "user_id")
    @JsonSerialize(using = LongJsonSerializer.class)
    @JsonDeserialize(using = LongJsonDeserializer.class)
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
     * 订单总数量
     */
    @Column(name = "total_quantity")
    private Integer totalQuantity;

    /**
     * 订单总金额
     */
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    /**
     * 收货地址
     */
    @Column(name = "shipping_address")
    private String shippingAddress;

    /**
     * 状态: 0: 已删除, 1: 未支付, 2: 已支付, 3: 已收货
     */
    private Integer status;

    private String orderStatus;

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
