package com.csk.mall.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: redis缓存切面，处理异常，防止redis服务器宕机影响正常功能
 * @author: caishengkai
 * @time: 2020/4/17 11:47
 */
@Slf4j
@Aspect
@Component
public class RedisCacheAspect {

    @Pointcut("execution(public * com.csk.mall.service.*CacheService.*(..))")
    public void cacheAspect() {
    }

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            log.error(throwable.getMessage());
        }
        return result;
    }
}
