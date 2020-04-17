package com.csk.mall.controller;

import com.csk.mall.common.api.CommonResult;
import com.csk.mall.model.UmsRole;
import com.csk.mall.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 后台角色管理
 * @author: caishengkai
 * @time: 2020/4/6 20:51
 */
@Api(tags = "UmsRoleController", description = "后台角色管理")
@RestController
@RequestMapping("/role")
public class UmsRoleController {

    @Autowired
    private UmsRoleService roleService;

    @ApiOperation("添加角色")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('test')")
    public CommonResult add(@RequestBody UmsRole role) {
        int count = roleService.add(role);
        if (count > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
