package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.PurchaseOrderItem;
import com.fengrui.ademotest.mapper.PurchaseOrderItemMapper;
import com.fengrui.ademotest.service.PurchaseOrderItemService;
import org.springframework.stereotype.Service;

@Service
public class PurchaseOrderItemServiceImpl extends ServiceImpl<PurchaseOrderItemMapper, PurchaseOrderItem> implements PurchaseOrderItemService {
}
