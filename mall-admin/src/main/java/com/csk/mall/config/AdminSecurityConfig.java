package com.csk.mall.config;

import com.csk.mall.model.UmsResource;
import com.csk.mall.security.component.DynamicSecurityService;
import com.csk.mall.security.config.SecurityConfig;
import com.csk.mall.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 针对后台用户的springsecurity配置
 * @author: caishengkai
 * @time: 2020/4/5 11:11
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled =true)
public class AdminSecurityConfig extends SecurityConfig {

    @Autowired
    private UmsAdminService adminService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        //return username -> adminService.loadUserByUsername(username);
        return username -> adminService.loadUserByUsername2(username);
    }
}
