package com.aiolos.library.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author Aiolos
 * @date 2021/3/22 7:24 下午
 */
@Data
@ApiModel(value = "修改的书籍对象", description = "前端可以传一个该对象的集合来批量修改")
public class BookUpdateBO {

    @ApiModelProperty(value = "书籍的主键", required = true)
    @NotNull(message = "书籍的主键不能为空")
    public Long id;

    @ApiModelProperty(value = "书名", required = true)
    @NotBlank(message = "书名不能为空")
    public String name;

    @ApiModelProperty(value = "内容简介")
    public String briefIntroduction;

    @ApiModelProperty(value = "进货价格", required = true)
    @NotNull(message = "进货价格不能为空")
    @Min(value = 0, message = "请填写有效的价格")
    public BigDecimal purchasePrice;

    @ApiModelProperty(value = "进货数量", required = true)
    @Min(value = 0, message = "请填写有效的进货数量")
    public BigDecimal purchaseNum;

    @ApiModelProperty(value = "售价", required = true)
    public BigDecimal sellingPrice;

    @ApiModelProperty(value = "折扣", notes = "discount / 1000为实际折扣，discount / 100为显示的折扣")
    public Integer discount;

    @ApiModelProperty(value = "书籍大分类")
    public Integer classification;

    @ApiModelProperty(value = "书籍小分类")
    public Integer category;
}
