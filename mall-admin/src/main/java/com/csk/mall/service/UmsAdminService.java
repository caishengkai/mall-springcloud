package com.csk.mall.service;

import com.csk.mall.dto.UmsAdminParam;
import com.csk.mall.model.UmsAdmin;
import org.springframework.security.core.userdetails.UserDetails;

public interface UmsAdminService {

    /**
     * 获取springsecurity用户信息
     */
    UserDetails loadUserByUsername(String username);

    String login(String username, String password);

    UmsAdmin regist(UmsAdminParam umsAdminParam);

    UmsAdmin getAdminByUsername(String username);
}
