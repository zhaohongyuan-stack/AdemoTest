package com.fengrui.ademotest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    PENDING(0, "待入库"),
    COMPLETED(1, "已入库"),
    CANCELLED(2, "已取消");

    private final Integer code;
    private final String desc;

    public static OrderStatusEnum getByCode(Integer code) {
        for (OrderStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
