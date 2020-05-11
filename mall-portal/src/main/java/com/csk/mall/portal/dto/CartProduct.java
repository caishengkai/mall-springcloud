package com.csk.mall.portal.dto;

import com.csk.mall.model.PmsProductAttribute;
import com.csk.mall.model.PmsSkuStock;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 购物车中选择规格的商品信息
 * @author: caishengkai
 * @time: 2020/5/10 15:46
 */
@Getter
@Setter
public class CartProduct {
    private List<PmsProductAttribute> productAttributeList;
    private List<PmsSkuStock> skuStockList;
}
