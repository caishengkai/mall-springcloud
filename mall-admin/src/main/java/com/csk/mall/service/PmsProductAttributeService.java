package com.csk.mall.service;


import com.csk.mall.dto.PmsProductAttributeParam;
import com.csk.mall.dto.ProductAttrInfo;
import com.csk.mall.model.PmsProductAttribute;

import java.util.List;

/**
 * @description: 商品属性相关接口
 * @author: caishengkai
 * @time: 2020/5/1 15:32
 */
public interface PmsProductAttributeService {
    List<PmsProductAttribute> list(Long cid, Integer type, String keyword, Integer pageSize, Integer pageNum);

    int create(PmsProductAttributeParam productAttributeParam);

    int update(Long id, PmsProductAttributeParam productAttributeParam);

    PmsProductAttribute getItem(Long id);

    int delete(List<Long> ids);

    List<ProductAttrInfo> getProductAttrInfo(Long productCategoryId);
}
