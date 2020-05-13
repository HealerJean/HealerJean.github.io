---
title: Eureka和Zookeeper注册中心的区别
date: 2020-05-13 03:33:00
tags: 
- SpringCloud
category: 
- SpringCloud
description: Eureka和Zookeeper注册中心的区别
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、Eureka和Zookeeper  

## 1.1、Zookeeper 

### 1.2.1、Zookeeper保证CP

不能保证每次服务请求的可用性。     

任何时刻对ZooKeeper的访问请求能得到一致的数据结果，同时系统对网络分割具备容错性；但是它不能保证每次服务请求的可用性（注：也就是在极端环境下，ZooKeeper可能会丢弃一些请求，消费者程序需要重新请求才能获得结果）。所以说，ZooKeeper不能保证服务可用性。   



### 1.2.2、为什么不能保证可用性

> **进行leader选举时集群都是不可用。**       

在使用ZooKeeper获取服务列表时，当master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举。问题在于，选举leader的时间太长，30 ~ 120s, 且选举期间整个zk集群都是不可用的，这就导致在选举期间注册服务瘫痪，虽然服务能够最终恢复，**但是漫长的选举时间导致的注册长期不可用是不能容忍的。所以说，ZooKeeper不能保证服务可用性。**     



**当向注册中心查询服务列表时**，我们可以容忍注册中心返回的是几分钟以前的注册信息，但不能接受服务直接down掉不可用。也就是说，服务注册功能对可用性的要求要高于一致性。但是zk会出现这样一种情况，当master节点因为网络故障与其他节点失去联系时，剩余节点会重新进行leader选举。问题在于，选举leader的时间太长，30 ~ 120s, 且选举期间整个zk集群都是不可用的，这就导致在选举期间注册服务瘫痪。在云部署的环境下，因网络问题使得zk集群失去master节点是较大概率会发生的事，虽然服务能够最终恢复，但是漫长的选举时间导致的注册长期不可用是不能容忍的。











## 1.2、Eurake

> Eureka 是 Netflix 出品的用于实现服务注册和发现的工具。 Spring Cloud 集成了 Eureka，并提供了开箱即用的支持。其中， Eureka 又可细分为 Eureka Server 和 Eureka Client。



服务启动后向Eureka注册，Eureka Server会将注册信息向其他Eureka Server进行同步，当服务消费者要调用服务提供者，则向服务注册中心获取服务提供者地址，然后会将服务提供者地址缓存在本地，**第一次请求比较慢**，下次再调用时，则直接从本地缓存中取，完成一次调用。    

服务提供者在启动后，周期性（默认30秒）向Eureka Server发送心跳，以证明当前服务是可用状态。Eureka Server在一定的时间（默认90秒）未收到客户端的心跳，则认为服务宕机，注销该实例。     

当服务注册中心Eureka Server检测到服务提供者因为宕机、网络原因不可用时，**则在服务注册中心将服务置为DOWN状态，并把当前服务提供者状态向订阅者发布，订阅过的服务消费者更新本地缓存**。         




### 1.2.1、Eureka的自我保护机制（针对客户端） 

> 在默认配置中，Eureka Server在默认90s没有得到客户端的心跳，则注销该实例        
>
> 但是往往因为微服务跨进程调用，网络通信往往会面临着各种问题，比如微服务状态正常，但是因为网络问题故障时，Eureka Server注销服务实例则会让大部分微服务不可用，这很危险，因为服务明明没有问题。     



为了解决这个问题，Eureka 有自我保护机制，通过在Eureka Server配置如下参数，可启动保护机制     

```properties
eureka.server.enable-self-preservation=true
```



**它的原理是    如果在15分钟内超过85%的客户端节点都没有正常的心跳，那么Eureka就认为客户端与注册中心出现了网络故障，那么这个节点将进入自我保护模式，当网络故障恢复后，该节点会自动退出自我保护模式。**     

自我保护模式会出现以下几种情况，：      

1、Eureka不再从注册列表中移除因为长时间没收到心跳而应该过期的服务    

2、Eureka仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点上(即保证当前节点依然可用)     

3、当网络稳定时，当前实例新的注册信息会被同步到其它节点中    

**因此， Eureka可以很好的应对因网络故障导致部分节点失去联系的情况，而不会像zookeeper那样使整个注册服务瘫痪。**





### 1.2.2、Eureka保证AP

Eureka看明白了这一点，因此在设计时就优先保证可用性。Eureka各个节点都是平等的，几个节点挂掉不会影响正常节点的工作，剩余的节点依然可以提供注册和查询服务。      

Eureka的客户端在向某个Eureka注册或时如果发现连接失败，则会自动切换至其它节点，只要有一台Eureka还在，就能保证注册服务可用(保证可用性)，只不过查到的信息可能不是最新的(不保证强一致性)。       



### 1.2.3、为什么不能保证强一致性  





# 2、总结



## 2.1、`zookeeper` 的性能不适合注册中心



因为注册中心为了保障数据一致性而放弃了可用性，导致同机房服务之间无法调用，这个是接受不了的。注册中心的可用性比数据强一致性更加重要，所以注册中心应该是偏向 AP，而不是 CP。



























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
