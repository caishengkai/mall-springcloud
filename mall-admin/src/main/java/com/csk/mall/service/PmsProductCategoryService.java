package com.csk.mall.service;

import com.csk.mall.dto.PmsProductCategoryParam;
import com.csk.mall.dto.PmsProductCategoryWithChildrenItem;
import com.csk.mall.model.PmsProductCategory;

import java.util.List;

/**
 * @description: 商品分类相关接口
 * @author: caishengkai
 * @time: 2020/5/1 15:17
 */
public interface PmsProductCategoryService {

    List<PmsProductCategory> list(Long parentId, Integer pageSize, Integer pageNum);

    int create(PmsProductCategoryParam productCategoryParam);

    int update(Long id, PmsProductCategoryParam productCategoryParam);

    PmsProductCategory getItem(Long id);

    int delete(Long id);

    List<PmsProductCategoryWithChildrenItem> listWithChildren();
}
