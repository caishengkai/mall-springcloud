package com.csk.mall.portal.service;

import com.csk.mall.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * @description:
 * @author: caishengkai
 * @time: 2020/5/9 16:03
 */
public interface UmsMemberReceiveAddressService {
    int add(UmsMemberReceiveAddress address);

    int delete(Long id);

    int update(Long id, UmsMemberReceiveAddress address);

    List<UmsMemberReceiveAddress> list();

    UmsMemberReceiveAddress getItem(Long id);
}
