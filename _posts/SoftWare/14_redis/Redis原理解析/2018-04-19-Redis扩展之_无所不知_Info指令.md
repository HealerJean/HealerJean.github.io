---
title: Redis扩展之_无所不知_Info指令
date: 2018-04-19 03:33:00
tags: 
- Redis
category: 
- Redis
description: Redis扩展之_无所不知_Info指令
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、Info指令

>  在使用 `Redis` 时，时常会遇到很多问题需要诊断，在诊断之前需要了解 `Redis` 的运行状 态，通过强大的 `Info` 指令，你可以清晰地知道 `Redis` 内部一系列运行参数。    
>
> `Info` 指令显示的信息非常繁多，分为 `9` 大块，每个块都有非常多的参数，这 `9` 个块分 别是:



| 指令          | 说明                   |
| ------------- | ---------------------- |
| `Server`      | 服务器运行的环境参数   |
| `Clients`     | 客户端相关信息         |
| `Memory`      | 服务器运行内存统计数据 |
| `Persistence` | 持久化信息             |
| `Stats`       | 通用统计数据           |
| `Replication` | 主从复制相关信息       |
| `CPU`         | `CPU`使用情况          |
| `Cluster`     | 集群信息               |
| `KeySpace`    | 键值对统计数量信息     |



# 2、`info stats`

## 2.1、`Redis` 每秒执行多少次指令

> 这个信息在 `Stats` 块里，可以通过 `info stats` 看到。



```shell
>redis-cli info stats |grep ops 
instantaneous_ops_per_sec:789
```

以上，表示 `ops `是 789，也就是所有客户端每秒会发送 `789` 条指令到服务器执行。极 限情况下，`Redis` 可以每秒执行 `10w `次指令，`CPU `几乎完全榨干。    



**问题1：如果发现`qps`过高，怎么看下到底是哪些`key`访问比较高呢?**      

如果 `qps` 过高，可以考 虑通过 `monitor` 指令快速观察一下究竟是哪些 `key` 访问比较频繁，从而在相应的业务上进 行优化，以减少 `IO` 次数。`monitor `指令会瞬间吐出来巨量的指令文本，所以一般在执行 `monitor` 后立即 `ctrl+c` 中断输出。



# 2、`info client`

## 2.1、`Redis`连接了多少客户端?

> 这个信息在 `Clients` 块里，可以通过 `info clients` 看到。

```shell
\> redis-cli info clients
 connected_clients:124 # 这个就是正在连接的客户端数量 
 client_longest_output_list:0
 client_biggest_input_buf:0
 blocked_clients:0
```



这个信息也是比较有用的，通过观察这个数量可以确定是否存在意料之外的连接。如果 发现这个数量不对劲，接着就可以使用 `client list `指令列出所有的客户端链接地址来确定源头。

关于客户端的数量还有个重要的参数需要观察，那就是 rejected_connections，它表示因

为超出最大连接数限制而被拒绝的客户端连接次数，如果这个数字很大，意味着服务器的最

大连接数设置的过低需要调整 maxclients 参数。 > redis-cli info stats |grep reject

rejected_connections:0















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
		id: '5igtsPoSQzR4MqDp',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



