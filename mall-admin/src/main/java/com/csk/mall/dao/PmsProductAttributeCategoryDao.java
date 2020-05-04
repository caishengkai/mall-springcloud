package com.csk.mall.dao;

import com.csk.mall.dto.PmsProductAttributeCategoryItem;

import java.util.List;

/**
 * @description: 商品属性分类额外查询接口
 * @author: caishengkai
 * @time: 2020/5/3 14:22
 */
public interface PmsProductAttributeCategoryDao {
    List<PmsProductAttributeCategoryItem> getListWithAttr();
}
