package com.csk.mall.service;

import com.csk.mall.dto.UmsMenuNode;
import com.csk.mall.model.UmsMenu;

import java.util.List;

public interface UmsMenuService {
    int add(UmsMenu menu);

    int update(Long id, UmsMenu menu);

    UmsMenu getItem(Long id);

    int delete(Long id);

    List<UmsMenu> list(Long parentId, Integer pageSize, Integer pageNum);

    List<UmsMenuNode> treeList();

    int updateHidden(Long id, Integer hidden);
}
