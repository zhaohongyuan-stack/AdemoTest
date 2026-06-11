package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.SaleOrder;
import com.fengrui.ademotest.mapper.SaleOrderMapper;
import com.fengrui.ademotest.service.SaleOrderService;
import org.springframework.stereotype.Service;

@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder> implements SaleOrderService {
}
