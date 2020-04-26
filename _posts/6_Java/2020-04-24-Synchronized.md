---
title: Synchronized
date: 2020-04-24 03:33:00
tags: 
- Java
category: 
- Java
description: Synchronized
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`synchronized` 使用 

> synchronized关键字是用来控制线程同步的，就是在多线程的环境下，控制synchronized代码段不被多个线程同时执行。synchronized既可以加在一段代码上，也可以加在方法上。



## 1.1、线程中new对象，未成功

### 1.1.1、demo方法

```java

public class Synchronized_1 {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread thread = new MyThread();
            thread.start();
        }
    }
}

class Sync {

    public synchronized void test() {
        System.out.println("test开始..");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("test结束..");
    }
}

class MyThread extends Thread {

    @Override
    public void run() {
        Sync sync = new Sync();
        sync.test();
    }
}


```



### 1.1.2、控制台测试结果

![1587721413477](D:\study\HealerJean.github.io\blogImages\1587721413477.png)



### 1.1.3、原理解释 

**上面的程序起了三个线程，同时运行Sync类中的test()方法，虽然test()方法加上了synchronized，但是还是同时运行起来，貌似synchronized没起作用。**   

**1、每个线程中都new了一个Sync类的对象，也就是产生了三个Sync对象，由于不是同一个对象，所以可以多线程同时运行synchronized方法或代码段。**        

**2、对于非static的synchronized方法，锁的就是对象本身也就是this。**





### 1.1.4、正确写法

```java
public class ThreeForOneMain {  
	  
    public static void main(String[] args) {  
    	ThreeOneSync threeTeoSync = new ThreeOneSync();

        for (int i = 0; i < 3; i++) {  
        	ThreeOneMyThread thread = new ThreeOneMyThread(threeTeoSync);  
            thread.start();  
        }  
    }  
}

class ThreeOneSync {  
	  
    public synchronized void test() {  
        System.out.println("test开始..");  
        try {  
            Thread.sleep(1000);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        System.out.println("test结束..");  
    	
    }  
}  
  
class ThreeOneMyThread extends Thread {  
	
	public ThreeOneSync threeTeoSync;
	
	public ThreeOneMyThread(ThreeOneSync threeTeoSync){
		this.threeTeoSync = threeTeoSync;
	}
    public void run() {  
    	threeTeoSync.test();  
    }  
}  

```



## 1.2、synchronized 写到方法里面，未成功

### 1.2.1、demo方法

```java

public class Synchronized_2 {

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			TwoMyThread thread = new TwoMyThread();
			thread.start();
		}
	}
}

class TeoSync {

	public  void test() {
		synchronized(this){
			System.out.println("test开始..");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("test结束..");
		}
	}
}

class TwoMyThread extends Thread {

	@Override
	public void run() {
		TeoSync sync = new TeoSync();
		sync.test();
	}
}

```

 

### 1.2.2、控制台测试结果

![1587721575773](D:\study\HealerJean.github.io\blogImages\1587721575773.png)



### 1.2.3、原理解释

**1、synchronized(this)以及非static的synchronized方法（至于static synchronized方法请往下看），只能防止多个线程同时执行同一个对象的同步代码段。很明显，线程中new了的是3个对象**    

**2、synchronized锁住的是括号里的对象，而不是代码。**





### 1.2.4、正确写法

```java

public class Synchronized_3to2 {

	public static void main(String[] args) {
		ThreeTeoSync threeTeoSync = new ThreeTeoSync();

		for (int i = 0; i < 3; i++) {
			ThreeTwoMyThread thread = new ThreeTwoMyThread(threeTeoSync);
			thread.start();
		}
	}
}

class ThreeTeoSync {

	public  void test() {
		synchronized(this){
			System.out.println("test开始..");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("test结束..");
		}
	}
}

class ThreeTwoMyThread extends Thread {

	public ThreeTeoSync threeTeoSync;

	public ThreeTwoMyThread(ThreeTeoSync threeTeoSync){
		this.threeTeoSync = threeTeoSync;
	}
	public void run() {
		threeTeoSync.test();
	}
}

```



## 1.3、非要new3个对象怎么解决

>则是用： `synchronized(FourNewMain.class)`    
>
>解释：synchronized后的括号中锁同一个固定对象，这样就行了。这样是没问题，但是，比较多的做法是让synchronized锁这个类对应的Class对象。  



```java

public class Synchronized_4 {

	public static void main(String[] args) {
		for (int i = 0; i < 3; i++) {
			FourMyThread thread = new FourMyThread();
			thread.start();
		}
	}
}

class FourSync {

	public  void test() {
		synchronized(FourSync.class){
			System.out.println("test开始..");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("test结束..");
		}
	}
}

class FourMyThread extends Thread {

	@Override
	public void run() {
		FourSync sync = new FourSync();
		sync.test();
	}

}

```



## 1.4、静态的static

> 单例模式有相关代码  



**static方法可以直接类名加方法名调用，方法中无法使用this，所以它锁的不是this，而是类的Class对象，所以，static synchronized方法也相当于全局锁，相当于锁住了代码段。**



# 2、原理解释

> 对象被创建在堆中。并且对象在内存中的存储布局方式可以分为3块区域：对象头、实例数据、对齐填充。**其中对象头，便是我们今天的主角。**关于实例数据，对其填充看jvm



**对于对象头来说，主要是包括俩部分信息：**

1、自身运行时的数据，比如：锁状态标志、线程持有的锁…等等。（此部分内容被称之为Mark Word）   

2、另一部分是类型指针：JVM通过这个指针来确定这个对象是哪个类的实例。



 java.lang.Object 类定义了 wait()，notify()，notifyAll() 方法。 这些都是 native方法，底层是C++来实现的。 这些方法的具体实现，依赖一个叫做ObjectMonitor模式实现，这是JVM内部C++实现的机   

```
_owner 指向持有ObjectMonitor对象的线程地址。

_WaitSet 存放调用wait方法，而进入等待状态的线程的队列。

_EntryList 这里是等待锁block状态的线程的队列。

_recursions 锁的重入次数。

_count 线程获取锁的次数。
```





synchronized的对象锁，其指针指向的是一个monitor对象（由C++实现）的起始地址。每个对象实例都会有一个 monitor。其中monitor可以与对象一起创建、销毁；亦或者当线程试图获取对象锁时自动生成。   












![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





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
		id: 'tHuUsLvxoncRmWh5',
    });
    gitalk.render('gitalk-container');
</script> 

