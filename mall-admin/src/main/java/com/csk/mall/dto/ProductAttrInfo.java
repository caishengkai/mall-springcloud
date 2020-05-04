package com.csk.mall.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 商品分类对应属性信息
 * @author: caishengkai
 * @time: 2020/5/3 15:13
 */
@Getter
@Setter
public class ProductAttrInfo {
    private Long attributeId;
    private Long attributeCategoryId;
}
