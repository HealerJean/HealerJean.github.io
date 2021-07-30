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

> `synchronized` 关键字是用来控制线程同步的，就是在多线程的环境下，控制`synchronized`代码段不被多个线程同时执行。`synchronized` 既可以加在一段代码上，也可以加在方法上。



## 1.1、线程中`new`对象，未成功

### 1.1.1、`demo`方法

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

![1587721413477](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1587721413477.png)



### 1.1.3、原理解释 

**上面的程序起了三个线程，同时运行`Sync`类中的test()方法，虽然test()方法加上了synchronized，但是还是同时运行起来，貌似`synchronized`没起作用。**   

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



## 1.2、`synchronized` 写到方法里面，未成功

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

![1587721575773](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1587721575773.png)



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



> **`static`方法可以直接类名加方法名调用，方法中无法使用`this`，所以它锁的不是`this`，而是类的`Class`对象，所以，`static synchronized`方法也相当于全局锁，相当于锁住了代码段。**



# 2、原理解释  

## 2.1、Java堆结构上分析  

> `synchronised`作用：只要一个线程访问了其中的一个`synchronized`方法，其它线程不能同时访问这个对象中任何一个`synchronized`方法         
>
> 对象被创建在堆中。并且对象在内存中的存储布局方式可以分为3块区域：对象头、实例数据、对齐填充。**其中对象头，便是我们今天的主角。**关于实例数据，对其填充看jvm



![1578561760566](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/1578561760566.png)





**对于对象头来说，主要是包括俩部分信息：**  

1、`mark word（标记字段）`：主要存储对象运行时的信息，如`hashcode`, GC分代年龄，线程ID，**锁状态标志**，时间戳等，`markword`数据最后`2bit`是锁状态标志位，用来标记当前对象的状态，对象的所处的状态，决定了`markword`存储的内容（**因为锁信息是存储在对象上的，所以就不难理解 锁是对象 这句话了**），如下表所示：      



| 状态             | 标志位 | 存储内容                             |
| ---------------- | ------ | ------------------------------------ |
| 未锁定           | 01     | 对象哈希码、对象分代年龄             |
| **轻量级锁定**   | 00     | **指向锁记录的指针**                 |
| 膨胀(重量级锁定) | 10     | 偏向线程ID、偏向时间戳、对象分代年龄 |
| GC标记           | 11     | 空(不需要记录信息)                   |
| 可偏向           | 01     | 偏向线程ID、偏向时间戳、对象分代年龄 |

2、`klass`（元数据指针）：即对象指向它的类元数据（`instanceKlass`实例）的指针，虚拟机通过这个指针来确定这个对象是哪个类的实例.        



![image-20201214152128994](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201214152128994.png)



**了解 `ObjectMonitor` 对象监听器模式**    ：**上锁的过程**

​    `java.lang.Object`  类定义了 `wait()`，`notify()`，`notifyAll() `方法。 这些都是 `native`方法，底层是C++来实现的。 这些方法的具体实现，依赖一个叫做`ObjectMonitor` 模式实现，这是 `JVM`内部C++实现的机制，其内部有如下结构     

 

| 字段          | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| `_owner`      | `_owner`，它指向持有`ObjectMonitor`对象的线程。初始时为`NULL`，表示当前没有任何线程拥有该`monitor` `record`，当线程成功拥有该锁后保存线程唯一标识，指向当前线程的地址，当锁被释放时又设置为NULL； |
| `_WaitSet`    | **存放调用`wait`方法，而进入等待状态的线程的队列。**         |
| `_EntryList`  | 这里是等待锁`block`状态的线程的队列。                        |
| `_recursions` | 锁的重入次数。                                               |
| `_count`      | 线程获取锁的次数。                                           |



当多个线程同时访问一段`同步代码`时，会先存放到 `_EntryLis`t 集合中，接下来当线程获取到对象的`monitor`时，就会把`_owner`变量设置为当前线程。同时`count`变量+1。     

如果线程调用`wait()` 方法，就会释放当前持有的`monitor`，那么`_owner`变量就会被置为`null`，同时`_count`减1，并且该线程进入 `_waitSet`集合中，等待下一次被唤醒。   





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



![1587884358158](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1587884358158.png)



通过上面代码对应的字节码图片，可以看到，锁是通过`monitorenter`和`monitorexit`来实现的，这两个字节码代表的是啥意思：    

同步代码块：每个对象都有一个`monitor` 监视器，**`monitorenter`指令插入到同步代码块的开始位置**，**`monitorexit`指令插入到同步代码块的结束位置**，JVM需要保证每一个`monitorenter`都有一个`monitorexit`与之相对应。任何对象都有一个`monitor`与之相关联



```java
它在字节码文件中被编译为：monitorenter;//获取monitor许可证，进入同步块

     同步代码...

monitorexit;//离开同步块后，释放monitor许可证
```



## 2.2、锁的状态：

> 锁的状态总共有四种，无锁状态、偏向锁、轻量级锁和重量级锁。随着锁的竞争，锁可以从偏向锁升级到轻量级锁，再升级的重量级锁，但是**锁的升级是单向的，也就是说只能从低到高升级，不会出现锁的降级，**



**在多线程并发编程中`Synchronized`一直是元老级角色，很多人都会称呼它为重量级锁，但是随着Java SE1.6对`Synchronized`进行了各种优化之后，有些情况下它并不那么重了，Java SE1.6中为了减少获得锁和释放锁带来的性能消耗而引入的偏向锁和轻量级锁，以及锁的存储结构和升级过程。**          



     **获取偏向锁**：       小明去上厕所，小明问了一句里面，有人嘛？没人回应（**CAS**），于是小明进去上了厕所，并在厕所门上标注：“小明”；    

    **拥有偏向锁**：       小明过了一会又来了，发现厕所门上的纸还在，还是“小明”（CAS），于是小明这次没有问，直接去上厕所了；     

    **升级轻量级锁**：   小明在上厕所的时候小华来了，小华看到门上有“小明”的字样，知道小明在里面上厕所，于是催促小明说你快点，我在外面等你呢，每隔一段时间小华就要问一次（自旋锁）。等到小明上完厕所出来，小华进去上了厕所，临上厕所之前还不忘记把厕所上的字改成了“小华”

   **升级重量级锁**：   小华在里面上厕所的时候，这时候来了很多的人，这些人就在门口每隔一段时间问小华什么时候好，然而经过了很多次以后小华说你们等吧，我出来了叫你们。众人说那就算了，在厕所外面安静的等吧（停止自旋，进入阻塞状态）。当小华上完厕所出来以后告诉大家，我上完了，然后众人靠自己的手速抢位置，并不是第一个人就能抢到，完全是随机性的（非公平锁）。





### 2.2.1、偏向锁

偏向锁是Java 6之后加入的新锁，它是一种针对加锁操作的优化手段，经过研究发现，在大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得，因此为了减少同一线程获取锁(会涉及到一些CAS操作,耗时)的代价而引入偏向锁。      

偏向锁的核心思想是，**如果一个线程获得了锁，那么锁就进入偏向模式，此时`Mark Word` 的结构也变为偏向锁结构，当这个线程再次请求锁时（`CAS`比较线程），无需再做任何同步操作，即获取锁的过程**，这样就省去了大量有关锁申请的操作，从而也就提供程序的性能。**所以，对于没有锁竞争的场合，偏向锁有很好的优化效果，毕竟极有可能连续多次是同一个线程申请相同的锁**。     

但是对于锁竞争比较激烈的场合，偏向锁就失效了，因为这样场合极有可能每次申请锁的线程都是不相同的，因此这种场合下不应该使用偏向锁，否则会得不偿失，需要注意的是，**偏向锁失败后，并不会立即膨胀为重量级锁，而是先升级为轻量级锁。**     

**如果在运行过程中，遇到了其他线程抢占锁，则持有偏向锁的线程会被挂起，JVM会消除它身上的偏向锁，将锁恢复到标准的轻量级锁。**   



#### 2.2.1.1、加锁

1、检查 `mark word` 的线程 `id` 。   

2、如果为空则设置 `CAS` 替换当前线程 `id`。如果替换成功则获取锁成功，如果失败则撤销偏向锁。   

3、如果不为空则检查 线程 `id`为是否为本线程。如果是则获取锁成功，如果失败则撤销偏向锁。

持有偏向锁的线程以后每次进入这个锁相关的同步块时，只需比对一下 `mark word` 的线程 id 是否为本线程，如果是则获取锁成功。     



#### 2.2.1.2、解锁

1、使用 CAS 操作将 Mark Word 还原    

2、如果第 1 步执行成功则释放完成    

3、如果第 1 步执行失败则膨胀为重量级锁。





### 2.2.2、轻量级锁

倘若偏向锁失败，虚拟机并不会立即升级为重量级锁，它还会尝试使用一种称为轻量级锁的优化手段(1.6之后加入的)，此时Mark Word 的结构也变为轻量级锁的结构。      

**轻量级锁能够提升程序性能的依据是“对绝大部分的锁**，**在整个同步周期内都不存在竞争，注意这是经验数据。需要了解的是，轻量级锁所适应的场景是线程交替执行同步块的场合，如果存在同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁**。   

轻量级锁也被称为**非阻塞同步**、**乐观锁**，因为这个过程并没有把线程阻塞挂起，而是让线程空循环等待，串行执行。





#### 2.2.2.1、加锁

1、JVM 在当前线程的栈帧中创建` Lock Reocrd`，并将对象头中的 `Mark Word` 复制到 `Lock Reocrd` 中。（Displaced Mark Word）    

2、线程尝试使用 CAS 将对象头中的 `Mark Word` 替换为指向 `Lock Reocrd` 的指针。如果成功则获得锁，如果失败则先检查对象的 `Mark Word` 是否指向当前线程的栈帧如果是则说明已经获取锁，否则说明其它线程竞争锁则膨胀为重量级锁。



#### 2.2.2.2、解锁

1、使用 CAS 操作将 `Mark Word` 还原   

2、如果第 1 步执行成功则释放完成    

3、如果第 1 步执行失败则膨胀为重量级锁。





### 2.2.3、自旋锁

> **轻量级锁失败后，如果持有锁的线程能在很短时间内释放锁资源，那么那些等待竞争锁的线程就不需要做内核态和用户态之间的切换进入阻塞挂起状态**，这个切换需要相对较长的时间，它们只需要等一等（等待锁的线程自旋），虚拟机会让当前想要获取锁的线程做几个空循环(这也是称为自旋的原因)，一般不会太久，可能是50个循环或100循环，在经过若干次循环后，如果得到锁，就顺利进入临界区等持有锁的线程释放锁后即可立即获取锁，这样就**避免用户线程和内核的切换的消耗**。           
>
> 
>
> 但是线程自旋是需要消耗CPU的，说白了就是让`CPU`在做无用功，线程不能一直占用`CPU`自旋做无用功，所以需要设定一个自旋等待的最大时间。，**如果持有锁的线程执行的时间超过自旋等待的最大时间扔没有释放锁，就会导致其它争用锁的线程在最大等待时间内还是获取不到锁，这时争用线程会停止自旋进入阻塞状态，没办法也就只能升级为重量级锁了。**     



优点：尽可能的减少线程的阻塞，**这对于锁的竞争不激烈**，且**占用锁时间非常短**的代码块来说性能能大幅度的提升，**因为自旋的消耗会小于线程阻塞挂起操作的消耗！**



**自旋锁的开启**：    

 JDK1.6中-XX:+UseSpinning开启；    

JDK1.7后，去掉此参数，由jvm控制；



### 2.2.4、重量级锁   

> 升级为重量级锁时会在堆中创建 monitor 对象，并将 Mark Word 指向该 monitor 对象。monitor 中有 cxq（ContentionList），EntryList ，WaitSet，owner：



**为什么说重量级锁开销大呢**    

主要是，当系统检查到锁是重量级锁之后，会把等待想要获得锁的线程进行**阻塞挂起**，被阻塞的线程不会消耗cup。**但是阻塞或者唤醒一个线程时，都需要操作系统来帮忙**，这就需要从**用户态**转换到**内核态**，而转换状态是需要消耗很多时间的，有可能比用户执行代码的时间还要长。互斥锁(重量级锁)也称为**阻塞同步**、**悲观锁**





# 3、其他问题

## 3.1、`Synchnorsed` 如何保证可见性  

一般情况下，内存可见性第一反应是`volatile`：被 `volatile`修饰的变量能够保证每个线程能够获取该变量的最新值，从而避免出现数据脏读的现象。     



`synchronized `会保证对进入同一个监视器的线程保证可见性。     

**`synchronized `针对同进入一个监视器线程而言（如果不是同一个监视器就不保证了）这里语义的解读只是说了对于同一个监视器，变量的可见性有一定的方式可寻，非同一个监视器就不保证了**。

　　1）线程加锁时，将清空工作内存中共享变量**（被锁的对象的共享变量）**的值（相当于volatile的作用，使其他缓存行失效），从而使其他进入这个监视器的线程使用共享变量时需要从主内存中重新获取最新的值       

　　2）线程解锁前，必须把共享变量的最新值刷新到主内存中    

**举例：**     

线程 1修改了变量，退出监视器之前，会把修改变量值v1刷新的主内存当中；    

当线程2进入这个监视器时，如果线程1还在持有锁，会发现自己的缓存行失效，然后必须重主内存重新加载变量值v1（这点和volatile很像）。    



## 3.2、单例模式为什么进入Synchronise还要进行判断  

> 单例模式如下，加入有多个线程同时通过了第一次的判空，有一个线程很快获取了锁，并在执行了里面的线程同步块里面方法使得`instance`有了值。那么我们当前线程再进入的时候势必要判断一下啦  



```java
class Singleton{
    private volatile static Singleton instance = null;
     
    private Singleton() {
         
    }
     
    public static Singleton getInstance() {
        if(instance==null) {
            synchronized (Singleton.class) {
                if(instance==null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}

```





## 3.3、`ReenTrantLock`和`synchronized`区别

**1、可重入性**：   

从名字上理解，`ReenTrantLock`的字面意思就是再进入的锁，其实`synchronized`关键字所使用的锁也是可重入的，两者关于这个的区别不大。**两者都是同一个线程没每入一次，锁的计数器都自增1，所以要等到锁的计数器下降为0时才能释放锁**。



**2、锁的实现**：    

`Synchronized`是依赖于`JVM`实现的，而`ReenTrantLock`是`JDK`实现的，有什么区别，说白了就类似于操作系统来控制实现和用户自己敲代码实现的区别。



**3、性能的区别**：    

在`Synchronized`优化以前，`synchronized`的性能是比`ReenTrantLock`差很多的，但是自从`Synchronized`引入了偏向锁，轻量级锁（自旋锁）后，两者的性能就差不多了。     

在两种方法都可用的情况下，官方甚至建议使用`synchronized`（比如`ConcurrentHashMap`的优化），**其实`synchronized`的优化借鉴了`ReenTrantLock`中的`CAS`技术。都是试图在用户态就把加锁问题解决，避免进入内核态的线程阻塞**。    



**4、功能区别**：    

便利性：很明显 `Synchronized` 的使用比较方便简洁，并且由编译器去保证锁的加锁和释放，而`ReenTrantLock`需要手工声明来加锁和释放锁，为了避免忘记手工释放锁造成死锁，所以最好在`finally`中声明释放锁。    



1、`ReenTrantLock`可以指定是公平锁还是非公平锁。而`synchronized`只能是非公平锁。所谓的公平锁就是先等待的线程先获得锁。    

2、`ReenTrantLock`提供了一个`Condition`（条件）类，用来实现分组唤醒需要唤醒的线程们，而不是像 `synchronized`要么随机唤醒一个线程要么唤醒全部线程   

3、`synchronized`是不可中断锁，而`ReentrantLock`则提供了中断功能：`ReenTrantLock`提供了一种能够中断等待锁的线程的机制，通过`lock.lockInterruptibly()`来实现这个机制（会抛异常）可中断锁是指线程尝试获取锁的过程是否可以响应终端。    



`ReenTrantLock`实现原理     

**`ReenTrantLock`的实现是一种自旋锁，**通过循环调用 `CAS` 操作来实现加锁。它的性能比较好也是因为避免了使线程进入内核态的阻塞状态。想尽办法避免线程进入内核的阻塞状态是我们去分析和理解锁设计的关键钥匙。     

​    

什么情况下使用 `ReenTrantLock`：

答案是，如果你需要实现`ReenTrantLock`的三个独有功能时。









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
