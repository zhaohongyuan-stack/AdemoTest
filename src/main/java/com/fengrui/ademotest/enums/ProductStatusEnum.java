package com.fengrui.ademotest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductStatusEnum {

    DISABLED(0, "停用"),
    ENABLED(1, "启用");

    private final Integer code;
    private final String desc;

    public static ProductStatusEnum getByCode(Integer code) {
        for (ProductStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
