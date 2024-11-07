---
title: SpringBoot之_ SmartLifecycle自定义生命周期
date: 2020-07-06 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot之_ SmartLifecycle自定义生命周期
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、`Lifecycle`

>  在使用`Spring` 开发时，我们都知道，所有`bean`都交给 `Spring` 容器来统一管理，其中包括每一个`bean`的加载和初始化。       
>
> 有时候我们需要在`Spring` 加载和初始化所有 `bean` 后，接着执行一些任务或者启动需要的异步服务，这样我们可以使用 `SmartLifecycle` 来做到。     
>
> 这个和 `@PostConstruct`、`@PreDestroy` 的 `bean` 的初始化和销毁方法不同，`Bean`生命周期级别和容器生命周期级别在应用场景上是有区别的。
>
> `SmartLifecycle` 是一个接口。当`Spring`容器加载所有`bean`并完成初始化之后，会接着回调实现该接口的类中对应的方法（`start()`方法）。



## 1、基本介绍

### 1）`start`

> ⬤ **容器 `refresh` 时**：在 `Spring` 容器执行 `refresh` 方法的最后阶段，即 `finishRefresh` 阶段，会实例化所有的单例对象。此后，会获取所有的生命周期处理器，并根据 `phase` 分组，然后以组为单位执行 `start` 方法。不过，需要注意的是，只有在 `bean`的`isRunning()` 方法返回 `false` 时，才会执行其 `start()` 方法。    
>
> **自动启动**：对于实现了 `SmartLifecycle` 接口的 `bean` ，如果其 `isAutoStartup()` 方法返回 `true`（这也是默认值），则容器会在适当的时候自动调用其 `start()` 方法，而不需要显式地调用。



### 2）`stop`

> **容器 `close` 时**：当 `Spring` 容器关闭时，会优化执行所有实现了 `SmartLifecycle` 接口的 `bean` 的 `stop` 方法，以进行资源的清理和释放。    
>
> **回调执行**：`SmartLifecycle` 接口还提供了一个 `stop(Runnable callback)` 方法，允许在停止生命周期组件后执行一个回调。



### 3）使用场景

> `SmartLifecycle` 接口通常用于需要在`Spring` 容器完全启动之前或之后执行特定任务的场景。    
>
> > 例如，在 `Spring` `Boot` 应用中，可能需要一些组件在数据库连接建立之后才能启动，或者需要在某些服务注册到 `Eureka` 服务器之后才能启动。通过使用 `SmartLifecycle`接口，可以确保这些组件在依赖条件满足后按正确的顺序启动。



### 4）注意事项

> 1、实现 `SmartLifecycle` 接口的 `bean` 必须是单例的。   
>
> 2、如果需要控制 `SmartLifecycle` 实例的启动顺序，可以通过实现 `getPhase()` 方法来返回一个整数值，数值越小，启动越早。





## 2、案例

```java
package com.sankuai.windmill.riding.mafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
@Component
public class ConsumerSmartLifecycle implements SmartLifecycle {

  private AtomicBoolean isRunning = new AtomicBoolean(false);

  /**
     * 作用：启动任务或者其他异步服务，比如开启MQ接收消息
     * 调用时机：
     * 1、当上下文被刷新（所有对象已被实例化和初始化之后）时，将调用该方法
     * 2、默认生命周期处理器将检查每个SmartLifecycle对象的isAutoStartup()方法返回的布尔值（默认为true）。如果为“true”，则该方法会被调用，而不是等待显式调用自己的start()方法。
     */
  @Override
  public void start() {
    // 保证之初始化一次
    if (!isRunning.compareAndSet(false, true)) {
      return;
    }

    //TODO someThing

  }

  /**
     * 作用： 如果工程中有多个实现接口SmartLifecycle的类，则这些类的start的执行顺序按getPhase方法返回值从小到大执行。
     * 例如： 1比2先执行，-1比0先执行。 stop方法的执行顺序则相反，getPhase返回值较大类的stop方法先被调用，小的后被调用。（也就是说start先开始的后结束）
     * 是否重写：不一定 接口中有提供默认值
     */
  @Override
  public int getPhase() {
    return 0;
  }

  /**
     *
     * 作用：根据该方法的返回值决定是否执行start方法。
     * 使用：返回true时start方法会被自动执行，返回false则不会。
     * 是否重写：不一定，默认是true
     */
  @Override
  public boolean isAutoStartup() {
    return true;
  }


  /**
     * 1. 只有该方法返回false时，start方法才会被执行。
     * 2. 只有该方法返回true时， stop(Runnable callback)或stop()方法才会被执行。
     */
  @Override
  public boolean isRunning() {
    return isRunning.get();
  }



  /**
     * 说明：当isRunning方法返回true时，该方法才会被调用。
     * 时机：容器关闭后：
     * 1、如果容器里当前对象实现了SmartLifecycle接口，则调用stop ( Runnable )；
     * 2、如果只实现了LifeCycle，就调用stop ( )
     */
  @Override
  public void stop() {
    try {
      // TODO close some source
    } catch (Exception e) {
      log.info("[ConsumerSmartLifecycle]========error", e);
    } finally {
      isRunning.set(false);
    }
  }

  /**
     * 说明：当isRunning方法返回true时，该方法才会被调用。
     * 时机：容器关闭后：
     * 1、如果容器里当前对象实现了SmartLifecycle接口，则调用stop ( Runnable )；
     * 2、如果只实现了LifeCycle，就调用stop ( )
     */
  @Override
  public void stop(Runnable callback) {
    try {
      // TODO close some source
    } catch (Exception e) {
      log.info("[ConsumerSmartLifecycle]========error", e);
    } finally {
      isRunning.set(false);
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
		id: 'KYqNyvZI05weBrMn',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



