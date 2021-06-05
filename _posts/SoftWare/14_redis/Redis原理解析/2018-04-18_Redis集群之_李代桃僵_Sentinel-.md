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

![image-20210601193347947](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20210601193347947.png)





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

![image-20210601194654867](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20210601194654867.png)

`Sentinel` 会持续监控已经挂掉了主节点，待它恢复后， 集群会调整为下面这张图，此时原先挂掉的主节点现在变成了从节点，从新的主节点那里建立复制关系。。



![image-20210601194929297](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20210601194929297.png)

# 3、消息丢失

> `Redis` 主从采用异步复制，意味着当主节点挂掉时，从节点可能没有收到全部的同步消息，这部分未同步的消息就丢失了。如果主从延迟特别大，那么丢失的数据就可能会特别 多。    
>
> **注意:`Sentinel` 无法保证消息完全不丢失，但是也尽可能保证消息少丢失。它有两个选项可以限制主从延迟过大**。

```
min-slaves-to-write 1 
min-slaves-max-lag 10
```

`min-slaves-to-write 1`：**主节点必须至少有一个从节点在进行正常复制，否则就停止对外写服务，丧失可用性**。     

`min-slaves-max-lag 10`：**控制节点状态是否正常，单位是秒，表示如果 `10s` 没有收到从节点的反馈，就意味着从节点同步不正常，要么网络断开了，要么一直没有给反馈。**





# 4、`Sentinel` 实现原理

## 4.1、3个定时监控任务

### 4.1.1、每隔10秒，获取最新拓扑结构

> 每隔10秒，每个`Sentinel`节点会向主节点和从节点发送`info`命令来获取最新的拓扑结构         
>
> > 1、用来感知其他新加入的主从节点       
> >
> > 2、故障转移之后用来更新拓扑信息  



### 4.1.2、每隔2秒，发现新`Sentinel`，交换信息

> 每隔2秒会对主节点的频道发送该`Sentinel`对于主节点的判断以及当前`Sentinel`的信息。同时也会订阅该频道，来了解他们各自关于节点的判断

1、发现新的`Sentinel`节点并建立连接           

2、交换主节点的信息







3、每隔1秒，每隔sentinel节点会向主、从、sentinel及诶单发送ping命令，来确认是不是可达的。


## 6、主观下线和客观下线

6.1、主观下线

本章之前介绍到，每个Sentinel每隔1秒对主节点、从节点、其他sentinel节点发送ping命令来做心跳检测，当这些超过down-after-millisencondme没有有效回复，则对该节点做失败判定，这个行为就叫做主观下线，那么可以知道这个是当前的sentinel节点的一家之言，有误判的可能

 6.2、客观下线

当判定为下线的时候，需要和其他的节点进行商量，才会做出下线处理。这个判断就是客观的。































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



