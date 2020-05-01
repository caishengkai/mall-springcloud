package com.csk.mall.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.csk.mall.dao.*;
import com.csk.mall.mapper.UmsAdminRoleRelationMapper;
import com.csk.mall.mapper.UmsRoleMapper;
import com.csk.mall.mapper.UmsRoleMenuRelationMapper;
import com.csk.mall.mapper.UmsRoleResourceRelationMapper;
import com.csk.mall.model.*;
import com.csk.mall.service.UmsRoleService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @description: 后台角色相关service实现类
 * @author: caishengkai
 * @time: 2020/4/6 20:52
 */
@Service
public class UmsRoleServiceImpl implements UmsRoleService {

    @Autowired
    private UmsRoleMapper umsRoleMapper;
    @Autowired
    private UmsMenuDao umsMenuDao;
    @Autowired
    private UmsResourceDao umsResourceDao;
    @Autowired
    private UmsAdminRoleRelationMapper adminRoleRelationMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;
    @Autowired
    private UmsRoleMenuRelationMapper umsRoleMenuRelationMapper;
    @Autowired
    private UmsRoleResourceRelationMapper umsRoleResourceRelationMapper;
    @Autowired
    private UmsRoleMenuRelationDao umsRoleMenuRelationDao;
    @Autowired
    private UmsRoleResourceRelationDao umsRoleResourceRelationDao;

    @Override
    public int add(UmsRole role) {
        role.setCreateTime(new Date());
        role.setAdminCount(0);
        role.setSort(0);
        role.setStatus(1);
        return umsRoleMapper.insert(role);
    }

    @Override
    public List<UmsMenu> getMenuList(Long adminId) {
        return umsMenuDao.getMenuList(adminId);
    }

    @Override
    public List<UmsRole> list() {
        UmsRoleExample example = new UmsRoleExample();
        return umsRoleMapper.selectByExample(example);
    }

    @Override
    public void updateRole(Long adminId, List<Long> roleIds) {
        //先删除原有角色
        UmsAdminRoleRelationExample example = new UmsAdminRoleRelationExample();
        example.createCriteria().andAdminIdEqualTo(adminId);
        adminRoleRelationMapper.deleteByExample(example);
        //再重新赋值角色
        if (CollectionUtil.isNotEmpty(roleIds)) {
            List<UmsAdminRoleRelation> list = new ArrayList<>();
            for (Long roleId : roleIds) {
                UmsAdminRoleRelation adminRoleRelation = new UmsAdminRoleRelation();
                adminRoleRelation.setAdminId(adminId);
                adminRoleRelation.setRoleId(roleId);
                list.add(adminRoleRelation);
            }
            adminRoleRelationDao.insertList(list);
        }
    }

    @Override
    public List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsRoleExample example = new UmsRoleExample();
        if (StringUtils.hasText(keyword)) {
            example.createCriteria().andNameLike("%" + keyword + "%");
            example.or(example.createCriteria().andDescriptionLike("%" + keyword + "%"));
        }
        return umsRoleMapper.selectByExample(example);
    }

    @Override
    public int update(Long id, UmsRole role) {
        role.setId(id);
        return umsRoleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public int delete(List<Long> roleIds) {
        UmsRoleExample example = new UmsRoleExample();
        example.createCriteria().andIdIn(roleIds);
        return umsRoleMapper.deleteByExample(example);
    }

    @Override
    public List<UmsMenu> listMenu(Long roleId) {
        return umsMenuDao.getMenuListByRoleId(roleId);
    }

    @Override
    public List<UmsResource> listResource(Long roleId) {
        return umsResourceDao.getResourceListByRoleId(roleId);
    }

    @Override
    public void allocMenu(Long roleId, List<Long> menuIds) {
        //先删除原有关系
        UmsRoleMenuRelationExample example = new UmsRoleMenuRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleMenuRelationMapper.deleteByExample(example);
        //重新赋值
        List<UmsRoleMenuRelation> list = new ArrayList<>();
        for (Long menuId : menuIds) {
            UmsRoleMenuRelation relation = new UmsRoleMenuRelation();
            relation.setRoleId(roleId);
            relation.setMenuId(menuId);
            list.add(relation);
        }
        umsRoleMenuRelationDao.insertList(list);
    }

    @Override
    public void allocResource(Long roleId, List<Long> resourceIds) {
        //先删除原有关系
        UmsRoleResourceRelationExample example = new UmsRoleResourceRelationExample();
        example.createCriteria().andRoleIdEqualTo(roleId);
        umsRoleResourceRelationMapper.deleteByExample(example);
        //重新赋值
        List<UmsRoleResourceRelation> list = new ArrayList<>();
        for (Long resourceId : resourceIds) {
            UmsRoleResourceRelation relation = new UmsRoleResourceRelation();
            relation.setRoleId(roleId);
            relation.setResourceId(resourceId);
            list.add(relation);
        }
        umsRoleResourceRelationDao.insertList(list);
    }
}
