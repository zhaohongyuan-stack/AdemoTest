package com.fengrui.ademotest.controller;

import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.service.StatisticsService;
import com.fengrui.ademotest.vo.ProductSalesRankVO;
import com.fengrui.ademotest.vo.StatisticsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "统计查询", description = "数据统计相关接口")
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @Operation(summary = "今日采购金额统计")
    @GetMapping("/purchase/today")
    public Result<StatisticsVO> getTodayPurchaseStatistics() {
        return Result.success(statisticsService.getTodayPurchaseStatistics());
    }

    @Operation(summary = "今日销售金额统计")
    @GetMapping("/sale/today")
    public Result<StatisticsVO> getTodaySaleStatistics() {
        return Result.success(statisticsService.getTodaySaleStatistics());
    }

    @Operation(summary = "商品销量排行榜")
    @GetMapping("/product/rank")
    public Result<List<ProductSalesRankVO>> getProductSalesRank(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        return Result.success(statisticsService.getProductSalesRank(startTime, endTime));
    }
}
