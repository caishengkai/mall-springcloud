package com.csk.mall.controller;

import com.csk.mall.common.api.CommonResult;
import com.csk.mall.dto.UmsAdminLoginParam;
import com.csk.mall.dto.UmsAdminParam;
import com.csk.mall.model.UmsAdmin;
import com.csk.mall.service.UmsAdminService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 后台用户管理
 * @author: caishengkai
 * @time: 2020/4/4 14:53
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "UmsAdminController", description = "xxx")
public class UmsAdminController {

    @Autowired
    private UmsAdminService adminService;

    @RequestMapping("/regist")
    public CommonResult regist(@RequestBody UmsAdminParam umsAdminParam) {
        UmsAdmin admin = adminService.regist(umsAdminParam);
        if (admin == null) {
            return CommonResult.failed();
        }
        return CommonResult.success(admin);
    }

    @RequestMapping("/login")
    public CommonResult login(@RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            return CommonResult.validateFailed("用户名或密码错误！");
        }
        return CommonResult.success(token);
    }

    @RequestMapping("/info")
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
}
