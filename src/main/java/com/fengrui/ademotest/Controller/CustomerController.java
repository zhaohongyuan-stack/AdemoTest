package com.fengrui.ademotest.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fengrui.ademotest.common.Result;
import com.fengrui.ademotest.entity.Customer;
import com.fengrui.ademotest.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "客户管理", description = "客户相关接口")
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Operation(summary = "分页查询客户列表")
    @GetMapping("/page")
    public Result<Page<Customer>> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String customerName) {
        Page<Customer> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Customer> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(customerName != null && !customerName.isEmpty(), Customer::getCustomerName, customerName);
        wrapper.orderByDesc(Customer::getCreateTime);
        return Result.success(customerService.page(page, wrapper));
    }

    @Operation(summary = "根据ID查询客户详情")
    @GetMapping("/{id}")
    public Result<Customer> getById(@PathVariable Integer id) {
        return Result.success(customerService.getById(id));
    }

    @Operation(summary = "新增客户")
    @PostMapping
    public Result<Void> add(@RequestBody Customer customer) {
        customerService.save(customer);
        return Result.success();
    }

    @Operation(summary = "修改客户")
    @PutMapping
    public Result<Void> update(@RequestBody Customer customer) {
        customerService.updateById(customer);
        return Result.success();
    }

    @Operation(summary = "删除客户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        customerService.removeById(id);
        return Result.success();
    }
}
