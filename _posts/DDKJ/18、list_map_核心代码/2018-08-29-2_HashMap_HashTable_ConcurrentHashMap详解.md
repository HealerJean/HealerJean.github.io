---
title: HashMap_HashTable_ConcurrentHashMap
date: 2018-11-27 03:33:00
tags: 
- Map
category: 
- Map
description: Map详解
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

## 1、HashMap

众所周知 HashMap 底层是基于 `数组 + 链表` 组成的，不过在 jdk1.7 和 1.8 中具体实现稍有不同。

### 1.1、HashMap为什么线程不安全(hash碰撞与扩容导致)
HashMap的容量是有限的。当经过多次元素插入，使得HashMap达到一定饱和度时，Key映射位置发生冲突的几率会逐渐提高。<br/>

这时候，HashMap需要扩展它的长度，也就是进行Resize。<br/>

#### 影响发生Resize的因素有两个：
#### 1.Capacity

>HashMap的当前长度。上一期曾经说过，HashMap的长度是2的幂。

#### 2.LoadFactor

>HashMap负载因子，默认值为0.75f。

####  衡量HashMap是否进行Resize的条件如下：

>HashMap.Size   >=  Capacity * LoadFactor


#### 1.扩容

创建一个新的Entry空数组，长度是原数组的2倍。

#### 2.ReHash

遍历原Entry数组，把所有的Entry重新Hash到新数组。为什么要重新Hash呢？因为长度扩大以后，Hash的规则也随之改变。

![WX20181126-173242@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181126-173242@2x.png)




```java
/**
 * Transfers all entries from current table to newTable.
 */
void transfer(Entry[] newTable, boolean rehash) {
    int newCapacity = newTable.length;
    for (Entry<K,V> e : table) {
        while(null != e) {
            Entry<K,V> next = e.next;
            if (rehash) {
                e.hash = null == e.key ? 0 : hash(e.key);
            }
            int i = indexFor(e.hash, newCapacity);
            e.next = newTable[i];
            newTable[i] = e;
            e = next;
        }
    }
}

```

### 注意：下面的内容十分烧脑，请小伙伴们坐稳扶好。

![WX20181126-173418@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181126-173418@2x.png)


#### 1、此时达到Resize条件，两个线程各自进行Rezie的第一步，也就是扩容：

![WX20181126-173632@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181126-173632@2x.png)


[具体图解](https://mp.weixin.qq.com/s?__biz=MzI2NjA3NTc4Ng==&mid=2652079766&idx=1&sn=879783e0b0ebf11bf1a5767933d4e61f&chksm=f1748d73c6030465fe6b9b3fa7fc816d4704c91bfe46cb287aefccee459153d3287172d91d23&scene=21#wechat_redirect)


## 2、HashTable是线程安全的

但是HashTable线程安全的策略实现代价却太大了，简单粗暴，<font color="red">  get/put所有相关操作都是synchronized的，这相当于给整个哈希表加了一把大锁，</font>多线程访问时候，只要有一个线程访问或操作该对象，那其他线程只能阻塞，相当于将所有的操作串行化，在竞争激烈的并发场景中性能就会非常差。

![WX20181126-175036@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181126-175036@2x.png)

　HashTable性能差主要是由于所有操作需要竞争同一把锁，而如果容器中有多把锁，每一把锁锁一段数据，这样在多线程访问时不同段的数据时，就不会存在锁竞争了，这样便可以有效地提高并发效率。这就是ConcurrentHashMap所采用的"分段锁"思想。
　
　
## 3、ConcurrentHashMap

ConcurrentHashMap采用了非常精妙的"分段锁"策略，ConcurrentHashMap的主干是个Segment数组。


```java
 final Segment<K,V>[] segments;

```


　![WX20181126-175152@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181126-175152@2x.png)

　<font color="red"> Segment继承了ReentrantLock，所以它就是一种可重入锁（ReentrantLock) </font><br/>
　
　在ConcurrentHashMap，一个Segment就是一个子哈希表，Segment里维护了一个HashEntry数组，并发环境下，对于不同Segment的数据进行操作是不用考虑锁竞争的。<br/>

　<font color="red" size="4" >　
　（就按默认的ConcurrentLeve为16来讲，理论上就允许16个线程并发执行,因为这样就有16个Segment数组，有木有很酷）
　</font>
　<br/>
　
<font color="red" size="4" >　
所以，对于同一个Segment的操作才需考虑线程同步，不同的Segment则无需考虑。 
</font>

Segment类似于HashMap，一个Segment维护着一个HashEntry数组,HashEntry是目前我们提到的最小的逻辑处理单元了。一个ConcurrentHashMap维护一个Segment数组，一个Segment维护一个HashEntry数组。


```java
 transient volatile HashEntry<K,V>[] table;

```


HashEntry是目前我们提到的最小的逻辑处理单元了。一个ConcurrentHashMap维护一个Segment数组，一个Segment维护一个HashEntry数组。
```java

 static final class HashEntry<K,V> {
        final int hash;
        final K key;
        volatile V value;
        volatile HashEntry<K,V> next;
        //其他省略
}  
```


我们说Segment类似哈希表，那么一些属性就跟我们之前提到的HashMap差不离，比如负载因子loadFactor，比如阈值threshold等等，看下Segment的构造方法<br/>
　　
　　初始化方法有三个参数，如果用户不指定则会使用默认值，initialCapacity为16，loadFactor为0.75（负载因子，扩容时需要参考），concurrentLevel为16。

```java
Segment(float lf, int threshold, HashEntry<K,V>[] tab) {
            this.loadFactor = lf;//负载因子
            this.threshold = threshold;//阈值
            this.table = tab;//主干数组即HashEntry数组
        }
```

　　

### 我们来看下ConcurrentHashMap的构造方法

从下面的的代码可以看出来<br/>

>Segment数组的大小ssize是由concurrentLevel来决定的，但是却不一定等于concurrentLevel，ssize一定是大于或等于concurrentLevel的最小的2的次幂。比如：默认情况下concurrentLevel是16，则ssize为16；若concurrentLevel为14，ssize为16；若concurrentLevel为17，则ssize为32。



```java

public ConcurrentHashMap(int initialCapacity,
                               float loadFactor, int concurrencyLevel) {
          if (!(loadFactor > 0) || initialCapacity < 0 || concurrencyLevel <= 0)
              throw new IllegalArgumentException();
          //MAX_SEGMENTS 为1<<16=65536，也就是最大并发数为65536
          if (concurrencyLevel > MAX_SEGMENTS)
              concurrencyLevel = MAX_SEGMENTS;
          //2的sshif次方等于ssize，例:ssize=16,sshift=4;ssize=32,sshif=5
         int sshift = 0;
         //ssize 为segments数组长度，根据concurrentLevel计算得出
         int ssize = 1;
         while (ssize < concurrencyLevel) {
             ++sshift;
             ssize <<= 1;
         }
         //segmentShift和segmentMask这两个变量在定位segment时会用到，后面会详细讲
         this.segmentShift = 32 - sshift;
         this.segmentMask = ssize - 1;
         if (initialCapacity > MAXIMUM_CAPACITY)
             initialCapacity = MAXIMUM_CAPACITY;
         //计算cap的大小，即Segment中HashEntry的数组长度，cap也一定为2的n次方.
         int c = initialCapacity / ssize;
         if (c * ssize < initialCapacity)
             ++c;
         int cap = MIN_SEGMENT_TABLE_CAPACITY;
         while (cap < c)
             cap <<= 1;
         //创建segments数组并初始化第一个Segment，其余的Segment延迟初始化
         Segment<K,V> s0 =
             new Segment<K,V>(loadFactor, (int)(cap * loadFactor),
                              (HashEntry<K,V>[])new HashEntry[cap]);
         Segment<K,V>[] ss = (Segment<K,V>[])new Segment[ssize];
         UNSAFE.putOrderedObject(ss, SBASE, s0); 
         this.segments = ss;
     }
```



### put方法

　从源码看出，put的主要逻辑也就两步：
　1.定位segment并确保定位的Segment已初始化 
　2.调用Segment的put方法。



```java
public V put(K key, V value) {
        Segment<K,V> s;
        //concurrentHashMap不允许key/value为空
        if (value == null)
            throw new NullPointerException();
        //hash函数对key的hashCode重新散列，避免差劲的不合理的hashcode，保证散列均匀
        int hash = hash(key);
        //返回的hash值无符号右移segmentShift位与段掩码进行位运算，定位segment
        int j = (hash >>> segmentShift) & segmentMask;
        if ((s = (Segment<K,V>)UNSAFE.getObject          // nonvolatile; recheck
             (segments, (j << SSHIFT) + SBASE)) == null) //  in ensureSegment
            s = ensureSegment(j);
        return s.put(key, hash, value, false);
    }

```


### get 方法

　　get方法无需加锁，由于其中涉及到的共享变量都使用volatile修饰，volatile可以保证内存可见性，所以不会读取到过期数据。



```java
public V get(Object key) {
        Segment<K,V> s; 
        HashEntry<K,V>[] tab;
        int h = hash(key);
        long u = (((h >>> segmentShift) & segmentMask) << SSHIFT) + SBASE;
        //先定位Segment，再定位HashEntry
        if ((s = (Segment<K,V>)UNSAFE.getObjectVolatile(segments, u)) != null &&
            (tab = s.table) != null) {
            for (HashEntry<K,V> e = (HashEntry<K,V>) UNSAFE.getObjectVolatile
                     (tab, ((long)(((tab.length - 1) & h)) << TSHIFT) + TBASE);
                 e != null; e = e.next) {
                K k;
                if ((k = e.key) == key || (e.hash == h && key.equals(k)))
                    return e.value;
            }
        }
        return null;
    }
```


### 　put 下concurrentHashMap代理到Segment上的put方法，Segment中的put方法是要加锁的。只不过是锁粒度细了而已。



```java
final V put(K key, int hash, V value, boolean onlyIfAbsent) {
            HashEntry<K,V> node = tryLock() ? null :
                scanAndLockForPut(key, hash, value);//tryLock不成功时会遍历定位到的HashEnry位置的链表（遍历主要是为了使CPU缓存链表），若找不到，则创建HashEntry。tryLock一定次数后（MAX_SCAN_RETRIES变量决定），则lock。若遍历过程中，由于其他线程的操作导致链表头结点变化，则需要重新遍历。
            V oldValue;
            try {
                HashEntry<K,V>[] tab = table;
                int index = (tab.length - 1) & hash;//定位HashEntry，可以看到，这个hash值在定位Segment时和在Segment中定位HashEntry都会用到，只不过定位Segment时只用到高几位。
                HashEntry<K,V> first = entryAt(tab, index);
                for (HashEntry<K,V> e = first;;) {
                    if (e != null) {
                        K k;
                        if ((k = e.key) == key ||
                            (e.hash == hash && key.equals(k))) {
                            oldValue = e.value;
                            if (!onlyIfAbsent) {
                                e.value = value;
                                ++modCount;
                            }
                            break;
                        }
                        e = e.next;
                    }
                    else {
                        if (node != null)
                            node.setNext(first);
                        else
                            node = new HashEntry<K,V>(hash, key, value, first);
                        int c = count + 1;
　　　　　　　　　　　　　　//若c超出阈值threshold，需要扩容并rehash。扩容后的容量是当前容量的2倍。这样可以最大程度避免之前散列好的entry重新散列，具体在另一篇文章中有详细分析，不赘述。扩容并rehash的这个过程是比较消耗资源的。
                        if (c > threshold && tab.length < MAXIMUM_CAPACITY)
                            rehash(node);
                        else
                            setEntryAt(tab, index, node);
                        ++modCount;
                        count = c;
                        oldValue = null;
                        break;
                    }
                }
            } finally {
                unlock();
            }
            return oldValue;
        }
```

<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，添加博主微信哦， 请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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
		id: 'AAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

