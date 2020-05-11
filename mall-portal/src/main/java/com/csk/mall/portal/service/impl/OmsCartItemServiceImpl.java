package com.csk.mall.portal.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.csk.mall.mapper.OmsCartItemMapper;
import com.csk.mall.model.OmsCartItem;
import com.csk.mall.model.OmsCartItemExample;
import com.csk.mall.model.UmsMember;
import com.csk.mall.portal.dao.PortalProductDao;
import com.csk.mall.portal.dto.CartProduct;
import com.csk.mall.portal.service.OmsCartItemService;
import com.csk.mall.portal.service.UmsMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @description: 购物车相关实现类
 * @author: caishengkai
 * @time: 2020/5/10 15:31
 */
@Service
public class OmsCartItemServiceImpl implements OmsCartItemService {

    @Autowired
    private UmsMemberService memberService;
    @Autowired
    private OmsCartItemMapper cartItemMapper;
    @Autowired
    private PortalProductDao productDao;

    @Override
    public int add(OmsCartItem cartItem) {
        int count;
        UmsMember member = memberService.getCurrentMember();
        cartItem.setMemberId(member.getId());
        cartItem.setMemberNickname(member.getNickname());
        cartItem.setDeleteStatus(0);
        //判断购物车中是否已存在该商品
        OmsCartItem existCartItem = getCartItem(cartItem);
        if (existCartItem != null) {
            cartItem.setModifyDate(new Date());
            cartItem.setQuantity(existCartItem.getQuantity() + cartItem.getQuantity());
            count = cartItemMapper.updateByPrimaryKeySelective(cartItem);
        } else {
            cartItem.setCreateDate(new Date());
            count = cartItemMapper.insert(cartItem);
        }
        return count;
    }

    @Override
    public List<OmsCartItem> list() {
        UmsMember member = memberService.getCurrentMember();
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId()).andDeleteStatusEqualTo(0);
        return cartItemMapper.selectByExample(example);
    }

    @Override
    public int updateQuantity(Long id, Integer quantity) {
        UmsMember member = memberService.getCurrentMember();
        OmsCartItem cartItem = new OmsCartItem();
        cartItem.setQuantity(quantity);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andDeleteStatusEqualTo(0)
                .andIdEqualTo(id).andMemberIdEqualTo(member.getId());
        return cartItemMapper.updateByExampleSelective(cartItem, example);
    }

    @Override
    public CartProduct getCartProduct(Long productId) {
        return productDao.getCartProduct(productId);
    }

    @Override
    public int updateAttr(OmsCartItem cartItem) {
        cartItem.setModifyDate(new Date());
        return cartItemMapper.updateByPrimaryKeySelective(cartItem);
    }

    @Override
    public int delete(List<Long> ids) {
        UmsMember member = memberService.getCurrentMember();
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andIdIn(ids).andMemberIdEqualTo(member.getId());
        return cartItemMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int clear() {
        UmsMember member = memberService.getCurrentMember();
        OmsCartItem record = new OmsCartItem();
        record.setDeleteStatus(1);
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andMemberIdEqualTo(member.getId());
        return cartItemMapper.updateByExampleSelective(record,example);
    }

    /**
     * 根据商品id,用户id,商品规格获取购物车中的商品
     * @param cartItem
     * @return
     */
    private OmsCartItem getCartItem(OmsCartItem cartItem) {
        OmsCartItemExample example = new OmsCartItemExample();
        example.createCriteria().andProductIdEqualTo(cartItem.getProductId())
                .andMemberIdEqualTo(cartItem.getMemberId())
                .andDeleteStatusEqualTo(0);
        if (!StringUtils.isEmpty(cartItem.getProductSkuId())) {
            example.createCriteria().andProductSkuIdEqualTo(cartItem.getProductSkuId());
        }
        List<OmsCartItem> list = cartItemMapper.selectByExample(example);
        if (CollectionUtil.isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }
}
