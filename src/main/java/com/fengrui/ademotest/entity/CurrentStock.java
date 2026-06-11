package com.fengrui.ademotest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("current_stock")
public class CurrentStock {

    @TableId(value = "stock_id", type = IdType.AUTO)
    private Integer stockId;

    @TableField("product_id")
    private Integer productId;

    @TableField("quantity")
    private Integer quantity;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
