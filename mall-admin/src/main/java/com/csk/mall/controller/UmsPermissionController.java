package com.csk.mall.controller;

import com.csk.mall.common.api.CommonResult;
import com.csk.mall.model.UmsPermission;
import com.csk.mall.service.UmsPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 后台权限管理
 * @author: caishengkai
 * @time: 2020/4/6 20:51
 */
@Api(tags = "UmsPermissionController", description = "后台权限管理")
@RestController
@RequestMapping("/permission")
public class UmsPermissionController {

    @Autowired
    private UmsPermissionService permissionService;

    @ApiOperation("添加权限")
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('test2')")
    public CommonResult add(@RequestBody UmsPermission umsPermission) {
        int count = permissionService.add(umsPermission);
        if (count > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
