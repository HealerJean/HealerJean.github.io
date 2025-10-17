package com.healerjean.proj.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.healerjean.proj.config.interceptor.SqlLogInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatisPlusInterceptor
 *
 * @author zhangyujin
 * @date 2023/6/15  11:54.
 */
@Slf4j
@MapperScan("com.healerjean.proj.data.mapper")
@Configuration
public class MybatisPlusConfiguration implements SmartInitializingSingleton {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * MyBatis支持
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        long t1 = System.currentTimeMillis();
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        long t2 = System.currentTimeMillis();
        log.info("MybatisPlusInterceptor injected! times:{}ms", t2 - t1);
        return interceptor;
    }

    /**
     * 注册 SQL 日志拦截器
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        // 通过 lambda 表达式配置 MyBatis 全局配置
        return configuration -> {
            // 添加我们自定义的 SQL 拦截器
            configuration.addInterceptor(new SqlLogInterceptor());
        };
    }

    @Override
    public void afterSingletonsInstantiated() {
    }
}
