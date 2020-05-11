package com.csk.mall.portal.dto;

import com.csk.mall.model.OmsCartItem;
import com.csk.mall.model.UmsMemberReceiveAddress;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description:
 * @author: caishengkai
 * @time: 2020/5/10 16:15
 */
@Getter
@Setter
public class ConfirmOrderResult {

    //购物车商品信息
    private List<OmsCartItem> cartItemList;
    //用户收货地址信息
    private List<UmsMemberReceiveAddress> memberReceiveAddressList;
    //计算的金额
    private CalcAmount calcAmount;

    @Getter
    @Setter
    public static class CalcAmount {
        //订单商品总金额
        private BigDecimal totalAmount;
        //运费
        private BigDecimal freightAmount;
        //活动优惠
        private BigDecimal promotionAmount;
        //应付金额
        private BigDecimal payAmount;
    }
}
