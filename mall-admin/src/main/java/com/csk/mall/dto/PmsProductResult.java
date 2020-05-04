package com.csk.mall.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 查询单个产品进行修改时返回的结果
 * @author: caishengkai
 * @time: 2020/5/2 21:46
 */
@Getter
@Setter
public class PmsProductResult extends PmsProductParam {
    //商品所选分类的父id
    private Long cateParentId;
}
