package com.healerjean.proj.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MybatisPlusConfig
 *
 * @author HealerJean
 * @date 2020/3/5  21:03.
 */
@Configuration
@MapperScan("com.healerjean.proj.dao.mapper")
public class MybatisPlusConfig {

}
