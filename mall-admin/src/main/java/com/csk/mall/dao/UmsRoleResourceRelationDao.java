package com.csk.mall.dao;

import com.csk.mall.model.UmsAdminRoleRelation;
import com.csk.mall.model.UmsRole;
import com.csk.mall.model.UmsRoleResourceRelation;

import java.util.List;

/**
 * @description: 角色资源关系表额外查询接口
 * @author: caishengkai
 * @time: 2020/4/29 10:56
 */
public interface UmsRoleResourceRelationDao {
    void insertList(List<UmsRoleResourceRelation> list);
}
