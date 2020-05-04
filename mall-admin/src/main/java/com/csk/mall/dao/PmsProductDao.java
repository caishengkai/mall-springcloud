package com.csk.mall.dao;

import com.csk.mall.dto.PmsProductResult;

/**
 * @description: 商品额外查询接口
 * @author: caishengkai
 * @time: 2020/5/2 22:04
 */
public interface PmsProductDao {
    PmsProductResult getUpdateInfo(Long id);
}
