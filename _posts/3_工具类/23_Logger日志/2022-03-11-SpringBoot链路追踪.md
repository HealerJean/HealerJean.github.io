---
title: SpringBoot链路追踪
date: 2022-03-11 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot链路追踪
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`MDC` 介绍

> `MDC`（Mapped Diagnostic Context，映射调试上下文）是 **`log4j`** 、**`logback`**及**`log4j2`** 提供的一种方便在多线程条件下记录日志的功能。**·MDC·** 可以看成是一个**与当前线程绑定的哈希表**，可以往其中添加键值对。`MDC` 中包含的内容可以**被同一线程中执行的代码所访问**。         
>
> 普通的使用方法在`Log`那篇文章中有，下面主要还是进攻



# 2、子线程日志打印丢失 `traceId`

> 子线程在打印日志的过程中 `traceId` 将丢失，解决方式为重写线程池，对于直接`new`创建线程的情况不考略【实际应用中应该避免这种用法】，重写线程池无非是对任务进行一次封装。





## 2.1、`MDC` 封装线程工具

```java
public class ThreadMdcUtil {

    public static final String TRACE_ID = "REQ_UID";
    public static final String SON_ID = "SON_UID";

    public static void setTraceIdIfAbsent() {
        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
        }

        if (MDC.get(SON_ID) == null) {
            MDC.put(SON_ID, UUID.randomUUID().toString().replace("-", ""));
        }
    }

    /**
     * 封装线程任务，在执行的时候放入MDC，执行结束删除MDC
     * @param callable callable 线程任务
     * @param context MDC属性
     * @param <T>
     * @return
     */
    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }


    /**
     * 封装线程任务，在执行的时候放入MDC，执行结束删除MDC
     * @param runnable Runnable 线程任务
     * @param context MDC属性
     * @return
     */
    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            // 断当前线程对应MDC的Map是否存在，存在则设置
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}

```



## 2.2、线程池`wrapper`

```java
public class ThreadPoolExecutorMdcWrapper extends ThreadPoolExecutor {

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ThreadPoolExecutorMdcWrapper(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public void execute(Runnable task) {
        super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()), result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
    }
}
```

## 2.3、`log4j2.xml` 配置

```
%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level -[%-32X{REQ_UID}] %-32X{SON_UID} - %msg%xEx %logger{36}.%M[%L]%n
```



## 2.4、测试

```java
@Slf4j
public class TestMain2_TraceId {


  public static final String TRACE_ID = "REQ_UID";

  @Test
  public void threadpool() throws InterruptedException {
    MDC.put(TRACE_ID, UUID.randomUUID().toString().replace("-", ""));
    log.info("[TestMain2_TraceId#threadpool] 主线程开始");
    
    ThreadPoolExecutorMdcWrapper threadPoolExecutorMdcWrapper = new ThreadPoolExecutorMdcWrapper(
      10, 300, 30000, TimeUnit.MILLISECONDS,  new LinkedBlockingQueue<>(10));
    threadPoolExecutorMdcWrapper.submit(()->{
      log.info("[TestMain2_TraceId#threadpool] 线程池任务1 start ");

      log.info("[TestMain2_TraceId#test] 线程池任务1 end  ");

    });

    threadPoolExecutorMdcWrapper.submit(()->{
      log.info("[TestMain2_TraceId#threadpool] 线程池任务2 start ");

      log.info("[TestMain2_TraceId#test] 线程池任务2 end  ");

    });

    log.info("[TestMain2_TraceId#threadpool] 主线程结束");

    Thread.sleep(3000);

    MDC.remove(TRACE_ID);

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
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'B2jsPbCghUKlmzwM',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



