package com.fengrui.ademotest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengrui.ademotest.entity.CurrentStock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CurrentStockMapper extends BaseMapper<CurrentStock> {
}
