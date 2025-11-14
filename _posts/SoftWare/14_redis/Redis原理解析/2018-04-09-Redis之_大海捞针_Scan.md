---
title: Redis之_大海捞针_Scan
date: 2018-04-09 03:33:00
tags: 
- Redis
category: 
- Redis
description: Redis之_大海捞针_Scan
---



**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          







# 一、命令：`keys`

> `redis`提供了两个命令遍历所有的键分别是`keys` `scan`

问题1：在平时线上 `Redis` 维护工作中，有时候需要从 `Redis` 实例成千上万的 `key` 中找出特定 前缀的 `key` 列表来手动处理数据，可能是修改它的值，也可能是删除 `key`。这里就有一个问 题，如何从海量的 `key` 中找出满足特定前缀的 `key` 列表来?         

答案：`Redis` 提供了一个简单暴力的指令 `keys` 用来列出所有满足特定正则字符串规则的 key。       

## 1、命令使用

### 1）全键遍历

```shell

127.0.0.1:6379> set codehole1 a 
OK
127.0.0.1:6379> set codehole2 b 
OK
127.0.0.1:6379> set codehole3 c 
OK
127.0.0.1:6379> set code1hole a 
OK
127.0.0.1:6379> set code2hole b 
OK
127.0.0.1:6379> set code3hole b 
OK
127.0.0.1:6379> keys * 
1) "codehole1"
2) "code3hole"
3) "codehole3"
4) "code2hole"
5) "codehole2"
6) "code1hole"
```



### 2）匹配遍历

| 命令 | 说明                                                         | 举例     |
| ---- | ------------------------------------------------------------ | -------- |
| ` *` | 匹配任意字符                                                 | `keys *` |
| `?`  | 匹配一个字符                                                 |          |
| `[]` | 匹配部分字符 [1,3] 匹配1或者3 、   [1-10] 匹配1到10之间的任意数字 |          |

```shell
127.0.0.1:6379> keys codehole* 
1) "codehole1"
2) "codehole3"
3) "codehole2"
127.0.0.1:6379> keys code*hole 
1) "code3hole"
2) "code2hole"
3) "code1hole"
```



### 3）删除匹配的`key`

> 再强调`redis`是单线程的，如果`redis`中存在了大量的键就不太美妙了，可能会造成`redis`阻塞，所以一般不建议在生产环境使用`keys`命令，但是假如有时候不得不使用怎么办 ，使用`scan`命令


```shell
#删除vedio开头的键
127.0.0.1:6379> keys vedio* | xargs redis-cli del
```



## 2、问题总结

#### **a：`keys`命令有什么缺点**     

**1、没有 `offset`、`limit` 参数**，一次性吐出所有满足条件的 `key`，万一实例中有几百 w 个`key` 满足条件，当你看到满屏的字符串刷的没有尽头时，你就知道难受了。        

**2、`keys` 算法是遍历算法，复杂度是 `O(n)`**，如果实例中有千万级以上的 `key`，这个指令就会导致 `Redis` 服务卡顿，所有读写 `Redis` 的其它的指令都会被延后甚至会超时报错，因为`Redis` 是单线程程序，顺序执行所有指令，其它指令必须等到当前的 `keys` 指令执行完了才可以继续。        



#### **b：怎么解决这两个显著的缺点**        

答案：使用`scan`命令





# 二、命令：`scan`

## 1、`scan `特点

> 1、复杂度虽然也是 `O(n)`，但是它是通过游标分步进行的，不会阻塞线程;      
>
> 2、提供 `limit` 参数，可以控制每次返回结果的最大条数，`limit` 只是一个 `hint`，返回的 结果可多可少;           
>
> 3、同 `keys` 一样，它也提供模式匹配功能;          
>
> 4、服务器不需要为游标保存状态，游标的唯一状态就是 `scan` 返回给客户端的游标整数;         
>
> 5、**返回的结果可能会有重复，需要客户端去重复，这点非常重要**;         
>
> 6、**遍历的过程中如果有数据修改，改动后的数据能不能遍历到是不确定的;**         
>
> 7、**单次返回的结果是空的并不意味着遍历结束，而要看返回的游标值是否为零**;



## 2.2、`scan` 基础使用  

> `scan` 参数提供了三个参数 如下   
>
> >  ◯ 第一个是 `cursor` 整数值    
> >
> > ◯  第二个是 `key` 的正则模式，    
> >
> > ◯  **第三个是遍历的 `limit` `hint`，这个 `limit` 不是限定返回结果的数量，而是限定服务器单次遍历的字典槽位数量(约等于)。默认是 `10`**      
>
> **使用方式：第一次遍历时，`cursor` 值为 0，然后将返回结果中第一个整数值作为下一次遍历的 `cursor`。一直遍历到返回的 `cursor` 值为 0 时结束。**



### 2.2.1、实例分析

#### 2.2.1.1、数据准备

> 往 `Redis` 里插入 `10000` 条数据来进行测试

```java
import redis
client = redis.StrictRedis() for i in range(10000):
client.set("key%d" % i, i)
```

#### 2.2.1.2、实例测试

> 好，`Redis` 中现在有了 `10000` 条数据，接下来我们找出以 `key99` 开头 `key` 列表。

```shell
127.0.0.1:6379> scan 0 match key99* count 1000
1) "13976"
2)  1) "key9911"
    2) "key9974"
    3) "key9994"
    4) "key9910"
    5) "key9907"
    6) "key9989"
    7) "key9971"
    8) "key99"
    9) "key9966"
   10) "key992"
   11) "key9903"
   12) "key9905"
127.0.0.1:6379> scan 13976 match key99* count 1000
1) "1996"
2)  1) "key9982"
    2) "key9997"
    3) "key9963"
    4) "key996"
    5) "key9912"
    6) "key9999"
    7) "key9921"
    8) "key994"
    9) "key9956"
   10) "key9919"
127.0.0.1:6379> scan 1996 match key99* count 1000
1) "12594"
2) 1) "key9939"
   2) "key9941"
   3) "key9967"
   4) "key9938"
   5) "key9906"
   6) "key999"
   7) "key9909"
   8) "key9933"
   9) "key9992"
   
......


127.0.0.1:6379> scan 11687 match key99* count 1000
1) "0"
2)  1) "key9969"
    2) "key998"
    3) "key9986"
    4) "key9968"
    5) "key9965"
    6) "key9990"
    7) "key9915"
    8) "key9928"
    9) "key9908"
   10) "key9929"
   11) "key9944"
```



#### 2.2.1.3、结果分析

> 从上面的过程可以看到虽然提供的 `limit` 是 `1000`，但是返回的结果只有 `10` 个左右。**因为这个 `limit` 不是限定返回结果的数量，而是限定服务器单次遍历的字典槽位数量(约等于)**。     
>
> 如果将 `limit` 设置为 10，你会发现返回结果是空的，但是游标值不为零，意味着遍历还没结 束

```shell
127.0.0.1:6379> scan 0 match key99* count 10
1) "3072"
2) (empty list or set)
```







# 3、`scan` 原理

## 3.1、字典的结构

> 在 `Redis` 中所有的 `key` 都存储在一个很大的字典中，这个字典的结构和 `Java` 中的 `HashMap` 一样，是一维数组 + 二维链表结构，第一维数组的大小总是` 2^n`( `n>=0`)，扩容一 次数组大小空间加倍，也就是 `n++`。



![image-20210508150505749](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210508150505749.png)



`scan` 指令返回的游标就是第一维数组的位置索引，我们将这个位置索引称为槽 (`slot`)。 如果不考虑字典的扩容缩容，直接按数组下标挨个遍历就行了，实际中遍历顺序会考虑扩容和缩容。    

`limit` 参数就表示需要遍历的槽位数，之所以返回的结果可能多可能少，是因为不是所有的槽位上都会挂接链表，有些槽 位可能是空的，还有些槽位上挂接的链表上的元素可能会有多个。每一次遍历都会将 `limit` 数量的槽位上挂接的所有链表元素进行模式匹配过滤后，一次性返回给客户端。





## 3.2、`scan` 遍历顺序

> 1、如果不考虑扩容与缩容,那么无论是从前遍历还是从后遍历都可以获取所有的`key`值,但是有扩容,缩容后就需要考虑遍历的准确性     
>
> 2、如果我们按照低位加法,即从前向后遍历,当扩容或者缩容时进行的`rehash`操作使得数据分散到不同的槽位,**这就有可能发生重复遍历与遗漏遍历的情况**.   
>
> 3、因此`scan` 的遍历不是从第一维数组的第 `0` 位一直遍历到末尾，而是**采用了高位进位加法来遍历。之所以使用这样特殊的方式进行遍历，是考虑到字典的扩容和缩容时避免槽位的遍历重复和遗漏**。      
>
> 普通加法和高位进位加法的区别:
>
> > 高位进位法从左边加，进位往右边移动，同普通加法正好相反。但是最终它们都会遍历所有的槽位并且没有重复。



关于`scan`命令的遍历顺序，我们可以用一个小例子来具体看一下：

```shell
127.0.0.1:6379> keys *
1) "db_number"
2) "key1"
3) "myKey"
127.0.0.1:6379> scan 0 MATCH * COUNT 1
1) "2"
2) 1) "db_number"
127.0.0.1:6379> scan 2 MATCH * COUNT 1
1) "1"
2) 1) "myKey"
127.0.0.1:6379> scan 1 MATCH * COUNT 1
1) "3"
2) 1) "key1"
127.0.0.1:6379> scan 3 MATCH * COUNT 1
1) "0"
2) (empty list or set)
```

我们的Redis中有3个key，我们每次只遍历一个一维数组中的元素。如上所示，SCAN命令的遍历顺序是

> 0->2->1->3

这个顺序看起来有些奇怪。我们把它转换成二进制就好理解一些了。

> 00 -> 10 -> 01->11

我们发现每次这个序列是高位加1的。普通二进制的加法，是从右往左相加、进位。而这个序列是从左往右相加、进位的。这一点我们在`redis`的源码中也得到印证。



**问题：为什么要使用这样的顺序进行遍历，而不是用正常的0、1、2……这样的顺序呢？**   

答案：这是因为需要考虑遍历时发生字典扩容与缩容的情况，如果我们按照低位加法,即从前向后遍历,当扩容或者缩容时进行的`rehash`操作使得数据分散到不同的槽位,**这就有可能发生重复遍历与遗漏遍历的情况**.    



## 3.2、字典扩容

> Java 中的 `HashMap` 有扩容的概念，当 `loadFactor` 达到阈值时，需要重新分配一个新的 2 倍大小的数组，然后将所有的元素全部 `rehash` 挂到新的数组下面。`rehash` 就是将元素的 `hash` 值对数组长度进行取模运算，因为长度变了，所以每个元素挂接的槽位可能也发生了变 化。又因为数组的长度是 `2^n` 次方，所以取模运算等价于位与操作。

```java
a mod 8 = a & (8-1) = a & 7
a mod 16 = a & (16-1) = a & 15 
a mod 32 = a & (32-1) = a & 31
```

1、假设当前的字典的数组长度由 `8` 位扩容到 `16` 位，那么 `3` 号槽位 `011` 将会被 `rehash` 到 `3` 号槽位和 `11 （3 + 8）` 号槽位，也就是说该槽位链表中大约有一半的元素还是 `3` 号槽位，其它 的元素会放到 `11` 号槽位，`11` 这个数字的二进制是 `1011`，就是对 `3` 的二进制 `011` 增加了 一个高位 1。      

2、抽象一点说，假设开始槽位的二进制数是 xxx，那么该槽位中的元素将被 `rehash` 到 `0xxx` 和 `1xxx(xxx+8) `中。 如果字典长度由 `16` 位扩容到 `32` 位，那么对于二进制槽位 `xxxx` 中的元素将被` rehash` 到 `0xxxx` 和 `1xxxx`(`xxxx+16`) 中。



![image-20210508160945556](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210508160945556.png)





### 3.2.1、对比扩容缩容前后的遍历顺序



![image-20210508154715880](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210508154715880.png)



**观察这张图，我们发现采用高位进位加法的遍历顺序，`rehash` 后的槽位在遍历顺序上是相邻的**。

扩容：假设当前要即将遍历 `110` 这个位置 (橙色)    

1、那么扩容后，当前槽位上所有的元素对应的新槽位是 `0110` 和 `1110`(深绿色)，也就是在槽位的二进制数增加一个高位 `0` 或 `1`。    

2、这时我们可以直接从 `0110` 这个槽位开始往后继续遍历，`0110` 槽位之前的所有槽位都是已经遍历过的，这样就可以避免扩容后对已经遍历过的槽位进行重复遍历。     





缩容：假设当前即将遍历 `110` 这个位置 (橙色)    

1、那么缩容后，当前槽位所有的元素对应的新槽位是 `10`(深绿色)，也就是去掉槽位二进制最高位。      

2、这时我们可以直接从 `10` 这个槽位继续往后遍历，`10` 槽位之前的所有槽位都是已经遍历过的，这样就可以避免缩容的重复遍历。    

**3、不过缩容还是不太一样，它会对图中 `010` 这个槽位上的元素进行重复遍历，因为缩融后 `10` 槽位的元素是 `010` 和 `110` 上挂接的元素的融合。**



## 3.3、渐进式 `rehash`

> `Java` 的 `HashMap` 在扩容时会一次性将旧数组下挂接的元素全部转移到新数组下面。如 果 `HashMap` 中元素特别多，线程就会出现卡顿现象。**`Redis` 为了解决这个问题，它采用渐 进式 `rehash`**。
>
> **它会同时保留旧数组和新数组，然后在定时任务中以及后续对 `hash` 的指令操作中渐渐 地将旧数组中挂接的元素迁移到新数组上**。    
>
> **1、这意味着要操作处于 `rehash` 中的字典，需要同时访问新旧两个数组结构。如果在旧数组下面找不到元素，还需要去新数组下面去寻找。**
>
> **2、`scan` 也需要考虑这个问题，对与 `rehash` 中的字典，它需要同时扫描新旧槽位，然后将 结果融合后返回给客户端**。





# 4、更多的 `scan` 指令

> `scan` 指令是一系列指令，除了可以遍历所有的 `key` 之外，还可以对指定的容器集合进 行遍历。比如 `zscan` 遍历 `zset` 集合元素，`hscan` 遍历 `hash` 字典的元素、`sscan` 遍历 `set` 集 合的元素。     
>
> 它们的原理同 `scan` 都会类似的，因为 `hash` 底层就是字典，`set` 也是一个特殊的 `hash`(所有的 `value` 指向同一个元素)，`zset` 内部也使用了字典来存储所有的元素内容，所以 这里不再赘述。


```java
String key ="myset";
String patten="old：user*";
String cursor = "0";
while(true){
  ScanResult scanResult = redis.sscan(key,cursor,patten);
  List emelemts = scanResult.getResult();
  redis.srem(key,elements);

  //获取新的游标
  cursor =scanResult.getStringCursor();
  if("0".equals(cursor)){
    break; /、结束循环
  }
}
```













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
		id: 'ABaWqPFQmkwcdUfS',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



