package com.fengrui.ademotest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengrui.ademotest.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {
}
