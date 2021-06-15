---
title: Redids畅通之_大key打散
date: 2019-05-01 03:33:00
tags: 
- Redids
category: 
- Redids
description: Redids畅通之_大key打散
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、大`key`的危害



## **1.1、内存分布不均匀**     

> 内存使用不均匀：当`slot`分配均匀的时候，大`key`的出现会导致`redis`内存使用的不均。       



## **1.2、容易造成网络堵塞**      

> **我理解，就是说会扫描很多内存页**，每次获取`bigkey`产生的网络流量比较大。涉及到大`key`的操作，尤其是使用`hgetall`、`lrange 0 -1`、`get`、`hmget` 等操作时，网卡可能会成为瓶颈，也会到导致堵塞其它操作，`qps` 就有可能出现突降或者突升的情况，趋势上看起来十分不平滑，严重时会导致应用程序连不上，实例或者集群在某些时间段内不可用的状态。    

假设一个`bigkey`为`1MB`，每秒访问量为`1000`，那么每秒产生`1000MB`的流量。             



## **1.3、影响扩容迁移效率**     

> **我理解，就是说会扫描很多内存页，会一次性申请更大的一块内存，这也会导致卡顿**      
>
> 在迁移过程中，如果每个 `key` 的内容都很小，`migrate` (`dump` + `resotre` + `del`) 指令执行会很快，它就并不会影响 客户端的正常访问。如果 `key` 的内容很大，因为 `migrate` 指令是阻塞指令会同时导致原节点和 目标节点卡顿，影响集群的稳定型。所以在集群环境下业务逻辑要尽可能避免大 `key `的产 生       



## **1.4、大`key`删除会引起卡顿**     

> **我理解，就是说会扫描很多内存页，如果这个大 `key` 被删除，内存会一次性回收，卡顿现象会再一次产生。**       
>
> 删除指令 `del` 会直接释放对象的内存，大部分情况下，这个指令非常快，没有明显延迟。不过如果删除的 `key `是一个非常大的对象，比如一个包含了千万元素的 `hash`，那么删除操作就会导致单线程卡顿。



# 2、大 `key` 扫描

```shell
redis-cli -h 127.0.0.1 -p 7001 –-bigkeys
```

如果你担心这个指令会大幅抬升 `Redis` 的 `ops` 导致线上报警，还可以增加一个休眠参 数。

```shell
redis-cli -h 127.0.0.1 -p 7001 –-bigkeys -i 0.1
```

上面这个指令每隔 `100` 条 `scan` 指令就会休眠 `0.1s`，`ops` 就不会剧烈抬升，但是扫描的 时间会变长。





# 3、删除大`key`

> 单个耗时过大命令，导致阻塞其他命令，容易引起应用程序雪崩或`Redis`集群发生故障切换。所以避免在生产环境中使用耗时过大命令。



| Key类型 | Item数量 | 耗时    |
| ------- | -------- | ------- |
| `Hash`  | ~100万   | ~1000ms |
| `List`  | ~100万   | ~1000ms |
| `Set`   | ~100万   | ~1000ms |
| `Zset`  | ~100万   | ~1000ms |

## 3.1、命令直接删除

> `Redis `为了解决这个卡顿问题，在 `4.0` 版本引入了 `unlink` 指令，它能对删除操作进行懒处理，丢给后台线程来异步回收内存。   
>
> 可以看我的另一篇文章  《朝生暮死_过期策略》

```
\> unlink key 
OK
```



## 3.2、命令缓缓删除

### 3.2.1、`Hash`

> `hash`： 使用 `hscan` + `hdel`



### 3.2.2、`List`

> `scan` + `ltrim`



### 3.2.3、`Set`

> `sscan + srem`



### 3.2.4、`Zset`

> `zremrangebyrank`



# 4、大`key`优化

## 4.1、`String`

### 4.1.1、该对象需要每次都整存整取

**方案1：**  可以尝试将对象分拆成几个`key` - `value`， 使用`multiGet`获取值，这样分拆的意义在于分拆单次操作的压力，将操作压力平摊到多个`redis`实例中，降低对单个`redis`的`IO`影响；   





### 4.1.4、该对象每次只需要存取部分数据

**方案1：** 可以尝试将对象分拆成几个`key` - `value`， 使用`multiGet`获取值，这样分拆的意义在于分拆单次操作的压力，将操作压力平摊到多个`redis`实例中，降低对单个`redis`的`IO`影响；            

**方案2：**可以将这个存储在一个`hash`中，每个`field`代表一个具体的属性，使用`hget`, `hmget`来获取部分的`value`，使用`hset`，`hmset`来更新部分属性   



## 4.2、`hash`， `set`，`zset`，`list` 

> 类似于场景一种的第一个做法，可以将这些元素分拆。   以下以以 `hash` 为例，`set`, `zset`, `list` 也可以类似上述做法     

1、原先的正常存取流程是  

```java
hget(hashKey, field) ; 
hset(hashKey, field, value)
```



2、现在，固定一个桶的数量，比如 `10000`， 每次存取的时候，先在本地计算`field`的`hash`值，模除 `10000`， 确定了该`field`落在哪个`key`上。

```
newHashKey  =  hashKey + (hash(field) % 10000）;  
```



3、使用新的`hash` `key`

```
hset (newHashKey, field, value) ;  
hget(newHashKey, field)
```

 

### 4.2.1、不适合的场景

1、要保证 `lpop` 的数据的确是最早`push`到`list`中去的，这个就需要一些附加的属性，或者是在`key`的拼接上做一些工作（比如`list`按照时间来分拆）。



## 4.3、大`Bitmap`或布隆过滤器（`Bloom` ）拆分

> 使用`bitmap`或布隆过滤器的场景，往往是数据量极大的情况，在这种情况下，`Bitmap`和布隆过滤器使用空间也比较大，比如用于公司`userid`匹配的布隆过滤器，就需要`512MB`的大小，这对`redis`来说是绝对的大`value`了。      
>
> 方案：这种场景下，我们就需要对其进行拆分，拆分为足够小的`Bitmap`，比如将`512MB`的大`Bitmap`拆分为`1024`个`512KB`的`Bitmap`。         
>
> > **不过拆分的时候需要注意，要将保证 一个`key`计算出的一系列`Hash`值都落在一个`Bitmap`上。 把所有拆分后的`Bitmap`当作独立的`bitmap`，然后通过分桶原理将不同的`key`分配给不同的小`bitmap`上，这样做后每次请求都只要在`redis`中一个`bitmap`上操作即可**。     
> >
> > **建议 ： `k` 取 13 个，  单个`bloomfilter`控制在 512KB 以下**

![image-20210615175822706](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210615175822706.png)

**问题1：通过这样拆分后，相当于`Bitmap`变小了，会不会增加布隆过滤器的误判率？**      

答案：实际上是不会的，布隆过滤器的误判率是哈希函数个数`k`，集合元素个数`n`，以及`Bitmap`大小`m`所决定的。      







# 5、`key` 太多

> 如果`key`的个数过多会带来更多的内存空间占用，  这两个方面在`key`个数上亿的时候消耗内存十分明显（`Redis 3.2`及以下版本均存在这个问题，`4.0`有优化）；  
>
> > 1、`key`本身的占用      
> >
> > 2、集群模式中，服务端需要建立一些`slot2key`的映射关系，这其中的指针占用在`key`多的情况下也是浪费巨大空间     



## 5.1、`key` 本身就有很强的相关性

> 比如：多个`key` 代表一个对象，每个`key`是对象的一个属性    
>
> 方案：这种可直接按照特定对象的特征来设置一个新 `Key`——`Hash` 结构， 原先的 `key` 则作为这个新`Hash` 的 `field`。



 举例： 原先存储的三个`key` ，  

```
user.zhangsan-id = 123;  
user.zhangsan-age = 18; 
user.zhangsan-country = china;   
```

这三个`key`本身就具有很强的相关特性，转成`Hash`存储就像这样

```
key = user.zhangsan

field:id = 123; 
field:age = 18; 
field:country = china;
```

 即`redis`中存储的是一个`key` ：`user.zhangsan`， 他有三个 `field`， 每个`field + key` 就对应原先的一个key



## 5.2、`key` 本身没有相关性

> 比如现在预估 `key` 的总数为 `2` 亿，      
>
> 方案：我们可以按照一个`hash`存储 `100`个`field`来算，需要 `2亿` / `100` = `200W` 个桶 (`200W` 个`key`占用的空间很少，`2`亿可能有将近 `20G` )

举例：有三个 `key` （`userId`） ：  `123456789`  ,  `987654321`，   `678912345`       

现在按照`200W` 固定桶分就是先计算出桶的序号  `hash(12345678)`  % `200W` ， 这里最好保证这个 `hash`算法的值是个正数，否则需要调整下模除的规则；        

这样算出三个`key` 的桶分别是   `1` ， `2`， `2`         

**这里 `bucket` `key` 为了标识出来意义， 加了个前缀 `userid` - `bucket`，  不影响整体逻辑，业务自行判断**     

```
存储的时候
	原先 set (realKey, value)      
	现在 hset（bucketKey，realKey， value ），
读取的时候
	原先 get（realKey）   
	现在 hget（bucketKey， realKey）   

 
key1 : 
	hset（userid-bucket-1,  123456789 ,  value ）      
	hget（userid-bucket-1,  123456789）

key2:  
 hset (userid-bucket-2,  987654321,  value )       
 hget（ userid-bucket-2,  987654321）

key3:  
	hset（userid-bucket-2,  678912345,  value)         
	hget（userid-bucket-2,  678912345）
```



















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
		id: 'SBdrw8YVnxbeyHCA',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



