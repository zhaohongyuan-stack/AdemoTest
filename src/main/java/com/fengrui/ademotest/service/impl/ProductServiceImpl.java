package com.fengrui.ademotest.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fengrui.ademotest.entity.Product;
import com.fengrui.ademotest.mapper.ProductMapper;
import com.fengrui.ademotest.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
