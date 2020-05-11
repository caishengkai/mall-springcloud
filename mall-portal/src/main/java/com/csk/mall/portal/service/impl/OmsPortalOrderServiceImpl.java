package com.csk.mall.portal.service.impl;

import com.csk.mall.common.service.RedisService;
import com.csk.mall.exception.MallAsserts;
import com.csk.mall.mapper.OmsOrderItemMapper;
import com.csk.mall.mapper.OmsOrderMapper;
import com.csk.mall.mapper.PmsSkuStockMapper;
import com.csk.mall.model.*;
import com.csk.mall.portal.component.CancelOrderSender;
import com.csk.mall.portal.dao.OmsPortalOrderDao;
import com.csk.mall.portal.dao.OmsPortalOrderItemDao;
import com.csk.mall.portal.dto.ConfirmOrderResult;
import com.csk.mall.portal.dto.OrderParam;
import com.csk.mall.portal.service.OmsCartItemService;
import com.csk.mall.portal.service.OmsPortalOrderService;
import com.csk.mall.portal.service.UmsMemberReceiveAddressService;
import com.csk.mall.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @description: 前台订单模块接口实现类
 * @author: caishengkai
 * @time: 2020/5/10 16:11
 */
@Service
public class OmsPortalOrderServiceImpl implements OmsPortalOrderService {

    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private OmsCartItemService cartItemService;
    @Autowired
    private UmsMemberReceiveAddressService memberReceiveAddressService;
    @Autowired
    private OmsPortalOrderDao portalOrderDao;
    @Autowired
    private OmsOrderMapper orderMapper;
    @Autowired
    private OmsPortalOrderItemDao orderItemDao;
    @Autowired
    private OmsOrderItemMapper orderItemMapper;
    @Autowired
    private PmsSkuStockMapper skuStockMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private CancelOrderSender cancelOrderSender;

    @Value("${mall.order.overtime}")
    private Integer MALL_ORDER_OVERTIME;
    @Value("${redis.key.orderId}")
    private String REDIS_KEY_ORDER_ID;
    @Value("${redis.database}")
    private String REDIS_DATABASE;

    @Override
    public ConfirmOrderResult generateConfirmOrder() {
        ConfirmOrderResult result = new ConfirmOrderResult();
        //获取购物车信息
        List<OmsCartItem> cartItemList = cartItemService.list();
        result.setCartItemList(cartItemList);
        //获取用户收货地址信息
        List<UmsMemberReceiveAddress> memberReceiveAddressList = memberReceiveAddressService.list();
        result.setMemberReceiveAddressList(memberReceiveAddressList);
        //计算总金额、活动优惠、应付金额
        ConfirmOrderResult.CalcAmount calcAmount = calcCartAmount(cartItemList);
        result.setCalcAmount(calcAmount);
        return result;
    }

    /**
     * 计算购物车中商品的价格
     *
     * @param cartItemList
     * @return
     */
    private ConfirmOrderResult.CalcAmount calcCartAmount(List<OmsCartItem> cartItemList) {
        ConfirmOrderResult.CalcAmount calcAmount = new ConfirmOrderResult.CalcAmount();
        calcAmount.setFreightAmount(new BigDecimal(0));
        BigDecimal totalAmount = new BigDecimal("0");
        BigDecimal promotionAmount = new BigDecimal("0");
        // TODO 这里暂时没有考虑 优惠、促销的功能
        for (OmsCartItem cartItem : cartItemList) {
            totalAmount = totalAmount.add(cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }
        calcAmount.setTotalAmount(totalAmount);
        calcAmount.setPromotionAmount(promotionAmount);
        calcAmount.setPayAmount(totalAmount.subtract(promotionAmount));
        return calcAmount;
    }

    @Override
    public Map<String, Object> generateOrder(OrderParam orderParam) {
        UmsMember member = memberService.getCurrentMember();
        //获取购物车信息
        List<OmsCartItem> cartItemList = cartItemService.list();
        List<OmsOrderItem> orderItemList = new ArrayList<>();
        for (OmsCartItem cartItem : cartItemList) {
            OmsOrderItem orderItem = new OmsOrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProductName(cartItem.getProductName());
            orderItem.setProductPic(cartItem.getProductPic());
            orderItem.setProductAttr(cartItem.getProductAttr());
            orderItem.setProductBrand(cartItem.getProductBrand());
            orderItem.setProductSn(cartItem.getProductSn());
            orderItem.setProductPrice(cartItem.getPrice());
            orderItem.setRealAmount(cartItem.getPrice());
            orderItem.setProductQuantity(cartItem.getQuantity());
            orderItem.setProductSkuId(cartItem.getProductSkuId());
            orderItem.setProductSkuCode(cartItem.getProductSkuCode());
            orderItem.setProductCategoryId(cartItem.getProductCategoryId());
            // TODO 未集成优惠模块
            orderItem.setCouponAmount(new BigDecimal(0));
            orderItem.setPromotionAmount(new BigDecimal(0));
            orderItem.setPromotionName("无优惠");
            // TODO 未集成积分模块
            orderItem.setIntegrationAmount(new BigDecimal(0));
            orderItem.setGiftIntegration(0);
            orderItem.setGiftGrowth(0);
            orderItemList.add(orderItem);
        }

        //判断购物车中商品是否都有库存
        if (!hasStock(cartItemList)) {
            MallAsserts.fail("库存不足，无法下单");
        }

        //进行库存锁定
        lockStock(cartItemList);

        //TODO 计算order_item的实付金额 实付金额 = 商品售价-优惠、促销等 目前实付金额 = 商品售价
        //handleRealAmount(orderItemList);

        //根据商品合计、运费、活动优惠、优惠券、积分计算应付金额
        OmsOrder order = new OmsOrder();
        order.setDiscountAmount(new BigDecimal(0));
        order.setTotalAmount(calcTotalAmount(orderItemList));
        order.setFreightAmount(new BigDecimal(0));
        //order.setPromotionAmount(calcPromotionAmount(orderItemList));
        //order.setPromotionInfo(getOrderPromotionInfo(orderItemList));
        order.setPayAmount(calcPayAmount(order));
        //转化为订单信息并插入数据库
        order.setMemberId(member.getId());
        order.setCreateTime(new Date());
        order.setMemberUsername(member.getUsername());
        //支付方式：0->未支付；1->支付宝；2->微信
        order.setPayType(orderParam.getPayType());
        //订单来源：0->PC订单；1->app订单
        order.setSourceType(1);
        //订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单
        order.setStatus(0);
        //订单类型：0->正常订单；1->秒杀订单
        order.setOrderType(0);
        //收货人信息：姓名、电话、邮编、地址
        UmsMemberReceiveAddress address = memberReceiveAddressService.getItem(orderParam.getMemberReceiveAddressId());
        order.setReceiverName(address.getName());
        order.setReceiverPhone(address.getPhoneNumber());
        order.setReceiverPostCode(address.getPostCode());
        order.setReceiverProvince(address.getProvince());
        order.setReceiverCity(address.getCity());
        order.setReceiverRegion(address.getRegion());
        order.setReceiverDetailAddress(address.getDetailAddress());
        //0->未确认；1->已确认
        order.setConfirmStatus(0);
        order.setDeleteStatus(0);
        // TODO 计算赠送积分
        //order.setIntegration(calcGifIntegration(orderItemList));
        //计算赠送成长值
        //order.setGrowth(calcGiftGrowth(orderItemList));
        // TODO 生成订单号
        order.setOrderSn(generateOrderSn(order));
        //插入order表和order_item表
        orderMapper.insert(order);
        for (OmsOrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
            orderItem.setOrderSn(order.getOrderSn());
        }
        orderItemDao.insertList(orderItemList);
        // TODO 如使用优惠券更新优惠券使用状态
        /*if (orderParam.getCouponId() != null) {
            updateCouponStatus(orderParam.getCouponId(), currentMember.getId(), 1);
        }*/
        // TODO 如使用积分需要扣除积分
        /*if (orderParam.getUseIntegration() != null) {
            order.setUseIntegration(orderParam.getUseIntegration());
            memberService.updateIntegration(currentMember.getId(), currentMember.getIntegration() - orderParam.getUseIntegration());
        }*/
        //删除购物车中的下单商品
        deleteCartItemList(cartItemList);
        //发送延迟消息取消订单
        sendDelayMessageCancelOrder(order.getId());
        Map<String, Object> result = new HashMap<>();
        result.put("order", order);
        result.put("orderItemList", orderItemList);
        return result;
    }

    @Override
    public void cancelOrder(Long orderId) {
        //获取订单
        OmsOrder cancelOrder = orderMapper.selectByPrimaryKey(orderId);
        if (cancelOrder != null && "0".equals(cancelOrder.getDeleteStatus())) {
            //修改订单状态为取消
            cancelOrder.setStatus(4);
            orderMapper.updateByPrimaryKeySelective(cancelOrder);
            OmsOrderItemExample orderItemExample = new OmsOrderItemExample();
            orderItemExample.createCriteria().andOrderIdEqualTo(orderId);
            List<OmsOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
            //解除订单商品库存锁定
            if (!CollectionUtils.isEmpty(orderItemList)) {
                portalOrderDao.releaseSkuStockLock(orderItemList);
            }
        }
    }

    /**
     * 往RabbitMQ里发送延迟消息
     * @param orderId
     */
    private void sendDelayMessageCancelOrder(Long orderId) {
        //
        long delayTimes = MALL_ORDER_OVERTIME * 60 * 1000;
        //发送延迟消息
        cancelOrderSender.sendMessage(orderId, delayTimes);
    }

    /**
     * 删除下单商品的购物车信息
     * @param cartItemList
     */
    private void deleteCartItemList(List<OmsCartItem> cartItemList) {
        List<Long> ids = new ArrayList<>();
        for (OmsCartItem cartItem : cartItemList) {
            ids.add(cartItem.getId());
        }
        cartItemService.delete(ids);
    }

    /**
     * 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id
     * @param order
     * @return
     */
    private String generateOrderSn(OmsOrder order) {
        StringBuilder sb = new StringBuilder();
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String key = REDIS_DATABASE+":"+ REDIS_KEY_ORDER_ID + date;
        Long increment = redisService.incr(key, 1);
        sb.append(date);
        sb.append(String.format("%02d", order.getSourceType()));
        sb.append(String.format("%02d", order.getPayType()));
        String incrementStr = increment.toString();
        if (incrementStr.length() <= 6) {
            sb.append(String.format("%06d", increment));
        } else {
            sb.append(incrementStr);
        }
        return sb.toString();
    }

    /**
     * 计算订单总金额 总金额=商品总额+运费-促销优惠-优惠券优惠-积分抵扣
     * @param order
     * @return
     */
    private BigDecimal calcPayAmount(OmsOrder order) {
        // TODO 目前 总金额=商品总额+运费
        BigDecimal payAmount = order.getTotalAmount()
                .add(order.getFreightAmount());
        return payAmount;
    }

    /**
     * 计算总金额
     * @param orderItemList
     * @return
     */
    private BigDecimal calcTotalAmount(List<OmsOrderItem> orderItemList) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsOrderItem item : orderItemList) {
            totalAmount = totalAmount.add(item.getProductPrice().multiply(new BigDecimal(item.getProductQuantity())));
        }
        return totalAmount;
    }

    /**
     * 锁定订单中商品所需库存
     * @param cartItemList
     */
    private void lockStock(List<OmsCartItem> cartItemList) {
        for (OmsCartItem cartItem : cartItemList) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(cartItem.getProductSkuId());
            skuStock.setLockStock(skuStock.getLockStock() + cartItem.getQuantity());
            skuStockMapper.updateByPrimaryKeySelective(skuStock);
        }
    }

    /**
     * 判断订单中商品是否有库存
     * @param cartItemList
     * @return
     */
    private boolean hasStock(List<OmsCartItem> cartItemList) {
        for (OmsCartItem cartItem : cartItemList) {
            PmsSkuStock skuStock = skuStockMapper.selectByPrimaryKey(cartItem.getProductSkuId());
            if (skuStock.getStock() - (skuStock.getLockStock() + cartItem.getQuantity()) < 0) {
                return false;
            }
        }
        return true;
    }
}
