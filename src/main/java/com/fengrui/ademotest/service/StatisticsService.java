package com.fengrui.ademotest.service;

import com.fengrui.ademotest.vo.ProductSalesRankVO;
import com.fengrui.ademotest.vo.StatisticsVO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsService {

    /**
     * 今日采购金额统计
     * @return 采购统计结果
     */
    StatisticsVO getTodayPurchaseStatistics();

    /**
     * 今日销售金额统计
     * @return 销售统计结果
     */
    StatisticsVO getTodaySaleStatistics();

    /**
     * 商品销量排行榜
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 前10名商品
     */
    List<ProductSalesRankVO> getProductSalesRank(LocalDateTime startTime, LocalDateTime endTime);
}
