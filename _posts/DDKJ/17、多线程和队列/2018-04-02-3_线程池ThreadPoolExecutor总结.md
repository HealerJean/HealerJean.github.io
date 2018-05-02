---
title: 3、线程池ThreadPoolExecutor总结
date: 2018-04-02 17:33:00
tags: 
- Thread
category: 
- Thread
description: 3、线程池ThreadPoolExecutor总结
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

本文之前应该先稍微了解下新建线程的几种方式。然后知道线程池的集中模式


## 1、知识点
### 1.1、ExecutorService中execute()和submit()方法的区别
>1. 方法execute()没有返回值，而submit()方法可以有返回值（通过Callable和Future接口）
2. 方法execute()提交的未执行的任务可以通过remove(Runnable)方法删除，而submit()提交的任务即使还未执行也不能通过remove(Runnable)方法删除
3. 方法execute()在默认情况下异常直接抛出（即打印堆栈信息），不能捕获，但是可以通过自定义ThreadFactory的方式进行捕获（通过setUncaughtExceptionHandler方法设置），而submit()方法在默认的情况下可以捕获异常


线程池的概念是Executor这个接口ExecutorService继承了它，具体实现为ThreadPoolExecutor类，学习Java中的线程池，就可以直接学习对线程池的配置，就是对ThreadPoolExecutor构造函数的参数的配置


## 2、获取`ThreadPoolExecutor`的几种构造函数

### 1、int corePoolSize：该线程池中核心线程数最大值 


```
核心线程：线程池新建线程的时候，如果当前线程总数小于corePoolSize，则新建的是核心线程，如果超过corePoolSize，则新建的是非核心线程核心线程默认情况下会一直存活在线程池中，即使这个核心线程啥也不干(闲置状态)。
 如果指定ThreadPoolExecutor的allowCoreThreadTimeOut这个属性为true，那么核心线程如果不干活(闲置状态)的话，超过一定时间(时长下面参数决定)，就会被销毁掉。

```


### 2、	int maximumPoolSize： 该线程池中线程总数最大值

```
线程总数 = 核心线程数 + 非核心线程数。

```


### 3、long keepAliveTime：该线程池中非核心线程闲置超时时长

```
一个非核心线程，如果不干活(闲置状态)的时长超过这个参数所设定的时长，就会被销毁掉，如果设置allowCoreThreadTimeOut = true，则会作用于核心线程。

```

### 4、imeUnit unit：keepAliveTime的单位

```
TimeUnit是一个枚举类型，其包括： NANOSECONDS ： 1微毫秒 = 1微秒 / 1000 MICROSECONDS ： 1微秒 = 1毫秒 / 1000 MILLISECONDS ： 1毫秒 = 1秒 /1000 SECONDS ： 秒 MINUTES ： 分 HOURS ： 小时 DAYS ： 天
```

### 5、BlockingQueue workQueue：该线程池中的任务队列：维护着等待执行的Runnable对象


```
当所有的核心线程都在干活时，新添加的任务会被添加到这个队列中等待处理，
如果队列满了，则新建非核心线程执行任务。 常用的workQueue类型：

```

#### 5.1、LinkedBlockingQueue：


```
1、这个队列接收到任务的时候，如果当前线程数小于核心线程数，则新建线程(核心线程)处理任务；（先新建核心线程）
2、如果当前线程数等于核心线程数，则进入队列等待。由于这个队列没有最大值限制，即所有超过核心线程数的任务都将被添加到队列中，这也就导致了maximumPoolSize的设定失效，因为总线程数永远不会超过corePoolSize

```


### 6、ThreadFactory threadFactory：

```
创建线程的方式，这是一个接口，你new他的时候需要实现他的Thread new Thread(Runnable r)方法，一般用不上。

```

### 7、RejectedExecutionHandler handler：


```
//任务拒绝策略，这玩意儿就是抛出异常专用的，比如上面提到的两个错误发生了，就会由这个handler抛出异常，根本用不上。
```



```

//五个参数的构造函数
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue)

//六个参数的构造函数-1
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory)

//六个参数的构造函数-2
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          RejectedExecutionHandler handler)

//七个参数的构造函数
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler)



```


## 3、ThreadPoolExecutor的策略
>
	•	1.线程数量未达到corePoolSize，则新建一个线程(核心线程)执行任务
	•	2.线程数量达到了corePoolSize，则将任务移入队列等待
	•	3.队列已满，新建线程(非核心线程)执行任务
	•	4.队列已满，总线程数又达到了maximumPoolSize，就会由(RejectedExecutionHandler)抛出异常



## 4、项目中使用


```
package com.hlj.threadpool.thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class ThreadPoolUtils {

    private static AtomicLong atomicLong = new AtomicLong(0);
    private ThreadPoolExecutor threadPoolExecutor;

    private int corePoolSize = 10;
    private int maximumPoolSize = 300;
    private long keepAliveTime = 30000;
    private TimeUnit unit = TimeUnit.MILLISECONDS;
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(10);
    private ThreadFactory threadFactory = r -> new Thread(r, "ThreadPoolUtils" + atomicLong.incrementAndGet());
    private RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

    public void execute(Runnable runnable){
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        threadPoolExecutor.execute(runnable);
    }

    private ThreadPoolExecutor getThreadPoolExecutor(){
        if (threadPoolExecutor != null) {
            return threadPoolExecutor;
        }
        synchronized (this){
            if (threadPoolExecutor != null){
                return threadPoolExecutor;
            }
            threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,unit,workQueue,threadFactory,handler);
            return threadPoolExecutor;
        }
    }

    public ThreadPoolUtils(){
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
    }

    public ThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
    }

    public RejectedExecutionHandler getHandler() {
        return handler;
    }

    public void setHandler(RejectedExecutionHandler handler) {
        this.handler = handler;
    }

}

```



### 4.2、参数为接口调用之箭头


这个箭头里面的东西就代表在run中了

```
@Slf4j
public class ThradMain {

    public static void main(String[] args) {

        ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils();
        threadPoolUtils.execute(() ->{ //这里的参数必须是一个接口
            System.out.println(Thread.currentThread().getId());

        });

    }
}

```



```
newFixedThreadPool创建的线程池corePoolSize和maximumPoolSize值是相等的，它使用的LinkedBlockingQueue；
newSingleThreadExecutor将corePoolSize和maximumPoolSize都设置为1，也使用的LinkedBlockingQueue；
newCachedThreadPool将corePoolSize设置为0，将maximumPoolSize设置为Integer.MAX_VALUE，使用的SynchronousQueue，

```

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
		id: 'TWtfKmIZF94rRFxQ',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

