package com.csk.mall.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description: mybaits配置类
 * @author: caishengkai
 * @time: 2020/4/4 14:50
 */
@Configuration
@MapperScan("com.csk.mall.mapper")
public class MyBatisConfig {
}
