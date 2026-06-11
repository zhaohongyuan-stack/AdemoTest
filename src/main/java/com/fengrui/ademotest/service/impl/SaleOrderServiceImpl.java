package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.dto.SaleOrderCreateDTO;
import com.fengrui.ademotest.entity.CurrentStock;
import com.fengrui.ademotest.entity.SaleOrder;
import com.fengrui.ademotest.entity.SaleOrderItem;
import com.fengrui.ademotest.entity.StockRecord;
import com.fengrui.ademotest.enums.OrderStatusEnum;
import com.fengrui.ademotest.enums.StockTypeEnum;
import com.fengrui.ademotest.exception.BusinessException;
import com.fengrui.ademotest.exception.ErrorCode;
import com.fengrui.ademotest.mapper.CurrentStockMapper;
import com.fengrui.ademotest.mapper.StockRecordMapper;
import com.fengrui.ademotest.mapper.SaleOrderMapper;
import com.fengrui.ademotest.service.SaleOrderItemService;
import com.fengrui.ademotest.service.SaleOrderService;
import com.fengrui.ademotest.utils.OrderNoGenerator;
import com.fengrui.ademotest.vo.SaleOrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder> implements SaleOrderService {

    @Autowired
    private SaleOrderItemService saleOrderItemService;

    @Autowired
    private CurrentStockMapper currentStockMapper;

    @Autowired
    private StockRecordMapper stockRecordMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SaleOrderVO createSaleOrder(SaleOrderCreateDTO dto) {
        // 1. 生成销售单号
        String orderNo = OrderNoGenerator.generateSaleOrderNo();

        // 2. 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (SaleOrderCreateDTO.SaleOrderItemDTO itemDTO : dto.getItems()) {
            totalAmount = totalAmount.add(itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
        }

        // 3. 创建销售单主表
        SaleOrder order = new SaleOrder();
        order.setOrderNo(orderNo);
        order.setCustomerId(dto.getCustomerId());
        order.setTotalAmount(totalAmount);
        order.setOrderStatus(OrderStatusEnum.PENDING.getCode());
        order.setCreateTime(LocalDateTime.now());
        this.save(order);

        // 4. 保存销售单明细
        List<SaleOrderItem> items = dto.getItems().stream().map(itemDTO -> {
            SaleOrderItem item = new SaleOrderItem();
            item.setOrderId(order.getOrderId());
            item.setProductId(itemDTO.getProductId());
            item.setQuantity(itemDTO.getQuantity());
            item.setUnitPrice(itemDTO.getUnitPrice());
            item.setTotalPrice(itemDTO.getUnitPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity())));
            return item;
        }).collect(Collectors.toList());

        saleOrderItemService.saveBatch(items);

        // 5. 返回VO
        return buildSaleOrderVO(order, items);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void shipSaleOrder(Integer orderId) {
        // 1. 查询销售单
        SaleOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        // 2. 校验订单状态
        if (!OrderStatusEnum.PENDING.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有待出库状态的销售单才能出库");
        }

        // 3. 查询销售单明细
        List<SaleOrderItem> items = saleOrderItemService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SaleOrderItem>()
                        .eq(SaleOrderItem::getOrderId, orderId)
        );

        // 4. 遍历明细，校验库存并扣减
        for (SaleOrderItem item : items) {
            // 4.1 查询库存记录
            CurrentStock stock = currentStockMapper.selectOne(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<CurrentStock>()
                            .eq(CurrentStock::getProductId, item.getProductId())
            );

            if (stock == null || stock.getQuantity() < item.getQuantity()) {
                throw new BusinessException(ErrorCode.STOCK_NOT_FOUND, "商品ID:" + item.getProductId() + " 库存不足");
            }

            // 4.2 扣减库存
            stock.setQuantity(stock.getQuantity() - item.getQuantity());
            stock.setUpdateTime(LocalDateTime.now());
            currentStockMapper.updateById(stock);

            // 4.3 生成库存流水记录
            StockRecord record = new StockRecord();
            record.setProductId(item.getProductId());
            record.setType(StockTypeEnum.SALE_OUT.getCode());
            record.setQuantity(item.getQuantity());
            record.setRelatedOrderNo(order.getOrderNo());
            record.setRemark("销售出库");
            record.setCreateTime(LocalDateTime.now());
            stockRecordMapper.insert(record);
        }

        // 5. 更新销售单状态为已出库
        order.setOrderStatus(OrderStatusEnum.COMPLETED.getCode());
        this.updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelSaleOrder(Integer orderId) {
        // 1. 查询销售单
        SaleOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        // 2. 校验订单状态
        if (!OrderStatusEnum.PENDING.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException(ErrorCode.ORDER_STATUS_ERROR, "只有待出库状态的销售单才能取消");
        }

        // 3. 更新订单状态为已取消
        order.setOrderStatus(OrderStatusEnum.CANCELLED.getCode());
        this.updateById(order);
    }

    @Override
    public SaleOrderVO getSaleOrderDetail(Integer orderId) {
        SaleOrder order = this.getById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }

        List<SaleOrderItem> items = saleOrderItemService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<SaleOrderItem>()
                        .eq(SaleOrderItem::getOrderId, orderId)
        );

        return buildSaleOrderVO(order, items);
    }

    /**
     * 构建SaleOrderVO
     */
    private SaleOrderVO buildSaleOrderVO(SaleOrder order, List<SaleOrderItem> items) {
        SaleOrderVO vo = new SaleOrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setOrderStatusDesc(OrderStatusEnum.getByCode(order.getOrderStatus()).getDesc());
        vo.setItems(items);
        return vo;
    }
}
