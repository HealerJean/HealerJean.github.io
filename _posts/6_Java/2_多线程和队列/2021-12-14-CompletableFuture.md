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

> 既可以感知异常，也可以返回默认数据，是 `whenComplete` 和 `exceptionally`的结合

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

## 3.2、`thenAccept()`同步  有入参，无返回值

> `thenAccept() ` 有入参，无返回值，不传线程池 

## 3.2、`thenApply() `  同步 有入参，有返回值

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


    // 2、thenAccept() 有入参，无返回值，不传线程池 
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



## 4.2、当一个任务失败后迅速返回

```java
@Test
public void test4_3() {
  ExecutorService service = Executors.newFixedThreadPool(10);
  long start = System.currentTimeMillis();
  CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
    try {
      int i = 1 / 0;
      Thread.sleep(100L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("[completableFuture.supplyAsync] task1 currentThread:{}", Thread.currentThread().getId());
    return "task_1";
  }, service);
  CompletableFuture<String> task2 = CompletableFuture.supplyAsync(() -> {
    try {
      Thread.sleep(500L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("[completableFuture.supplyAsync] task2 currentThread:{}", Thread.currentThread().getId());
    return "task_2";
  }, service);

  CompletableFuture<String> task3 = CompletableFuture.supplyAsync(() -> {
    try {
      Thread.sleep(2000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("[completableFuture.supplyAsync] task3 currentThread:{}", Thread.currentThread().getId());
    return "task_3";
  }, service);


  CompletableFuture[] completableFutures = new CompletableFuture[]{task1, task2, task3};
  try {
    CompletableFuture.allOf(completableFutures).join();
  } catch (Exception e) {
    log.info("[CompletableFuture.allOf] error", e);
  }
  Long cost = System.currentTimeMillis() - start;
  log.info("[test] task finish cost:{}", cost);

}
```



## 4.3、等待所有任务结束

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

## 4.4、输出最先完成的任务的结果

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

# 5、异步超时

## 5.1、`CompletableFutureTimeout`

> `java8` 中 `CompletableFuture` 异步处理超时的方法     
>
> ⬤ `Java` 8 的 `CompletableFuture` 并没有 `timeout` 机制，虽然可以在 `get` 的时候指定 `timeout`，是一个同步堵塞的操作。怎样让 `timeout` 也是异步的呢？`Java` 8 内有内建的机制支持，一般的实现方案是启动一个 `ScheduledThreadpoolExecutor` 线程在 `timeout` 时间后直接调用 `CompletableFuture`.`completeExceptionally`(`new` `TimeoutException`())，然后用`acceptEither() `或者 `applyToEither` 看是先计算完成还是先超时：    
>
> ⬤ 在 `java` 9 引入了 `orTimeout` 和 `completeOnTimeOut` 两个方法支持 异步 `timeout` 机制：内部实现上跟我们上面的实现方案是一模一样的，只是现在不需要自己实现了。



### 5.1.1、`CompletableFutureTimeout`

> **注意：异常会被吃掉，返回超时的默认值。我就猜想，如果代码无法保证，既然有超时这种默认值，那么本身的业务异常应该毫无意义**

```java
@Slf4j
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

  static final class FutureTimeOutException extends RuntimeException {

      public FutureTimeOutException() {
      }
  }

  static class ExceptionUtils {
      public static Throwable extractRealException(Throwable throwable) {
          //这里判断异常类型是否为CompletionException、ExecutionException，如果是则进行提取，否则直接返回。
          if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
              if (throwable.getCause() != null) {
                  return throwable.getCause();
              }
          }
          return throwable;
      }
  }

  /**
   * 哪个先完成 就apply哪一个结果 这是一个关键的API,exceptionally出现异常后返回默认值
   * @param future future
   * @param t 异常返回默认结果
   * @param to 超时返回默认结果
   * @param timeout 超时时间
   * @param unit 时间单位
   * @return
   * @param <T>
   */
  public static  <T> CompletableFuture<T> completeOnTimeout( CompletableFuture<T> future,T t, T to, long timeout, TimeUnit unit) {
      final CompletableFuture<T> timeoutFuture = timeoutAfter(timeout, unit);
      return future.applyToEither(timeoutFuture, Function.identity()).exceptionally(throwable -> {

          if (ExceptionUtils.extractRealException(throwable) instanceof FutureTimeOutException){
              return to;
          }
          return t;
      });
  }


  private static <T> CompletableFuture<T> timeoutAfter(long timeout, TimeUnit unit) {
      CompletableFuture<T> result = new CompletableFuture<T>();
      CompletableFutureTimeout.Delayer.delayer.schedule(() -> result.completeExceptionally(new FutureTimeOutException()), timeout, unit);
      return result;
  }


}
```

### 5.1.2、测试

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

    int i = 1/0;
    try {
      Thread.sleep(3500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    log.info("task3 end ");
    return "task3 success";
  });

  CompletableFuture<String> completableFuture1 = CompletableFutureTimeout.completeOnTimeout(task1, "Exception", "TimeOutException", 2, TimeUnit.SECONDS);
  CompletableFuture<Integer> completableFuture2 = CompletableFutureTimeout.completeOnTimeout(task2, 0, -1,1, TimeUnit.SECONDS);
  CompletableFuture<String> completableFuture3 = CompletableFutureTimeout.completeOnTimeout(task3, "Exception","TimeOutException", 2, TimeUnit.SECONDS);

  String result1 = completableFuture1.get();
  Integer result2 = completableFuture2.get();
  String result3 = completableFuture3.get();
  Long cost = System.currentTimeMillis() - start;
  log.info("result1: {}, cost:{}", result1, cost);
  log.info("result2: {}, cost:{}", result2, cost);
  log.info("result3: {}, cost:{}", result3, cost);
  Thread.sleep(500000L);
}


20:36:13.418 [ForkJoinPool.commonPool-worker-11] INFO com.healerjean.proj.d04_多接口返回.TestMain - task3 start 
20:36:13.418 [ForkJoinPool.commonPool-worker-2] INFO com.healerjean.proj.d04_多接口返回.TestMain - task2 start 
20:36:13.418 [ForkJoinPool.commonPool-worker-9] INFO com.healerjean.proj.d04_多接口返回.TestMain - task1 start 
20:36:13.437 [ForkJoinPool.commonPool-worker-9] INFO com.healerjean.proj.d04_多接口返回.TestMain - task1 end 
20:36:14.431 [main] INFO com.healerjean.proj.d04_多接口返回.TestMain - result1: TimeOutException, cost:1121
20:36:14.435 [main] INFO com.healerjean.proj.d04_多接口返回.TestMain - result2: -1, cost:1121
20:36:14.435 [main] INFO com.healerjean.proj.d04_多接口返回.TestMain - result3: Exception, cost:1121
20:36:16.429 [ForkJoinPool.commonPool-worker-2] INFO com.healerjean.proj.d04_多接口返回.TestMain - task2 end 

```



## 5.2、`completableFuture`

```java
@Test
  public void testTimeOut2() {
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


      CompletableFuture<String>[] allCheckFuture = new CompletableFuture[]{
              task1,
              task3};

      ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolTaskExecutor();
      Map<Integer, String> map = new ConcurrentHashMap<>();
      CompletableFuture<Void> completableFuture = 
        CompletableFuture.runAsync(() -> allCheckFutureResult(map, 
                                                              allCheckFuture), 
                                   threadPoolTaskExecutor);
      int timeOut = 50;
      try {
          completableFuture.get(timeOut, TimeUnit.MILLISECONDS);
      } catch (TimeoutException e) {
          log.info("任务超时");
      } catch (Exception e) {
          log.error("失败", e);
      }
      log.info("map:{}", map);
  }


20:53:23.930 [main] INFO com.healerjean.proj.d04_多接口返回.TestMain - Executor - threadPoolTaskExecutor injected!
20:53:23.984 [main] INFO com.healerjean.proj.d04_多接口返回.TestMain - 任务超时
20:53:23.984 [main] INFO com.healerjean.proj.d04_多接口返回.TestMain - map:{0=task1 success}
```





```java
/**
 * 试算超时
 *
 * @param allCheckFuture allCheckFuture
 * @return MarginCheckBO
 */
public MarginCheckContextBO trialAllCheckFutureResult(CompletableFuture<MarginCheckContextBO>[] allCheckFuture) {
    MarginCheckContextBO marginCheck = new MarginCheckContextBO();
    CompletableFuture<MarginEnum.InsuredFailEnum> insureCheckFuture = CompletableFuture.supplyAsync(() -> allCheckFutureResult(allCheckFuture), threadPoolTaskExecutor);
    int timeOut = 3;
    try {
        MarginEnum.InsuredFailEnum insuredFailEnum = insureCheckFuture.get(timeOut, TimeUnit.SECONDS);
        marginCheck.setInsuredFailEnum(insuredFailEnum);
    } catch (TimeoutException e) {
        log.warn("[MarginService#premiumTrial] 商家试算验超时3s，直接算费");
        marginCheck.setTrialTimeOut(Boolean.TRUE);
        marginCheck.setInsuredFailEnum(MarginEnum.InsuredFailEnum.SUCCESSFUL);
    } catch (Exception e) {
        log.error("[MarginService#premiumTrial] 试算失败", e);
        throw new AppRunException(e.getMessage());
    }
    return marginCheck;

}
```



```java
    /**
     * 获取线程池结果
     *
     * @param allCheckFuture allCheckFuture
     * @return MarginEnum.InsuredFailEnum
     */
    public MarginEnum.InsuredFailEnum allCheckFutureResult(CompletableFuture<MarginCheckContextBO>[] allCheckFuture) {
        for (CompletableFuture<MarginCheckContextBO> completableFuture : allCheckFuture) {
            if (Objects.isNull(completableFuture)) {
                continue;
            }
            try {
                MarginCheckContextBO marginCheckContextBo = completableFuture.get();
                if (MarginEnum.InsuredFailEnum.SUCCESSFUL != marginCheckContextBo.getInsuredFailEnum()) {
                    return marginCheckContextBo.getInsuredFailEnum();
                }
            } catch (Exception e) {
                log.error("[MarginService#vendorInsureCheck] ERROR", e);
                throw new AppRunException(e.getMessage());
            }
        }
        return MarginEnum.InsuredFailEnum.SUCCESSFUL;
    }
```







# 6、原理

转子：https://mp.weixin.qq.com/s/GQGidprakfticYnbVYVYGQ

## 6.1、`CompletableFuture` 的背景和定义加载

### 6.1.1、`CompletableFuture` 解决的问题

> `CompletableFuture` 是由 `Java 8` 引入的，在 `Java8` 之前我们一般通过 `Future` 实现异步。      
>
> ⬤ `Future` 用于表示异步计算的结果，只能通过阻塞或者轮询的方式获取结果，而且不支持设置回调方法，Java 8之前若要设置回调一般会使用`guava` 的 `ListenableFuture`，回调的引入又会导致臭名昭著的回调地狱（下面的例子会通过ListenableFuture的使用来具体进行展示）。            
>
> ⬤  `CompletableFuture` 对 `Future` 进行了扩展，可以通过设置回调的方式处理计算结果，同时也支持组合操作，支持进一步的编排，同时一定程度解决了回调地狱的问题。        

下面将举例来说明，我们通过 `ListenableFuture`、`CompletableFuture`来实现异步的差异。假设有三个操作 `step1`、`step2`、`step3`存在依赖关系，其中 `step3` 的执行依赖step1和step2的结果。

#### 6.1.1.1、`Future` ( `ListenableFuture` )：

```java
ExecutorService executor = Executors.newFixedThreadPool(5);
ListeningExecutorService guavaExecutor = MoreExecutors.listeningDecorator(executor);
ListenableFuture<String> future1 = guavaExecutor.submit(() -> {
  //step 1
  System.out.println("执行step 1");
  return "step1 result";
});
ListenableFuture<String> future2 = guavaExecutor.submit(() -> {
  //step 2
  System.out.println("执行step 2");
  return "step2 result";
});
ListenableFuture<List<String>> future1And2 = Futures.allAsList(future1, future2);
Futures.addCallback(future1And2, new FutureCallback<List<String>>() {
  @Override
  public void onSuccess(List<String> result) {
    System.out.println(result);
    ListenableFuture<String> future3 = guavaExecutor.submit(() -> {
      System.out.println("执行step 3");
      return "step3 result";
    });
    Futures.addCallback(future3, new FutureCallback<String>() {
      @Override
      public void onSuccess(String result) {
        System.out.println(result);
      }        
      @Override
      public void onFailure(Throwable t) {
      }
    }, guavaExecutor);
  }

  @Override
  public void onFailure(Throwable t) {
  }}, guavaExecutor);
```

#### 6.1.1.2、`CompletableFuture` 的实现如下：

> 显然，`CompletableFuture` 的实现更为简洁，可读性更好。

```java
ExecutorService executor = Executors.newFixedThreadPool(5);
CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
  System.out.println("执行step 1");
  return "step1 result";
}, executor);
CompletableFuture<String> cf2 = CompletableFuture.supplyAsync(() -> {
  System.out.println("执行step 2");
  return "step2 result";
});
cf1.thenCombine(cf2, (result1, result2) -> {
  System.out.println(result1 + " , " + result2);
  System.out.println("执行step 3");
  return "step3 result";
}).thenAccept(result3 -> System.out.println(result3));
```



## 6.2、`CompletableFuture` 的定义

> `CompletableFuture` 实现了两个接口（如上图所示）：``Future`、`CompletionStage`。
>
> ⬤ `Future ` 表示异步计算的结果，    
>
> ⬤ `CompletionStage` 用于表示异步执行过程中的一个步骤（`Stage`），这个步骤可能是由另外一个`CompletionStage`触发的，随着当前步骤的完成，也可能会触发其他一系列`CompletionStage`的执行。从而我们可以根据实际业务对这些步骤进行多样化的编排组合，`CompletionStage` 接口正是定义了这样的能力，我们可以通过其提供的 `thenAppy`、`thenCompose` 等函数式编程方法来组合编排这些步骤。



## 6.3、`CompletableFuture` 的使用

> 下面我们通过一个例子来讲解 `CompletableFuture` 如何使用，使用 `CompletableFuture` 也是构建依赖树的过程。一个`CompletableFuture` 的完成会触发另外一系列依赖它的 `CompletableFuture` 的执行：

如图所示，这里描绘的是一个业务接口的流程，其中包括 `CF1` \ `CF2` \ `CF3` \ `CF4` \ `CF5` 共5个步骤，并描绘了这些步骤之间的依赖关系，每个步骤可以是一次 `RPC` 调用、一次数据库操作或者是一次本地方法调用等，在使用 `CompletableFuture` 进行异步化编程时，图中的每个步骤都会产生一个 `CompletableFuture` 对象，最终结果也会用一个 `CompletableFuture` 来进行表示。

根据 `CompletableFuture`依赖数量，可以分为以下几类：零依赖、一元依赖、二元依赖和多元依赖。

![image-20220525174132283](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525174132283.png)

### 6.3.1、零依赖：`CompletableFuture` 的创建

> 我们先看下如何不依赖其他 `CompletableFuture` 来创建新的 `CompletableFuture`：

![image-20220525180028811](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525180028811.png)

如上图红色链路所示，接口接收到请求后，首先发起两个异步调用 `CF1`、`CF2`，主要有三种方式：

```java
ExecutorService executor = Executors.newFixedThreadPool(5);
//1、使用runAsync或supplyAsync发起异步调用
CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
  return "result1";
}, executor);

//2、CompletableFuture.completedFuture()直接创建一个已完成状态的CompletableFuture
CompletableFuture<String> cf2 = CompletableFuture.completedFuture("result2");

//3、先初始化一个未完成的CompletableFuture，然后通过complete()、completeExceptionally()，完成该CompletableFuture
CompletableFuture<String> cf = new CompletableFuture<>();
cf.complete("success");


```

### 6.3.2、一元依赖：依赖一个CF

> `CF3`，`CF5` 分别依赖于 `CF1` 和 `CF2` ，这种对于单个 `CompletableFuture` 的依赖可以通过 `thenApply`、`thenAccept`、`thenCompose`等方法来实现，代码如下所示：

![image-20220525180824745](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525180824745.png)

```java
CompletableFuture<String> cf3 = cf1.thenApply(result1 -> {
  //result1为CF1的结果
  //......
  return "result3";
});
CompletableFuture<String> cf5 = cf2.thenApply(result2 -> {
  //result2为CF2的结果
  //......
  return "result5";
});
```



### 6.3.3、二元依赖：依赖两个 `CF`

> `CF4 ` 同时依赖于两个 `CF1` 和 `CF2`，这种二元依赖可以通过 `thenCombine` 等回调来实现，如下代码所示：
>
> 

![image-20220525181050702](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525181050702.png)



```java
CompletableFuture<String> cf4 = cf1.thenCombine(cf2, (result1, result2) -> {
  //result1和result2分别为cf1和cf2的结果
  return "result4";
});
```

### 6.3.4、多元依赖：依赖多个 `CF`

> 如上图红色链路所示，整个流程的结束依赖于三个步骤CF3、CF4、CF5，这种多元依赖可以通过`allOf`或`anyOf`方法来实现，     
>
> ⬤ 多个依赖全部完成时使用 `allOf`       
>
> ⬤  当多个依赖中的任意一个完成即可时使用 `anyOf`

![image-20220525181209749](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525181209749.png)

```java
CompletableFuture<Void> cf6 = CompletableFuture.allOf(cf3, cf4, cf5);

CompletableFuture<String> result = cf6.thenApply(v -> {
  //这里的join并不会阻塞，因为传给thenApply的函数是在CF3、CF4、CF5全部完成时，才会执行 。
  result3 = cf3.join();
  result4 = cf4.join();
  result5 = cf5.join();
  //根据result3、result4、result5组装最终result;
  return "result";
});
```



## 6.4、`CompletbleFuture`  原理

> `CompletableFuture` 中包含两个字段：**`result`** 和 **`stack`**。 `result` 用于存储当前 `CF` 的结果，`stack`（`Completion`）表示当前`CF`完成后需要触发的依赖动作（`Dependency` `Actions`），去触发依赖它的 `CF` 的计算，依赖动作可以有多个（表示有多个依赖它的 `CF`），以栈[`Treiber stack`]的形式存储，`stack` 表示栈顶元素。



![image-20220525181624406](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525181624406.png)



这种方式类似“观察者模式”，依赖动作（`Dependency Action`）都封装在一个单独 `Completion`子类中。下面是 `Completion` 类关系结构图。`CompletableFuture`中的每个方法都对应了图中的一个 `Completion` 的子类，`Completion` 本身是**观察者**的基类。

- `UniCompletion` 继承了 `Completion`，是一元依赖的基类，例如 `thenApply` 的实现类 `UniApply`就继承自 `UniCompletion`。
- `BiCompletion` 继承了 `UniCompletion`，是二元依赖的基类，同时也是多元依赖的基类。例如 `thenCombine`的实现类 `BiRelay`就继承自`BiCompletion`。

### 6.4.1、设计思想

> 按照类似“观察者模式”的设计思想，原理分析可以从“观察者”和“被观察者”两个方面着手。由于回调种类多，但结构差异不大，所以这里单以一元依赖中的 `thenApply` 为例，不再枚举全部回调类型。如下图所示：

![image-20220525182352908](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525182352908.png)

### 6.4.1、被观察者

1、每个 `CompletableFuture`· 都可以被看作一个被观察者，其内部有一个 `Completion`类型的链表成员变量 `stack`，用来存储注册到其中的所有观察者。当被观察者执行完成后会弹栈 `stack`属性，依次通知注册到其中的观察者。上面例子中步骤 `fn2`就是作为观察者被封装在 `UniApply` 中。      

3、被观察者 `CF` 中的 `result` 属性，用来存储返回结果数据。这里可能是一次 `RPC` 调用的返回值，也可能是任意对象，在上面的例子中对应步骤 `fn1` 的执行结果。

 

### 6.4.2、观察者

> `CompletableFuture` 支持很多回调方法，例如 `thenAccept`、`thenApply`、`exceptionally` 等，这些方法接收一个函数类型的参数f，生成一个`Completion`类型的对象（即观察者），并将入参函数f赋值给 `Completion` 的成员变量 `fn`，然后检查当前CF是否已处于完成状态（即result != null），如果已完成直接触发 `fn`，否则将观察者 `Completion`加入到 `CF` 的观察者链 `stack`中，再次尝试触发，如果被观察者未执行完则其执行完毕之后通知触发。

1、观察者中的 `dep` 属性：指向其对应的 `CompletableFuture`，在上面的例子中 `dep` 指向 `CF2`。     

2、观察者中的 `src` 属性：指向其依赖的 `CompletableFuture`，在上面的例子中 `src` 指向 `CF1`。   观察者`Completion`中的 `fn` 属性：用来存储具体的等待被回调的函数。这里需要注意的是不同的回调方法（`thenAccept`、`thenApply`、`exceptionally`等）接收的函数类型也不同，即fn的类型有很多种，在上面的例子中fn指向fn2。



## 6.5、整体流程

### 6.5.1、一元依赖

这里仍然以 `thenApply` 为例来说明一元依赖的流程：

1、将观察者 `Completion` 注册到 `CF1`，此时 `CF1`将 `Completion` 压栈。        

2、当`CF1` 的操作运行完成时，会将结果赋值给 `CF1` 中的 `result` 属性。           

3、依次弹栈，通知观察者尝试运行。



![MP7YAkiaGu6dh4Qp](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/MP7YAkiaGu6dh4Qp-3474989.gif)



#### 5.6.1.1、FAQ

**Q1**：在观察者注册之前，如果 `CF` 已经执行完成，并且已经发出通知，那么这时观察者由于错过了通知是不是将永远不会被触发呢 ？     

**A1**：不会。在注册时检查依赖的CF是否已经完成。如果未完成（即 `result` == `null`）则将观察者入栈，如果已完成（`result != null`）则直接触发观察者操作。      



**Q2**：在”入栈“前会有”result == null“的判断，这两个操作为非原子操作，`CompletableFufure` 的实现也没有对两个操作进行加锁，完成时间在这两个操作之间，观察者仍然得不到通知，是不是仍然无法触发？         

**A2**：不会。入栈之后再次检查CF是否完成，如果完成则触发。         



**Q3**：当依赖多个CF时，观察者会被压入所有依赖的CF的栈中，每个CF完成的时候都会进行，那么会不会导致一个操作被多次执行呢 ？如下图所示，即当CF1、CF2同时完成时，如何避免CF3被多次触发。       

**A3**：`CompletableFuture` 的实现是这样解决该问题的：观察者在执行之前会先通过CAS操作设置一个状态位，将status由0改为1。如果观察者已经执行过了，那么CAS操作将会失败，取消执行。    

  

**通过对以上3个问题的分析可以看出，`CompletableFuture`在处理并行问题时，全程无加锁操作，极大地提高了程序的执行效率。我们将并行问题考虑纳入之后，可以得到完善的整体流程图如下所示：**



![pVG9zmUCWb8an7K4](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/pVG9zmUCWb8an7K4.gif)



### 6.5.2、二元依赖

> 我们以thenCombine为例来说明二元依赖：    
>
> `thenCombine` 操作表示依赖两个 `CompletableFuture`。其观察者实现类为 `BiApply`，如上图所示，`BiApply` 通过 `src` 和 `snd`两个属性关联被依赖的两个`CF`，`fn` 属性的类型为 `BiFunction`。与单个依赖不同的是，在依赖的`CF`未完成的情况下，`thenCombine`会尝试将 `BiApply`压入这两个被依赖的 `CF`的栈中，每个被依赖的 `CF` 完成时都会尝试触发观察者 `BiApply`，`BiApply`会检查两个依赖是否都完成，如果完成则开始执行。这里为了解决重复触发的问题，同样用的是上一章节提到的`CAS`操作，执行时会先通过`CAS`设置状态位，避免重复触发。

![image-20220525184716812](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220525184716812.png)

### 6.5.3、多元依赖

> 依赖多个 `CompletableFuture` 的回调方法包括 `allOf`、`anyOf`，区别在于 `allOf `观察者实现类为 `BiRelay`，需要所有被依赖的CF完成后才会执行回调；而 `anyOf`观察者实现类为 `OrRelay`，任意一个被依赖的 `C` F完成后就会触发。二者的实现方式都是将多个被依赖的 `C` F构建成一棵平衡二叉树，执行结果层层通知，直到根节点，触发回调监听。



# 7、线程阻塞的问题

## 7.1、代码执行在哪个线程上

> 要合理治理线程资源，最基本的前提条件就是要在写代码时，清楚地知道每一行代码都将执行在哪个线程上。下面我们看一下`CompletableFuture`的执行线程情况。     
>
> `CompletableFuture`  实现了 `CompletionStage`接口，通过丰富的回调方法，支持各种组合操作，每种组合场景都有同步和异步两种方法。

⬤ 同步方法（即不带 `Async`后缀的方法）有两种情况。     

1、如果注册时被依赖的操作已经执行完成，则直接由当前线程执行。      

2、如果注册时被依赖的操作还未执行完，则由回调线程执行。

⬤ 异步方法（即带 `Async` 后缀的方法）：可以选择是否传递线程池参数 `Executor`运行在指定线程池中；当不传递`Executor`时，会使用 `ForkJoinPool` 中的共用线程池 `CommonPool`（`CommonPool`的大小是 `CPU` 核数 - 1 ，如果是`IO`密集的应用，线程数可能成为瓶颈）。

```java
ExecutorService threadPool1 = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));
CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
    System.out.println("supplyAsync 执行线程：" + Thread.currentThread().getName());
    //业务操作
    return "";
}, threadPool1);
//1、此时，如果future1中的业务操作已经执行完毕并返回，则该thenApply直接由当前main线程执行；否则，将会由执行以上业务操作的threadPool1中的线程执行。
future1.thenApply(value -> {
    System.out.println("thenApply 执行线程：" + Thread.currentThread().getName());
    return value + "1";
});


//使用 ForkJoinPool中的共用线程池 CommonPool，但是一定要注意传线程池，具体看下面
future1.thenApplyAsync(value -> {
//do something
  return value + "1";
});


//使用指定线程池
future1.thenApplyAsync(value -> {
//do something
  return value + "1";
}, threadPool1);
```

## 7.2、线程池须知

### 7.2.1、 异步回调要传线程池

> 前面提到，异步回调方法可以选择是否传递线程池参数 `Executor`，这里我们建议**强制传线程池，且根据实际情况做线程池隔离**。
>
> 当不传递线程池时，会使用 `ForkJoinPool` 中的公共线程池 `CommonPool`，这里所有调用将共用该线程池，核心线程数=处理器数量-1（单核核心线程数为1），所有异步回调都会共用该 `CommonPool`，核心与非核心业务都竞争同一个池中的线程，很容易成为系统瓶颈。      
>
> **手动传递线程池参数可以更方便的调节参数，并且可以给不同的业务分配不同的线程池，以求资源隔离，减少不同业务之间的相互干扰。**



### 7.2.2、线程池循环引用会导致死锁

> `doGet` 方法第三行通过 `supplyAsync` 向 `threadPool1` 请求线程，并且内部子任务又向 `threadPool1`请求线程。`threadPool1`大小为 `10`，当同一时刻有 `10` 个请求到达，则 `threadPool1`被打满，子任务请求线程时进入阻塞队列排队，但是父任务的完成又依赖于子任务，这时由于子任务得不到线程，父任务无法完成。主线程执行 `cf1.join()` 进入阻塞状态，并且永远无法恢复。

为了修复该问题，需要将父任务与子任务做线程池隔离，两个任务请求不同的线程池，避免循环依赖导致的阻塞。

```java
public Object doGet() {
  ExecutorService threadPool1 = new ThreadPoolExecutor(10, 10, 0L,
                                                       TimeUnit.MILLISECONDS, 
                                                       new ArrayBlockingQueue<>(100));
  CompletableFuture cf1 = CompletableFuture.supplyAsync(() -> {
    //do sth
    return CompletableFuture.supplyAsync(() -> {
      System.out.println("child");
      return "child";
    }, threadPool1).join();//子任务
  }, threadPool1);
  return cf1.join();
}
```



### 7.2.3、异步 `RPC `调用注意不要阻塞IO线程池

服务异步化后很多步骤都会依赖于异步 `RPC` 调用的结果，这时需要特别注意一点，如果是使用基于NIO（比如 `Netty`）的异步 `RPC`，则返回结果是由 `IO` 线程负责设置的，即回调方法由 `IO` 线程触发，`CompletableFuture` 同步回调（如 `thenApply`、`thenAccept` 等无`Async`后缀的方法）如果依赖的异步 `RPC` 调用的返回结果，那么这些同步回调将运行在 `IO` 线程上，而整个服务只有一个 `IO` 线程池，**这时需要保证同步回调中不能有阻塞等耗时过长的逻辑，否则在这些逻辑执行完成前，`IO` 线程将一直被占用，影响整个服务的响应**。



### 7.2.4、其他

#### 7.2.4.1、异常处理

> 由于异步执行的任务在其他线程上执行，而异常信息存储在线程栈中，因此当前线程除非阻塞等待返回结果，否则无法通过 `try` \ `catch` 捕获异常。`CompletableFuture` 提供了异常捕获回调 `exceptionally`，相当于同步调用中的 `try` \ `catch`。使用方法如下所示：

```java
@Autowired
private WmOrderAdditionInfoThriftService wmOrderAdditionInfoThriftService;//内部接口
public CompletableFuture<Integer> getCancelTypeAsync(long orderId) {
  //业务方法，内部会发起异步rpc调用
  CompletableFuture<WmOrderOpRemarkResult> remarkResultFuture = 
    wmOrderAdditionInfoThriftService.findOrderCancelledRemarkByOrderIdAsync(orderId);
  return remarkResultFuture
    .exceptionally(err -> {//通过exceptionally 捕获异常，打印日志并返回默认值
      log.error("WmOrderRemarkService.getCancelTypeAsync Exception orderId={}", orderId, err);
      return 0;
    });
}
```



#### 7.2.4.1、真正的异常

> 有一点需要注意，`CompletableFuture` 在回调方法中对异常进行了包装。大部分异常会封装成 `CompletionException` 后抛出，真正的异常存储在 `cause` 属性中，因此如果调用链中经过了回调方法处理那么就需要用 `Throwable.getCause()` 方法提取真正的异常。但是，有些情况下会直接返回真正的异常

```java
@Autowired
private WmOrderAdditionInfoThriftService wmOrderAdditionInfoThriftService;//内部接口
public CompletableFuture<Integer> getCancelTypeAsync(long orderId) {
  CompletableFuture<WmOrderOpRemarkResult> remarkResultFuture = wmOrderAdditionInfoThriftService.findOrderCancelledRemarkByOrderIdAsync(orderId);//业务方法，内部会发起异步rpc调用
  return remarkResultFuture
    .thenApply(result -> {//这里增加了一个回调方法thenApply，如果发生异常thenApply内部会通过new CompletionException(throwable) 对异常进行包装
      //这里是一些业务操作
    })
    .exceptionally(err -> {//通过exceptionally 捕获异常，这里的err已经被thenApply包装过，因此需要通过Throwable.getCause()提取异常
      log.error("WmOrderRemarkService.getCancelTypeAsync Exception orderId={}", orderId, ExceptionUtils.extractRealException(err));
      return 0;
    });
}
```

> 上面代码中用到了一个自定义的工具类 `ExceptionUtils`，用于 `CompletableFuture` 的异常提取，在使用 `CompletableFuture` 做异步编程时，可以直接使用该工具类处理异常。实现代码如下：

```java
public class ExceptionUtils {
    public static Throwable extractRealException(Throwable throwable) {
          //这里判断异常类型是否为CompletionException、ExecutionException，如果是则进行提取，否则直接返回。
        if (throwable instanceof CompletionException || throwable instanceof ExecutionException) {
            if (throwable.getCause() != null) {
                return throwable.getCause();
            }
        }
        return throwable;
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
		id: 'Cleskowqab1HR97X',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



