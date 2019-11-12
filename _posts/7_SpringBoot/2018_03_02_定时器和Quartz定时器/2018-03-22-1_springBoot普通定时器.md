---
title: 1、SpringBoot普通定时器
date: 2018-03-22 18:33:00
tags: 
- Quartz
category: 
- Quartz
description: SpringBoot普通定时器
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

SpringBoot自带的Scheduled，

有两种定时任务执行方式：
	•	单线程（串行）
	•	多线程（并行）

有时候需要实现动态定时任务，即工程启动后，可以实现启动和关闭任务，同时也可以设置定时计划。这就需要利用到quartz，那么下一篇我将会开始介绍quartz

## 1、串行任务

### 1.1、sprinBoot启动开启定时器支持


```
@SpringBootApplication
@EnableScheduling
public class ComHljQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComHljQuartzApplication.class, args);
	}
}

```

### 1.2、准备好定时器开始，执行吧


```
package com.hlj.quartz.normalschedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 没有config 那就是串行，也就是一个线程，如果有了config中的配置，那么就是并行了
 * SpringBoot定时任务默认单线程，多线程需要自行实现或配置文件(sinosoft中一样)
 * @Author HealerJean
 * @Date 2018/3/22  下午2:52.
 */
@Component
public class MyTaskAnnotation {
    private static final Logger logger = LoggerFactory.getLogger(MyTaskAnnotation.class);

    /**
     * 定时计算。每天凌晨 01:00 执行一次
     */
    @Scheduled(cron = "0 0 1 * * *")
    public void show(){
        System.out.println("Annotation：is show run");
    }



    /**
     * 心跳更新。启动时执行一次，之后每隔1秒执行一次
     */
    @Scheduled(fixedRate = 1000*1)
    public void printOne(){
        Thread current = Thread.currentThread();
        System.out.println("定时任务1:"+current.getId());
        logger.info("心跳更新。启动时执行一次，之后每隔3秒执行一次 :"+current.getId()+ ",name:"+current.getName());
    }

    /**
     * 心跳更新。启动时执行一次，之后每隔3秒执行一次
     */
    @Scheduled(fixedRate = 1000*3)
    public void printTow(){
        Thread current = Thread.currentThread();
        System.out.println("定时任务2:"+current.getId());
        logger.info("心跳更新。启动时执行一次，之后每隔1秒执行一次 :"+current.getId()+ ",name:"+current.getName());
    }

}

```


### 1.3、项目启动，开始执行观察控制台

> 两个任务共同时候用的同一个线程，也就是这里的定时器任务是串行的，也就是说spirngBoot定时器任务默认是串行的


<br/>

![WX20180322-184806](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180322-184806.png)



## 2、并行任务，（将上面的串行变成并行）
> 1、其实做这个定时器的时候，想起给sinosoft的定时器任务了，代码虽然垃圾，但也算是实现了定时器的基本需求，
> 2、那里的spinng文件中配置的其实也可以用在这里，也是并行运行的。只不过这里sprignBoot就是为了省略配置文件。所以我下面用注解的方式实现


### 2.1、开启并行任务支持，进行任务执行器的配置

```
package com.hlj.quartz.normalschedule.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

/**
 * @Description SpringBoot 默认是串行任务，也就是使用的是一个线程。这里配置使得SpringBoot可以使用多线程
 * （和Sinosoft中配置文件中添加的配置一样哦）
 * @Author HealerJean
 * @Date 2018/3/22  下午3:01.
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer, AsyncConfigurer
{

    /*
     * 并行任务
     */
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        TaskScheduler taskScheduler = taskScheduler();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

    /**
     * 并行任务使用策略：多线程处理（配置线程数等）
     *
     * @return ThreadPoolTaskScheduler 线程池
     */
    @Bean(destroyMethod = "shutdown")
    public ThreadPoolTaskScheduler taskScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("task-");  //设置线程名开头
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        return scheduler;
    }

    /*
     * 异步任务
     */
    public Executor getAsyncExecutor()
    {
        Executor executor = taskScheduler();
        return executor;
    }

    /*
     * 异步任务 异常处理
     */
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
    {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}

```

### 2.2、项目启动，开始观察控制台 

发现了吧，定时器的线程改变了，变成多线程的样式，那么这里就代码是多线程喽。也就是实现并行了

![WX20180322-185336](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180322-185336.png)


## [代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_03_22_1_springBoot%E6%99%AE%E9%80%9A%E5%AE%9A%E6%97%B6%E5%99%A8/com-hlj-schedule.zip)

<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: '51FrwFLypWGMR0K3',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

