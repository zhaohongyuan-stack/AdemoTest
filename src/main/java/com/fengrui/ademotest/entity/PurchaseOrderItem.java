package com.fengrui.ademotest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("purchase_order_item")
public class PurchaseOrderItem {

    @TableId(value = "item_id", type = IdType.AUTO)
    private Integer itemId;

    @TableField("order_id")
    private Integer orderId;

    @TableField("product_id")
    private Integer productId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("unit_price")
    private BigDecimal unitPrice;

    @TableField("total_price")
    private BigDecimal totalPrice;
}
