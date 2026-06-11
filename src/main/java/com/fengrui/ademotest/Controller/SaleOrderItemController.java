package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.SaleOrderItem;
import com.fengrui.ademotest.service.SaleOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "销售订单明细管理", description = "销售订单明细相关接口")
@RestController
@RequestMapping("/api/sale-order-item")
public class SaleOrderItemController {

    @Autowired
    private SaleOrderItemService saleOrderItemService;

    @Operation(summary = "分页查询销售订单明细列表")
    @GetMapping("/page")
    public Result<Page<SaleOrderItem>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer orderId) {
        Page<SaleOrderItem> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SaleOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(orderId != null, SaleOrderItem::getOrderId, orderId);
        return Result.success(saleOrderItemService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询销售订单明细详情")
    @GetMapping("/{id}")
    public Result<SaleOrderItem> getById(@PathVariable Integer id) {
        return Result.success(saleOrderItemService.getById(id));
    }

    @Operation(summary = "新增销售订单明细")
    @PostMapping
    public Result<Void> add(@RequestBody SaleOrderItem item) {
        saleOrderItemService.save(item);
        return Result.success();
    }

    @Operation(summary = "修改销售订单明细")
    @PutMapping
    public Result<Void> update(@RequestBody SaleOrderItem item) {
        saleOrderItemService.updateById(item);
        return Result.success();
    }

    @Operation(summary = "删除销售订单明细")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        saleOrderItemService.removeById(id);
        return Result.success();
    }
}
