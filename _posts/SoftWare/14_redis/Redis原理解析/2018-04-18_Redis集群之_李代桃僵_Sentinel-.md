---
title: Redis集群之_李代桃僵_Sentinel
date: 2018-04-18 03:33:00
tags: 
- Redis
category: 
- Redis
description: Redis集群之_李代桃僵_Sentinel
---



**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、引入

> 目前我们讲的 `Redis` 还只是主从方案，最终一致性。读者们可思考过，如果主节点凌晨 突发宕机怎么办?就坐等运维从床上爬起来，然后手工进行从主切换，再通知所有的程 序把地址统统改一遍重新上线么?     
>
> 毫无疑问，这样的人工运维效率太低，事故发生时估计得 至少 1 个小时才能缓过来。如果是一个大型公司，这样的事故足以上新闻了。    
>
> 所以我们必须有一个高可用方案来抵抗节点故障，当故障发生时可以自动进行从主切换，程序可以不用重启，运维可以继续睡大觉，仿佛什么事也没发生一样。`Redis` 官方提供 了这样一种方案 —— `Redis` `Sentinel`(哨兵)。



![image-20210601193347947](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210601193347947.png)





# 2、`Redis` `Sentinel` 

> 我们可以将`Redis` `Sentinel` 集群看成是一个 `ZooKeeper`集群，它是集群高可用的心脏， 它一般是由 `3~5` 个节点组成，负责持续监控主从节点的健康，挂了个别节点集群还可以正常运转。     
>
> > 它是一个分布式架构，其中包多个`Sentinel`节点和`Redis`数据节点    
> >
> > **1、每个`Sentinel`节点会对数据节点和其余`Sentinel`节点进行监控，当它发现节点不可达的时候，会节点做下线标识**     
> >
> > **2、如果被标识的是主节点，它还会和其他`Sentinel`商量，当大部分人为主节点不可用的时候**        
> >
> > **3、会选举出一个`Sentinel`节点用来完成主节点的自动故障转移的工作，同时将这个变化通知给应用方，整个过程是自动的。不需要人工来介入**



**简述过程：**     

1、当主节点挂掉时，自动选择一个最优的从节点切换为 主节点。    

2、客户端来连接集群时，会首先连接 `sentinel`，通过 `sentinel` 来查询主节点的地址， 然后再去连接主节点进行数据交互。      

3、当主节点发生故障时，客户端会重新向 `sentinel `要地址，`sentinel` 会将最新的主节点地址告诉客户端。     



**问题1：上面的图，如果主节点挂掉之后，`sentinel`怎么工作的?**

答案：主节点挂掉后，集群将可能自动调整为下图所示结构。     

1、从这张图中我们能看到主节点挂掉了，原先的主从复制也断开了，客户端和损坏的主节点也断开了。    

2、从节点被提升为新的主节点，其它从节点开始和新的主节点建立复制关系。      

3、客户端通过新的主节点继续进行交互。     

![image-20210601194654867](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210601194654867.png)

`Sentinel` 会持续监控已经挂掉了主节点，待它恢复后， 集群会调整为下面这张图，此时原先挂掉的主节点现在变成了从节点，从新的主节点那里建立复制关系。。



![image-20210601194929297](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210601194929297.png)



# 3、`Sentinel` 实现原理

## 3.1、3个定时监控任务

> 1、每`1s`，股东成员根据现有的公司名单检查考勤，看看CEO和小员工有没有好好上班（主观下线）；    
>
> 2、每`2s`，根据每`1s`观察的结果，股东成员发表对`CEO`的看法（客观下线），如果有新的股东成员，这时候也会说话，并和其他股东建立联系；    
>
> 3、每`10s`，股东询问`CEO`是否有招聘新员工，如果有，更新到公司名单里面，以后每`1s`检查考勤的时候也留意一下。



**1、每1秒，`sentinel`发送`ping`命令**

>每个`sentinel`对其他`sentinel`和`redis`主、从节点执行`ping`操作（心跳检测，失败判定的依据），确认当前节点是否可达



**2、每2秒，每个`sentinel`通过`master`节点的 `channel`交换信息（`pub/sub`）**

> 1、`master`节点上有一个发布订阅的频道`__sentinel__:hello`。`sentinel`节点通过`__sentinel__:hello`频道进行信息交换(掌握节点的信息和自身信息)。`Sentinel`节点之间在此频道上交换主节点的状态， 作为后面客观下线以及领导者选举的依据。
>
> 2、可以感知新加入的`sentinel`



**2、每`10`秒，每个`Sentinel`节点会向 `master` 节点和 `salve` 节点发送`info`命令来获取最新的拓扑结构**         

> 1、用来感知其他新加入的主从节点       
>
> 2、故障转移之后用来更新拓扑信息  




# 4、主观下线和客观下线

## 4.1、主观下线

> 上面介绍到，每个`Sentinel`每隔`1`秒对主节点、从节点、其他`sentinel`节点发送ping命令来做心跳检测，当这些超过`down-after-millisencondme`没有有效回复，则对该节点做失败判定，这个行为就叫做主观下线，那么可以知道这个是当前的`sentinel`节点的一家之言，有误判的可能



## 4.2、客观下线

> 当判定为下线的时候，需要和其他的节点进行商量，才会做出下线处理。这个判断就是客观的。





# 5、领导者选举

> 故障转移实际上是一个`sentinel`节点完成的，所以会进行领导者选举。

1、每个在线的 `sentinel` 节点都有资格成为领导者，当他确认主节点主观下线的时候，回向其他 `sentinel` 节点发送`sentinel is-master-down-by-addr` 命令要求将自己设置为领导者       

2、收到命令的`sentinel`节点，如果没有同意过其他`sentinel`节点，将同意它，否则拒绝       

3、如果该`sentinel`节点发送自己得票大于等于`max(quorum,(sentinel)/2+1)` 难免他将成为领导者      

4、如果没有选出则，将进行下一场选举





`quorum` 和 `majority`        

> `quorum`：确认`odown`的最少的哨兵数       
>
> `majority`：授权进行主从切换的最少的哨兵数量，= `(sentinel)/2+1`

1、每次一个哨兵要做主备切换，首先需要`quorum`数量的哨兵认为`odown`，然后选举出一个哨兵来做切换，这个哨兵还得得到`majority`哨兵数量的授权，才能正式执行切换     

2、如果`quorum `< `majority`，比如`5`个哨兵，`majority`就是3，`quorum`设置为`2`，那么就`3` 个哨兵授权就可以执行切换，    

3、如果 `quorum` >= `majority`，那么必须`quorum`数量的哨兵都授权，比如 `5`个哨兵，`quorum`是5，那么必须`5`个哨兵都同意授权，才能执行切换





**问题1：为什么哨兵至少3个节点？**     

答案：   哨兵集群必须部署2个以上节点。如果哨兵集群仅仅部署了个 `2` 个哨兵实例，那么它的 `majority` 就是 `2`（ `2` 的`majority` = `2`，`3` 的`majority` = `2`，`5` 的`majority` = `3`，`4` 的`majority` = `2`），如果其中一个哨兵宕机了，就无法满足 `majority` >= `2`这个条件，那么在`master`发生故障的时候也就无法进行主从切换  





# 6、故障转移

> 挑选一个从节点，准备让它上位

1、过滤掉不健康的，主观下线，断线，等      

2、选择从节点优先级高的`slave-proprity`，如果存在则返回，不存在则继续          

3、选择复制偏移量最大的，存在则返回，不存在则接续         

4、选择`runid`最小的从节点

5、将从节点执行`slaveof no one`命令让他成为主节点

6、`sentinel`会向其它从节点发送命令，让他们成为新主节点的从节点，复制规则和`parallel-syncs`参数有关

7、`sentinel` 结合将原来的主节点更新为从节点，保持它的关注，当其恢复后复制新的主节点信息      





# 7、脑裂以及`redis`数据丢失

## 7.1、数据丢失

### 7.1.1、异步复制导致的数据丢失 

> 因为`master` -> `slave `的复制是异步的，所以可能有部分数据还没复制到`slave`，`master`就宕机了，此时这些部分数据就丢失了      



### 7.1.2、脑裂导致的数据丢失 

> 脑裂，也就是说，某个`master`所在机器突然脱离了正常的网络，跟其他`slave`机器不能连接，但是实际上`master`还运行着 
> 此时哨兵可能就会认为`master`宕机了，然后开启选举，将其他`slave`切换成了`master`，这个时候，集群里就会有两个`master`，也就是所谓的脑裂。      
>
> 此时虽然某个`slave`被切换成了`master`，但是可能`client`还没来得及切换到新的`master`，还继续写向旧`master`的数据可能也丢失了，旧`master`再次恢复的时候，会被作为一个`slave`挂到新的`master`上去，自己的数据会清空，重新从新的`master`复制数据



## 7.2、如何尽可能减少数据丢失

> `Redis` 主从采用异步复制，意味着当主节点挂掉时，从节点可能没有收到全部的同步消息，这部分未同步的消息就丢失了。如果主从延迟特别大，那么丢失的数据就可能会特别 多。    
>
> **注意: `Sentinel` 无法保证消息完全不丢失，但是也尽可能保证消息少丢失。它有两个选项可以限制主从延迟过大**。   
>
> > 解释：要求至少有 `1`个`slave`，数据复制和同步的延迟不能超过 `10`秒，如果说一旦所有的`slave`，数据复制和同步的延迟都超过了`10`秒钟，那么这个时候，`master`就不会再接收任何请求了 



```
min-slaves-to-write 1
min-slaves-max-lag 10
```

| 参数                    | 说明                                                         |
| ----------------------- | ------------------------------------------------------------ |
| `min-slaves-to-write 1` | 主节点必须至少有一个从节点在进行正常复制，否则就停止对外写服务，丧失可用性 |
| `min-slaves-max-lag 10` | 控制节点状态是否正常，单位是秒，表示如果 `10s` 没有收到从节点的反馈，就意味着从节点同步不正常，要么网络断开了，要么一直没有给反馈 |



### 7.2.1、减少异步复制的数据丢失 

> 有了`min-slaves-max-lag`这个配置，就可以确保说，一旦`slave`复制数据和`ack`延时太长，就认为可能`master`宕机后损失的数据太多了，那么就拒绝写请求，这样可以把`master`宕机时由于部分数据未同步到`slave`导致的数据丢失降低的可控范围内     



### 7.2.2、减少脑裂的数据丢失 

> **重点：在脑裂场景下，最多就丢失`10`秒的数据**    
>
> 如果一个`master`出现了脑裂，跟其他`slave`丢了连接，那么上面两个配置可以确保说，     
>
> 1、如果不能继续给指定数量的`slave`发送数据，而且`slave`超过`10`秒没有给自己`ack`消息，那么就直接拒绝客户端的写请求，这样脑裂后的旧`master`就不会接受`client`的新数据，也就避免了数据丢失      



























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
		id: 'xzg4qVWPy3moERIs',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



