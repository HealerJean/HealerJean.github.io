---
title: ThreadLocal
date: 2019-03-13 03:33:00
tags: 
- Thread
category: 
- Thread
description: ThreadLocal
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进


<font  clalss="healerColor" color="red" size="5" >     

</font>

<font  clalss="healerSize"  size="5" >     </font>

-->

## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    


​     
ThreadLocal翻译成中文比较准确的叫法应该是：线程局部变量。



```java
package com.duodian.admore.zhaobutong.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author fengchuanbo
 */
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

### 1、set方法

线程隔离的秘密，就在于ThreadLocalMap这个类。     

1、先看set方法，先获取当前线程，然后通过getMap(Thread t)方法获取一个和当前线程相关的ThreadLocalMap，然后将变量的值设置到这个ThreadLocalMap对象中，当然如果获取到的ThreadLocalMap对象为空，就通过createMap方法创建。


ThreadLocalMap是ThreadLocal类的一个静态内部类，它实现了键值对的设置和获取（对比Map对象来理解），      

<font  clalss="healerColor" color="red" size="5" >   

每个线程中都有一个独立的ThreadLocalMap副本，它所存储的值，只能被当前线程读取和修改。ThreadLocal类通过操作每一个线程特有的ThreadLocalMap副本，从而实现了变量访问在不同线程中的隔离。因为每个线程的变量都是自己特有的，完全不会有并发错误。还有一点就是    

ThreadLocalMap存储的键值对中的键是this对象指向的ThreadLocal对象，而值就是你所设置的对象了。

</font>



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

### 2、get方法



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
    
 ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
  }


```

### 3、总结：

当我们调用get方法的时候，其实每个当前线程中都有一个ThreadLocalMap。每次获取或者设置都是对该ThreadLocal进行的操作，是与其他线程分开的。从本质来讲，就是每个线程都维护了一个map，而这个map的key就是threadLocal，而值就是我们set的那个值     

每次线程在get的时候，都从自己的变量中取值，既然从自己的变量中取值，那肯定就不存在线程安全问题，总体来讲，ThreadLocal这个变量的状态根本没有发生变化，他仅仅是充当一个key的角色，另外提供给每一个线程一个初始值
　4、应用场景：当很多线程需要多次使用同一个对象，并且需要该对象具有相同初始化值的时候最适合使用ThreadLocal。


## 2、ThreadLocal 与 Synchronized区别


相同：ThreadLocal和线程同步机制都是为了解决多线程中相同变量的访问冲突问题。    

不同：Synchronized同步机制采用了“以时间换空间”的方式，仅提供一份变量，让不同的线程排队访问；而ThreadLocal采用了“以空间换时间”的方式，每一个线程都提供了一份变量，因此可以同时访问而互不影响。    

以时间换空间->即枷锁方式，某个区域代码或变量只有一份节省了内存，但是会形成很多线程等待现象，因此浪费了时间而节省了空间。   

以空间换时间->为每一个线程提供一份变量，多开销一些内存，但是呢线程不用等待，可以一起执行而相互之间没有影响。    

小结：ThreadLocal是解决线程安全问题一个很好的思路，它通过为每个线程提供一个独立的变量副本解决了变量并发访问的冲突问题。在很多情况下，ThreadLocal比直接使用synchronized同步机制解决线程安全问题更简单，更方便，且结果程序拥有更高的并发性。


## 3、ThreadLocalMap key弱引用


```
在ThreadLocal的get(),set()的时候都会清除线程ThreadLocalMap里所有key为null的value。 

ThreadLocal的remove()方法会先将Entry中对key的弱引用断开，设置为null，然后再清除对应的key为null的value。 

```

### 3.1、为什么使用弱引用

key 使用强引用：引用的ThreadLocal对象被回收了，这时候Entry中的ThreadLocal理应被回收了，但是如果Entry的key被设置成强引用则该ThreadLocal就不能被回收，这就是将其设置成弱引用的目的。

key 使用弱引用：引用的ThreadLocal的对象被回收了，由于ThreadLocalMap持有ThreadLocal的弱引用，即使没有手动删除，ThreadLocal也会被回收。即，value在下一次ThreadLocalMap调用set,get，remove的时候会被清除      

```
Entry中的key是弱引用，key 弱指向ThreadLocal<UserInfo> 对象，并且Key只是userInfoLocal强引用的副本（结合第一个问题），value是userInfo对象。

当我显示的把userInfoLocal = null 时就只剩下了key这一个弱引用，GC时也就会回收掉ThreadLocal<UserInfo> 对象。

```




### 3.2、使用和注意事项

**1、JVM利用设置ThreadLocalMap的Key为弱引用，来避免内存泄露。JVM利用调用remove、get、set方法的时候，回收弱引用。当ThreadLocal存储很多Key为null的Entry的时候，而不再去调用remove、get、set方法，那么将导致内存泄漏。**   

3、当使用static ThreadLocal的时候，延长ThreadLocal的生命周期，那也可能导致内存泄漏。因为，static变量在类未加载的时候，它就已经加载，当线程结束的时候，static变量不一定会回收。那么，比起普通成员变量使用的时候才加载，static的生命周期加长将更容易导致内存泄漏危机。

在线程消失之后，其线程局部实例的所有副本都会被垃圾回收，这样的话就不会造成影响   

### 3.3、线程池中的使用

开始前重新set值，以及结束后要记得remove


​     
<br><br>    
<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>
<br>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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

