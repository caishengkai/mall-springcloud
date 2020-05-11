package com.csk.mall.exception;

import com.csk.mall.common.api.CommonResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: mall全局异常处理类
 * @author: caishengkai
 * @time: 2020/5/9 15:44
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = MallException.class)
    public CommonResult handle(MallException e) {
        /*if (e.getErrorCode() != null) {
            return CommonResult.failed(e.getErrorCode());
        }*/
        return CommonResult.failed(e.getMessage());
    }
}
