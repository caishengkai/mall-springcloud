package com.csk.mall.dao;

import com.csk.mall.dto.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * @description: 商品分类额外查询接口
 * @author: caishengkai
 * @time: 2020/5/3 14:00
 */
public interface PmsProductCategoryDao {
    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
