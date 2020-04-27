---
title: HashMap中的key和value可以为空
date: 2020-04-207 03:33:00
tags: 
- Java
category: 
- Java
description: HashMap中的key和value可以为空
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



| 对象              | key是否可以为空 | value是否可以为空 |
| ----------------- | --------------- | ----------------- |
| HashMap           | 是              | 是                |
| HashTable         | 否              | 否                |
| ConcurrentHashMap | 否              | 否                |



# 1、代码层面分析



## 1.1、从HashMap分析  

**key可以为空：**

> `HashMap`在`put`的时候会调用`hash()`方法来**计算key的hashcode值**，可以从hash算法中看出**当key==null时返回的值为0**。因此key为null时，hash算法返回值为0，不会调用key的hashcode方法。      



**value可以为空；   **

> key都可以为空了，还在乎value吗，即使存在key，value为空怎么了。

```java
// 代码1
static final int hash(Object key) { // 计算key的hash值
    int h;
    // 1.先拿到key的hashCode值; 2.将hashCode的高16位参与运算
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```





## 1.2、从HashTable分析

**value不可以为空 ：**，  

>  **可以很清晰看到，直接就抛出异常了**    



**key不可以为空：**

> `key`为空，在执行到`int  hash = key.hashCode()`时同样会抛出`NullPointerException`异常   



```java
public synchronized V put(K key, V value) {
    // Make sure the value is not null
    if (value == null) {
        throw new NullPointerException();
    }

    // Makes sure the key is not already in the hashtable.
    Entry<?,?> tab[] = table;
    int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
    @SuppressWarnings("unchecked")
    Entry<K,V> entry = (Entry<K,V>)tab[index];
    for(; entry != null ; entry = entry.next) {
        if ((entry.hash == hash) && entry.key.equals(key)) {
            V old = entry.value;
            entry.value = value;
            return old;
        }
    }

    addEntry(hash, key, value, index);
    return null;
}
```





## 1.3、ConcurrentHashMap 

> 这也太明显了吧，直接就抛出空指针异常了

```java
 if (key == null || value == null) throw new NullPointerException();
```



```java
 final V putVal(K key, V value, boolean onlyIfAbsent) {
        if (key == null || value == null) throw new NullPointerException();
        int hash = spread(key.hashCode());
        int binCount = 0;
        for (Node<K,V>[] tab = table;;) {
            Node<K,V> f; int n, i, fh;
            if (tab == null || (n = tab.length) == 0)
                tab = initTable();
            else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
                if (casTabAt(tab, i, null,
                             new Node<K,V>(hash, key, value, null)))
                    break;                   // no lock when adding to empty bin
            }
            else if ((fh = f.hash) == MOVED)
                tab = helpTransfer(tab, f);
            else {
                V oldVal = null;
                synchronized (f) {
                    if (tabAt(tab, i) == f) {
                        if (fh >= 0) {
                            binCount = 1;
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
                        else if (f instanceof TreeBin) {
                            Node<K,V> p;
                            binCount = 2;
                            if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                           value)) != null) {
                                oldVal = p.val;
                                if (!onlyIfAbsent)
                                    p.val = value;
                            }
                        }
                    }
                }
                if (binCount != 0) {
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



# 2、设计层面分析 



个人理解：   

1、HashMap本身使用key和value为null就不应该存在，   

比如下面这行代码 ，我们还是不知道这个null是没有映射的null还是存的值就是null。

```java
System.out.println(hashMap.get("1")); //null
```



2、null指针本身就是一个让开发人员头大的问题，HashMap只在单线程中使用，不会存在修改了，有找不到的情况，但是多线程则的设计其实就更为复杂，既然复杂为什么不去掉呢，网上解释的什么 玩意。    



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
		id: 'dSgsqIRuokvwM1nG',
    });
    gitalk.render('gitalk-container');
</script> 


