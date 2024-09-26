package com.healerjean.proj.config;

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


    @Override
    public void afterSingletonsInstantiated() {
        sqlSessionFactory.getConfiguration().addInterceptor(new SqlLogInterceptor());
    }



}
