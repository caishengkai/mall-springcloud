package com.csk.mall.controller;

import com.csk.mall.common.api.CommonPage;
import com.csk.mall.common.api.CommonResult;
import com.csk.mall.dto.PmsProductAttributeParam;
import com.csk.mall.dto.ProductAttrInfo;
import com.csk.mall.model.PmsProductAttribute;
import com.csk.mall.service.PmsProductAttributeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 商品属性模块
 * @author: caishengkai
 * @time: 2020/5/1 15:31
 */
@Api(tags = "PmsProductAttributeController", description = "商品属性模块")
@RestController
@RequestMapping("/productAttribute")
public class PmsProductAttributeController {

    @Autowired
    private PmsProductAttributeService productAttributeService;

    @ApiOperation(value = "根据商品属性分类获得商品属性列表")
    @GetMapping("/list/{cid}")
    public CommonResult list(@PathVariable Long cid,
                             @RequestParam Integer type,
                             @RequestParam(required = false) String keyword,
                             @RequestParam(defaultValue = "5") Integer pageSize,
                             @RequestParam(defaultValue = "1") Integer pageNum) {
        List<PmsProductAttribute> list = productAttributeService.list(cid, type, keyword, pageSize, pageNum);
        return CommonResult.success(CommonPage.restPage(list));
    }

    @ApiOperation("添加商品属性信息")
    @PostMapping(value = "/create")
    public CommonResult create(@RequestBody PmsProductAttributeParam productAttributeParam, BindingResult bindingResult) {
        int count = productAttributeService.create(productAttributeParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("修改商品属性信息")
    @PostMapping(value = "/update/{id}")
    public CommonResult update(@PathVariable Long id, @RequestBody PmsProductAttributeParam productAttributeParam, BindingResult bindingResult) {
        int count = productAttributeService.update(id, productAttributeParam);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("查询单个商品属性")
    @GetMapping(value = "/{id}")
    public CommonResult<PmsProductAttribute> getItem(@PathVariable Long id) {
        PmsProductAttribute productAttribute = productAttributeService.getItem(id);
        return CommonResult.success(productAttribute);
    }

    @ApiOperation("批量删除商品属性")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = productAttributeService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        } else {
            return CommonResult.failed();
        }
    }

    @ApiOperation("根据商品分类的id获取商品属性及属性分类")
    @GetMapping(value = "/attrInfo/{productCategoryId}")
    public CommonResult<List<ProductAttrInfo>> getAttrInfo(@PathVariable Long productCategoryId) {
        List<ProductAttrInfo> productAttrInfoList = productAttributeService.getProductAttrInfo(productCategoryId);
        return CommonResult.success(productAttrInfoList);
    }
}
