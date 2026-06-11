package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.StockRecord;
import com.fengrui.ademotest.service.StockRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "库存记录管理", description = "库存变动记录相关接口")
@RestController
@RequestMapping("/api/stock-record")
public class StockRecordController {

    @Autowired
    private StockRecordService stockRecordService;

    @Operation(summary = "分页查询库存记录列表")
    @GetMapping("/page")
    public Result<Page<StockRecord>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer productId) {
        Page<StockRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(productId != null, StockRecord::getProductId, productId);
        wrapper.orderByDesc(StockRecord::getCreateTime);
        return Result.success(stockRecordService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询库存记录详情")
    @GetMapping("/{id}")
    public Result<StockRecord> getById(@PathVariable Integer id) {
        return Result.success(stockRecordService.getById(id));
    }

    @Operation(summary = "新增库存记录")
    @PostMapping
    public Result<Void> add(@RequestBody StockRecord record) {
        stockRecordService.save(record);
        return Result.success();
    }

    @Operation(summary = "修改库存记录")
    @PutMapping
    public Result<Void> update(@RequestBody StockRecord record) {
        stockRecordService.updateById(record);
        return Result.success();
    }

    @Operation(summary = "删除库存记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        stockRecordService.removeById(id);
        return Result.success();
    }
}
