package com.csk.mall.service.impl;

import com.csk.mall.mapper.UmsRoleMapper;
import com.csk.mall.model.UmsRole;
import com.csk.mall.service.UmsRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description: 后台角色相关service实现类
 * @author: caishengkai
 * @time: 2020/4/6 20:52
 */
@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Autowired
    private UmsRoleMapper umsRoleMapper;

    @Override
    public int add(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        role.setStatus(1);
        return umsRoleMapper.insert(role);
    }
}
