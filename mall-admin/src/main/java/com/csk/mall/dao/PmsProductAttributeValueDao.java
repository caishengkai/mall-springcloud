package com.csk.mall.dao;

import com.csk.mall.model.PmsProductAttributeValue;

import java.util.List;

/**
 * @description: 商品参数，商品自定义规格属性额外查询接口
 * @author: caishengkai
 * @time: 2020/5/4 11:00
 */
public interface PmsProductAttributeValueDao {
    void insertList(List<PmsProductAttributeValue> productAttributeValueList, Long productId);
}
