package com.csk.mall.service;

import com.csk.mall.model.UmsMenu;
import com.csk.mall.model.UmsResource;
import com.csk.mall.model.UmsRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UmsRoleService {
    int add(UmsRole role);

    List<UmsMenu> getMenuList(Long id);

    List<UmsRole> list();

    @Transactional
    void updateRole(Long adminId, List<Long> roleIds);

    List<UmsRole> list(String keyword, Integer pageSize, Integer pageNum);

    int update(Long id, UmsRole role);

    int delete(List<Long> roleIds);

    List<UmsMenu> listMenu(Long roleId);

    List<UmsResource> listResource(Long roleId);

    void allocMenu(Long roleId, List<Long> menuIds);

    void allocResource(Long roleId, List<Long> resourceIds);
}
