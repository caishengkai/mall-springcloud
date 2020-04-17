package com.csk.mall.service.impl;

import com.csk.mall.common.service.RedisService;
import com.csk.mall.model.UmsAdmin;
import com.csk.mall.model.UmsPermission;
import com.csk.mall.service.UmsAdminCacheService;
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

    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.admin}")
    private String REDIS_KEY_ADMIN;
    @Value("${redis.key.permissionList}")
    private String REDIS_KEY_PERMISSIONLIST;

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
    public List<UmsPermission> getPermissionList(long adminId) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSIONLIST + ":" + adminId;
        return (List<UmsPermission>) redisService.get(key);
    }

    @Override
    public void setPermissionList(long adminId, List<UmsPermission> permissionList) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_PERMISSIONLIST + ":" + adminId;
        redisService.set(key, permissionList, REDIS_EXPIRE);
    }
}
