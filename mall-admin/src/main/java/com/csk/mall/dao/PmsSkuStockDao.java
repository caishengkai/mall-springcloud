package com.csk.mall.dao;

import com.csk.mall.model.PmsSkuStock;

import java.util.List;

/**
 * @description: sku库存额外查询接口
 * @author: caishengkai
 * @time: 2020/5/4 10:58
 */
public interface PmsSkuStockDao {
    void insertList(List<PmsSkuStock> skuStockList, Long productId);
}
