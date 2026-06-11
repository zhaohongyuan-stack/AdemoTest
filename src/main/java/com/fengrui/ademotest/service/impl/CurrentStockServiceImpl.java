package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.CurrentStock;
import com.fengrui.ademotest.mapper.CurrentStockMapper;
import com.fengrui.ademotest.service.CurrentStockService;
import org.springframework.stereotype.Service;

@Service
public class CurrentStockServiceImpl extends ServiceImpl<CurrentStockMapper, CurrentStock> implements CurrentStockService {
}
