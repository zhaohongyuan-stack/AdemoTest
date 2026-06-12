package com.fengrui.ademotest.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductCategoryTreeVO {

    private Integer categoryId;
    private String categoryName;
    private Integer parentId;
    private Integer sortOrder;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<ProductCategoryTreeVO> children;
}
