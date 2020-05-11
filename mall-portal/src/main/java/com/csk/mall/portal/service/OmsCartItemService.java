package com.csk.mall.portal.service;

import com.csk.mall.model.OmsCartItem;
import com.csk.mall.portal.dto.CartProduct;

import java.util.List;

/**
 * @description: 购物车相关接口
 * @author: caishengkai
 * @time: 2020/5/10 15:30
 */
public interface OmsCartItemService {

    /**
     * 添加商品到购物车
     * @param cartItem
     * @return
     */
    int add(OmsCartItem cartItem);

    /**
     * 当前用户购物车列表
     * @return
     */
    List<OmsCartItem> list();

    /**
     * 更新购物车中商品数量
     * @param id
     * @param quantity
     * @return
     */
    int updateQuantity(Long id, Integer quantity);

    /**
     * 获取购物车中商品的商品规格
     * @param productId
     * @return
     */
    CartProduct getCartProduct(Long productId);

    /**
     * 修改购物车中商品的商品规格
     * @param cartItem
     * @return
     */
    int updateAttr(OmsCartItem cartItem);

    /**
     * 删除购物车中的商品
     * @param ids
     * @return
     */
    int delete(List<Long> ids);

    /**
     * 清空购物车
     * @return
     */
    int clear();
}
