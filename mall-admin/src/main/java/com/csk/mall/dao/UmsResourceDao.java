package com.csk.mall.dao;

import com.csk.mall.model.UmsResource;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UmsResourceDao {
    /**
     * 获取用户所有可访问的资源
     * @param adminId
     * @return
     */
    List<UmsResource> getResourceList(@Param("adminId") Long adminId);

    /**
     * 获取角色下的资源
     * @param roleId
     * @return
     */
    List<UmsResource> getResourceListByRoleId(Long roleId);
}
