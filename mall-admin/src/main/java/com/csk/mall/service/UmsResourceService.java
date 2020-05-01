package com.csk.mall.service;

import com.csk.mall.model.UmsMenu;
import com.csk.mall.model.UmsResource;

import java.util.List;

/**
 * @description: 用户资源相关业务接口
 * @author: caishengkai
 * @time: 2020/4/25 21:33
 */
public interface UmsResourceService {
    List<UmsResource> listAll();

    int add(UmsResource resource);

    int update(Long id, UmsResource resource);

    UmsResource getItem(Long id);

    int delete(Long id);

    List<UmsResource> list(Integer pageSize, Integer pageNum);
}
