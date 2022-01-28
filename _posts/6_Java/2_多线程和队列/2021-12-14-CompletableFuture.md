---
title: CompletableFuture
date: 2021-12-14 00:00:00
tags: 
- Thread
category: 
- Thread
description: CompletableFuture
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`CompletableFuture.runAsync`

```java
/**
 * 1、无返回值的异步任务  CompletableFuture.runAsync
 */
@Test
public void test1() {
  ExecutorService service = Executors.newFixedThreadPool(10);

  //1.无返回值的异步任务 runAsync()
  CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
    log.info("currentThread:{}", Thread.currentThread().getId());
  }, service);

}

```



# 2、`CompletableFuture.supplyAsync`

## 2.1、`whenComplete`

> ⬤ 第一个参数是结果      
>
> ⬤ 第二个参数是异常，他可以感知异常，无法返回默认数据
>
> >**只会存在一个，无法返回默认数据**

## 2.2、`exceptionally`

> 只有一个参数是异常类型，他可以感知异常，同时返回默认数据

## 2.3、`handle`

> 既可以感知异常，也可以返回默认数据，是whenComplete和exceptionally的结合



```java
/**
 * 2、有返回值异步任务 CompletableFuture.supplyAsync
 */
@Test
public void test2() {
    ExecutorService service = Executors.newFixedThreadPool(10);

    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
        log.info("currentThread:{}", Thread.currentThread().getId());
        int i = 1/0;
        return "HealerJean";
    }, service);


    // 1、whenComplete
    // 第一个参数是结果
    // 第二个参数是异常，他可以感知异常，无法返回默认数据
    completableFuture.whenComplete((r, e) -> {
        log.info("[completableFuture.whenComplete] result:{} ", r, e);
    });

    // 2、exceptionally
    // 只有一个参数是异常类型，他可以感知异常，同时返回默认数据
    completableFuture .exceptionally(e -> {
        log.error("[completableFuture.whenComplete] error" , e);
        return "exceptionally";
    });

    // 3、handler
    // 既可以感知异常，也可以返回默认数据，是whenComplete和exceptionally的结合
    completableFuture.handle((r, e) -> {
        if (r != null) {
            log.error("[completableFuture.handle] ", r);
            return r;
        }
        if (e != null) {
            log.error("[completableFuture.handle] error", r);
            return "error";
        }
        return "";
    });

    try {
        Thread.sleep(10000L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}



16:45:56.769 [pool-1-thread-1] INFO com.hlj.threadpool.d04_多接口返回.TestMain - currentThread:11


  
16:45:56.769 [main] INFO com.hlj.threadpool.d04_多接口返回.TestMain - ==============
16:45:56.777 [main] INFO com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.whenComplete] result:null 
java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
	at java.util.concurrent.CompletableFuture.encodeThrowable(CompletableFuture.java:273)
	at java.util.concurrent.CompletableFuture.completeThrowable(CompletableFuture.java:280)
	at java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1606)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.lang.ArithmeticException: / by zero
	at com.hlj.threadpool.d04_多接口返回.TestMain.lambda$test2$1(TestMain.java:42)
	at java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1604)
	... 3 common frames omitted
  
  
16:45:56.777 [main] INFO com.hlj.threadpool.d04_多接口返回.TestMain - ==============
16:45:56.778 [main] ERROR com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.exceptionally] error
java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
	at java.util.concurrent.CompletableFuture.encodeThrowable(CompletableFuture.java:273)
	at java.util.concurrent.CompletableFuture.completeThrowable(CompletableFuture.java:280)
	at java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1606)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.lang.ArithmeticException: / by zero
	at com.hlj.threadpool.d04_多接口返回.TestMain.lambda$test2$1(TestMain.java:42)
	at java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1604)
	... 3 common frames omitted
  
  
  
16:45:56.779 [main] INFO com.hlj.threadpool.d04_多接口返回.TestMain - ==============
16:45:56.779 [main] ERROR com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.handle] error

```



# 3、线程串行化

## 3.1、`thenRunAsync()`  异步，无入参，无返回值

> `thenRunAsync()`  无入参，无返回值 、线程池执行

## 3.2、`thenAccept() 同步  `有入参，无返回值

> `thenAccept() ` 有入参，无返回值，不传线程池 （和 `CompletableFuture.supplyAsync ` 用的一个线程池，非异步）

## 3.2、`thenApply() `  同步 有入参，无返回值

> `thenApply() ` 有入参，有返回值，不传线程池 （`Main`线程）

```java
/**
 * 3.1、线程串行化
 */
@Test
public void test() throws ExecutionException, InterruptedException {
          log.info("[test]  currentThread:{}", Thread.currentThread().getId());
    long start = System.currentTimeMillis();
    ExecutorService service = Executors.newFixedThreadPool(10);
    CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] currentThread:{}", Thread.currentThread().getId());

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "HealerJean" ;
    }, service);

    // 1、thenRunAsync() 无入参，无返回值 、线程池执行
    completableFuture.thenRunAsync(() -> {
        long cost  = System.currentTimeMillis() - start;
        log.info("[completableFuture.thenRunAsync] currentThread:{}, cost:{}", Thread.currentThread().getId(), cost);
    }, service);


    // 2、thenAccept() 有入参，无返回值，不传线程池 （和CompletableFuture.supplyAsync 用的一个线程池，非异步）
    completableFuture.thenAccept((r) -> {
        long cost = System.currentTimeMillis() - start;
        log.info("[completableFuture.thenAccept]  currentThread:{}, result:{}, cost:{}", Thread.currentThread().getId(), r, cost);
    });


    String result = completableFuture.get();
    log.info("[stringCompletableFuture.get()]  result:{}", result);

    // 3、thenApply() 有入参，有返回值，不传线程池 main线程
    CompletableFuture<String> stringCompletableFuture = completableFuture.thenApply((r) -> {
        long cost = System.currentTimeMillis() - start;
        log.info("[completableFuture.thenApply]  currentThread:{}, result:{}, cost:{}", Thread.currentThread().getId(), r, cost);
        return "thenAccept";
    });
    result = stringCompletableFuture.get();
    log.info("[stringCompletableFuture.get()]  result:{}", result);

    try {
        Thread.sleep(10000L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

```

```
[completableFuture.supplyAsync] currentThread:11
[completableFuture.thenAccept]  currentThread:11, result:HealerJean, cost:3089

[stringCompletableFuture.get()]  result:HealerJean

[completableFuture.thenRunAsync] currentThread:13, cost:3090
[completableFuture.thenApply]  currentThread:1, result:HealerJean, cost:3091
[stringCompletableFuture.get()]  result:thenAccept

Process finished with exit code 0
```





# 4、异步任务

## 4.1、两个异步任务都完成，第三个任务才开始执行

```java
/**
 * 4、异步，两任务组合 ：两个异步任务都完成，第三个任务才开始执行
 */
@Test
public void test4(){
    ExecutorService service = Executors.newFixedThreadPool(10);
    //定义两个任务
    //任务一
    CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
        return "task_1";
    }, service);

    //任务二
    CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
        return "task_2";
    }, service);


    // 1、runAfterBothAsync：无传入值、无返回值
    task1.runAfterBothAsync(task2, () -> {
        log.info("[completableFuture.runAfterBothAsync] task3 currentThread:{}", Thread.currentThread().getId());
    }, service);

    // 2、thenAcceptBothAsync：有传入值、无返回值
    task1.thenAcceptBothAsync(task2, (x, y) -> {
        log.info("[completableFuture.thenAcceptBothAsync] task3 currentThread:{}, result1:{}, result2:{}", Thread.currentThread().getId(), x, y);
    }, service);

    // 2、thenCombineAsync：有传入值、有返回值
    task1.thenCombineAsync(task2, (x, y) -> {
        log.info("[completableFuture.thenCombineAsync] task3 currentThread:{}, result1:{}, result2:{}", Thread.currentThread().getId(), x, y);
        return "task3";
    }, service);


    try {
        Thread.sleep(10000L);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

```

```log
21:43:27.652 [pool-1-thread-1] INFO com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.supplyAsync] task1 currentThread:11
21:43:27.652 [pool-1-thread-2] INFO com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.supplyAsync] task2 currentThread:12
21:43:27.657 [pool-1-thread-3] INFO com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.thenCombineAsync] task3 currentThread:13, result1:task_1, result2:task_2
21:43:27.657 [pool-1-thread-4] INFO com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.thenAcceptBothAsync] task3 currentThread:14, result1:task_1, result2:task_2
21:43:27.657 [pool-1-thread-5] INFO com.hlj.threadpool.d04_多接口返回.TestMain - [completableFuture.runAfterBothAsync] task3 currentThread:15
```



# 5、等待所有任务结束

> 循环使用 `get` 方法等待任务结束耗时比 `allOf`  方法多

```java

/**
 * 5、等待所有任务都结束
 */
@Test
public void test5() throws ExecutionException, InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(10);
    long start = System.currentTimeMillis();
    CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task_1";
    }, service);
    CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task_2";
    }, service);

    CompletableFuture<String> task3= CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task3 currentThread:{}", Thread.currentThread().getId());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task_3";
    }, service);


      // 第一种方式：CompletableFuture.allOf
       // CompletableFuture[] completableFutures = new CompletableFuture[]{task1,task2, task3};
        // CompletableFuture.allOf(completableFutures).join();
        // Long cost = System.currentTimeMillis() - start;
        // log.info("[test] task finish cost:{}", cost);

    // 第二种方式：completableFuture.get()
    for (CompletableFuture completableFuture : completableFutures){
        Object result = completableFuture.get();
        Long cost = System.currentTimeMillis() - start;
        log.info("[task]  cost:{},  result:{}", cost, result);
    }
}

```

# 6、输出最先完成的任务的结果

```java
/**
 * 一个任务完成则结束
 */
@Test
public void test6(){
    ExecutorService service = Executors.newFixedThreadPool(10);
    long start = System.currentTimeMillis();
    CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task_1";
    }, service);
    CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task_2";
    }, service);

    CompletableFuture<String> task3= CompletableFuture.supplyAsync(() -> {
        log.info("[completableFuture.supplyAsync] task3 currentThread:{}", Thread.currentThread().getId());
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "task_3";
    }, service);


    CompletableFuture[] completableFutures = new CompletableFuture[]{task1,task2, task3};
    Object result = CompletableFuture.anyOf(completableFutures).join();
    Long cost = System.currentTimeMillis() - start;
    log.info("[test] task finish cost:{}, result:{}", cost, result);
}
```

# 7、异步超时设置

> `java8` 中 `CompletableFuture` 异步处理超时的方法     
>
> ⬤ `Java` 8 的 `CompletableFuture` 并没有 `timeout` 机制，虽然可以在 `get` 的时候指定 `timeout`，是一个同步堵塞的操作。怎样让 `timeout` 也是异步的呢？`Java` 8 内有内建的机制支持，一般的实现方案是启动一个 `ScheduledThreadpoolExecutor` 线程在 `timeout` 时间后直接调用 `CompletableFuture`.`completeExceptionally`(`new` `TimeoutException`())，然后用`acceptEither() `或者 `applyToEither` 看是先计算完成还是先超时：    
>
> ⬤ 在 `java` 9 引入了 `orTimeout` 和 `completeOnTimeOut` 两个方法支持 异步 `timeout` 机制：内部实现上跟我们上面的实现方案是一模一样的，只是现在不需要自己实现了。



## 7.1、`CompletableFutureTimeout`

> 注意：异常会被吃掉，返回超时的默认值。我就猜想，如果代码无法保证，既然有超时这种默认值，那么本身的业务异常应该毫无意义

```java

import java.util.concurrent.*;
import java.util.function.Function;

public class CompletableFutureTimeout {
  static final class Delayer {
    static ScheduledFuture<?> delay(Runnable command, long delay,
                                    TimeUnit unit) {
      return delayer.schedule(command, delay, unit);
    }

    static final class DaemonThreadFactory implements ThreadFactory {
      @Override
      public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("CompletableFutureDelayScheduler");
        return t;
      }
    }

    static final ScheduledThreadPoolExecutor delayer;

    // 注意，这里使用一个线程就可以搞定 因为这个线程并不真的执行请求 而是仅仅抛出一个异常
    static {
      (delayer = new ScheduledThreadPoolExecutor(
        1, new CompletableFutureTimeout.Delayer.DaemonThreadFactory())).
        setRemoveOnCancelPolicy(true);
    }
  }


  /**
     * 哪个先完成 就apply哪一个结果 这是一个关键的API,exceptionally出现异常后返回默认值
     */
  public static  <T> CompletableFuture<T> completeOnTimeout( CompletableFuture<T> future,T t, long timeout, TimeUnit unit) {
    final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
    return future.applyToEither(timeoutFuture, Function.identity()).exceptionally((throwable) -> t);
  }



  private static <T> CompletableFuture<T> timeoutAfter(long timeout, TimeUnit unit) {
    CompletableFuture<T> result = new CompletableFuture<T>();
    CompletableFutureTimeout.Delayer.delayer.schedule(() -> result.completeExceptionally(new TimeoutException()), timeout, unit);
    return result;
  }

}
```

## 7.2、测试

```java
 @Test
    public void test() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            log.info("task1 start ");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task1 end ");
            return "task1 success";
        });

        CompletableFuture<Integer> task2 = CompletableFuture.supplyAsync(() -> {
            log.info("task2 start ");

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task2 end ");
            return 100;
        });
        CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
            log.info("task3 start ");

            try {
                Thread.sleep(3500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("task3 end ");
            return "task3 success";
        });

        CompletableFuture<String> completableFuture1 = CompletableFutureTimeout.completeOnTimeout(task1, "InterTimeOut", 2, TimeUnit.SECONDS);
        CompletableFuture<Integer> completableFuture2 = CompletableFutureTimeout.completeOnTimeout(task2, 0, 2, TimeUnit.SECONDS);
        CompletableFuture<String> completableFuture3 = CompletableFutureTimeout.completeOnTimeout(task3, "InterTimeOut", 2, TimeUnit.SECONDS);

        String result1 = completableFuture1.get();
        Integer result2 = completableFuture2.get();
        String result3 = completableFuture3.get();
        Long cost = System.currentTimeMillis() - start;
        log.info("result1: {}, cost:{}", result1, cost);
        log.info("result2: {}, cost:{}", result2, cost);
        log.info("result3: {}, cost:{}", result3, cost);
        Thread.sleep(500000L);
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
		id: 'Cleskowqab1HR97X',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



