package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.dto.PurchaseOrderCreateDTO;
import com.fengrui.ademotest.entity.CurrentStock;
import com.fengrui.ademotest.entity.PurchaseOrder;
import com.fengrui.ademotest.entity.PurchaseOrderItem;
import com.fengrui.ademotest.entity.StockRecord;
import com.fengrui.ademotest.enums.OrderStatusEnum;
import com.fengrui.ademotest.enums.StockTypeEnum;
import com.fengrui.ademotest.exception.BusinessException;
import com.fengrui.ademotest.exception.ErrorCode;
import com.fengrui.ademotest.mapper.CurrentStockMapper;
import com.fengrui.ademotest.mapper.PurchaseOrderItemMapper;
import com.fengrui.ademotest.mapper.PurchaseOrderMapper;
import com.fengrui.ademotest.mapper.StockRecordMapper;
import com.fengrui.ademotest.service.PurchaseOrderService;
import com.fengrui.ademotest.utils.OrderNoGenerator;
import com.fengrui.ademotest.vo.PurchaseOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {

    @Autowired
    private com.fengrui.ademotest.service.PurchaseOrderItemService purchaseOrderItemService;

    @Autowired
    private CurrentStockMapper currentStockMapper;

    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchaseOrderVO createPurchaseOrder(PurchaseOrderCreateDTO dto) {
        // 1. 生成采购单号
        String orderNo = OrderNoGenerator.generatePurchaseOrderNo();

        // 2. 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PurchaseOrderCreateDTO.PurchaseOrderItemDTO itemDTO : dto.getItems()) {
            totalAmount = totalAmount.add(itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }

        // 3. 创建采购单主表
        PurchaseOrder order = new PurchaseOrder();
        order.setOrderNo(orderNo);
        order.setSupplierId(dto.getSupplierId());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatusEnum.PENDING.getCode());
        order.setCreateTime(LocalDateTime.now());
        this.save(order);

        // 4. 保存采购单明细
        List<PurchaseOrderItem> items = dto.getItems().stream().map(itemDTO -> {
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setOrderId(order.getOrderId());
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setTotalPrice(itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            return item;
        }).collect(Collectors.toList());

        purchaseOrderItemService.saveBatch(items);

        // 5. 返回VO
        return buildPurchaseOrderVO(order, items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void receivePurchaseOrder(Integer orderId) {
        // 1. 查询采购单
        PurchaseOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        // 2. 校验订单状态
        if (!OrderStatusEnum.PENDING.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有待入库状态的订单才能入库");
        }

        // 3. 查询采购单明细
        List<PurchaseOrderItem> items = purchaseOrderItemService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PurchaseOrderItem>()
                        .eq(PurchaseOrderItem::getOrderId, orderId)
        );

        // 4. 遍历明细，更新库存并生成流水
        for (PurchaseOrderItem item : items) {
            // 4.1 查询或创建库存记录
            CurrentStock stock = currentStockMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<CurrentStock>()
                            .eq(CurrentStock::getProductId, item.getProductId())
            );

            if (stock == null) {
                // 创建新库存记录
                stock = new CurrentStock();
                stock.setProductId(item.getProductId());
                stock.setQuantity(item.getQuantity());
                stock.setUpdateTime(LocalDateTime.now());
                currentStockMapper.insert(stock);
            } else {
                // 更新现有库存
                stock.setQuantity(stock.getQuantity() + item.getQuantity());
                stock.setUpdateTime(LocalDateTime.now());
                currentStockMapper.updateById(stock);
            }

            // 4.2 生成库存流水记录
            StockRecord record = new StockRecord();
            record.setProductId(item.getProductId());
            record.setType(StockTypeEnum.PURCHASE_IN.getCode());
            record.setQuantity(item.getQuantity());
            record.setRelatedOrderNo(order.getOrderNo());
            record.setRemark("采购入库");
            record.setCreateTime(LocalDateTime.now());
            stockRecordMapper.insert(record);
        }

        // 5. 更新采购单状态为已入库
        order.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
        this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelPurchaseOrder(Integer orderId) {
        // 1. 查询采购单
        PurchaseOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        // 2. 校验订单状态
        if (!OrderStatusEnum.PENDING.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有待入库状态的订单才能取消");
        }

        // 3. 更新订单状态为已取消
        order.setOrderStatus(OrderStatusEnum.CANCELLED.getCode());
        this.updateById(order);
    }

    @Override
    public PurchaseOrderVO getPurchaseOrderDetail(Integer orderId) {
        PurchaseOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        List<PurchaseOrderItem> items = purchaseOrderItemService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<PurchaseOrderItem>()
                        .eq(PurchaseOrderItem::getOrderId, orderId)
        );

        return buildPurchaseOrderVO(order, items);
    }

    /**
     * 构建PurchaseOrderVO
     */
    private PurchaseOrderVO buildPurchaseOrderVO(PurchaseOrder order, List<PurchaseOrderItem> items) {
        PurchaseOrderVO vo = new PurchaseOrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderStatusDesc(OrderStatusEnum.getByCode(order.getOrderStatus()).getDesc());
        vo.setItems(items);
        return vo;
    }
}
