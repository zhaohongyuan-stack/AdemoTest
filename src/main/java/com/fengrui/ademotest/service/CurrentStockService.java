package com.fengrui.ademotest.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fengrui.ademotest.entity.CurrentStock;
import com.fengrui.ademotest.vo.CurrentStockVO;

import java.util.List;

public interface CurrentStockService extends IService<CurrentStock> {

    /**
     * 分页查询商品当前库存
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param productName 商品名称（模糊查询）
     * @return 库存VO分页结果
     */
    Page<CurrentStockVO> queryStockPage(Integer pageNum, Integer pageSize, String productName);

    /**
     * 库存预警查询
     * @return 库存低于预警值的商品列表
     */
    List<CurrentStockVO> getWarningStockList();
}
