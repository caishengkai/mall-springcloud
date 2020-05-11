package com.csk.mall.exception;

/**
 * @description: 断言处理类，用于抛出Mall自定义异常
 * @author: caishengkai
 * @time: 2020/5/9 15:43
 */
public class MallAsserts {
    public static void fail(String message) {
        throw new MallException(message);
    }
}
