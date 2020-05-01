package com.csk.mall.controller;

import com.csk.mall.common.api.CommonPage;
import com.csk.mall.common.api.CommonResult;
import com.csk.mall.model.UmsResource;
import com.csk.mall.service.UmsResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: caishengkai
 * @time: 2020/4/28 15:37
 */
@Api(tags = "UmsResourceController", description = "后台资源管理")
@RestController
@RequestMapping("/resource")
public class UmsResourceController {

    @Autowired
    private UmsResourceService resourceService;

    @ApiOperation("添加资源")
    @PostMapping("/create")
    public CommonResult add(@RequestBody UmsResource resource) {
        int count = resourceService.add(resource);
        if (count > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @ApiOperation("更新指定资源")
    @PostMapping("/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody UmsResource umsResource) {
        int count = resourceService.update(id, umsResource);
        if (count > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @ApiOperation("根据ID获取资源详情")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsResource> getItem(@PathVariable Long id) {
        UmsResource resource = resourceService.getItem(id);
        return CommonResult.success(resource);
    }

    @ApiOperation("根据ID删除后台资源")
    @PostMapping(value = "/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        int count = resourceService.delete(id);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询资源列表分页")
    @GetMapping("/list")
    public CommonResult list(@RequestParam(defaultValue = "5") Integer pageSize,
                             @RequestParam(defaultValue = "1") Integer pageNum) {
        List<UmsResource> list = resourceService.list(pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("查询全部资源")
    @GetMapping("/listAll")
    public CommonResult listAll() {
        List<UmsResource> list = resourceService.listAll();
        return CommonResult.success(list);
    }
}
