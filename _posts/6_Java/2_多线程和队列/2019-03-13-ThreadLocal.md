---
title: ThreadLocal
date: 2019-03-13 03:33:00
tags: 
- Thread
category: 
- Thread
description: ThreadLocal
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)                



# 1、`ThreadLocal`

>`ThreadLocal` 翻译成中文比较准确的叫法应该是：线程局部变量。

```java
package com.duodian.admore.zhaobutong.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContextHolder {

    /** 将openId保存到上下文 */
    private final static ThreadLocal<Long> userIdCcontainer = new ThreadLocal<>();

    /**
     * 把userId保存到上下文
     * @param userId
     */
    public static void setUserId(Long userId){
        userIdCcontainer.set(userId);
    }


    /**
     * 获取用户ID
     * @return
     */
    public static Long getUserId(){
        return userIdCcontainer.get();
    }

 

    /**
     * 清空上下文
     */
    public static void clear(){
        userIdCcontainer.remove();
    }
}

```

### 1、`#set `方法

> 线程隔离的秘密，就在于`ThreadLocalMap`这个类。`ThreadLocalMap`是`ThreadLocal`类的一个静态内部类，它实现了键值对的设置和获取（对比 `Map` 对象来理解），          

> 1、先获取当前线程，然后通过`#getMap(Thread t)`方法获取一个和当前线程相关的`ThreadLocalMap`，然后将变量的值设置到这个`ThreadLocalMap`对象中，当然如果获取到的`ThreadLocalMap`对象为空，就通过`createMap`方法创建。        
>
>  2、每个线程中都有一个独立的`ThreadLocalMap`副本，它所存储的值，只能被当前线程读取和修改。`ThreadLocal`类通过操作每一个线程特有的`ThreadLocalMap`副本，从而实现了变量访问在不同线程中的隔离。因为每个线程的变量都是自己特有的，完全不会有并发错误。还有一点就是   `ThreadLocalMap`存储的键值对中的键是 `this` 对象指向的`ThreadLocal`对象，而值就是你所设置的对象了



```java

public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value); //如果没有就创建这个map
}	


ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}	



void createMap(Thread t, T firstValue) {	 //this是ThreadLocal对象
    t.threadLocals = new ThreadLocalMap(this, firstValue); 
}

ThreadLocal.ThreadLocalMap threadLocals = null; （在Thread类中）

```



### 2、`#get`方法

```java
public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
        ThreadLocalMap.Entry e = map.getEntry(this);
        if (e != null)
            return (T)e.value;
    }
    return setInitialValue();
}


ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
}

private T setInitialValue() {
    T value = initialValue();
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) 
        map.set(this, value);
    else	
        createMap(t, value);
    return value;
}

protected T initialValue() {
    return null;
}

```



### 3、总结：

> 当我们调用`get`方法的时候，其实每个当前线程中都有一个`ThreadLocalMap`。每次获取或者设置都是对该ThreadLocal进行的操作，是与其他线程分开的。从本质来讲，就是每个线程都维护了一个`map`，而这个`map`的`key`就是`threadLocal`，而值就是我们set的那个值        



每次线程在`get`的时候，都从自己的变量中取值，既然从自己的变量中取值，那肯定就不存在线程安全问题，    

**总体来讲，`ThreadLocal` 这个变量的状态根本没有发生变化，他仅仅是充当一个`key`的角色，另外提供给每一个线程一个初始值**    



4、应用场景：当很多线程需要多次使用同一个对象，并且需要该对象具有相同初始化值的时候最适合使用ThreadLocal。



## 2、`ThreadLocal` 与 `Synchronized`区别


相同：`ThreadLocal` 和 线程同步机制都是为了解决多线程中相同变量的访问冲突问题。    

不同：`Synchronized` 同步机制采用了“以时间换空间”的方式，仅提供一份变量，让不同的线程排队访问；而ThreadLocal采用了“以空间换时间”的方式，每一个线程都提供了一份变量，因此可以同时访问而互不影响。    

以时间换空间->即枷锁方式，某个区域代码或变量只有一份节省了内存，但是会形成很多线程等待现象，因此浪费了时间而节省了空间。   

以空间换时间->为每一个线程提供一份变量，多开销一些内存，但是呢线程不用等待，可以一起执行而相互之间没有影响。    

小结：ThreadLocal是解决线程安全问题一个很好的思路，它通过为每个线程提供一个独立的变量副本解决了变量并发访问的冲突问题。在很多情况下，ThreadLocal比直接使用synchronized同步机制解决线程安全问题更简单，更方便，且结果程序拥有更高的并发性。



## 3、`ThreadLocalMap` `key`弱引用

### 3.1、为什么使用弱引用

`key` 使用强引用：当`ThreadLocalMap`的`key`为强引用回收`ThreadLocal`时，因为`ThreadLocalMap`还持有`ThreadLocal`的强引用，如果没有`key` 使用弱引用：手动删除，`ThreadLocal`不会被回收，导致`Entry`内存泄漏。 譬如 设置：`ThreadLocal=null` 以后，应该会被回收的，但实际情况是`ThreadLocalMap`还有一个强引用，导致无法回收           

`key` 使用弱引用：当`ThreadLocalMap`的`key`为弱引用回收`ThreadLocal`时，由于`ThreadLocalMap`持有`ThreadLocal`的弱引用，即使没有手动删除，`ThreadLocal`也会被回收。当`key`为`null`，在下一次`ThreadLocalMap`调用`set()` ,`get()`，`remove()`方法的时候会被清除`value`值。（`ThreadLocal` 对象只是作为`ThreadLocalMap`的一个`key`而存在的，现在它被回收了，但是它对应的`value`并没有被回收，内存泄露依然存在！而且`key`被删了之后，变成了`null`，`value`更是无法被访问到了！针对这一问题，`ThreadLocalMap`类的设计本身已经有了这一问题的解决方案，那就是在每次`get()`/`set()`/`remove()` `ThreadLocalMap`中的值的时候，会自动清理`key`为 `null` 的 `value`。如此一来，value也能被回收了）        

​       

譬如 设置：`ThreadLocal=null` 以后，强引用已没有，`ThreadLocalMap`还有一个弱引用，下次`GC`就会被回收



```

在ThreadLocal的get(),set()的时候都会清除线程ThreadLocalMap里所有key为null的value。 

ThreadLocal的remove()方法会先将Entry中对key的弱引用断开，设置为null，然后再清除对应的key为null的value。 
```






### 3.2、使用和注意事项

**1、`JVM`利用设置`ThreadLocalMap`的Key为弱引用，来避免内存泄露。`JVM`利用调用`remove`、`get`、`set`方法的时候，回收弱引用。当`ThreadLocal`存储很多`Key`为`null`的`Entry`的时候，而不再去调用remove、get、set方法，那么将导致内存泄漏。**   

2、当使用`static` `ThreadLocal`的时候，延长`ThreadLocal`的生命周期，那也可能导致内存泄漏。因为，`static`变量在类未加载的时候，它就已经加载，当线程结束的时候，`static`变量不一定会回收。那么，比起普通成员变量使用的时候才加载，static的生命周期加长将更容易导致内存泄漏危机。    

3、在线程消失之后，其线程局部实例的所有副本都会被垃圾回收，这样的话就不会造成影响 

 

### 3.3、线程池中的使用

开始前重新set值，以及结束后要记得remove





# 2、`InheritableThreadLocal`

> 其实现原理就是在创建子线程将父线程当前存在的本地线程变量拷贝到子线程的本地线程变量中

## 2.1、样例分析

```java
public class InheritableThreadLocalDemo {

    private static final InheritableThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    @Test
    public void test(){

        INHERITABLE_THREAD_LOCAL.set("HealerJean");
        System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
        //main:HealerJean

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
            // Thread-0:HealerJean

            INHERITABLE_THREAD_LOCAL.remove();
            System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
            // Thread-0:null
        }).start();


        //休眠5s等待，子线程执行完毕
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
        }

        System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
        //main:HealerJean
        //由此可见，子线程Thread-0 创建后移除的时候。是移除的自己的内容
    }
}

```

## 2.2、原理分析

> 可以看到 `inheritableThreadLocals` 还是一个 `ThreadLocalMap`，只不过是在 `Thread`  初始化的时候 把父`Thread`的`inheritableThreadLocals` 变量 `copy` 了一份给自己。同样借助` ThreadLocalMap` 子线程可以获取到父线程的所有变量。
>  根据它的实现，我们也可以看到它的缺点，也就是在线程复用的线程池中是没有办法使用的 。      
>
> 缺点：   
>
> > `InheritableThreadLocal` 的核心思想即：创建线程的时候将父线程中的线程上下文变量值复制到子线程 ,在平时开发中，不可能每一个异步请求都 `new` 一个单独的子线程来处理（内存会被撑爆），因此需要使用到线程池，线程池中即存在线程复用的情况，假设线程池中后面创建的线程中的上下文数据否都来自线程池中被复用的线程，这就出现父子线程的上下文变量复制混乱的情况



```java
public class Thread implements Runnable {

  ThreadLocalMap threadLocals;
  ThreadLocalMap inheritableThreadLocals;
  ……
}
```

```java
//new Thead（）方法下层
if (inheritThreadLocals && parent.inheritableThreadLocals != null) {
  this.inheritableThreadLocals = ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
}
```



## 2.3、线程池使用-缺陷



```java
public class InheritableThreadLocalWeaknessDemo {

    private static final InheritableThreadLocal<Integer> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();
    private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);


    @Test
    public void test() {
        //模拟同时10个web请求，一个请求一个线程
        for (int i = 0; i < 10; i++) {
            new TomcatThread(i).start();
        }
          try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class TomcatThread extends Thread {
        private int index;

        public TomcatThread(int index) {
            this.index = index;
        }

        @Override
        public void run() {
            String curThreadName = Thread.currentThread().getName();
            System.out.println(curThreadName + ":" + index);
            INHERITABLE_THREAD_LOCAL.set(index);
            threadPool.submit(new BusinessThread(curThreadName));
        }
    }

    static class BusinessThread implements Runnable {
        private String parentThreadName;

        public BusinessThread(String parentThreadName) {
            this.parentThreadName = parentThreadName;
        }

        @Override
        public void run() {
            System.out.println("parent:" + parentThreadName + ":" + INHERITABLE_THREAD_LOCAL.get());
        }
    }
}


```

### 2.3.1、日志打印

> 子线程中输出的父线程名称与下标 `index` 无法一一对应，即 `ThreadLocal` 线程上下文变量出现混乱的情况，应用需要的实际上是把 任务提交给线程池时的 `ThreadLocal`值传递到 任务执行时         
>
> 这种情况就需要使用阿里开源的 `TransmittableThreadLocal`来解决了

```
Thread-0:0
Thread-1:1
Thread-2:2
Thread-3:3
Thread-4:4
Thread-5:5
Thread-6:6
parent:Thread-0:0
parent:Thread-3:3
parent:Thread-5:5
parent:Thread-1:1
parent:Thread-4:5
parent:Thread-6:1
Thread-7:7
parent:Thread-2:2
parent:Thread-7:2
Thread-8:8
parent:Thread-8:3
Thread-9:9
parent:Thread-9:1
```



# 3、`TransmittableThreadLocal`

> `TTL`是用来解决`ITL解`决不了的问题而诞生的，所以`TTL`一定是支持父线程的本地变量传递给子线程这种基本操作的，`ITL`也可以做到，但是前面有讲过，`ITL`在线程池的模式下，就没办法再正确传递了，所以`TTL`做出的改进就是即便是在线程池模式下，也可以很好的将父线程本地变量传递下去

## 3.1、样例

### 3.1.1、样例1

```java
public class TransmittableThreadLocalDemo {

  private static final TransmittableThreadLocal<Integer> INHERITABLE_THREAD_LOCAL = new TransmittableThreadLocal<>();
  private static final ExecutorService threadPool = Executors.newFixedThreadPool(5);

  @Test
  public void test() {

    //模拟同时10个web请求，一个请求一个线程
    for (int i = 0; i < 10; i++) {
      new TomcatThread(i).start();
    }

    try {
      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  static class TomcatThread extends Thread {
    //线程下标
    int index;

    public TomcatThread(int index) {
      this.index = index;
    }

    @Override
    public void run() {
      String curThreadName = Thread.currentThread().getName();
      System.out.println(curThreadName + ":" + index);
      INHERITABLE_THREAD_LOCAL.set(index);

      threadPool.submit(TtlRunnable.get(new BusinessThread(curThreadName)));
    }
  }

  static class BusinessThread implements Runnable {
    //父进程名称
    private String parentThreadName;

    public BusinessThread(String parentThreadName) {
      this.parentThreadName = parentThreadName;
    }

    @Override
    public void run() {
      System.out.println("parent:" + parentThreadName + ":" + INHERITABLE_THREAD_LOCAL.get());
    }
  }
}
```

#### 3.1.1.1、日志打印

```java
Thread-0:0
Thread-1:1
Thread-3:3
Thread-4:4
Thread-2:2
Thread-5:5
Thread-6:6
Thread-7:7
Thread-8:8
Thread-9:9
parent:Thread-6:6
parent:Thread-4:4
parent:Thread-0:0
parent:Thread-2:2
parent:Thread-5:5
parent:Thread-8:8
parent:Thread-7:7
parent:Thread-9:9
parent:Thread-3:3
parent:Thread-1:1
```



### 3.1.2、样例2-线程池方式

```java
package com.hlj.moudle.thread.D13_ThreadLocal.demo.TransmittableThreadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransmittableThreadLocalTestMaIn {

  /**
     * 需要注意的是，使用TTL的时候，要想传递的值不出问题，线程池必须得用TTL加一层代理
     */
  private static ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));

  private static TransmittableThreadLocal ttl = new TransmittableThreadLocal<>();

  /**
     * 1、线程池的方式
     */
  @Test
  public void test() {

    new Thread(() -> {

      String mainThreadName = Thread.currentThread().getName();

      ttl.set(1);

      executorService.execute(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      });

      executorService.execute(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      });

      executorService.execute(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      });

      sleep(1L); //确保上面的会在tl.set执行之前执行
      ttl.set(2); // 等上面的线程池第一次启用完了，父线程再给自己赋值

      executorService.execute(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      });

      executorService.execute(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      });

      executorService.execute(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      });

      System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));

    }).start();


    sleep(5000L);
  }



  private static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}


```



#### 3.1.2.1、日志打印

> 两个主线程里都使用线程池异步，而且值在主线程里还发生过改变，测试结果展示一切正常，由此可以知道TTL在使用线程池的情况下，也可以很好的完成传递，而且不会发生错乱。那么是不是对普通线程异步也有这么好的支撑呢？

```
本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-pool-1-thread-1, 变量值=1
本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-pool-1-thread-2, 变量值=1
本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-pool-1-thread-1, 变量值=1
主线程名称-Thread-0, 变量值=2
本地变量改变之后(2), 父线程名称-Thread-0, 子线程名称-pool-1-thread-1, 变量值=2
本地变量改变之后(2), 父线程名称-Thread-0, 子线程名称-pool-1-thread-2, 变量值=2
本地变量改变之后(2), 父线程名称-Thread-0, 子线程名称-pool-1-thread-1, 变量值=2

```



### 3.1.3、样例3-异步方式

```java
package com.hlj.moudle.thread.D13_ThreadLocal.demo.TransmittableThreadLocal;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransmittableThreadLocalTestMaIn {

  /**
     * 需要注意的是，使用TTL的时候，要想传递的值不出问题，线程池必须得用TTL加一层代理
     */
  private static ExecutorService executorService = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(2));

  private static TransmittableThreadLocal ttl = new TransmittableThreadLocal<>();




  /**
     * 2、普通异步线程
     */
  @Test
  public void test2() {

    new Thread(() -> {

      String mainThreadName = Thread.currentThread().getName();

      ttl.set(1);

      new Thread(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      }).start();

      new Thread(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      }).start();

      new Thread(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      }).start();

      sleep(1L); //确保上面的会在tl.set执行之前执行
      ttl.set(2); // 等上面的线程池第一次启用完了，父线程再给自己赋值

      new Thread(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      }).start();

      new Thread(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      }).start();

      new Thread(() -> {
        sleep(1L);
        System.out.println(String.format("本地变量改变之后(2), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      }).start();

      System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));

    }).start();


    sleep(5000L);

  }


  private static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}


```

#### 3.1.3.1、日志打印

> 可以看到，达到了跟线程池测试一致的结果。

```
本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-Thread-1, 变量值=1
本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-Thread-2, 变量值=1
本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-Thread-3, 变量值=1
主线程名称-Thread-0, 变量值=2
本地变量改变之后(2), 父线程名称-Thread-0, 子线程名称-Thread-4, 变量值=2
本地变量改变之后(2), 父线程名称-Thread-0, 子线程名称-Thread-5, 变量值=2
本地变量改变之后(2), 父线程名称-Thread-0, 子线程名称-Thread-6, 变量值=2
```



### 3.2.4、样例4-父子线程remove

```java
/**
     * 1、主线程remove，不会影响子线程的值
     */
@Test
public void test3() {

  new Thread(() -> {
    String mainThreadName = Thread.currentThread().getName();

    ttl.set(1);

    executorService.execute(() -> {
      sleep(1L);
      System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      sleep(4000L);
      System.out.println(String.format("主线程remove,本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      //主线程remove,本地变量改变之前(1), 父线程名称-Thread-0, 子线程名称-pool-1-thread-1, 变量值=1
      //可以看到主线程remove了，子线程还有值
    });

    sleep(2000L);//确保子线程进入sleep 主线程才remove
    ttl.remove();
    System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));
    sleep(10000L); //等待主线程执行

  }).start();
  sleep(20000L);
}


/**
     * 1、子线程remove，不会影响子线程的值
     */
@Test
public void test4() {

  new Thread(() -> {
    String mainThreadName = Thread.currentThread().getName();

    ttl.set(1);

    executorService.execute(() -> {
      sleep(1L);
      System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
      ttl.remove();
      System.out.println(String.format("本地变量改变之前(1), 父线程名称-%s, 子线程名称-%s, 变量值=%s", mainThreadName, Thread.currentThread().getName(), ttl.get()));
    });

    sleep(2000L);//确保子线程执行结束
    System.out.println(String.format("主线程名称-%s, 变量值=%s", Thread.currentThread().getName(), ttl.get()));

  }).start();
  sleep(20000L);
}

```



## 3.2、原理分析

### 3.2.1、`TTL`继承`ITL`

> `TTL` 继承了 `ITL` ，意味着`TTL`首先具备 `ITL` 的功能。

```java

public class TransmittableThreadLocal extends InheritableThreadLocal

```

### 3.2.2、重要属性holder

> 1、这是一个 `ITL` 类型的对象，持有一个全局的 `WeakMap`（`weakMap`的`key`是弱引用，同`TL`一样，也是为了解决内存泄漏的问题），里面存放了`TTL`对象               
>
> 2、重写了`initialValue`和`childValue`方法，尤其是`childValue`，可以看到在即将异步时父线程的属性。是直接作为初始化值赋值给子线程的本地变量对象（`TTL`）的

```java

private static InheritableThreadLocal<Map<TransmittableThreadLocal<?>, ?>> holder =
  new InheritableThreadLocal<Map<TransmittableThreadLocal<?>, ?>>() {
  @Override
  protected Map<TransmittableThreadLocal<?>, ?> initialValue() {
    return new WeakHashMap<TransmittableThreadLocal<?>, Object>();
  }

  @Override
  protected Map<TransmittableThreadLocal<?>, ?> childValue(Map<TransmittableThreadLocal<?>, ?> parentValue) {
    return new WeakHashMap<TransmittableThreadLocal<?>, Object>(parentValue);
  }
};

```



### 3.2.3、`Set`和`Get`

```java
//下面的方法均属于TTL类
@Override
public final void set(T value) {
  super.set(value);
  if (null == value) removeValue();
  else addValue();
}

@Override
public final T get() {
  T value = super.get();
  if (null != value) addValue();
  return value;
}

private void removeValue() {
  holder.get().remove(this); //从holder持有的map对象中移除
}

private void addValue() {
  if (!holder.get().containsKey(this)) {
    holder.get().put(this, null); //从holder持有的map对象中添加
  }
}

```

### 3.2.4、线程池 `execute` 方法

> `TTL`里先了解上述的几个方法及对象，可以看出，单纯的使用TTL是达不到支持线程池本地变量的传递的，通过第一部分的例子，可以发现，除了要启用`TTL`，还需要通过`TtlExecutors.getTtlExecutorService`包装一下线程池才可以，那么，下面就来看看在程序即将通过线程池异步的时候，`TTL`帮我们做了哪些操作（这一部分是TTL支持线程池传递的核心部分）：

#### 3.2.4.1、线程池包装类`ExecutorTtlWrapper`

> 结合代码，大致知道了在线程池异步之前需要做的事情，其实就是把当前父线程里的本地变量取出来，然后赋值给`Rannable`包装类里的`capturedRef`属性， 
>
> 到此为止，下面会发生什么，我们大致上可以猜出来了，接下来大概率会在`run`方法里，将这些捕获到的值赋给子线程的`holder`赋对应的`TTL`值，那么我们继续往下看`Rannable`包装类里的run方法是怎么实现的：



```java
// 此方法属于线程池包装类ExecutorTtlWrapper
@Override
public void execute(@Nonnull Runnable command) {
  executor.execute(TtlRunnable.get(command)); //这里会把Rannable包装一层，这是关键，有些逻辑处理，需要在run之前执行
}

// 对应上面的get方法，返回一个TtlRunnable对象，属于TtLRannable包装类
@Nullable
public static TtlRunnable get(@Nullable Runnable runnable) {
  return get(runnable, false, false);
}

// 对应上面的get方法
@Nullable
public static TtlRunnable get(@Nullable Runnable runnable, boolean releaseTtlValueReferenceAfterRun, boolean idempotent) {
  if (null == runnable) return null;

  if (runnable instanceof TtlEnhanced) { // 若发现已经是目标类型了（说明已经被包装过了）直接返回
    // avoid redundant decoration, and ensure idempotency
    if (idempotent) return (TtlRunnable) runnable;
    else throw new IllegalStateException("Already TtlRunnable!");
  }
  return new TtlRunnable(runnable, releaseTtlValueReferenceAfterRun); //最终初始化
}

// 对应上面的TtlRunnable方法
private TtlRunnable(@Nonnull Runnable runnable, boolean releaseTtlValueReferenceAfterRun) {
  this.capturedRef = new AtomicReference<Object>(capture()); //这里将捕获后的父线程本地变量存储在当前对象的capturedRef里
  this.runnable = runnable;
  this.releaseTtlValueReferenceAfterRun = releaseTtlValueReferenceAfterRun;
}

// 对应上面的capture方法，用于捕获当前线程（父线程）里的本地变量，此方法属于TTL的静态内部类Transmitter
@Nonnull
public static Object capture() {
  Map<TransmittableThreadLocal<?>, Object> captured = new HashMap<TransmittableThreadLocal<?>, Object>();
  for (TransmittableThreadLocal<?> threadLocal : holder.get().keySet()) { // holder里目前存放的k-v里的key，就是需要传给子线程的TTL对象
    captured.put(threadLocal, threadLocal.copyValue());
  }
  return captured; // 这里返回的这个对象，就是当前将要使用线程池异步出来的子线程，所继承的本地变量合集
}

// 对应上面的copyValue，简单的将TTL对象里的值返回（结合之前的源码可以知道get方法其实就是获取当前线程（父线程）里的值，调用super.get方法）
private T copyValue() {
  return copy(get());
}
protected T copy(T parentValue) {
  return parentValue;
}

```

#### 3.2.3.2、`Rannable` 的包装类 `TtlRunnable`

> `TTL` 在异步任务执行前，会先进行赋值操作（就是拿着异步发生时捕获到的父线程的本地变量，赋给自己），当任务执行完，就恢复原生的自己本身的线程变量值

```java
//run方法属于Rannable的包装类TtlRunnable

@Override
public void run() {
  Object captured = capturedRef.get(); // 获取由之前捕获到的父线程变量集
  if (captured == null || releaseTtlValueReferenceAfterRun && !capturedRef.compareAndSet(captured, null)) {
    throw new IllegalStateException("TTL value reference is released after run!");
  }

  /**
   * 重点方法replay，此方法用来给当前子线程赋本地变量，返回的backup是此子线程原来就有的本地变量值（原生本地变量，下面会详细讲），
   * backup用于恢复数据（如果任务执行完毕，意味着该子线程会归还线程池，那么需要将其原生本地变量属性恢复）
    */
  Object backup = replay(captured);
  try {
    runnable.run(); // 执行异步逻辑
  } finally {
    restore(backup); // 结合上面对于replay的解释，不难理解，这个方法就是用来恢复原有值的
  }
}


//下面的方法均属于TTL的静态内部类Transmittable

@Nonnull
public static Object replay(@Nonnull Object captured) {
  @SuppressWarnings("unchecked")
  Map<TransmittableThreadLocal<?>, Object> capturedMap = (Map<TransmittableThreadLocal<?>, Object>) captured; //使用此线程异步时捕获到的父线程里的本地变量值
  Map<TransmittableThreadLocal<?>, Object> backup = new HashMap<TransmittableThreadLocal<?>, Object>(); //当前线程原生的本地变量，用于使用完线程后恢复用

  //注意：这里循环的是当前子线程原生的本地变量集合，与本方法相反，restore方法里循环这个holder是指：该线程运行期间产生的变量+父线程继承来的变量
  for (Iterator<? extends Map.Entry<TransmittableThreadLocal<?>, ?>> iterator = holder.get().entrySet().iterator();
       iterator.hasNext(); ) {
    Map.Entry<TransmittableThreadLocal<?>, ?> next = iterator.next();
    TransmittableThreadLocal<?> threadLocal = next.getKey();

    backup.put(threadLocal, threadLocal.get()); // 所有原生的本地变量都暂时存储在backup里，用于之后恢复用

    /**
             * 检查，如果捕获到的线程变量里，不包含当前原生变量值，则从当前原生变量里清除掉，对应的线程本地变量也清掉
             * 这就是为什么会将原生变量保存在backup里的原因，为了恢复原生值使用
             * 那么，为什么这里要清除掉呢？因为从使用这个子线程做异步那里，捕获到的本地变量并不包含原生的变量，当前线程
             * 在做任务时的首要目标，是将父线程里的变量完全传递给任务，如果不清除这个子线程原生的本地变量，
             * 意味着很可能会影响到任务里取值的准确性。
             *
             * 打个比方，有ttl对象tl，这个tl在线程池的某个子线程里存在对应的值2，当某个主线程使用该子线程做异步任务时
             * tl这个对象在当前主线程里没有值，那么如果不进行下面这一步的操作，那么在使用该子线程做的任务里就可以通过
             * 该tl对象取到值2，不符合预期
             */
    if (!capturedMap.containsKey(threadLocal)) {
      iterator.remove();
      threadLocal.superRemove();
    }
  }

  // 这一步就是直接把父线程本地变量赋值给当前线程了（这一步起就刷新了holder里的值了，具体往下看该方法，在异步线程运行期间，还可能产生别的本地变量，比如在真正的run方法内的业务代码，再用一个tl对象设置一个值）
  setTtlValuesTo(capturedMap);

  // 这个方法属于扩展方法，ttl本身支持重写异步任务执行前后的操作，这里不再具体赘述
  doExecuteCallback(true);

  return backup;
}

// 结合之前Rannable包装类的run方法来看，这个方法就是使用上面replay记录下的原生线程变量做恢复用的
public static void restore(@Nonnull Object backup) {
  @SuppressWarnings("unchecked")
  Map<TransmittableThreadLocal<?>, Object> backupMap = (Map<TransmittableThreadLocal<?>, Object>) backup;
  // call afterExecute callback
  doExecuteCallback(false);

  // 注意，这里的holder取出来的，实际上是replay方法设置进去的关于父线程里的所有变量（结合上面来看，就是：该线程运行期间产生的变量+父线程继承来的变量）
  for (Iterator<? extends Map.Entry<TransmittableThreadLocal<?>, ?>> iterator = holder.get().entrySet().iterator();
       iterator.hasNext(); ) {
    Map.Entry<TransmittableThreadLocal<?>, ?> next = iterator.next();
    TransmittableThreadLocal<?> threadLocal = next.getKey();

    /**
             * 同样的，如果子线程原生变量不包含某个父线程传来的对象，那么就删除，可以思考下，这里的清除跟上面replay里的有什么不同？
             * 这里会把不属于原生变量的对象给删除掉（这里被删除掉的可能是父线程继承下来的，也可能是异步任务在执行时产生的新值）
             */
    if (!backupMap.containsKey(threadLocal)) {
      iterator.remove();
      threadLocal.superRemove();
    }
  }

  // 同样调用这个方法，进行值的恢复
  setTtlValuesTo(backupMap);
}

// 真正给当前子线程赋值的方法，对应上面的setTtlValuesTo方法
private static void setTtlValuesTo(@Nonnull Map<TransmittableThreadLocal<?>, Object> ttlValues) {
  for (Map.Entry<TransmittableThreadLocal<?>, Object> entry : ttlValues.entrySet()) {
    @SuppressWarnings("unchecked")
    TransmittableThreadLocal<Object> threadLocal = (TransmittableThreadLocal<Object>) entry.getKey();
    threadLocal.set(entry.getValue()); //赋值，注意，从这里开始，子线程的holder里的值会被重新赋值刷新，可以参照上面ttl的set方法的实现
  }
}

```



### 3.2.5、TTL中线程池子线程原生变量的产生

> 所谓线程池内线程的本地原生变量，其实是第一次使用线程时被传递进去的值，我们之前有说过`TTL`是继承至`ITL`的，线程池第一次启用时是会触发`Thread` 的 `init` 方法的，也就是说，在第一次异步时拿到的主线程的变量会被传递给子线程，作为子线程的原生本地变量保存起来，后续是`replay` 操作和 `restore` 操作也是围绕着这个原生变量（即原生`holder`里的值）来进行设置、恢复的，设置的是当前父线程捕获到的本地变量，恢复的是子线程原生本地变量。      
>
> `holder`里持有的可以理解就是当前线程内的所有本地变量，当子线程将异步任务执行完毕后，会执行`restore`进行恢复原生本地变量
>
> > **正常程序里想要完成线程池上下文传递，使用TL就足够了，我们可以效仿TTL包装线程池对象的原理，进行值传递，异步任务结束后，再`remove`，以此类推来完成线程池值传递，不过这种方式过于单纯，且要求上下文为只读对象，否则子线程存在写操作，就会发生上下文污染**。















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
		id: '7WolTUqcuxa6D1rQ',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

