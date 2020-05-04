package com.csk.mall.dto;

import com.csk.mall.model.PmsProduct;
import com.csk.mall.model.PmsProductAttributeValue;
import com.csk.mall.model.PmsSkuStock;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 商品参数
 * @author: caishengkai
 * @time: 2020/5/2 16:16
 */
@Getter
@Setter
public class PmsProductParam extends PmsProduct {

    @ApiModelProperty("商品的sku库存信息")
    private List<PmsSkuStock> skuStockList;
    @ApiModelProperty("商品参数及自定义规格属性")
    private List<PmsProductAttributeValue> productAttributeValueList;

}
