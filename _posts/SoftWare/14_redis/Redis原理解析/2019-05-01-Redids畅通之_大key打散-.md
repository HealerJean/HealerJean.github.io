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



