package com.csk.mall.controller;

import com.csk.mall.common.api.CommonPage;
import com.csk.mall.common.api.CommonResult;
import com.csk.mall.model.UmsAdmin;
import com.csk.mall.model.UmsMenu;
import com.csk.mall.model.UmsResource;
import com.csk.mall.model.UmsRole;
import com.csk.mall.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public CommonResult add(@RequestBody UmsRole role) {
        int count = roleService.add(role);
        if (count > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @ApiOperation("角色列表")
    @GetMapping("/listAll")
    public CommonResult listAll() {
        List<UmsRole> list = roleService.list();
        return CommonResult.success(list);
    }

    @ApiOperation("角色列表分页")
    @GetMapping("/list")
    public CommonResult list(@RequestParam(required = false) String keyword,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             @RequestParam(defaultValue = "1") Integer pageNum) {
        List<UmsRole> list = roleService.list(keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation(value = "修改指定角色信息")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsRole role) {
        int count = roleService.update(id, role);
        if (count > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @ApiOperation("批量删除用户信息")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam List<Long> roleIds) {
        int count = roleService.delete(roleIds);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("修改角色状态")
    @PostMapping(value = "/updateStatus/{id}")
    public CommonResult updateStatus(@PathVariable Long id, @RequestParam(value = "status") Integer status) {
        UmsRole umsRole = new UmsRole();
        umsRole.setStatus(status);
        int count = roleService.update(id, umsRole);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取角色相关菜单")
    @GetMapping(value = "/listMenu/{roleId}")
    public CommonResult<List<UmsMenu>> listMenu(@PathVariable Long roleId) {
        List<UmsMenu> roleList = roleService.listMenu(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("获取角色相关资源")
    @GetMapping(value = "/listResource/{roleId}")
    public CommonResult<List<UmsResource>> listResource(@PathVariable Long roleId) {
        List<UmsResource> roleList = roleService.listResource(roleId);
        return CommonResult.success(roleList);
    }

    @ApiOperation("给角色分配菜单")
    @PostMapping(value = "/allocMenu")
    @ResponseBody
    public CommonResult allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        roleService.allocMenu(roleId, menuIds);
        return CommonResult.success();
    }

    @ApiOperation("给角色分配资源")
    @PostMapping(value = "/allocResource")
    public CommonResult allocResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        roleService.allocResource(roleId, resourceIds);
        return CommonResult.success();
    }
}
