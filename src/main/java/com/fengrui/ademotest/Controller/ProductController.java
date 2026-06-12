package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.Product;
import com.fengrui.ademotest.service.ProductService;
import com.fengrui.ademotest.vo.ProductDetailVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "商品管理", description = "商品相关接口")
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "生成商品编码")
    @GetMapping("/generate-code")
    public Result<String> generateCode() {
        return Result.success(productService.generateProductCode());
    }

    @Operation(summary = "分页查询商品列表")
    @GetMapping("/page")
    public Result<Page<ProductDetailVO>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Short status) {
        return Result.success(productService.queryProductPage(pageNum, pageSize, productName, categoryId, status));
    }

    @Operation(summary = "根据ID查询商品详情")
    @GetMapping("/{id}")
    public Result<ProductDetailVO> getById(@PathVariable Integer id) {
        return Result.success(productService.getProductDetail(id));
    }

    @Operation(summary = "新增商品")
    @PostMapping
    public Result<Void> add(@RequestBody Product product) {
        // 自动生成商品编码
        if (product.getProductCode() == null || product.getProductCode().isEmpty()) {
            product.setProductCode(productService.generateProductCode());
        }
        productService.save(product);
        return Result.success();
    }

    @Operation(summary = "修改商品")
    @PutMapping
    public Result<Void> update(@RequestBody Product product) {
        productService.updateById(product);
        return Result.success();
    }

    @Operation(summary = "删除商品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        productService.removeById(id);
        return Result.success();
    }
}
