package com.fengrui.ademotest.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductDetailVO {

    private Integer productId;
    private String productCode;
    private String productName;
    private Integer categoryId;
    private String categoryName;
    private String unit;
    private BigDecimal purchasePrice;
    private BigDecimal sellingPrice;
    private Integer stockWarning;
    private Short status;
    private String statusDesc;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
