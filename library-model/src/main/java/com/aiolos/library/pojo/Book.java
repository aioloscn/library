package com.aiolos.library.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Book implements Serializable {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SELECT LAST_INSERT_ID()")
    private String id;

    /**
     * 书名
     */
    private String name;

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
     * 书籍大分类
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

    private static final long serialVersionUID = 1L;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取书名
     *
     * @return name - 书名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置书名
     *
     * @param name 书名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取进货价格
     *
     * @return purchase_price - 进货价格
     */
    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * 设置进货价格
     *
     * @param purchasePrice 进货价格
     */
    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * 获取进货数量
     *
     * @return purchase_num - 进货数量
     */
    public Integer getPurchaseNum() {
        return purchaseNum;
    }

    /**
     * 设置进货数量
     *
     * @param purchaseNum 进货数量
     */
    public void setPurchaseNum(Integer purchaseNum) {
        this.purchaseNum = purchaseNum;
    }

    /**
     * 获取售价
     *
     * @return selling_price - 售价
     */
    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    /**
     * 设置售价
     *
     * @param sellingPrice 售价
     */
    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /**
     * 获取销售量
     *
     * @return sales_volume - 销售量
     */
    public Integer getSalesVolume() {
        return salesVolume;
    }

    /**
     * 设置销售量
     *
     * @param salesVolume 销售量
     */
    public void setSalesVolume(Integer salesVolume) {
        this.salesVolume = salesVolume;
    }

    /**
     * 获取折扣: discount / 1000为实际折扣，discount / 100为显示的折扣
     *
     * @return discount - 折扣: discount / 1000为实际折扣，discount / 100为显示的折扣
     */
    public Integer getDiscount() {
        return discount;
    }

    /**
     * 设置折扣: discount / 1000为实际折扣，discount / 100为显示的折扣
     *
     * @param discount 折扣: discount / 1000为实际折扣，discount / 100为显示的折扣
     */
    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    /**
     * 获取书籍大分类
     *
     * @return classification - 书籍大分类
     */
    public Integer getClassification() {
        return classification;
    }

    /**
     * 设置书籍大分类
     *
     * @param classification 书籍大分类
     */
    public void setClassification(Integer classification) {
        this.classification = classification;
    }

    /**
     * 获取书籍小分类
     *
     * @return category - 书籍小分类
     */
    public Integer getCategory() {
        return category;
    }

    /**
     * 设置书籍小分类
     *
     * @param category 书籍小分类
     */
    public void setCategory(Integer category) {
        this.category = category;
    }

    /**
     * 获取状态: 0: 已删除, 1: 已上架: 2: 未上架
     *
     * @return status - 状态: 0: 已删除, 1: 已上架: 2: 未上架
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态: 0: 已删除, 1: 已上架: 2: 未上架
     *
     * @param status 状态: 0: 已删除, 1: 已上架: 2: 未上架
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取创建时间
     *
     * @return gmt_create - 创建时间
     */
    public Date getGmtCreate() {
        return gmtCreate;
    }

    /**
     * 设置创建时间
     *
     * @param gmtCreate 创建时间
     */
    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    /**
     * 获取修改时间
     *
     * @return gmt_modified - 修改时间
     */
    public Date getGmtModified() {
        return gmtModified;
    }

    /**
     * 设置修改时间
     *
     * @param gmtModified 修改时间
     */
    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", purchasePrice=").append(purchasePrice);
        sb.append(", purchaseNum=").append(purchaseNum);
        sb.append(", sellingPrice=").append(sellingPrice);
        sb.append(", salesVolume=").append(salesVolume);
        sb.append(", discount=").append(discount);
        sb.append(", classification=").append(classification);
        sb.append(", category=").append(category);
        sb.append(", status=").append(status);
        sb.append(", sort=").append(sort);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModified=").append(gmtModified);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}