---
title: HashTable_ConcurrentHashMap
date: 2018-11-27 03:33:00
tags: 
- Java
category: 
- Java
description: HashTable_ConcurrentHashMap
---


**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            






# 1、HashTable

## 1.1、线程安全的原因 



但是HashTable线程安全的策略实现代价非常大，简单粗暴，<font color="red">  get/put所有相关操作都是synchronized的，这相当于给整个哈希表加了一把大锁，</font>    

多线程访问时候，只要有一个线程访问或操作该对象，那其他线程只能阻塞，相当于将所有的操作串行化，在竞争激烈的并发场景中性能就会非常差。   



![WX20181126-175036@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20181126-175036@2x.png)

　

## 1.2、Get取值

> synchronize上锁，强一致性



# 2、ConcurrentHashMap   

## 2.1、原理      

JDK8中ConcurrentHashMap参考了JDK8 HashMap的实现，采用了**数组+链表+红黑树**的实现方式来设计

**利用 ==CAS + synchronized== 来保证并发更新的安全 + volatile**    

  ![1587957489217](D:\study\HealerJean.github.io\blogImages\1587957489217.png)



## 2.2、实例变量       

```java
//默认为null，初始化发生在第一次插入操作，默认大小为16的数组，用来存储Node节点数据，扩容时大小总是2的幂次方
transient volatile Node<K,V>[] table;

//默认为null，扩容时新生成的数组，其大小为原数组的两倍。
private transient volatile Node<K,V>[] nextTable;



private transient volatile long baseCount;

//负数代表正在进行初始化或扩容操作 ,其中-1代表正在初始化 ,-N 表示有N-1个线程正在进行扩容操作      
//正数或0代表hash表还没有被初始化，这个数值表示初始化或下一次进行扩容的大小，**类似于扩容阈值**。它的值始终是当前ConcurrentHashMap容量的0.75倍，这与loadfactor是对应的。**实际容量>=sizeCtl，则扩容**。
private transient volatile int sizeCtl;

/**
 * The next table index (plus one) to split while resizing.
 */
private transient volatile int transferIndex;

/**
 * Spinlock (locked via CAS) used when resizing and/or creating CounterCells.
 */
private transient volatile int cellsBusy;

/**
 * Table of counter cells. When non-null, size is a power of 2.
 */
private transient volatile CounterCell[] counterCells;

// views
private transient KeySetView<K,V> keySet;
private transient ValuesView<K,V> values;
private transient EntrySetView<K,V> entrySet;
```



### 2.1.1、`int sizeCtl`

> sizeCtl最重要的属性之一   
>
> 负数代表正在进行初始化或扩容操作 ,其中-1代表正在初始化 ,-N 表示有N-1个线程正在进行扩容操作      
>
> 正数或0代表hash表还没有被初始化，**这个数值表示初始化或下一次进行扩容的阀值**，**类似于扩容阈值**。它的值始终是当前ConcurrentHashMap容量的0.75倍，这与loadfactor是对应的。**实际容量>=sizeCtl，则扩容**。



```java
private transient volatile int sizeCtl;
```



### 2.1.2、`Node<K,V>[] table`

> 默认为null，初始化发生在第一次插入操作，默认大小为16的数组，用来存储Node节点数据，扩容时大小总是2的幂次方

```java
transient volatile Node<K,V>[] table;
```





## 2.3、静态变量 

```java
// 最大容量 (和 hashmap相同)
private static final int MAXIMUM_CAPACITY = 1 << 30; 

// 初始容量 (和 hashmap相同)
private static final intDEFAULT_CAPACITY = 16;

static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8; // MAX_VALUE=2^31-1=2147483647

private static finalint DEFAULT_CONCURRENCY_LEVEL = 16;

//负载因子 (和 hashmap相同)
private static final float LOAD_FACTOR = 0.75f;
 
// 链表转树的阀值，如果table[i]下面的链表长度大于8时就转化为数 (和 hashmap相同)
static final int TREEIFY_THRESHOLD = 8; 

//树转链表的阀值，小于等于6是转为链表，仅在扩容tranfer时才可能树转链表 (和 hashmap相同)
static final int UNTREEIFY_THRESHOLD = 6; 

static final int MIN_TREEIFY_CAPACITY = 64;

private static final int MIN_TRANSFER_STRIDE = 16;

private static int RESIZE_STAMP_BITS = 16;
////  resize的最大线程数
private static final int MAX_RESIZERS = (1 << (32 - RESIZE_STAMP_BITS)) - 1; 

private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;

static final int MOVED = -1; // hash for forwarding nodes（forwarding nodes的hash值）、标示位

static final int TREEBIN = -2; // hash for roots of trees（树根节点的hash值）

static final int RESERVED = -3; // hash for transient reservations（ReservationNode的hash值）
```





## 2.4、构造函数  

### 2.4.1、无参构造函数 

```java
public ConcurrentHashMap() {
}

```



### 2.4.2、指定初始化桶数组的大小

```java
public ConcurrentHashMap(int initialCapacity) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException();
    int cap = ((initialCapacity >= (MAXIMUM_CAPACITY >>> 1)) ?
               MAXIMUM_CAPACITY :
               tableSizeFor(initialCapacity + (initialCapacity >>> 1) + 1));
    
    //需要初始化的桶的大小
    this.sizeCtl = cap;
}

```



hash桶没有在构造函数中初始化，`tableSizeFor(initialCapacity)`方法，这个方法的作用是，将你传入的`initialCapacity`做计算，返回一个大于等于`initialCapacity` 最小的2的幂次方。**这个暂时作为桶的初始化数量，没有初始化桶，只有在put存储键值对的时候才回进行桶的初始化**       



### 2.4.3、指定初始化桶数组的大小 和 负载因子

```java
public ConcurrentHashMap(int initialCapacity, float loadFactor) {
    this(initialCapacity, loadFactor, 1);
}
```



### 2.4.4、指定初始化桶数组的大小 和 负载因子以及`concurrencyLevel`

```java
public ConcurrentHashMap(int initialCapacity,
                         float loadFactor, int concurrencyLevel) {
    if (!(loadFactor > 0.0f) || initialCapacity < 0 || concurrencyLevel <= 0)
        throw new IllegalArgumentException();
    if (initialCapacity < concurrencyLevel)   // Use at least as many bins
        initialCapacity = concurrencyLevel;   // as estimated threads
    long size = (long)(1.0 + (long)initialCapacity / loadFactor);
    int cap = (size >= (long)MAXIMUM_CAPACITY) ?
        MAXIMUM_CAPACITY : tableSizeFor((int)size);
    this.sizeCtl = cap;
}
```



`concurrencyLevel`，**表示能够同时更新`ConccurentHashMap`且不产生锁竞争的最大线程数。默认值为16**，(即允许16个线程并发可能不会产生竞争)。为了保证并发的性能，**我们要很好的估计出concurrencyLevel值，不然要么竞争相当厉害，从而导致线程试图写入当前锁定的段时阻塞**。       

保证初始化的桶`initialCapacity`的大小比`concurrencyLevel`小的时候，**肯定是开发人员放错了，谁会将线程数设置的比桶的数量大呢**。`initialCapacity`就不可用了,初始化桶的数量就等于了`concurrencyLevel`    





### 2.3.5、直接放入一个已经存在的map

```java

public ConcurrentHashMap(Map<? extends K, ? extends V> m) {
    this.sizeCtl = DEFAULT_CAPACITY;
    putAll(m);
}
```





## 2.5、内部类 

### 2.5.1、Node<K,V>   

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    volatile V val; //volatile，保证可见性
    volatile Node<K,V> next; //volatile，保证可见性

    Node(int hash, K key, V val, Node<K,V> next) {
        this.hash = hash;
        this.key = key;
        this.val = val;
        this.next = next;
    }

    public final K getKey()       { return key; }
    public final V getValue()     { return val; }
    public final int hashCode()   { return key.hashCode() ^ val.hashCode(); }
    public final String toString(){ return key + "=" + val; }
    public final V setValue(V value) {
        throw new UnsupportedOperationException();
    }

    public final boolean equals(Object o) {
        Object k, v, u; Map.Entry<?,?> e;
        return ((o instanceof Map.Entry) &&
                (k = (e = (Map.Entry<?,?>)o).getKey()) != null &&
                (v = e.getValue()) != null &&
                (k == key || k.equals(key)) &&
                (v == (u = val) || v.equals(u)));
    }

    /**
         * Virtualized support for map.get(); overridden in subclasses.
         */
    Node<K,V> find(int h, Object k) {
        Node<K,V> e = this;
        if (k != null) {
            do {
                K ek;
                if (e.hash == h &&
                    ((ek = e.key) == k || (ek != null && k.equals(ek))))
                    return e;
            } while ((e = e.next) != null);
        }
        return null;
    }
}
```



### 2.5.2、TreeNode<K,V>红黑树   

> 和`HashMap`相比，这里的`TreeNode`相当简洁；`ConcurrentHashMap`链表转树时，并不会直接转，**只是把这些链表节点包装成`TreeNode`放到`TreeBin`中，再由`TreeBin`来转化红黑树**。红黑树不理解没关系，并不影响看`ConcurrentHashMap`的内部实现



```java
/**
     * Nodes for use in TreeBins
     */
static final class TreeNode<K,V> extends Node<K,V> {
    TreeNode<K,V> parent;  // red-black tree links
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;    // needed to unlink next upon deletion
    boolean red;

    TreeNode(int hash, K key, V val, Node<K,V> next,
             TreeNode<K,V> parent) {
        super(hash, key, val, next);
        this.parent = parent;
    }

    Node<K,V> find(int h, Object k) {
        return findTreeNode(h, k, null);
    }

    /**
         * Returns the TreeNode (or null if not found) for the given key
         * starting at given root.
         */
    final TreeNode<K,V> findTreeNode(int h, Object k, Class<?> kc) {
        if (k != null) {
            TreeNode<K,V> p = this;
            do  {
                int ph, dir; K pk; TreeNode<K,V> q;
                TreeNode<K,V> pl = p.left, pr = p.right;
                if ((ph = p.hash) > h)
                    p = pl;
                else if (ph < h)
                    p = pr;
                else if ((pk = p.key) == k || (pk != null && k.equals(pk)))
                    return p;
                else if (pl == null)
                    p = pr;
                else if (pr == null)
                    p = pl;
                else if ((kc != null ||
                          (kc = comparableClassFor(k)) != null) &&
                         (dir = compareComparables(kc, k, pk)) != 0)
                    p = (dir < 0) ? pl : pr;
                else if ((q = pr.findTreeNode(h, k, kc)) != null)
                    return q;
                else
                    p = pl;
            } while (p != null);
        }
        return null;
    }
}
```






### 2.1.3、`TreeBin<K,V> ` 

> TreeBin用于封装维护TreeNode，包含putTreeVal、lookRoot、UNlookRoot、remove、balanceInsetion、balanceDeletion等方法，当链表转树时，用于封装TreeNode，也就是说，ConcurrentHashMap的红黑树存放的时TreeBin，而不是treeNode。
>
> TreeBins类代码太长，截取部分代码如下：   



```java
    static final class TreeBin<K,V> extends Node<K,V> {
        TreeNode<K,V> root;
        volatile TreeNode<K,V> first;
        volatile Thread waiter;
        volatile int lockState;
        // values for lockState
        static final int WRITER = 1; // set while holding write lock
        static final int WAITER = 2; // set when waiting for write lock
        static final int READER = 4; // increment value for setting read lock

        /**
         * Creates bin with initial set of nodes headed by b.
         */
        TreeBin(TreeNode<K,V> b) {
            super(TREEBIN, null, null, null);
            this.first = b;
            TreeNode<K,V> r = null;
            for (TreeNode<K,V> x = b, next; x != null; x = next) {
                next = (TreeNode<K,V>)x.next;
                x.left = x.right = null;
                if (r == null) {
                    x.parent = null;
                    x.red = false;
                    r = x;
                }
                else {
                    K k = x.key;
                    int h = x.hash;
                    Class<?> kc = null;
                    for (TreeNode<K,V> p = r;;) {
                        int dir, ph;
                        K pk = p.key;
                        if ((ph = p.hash) > h)
                            dir = -1;
                        else if (ph < h)
                            dir = 1;
                        else if ((kc == null &&
                                  (kc = comparableClassFor(k)) == null) ||
                                 (dir = compareComparables(kc, k, pk)) == 0)
                            dir = tieBreakOrder(k, pk);
                            TreeNode<K,V> xp = p;
                        if ((p = (dir <= 0) ? p.left : p.right) == null) {
                            x.parent = xp;
                            if (dir <= 0)
                                xp.left = x;
                            else
                                xp.right = x;
                            r = balanceInsertion(r, x);
                            break;
                        }
                    }
                }
            }
            this.root = r;
            assert checkInvariants(root);
        }
        //........other methods
    }
```



### 2.5.4、ForwardingNode

> 在transfer扩容操作中，将一个节点插入到桶中

```java
    /*
     * A node inserted at head of bins during transfer operations.
     *在transfer操作中，一个节点插入到bins中
     */
    static final class ForwardingNode<K,V> extends Node<K,V> {
        final Node<K,V>[] nextTable;
        ForwardingNode(Node<K,V>[] tab) {
            //Node(int hash, K key, V val, Node<K,V> next)是Node类的构造函数
            super(MOVED, null, null, null);
            this.nextTable = tab;
        }

        Node<K,V> find(int h, Object k) {
            // loop to avoid arbitrarily deep recursion on forwarding nodes
            outer: for (Node<K,V>[] tab = nextTable;;) {
                Node<K,V> e; int n;
                if (k == null || tab == null || (n = tab.length) == 0 ||
                    (e = tabAt(tab, (n - 1) & h)) == null)
                    return null;
                for (;;) {
                    int eh; K ek;
                    if ((eh = e.hash) == h &&
                        ((ek = e.key) == k || (ek != null && k.equals(ek))))
                        return e;
                    if (eh < 0) {
                        if (e instanceof ForwardingNode) {
                            tab = ((ForwardingNode<K,V>)e).nextTable;
                            continue outer;
                        }
                        else
                            return e.find(h, k);
                    }
                    if ((e = e.next) == null)
                        return null;
                }
            }
        }
    }
```





## 2.5、put方法  

> 不能放入控制

```java
public V put(K key, V value) {
    return putVal(key, value, false);
}
```



1、检查key/value是否为空，如果为空，则抛空指针异常`NullPointerException`，否则进行2    

2、一个关闭Node数组for的死循环，进行3，一直到插入成功。    

3、检查table是否初始化了，如果没有，则调用initTable()进行初始化然后进行 2，否则进行4   

4、根据key的hash值计算出其应该在table中储存的位置i，取出table[i]的节点用f表示，根据f的不同有如下三种情况     

1）   如果`table[i]==null`(即该位置的节点为空)， **则利用CAS死循环操作直到存储在该位置**     

2）如果`table[i]!=null`(即该位置已经有其它节点)，**开始为这个数组节点`table[i]`头synchronized上锁**，碰撞处理也有两种情况。      

2.1）检查table[i]的节点的hash是否等于MOVED（-1），如果等于，则检测到正在扩容，则帮助其扩容        

2.2）说明table[i]的节点的hash值不等于MOVED，如果table[i]为链表节点，则将此节点插入链表中即可，如果table[i]为树节点，则将此节点插入树中即可 。插入成功后进行5    

5、如果table[i]的节点是链表节点，则检查table的第i个位置的链表是否需要转化为数（满足2个条件，同hashmap），如果需要则调用`treeifyBin`函数进行转化




```java

/** Implementation for put and putIfAbsent */
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) { //死循环，直到插入成功 
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        
  	
         //如果table[i]==null说明当前位置没有值，则利用CAS操作直接存储在该位置，如果CAS操作成功则退出死循环。否则一直继续for死循环执行
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null,
                         new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        else if ((fh = f.hash) == MOVED)////检查table[i]的节点的hash是否等于MOVED(静态常亮-1)，如果等于，则检测到正在扩容，则帮助其扩容
            tab = helpTransfer(tab, f);
        else {
            ///运行到这里，说明table[i]的节点的hash值不等于MOVED(-1也就是不需要扩容)。
            V oldVal = null;
            //hash值相同的链表的头节点上锁，（前面有执行过 f = tabAt(tab, i = (n - 1) & hash) ）  
            synchronized (f) {
                if (tabAt(tab, i) == f) { ///避免多线程，重新检查一下
                    if (fh >= 0) { //当前数组位置节点的hash值 >= 0 表示 链表节点
                        binCount = 1;
                        //下面的代码就是先查找链表中是否出现了此key，如果出现，则更新value，并跳出循环，否则将节点加入到链表末尾并跳出循环
                            
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                   //如果当前数组位置为红黑树节点
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                              value)) != null) {//插入到树中
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
                
            //插入成功后，如果插入的是链表节点，则要判断下该桶位是否要转化为树
            if (binCount != 0) {
                //实则是>8,执行else,说明该桶位本就有Node
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}
```



### 2.5.1、CAS操作    

#### 2.5.1.2、`tabAt` ：获取索引位置的node节点   

> 该方法用来获取 table 数组中索引为 i 的 Node 元素   
> `tabAt`方法原子读取`table[i]`；调用Unsafe对象的`getObjectVolatile方`法获取`tab[i]`，由于对volatile写操作happen-before于volatile读操作，因此其他线程对table的修改均对get读取可见；
((long)i << ASHIFT) + ABASE)计算i元素的地址

```java
  else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                if (casTabAt(tab, i, null,
                             new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
```




```java

// 数组对象的起始 offset
long ABASE = unsafe.arrayBaseOffset(Class)
// 对象的每个元素的长度，（boolean 1, char 2, short 2, int 4, reference 4, float 4, double 8, long 8）
int scale = unsafe.arrayIndexScale(Class)
// 需要移动的位数,如果数组元素是 int类型，则ASHIFT 为 2，即元素的偏移量为 ABASE + (1 << ASHIFT) * i. 第一个元素为 ABASE + 4； 第二个元素的偏移量为 ABASE + 8
int ASHIFT = 31 - Integer.numberOfLeadingZeros() 

// 使用 unsafe 以原子化的形式读取数组的第 i 个元素


 static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
        return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
    }
```



#### 2.5.1.1、`casTabAt` ：节点插入         

> 利用 CAS 操作设置 table 数组中索引为 i 的元素，不论多个线程执行，我都会在当前节点放入数据，如果是更新的话，后面有会`synchronize`锁    

```java
else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
    if (casTabAt(tab, i, null,
                 new Node<K,V>(hash, key, value, null)))
        break;                   // no lock when adding to empty bin
}
```



```java
    static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                        Node<K,V> c, Node<K,V> v) {
        return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
    }
```



#### 2.5.1.3、`setTabAt`：节点更新

> 替换节点，这一步操作之前已经上了`synchronize`锁了,仅在synchronized同步块中被调用，更新键值对；

```java
    static final <K,V> void setTabAt(Node<K,V>[] tab, int i, Node<K,V> v) {
        U.putObjectVolatile(tab, ((long)i << ASHIFT) + ABASE, v);
    }
```



## 2.6、initTable ：初始化数组 

```java
private final Node<K,V>[] initTable() {
    Node<K,V>[] tab; int sc;
    while ((tab = table) == null || tab.length == 0) {
        if ((sc = sizeCtl) < 0)//如果sizeCtl为负数，则说明已经有其它线程正在进行扩容或者初始化操作
            Thread.yield(); // 线程谦让掉  
        
        //如果CAS成功，说明我们要进行初始化操作，将sc将设置为 -1（volidate 原子性保证了可见性），最后再final中将会赋值给sizeCtl，否则说明其它线程已经对其正在初始化或是已经初始化完毕
        else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
            try {
                if ((tab = table) == null || tab.length == 0) {//再一次检查确认是否还没有初始化
                    int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                    @SuppressWarnings("unchecked")
                    Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                    table = tab = nt;
                    sc = n - (n >>> 2);//即sc = 0.75n。
                }
            } finally {
                sizeCtl = sc;//sizeCtl = 0.75*Capacity,为扩容门限
            }
            break;
        }
    }
    return tab;
}

```



## 2.7、treeifyBin ：链表转数 

> 这个方法之前已经校验过链表的长度是否大于等于8了    
>
> 此方法继续检查下table的长度是否大于等于MIN_TREEIFY_CAPACITY（64），如果不大于，则调用tryPresize方法将table两倍扩容就可以了，就不降链表转化为树了。如果大于，则就将table[i]的链表转化为树。

```java
  /*
     *链表转树：将将数组tab的第index位置的链表转化为 树
     */
    private final void treeifyBin(Node<K,V>[] tab, int index) {
        Node<K,V> b; int n, sc;
        if (tab != null) {
            if ((n = tab.length) < MIN_TREEIFY_CAPACITY)// 容量<64，则table两倍扩容，不转树了
                tryPresize(n << 1);
            else if ((b = tabAt(tab, index)) != null && b.hash >= 0) {
                synchronized (b) { // 读写锁  
                    if (tabAt(tab, index) == b) {
                        TreeNode<K,V> hd = null, tl = null;
                        for (Node<K,V> e = b; e != null; e = e.next) {
                            TreeNode<K,V> p =
                                new TreeNode<K,V>(e.hash, e.key, e.val,
                                                  null, null);
                            if ((p.prev = tl) == null)
                                hd = p;
                            else
                                tl.next = p;
                            tl = p;
                        }
                        setTabAt(tab, index, new TreeBin<K,V>(hd));
                    }
                }
            }
        }
    }

```



## 2.8、get()方法  

> 不上锁，所以ConcurrentHashMap不是强一致性的，因为val被volitale修饰，保证了可见性， 



```java
//会发现源码中没有一处加了锁
public V get(Object key) {
    Node<K,V>[] tab; Node<K,V> e, p; int n, eh; K ek;
    int h = spread(key.hashCode()); //计算hash
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (e = tabAt(tab, (n - 1) & h)) != null) {//读取首节点的Node元素
        if ((eh = e.hash) == h) { //如果该节点就是首节点就返回
            if ((ek = e.key) == key || (ek != null && key.equals(ek)))
                return e.val;
        }
        
 
        else if (eh < 0) //首个数组节点的位置，hash值为负数 table[i]为一颗树
            return (p = e.find(h, key)) != null ? p.val : null;
        
        ////以上都不成立，就是链表，遍历寻找即可
        while ((e = e.next) != null) {
            if (e.hash == h &&
                ((ek = e.key) == key || (ek != null && key.equals(ek))))
                return e.val;
        }
    }
    return null;
}
```





## 2.9、containsKey/containsValue

```java
   public boolean containsKey(Object key) {
        return get(key) != null;//直接调用get(int key)方法即可，如果有返回值，则说明是包含key的
    }

```

```java
  /*
     *功能，检查在所有映射(k,v)中只要出现一次及以上的v==value，返回true
     *注意：这个方法可能需要一个完全遍历Map，因此比containsKey要慢的多
     */
    public boolean containsValue(Object value) {
        if (value == null)
            throw new NullPointerException();
        Node<K,V>[] t;
        if ((t = table) != null) {
            Traverser<K,V> it = new Traverser<K,V>(t, t.length, 0, t.length);
            for (Node<K,V> p; (p = it.advance()) != null; ) {
                V v;
                if ((v = p.val) == value || (v != null && value.equals(v)))
                    return true;
            }
        }
        return false;
    }

```





## 2.6、ConcurrentHashMap 线程不安全行为  

 

containsKey和 put 两个方法都是原子的，但在jvm中并不是将这段代码做为单条指令来执行的，   

例如：假设连续生成2个随机数1，map的 containsKey 和 put 方法由线程A和B 同时执行 ，那么有可能会出现A线程还没有把 1 put进去时，B线程已经在进行if 的条件判断了，也就是如下的执行顺序：

```java

public class ThreadSafeTest {
public static Map<Integer,Integer> map=new ConcurrentHashMap<>();
public static void main(String[] args) {
    ExecutorService pool1 = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10; i++) {
        pool1.execute(new Runnable() {
            @Override
            public void run() {
                Random random=new Random();
                int randomNum=random.nextInt(10);
                if(map.containsKey(randomNum)){
                    map.put(randomNum,map.get(randomNum)+1);
                }else{
                    map.put(randomNum,1);
                }
            }
        });
    }
}

```



解决方案：改成同步

```java
public class ThreadSafeTest {
public static Map<Integer,Integer> map=new ConcurrentHashMap<>();
public static void main(String[] args) {
    ExecutorService pool1 = Executors.newFixedThreadPool(10);
    for (int i = 0; i < 10; i++) {
        pool1.execute(new Runnable() {
            @Override
            public void run() {
                Random random=new Random();
                int randomNum=random.nextInt(10);
               countRandom(randomNum);
            }
        });
    }
}
public static synchronized void countRandom(int randomNum){
    if(map.containsKey(randomNum)){
        map.put(randomNum,map.get(randomNum)+1);
    }else{
        map.put(randomNum,1);
    }
}


```





2.5、时间复杂度  

>  **从1.7到1.8版本，由于HashEntry从链表 变成了红黑树所以 concurrentHashMap的时间复杂度从O(n)到O(log(n))**



而对于锁的粒度，调整为对每个数组元素加锁（Node）。然后是定位节点的hash算法被简化了，这样带来的弊端是Hash冲突会加剧。因此在链表节点数量大于8时，会将链表转化为红黑树进行存储。这样一来，查询的时间复杂度就会由原先的O(n)变为O(logN)。下面是其基本结构：





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
		id: 'SXPrYHo72dbpFs5w',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

