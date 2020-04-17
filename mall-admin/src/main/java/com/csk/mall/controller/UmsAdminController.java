package com.csk.mall.controller;

import com.csk.mall.common.api.CommonResult;
import com.csk.mall.dto.UmsAdminLoginParam;
import com.csk.mall.dto.UmsAdminParam;
import com.csk.mall.model.UmsAdmin;
import com.csk.mall.service.UmsAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 后台用户管理
 * @author: caishengkai
 * @time: 2020/4/4 14:53
 */
@Api(tags = "UmsAdminController", description = "后台用户管理")
@RestController
@RequestMapping("/admin")
public class UmsAdminController {

    @Autowired
    private UmsAdminService adminService;

    @ApiOperation(value = "用户注册")
    @PostMapping("/regist")
    public CommonResult regist(@RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin admin = adminService.regist(umsAdminParam);
        if (admin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(admin);
    }

    @ApiOperation(value = "登录")
    @PostMapping("/login")
    public CommonResult login(@RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误！");
        }
        return CommonResult.success(token);
    }

    @ApiOperation(value = "查看用户详情")
    @GetMapping("/info")
    public CommonResult info(Principal principal) {
        if (principal == null) {
            return CommonResult.unauthorized();
        }
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        //data.put("roles", new String[]{"TEST"});
        //data.put("menus", roleService.getMenuList(umsAdmin.getId()));
        data.put("email", umsAdmin.getEmail());
        data.put("icon", umsAdmin.getIcon());
        return CommonResult.success(data);
    }

    /*@ApiOperation(value = "给用户分配角色")
    @GetMapping("/role/update")
    public CommonResult updateRole() {

    }*/
}
