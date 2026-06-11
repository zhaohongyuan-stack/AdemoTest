package com.fengrui.ademotest.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    PARAM_ERROR(400, "参数错误"),
    ORDER_NOT_FOUND(1001, "订单不存在"),
    ORDER_STATUS_ERROR(1002, "订单状态不允许此操作"),
    STOCK_NOT_FOUND(1003, "库存记录不存在"),
    SYSTEM_ERROR(500, "系统错误");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
