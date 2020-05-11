package com.csk.mall.exception;

/**
 * @description: mall自定义异常类
 * @author: caishengkai
 * @time: 2020/5/1 17:22
 */
public class MallException extends RuntimeException {

    public MallException(String message) {
        super(message);
    }
}
