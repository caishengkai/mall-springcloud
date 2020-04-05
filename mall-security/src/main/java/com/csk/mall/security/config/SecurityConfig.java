package com.csk.mall.security.config;

import com.csk.mall.security.component.JwtAuthenticationTokenFilter;
import com.csk.mall.security.component.RestfulAccessDeniedHandler;
import com.csk.mall.security.component.RestfulAuthEntryPoint;
import com.csk.mall.security.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @description: SpringSecurity的配置
 * @author: caishengkai
 * @time: 2020/4/5 9:58
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;
    @Autowired
    private RestfulAuthEntryPoint restfulAuthEntryPoint;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf() // 由于使用的是JWT，我们这里不需要csrf
                .disable()
                .sessionManagement() // 基于token，所以不需要session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET) // 允许对于网站静态资源的无授权访问
                .permitAll()
                .antMatchers("/admin/login", "/admin/regist") // 对登录注册要允许匿名访问
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS) //跨域请求会先进行一次options请求
                .permitAll()
                .anyRequest() // 除上面外的所有请求全部需要鉴权认证
                .authenticated();

        // 禁用缓存
        httpSecurity.headers().cacheControl();
        //在账号密码校验前添加JWT filter
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        httpSecurity.exceptionHandling()
                .accessDeniedHandler(restfulAccessDeniedHandler)
                .authenticationEntryPoint(restfulAuthEntryPoint);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }
}
