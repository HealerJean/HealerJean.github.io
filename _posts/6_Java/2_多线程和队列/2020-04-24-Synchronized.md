---
title: synchronized
date: 2020-04-24 03:33:00
tags: 
- Java
category: 
- Java
description: synchronized
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

> `synchronised`作用：只要一个线程访问了其中的一个synchronized方法，其它线程不能同时访问这个对象中任何一个synchronized方法         
>
> 对象被创建在堆中。并且对象在内存中的存储布局方式可以分为3块区域：对象头、实例数据、对齐填充。**其中对象头，便是我们今天的主角。**关于实例数据，对其填充看jvm



![1578561760566](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/1578561760566.png)





**对于对象头来说，主要是包括俩部分信息：**  

1、`mark word（标记字段）`：主要存储对象运行时的信息，如hashcode, GC分代年龄，线程ID，**锁状态标志**，时间戳等，markword数据最后2bit是锁状态标志位，用来标记当前对象的状态，对象的所处的状态，决定了markword存储的内容（**因为锁信息是存储在对象上的，所以就不难理解 锁是对象 这句话了**），如下表所示：      



| 状态             | 标志位 | 存储内容                             |
| ---------------- | ------ | ------------------------------------ |
| 未锁定           | 01     | 对象哈希码、对象分代年龄             |
| **轻量级锁定**   | 00     | **指向锁记录的指针**                 |
| 膨胀(重量级锁定) | 10     | 偏向线程ID、偏向时间戳、对象分代年龄 |
| GC标记           | 11     | 空(不需要记录信息)                   |
| 可偏向           | 01     | 偏向线程ID、偏向时间戳、对象分代年龄 |



2、`klass`（元数据指针）：即对象指向它的类元数据（`instanceKlass`实例）的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例.



**了解`ObjectMonitor`对象监听器模式**    ：**上锁的过程**

​    `java.lang.Object` 类定义了 `wait()`，`notify()`，`notifyAll() `方法。 这些都是 native方法，底层是C++来实现的。 这些方法的具体实现，依赖一个叫做`ObjectMonitor`模式实现，这是JVM内部C++实现的机制，其内部有如下结构     



| 字段          | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| `_owner`      | 初始时为NULL，表示当前没有任何线程拥有该monitor record，当线程成功拥有该锁后保存线程唯一标识，指向当前线程的地址，当锁被释放时又设置为NULL； |
| `_WaitSet`    | **存放调用wait方法，而进入等待状态的线程的队列。**           |
| `_EntryList`  | 这里是等待锁block状态的线程的队列。                          |
| `_recursions` | 锁的重入次数。                                               |
| `_count`      | 线程获取锁的次数。                                           |

`_owner`，它指向持有`ObjectMonitor`对象的线程。    

当多个线程同时访问一段`同步代码`时，会先存放到 `_EntryLis`t 集合中，接下来当线程获取到对象的`monitor`时，就会把`_owner`变量设置为当前线程。同时count变量+1。     

如果线程调用`wait()` 方法，就会释放当前持有的monitor，那么_owner变量就会被置为null，同时_count减1，并且该线程进入 `_waitSet`集合中，等待下一次被唤醒。   





**举例说明：**

```java
public class Test {
 
    private static Object object = new Object();
 
    public static int main(String[] args) {
        synchronized (object){
            System.out.println("Hello World");
        }
        return 1;
    }
}
```



![1587884358158](D:\study\HealerJean.github.io\blogImages\1587884358158.png)



通过上面代码对应的字节码图片，可以看到，锁是通过monitorenter和monitorexit来实现的，这两个字节码代表的是啥意思：    

同步代码块：每个对象都有一个`monitor`监视器，**`monitorenter`指令插入到同步代码块的开始位置**monitor，**`monitorexit`指令插入到同步代码块的结束位置**，JVM需要保证每一个monitorenter都有一个monitorexit与之相对应。任何对象都有一个monitor与之相关联



```
它在字节码文件中被编译为：monitorenter;//获取monitor许可证，进入同步块

     同步代码...

monitorexit;//离开同步块后，释放monitor许可证
```





## 锁的状态：

> 锁的状态总共有四种，无锁状态、偏向锁、轻量级锁和重量级锁。随着锁的竞争，锁可以从偏向锁升级到轻量级锁，再升级的重量级锁，但是**锁的升级是单向的，也就是说只能从低到高升级，不会出现锁的降级，**



**在多线程并发编程中Synchronized一直是元老级角色，很多人都会称呼它为重量级锁，但是随着Java SE1.6对Synchronized进行了各种优化之后，有些情况下它并不那么重了，Java SE1.6中为了减少获得锁和释放锁带来的性能消耗而引入的偏向锁和轻量级锁，以及锁的存储结构和升级过程。**






**1、偏向锁**

偏向锁是Java 6之后加入的新锁，它是一种针对加锁操作的优化手段，经过研究发现，在大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，因此为了减少同一线程获取锁(会涉及到一些CAS操作,耗时)的代价而引入偏向锁。偏向锁的核心思想是，**如果一个线程获得了锁，那么锁就进入偏向模式，此时Mark Word 的结构也变为偏向锁结构，当这个线程再次请求锁时（CAS比较线程），无需再做任何同步操作，即获取锁的过程**，这样就省去了大量有关锁申请的操作，从而也就提供程序的性能。**所以，对于没有锁竞争的场合，偏向锁有很好的优化效果，毕竟极有可能连续多次是同一个线程申请相同的锁**。     

但是对于锁竞争比较激烈的场合，偏向锁就失效了，因为这样场合极有可能每次申请锁的线程都是不相同的，因此这种场合下不应该使用偏向锁，否则会得不偿失，需要注意的是，**偏向锁失败后，并不会立即膨胀为重量级锁，而是先升级为轻量级锁。**     

**如果在运行过程中，遇到了其他线程抢占锁，则持有偏向锁的线程会被挂起，JVM会消除它身上的偏向锁，将锁恢复到标准的轻量级锁。**   



**2、轻量级锁**

倘若偏向锁失败，虚拟机并不会立即升级为重量级锁，它还会尝试使用一种称为轻量级锁的优化手段(1.6之后加入的)，此时Mark Word 的结构也变为轻量级锁的结构。      

**轻量级锁能够提升程序性能的依据是“对绝大部分的锁**，**在整个同步周期内都不存在竞争，注意这是经验数据。需要了解的是，轻量级锁所适应的场景是线程交替执行同步块的场合，如果存在同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁**。   

轻量级锁也被称为**非阻塞同步**、**乐观锁**，因为这个过程并没有把线程阻塞挂起，而是让线程空循环等待，串行执行。



**3、自旋锁**

**轻量级锁失败后，如果持有锁的线程能在很短时间内释放锁资源，那么那些等待竞争锁的线程就不需要做内核态和用户态之间的切换进入阻塞挂起状态**，这个切换需要相对较长的时间，它们只需要等一等（等待锁的线程自旋），虚拟机会让当前想要获取锁的线程做几个空循环(这也是称为自旋的原因)，一般不会太久，可能是50个循环或100循环，在经过若干次循环后，如果得到锁，就顺利进入临界区等持有锁的线程释放锁后即可立即获取锁，这样就**避免用户线程和内核的切换的消耗**。       

但是线程自旋是需要消耗CPU的，说白了就是让CPU在做无用功，线程不能一直占用CPU自旋做无用功，所以需要设定一个自旋等待的最大时间。，**如果持有锁的线程执行的时间超过自旋等待的最大时间扔没有释放锁，就会导致其它争用锁的线程在最大等待时间内还是获取不到锁，这时争用线程会停止自旋进入阻塞状态，没办法也就只能升级为重量级锁了。**     



优点：尽可能的减少线程的阻塞，**这对于锁的竞争不激烈**，且**占用锁时间非常短**的代码块来说性能能大幅度的提升，**因为自旋的消耗会小于线程阻塞挂起操作的消耗！**



**自旋锁的开启**：    

 JDK1.6中-XX:+UseSpinning开启；    

JDK1.7后，去掉此参数，由jvm控制；



**4、重量级锁**    

**为什么说重量级锁开销大呢**    

主要是，当系统检查到锁是重量级锁之后，会把等待想要获得锁的线程进行**阻塞挂起**，被阻塞的线程不会消耗cup。**但是阻塞或者唤醒一个线程时，都需要操作系统来帮忙**，这就需要从**用户态**转换到**内核态**，而转换状态是需要消耗很多时间的，有可能比用户执行代码的时间还要长。互斥锁(重量级锁)也称为**阻塞同步**、**悲观锁**





## 举例说明 



     **获取偏向锁**：       小明去上厕所，小明问了一句里面，有人嘛？没人回应（**CAS**），于是小明进去上了厕所，并在厕所门上标注：“小明”；    

    **拥有偏向锁**：       小明过了一会又来了，发现厕所门上的纸还在，还是“小明”（CAS），于是小明这次没有问，直接去上厕所了；     

    **升级轻量级锁**：   小明在上厕所的时候小华来了，小华看到门上有“小明”的字样，知道小明在里面上厕所，于是催促小明说你快点，我在外面等你呢，每隔一段时间小华就要问一次（自旋锁）。等到小明上完厕所出来，小华进去上了厕所，临上厕所之前还不忘记把厕所上的字改成了“小华”

   **升级重量级锁**：   小华在里面上厕所的时候，这时候来了很多的人，这些人就在门口每隔一段时间问小华什么时候好，然而经过了很多次以后小华说你们等吧，我出来了叫你们。众人说那就算了，在厕所外面安静的等吧（停止自旋，进入阻塞状态）。当小华上完厕所出来以后告诉大家，我上完了，然后众人靠自己的手速抢位置，并不是第一个人就能抢到，完全是随机性的（非公平锁）。









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

