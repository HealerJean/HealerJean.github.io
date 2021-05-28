---
title: Redis原理之_小道消息_PubSub
date: 2018-04-14 03:33:00
tags: 
- Redis
category: 
- Redis
description: Redis原理之_小道消息_PubSub
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`PubSub`

> **前面我们讲了 `Redis` 消息队列的使用方法，但是没有提到 `Redis` 消息队列的不足之处，那就是消息队列不支持消息的多播机制。**

![image-20210527150842748](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20210527150842748.png)



## 1.1、消息多播  

> 答案：简单地说就是一个生产者对应多个消费者   

1、消息多播允许生产者生产一次消息，中间件负责将消息复制到多个消息队列，每个消息队列由相应的消费组进行消费。它是分布式系统常用的一种解耦方式，用于将多个消费组的逻辑进行拆分。  (比如：`Kafka`)    

2、支持了消息多播，多个消费组的逻辑就可以放到不同的子系统中。如果是普通的消息队列，就得将多个不同的消费组逻辑串接起来放在一个子系统中，进行连续消费。



## 1.2、`PubSub介绍`

> 为了支持消息多播，`Redis` 不能再依赖于那 5 种基本数据类型了。它单独使用了一个模块来支持消息多播，这个模块的名字叫着 `PubSub`，也就是 `PublisherSubscriber`，发布者订阅者模型。    



### 1.2.1、生产者和消费者是不同的连接

> 1、`Redis` `PubSub` 的生产者和消费者是不同的连接，也就是两个 `Redis` 的连接。这是必须的，**因为 `Redis` 不允许连接在 `subscribe` 等待消息时还要进行其它的操作**。     
>
>  2、在生产环境中，我们很少将生产者和消费者放在同一个线程里。如果它们真要在同一个线程里，何必通过中间件来流转，直接使用函数调用就行。      





# 2、`PubSub`命令

> Redis提供了基于发布/订阅的消息机制     
>
> **此种模式下，消息发布者和订阅着不能相互直接通信**，**而是发布者客户端向指定的频道（channel）发布消息，订阅该频道的每个客户端都可以收到该消息**

![WX20180413-154821@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180413-154821@2x.png)



## 2.1、发布消息

> 发布客户端发布消息  
>
> ```shell
> `publish channel message` 
> ```



下面在频道`channel:student` 发布了一条消息，返回值为订阅者个数，此时没有订阅者，返回为0


```shell
127.0.0.1:6379> publish channel:student "teacher coming"
(integer) 0
127.0.0.1:6379> 
```



## 2.2、订阅消息

> 1、当订阅的通道的时候，会进入订阅状态，一直等待消息接收，只能接收命令为`subscribe` ,`psubscribe `,`unsubscribe`,`punsubscribe`     
>
> 2、新开启的订阅，无法接收以前的消息，因为`redis`不会对之前的消息进行持久化     
>
> ```shell
> `subscribe channel`
> ```



```shell
订阅客户端
127.0.0.1:6379> subscribe channel:student
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



## 2.3、取消订阅


```shell
unsubscribe channel:student
```



## 2.4、按照匹配模式订阅和取消订阅

> 如果现在要增加一个主题，客户端必须也跟着增加一个订阅指令才可以收 到新开主题的消息推送。但是为了简化订阅的繁琐，`redis` 提供了模式订阅功能 `Pattern Subscribe`，这样就可以一次订阅多个主题，即使生产者新增加了同模式的主题，消费者也可以立即收到消息    




```shell
匹配订阅 psubscribe hello*


匹配取消  punsubscribe hello*
```



## 2.5、查询订阅

### 2.5.1、查看活跃的频道

> 所谓活跃的频道是指至少有一个频道被订阅，如果没有的被定义则返回0    
>
> ```shell
> pubsub  channels [partten] 
> ```



```shell
127.0.0.1:6379> pubsub channels
1) "channel:student"
127.0.0.1:6379> 


没有客户端订阅频道
127.0.0.1:6379> pubsub channels
(empty list or set)
```



### 2.5.2、查看频道订阅数


```shell
127.0.0.1:6379> pubsub numsub channel:student
1) "channel:student"
2) (integer) 1
127.0.0.1:6379> 
```



## 1.6、使用场景

> 聊天教、公告牌  



## 1.7、代码实现

### 1.7.1、redis配置文件


```xml
    <!--配置监听队列-->
    <bean id="requestMessageListener" class="com.hlj.redis.listener.RequestMessageListener"/>

    <redis:listener-container>
        <redis:listener ref="requestMessageListener"  topic="request" />
    </redis:listener-container>

</beans>
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:redis="http://www.springframework.org/schema/redis"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/redis http://www.springframework.org/schema/redis/spring-redis.xsd">



<bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig" >
        <property name="maxTotal" value="${hlj.redis.max-total}"/>
        <property name="maxIdle" value="${hlj.redis.max-idle}"/>
        <property name="maxWaitMillis" value="${hlj.redis.pool.max-wait}"/>
    </bean>

    <bean id="redisConnectionFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory" destroy-method="destroy">
        <property name="password" value="${hlj.redis.password}"/>
        <property name="hostName" value="${hlj.redis.host-name}"/>
        <property name="port" value="${hlj.redis.port}"/>
        <property name="usePool" value="true"/>
        <property name="poolConfig" ref="jedisPoolConfig"/>
    </bean>

    <bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate" scope="prototype">
        <property name="connectionFactory" ref="redisConnectionFactory"/>
        <property name="keySerializer">
            <bean class="com.hlj.redis.cacheSerializer.CustomStringRedisSerializer"/>
        </property>
        <property name="valueSerializer">
            <bean class="com.hlj.redis.cacheSerializer.CustomJSONStringRedisSerializer"/>
        </property>
    </bean>


    <bean id="stringRedisTemplate" class="org.springframework.data.redis.core.StringRedisTemplate" scope="prototype">
        <property name="connectionFactory" ref="redisConnectionFactory"/>
    </bean>

    <!--配置监听队列-->
    <bean id="requestMessageListener" class="com.hlj.redis.listener.RequestMessageListener"/>

    <redis:listener-container>
        <redis:listener ref="requestMessageListener"  topic="request" />
    </redis:listener-container>



</beans>

```


### 1.7.2、配置监听消息


```java
package com.hlj.redis.listener;

import com.hlj.redis.cacheSerializer.CustomJSONStringRedisSerializer;
import com.hlj.redis.cacheSerializer.CustomStringRedisSerializer;
import com.hlj.redis.listener.data.ConvertBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * 通过监听redistemplate进行发送消息
 */
public class RequestMessageListener implements MessageListener {

    private CustomStringRedisSerializer stringRedisSerializer = new CustomStringRedisSerializer();
    private CustomJSONStringRedisSerializer jsonStringRedisSerializer = new CustomJSONStringRedisSerializer();
    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println("message监听");
        ConvertBean convertBean = (ConvertBean) jsonStringRedisSerializer.deserialize(message.getBody());
//        System.out.println(convertBean.toString());

    }
}


```


### 1.7.3、测试


```java
package com.hlj.redis.listener.controller;

import com.hlj.redis.listener.data.ConvertBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


@RequestMapping("redis/listener")
@Controller
public class ListenerController {

    private  RedisTemplate redisTemplate;

    @GetMapping("test")
    @ResponseBody
    public void lockRedis(){
        ConvertBean convertBean = new ConvertBean();
        convertBean.setContent("content");
        convertBean.setToUid("uuid");

        redisTemplate.convertAndSend("request",convertBean);

    }

}


```



# 3、缺点和总结：

**1、消费者挂掉，重启后收不到历史消息**：`PubSub` 的生产者传递过来一个消息，`Redis` 会直接找到相应的消费者传递过去。如果一 个消费者都没有，那么消息直接丢弃。如果开始有三个消费者，一个消费者突然挂掉了，生 产者会继续发送消息，另外两个消费者可以持续收到消息。但是挂掉的消费者重新连上的时 候，这断连期间生产者发送的消息，对于这个消费者来说就是彻底丢失了。

**2、无法持久化：**如果 `Redis` 停机重启，`PubSub` 的消息是不会持久化的，毕竟 `Redis` 宕机就相当于一个 消费者都没有，所有的消息直接被丢弃。   



**总结：因为 `PubSub` 有这些缺点，它几乎找不到合适的应用场景。**       

同步：近期 `Redis5.0 `新增了 `Stream` 数据结构，这个功能给 `Redis `带来了持久化消息队列， 从此 `PubSub` 可以消失了









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
		id: 'LVxZFHCmg054nsq7',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



