package com.csk.mall.portal.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: mybaits配置类
 * @author: caishengkai
 * @time: 2020/5/11 11:26
 */
@Configuration
@MapperScan({"com.csk.mall.mapper","com.csk.mall.portal.dao"})
public class MyBatisConfig {
}
