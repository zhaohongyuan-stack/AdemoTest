package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.PurchaseOrderItem;
import com.fengrui.ademotest.service.PurchaseOrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "采购订单明细管理", description = "采购订单明细相关接口")
@RestController
@RequestMapping("/api/purchase-order-item")
public class PurchaseOrderItemController {

    @Autowired
    private PurchaseOrderItemService purchaseOrderItemService;

    @Operation(summary = "分页查询采购订单明细列表")
    @GetMapping("/page")
    public Result<Page<PurchaseOrderItem>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer orderId) {
        Page<PurchaseOrderItem> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PurchaseOrderItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(orderId != null, PurchaseOrderItem::getOrderId, orderId);
        return Result.success(purchaseOrderItemService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询采购订单明细详情")
    @GetMapping("/{id}")
    public Result<PurchaseOrderItem> getById(@PathVariable Integer id) {
        return Result.success(purchaseOrderItemService.getById(id));
    }

    @Operation(summary = "新增采购订单明细")
    @PostMapping
    public Result<Void> add(@RequestBody PurchaseOrderItem item) {
        purchaseOrderItemService.save(item);
        return Result.success();
    }

    @Operation(summary = "修改采购订单明细")
    @PutMapping
    public Result<Void> update(@RequestBody PurchaseOrderItem item) {
        purchaseOrderItemService.updateById(item);
        return Result.success();
    }

    @Operation(summary = "删除采购订单明细")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        purchaseOrderItemService.removeById(id);
        return Result.success();
    }
}
