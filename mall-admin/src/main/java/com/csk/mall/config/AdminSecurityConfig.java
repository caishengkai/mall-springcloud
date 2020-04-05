package com.csk.mall.config;

import com.csk.mall.security.config.SecurityConfig;
import com.csk.mall.service.UmsAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

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
        return username -> adminService.loadUserByUsername(username);
    }
}
