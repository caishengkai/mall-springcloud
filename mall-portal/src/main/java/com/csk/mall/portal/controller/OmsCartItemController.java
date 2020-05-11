package com.csk.mall.portal.controller;

import com.csk.mall.common.api.CommonResult;
import com.csk.mall.model.OmsCartItem;
import com.csk.mall.portal.dto.CartProduct;
import com.csk.mall.portal.service.OmsCartItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 购物车模块
 * @author: caishengkai
 * @time: 2020/5/10 15:29
 */
@RestController
@Api(tags = "OmsCartItemController", description = "购物车模块")
@RequestMapping("/cart")
public class OmsCartItemController {

    @Autowired
    private OmsCartItemService cartItemService;

    @ApiOperation("添加商品到购物车")
    @PostMapping(value = "/add")
    public CommonResult add(@RequestBody OmsCartItem cartItem) {
        int count = cartItemService.add(cartItem);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取购物车列表")
    @GetMapping(value = "/list")
    public CommonResult<List<OmsCartItem>> list() {
        List<OmsCartItem> cartItemList = cartItemService.list();
        return CommonResult.success(cartItemList);
    }

    /*@ApiOperation("获取某个会员的购物车列表,包括促销信息")
    @GetMapping(value = "/list/promotion", method = RequestMethod.GET)
    public CommonResult<List<CartPromotionItem>> listPromotion() {
        List<CartPromotionItem> cartPromotionItemList = cartItemService.listPromotion(memberService.getCurrentMember().getId());
        return CommonResult.success(cartPromotionItemList);
    }*/

    @ApiOperation("修改购物车中某个商品的数量")
    @GetMapping(value = "/update/quantity")
    public CommonResult updateQuantity(@RequestParam Long id,
                                       @RequestParam Integer quantity) {
        int count = cartItemService.updateQuantity(id, quantity);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("获取购物车中某个商品的规格,用于重选规格")
    @GetMapping(value = "/getProduct/{productId}")
    public CommonResult<CartProduct> getCartProduct(@PathVariable Long productId) {
        CartProduct cartProduct = cartItemService.getCartProduct(productId);
        return CommonResult.success(cartProduct);
    }

    @ApiOperation("修改购物车中商品的规格")
    @PostMapping(value = "/update/attr")
    public CommonResult updateAttr(@RequestBody OmsCartItem cartItem) {
        int count = cartItemService.updateAttr(cartItem);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("删除购物车中的某个商品")
    @PostMapping(value = "/delete")
    public CommonResult delete(@RequestParam("ids") List<Long> ids) {
        int count = cartItemService.delete(ids);
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }

    @ApiOperation("清空购物车")
    @PostMapping(value = "/clear")
    public CommonResult clear() {
        int count = cartItemService.clear();
        if (count > 0) {
            return CommonResult.success(count);
        }
        return CommonResult.failed();
    }
}
