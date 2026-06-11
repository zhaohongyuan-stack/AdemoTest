package com.fengrui.ademotest.vo;

import com.fengrui.ademotest.entity.SaleOrderItem;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleOrderVO {

    private Integer orderId;
    private String orderNo;
    private Integer customerId;
    private BigDecimal totalAmount;
    private Short orderStatus;
    private String orderStatusDesc;
    private LocalDateTime createTime;
    private List<SaleOrderItem> items;
}
