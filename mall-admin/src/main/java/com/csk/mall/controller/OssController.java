package com.csk.mall.controller;

import com.csk.mall.common.api.CommonResult;
import com.csk.mall.service.OssService;
import com.csk.mall.dto.OssPolicyResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: Oss阿里云对象存储
 * @author: caishengkai
 * @time: 2020/5/3 16:16
 */
@Api(tags = "OssController", description = "Oss阿里云对象存储")
@RestController
@RequestMapping("/aliyun/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @ApiOperation(value = "oss上传签名生成")
    @GetMapping(value = "/policy")
    public CommonResult<OssPolicyResult> policy() {
        OssPolicyResult result = ossService.policy();
        return CommonResult.success(result);
    }

}
