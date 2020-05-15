---
title: SpringBoot集成Quartz
date: 2018-03-22 21:33:00
tags: 
- Quartz
category: 
- Quartz
description: SpringBoot集成Quartz
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          




有时候需要实现动态定时任务，即工程启动后，可以实现启动和关闭任务，同时也可以设置定时计划。这就需要利用到quartz


别的先不多说，先利用配置文件制作一个简单的定时器器吧



# 1、SpringBoot集成Quartz

## 1.1、第一个简单的任务

### 1.1.1 、导入pom依赖

```xml
<!--quartz-->
<dependency>
    <groupId>org.quartz-scheduler</groupId>
    <artifactId>quartz-jobs</artifactId>
    <version>2.2.1</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```


### 1.1.2、quartz.properties


```properties
#配置线程池的容量，即表示同时最多可运行的线程数量
org.quartz.threadPool.threadCount = 20
org.quartz.scheduler.skipUpdateCheck = true
#scheduler实例名称。
org.quartz.scheduler.instanceName = HealerJeanQuartzScheduler
org.quartz.scheduler.jobFactory.class = org.quartz.simpl.SimpleJobFactory
org.quartz.threadPool.class = org.quartz.simpl.SimpleThreadPool
#job存储方式，RAMJobStore是使用JobStore最简单的一种方式，它也是性能最高效的，顾名思义，JobStore是把它的数据都存储在RAM中，
# 这也是它的快速和简单配置的原因；JDBCJobStore也是一种相当有名的JobStore，它通过JDBC把数据都保存到数据库中，
# 所以在配置上会比RAMJobStore复杂一些，而且不像RAMJobStore那么快，但是当我们对数据库中的表的主键创建索引时，性能上的缺点就不是很关键的了。
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore
```



### 1.1.3、`OneJob`：任务类

> 继承Job类，也就是工作任务类，然后重写里面的方法 `execute`


```java

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

@Slf4j
public class OneJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        log.info("quartz任务--------OneJob--------开始执行：执行事件：{}", new Date());
    }

}
```



### 1.1.4、启动定时器任务：`DemoService`

#### 1.1.1.4、`DemoService`

```java
public interface DemoService {

    void oneJob();
}

```



#### 1.1.1.5、`DemoServiceImpl`

```java

@Service
@Slf4j
public class DemoServiceImpl implements DemoService {

    /**
     * 第一个quartz Job任务
     */
    @Override
    public void oneJob() {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            log.info("调度器启动：scheduler.start");

            //具体任务.
            String jobDetailName = "oneJobName";
            String jobDetailGroup = "oneJobGroup";
            JobDetail jobDetail = JobBuilder.newJob(OneJob.class)
                .withIdentity(jobDetailName, jobDetailGroup)
                .build();
            log.info("任务详情：jobDetail：任务类：OneJob.class，任务名：{}，任务分组：{}", 
                     jobDetailName, jobDetailGroup);

            //触发时间点. (每5秒执行1次.)
            SimpleScheduleBuilder simpleScheduleBuilder = 
                SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(5).repeatForever();
            log.info("任务触发时间点：每5秒执行1次");

            String triggerName = "triggerName";
            String triggerGroup = "triggerGroup";
            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, triggerGroup)
                .startNow()
                .withSchedule(simpleScheduleBuilder)
                .build();
            log.info("任务触发器：触发器名称：{}，触发器分组：{}", triggerName, triggerName);

            // 交由Scheduler安排触发
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("任务详情 和 触发器 交由Scheduler安排触发");

            //睡眠20秒.等待任务完成
            TimeUnit.SECONDS.sleep(20);

            //关闭定时任务调度器.
            log.info("关闭定时器调度器");
            scheduler.shutdown();
            System.out.println("scheduler.shutdown");
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

```





### 1.1.5、`Controller`

```java
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "quartz定时器")
@RequestMapping("hlj/quartz")
@Slf4j
@RestController
public class QuartzController {


    @Autowired
    private DemoService demoService;

    @GetMapping("oneJob")
    public ResponseBean oneJob() {
        demoService.oneJob();
        return ResponseBean.buildSuccess();
    }
}

```



### 1.1.6、启动测试 

```http
http://127.0.0.1:8888/hlj/quartz/oneJob
```

**浏览器：**

```json
{
  "success": true,
  "result": "{}",
  "msg": "",
  "code": 200,
  "date": "1589515699682"
}
```

**服务器控制台：**

```
调度器启动：scheduler.start 
任务详情：jobDetail：任务类：OneJob.class，任务名：oneJobName，任务分组：oneJobGroup 
任务触发时间点：每5秒执行1次 
任务触发器：触发器名称：triggerName，触发器分组：triggerName 
任务详情 和 触发器 交由Scheduler安排触发
quartz任务--------OneJob--------开始执行：执行事件：2020-05-15T12:07:59.683+0800 
quartz任务--------OneJob--------开始执行：执行事件：2020-05-15 12:08:09.677+0800 
quartz任务--------OneJob--------开始执行：执行事件：2020-05-15T12:08:09.677+0800 
quartz任务--------OneJob--------开始执行：执行事件：2020-05-15T12:08:14.676+0800 
quartz任务--------OneJob--------开始执行：执行事件：2020-05-15T12:08:19.676+0800 
关闭定时器调度器 
```



# 2、集成方法

## 2.1、`QuartzConfig`

```java
@Configuration
public class QuartzConfig {


    @Bean
    public SpringBeanJobFactory jobFactory() {
        return new SpringBeanJobFactory();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(SpringBeanJobFactory simpleJobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        schedulerFactoryBean.setJobFactory(simpleJobFactory);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
        return schedulerFactoryBean;
    }

}
```



## 2.2、定时器服务层：`QuartzService`

```java
package com.healerjean.proj.service;


/**
 * @author HealerJean
 * @ClassName QuartzService
 * @date 2020/5/15  12:27.
 * @Description
 */
public interface QuartzService {

    /**
     * 开启定时器
     *
     * @param time         Cron 表达式
     * @param jobName      任务名称，
     * @param group        任务分组
     * @param jobClassName 任务类
     */
    void startJob(String time, String jobName, String group, String jobClassName);


    /**
     * 暂停任务
     *
     * @param name  任务名称
     * @param group 任务分组
     */
    void pauseJob(String name, String group);

    /**
     * 继续定时器任务：暂停中的任务
     * 注意：shutdown关闭了，或者删除了就不能重启了
     */
    void resumeJob(String name, String group);


    /**
     * 删除定时器任务
     */
    void deleteJob(String name, String group);


    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    void startAllJob();

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    void shutdown();
}

```



### 2.2.1、实现类：`QuartzServiceImpl`

```java
package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HealerJean
 * @ClassName QuartzServiceImpl
 * @date 2020/5/15  12:27.
 * @Description
 */
@Slf4j
@Service
public class QuartzServiceImpl implements QuartzService {

    @Resource
    private Scheduler scheduler;

    /**
     * 开启定时器
     *
     * @param time         Cron 表达式
     * @param name         任务名称，
     * @param group        任务分组
     * @param jobClassName 任务类
     */
    @Override
    public void startJob(String time, String name, String group, String jobClassName) {
        try {
            Class jobClass = Class.forName(jobClassName);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            log.info("quartz定时器--------启动任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停任务
     *
     * @param name  任务名称
     * @param group 任务分组
     */
    @Override
    public void pauseJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.pauseJob(jobKey);
            log.info("quartz定时器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------暂停任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 继续任务：暂停中的任务
     * 注意：shutdown关闭了，或者删除了就不能重启了
     */
    @Override
    public void resumeJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.resumeJob(jobKey);
            log.info("quartz定时器--------继续任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------继续任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 删除任务
     */
    @Override
    public void deleteJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return;
            }
            scheduler.deleteJob(jobKey);
            log.info("quartz定时器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------删除任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    @Override
    public void startAllJob() {
        try {
            scheduler.start();
            log.info("quartz定时器--------开启定时器}");
        } catch (SchedulerException e) {
            log.error("quartz定时器--------开启定时器失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    @Override
    public void shutdown() {
        try {
            scheduler.shutdown();
            log.info("quartz定时器--------关闭定时器}");
        } catch (SchedulerException e) {
            log.error("quartz定时器--------关闭定时器失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}

```



#### 2.2.2.1、`startJob`：开启任务

```java
@Override
public void startJob(String time, String name, String group, String jobClassName) {
    try {
        Class jobClass = Class.forName(jobClassName);
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
            .withIdentity(name, group).build();
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
            .withIdentity(name, group)
            .withSchedule(scheduleBuilder)
            .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
        log.info("quartz定时器--------启动任务--------任务名称：{}, 任务分组：{}", name, group);
    } catch (SchedulerException e) {
        log.error("quartz定时器--------启动任务失败", e);
        throw new RuntimeException(e.getMessage(), e);
    } catch (ClassNotFoundException e) {
        log.error("quartz定时器--------启动任务失败", e);
        throw new RuntimeException(e.getMessage(), e);
    }
}

```



#### 2.2.2.2、`pauseJob`：暂停任务

> **正在执行中的任务不受任何影响**

```java

@Override
public void pauseJob(String name, String group) {
    try {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.pauseJob(jobKey);
        log.info("quartz定时器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
    } catch (SchedulerException e) {
        log.error("quartz定时器--------暂停任务失败--------任务名称："
                  + name + ", 任务分组：" + group, e);
        throw new RuntimeException(e.getMessage(), e);
    }
}
```



#### 2.2.2.3、`resumeJob`：继续任务：暂停中的任务

```java

@Override
public void resumeJob(String name, String group) {
    try {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.resumeJob(jobKey);
        log.info("quartz定时器--------继续任务--------任务名称：{}, 任务分组：{}", name, group);
    } catch (SchedulerException e) {
        log.error("quartz定时器--------继续任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
        throw new RuntimeException(e.getMessage(), e);
    }
}
```



#### 2.2.2.4、`deleteJob`：删除任务

> 正在执行的不受任何影响  



```java
/**
     * 删除任务
     */
@Override
public void deleteJob(String name, String group) {
    try {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return;
        }
        scheduler.deleteJob(jobKey);
        log.info("quartz定时器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
    } catch (SchedulerException e) {
        log.error("quartz定时器--------删除任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
        throw new RuntimeException(e.getMessage(), e);
    }
}

```



#### 2.2.2.5、开启定时器：`scheduler.start()`

> 开启定时器，这时才可以开始所有的任务，默认是开启的;

```java

@Override
public void startAllJob() {
    try {
        scheduler.start();
        log.info("quartz定时器--------开启定时器}");
    } catch (SchedulerException e) {
        log.error("quartz定时器--------开启定时器失败", e);
        throw new RuntimeException(e.getMessage(), e);
    }
}
```



#### 2.2.2.5、关闭定时器 ：`scheduler.shutdown()`

>  关闭定时器，则所有任务不能执行和创建，从此也再不能开启定时器

```java

@Override
public void shutdown() {
    try {
        scheduler.shutdown();
        log.info("quartz定时器--------关闭定时器}");
    } catch (SchedulerException e) {
        log.error("quartz定时器--------关闭定时器失败", e);
        throw new RuntimeException(e.getMessage(), e);
    }
}
```



## 2.3、`QuartzController`

```java
package com.healerjean.proj.controller;

import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.scheduler.OneJob;
import com.healerjean.proj.service.DemoService;
import com.healerjean.proj.service.QuartzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  下午4:47.
 */
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "访问正常"),
        @ApiResponse(code = 301, message = "逻辑错误"),
        @ApiResponse(code = 500, message = "系统错误"),
        @ApiResponse(code = 401, message = "未认证"),
        @ApiResponse(code = 403, message = "禁止访问"),
        @ApiResponse(code = 404, message = "url错误")
})
@Api(description = "quartz控制器")
@RequestMapping("hlj/quartz")
@Slf4j
@RestController
public class QuartzController {


    @Autowired
    private DemoService demoService;
    @Autowired
    private QuartzService quartzService;

    @GetMapping("oneJob")
    public ResponseBean oneJob() {
        demoService.oneJob();
        return ResponseBean.buildSuccess();
    }



    @GetMapping("startJob")
    public ResponseBean startJob(String time, String name, String group, String className) {
        log.info("quartz控制器--------开启任务--------任务名称：{}, 任务分组：{}", name, group);
        quartzService.startJob(time, name, group, className);
        return ResponseBean.buildSuccess("已经开启任务");
    }

    @GetMapping("pauseJob")
    public ResponseBean pauseJob(String name, String group) {
        log.info("quartz控制器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
        quartzService.pauseJob(name, group);
        return ResponseBean.buildSuccess("暂停任务");
    }


    @GetMapping("resumeJob")
    public ResponseBean resumeJob(String name, String group) {
        log.info("quartz控制器--------暂停任务");
        quartzService.resumeJob(name, group);
        return ResponseBean.buildSuccess("暂停后继续任务");
    }

    @GetMapping("deleteJob")
    public ResponseBean deleteJob(String name, String group) {
        log.info("quartz控制器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
        quartzService.deleteJob(name, group);
        return ResponseBean.buildSuccess("删除任务");
    }


    @GetMapping("startAllJob")
    public ResponseBean startAllJob() {
        log.info("quartz控制器--------启动定时器");
        quartzService.startAllJob();
        return ResponseBean.buildSuccess("启动定时器");
    }

    @GetMapping("shutdown")
    public ResponseBean shutdown() {
        log.info("quartz控制器--------关闭定时器");
        quartzService.shutdown();
        return ResponseBean.buildSuccess("关闭定时器");
    }

}

```



## 2.4、启动测试

### 2.4.1、开启任务

```http
http://127.0.0.1:8888/hlj/quartz/startJob?className=com.healerjean.proj.scheduler.OneJob&group=group&name=name&time=0%2F1%20*%20*%20*%20*%20%3F
```

**接口返回：**

```json
{
  "success": true,
  "result": "已经开启任务",
  "msg": "",
  "code": 200,
  "date": "1589524122848"
}
```

**控制台：**

![image-20200515144852649](D:\study\HealerJean.github.io\blogImages\image-20200515144852649.png)



# 4、概念解析

## 4.1、`ScheduleBuilde`

### 4.1.1、`SimpleScheduleBuilder` 

> **只能指定触发的间隔时间和执行次数，需要触发器指定开始时间**；



```java
@Override
public void oneJob() {
    try {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();

        //具体任务.
        String jobDetailName = "oneJobName";
        String jobDetailGroup = "oneJobGroup";
        JobDetail jobDetail = JobBuilder.newJob(OneJob.class)
            .withIdentity(jobDetailName, jobDetailGroup).build();

        //触发时间点. (每5秒执行1次.)
        SimpleScheduleBuilder simpleScheduleBuilder = 
            SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(5)
            .repeatForever();

        String triggerName = "triggerName";
        String triggerGroup = "triggerGroup";
        Trigger trigger = TriggerBuilder.newTrigger()
            .withIdentity(triggerName, triggerGroup)
            .startNow()
            .withSchedule(simpleScheduleBuilder)
            .build();

        scheduler.scheduleJob(jobDetail, trigger);

        //睡眠20秒.等待任务完成
        TimeUnit.SECONDS.sleep(20);

        //关闭定时任务调度器.
        log.info("关闭定时器调度器");
        scheduler.shutdown();
        System.out.println("scheduler.shutdown");
    } catch (SchedulerException e) {
        e.printStackTrace();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }

}
```



### 4.1.2、`CronScheduleBuilder`

> CronScheduleBuilder是类似于Linux Cron的触发器，它通过一个称为CronExpression的规则来指定触发规则，通常是每次触发的具体时间；



```java
public void startJob(String time, String name, String group, String jobClassName) {
    try {
        Class jobClass = Class.forName(jobClassName);
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
            .withIdentity(name, group).build();
        
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
            .withIdentity(name, group)
            .withSchedule(scheduleBuilder)
            .build();
        
        scheduler.scheduleJob(jobDetail, cronTrigger);
        log.info("quartz定时器--------启动任务--------任务名称：{}, 任务分组：{}", name, group);
    } catch (SchedulerException e) {
        log.error("quartz定时器--------启动任务失败", e);
        throw new RuntimeException(e.getMessage(), e);
    } catch (ClassNotFoundException e) {
        log.error("quartz定时器--------启动任务失败", e);
        throw new RuntimeException(e.getMessage(), e);
    }
}
```





## 



![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean123.github.io`,
		owner: 'HealerJean123',
		admin: ['HealerJean123'],
		id: 'EwZh0PdvJ3mKaAPV',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

