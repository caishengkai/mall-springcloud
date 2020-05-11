package com.csk.mall.portal.dto;

import com.csk.mall.model.UmsMember;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

/**
 * @description: TODO springsecurity需要的用户详情，目前前台用户没有权限控制
 * @author: caishengkai
 * @time: 2020/5/9 15:49
 */
public class MemberDetails implements UserDetails {

    private UmsMember umsMember;
    public MemberDetails(UmsMember umsMember) {
        this.umsMember = umsMember;
    }

    /**
     * 获得用户所拥有的权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("TEST"));
    }

    @Override
    public String getPassword() {
        return umsMember.getPassword();
    }

    @Override
    public String getUsername() {
        return umsMember.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否禁用
     * @return
     */
    @Override
    public boolean isEnabled() {
        return umsMember.getStatus().equals("1");
    }

    public UmsMember getUmsMember() {
        return umsMember;
    }
}
