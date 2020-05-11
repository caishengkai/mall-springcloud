package com.csk.mall.portal.service;

import com.csk.mall.portal.dto.ConfirmOrderResult;
import com.csk.mall.portal.dto.OrderParam;

import java.util.Map;

/**
 * @description: 前台订单模块接口
 * @author: caishengkai
 * @time: 2020/5/10 16:11
 */
public interface OmsPortalOrderService {

    /**
     * 根据购物车生成订单确认单
     * @return
     */
    ConfirmOrderResult generateConfirmOrder();

    /**
     * 根据购物车生成订单
     * @param orderParam
     * @return
     */
    Map<String, Object> generateOrder(OrderParam orderParam);

    /**
     * 取消指定的订单
     * @param orderId
     */
    void cancelOrder(Long orderId);
}
