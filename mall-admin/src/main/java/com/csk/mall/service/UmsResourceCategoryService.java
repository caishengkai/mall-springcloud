package com.csk.mall.service;

import com.csk.mall.model.UmsResourceCategory;

import java.util.List;

/**
 * @description: 资源分类模块业务相关接口
 * @author: caishengkai
 * @time: 2020/4/29 14:02
 */
public interface UmsResourceCategoryService {
    List<UmsResourceCategory> listAll();

    int create(UmsResourceCategory umsResourceCategory);

    int update(Long id, UmsResourceCategory umsResourceCategory);

    int delete(Long id);
}
