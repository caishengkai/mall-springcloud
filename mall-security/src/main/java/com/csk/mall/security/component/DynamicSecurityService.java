package com.csk.mall.security.component;

import org.springframework.security.access.ConfigAttribute;

import java.util.Map;

/**
 * @description: 动态权限业务相关接口
 * @author: caishengkai
 * @time: 2020/4/15 15:34
 */
public interface DynamicSecurityService {

    /**
     * 加载资源ANT通配符和资源对应MAP（获取所有资源）
     */
    Map<String, ConfigAttribute> loadDataSource();

}
