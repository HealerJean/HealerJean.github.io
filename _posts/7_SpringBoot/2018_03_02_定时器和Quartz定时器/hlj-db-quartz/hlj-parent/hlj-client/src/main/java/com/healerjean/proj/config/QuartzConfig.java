package com.healerjean.proj.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.healerjean.proj.schedule.listener.CustomSchedulerJobListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import javax.sql.DataSource;


/**
 * @author HealerJean
 * @ClassName QuartzConfig
 * @date 2020/5/15  15:30.
 * @Description
 */
@Configuration
public class QuartzConfig {

    @Value("${customQuartz.datasource.url}")
    private String dbUrl;
    @Value("${customQuartz.datasource.username}")
    private String username;
    @Value("${customQuartz.datasource.password}")
    private String password;

    @Bean
    public SpringBeanJobFactory jobFactory (){
        return new SpringBeanJobFactory();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean (SpringBeanJobFactory jobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(createCustomDB());
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactoryBean.setGlobalJobListeners(new CustomSchedulerJobListener());
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        return schedulerFactoryBean;
    }

    /**
     * 单独为定时任务创建一个单独的datasource
     */
    private DataSource createCustomDB(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(dbUrl);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setMaxActive(10);
        druidDataSource.setInitialSize(5);
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setMaxWait(2500);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(300000);
        return druidDataSource;
    }



}
