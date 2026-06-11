package com.fengrui.ademotest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleOrderCreateDTO {

    private Integer customerId;

    private List<SaleOrderItemDTO> items;

    @Data
    public static class SaleOrderItemDTO {
        private Integer productId;
        private Integer quantity;
        private BigDecimal unitPrice;
    }
}
