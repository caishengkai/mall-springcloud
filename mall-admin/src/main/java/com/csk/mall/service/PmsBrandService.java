package com.csk.mall.service;

import com.csk.mall.dto.PmsBrandParam;
import com.csk.mall.model.PmsBrand;

import java.util.List;

/**
 * @description: 商品品牌相关接口
 * @author: caishengkai
 * @time: 2020/5/2 15:36
 */
public interface PmsBrandService {
    List<PmsBrand> listAll();

    int create(PmsBrandParam pmsBrand);

    int update(Long id, PmsBrandParam pmsBrandParam);

    int delete(Long id);

    List<PmsBrand> list(String keyword, Integer pageNum, Integer pageSize);

    PmsBrand getItem(Long id);

    int delete(List<Long> ids);

    int updateShowStatus(List<Long> ids, Integer showStatus);
}
