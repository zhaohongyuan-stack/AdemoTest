package com.fengrui.ademotest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fengrui.ademotest.dto.PurchaseOrderCreateDTO;
import com.fengrui.ademotest.entity.PurchaseOrder;
import com.fengrui.ademotest.vo.PurchaseOrderVO;

public interface PurchaseOrderService extends IService<PurchaseOrder> {

    /**
     * 创建采购单
     * @param dto 采购单创建DTO
     * @return 采购单VO
     */
    PurchaseOrderVO createPurchaseOrder(PurchaseOrderCreateDTO dto);

    /**
     * 采购单入库
     * @param orderId 采购单ID
     */
    void receivePurchaseOrder(Integer orderId);

    /**
     * 取消采购单
     * @param orderId 采购单ID
     */
    void cancelPurchaseOrder(Integer orderId);

    /**
     * 根据ID查询采购单详情（含明细）
     * @param orderId 采购单ID
     * @return 采购单VO
     */
    PurchaseOrderVO getPurchaseOrderDetail(Integer orderId);
}
