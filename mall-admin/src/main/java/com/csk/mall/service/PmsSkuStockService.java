package com.csk.mall.service;

import com.csk.mall.model.PmsSkuStock;

import java.util.List;

/**
 * @description: 商品sku库存相关接口
 * @author: caishengkai
 * @time: 2020/5/4 14:47
 */
public interface PmsSkuStockService {
    List<PmsSkuStock> getList(Long pid, String keyword);

    int update(Long pid, List<PmsSkuStock> skuStockList);
}
