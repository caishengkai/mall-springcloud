package com.csk.mall.portal.dao;

import com.csk.mall.portal.dto.CartProduct;

/**
 * @description: 前台系统自定义商品Dao
 * @author: caishengkai
 * @time: 2020/5/10 15:59
 */
public interface PortalProductDao {

    /**
     * 获取购物车中商品的商品规格
     * @param productId
     * @return
     */
    CartProduct getCartProduct(Long productId);
}
