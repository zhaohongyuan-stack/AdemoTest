package com.fengrui.ademotest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("stock_record")
public class StockRecord {

    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    @TableField("product_id")
    private Integer productId;

    @TableField("type")
    private Short type;

    @TableField("quantity")
    private Integer quantity;

    @TableField("related_order_no")
    private String relatedOrderNo;

    @TableField("remark")
    private String remark;

    @TableField("create_time")
    private LocalDateTime createTime;
}
