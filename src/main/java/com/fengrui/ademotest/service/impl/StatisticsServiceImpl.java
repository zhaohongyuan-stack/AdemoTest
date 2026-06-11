package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fengrui.ademotest.entity.Product;
import com.fengrui.ademotest.entity.PurchaseOrder;
import com.fengrui.ademotest.entity.SaleOrder;
import com.fengrui.ademotest.entity.SaleOrderItem;
import com.fengrui.ademotest.enums.OrderStatusEnum;
import com.fengrui.ademotest.mapper.ProductMapper;
import com.fengrui.ademotest.mapper.PurchaseOrderMapper;
import com.fengrui.ademotest.mapper.SaleOrderItemMapper;
import com.fengrui.ademotest.mapper.SaleOrderMapper;
import com.fengrui.ademotest.service.StatisticsService;
import com.fengrui.ademotest.vo.ProductSalesRankVO;
import com.fengrui.ademotest.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private SaleOrderMapper saleOrderMapper;

    @Autowired
    private SaleOrderItemMapper saleOrderItemMapper;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public StatisticsVO getTodayPurchaseStatistics() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<PurchaseOrder> orders = purchaseOrderMapper.selectList(
                new LambdaQueryWrapper<PurchaseOrder>()
                        .eq(PurchaseOrder::getOrderStatus, OrderStatusEnum.COMPLETED.getCode())
                        .ge(PurchaseOrder::getCreateTime, startOfDay)
                        .le(PurchaseOrder::getCreateTime, endOfDay)
        );

        BigDecimal totalAmount = orders.stream()
                .map(PurchaseOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new StatisticsVO(totalAmount, orders.size());
    }

    @Override
    public StatisticsVO getTodaySaleStatistics() {
        LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        List<SaleOrder> orders = saleOrderMapper.selectList(
                new LambdaQueryWrapper<SaleOrder>()
                        .eq(SaleOrder::getOrderStatus, OrderStatusEnum.COMPLETED.getCode())
                        .ge(SaleOrder::getCreateTime, startOfDay)
                        .le(SaleOrder::getCreateTime, endOfDay)
        );

        BigDecimal totalAmount = orders.stream()
                .map(SaleOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new StatisticsVO(totalAmount, orders.size());
    }

    @Override
    public List<ProductSalesRankVO> getProductSalesRank(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询指定时间范围内已出库的销售订单
        List<SaleOrder> orders = saleOrderMapper.selectList(
                new LambdaQueryWrapper<SaleOrder>()
                        .eq(SaleOrder::getOrderStatus, OrderStatusEnum.COMPLETED.getCode())
                        .ge(startTime != null, SaleOrder::getCreateTime, startTime)
                        .le(endTime != null, SaleOrder::getCreateTime, endTime)
        );

        if (orders.isEmpty()) {
            return List.of();
        }

        List<Integer> orderIds = orders.stream()
                .map(SaleOrder::getOrderId)
                .collect(Collectors.toList());

        // 查询销售明细
        List<SaleOrderItem> items = saleOrderItemMapper.selectList(
                new LambdaQueryWrapper<SaleOrderItem>()
                        .in(SaleOrderItem::getOrderId, orderIds)
        );

        // 按商品ID分组统计
        Map<Integer, ProductSalesRankVO> rankMap = items.stream()
                .collect(Collectors.groupingBy(
                        SaleOrderItem::getProductId,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                itemList -> {
                                    Integer totalQuantity = itemList.stream()
                                            .mapToInt(SaleOrderItem::getQuantity)
                                            .sum();
                                    BigDecimal totalAmount = itemList.stream()
                                            .map(SaleOrderItem::getTotalPrice)
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    return new ProductSalesRankVO(
                                            itemList.get(0).getProductId(),
                                            null,
                                            totalQuantity,
                                            totalAmount
                                    );
                                }
                        )
                ));

        // 查询商品信息并填充名称
        List<Integer> productIds = rankMap.keySet().stream().collect(Collectors.toList());
        List<Product> products = productMapper.selectBatchIds(productIds);
        Map<Integer, String> productNameMap = products.stream()
                .collect(Collectors.toMap(Product::getProductId, Product::getProductName));

        rankMap.forEach((productId, vo) -> vo.setProductName(productNameMap.get(productId)));

        // 按销量倒序排列，取前10名
        return rankMap.values().stream()
                .sorted((a, b) -> b.getTotalQuantity().compareTo(a.getTotalQuantity()))
                .limit(10)
                .collect(Collectors.toList());
    }
}
