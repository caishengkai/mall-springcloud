package com.csk.mall.service;

import com.csk.mall.dto.PmsProductAttributeCategoryItem;
import com.csk.mall.model.PmsProductAttributeCategory;

import java.util.List;

/**
 * @description: 商品属性分类相关接口
 * @author: caishengkai
 * @time: 2020/5/1 15:44
 */
public interface PmsProductAttributeCategoryService {
    List<PmsProductAttributeCategory> list(String keyword, Integer pageSize, Integer pageNum);

    int create(String name);

    int update(Long id, String name);

    int delete(Long id);

    PmsProductAttributeCategory getItem(Long id);

    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
