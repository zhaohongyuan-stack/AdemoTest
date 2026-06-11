package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.dto.StockRecordQueryDTO;
import com.fengrui.ademotest.entity.StockRecord;
import com.fengrui.ademotest.mapper.StockRecordMapper;
import com.fengrui.ademotest.service.StockRecordService;
import org.springframework.stereotype.Service;

@Service
public class StockRecordServiceImpl extends ServiceImpl<StockRecordMapper, StockRecord> implements StockRecordService {

    @Override
    public Page<StockRecord> queryRecordPage(Integer pageNum, Integer pageSize, StockRecordQueryDTO queryDTO) {
        Page<StockRecord> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<StockRecord> wrapper = new LambdaQueryWrapper<>();
        
        // 按商品ID筛选
        if (queryDTO.getProductId() != null) {
            wrapper.eq(StockRecord::getProductId, queryDTO.getProductId());
        }
        
        // 按流水类型筛选
        if (queryDTO.getType() != null) {
            wrapper.eq(StockRecord::getType, queryDTO.getType());
        }
        
        // 按时间范围筛选
        if (queryDTO.getStartTime() != null) {
            wrapper.ge(StockRecord::getCreateTime, queryDTO.getStartTime());
        }
        if (queryDTO.getEndTime() != null) {
            wrapper.le(StockRecord::getCreateTime, queryDTO.getEndTime());
        }
        
        wrapper.orderByDesc(StockRecord::getCreateTime);
        
        return this.page(page, wrapper);
    }
}
