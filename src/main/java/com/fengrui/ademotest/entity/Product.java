package com.fengrui.ademotest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("product")
public class Product {

    @TableId(value = "product_id", type = IdType.AUTO)
    private Integer productId;

    @TableField("product_code")
    private String productCode;

    @TableField("product_name")
    private String productName;

    @TableField("category_id")
    private Integer categoryId;

    @TableField("unit")
    private String unit;

    @TableField("purchase_price")
    private BigDecimal purchasePrice;

    @TableField("selling_price")
    private BigDecimal sellingPrice;

    @TableField("stock_warning")
    private Integer stockWarning;

    @TableField("status")
    private Short status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
