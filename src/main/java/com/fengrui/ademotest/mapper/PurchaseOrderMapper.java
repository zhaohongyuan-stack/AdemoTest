package com.fengrui.ademotest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fengrui.ademotest.entity.PurchaseOrder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
}
