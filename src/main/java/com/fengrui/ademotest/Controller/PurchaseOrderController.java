package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.dto.PurchaseOrderCreateDTO;
import com.fengrui.ademotest.entity.PurchaseOrder;
import com.fengrui.ademotest.service.PurchaseOrderService;
import com.fengrui.ademotest.vo.PurchaseOrderVO;
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

    @Operation(summary = "根据ID查询采购订单详情（含明细）")
    @GetMapping("/{id}")
    public Result<PurchaseOrderVO> getById(@PathVariable Integer id) {
        return Result.success(purchaseOrderService.getPurchaseOrderDetail(id));
    }

    @Operation(summary = "创建采购单（含明细）")
    @PostMapping
    public Result<PurchaseOrderVO> create(@RequestBody PurchaseOrderCreateDTO dto) {
        return Result.success(purchaseOrderService.createPurchaseOrder(dto));
    }

    @Operation(summary = "采购单入库")
    @PutMapping("/{id}/receive")
    public Result<Void> receive(@PathVariable Integer id) {
        purchaseOrderService.receivePurchaseOrder(id);
        return Result.success();
    }

    @Operation(summary = "取消采购单")
    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Integer id) {
        purchaseOrderService.cancelPurchaseOrder(id);
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
