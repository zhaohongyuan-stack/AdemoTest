package com.fengrui.ademotest.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsVO {

    private BigDecimal totalAmount;
    private Integer orderCount;
}
