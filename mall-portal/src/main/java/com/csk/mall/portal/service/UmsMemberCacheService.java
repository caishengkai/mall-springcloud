package com.csk.mall.portal.service;

import com.csk.mall.model.UmsMember;

/**
 * @description:
 * @author: caishengkai
 * @time: 2020/5/9 15:34
 */
public interface UmsMemberCacheService {

    /**
     * 缓存用户注册用到的验证码
     * @param telephone
     * @param authCode
     */
    void setAuthCode(String telephone, String authCode);

    /**
     * 从缓存中获取验证码
     * @param telephone
     * @return
     */
    String getAuthCode(String telephone);

    /**
     *  缓存登录用户
     * @param member
     */
    void setMember(UmsMember member);

    /**
     * 从缓存中获取登录用户
     * @param username
     * @return
     */
    UmsMember getMember(String username);
}
