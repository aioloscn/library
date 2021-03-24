package com.aiolos.library.pojo;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "admin_operate_record")
public class AdminOperateRecord implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    /**
     * 用户主键
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 书籍主键
     */
    @Column(name = "book_id")
    private Long bookId;

    /**
     * 购物车主键
     */
    @Column(name = "shopping_cart_id")
    private Long shoppingCartId;

    /**
     * 订单主键
     */
    @Column(name = "order_form_id")
    private Long orderFormId;

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取用户主键
     *
     * @return user_id - 用户主键
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户主键
     *
     * @param userId 用户主键
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取书籍主键
     *
     * @return book_id - 书籍主键
     */
    public Long getBookId() {
        return bookId;
    }

    /**
     * 设置书籍主键
     *
     * @param bookId 书籍主键
     */
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    /**
     * 获取购物车主键
     *
     * @return shopping_cart_id - 购物车主键
     */
    public Long getShoppingCartId() {
        return shoppingCartId;
    }

    /**
     * 设置购物车主键
     *
     * @param shoppingCartId 购物车主键
     */
    public void setShoppingCartId(Long shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    /**
     * 获取订单主键
     *
     * @return order_form_id - 订单主键
     */
    public Long getOrderFormId() {
        return orderFormId;
    }

    /**
     * 设置订单主键
     *
     * @param orderFormId 订单主键
     */
    public void setOrderFormId(Long orderFormId) {
        this.orderFormId = orderFormId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", bookId=").append(bookId);
        sb.append(", shoppingCartId=").append(shoppingCartId);
        sb.append(", orderFormId=").append(orderFormId);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}