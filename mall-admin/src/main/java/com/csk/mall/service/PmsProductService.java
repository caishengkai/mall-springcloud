package com.csk.mall.service;

import com.csk.mall.dto.PmsProductParam;
import com.csk.mall.dto.PmsProductQueryParam;
import com.csk.mall.dto.PmsProductResult;
import com.csk.mall.model.PmsProduct;

import java.util.List;

/**
 * @description: 商品相关业务接口
 * @author: caishengkai
 * @time: 2020/5/2 15:05
 */
public interface PmsProductService {
    int create(PmsProductParam productParam);

    List<PmsProduct> list(PmsProductQueryParam productQueryParam, Integer pageSize, Integer pageNum);

    PmsProductResult getUpdateInfo(Long id);

    int update(Long id, PmsProductParam productParam);

    int updateDeleteStatus(List<Long> ids, Integer deleteStatus);
}
