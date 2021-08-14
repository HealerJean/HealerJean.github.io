---
title: 3、线程池ThreadPoolExecutor总结
date: 2018-04-02 17:33:00
tags: 
- Thread
category: 
- Thread
description: 3、线程池ThreadPoolExecutor总结
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             

本文之前应该先稍微了解下新建线程的几种方式。然后知道线程池的集中模式



# 1、线程池参数         



> 线程池的概念是 `Executor` 这个接口 `ExecutorService` 继承了它，具体实现为 `ThreadPoolExecutor` 类，学习Java中的线程池，就可以直接学习对线程池的配置，就是对`ThreadPoolExecutor`构造函数的参数的配置

```java
public abstract class AbstractExecutorService implements ExecutorService {

public class ThreadPoolExecutor extends AbstractExecutorService {

ExecutorService exec=Executors.newCachedThreadPool();
```



```java
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



## 1.1、参数解释

### 1.1.1、`int corePoolSize`：线程池中核心线程数最大值    



> 核心线程：线程池新建线程的时候，如果当前线程总数小于`corePoolSize`，则新建的是核心线程，如果超过`corePoolSize`，则新建的是非核心线程。      
>
> 核心线程默认情况下会一直存活在线程池中，即使这个核心线程啥也不干(闲置状态)。   
>
>  如果指定`ThreadPoolExecutor`的`allowCoreThreadTimeOut`这个属性为true，那么核心线程如果不干活(闲置状态)的话，超过一定时间(时长下面参数决定)，就会被销毁掉。



### 1.1.2、`int maximumPoolSize`： 线程池中线程总数最大值    

> 线程总数 = 核心线程数 + 非核心线程数。





### 1.1.3、`long keepAliveTime`：线程池中非核心线程闲置超时时长  

> 一个非核心线程，如果不干活(闲置状态)的时长超过这个参数所设定的时长，就会被销毁掉，    
>
> 如果设置`allowCoreThreadTimeOut = true`，则会作用于核心线程。



### 1.1.4、`imeUnit unit`：`keepAliveTime`的单位   

>  `TimeUnit`是一个枚举类型，其包括： NANOSECONDS ：     

>
> 1微毫秒 = 1微秒 / 1000 MICROSECONDS ：    
>
> 1微秒 = 1毫秒 / 1000 MILLISECONDS ：      
>
> 1毫秒 = 1秒 /1000 SECONDS ：      
>
> 秒 MINUTES ：    
>
>  分 HOURS ：    
>
> 小时 DAYS ：    
>
> 天



### 1.1.5、`BlockingQueue workQueue`：该线程池中的任务队列：维护着等待执行的`Runnable`对象

> 当所有的核心线程都在干活时，新添加的任务会被添加到这个队列中等待处理，如果队列满了，则新建非核心线程执行任务。 常用的`workQueue`类型：

>**发现如果阻塞队列数量>0，则使用 `LinkedBlockingQueue`，否则使用 `SynchronousQueue`。**



#### 1.1.5.1、`LinkedBlockingQueue`：

> 1、这个队列接收到任务的时候，如果当前线程数小于核心线程数，则新建线程(核心线程)处理任务；（先新建核心线程）     
>
> 2、如果当前线程数等于核心线程数，则进入队列等待。由于这个队列没有最大值限制，即所有超过核心线程数的任务都将被添加到队列中，这也就导致了`maximumPoolSize`的设定失效，因为总线程数永远不会超过`corePoolSize`



### 1.1.6、`ThreadFactory threadFactory`：线程工厂，创建线程的方式

> 创建线程的方式，这是一个接口，你new他的时候需要实现他的Thread new Thread(Runnable r)方法，一般用不上。   



### 1.1.7、`RejectedExecutionHandler handler`：任务拒绝/饱和策略     

> `RejectedExecutionHandler`：任务拒绝/饱和策略，这玩意儿就是抛出异常专用的，下面会通过介绍`shotDowm`， `shotDownNow`来解释拒绝的过程

```java
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.concurrent.ArrayBlockingQueue;
    import java.util.concurrent.BlockingQueue;
    import java.util.concurrent.ThreadPoolExecutor;
    import java.util.concurrent.TimeUnit;

    public class ExecutorDemo {

        private static  SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        public static void main(String[] args) {
            int corePoolSize = 1;
            int maximumPoolSize = 1;
            BlockingQueue queue = new  ArrayBlockingQueue<Runnable>(1);
            ThreadPoolExecutor pool = new ThreadPoolExecutor(corePoolSize,  maximumPoolSize, 
                    0, TimeUnit.SECONDS, queue ) ;
            pool.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy ());
            for(int i=0;i<10;i++){
                final int index = i;
                pool.submit(new Runnable(){

                    @Override
                    public void run() {
                        log(Thread.currentThread().getName()+"begin run task :"+index);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        log(Thread.currentThread().getName()+" finish run  task :"+index);
                    }

                });
            }

            log("main thread before sleep!!!");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("before shutdown()");

            pool.shutdown();

            log("after shutdown(),pool.isTerminated=" + pool.isTerminated());
            try {
                pool.awaitTermination(1000L, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("now,pool.isTerminated=" + pool.isTerminated());
        }

        protected static void log(String string) {
            System.out.println(sdf.format(new Date())+"  "+string);
        }

    }
```



#### 1.1.7.1、`DiscardPolicy`：

> 直接丢弃

```java
    2016-08-04 22:29:21  main thread before sleep!!!
    2016-08-04 22:29:21  pool-1-thread-1begin run task :0
    2016-08-04 22:29:22  pool-1-thread-1 finish run  task :0
    2016-08-04 22:29:22  pool-1-thread-1begin run task :1
    2016-08-04 22:29:23  pool-1-thread-1 finish run  task :1
    2016-08-04 22:29:25  before shutdown()
    2016-08-04 22:29:25  after shutdown(),pool.isTerminated=false
    2016-08-04 22:29:25  now,pool.isTerminated=true

```



从结果可以看出，只有`task0`和 `task1`两个任务被执行了。为什么只有`task0` 和 `task1` 两个任务被执行了呢？   

过程是这样的：由于我们的任务队列的容量为1.当`task0`正在执行的时候，`task1`被提交到了队列中但是还没有执行，受队列容量的限制，`submit`提交的`task2~task9`就都被直接抛弃了。因此就只有`task0`和`task1`被执行了。



#### 1.1.7.2、`DiscardOldestPolicy`：

> 丢弃队列中最老的任务   

如果将拒绝策略改为：`DiscardOldestPolicy` (丢弃队列中比较久的任务)   

```java
    2016-08-04 22:31:58  pool-1-thread-1begin run task :0
    2016-08-04 22:31:58  main thread before sleep!!!
    2016-08-04 22:31:59  pool-1-thread-1 finish run  task :0
    2016-08-04 22:31:59  pool-1-thread-1begin run task :9
    2016-08-04 22:32:00  pool-1-thread-1 finish run  task :9
    2016-08-04 22:32:02  before shutdown()
    2016-08-04 22:32:02  after shutdown(),pool.isTerminated=false
    2016-08-04 22:32:02  now,pool.isTerminated=true
```

从结果可以看出，只有`task0`和`task9`被执行了。





#### 1.1.7.4、`CallerRunsPolicy`：将任务分给调用线程来执行

> 没有任务被抛弃，而是将由的任务分配到main线程中执行了



如果将拒绝策略改为：`CallerRunsPolicy` (即不用线程池中的线程执行，而是交给调用方来执行)   

```java
    2016-08-04 22:33:07  mainbegin run task :2
    2016-08-04 22:33:07  pool-1-thread-1begin run task :0
    2016-08-04 22:33:08  main finish run  task :2
    2016-08-04 22:33:08  mainbegin run task :3
    2016-08-04 22:33:08  pool-1-thread-1 finish run  task :0
    2016-08-04 22:33:08  pool-1-thread-1begin run task :1
    2016-08-04 22:33:09  pool-1-thread-1 finish run  task :1
    2016-08-04 22:33:09  main finish run  task :3
    2016-08-04 22:33:09  mainbegin run task :5
    2016-08-04 22:33:09  pool-1-thread-1begin run task :4
    2016-08-04 22:33:10  main finish run  task :5
    2016-08-04 22:33:10  mainbegin run task :7
    2016-08-04 22:33:10  pool-1-thread-1 finish run  task :4
    2016-08-04 22:33:10  pool-1-thread-1begin run task :6
    2016-08-04 22:33:11  main finish run  task :7
    2016-08-04 22:33:11  mainbegin run task :9
    2016-08-04 22:33:11  pool-1-thread-1 finish run  task :6
    2016-08-04 22:33:11  pool-1-thread-1begin run task :8
    2016-08-04 22:33:12  main finish run  task :9
    2016-08-04 22:33:12  main thread before sleep!!!
    2016-08-04 22:33:12  pool-1-thread-1 finish run  task :8
    2016-08-04 22:33:16  before shutdown()
    2016-08-04 22:33:16  after shutdown(),pool.isTerminated=false
    2016-08-04 22:33:16  now,pool.isTerminated=true
```

从结果可以看出，没有任务被抛弃，而是将由的任务分配到main线程中执行了。



#### 1.1.7.3、`AbortPolicy`：抛异常(默认)

> 抛出异常的方式（AbortPolicy）丢弃任务并抛出RejectedExecutionException    





## 1.2、线程池构造器  



### 1.2.1、 五个参数的构造函数 

```java
//五个参数的构造函数
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue)
```



### 1.2.2、六个参数的构造函数  

```java
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
```



### 1.2.3、七个参数的构造函数

```java
//七个参数的构造函数
public ThreadPoolExecutor(int corePoolSize,
                          int maximumPoolSize,
                          long keepAliveTime,
                          TimeUnit unit,
                          BlockingQueue<Runnable> workQueue,
                          ThreadFactory threadFactory,
                          RejectedExecutionHandler handler)
```



# 2、知识点详解    

## 2.1、线程池任务执行的过程和作用        

1、线程数量未达到`corePoolSize`，则新建一个线程(核心线程)执行任务    

2、线程数量达到了`corePoolSize`，则将任务移入队列等待    

3、队列已满，新建线程(非核心线程)执行任务    

4、队列已满，总线程数又达到了`maximumPoolSize`，就会由(`RejectedExecutionHandler`)抛出异常  



### 2..1.1、好处

> **1、降低资源消耗**：通过池化技术重复利用已创建的线程，降低线程创建和销毁造成的损耗。       
>
> **2、提高响应速度**：任务到达时，无需等待线程创建即可立即执行。       
>
> **3、提高线程的可管理性**：线程是稀缺资源，如果无限制创建，不仅会消耗系统资源，还会因为线程的不合理分布导致资源调度失衡，降低系统的稳定性。使用线程池可以进行统一的分配、调优和监控。         
>
> **4、提供更多更强大的功能**：线程池具备可拓展性，允许开发人员向其中增加更多的功能。比如延时定时线程池`ScheduledThreadPoolExecutor`，就允许任务延期执行或定期执行。                  



### 2.1.2、风险  

> 虽然线程池是构建多线程应用程序的强大机制，但使用它并不是没有风险的。用线程池构建的应用程序容易遭受任何其它多线程应用程序容易遭受的所有并发风险，诸如同步错误和死锁，它还容易遭受特定于线程池的少数其它风险，诸如与池有关的死锁、资源不足和线程泄漏。    






## 2.2、`ExecutorService`中`execute()`和`submit()`方法的区别

> 1、方法`execute()`没有返回值，而`submit()`方法可以有返回值（通过Callable和Future接口）        
>
> 2、方法`execute()`提交的未执行的任务可以通过`remove(Runnable)`方法删除，而`submit()`提交的任务即使还未执行也不能通过`remove(Runnable)`方法删除        
>
> 3、方法`execute()`在默认情况下异常直接抛出（即打印堆栈信息），不能捕获，但是可以通过自定义`ThreadFactory`的方式进行捕获（通过`setUncaughtExceptionHandler`方法设置），而`submit()`方法在默认的情况下可以捕获异常







## 2.3、几种常见的线程池  

### 2.3.1、`newFixedThreadPool`：创建固定的线程池 

> 创建一个固定数目的、可重用的线程池。可控制现场最大并发数，超出的线程会在线程队列中等待     
>
> `newFixedThreadPool`创建的线程池`corePoolSize`和`maximumPoolSize`值是相等的，它使用的`LinkedBlockingQueue；`



```java
public class FixedThreadPoolTest {
    @Test
    public void newFixedThreadPoolTest() throws InterruptedException {
        // 固定线程的数量4
        ExecutorService m = Executors.newFixedThreadPool(4);

        for (int i = 1; i <= 10; i++) {
            final int count = i;
            m.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println("线程：" + Thread.currentThread() + 
                                       "负责了" + count + "任务");
                }
            });
            Thread.sleep(1000);
        }
    }
}

```



**控制台，会发现只有4个线程被创建了**   



![1583116323843](D:\study\HealerJean.github.io\blogImages\1583116323843.png)



### 2.3.2、`newCachedThreadPool` ：创建可缓存线程池  

> 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。就是一次添加多个任务，长度会变大，会一直新建线程，当没有任务时，空闲线程会被回收，线程池为无限大，当执行第二个任务时第一个任务已经完成。会复用第一个任务的线程，而不是每次新建线程。       

**参数情况 **       

`corePoolSize`设置为0，`maximumPoolSize`为`Integer.MAX_VALUE`，    队列为`SynchronousQueue`，   

`SynchronousQueue`：**不存储元素的阻塞队列 ,每个插入操作必须等到另一个线程调用移除操作**，否则插入操作一直处于阻塞状态,每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态



```java
public class NewCachedThreadPooltest {
    @Test
    public void testRunnalbeSubmit(){
        ExecutorService exec=Executors.newCachedThreadPool();  
        for(int i=0;i<10;i++) {  
            exec.submit(new MyRunnable());
        }   
    }

    class MyRunnable implements Runnable{
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
        }
    } 
}

```



**控制台执行结果，会发现虽然上面开启了10个线程，但是线程其实是有重复使用的，这样就大大节省了资源**   



![1583116171179](D:\study\HealerJean.github.io\blogImages\1583116171179.png)   





### 2.3.3、`newSingleThreadExecutor`：创建一个单线程化的线程池

> 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。   
>
> `newSingleThreadExecutor`将`corePoolSize`和`maximumPoolSize`都设置为1，队列为`LinkedBlockingQueue`；





## 2.2、中断判断  

### 2.2.1、`isShutDown()`     

`isShutDown()` 当调用`shutdown()`或`shutdownNow()`方法后返回为`true`。    

如果线程池任务正常完成，为false（表示没有中断哦）



### 2.2.2、`isTerminated()`

`isTerminated()`当调用`shutdown()`方法后，并且所有提交的任务完成后返回为`true`;    

`isTerminated()`当调用`shutdownNow()`方法后，成功停止后返回为`true`;       

如果线程池任务正常完成，为false（表示没有中断哦）



```java
threadPoolUtils.getThreadPoolExecutor().shutdown();
while(true){  
    if(threadPoolUtils.getThreadPoolExecutor().isTerminated()){  
        System.out.println("所有的子线程都结束了！");  
        break;  
    }  
    Thread.sleep(10000);    //防止while判断过快，浪费资源，这里我设置为10秒大家看情况设置
}
```





## 2.3、`shutdown()`、`shutdownNow()`、``awaitTerminate(long timeout,TimeUnit unit)`



#### 2.3.1、`shutdown（）`

> 阻止新来的任务提交，对已经提交了的任务不会产生任何影响。当已经提交的任务执行完后，它会将那些闲置的线程进行中断，这个过程是异步的。    



**问：如何阻止新来的任务提交**    

答：通过将线程池的状态改成`SHUTDOWN`，当再将执行execute提交任务时，如果测试到状态不为`RUNNING`，则抛出`rejectedExecution`，从而达到阻止新任务提交的目的。       



**问：为何对提交的任务不产生任何影响**    

 答：在调用中断任务的方法时，它会检测workers中的任务，如果worker对应的任务没有中断，并且是空闲线程，它才会去中断。<font color="red"> 另外的话，workQueue中的值，还是按照一定的逻辑顺序不断的往works中进行输送的，这样一来，就可以保证提交的任务按照线程本身的逻辑执行，不受到影响。</font>      



#### 2.3.2、`shutdownNow()`

> 阻止新来的任务提交，同时会中断当前正在运行的线程，即workers中的线程。<font color="red">   另外它还将workQueue中的任务给移除，并将这些任务添加到列表中进行返回。</font>          



**问：如何阻止新来的任务提交**    

答：通过将线程池的状态改成 `STOP`，当再将执行execute提交任务时，如果测试到状态不为`RUNNING`，则抛出`rejectedExecution`，从而达到阻止新任务提交的目的.<font color="red">（如果为RUNNING,这个线程没有抛出异常类型的阻塞，则继续阻塞，阻塞请看下个问题）</font>           



**问：如果我提交的任务代码块中，正在等待某个资源，而这个资源没到，但此时执行`shutdownNow()`，会出现什么情况**

答：当执行`shutdownNow()`方法时，如遇已经激活的任务，并且处于阻塞状态时，`shutdownNow()`会执行1次中断阻塞的操作，此时对应的线程报`InterruptedException`，如果后续还要等待某个资源，则按正常逻辑等待某个资源的到达。

例如，一个线程正在`sleep()`中，此时执行`shutdownNow()`，它向该线程发起`interrupt()`请求，而`sleep()`方法遇到有`interrupt()`请求时，会抛出`InterruptedException()`，并继续往下执行。在这里要提醒注意的是，在激活的任务中，如果有多个`sleep()`,该方法只会中断第一个`sleep()`，而后面的仍然按照正常的执行逻辑进行。



#### 2.2.3、`awaitTermination(long timeout,TimeUnit unit)`

> `awaitTermination`会一直等待，直到线程池状态为`TERMINATED`(中断)或者，等待的时间到达了指定的时间。



```java
  exec.shutdown();
  exec.awaitTermination(1, TimeUnit.HOURS);
```



```java
public static void main(String[] args) {
    ExecutorService executor = Executors.newFixedThreadPool(3);
    int i = 0;
    AtomicInteger result = new AtomicInteger(0);
    while (i < 10) {
        i++;
        executor.execute(() -> {
            System.out.println(result.addAndGet(1));
        });
    }
    System.out.println("调用shutdown()方法时，result的值为：" + result.get());
    executor.shutdown();
    
    
    
    
    try {
        //等待所有任务结束，最多等待30分钟
        executor.awaitTermination(30, TimeUnit.MINUTES);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```



## 2.5、优雅的关闭线程池  

> 一般来说，线程执行完毕会自动结束，无需手动关闭     
>
> 线程提供给了一个·`Stop`方法，可以即使终止一个线程，但是会发现stop已经被弃用了。Thread.stop()方法在结束线程的时候，会直接终止线程，这个线程就会立即释放掉所有的锁，而这些锁恰恰是用来维护对象的一致性的，如果此时写到了一半，强行中止，那么对象就会被破坏，或者线程可能在操作数据库，强⾏中断导致数据不一致，从而混乱的问题。所以现在不使用了



### 2.5.1、线程的`interrupt`

> 1、中断异常的抛出：如果此线程处于阻塞状态(比如调⽤了wait方法，io等待)，则会立刻退出阻塞，并抛出`InterruptedException`异常，线程就可以通过捕获`InterruptedException`来做⼀定的处理，然后让线程退出。

> 2、中断标记状态的判断： 如果此线程正处于运行之中，则线程不受任何影响，继续运行，仅仅是线程的中断标记被设置为true。所以线程要在适当的位置通过调用`isInterrupted`方法来查看自⼰是否被中断，并做退出操作。

### 2.5.2、线程池的关闭

> **一定要记得，`shutdownNow`和`shuwdown`调用完，线程池并不是立马就关闭了，要想等待线程池关闭，还需调用`awaitTermination`方法来阻塞等待。**

#### 2.5.2.1. `shuwdown()` 关闭

当我们使用`shuwdown`方法关闭线程池时，一定要确保任务里不会有永久阻塞等待的逻辑，否则线程池就关闭不了。

#### 2.5.2.2、`shuwdownnow()`关闭

**所以当我们使用`shutdownNow`方法关闭线程池时，一定要对任务里进行异常捕获（相当于线程中断）。或者线程中断的判断(当然如果上面出现了非抛出异常类型的阻塞，也会一直阻塞下去)**     



## 2.6、`submit()`：获取返回值      

### 2.6.1、`sumbit()`返回代码详解  

#### 2.6.1.1、`submit` 中可以放三个参数  

```java

Future<?> submit(Runnable runnable);

<T> Future<T> submit(Callable<T> callable);

<T> Future<T> submit(Runnable var1, T result);
```



#### 2.6.1.2、分析下 `Runable`和`Callable`的不同  

```java
public Future<?> submit(Runnable task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<Void> ftask = newTaskFor(task, null);
    execute(ftask);
    return ftask;
}


public <T> Future<T> submit(Callable<T> task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<T> ftask = newTaskFor(task);
    execute(ftask);
    return ftask;
}
```



这个时候回发现 `newTaskFor` 参数不同，返回的都是一个`FtureTask`对象  

```java
protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
    return new FutureTask<T>(runnable, value);
}


protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
    return new FutureTask<T>(callable);
}

```



**看到了下面，其实就大致可以知道，`Runnable` 会在这里转化成 `Callable`。我们来看下 `Executors.callable()`**    

```java

public FutureTask(Runnable runnable, V result) {
    this.callable = Executors.callable(runnable, result);
    this.state = NEW;       
}


public FutureTask(Callable<V> callable) {
    if (callable == null)throw new NullPointerException();
    this.callable = callable;
    this.state = NEW;       
}
```



`submit()`返回的是一个 `RunnableFuture` 类对象，真正是通过`newTaskFor()`方法返回一个`new FutureTask()`对象。所以`submit()`返回的真正的对象是`FutureTask `对象。   

```java
public interface RunnableFuture<V> extends Runnable, Future<V> {
    void run();
}

public class FutureTask<V> implements RunnableFuture<V> {
    ...
}
```



### 2.6.2、`get方法 `   

> `V get(long timeOut, TimeUnit unit)`：获取结果，超时返回null



```java
//Runable接口的对象，这样当调用get方法的时候，如果线程执行成功会直接返回null，如果线程执行异常会抛出异常

public Future<?> submit(Runnable task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<Void> ftask = newTaskFor(task, null);
    execute(ftask);
    return ftask;
}


//Callable接口的对象，当主线程调用Future的get方法的时候会获取到从线程中返回的结果数据，如果线程执行异常会抛出异常信息
public <T> Future<T> submit(Callable<T> task) {
    if (task == null) throw new NullPointerException();
    RunnableFuture<T> ftask = newTaskFor(task);
    execute(ftask);
    return ftask;
}

```



#### 2.6.2.1、 Callable

```java
package com.hlj.threadpool.D02SubmitFuture;

import java.util.concurrent.*;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/3/13  下午1:34.
 * 类描述：
 */
public class D02Future {

    private static final String SUCCESS = "success";

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("线程开始执行");

        Future<String> future = executorService.submit
            (new D02Future().new TaskCallable());

        try {
            String s = future.get();
            if (SUCCESS.equals(s)) {
                System.out.println("线程执行结束");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


class TaskCallable implements Callable<String> {

    @Override
    public String call() throws Exception {
        Thread.sleep(5000);
       // int i = 1/0 ; 测试有无异常的发生
        System.out.println("submit方法执行任务完成" + "   thread name: " +
                           Thread.currentThread().getName());
        return SUCCESS;
    }
}

}


1、如果没有 int i = 1/0 ;  

线程开始执行
submit方法执行任务完成   thread name: pool-1-thread-1
线程执行结束


------------------------

2、如果有 int i = 1/0 ;

线程开始执行
java.util.concurrent.ExecutionException: java.lang.ArithmeticException: / by zero
	at java.util.concurrent.FutureTask.report(FutureTask.java:122)
	at java.util.concurrent.FutureTask.get(FutureTask.java:192)
	at com.hlj.threadpool.D02SubmitFuture.D02Future.main(D02Future.java:23)
Caused by: java.lang.ArithmeticException: / by zero
	at com.hlj.threadpool.D02SubmitFuture.D02Future$TaskCallable.call(D02Future.java:42)
	at com.hlj.threadpool.D02SubmitFuture.D02Future$TaskCallable.call(D02Future.java:36)
	at java.util.concurrent.FutureTask.run(FutureTask.java:266)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
```



#### 2.6.2.2、Runnable

```java

package com.hlj.threadpool.d01Submit;

import java.util.concurrent.*;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/3/13  上午11:43.
 * 类描述：
 */
public class CallableSubmit {

    private static final String SUCCESS = "success";

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        System.out.println("------------------任务开始执行---------------------");


        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(5000);
                System.out.println("submit方法执行任务完成" + "   thread name: " + Thread.currentThread().getName());
                return SUCCESS;
            }
        });


        try {
            String s = future.get();
            if (SUCCESS.equals(s)) {
                String name = Thread.currentThread().getName();
                System.out.println("经过返回值比较，submit方法执行任务成功    thread name: " + name);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("-------------------main thread end---------------------");
    }
}


```



### 2.6.3、`isDone  `:  执行结束（完成/取消/异常）返回`true`

```java

package com.hlj.threadpool.d01Submit.D02SubmitFuture;

import java.util.concurrent.*;

public class D01FutureCancle {

    private static final String SUCCESS = "success";


    public static void main(String[] args) {

        ExecutorService executorService = Executors.newFixedThreadPool(3);
        System.out.println("线程开始执行");

        Future<String> future = executorService.submit(
            new D01FutureCancle().new TaskCallable());

        try {
            System.out.println("future.isDone()="+future.isDone());
            String s = future.get();
            if (SUCCESS.equals(s)) {
                System.out.println("future.isDone()"+future.isDone());
                System.out.println("线程执行结束");
            }
        } catch (InterruptedException e) {
            System.out.println("InterruptedException future.isDone()="+future.isDone());
            e.printStackTrace();
        } catch (ExecutionException e) {
            System.out.println("ExecutionException future.isDone()="+future.isDone());
            e.printStackTrace();
        }

    }


    class TaskCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            Thread.sleep(5000);
            int i = 1/0 ;
            System.out.println("submit方法执行任务完成" + "   thread name: " + Thread.currentThread().getName());
            return SUCCESS;
        }
    }

}



线程开始执行
    future.isDone()=false
    ExecutionException future.isDone()=true
    java.util.concurrent.ExecutionException: java.lang.ArithmeticException: / by zero
        at java.util.concurrent.FutureTask.report(FutureTask.java:122)
        at java.util.concurrent.FutureTask.get(FutureTask.java:192)
        at com.hlj.threadpool.d01Submit.D02SubmitFuture.D01FutureCancle.main(D01FutureCancle.java:24)
        Caused by: java.lang.ArithmeticException: / by zero
            at com.hlj.threadpool.d01Submit.D02SubmitFuture.D01FutureCancle$TaskCallable.call(D01FutureCancle.java:46)
            at com.hlj.threadpool.d01Submit.D02SubmitFuture.D01FutureCancle$TaskCallable.call(D01FutureCancle.java:40)
            at java.util.concurrent.FutureTask.run(FutureTask.java:266)
            at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
            at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
            at java.lang.Thread.run(Thread.java:748)
```



### 2.6.4、`cancel(boolean mayInterruptRunning)`：取消任务

> **未开始或已完成返回false，参数表示是否中断执行中的线程**    
>
> `true`：会中断正在运行的任务（相当于停止）      
>
> `false`：会让线程正常执行至完成，取消的是还没有开始执行的任务，如果任务以及开始，则由它执行下去   



**等待状态的任务。此时调用cancel()方法不管传入true还是false都会标记为取消，任务依然保存在任务队列中，但当轮到此任务运行时会直接跳过**。     

**运行中的任务。此时传入true会中断正在执行的任务，传入false则不会中断。**    

**完成状态。此时cancel()不会起任何作用，因为任务已经完成了。**     



## 2.4、判断任务是否完成 

### 2.4.1、倒计时器  

```java

public class CountDownLatchDemo implements Runnable {
	//计数数量为10，表示需要有10个线程完成任务等待在CountDownLatch 上的线程才能执行
    static final CountDownLatch end = new CountDownLatch(10); 
    static final CountDownLatchDemo demo = new CountDownLatchDemo();

    @Override
    public void run() {

        try {
        	System.out.println(Thread.currentThread().getName());
            Thread.sleep(new Random().nextInt(3) * 1000);
            System.out.println("check complete");
            end.countDown(); //通知CountDownLatch 一个线程已经完成了任务 倒计时可以减一了
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10); //固定开启10个线程
        for (int i = 0; i < 10; i++) {
            executorService.submit(demo);
        }
        //等待检查
        end.await(); //要求主线程等待所有的10个检查任务全部完成，主线程才能继续运行
        //发射火箭
        System.out.println("Fire!");
        executorService.shutdown();
    }
}
```



### 2.4.2、使用submit提交任务，获取返回值判断线程池任务结束

```java
long startTime = System.currentTimeMillis();


private void doOnceTasks(){
  List<Future> futureList = Lists.newArrayListWithCapacity(15);
  for(int i = 0; i < 15; ++i){
    Future future = executor.submit(()->{
      // 随机睡 0-5 秒
      int sec = new Double(Math.random() * 5).intValue();
      LockSupport.parkNanos(sec * 1000 * 1000 * 1000);
      System.out.println(Thread.currentThread().getName() + "  end");
    });
    futureList.add(future);
  }

  // 等待所有任务执行结束
  for(Future future : futureList){
    try {
      future.get();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
long totalTime = (System.currentTimeMillis() - startTime) / NumberConstant.ONE_THOUSAND;

```





## 2.5、线程池几种状态

| 运行状态       | 状态描述                                                     |
| :------------- | :----------------------------------------------------------- |
| **RUNNING**    | 能接受新提交的任务，并且也能处理阻塞队列中的任务。           |
| **SHUTDOWN**   | 关闭状态，不再接受新提交的任务，但却可以继续处理阻塞队列中已保存的任务。 |
| **STOP**       | 不能接受新任务，也不处理队列中的任务，会中断正在处理任务的线程。 |
| **TERMINATED** | 在 `terminated() `方法执行完后进入该状态                     |
| **TIDYING**    | 所有的任务都已终止了，`workerCount` (有效线程数) 为0         |

 ![image-20210730155956344](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730155956344.png)



## 2.6、任务执行机制

### 2.6.1、任务调度

1、首先检测线程池运行状态，如果不是**RUNNING**，则直接拒绝，线程池要保证在RUNNING的状态下执行任务。    

2、如果workerCount < corePoolSize，则创建并启动一个线程来执行新提交的任务。       

3、如果workerCount >= corePoolSize，且线程池内的阻塞队列未满，则将任务添加到该阻塞队列中。     

4、如果workerCount >= corePoolSize && workerCount < maximumPoolSize，且线程池内的阻塞队列已满，则创建并启动一个线程来执行新提交的任务。        

5、如果workerCount >= maximumPoolSize，并且线程池内的阻塞队列已满, 则根据拒绝策略来处理该任务, 默认的处理方式是直接抛异常。

![image-20210730160103636](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730160103636.png)



### 2.6.2、任务缓冲

> 任务缓冲模块是线程池能够管理任务的核心部分。线程池的本质是对任务和线程的管理，而做到这一点最关键的思想就是将任务和线程两者解耦，不让两者直接关联，才可以做后续的分配工作。线程池中是以生产者消费者模式，通过一个阻塞队列来实现的。阻塞队列缓存任务，工作线程从阻塞队列中获取任务。      



| 名称                  | 描述                                                         |
| :-------------------- | :----------------------------------------------------------- |
| ArrayBlockingQueue    | **一个用数组实现的有界阻塞队列**，此队列按照先进先出(FIFO)的原则对元素进行排序。支持公平锁和非公平锁。 |
| LinkedBlockingQueue   | **一个由链表结构组成的有界队列**，此队列按照先进先出(FIFO)的原则对元素进行排序。此队列的默认长度为Integer.MAX_VALUE，所以默认创建的该队列有容量危险。 |
| PriorityBlockingQueue | **一个支持线程优先级排序的无界队列**，默认自然序进行排序，也可以自定义实现compareTo()方法来指定元素排序规则，不能保证同优先级元素的顺序。 |
| DelayQueue            | 一个实现`PriorityBlockingQueue`实现延迟获取的无界队列，在创建元素时，可以指定多久才能从队列中获取当前元素。只有延时期满后才能从队列中获取元素。 |
| SynchronousQueue      | **一个不存储元素的阻塞队列**，每一个put操作必须等待take操作，否则不能添加元素。支持公平锁和非公平锁。SynchronousQueue的一个使用场景是在线程池里。Executors.newCachedThreadPool()就使用了SynchronousQueue，这个线程池根据需要（新任务到来时）创建新的线程，如果有空闲线程则会重复使用，线程空闲了60秒后会被回收。 |
| LinkedTransferQueue   | **一个由链表结构组成的无界阻塞队列**，相当于其它队列，LinkedTransferQueue队列多了transfer和tryTransfer方法。 |
| LinkedBlockingDeque   | **一个由链表结构组成的双向阻塞队列**。队列头部和尾部都可以添加和移除元素，多线程并发时，可以将锁的竞争最多降到一半。 |

 

# 3、业务实战

## 3.1、业务背景

### 3.1.1、场景1：快速响应用户请求

> **描述**：用户发起的实时请求，服务追求响应时间。比如说用户要查看一个商品的信息，那么我们需要将商品维度的一系列信息如商品的价格、优惠、库存、图片等等聚合起来，展示给用户。     
>
> **分析**：从用户体验角度看，这个结果响应的越快越好，如果一个页面半天都刷不出，用户可能就放弃查看这个商品了。而面向用户的功能聚合通常非常复杂，伴随着调用与调用之间的级联、多级级联等情况，业务开发同学往往会选择使用线程池这种简单的方式，将调用封装成任务并行的执行，缩短总体响应时间。另外，使用线程池也是有考量的，这种场景最重要的就是获取最大的响应速度去满足用户，**所以应该不设置队列去缓冲并发任务，调高`corePoolSize` 和 `maxPoolSize` 去尽可能创造多的线程快速执行任务。**

![image-20210730162000392](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730162000392.png)





### **3.1.1、场景1：快速处理批量任务**

> **描述**：离线的大量计算任务，需要快速执行。比如说，统计某个报表，需要计算出全国各个门店中有哪些商品有某种属性，用于后续营销策略的分析，那么我们需要查询全国所有门店中的所有商品，并且记录具有某属性的商品，然后快速生成报表。        
>
> **分析**：这种场景需要执行大量的任务，我们也会希望任务执行的越快越好。这种情况下，也应该使用多线程策略，并行计算。     **但与响应速度优先的场景区别在于，这类场景任务量巨大，并不需要瞬时的完成，而是关注如何使用有限的资源，尽可能在单位时间内处理更多的任务，也就是吞吐量优先的问题。所以应该设置队列去缓冲并发任务，调整合适的corePoolSize去设置处理任务的线程数。在这里，设置的线程数过多可能还会引发线程上下文切换频繁的问题，也会降低处理任务的速度，降低吞吐量**。



![image-20210730162010971](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730162010971.png)

## 3.2、实际问题及方案思考

### 3.2.1、问题1：最大核心数偏小，导致任务拒绝服务降级

> **事故描述**：XX页面展示接口产生大量调用降级，数量级在几十到上百。      
>
> **事故原因**：该服务展示接口内部逻辑使用线程池做并行计算，由于没有预估好调用的流量，导致**最大核心数设置偏小**，大量抛出`RejectedExecutionException`，触发接口降级条件，示意图如下：



![image-20210730162233535](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730162233535.png)

### 3.2.2、问题2：队列过长，任务执行时间拉长，导致超时

> **事故描述**：XX业务提供的服务执行时间过长，作为上游服务整体超时，大量下游服务调用失败。      
>
> **事故原因**：该服务处理请求内部逻辑使用线程池做资源隔离，由于**队列设置过长**，最大线程数设置失效，导致请求数量增加时，大量任务堆积在队列中，任务执行时间过长，最终导致下游服务的大量调用超时失败。示意图如下：

![image-20210730162412222](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730162412222.png)



## 3.3、如何设置参数才合理

> 线程池使用面临的核心的问题在于：**线程池的参数并不好配置**。     
>
> ⬤ 一方面线程池的运行机制不是很好理解，配置合理需要强依赖开发人员的个人经验和知识；         
>
> ⬤ 另一方面，线程池执行的情况和任务类型相关性较大，`IO`密集型和`CPU`密集型的任务运行起来的情况差异非常大，这导致业界并没有一些成熟的经验策略帮助开发人员参考。    
>
> > 这导致业界并没有一些成熟的经验策略帮助开发人员参考，下面罗列了几个业界线程池参数配置方案：

|      | 方案                                                         | 问题                                                         |
| ---- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 1    | ⬤ cup的个数（Ncpu）=当前机器的cpu核数<br/>⬤ cup的利用率(Rcpu)=目标cpu的利用率（0<= 利用率<=1） <br/>⬤ cpu的等待时间和计算时间百分比(W/C)=cpu的等待时间/cpu的计算时间 <br/>⬤ 保持处理器的理想利用率的最佳池大小是Nthreads=Ncpu * Rcpu * (1+W/C) | 出自《Java并发编程实践》该方案偏理论化。首先，线程计算的时间和等待的时间要如何确定呢？这个在实际开发中很难得到确切的值。       **另外计算出来的线程个数逼近线程实体的个数，Java线程池可以利用线程切换的方式最大程度利用CPU核数，这样计算出来的结果是非常偏离业务场景的** |
| 2    | **核心线程数=2\*cpu的个数**》<br/>**最大线程数**=25\*cpu的个数** | 没有考虑应用中往往使用多个线程池的情况，统一的配置明显不符合多样的业务场景。 |
| 3    | *核心线程数=Tps\*time**最大线程数=tps\*time\*(1.7-2)*        | 这种计算方式，考虑到了业务场景，但是该模型是在假定流量平均分布得出的。业务场景的流量往往是随机的，这样不符合真实情况。 |

 

### 3.3.1、动态化线程池

#### 3.3.1.1、新流程图

> 调研了以上业界方案后，我们并没有得出通用的线程池计算方式，这样就导致了下面两个问题：         
>
> **1、程序开发期难以敲定合适的线程池参数配置**；         
>
> **2、程序运行期难以更改线程池参数，需要重新修改程序再重新上线，投入成本巨大**；



![image-20210730163623141](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730163623141.png)

#### 3.3.1.2、使用场景参数配置

> 线程池构造参数有8个，但是最核心的是3个：corePoolSize、maximumPoolSize，workQueue，它们最大程度地决定了线程池的任务分配和线程分配策略。考虑到在实际应用中我们获取并发性的场景主要是两种
>
> > **1、并行执行子任务，提高响应速度。这种情况下，应该使用同步队列，没有什么任务应该被缓存下来，而是应该立即执行**。            
> >
> > **2、并行执行大批次任务，提升吞吐量。**这种情况下，应该使用有界队列，使用队列去缓冲大批量的任务，队列容量必须声明，防止任务无限制堆积。所以线程池只需要提供这三个关键参数的配置，并且提供两种队列的选择，就可以满足绝大多数的业务需求，Less is More。



#### 3.3.1.3、功能架构

**动态调参：**支持线程池参数动态调整、界面化操作；包括修改线程池核心大小、最大核心大小、队列长度等；参数修改后及时生效。     

**任务监控：**支持应用粒度、线程池粒度、任务粒度监控；可以看到线程池的任务执行情况、最大任务执行时间、平均任务执行时间、`95`/`99`线等。         

⬤ 事前，线程池定义了“活跃度”这个概念，来让用户在发生`Reject`异常之前能够感知线程池负载问题，线程池活跃度计算公式为：线程池活跃度 = `activeCount`/`maximumPoolSize`。这个公式代表当活跃线程数趋向于`maximumPoolSize`的时候，代表线程负载趋高。       

⬤ 事中，也可以从两方面来看线程池的过载判定条件，一个是发生了`Reject`异常，一个是队列中有等待任务（支持定制阈值）。以上两种情况发生了都会触发告警，告警信息会通过大象推送给服务所关联的负责人。

   

**负载告警：**线程池队列任务积压到一定值的时候告知应用开发负责人；当线程池负载数达到一定阈值的时候会通过大象告知应用开发负责人。            

**操作监控：**创建/修改和删除线程池都会通知到应用的开发负责人。      

**操作日志：**可以查看线程池参数的修改记录，谁在什么时候修改了线程池参数、修改前的参数值是什么。       

**权限校验：**只有应用开发负责人才能够修改应用的线程池参数。



### 3.3.2、利特尔法则合理配置参数值

> 我们认为线程池是由队列连接的一个或多个服务提供程序，这样我们也可以通过科特尔法则来定义线程池大小。我们只需计算请求到达率和请求处理的平均时间。然后，将上述值放到利特尔法则（Little’s law）就可以算出系统平均请求数。     
>
> ⬤ 若请求数小于我们线程池的大小，就相应地减小线程池的大小。      
>
> ⬤ 与之相反，如果请求数大于线程池大小，事情就有点复杂了,因为不可能无限放大线程池线程数，也不能无限放大线程池队列数，太大了，服务会崩溃掉。所以最好的办法就是通过压力测试来合理的设置线程池大小。

![image-20210730164313732](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730164313732.png)



 实际业务场景参数：

⬤ tasks: 每秒的任务数，假设是500~1000    

⬤ taskcost: 每个任务花费的时间，假设为0.1s         

⬤ responsetime: 系统允许容忍的最大响应时间，假设为1s        



  结合利特尔法则做几个计算：

1、corePoolSize = 每秒需要多少个线程处理？     

⬤ **threadcount = tasks/(1/taskcost) =tasks\*taskcost** =(500~1000)*0.1 = 50~100 个线程。corePoolSize设置应该大于50        

⬤ 根据8020原则，如果80%的每秒任务数小于800，那么corePoolSize设置为80即可        *



**2、queueCapacity = (coreSizePool/taskcost)responsetime** = 80/0.1 * 1 = 800 （0.1s 线程池可处理80个任务，1s 线程池可处理800个任务）也就是说队列里的线程可以等待1s，超过了的需要新开线程来执行              

⬤  切记不能设置为Integer.MAX_VALUE，这样队列会很大，线程数只会保持在corePoolSize大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。        



**3、maxPoolSize = (max(tasks)- queueCapacity)/(1/taskcost)**（最大任务数-队列容量）/每个线程每秒处理能力 = 最大线程数）       

   

4、**rejectedExecutionHandler**：根据具体情况来决定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理。      

​        

**5、keepAliveTime和allowCoreThreadTimeout采用默认通常能满足**。













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
		id: 'TWtfKmIZF94rRFxQ',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

