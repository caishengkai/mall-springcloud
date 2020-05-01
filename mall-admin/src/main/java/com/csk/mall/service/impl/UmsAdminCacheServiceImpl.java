package com.csk.mall.service.impl;

import com.csk.mall.common.service.RedisService;
import com.csk.mall.model.UmsAdmin;
import com.csk.mall.model.UmsPermission;
import com.csk.mall.model.UmsResource;
import com.csk.mall.service.UmsAdminCacheService;
import com.csk.mall.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description: 后台用户缓存业务上实现类
 * @author: caishengkai
 * @time: 2020/4/17 10:04
 */
@Service
public class UmsAdminCacheServiceImpl implements UmsAdminCacheService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private UmsAdminService umsAdminService;

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.permissionList}")
    private String REDIS_KEY_PERMISSIONLIST;
    @Value("${redis.key.resourceList}")
    private String REDIS_KEY_RESOURCELIST;

    @Override
    public UmsAdmin getAdmin(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + username;
        return (UmsAdmin) redisService.get(key);
    }

    @Override
    public void setAdmin(UmsAdmin admin) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
        redisService.set(key, admin, REDIS_EXPIRE);
    }

    @Override
    public void delAdmin(Long id) {
        UmsAdmin admin = umsAdminService.getItem(id);
        if (admin != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_ADMIN + ":" + admin.getUsername();
            redisService.del(key);
        }
    }

    @Override
    public List<UmsPermission> getPermissionList(long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSIONLIST + ":" + adminId;
        return (List<UmsPermission>) redisService.get(key);
    }

    @Override
    public void setPermissionList(long adminId, List<UmsPermission> permissionList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSIONLIST + ":" + adminId;
        redisService.set(key, permissionList, REDIS_EXPIRE);
    }

    @Override
    public List<UmsResource> getResourceList(long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCELIST + ":" + adminId;
        return (List<UmsResource>) redisService.get(key);
    }

    @Override
    public void setResourceList(long adminId, List<UmsResource> resourceList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCELIST + ":" + adminId;
        redisService.set(key, resourceList, REDIS_EXPIRE);
    }

    @Override
    public void delResourceList(long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_RESOURCELIST + ":" + adminId;
        redisService.del(key);
    }
}
