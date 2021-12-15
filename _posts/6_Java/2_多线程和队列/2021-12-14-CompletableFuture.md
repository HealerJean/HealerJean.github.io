---
title: CompletableFuture
date: 2021-12-14 00:00:00
tags: 
- Java
category: 
- Java
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

> `thenApply() ` 有入参，有返回值，不传线程池 （和 `CompletableFuture.supplyAsync`  用的一个线程池，非异步）

```java
/**
 * 3.1、线程串行化
 */
@Test
public void test() throws ExecutionException, InterruptedException {
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



## 4.2、两个任务都完成其中一个完成，第三个任务才开始执行

```

```

## 4.3、当一个任务失败后快速返回

```java

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



