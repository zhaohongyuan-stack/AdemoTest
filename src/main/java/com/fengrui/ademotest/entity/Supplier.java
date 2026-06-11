package com.fengrui.ademotest.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("supplier")
public class Supplier {

    @TableId(value = "supplier_id", type = IdType.AUTO)
    private Integer supplierId;

    @TableField("supplier_name")
    private String supplierName;

    @TableField("contact_person")
    private String contactPerson;

    @TableField("phone")
    private String phone;

    @TableField("address")
    private String address;

    @TableField("status")
    private Short status;

    @TableField("create_time")
    private LocalDateTime createTime;
}
