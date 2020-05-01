package com.csk.mall.service.impl;

import cn.hutool.core.util.StrUtil;
import com.csk.mall.mapper.UmsResourceMapper;
import com.csk.mall.model.UmsMenu;
import com.csk.mall.model.UmsResource;
import com.csk.mall.model.UmsResourceExample;
import com.csk.mall.service.UmsResourceService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @description: 用户资源相关业务实现类
 * @author: caishengkai
 * @time: 2020/4/25 21:33
 */
@Service
public class UmsResourceServiceImpl implements UmsResourceService {

    @Autowired
    private UmsResourceMapper resourceMapper;

    @Override
    public List<UmsResource> listAll() {
        return resourceMapper.selectByExample(new UmsResourceExample());
    }

    @Override
    public int add(UmsResource resource) {
        resource.setCreateTime(new Date());
        return resourceMapper.insert(resource);
    }

    @Override
    public int update(Long id, UmsResource resource) {
        resource.setId(id);
        int count = resourceMapper.updateByPrimaryKeySelective(resource);
        return count;
    }

    @Override
    public UmsResource getItem(Long id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        return resourceMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<UmsResource> list(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        UmsResourceExample example = new UmsResourceExample();
        return resourceMapper.selectByExample(example);
    }
}
