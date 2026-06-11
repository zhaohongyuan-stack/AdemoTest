package com.fengrui.ademotest.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StockTypeEnum {

    PURCHASE_IN(1, "采购入库"),
    SALE_OUT(2, "销售出库");

    private final Integer code;
    private final String desc;

    public static StockTypeEnum getByCode(Integer code) {
        for (StockTypeEnum type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}
