package com.fengrui.ademotest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("purchase_order")
public class PurchaseOrder {

    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @TableField("order_no")
    private String orderNo;

    @TableField("supplier_id")
    private Integer supplierId;

    @TableField("total_amount")
    private BigDecimal totalAmount;

    @TableField("order_status")
    private Short orderStatus;

    @TableField("create_time")
    private LocalDateTime createTime;
}
