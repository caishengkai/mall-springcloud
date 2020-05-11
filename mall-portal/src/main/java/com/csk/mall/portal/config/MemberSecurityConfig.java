package com.csk.mall.portal.config;

import com.csk.mall.portal.service.UmsMemberService;
import com.csk.mall.security.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @description: 针对前台用户的springsecurity配置
 * @author: caishengkai
 * @time: 2020/5/9 15:54
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled =true)
public class MemberSecurityConfig extends SecurityConfig {
    @Autowired
    private UmsMemberService memberService;

    @Bean
    public UserDetailsService userDetailsService() {
        //获取登录用户信息
        return username -> memberService.loadUserByUsername(username);
    }
}
