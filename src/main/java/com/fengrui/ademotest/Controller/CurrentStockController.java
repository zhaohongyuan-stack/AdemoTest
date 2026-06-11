package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.dto.StockRecordQueryDTO;
import com.fengrui.ademotest.entity.StockRecord;
import com.fengrui.ademotest.service.CurrentStockService;
import com.fengrui.ademotest.service.StockRecordService;
import com.fengrui.ademotest.vo.CurrentStockVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "库存管理", description = "库存相关接口")
@RestController
@RequestMapping("/api/stock")
public class CurrentStockController {

    @Autowired
    private CurrentStockService currentStockService;

    @Autowired
    private StockRecordService stockRecordService;

    @Operation(summary = "分页查询当前库存")
    @GetMapping("/page")
    public Result<Page<CurrentStockVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String productName) {
        return Result.success(currentStockService.queryStockPage(pageNum, pageSize, productName));
    }

    @Operation(summary = "库存预警列表")
    @GetMapping("/warning")
    public Result<List<CurrentStockVO>> getWarningList() {
        return Result.success(currentStockService.getWarningStockList());
    }

    @Operation(summary = "分页查询库存流水")
    @GetMapping("/record/page")
    public Result<Page<StockRecord>> recordPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            StockRecordQueryDTO queryDTO) {
        return Result.success(stockRecordService.queryRecordPage(pageNum, pageSize, queryDTO));
    }
}
