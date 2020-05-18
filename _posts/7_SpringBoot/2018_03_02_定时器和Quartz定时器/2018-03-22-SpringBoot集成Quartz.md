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


### 1.1.2、`quartz.properties`


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
package com.healerjean.proj.scheduler.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Date;

@Slf4j
@DisallowConcurrentExecution //禁止并发执行多个相同定义的JobDetail
public class OneJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
      log.info("quartz任务--------OneJob--------开始执行：执行事件：{}", new Date());
    }

}

```



### 1.1.4、启动定时器任务：`QuartzService`

#### 1.1.1.4、`QuartzService`

```java

public interface QuartzService {

    void oneJob();
}

```



#### 1.1.1.5、`QuartzServiceImpl`

```java

@Service
@Slf4j
public class QuartzServiceImpl implements QuartzService {

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
            JobDetail jobDetail = JobBuilder.newJob(OneJob.class).withIdentity(jobDetailName, jobDetailGroup).build();
            log.info("任务详情：jobDetail：任务类：OneJob.class，任务名：{}，任务分组：{}", jobDetailName, jobDetailGroup);

            //触发时间点. (每5秒执行1次.)
            SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
            log.info("任务触发时间点：每5秒执行1次");

            String triggerName = "triggerName";
            String triggerGroup = "triggerGroup";
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, triggerGroup).startNow().withSchedule(simpleScheduleBuilder).build();
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
    private QuartzService quartzService;
    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("oneJob")
    public ResponseBean oneJob() {
        quartzService.oneJob();
        return ResponseBean.buildSuccess();
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



## 2.2、定时器服务层：`SchedulerService`

```java
package com.healerjean.proj.scheduler.service;


import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.Set;

/**
 * @author HealerJean
 * @ClassName QuartzService
 * @date 2020/5/15  12:27.
 * @Description
 */
public interface SchedulerService {

    /**
     * 启动任务
     *
     * @param name      任务名称，
     * @param group     任务分组
     * @param className 任务类
     * @param cron      Cron 表达式
     */
    void startJob(String name, String group, String className, String cron,  String jobDesc);

    /**
     * 重置任务
     */
    void resetJob(String name, String group, String cron);

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


    /**
     * 获取所有的任务 的 JobKey
     */
    Set<JobKey> currentJobs();

    /**
     * 获取任务详情 JobDetail
     */
    JobDetail getJobDetail(String name, String group);

    /**
     * 获取触发器 Trigger
     */
    Trigger getJobTrigger(String name, String group);


    /**
     * 获取触发器 任务的执行状态
     */
    Trigger.TriggerState getTriggerState(String name, String group);

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



### 2.2.1、实现类：`SchedulerServiceImpl`

```java
package com.healerjean.proj.scheduler.service.impl;

import com.healerjean.proj.scheduler.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author HealerJean
 * @ClassName QuartzServiceImpl
 * @date 2020/5/15  12:27.
 * @Description
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Resource
    private Scheduler scheduler;

    /**
     * 启动任务
     *
     * @param name      任务名称，
     * @param group     任务分组
     * @param className 任务类
     * @param cron      Cron 表达式
     */
    @Override
    public void startJob(String name, String group, String className, String cron,  String jobDesc) {
        try {
            Class jobClass = Class.forName(className);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).withDescription(jobDesc).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            log.info("quartz定时器--------启动任务--------任务名称：{}, 任务分组：{}, 任务类：{}，corn表达式", name, group, className, cron);
        } catch (SchedulerException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("quartz定时器--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 重置任务
     */
    @Override
    public void resetJob(String name, String group, String cron) {
        try {
            JobKey jobKey = new JobKey(name, group);
            if (scheduler.checkExists(jobKey)) {
                TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("quartz定时器--------重置任务--------任务名称：{},任务分组：{}", name, group);
            }
            throw new RuntimeException("quartz定时器--------重置任务--------任务" + name + "不存在");
        } catch (SchedulerException e) {
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
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                log.info("quartz定时器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
            }
        } catch (SchedulerException e) {
            log.error("quartz定时器--------暂停任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停重启任务：暂停中的任务
     * 注意：shutdown关闭了，或者删除了就不能重启了
     */
    @Override
    public void resumeJob(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            if (scheduler.checkExists(jobKey)) {
                scheduler.resumeJob(jobKey);
                log.info("quartz定时器--------暂停重启任务--------任务名称：{}, 任务分组：{}", name, group);
            }
        } catch (SchedulerException e) {
            log.error("quartz定时器--------暂停重启任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
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
            if (scheduler.checkExists(jobKey)) {
                scheduler.deleteJob(jobKey);
                log.info("quartz定时器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
            }
        } catch (SchedulerException e) {
            log.error("quartz定时器--------删除任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 获取所有的任务 的 JobKey
     */
    @Override
    public Set<JobKey> currentJobs() {
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            return scheduler.getJobKeys(matcher);
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取任务详情 JobDetail
     */
    @Override
    public JobDetail getJobDetail(String name, String group) {
        try {
            JobKey jobKey = new JobKey(name, group);
            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取触发器 Trigger
     *
     * @return
     */
    @Override
    public Trigger getJobTrigger(String name, String group) {
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }


    /**
     * 获取触发器 任务的执行状态
     */
    @Override
    public Trigger.TriggerState getTriggerState(String name, String group) {
        try {
            TriggerKey triggerKey = new TriggerKey(name, group);
            return scheduler.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
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
/**
* 启动任务
*
* @param name      任务名称，
* @param group     任务分组
* @param className 任务类
* @param cron      Cron 表达式
*/
@Override
public void startJob(String name, String group, String className, String cron,  String jobDesc) {
   try {
       Class jobClass = Class.forName(className);
       JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name, group).withDescription(jobDesc).build();
       CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
       CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name, group).withSchedule(scheduleBuilder).build();
       scheduler.scheduleJob(jobDetail, cronTrigger);
       log.info("quartz定时器--------启动任务--------任务名称：{}, 任务分组：{}, 任务类：{}，corn表达式", name, group, className, cron);
   } catch (SchedulerException e) {
       log.error("quartz定时器--------启动任务失败", e);
       throw new RuntimeException(e.getMessage(), e);
   } catch (ClassNotFoundException e) {
       log.error("quartz定时器--------启动任务失败", e);
       throw new RuntimeException(e.getMessage(), e);
   }
}
```



#### 2.2.2.2、重置任务 


```java
/**
* 重置任务
*/
@Override
public void resetJob(String name, String group, String cron) {
   try {
       JobKey jobKey = new JobKey(name, group);
       if (scheduler.checkExists(jobKey)) {
           TriggerKey triggerKey = TriggerKey.triggerKey(name, group);
           CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
           CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
           trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
           //按新的trigger重新设置job执行
           scheduler.rescheduleJob(triggerKey, trigger);
           log.info("quartz定时器--------重置任务--------任务名称：{},任务分组：{}", name, group);
       }
       throw new RuntimeException("quartz定时器--------重置任务--------任务" + name + "不存在");
   } catch (SchedulerException e) {
       throw new RuntimeException(e.getMessage(), e);
   }
}
```


#### 2.2.2.3、`pauseJob`：暂停任务

> **正在执行中的任务不受任何影响**

```java


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
       if (scheduler.checkExists(jobKey)) {
           scheduler.pauseJob(jobKey);
           log.info("quartz定时器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
       }
   } catch (SchedulerException e) {
       log.error("quartz定时器--------暂停任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
       throw new RuntimeException(e.getMessage(), e);
   }
}
    
```



#### 2.2.2.4、`resumeJob`：继续任务：暂停中的任务

```java

/**
* 暂停重启任务：暂停中的任务
* 注意：shutdown关闭了，或者删除了就不能重启了
*/
@Override
public void resumeJob(String name, String group) {
   try {
       JobKey jobKey = new JobKey(name, group);
       if (scheduler.checkExists(jobKey)) {
           scheduler.resumeJob(jobKey);
           log.info("quartz定时器--------暂停重启任务--------任务名称：{}, 任务分组：{}", name, group);
       }
   } catch (SchedulerException e) {
       log.error("quartz定时器--------暂停重启任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
       throw new RuntimeException(e.getMessage(), e);
   }
}

```



#### 2.2.2.5、`deleteJob`：删除任务

> 正在执行的不受任何影响  



```java

/**
* 删除任务
*/
@Override
public void deleteJob(String name, String group) {
   try {
       JobKey jobKey = new JobKey(name, group);
       if (scheduler.checkExists(jobKey)) {
           scheduler.deleteJob(jobKey);
           log.info("quartz定时器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
       }
   } catch (SchedulerException e) {
       log.error("quartz定时器--------删除任务失败--------任务名称：" + name + ", 任务分组：" + group, e);
       throw new RuntimeException(e.getMessage(), e);
   }
}


```


#### 2.2.2.6、获取所有的任务


```java

/**
* 获取所有的任务 的 JobKey
*/
@Override
public Set<JobKey> currentJobs() {
   try {
       GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
       return scheduler.getJobKeys(matcher);
   } catch (SchedulerException e) {
       throw new RuntimeException(e.getMessage(), e);
   }
}


```


#### 2.2.2.7、获取任务详情


```java
/**
* 获取任务详情 JobDetail
*/
@Override
public JobDetail getJobDetail(String name, String group) {
   try {
       JobKey jobKey = new JobKey(name, group);
       return scheduler.getJobDetail(jobKey);
   } catch (SchedulerException e) {
       throw new RuntimeException();
   }
}

```

#### 2.2.2.8、获取触发器


```java
/**
* 获取触发器 Trigger
*
* @return
*/
@Override
public Trigger getJobTrigger(String name, String group) {
   try {
       TriggerKey triggerKey = new TriggerKey(name, group);
       return scheduler.getTrigger(triggerKey);
   } catch (SchedulerException e) {
       throw new RuntimeException();
   }
}

```


#### 2.2.2.9、获取触发器任务的执行状态


```java

/**
* 获取触发器 任务的执行状态
*/
@Override
public Trigger.TriggerState getTriggerState(String name, String group) {
   try {
       TriggerKey triggerKey = new TriggerKey(name, group);
       return scheduler.getTriggerState(triggerKey);
   } catch (SchedulerException e) {
       throw new RuntimeException();
   }
}
```



#### 2.2.2.10、开启定时器：`scheduler.start()`

> 开启定时器，这时才可以开始所有的任务，默认是开启的;

```java
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

```



#### 2.2.2.11、关闭定时器 ：`scheduler.shutdown()`

>  关闭定时器，则所有任务不能执行和创建，从此也再不能开启定时器

```java
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
```



## 2.3、`QuartzController`

```java

package com.healerjean.proj.controller;

import com.healerjean.proj.common.ResponseBean;
import com.healerjean.proj.dto.ScheduleJobDTO;
import com.healerjean.proj.scheduler.service.SchedulerService;
import com.healerjean.proj.service.QuartzService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
    private QuartzService quartzService;
    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("oneJob")
    public ResponseBean oneJob() {
        quartzService.oneJob();
        return ResponseBean.buildSuccess();
    }


    @GetMapping("startJob")
    public ResponseBean startJob(String name, String group, String className, String cron, String jobDesc) {
        log.info("quartz控制器--------启动任务--------任务名称：{}, 任务分组：{}, 任务类：{}，corn表达式", name, group, className, cron);
        schedulerService.startJob(name, group, className, cron, jobDesc);
        return ResponseBean.buildSuccess("已经开启任务");
    }

    @GetMapping("pauseJob")
    public ResponseBean pauseJob(String name, String group) {
        log.info("quartz控制器--------暂停任务--------任务名称：{}, 任务分组：{}", name, group);
        schedulerService.pauseJob(name, group);
        return ResponseBean.buildSuccess("暂停任务");
    }


    @GetMapping("resumeJob")
    public ResponseBean resumeJob(String name, String group) {
        log.info("quartz控制器--------暂停任务");
        schedulerService.resumeJob(name, group);
        return ResponseBean.buildSuccess("暂停后继续任务");
    }

    @GetMapping("deleteJob")
    public ResponseBean deleteJob(String name, String group) {
        log.info("quartz控制器--------删除任务--------任务名称：{}, 任务分组：{}", name, group);
        schedulerService.deleteJob(name, group);
        return ResponseBean.buildSuccess("删除任务");
    }

    @GetMapping("currentJobs")
    public ResponseBean currentJobs() {
        log.info("quartz控制器--------获取所有的任务");
        Set<JobKey> jobKeys = schedulerService.currentJobs();
        List<ScheduleJobDTO> jobList = new ArrayList<>();

        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = schedulerService.getJobDetail(jobKey.getName(), jobKey.getGroup());
            Trigger trigger = schedulerService.getJobTrigger(jobKey.getName(), jobKey.getGroup());
            Trigger.TriggerState triggerState = schedulerService.getTriggerState(jobKey.getName(), jobKey.getGroup());
            ScheduleJobDTO jobDTO = new ScheduleJobDTO();
            jobDTO.setJobName(jobKey.getName());
            jobDTO.setJobDesc(jobDetail.getDescription());
            jobDTO.setCron(((CronTrigger) trigger).getCronExpression());
            jobDTO.setJobClass(jobDetail.getJobClass().toString());
            jobDTO.setPreviousFireTime(trigger.getPreviousFireTime());
            jobDTO.setNextFireTime(trigger.getNextFireTime());
            jobDTO.setJobStatus(triggerState.name());
            jobList.add(jobDTO);
        }
        return ResponseBean.buildSuccess("获取所有的任务成功", jobList);
    }

    @GetMapping("getTriggerState")
    public ResponseBean getTriggerState(String name, String group) {
        log.info("quartz控制器--------任务的执行状态--------任务名称：{}, 任务分组：{}", name, group);
        return ResponseBean.buildSuccess("任务的执行状态", schedulerService.getTriggerState(name, group));
    }


    @GetMapping("startAllJob")
    public ResponseBean startAllJob() {
        log.info("quartz控制器--------启动定时器");
        schedulerService.startAllJob();
        return ResponseBean.buildSuccess("启动定时器");
    }

    @GetMapping("shutdown")
    public ResponseBean shutdown() {
        log.info("quartz控制器--------关闭定时器");
        schedulerService.shutdown();
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

![image-20200515144852649](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200515144852649.png)


# 3、Quartz接入数据库
> 打开网址 http://www.quartz-scheduler.org/downloads/

下载quartz，然后压缩之后打开里面的/docs/dbTables/ ，根据我们所使用的数据库进行选择，这里我用的是 mysql，所以选择了数据库表为 ables_mysql_innodb.sql


```sql
#
# In your Quartz properties file, you'll need to set
# org.quartz.jobStore.driverDelegateClass = org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#
#
# By: Ron Cordell - roncordell
#  I didn't see this anywhere, so I thought I'd post it here. This is the script from Quartz to create the tables in a MySQL database, modified to use INNODB instead of MYISAM.

DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    JOB_NAME          VARCHAR(200) NOT NULL,
    JOB_GROUP         VARCHAR(200) NOT NULL,
    DESCRIPTION       VARCHAR(250) NULL,
    JOB_CLASS_NAME    VARCHAR(250) NOT NULL,
    IS_DURABLE        VARCHAR(1)   NOT NULL,
    IS_NONCONCURRENT  VARCHAR(1)   NOT NULL,
    IS_UPDATE_DATA    VARCHAR(1)   NOT NULL,
    REQUESTS_RECOVERY VARCHAR(1)   NOT NULL,
    JOB_DATA          BLOB         NULL,
    PRIMARY KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_TRIGGERS
(
    SCHED_NAME     VARCHAR(120) NOT NULL,
    TRIGGER_NAME   VARCHAR(200) NOT NULL,
    TRIGGER_GROUP  VARCHAR(200) NOT NULL,
    JOB_NAME       VARCHAR(200) NOT NULL,
    JOB_GROUP      VARCHAR(200) NOT NULL,
    DESCRIPTION    VARCHAR(250) NULL,
    NEXT_FIRE_TIME BIGINT(13)   NULL,
    PREV_FIRE_TIME BIGINT(13)   NULL,
    PRIORITY       INTEGER      NULL,
    TRIGGER_STATE  VARCHAR(16)  NOT NULL,
    TRIGGER_TYPE   VARCHAR(8)   NOT NULL,
    START_TIME     BIGINT(13)   NOT NULL,
    END_TIME       BIGINT(13)   NULL,
    CALENDAR_NAME  VARCHAR(200) NULL,
    MISFIRE_INSTR  SMALLINT(2)  NULL,
    JOB_DATA       BLOB         NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, JOB_NAME, JOB_GROUP)
        REFERENCES QRTZ_JOB_DETAILS (SCHED_NAME, JOB_NAME, JOB_GROUP)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    REPEAT_COUNT    BIGINT(7)    NOT NULL,
    REPEAT_INTERVAL BIGINT(12)   NOT NULL,
    TIMES_TRIGGERED BIGINT(10)   NOT NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_CRON_TRIGGERS
(
    SCHED_NAME      VARCHAR(120) NOT NULL,
    TRIGGER_NAME    VARCHAR(200) NOT NULL,
    TRIGGER_GROUP   VARCHAR(200) NOT NULL,
    CRON_EXPRESSION VARCHAR(120) NOT NULL,
    TIME_ZONE_ID    VARCHAR(80),
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME    VARCHAR(120)   NOT NULL,
    TRIGGER_NAME  VARCHAR(200)   NOT NULL,
    TRIGGER_GROUP VARCHAR(200)   NOT NULL,
    STR_PROP_1    VARCHAR(512)   NULL,
    STR_PROP_2    VARCHAR(512)   NULL,
    STR_PROP_3    VARCHAR(512)   NULL,
    INT_PROP_1    INT            NULL,
    INT_PROP_2    INT            NULL,
    LONG_PROP_1   BIGINT         NULL,
    LONG_PROP_2   BIGINT         NULL,
    DEC_PROP_1    NUMERIC(13, 4) NULL,
    DEC_PROP_2    NUMERIC(13, 4) NULL,
    BOOL_PROP_1   VARCHAR(1)     NULL,
    BOOL_PROP_2   VARCHAR(1)     NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_BLOB_TRIGGERS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_NAME  VARCHAR(200) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    BLOB_DATA     BLOB         NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    INDEX (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_CALENDARS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    CALENDAR_NAME VARCHAR(200) NOT NULL,
    CALENDAR      BLOB         NOT NULL,
    PRIMARY KEY (SCHED_NAME, CALENDAR_NAME)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS
(
    SCHED_NAME    VARCHAR(120) NOT NULL,
    TRIGGER_GROUP VARCHAR(200) NOT NULL,
    PRIMARY KEY (SCHED_NAME, TRIGGER_GROUP)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_FIRED_TRIGGERS
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    ENTRY_ID          VARCHAR(95)  NOT NULL,
    TRIGGER_NAME      VARCHAR(200) NOT NULL,
    TRIGGER_GROUP     VARCHAR(200) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    FIRED_TIME        BIGINT(13)   NOT NULL,
    SCHED_TIME        BIGINT(13)   NOT NULL,
    PRIORITY          INTEGER      NOT NULL,
    STATE             VARCHAR(16)  NOT NULL,
    JOB_NAME          VARCHAR(200) NULL,
    JOB_GROUP         VARCHAR(200) NULL,
    IS_NONCONCURRENT  VARCHAR(1)   NULL,
    REQUESTS_RECOVERY VARCHAR(1)   NULL,
    PRIMARY KEY (SCHED_NAME, ENTRY_ID)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_SCHEDULER_STATE
(
    SCHED_NAME        VARCHAR(120) NOT NULL,
    INSTANCE_NAME     VARCHAR(200) NOT NULL,
    LAST_CHECKIN_TIME BIGINT(13)   NOT NULL,
    CHECKIN_INTERVAL  BIGINT(13)   NOT NULL,
    PRIMARY KEY (SCHED_NAME, INSTANCE_NAME)
)
    ENGINE = InnoDB;

CREATE TABLE QRTZ_LOCKS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    LOCK_NAME  VARCHAR(40)  NOT NULL,
    PRIMARY KEY (SCHED_NAME, LOCK_NAME)
)
    ENGINE = InnoDB;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS (SCHED_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS (SCHED_NAME, JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS (SCHED_NAME, CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_GROUP, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS (SCHED_NAME, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS (SCHED_NAME, TRIGGER_STATE, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS (SCHED_NAME, MISFIRE_INSTR, NEXT_FIRE_TIME, TRIGGER_GROUP,
                                                             TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, INSTANCE_NAME, REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_NAME, TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS (SCHED_NAME, TRIGGER_GROUP);

commit;

```



## 3.1、`quartz.properties`


```properties

#默认或是自己改名字都行
org.quartz.scheduler.instanceName=CustomQuartzScheduler
#如果使用集群，instanceId必须唯一，设置成AUTO
org.quartz.scheduler.instanceId=AUTO
org.quartz.scheduler.jobFactory.class=org.quartz.simpl.SimpleJobFactory
org.quartz.scheduler.autoStartup=true
org.quartz.scheduler.skipUpdateCheck=true

# 配置现场池
org.quartz.threadPool.class=org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount=30
org.quartz.threadPool.threadPriority=5


#存储方式使用JobStoreTX，也就是数据库
#存储的JobDataMaps是否都为String类型
org.quartz.jobStore.useProperties=true
#是否使用集群（如果项目只部署到 一台服务器，就不用了）
org.quartz.jobStore.isClustered=true
org.quartz.jobStore.clusterCheckinInterval=20000
#存储方式使用JobStoreTX，也就是数据库
org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#misfireThreshold是用来设置调度引擎对触发器超时的忍耐时间，简单来说 假设misfireThreshold=6000(单位毫秒)。
#那么它的意思说当一个触发器超时时间如果大于misfireThreshold的值 就认为这个触发器真正的超时(也叫Misfires)。
org.quartz.jobStore.misfireThreshold=60000
#数据库中quartz表的表名前缀
org.quartz.jobStore.tablePrefix = QRTZ_
org.quartz.jobStore.dataSource = customQuartz

```

## 3.2、`application.properties`


```properties

#datasource healerjean 这个我们自己配置
customQuartz.datasource.url=jdbc:mysql://localhost:3306/hlj_quartz?characterEncoding=utf-8
customQuartz.datasource.username=root
customQuartz.datasource.password=123456
customQuartz.datasource.driver-class-name=com.mysql.jdbc.Driver

```


## 3.3、`QuartzConfig`


```java
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

```

## 3.4、任务类：`Job` 

### 3.4.1、`PrintTaskJob`


```java
package com.healerjean.proj.schedule.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

/**
 * @author HealerJean
 * @ClassName PrintTaskJob
 * @date 2020/5/15  17:27.
 * @Description
 */
@Slf4j
@Component
@DisallowConcurrentExecution //禁止并发执行多个相同定义的JobDetail
public class PrintTaskJob implements Job {

    @Override
    public void execute(JobExecutionContext context) {
        try {
            CronTrigger trigger = (CronTrigger) context.getTrigger();
            String corn = trigger.getCronExpression();
            String jobName = trigger.getKey().getName();
            String jobGroup = trigger.getKey().getGroup();
            log.info("定时器任务开始执行--------【PrintTaskJob】 ,任务corn：{}, 任务名称：{}， 任务组：{}", corn, jobName, jobGroup);
        } catch (Exception e) {
            log.error("定时器任务--------【PrintTaskJob】 任务执行失败");
        }
    }
}

```



## 3.5、`SchedulerService`：定时器任务操作接口


```java
package com.healerjean.proj.schedule.service;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.Set;

/**
 * @author HealerJean
 * @ClassName SchedulerService
 * @date 2020/5/15  17:40.
 * @Description
 */
public interface SchedulerService {

    /**
     * 启动任务
     *
     * @param cron      Cron 表达式
     * @param name      任务名称
     * @param className 任务类
     */
    void startJob( String name, String className, String cron,String jobDesc);


    /**
     * 重置任务
     *
     * @param name 任务名称
     * @param cron Cron 表达式
     */
    void resetJob(String name, String cron);


    /**
     * 暂停任务
     */
    void pauseJob(String name);


    /**
     * 删除任务
     */
    void deleteJob(String name);


    /**
     * 暂停重启
     */
    void resumeJob(String name);


    /**
     * 获取所有的任务 的 JobKey
     */
    Set<JobKey> currentJobs();


    /**
     * 获取任务详情 JobDetail
     */
    JobDetail getJobDetail(String name);

    /**
     * 获取触发器 Trigger
     */
    Trigger getJobTrigger(String name);


    /**
     * 获取触发器 任务的执行状态
     */
    Trigger.TriggerState getTriggerState(String name);

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


### 3.5.1、`SchedulerServiceImpl`


```java


package com.healerjean.proj.schedule.service.impl;

import com.healerjean.proj.schedule.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author HealerJean
 * @ClassName SchedulerServiceImpl
 * @date 2020/5/15  17:41.
 * @Description
 */
@Slf4j
@Service
public class SchedulerServiceImpl implements SchedulerService {


    @Resource
    private Scheduler scheduler;


    /**
     * 启动任务
     *
     * @param cron      Cron 表达式
     * @param name      任务名称
     * @param className 任务类
     * @param jobDesc   任务描述
     */
    @Override
    public void startJob( String name, String className, String cron,String jobDesc) {
        try {
            Class jobClass = Class.forName(className);
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(name).withDescription(jobDesc).build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(name).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, cronTrigger);
            log.info("定时器服务--------启动任务--------任务名称：{}, 任务类：{}，corn表达式", name, className, cron);
        } catch (SchedulerException e) {
            log.error("定时器服务--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            log.error("定时器服务--------启动任务失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 重置任务
     *
     * @param name 任务名称
     * @param cron Cron 表达式
     */
    @Override
    public void resetJob(String name, String cron) {
        try {
            JobKey jobKey = new JobKey(name);
            if (scheduler.checkExists(jobKey)) {
                TriggerKey triggerKey = TriggerKey.triggerKey(name);
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
                trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
                //按新的trigger重新设置job执行
                scheduler.rescheduleJob(triggerKey, trigger);
                log.info("定时器服务--------重置任务--------任务名称：{}", name);
            }
            throw new RuntimeException("定时器服务--------重置任务--------任务" + name + "不存在");
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停任务
     *
     * @param name 任务名称
     */
    @Override
    public void pauseJob(String name) {
        try {
            JobKey jobKey = new JobKey(name);
            if (scheduler.checkExists(jobKey)) {
                scheduler.pauseJob(jobKey);
                log.info("定时器服务--------暂停任务--------任务名称：{}", name);
            }
        } catch (SchedulerException e) {
            log.error("定时器服务--------暂停任务失败--------任务名称：" + name + "", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 删除任务
     */
    @Override
    public void deleteJob(String name) {
        try {
            if (scheduler.checkExists(new JobKey(name))) {
                scheduler.deleteJob(new JobKey(name));
                log.info("定时器服务--------删除任务--------任务名称：{}", name);
            }
        } catch (SchedulerException e) {
            log.error("定时器服务--------删除任务失败--------任务名称：" + name + "", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 暂停重启
     */
    @Override
    public void resumeJob(String name) {
        try {
            if (scheduler.checkExists(new JobKey(name))) {
                scheduler.resumeJob(new JobKey(name));
                log.info("定时器服务--------暂停重启任务--------任务名称：{}", name);
            }
        } catch (SchedulerException e) {
            log.error("定时器服务--------暂停重启任务失败--------任务名称：" + name + "", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 获取所有的任务 的 JobKey
     */
    @Override
    public Set<JobKey> currentJobs() {
        try {
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            return scheduler.getJobKeys(matcher);
        } catch (SchedulerException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 获取任务详情 JobDetail
     */
    @Override
    public JobDetail getJobDetail(String name) {
        try {
            JobKey jobKey = new JobKey(name);
            return scheduler.getJobDetail(jobKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取触发器 Trigger
     *
     * @return
     */
    @Override
    public Trigger getJobTrigger(String name) {
        try {
            TriggerKey triggerKey = new TriggerKey(name);
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 获取触发器 任务的执行状态
     */
    @Override
    public Trigger.TriggerState getTriggerState(String name ) {
        try {
            TriggerKey triggerKey = new TriggerKey(name);
            return scheduler.getTriggerState(triggerKey);
        } catch (SchedulerException e) {
            throw new RuntimeException();
        }
    }


    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    @Override
    public void startAllJob() {
        try {
            scheduler.start();
            log.info("定时器服务--------开启定时器}");
        } catch (SchedulerException e) {
            log.error("定时器服务--------开启定时器失败", e);
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
            log.info("定时器服务--------关闭定时器}");
        } catch (SchedulerException e) {
            log.error("定时器服务--------关闭定时器失败", e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }


}

```



## 3.6、定时器监听：`CustomSchedulerJobListener`


```java
package com.healerjean.proj.schedule.listener;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * 类描述：
 * 创建人： HealerJean
 */
@Slf4j
public class CustomSchedulerJobListener implements JobListener {

    private static final String LISTENER_NAME = "healerjean.job.listener";

    @Override
    public String getName() {
        return LISTENER_NAME;
    }


    /**
     * 任务执行之前执行
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        String name = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("定时器监听，任务：【{} 准备开始执行", name);
    }

    /**
     * 任务取消
     * 这个方法正常情况下不执行,但是如果当 TriggerListener中的 vetoJobExecution 方法返回true时, 那么执行这个方法.
     * 注意： 如果方法(2)执行 那么(1),(3)这个俩个方法不会执行,因为任务被终止了嘛.
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        String name = jobExecutionContext.getJobDetail().getKey().getName();
        log.info("定时器监听，任务：【{} 取消", name);
    }


    /**
     * 任务执行完成后执行,jobException如果它不为空则说明任务在执行过程中出现了异常
     */
    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        String name = jobExecutionContext.getJobDetail().getKey().getName();
        if (e != null) {
            log.error("定时器监听，任务：【{"+name+"}】 执行失败", e);
        } else {
            log.info("定时器监听，任务：【{}】执行成功", name);

        }
    }
}

```

## 3.7、`QuartzController`


```java
package com.healerjean.proj.controller;

import com.healerjean.proj.common.dto.ResponseBean;
import com.healerjean.proj.dto.ScheduleJobDTO;
import com.healerjean.proj.schedule.service.SchedulerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


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
    private SchedulerService schedulerService;



    @GetMapping("startJob")
    public ResponseBean startJob( String name, String className, String cron, String jobDesc) {
        log.info("quartz控制器--------启动任务--------任务名称：{}, 任务类：{}，corn表达式", name, className, cron);
        schedulerService.startJob(name, className,cron, jobDesc);
        return ResponseBean.buildSuccess("已经开启任务");
    }

    @GetMapping("pauseJob")
    public ResponseBean pauseJob(String name ) {
        log.info("quartz控制器--------暂停任务--------任务名称：{}", name);
        schedulerService.pauseJob(name);
        return ResponseBean.buildSuccess("暂停任务");
    }


    @GetMapping("resumeJob")
    public ResponseBean resumeJob(String name ) {
        log.info("quartz控制器--------暂停任务");
        schedulerService.resumeJob(name);
        return ResponseBean.buildSuccess("暂停后继续任务");
    }

    @GetMapping("deleteJob")
    public ResponseBean deleteJob(String name) {
        log.info("quartz控制器--------删除任务--------任务名称：{}", name);
        schedulerService.deleteJob(name);
        return ResponseBean.buildSuccess("删除任务");
    }

    @GetMapping("currentJobs")
    public ResponseBean currentJobs() {
        log.info("quartz控制器--------获取所有的任务");
        Set<JobKey> jobKeys = schedulerService.currentJobs();
        List<ScheduleJobDTO> jobList = new ArrayList<>();

        for (JobKey jobKey : jobKeys){
            JobDetail jobDetail = schedulerService.getJobDetail(jobKey.getName());
            Trigger trigger = schedulerService.getJobTrigger(jobKey.getName());
            Trigger.TriggerState triggerState = schedulerService.getTriggerState(jobKey.getName());
            ScheduleJobDTO jobDTO = new ScheduleJobDTO();
            jobDTO.setJobName(jobKey.getName());
            jobDTO.setJobDesc(jobDetail.getDescription());
            jobDTO.setCron(((CronTrigger)trigger).getCronExpression());
            jobDTO.setJobClass(jobDetail.getJobClass().toString());
            jobDTO.setPreviousFireTime(trigger.getPreviousFireTime());
            jobDTO.setNextFireTime(trigger.getNextFireTime());
            jobDTO.setJobStatus(triggerState.name());
            jobList.add(jobDTO);
        }
        return ResponseBean.buildSuccess("获取所有的任务成功", jobList);
    }


    @GetMapping("getTriggerState")
    public ResponseBean getTriggerState(String name) {
        log.info("quartz控制器--------任务的执行状态--------任务名称：{}", name);
        return ResponseBean.buildSuccess("任务的执行状态",schedulerService.getTriggerState(name));
    }


    @GetMapping("startAllJob")
    public ResponseBean startAllJob() {
        log.info("quartz控制器--------启动定时器");
        schedulerService.startAllJob();
        return ResponseBean.buildSuccess("启动定时器");
    }

    @GetMapping("shutdown")
    public ResponseBean shutdown() {
        log.info("quartz控制器--------关闭定时器");
        schedulerService.shutdown();
        return ResponseBean.buildSuccess("关闭定时器");
    }

}

```




# 4、概念解析

## 4.1、name，group

> 关于这两个参数，使用到的地方有jobKey 和 triggerKey,如下介绍的是jobKey，其实triggerKey 同理


```java

// 创建jobDetail实例，绑定Job实现类
// 指明job的名称，所在组的名称，以及绑定job类
JobDetail jobDetail = JobBuilder.newJob(job)
                .withIdentity(jobName, group).//设置Job的名字和组
                .withDescription(jobDesc) //设置描述，描述这个任务是干什么的
                .build();

JobDetail job = JobBuilder.newJob(t)
                .withIdentity(jobName) ////设置Job名字，因为这个是将来唯一识别的，所以我这里变量取名为jobId，组是默认的，观察它的源码，我们可以看到是default
                .withDescription(jobDesc)
                .build();


观察JobDetail源码 ，如果没有设置组，则系统提供为 `DEFAULT`

public class JobBuilder {
    
   public JobBuilder withIdentity(String name) {
        this.key = new JobKey(name, (String)null);
        return this;
    }

    public JobBuilder withIdentity(String name, String group) {
        this.key = new JobKey(name, group);
        return this;
    }    
 }
 
 
 public final class JobKey extends Key<JobKey> {
    private static final long serialVersionUID = -6073883950062574010L;

    public JobKey(String name) {
        super(name, (String)null);
    }

    public JobKey(String name, String group) {
        super(name, group);
    }
    
      
  }
    
    
  
  public class Key<T> implements Serializable, Comparable<Key<T>> {
    private static final long serialVersionUID = -7141167957642391350L;
    public static final String DEFAULT_GROUP = "DEFAULT";
    private final String name;
    private final String group;

    public Key(String name, String group) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null.");
        } else {
            this.name = name;
            if (group != null) {
                this.group = group;
            } else {
                this.group = "DEFAULT";
            }

        }
    }


  


```


## 4.1、`ScheduleBuilde`

### 4.1.1、`SimpleScheduleBuilder` 

> **只能指定触发的间隔时间和执行次数，需要触发器指定开始时间**；



```java
//创建触发器
//SimpleScheduleBuilder是简单调用触发器，它只能指定触发的间隔时间和执行次数
SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();  

//指定一个重复间隔,以毫秒为单位。  
simpleScheduleBuilder.withIntervalInSeconds(3);

//指定重复的的次数
simpleScheduleBuilder.withRepeatCount(5);

 
Trigger trigger = TriggerBuilder.newTrigger()
      .withIdentity("触发器的名称", "触发器的组")
      .withDescription("触发器的描述")
      .withSchedule(simpleScheduleBuilder)
      .startAt(new Date())//不设置，默认为当前时间
      .build();
      


```





### 4.1.2、`CronScheduleBuilder`

> CronScheduleBuilder是类似于Linux Cron的触发器，它通过一个称为CronExpression的规则来指定触发规则，通常是每次触发的具体时间；



```java
CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
                           
CronTrigger trigger = TriggerBuilder.newTrigger()
            .withIdentity("触发器名称，唯一指定")
            .withSchedule(scheduleBuilder)
            .build();

```





## 4.2、`@DisallowConcurrentExecution`

> 禁止并发执行多个相同定义的JobDetail       
>
> 这个注解是加在`Job`类上的, 但意思并不是不能同时执行多个Job, 而是不能并发执行同一个Job Definition(由`JobDetail`定义)    

举例说明,我们有一个`Job`类,叫做`SayHelloJob`, 并在这个`Job`上加了这个注解, 然后在这个Job上定义了很多个`JobDetail`，如`sayHelloToJoeJobDetail`, `sayHelloToMikeJobDetail`,      

那么当`scheduler`启动时, 不会并发执行多个`sayHelloToJoeJobDetail`或者`sayHelloToMikeJobDetail`。而是会按照顺序同步执行。       




![](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/15897150880051.jpg)





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

