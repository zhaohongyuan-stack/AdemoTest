package com.fengrui.ademotest.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockRecordQueryDTO {

    private Integer productId;
    private Short type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
