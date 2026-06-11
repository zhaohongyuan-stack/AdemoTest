package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.SaleOrderItem;
import com.fengrui.ademotest.mapper.SaleOrderItemMapper;
import com.fengrui.ademotest.service.SaleOrderItemService;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderItemServiceImpl extends ServiceImpl<SaleOrderItemMapper, SaleOrderItem> implements SaleOrderItemService {
}
