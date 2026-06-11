package com.fengrui.ademotest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PurchaseOrderCreateDTO {

    private Integer supplierId;

    private List<PurchaseOrderItemDTO> items;

    @Data
    public static class PurchaseOrderItemDTO {
        private Integer productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
