package com.csk.mall.service.impl;

import com.csk.mall.mapper.UmsPermissionMapper;
import com.csk.mall.model.UmsPermission;
import com.csk.mall.service.UmsPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @description: 后台权限相关service实现类
 * @author: caishengkai
 * @time: 2020/4/6 20:53
 */
@Service
public class UmsPermissionServiceImpl implements UmsPermissionService {

    @Autowired
    private UmsPermissionMapper umsPermissionMapper;

    @Override
    public int add(UmsPermission umsPermission) {
        umsPermission.setCreateTime(new Date());
        umsPermission.setSort(0);
        umsPermission.setStatus(1);
        return umsPermissionMapper.insert(umsPermission);
    }
}
