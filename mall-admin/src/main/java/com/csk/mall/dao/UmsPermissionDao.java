package com.csk.mall.dao;

import com.csk.mall.model.UmsPermission;

import java.util.List;

/**
 * @description: 用户权限模块额外查询接口
 * @author: caishengkai
 * @time: 2020/4/26 17:04
 */
public interface UmsPermissionDao {
    /**
     * 获取用户所拥有的权限
     * @param adminId
     * @return
     */
    List<UmsPermission> getPermissionList(long adminId);
}
