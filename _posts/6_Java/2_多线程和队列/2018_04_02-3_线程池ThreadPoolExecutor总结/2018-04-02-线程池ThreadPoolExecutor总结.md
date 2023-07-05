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



> ⬤  线程池的概念是 `Executor` 这个接口 `ExecutorService` 继承了它，具体实现为 `ThreadPoolExecutor` 类，学习Java中的线程池，就可以直接学习对线程池的配置，就是对`ThreadPoolExecutor`构造函数的参数的配置    
>
> ⬤ `Executors` 类提供工厂方法用来创建不同类型的线程池

```java
public interface ExecutorService extends Executor {}
public abstract class AbstractExecutorService implements ExecutorService {}
public class ThreadPoolExecutor extends AbstractExecutorService {}
  
public interface ScheduledExecutorService extends ExecutorService {}
public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {}

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

>**发现如果阻塞队列数量 > 0，则使用 `LinkedBlockingQueue`，否则使用 `SynchronousQueue`。**



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

> 抛出异常的方式（ `AbortPolicy` ）丢弃任务并抛出 `RejectedExecutionException`   





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



## 1.3、线程池信息统计

| 方法           | 说明                                                         |
| -------------- | ------------------------------------------------------------ |
| 活动线程数     | executor.getActiveCount()                                    |
| 当前线程数     | executor.getPoolSize()                                       |
| 最大线程数     | executor.getMaximumPoolSize()                                |
| 历史最大线程数 | executor.getLargestPoolSize()                                |
| 线程池活跃度   | divide(executor.getActiveCount(), executor.getMaximumPoolSize()) |
| 任务完成数     | executor.getCompletedTaskCount()                             |
| 队列大小       | executor.getQueue().size() + executor.getQueue().remainingCapacity() |
| 当前排队线程数 | executor.getQueue().size()                                   |
| 队列剩余大小   | executor.getQueue().remainingCapacity()                      |
| 队列使用度     | divide(executor.getQueue().size(), executor.getQueue().size() + executor.getQueue().remainingCapacity()) |



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

例如，一个线程正在`sleep()`中，此时执行`shutdownNow()`，它向该线程发起`interrupt()`请求，而`sleep()`方法遇到有`interrupt()`请求时，会抛出`InterruptedException()`，并继续往下执行。在这里要提醒注意的是，在激活的任务中，如果有多个 `sleep()`,该方法只会中断第一个`sleep()`，而后面的仍然按照正常的执行逻辑进行。



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

> `V get(long timeOut, TimeUnit unit)`：获取结果，超时抛出异常

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



#### 2.6.2.1、 `Callable`

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



#### 2.6.2.2、`Runnable`

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



⬤ **等待状态的任务。此时调用cancel()方法不管传入true还是false都会标记为取消，任务依然保存在任务队列中，但当轮到此任务运行时会直接跳过**。     

⬤ **运行中的任务。此时传入true会中断正在执行的任务，传入false则不会中断。**    

⬤ **完成状态。此时cancel()不会起任何作用，因为任务已经完成了。**     



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

2、如果 `workerCount` < `corePoolSize`，则创建并启动一个线程来执行新提交的任务。       

3、如果 `workerCount` >= `corePoolSize`，且线程池内的阻塞队列未满，则将任务添加到该阻塞队列中。     

4、如果 `workerCount` >= `corePoolSiz`e && `workerCount` < `maximumPoolSiz`e，且线程池内的阻塞队列已满，则创建并启动一个线程来执行新提交的任务。        

5、如果 `workerCount` >= `maximumPoolSize`，并且线程池内的阻塞队列已满, 则根据拒绝策略来处理该任务, 默认的处理方式是直接抛异常。

![image-20210730160103636](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730160103636.png)



### 2.6.2、任务缓冲

> 任务缓冲模块是线程池能够管理任务的核心部分。线程池的本质是对任务和线程的管理，而做到这一点最关键的思想就是将任务和线程两者解耦，不让两者直接关联，才可以做后续的分配工作。线程池中是以生产者消费者模式，通过一个阻塞队列来实现的。阻塞队列缓存任务，工作线程从阻塞队列中获取任务。      



| 名称                    | 描述                                                         |
| :---------------------- | :----------------------------------------------------------- |
| `ArrayBlockingQueue`    | **一个用数组实现的有界阻塞队列**，此队列按照先进先出 ( `FIFO`)的原则对元素进行排序。支持公平锁和非公平锁。 |
| `LinkedBlockingQueue`   | **一个由链表结构组成的有界队列**，此队列按照先进先出( `FIFO` ) 的原则对元素进行排序。此队列的默认长度为 `Integer.MAX_VALUE`，所以默认创建的该队列有容量危险。 |
| `PriorityBlockingQueue` | **一个支持线程优先级排序的无界队列**，默认自然序进行排序，也可以自定义实现 `compareTo()` 方法来指定元素排序规则，不能保证同优先级元素的顺序。 |
| `DelayQueue`            | 一个实现`PriorityBlockingQueue`实现延迟获取的无界队列，在创建元素时，可以指定多久才能从队列中获取当前元素。只有延时期满后才能从队列中获取元素。 |
| `SynchronousQueue`      | **一个不存储元素的阻塞队列**，每一个 `put`操作必须等待 `take` 操作，否则不能添加元素。支持公平锁和非公平锁。`SynchronousQueue` 的一个使用场景是在线程池里。`Executors.newCachedThreadPool()`就使用了 `SynchronousQueue`，这个线程池根据需要（新任务到来时）创建新的线程，如果有空闲线程则会重复使用，线程空闲了 `60` 秒后会被回收。 |
| `LinkedTransferQueue`   | **一个由链表结构组成的无界阻塞队列**，相当于其它队列，`LinkedTransferQueue` 队列多了 `transfer`和 `tryTransfer`方法。 |
| `LinkedBlockingDeque`   | **一个由链表结构组成的双向阻塞队列**。队列头部和尾部都可以添加和移除元素，多线程并发时，可以将锁的竞争最多降到一半。 |



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





### 3.3.1、设置规则

> **第一阶段**，单 `CPU` 时代，单 `CPU` 在同一时间点，只能执行单一线程。比如，的某一刻 00:00:00 这一秒，只计算1＋1=2（假设cpu每秒计算一次）          
>
> **第二阶段**，单 `CPU` 多任务阶段，计算机在同一时间点，并行执行多个线程。但这并非真正意义上的同时执行，而是多个任务共享一个`CPU`，操作系统协调`CPU`在某个时间点，执行某个线程，因为`CPU`在线程之间切换比较快，给人的感觉，就好像多个任务在同时运行。比如，电脑开了两个程序 `qq` 和 `qq`音乐，假设这两个程序都只有一个线程。人能够感觉到`CPU`切换的频率是一秒一次，假设当前 `cpu`计算速度是1秒1次，那么我们就能明显感到卡顿，当聊天，点击发送按钮时候，qq音乐就会停止运行。当前cpu计算速度是1秒`100`次，也就是它能在一秒之内在这两个进程见切换100次，那么我们就感不到卡顿，觉得QQ和QQ音乐是同时在运行。                 
>
> **第三阶段**，多 `CPU` 多任务阶段，真正实现的，在同一时间点运行多个线程。具体到哪个线程在哪个 `CPU`执行，这就跟操作系统和`CPU`本身的设计有关了。



#### 3.3.1.1、设计原则

> 1、系统的资源状况（处理器的数目，内存容量，CPU使用率上限）         
>
> 2、线程所执行任务的特性（`cpu` 密集型，`i/o` 密集型）             
>
> 3、设计线程数要尽可能考虑其他所有进程内部线程数设置的情况。

**`CPU` 密集型线程：如果是 `CPU`密集型任务，就需要尽量压榨 `CPU`**，考虑到这类线程执行任务时消耗主要是处理器资源，我们可以将这类线程数设置为N<sub>cpu</sub>,有时候，因为`CPU`密集型线程也可能由于某些原因被切出，为了避免处理器资源浪费，可以为它添加一个额外的线程N<sub>cpu</sub>+1

**`I/O`密集型线程：**考虑到`I/O`操作可能导致上下文切换，为这样的线程设置过多的线程数会导致额外的系统开销，在 `I/O`密集型线程在等待I/O操作返回结果的时候不占用处理器资源。因此我们可以为每个处理器安排一个额外的线程来提高处理器资源的利用率。所以设置为N<sub>cpu</sub>*2

```java
public class NumMain {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
```

#### 3.3.1.2、验证 `cup`密集型 N<sub>cpu</sub>+1 是否可行

```java
import java.util.List;

public class CPUTypeTest implements Runnable {


    // 整体执行时间，包括在队列中等待的时间
    List<Long> wholeTimeList;
    // 真正执行时间
    List<Long> runTimeList;
    private long initStartTime = 0;

    /**
     * 构造函数
     * @param runTimeList
     * @param wholeTimeList
     */
    public CPUTypeTest(List<Long> runTimeList, List<Long> wholeTimeList) {
        initStartTime = System.currentTimeMillis();
        this.runTimeList = runTimeList;
        this.wholeTimeList = wholeTimeList;
    }

    /**
     * 判断素数
     * @param number
     * @return
     */
    public boolean isPrime(final int number) {
        if (number <= 1)
            return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0)
                return false;
        }
        return true;
    }

    /**
     * 計算素数
     * @return
     */
    public int countPrimes(final int lower, final int upper) {
        int total = 0;
        for (int i = lower; i <= upper; i++) {
            if (isPrime(i))
                total++;
        }
        return total;
    }

    public void run() {
        long start = System.currentTimeMillis();
        countPrimes(1, 1000000);
        long end = System.currentTimeMillis();

        long wholeTime = end - initStartTime;
        long runTime = end - start;
        wholeTimeList.add(wholeTime);
        runTimeList.add(runTime);
        System.out.println(" 单个线程花费时间：" + (end - start));
    }
}

```

**分析：**

> 测试代码在 `4` 核 intel i5 CPU 机器上的运行时间变化如下：

![image-20210730144823839](/Users/healerjean/Library/Application Support/typora-user-images/image-20210730144823839.png)

**总结：**

> 1、当线程数量太小，同一时间大量请求将被阻塞在线程队列中排队等待执行线程，此时 `CPU` 没有得到充分利用；         
>
> 2、当线程数量太大，被创建的执行线程同时在争取 `CPU` 资源，又会导致大量的上下文切换，从而增加线程的执行时间，影响了整体执行效率。             
>
> **3、通过测试可知，4~6 个线程数是最合适的。**



#### 3.3.1.3、验证 `i/o`密集型任务

> 这种任务应用起来，系统会用大部分的时间来处理 `I/O` 交互，而线程在处理 `I/O` 的时间段内不会占用 `CPU` 来处理，这时就可以将 `CPU` 交出给其它线程使用。因此在` I/O` 密集型任务的应用中，我们可以多配置一些线程，具体的计算方法是 `2N`。

```java
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

/**
* @author zhangyujin
* @date 2021/7/30  2:22 下午.
* @description
*/
public class IOTypeTest implements Runnable {


  // 整体执行时间，包括在队列中等待的时间
  Vector<Long> wholeTimeList;
  // 真正执行时间
  Vector<Long> runTimeList;
  private long initStartTime = 0;


  /**
   * 构造函数
   *
   * @param runTimeList
   * @param wholeTimeList
   */
  public IOTypeTest(Vector<Long> runTimeList, Vector<Long> wholeTimeList) {
      initStartTime = System.currentTimeMillis();
      this.runTimeList = runTimeList;
      this.wholeTimeList = wholeTimeList;
  }


  /**
   * IO 操作
   *
   * @return
   * @throws IOException
   */
  public void readAndWrite() throws IOException {
      File sourceFile = new File("D:/test.txt");
      // 创建输入流
      BufferedReader input = new BufferedReader(new FileReader(sourceFile));
      // 读取源文件, 写入到新的文件
      String line = null;
      while ((line = input.readLine()) != null) {
      //System.out.println(line);
      }
      // 关闭输入输出流
      input.close();
  }


  public void run() {
      long start = System.currentTimeMillis();
      try {
          readAndWrite();
      } catch (IOException e) {

      }
      long end = System.currentTimeMillis();
      long wholeTime = end - initStartTime;
      long runTime = end - start;
      wholeTimeList.add(wholeTime);
      runTimeList.add(runTime);
      System.out.println(" 单个线程花费时间：" + (end - start));
  }
}

```



**分析：**

![image-20210730145243354](/Users/healerjean/Library/Application Support/typora-user-images/image-20210730145243354.png)



**总结：**当线程数量在 8 时，线程平均执行时间是最佳的，这个线程数量和我们的计算公式所得的结果就差不多。



#### 3.2.3.4、非极端如何配置

> 看完以上两种情况下的线程计算方法，你可能还想说，在平常的应用场景中，我们常常遇不到这两种极端情况，那么碰上一些常规的业务操作      
>
> > 比如，通过一个线程池实现向用户定时推送消息的业务，我们又该如何设置线程池的数量呢？此时我们可以参考以下公式来计算线程数：
>
> **根据自己的业务场景，从“N+1”和“2N”两个公式中选出一个适合的，计算出一个大概的线程数量，之后通过实际压测，逐渐往“增大线程数量”和“减小线程数量”这两个方向调整，然后观察整体的处理时间变化，最终确定一个具体的线程数量**。





### 3.3.2、方案收集

#### 3.3.2.1、方案1

⬤ cup 的个数（Ncpu）= 当前机器的cpu核数（number of CPUs）   

⬤ cup 的利用率 (Rcpu) = 目标cpu的利用率（0 <=  利用率 <= 1 ）    

⬤ cpu 的等待时间和计算时间百分比 ( W / C ) = cpu 的等待时间 / cpu的计算时间    

⬤ 保持处理器的理想利用率的最佳池大小是 Nthreads=Ncpu*Rcpu*(1+W/C)

出自《Java并发编程实践》该方案偏理论化。     

问题：首先，线程计算的时间和等待的时间要如何确定呢？这个在实际开发中很难得到确切的值。另外计算出来的线程个数逼近线程实体的个数，Java线程池可以利用线程切换的方式最大程度利用CPU核数，这样计算出来的结果是非常偏离业务场景的 

**任务类型：`CPU` 密集型任务，则线程数 = `Ncpu` +1**

**任务类型：`I/O` 密集型任务，则线程数 =  (`W/C`+1)  *  `Ncpu`**  一般认为是 **2 * `Ncpu`**



#### 3.3.2.2、方案2

⬤  核心线程数 = 2 * `cpu` 的个数     

⬤ 最大线程数 = 25 * `cpu` 的个数

问题：没有考虑应用中往往使用多个线程池的情况，统一的配置明显不符合多样的业务场景。



#### 3.3.2.3、方案3

⬤ 核心线程数=Tps * time      

⬤ 最大线程数 = Tps  * time *(1.7-2)

**问题：这种计算方式，考虑到了业务场景，但是该模型是在假定流量平均分布得出的。业务场景的流量往往是随机的，这样不符合真实情况。**

 

### 3.3.1、动态化线程池

#### 3.3.1.1、新流程图

> 调研了以上业界方案后，我们并没有得出通用的线程池计算方式，这样就导致了下面两个问题：         
>
> **1、程序开发期难以敲定合适的线程池参数配置**；         
>
> **2、程序运行期难以更改线程池参数，需要重新修改程序再重新上线，投入成本巨大**；



![image-20210730163623141](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210730163623141.png)

#### 3.3.1.2、使用场景参数配置

> 线程池构造参数有8个，但是最核心的是3个：`corePoolSize`、`maximumPoolSize`，`workQueue`，它们最大程度地决定了线程池的任务分配和线程分配策略。考虑到在实际应用中我们获取并发性的场景主要是两种
>
> > **1、并行执行子任务，提高响应速度。这种情况下，应该使用同步队列，没有什么任务应该被缓存下来，而是应该立即执行**。            
> >
> > **2、并行执行大批次任务，提升吞吐量。**这种情况下，应该使用有界队列，使用队列去缓冲大批量的任务，队列容量必须声明，防止任务无限制堆积。所以线程池只需要提供这三个关键参数的配置，并且提供两种队列的选择，就可以满足绝大多数的业务需求，`Less`  `is`  `More`。



#### 3.3.1.3、功能架构

**动态调参：**支持线程池参数动态调整、界面化操作；包括修改线程池核心大小、最大核心大小、队列长度等；参数修改后及时生效。     

**任务监控：**支持应用粒度、线程池粒度、任务粒度监控；可以看到线程池的任务执行情况、最大任务执行时间、平均任务执行时间、`95`/`99`线等。         

⬤ 事前，线程池定义了“活跃度”这个概念，来让用户在发生 `Reject` 异常之前能够感知线程池负载问题，线程池活跃度计算公式为：线程池活跃度 = `activeCount` / `maximumPoolSize`。这个公式代表当活跃线程数趋向于`maximumPoolSize`的时候，代表线程负载趋高。       

⬤ 事中，也可以从两方面来看线程池的过载判定条件，一个是发生了 `Reject` 异常，一个是队列中有等待任务（支持定制阈值）。以上两种情况发生了都会触发告警，告警信息会推送给服务所关联的负责人。

   

**负载告警：**线程池队列任务积压到一定值的时候告知应用开发负责人；当线程池负载数达到一定阈值的时候会告知应用开发负责人。            

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

⬤ `tasks`: 每秒的任务数，假设是 `500` ~`1000`    

⬤ `taskcost`: 每个任务花费的时间，假设为 `0.1s`         

⬤ `responsetime`: 系统允许容忍的最大响应时间，假设为 `1s`        



结合利特尔法则做几个计算：

1、**`corePoolSize`** = 每秒需要多少个线程处理？     

⬤ **`threadcount` = `tasks` / ( 1 / `taskcost` )  = `tasks` \* `taskcost`**  = (500~1000)*0.1 = 50~100 个线程。 `corePoolSize` 设置应该大于 `50`        

⬤ 根据 `8020` 原则，如果 `80%` 的每秒任务数小于 `800`，那么 `corePoolSize` 设置为 `80`即可  



**2、`queueCapacity ` = ( `coreSizePool` / `taskcost`) * `responsetime`**  = 80 / 0.1 * 1 = 800 （ `0.1` 线程池可处理80个任务，`1s` 线程池可处理800个任务）也就是说队列里的线程可以等待 `1s`，超过了的需要新开线程来执行        

⬤  切记不能设置为 `Integer.MAX_VALU`E，这样队列会很大，线程数只会保持在 `corePoolSize`大小，当任务陡增时，不能新开线程来执行，响应时间会随之陡增。        



**3、`maxPoolSize` = ( `max` ( `tasks` ) - `queueCapacity` ) / ( 1 / `taskcost` )**（最大任务数-队列容量）/ 每个线程每秒处理能力 = 最大线程数）       

   

4、**`rejectedExecutionHandler`**：根据具体情况来决定，任务不重要可丢弃，任务重要则要利用一些缓冲机制来处理。      

​        

**5、`keepAliveTime` 和 `allowCoreThreadTimeout` 采用默认通常能满足**。





# 4、线程池类详解

```java
public interface ExecutorService extends Executor {}
public abstract class AbstractExecutorService implements ExecutorService {}
public class ThreadPoolExecutor extends AbstractExecutorService {}
  
public interface ScheduledExecutorService extends ExecutorService {}
public class ScheduledThreadPoolExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {}

```



## 4.1、接口 `Executor`

> `Executor` 接口中定义了`execute()` 方法，用来接收一个 `Runnable` 接口的对象，   
>
> ⬤ `Executor` 接口中 `execute()` 方法不返回任何结果，而 `ExecutorService` 接口中 `submit()` 方法可以通过一个 `Future` 对象返回运算结果

```java
public interface ExecutorService extends Executor {}
```



## 4.2、接口 `ExecutorService`

> `ExecutorService` 接口中定义的 `submit()` 方法可以接收 `Runnable` 和 `Callable` 接口对象    
>
> ⬤ `Executor` 接口中 `execute()` 方法不返回任何结果，而 `ExecutorService` 接口中 `submit()` 方法可以通过一个 `Future` 对象返回运算结果    
>
> ⬤  `Executor` 和 `ExecutorService` 除了允许客户端提交一个任务，`ExecutorService `还提供用来控制线程池的方法。比如：调用 `shutDown()` 方法终止线程池。

```java
public interface ExecutorService extends Executor {}
```



## 4.3、类 `Executors`

> `Executors` 类提供工厂方法用来创建不同类型的线程池

```
ExecutorService executorService = Executors.newSingleThreadExecutor();
```



## 4.4、类 `ThreadPoolExecutor`

> 这个类是 `JDK` 中的线程池类，继承自 `Executor`里面有一个 `execute()`方法，用来执行线程，线程池主要提供一个线程队列，队列中保存着所有等待状态的线程。    
>
> ⬤ 支持 `submit` 和 `execute`

```java
public class ThreadPoolExecutor extends AbstractExecutorService {
```



```java
package com.healerjean.proj.d05_线程池;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
* 线程池工具
*
* @author zhangyujin
* @date 2023-07-05 09:07:42
*/
@Slf4j
@Component
public class ThreadPoolUtils {
  /**
   * 线程池工具 - DEFAULT_THREAD_POOL_EXECUTOR
   */
  public static ThreadPoolExecutor DEFAULT_THREAD_POOL_EXECUTOR;


  static {
      DEFAULT_THREAD_POOL_EXECUTOR = buildDefaultThreadPoolExecutor();
  }

    /**
     * 构建默认线程池-ThreadPoolExecutor
     *
     * @return ThreadPoolExecutor
     */
    private static ThreadPoolExecutor buildDefaultThreadPoolExecutor() {
        int corePoolSize = 10;
        int maximumPoolSize = 100;
        long keepAliveTime = 60;
        TimeUnit unit = TimeUnit.SECONDS;
        AtomicInteger tag = new AtomicInteger(1);
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(500);
        ThreadFactory threadFactory = r -> new Thread(r, "defaultThreadPoolExecutor" + tag.incrementAndGet());
        RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, 
                                      threadFactory, handler);
    }

  
      @Test
    public void testThreadPoolExecutor() {
        ThreadPoolExecutor defaultThreadPoolExecutor = ThreadPoolUtils.DEFAULT_THREAD_POOL_EXECUTOR;
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            defaultThreadPoolExecutor.submit(() -> System.out.println(finalI +  " " + 
                                                                      Thread.currentThread().getName()));
        }
    }
}

```



## 4.5、类 `ThreadPoolTaskExecutor`

> 这个类则是 `spring` 包下的，是 `sring` 为我们提供的线程池类,可以使用基于xml配置的方式创建.   
>
> ⬤ 支持 `submit` 和 `execute`

```java
package com.healerjean.proj.d05_线程池;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
* 线程池工具
*
* @author zhangyujin
* @date 2023-07-05 09:07:42
*/
@Slf4j
@Component
public class ThreadPoolUtils {

  /**
   * 默认线程池工具 - DEFAULT_THREAD_POOL_TASK_EXECUTOR
   */
  public static ThreadPoolTaskExecutor DEFAULT_THREAD_POOL_TASK_EXECUTOR;

  static {
      DEFAULT_THREAD_POOL_TASK_EXECUTOR = buildDefaultThreadPoolTaskExecutor();
  }

/**
   * 构建默认线程池-ThreadPoolTaskExecutor
   *
   * @return ThreadPoolTaskExecutor
   */
  private static ThreadPoolTaskExecutor buildDefaultThreadPoolTaskExecutor() {
      ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
      // 核心线程数：线程池创建时候初始化的线程数
      taskExecutor.setCorePoolSize(10);
      // 最大线程数：只有在缓冲队列满了之后才会申请超过核心线程数的线程
      taskExecutor.setMaxPoolSize(100);
      // 线程池维护线程所允许的空闲时间：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
      taskExecutor.setKeepAliveSeconds(60);
      // 缓存队列：用来缓冲执行任务的队列
      taskExecutor.setQueueCapacity(500);
      // 线程工厂
      AtomicInteger tag = new AtomicInteger(1);
      taskExecutor.setThreadFactory(r -> new Thread(r, "defaultThreadPoolTaskExecutor" + 
                                                    tag.incrementAndGet()));
      // 拒绝策略由调用线程处理该任务
      taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
      // defaultThreadPoolTaskExecutor
      taskExecutor.setThreadNamePrefix("defaultThreadPoolTaskExecutor");

      // 调度器shutdown被调用时等待当前被调度的任务完成：用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
      taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
      //该方法用来设置线程池中任务的等待时间:如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
      taskExecutor.setAwaitTerminationSeconds(60);
      taskExecutor.initialize();
      log.info("Executor - threadPoolTaskExecutor injected!");
      return taskExecutor;
  }


   @Test
  public void testThreadPoolTaskExecutor() {
      ThreadPoolTaskExecutor defaultThreadPoolTaskExecutor = ThreadPoolUtils.DEFAULT_THREAD_POOL_TASK_EXECUTOR;
      for (int i = 0; i < 20; i++) {
          int finalI = i;
          defaultThreadPoolTaskExecutor.submit(() -> System.out.println(finalI + " " + 
                                                                        Thread.currentThread().getName()));
      }
  }



0 defaultThreadPoolTaskExecutor2
1 defaultThreadPoolTaskExecutor3
2 defaultThreadPoolTaskExecutor4
3 defaultThreadPoolTaskExecutor5
4 defaultThreadPoolTaskExecutor6
5 defaultThreadPoolTaskExecutor7
6 defaultThreadPoolTaskExecutor8
7 defaultThreadPoolTaskExecutor9
8 defaultThreadPoolTaskExecutor10
9 defaultThreadPoolTaskExecutor11
10 defaultThreadPoolTaskExecutor2
12 defaultThreadPoolTaskExecutor4
14 defaultThreadPoolTaskExecutor3
15 defaultThreadPoolTaskExecutor5
18 defaultThreadPoolTaskExecutor7
11 defaultThreadPoolTaskExecutor11
19 defaultThreadPoolTaskExecutor8
17 defaultThreadPoolTaskExecutor3
16 defaultThreadPoolTaskExecutor6
13 defaultThreadPoolTaskExecutor2
}

```



## 4.6、接口 `CompletionService`

> `CompletionService` 与 `ExecutorService` 类似都可以用来执行线程池的任务，`ExecutorService` 继承了 `Executor` 接口，而`CompletionService` 则是一个接口        

| 方法                                         | 说明                                                         |
| -------------------------------------------- | ------------------------------------------------------------ |
| `completionService.take()`                   | `take() `方法会阻塞线程，直到有已经完成的任务可用为止。      |
| `completionService.poll()`                   | `poll()`方法会立即返回一个已经完成的任务，如果没有已经完成的任务，它会返回 `null`； |
| `completionService.poll(5,TimeUnit.SECONDS)` | 阻塞线程等待指定时间，如果有完成结果返回，没有的直接返回null。 |

**原理：**

1、`CompletionService` 实际上可以看做是 `Executor` 和 `BlockingQueue`的结合体。`CompletionService` 在接收到要执行的任务时，通过类似 `BlockingQueue`的 `put` 和 `take` 获得任务执行的结果。`CompletionService` 的一个实现是`ExecutorCompletionService`，`ExecutorCompletionService` 把具体的计算任务交给 `Executor`完成。    

2、在实现上，`ExecutorCompletionService` 在构造函数中会创建一个 `BlockingQueue`（使用的基于链表的无界队列`LinkedBlockingQueue`），**该 `BlockingQueue` 的作用是保存 `Executor` 执行的结果**。当计算完成时，调用 `FutureTask` 的 `done` 方法。当提交一个任务到 `ExecutorCompletionService`时，首先将任务包装成 `QueueingFuture`，它是 `FutureTask`的一个子类，然后改 写 `FutureTask` 的 `done` 方法，之后把 `Executor` 执行的计算结果放入 `BlockingQueue`中。



**小结**：

1、相比 `ExecutorService`，`CompletionService` 可以更精确和简便地完成异步任务的执行     

2、`CompletionService` 的一个实现是 `ExecutorCompletionService`，它是 `Executor` 和 `BlockingQueue` 功能的融合体，`Executor`完成计算任务，`BlockingQueue`负责保存异步任务的执行结果    

3、在执行大量相互独立和同构的任务时，可以使用 `CompletionService`    

4、`CompletionService` 可以为任务的执行设置时限，主要是通过 `BlockingQueue`的  `poll` (long time,TimeUnit unit)为任务执行结果的取得限制时间，如果没有完成就取消任务



```java
public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
    // 核心线程数：线程池创建时候初始化的线程数
    taskExecutor.setCorePoolSize(10);
    // 最大线程数：只有在缓冲队列满了之后才会申请超过核心线程数的线程
    taskExecutor.setMaxPoolSize(100);
    // 缓存队列：用来缓冲执行任务的队列
    taskExecutor.setQueueCapacity(500);
    // 线程池维护线程所允许的空闲时间：当超过了核心线程出之外的线程在空闲时间到达之后会被销毁
    taskExecutor.setKeepAliveSeconds(60);
    // threadPoolTaskExecutor
    taskExecutor.setThreadNamePrefix("threadPoolTaskExecutor");
    // 调度器shutdown被调用时等待当前被调度的任务完成：用来设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
    taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
    //该方法用来设置线程池中任务的等待时间:如果超过这个时候还没有销毁就强制销毁，以确保应用最后能够被关闭，而不是阻塞住
    taskExecutor.setAwaitTerminationSeconds(60);
    taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    taskExecutor.initialize();
    log.info("Executor - threadPoolTaskExecutor injected!");
    return taskExecutor;
}

@Test
public void testTimeOut() throws InterruptedException, ExecutionException {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolTaskExecutor();

    CompletionService<String> completionService = new ExecutorCompletionService<>(threadPoolTaskExecutor);
    List<Future<String>> futures = Arrays.asList(
            completionService.submit(() -> {
                Thread.sleep(5000);
                return "5000";
            }),
            completionService.submit(() -> {
                Thread.sleep(1000);
                return "1000";
            }),
            completionService.submit(() -> {
                Thread.sleep(6000);
                return "6000";
            })
    );

    int timeOut = 2500;
    for (int i = 0; i < futures.size(); i++) {
        Future<String> future = completionService.poll(timeOut, TimeUnit.MILLISECONDS);
        if (future == null){
            System.out.println("获取任务超时");
            continue;
        }
        System.out.println(future.get());
    }
}
```



# 5、动态线程池

## 5.1、`ThreadPoolConfig`

```java
package com.jd.merchant.business.platform.core.service.util.threadpool;


import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;


/**
 * 线程池配置属性
 *
 * @author zhangyujin
 * @date 2023/6/13  18:01
 */
@Data
public class ThreadPoolConfig implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6763948033008435707L;

    /**
     * 线程名称
     */
    private String name;
    /**
     * 核心线程数
     */
    private int corePoolSize;
    /**
     * 最大线程数
     */
    private int maximumPoolSize;
    /**
     * 存活时间（秒）
     */
    private int keepAliveSeconds;
    /**
     * 队列长度
     */
    private int queueCapacity;
    /**
     * 拒绝策略名称
     */
    private String rejectPolicyName;

    /**
     *
     * 拒绝策略
     */
    private RejectedExecutionHandler rejectPolicy;

    /**
     * 队列类型名称
     */
    private String queueTypeName;

    /**
     * 队列类型
     */
    private BlockingQueue<Runnable> queueType;

    /**
     *
     */
    public ThreadPoolConfig() {
    }


    /**
     * ThreadPoolConfiguration
     *
     * @param name             name
     * @param corePoolSize     corePoolSize
     * @param maximumPoolSize  maximumPoolSize
     * @param keepAliveSeconds keepAliveSeconds
     * @param queueCapacity    queueCapacity
     * @param rejectPolicyEnum rejectPolicyEnum
     * @param queueTypeEnum    queueTypeEnum
     */
    public ThreadPoolConfig(String name, int corePoolSize, int maximumPoolSize, int keepAliveSeconds, int queueCapacity,
                            ThreadEnum.RejectPolicyEnum rejectPolicyEnum, ThreadEnum.QueueEnum queueTypeEnum) {
        this.name = name;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveSeconds = keepAliveSeconds;
        this.queueCapacity = queueCapacity;
        this.rejectPolicyName = rejectPolicyEnum.getCode();
        buildRejectedExecutionHandler(rejectPolicyEnum);
        buildQueueType(queueTypeEnum);
    }



    /**
     * 拒绝策略映射
     *
     * @param rejectPolicyEnum rejectPolicyEnum
     */
    public void buildRejectedExecutionHandler(ThreadEnum.RejectPolicyEnum rejectPolicyEnum) {
        if (Objects.isNull(rejectPolicyEnum)) {
            this.rejectPolicy = new CallerRunsPolicy();
            return;
        }
        RejectedExecutionHandler rejectedExecutionHandler;
        switch (rejectPolicyEnum) {
            case ABORT_POLICY:
                rejectedExecutionHandler = new AbortPolicy();
                break;
            case DISCARD_POLICY:
                rejectedExecutionHandler = new DiscardPolicy();
                break;
            case DISCARD_OLDEST_POLICY:
                rejectedExecutionHandler = new DiscardOldestPolicy();
                break;
            default:
                rejectedExecutionHandler = new CallerRunsPolicy();
        }
        this.rejectPolicy = rejectedExecutionHandler;
    }

    /**
     * 队列类型映射
     *
     * @param queueTypeEnum queueTypeEnum
     */
    public void buildQueueType(ThreadEnum.QueueEnum queueTypeEnum) {
        if (ThreadEnum.QueueEnum.SYNCHRONOUS_QUEUE == queueTypeEnum) {
            this.queueType = new SynchronousQueue<>();
            return;
        }
        //默认类型：自定义无界缓存等待队列
        this.queueType = new ResizeableCapacityLinkedBlockingQueue<>(this.queueCapacity);
    }
}

```

## 5.2、`ThreadEnum`

```java
package com.jd.merchant.business.platform.core.service.util.threadpool;

import java.util.Arrays;

/**
 * ThreadEnum
 *
 * @author zhangyujin
 * @date 2023/6/13  21:46.
 */
public interface ThreadEnum {

    /**
     * RejectPolicyEnum
     *
     * @author zhangyujin
     * @date 2023/4/14  11:33.
     */
    enum RejectPolicyEnum implements ThreadEnum {

        /**
         * RejectPolicyEnum
         */
        ABORT_POLICY("AbortPolicy", "丢弃任务并抛出RejectedExecutionException异常"),
        DISCARD_POLICY("DiscardPolicy", "丢弃任务，但是不抛出异常。"),
        DISCARD_OLDEST_POLICY("DiscardOldestPolicy", "丢弃队列最前面的任务，然后重新提交被拒绝的任务。"),
        CALLER_RUNS_POLICY("CallerRunsPolicy", "由调用线程处理该任务"),
        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;

        /**
         * RejectPolicyEnum
         *
         * @param code code
         * @param desc desc
         */
        RejectPolicyEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * RejectPolicyEnum
         *
         * @param code code
         * @return RejectPolicyEnum
         */
        public static RejectPolicyEnum toRejectPolicyEnum(String code) {
            return Arrays.stream(RejectPolicyEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }


    }


    /**
     * QueueEnum
     *
     * @author zhangyujin
     * @date 2023/4/14  11:33.
     */
    enum QueueEnum implements ThreadEnum {

        /**
         * QueueEnum
         */
        SYNCHRONOUS_QUEUE("SynchronousQueue", "无缓冲等待队列"),
        RESIZEABLE_CAPACITY_LINKED_BLOCKING_QUEUE("LinkedBlockingQueue", "无界缓存等待队列"),
        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;

        /**
         * QueueEnum
         *
         * @param code code
         * @param desc desc
         */
        QueueEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * QueueEnum
         *
         * @param code code
         * @return QueueEnum
         */
        public static QueueEnum toQueueEnum(String code) {
            return Arrays.stream(QueueEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }


    }


    /**
     * QueueEnum
     *
     * @author zhangyujin
     * @date 2023/4/14  11:33.
     */
    enum ThreadPoolEnum implements ThreadEnum {

        /**
         * QueueEnum
         */
        DEFAULT("default", "默认线程池名称"),
        RPC("rpc", "rpc线程池名称"),
        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;

        /**
         * QueueEnum
         *
         * @param code code
         * @param desc desc
         */
        ThreadPoolEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * ThreadPoolEnum
         *
         * @param code code
         * @return ThreadPoolEnum
         */
        public static ThreadPoolEnum toQueueEnum(String code) {
            return Arrays.stream(ThreadPoolEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

    }


}

```



## 5.3、`ResizeableCapacityLinkedBlockingQueue`

> 美团的方式是自定义了一个叫做 `ResizableCapacityLinkedBlockIngQueue` 的队列（主要就是把`LinkedBlockingQueue`的 `capacity` 字段的 `final`关键字修饰给去掉了，让它变为可变的）

```java


package com.jd.merchant.business.platform.core.service.util.threadpool;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * An optionally-bounded {@linkplain BlockingQueue blocking queue} based on
 * linked nodes.
 * This queue orders elements FIFO (first-in-first-out).
 * The <em>head</em> of the queue is that element that has been on the
 * queue the longest time.
 * The <em>tail</em> of the queue is that element that has been on the
 * queue the shortest time. New elements
 * are inserted at the tail of the queue, and the queue retrieval
 * operations obtain elements at the head of the queue.
 * Linked queues typically have higher throughput than array-based queues but
 * less predictable performance in most concurrent applications.
 *
 * <p>The optional capacity bound constructor argument serves as a
 * way to prevent excessive queue expansion. The capacity, if unspecified,
 * is equal to {@link Integer#MAX_VALUE}.  Linked nodes are
 * dynamically created upon each insertion unless this would bring the
 * queue above capacity.
 *
 * <p>This class and its iterator implement all of the
 * <em>optional</em> methods of the {@link Collection} and {@link
 * Iterator} interfaces.
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * @date
 */
public class ResizeableCapacityLinkedBlockingQueue<E> extends AbstractQueue<E>
        implements BlockingQueue<E>, java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -6903933977591709194L;

    /*
     * A variant of the "two lock queue" algorithm.  The putLock gates
     * entry to put (and offer), and has an associated condition for
     * waiting puts.  Similarly for the takeLock.  The "count" field
     * that they both rely on is maintained as an atomic to avoid
     * needing to get both locks in most cases. Also, to minimize need
     * for puts to get takeLock and vice-versa, cascading notifies are
     * used. When a put notices that it has enabled at least one take,
     * it signals taker. That taker in turn signals others if more
     * items have been entered since the signal. And symmetrically for
     * takes signalling puts. Operations such as remove(Object) and
     * iterators acquire both locks.
     *
     * Visibility between writers and readers is provided as follows:
     *
     * Whenever an element is enqueued, the putLock is acquired and
     * count updated.  A subsequent reader guarantees visibility to the
     * enqueued Node by either acquiring the putLock (via fullyLock)
     * or by acquiring the takeLock, and then reading n = count.get();
     * this gives visibility to the first n items.
     *
     * To implement weakly consistent iterators, it appears we need to
     * keep all Nodes GC-reachable from a predecessor dequeued Node.
     * That would cause two problems:
     * - allow a rogue Iterator to cause unbounded memory retention
     * - cause cross-generational linking of old Nodes to new Nodes if
     *   a Node was tenured while live, which generational GCs have a
     *   hard time dealing with, causing repeated major collections.
     * However, only non-deleted Nodes need to be reachable from
     * dequeued Nodes, and reachability does not necessarily have to
     * be of the kind understood by the GC.  We use the trick of
     * linking a Node that has just been dequeued to itself.  Such a
     * self-link implicitly means to advance to head.next.
     */

    /**
     * Linked list node class
     */
    static class Node<E> {
        /**
         *
         */
        E item;

        /**
         * One of:
         * - the real successor Node
         * - this Node, meaning the successor is head.next
         * - null, meaning there is no successor (this is the last node)
         */
        Node<E> next;
        /**
         *
         */
        Node(E x) { item = x; }
    }

    /** The capacity bound, or Integer.MAX_VALUE if none */
    private volatile int capacity;

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        while (count.get() <= capacity){
            this.capacity = capacity;
            break;
        }
    }

    /** Current number of elements */
    private final AtomicInteger count = new AtomicInteger();

    /**
     * Head of linked list.
     * Invariant: head.item == null
     */
    transient Node<E> head;

    /**
     * Tail of linked list.
     * Invariant: last.next == null
     */
    private transient Node<E> last;

    /** Lock held by take, poll, etc */
    private final ReentrantLock takeLock = new ReentrantLock();

    /** Wait queue for waiting takes */
    private final Condition notEmpty = takeLock.newCondition();

    /** Lock held by put, offer, etc */
    private final ReentrantLock putLock = new ReentrantLock();

    /** Wait queue for waiting puts */
    private final Condition notFull = putLock.newCondition();

    /**
     * Signals a waiting take. Called only from put/offer (which do not
     * otherwise ordinarily lock takeLock.)
     */
    private void signalNotEmpty() {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            notEmpty.signal();
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * Signals a waiting put. Called only from take/poll.
     */
    private void signalNotFull() {
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            notFull.signal();
        } finally {
            putLock.unlock();
        }
    }

    /**
     * Links node at end of queue.
     *
     * @param node the node
     */
    private void enqueue(Node<E> node) {
        // assert putLock.isHeldByCurrentThread();
        // assert last.next == null;
        last = last.next = node;
    }

    /**
     * Removes a node from head of queue.
     *
     * @return the node
     */
    private E dequeue() {
        // assert takeLock.isHeldByCurrentThread();
        // assert head.item == null;
        Node<E> h = head;
        Node<E> first = h.next;
        h.next = h; // help GC
        head = first;
        E x = first.item;
        first.item = null;
        return x;
    }

    /**
     * Locks to prevent both puts and takes.
     */
    void fullyLock() {
        putLock.lock();
        takeLock.lock();
    }

    /**
     * Unlocks to allow both puts and takes.
     */
    void fullyUnlock() {
        takeLock.unlock();
        putLock.unlock();
    }

//     /**
//      * Tells whether both locks are held by current thread.
//      */
//     boolean isFullyLocked() {
//         return (putLock.isHeldByCurrentThread() &&
//                 takeLock.isHeldByCurrentThread());
//     }

    /**
     * Creates a {@code LinkedBlockingQueue} with a capacity of
     * {@link Integer#MAX_VALUE}.
     */
    public ResizeableCapacityLinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    /**
     * Creates a {@code LinkedBlockingQueue} with the given (fixed) capacity.
     *
     * @param capacity the capacity of this queue
     * @throws IllegalArgumentException if {@code capacity} is not greater
     *         than zero
     */
    public ResizeableCapacityLinkedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        last = head = new Node<E>(null);
    }

    /**
     * Creates a {@code LinkedBlockingQueue} with a capacity of
     * {@link Integer#MAX_VALUE}, initially containing the elements of the
     * given collection,
     * added in traversal order of the collection's iterator.
     *
     * @param c the collection of elements to initially contain
     * @throws NullPointerException if the specified collection or any
     *         of its elements are null
     */
    public ResizeableCapacityLinkedBlockingQueue(Collection<? extends E> c) {
        this(Integer.MAX_VALUE);
        final ReentrantLock putLock = this.putLock;
        putLock.lock(); // Never contended, but necessary for visibility
        try {
            int n = 0;
            for (E e : c) {
                if (e == null) {
                    throw new NullPointerException();
                }
                if (n == capacity) {
                    throw new IllegalStateException("Queue full");
                }
                enqueue(new Node<E>(e));
                ++n;
            }
            count.set(n);
        } finally {
            putLock.unlock();
        }
    }

    // this doc comment is overridden to remove the reference to collections
    // greater in size than Integer.MAX_VALUE
    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    @Override
    public int size() {
        return count.get();
    }

    // this doc comment is a modified copy of the inherited doc comment,
    // without the reference to unlimited queues.
    /**
     * Returns the number of additional elements that this queue can ideally
     * (in the absence of memory or resource constraints) accept without
     * blocking. This is always equal to the initial capacity of this queue
     * less the current {@code size} of this queue.
     *
     * <p>Note that you <em>cannot</em> always tell if an attempt to insert
     * an element will succeed by inspecting {@code remainingCapacity}
     * because it may be the case that another thread is about to
     * insert or remove an element.
     */
    @Override
    public int remainingCapacity() {
        return capacity - count.get();
    }

    /**
     * Inserts the specified element at the tail of this queue, waiting if
     * necessary for space to become available.
     *
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public void put(E e) throws InterruptedException {
        if (e == null) {
            throw new NullPointerException();
        }
        // Note: convention in all put/take/etc is to preset local var
        // holding count negative to indicate failure unless set.
        int c = -1;
        Node<E> node = new Node<E>(e);
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            /*
             * Note that count is used in wait guard even though it is
             * not protected by lock. This works because count can
             * only decrease at this point (all other puts are shut
             * out by lock), and we (or some other waiting put) are
             * signalled if it ever changes from capacity. Similarly
             * for all other uses of count in other wait guards.
             */
            while (count.get() == capacity) {
                notFull.await();
            }
            enqueue(node);
            c = count.getAndIncrement();
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0) {
            signalNotEmpty();
        }
    }

    /**
     * Inserts the specified element at the tail of this queue, waiting if
     * necessary up to the specified wait time for space to become available.
     *
     * @return {@code true} if successful, or {@code false} if
     *         the specified waiting time elapses before space is available
     * @throws InterruptedException {@inheritDoc}
     * @throws NullPointerException {@inheritDoc}
     */
    @Override
    public boolean offer(E e, long timeout, TimeUnit unit)
            throws InterruptedException {

        if (e == null) {
            throw new NullPointerException();
        }
        long nanos = unit.toNanos(timeout);
        int c = -1;
        final ReentrantLock putLock = this.putLock;
        final AtomicInteger count = this.count;
        putLock.lockInterruptibly();
        try {
            while (count.get() == capacity) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = notFull.awaitNanos(nanos);
            }
            enqueue(new Node<E>(e));
            c = count.getAndIncrement();
            if (c + 1 < capacity) {
                notFull.signal();
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0) {
            signalNotEmpty();
        }
        return true;
    }

    /**
     * Inserts the specified element at the tail of this queue if it is
     * possible to do so immediately without exceeding the queue's capacity,
     * returning {@code true} upon success and {@code false} if this queue
     * is full.
     * When using a capacity-restricted queue, this method is generally
     * preferable to method {@link BlockingQueue#add add}, which can fail to
     * insert an element only by throwing an exception.
     *
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean offer(E e) {
        if (e == null) {
            throw new NullPointerException();
        }
        final AtomicInteger count = this.count;
        if (count.get() == capacity) {
            return false;
        }
        int c = -1;
        Node<E> node = new Node<E>(e);
        final ReentrantLock putLock = this.putLock;
        putLock.lock();
        try {
            if (count.get() < capacity) {
                enqueue(node);
                c = count.getAndIncrement();
                if (c + 1 < capacity) {
                    notFull.signal();
                }
            }
        } finally {
            putLock.unlock();
        }
        if (c == 0) {
            signalNotEmpty();
        }
        return c >= 0;
    }

    @Override
    public E take() throws InterruptedException {
        E x;
        int c = -1;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                notEmpty.await();
            }
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity) {
            signalNotFull();
        }
        return x;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E x = null;
        int c = -1;
        long nanos = unit.toNanos(timeout);
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lockInterruptibly();
        try {
            while (count.get() == 0) {
                if (nanos <= 0) {
                    return null;
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
            x = dequeue();
            c = count.getAndDecrement();
            if (c > 1) {
                notEmpty.signal();
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity) {
            signalNotFull();
        }
        return x;
    }

    @Override
    public E poll() {
        final AtomicInteger count = this.count;
        if (count.get() == 0) {
            return null;
        }
        E x = null;
        int c = -1;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            if (count.get() > 0) {
                x = dequeue();
                c = count.getAndDecrement();
                if (c > 1) {
                    notEmpty.signal();
                }
            }
        } finally {
            takeLock.unlock();
        }
        if (c == capacity) {
            signalNotFull();
        }
        return x;
    }

    @Override
    public E peek() {
        if (count.get() == 0) {
            return null;
        }
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            Node<E> first = head.next;
            if (first == null) {
                return null;
            } else {
                return first.item;
            }
        } finally {
            takeLock.unlock();
        }
    }

    /**
     * Unlinks interior Node p with predecessor trail.
     */
    void unlink(Node<E> p, Node<E> trail) {
        // assert isFullyLocked();
        // p.next is not changed, to allow iterators that are
        // traversing p to maintain their weak-consistency guarantee.
        p.item = null;
        trail.next = p.next;
        if (last == p) {
            last = trail;
        }
        if (count.getAndDecrement() == capacity) {
            notFull.signal();
        }
    }

    /**
     * Removes a single instance of the specified element from this queue,
     * if it is present.  More formally, removes an element {@code e} such
     * that {@code o.equals(e)}, if this queue contains one or more such
     * elements.
     * Returns {@code true} if this queue contained the specified element
     * (or equivalently, if this queue changed as a result of the call).
     *
     * @param o element to be removed from this queue, if present
     * @return {@code true} if this queue changed as a result of the call
     */
    @Override
    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        fullyLock();
        try {
            for (Node<E> trail = head, p = trail.next;
                 p != null;
                 trail = p, p = p.next) {
                if (o.equals(p.item)) {
                    unlink(p, trail);
                    return true;
                }
            }
            return false;
        } finally {
            fullyUnlock();
        }
    }

    /**
     * Returns {@code true} if this queue contains the specified element.
     * More formally, returns {@code true} if and only if this queue contains
     * at least one element {@code e} such that {@code o.equals(e)}.
     *
     * @param o object to be checked for containment in this queue
     * @return {@code true} if this queue contains the specified element
     */
    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }
        fullyLock();
        try {
            for (Node<E> p = head.next; p != null; p = p.next) {
                if (o.equals(p.item)) {
                    return true;
                }
            }
            return false;
        } finally {
            fullyUnlock();
        }
    }

    /**
     * Returns an array containing all of the elements in this queue, in
     * proper sequence.
     *
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this queue.  (In other words, this method must allocate
     * a new array).  The caller is thus free to modify the returned array.
     *
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this queue
     */
    @Override
    public Object[] toArray() {
        fullyLock();
        try {
            int size = count.get();
            Object[] a = new Object[size];
            int k = 0;
            for (Node<E> p = head.next; p != null; p = p.next) {
                a[k++] = p.item;
            }
            return a;
        } finally {
            fullyUnlock();
        }
    }

    /**
     * Returns an array containing all of the elements in this queue, in
     * proper sequence; the runtime type of the returned array is that of
     * the specified array.  If the queue fits in the specified array, it
     * is returned therein.  Otherwise, a new array is allocated with the
     * runtime type of the specified array and the size of this queue.
     *
     * <p>If this queue fits in the specified array with room to spare
     * (i.e., the array has more elements than this queue), the element in
     * the array immediately following the end of the queue is set to
     * {@code null}.
     *
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     *
     * <p>Suppose {@code x} is a queue known to contain only strings.
     * The following code can be used to dump the queue into a newly
     * allocated array of {@code String}:
     *
     *  <pre> {@code String[] y = x.toArray(new String[0]);}</pre>
     *
     * Note that {@code toArray(new Object[0])} is identical in function to
     * {@code toArray()}.
     *
     * @param a the array into which the elements of the queue are to
     *          be stored, if it is big enough; otherwise, a new array of the
     *          same runtime type is allocated for this purpose
     * @return an array containing all of the elements in this queue
     * @throws ArrayStoreException if the runtime type of the specified array
     *         is not a supertype of the runtime type of every element in
     *         this queue
     * @throws NullPointerException if the specified array is null
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        fullyLock();
        try {
            int size = count.get();
            if (a.length < size) {
                a = (T[])java.lang.reflect.Array.newInstance
                        (a.getClass().getComponentType(), size);
            }

            int k = 0;
            for (Node<E> p = head.next; p != null; p = p.next) {
                a[k++] = (T)p.item;
            }
            if (a.length > k) {
                a[k] = null;
            }
            return a;
        } finally {
            fullyUnlock();
        }
    }
    @Override
    public String toString() {
        fullyLock();
        try {
            Node<E> p = head.next;
            if (p == null) {
                return "[]";
            }

            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for (;;) {
                E e = p.item;
                sb.append(e == this ? "(this Collection)" : e);
                p = p.next;
                if (p == null) {
                    return sb.append(']').toString();
                }
                sb.append(',').append(' ');
            }
        } finally {
            fullyUnlock();
        }
    }

    /**
     * Atomically removes all of the elements from this queue.
     * The queue will be empty after this call returns.
     */
    @Override
    public void clear() {
        fullyLock();
        try {
            for (Node<E> p, h = head; (p = h.next) != null; h = p) {
                h.next = h;
                p.item = null;
            }
            head = last;
            // assert head.item == null && head.next == null;
            if (count.getAndSet(0) == capacity) {
                notFull.signal();
            }
        } finally {
            fullyUnlock();
        }
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    @Override
    public int drainTo(Collection<? super E> c) {
        return drainTo(c, Integer.MAX_VALUE);
    }

    /**
     * @throws UnsupportedOperationException {@inheritDoc}
     * @throws ClassCastException            {@inheritDoc}
     * @throws NullPointerException          {@inheritDoc}
     * @throws IllegalArgumentException      {@inheritDoc}
     */
    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        if (c == null) {
            throw new NullPointerException();
        }
        if (c == this) {
            throw new IllegalArgumentException();
        }
        if (maxElements <= 0) {
            return 0;
        }
        boolean signalNotFull = false;
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        try {
            int n = Math.min(maxElements, count.get());
            // count.get provides visibility to first n Nodes
            Node<E> h = head;
            int i = 0;
            try {
                while (i < n) {
                    Node<E> p = h.next;
                    c.add(p.item);
                    p.item = null;
                    h.next = h;
                    h = p;
                    ++i;
                }
                return n;
            } finally {
                // Restore invariants even if c.add() threw
                if (i > 0) {
                    // assert h.item == null;
                    head = h;
                    signalNotFull = (count.getAndAdd(-i) == capacity);
                }
            }
        } finally {
            takeLock.unlock();
            if (signalNotFull) {
                signalNotFull();
            }
        }
    }

    /**
     * Returns an iterator over the elements in this queue in proper sequence.
     * The elements will be returned in order from first (head) to last (tail).
     *
     * <p>The returned iterator is
     * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
     *
     * @return an iterator over the elements in this queue in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        /*
         * Basic weakly-consistent iterator.  At all times hold the next
         * item to hand out so that if hasNext() reports true, we will
         * still have it to return even if lost race with a take etc.
         */
        /**
         *
         */
        private Node<E> current;
        /**
         *
         */
        private Node<E> lastRet;
        /**
         *
         */
        private E currentElement;
        /**
         *
         */
        Itr() {
            fullyLock();
            try {
                current = head.next;
                if (current != null) {
                    currentElement = current.item;
                }
            } finally {
                fullyUnlock();
            }
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next live successor of p, or null if no such.
         *
         * Unlike other traversal methods, iterators need to handle both:
         * - dequeued nodes (p.next == p)
         * - (possibly multiple) interior removed nodes (p.item == null)
         */
        private Node<E> nextNode(Node<E> p) {
            for (;;) {
                Node<E> s = p.next;
                if (s == p) {
                    return head.next;
                }
                if (s == null || s.item != null) {
                    return s;
                }
                p = s;
            }
        }

        @Override
        public E next() {
            fullyLock();
            try {
                if (current == null) {
                    throw new NoSuchElementException();
                }
                E x = currentElement;
                lastRet = current;
                current = nextNode(current);
                currentElement = (current == null) ? null : current.item;
                return x;
            } finally {
                fullyUnlock();
            }
        }

        @Override
        public void remove() {
            if (lastRet == null) {
                throw new IllegalStateException();
            }
            fullyLock();
            try {
                Node<E> node = lastRet;
                lastRet = null;
                for (Node<E> trail = head, p = trail.next;
                     p != null;
                     trail = p, p = p.next) {
                    if (p == node) {
                        unlink(p, trail);
                        break;
                    }
                }
            } finally {
                fullyUnlock();
            }
        }
    }

    /** A customized variant of Spliterators.IteratorSpliterator */
    static final class LBQSpliterator<E> implements Spliterator<E> {
        /**
         *
         */
        static final int MAX_BATCH = 1 << 25;  // max batch array size;
        /**
         *
         */
        final ResizeableCapacityLinkedBlockingQueue<E> queue;
        /**
         *
         */
        Node<E> current;    // current node; null until initialized
        /**
         *
         */
        int batch;          // batch size for splits
        /**
         *
         */
        boolean exhausted;  // true when no more nodes
        /**
         *
         */
        long est;           // size estimate
        /**
         *
         */
        LBQSpliterator(ResizeableCapacityLinkedBlockingQueue<E> queue) {
            this.queue = queue;
            this.est = queue.size();
        }

        @Override
        public long estimateSize() { return est; }

        @Override
        public Spliterator<E> trySplit() {
            Node<E> h;
            final ResizeableCapacityLinkedBlockingQueue<E> q = this.queue;
            int b = batch;
            int n = (b <= 0) ? 1 : (b >= MAX_BATCH) ? MAX_BATCH : b + 1;
            if (!exhausted &&
                    ((h = current) != null || (h = q.head.next) != null) &&
                    h.next != null) {
                Object[] a = new Object[n];
                int i = 0;
                Node<E> p = current;
                q.fullyLock();
                try {
                    if (p != null || (p = q.head.next) != null) {
                        do {
                            if ((a[i] = p.item) != null) {
                                ++i;
                            }
                        } while ((p = p.next) != null && i < n);
                    }
                } finally {
                    q.fullyUnlock();
                }
                if ((current = p) == null) {
                    est = 0L;
                    exhausted = true;
                }
                else if ((est -= i) < 0L) {
                    est = 0L;
                }
                if (i > 0) {
                    batch = i;
                    return Spliterators.spliterator
                            (a, 0, i, Spliterator.ORDERED | Spliterator.NONNULL |
                                    Spliterator.CONCURRENT);
                }
            }
            return null;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final ResizeableCapacityLinkedBlockingQueue<E> q = this.queue;
            if (!exhausted) {
                exhausted = true;
                Node<E> p = current;
                do {
                    E e = null;
                    q.fullyLock();
                    try {
                        if (p == null) {
                            p = q.head.next;
                        }
                        while (p != null) {
                            e = p.item;
                            p = p.next;
                            if (e != null) {
                                break;
                            }
                        }
                    } finally {
                        q.fullyUnlock();
                    }
                    if (e != null) {
                        action.accept(e);
                    }
                } while (p != null);
            }
        }

        @Override
        public boolean tryAdvance(Consumer<? super E> action) {
            if (action == null) {
                throw new NullPointerException();
            }
            final ResizeableCapacityLinkedBlockingQueue<E> q = this.queue;
            if (!exhausted) {
                E e = null;
                q.fullyLock();
                try {
                    if (current == null) {
                        current = q.head.next;
                    }
                    while (current != null) {
                        e = current.item;
                        current = current.next;
                        if (e != null) {
                            break;
                        }
                    }
                } finally {
                    q.fullyUnlock();
                }
                if (current == null) {
                    exhausted = true;
                }
                if (e != null) {
                    action.accept(e);
                    return true;
                }
            }
            return false;
        }

        @Override
        public int characteristics() {
            return Spliterator.ORDERED | Spliterator.NONNULL |
                    Spliterator.CONCURRENT;
        }
    }

    /**
     * Returns a {@link Spliterator} over the elements in this queue.
     *
     * <p>The returned spliterator is
     * <a href="package-summary.html#Weakly"><i>weakly consistent</i></a>.
     *
     * <p>The {@code Spliterator} reports {@link Spliterator#CONCURRENT},
     * {@link Spliterator#ORDERED}, and {@link Spliterator#NONNULL}.
     *
     * @implNote
     * The {@code Spliterator} implements {@code trySplit} to permit limited
     * parallelism.
     *
     * @return a {@code Spliterator} over the elements in this queue
     * @since 1.8
     */
    @Override
    public Spliterator<E> spliterator() {
        return new LBQSpliterator<E>(this);
    }

    /**
     * Saves this queue to a stream (that is, serializes it).
     *
     * @param s the stream
     * @throws java.io.IOException if an I/O error occurs
     * @serialData The capacity is emitted (int), followed by all of
     * its elements (each an {@code Object}) in the proper order,
     * followed by a null
     */
    private void writeObject(java.io.ObjectOutputStream s)
            throws java.io.IOException {

        fullyLock();
        try {
            // Write out any hidden stuff, plus capacity
            s.defaultWriteObject();

            // Write out all elements in the proper order.
            for (Node<E> p = head.next; p != null; p = p.next) {
                s.writeObject(p.item);
            }

            // Use trailing null as sentinel
            s.writeObject(null);
        } finally {
            fullyUnlock();
        }
    }

    /**
     * Reconstitutes this queue from a stream (that is, deserializes it).
     * @param s the stream
     * @throws ClassNotFoundException if the class of a serialized object
     *         could not be found
     * @throws java.io.IOException if an I/O error occurs
     */
    private void readObject(java.io.ObjectInputStream s)
            throws java.io.IOException, ClassNotFoundException {
        // Read in capacity, and any hidden stuff
        s.defaultReadObject();

        count.set(0);
        last = head = new Node<E>(null);

        // Read in all elements and place in queue
        for (;;) {
            @SuppressWarnings("unchecked")
            E item = (E)s.readObject();
            if (item == null) {
                break;
            }
            add(item);
        }
    }
}

```



## 5.4、`ThreadPoolUtils`

```java
package com.jd.merchant.business.platform.core.service.util.threadpool;

import com.alibaba.fastjson.JSONObject;
import com.jd.merchant.business.platform.core.common.UmpConstants;
import com.jd.merchant.business.platform.core.common.property.DuccThreadPoolProp;
import com.jd.ump.profiler.proxy.Profiler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ThreadPoolUtils
 *
 * @author zhangyujin
 * @date 2023/6/13  18:17
 */
@Slf4j
@Component
public class ThreadPoolUtils {

    /**
     * 统一配置中心ducc
     */
    @Resource
    private DuccThreadPoolProp dcThreadPoolProp;

    /**
     * 锁
     */
    private final static ReentrantLock LOCK = new ReentrantLock();


    /**
     * 默认线程池
     */
    public static ThreadPoolExecutor DEFAULT_THREAD_POOL_EXECUTOR;

    /**
     * 自定义线程池 rpc
     */
    public static ThreadPoolExecutor RPC_THREAD_POOL_EXECUTOR;


    /**
     * 初始化线程池并启动定时监测任务
     */
    @PostConstruct
    private void init() {
        DEFAULT_THREAD_POOL_EXECUTOR = buildThreadPoolExecutor(ThreadEnum.ThreadPoolEnum.DEFAULT);
        RPC_THREAD_POOL_EXECUTOR = buildThreadPoolExecutor(ThreadEnum.ThreadPoolEnum.RPC);

        //定时任务
        Runnable poolThreadTask = () -> {
            updateExecutorConfig(DEFAULT_THREAD_POOL_EXECUTOR, ThreadEnum.ThreadPoolEnum.DEFAULT);
            updateExecutorConfig(RPC_THREAD_POOL_EXECUTOR, ThreadEnum.ThreadPoolEnum.RPC);
        };

        ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(
                1,
                new BasicThreadFactory.Builder().namingPattern("thread-pool-monitor-task-%d").daemon(true).build());
        scheduledExecutor.scheduleAtFixedRate(poolThreadTask,
                5000,
                1000,
                TimeUnit.MILLISECONDS);
    }


    /**
     * 更新线程池参数并上报ump
     *
     * @param executor       executor
     * @param threadPoolEnum threadPoolEnum
     */
    private void updateExecutorConfig(ThreadPoolExecutor executor, ThreadEnum.ThreadPoolEnum threadPoolEnum) {
        String poolName = threadPoolEnum.getCode();
        log.info("updateExecutorConfig start");
        LOCK.lock();
        try {
            ThreadPoolConfig conf = getThreadPoolConfigFromDucc(poolName);
            if (conf.getCorePoolSize() != 0 && conf.getCorePoolSize() != executor.getCorePoolSize()) {
                executor.setCorePoolSize(conf.getCorePoolSize());
            }
            if (conf.getMaximumPoolSize() != 0 && conf.getMaximumPoolSize() != executor.getMaximumPoolSize()) {
                executor.setMaximumPoolSize(conf.getMaximumPoolSize());
            }
            if (conf.getKeepAliveSeconds() != 0 && conf.getKeepAliveSeconds() != executor.getKeepAliveTime(TimeUnit.SECONDS)) {
                executor.setKeepAliveTime(conf.getKeepAliveSeconds(), TimeUnit.SECONDS);
            }
            if (executor.getQueue() instanceof ResizeableCapacityLinkedBlockingQueue) {
                ResizeableCapacityLinkedBlockingQueue queue = (ResizeableCapacityLinkedBlockingQueue) executor.getQueue();
                int capacity = queue.size() + queue.remainingCapacity();
                if (conf.getQueueCapacity() != 0 && conf.getQueueCapacity() != capacity) {
                    queue.setCapacity(conf.getQueueCapacity());
                }
            }
            if (conf.getRejectPolicy() != null && conf.getRejectPolicy() != executor.getRejectedExecutionHandler()) {
                executor.setRejectedExecutionHandler(conf.getRejectPolicy());
            }
            //统计线程池信息
            Map<String, Number> dataMap = buildCustomerProperty(executor);
            log.debug("ThreadPoolUtils updateExecutorConfig poolName:{},dataMap:{}", poolName, dataMap);
            Profiler.sourceDataByNum(UmpConstants.THREAD_POOL_UMP_KEY + poolName, dataMap);
        } catch (Exception e) {
            log.error("updateExecutorConfig error:{}", e.getMessage(), e);
        } finally {
            LOCK.unlock();
        }
        log.info("updateExecutorConfig end");
    }

    /**
     * 自定义上报属性，字段值与ump监控配置"自定义字段"一致
     *
     * @param executor executor
     * @return Map<String, Number>
     */
    private Map<String, Number> buildCustomerProperty(ThreadPoolExecutor executor) {
        Map<String, Number> dataMap = new HashMap<>(16);
        dataMap.put("corePoolSize", executor.getCorePoolSize());
        dataMap.put("maximumPoolSize", executor.getMaximumPoolSize());
        dataMap.put("poolSize", executor.getPoolSize());
        dataMap.put("activeCount", executor.getActiveCount());
        dataMap.put("queueCapacity", executor.getQueue().size() + executor.getQueue().remainingCapacity());
        dataMap.put("queueSize", executor.getQueue().size());
        dataMap.put("remainingCapacity", executor.getQueue().remainingCapacity());
        dataMap.put("completedTaskCount", executor.getCompletedTaskCount());
        dataMap.put("largestPoolSize", executor.getLargestPoolSize());
        return dataMap;
    }


    /**
     * 根据名称获取线程池配置
     *
     * @param poolName poolName
     * @return ThreadPoolConfiguration
     */
    private ThreadPoolConfig getThreadPoolConfigFromDucc(String poolName) {
        String threadPoolConfigs = dcThreadPoolProp.getThreadPoolConfigs();
        log.debug("[ThreadPoolUtils#getThreadPoolConfFromDucc] configStr:{}", threadPoolConfigs);
        List<ThreadPoolConfig> confList = JSONObject.parseArray(threadPoolConfigs, ThreadPoolConfig.class);
        for (ThreadPoolConfig conf : confList) {
            if (poolName.equals(conf.getName())) {
                //拒绝策略映射
                ThreadEnum.RejectPolicyEnum rejectPolicyEnum = ThreadEnum.RejectPolicyEnum.toRejectPolicyEnum(conf.getRejectPolicyName());
                conf.buildRejectedExecutionHandler(rejectPolicyEnum);
                ThreadEnum.QueueEnum queueEnum = ThreadEnum.QueueEnum.toQueueEnum(conf.getQueueTypeName());
                //队列类型
                conf.buildQueueType(queueEnum);
                return conf;
            }
        }
        return getDefaultConf();
    }


    /**
     * getDefaultConf
     *
     * @return ThreadPoolConfiguration
     */
    private static ThreadPoolConfig getDefaultConf() {
        return new ThreadPoolConfig(
                ThreadEnum.ThreadPoolEnum.DEFAULT.getCode(),
                20,
                20,
                60,
                1024, ThreadEnum.RejectPolicyEnum.CALLER_RUNS_POLICY,
                ThreadEnum.QueueEnum.RESIZEABLE_CAPACITY_LINKED_BLOCKING_QUEUE);
    }


    /**
     * 根据名称配置构建线程池
     *
     * @param threadPoolEnum threadPoolEnum
     * @return ThreadPoolExecutor
     */
    private ThreadPoolExecutor buildThreadPoolExecutor(ThreadEnum.ThreadPoolEnum threadPoolEnum) {
        ThreadPoolConfig threadPoolConfig = getThreadPoolConfigFromDucc(threadPoolEnum.getCode());
        log.info("[ThreadPoolUtils#buildThreadPoolExecutor] threadPoolConf:{}", threadPoolConfig);
        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(),
                threadPoolConfig.getMaximumPoolSize(),
                threadPoolConfig.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                threadPoolConfig.getQueueType(),
                r -> new Thread(r, threadPoolEnum.getCode()),
                threadPoolConfig.getRejectPolicy()
        );
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
		id: 'TWtfKmIZF94rRFxQ',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

