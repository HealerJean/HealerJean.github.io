---
title: JVM之_13_ShutdownHook
date: 2024-01-09 00:00:00
tags: 
- JVM
category: 
- JVM
description: JVM之_13_ShutdownHook
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、`ShutdownHook`

> `java` 里有个方法 **`Runtime.getRuntime#addShutdownHook`**，是否了解呢?
>
> `ShutdownHook` 是什么意思呢，看单词解释“**关闭钩子”，`addShutdownHook`**就是添加一个关闭钩子，这个钩子是做什么的呢？能否解决上面的问题？

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
  //todo
}));
```



> `shutdownHook `是一种特殊的结构，它允许开发人员插入 `JVM` 关闭时执行的一段代码。这种情况在我们需要做特殊清理操作 的情况下很有用

⬤ **`Application` 正常退出，在退出时执行特定的业务逻辑，或者关闭资源等操作**。

⬤ **虚拟机非正常退出，比如用户按下`ctrl+C`、`OOM`宕机、操作系统关闭(`kill pid`)等。在退出时执行必要的挽救措施**。



## 2、`RunTime` 类

```java
public void addShutdownHook(Thread hook) {
      @SuppressWarnings("removal")
      SecurityManager sm = System.getSecurityManager();
      if (sm != null) {
          sm.checkPermission(new RuntimePermission("shutdownHooks"));
      }
      ApplicationShutdownHooks.add(hook);
  }
```



## 3、`ApplicationShutdownHooks` 

### 1）添加钩子

```java
 private static IdentityHashMap<Thread, Thread> hooks;
 static synchronized void add(Thread hook) {
    if(hooks == null)
        throw new IllegalStateException("Shutdown in progress");
    if (hook.isAlive())
        throw new IllegalArgumentException("Hook already running");
    if (hooks.containsKey(hook))
        throw new IllegalArgumentException("Hook previously registered");
    hooks.put(hook, hook);
}
```



### 2） 执行钩子

```java
static void runHooks() {
    Collection<Thread> threads;
    synchronized(ApplicationShutdownHooks.class) {
        threads = hooks.keySet();
        hooks = null;
    }
    for (Thread hook : threads) {
        hook.start();
    }
    for (Thread hook : threads) {
        while (true) {
            try {
                hook.join();
                break;
            } catch (InterruptedException ignored) {
            }
        }
    }
}
```



### 3）执行时机

> 为什么在系统退出的时候会执行添加的 `hook` 呢？我们看一下正常的退出操作 `System#exit`方法。

#### **a、 类调用层级**

```
System->Runtime->Shutdown->ApplicationShutdownHooks 
```



#### **b、 方法调用**

 系统退出入口：System#exit

​      步骤  1-->System#exit

​      步骤  2-->Runtime#exit;

​      步骤  3--> Shutdown#*exit*

​      **步骤  4--> Shutdown#runHooks**      

​      **步骤 5--> ApplicationShutdownHooks#runHooks**

​      步骤 6-->启动添加的hook线程

#### **c、 补充一下**

**为什么步骤 `4` 会调用到步骤5呢？**

可以看一下 `ApplicationShutdownHooks` 的构造函数，在创建的时候，封装了 `runHooks` 方法，放到了 `Shutdown` 的钩子集合里。如此形成**闭环**，在系统正常退出的时候，最终执行我们添加的 `hook`。



## 二、例子

```java
public static void main(String[] args) throws InterruptedException {
    Thread thread = new Thread() {
        @Override
        public void run() {
            System.out.println("等等我");
        }
    };
    Runtime.getRuntime().addShutdownHook(thread);
    System.out.println("程序关闭"); 
}
输出：
程序关闭
等等我
```





## 三、**应用场景** 

> 关闭链接、线程、资源释放、记录执行状态等。



# 四、风险点

## 1、长时间等待

### 1）样例

> 如果添加的 `hook` 线程长时间执行，我们的退出命令会一直等待，为什么呢？

举个例子，我们在执行的时候 `sleep`一下

```java
  public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000*300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(new Date()+" 等我5分钟");
            }
        };
        Runtime.getRuntime().addShutdownHook(thread);
        System.out.println(new Date()+" 程序关闭");
    }
输出：
Tue Nov 12 17:37:38 CST 2024 程序关闭
Tue Nov 12 17:42:38 CST 2024 等我5分钟
```

### 2）原因

> `JVM `在退出的时候会调用 `runHooks` 方法，看一下上面的方法 java.lang.ApplicationShutdownHooks#**runHooks**方法。
>
> 关键字 **`hook.join();`** 主线程会等待子线程执行完成。如果程序一直执行，不能退出怎么办？



### 3）解决

1 ) 写代码时候控制执行逻辑、时长    

2）`kill -9` 命令 强制退出



## 2、使用事项

#### **a、应用程序无法保证 `shutdownHook`总是运行的**    

如 `JVM` 由于某些内部错误而崩溃，或（`Unix / Linux`中的`kill -9`）或 `TerminateProcess`（ `Windows`）），那么应用程序需要立即终止而不会甚至等待任何清理活动。除了上述之外，还可以通过调用 `Runime.halt（）`方法来终止`JVM`，而阻止`shutdownHook`运行。



#### **b、`shutdownHook`可以在完成前强行停止**      

虽然`shutdownHook` 开始执行，但是在操作系统关闭的情况下，仍然可以在完成之前终止它。在这种情况下，一旦 `SIGTERM` 被提供，`O/S `等待一个进程终止指定的时间。如果进程在该时间限制内没有终止，则`O/S `通过发出 `SIGTERM`（或 `Windows` 中的对等方）强制终止进程。所以有可能这是在 `shutdownHook` 中途执行时发生的。     

因此，建议谨慎地编写`shutdownHook`，确保它们快速完成，并且不会造成死锁等情况。另外特别注意的是，不应该执行长时间计算或等待用户 `I/O`操作在钩子。



#### c、**可以有多个`shutdownHook`，但其执行顺序无法保证**



#### d、面相对象

**1、`Runtime.getRuntime`#`addShutdownHook`是面向开发者的**    

​    `ApplicationShutdownHook`#**`add`**、`Shutdown`#**`add`**我们都不能直接使用。    

**2、许多中间件框架也利用`addShutdownHook`来实现资源回收、清理等操作**

​    比如 `Spring` 框架中，使用了 `ShutdownHook` 注册，我们常用的 `@PreDestroy` 在 `Bean`销毁前执行一些操作，也是借助其回调的。









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
		id: 'xbpyrv7n5tsDAgFT',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



