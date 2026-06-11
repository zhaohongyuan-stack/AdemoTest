package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.PurchaseOrder;
import com.fengrui.ademotest.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "采购订单管理", description = "采购订单相关接口")
@RestController
@RequestMapping("/api/purchase-order")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Operation(summary = "分页查询采购订单列表")
    @GetMapping("/page")
    public Result<Page<PurchaseOrder>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo) {
        Page<PurchaseOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<PurchaseOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(orderNo != null && !orderNo.isEmpty(), PurchaseOrder::getOrderNo, orderNo);
        wrapper.orderByDesc(PurchaseOrder::getCreateTime);
        return Result.success(purchaseOrderService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询采购订单详情")
    @GetMapping("/{id}")
    public Result<PurchaseOrder> getById(@PathVariable Integer id) {
        return Result.success(purchaseOrderService.getById(id));
    }

    @Operation(summary = "新增采购订单")
    @PostMapping
    public Result<Void> add(@RequestBody PurchaseOrder order) {
        purchaseOrderService.save(order);
        return Result.success();
    }

    @Operation(summary = "修改采购订单")
    @PutMapping
    public Result<Void> update(@RequestBody PurchaseOrder order) {
        purchaseOrderService.updateById(order);
        return Result.success();
    }

    @Operation(summary = "删除采购订单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        purchaseOrderService.removeById(id);
        return Result.success();
    }
}
