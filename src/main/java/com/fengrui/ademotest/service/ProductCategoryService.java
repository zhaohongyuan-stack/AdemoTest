package com.fengrui.ademotest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengrui.ademotest.entity.ProductCategory;
import com.fengrui.ademotest.vo.ProductCategoryTreeVO;

import java.util.List;

public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 获取分类树形结构
     * @return 分类树
     */
    List<ProductCategoryTreeVO> getCategoryTree();

    /**
     * 根据父级ID获取子分类列表
     * @param parentId 父级ID
     * @return 子分类列表
     */
    List<ProductCategory> getChildrenByParentId(Integer parentId);
}
