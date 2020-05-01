package com.csk.mall.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.csk.mall.bo.AdminUserDetails;
import com.csk.mall.bo.AdminUserDetailsByPerm;
import com.csk.mall.dao.UmsAdminRoleRelationDao;
import com.csk.mall.dao.UmsPermissionDao;
import com.csk.mall.dao.UmsResourceDao;
import com.csk.mall.dto.UmsAdminParam;
import com.csk.mall.mapper.UmsAdminLoginLogMapper;
import com.csk.mall.mapper.UmsAdminMapper;
import com.csk.mall.model.*;
import com.csk.mall.security.util.JwtTokenUtil;
import com.csk.mall.service.UmsAdminCacheService;
import com.csk.mall.service.UmsAdminService;
import com.github.pagehelper.PageHelper;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
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
    private UmsPermissionDao umsPermissionDao;
    @Autowired
    private UmsResourceDao umsResourceDao;
    @Autowired
    private UmsAdminRoleRelationDao umsAdminRoleRelationDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UmsAdminCacheService adminCacheService;
    @Autowired
    private UmsAdminLoginLogMapper adminLoginLogMapper;

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
        List<UmsResource> resourceList = adminCacheService.getResourceList(adminId);
        if (CollectionUtil.isEmpty(resourceList)) {
            resourceList = umsResourceDao.getResourceList(adminId);
            if (CollectionUtil.isNotEmpty(resourceList)) {
                adminCacheService.setResourceList(adminId, resourceList);
            }
        }
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
        permissionList = umsPermissionDao.getPermissionList(adminId);
        if (CollUtil.isNotEmpty(permissionList)) {
            adminCacheService.setPermissionList(adminId, permissionList);
        }
        return permissionList;
    }


    @Override
    public String login(String username, String password) {
        String token = null;
        try {
            UserDetails userDetails = loadUserByUsername(username);
            //UserDetails userDetails = loadUserByUsername2(username);
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确！");
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            token = jwtTokenUtil.generateToken(userDetails);
            //保存登录日志
            insertLoginLog(username);
        } catch (AuthenticationException e) {
            log.warn("登录异常：{}" + e.getMessage());
        }
        return token;
    }

    /**
     * 保存登录日志
     * @param username 用户名
     */
    private void insertLoginLog(String username) {
        UmsAdmin admin = getAdminByUsername(username);
        if (admin == null) {
            return;
        }
        UmsAdminLoginLog loginLog = new UmsAdminLoginLog();
        loginLog.setAdminId(admin.getId());
        loginLog.setCreateTime(new Date());
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        loginLog.setIp(request.getRemoteAddr());
        adminLoginLogMapper.insert(loginLog);
        //更新用户最后登录时间
        UmsAdmin updateAdmin = new UmsAdmin();
        updateAdmin.setId(admin.getId());
        updateAdmin.setLoginTime(new Date());
        adminMapper.updateByPrimaryKeySelective(updateAdmin);
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

    @Override
    public List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        UmsAdminExample example = new UmsAdminExample();
        if (StringUtils.hasText(keyword)) {
            example.createCriteria().andUsernameLike("%" + keyword + "%");
            example.or(example.createCriteria().andNickNameLike("%" + keyword + "%"));
        }
        return adminMapper.selectByExample(example);
    }

    @Override
    public List<UmsRole> getRoleList(Long adminId) {
        return umsAdminRoleRelationDao.getRoleList(adminId);
    }

    @Override
    public int update(Long id, UmsAdmin admin) {
        admin.setId(id);
        UmsAdmin rawAdmin = adminMapper.selectByPrimaryKey(id);
        if (StringUtils.isEmpty(admin.getPassword()) || rawAdmin.getPassword().equals(admin.getPassword())) {
            admin.setPassword(null);
        } else {
             admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        int count = adminMapper.updateByPrimaryKeySelective(admin);
        //修改用户信息后删除缓存
        adminCacheService.delAdmin(id);
        return count;
    }

    @Override
    public UmsAdmin getItem(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    @Override
    public int delete(Long id) {
        adminCacheService.delAdmin(id);
        int count = adminMapper.deleteByPrimaryKey(id);
        adminCacheService.delResourceList(id);
        return count;
    }
}
