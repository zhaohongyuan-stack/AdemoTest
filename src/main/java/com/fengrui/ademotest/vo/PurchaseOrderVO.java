package com.fengrui.ademotest.vo;

import com.fengrui.ademotest.entity.PurchaseOrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseOrderVO {

    private Integer orderId;
    private String orderNo;
    private Integer supplierId;
    private BigDecimal totalAmount;
    private Short orderStatus;
    private String orderStatusDesc;
    private LocalDateTime createTime;
    private List<PurchaseOrderItem> items;
}
