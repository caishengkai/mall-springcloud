package com.csk.mall.portal.dao;

import com.csk.mall.model.OmsOrderItem;

import java.util.List;

/**
 * @description:
 * @author: caishengkai
 * @time: 2020/5/10 20:57
 */
public interface OmsPortalOrderDao {
    /**
     * 解除订单商品库存锁定
     * @param orderItemList
     */
    void releaseSkuStockLock(List<OmsOrderItem> orderItemList);
}
