package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.ProductCategory;
import com.fengrui.ademotest.mapper.ProductCategoryMapper;
import com.fengrui.ademotest.service.ProductCategoryService;
import com.fengrui.ademotest.vo.ProductCategoryTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Override
    public List<ProductCategoryTreeVO> getCategoryTree() {
        // 查询所有分类
        List<ProductCategory> allCategories = this.list(
                new LambdaQueryWrapper<ProductCategory>()
                        .orderByAsc(ProductCategory::getSortOrder)
        );

        // 转换为VO
        List<ProductCategoryTreeVO> allVOList = allCategories.stream()
                .map(this::convertToTreeVO)
                .collect(Collectors.toList());

        // 构建Map：parentId -> 子节点列表
        Map<Integer, List<ProductCategoryTreeVO>> parentMap = allVOList.stream()
                .collect(Collectors.groupingBy(ProductCategoryTreeVO::getParentId));

        // 设置子节点
        for (ProductCategoryTreeVO vo : allVOList) {
            vo.setChildren(parentMap.getOrDefault(vo.getCategoryId(), new ArrayList<>()));
        }

        // 返回根节点（parentId为null或0）
        return allVOList.stream()
                .filter(vo -> vo.getParentId() == null || vo.getParentId() == 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductCategory> getChildrenByParentId(Integer parentId) {
        return this.list(
                new LambdaQueryWrapper<ProductCategory>()
                        .eq(ProductCategory::getParentId, parentId)
                        .orderByAsc(ProductCategory::getSortOrder)
        );
    }

    /**
     * 转换为TreeVO
     */
    private ProductCategoryTreeVO convertToTreeVO(ProductCategory category) {
        ProductCategoryTreeVO vo = new ProductCategoryTreeVO();
        BeanUtils.copyProperties(category, vo);
        return vo;
    }
}
