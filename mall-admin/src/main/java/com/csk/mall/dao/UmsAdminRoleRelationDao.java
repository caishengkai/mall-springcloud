package com.csk.mall.dao;

import com.csk.mall.model.UmsAdminRoleRelation;
import com.csk.mall.model.UmsRole;

import java.util.List;

/**
 * @description: 用户角色关系表额外查询接口
 * @author: caishengkai
 * @time: 2020/4/27 14:34
 */
public interface UmsAdminRoleRelationDao {
    /**
     * 获取用户所拥有的角色
     * @param adminId
     * @return
     */
    List<UmsRole> getRoleList(Long adminId);

    /**
     * 批量保存用户角色关系
     * @param list
     */
    void insertList(List<UmsAdminRoleRelation> list);
}
