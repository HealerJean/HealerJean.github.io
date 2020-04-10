package com.healerjean.proj.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName MybatisPlusConfig
 * @date 2020/3/5  21:03.
 * @Description
 */
@Configuration
@MapperScan("com.healerjean.proj.data.mapper")
public class MybatisPlusConfig {



}
