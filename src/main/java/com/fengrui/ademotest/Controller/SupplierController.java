package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.Supplier;
import com.fengrui.ademotest.service.SupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "供应商管理", description = "供应商相关接口")
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Operation(summary = "分页查询供应商列表")
    @GetMapping("/page")
    public Result<Page<Supplier>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String supplierName) {
        Page<Supplier> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Supplier> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(supplierName != null && !supplierName.isEmpty(), Supplier::getSupplierName, supplierName);
        wrapper.orderByDesc(Supplier::getCreateTime);
        return Result.success(supplierService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询供应商详情")
    @GetMapping("/{id}")
    public Result<Supplier> getById(@PathVariable Integer id) {
        return Result.success(supplierService.getById(id));
    }

    @Operation(summary = "新增供应商")
    @PostMapping
    public Result<Void> add(@RequestBody Supplier supplier) {
        supplierService.save(supplier);
        return Result.success();
    }

    @Operation(summary = "修改供应商")
    @PutMapping
    public Result<Void> update(@RequestBody Supplier supplier) {
        supplierService.updateById(supplier);
        return Result.success();
    }

    @Operation(summary = "删除供应商")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        supplierService.removeById(id);
        return Result.success();
    }
}
