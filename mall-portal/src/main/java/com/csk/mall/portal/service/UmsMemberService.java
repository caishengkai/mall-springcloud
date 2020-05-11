package com.csk.mall.portal.service;

import com.csk.mall.model.UmsMember;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @description:
 * @author: caishengkai
 * @time: 2020/5/9 15:30
 */
public interface UmsMemberService {

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    UmsMember getByUsername(String username);

    /**
     * 用户注册
     * @param username
     * @param password
     * @param telephone
     * @param authCode
     */
    void register(String username, String password, String telephone, String authCode);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

    /**
     * 获取当前登录用户
     * @return
     */
    UmsMember getCurrentMember();

    /**
     * 生成验证码
     * @param telephone
     * @return
     */
    String generateAuthCode(String telephone);

    /**
     * 根据用户名获取springsecurity UserDetails
     * @param username
     * @return
     */
    UserDetails loadUserByUsername(String username);
}
