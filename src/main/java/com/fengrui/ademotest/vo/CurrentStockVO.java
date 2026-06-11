package com.fengrui.ademotest.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrentStockVO {

    private Integer stockId;
    private Integer productId;
    private String productCode;
    private String productName;
    private Integer quantity;
    private Integer stockWarning;
    private Boolean isWarning;
}
