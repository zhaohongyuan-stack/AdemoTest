package com.fengrui.ademotest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengrui.ademotest.entity.Supplier;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SupplierMapper extends BaseMapper<Supplier> {
}
