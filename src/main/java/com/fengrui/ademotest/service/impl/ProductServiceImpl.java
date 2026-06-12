package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.Product;
import com.fengrui.ademotest.entity.ProductCategory;
import com.fengrui.ademotest.enums.ProductStatusEnum;
import com.fengrui.ademotest.mapper.ProductCategoryMapper;
import com.fengrui.ademotest.mapper.ProductMapper;
import com.fengrui.ademotest.service.ProductService;
import com.fengrui.ademotest.vo.ProductDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Override
    public Page<ProductDetailVO> queryProductPage(Integer pageNum, Integer pageSize, String productName, Integer categoryId, Short status) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();

        // 商品名称模糊查询
        if (productName != null && !productName.isEmpty()) {
            wrapper.like(Product::getProductName, productName);
        }

        // 分类ID筛选
        if (categoryId != null) {
            wrapper.eq(Product::getCategoryId, categoryId);
        }

        // 状态筛选
        if (status != null) {
            wrapper.eq(Product::getStatus, status);
        }

        wrapper.orderByDesc(Product::getCreateTime);
        Page<Product> productPage = this.page(page, wrapper);

        // 转换为VO
        Page<ProductDetailVO> voPage = new Page<>(pageNum, pageSize, productPage.getTotal());
        List<ProductDetailVO> voList = productPage.getRecords().stream()
                .map(this::convertToDetailVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        return voPage;
    }

    @Override
    public ProductDetailVO getProductDetail(Integer productId) {
        Product product = this.getById(productId);
        if (product == null) {
            return null;
        }
        return convertToDetailVO(product);
    }

    @Override
    public String generateProductCode() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 查询今日最大序号
        Long count = this.count(
                new LambdaQueryWrapper<Product>()
                        .likeRight(Product::getProductCode, "P" + dateStr)
        );
        int sequence = count.intValue() + 1;
        return "P" + dateStr + String.format("%04d", sequence);
    }

    /**
     * 转换为DetailVO
     */
    private ProductDetailVO convertToDetailVO(Product product) {
        ProductDetailVO vo = new ProductDetailVO();
        BeanUtils.copyProperties(product, vo);

        // 查询分类名称
        if (product.getCategoryId() != null) {
            ProductCategory category = productCategoryMapper.selectById(product.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getCategoryName());
            }
        }

        // 设置状态描述
        if (product.getStatus() != null) {
            ProductStatusEnum statusEnum = ProductStatusEnum.getByCode(product.getStatus());
            if (statusEnum != null) {
                vo.setStatusDesc(statusEnum.getDesc());
            }
        }

        return vo;
    }
}
