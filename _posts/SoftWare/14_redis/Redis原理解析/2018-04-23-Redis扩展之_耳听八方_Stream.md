---
title: Redis扩展之_耳听八方_Stream
date: 2018-04-23 03:33:00
tags: 
- Redis
category: 
- Redis
description: Redis扩展之_耳听八方_Stream
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`Stream`

> 而 `Redis5.0` 最大的 新特性就是多出了一个数据结构 `Stream`，它是一个新的强大的支持多播的可持久化的消息队 列，作者坦言 `Redis` `Stream` 狠狠地借鉴了 `Kafka` 的设计。     

`Redis Stream` 的结构如图所示，它有一个消息链表，将所有加入的消息都串起来，每 个消息都有一个唯一的 `ID` 和对应的内容。消息是持久化的，`Redis` 重启后，内容还在。     

**1、每个`Stream` 都有唯一的名称，它就是 `Redis` 的 `key`**，在我们首次使用 `xadd` 指令追加消息时自动创建。             

2、每个 `Stream` 都可以挂多个消费组，每个消费组会有个游标 `last_delivered_id` 在 `Stream` 数组之上往前移动，表示当前消费组已经消费到哪条消息了。**每个消费组都有一个 `Stream` 内唯一的名称，消费组不会自动创建，它需要单独的指令 `xgroup` `create` 进行创建，需要指定 从 `Stream` 的某个消息 `ID` 开始消费，这个 `ID` 用来初始化 `last_delivered_id` 变量**。      

**3、每个消费组 (`Consumer` `Group`) 的状态都是独立的，相互不受影响。也就是说同一份 `Stream` 内部的消息会被每个消费组都消费到**。      

**4、同一个消费组 (`Consumer` `Group)` 可以挂接多个消费者 (`Consumer`)，这些消费者之间是竞争关系，任意一个消费者读取了消息都会使游标 `last_delivered_id` 往前移动。每个消费者有 一个组内唯一名称。**      

5、**消费者 (`Consumer`) 内部会有个状态变量 `pending_ids`，它记录了当前已经被客户端读取的消息，但是还没有 `ack`。**如果客户端没有 `ack`，**这个变量里面的消息`ID` 会越来越多**，一 旦某个消息被 `ack`，它就开始减少。这个 `pending_ids` 变量在 `Redis` 官方被称之为 `PEL`，也 就是 `Pending Entries List`，这是一个很核心的数据结构，它用来确保客户端至少消费了消息一 次，而不会在网络传输的中途丢失了没处理。



![image-20210610160203723](/Users/healerjean/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20210610160203723.png)

# 2、消息

## 2.1、消息`ID`     

> 1、消息 `ID` 的形式是 `timestampInMillis`-`sequence`，例如 `1527846880572`-`5`，它表示当前的消 息在毫米时间戳 `1527846880572 `时产生，并且是该毫秒内产生的第 `5` 条消息。      
>
> 2、消息 `ID` 可以 由服务器自动生成，也可以由客户端自己指定，但是形式必须是整数-整数，而且必须是后面加入的消息的 `ID` 要大于前面的消息 `ID`。



## 2.2、消息内容

> 消息内容就是键值对，形如 `hash` 结构的键值对，这没什么特别之处。



# 3、增删改查

## 3.1、`xadd` 追加消息

> `*` 号表示服务器自动生成 `ID`，后面顺序跟着一堆 `key`/`value` # 名字叫 `laoqian`，年龄 `30` 岁

```shell
127.0.0.1:6379> xadd codehole * name laoqian age 30 
1527849609889-0 # 生成的消息 ID
127.0.0.1:6379> xadd codehole * name xiaoyu age 29 
1527849629172-0
127.0.0.1:6379> xadd codehole * name xiaoqian age 1 
1527849637634-0
```

## 3.2、`xlen` 消息长度

```shell
127.0.0.1:6379> xlen codehole 
(integer) 3
```



## 3.3、`xrange` 获取消息列表

```shell
# 1、 -表示最小值, +表示最大值
127.0.0.1:6379> xrange codehole - +
1) 1) 1527849609889-0
   2) 1) "name"
      2) "laoqian"
      3) "age"
      4) "30"
2) 1) 1527849629172-0
   2) 1) "name"
      2) "xiaoyu"
      3) "age"
      4) "29"
3) 1) 1527849637634-0
   2) 1) "name"
      2) "xiaoqian"
      3) "age"
      4) "1"
      
      
# 2、指定最小消息ID的列表      
127.0.0.1:6379> xrange codehole 1527849629172-0 + 
1) 1) 1527849629172-0
   2) 1) "name"
      2) "xiaoyu"
      3) "age"
      4) "29"
2) 1) 1527849637634-0
   2) 1) "name"
      2) "xiaoqian"
      3) "age"
      4) "1"
      
      
# 3、指定最大消息ID的列表      
127.0.0.1:6379> xrange codehole - 1527849629172-0  
1) 1) 1527849609889-0
   2) 1) "name"
      2) "laoqian"
      3) "age"
      4) "30"
2) 1) 1527849629172-0
   2) 1) "name"
      2) "xiaoyu"
      3) "age"
      4) "29"
```



## 3.4、`xdel` 删除消息

> 这里的删除仅仅是设置了标志位，不影响消息总长度

```shell
127.0.0.1:6379> xdel codehole 1527849609889-0
(integer) 1

127.0.0.1:6379> xlen codehole  # 长度不受影响
(integer) 3

127.0.0.1:6379> xrange codehole - +  # 被删除的消息没了
1) 1) 1527849629172-0
   2) 1) "name"
      2) "xiaoyu"
      3) "age"
      4) "29"
2) 1) 1527849637634-0
   2) 1) "name"
      2) "xiaoqian"
      3) "age"

```



## 3.5、`del `删除 Stream

```shell
127.0.0.1:6379> del codehole  # 删除整个Stream
(integer) 1
```



# 4、独立消费

> **不定义消费者，也可以消费，就好比是一个普通的消息队列 (`list`)**
>
> > 我们可以在不定义消费组的情况下进行 `Stream` 消息的独立消费    
> >
> > 1、当 `Stream` 没有新消息时，甚至可以阻塞等待。    
> >
> > 2、`Redis` 设计了一个单独的消费指令 `xread`，可以将 `Stream` 当成普 通的消息队列 (`list`) 来使用。使用 `xread` 时，我们可以完全忽略消费组 (`Consumer Group`) 的存在，就好比 `Stream` 就是一个普通的列表 (`list`)。

















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
		id: '61TkWS5QK0sFormU',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



