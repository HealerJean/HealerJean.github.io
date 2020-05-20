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

#============================================================================
# 配置定时器属性
#============================================================================
#默认或是自己改名字都行
org.quartz.scheduler.instanceName=CustomQuartzScheduler
#如果使用集群，instanceId必须唯一，设置成AUTO
org.quartz.scheduler.instanceId=AUTO

# 配置线程池
org.quartz.threadPool.class=org.quartz.simpl.SimpleThreadPool
org.quartz.threadPool.threadCount=30
org.quartz.threadPool.threadPriority=5


#存储方式使用JobStoreTX，也就是数据库
#misfireThreshold是用来设置调度引擎对触发器超时的忍耐时间，简单来说 假设misfireThreshold=6000(单位毫秒)。
#那么它的意思说当一个触发器超时时间如果大于misfireThreshold的值 就认为这个触发器真正的超时(也叫Misfires)。
org.quartz.jobStore.misfireThreshold=60000
#存储方式使用JobStoreTX，也就是数据库
org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
org.quartz.jobStore.useProperties=false


#是否使用集群
org.quartz.jobStore.isClustered=true
org.quartz.jobStore.clusterCheckinInterval=20000


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
            log.info("定时器任务开始执行--------任务corn：{}, 任务名称：{}， 任务组：{}", 
                     corn, jobName, jobGroup);
        } catch (Exception e) {
            log.error("定时器任务--------任务执行失败", e);
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

import com.healerjean.proj.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

import java.time.LocalDateTime;
import java.util.Date;

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
        log.info("定时器监听，任务：【{} 准备开始执行，执行时间：{}", name, DateUtils.toDateTimeString(LocalDateTime.now(), DateUtils.YYYY_MM_dd_HH_mm_ss));
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
            log.error("定时器监听，任务：【{" + name + "}】 执行失败", e);
        } else {
            log.info("定时器监听，任务：【{} 执行成功，执行时间：{}", name, DateUtils.toDateTimeString(LocalDateTime.now(), DateUtils.YYYY_MM_dd_HH_mm_ss));
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
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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


    @ApiOperation(notes = "启动任务",
            value = "启动任务",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = ResponseBean.class
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "任务名，英文", required = true, dataTypeClass = String.class, defaultValue = "PrintTaskJob", paramType = "query"),
            @ApiImplicitParam(name = "className", value = "任务类", required = true, dataTypeClass = String.class, defaultValue = "com.healerjean.proj.schedule.job.PrintTaskJob", paramType = "query"),
            @ApiImplicitParam(name = "cron", value = "cron表达式", required = true, dataTypeClass = String.class, defaultValue = "*/20 * * * * ?", paramType = "query"),
            @ApiImplicitParam(name = "jobDesc", value = "任务描述", required = true, dataTypeClass = String.class, defaultValue = "打印任务", paramType = "query"),
    })
    @GetMapping("startJob")
    public ResponseBean startJob(String name, String className, String cron, String jobDesc) {
        log.info("quartz控制器--------启动任务--------任务名称：{}, 任务类：{}，corn表达式", name, className, cron);
        schedulerService.startJob(name, className, cron, jobDesc);
        return ResponseBean.buildSuccess("已经开启任务");
    }

    @GetMapping("pauseJob")
    public ResponseBean pauseJob(String name) {
        log.info("quartz控制器--------暂停任务--------任务名称：{}", name);
        schedulerService.pauseJob(name);
        return ResponseBean.buildSuccess("暂停任务");
    }


    @GetMapping("resumeJob")
    public ResponseBean resumeJob(String name) {
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

        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = schedulerService.getJobDetail(jobKey.getName());
            Trigger trigger = schedulerService.getJobTrigger(jobKey.getName());
            Trigger.TriggerState triggerState = schedulerService.getTriggerState(jobKey.getName());
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
    public ResponseBean getTriggerState(String name) {
        log.info("quartz控制器--------任务的执行状态--------任务名称：{}", name);
        return ResponseBean.buildSuccess("任务的执行状态", schedulerService.getTriggerState(name));
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
>
> 会将该Trriger设置为BLOCK状态，以后不会执行，你都并发了还怎么执行

举例说明,我们有一个`Job`类,叫做`SayHelloJob`, 并在这个`Job`上加了这个注解, 然后在这个Job上定义了很多个`JobDetail`，如`sayHelloToJoeJobDetail`, `sayHelloToMikeJobDetail`,      

那么当`scheduler`启动时, 不会并发执行多个`sayHelloToJoeJobDetail`或者`sayHelloToMikeJobDetail`。而是会按照顺序同步执行。       




![](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/15897150880051.jpg)



## 4.3、数据库表结构





| Table Name               | **Description**                                              |
| ------------------------ | ------------------------------------------------------------ |
| qrtz_blob_triggers       | Trigger作为Blob类型存储                                      |
| qrtz_calendars           | 存储Quartz的Calendar信息                                     |
| qrtz_cron_triggers       | 存储CronTrigger，包括Cron表达式和时区信息                    |
| **qrtz_fired_triggers**  | **存储与与触发的Trigger相关的状态信息，以及相联Job的执行信息** |
| qrtz_job_details         | 存储每一个已配置的Job的详细信息                              |
| **qrtz_locks**           | **存储程序的悲观锁的信息**                                   |
| qrtz_paused_trigger_grps | 存储已暂停的Trigger组的信息                                  |
| qrtz_scheduler_state     | 存储少量的有关Scheduler的状态信息，和别的Scheduler实例       |
| qrtz_simple_triggers     | 存储简单的Trigger，包括重复次数、间隔、以及已触的次数        |
| qrtz_simprop_triggers    | 存储CalendarIntervalTrigger和DailyTimeIntervalTrigger两种类型的触发器，使用CalendarIntervalTrigger做如下配置： |
| qrtz_triggers            | 存储已配置的Trigger的信息                                    |



# 5、分布式单节点执行原理 

## 5.1、组件间的通讯图  

![image-20200518175715901](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200518175715901.png)



## 5.2、quartz对任务调度的时序图



![image-20200518180204471](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200518180204471.png)



## 5.3、文字解释调度流程 

```
1. 先获取线程池中的可用线程数量（若没有可用的会阻塞，直到有可用的）；
2. 获取30m内要执行的trigger(即acquireNextTriggers)：
   获取trigger的锁，经过select …for update方式实现；获取30m内（可配置）要执行的triggers（须要保证集群节点的时间一致），若@ConcurrentExectionDisallowed且列表存在该条trigger则跳过，不然更新trigger状态为ACQUIRED(刚开始为WAITING)；插入firedTrigger表，状态为ACQUIRED;（注意：在RAMJobStore中，有个timeTriggers，排序方式是按触发时间nextFireTime排的；JobStoreSupport从数据库取出triggers时是按照nextFireTime排序）;
3. 等待直到获取的trigger中最早执行的trigger在2ms内；
4. triggersFired：
   1. 更新firedTrigger的status=EXECUTING;
   2. 更新trigger下一次触发的时间；
   3. 更新trigger的状态：无状态的trigger->WAITING，有状态的trigger->BLOCKED，若nextFireTime==null ->COMPLETE；
   4. commit connection,释放锁；
5. 针对每一个要执行的trigger，建立JobRunShell，并放入线程池执行：
   1. execute:执行job
   2. 获取TRIGGER_ACCESS锁
   3. 如果有状态的job：更新trigger状态：BLOCKED->WAITING,PAUSED_BLOCKED->BLOCKED
   4. 若@PersistJobDataAfterExecution，则updateJobData
   5. 删除firedTrigger
   6. commit connection，释放锁
```



### 5.3.1、获取待触发trigger     

​	1.1、数据库`LOCKS`表`TRIGGER_ACCESS`行加锁

​	1.2、读取`JobDetail`信息     

​	1.3、读取`trigger`表中触发器信息并标记为"已获取`ACQUIRED`"       

​	1.4、commit事务,释放锁     

### 5.3.2、触发trigger   

 	2.1、数据库`LOCKS`表`TRIGGER_ACCESS`行加锁    

 	2.2、确认`trigger`的状态    

​	 2.3、读取`trigger`的`JobDetail`信息    

​	 2.4、读取`trigger`的`Calendar`信息    

​	 2.5、更新`trigger`信息    

 	2.6、commit事务,释放锁      

### 5.3.3、实例化并执行Job

​	3.1、从线程池获取线程执行JobRunShell的run方法



可以看到,这个过程中有两个相似的过程，同样是对数据表的更新操作,同样是在执行操作前获取锁 操作完成后释放锁.这一规则可以看做是quartz解决集群问题的核心思想.

![image-20200518184830501](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200518184830501.png)









# 6、源码分析过程


## 6.1、调度器实例化 

```java
////取得Schedule对象
Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//启动调度器
scheduler.start();

//具体任务.
JobDetail jobDetail = JobBuilder.newJob(OneJob.class).withIdentity(name, group).build();

//触发时间点. (每5秒执行1次.)
SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder
    .simpleSchedule()
    .withIntervalInSeconds(5)
    .repeatForever();

Trigger trigger = TriggerBuilder.newTrigger()
    .withIdentity(name, group)
    .startNow()
    .withSchedule(simpleScheduleBuilder)
    .build();

// 交由Scheduler安排触发
scheduler.scheduleJob(jobDetail, trigger);
```



### 6.1.1、从工厂中获取调度器

```java
public static Scheduler getDefaultScheduler() throws SchedulerException {
    StdSchedulerFactory fact = new StdSchedulerFactory();
    return fact.getScheduler();
}
```



```java
 public Scheduler getScheduler() throws SchedulerException {
        if (this.cfg == null) {
            this.initialize();
        }

        SchedulerRepository schedRep = SchedulerRepository.getInstance();
       //从"调度器仓库"中根据properties的SchedulerName配置获取一个调度器实例
        Scheduler sched = schedRep.lookup(this.getSchedulerName());
        if (sched != null) {
            if (!sched.isShutdown()) {
                return sched;
            }

            schedRep.remove(this.getSchedulerName());
        }

        sched = this.instantiate();
        return sched;
    }
```

`this.initialize()`：   读取配置资源,   生成`QuartzScheduler`对象,   初始化工作已完成,



### 6.1.2、启动调度器



```java
public void start() throws SchedulerException {
    if (!this.shuttingDown && !this.closed) {
        //通知该调度器的listener启动开始
        this.notifySchedulerListenersStarting();
        if (this.initialStart == null) {
            this.initialStart = new Date();
              //启动调度器的线程
            this.resources.getJobStore().schedulerStarted();
             //启动plugins
            this.startPlugins();
        } else {
            this.resources.getJobStore().schedulerResumed();
        }

        this.schedThread.togglePause(false);
        this.getLog().info("Scheduler " + this.resources.getUniqueIdentifier() + " started.");
         //通知该调度器的listener启动完成
        this.notifySchedulerListenersStarted();
    } else {
        throw new SchedulerException("The Scheduler cannot be restarted after shutdown() has been called.");
    }
}
```









## 6.2、调度过程

> 1、获取待触发trigger     
>
> 2、触发trigger     
>
> 3、实例化并执行Job    
>
> 



### 6.2.1、调度器线程类入口：调度入口，是个死循环

> `QuartzSchedulerThread`是调度器线程类，调度过程的三个步骤就承载在run()方法中,分析见代码注释:  



```java
public void run() {
   boolean lastAcquireFailed = false;
   while (!halted.get()) {
     ......

     int availThreadCount = qsRsrcs.getThreadPool().blockForAvailableThreads();
     if(availThreadCount > 0) { 

     ......

     //调度器在trigger队列中寻找30秒内一定数目的trigger(需要保证集群节点的系统时间一致)
        //参数1：`nolaterthan` = `now`+3000ms，即未来30s内将会被触发（idleWaitTime 30s）.      
		//参数2：最大获取数量，大小取线程池线程剩余量与定义值得较小者.      
     triggers = qsRsrcs.getJobStore().acquireNextTriggers(
                            now + idleWaitTime, 
         Math.min(availThreadCount, qsRsrcs.getMaxBatchSize()), 
         qsRsrcs.getBatchTimeWindow());

     ......

     //触发trigger，
     List<TriggerFiredResult> res = qsRsrcs.getJobStore().triggersFired(triggers);

     ......

     //释放trigger，任务开始执行
     for (int i = 0; i < triggers.size(); i++) {
         qsRsrcs.getJobStore().releaseAcquiredTrigger(triggers.get(i));
     }

   }                
}
```



由此可知，`QuartzScheduler`调度线程不断获取`trigger`，触发`trigger`，释放`trigger`。下面分析trigger的获取过程，`qsRsrcs.getJobStore()返`回对象是`JobStore`，配置如下：

```properties
org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX
```



`JobStoreTX`继承自`JobStoreSupport`，而`JobStoreSupport`的`acquireNextTriggers`、`triggersFired`、`releaseAcquiredTrigger`方法负责具体`trigger`相关操作，**都必须获得`TRIGGER_ACCESS`锁。核心逻辑d都在`executeInNonManagedTXLock`方法中**       



### 6.2.2、触发器的获取   

> 调度器在trigger队列中寻找30秒内一定数目的`trigger`(需要保证集群节点的系统时间一致)

```java
 //调度器在trigger队列中寻找30秒内一定数目的trigger(需要保证集群节点的系统时间一致)
//参数1：`nolaterthan` = `now`+3000ms，即未来30s内将会被触发（idleWaitTime 30s）.      
//参数2：最大获取数量，大小取线程池线程剩余量与定义值得较小者.  
triggers = qsRsrcs.getJobStore().acquireNextTriggers(
								now + idleWaitTime, Math.min(availThreadCount, 
                                qsRsrcs.getMaxBatchSize()), 
   							    qsRsrcs.getBatchTimeWindow());
```



```java
public List<OperableTrigger> acquireNextTriggers(final long noLaterThan, final int maxCount, final long timeWindow) throws JobPersistenceException {
    String lockName;
    if (!this.isAcquireTriggersWithinLock() && maxCount <= 1) {
        lockName = null;
    } else {
        lockName = "TRIGGER_ACCESS";
    }

    return (List)this.executeInNonManagedTXLock(lockName, 
                                                
        public List<OperableTrigger> execute(Connection conn) 
                                                throws JobPersistenceException {
            return JobStoreSupport.this.acquireNextTrigger(conn, 
                                                           noLaterThan, 
                                                           maxCount, 
                                                           timeWindow);
        }
    }, 
                                                
        new JobStoreSupport.TransactionValidator<List<OperableTrigger>>() {
            
        	public Boolean validate(Connection conn, List<OperableTrigger> result) 
                throws JobPersistenceException {
            try {
                List<FiredTriggerRecord> acquired = JobStoreSupport.this.getDelegate()
                    .selectInstancesFiredTriggerRecords(conn, 
                                                   JobStoreSupport.this.getInstanceId());
                Set<String> fireInstanceIds = new HashSet();
                Iterator i$ = acquired.iterator();

                while(i$.hasNext()) {
                    FiredTriggerRecord ft = (FiredTriggerRecord)i$.next();
                    fireInstanceIds.add(ft.getFireInstanceId());
                }

                i$ = result.iterator();

                OperableTrigger tr;
                do {
                    if (!i$.hasNext()) {
                        return false;
                    }

                    tr = (OperableTrigger)i$.next();
                } while(!fireInstanceIds.contains(tr.getFireInstanceId()));

                return true;
            } catch (SQLException var7) {
                throw new JobPersistenceException("error validating trigger acquisition", var7);
            }
        }
    });
}
```







```java
protected List<OperableTrigger> acquireNextTrigger(Connection conn, long noLaterThan, int maxCount, long timeWindow) throws JobPersistenceException {
    if (timeWindow < 0L) {
        throw new IllegalArgumentException();
    } else {
        List<OperableTrigger> acquiredTriggers = new ArrayList();
        Set<JobKey> acquiredJobKeysForNoConcurrentExec = new HashSet();
        int MAX_DO_LOOP_RETRY = true;
        int currentLoopCount = 0;

        while(true) {
            ++currentLoopCount;

            try {
                List<TriggerKey> keys = this.getDelegate().selectTriggerToAcquire(conn, noLaterThan + timeWindow, this.getMisfireTime(), maxCount);
                if (keys != null && keys.size() != 0) {
                    long batchEnd = noLaterThan;
                    Iterator i$ = keys.iterator();

                    while(i$.hasNext()) {
                        TriggerKey triggerKey = (TriggerKey)i$.next();
                        OperableTrigger nextTrigger = this.retrieveTrigger(conn, triggerKey);
                        if (nextTrigger != null) {
                            JobKey jobKey = nextTrigger.getJobKey();

                            JobDetail job;
                            try {
                                job = this.retrieveJob(conn, jobKey);
                            } catch (JobPersistenceException var22) {
                                JobPersistenceException jpe = var22;

                                try {
                                    this.getLog().error("Error retrieving job, setting trigger state to ERROR.", jpe);
                                    this.getDelegate().updateTriggerState(conn, triggerKey, "ERROR");
                                } catch (SQLException var21) {
                                    this.getLog().error("Unable to set trigger state to ERROR.", var21);
                                }
                                continue;
                            }

                            if (job.isConcurrentExectionDisallowed()) {
                                if (acquiredJobKeysForNoConcurrentExec.contains(jobKey)) {
                                    continue;
                                }

                                acquiredJobKeysForNoConcurrentExec.add(jobKey);
                            }

                            if (nextTrigger.getNextFireTime().getTime() > batchEnd) {
                                break;
                            }

                            int rowsUpdated = this.getDelegate().updateTriggerStateFromOtherState(conn, triggerKey, "ACQUIRED", "WAITING");
                            if (rowsUpdated > 0) {
                                nextTrigger.setFireInstanceId(this.getFiredTriggerRecordId());
                                this.getDelegate().insertFiredTrigger(conn, nextTrigger, "ACQUIRED", (JobDetail)null);
                                if (acquiredTriggers.isEmpty()) {
                                    batchEnd = Math.max(nextTrigger.getNextFireTime().getTime(), System.currentTimeMillis()) + timeWindow;
                                }

                                acquiredTriggers.add(nextTrigger);
                            }
                        }
                    }

                    if (acquiredTriggers.size() == 0 && currentLoopCount < 3) {
                        continue;
                    }

                    return acquiredTriggers;
                }

                return acquiredTriggers;
            } catch (Exception var23) {
                throw new JobPersistenceException("Couldn't acquire next trigger: " + var23.getMessage(), var23);
            }
        }
    }
}
```



`executeInNonManagedTXLock`方法指定了一个锁名`TRIGGER_ACCESS`，两个回调函数（接口）     

**在开始执行时获得锁，在方法执行完毕后随着事务的提交锁被释放，在该方法的底层使用 for update语句，在数据库中加入行级锁，保证了在该方法执行过程中，其他的调度器对trigger进行获取时将会等待该调度器释放该锁**       



```java
protected <T> T executeInNonManagedTXLock(
    String lockName, 
    TransactionCallback<T> txCallback, final TransactionValidator<T> txValidator) throws JobPersistenceException {
    boolean transOwner = false;
    Connection conn = null;
    try {
        if (lockName != null) {
            if (getLockHandler().requiresConnection()) {
                conn = getNonManagedTXConnection();
            }

            //获取锁
            transOwner = getLockHandler().obtainLock(conn, lockName);
        }

        if (conn == null) {
            conn = getNonManagedTXConnection();
        }

        final T result = txCallback.execute(conn);
        try {
            commitConnection(conn);
        } catch (JobPersistenceException e) {
            rollbackConnection(conn);
            if (txValidator == null || !retryExecuteInNonManagedTXLock(lockName, new TransactionCallback<Boolean>() {
                @Override
                public Boolean execute(Connection conn) throws JobPersistenceException {
                    return txValidator.validate(conn, result);
                }
            })) {
                throw e;
            }
        }

        Long sigTime = clearAndGetSignalSchedulingChangeOnTxCompletion();
        if(sigTime != null && sigTime >= 0) {
            signalSchedulingChangeImmediately(sigTime);
        }

        return result;
    } catch (JobPersistenceException e) {
        rollbackConnection(conn);
        throw e;
    } catch (RuntimeException e) {
        rollbackConnection(conn);
        throw new JobPersistenceException("Unexpected runtime exception: "
                                          + e.getMessage(), e);
    } finally {
        try {
            releaseLock(lockName, transOwner);      //释放锁
        } finally {
            cleanupConnection(conn);
        }
    }
}
```



**`getLockHandler()`方法返回的对象类型是`Semaphore`，取锁和释放锁的具体逻辑由该对象维护**      

```java
public interface Semaphore {

     boolean obtainLock(Connection conn, String lockName) throws LockException;

     void releaseLock(String lockName) throws LockException;

     boolean requiresConnection();
}
```



该接口的实现类完成具体操作锁的逻辑，在`JobStoreSupport`的初始化方法中注入的`Semaphore`具体类型是`StdRowLockSemaphore`

```java
setLockHandler(new StdRowLockSemaphore(getTablePrefix(), 
                                       getInstanceName(), 
                                       getSelectWithLockSQL()));

```



`StdRowLockSemaphore`源码如下    

```java
public class StdRowLockSemaphore extends DBSemaphore {
//锁定SQL语句
public static final String SELECT_FOR_LOCK = "SELECT * FROM "
        + TABLE_PREFIX_SUBST + TABLE_LOCKS + " WHERE " + COL_LOCK_NAME
        + " = ? FOR UPDATE";

public static final String INSERT_LOCK = "INSERT INTO " + TABLE_PREFIX_SUBST 
        + TABLE_LOCKS + "(" + COL_SCHEDULER_NAME + ", " 
        + COL_LOCK_NAME + ") VALUES (" + SCHED_NAME_SUBST + ", ?)"; 

//指定锁定SQL
protected void executeSQL(Connection conn, String lockName, String expandedSQL) throws LockException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    try {
        ps = conn.prepareStatement(expandedSQL);
        ps.setString(1, lockName);
        ......
        rs = ps.executeQuery();
        if (!rs.next()) {
            throw new SQLException(Util.rtp(
                "No row exists in table " + TABLE_PREFIX_SUBST +
                TABLE_LOCKS + " for lock named: " + lockName, getTablePrefix()));
        }
    } catch (SQLException sqle) {

    } finally {
      ...... //release resources
    }
  }
}

//获取QRTZ_LOCKS行级锁
public boolean obtainLock(Connection conn, String lockName) throws LockException {
    lockName = lockName.intern();

    if (!isLockOwner(conn, lockName)) {
        executeSQL(conn, lockName, expandedSQL);

        getThreadLocks().add(lockName);
    }
    return true;
}

//释放QRTZ_LOCKS行级锁
public void releaseLock(Connection conn, String lockName) {
    lockName = lockName.intern();

    if (isLockOwner(conn, lockName)) {
        getThreadLocks().remove(lockName);
    }
    ......
}
```



**quratz在获取数据库资源之前，先要以`for update`方式访问`LOCKS`表中相应`LOCK_NAM`E数据将改行锁定，如果在此前该行已经被锁定,那么等待释放锁，如果没有被锁定，那么读取满足要求的`trigger`。 **       



```java
public List<TriggerKey> selectTriggerToAcquire(Connection conn, long noLaterThan, long noEarlierThan, int maxCount) throws SQLException {
    PreparedStatement ps = null;
    ResultSet rs = null;
    LinkedList nextTriggers = new LinkedList();

    try {
        ps = conn.prepareStatement(this.rtp("SELECT TRIGGER_NAME, TRIGGER_GROUP, NEXT_FIRE_TIME, PRIORITY FROM {0}TRIGGERS WHERE SCHED_NAME = {1} AND TRIGGER_STATE = ? AND NEXT_FIRE_TIME <= ? AND (MISFIRE_INSTR = -1 OR (MISFIRE_INSTR != -1 AND NEXT_FIRE_TIME >= ?)) ORDER BY NEXT_FIRE_TIME ASC, PRIORITY DESC"));
        if (maxCount < 1) {
            maxCount = 1;
        }

        ps.setMaxRows(maxCount);
        ps.setFetchSize(maxCount);
        ps.setString(1, "WAITING");
        ps.setBigDecimal(2, new BigDecimal(String.valueOf(noLaterThan)));
        ps.setBigDecimal(3, new BigDecimal(String.valueOf(noEarlierThan)));
        rs = ps.executeQuery();

        while(rs.next() && nextTriggers.size() <= maxCount) {
            nextTriggers.add(TriggerKey.triggerKey(rs.getString("TRIGGER_NAME"), rs.getString("TRIGGER_GROUP")));
        }

        LinkedList var10 = nextTriggers;
        return var10;
    } finally {
        closeResultSet(rs);
        closeStatement(ps);
    }
}
```



**然后把它们的status置为`STATE_ACQUIRED`，如果有tirgger已被置为`STATE_ACQUIRED`，那么说明该`trigger`已被别的调度器实例认领,无需再次认领，调度器查询的时候会忽略此trigger，调度器实例之间的间接通信就体现在这里.**     



`JobStoreSupport.acquireNextTrigger()`方法中

```java
// 讲WAITING状态的设置为ACQUIRED
int rowsUpdated = getDelegate().updateTriggerStateFromOtherState(conn, 
                                                                 triggerKey, 
                                                                 STATE_ACQUIRED,
                                                                 STATE_WAITING);
```

```java
public int updateTriggerStateFromOtherState(Connection conn, TriggerKey triggerKey, String newState, String oldState) throws SQLException {
    PreparedStatement ps = null;

    int var6;
    try {
        ps = conn.prepareStatement(this.rtp("UPDATE {0}TRIGGERS SET TRIGGER_STATE = ? WHERE SCHED_NAME = {1} AND TRIGGER_NAME = ? AND TRIGGER_GROUP = ? AND TRIGGER_STATE = ?"));
        ps.setString(1, newState);
        ps.setString(2, triggerKey.getName());
        ps.setString(3, triggerKey.getGroup());
        ps.setString(4, oldState);
        var6 = ps.executeUpdate();
    } finally {
        closeStatement(ps);
    }

    return var6;
}
```



**最后释放锁,这时如果下一个调度器在排队获取`trigger`的话,则仍会执行相同的步骤.这种机制保证了`trigger`不会被重复获取.按照这种算法正常运行状态下调度器每次读取的`trigger`中会有相当一部分已被标记为被获取.**



### 6.2.3、触发trigger

```java
List<TriggerFiredResult> res = qsRsrcs.getJobStore().triggersFired(triggers)
```



```java
    public List<TriggerFiredResult> triggersFired(final List<OperableTrigger> triggers) throws JobPersistenceException {
        return (List)this.executeInNonManagedTXLock("TRIGGER_ACCESS", new JobStoreSupport.TransactionCallback<List<TriggerFiredResult>>() {
            public List<TriggerFiredResult> execute(Connection conn) throws JobPersistenceException {
                List<TriggerFiredResult> results = new ArrayList();

                TriggerFiredResult result;
                for(Iterator i$ = triggers.iterator(); i$.hasNext(); results.add(result)) {
                    OperableTrigger trigger = (OperableTrigger)i$.next();

                    try {
                        TriggerFiredBundle bundle = JobStoreSupport.this.triggerFired(conn, trigger);
                        result = new TriggerFiredResult(bundle);
                    } catch (JobPersistenceException var7) {
                        result = new TriggerFiredResult(var7);
                    } catch (RuntimeException var8) {
                        result = new TriggerFiredResult(var8);
                    }
                }

                return results;
            }
        }, new JobStoreSupport.TransactionValidator<List<TriggerFiredResult>>() {
            public Boolean validate(Connection conn, List<TriggerFiredResult> result) throws JobPersistenceException {
                try {
                    List<FiredTriggerRecord> acquired = JobStoreSupport.this.getDelegate().selectInstancesFiredTriggerRecords(conn, JobStoreSupport.this.getInstanceId());
                    Set<String> executingTriggers = new HashSet();
                    Iterator i$ = acquired.iterator();

                    while(i$.hasNext()) {
                        FiredTriggerRecord ft = (FiredTriggerRecord)i$.next();
                        if ("EXECUTING".equals(ft.getFireInstanceState())) {
                            executingTriggers.add(ft.getFireInstanceId());
                        }
                    }

                    i$ = result.iterator();

                    TriggerFiredResult tr;
                    do {
                        if (!i$.hasNext()) {
                            return false;
                        }

                        tr = (TriggerFiredResult)i$.next();
                    } while(tr.getTriggerFiredBundle() == null || !executingTriggers.contains(tr.getTriggerFiredBundle().getTrigger().getFireInstanceId()));

                    return true;
                } catch (SQLException var7) {
                    throw new JobPersistenceException("error validating trigger acquisition", var7);
                }
            }
        });
    }
```



此处再次用到了`executeInNonManagedTXLock()`方法，在获取锁的情况下对`trigger`进行触发操作.其中的触发细节如下:       



1、获取trigger当前状态

2、通过trigger中的JobKey读取trigger包含的Job信息   ，更新或者导入    `fired_trigger`表

3、将`fired_trigger`更新至触发状态,（表`qrtz_fired_triggers`），,及计算下一次触发时间，添加下一次的触发器 `qrtz_fired_triggers`

4、返回trigger触发结果的数据传输类TriggerFiredBundle



```java
protected TriggerFiredBundle triggerFired(Connection conn,
            OperableTrigger trigger)
        throws JobPersistenceException {
        JobDetail job;
        Calendar cal = null;
        // Make sure trigger wasn't deleted, paused, or completed...
        try { // if trigger was deleted, state will be STATE_DELETED
            String state = getDelegate().selectTriggerState(conn,
                    trigger.getKey());
            //如果任务已经被其他节点拥有了，则返回
            if (!state.equals(STATE_ACQUIRED)) {
                return null;
            }
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't select trigger state: "
                    + e.getMessage(), e);
        }
        try {
            //获取任务
            job = retrieveJob(conn, trigger.getJobKey());
            if (job == null) { return null; }
        } catch (JobPersistenceException jpe) {
            try {
                getLog().error("Error retrieving job, setting trigger state to ERROR.", jpe);
                getDelegate().updateTriggerState(conn, trigger.getKey(),
                        STATE_ERROR);
            } catch (SQLException sqle) {
                getLog().error("Unable to set trigger state to ERROR.", sqle);
            }
            throw jpe;
        }
        if (trigger.getCalendarName() != null) {
            cal = retrieveCalendar(conn, trigger.getCalendarName());
            if (cal == null) { return null; }
        }
        try {
            //任务开始执行，设置当前触发器状态为执行状态
            getDelegate().updateFiredTrigger(conn, trigger, STATE_EXECUTING, job);
        } catch (SQLException e) {
            throw new JobPersistenceException("Couldn't insert fired trigger: "
                    + e.getMessage(), e);
        }
        Date prevFireTime = trigger.getPreviousFireTime();
        // call triggered - to update the trigger's next-fire-time state...
        trigger.triggered(cal);
        String state = STATE_WAITING;
        boolean force = true;
         
        if (job.isConcurrentExectionDisallowed()) {
            state = STATE_BLOCKED;
            force = false;
            try {
                getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getKey(),
                        STATE_BLOCKED, STATE_WAITING);
                getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getKey(),
                        STATE_BLOCKED, STATE_ACQUIRED);
                getDelegate().updateTriggerStatesForJobFromOtherState(conn, job.getKey(),
                        STATE_PAUSED_BLOCKED, STATE_PAUSED);
            } catch (SQLException e) {
                throw new JobPersistenceException(
                        "Couldn't update states of blocked triggers: "
                                + e.getMessage(), e);
            }
        }
             
        if (trigger.getNextFireTime() == null) {
            state = STATE_COMPLETE;
            force = true;
        }
        storeTrigger(conn, trigger, job, true, state, force, false);
        job.getJobDataMap().clearDirtyFlag();
        return new TriggerFiredBundle(job, trigger, cal, trigger.getKey().getGroup()
                .equals(Scheduler.DEFAULT_RECOVERY_GROUP), new Date(), trigger
                .getPreviousFireTime(), prevFireTime, trigger.getNextFireTime());
    }
```





### 6.2.4、触发器释放，Job执行过程

```java

qsRsrcs.getJobStore().releaseAcquiredTrigger(triggers.get(i));
shell.initialize(qs);
```

> 为每个Job生成一个可运行的RunShell,并放入线程池运行.
>
> 在最后调度线程生成了一个随机的等待时间,进入短暂的等待,这使得其他节点的调度器都有机会获取数据库资源.如此就实现了quratz的负载平衡.
>
> 这样一次完整的调度过程就结束了.调度器线程进入下一次循环.



**触发器执行完成，要讲状态设置WAITING状态，并删除当前正在执行的触发器`Fired_Trugger`信息**  



```java
public void releaseAcquiredTrigger(final OperableTrigger trigger) {
    this.retryExecuteInNonManagedTXLock
        ("TRIGGER_ACCESS",                               
        new JobStoreSupport.VoidTransactionCallback() {
            public void executeVoid(Connection conn) throws JobPersistenceException {
                JobStoreSupport.this.releaseAcquiredTrigger(conn, trigger);
            }
        });
}

protected void releaseAcquiredTrigger(Connection conn, OperableTrigger trigger) throws JobPersistenceException {
    try {
        this.getDelegate().updateTriggerStateFromOtherState(conn,
                                                            trigger.getKey(), 
                                                            "WAITING", 
                                                            "ACQUIRED");
        this.getDelegate().deleteFiredTrigger(conn, trigger.getFireInstanceId());
    } catch (SQLException var4) {
        throw new JobPersistenceException("Couldn't release acquired trigger: " + 
                                          var4.getMessage(), 
                                          var4);
    }
}
```



## 6.3、宕机处理未完成的任务 





```sql
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL, #服务器实例
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;




CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) COLLATE utf8_unicode_ci NOT NULL,
  `ENTRY_ID` varchar(95) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `TRIGGER_GROUP` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `INSTANCE_NAME` varchar(200) COLLATE utf8_unicode_ci NOT NULL,#服务器实例
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `JOB_NAME` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `JOB_GROUP` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
```







```java
class ClusterManager extends Thread {

    private volatile boolean shutdown = false;

    private int numFails = 0;

    ClusterManager() {
        this.setPriority(Thread.NORM_PRIORITY + 2);
        this.setName("QuartzScheduler_" + instanceName + "-" 
                     + instanceId + "_ClusterManager");
        this.setDaemon(getMakeThreadsDaemons());
    }

    public void initialize() {
        this.manage();

        ThreadExecutor executor = getThreadExecutor();
        executor.execute(ClusterManager.this);
    }

    public void shutdown() {
        shutdown = true;
        this.interrupt();
    }

    private boolean manage() {
        boolean res = false;
        try {

            res = doCheckin();

            numFails = 0;
            getLog().debug("ClusterManager: Check-in complete.");
        } catch (Exception e) {
            if(numFails % 4 == 0) {
                getLog().error(
                    "ClusterManager: Error managing cluster: "
                    + e.getMessage(), e);
            }
            numFails++;
        }
        return res;
    }

    @Override
    public void run() {
        //死循环，一直检查
        while (!shutdown) {

            if (!shutdown) {
                long timeToSleep = getClusterCheckinInterval();
                long transpiredTime = (System.currentTimeMillis() - lastCheckin);
                timeToSleep = timeToSleep - transpiredTime;
                if (timeToSleep <= 0) {
                    timeToSleep = 100L;
                }

                if(numFails > 0) {
                    timeToSleep = Math.max(getDbRetryInterval(), timeToSleep);
                }

                try {
                    Thread.sleep(timeToSleep);
                } catch (Exception ignore) {
                }
            }
			
            //调用放方法 this.manage() 管理检查
            if (!shutdown && this.manage()) {
                //处理完成发动定时器通知
                signalSchedulingChangeImmediately(0L);
            }

        }//while !shutdown
    }
}
```



`ClusterManager`：机器管理器是继承Thread的的一个线程类，同时是在`JobStoreSupport`的内部类  ，一直会运行一个  `while (!shutdown)` ，死循环监听其他节点是否宕机，以及宕机后未完成的任务。       

`this.manage()`：调用方法进行检查`doCheckin()`



```java
protected boolean doCheckin() throws JobPersistenceException {
    boolean transOwner = false;
    boolean transStateOwner = false;
    boolean recovered = false;

    Connection conn = getNonManagedTXConnection();
    try {
        //除了第一次以外，总是先签入以确保
        //在获得锁之前有工作要做（因为这很昂贵，
        //而且几乎没有必要）。必须在单独的
        //事务中完成此操作，以防止在恢复条件下出现死锁
        List<SchedulerStateRecord> failedRecords = null;
        //如果不是第一次检查的检查，也要进行检查，获取失败的节点记录 （schedulerInstanceId）
        if (!firstCheckIn) {
            failedRecords = clusterCheckIn(conn);
            commitConnection(conn);
        }

        //如果是第一次执行，并且 有失败的节点记录，则进入
        if (firstCheckIn || (failedRecords.size() > 0)) {
            //获取状态锁
            getLockHandler().obtainLock(conn, LOCK_STATE_ACCESS);
            transStateOwner = true;

            // 如果是第一次进入获取失败的节点，如果不是第一次则再再次获取
            failedRecords = (firstCheckIn) ? clusterCheckIn(conn) : findFailedInstances(conn);
			
            //在状态锁下，依旧获取了失败的节点
            if (failedRecords.size() > 0) {
                //获取LOCK_TRIGGER_ACCESS 锁，开始准备执行任务
                getLockHandler().obtainLock(conn, LOCK_TRIGGER_ACCESS);
                //getLockHandler().obtainLock(conn, LOCK_JOB_ACCESS);
                transOwner = true;

                clusterRecover(conn, failedRecords);
                recovered = true;
            }
        }

        commitConnection(conn);
    } catch (JobPersistenceException e) {
        rollbackConnection(conn);
        throw e;
    } finally {
        try {
            releaseLock(LOCK_TRIGGER_ACCESS, transOwner);
        } finally {
            try {
                releaseLock(LOCK_STATE_ACCESS, transStateOwner);
            } finally {
                cleanupConnection(conn);
            }
        }
    }

    firstCheckIn = false;

    return recovered;
}
```



`clusterRecover`：节点恢复，找到未执行的任务改变`qrtz_triggers`表的状态，    

如果是阻塞状态变成等待状态，    

如果是暂停阻塞状态则变成暂停状态，   

如果是`STATE_ACQUIRED`，撞他变成`STATE_WAITING`等待状态   

接着删除失败的实例和，删除``FiredTriggers`，`deleteFiredTriggers`    





```java
@SuppressWarnings("ConstantConditions")
protected void clusterRecover(Connection conn, List<SchedulerStateRecord> failedInstances)
    throws JobPersistenceException {

    if (failedInstances.size() > 0) {

        long recoverIds = System.currentTimeMillis();

        logWarnIfNonZero(failedInstances.size(),
                         "ClusterManager: detected " + failedInstances.size()
                         + " failed or restarted instances.");
        try {
            for (SchedulerStateRecord rec : failedInstances) {
                getLog().info(
                    "ClusterManager: Scanning for instance \""
                    + rec.getSchedulerInstanceId()
                    + "\"'s failed in-progress jobs.");

                List<FiredTriggerRecord> firedTriggerRecs = getDelegate()
                    .selectInstancesFiredTriggerRecords(conn,
                                                        rec.getSchedulerInstanceId());

                int acquiredCount = 0;
                int recoveredCount = 0;
                int otherCount = 0;

                Set<TriggerKey> triggerKeys = new HashSet<TriggerKey>();

                for (FiredTriggerRecord ftRec : firedTriggerRecs) {

                    TriggerKey tKey = ftRec.getTriggerKey();
                    JobKey jKey = ftRec.getJobKey();

                    triggerKeys.add(tKey);

                    // release blocked triggers..
                    if (ftRec.getFireInstanceState().equals(STATE_BLOCKED)) {
                        getDelegate()
                            .updateTriggerStatesForJobFromOtherState(
                            conn, jKey,
                            STATE_WAITING, STATE_BLOCKED);
                    } else if (ftRec.getFireInstanceState().equals(STATE_PAUSED_BLOCKED)) {
                        getDelegate()
                            .updateTriggerStatesForJobFromOtherState(
                            conn, jKey,
                            STATE_PAUSED, STATE_PAUSED_BLOCKED);
                    }

                    // release acquired triggers..
                    if (ftRec.getFireInstanceState().equals(STATE_ACQUIRED)) {
                        getDelegate().updateTriggerStateFromOtherState(
                            conn, tKey, STATE_WAITING,
                            STATE_ACQUIRED);
                        acquiredCount++;
                    } else if (ftRec.isJobRequestsRecovery()) {
                        // handle jobs marked for recovery that were not fully
                        // executed..
                        if (jobExists(conn, jKey)) {
                            @SuppressWarnings("deprecation")
                            SimpleTriggerImpl rcvryTrig = new SimpleTriggerImpl(
                                "recover_"
                                + rec.getSchedulerInstanceId()
                                + "_"
                                + String.valueOf(recoverIds++),
                                Scheduler.DEFAULT_RECOVERY_GROUP,
                                new Date(ftRec.getScheduleTimestamp()));
                            rcvryTrig.setJobName(jKey.getName());
                            rcvryTrig.setJobGroup(jKey.getGroup());
                            rcvryTrig.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);
                            rcvryTrig.setPriority(ftRec.getPriority());
                            JobDataMap jd = getDelegate().selectTriggerJobDataMap(conn, tKey.getName(), tKey.getGroup());
                            jd.put(Scheduler.FAILED_JOB_ORIGINAL_TRIGGER_NAME, tKey.getName());
                            jd.put(Scheduler.FAILED_JOB_ORIGINAL_TRIGGER_GROUP, tKey.getGroup());
                            jd.put(Scheduler.FAILED_JOB_ORIGINAL_TRIGGER_FIRETIME_IN_MILLISECONDS, String.valueOf(ftRec.getFireTimestamp()));
                            jd.put(Scheduler.FAILED_JOB_ORIGINAL_TRIGGER_SCHEDULED_FIRETIME_IN_MILLISECONDS, String.valueOf(ftRec.getScheduleTimestamp()));
                            rcvryTrig.setJobDataMap(jd);

                            rcvryTrig.computeFirstFireTime(null);
                            storeTrigger(conn, rcvryTrig, null, false,
                                         STATE_WAITING, false, true);
                            recoveredCount++;
                        } else {
                            getLog()
                                .warn(
                                "ClusterManager: failed job '"
                                + jKey
                                + "' no longer exists, cannot schedule recovery.");
                            otherCount++;
                        }
                    } else {
                        otherCount++;
                    }

                    // free up stateful job's triggers
                    if (ftRec.isJobDisallowsConcurrentExecution()) {
                        getDelegate()
                            .updateTriggerStatesForJobFromOtherState(
                            conn, jKey,
                            STATE_WAITING, STATE_BLOCKED);
                        getDelegate()
                            .updateTriggerStatesForJobFromOtherState(
                            conn, jKey,
                            STATE_PAUSED, STATE_PAUSED_BLOCKED);
                    }
                }

                getDelegate().deleteFiredTriggers(conn,
                                                  rec.getSchedulerInstanceId());

                // Check if any of the fired triggers we just deleted were the last fired trigger
                // records of a COMPLETE trigger.
                int completeCount = 0;
                for (TriggerKey triggerKey : triggerKeys) {

                    if (getDelegate().selectTriggerState(conn, triggerKey).
                        equals(STATE_COMPLETE)) {
                        List<FiredTriggerRecord> firedTriggers =
                            getDelegate().selectFiredTriggerRecords(conn, triggerKey.getName(), triggerKey.getGroup());
                        if (firedTriggers.isEmpty()) {

                            if (removeTrigger(conn, triggerKey)) {
                                completeCount++;
                            }
                        }
                    }
                }

                logWarnIfNonZero(acquiredCount,
                                 "ClusterManager: ......Freed " + acquiredCount
                                 + " acquired trigger(s).");
                logWarnIfNonZero(completeCount,
                                 "ClusterManager: ......Deleted " + completeCount
                                 + " complete triggers(s).");
                logWarnIfNonZero(recoveredCount,
                                 "ClusterManager: ......Scheduled " + recoveredCount
                                 + " recoverable job(s) for recovery.");
                logWarnIfNonZero(otherCount,
                                 "ClusterManager: ......Cleaned-up " + otherCount
                                 + " other failed job(s).");

                if (!rec.getSchedulerInstanceId().equals(getInstanceId())) {
                    getDelegate().deleteSchedulerState(conn,
                                                       rec.getSchedulerInstanceId());
                }
            }
        } catch (Throwable e) {
            throw new JobPersistenceException("Failure recovering jobs: "
                                              + e.getMessage(), e);
        }
    }
}
```





















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

