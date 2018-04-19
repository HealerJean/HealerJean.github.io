---
title: 4、Redis的小功能大用处
date: 2018-04-13 17:33:00
tags: 
- Redis
category: 
- Redis
description: Redis的小功能大用处
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

## 1、慢查询分析

所谓的慢查询日志，就是在命令的执行前后计算每条命令的执行时间，当超过预设值，就将这条命令的相关信息记录下来，

redis命令执行分为4个步奏，发送命令，命令排队，执行命令，返回结果，这里的慢查询分析值统计执行命令的时间。

![WX20180413-102037@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180413-102037@2x.png)


### 1、慢查询的2个配置参数

#### 1、首先我们看了上面的介绍需要明白两件事

1、预设阀值如何设置<br/>

慢查询的预设阀值  slowlog-log-slower-than 
slowlog-log-slower-than参数就是预设阀值，单位是微秒,默认值是1000，也就是10毫秒，如果一条命令的执行时间超过10000微妙，那么它将被记录在慢查询日志中。
如果slowlog-log-slower-than的值是0，则会记录所有命令。
如果slowlog-log-slower-than的值小于0，则任何命令都不会记录日志。<br/>

2、慢查询的记录在哪里<br/>

慢查询日志的长度slowlog-max-len
slowlog-max-len只是说明了慢查询日志最多存储多少，从字面意思看这个只是说明日志最多存储多少条几率，并没有说存到哪里，实际上redis使用了一个列表来存储慢查询日志，这个值就是列表的最大长度，当一个新的命令满足慢查询条件的时候就会被插入到这个列表中，当慢查询日志已经到达列表的最大长度时，又有慢查询日志要进入列表，则最早插入列表的日志将会被移出列表，新日志被插入列表的末尾。比如设置了5，当放入第6条的时候，第一条就会出列<br/>

#### 2、修改配置的方法

1、通过配置文件修改
2。使用命令动态修改

```
config set slowlog-log-slower-than 2000
config set slowlog-max-len 1000

//将配置持久化到本地配置文件，
config rewrite

```

#### 3、查看慢查询日志


虽然慢查询日志在Redis内存列表中，但是Redis并没有暴露这个列表的主键，而是通过一组命令来访问和管理的

1、获取慢查询日志

```
slowlog get n(n可以指定条数)  分别是查询日志的标识id，发送时间戳，命令耗时，执行命令，以及参数

127.0.0.1:6379> config set slowlog-log-slower-than 0
OK
127.0.0.1:6379> set name healerjean
OK
127.0.0.1:6379> slowlog get
1) 1) (integer) 1
   2) (integer) 1523587843
   3) (integer) 5
   4) 1) "set"
      2) "name"
      3) "healerjean"
   5) "127.0.0.1:49894"
   6) ""
2) 1) (integer) 0
   2) (integer) 1523587826
   3) (integer) 5104
   4) 1) "config"
      2) "set"
      3) "slowlog-log-slower-than"
      4) "0"
   5) "127.0.0.1:49894"
   6) ""
127.0.0.1:6379> 


```


2、获取慢查询的列表当前的长度

```
当前有3条慢查询记录

127.0.0.1:6379> slowlog len
(integer) 3
127.0.0.1:6379> 

```

3、慢查询日志重置，实际上就是对列表进行清理

```
127.0.0.1:6379> slowlog reset
OK
```

### 2、最佳实战慢查询
慢查询功能能够帮我们找到Redis可能存在的瓶颈，但是在实际中主要建议下面得几点

1、slowlog-max-len配置建议<br/>
线上建议调大慢查询列表，记录慢查询的时候Reids会对长命令做拦截判断，并不会占用大量内存，增大查询列表可以减缓慢查询的被剔除的可能（因为超过长度就会将老的删除，我们后来就看不到了），例如线上可以设置1000以上<br/>


2、showlog-log-slower-than 建议配置，默认超过10毫秒判断为慢查询，其实是需要根据Redis并发量调整这个值，对于高流量的场景，建议设置为1毫秒<br/>


3、因为redis有排队机制，慢查询也可能造成其他命令阻塞，因此客户端出现命令超时的时候，需要检测该时间带你是是因为慢查询导致的阻塞<br/>

4、这里牛逼了，慢查询是一种先进先出的队列，也就是说当慢查询比较多的时候回丢失部分慢查询的。为了防止这种现象发生，可以将它持久化到其他存储中（mysql），然后使用可视化工具进行查看，



## 2、Redis Shell

### 1、redis-server

我们知道它是用来启动redis的，但是他还有一个--test-memory选项，用来检测当前操作系统能否稳定分配给指定容量的的内存给Redis，有效避免因为内存问题造成的崩溃




```
时间比较长，需要耐心等待
healerjean$ redis-server --test-memory 1024

Addressing test [1]

Please keep the test running several minutes per GB of memory.
Also check http://www.memtest86.com/ and http://pyropus.ca/software/memtester/

Your memory passed this test.
Please if you are still in doubt use the following two tools:
1) memtest86: http://www.memtest86.com/
2) memtester: http://pyropus.ca/software/memtester/
JeandeMBP:bin healerjean$ 
```

一般情况下，不需要每次开启Redis，都执行这个选项，这个功能更偏向于调节测试。


### 3、redis-benchmark

这个可以为Redis做基准性能测试，它提供了很多选项帮助开发和运行人员测试Redis的性能

#### 1、-c (client) 代码客户端的并发数量，默认是50

#### 2、-n <requests>   代表客户端请求的总量 默认是1000000 
-n(num) 


```
healerjean$ redis-benchmark -c 100 -n 20000

代表100个客户同时请求redis，一共执行20000次
redis-benchmark会对各类数据结构的命令进行测试，并给出指标

比如下面的get，一共执行了20000次，在0.19秒完成，每个数据请求是3个字节，98.58%小于2毫秒


====== GET ======
  20000 requests completed in 0.19 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

98.58% <= 1 milliseconds
99.82% <= 2 milliseconds
100.00% <= 2 milliseconds
104712.04 requests per second


healerjean$ redis-benchmark -c 100 -n 20000
====== PING_INLINE ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

87.22% <= 1 milliseconds
99.93% <= 2 milliseconds
100.00% <= 2 milliseconds
101522.84 requests per second

====== PING_BULK ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

74.24% <= 1 milliseconds
100.00% <= 2 milliseconds
100.00% <= 2 milliseconds
98522.17 requests per second

====== SET ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

91.82% <= 1 milliseconds
99.36% <= 2 milliseconds
99.85% <= 3 milliseconds
100.00% <= 3 milliseconds
99502.48 requests per second

====== GET ======
  20000 requests completed in 0.19 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

98.58% <= 1 milliseconds
99.82% <= 2 milliseconds
100.00% <= 2 milliseconds
104712.04 requests per second

====== INCR ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

89.14% <= 1 milliseconds
99.82% <= 2 milliseconds
100.00% <= 2 milliseconds
100000.00 requests per second

====== LPUSH ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

83.86% <= 1 milliseconds
99.93% <= 2 milliseconds
100.00% <= 2 milliseconds
101522.84 requests per second

====== RPUSH ======
  20000 requests completed in 0.19 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

92.49% <= 1 milliseconds
99.79% <= 2 milliseconds
100.00% <= 2 milliseconds
103092.78 requests per second

====== LPOP ======
  20000 requests completed in 0.22 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

87.38% <= 1 milliseconds
99.97% <= 2 milliseconds
100.00% <= 2 milliseconds
92592.59 requests per second

====== RPOP ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

94.17% <= 1 milliseconds
99.57% <= 2 milliseconds
100.00% <= 2 milliseconds
102040.82 requests per second

====== SADD ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

96.68% <= 1 milliseconds
99.47% <= 2 milliseconds
99.50% <= 5 milliseconds
99.54% <= 6 milliseconds
99.60% <= 7 milliseconds
99.83% <= 8 milliseconds
100.00% <= 8 milliseconds
101010.10 requests per second

====== HSET ======
  20000 requests completed in 0.19 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

71.61% <= 1 milliseconds
100.00% <= 1 milliseconds
104166.66 requests per second

====== SPOP ======
  20000 requests completed in 0.20 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

88.32% <= 1 milliseconds
100.00% <= 2 milliseconds
100.00% <= 2 milliseconds
102040.82 requests per second

====== LPUSH (needed to benchmark LRANGE) ======
  20000 requests completed in 0.19 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

91.89% <= 1 milliseconds
99.57% <= 2 milliseconds
100.00% <= 2 milliseconds
105263.16 requests per second

====== LRANGE_100 (first 100 elements) ======
  20000 requests completed in 0.74 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

0.01% <= 1 milliseconds
70.57% <= 2 milliseconds
99.56% <= 3 milliseconds
99.89% <= 4 milliseconds
100.00% <= 4 milliseconds
26954.18 requests per second

====== LRANGE_300 (first 300 elements) ======
  20000 requests completed in 1.97 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

0.00% <= 1 milliseconds
0.06% <= 2 milliseconds
2.81% <= 3 milliseconds
23.94% <= 4 milliseconds
54.51% <= 5 milliseconds
84.29% <= 6 milliseconds
96.56% <= 7 milliseconds
98.76% <= 8 milliseconds
99.46% <= 9 milliseconds
99.77% <= 10 milliseconds
99.86% <= 11 milliseconds
99.96% <= 12 milliseconds
100.00% <= 12 milliseconds
10162.60 requests per second

====== LRANGE_500 (first 450 elements) ======
  20000 requests completed in 2.92 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

0.02% <= 1 milliseconds
0.06% <= 2 milliseconds
0.12% <= 3 milliseconds
0.38% <= 4 milliseconds
9.48% <= 5 milliseconds
28.08% <= 6 milliseconds
47.22% <= 7 milliseconds
65.74% <= 8 milliseconds
81.12% <= 9 milliseconds
91.18% <= 10 milliseconds
97.42% <= 11 milliseconds
99.47% <= 12 milliseconds
99.75% <= 13 milliseconds
99.88% <= 14 milliseconds
99.97% <= 15 milliseconds
100.00% <= 15 milliseconds
6837.61 requests per second

====== LRANGE_600 (first 600 elements) ======
  20000 requests completed in 3.61 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

0.01% <= 1 milliseconds
0.06% <= 2 milliseconds
0.14% <= 3 milliseconds
0.75% <= 4 milliseconds
7.08% <= 5 milliseconds
18.12% <= 6 milliseconds
29.30% <= 7 milliseconds
40.19% <= 8 milliseconds
51.35% <= 9 milliseconds
62.12% <= 10 milliseconds
73.46% <= 11 milliseconds
85.25% <= 12 milliseconds
92.68% <= 13 milliseconds
96.00% <= 14 milliseconds
98.18% <= 15 milliseconds
99.06% <= 16 milliseconds
99.35% <= 17 milliseconds
99.40% <= 18 milliseconds
99.45% <= 19 milliseconds
99.48% <= 20 milliseconds
99.50% <= 22 milliseconds
99.50% <= 42 milliseconds
99.51% <= 43 milliseconds
99.52% <= 46 milliseconds
99.54% <= 47 milliseconds
99.56% <= 48 milliseconds
99.59% <= 49 milliseconds
99.62% <= 50 milliseconds
99.64% <= 51 milliseconds
99.68% <= 52 milliseconds
99.71% <= 53 milliseconds
99.74% <= 54 milliseconds
99.76% <= 55 milliseconds
99.79% <= 56 milliseconds
99.82% <= 57 milliseconds
99.84% <= 58 milliseconds
99.89% <= 59 milliseconds
99.95% <= 60 milliseconds
99.99% <= 61 milliseconds
100.00% <= 61 milliseconds
5534.03 requests per second

====== MSET (10 keys) ======
  20000 requests completed in 0.40 seconds
  100 parallel clients
  3 bytes payload
  keep alive: 1

0.19% <= 1 milliseconds
67.25% <= 2 milliseconds
98.31% <= 3 milliseconds
99.99% <= 4 milliseconds
100.00% <= 4 milliseconds
49627.79 requests per second


JeandeMBP:bin healerjean$ 

```
 
#### 3、 -q 仅显示上面出现结果的request per second的信息

表示每秒请求多少次

```
healerjean$ redis-benchmark -c 100 -n 20000 -q
PING_INLINE: 107526.88 requests per second
PING_BULK: 98039.22 requests per second
SET: 101522.84 requests per second
GET: 104712.04 requests per second
INCR: 103092.78 requests per second
LPUSH: 102564.11 requests per second
RPUSH: 108108.11 requests per second
LPOP: 103092.78 requests per second
RPOP: 103092.78 requests per second
SADD: 108108.11 requests per second
HSET: 103626.94 requests per second
SPOP: 99502.48 requests per second
LPUSH (needed to benchmark LRANGE): 109289.62 requests per second
LRANGE_100 (first 100 elements): 27137.04 requests per second
LRANGE_300 (first 300 elements): 11210.76 requests per second
LRANGE_500 (first 450 elements): 7830.85 requests per second
LRANGE_600 (first 600 elements): 5927.68 requests per second
MSET (10 keys): 56022.41 requests per second

JeandeMBP:bin healerjean$ 
```

#### 4、-t 对指定命令进行基准测试


```
healerjean$ redis-benchmark -t set,get -q 
SET: 109170.30 requests per second
GET: 112612.61 requests per second

JeandeMBP:bin healerjean$ 
```

#### 5、--csv 将结果按照csv格式输出，便于后续处理，如导出到excel中


```
JeandeMBP:bin healerjean$ redis-benchmark -t set,get --csv
"SET","105042.02"
"GET","105263.16"
JeandeMBP:bin healerjean$ 

```



## 3、Pipeline 不是原子性的

### 1、Pipeline 出现

Redis客户端执行一条没拿过来分为如下4个过程，发送命令，命令排队，执行命令，返回结果，其中1+4成为Round Trip Time (RTT,往返时间) 

1、Redis批量操作命令有效节省时间，这种批量命令相当于是原子性，比较节约RTT，但是大部分命令是不支持批量操作的，如果客户端和服务端在一台机器上还好，假如服务器在北京客户端在上海，那就有点麻烦了，传播速度非常慢， 
![WX20180413-122841@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180413-122841@2x.png)


2、为了解决这一的问题，PipeLine流水线机制就出现了，它能将一组RTT传输给Redis，再将这组命令的结果按照顺序返回给客户端，但是它不是原子性的


### 2、性能测试

在不同网络环境下，pipelin执行速度一般比逐条执行要快，客户端和服务端的网络延迟越大，Pipeline越明显


|网络|延迟|非Pipeline|Pipeline|
|---|---|---|---|
|本机|0.17ms|573ms|134ms|
|内网服务器|0.41ms|1610ms|240ms|
|异地机房|7ms|78499ms|1104ms|



虽然Pipeline好用，但是每次组装还需要节制，否则一次组装的数据量过大，一方面会增加客户端的等待时间，另一方面会造成一定的网络阻塞。

## 4、事物与Lua

即使有多个命令，就会遇到事物的问题。multi和exec这两个命令，一个代表事物开始，一个代表事物结束 
<font color ="red">redis是不支持回滚的特性，只要语法没有问题，中间即使有的命令报错，其他的命令也一定会执行 </font>
除非

举例：社交网站上用户A关注了用户B，那么需要再用户A的关注表中添加用户B，用户B的表中添加粉丝A 这是两个操作，只能同时失败或者成功


```
客户端1
127.0.0.1:6379> multi
OK
127.0.0.1:6379> sadd user:A:flow user:B
QUEUED
127.0.0.1:6379> sadd user:B:fans user:A
QUEUED

客户端2
这个时候，如果其他客户端执行查找命令，则会提示在队列中，不能查找成功
127.0.0.1:6379> sismember user:A:flow user:B
QUEUED
127.0.0.1:6379> smembers user:A:flow 
QUEUED
127.0.0.1:6379> 

客户端1
只有执行exec 上面两条命令才能执行成功
127.0.0.1:6379> exec
1) (integer) 1
2) (integer) 1
127.0.0.1:6379> 



```

### 1、解决事物中出现的错误


#### 1、命令错误 
假如说事物中间命令出现语法错误，事物exec也不能正常执行

```
127.0.0.1:6379> smembers user:A:flow user:B
(error) ERR wrong number of arguments for 'smembers' command
127.0.0.1:6379> smembers user:A:flow 
QUEUED
127.0.0.1:6379> exec
(error) EXECABORT Transaction discarded because of previous errors.
127.0.0.1:6379> MULTI
```


#### 2、运行时错误

就是说由于我们程序员自己的问题导致的，这样即使有事物，也不会回滚的，也都会执行，因为没有语法错误
针对这个问题：有些应用场景需要再执行事物之前，需要确保事物中的key没有被其他客户端修改过，才执行事物，否则不执行，Redis提供了watch命令来解决这个问题



```
客户端1
127.0.0.1:6379> set key "healerjean"
OK
127.0.0.1:6379> watch key
OK
127.0.0.1:6379> multi

客户端2 
127.0.0.1:6379> set key "zhangyj"
OK

客户端1 （下面这个在提交的时候已经被修改了，所以执行事物的记过是nil）

127.0.0.1:6379> append key is-good-man
QUEUED
127.0.0.1:6379> exec
(nil)

```

## 5、发布订阅


Redis提供了基于发布/订阅的消息机制，此种模式下，消息发布者和订阅中不能相互直接通信，发布者客户端向指定的频道（channel）发布消息，订阅该频道的每个客户端都可以收到该消息

![WX20180413-154821@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180413-154821@2x.png)



### 1、发布消息
publish channel message 


```

发布客户端

下面在频道channel:student 发布了一条消息，返回值为订阅者个数，此时没有订阅者，返回为0

127.0.0.1:6379> publish channel:student "teacher coming"
(integer) 0
127.0.0.1:6379> 
```

### 2、订阅消息

subscribe channel
 
```
订阅客户端
subscribe channel:student
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "channel:student"
3) (integer) 1
…… 这里在等待接收下次

这个时候，发布客户端发布一条消息，

127.0.0.1:6379> publish channel:student "gime start"
(integer) 1
127.0.0.1:6379> 



订阅客户端如下

127.0.0.1:6379> subscribe channel:student
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "channel:student"
3) (integer) 1
1) "message"
2) "channel:student"
3) "gime start"


```
注意事项
1、当订阅的通道的时候，会进入订阅状态，一直等待消息接收，只能接收命令为subscribe ,psubscribe ,unsubscribe,punsubscribe
<br/>
2、新开启的订阅，无法接收以前的消息，因为redis不会对之前的消息进行持久化


### 3、取消订阅


```
unsubscribe channel:student
```

### 4、按照匹配模式订阅和曲线订阅


```

punsubscribe hello*
psubscribe hello*

```

### 5、查询订阅
#### 1、查看活跃的频道

所谓活跃的频道是指至少有一个频道被订阅，如果没有的被定义则返回0
pubsub  channels [partten] 
 
```

6379> pubsub channels
1) "channel:student"
127.0.0.1:6379> 


没有客户端订阅频道
127.0.0.1:6379> pubsub channels
(empty list or set)
```


#### 2、查看频道订阅数


```
127.0.0.1:6379> pubsub numsub channel:student
1) "channel:student"
2) (integer) 1
127.0.0.1:6379> 


```

#### 查看模式的订阅数 


```
127.0.0.1:6379>pubsub numpat 
（integer） 1

```

### 6、使用场景

聊天教、公告牌

## 6、GEO （地理信息定位）
支持存储地理位置信息来实现比如地理位置，摇一摇等依赖于地理位置信息的孤男寡女，对于实现这些功能的开发者来说绝对是一个福音

### 1、增加地理位置信息

添加背景的地理位置信息
geoadd key 经度 纬度 城市


```
返回结果表示成功的个数，如果已经存在则返回0 ，
127.0.0.1:6379> geoadd cities:location 116.28 39.55 beijing
(integer) 1
127.0.0.1:6379> geoadd cities:location 116.28 39.550 beijing
(integer) 0
127.0.0.1:6379> 


同时添加多个地理位置信息 

127.0.0.1:6379> geoadd cities:location 116.28 39 beijing  11.78 44.5 shanghai
(integer) 1
127.0.0.1:6379> 

```

### 2、获取地理位置信息


```
127.0.0.1:6379> geopos cities:location beijing
1) 1) "116.28000229597091675"
   2) "38.99999918434559731"
127.0.0.1:6379> 

```


### 3、获取两个地理位置之间的距离

geodist key city1 city2 [unit] m米 km千米 mi英里 ft 尺

```
127.0.0.1:6379> geodist cities:location beijing shanghai
"8053178.4504"
127.0.0.1:6379> 
```


### 4、获取指定位置范围内的地理信息位置集合

这个有两个命令，一个给出成员goeradiusbymembe，一个给出经纬度georadius

```

georadiusbymember cities:location beijing 60000km

```


### 5、删除地理信息 

这里需要知道的是这里存放的数据类型为zset


```
127.0.0.1:6379> type cities:location
zset
127.0.0.1:6379> 
```

```
删除
zrem  cities:location beijing

```


<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean123.github.io`,
		owner: 'HealerJean123',
		admin: ['HealerJean123'],
		id: 'xkIOzhZZ6cOnwpmo',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

