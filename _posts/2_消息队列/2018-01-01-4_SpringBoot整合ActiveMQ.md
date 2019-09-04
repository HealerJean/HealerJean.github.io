---
title: SpringBoot整合ActiveMQ
date: 2018-01-01 03:33:00
tags: 
- MQ
category: 
- MQ
description: SpringBoot整合ActiveMQ
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    


### 1、Maven

```xml

        <!-- activemq -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
        </dependency>
        
```





### 2、SpringBoot配置

```properties
server.port=8888

# 将spring boot日志级别调整为debug级别，以便更方便查看打印出来的日志信息(只针对org.springframework及其子包)
logging.level.org.springframework=info

# ActiveMQ通讯地址
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.user=admin
spring.activemq.password=admin
#最大连接数
spring.activemq.pool.maxConnections=2
#空闲时间
spring.activemq.pool.idleTimeout=30000
#是否启用内存模式（就是不安装MQ，项目启动时同时启动一个MQ实例）
spring.activemq.in-memory=false
##信任所有的包
spring.activemq.packages.trust-al=true
# 是否替换默认的连接池，使用ActiveMQ的连接池需引入的依赖
spring.activemq.pool.enabled=false


```



### 3、Config配置

**springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置该bean**

```java
@Configuration
@EnableJms
public class ActiveMQConfig {

    /**
     * springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置该bean
     */
    @Bean(name = "jmsTopicListenerContainerFactory")
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(
        ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        return factory;
    }

}

```



### 4、Consumer监听消息（queue和非持久topic消息）



```java
@JmsListener(destination = QUEUE_NAME)

 @JmsListener(destination = TOPIC_NAME, 
                 containerFactory = "jmsTopicListenerContainerFactory")
```



```java

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author HealerJean
 * @ClassName JMSConsumer
 * @date 2019/9/3  10:27.
 * @Description
 */
@Slf4j
@Component
public class JMSConsumer {

    public static final String QUEUE_NAME = "SpringBoot:Queue";
    public static final String TOPIC_NAME = "SpringBoot:Topic";


    /**
     * 接收queue类型消息
     * destination对应配置类中ActiveMQQueue("SpringBoot:Queue")设置的名字
     */
    @JmsListener(destination = QUEUE_NAME)
    public void listenQueue(String msg) {
        System.out.println("接收到queue消息：" + msg);
    }

    /**
     * 接收topic类型消息
     * destination对应配置类中ActiveMQTopic("SpringBoot:Topic")设置的名字
     * containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
     */
    @JmsListener(destination = TOPIC_NAME, 
                 containerFactory = "jmsTopicListenerContainerFactory")
    public void listenTopic(String msg) {
        System.out.println("接收到topic消息：" + msg);
    }
}

```



### 5、producer生产者发送消息

```java
@Service
public class JMSProducer {


    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送Queue消息
     */
    public void sendMessage(Destination destination, String message) {
        this.jmsTemplate.convertAndSend(destination, message);
    }


}
```



### 6、controller测试

```java
package com.hlj.activemq.d03_SpringBoot配置activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @author HealerJean
 * @ClassName Controller
 * @date 2019/9/3  11:32.
 * @Description
 */
@RestController
@RequestMapping("hlj/activemq")
public class MainController {

    @Autowired
    private JMSProducer jmsProducer;

    /**
     * 发送消息的数量
     */
    public static final String QUEUE_NAME = "SpringBoot:Queue";
    public static final String TOPIC_NAME = "SpringBoot:Topic";

    /**
     * 发送queue类型消息
     */
    @GetMapping("/queue")
    @ResponseBody
    public String sendQueueMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return "msg不能为空";
        }
        Queue destination = new ActiveMQQueue(QUEUE_NAME);
       jmsProducer.sendMessage(destination, msg);
        return "queue send success";
    }


    /**
     * 发送topic类型消息
     */
    @GetMapping("/topic")
    @ResponseBody
    public String sendTopicMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return "msg不能为空";
        }
        Topic destination = new ActiveMQTopic(TOPIC_NAME);
        jmsProducer.sendMessage(destination, msg);
        return "topic send success";
    }



}

```



### 6.2.1、启动项目

##### 6.2.1.1.queue

![1567501888012](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567501888012.png)



| name       | Number Of Pending Messages | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ---------- | -------------------------- | ------------------- | ----------------- | ----------------- |
| SpringBoot:Queue | 0                         | 1                   | 0                | 0                 |



##### 6.2.1.1、topic



![1567501941928](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567501941928.png)



#### 6.2.1.2、Subscribers



![1567501979357](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567501979357.png)







#### 6.2.1、发送queue消息

```

http://localhost:8888/hlj/activemq/queue?msg=hello

控制台 接收到queue消息：hello

浏览器 queue send success



```





![1567502025270](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567502025270.png)





| name             | Number Of Pending Messages | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ---------------- | -------------------------- | ------------------- | ----------------- | ----------------- |
| SpringBoot:Queue | 0                          | 1                   | 1                 | 1                 |





#### 6.2.2、发送topic消息

```
GET http://localhost:8888/hlj/activemq/topic?msg=hello

控制台 接收到topic消息：hello

浏览器 topic send success



```

![1567502071104](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567502071104.png)





| name             | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ---------------- | ------------------- | ----------------- | ----------------- |
| SpringBoot:Queue | 1                   | 1                 | 1                 |

![1567502165444](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567502165444.png)







<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>       

   



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'ZOn90372pjumkzJX',
    });
    gitalk.render('gitalk-container');
</script> 

