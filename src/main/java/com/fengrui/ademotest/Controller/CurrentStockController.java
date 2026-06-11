package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.CurrentStock;
import com.fengrui.ademotest.service.CurrentStockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "库存管理", description = "当前库存相关接口")
@RestController
@RequestMapping("/api/stock")
public class CurrentStockController {

    @Autowired
    private CurrentStockService currentStockService;

    @Operation(summary = "分页查询库存列表")
    @GetMapping("/page")
    public Result<Page<CurrentStock>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer productId) {
        Page<CurrentStock> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CurrentStock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(productId != null, CurrentStock::getProductId, productId);
        wrapper.orderByDesc(CurrentStock::getUpdateTime);
        return Result.success(currentStockService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询库存详情")
    @GetMapping("/{id}")
    public Result<CurrentStock> getById(@PathVariable Integer id) {
        return Result.success(currentStockService.getById(id));
    }

    @Operation(summary = "新增库存记录")
    @PostMapping
    public Result<Void> add(@RequestBody CurrentStock stock) {
        currentStockService.save(stock);
        return Result.success();
    }

    @Operation(summary = "修改库存")
    @PutMapping
    public Result<Void> update(@RequestBody CurrentStock stock) {
        currentStockService.updateById(stock);
        return Result.success();
    }

    @Operation(summary = "删除库存记录")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        currentStockService.removeById(id);
        return Result.success();
    }
}
