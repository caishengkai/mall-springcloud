package com.csk.mall.dto;

import com.csk.mall.model.PmsProductCategory;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 商品分类包含子节点实体封装
 * @author: caishengkai
 * @time: 2020/5/3 13:55
 */
@Getter
@Setter
public class PmsProductCategoryWithChildrenItem extends PmsProductCategory{
    private List<PmsProductCategory> children;
}
