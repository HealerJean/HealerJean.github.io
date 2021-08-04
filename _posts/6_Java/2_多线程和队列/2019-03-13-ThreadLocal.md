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



# 1、ThreadLocal

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

`key` 使用强引用：当`ThreadLocalMap`的`key`为强引用回收`ThreadLocal`时，因为`ThreadLocalMap`还持有`ThreadLocal`的强引用，如果没有key 使用弱引用：手动删除，`ThreadLocal`不会被回收，导致`Entry`内存泄漏。 譬如 设置：`ThreadLocal=null` 以后，应该会被回收的，但实际情况是`ThreadLocalMap`还有一个强引用，导致无法回收           

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

