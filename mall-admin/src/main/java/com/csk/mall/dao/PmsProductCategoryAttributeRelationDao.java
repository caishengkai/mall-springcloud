package com.csk.mall.dao;

import com.csk.mall.dto.ProductAttrInfo;
import com.csk.mall.model.PmsProductCategoryAttributeRelation;

import java.util.List;

/**
 * @description: 商品分类和属性关系表额外查询接口
 * @author: caishengkai
 * @time: 2020/5/2 14:40
 */
public interface PmsProductCategoryAttributeRelationDao {
    void insertList(List<PmsProductCategoryAttributeRelation> list);

    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
