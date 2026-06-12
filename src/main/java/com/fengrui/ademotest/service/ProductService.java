package com.fengrui.ademotest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengrui.ademotest.entity.Product;
import com.fengrui.ademotest.vo.ProductDetailVO;

public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品列表（含分类名称）
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param productName 商品名称（模糊查询）
     * @param categoryId 分类ID
     * @param status 状态
     * @return 商品详情分页结果
     */
    Page<ProductDetailVO> queryProductPage(Integer pageNum, Integer pageSize, String productName, Integer categoryId, Short status);

    /**
     * 根据ID查询商品详情（含分类名称）
     * @param productId 商品ID
     * @return 商品详情VO
     */
    ProductDetailVO getProductDetail(Integer productId);

    /**
     * 生成商品编码
     * @return 商品编码
     */
    String generateProductCode();
}
