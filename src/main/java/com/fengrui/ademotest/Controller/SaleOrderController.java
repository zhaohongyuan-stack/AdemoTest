package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.dto.SaleOrderCreateDTO;
import com.fengrui.ademotest.entity.SaleOrder;
import com.fengrui.ademotest.service.SaleOrderService;
import com.fengrui.ademotest.vo.SaleOrderVO;
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

    @Operation(summary = "根据ID查询销售订单详情（含明细）")
    @GetMapping("/{id}")
    public Result<SaleOrderVO> getById(@PathVariable Integer id) {
        return Result.success(saleOrderService.getSaleOrderDetail(id));
    }

    @Operation(summary = "创建销售单（含明细）")
    @PostMapping
    public Result<SaleOrderVO> create(@RequestBody SaleOrderCreateDTO dto) {
        return Result.success(saleOrderService.createSaleOrder(dto));
    }

    @Operation(summary = "销售单出库")
    @PutMapping("/{id}/ship")
    public Result<Void> ship(@PathVariable Integer id) {
        saleOrderService.shipSaleOrder(id);
        return Result.success();
    }

    @Operation(summary = "取消销售单")
    @PutMapping("/{id}/cancel")
    public Result<Void> cancel(@PathVariable Integer id) {
        saleOrderService.cancelSaleOrder(id);
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
