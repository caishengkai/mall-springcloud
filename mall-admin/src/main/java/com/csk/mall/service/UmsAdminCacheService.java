package com.csk.mall.service;

import com.csk.mall.model.UmsAdmin;
import com.csk.mall.model.UmsPermission;
import com.csk.mall.model.UmsResource;

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
     *  删除缓存后台用户
     * @param id
     */
    void delAdmin(Long id);

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

    /**
     * 获取缓存后台用户资源
     * @param adminId
     * @return
     */
    List<UmsResource> getResourceList(long adminId);

    /**
     *  设置缓存后台用户资源
     * @param resourceList
     */
    void setResourceList(long adminId, List<UmsResource> resourceList);

    /**
     * 删除缓存后台用户资源
     * @param id
     */
    void delResourceList(long id);
}
