---
title: 2、SpringBoot集成Quartz
date: 2018-03-22 21:33:00
tags: 
- Quartz
category: 
- Quartz
description: SpringBoot集成Quartz
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


有时候需要实现动态定时任务，即工程启动后，可以实现启动和关闭任务，同时也可以设置定时计划。这就需要利用到quartz


别的先不多说，先利用配置文件制作一个简单的定时器器吧

## 1、利用配置文件配置定时器（很少，以后还会详解）

### 1.1 、导入pom依赖



```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<groupId>com.hlj.quartz</groupId>
	<artifactId>com-hlj-quartz</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<packaging>jar</packaging>

	<name>com-hlj-quartz</name>
	<description>Demo project for Spring Boot</description>

	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.0.0.RELEASE</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>

	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
		<java.version>1.8</java.version>
	</properties>

	<dependencies>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.quartz-scheduler</groupId>
			<artifactId>quartz-jobs</artifactId>
			<version>2.2.1</version>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-quartz</artifactId>
		</dependency>

    </dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>

```
### 1.2、resource下创建配置文件quartz.properties


```
#scheduler实例名称。
org.quartz.scheduler.instanceName =MyScheduler
#配置线程池的容量，即表示同时最多可运行的线程数量
org.quartz.threadPool.threadCount =3
#job存储方式，RAMJobStore是使用JobStore最简单的一种方式，它也是性能最高效的，顾名思义，JobStore是把它的数据都存储在RAM中，
# 这也是它的快速和简单配置的原因；JDBCJobStore也是一种相当有名的JobStore，它通过JDBC把数据都保存到数据库中，
# 所以在配置上会比RAMJobStore复杂一些，而且不像RAMJobStore那么快，但是当我们对数据库中的表的主键创建索引时，性能上的缺点就不是很关键的了。
org.quartz.jobStore.class = org.quartz.simpl.RAMJobStore

```

### 1.3、新建一个定时器任务

继承Job类，也就是工作任务类，然后重写里面的方法 `execute`




```
package com.hlj.quartz.quartz.Job;
import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * @Description 任务类.
 * @Author HealerJean
 * @Date 2018/3/22  下午4:17.
 */

public class HelloJobOne implements Job{

        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("一号任务"+new Date());

        }
}

```


### 1.4、service中开始调用执行这个job


#### 解释:  
#### 1、其实配置文件中配置的，也就是我们可以在下面使用的定时器了，也就是获取实例

```
Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
```
#### 2、定时器有了，下一步就是获取上面的任务，也就是获取工作详情

```
JobDetail jobDetail = JobBuilder.newJob(HelloJobOne.class).withIdentity("job1","group1").build();
```

#### 3、工作任务有了，那么下一布，就是工作的执行时间和触发规则，交给定时器

```
SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startNow().withSchedule(simpleScheduleBuilder).build();
// 交由Scheduler安排触发
scheduler.scheduleJob(jobDetail,trigger);

```

#### 下面是service全部代码

```

public void haveProperties() throws SchedulerException, InterruptedException{
    /*
     *在 Quartz 中， scheduler 由 scheduler 工厂创建：
     * DirectSchedulerFactory 或者 StdSchedulerFactory。
     *第二种工厂 StdSchedulerFactory 使用较多，
     *因为 DirectSchedulerFactory 使用起来不够方便，需要作许多详细的手工编码设置。
     */
    // 获取Scheduler实例
    Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
    scheduler.start();
    System.out.println("scheduler.start");
    //具体任务.
    JobDetail jobDetail = JobBuilder.newJob(HelloJobOne.class).withIdentity("job1","group1").build();
    //触发时间点. (每5秒执行1次.)
    SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever();
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger1","group1").startNow().withSchedule(simpleScheduleBuilder).build();
    // 交由Scheduler安排触发
    scheduler.scheduleJob(jobDetail,trigger);
    //睡眠20秒.
    TimeUnit.SECONDS.sleep(20);
    scheduler.shutdown();//关闭定时任务调度器.
    System.out.println("scheduler.shutdown");

}

```


### 1.5、controler中开始调用


```

/**
 * @Desc   有配置文件quartz.properties
 * @Date   2018/3/22 下午5:40.
 */

@GetMapping("haveProperties")
public String haveProperties() throws InterruptedException, SchedulerException {
    quartzService.haveProperties();
    return "使用配置文件-定时器开始执行，请观察控制台";
}

```


### 1.6、浏览器中访问吧，朋友

[http://localhost:8080/haveProperties](http://localhost:8080/haveProperties)

#### 调用的时候可以看到控制台关于，配置文件 quartz.properties 中的一些信息。 而且人物执行成功

![WX20180322-191414](markdownImage/WX20180322-191414.png)


## 2、没有配置文件，但是很牛逼的真正的quartz

### 2.1、定义一个工作工厂，`JobFactory`


```
package com.hlj.quartz.quartz.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  下午3:43.
 */
@Component
public class JobFactory extends AdaptableJobFactory {
    @Autowired
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
        //调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        //进行注入
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}

```

### 2.2、开始从定时器工厂中，找到定时器Bean

```
package com.hlj.quartz.quartz.config;

import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;


/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  下午3:45.
 */
@Configuration
public class QuartzConfig {

    @Autowired
    private JobFactory jobFactory;


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
            schedulerFactoryBean.setOverwriteExistingJobs(true);
            schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }


    // 创建schedule
    @Bean(name = "scheduler")
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }

}

```




### 2.3、这个时候，我们开始一系列的方法，对定时器进行了解

#### 实话实说，我看的比较着急，还没有对定时器自己的一些方法进行仔细深入的了解

1、这时，我们可以看到这里用到的其实就是固定时间的定时器（可以设置为某一天的几点开始执行） ，上面的定时器是没几秒开始执行。所以二者是有区别的，具体如下

SimpleScheduleBuilder是简单调用触发器，它只能**指定触发的间隔时间和执行次数**；

CronScheduleBuilder是类似于Linux Cron的触发器，它通过一个称为CronExpression的规则来指定触发规则，通常是每次触发的具体时间；

CalendarIntervalScheduleBuilder是对CronScheduleBuilder的补充，它能指定每隔一段时间触发一次。


2、针对暂停，和关闭任务，也是是利用触发规则的name和group，然后判断这个规则是否存在工作，如果存在则执行定时器

3、下面我设置了两个任务。controller中对这两个任务进行观察



```
package com.hlj.quartz.quartz.service;

import com.hlj.quartz.quartz.Job.HelloJobOne;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  下午4:38.
 */
/**
 SimpleScheduleBuilder是简单调用触发器，它只能指定触发的间隔时间和执行次数；
 CronScheduleBuilder是类似于Linux Cron的触发器，它通过一个称为CronExpression的规则来指定触发规则，通常是每次触发的具体时间；（关于CronExpression，详见：官方，中文网文）
 CalendarIntervalScheduleBuilder是对CronScheduleBuilder的补充，它能指定每隔一段时间触发一次。
 */

@Service
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    public void startJob(String time,String jobName,String group,Class job){
        try {
            // 创建jobDetail实例，绑定Job实现类
            // 指明job的名称，所在组的名称，以及绑定job类
            JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(jobName, group).build();//设置Job的名字和组
            //corn表达式  每2秒执行一次
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time/*"0/2 * * * * ?"*/);
            //设置定时任务的时间触发规则
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName,group) .withSchedule(scheduleBuilder).build();
            System.out.println(scheduler.getSchedulerName());
            // 把作业和触发器注册到任务调度中, 启动调度
            scheduler.scheduleJob(jobDetail,cronTrigger);
        /*
        // 启动调度
         scheduler.start();
         Thread.sleep(30000);
        // 停止调度
        scheduler.shutdown();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startJob2(String time,String jobName,String group,Class job){
        try {
            JobDetail jobDetail = JobBuilder.newJob(job).withIdentity(jobName, group).build();//设置Job的名字和组
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(time/*"0/2 * * * * ?"*/);
            CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName,group) .withSchedule(scheduleBuilder).build();
            System.out.println(scheduler.getSchedulerName());
            scheduler.scheduleJob(jobDetail,cronTrigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /****
     * 暂停一个任务
     * @param triggerName
     * @param triggerGroupName
     */
    public void pauseJob(String triggerName,String triggerGroupName){
        try {
            JobKey jobKey = new JobKey(triggerName, triggerGroupName);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail==null){
                return;
            }
            System.out.println("开始暂停一个定时器");
            scheduler.pauseJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /****
     * 暂停重启一个定时器任务
     * @param triggerName
     * @param triggerGroupName
     */
    public void resumeJob(String triggerName,String triggerGroupName){
        try {
            JobKey jobKey = new JobKey(triggerName, triggerGroupName);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail==null){
                return;
            }
            scheduler.resumeJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }



    /****
     * 删除一个定时器任务，删除了，重启就没什么用了
     * @param triggerName
     * @param triggerGroupName
     */
    public void deleteJob(String triggerName,String triggerGroupName){
        try {
            JobKey jobKey = new JobKey(triggerName, triggerGroupName);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail==null){
                return;
            }
            scheduler.deleteJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }




    /***
     * 根据出发规则匹配任务，立即执行定时任务，暂停的时候可以用
     */
    public void doJob(String triggerName,String triggerGroupName){
        try {
            JobKey jobKey = JobKey.jobKey(triggerName, triggerGroupName);
            scheduler.triggerJob(jobKey);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    public void startAllJob(){
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    public void shutdown(){
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


```

### 2.4、controller控制执行，注意观察控制台


```
package com.hlj.quartz.controller;

import com.hlj.quartz.quartz.Job.HelloJoTwo;
import com.hlj.quartz.quartz.service.QuartzService;
import com.hlj.quartz.quartz.Job.HelloJobOne;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/22  下午4:47.
 */
@RestController
public class QuartzController {


    @Autowired
    private QuartzService quartzService;


    @GetMapping("quartzStart")
    public String startNNoQuartz(){
        quartzService.startJob("0/1 * * * * ? ","job1","gropu1", HelloJobOne.class);
        quartzService.startJob("0/2 * * * * ? ","job2","gropu2", HelloJoTwo.class);

        return "定时器任务开始执行，请注意观察控制台";
    }

    @GetMapping("pauseJob")
    public String pauseJob(){
        quartzService.pauseJob("job1","gropu1");
        return "暂停一个定时器任务，请注意观察控制台";
    }


    @GetMapping("resumeJob") //shutdown关闭了，或者删除了就不能重启了
    public String resumeJob(){
        quartzService.resumeJob("job1","gropu1");
        return "暂停重启一个定时器任务，请注意观察控制台";
    }

    @GetMapping("deleteJob")
    public String deleteJob(){
        quartzService.deleteJob("job1","gropu1");
        return "删除一个定时器任务，请注意观察控制台，删除了，重启就没什么用了";
    }



    @GetMapping("doJob")
    public String doJob(){
        quartzService.doJob("job1","gropu1");
        return "根据出发规则匹配任务，立即执行定时任务，暂停的时候可以用";
    }

    @GetMapping("startAllJob")
    public String startAllJob(){
        quartzService.startAllJob();
        return "开启定时器，这时才可以开始所有的任务，默认是开启的";
    }

    @GetMapping("shutdown")
    public String shutdown(){
        quartzService.shutdown();
        return "关闭定时器，则所有任务不能执行和创建";
    }



```

### 2.5、日期的匹配规则

<table>
<thead>
<tr>
  <th>表达式</th>
  <th align="left">允许值</th>
</tr>
</thead>
<tbody><tr>
  <td>“0 0 12 * * ?”</td>
  <td align="left">每天中午12点触发</td>
</tr>
<tr>
  <td>“0 15 10 ? * *”</td>
  <td align="left">每天上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 * * ?”</td>
  <td align="left">每天上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 * * ? *”</td>
  <td align="left">每天上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 * * ? 2005”</td>
  <td align="left">2005年的每天上午10:15触发</td>
</tr>
<tr>
  <td>“0 * 14 * * ?”</td>
  <td align="left">在每天下午2点到下午2:59期间的每1分钟触发</td>
</tr>
<tr>
  <td>“0 0/5 14 * * ?”</td>
  <td align="left">在每天下午2点到下午2:55期间的每5分钟触发</td>
</tr>
<tr>
  <td>“0 0/5 14,18 * * ?”</td>
  <td align="left">在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发</td>
</tr>
<tr>
  <td>“0 0-5 14 * * ?”</td>
  <td align="left">在每天下午2点到下午2:05期间的每1分钟触发</td>
</tr>
<tr>
  <td>“0 10,44 14 ? 3 WED”</td>
  <td align="left">每年三月的星期三的下午2:10和2:44触发</td>
</tr>
<tr>
  <td>“0 15 10 ? * MON-FRI”</td>
  <td align="left">周一至周五的上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 15 * ?”</td>
  <td align="left">每月15日上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 L * ?”</td>
  <td align="left">每月最后一日的上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 ? * 6L”</td>
  <td align="left">每月的最后一个星期五上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 ? * 6L 2002-2005”</td>
  <td align="left">2002年至2005年的每月的最后一个星期五上午10:15触发</td>
</tr>
<tr>
  <td>“0 15 10 ? * 6#3”</td>
  <td align="left">每月的第三个星期五上午10:15触发</td>
</tr>
<tr>
  <td>0 6 * * *</td>
  <td align="left">每天早上6点</td>
</tr>
<tr>
  <td>0 <em>/2 </em> * *</td>
  <td align="left">每两个小时</td>
</tr>
<tr>
  <td>0 23-7/2，8 * * *</td>
  <td align="left">晚上11点到早上8点之间每两个小时，早上八点</td>
</tr>
<tr>
  <td>0 11 4 * 1-3</td>
  <td align="left">每个月的4号和每个礼拜的礼拜一到礼拜三的早上11点</td>
</tr>
<tr>
  <td>0 4 1 1 *</td>
  <td align="left">1月1日早上4点</td>
</tr>
</tbody></table>




## [代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2017_03_22_2_springBoot%E9%9B%86%E6%88%90Quartz/com-hlj-quartz.zip)




<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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

