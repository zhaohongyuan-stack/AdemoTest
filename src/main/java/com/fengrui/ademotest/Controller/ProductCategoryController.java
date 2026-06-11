package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.ProductCategory;
import com.fengrui.ademotest.service.ProductCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品分类管理", description = "商品分类相关接口")
@RestController
@RequestMapping("/api/product-category")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @Operation(summary = "分页查询分类列表")
    @GetMapping("/page")
    public Result<Page<ProductCategory>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String categoryName) {
        Page<ProductCategory> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<ProductCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(categoryName != null && !categoryName.isEmpty(), ProductCategory::getCategoryName, categoryName);
        wrapper.orderByAsc(ProductCategory::getSortOrder);
        return Result.success(productCategoryService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询分类详情")
    @GetMapping("/{id}")
    public Result<ProductCategory> getById(@PathVariable Integer id) {
        return Result.success(productCategoryService.getById(id));
    }

    @Operation(summary = "新增分类")
    @PostMapping
    public Result<Void> add(@RequestBody ProductCategory category) {
        productCategoryService.save(category);
        return Result.success();
    }

    @Operation(summary = "修改分类")
    @PutMapping
    public Result<Void> update(@RequestBody ProductCategory category) {
        productCategoryService.updateById(category);
        return Result.success();
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        productCategoryService.removeById(id);
        return Result.success();
    }
}
