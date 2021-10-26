---
title: GuavaCach
date: 2021-10-26 00:00:00
tags: 
- Java
category: 
- Java
description: GuavaCach
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

# 1、`Guava Cache`是什么

> 在高并发场景下我们很多场景都会使用缓存来抗高并发。通常的做法都是使用redis等分布式缓存+本地二级缓存方案来解决。即首先读取分布式缓存，如果分布式缓存挂了，则降级读取作为二级缓存的本地缓存。在实现本地缓存的时候，我们通常也不会闭门造车，而是使用Google大神提供的Guava Cache



## 1.1、缓存

### 1.1.1、JVM缓存

> ` JVM` 缓存，也可以认为是堆缓存。其实就是创建一些全局变量，如 `Map、List` 之类的容器用于存放数据

**缺点：**

①、只能显式的写入，清除数据。      

②、不能按照一定的规则淘汰数据，如 `LRU，LFU，FIFO` 等。        

③、没有清除数据时的回调通知。         

④、其他一些定制功能等。

### 1.1.2、`Ehcache`、`Guava Cache`

> 由于 `JVM` 缓存的缺点，出现了一些专门用作 `JVM` 缓存的开源工具出现了，如： `Guava Cache`。它具有上文 `JVM` 缓存不具有的功能，如自动清除数据、多种清除算法、清除回调等。       

**缺点**：**因为有了这些功能，所以必然会多出许多东西需要额外维护，自然也就增加了系统的消耗**。

### 1.1.3、分布式缓存

> 上面的两种缓存其实都是堆内缓存，只能在单个节点中使用，这样在分布式场景下就招架不住了。于是也有了一些缓存中间件，如 `Redis`、`Memcached`，在分布式环境下可以共享内存。



# 

# 2、`maven`

```xml
<!--guava-->
<dependency>
  <groupId>com.google.guava</groupId>
  <artifactId>guava</artifactId>
  <version>31.0.1-jre</version>
</dependency>
```



# 3、`CacheBuilder.newBuilder()`

## 3.1、`concurrencyLevel`缓存的并发级别

> 并发级别是指可以同时写缓存的线程数  
>
> > `Guava`提供了设置并发级别的 `api`，使得缓存支持并发的写入和读取。同 `ConcurrentHashMap` 类似`Guava cache` 的并发也是通过分离锁实现。在一般情况下，将并发级别设置为服务器 `cpu` 核心数是一个比较不错的选择。

```java
   cache = CacheBuilder.newBuilder()
                .concurrencyLevel(8)
```



## 3.2、缓存的初始容量设置

> 我们在构建缓存时可以为缓存设置一个合理大小初始容量，由于`Guava`的缓存使用了分离锁的机制，扩容的代价非常昂贵。所以合理的初始容量能够减少缓存容器的扩容次数。

```java
cache = CacheBuilder.newBuilder()
  								   .initialCapacity(10)
```



## 3.3、缓存设置最大存储

> 超过缓存最大容量之后就会按照LRU最近虽少使用算法来移除缓存项
>
> `Guava Cache` 可以在构建缓存对象时指定缓存所能够存储的最大记录数量。当 `Cache` 中的记录数量达到最大值后再调用`put`方法向其中添加对象，`Guava`会先从当前缓存的对象记录中选择一条删除掉，腾出空间后再将新的对象存储到`Cache`中。
>
> > ⬤ 基于容量的清除：通过`CacheBuilder.maximumSize(long)`方法可以设置`Cache`的最大容量数，当缓存数量达到或接近该最大值时，`Cache` 将清除掉那些最近最少使用的缓存;          
> >
> > ⬤ 基于权重的清除:  使用 `CacheBuilder.weigher(Weigher)` 指定一个权重函数，并且用 `CacheBuilder.maximumWeight(long)`指定最大总重。

```java
cache = CacheBuilder.newBuilder()
                .maximumSize(100)
```



## 3.4、缓存清除策略

### 3.4.1、`expireAfterAccess`

> 缓存项在给定时间内没有被读/写访问，则回收



### 3.4.2、`expireAfterWrite`

> 设置写缓存后`n`秒钟过期，过期删除
>
> 使用了`expireAfterWrites`之后，每次缓存失效 `LoadingCache` 都会去调用我们实现的`load`方法去重新加载缓存，**在加载期间，所有线程对该缓存 `key`的访问都将被 `block`。所以如果实际加载缓存需要较长时间的话，这种方式不太适用**。  
>
> > 工作原理：这里`Guava`内部会对某个时间点失效的缓存做统一失效，即：只要有`get`访问任一key，就会失效当前时间失效的缓存，会移除当前`key`。



```java
cache = CacheBuilder.newBuilder()
  .expireAfterWrite(5, TimeUnit.SECONDS)
  .build(new CacheLoader<Integer, Optional<CacheDTO>>() {
    @Override
    public Optional<CacheDTO> load(Integer id) throws Exception {
      log.info("线程名：{}, 加载数据开始", Thread.currentThread().getName());
      TimeUnit.SECONDS.sleep(8);
      Random random = new Random();

      CacheDTO cacheDTO = new CacheDTO()
        .setId(random.nextInt())
        .setName("HealerJean");
      log.info("线程名：{}, 加载数据结束", Thread.currentThread().getName());
      return Optional.ofNullable(cacheDTO);
    }
  });
```



### 3.4.3、`refreshAfterWrite`

> 定时刷新，可以为缓存增加自动定时刷新功能。缓存项只有在被检索时才会真正刷新，即只有刷新间隔时间到了再去 `get(key)` 才会重新去执行 `reload`，否则就算刷新间隔时间到了也不会执行loading操作。
>
> > 用了`refreshAfterWrites` 之后，需要自己实现 `CacheLoader` 的 `reload` 方法，在方法中创建一个 `ListenableFutureTask`，然后将这个`task`提交给线程池去异步执行，`reload`方法最后返回这个`ListenableFutureTask`。这样做之后，缓存失效后重新加载就变成了异步。**加载期间尝试获取缓存的线程也都不会被`block`，而是获取到加载之前的值。当加载完毕之后，各个线程就能取到最新值了（也就是说：只阻塞当前数据加载线程，其他线程返回旧值）**。



```java
private static Executor executor = Executors.newFixedThreadPool(10);


cache = CacheBuilder.newBuilder()
  .refreshAfterWrite(7, TimeUnit.SECONDS)
  .build(new CacheLoader<Integer, Optional<CacheDTO>>() {
    @Override
    public Optional<CacheDTO> load(Integer id) throws Exception {
      return Optional.ofNullable(null);
    }

    @Override
    public ListenableFuture<Optional<CacheDTO>> reload(Integer id, Optional<CacheDTO> oldValu) 
      throws Exception {
      log.info("线程名：{}, reload 开始", Thread.currentThread().getName());
      ListenableFutureTask<Optional<CacheDTO>> futureTask = ListenableFutureTask.create(() -> {
        Random random = new Random();
        CacheDTO cacheDTO = new CacheDTO()
          .setId(random.nextInt())
          .setName("HealerJean");
        return Optional.ofNullable(cacheDTO);
      });
      executor.execute(futureTask);
      log.info("线程名：{}, reload 已执行", Thread.currentThread().getName());
      return futureTask;
    }
  });
```



### 3.4.4、总结:

1、 对于互联网高并发的场景，`refreshAfterWrites` 这种使用异步刷新缓存的方法应该是我们首先考虑的，取到一个过期的旧值总比大量线程全都被 `block` 要好。`expireAfterWrites` 可能会导致请求大量堆积，连接数不够等一些列问题。     



**2、`expireAfterWrite` 与 `refreshAfterWrite` 同时配置的话。**    

> > ⬤ `expire` 小于等于 `refresh` 时间，优先expire失效，同时满足走 `expire`（无法refresh）。    `expire `大于 `refresh `时间 
> >
> > ⬤ 优先 `refresh`，同时满足走`expire`。





## 2.5、`recordStats`

> 是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除

```java
cache = CacheBuilder.newBuilder()
											.recordStats()
  
  
log.info("缓存统计信息：{}", cache.stats());
```



## 2.6、`removalListener`

> 设置缓存的移除通知

```java
cache = CacheBuilder.newBuilder()
  .removalListener(notification -> {
    log.info("缓存移除通知：key:{}, value{}, case:{}", 
             notification.getKey(), 
             notification.getValue(), 
             notification.getCause());
  })
```



# 4、`cache#get`

> `Cache` 的 `get` 方法有两个参数，第一个参数是要从 `Cache` 中获取记录的 `key`，第二个记录是一个`Callable`对象。
>
> ⬤ 当缓存中已经存在`key`对应的记录时，`get`方法直接返回`key`对应的记录。        
>
> ⬤ 如果缓存中不包含`key`对应的记录，`Guava`会启动一个线程执行`Callable`对象中的`call` 方法，`call` 方法的返回值会作为 `key` 对应的值被存储到缓存中，并且被`get`方法返回。

```JAVA
CacheDTO cacheDTO = cache.get(1).orElse(null);


```

```JAVA
CacheDTO cacheDTO = cache.get(0, () -> {
		CacheDTO cacheDTO1 = new CacheDTO()
		.setId(0)
		.setName("NULL");
		return Optional.ofNullable(cacheDTO1);
}).orElse(null);
```





# 5、工具类

## 5.1、测试类

```java

@Slf4j
public class GuavaCacheService {

  private static Executor executor = Executors.newFixedThreadPool(10);
  private static LoadingCache<Integer, Optional<CacheDTO>> cache = null;

  public static void initCache() {
    log.info("线程名：{}, LoadingCache初始化", Thread.currentThread().getName());

    cache = CacheBuilder.newBuilder()
      //设置并发级别为8，
      .concurrencyLevel(8)
      //设置缓存容器的初始容量为10
      .initialCapacity(10)
      //设置最大存储
      .maximumSize(100)
      //是否需要统计缓存情况,该操作消耗一定的性能,生产环境应该去除
      .recordStats()
      // 缓存清除策略
      //  expireAfterWrite 写缓存后多久过期
      //  expireAfterAccess 缓存项在给定时间内没有被读/写访问
      .expireAfterWrite(5, TimeUnit.SECONDS)
      // 定时刷新，只阻塞当前数据加载线程，其他线程返回旧值。
      .refreshAfterWrite(7, TimeUnit.SECONDS)

      //设置缓存的移除通知
      .removalListener(notification -> {
        log.info("缓存移除通知：key:{}, value{}, case:{}", 
                 notification.getKey(), 
                 notification.getValue(), 
                 notification.getCause());
      })

      .build(new CacheLoader<Integer, Optional<CacheDTO>>() {
        @Override
        public Optional<CacheDTO> load(Integer id) throws Exception {
          log.info("线程名：{}, load 开始", Thread.currentThread().getName());
          CacheDTO cacheDTO = new CacheDTO()
            .setId(1)
            .setName("HealerJean");
          log.info("线程名：{}, load 结束", Thread.currentThread().getName());
          return Optional.ofNullable(cacheDTO);

        }

        @Override
        public ListenableFuture<Optional<CacheDTO>> reload(Integer id, 
                                                           Optional<CacheDTO> oldValu) throws Exception {
          log.info("线程名：{}, reload 开始", Thread.currentThread().getName());
          ListenableFutureTask<Optional<CacheDTO>> futureTask = ListenableFutureTask.create(() -> {
            Random random = new Random();
            CacheDTO cacheDTO = new CacheDTO()
              .setId(random.nextInt())
              .setName("HealerJean");
            return Optional.ofNullable(cacheDTO);
          });
          executor.execute(futureTask);
          log.info("线程名：{}, reload 已执行", Thread.currentThread().getName());
          return futureTask;
        }

      });

    log.info("线程名：{}, LoadingCache 结束", Thread.currentThread().getName());
  }

  public static void main(String[] args) {

    initCache();

    //模拟线程并发
    new Thread(() -> {
      try {
        for (int i = 0; i < 10; i++) {
          CacheDTO cacheDTO = cache.get(1).orElse(null);
          log.info("main1 线程名:{}  时间:{} cache:{}", 
                   Thread.currentThread().getName(), 
                   LocalDateTime.now(), cacheDTO);
         					 TimeUnit.SECONDS.sleep(3);
        }
      } catch (Exception ignored) {
      }
    }).start();


    new Thread(() -> {
      try {
        for (int i = 0; i < 10; i++) {
          CacheDTO cacheDTO = cache.get(0, () -> {
            CacheDTO cacheDTO1 = new CacheDTO()
              .setId(0)
              .setName("NULL");
            return Optional.ofNullable(cacheDTO1);
          }).orElse(null);


          log.info("main2 线程名:{}  时间:{} cache:{}", 
                   Thread.currentThread().getName(), 
                   LocalDateTime.now(), 
                   cacheDTO);
          TimeUnit.SECONDS.sleep(5);
        }
      } catch (Exception ignored) {
      }
    }).start();


    log.info("缓存统计信息：{}", cache.stats());
  }

}
```



## 5.2、工具类

```java

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheManager {

    /**
     * 缓存项最大数量
     */
    private static final long GUAVA_CACHE_SIZE = 100000;
    /**
     * 缓存时间：天
     */
    private static final long GUAVA_CACHE_DAY = 10;
    /**
     * 缓存操作对象
     */
    private static LoadingCache<Long, String> GLOBAL_CACHE = null;

    static {
        try {
            GLOBAL_CACHE = loadCache(new CacheLoader<Long, String>() {
                @Override
                public String load(Long key) throws Exception {
                    // 处理缓存键不存在缓存值时的处理逻辑
                    LoadingCache<Long, String> cache = CacheBuilder.newBuilder()
                            //缓存池大小，在缓存项接近该大小时， Guava开始回收旧的缓存项
                            .maximumSize(GUAVA_CACHE_SIZE)
                            // 设置缓存在写入之后 设定时间 后失效
                            .expireAfterWrite(GUAVA_CACHE_DAY, TimeUnit.DAYS)
                            //移除监听器,缓存项被移除时会触发
                            .removalListener((RemovalListener<Long, String>) rn -> {
                                //逻辑操作
                            })
                            //开启Guava Cache的统计功能
                            .recordStats()
                            .build(new CacheLoader<Long, String>() {
                                @Override
                                public String load(Long aLong) throws Exception {
                                    // 处理缓存键不存在缓存值时的处理逻辑
                                    return "null";
                                }
                            });
                }
            });
        } catch (Exception e) {
            log.error("初始化Guava Cache出错", e);
        }
    }

    /**
     * 设置缓存值
     * 注: 若已有该key值，则会先移除(会触发removalListener移除监听器)，再添加
     *
     * @param key
     * @param value
     */
    public static void put(Long key, String value) {
        try {
            GLOBAL_CACHE.put(key, value);
        } catch (Exception e) {
            log.error("设置缓存值出错", e);
        }
    }

    /**
     * 批量设置缓存值
     *
     * @param map
     */
    public static void putAll(Map<? extends Long, ? extends String> map) {
        try {
            GLOBAL_CACHE.putAll(map);
        } catch (Exception e) {
            log.error("批量设置缓存值出错", e);
        }
    }

    /**
     * 获取缓存值
     * 注：如果键不存在值，将调用CacheLoader的load方法加载新值到该键中
     *
     * @param key
     * @return
     */
    public static String get(Long key) {
        String token = "";
        try {
            token = GLOBAL_CACHE.get(key);
        } catch (Exception e) {
            log.error("获取缓存值出错", e);
        }
        return token;
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public static void remove(Long key) {
        try {
            GLOBAL_CACHE.invalidate(key);
        } catch (Exception e) {
            log.error("移除缓存出错", e);
        }
    }

    /**
     * 批量移除缓存
     *
     * @param keys
     */
    public static void removeAll(Iterable<Long> keys) {
        try {
            GLOBAL_CACHE.invalidateAll(keys);
        } catch (Exception e) {
            log.error("批量移除缓存出错", e);
        }
    }

    /**
     * 清空所有缓存
     */
    public static void removeAll() {
        try {
            GLOBAL_CACHE.invalidateAll();
        } catch (Exception e) {
            log.error("清空所有缓存出错", e);
        }
    }

    /**
     * 获取缓存项数量
     *
     * @return
     */
    public static long size() {
        long size = 0;
        try {
            size = GLOBAL_CACHE.size();
        } catch (Exception e) {
            log.error("获取缓存项数量出错", e);
        }
        return size;
    }
}
```



# 6、原理

## 6.1、**如何做到高效读写**

> `Guava Cache` 借鉴了 `ConcurrentHashMap` 的实现原理(基于1.7版本的实现，即没有使用红黑树)，使用了桶+链表的方式来实现。其这部分实现代码逻辑集合都和`ConcurrentHashMap`一样。如下是新增缓存项的一段代码，是不是很`ConcurrentHashMap`很像呢。      
>
> > `Guava Cache` 借鉴了 `ConcurrentHashMap` 的思想（甚至代码结构和实现都差不多），通过分段锁的方式解决了高并发读写的问题。



```java
public V put(K key, V value) {
  Preconditions.checkNotNull(key);
  Preconditions.checkNotNull(value);
  int hash = this.hash(key);
  return this.segmentFor(hash).put(key, hash, value, false);
}
```



## 6.2、缓存项数量和容量大小限制实现

> **`maximumWeight` 和  这二者的实现都是通过权重来实现的**。
>
> > 实现容量大小限制的时候，通过`maximumWeight`来置总容量大小，然后通过`weigher`函数来计算并告诉 `Guava Cache`每个缓存项的容量大小。这样`Guava Cache`就只需要将所有的缓存项目的权重值相加就能够知道其是否超过最大容量限制了。
>
> > 在实现缓存项数据量大小限制的时候，虽然是通过`maximumSize`来指定的最大缓存项数据量。其实底层使用了和权重相同的代码逻辑实现，只是这里每个缓存项的权重为1。



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
		id: 'P6Ba59Ud7LJg1V2l',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



