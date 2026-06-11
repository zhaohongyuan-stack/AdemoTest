package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.CurrentStock;
import com.fengrui.ademotest.entity.Product;
import com.fengrui.ademotest.mapper.CurrentStockMapper;
import com.fengrui.ademotest.mapper.ProductMapper;
import com.fengrui.ademotest.service.CurrentStockService;
import com.fengrui.ademotest.vo.CurrentStockVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrentStockServiceImpl extends ServiceImpl<CurrentStockMapper, CurrentStock> implements CurrentStockService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Page<CurrentStockVO> queryStockPage(Integer pageNum, Integer pageSize, String productName) {
        Page<CurrentStock> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CurrentStock> wrapper = new LambdaQueryWrapper<>();
        
        if (productName != null && !productName.isEmpty()) {
            // 先查询匹配的商品ID
            List<Integer> productIds = productMapper.selectList(
                    new LambdaQueryWrapper<Product>()
                            .like(Product::getProductName, productName)
                            .select(Product::getProductId)
            ).stream().map(Product::getProductId).collect(Collectors.toList());
            
            if (productIds.isEmpty()) {
                return new Page<>(pageNum, pageSize);
            }
            wrapper.in(CurrentStock::getProductId, productIds);
        }
        
        wrapper.orderByDesc(CurrentStock::getUpdateTime);
        Page<CurrentStock> stockPage = this.page(page, wrapper);
        
        // 转换为VO
        Page<CurrentStockVO> voPage = new Page<>(pageNum, pageSize, stockPage.getTotal());
        List<CurrentStockVO> voList = stockPage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }

    @Override
    public List<CurrentStockVO> getWarningStockList() {
        // 查询所有库存记录
        List<CurrentStock> stockList = this.list();
        
        return stockList.stream()
                .map(this::convertToVO)
                .filter(CurrentStockVO::getIsWarning)
                .collect(Collectors.toList());
    }

    /**
     * 转换为VO
     */
    private CurrentStockVO convertToVO(CurrentStock stock) {
        CurrentStockVO vo = new CurrentStockVO();
        BeanUtils.copyProperties(stock, vo);
        
        // 查询商品信息
        Product product = productMapper.selectById(stock.getProductId());
        if (product != null) {
            vo.setProductCode(product.getProductCode());
            vo.setProductName(product.getProductName());
            vo.setStockWarning(product.getStockWarning());
            
            // 判断是否预警
            if (product.getStockWarning() != null) {
                vo.setIsWarning(stock.getQuantity() < product.getStockWarning());
            } else {
                vo.setIsWarning(false);
            }
        }
        
        return vo;
    }
}
