---
title: Semaphore_CyclicBarrier_CountDownLatch_Thead.sleep
date: 2023-01-13 00:00:00
tags: 
- Java
category: 
- Java
description:  Semaphore_CyclicBarrier_CountDownLatch_Thead.sleep
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`Semaphore`

## 1.1、`Semaphore` 是什么

> `Semaphore` 通常我们叫它信号量， 可以用来控制同时访问特定资源的线程数量，通过协调各个线程，以保证合理的使用资源。



## 1.2、使用场景

> 通常用于那些资源有明确访问数量限制的场景，常用于限流 。      
>
> 比如：数据库连接池，同时进行连接的线程有数量限制，连接不能超过一定的数量，当连接达到了限制数量后，后面的线程只能排队等前面的线程释放了数据库连接才能获得数据库连接。      
>
> 比如：停车场场景，车位数量有限，同时只能容纳多少台车，车位满了之后只有等里面的车离开停车场外面的车才可以进入。



## 1.3、常用方法

| 方法                                          | 说明                                                         |
| --------------------------------------------- | ------------------------------------------------------------ |
| **`acquire()`**                               | 获取一个令牌，在获取到令牌、或者被其他线程调用中断之前线程一直处于阻塞状态。 |
| **`acquire(int permits) `**                   | 获取一个令牌，在获取到令牌、或者被其他线程调用中断、或超时之前线程一直处于阻塞状态。 |
| `acquireUninterruptibly()`                    | 获取一个令牌，在获取到令牌之前线程一直处于阻塞状态（忽略中断）。 |
| **`tryAcquire()`**                            | 尝试获得令牌，返回获取令牌成功或失败，不阻塞线程。           |
| **`tryAcquire(long timeout, TimeUnit unit)`** | 尝试获得令牌，在超时时间内循环尝试获取，直到尝试获取成功或超时返回，不阻塞线程。 |
| **`release()`**                               | 释放一个令牌，唤醒一个获取令牌不成功的阻塞线程。             |
| `hasQueuedThreads()`                          | 等待队列里是否还存在等待线程。                               |
| `getQueueLength()`                            | 获取等待队列里阻塞的线程数。                                 |
| `drainPermits()`                              | 清空令牌把可用令牌数置为0，返回清空令牌的数量。              |
| `availablePermits()`                          | 返回可用的令牌数量。                                         |



## 1.4、用例

### 1.4.1、停车场提示牌功能

> 每个停车场入口都有一个提示牌，上面显示着停车场的剩余车位还有多少，当剩余车位为0时，不允许车辆进入停车场，直到停车场里面有车离开停车场，这时提示牌上会显示新的剩余车位数。
>

**业务场景 ：**停车场容纳总停车量10。   当一辆车进入停车场后，显示牌的剩余车位数响应的减1。每有一辆车驶出停车场后，显示牌的剩余车位数响应的加1。停车场剩余车位不足时，车辆只能在外面等待。

```java
package io.binghe.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
public class SemaphoreExample {
  
  private static final int threadCount = 200;

  public static void main(String[] args) throws InterruptedException {

    ExecutorService exec = Executors.newCachedThreadPool();
    final Semaphore semaphore  = new Semaphore(10);

    for (int i = 0; i < threadCount; i++){
      final int threadNum = i;
      exec.execute(() -> {
        try {
          semaphore.acquire();  //获取一个许可
          test(threadNum);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }final{
          semaphore.release();  //释放一个许可

        }
      });
    }
    exec.shutdown();
  }

  private static void test(int threadNum) throws InterruptedException {
    log.info("{}", threadNum);
    Thread.sleep(1000);
  }
}
```



# 2、`CyclicBarrier`

> 是一个同步辅助类，允许一组线程相互等待，直到到达某个公共的屏障点，**通过它可以完成多个线程之间相互等待，只有当每个线程都准备就绪后，才能各自继续往下执行后面的操作**。
>
> 与 `CountDownLatch` 有相似的地方，都是使用计数器实现，当某个线程调用了 `CyclicBarrier` 的 `await()` 方法后，该线程就进入了等待状态，而且计数器执行加1操作，当计数器的值达到了设置的初始值，调用 `await()` 方法进入等待状态的线程会被唤醒，继续执行各自后续的操作。`CyclicBarrier` 在释放等待线程后可以重用，所以，`CyclicBarrier` 又被称为循环屏障。



## 2.1、`CyclicBarrier` 使用场景

> 可以用于多线程计算数据，最后合并计算结果的场景



## 2.2、`CyclicBarrier` 与 `CountDownLatch` 的区别

1、`CountDownLatch` 的计数器只能使用一次，而 `CyclicBarrier`的计数器可以使用 `reset()` 方法进行重置，并且可以循环使   

2、`CountDownLatch` 主要实现 1 个或 n 个线程需要等待其他线程完成某项操作之后，才能继续往下执行，描述的是 1 个或 n 个线程等待其他线程的关系。而 `CyclicBarrier` 主要实现了多个线程之间相互等待，直到所有的线程都满足了条件之后，才能继续执行后续的操作，描述的是各个线程内部相互等待的关系。      

3、`CyclicBarrier` 能够处理更复杂的场景，如果计算发生错误，可以重置计数器让线程重新执行一次。



## 2.3、用例

```java
=package io.binghe.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
public class CyclicBarrierExample {

  private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

  public static void main(String[] args) throws Exception {
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++){
      final int threadNum = i;
      Thread.sleep(1000);
      executorService.execute(() -> {
        try {
          race(threadNum);
        } catch (Exception e) {
          e.printStackTrace();
        }
      });
    }
    executorService.shutdown();
  }
  private static void race(int threadNum) throws Exception{
    Thread.sleep(1000);
    log.info("{} is ready", threadNum);
    cyclicBarrier.await();
    log.info("{} continue", threadNum);
  }
}
```



# 3、`CountDownLatch`

> 闭锁，通过一个计数，来保证线程是否一直阻塞     
>
> 同步辅助类，通过它可以阻塞当前线程。也就是说，能够实现一个线程或者多个线程一直等待，直到其他线程执行的操作完成。使用一个给定的计数器进行初始化，该计数器的操作是原子操作，即同时只能有一个线程操作该计数器。



## 3.1、用例

### 3.1.1、判断线程池是否结束

> 调用 `ExecutorService` 类的 `shutdown()` 方法，并不会第一时间内把所有线程全部都销毁掉，而是让当前已有的线程全部执行完，之后，再把线程池销毁掉。

```java
package io.binghe.concurrency.example.aqs;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Slf4j
public class CountDownLatchExample {
  private static final int threadCount = 200;

  public static void main(String[] args) throws InterruptedException {

    ExecutorService exec = Executors.newCachedThreadPool();
    final CountDownLatch countDownLatch = new CountDownLatch(threadCount);
    for (int i = 0; i < threadCount; i++){
      final int threadNum = i;
      exec.execute(() -> {
        try {
          test(threadNum);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }finally {
          countDownLatch.countDown();
        }
      });
    }
    countDownLatch.await();
    log.info("finish");
    exec.shutdown();
  }

  private static void test(int threadNum) throws InterruptedException {
    Thread.sleep(100);
    log.info("{}", threadNum);
    Thread.sleep(100);
  }
}
```





# 4、`Thead.sleep()`

## 4.1、思考两个问题

**Q1：假设现在是 2021-05-04 12:00:00.000，如果我调用一下 Thread.Sleep(1000) ，在 2021-05-04 12:00:01.000 的时候，这个线程会不会被唤醒？ **    

A1：不一定。因为你只是告诉操作系统：在未来的1000毫秒内我不想再参与到 `CPU` 竞争。那么 `1000` 毫秒过去之后，这时候也许另外一个线程正在使用 `CPU` ，那么这时候操作系统是不会重新分配 `CPU` 的，直到那个线程挂起或结束；况且，即使这个时候恰巧轮到操作系统进行 `CPU` 分配，那么当前线程也不一定就是总优先级最高的那个，`CPU` 还是可能被其他线程抢占去。       



**Q2：某人的代码中用了一句看似莫明其妙的话：Thread.Sleep(0) 。既然是 Sleep 0 毫秒，那么他跟去掉这句代码相比，有啥区别么**？       

A2：有，而且区别很明显。`Thread.Sleep(0)` 的作用，就是“触发操作系统立刻重新进行一次 `CPU` 竞争”。竞争的结果也许是当前线程仍然获得 `CPU` 控制权，也许会换成别的线程获得 `CPU` 控制权。**这也是我们在大循环里面经常会写一句 `Thread.Sleep(0)`  ，因为这样就给了其他线程比如 `Paint` 线程获得 `CPU` 控制权的权力，这样界面就不会假死在那里**。



## 4.2、介绍

```java
while (true) {
  if( check() ) {
    //执行某个操作
  }
  Thread.sleep(10);
}

```

**出现的问题：**这么做可以让线程每个 `10` 毫秒陷入一次睡眠，避免 `while` 死循环大量暂用 `CPU` 时间。然而`Thread.sleep` 的执行并非没有成本，如果循环中 `sleep` 的时间过短，开销也非常大。

**原因：`sleep` 的主要开销来自线程的切换，当代码运行到 `Thread.sleep` 时，当前线程进入 `time_wait`状态，通常我们会说线程进入睡眠状态，此时该线程不需要占用 `CPU`了，操作系统就会执行一次线程切换，将该线程的 `CPU` 时间给其他线程使用，这种切换叫做主动线程切换，是该线程使用 `sleep` 主动让出 `CPU` 时间，而不是 `CPU` 时间用完了操作系统将`CPU`使用权强行切换给其他线程。**     

网上有人对线程上下文切换时间做过计算，每一次大概需要消耗5微妙左右`CPU` 时间。而每一次 `sleep` 会造成 2 次线程切换，一次切出去， 一次切回来，那么就会消耗 `10` 微妙 `CPU` 时间。这个消耗虽然很小，但是架不住次数多，就像上面的示例进程，每条切换将近 `500` 次数，那么这个进程每运行 `1` 秒，就会平白浪费 `500` 微妙也就是 `5` 毫秒在线程切换上，这是一种可耻的浪费。      

系统调用相对于普通代码执行，也是一项重量级操作，每执行一次系统调用，操作系统需要将该线程的状态从用户太切为内核态，完成后还需要切回来，这也设计到执行现场的保存和恢复，设计到 `CPU` 上下文切换。 **当然，系统调用的切换开销不及线程切换的开销大，据统计，每次系统调用，涉及到切换小下文的的消耗时间为 `200` 纳秒，远小于线程切换的 `5` 微妙，但是如果次数多了，消耗的总和也不可忽视**。



**重点：**`sleep` 期间是不占用 `cpu` 的，对 `sleep` 来说，占用 `cpu` 主要是 `cpu` 时间片切换耗费的时间



# 5、`Uninterruptibles.sleepUninterruptibly`

> 这里使用 `NANOSECONDS.sleep(remainingNanos)` 的写法代替 `sleep(xxxx)`主要是更方便理解，将时间的单位显示表述出来。该改法同时也对 `sleep` 期间被中断的异常做了捕获，并计算出剩余需要`sleep`的时间，然后继续等待，直到耗尽所有时间。所以这个方法可以描述为不可中断的`sleep` 。其意义是为了支持上层限流对于请求的控制。
>

```java
public static void sleepUninterruptibly(long sleepFor, TimeUnit unit) {
  boolean interrupted = false;
  try {
    // 将sleepFor转换为对应纳秒级别的数值
    long remainingNanos = unit.toNanos(sleepFor);
    long end = System.nanoTime() + remainingNanos;
    while (true) {
      try {
        // TimeUnit.sleep() treats negative timeouts just like zero.
        // sleep对应的纳秒时间
        NANOSECONDS.sleep(remainingNanos);
        return;
      } catch (InterruptedException e) {
        interrupted = true;
        remainingNanos = end - System.nanoTime();
      }
    }
  } finally {
    if (interrupted) {
      Thread.currentThread().interrupt();
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
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: '0oPQb84ryBejNp9q',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



