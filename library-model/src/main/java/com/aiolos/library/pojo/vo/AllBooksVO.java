package com.aiolos.library.pojo.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Aiolos
 * @date 2021/3/28 6:00 下午
 */
@Data
public class AllBooksVO {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private Long id;

    /**
     * 书名
     */
    private String name;

    /**
     * 封面
     */
    @Column(name = "pic_url")
    private String picUrl;

    /**
     * 进货价格
     */
    @Column(name = "purchase_price")
    private BigDecimal purchasePrice;

    /**
     * 进货数量
     */
    @Column(name = "purchase_num")
    private Integer purchaseNum;

    /**
     * 售价
     */
    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    /**
     * 销售量
     */
    @Column(name = "sales_volume")
    private Integer salesVolume;

    /**
     * 折扣: discount / 1000为实际折扣，discount / 100为显示的折扣
     */
    private Integer discount;

    /**
     * int类型的折扣处理后保存的字符串类型值
     */
    private String discountStr;

    /**
     * 原价
     */
    @Column(name = "original_price")
    private BigDecimal originalPrice;

    /**
     * 目录
     */
    private String catalog;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 其他信息
     */
    @Column(name = "other_infor")
    private String otherInfor;

    /**
     * 书籍大分类，可能不使用全部放在小分类里
     */
    private Integer classification;

    /**
     * 书籍小分类
     */
    private Integer category;

    /**
     * 状态: 0: 已删除, 1: 已上架: 2: 未上架
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sort;

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

    /**
     * 内容简介
     */
    private String brief;

    /**
     * 前言
     */
    private String preface;

    /**
     * 节选
     */
    private String excerpt;
}
