package com.csk.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.csk.mall.bo.AdminUserDetails;
import com.csk.mall.bo.AdminUserDetailsByPerm;
import com.csk.mall.dao.UmsAdminRoleRelationDao;
import com.csk.mall.dto.UmsAdminParam;
import com.csk.mall.mapper.UmsAdminMapper;
import com.csk.mall.model.UmsAdmin;
import com.csk.mall.model.UmsAdminExample;
import com.csk.mall.model.UmsPermission;
import com.csk.mall.model.UmsResource;
import com.csk.mall.security.util.JwtTokenUtil;
import com.csk.mall.service.UmsAdminCacheService;
import com.csk.mall.service.UmsAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @description: 后台用户模块Service实现类
 * @author: caishengkai
 * @time: 2020/4/5 11:15
 */
@Service
@Slf4j
public class UmsAdminServiceImpl implements UmsAdminService {

    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsAdminRoleRelationDao adminRoleRelationDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UmsAdminCacheService adminCacheService;

    /**
     * 根据username获取springsecurity需要的用户详情UserDetails
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsResource> resourceList = getResourceList(admin.getId());
            return new AdminUserDetails(admin, resourceList);
        }
        throw new UsernameNotFoundException("用户名或密码错误！");
    }

    /**
     * 根据username获取springsecurity需要的用户详情UserDetails,基于权限
     * @param username
     * @return
     */
    @Override
    public UserDetails loadUserByUsername2(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin != null) {
            List<UmsPermission> permissionList = getPermissionList(admin.getId());
            return new AdminUserDetailsByPerm(admin, permissionList);
        }
        throw new UsernameNotFoundException("用户名或密码错误！");
    }


    /**
     * 根据username获取UmsAdmin
     * @param username
     * @return
     */
    @Override
    public UmsAdmin getAdminByUsername(String username) {
        UmsAdmin admin = adminCacheService.getAdmin(username);
        if (admin != null) {
            return admin;
        }
        UmsAdminExample example = new UmsAdminExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<UmsAdmin> adminList = adminMapper.selectByExample(example);
        if (adminList != null && adminList.size() > 0) {
            admin = adminList.get(0);
            adminCacheService.setAdmin(admin);
            return admin;
        }
        return null;
    }

    /**
     * 根据adminId获取用户所有可访问资源
     * @param adminId
     * @return
     */
    private List<UmsResource> getResourceList(long adminId) {
        List<UmsResource> resourceList = adminRoleRelationDao.getResourceList(adminId);
        return resourceList;
    }

    /**
     * 根据adminId获取用户所有可访问资源 TODO
     * 目前只获取角色拥有的权限
     * @param adminId
     * @return
     */
    private List<UmsPermission> getPermissionList(long adminId) {
        List<UmsPermission> permissionList = adminCacheService.getPermissionList(adminId);
        if (CollUtil.isNotEmpty(permissionList)) {
            return permissionList;
        }
        permissionList = adminRoleRelationDao.getPermissionList(adminId);
        if (CollUtil.isNotEmpty(permissionList)) {
            adminCacheService.setPermissionList(adminId, permissionList);
        }
        return permissionList;
    }


    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername2(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确！");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            log.warn("登录异常：{}" + e.getMessage());
        }
        return token;
    }

    @Override
    public UmsAdmin regist(UmsAdminParam umsAdminParam) {
        //用户名重复直接返回
        UmsAdmin admin = getAdminByUsername(umsAdminParam.getUsername());
        if (admin != null) {
            return null;
        }
        admin = new UmsAdmin();
        BeanUtils.copyProperties(umsAdminParam, admin);
        String encodePassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodePassword);
        admin.setStatus(1);
        admin.setCreateTime(new Date());
        adminMapper.insert(admin);
        return admin;
    }
}
