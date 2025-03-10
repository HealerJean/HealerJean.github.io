---
title: SpringBoot普通定时器
date: 2018-03-22 18:33:00
tags: 
- Quartz
category: 
- Quartz
description: SpringBoot普通定时器
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             



# 1、SpringBoot普通定时器  

> SpringBoot自带的Scheduled，  有两种定时任务执行方式：    
>
> 1、单线程（串行） 默认    
>
> 2、多线程（并行）      
>
> 
>
> 有时候需要实现动态定时任务，即工程启动后，可以实现启动和关闭任务，同时也可以设置定时计划。这就需要利用到quartz，那么下一篇我将会开始介绍quartz
>



## 1.1、串行任务

### 1.1.1、启动定时器支持


```java
@SpringBootApplication
@EnableScheduling
public class ComHljQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComHljQuartzApplication.class, args);
	}
}

```



### 1.1.2、定时器任务


```java
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



### 1.1.3、启动测试  



> 两个任务共同时候用的同一个线程，也就是这里的定时器任务是串行的，也就是说spirngBoot定时器任务默认是串行的



![WX20180322-184806](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180322-184806.png)





## 1.2、并行任务  



### 1.2.1、并行任务支持 



```java
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



### 1.2.2、启动测试  



>  **发现了吧，定时器的线程改变了，变成多线程的样式，那么这里就代码是多线程喽。也就是实现并行了**  



![WX20180322-185336](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180322-185336.png)







# 2、解释

## 2.1、名词解释 

### 2.1.1、`fixedDelay`

> `fixedDelay`控制方法执行的间隔时间，是以上一次方法执行完开始算起，如上一次方法执行阻塞住了，那么直到上一次执行完，并间隔给定的时间后，执行下一次。



### 2.1.2、`fixedRate`

> `fixedRate`是按照一定的速率执行，是从上一次方法执行开始的时间算起.。      
>
> 如果上一次方法阻塞住了，下一次也是不会执行，但是在阻塞这段时间内累计应该执行的次数，当不再阻塞时，一下子把这些全部执行掉，而后再按照固定速率继续执行。    



### 2.1.3、cron

> cron表达式可以定制化执行任务









![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)




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

