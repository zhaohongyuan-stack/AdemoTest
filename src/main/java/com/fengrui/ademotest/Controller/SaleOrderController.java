package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.SaleOrder;
import com.fengrui.ademotest.service.SaleOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "销售订单管理", description = "销售订单相关接口")
@RestController
@RequestMapping("/api/sale-order")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @Operation(summary = "分页查询销售订单列表")
    @GetMapping("/page")
    public Result<Page<SaleOrder>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String orderNo) {
        Page<SaleOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SaleOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(orderNo != null && !orderNo.isEmpty(), SaleOrder::getOrderNo, orderNo);
        wrapper.orderByDesc(SaleOrder::getCreateTime);
        return Result.success(saleOrderService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询销售订单详情")
    @GetMapping("/{id}")
    public Result<SaleOrder> getById(@PathVariable Integer id) {
        return Result.success(saleOrderService.getById(id));
    }

    @Operation(summary = "新增销售订单")
    @PostMapping
    public Result<Void> add(@RequestBody SaleOrder order) {
        saleOrderService.save(order);
        return Result.success();
    }

    @Operation(summary = "修改销售订单")
    @PutMapping
    public Result<Void> update(@RequestBody SaleOrder order) {
        saleOrderService.updateById(order);
        return Result.success();
    }

    @Operation(summary = "删除销售订单")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        saleOrderService.removeById(id);
        return Result.success();
    }
}
