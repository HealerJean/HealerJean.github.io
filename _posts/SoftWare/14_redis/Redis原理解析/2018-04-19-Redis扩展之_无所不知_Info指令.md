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



# 1、`Info`指令

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



## 1.1、`info stats`

### 1.1.1、`Redis` 每秒执行多少次指令

> 这个信息在 `Stats` 块里，可以通过 `info stats` 看到。



```shell
>redis-cli info stats |grep ops 
instantaneous_ops_per_sec:789
```

以上，表示 `ops `是 `789`，也就是所有客户端每秒会发送 `789` 条指令到服务器执行。极 限情况下，`Redis` 可以每秒执行 `10w `次指令，`CPU `几乎完全榨干。    



**问题1：如果发现`qps`过高，怎么看下到底是哪些`key`访问比较高呢?**      

如果 `qps` 过高，可以考虑通过 `monitor` 指令快速观察一下究竟是哪些 `key` 访问比较频繁，从而在相应的业务上进 行优化，以减少 `IO` 次数。`monitor `指令会瞬间吐出来巨量的指令文本，所以一般在执行 `monitor` 后立即 `ctrl+c` 中断输出。



### 1.1.2、超出最大连接数限制而被拒绝的客户端连接次数

> 关于客户端的数量还有个重要的参数需要观察，那就是 `rejected_connections`     
>
> 它表示因为超出最大连接数限制而被拒绝的客户端连接次数，如果这个数字很大，意味着服务器的最大连接数设置的过低需要调整 `maxclients` 参数。 

```shell
> redis-cli info stats |grep reject

rejected_connections:0
```





## 1.2、`info client`

### 1.2.1、`Redis`连接了多少客户端?

> 这个信息在 `Clients` 块里，可以通过 `info clients` 看到。   
>
> 注意：这个信息也是比较有用的，通过观察这个数量可以确定是否存在意料之外的连接。如果 发现这个数量不对劲，接着就可以使用 `client list `指令列出所有的客户端链接地址来确定源头。

```shell
127.0.0.1:6379> info clients
# Clients
connected_clients:2  //当前的连接数
client_longest_output_list:0 //客户端输出缓冲区列表最大对象数
client_biggest_input_buf:0 //客户端输入缓冲区最大值
blocked_clients:0
127.0.0.1:6379> 
```



## 1.3、`info memory`

### 1.3.1、`Redis` 内存占用多大 ?

> 这个信息在 `Memory` 块里，可以通过 `info memory` 看到。  
>
> 说明：如果单个 `Redis` 内存占用过大，并且在业务上没有太多压缩的空间的话，可以考虑集群 化了。

```
> redis-cli info memory | grep used | grep human
used_memory_human:827.46K # 内存分配器 (jemalloc) 从操作系统分配的内存总量 
used_memory_rss_human:3.61M # 操作系统看到的内存占用 ,top 命令看到的内存 
used_memory_peak_human:829.41K # Redis 内存消耗的峰值 
used_memory_lua_human:37.00K # lua 脚本引擎占用的内存大小
```



## 1.4、`info replication`

### 1.4.1、复制积压缓冲区多大?

> 1、复制积压缓冲区大小非常重要，它严重影响到主从复制的效率。当从库因为网络原因临 时断开了主库的复制，然后网络恢复了，又重新连上的时候，这段断开的时间内发生在 `master` 上的修改操作指令都会放在积压缓冲区中，这样从库可以通过积压缓冲区恢复中断的 主从同步过程。      
>
> 2、积压缓冲区是环形的，后来的指令会覆盖掉前面的内容。如果从库断开的时间过长，或者缓冲区的大小设置的太小，都会导致从库无法快速恢复中断的主从同步过程，因为中间的修改指令被覆盖掉了。这时候从库就会进行全量同步模式，非常耗费 `CPU `和网络资源。     
>
> **3、如果有多个从库复制，积压缓冲区是共享的，它不会因为从库过多而线性增长。**



> 这个信息在 `Replication` 块里，可以通过 `info replication` 看到。

```shell
> redis-cli info replication |grep backlog 
repl_backlog_active:0
repl_backlog_size:1048576 # 这个就是积压缓冲区大小 
repl_backlog_first_byte_offset:0
repl_backlog_histlen:0
```



通过查看 `sync_partial_err` 变量的次数来决定是否需要扩大积压缓冲区，它表示主从半同步复制失败的次数。

```shell
\> redis-cli info stats | grep sync sync_full:0
 sync_partial_ok:0
 sync_partial_err:0 # 半同步失败次数
```





# 2、客户端管理和异常分析

| 命令           | 优点                                        | info clients                                                 |
| -------------- | ------------------------------------------- | ------------------------------------------------------------ |
| `client list`  | 能精准分析每个客户端来定位问题              | 执行速度较慢，尤其是在连接数较多的情况下，频繁执行可能阻塞`Redis` |
| `info clients` | 执行速度比`client list`快，分析过程较为简单 | **不能精准定位到客户端，不能显示所有输入缓冲区的总量，只能显示最大量** |



## 2.1、`client list`

说明：这里我同一个`redis`开启了两个客户端

```shell
127.0.0.1:6379> client list
id=3808 addr=127.0.0.1:55369 fd=8 name= age=5050 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=0 events=r cmd=client
id=3810 addr=127.0.0.1:56759 fd=7 name= age=27 idle=26 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=client
```



### 2.1.2、标识

| 解析        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| **标识**    |                                                              |
| `id`        | 客户端连接的唯一标识                                         |
| `addr`      | 客户端链接的ip和标识                                         |
| `name`      | 客户端的名字                                                 |
| `age`       | 客户点已经链接的时间 ，连接`redis`的时间为27秒               |
| `idle`      | 最近一次的空闲时间   `redis`空闲了26秒（当`Redis`连接的时间等于空闲时间的时候，就说明连接一直处于空闲，这种情况就是不正常的） |
| `flags`     | 客户端 `flag`                                                |
| `db`        | 该客户端正在使用的数据库 `ID`                                |
| `sub`       | 已订阅频道的数量                                             |
| `psub`      | 已订阅模式的数量                                             |
| `multi`     | 在事务中被执行的命令数量                                     |
| `qbuf`      | 输入缓冲区已使用总容量                                       |
| `qbuf-free` | 输入缓冲区剩余容量                                           |
| `obl`       | 固定缓冲区的长度 举例，`obl=0`，固定缓冲区长度为0            |
| `oll`       | 动态缓冲区的长度，举例：`oll=4869`，动态缓冲区有4869个对象   |
| `omem`      | 总的使用的字节数，举例：`omem=133081288`，两个部分共使用了133081288字节=126M内存 |
| `events`    | 文件描述符事件                                               |
| `cmd`       | 最近一次执行的命令                                           |





### 2.1.3、输入缓冲区

> `redis`为每个客户端分配了输入缓冲区，它的作用是将客户端发送的命令临时保存，同时`Redis`会从缓冲区拉取命令并执行，输入缓存区为客户端发送命令到`Redis`提供了缓存功能。        
>
> <font color="red">**输入缓冲区会根据输入内容的大小而动态调整，只是要求缓冲区的大小不超过1G，超过后客户端将关闭**</font>



| 解析        | 说明                   |
| ----------- | ---------------------- |
| `qbuf`      | 输入缓冲区已使用总容量 |
| `qbuf-free` | 输入缓冲区剩余容量     |



#### 2.1.3.1、造成输入缓冲区过大的原因

1、`Redis`的处理速度跟不上输入缓冲区的输入速度，并且每次进入缓冲区的命令包含了大量`bigkey`      

2、`Redis`发生了阻塞，短期内不能处理命令，造成客户端输入的命令积压在缓冲区　　　　



#### 2.1.3.2、如何快速发现和监控输入缓冲区过大    

1、通过定期执行·`client list`命令，收集`qbuf`和`qbuf-free`找到异常的连接记录并分析，最终找到可能出问题的客户端         

2、通过`info`命令的`info clients`模块，找到最大的输入缓冲区`client_biggest_input_buf:0`，例如，可以设置`10M`就开始报警   



```shell
127.0.0.1:6379> info clients
# Clients
connected_clients:2
client_longest_output_list:0
client_biggest_input_buf:0
blocked_clients:0
127.0.0.1:6379> 
```







### 2.1.4、输出缓冲区   

> **`redis`为每个客户端分配了输出缓冲区，它的作用是保存命令执行的结果返回给客户端。**    
>
> 实际上输出缓冲区由两部分组成，固定缓冲区和动态缓冲区，其中固定缓冲区返回比较小的执行结果，**而动态缓冲区返回比较大的结果，例如大的字符串，`hgetall`、`smembers` 命令的结果**。       
>
> **固定缓冲区使用的是字节数组，动态缓冲区使用的是列表。当固定缓冲区存满后会将`Redis`新的返回结果存放到动态缓冲区的队列中。队列中每个对象就是每个返回结果**          

```
127.0.0.1:6379> client list
id=3808 addr=127.0.0.1:55369 fd=8 name= age=5050 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=0 events=r cmd=client
id=3810 addr=127.0.0.1:56759 fd=7 name= age=27 idle=26 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=client
```

| 参数   | 说明                                                         |
| ------ | ------------------------------------------------------------ |
| `obl`  | 固定缓冲区的长度 举例，`obl=0`，固定缓冲区长度为0            |
| `oll`  | 动态缓冲区的长度，举例：`oll=4869`，动态缓冲区有4869个对象   |
| `omem` | 总的使用的字节数，举例：`omem=133081288`，两个部分共使用了133081288字节=126M内存 |



#### 2.1.4.1、配置输出缓存区的容量

> **与输入缓冲区不同，它的容量是可以通过参数配置的，。并且按照客户端的不同分为三种，普通客户端，发布订阅客户端，`slave`客户端。**   
>
> > 客户端缓冲区超过`<hard limit>` ，客户端会立即关闭；       
> >
> > 如何客户端缓冲区的输出缓冲区超过了`<soft limit>` 并且持续了 `<soft secontds>`秒，客户端会被立即关闭 




```
默认
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit slave 256mb 64mb 60
client-output-buffer-limit pubsub 8mb 2mb 60
```



#### 2.1.4.2、监控输出缓冲区的方法    

> 1、定期执行`client list` 模块命令，收集`obl`、`oll`、``omem``找到异常的连接记录并分析，最终找到可能出问题的客户端          
>
> 2、通过`info`命令的`info clients`模块，找到输出缓冲区列表的最大对象数`client_longest_output_list`   




```shell
127.0.0.1:6379> info clients
# Clients
connected_clients:2
client_longest_output_list:0
client_biggest_input_buf:0
blocked_clients:0
127.0.0.1:6379> 
```





## 2.2、最大客户端连接数和超时时间

### 2.2.1、查看`maxclients`和`timeout`属性值

```shell
127.0.0.1:6379> config get maxclients
1) "maxclients"
2) "10000" //默认10000


127.0.0.1:6379> config get timeout
1) "timeout" //默认是0
2) "0"
127.0.0.1:6379> 
```



#### 2.2.1.1、限制`maxclients`

> `Redis`提供了`maxclients`参数来限制最大客户端连接数，一旦超过`maxclients`，新的连接将会被拒绝。   
>
> `maxclients`默认值是`10000`，可以通过`info clients`来查看当前的连接数

```
127.0.0.1:6379> info clients
# Clients
connected_clients:2  //当前的连接数
client_longest_output_list:0 //客户端输出缓冲区列表最大对象数
client_biggest_input_buf:0 //客户端输入缓冲区最大值
blocked_clients:0
127.0.0.1:6379> 

```



#### 2.2.1.2、限制`timeout`

> 一般情况下来说`maxclients=10000`在大部分场景已经绝对够用，但是某些情况由于业务方使用不当。    
>
> 例如，没有主动关闭连接，可能存在大量的`idle`空闲连接，无论是从网络连接成本还是超过`maxclients`的后果来说都不是什么好事，因此`Redis`提供了一个`timeou`来限制最大空闲连接，一旦空闲连接超过了`timeout`，连接将会被关闭      
>
> > 可以将`timeout`设置为300秒，同时客户端加上空闲检测和验证等措施



| 参数名字                        | 内容                                                         | 默认值            |
| ------------------------------- | ------------------------------------------------------------ | ----------------- |
| `minEvictableIdleTimeMillis`    | `连接的最小空闲时间，达到这个值后空闲连接将被移除timeBetweenEvictionRunsMillis`大于0时才有意义； | 默认30分钟        |
| `timeBetweenEvictionRunsMillis` | 空闲检测周期（单位毫秒）                                     | `-1`,表示永不检测 |
| `testWhileIdle`                 | 向连接池借用连接时是否做连接空闲检测，空闲超时的连接将会被移除 | `false`           |







## 2.3、客户端命名、杀死、暂停、监控

### 2.3.1、客户端命名命名

> 这样比较容易标识出客户端的来源

```shell
127.0.0.1:6379> client setName clent1
OK
127.0.0.1:6379> client listid=3818 addr=127.0.0.1:62540 fd=7 name=clent1 age=15 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=0 events=r cmd=client127.0.0.1:6379> 
```



### 2.3.2、杀死指定`ip`地址和端口的客户端


```shell
client kill 127.0.0.1:62569
```

```
127.0.0.1:6379> client list
id=3819 addr=127.0.0.1:62555 fd=7 name=client1 age=153 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=0 events=r cmd=clientid=3820 addr=127.0.0.1:62569 fd=8 name= age=102 idle=102 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=0 obl=0 oll=0 omem=0 events=r cmd=client127.0.0.1:6379> client kill 127.0.0.1:62569OK127.0.0.1:6379> 
```



### 2.3.3、客户端暂停(阻塞)

> 使用范围:只对普通发布者和订阅发布者有效，对于主从复制是无效的，生产环境中成本非常高

```
127.0.0.1:6379> client pause 10000……
```



## 2.4、`monitor` 用于监控`Redis`正在执行的命令

> 另一个客户端B输入命令，则客户端A,`monitor`可以监听到正在执行的命令，并且记录了详细的时间戳    
>
> **禁止在生产环境中使用`monitor`命令**"："每个客户端都有自己的输出缓冲区，既然`monitor`能监听到所有的命令，则一旦并发量过大，则`monitor`的输出缓存就会暴涨，瞬间占用大量内存




```
打开两个客户端，一个客户端A输入 monitor
127.0.0.1:6379> monitor

另一个客户端B输入命令，则客户端A可以监听到正在执行的命令，并且记录了详细的时间戳客户端B 
127.0.0.1:6379> keys *
1) "name"
2) "count"
3) "age"
4) "hello"
5) "qq"

127.0.0.1:6379> set m girl
OK
127.0.0.1:6379> 


客户端A
127.0.0.1:6379> monitor
OK
1523876898.815455 [0 127.0.0.1:62628] "keys" "*"1523876908.519187 [0 127.0.0.1:62628] "set" "m" "girl"
```





# 3、客户端案例分析

### 3.1、`Redis`内存陡增

> 服务端现象 ：`Redis`主节点内存陡增，几乎用满`maxmemory`，而从节点没有变化（主从复制，内存使用主从节点基本相同）   
>
> 客户端现象：客户端产生了`OOM`，也就是`Redis`主节点使用的内存已经超过了`maxmemory`，无法写入新的数据



分析过程：

1、通过查看主节点的`dbseze`和从节点的`dbsize`相同，发现确实有大量写入      

2、`info clients` 查看输入缓冲区和输出缓冲区，查看到输出缓冲区数量过大    

```shell
127.0.0.1:6379> info clients
sconnected_clients:2
client_longest_output_list:225698 #发现输出缓冲区的队列已经超过了20万个对象
client_biggest_input_buf:0
blocked_clients:0
127.0.0.1:6379> 
```



3、通过 `client list` 找到不正常的连接，一般来说大部分客户端的`omem`（输出缓存区占用字节为`0`，因为都已经执行完事了，只要找到不为`omem=0`的就代表找到了异常的链接）,通过下面的查找发现最后一次执行的命令是`monito`r，很明显就知道是通过monitor造成的

```shell
>redis-cli client list | grep -v “omem=0”   
id=3822 addr=127.0.0.1:64244 fd=7 name= age=0 idle=0 flags=N db=0 sub=0 psub=0 multi=-1 qbuf=0 qbuf-free=32768 obl=0 oll=0 omem=224551255844 events=r cmd=monitor
```



4、处理方法：只要使用` client kill`就可以杀掉这个连接，但是我们以后如果及时发现并且避免











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



