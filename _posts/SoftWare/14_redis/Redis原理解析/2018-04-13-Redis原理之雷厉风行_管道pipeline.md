---
title: Redis原理之雷厉风行_管道
date: 2018-04-13 03:33:00
tags: 
- Redis
category: 
- Redis
description: Redis原理之雷厉风行_管道
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、`pipeline`出现的背景

> 大多数同学一直以来对 `Redis` 管道有一个误解，他们以为这是 `Redis` 服务器提供的一种 特别的技术，有了这种技术就可以加速 `Redis` 的存取效率。但是实际上 `Redis` 管道 (`Pipeline`) 本身并不是 `Redis` 服务器直接提供的技术，**这个技术本质上是由客户端提供的， 跟服务器没有什么直接的关系**



`redis`客户端执行一条命令分4个过程：

```
发送命令－〉命令排队－〉命令执行－〉返回结果
```

这个过程称为**`Round trip time`(简称`RTT`, 往返时间)**，`mget` `mset`有效节约了`RTT`，但大部分命令（如`hgetall`，并没有`mhgetall`）不支持批量操作，需要消耗`N`次`RTT` ，这个时候需要`pipeline`来解决这个问题



# 2、`pipeline`的性能

## 2.1、未使用`pipeline`执行N条命令

![image-20210521150730964](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210521150730964.png)

## 2.2、使用了`pipeline`执行N条命令

![image-20210521150804430](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210521150804430.png)



## 2.3、两者性能对比

> **使用Pipeline执行速度比逐条执行要快，特别是客户端与服务端的网络延迟越大，性能体能越明显**

![image-20210521150837617](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210521150837617.png)

## 2.4、原生批命令(`mset`, `mget`)与`Pipeline`对比

1、**原生批命令是原子性，`pipeline`是非原子性**       

2、**原生批命令一命令多个`key`, 但`pipeline`支持多命令(支持事务)**        

3、**原生批命令是服务端实现，而`pipeline`需要服务端与客户端共同完成**



# 3、管道压力测试

> `Redis` 自带了一个压力测试工具 `redis-benchmark`，使用这个工具就可以进行管道测试。



1、首先我们对一个普通的 `set` 指令进行压测，`QPS 大约 5w/s`。

```shell
\> redis-benchmark -t set -q
 SET: 51975.05 requests per second
```



**2、我们加入管道选项-P 参数，它表示单个管道内并行的请求数量**，看下面 `P=2`，`QPS `达到 了 `9w/s`。

```shell
\> redis-benchmark -t set -P 2 -q 
SET: 91240.88 requests per second

```



3、''再看看`P=3`，`QPS` 达到了 `10w/s`。 

```shell
\> redis-benchmark -t set -P 3 -q 
SET: 102354.15 requests per second
```



**问题1：但如果再继续提升 P 参数，发现 QPS 已经上不去了。这是为什么呢?**      

答案：因为这里`CPU` 处理能力已经达到了瓶颈，`Redis` 的单线程 `CPU` 已经飙到了 `100%`，所 以无法再继续提升了。





# 4、深入理解管道本质

> 管道本质，它并不是服务器的什么特性，而是客户端通过改变了读写的顺序带来的性能的巨大提升。



![image-20210524165213154](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210524165213154.png)



 上图就是一个完整的请求交互流程图。我用文字来仔细描述一遍: 其中步骤 5~8 和 1~4 是一样的，只不过方向是反过来的，一个是请求，一个是响应。        

1、客户端进程调用 `write` 将消息写到操作系统内核为套接字分配的发送缓冲 `send buffer`。

2、客户端操作系统内核将发送缓冲的内容发送到网卡，网卡硬件将数据通过「网际路由」送到服务器的网卡。      

3、服务器操作系统内核将网卡的数据放到内核为套接字分配的接收缓冲 `recv buffer`。        

4、服务器进程调用 `read` 从接收缓冲中取出消息进行处理。           

5、服务器进程调用 `write` 将响应消息写到内核为套接字分配的发送缓冲 `send buffer`。        

6、服务器操作系统内核将发送缓冲的内容发送到网卡，网卡硬件将数据通过「网际路由」送到客户端的网卡。         

7、客户端操作系统内核将网卡的数据放到内核为套接字分配的接收缓冲 `recv buffer`。         

8、客户端进程调用 `read` 从接收缓冲中取出消息返回给上层业务逻辑进行处理。       

9、结束。    



我们开始以为 `write` 操作是要等到对方收到消息才会返回，但实际上不是这样的。`writ`e 操作只负责将数据写到本地操作系统内核的发送缓冲然后就返回了。剩下的事交给操作系统 内核异步将数据送到目标机器。**但是如果发送缓冲满了，那么就需要等待缓冲空出空闲空间 来，这个就是写操作 `IO` 操作的真正耗时**。     

我们开始以为 `read` 操作是从目标机器拉取数据，但实际上不是这样的。`read` 操作只负 责将数据从本地操作系统内核的接收缓冲中取出来就了事了。**但是如果缓冲是空的，那么就 需要等待数据到来，这个就是读操作 `IO` 操作的真正耗时**。        



所以对于 `value = redis.get(key)`这样一个简单的请求来说，`write` 操作几乎没有耗时，直接 写到发送缓冲就返回，而 `read` 就会比较耗时了，因为它要等待消息经过网络路由到目标机器 处理后的响应消息,再回送到当前的内核读缓冲才可以返回。这才是一个网络来回的真正开 销。     

**而对于管道来说，连续的 `write` 操作根本就没有耗时，之后第一个 `read` 操作会等待一个 网络的来回开销，然后所有的响应消息就都已经回送到内核的读缓冲了，后续的 `read` 操作 直接就可以从缓冲拿到结果，瞬间就返回了。**









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
		id: 'znOHsqV9dGMatZ4D',
    });
    gitalk.render('gitalk-container');
</script> 



<!-- Gitalk end -->



