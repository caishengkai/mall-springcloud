package com.csk.mall.dao;

import com.csk.mall.model.UmsMenu;
import com.csk.mall.model.UmsResource;

import java.util.List;

/**
 * @description: 用户菜单模块额外查询接口
 * @author: caishengkai
 * @time: 2020/4/26 16:58
 */
public interface UmsMenuDao {
    /**
     * 获取用户可访问的菜单
     * @param adminId
     * @return
     */
    List<UmsMenu> getMenuList(Long adminId);

    /**
     * 获取角色下的菜单
     * @param roleId
     * @return
     */
    List<UmsMenu> getMenuListByRoleId(Long roleId);
}
