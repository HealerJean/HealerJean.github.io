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



> 线程池的概念是`Executo`r这个接口`ExecutorService`继承了它，具体实现为`ThreadPoolExecutor`类，学习Java中的线程池，就可以直接学习对线程池的配置，就是对`ThreadPoolExecutor`构造函数的参数的配置

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



### 1.1.5、`BlockingQueue workQueue`：该线程池中的任务队列：维护着等待执行的Runnable对象

> 当所有的核心线程都在干活时，新添加的任务会被添加到这个队列中等待处理，如果队列满了，则新建非核心线程执行任务。 常用的`workQueue`类型：

>**发现如果阻塞队列数量>0，则使用 LinkedBlockingQueue，否则使用 SynchronousQueue。**



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



从结果可以看出，只有task0和task1两个任务被执行了。为什么只有task0和task1两个任务被执行了呢？   

过程是这样的：由于我们的任务队列的容量为1.当task0正在执行的时候，task1被提交到了队列中但是还没有执行，受队列容量的限制，submit提交的task2~task9就都被直接抛弃了。因此就只有task0和task1被执行了。



#### 1.1.7.2、`DiscardOldestPolicy`：

> 丢弃队列中最老的任务   

如果将拒绝策略改为：DiscardOldestPolicy(丢弃队列中比较久的任务)   

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

从结果可以看出，只有task0和task9被执行了。





#### 1.1.7.4、`CallerRunsPolicy`：将任务分给调用线程来执行

> 没有任务被抛弃，而是将由的任务分配到main线程中执行了



如果将拒绝策略改为：CallerRunsPolicy(即不用线程池中的线程执行，而是交给调用方来执行)   

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

> 减少创建和销毁线程的次数，每个工作线程可以多次使用，可根据系统情况调整执行的线程数量，防止消耗过多内存  

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

`isShutDown()`当调用`shutdown()`或`shutdownNow()`方法后返回为`true`。    

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

答：通过将线程池的状态改成`STOP`，当再将执行execute提交任务时，如果测试到状态不为`RUNNING`，则抛出`rejectedExecution`，从而达到阻止新任务提交的目的.<font color="red">（如果为RUNNING,这个线程没有抛出异常类型的阻塞，则继续阻塞，阻塞请看下个问题）</font>           



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



### 2.5.1、线程的interrupt

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

#### 2.6.1.1、submit中可以放三个参数  

```java

Future<?> submit(Runnable runnable);

<T> Future<T> submit(Callable<T> callable);

<T> Future<T> submit(Runnable var1, T result);
```



#### 2.6.1.2、分析下 Runable和Callable的不同  

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



**看到了下面，其实就大致可以知道，Runnable会在这里转化成Callable。我们来看下Executors.callable()**    

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



`submit()`返回的是一个`RunnableFuture`类对象，真正是通过`newTaskFor()`方法返回一个`new FutureTask()`对象。所以`submit()`返回的真正的对象是`FutureTask`对象。   

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



### 2.6.3、`isDone  `:  执行结束（完成/取消/异常）返回true



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
> true：会中断正在运行的任务（相当于停止）      
>
> false：会让线程正常执行至完成，取消的是还没有开始执行的任务，如果任务以及开始，则由它执行下去   



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

