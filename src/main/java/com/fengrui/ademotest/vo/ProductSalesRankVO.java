package com.fengrui.ademotest.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSalesRankVO {

    private Integer productId;
    private String productName;
    private Integer totalQuantity;
    private BigDecimal totalAmount;
}
