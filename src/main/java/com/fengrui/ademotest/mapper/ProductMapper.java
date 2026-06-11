package com.fengrui.ademotest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengrui.ademotest.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
