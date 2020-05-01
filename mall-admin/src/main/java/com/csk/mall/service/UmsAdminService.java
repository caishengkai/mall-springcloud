package com.csk.mall.service;

import com.csk.mall.dto.UmsAdminParam;
import com.csk.mall.model.UmsAdmin;
import com.csk.mall.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UmsAdminService {

    /**
     * 获取springsecurity用户信息
     */
    UserDetails loadUserByUsername(String username);

    String login(String username, String password);

    UmsAdmin regist(UmsAdminParam umsAdminParam);

    UserDetails loadUserByUsername2(String username);

    UmsAdmin getAdminByUsername(String username);

    List<UmsAdmin> list(String keyword, Integer pageSize, Integer pageNum);

    List<UmsRole> getRoleList(Long adminId);

    int update(Long id, UmsAdmin admin);

    UmsAdmin getItem(Long id);

    int delete(Long id);
}
