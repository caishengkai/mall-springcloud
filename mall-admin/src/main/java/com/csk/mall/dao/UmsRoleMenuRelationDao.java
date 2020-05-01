package com.csk.mall.dao;

import com.csk.mall.model.UmsAdminRoleRelation;
import com.csk.mall.model.UmsRole;
import com.csk.mall.model.UmsRoleMenuRelation;

import java.util.List;

/**
 * @description: 角色菜单关系表额外查询接口
 * @author: caishengkai
 * @time: 2020/4/29 10:56
 */
public interface UmsRoleMenuRelationDao {
    void insertList(List<UmsRoleMenuRelation> list);
}
