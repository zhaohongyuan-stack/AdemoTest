package com.fengrui.ademotest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengrui.ademotest.dto.StockRecordQueryDTO;
import com.fengrui.ademotest.entity.StockRecord;

public interface StockRecordService extends IService<StockRecord> {

    /**
     * 分页查询库存流水
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param queryDTO 查询条件
     * @return 库存流水分页结果
     */
    Page<StockRecord> queryRecordPage(Integer pageNum, Integer pageSize, StockRecordQueryDTO queryDTO);
}
