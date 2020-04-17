package com.csk.mall.service;

import com.csk.mall.model.UmsAdmin;
import com.csk.mall.model.UmsPermission;

import java.util.List;

/**
 *  后台用户缓存业务接口
 */
public interface UmsAdminCacheService {

    /**
     * 获取缓存后台用户
     * @param username
     * @return
     */
    UmsAdmin getAdmin(String username);

    /**
     *  设置缓存后台用户
     * @param admin
     */
    void setAdmin(UmsAdmin admin);

    /**
     * 获取缓存后台用户权限
     * @param adminId
     * @return
     */
    List<UmsPermission> getPermissionList(long adminId);

    /**
     *  设置缓存后台用户权限
     * @param adminId
     * @param permissionList
     */
    void setPermissionList(long adminId, List<UmsPermission> permissionList);
}
