package com.fengrui.ademotest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengrui.ademotest.dto.SaleOrderCreateDTO;
import com.fengrui.ademotest.entity.SaleOrder;
import com.fengrui.ademotest.vo.SaleOrderVO;

public interface SaleOrderService extends IService<SaleOrder> {

    /**
     * 创建销售单
     * @param dto 销售单创建DTO
     * @return 销售单VO
     */
    SaleOrderVO createSaleOrder(SaleOrderCreateDTO dto);

    /**
     * 销售单出库
     * @param orderId 销售单ID
     */
    void shipSaleOrder(Integer orderId);

    /**
     * 取消销售单
     * @param orderId 销售单ID
     */
    void cancelSaleOrder(Integer orderId);

    /**
     * 根据ID查询销售单详情（含明细）
     * @param orderId 销售单ID
     * @return 销售单VO
     */
    SaleOrderVO getSaleOrderDetail(Integer orderId);
}
