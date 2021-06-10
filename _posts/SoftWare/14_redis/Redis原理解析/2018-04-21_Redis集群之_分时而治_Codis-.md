---
title: Redis集群之_分时而治_Codis
date: 2018-04-21 03:33:00
tags: 
- Redis
category: 
- Redis
description: Redis集群之_分时而治_Codis
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、引入

**问题1、为什么要有 `Redis` 集群？**    

答案：在大数据高并发场景下，单个 `Redis` 实例往往会显得捉襟见肘       



**问题2：怎么会呢？**     

答案：如下    

1、首先体现在内存上，单 个 `Redis` 的内存不宜过大，内存太大会导致` rdb` 文件过大，进一步导致主从同步时全量同 步时间过长，在实例重启恢复时也会消耗很长的数据加载时间。     

2、其次体现在 `CPU `的利用率上，单个 `Redis` 实例只能利用单个核心，这单个核心要完成海量数据的存取和管理工作压力会非常大。           



**问题3：所以怎么解决单机`Redis`呢？**

答案：正是在这样的大数据高并发的需求之下，`Redis` 集群方案应运而生。它可以将众多小内 存的 `Redis` 实例综合起来，将分布在多台机器上的众多 `CPU` 核心的计算能力聚集到一起， 完成海量数据存储和高并发读写操作。



# 2、`Codis` 介绍

> `Codis` 使用 `Go` 语言开发，它是一个代理中间件，它和 `Redis` 一样也使用 `Redis` 协议 对外提供服务，**当客户端向` Codis` 发送指令时，`Codis` 负责将指令转发到后面的 `Redis` 实例来执行，并将返回结果再转回给客户端。**       
>
> 1、`Codis` 上挂接的所有 `Redis `实例构成一个 `Redis` 集群，当集群空间不足时，可以通过动态增加 `Redis` 实例来实现扩容需求。     
>
> 2、客户端操纵 `Codis` 同操纵 `Redis` 几乎没有区别，还是可以使用相同的客户端 `SDK`，不需要任何变化。     
>
> 3、**因为 `Codis` 是无状态的，它只是一个转发代理中间件，这意味着我们可以启动多个 `Codis` 实例**，供客户端使用，每个 `Codis` 节点都是对等的。因为单个 `Codis` 代理能支撑的 `QPS` 比较有限，通过启动多个 `Codis` 代理可以显著增加整体的 `QPS` 需求，还能起到容灾功 能，挂掉一个 `Codis` 代理没关系，还有很多 `Codis `代理可以继续服务



![image-20210609171152514](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210609171152514.png)



# 3、`Codis` 分片原理

> `Codis` 要负责将特定的 `key `转发到特定的 `Redis` 实例，那么这种对应关系 `Codis `是如 何管理的呢?
>
> > 1、`Codis` 将所有的 `key` 默认划分为 `1024` 个槽位(`slot`)，它首先对客户端传过来的 `key` 进 行 `crc32` 运算计算哈希值     
> >
> > 2、再将 `hash` 后的整数值对 `1024` 这个整数进行取模得到一个余数，这个余数就是对应 `key` 的槽位。

![image-20210609171408323](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210609171408323.png)



**⬤ 每个槽位都会唯一映射到后面的多个 `Redis` 实例之一，`Codis` 会在内存维护槽位和`Redis` 实例的映射关系。这样有了上面 `key` 对应的槽位，那么它应该转发到哪个 `Redis` 实例就很明确了。**         

**⬤ 槽位数量默认是 `1024`，它是可以配置的，如果集群节点比较多，建议将这个数值配置大 一些，比如 `2048`、`4096`。**

```shell
hash = crc32(command.key) 
slot_index = hash % 1024 
redis = slots[slot_index].redis 
redis.do(command)

```



# 4、不同的 `Codis` 实例之间槽位关系如何同步

> 如果 `Codis` 的槽位映射关系只存储在内存里，那么不同的 `Codis` 实例之间的槽位关系 就无法得到同步。所以 `Codis` 还需要一个分布式配置存储数据库专门用来持久化槽位关系。 `Codis` 开始使用 `ZooKeeper`，后来连 `etcd` 也一块支持了。     
>
> > `Codis` 将槽位关系存储在 `zk` 中，并且提供了一个 `Dashboard` 可以用来观察和修改槽位 关系，当槽位关系变化时，`Codis` `Proxy` 会监听到变化并重新同步槽位关系，从而实现多个 `Codis` `Proxy` 之间共享相同的槽位关系配置。    



![image-20210609171924652](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210609171924652.png)



# 5、扩容

> 刚开始 `Codis` 后端只有一个` Redis` 实例，`1024` 个槽位全部指向同一个 `Redis`。然后一 个 `Redis` 实例内存不够了，所以又加了一个 `Redis` 实例。这时候需要对槽位关系进行调整， 将一半的槽位划分到新的节点。这意味着需要对这一半的槽位对应的所有 `key` 进行迁移，迁 移到新的 `Redis` 实例。



**问题1：那 `Codis` 如果找到槽位对应的所有 `key` 呢?**      

答案：`Codis` 对 `Redis` 进行了改造，增加了 `SLOTSSCAN` 指令，可以遍历指定 `slot` 下所有的 `key`。`Codis` 通过 `SLOTSSCAN` 扫描出待迁移槽位的所有的 `key`，然后挨个迁移每个 `key `到 新的 `Redis `节点。



**问题2：在迁移过程中，`Codis` 还是会接收到新的请求打在当前正在迁移的槽位上，因为当前槽位的数据（ 不代表是同一 `key` 哦）同时存在于新旧两个槽位中，`Codis` 如何判断该将请求转发到后面的哪个具体实例 呢?**       

答案：`Codis` 无法判定迁移过程中的 `key` 究竟在哪个实例中，当 `Codis` 接收到位于正在迁移槽位中的 `key` 后，会立即强制对当前的单个 `key` 进行迁移，迁移完成后，再将请求转发到新的 `Redis` 实例。



**问题3： `SLOTSSCAN `能够避免重复迁移吗？**     

答案：不能，`Redis` 支持的所有 `Scan` 指令都是无法避免重复的，同样 `Codis` 自定义的 `SLOTSSCAN `也是一样，但是这并不会影响迁移。因为单个 `key` 被迁移一次后，在旧实例中它就彻底被删除了，也就不可能会再次被扫描出来了





# 6、自动均衡

> `Redis` 新增实例，手工均衡 `slots` 太繁琐，所以 `Codis` 提供了自动均衡功能。自动均衡会在系统比较空闲的时候观察每个 `Redis` 实例对应的 `Slots` 数量，如果不平衡，就会自动进行 迁移。





# 7、`Codis` 的代价(限制)）

**1、事务不能支持**    

`Codis` 给 `Redis` 带来了扩容的同时，也损失了其它一些特性。因为 `Codis` 中所有的 `key `分散在不同的 `Redis` 实例中，所以事务就不能再支持了，事务只能在单个 `Redis` 实例中完 成。同样 `rename` 操作也很危险，它的参数是两个 `key`，如果这两个 `key` 在不同的 `Redis` 实 例中，`rename` 操作是无法正确完成的。`Codis` 的官方文档中给出了一系列不支持的命令列 表。      



**2、网络上开销变大**

`Codis` 因为增加了 `Proxy` 作为中转层，所有在网络开销上要比单个 `Redis` 大，毕竟数据 包多走了一个网络节点，整体在性能上要比单个 `Redis` 的性能有所下降。但是这部分性能损 耗不是太明显，可以通过增加 `Proxy` 的数量来弥补性能上的不足。          



**3、`Codis` 的集群配置中心依赖 `zk`**      

正所谓能省一步是一步，`Codis` 的集群配置中心使用 `zk` 来实现，意味着在部署上增加了 `zk` 运维的代价，不过 大部分互联网企业内部都有 `zk` 集群，可以使用现有的 `zk` 集群使用即可。         



**4、为了支持扩容，单个 `key` 对应的 `value` 不宜过大**       

因为集群的迁移的最小单位是 `key`，对于一个 `hash` 结构，它会一次性使用 `hgetall` 拉取所有的内容，然后使用 `hmset` 放置 到另一个节点。如果 `hash` 内部的 `kv` 太多（参考另一篇文章，大`Key`优化），可能会带来迁移卡顿。     



**5、 `mget` 指令指令会被拆解**     

`mget` 指令用于批量获取多个 `key` 的值，这些 `key` 可能会分布在多个 `Redis` 实例中。   `Codis` 的策略是将 `key` 按照所分配的实例打散分组，然后依次对每个实例调用 `mget` 方法， 最后将结果汇总为一个，再返回给客户端。



# 8、`Codis`的优点

**1、设计简单**     

`Codis` 在设计上相比 `Redis` `Cluster` 官方集群方案要简单很多，**因为它将分布式的问题交 给了第三方 `zk`/`etcd` 去负责，自己就省去了复杂的分布式一致性代码的编写维护工作**。而 `Redis` `Cluster` 的内部实现非常复杂，当集群出现故障时，维护人员往往不知道从 何处着手。       





# 9、`Codeis` 使用焦虑    



1、`Codis` 不是 `Redis` 官方项目，这意味着它的命运会无比曲折，它总是要被官方 `Redis` 牵 着牛鼻子走。当 `Redis `官方提供了什么功能它欠缺时，`Codis` 就会感到恐惧，害怕自己被市 场甩掉，所以必须实时保持跟进。

2、同时因为 `Codis` 总是要比 `Redis` 官方慢一拍，`Redis` 官方提供的最新功能，`Codis` 往往 要等很久才能同步。比如现在 `Redis` 已经进入到 `4.0` 阶段，提供了插件化 `Redis-Module`· 支 持，目前 `Codis` 还没有提供解决方案。     

3、现在 `Redis-Cluster` 在业界已经逐渐流行起来，`Codis`· 能否持续保持竞争力是个问题      



























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
		id: 'S8FuLj65sWGowmTb',
    });
    gitalk.render('gitalk-container');
</script> 



<!-- Gitalk end -->



