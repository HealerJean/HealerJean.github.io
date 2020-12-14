---
title: ReentrantLock
date: 2020-02-20 03:33:00
tags: 
- Java
category: 
- Java
description: ReentrantLock
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、AQS

> **`AbstarctQueuedSynchronizer`简称`AQS`，是一个用于构建锁和同步容器的框架**。事实上`concurrent`包内许多类都是基于AQS构建的，     
>
> 例如ReentrantLock，Semphore，CountDownLatch，ReentrantReadWriteLock，FutureTask等。AQS解决了在实现同步容器时大量的细节问题。



AQS使用一个FIFO队列表示排队等待锁的线程，队列头结点称作“哨兵节点”或者“哑结点”，它不与任何线程关联。其他的节点与等待线程关联，每个阶段维护一个等待状态`waitStatus`。如图：



![image-20201214173045937](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201214173045937.png)





AQS中还有一个表示状态的字段`state`，例如`ReentrantLock`用它来表示线程重入锁的次数，`Semphore`用它表示剩余的许可数量，`FutureTask`用它表示任务的状态。对`state`变量值的更新都采用CAS操作保证更新操作的原子性。    



```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {    
    
    /**
     * The synchronization state.
     */
    private volatile int state;


}
```



`AbstractQueuedSynchronized`继承了`AbstractOwnableSynchronized`，这个类只有一个变量：`exclusiveOwnerThread`，表示当前占用该锁的线程，并且提供了相应的get，set方法。

```java
public abstract class AbstractOwnableSynchronizer
    implements java.io.Serializable {

    private static final long serialVersionUID = 3737899427754241961L;
   
    protected AbstractOwnableSynchronizer() { }

    
    private transient Thread exclusiveOwnerThread;

  
    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

   
    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}

```





# 2、 lock()与unlock()实现原理

> ReentrantLock是Lock的默认实现之一。那么Lock()和unlock(）是怎样实现的呢？首先我们要弄清楚几个概念
>
> > 1、可重入锁。可重入锁是指一个线程可以多次获取同一把锁。ReentrantLock和Synchronized都是可重入锁。   
> >
> > 2、可中断锁。可中断锁是指线程尝试获取锁的过程是否可以响应终端。synchronized是不可中断锁，而ReentrantLock则提供了中断功能。    
> >
> > 3、公平锁与非公平锁。公平所指多个线程同时尝试获取同一把锁时，获取锁的顺序按照线程达到的顺序，而非公平锁则允许线程“插队”。synchronized是非公平锁，而ReentrantLock的默认实现是非公平锁，但是也可以设置为公平锁。     
> >
> > 4、CAS操作(CompareAndSwap)。CAS操作简单的说就是比较并交换。CAS 操作包含三个操作数 —— 内存位置（V）、预期原值（A）和新值(B)。如果内存位置的值与预期原值相匹配，那么处理器会自动将该位置值更新为新值。否则，处理器不做任何操作。无论哪种情况，它都会在 CAS 指令之前返回该位置的值。CAS 有效地说明了“我认为位置 V 应该包含值 A；如果包含该值，则将 B 放到这个位置；否则，不要更改该位置，只告诉我这个位置现在的值即可。” Java并发包(java.util.concurrent)中大量使用了CAS操作,涉及到并发的地方都调用了sun.misc.Unsafe类方法进行CAS操作。



## 2.1、构造器



```java
//默认构造器初始化为NonfairSync对象，即非公平锁，

public ReentrantLock() {
    sync = new NonfairSync();
}
```



```java
// 带参数的构造器可以指定使用公平锁和非公平锁。由lock()和unlock的源码可以看到，它们只是分别调用了sync对象的lock()和release(1)方法。
public ReentrantLock(boolean fair) {
    sync = fair ? new FairSync() : new NonfairSync();
}
```

![image-20201214173636553](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201214173636553.png)



## 2.2、NonfairSync

![image-20201214184713073](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201214184713073.png)



### 2.2.1、`#lock`

> 我们从源代码出发，分析非公平锁获取锁和释放锁的过程。 

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;

    //首先用一个CAS操作，判断state是否是0（表示当前锁未被占用），如果是0则把它置为1，
    //并且设置当前线程为该锁的独占线程，表示获取锁成功。当多个线程同时尝试占用同一个锁时，CAS操作只能保证一个线程操作成功，剩下的只能乖乖的去排队啦。
     //“非公平”即体现在这里，如果占用锁的线程刚释放锁，state置为0，而排队等待锁的线程还未唤醒时，新来的线程就直接抢占了该锁，那么就“插队”了。
    final void lock() {
        if (compareAndSetState(0, 1))
            setExclusiveOwnerThread(Thread.currentThread());
        else
            acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}




//AbstractOwnableSynchronizer.java 中
protected final void setExclusiveOwnerThread(Thread thread) {
    exclusiveOwnerThread = thread;
}
```



　

### 2.2.2、`#acquire`

> 非公平锁tryAcquire的流程是：    
>
> 1、检查state字段，若为0，表示锁未被占用，那么尝试占用，    
>
> 2、若不为0，检查当前锁是否被自己占用，若被自己占用，则更新state字段，表示重入锁的次数。    
>
> 3、如果以上两点都没有成功，则获取锁失败，返回false。

```java
public final void acquire(int arg) {
    if (!tryAcquire(arg) && 
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) //(独占)
        selfInterrupt();
}
```



#### 2.2.1.1. 第一步：尝试去获取锁

>  第一步。尝试去获取锁。如果尝试获取锁成功，方法直接返回

```java

protected final boolean tryAcquire(int acquires) {
    return nonfairTryAcquire(acquires);
}


final boolean nonfairTryAcquire(int acquires) {
    final Thread current = Thread.currentThread();
    //获取state变量值
    int c = getState();
    ////没有线程占用锁
    if (c == 0) {
        if (compareAndSetState(0, acquires)) {
            //占用锁成功,设置独占线程为当前线程
            setExclusiveOwnerThread(current);
            return true;
        }
    }
    //当前线程已经占用该锁，说明是重入了
    else if (current == getExclusiveOwnerThread()) {
        int nextc = c + acquires;
        if (nextc < 0) // overflow
            throw new Error("Maximum lock count exceeded");
       // 更新state值为新的重入次数
        setState(nextc);
        return true;
    }
    
 //获取锁失败
    return false;
}

```



#### 2..2.1.2. 第二步：入队

> 如果没有获取到锁，则入队

```java
//将新节点和当前线程关联并且入队列 mode 独占/共享
private Node addWaiter(Node mode) {
    //初始化节点,设置关联线程和模式(独占 or 共享)
    Node node = new Node(Thread.currentThread(), mode);
    Node pred = tail;
    if (pred != null) {
        node.prev = pred;
        // 设置新节点为尾节点
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    //尾节点为空,说明队列还未初始化,需要初始化head节点并入队新节点
    enq(node);
    return node;
}

//  通过自旋的方式保证一定进入队列 初始化队列并且入队新节点
private Node enq(final Node node) {
    //开始自旋
    for (;;) {
        Node t = tail;
        if (t == null) { 
             // 如果tail为空,则新建一个head节点,并且tail指向head
            if (compareAndSetHead(new Node()))
                tail = head;
        } else {
            // tail不为空,将新节点入队
            node.prev = t;
            if (compareAndSetTail(t, node)) {
                t.next = node;
                return t;
            }
        }
    }
}


```



#### 2.2.1.3、第三步：挂起

> 这个方法让已经入队的线程尝试获取锁，若失败则会被挂起。

```java
/**
 * 已经入队的线程尝试获取锁
 */
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true; //标记是否成功获取锁
    try {
        boolean interrupted = false; //标记线程是否被中断过
        for (;;) {
            final Node p = node.predecessor(); //获取前驱节点
            //如果前驱是head,即该结点已成老二，那么便有资格去尝试获取锁
            if (p == head && tryAcquire(arg)) {
                setHead(node); // 获取成功,将当前节点设置为head节点
                p.next = null; // 原head节点出队,在某个时间点被GC回收
                failed = false; //获取成功
                return interrupted; //返回是否被中断过
            }
            // 判断获取失败后是否可以挂起,若可以则挂起
            if (shouldParkAfterFailedAcquire(p, node) &&
                parkAndCheckInterrupt())
                // 线程若被中断,设置interrupted为true
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}

// 值表示线程已被取消
/** waitStatus value to indicate thread has cancelled */
static final int CANCELLED =  1;
//指示后续线程需要打开
/** waitStatus value to indicate successor's thread needs unparking */
static final int SIGNAL    = -1;
//指示线程处于等待状态
/** waitStatus value to indicate thread is waiting on condition */
static final int CONDITION = -2;
//指示下一个被默许的应该无条件地传播

/**
 * 指示下一个被默许的应该无条件地传播
*/
static final int PROPAGATE = -3;


/**
 * 判断当前线程获取锁失败之后是否需要挂起.
 */
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    //前驱节点的状态
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
        // 前驱节点状态为signal,返回true
        return true;
    // 前驱节点状态为CANCELLED
    if (ws > 0) {
        // 从队尾向前寻找第一个状态不为CANCELLED的节点
        do {
            node.prev = pred = pred.prev;
        } while (pred.waitStatus > 0);
        pred.next = node;
    } else {
        // 将前驱节点的状态设置为SIGNAL
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}

/**
 * 挂起当前线程,返回线程中断状态并重置
 */
private final boolean parkAndCheckInterrupt() {
    LockSupport.park(this);
    return Thread.interrupted();
}
```

**线程入队后能够挂起的前提是，它的前驱节点的状态为`SIGNAL`，它的含义是“Hi，前面的兄弟，如果你获取锁并且出队后，记得把我唤醒！”。**    

所以`shouldParkAfterFailedAcquire`会先判断当前节点的前驱是否状态符合要求，   

若符合则返回true，   然后调用`parkAndCheckInterrupt`，将自己挂起,     

如果不符合，再看前驱节点是否>0(CANCELLED)，若是那么向前遍历直到找到第一个符合要求的前驱，若不是则将前驱节点的状态设置为SIGNAL。    



 整个流程中，如果前驱结点的状态不是SIGNAL，那么自己就不能安心挂起，需要去找个安心的挂起点，同时可以再尝试下看有没有机会去尝试竞争锁。

![image-20201214183852058](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20201214183852058.png)





### 2.2.3、`#unlock`

```java
public void unlock() {
    sync.release(1);
}
  
public final boolean release(int arg) {
    if (tryRelease(arg)) {
        Node h = head;
        if (h != null && h.waitStatus != 0)
            unparkSuccessor(h);
        return true;
    }
    return false;
}
```

如果理解了加锁的过程，那么解锁看起来就容易多了。流程大致为先尝试释放锁，若释放成功，那么查看头结点的状态是否为SIGNAL，如果是则唤醒头结点的下个节点关联的线程，     

**如果释放失败那么返回false表示解锁失败。这里我们也发现了，每次都只唤起头结点的下一个节点关联的线程。**

```java
/**
 * 释放当前线程占用的锁
 * @param releases
 * @return 是否释放成功
 */
protected final boolean tryRelease(int releases) {
    // 计算释放后state值
    int c = getState() - releases;
    // 如果不是当前线程占用锁,那么抛出异常
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    if (c == 0) {
        // 锁被重入次数为0,表示释放成功
        free = true;
        // 清空独占线程
        setExclusiveOwnerThread(null);
    }
    // 更新state值
    setState(c);
    return free;
}
```

`tryRelease`的过程为：   

当前释放锁的线程若不持有锁，则抛出异常。    

若持有锁，计算释放后的state值是否为0，若为0表示锁已经被成功释放，并且则清空独占线程，最后更新state值，返回free。 







## 2.3、`#FairSync`

>   公平锁和非公平锁不同之处在于，公平锁在获取锁的时候，不会先去检查state状态，而是直接执行aqcuire(1)，这里不再赘述。 

```java
static final class FairSync extends Sync {
    private static final long serialVersionUID = -3000897897090466540L;

    final void lock() {
        acquire(1);
    }

    /**
         * Fair version of tryAcquire.  Don't grant access unless
         * recursive call or no waiters or is first.
         */
    protected final boolean tryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        int c = getState();
        if (c == 0) {
            if (!hasQueuedPredecessors() &&
                compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0)
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
}
```



# 3、超时机制

> 　　在ReetrantLock的tryLock(long timeout, TimeUnit unit) 提供了超时获取锁的功能。它的语义是在指定的时间内如果获取到锁就返回true，获取不到则返回false。这种机制避免了线程无限期的等待锁释放。那么超时的功能是怎么实现的呢？我们还是用非公平锁为例来一探究竟。



```java
public boolean tryLock(long timeout, TimeUnit unit)
        throws InterruptedException {
    return sync.tryAcquireNanos(1, unit.toNanos(timeout));
}


public final boolean tryAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    if (Thread.interrupted())
        throw new InterruptedException();
    return tryAcquire(arg) ||
        doAcquireNanos(arg, nanosTimeout);
}
```

　　这里的语义是：如果线程被中断了，那么直接抛出InterruptedException。如果未中断，先尝试获取锁，获取成功就直接返回，获取失败则进入doAcquireNanos。tryAcquire我们已经看过，这里重点看一下doAcquireNanos做了什么。



```java
/**
 * 在有限的时间内去竞争锁
 * @return 是否获取成功
 */
private boolean doAcquireNanos(int arg, long nanosTimeout)
        throws InterruptedException {
    // 起始时间
    long lastTime = System.nanoTime();
    // 线程入队
    final Node node = addWaiter(Node.EXCLUSIVE);
    boolean failed = true;
    try {
        // 又是自旋!
        for (;;) {
            // 获取前驱节点
            final Node p = node.predecessor();
            // 如果前驱是头节点并且占用锁成功,则将当前节点变成头结点
            if (p == head && tryAcquire(arg)) {
                setHead(node);
                p.next = null; // help GC
                failed = false;
                return true;
            }
            // 如果已经超时,返回false
            if (nanosTimeout <= 0)
                return false;
            // 超时时间未到,且需要挂起
            if (shouldParkAfterFailedAcquire(p, node) &&
                    nanosTimeout > spinForTimeoutThreshold)
                // 阻塞当前线程直到超时时间到期
                LockSupport.parkNanos(this, nanosTimeout);
            long now = System.nanoTime();
            // 更新nanosTimeout
            nanosTimeout -= now - lastTime;
            lastTime = now;
            if (Thread.interrupted())
                //相应中断
                throw new InterruptedException();
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

doAcquireNanos的流程简述为：线程先入等待队列，然后开始自旋，尝试获取锁，获取成功就返回，    

失败则在队列里找一个安全点把自己挂起直到超时时间过期。这里为什么还需要循环呢？因为当前线程节点的前驱状态可能不是`SIGNAL`，那么在当前这一轮循环中线程不会被挂起，然后更新超时时间，开始新一轮的尝试。













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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 
