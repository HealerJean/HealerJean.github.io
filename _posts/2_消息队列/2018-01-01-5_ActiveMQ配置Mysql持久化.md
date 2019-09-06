---
title: ActiveMQ配置Mysql持久化
date: 2018-01-01 03:33:00
tags: 
- MQ
category: 
- MQ
description: ActiveMQ配置Mysql持久化
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


## 1、ActiveMQ配置Mysql持久化

### 1.1、准备mysql相关的jar包

![1567504683878](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567504683878.png)





### 1.2、修改`activemq.xml`



#### 1.2.1、原来的`activemq.xml`

```xml
<!--
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with
    this work for additional information regarding copyright ownership.
    The ASF licenses this file to You under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with
    the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
-->
<!-- START SNIPPET: example -->
<beans
  xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
  http://activemq.apache.org/schema/core http://activemq.apache.org/schema/core/activemq-core.xsd">

    <!-- Allows us to use system properties as variables in this configuration file -->
    <bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer">
        <property name="locations">
            <value>file:${activemq.conf}/credentials.properties</value>
        </property>
    </bean>

    <!-- Allows accessing the server log -->
    <bean id="logQuery" class="io.fabric8.insight.log.log4j.Log4jLogQuery"
          lazy-init="false" scope="singleton"
          init-method="start" destroy-method="stop">
    </bean>

    <!--
        The <broker> element is used to configure the ActiveMQ broker.
    -->
    <broker xmlns="http://activemq.apache.org/schema/core" brokerName="localhost" dataDirectory="${activemq.data}">

        <destinationPolicy>
            <policyMap>
                <policyEntries>
                    <policyEntry topic=">" >
                        <!-- The constantPendingMessageLimitStrategy is used to prevent
                         slow topic consumers to block producers and affect other consumers
                         by limiting the number of messages that are retained
                         For more information, see:

                         http://activemq.apache.org/slow-consumer-handling.html

                    -->
                        <pendingMessageLimitStrategy>
                            <constantPendingMessageLimitStrategy limit="1000"/>
                        </pendingMessageLimitStrategy>
                    </policyEntry>
                </policyEntries>
            </policyMap>
        </destinationPolicy>


        <!--
            The managementContext is used to configure how ActiveMQ is exposed in
            JMX. By default, ActiveMQ uses the MBean server that is started by
            the JVM. For more information, see:

            http://activemq.apache.org/jmx.html
        -->
        <managementContext>
            <managementContext createConnector="false"/>
        </managementContext>

        <!--
            Configure message persistence for the broker. The default persistence
            mechanism is the KahaDB store (identified by the kahaDB tag).
            For more information, see:

            http://activemq.apache.org/persistence.html
        -->
        <persistenceAdapter>
            <kahaDB directory="${activemq.data}/kahadb"/> 
        </persistenceAdapter>


        <!--
            The systemUsage controls the maximum amount of space the broker will
            use before disabling caching and/or slowing down producers. For more information, see:
            http://activemq.apache.org/producer-flow-control.html
          -->
        <systemUsage>
            <systemUsage>
                <memoryUsage>
                    <memoryUsage percentOfJvmHeap="70" />
                </memoryUsage>
                <storeUsage>
                    <storeUsage limit="100 gb"/>
                </storeUsage>
                <tempUsage>
                    <tempUsage limit="50 gb"/>
                </tempUsage>
            </systemUsage>
        </systemUsage>

        <!--
            The transport connectors expose ActiveMQ over a given protocol to
            clients and other brokers. For more information, see:

            http://activemq.apache.org/configuring-transports.html
        -->
        <transportConnectors>
            <!-- DOS protection, limit concurrent connections to 1000 and frame size to 100MB -->
            <transportConnector name="openwire" uri="tcp://0.0.0.0:61616?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            <transportConnector name="amqp" uri="amqp://0.0.0.0:5672?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            <transportConnector name="stomp" uri="stomp://0.0.0.0:61613?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            <transportConnector name="mqtt" uri="mqtt://0.0.0.0:1883?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
            <transportConnector name="ws" uri="ws://0.0.0.0:61614?maximumConnections=1000&amp;wireFormat.maxFrameSize=104857600"/>
        </transportConnectors>

        <!-- destroy the spring context on shutdown to stop jetty -->
        <shutdownHooks>
            <bean xmlns="http://www.springframework.org/schema/beans" class="org.apache.activemq.hooks.SpringContextHook" />
        </shutdownHooks>

    </broker>




    <!--
        Enable web consoles, REST and Ajax APIs and demos
        The web consoles requires by default login, you can disable this in the jetty.xml file

        Take a look at ${ACTIVEMQ_HOME}/conf/jetty.xml for more details
    -->
    <import resource="jetty.xml"/>





</beans>
<!-- END SNIPPET: example -->
```





#### 1.2.2、修改两处



###### 1、persistenceAdapter 注释掉之前的

```xml
<persistenceAdapter>
    <!--   <kahaDB directory="${activemq.data}/kahadb"/>  -->
    <jdbcPersistenceAdapter dataSource="#mysql-ds" />
</persistenceAdapter>

```

###### 2、在`</broker>` 后面紧跟着加上mysql连接属性

```xml

</broker>


<bean id="mysql-ds" class="org.apache.commons.dbcp2.BasicDataSource" 
      destroy-method="close">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost:3306/hlj-activemq?useUnicode=true&amp;characterEncoding=UTF-8"/>
    <property name="username" value="root"/>
    <property name="password" value="123456"/>
    <property name="poolPreparedStatements" value="true"/>
</bean>
```



### 1.3、启动activemq

![1567504945676](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567504945676.png)







### 1.4、观察数据库生成了3张表

```java

ACTIVEMQ_MSGS ：消息表，queue和topic都存在这个表中，发送后会添加相应记录，消费完后会自动删除
ACTIVEMQ_ACKS ：存储持久订阅的消息，订阅者和服务器的订阅关系在这个表保存
ACTIVEMQ_LOCKS ：锁表，ActiveMQ的锁信息，在集群环境中才有用，锁表，用来确保某一时刻，只能有一个ActiveMQ broker实例来访问数据库

```



#### 1.4.1、ACTIVEMQ_MSGS



**ACTIVEMQ_MSGS：**用于存储消息，Queue和Topic都存储在这个表中，发送后会添加相应记录，消费完后会自动删除



```sql
create table `activemq_msgs` (
    `id` bigint(20) not null,
    `container` varchar(250) not null,
    `msgid_prod` varchar(250) default null,
    `msgid_seq` bigint(20) default null,
    `expiration` bigint(20) default null,
    `msg` blob,
    `priority` bigint(20) default null,
    `xid` varchar(250) default null,
    primary key (`id`),
    key `activemq_msgs_midx` (`msgid_prod`,`msgid_seq`),
    key `activemq_msgs_cidx` (`container`),
    key `activemq_msgs_eidx` (`expiration`),
    key `activemq_msgs_pidx` (`priority`),
    key `activemq_msgs_xidx` (`xid`)
) engine=innodb default charset=utf8mb4;
```



| 字段名     | 类型     | 含义                            |
| ---------- | -------- | ------------------------------- |
| container  | varchar  | 容器，消息的destination         |
| msgid_prod | varchar  | 消息发送者客户端的主键          |
| msgid_seq  | bigint   | 发送消息的顺序                  |
| expiration | varchar  | 消息过期时间，毫秒数            |
| msg        | longblob | 消息数据，二进制                |
| priority   | bigint   | 优先级0-9，数值越大，优先级越高 |
| xid        | varchar  |                                 |





#### 1.4.1、`ACTIVEMQ_ACKS`



**存储持久订阅的消息，订阅者和服务器的订阅关系在这个表保存**



```sql


create table `activemq_acks` (
    `container` varchar(250) not null,
    `sub_dest` varchar(250) default null,
    `client_id` varchar(250) not null,
    `sub_name` varchar(250) not null,
    `selector` varchar(250) default null,
    `last_acked_id` bigint(20) default null,
    `priority` bigint(20) not null default '5',
    `xid` varchar(250) default null,
    primary key (`container`,`client_id`,`sub_name`,`priority`),
    key `activemq_acks_xidx` (`xid`)
) engine=innodb default charset=utf8mb4;


```



| 字段名        | 类型    | 含义                                                         |
| ------------- | ------- | ------------------------------------------------------------ |
| container     | varchar | 容器，消息的destination                                      |
| sub_dest      | varchar | 如果是使用的Static集群，这个字段会有集群其他系统的信息       |
| client_id     | varchar | 订阅者的客户端唯一Id                                         |
| sub_name      | varchar | 订阅者名称                                                   |
| selector      | varchar | 用户可以选择只消费满足条件的信息，条件可以用自定义的属性实现 |
| last_acked_id | bigint  | 记录消费过的信息Id                                           |
| priority      | bigint  | 优先级0-9，数值越大，优先级越高                              |
| xid           | varchar |                                                              |
|               |         |                                                              |





#### 1.4.2、`ACTIVEMQ_LOCKS`



锁表，ActiveMQ的锁信息，在集群环境中才有用，锁表，用来确保某一时刻，只能有一个ActiveMQ broker实例来访问数据库



```sql

create table `activemq_lock` (
    `id` bigint(20) not null,
    `time` bigint(20) default null,
    `broker_name` varchar(250) default null,
    primary key (`id`)
) engine=innodb default charset=utf8mb4;


```



| 字段名      | 类型 | 含义 |
| ----------- | ---- | ---- |
| id          |      |      |
| time        |      |      |
| broker_name |      |      |





## 2、Queue测试Mysql持久化

### 2.1、生产者 `MysqlQueueProducer`



```java
package com.hlj.activemq.d04_Mysql持久化.d01_queue;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.MessageTransformer;

import javax.jms.*;


/**
 * queue模式是持久化的
 */
public class MysqlQueueProducer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "MysqlQueue";
    /**
     * 发送消息的数量
     */
    private static final int SEND_NUMBER = 5;

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            // 构造从工厂得到连接对象
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // 获取操作连接,一个发送或接收消息的线程
            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            // 消息的目的地;消息发送给谁.
            Destination destination = session.createQueue(QUEUE_NAME);

            // 根据目的地获取一个生产者
            MessageProducer producer = session.createProducer(destination);

            //构造消息
            sendTextMessage(session, producer);


            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 1、创建TextMessage
     */
    private static void sendTextMessage(Session session, MessageProducer producer) throws JMSException {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("ActiveMq 发送的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }

}

```



### 2.2、消费者`MysqlQueueConsumer`

```java
package com.hlj.activemq.d04_Mysql持久化.d01_queue;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class MysqlQueueConsumer {


    public static final String QUEUE_NAME = "MysqlQueue";
    public static final Long   WITE_TIME = (100L * 1000L);


    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            // 构造从工厂得到连接对象
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // 获取操作连接,一个发送或接收消息的线程
            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            // 消息的目的地;消息发送给谁.
            Destination destination = session.createQueue(QUEUE_NAME);

            //根据目的地获取一个消费者
            MessageConsumer consumer = session.createConsumer(destination);


            //消费消息
            //1、接收TestMessage
            reveiveTestMessage(consumer);

            // 没有事务，下面提交会报错
            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收TestMessage
     */
    private static void reveiveTestMessage(MessageConsumer consumer) throws JMSException {
        while (true) {
            //100s内阻塞等待消息的传入
            TextMessage message = (TextMessage) consumer.receive(WITE_TIME);
            if (null != message) {
                System.out.println("收到消息" + message.getText());
            } else {
                break;
            }
        }
    }
}




```



### 2.3、运行消费者，等待消息接入

#### 2.3.1、观察浏览器

![1567568602072](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567568602072.png)



| name       | Number Of Pending Messages | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ---------- | -------------------------- | ------------------- | ----------------- | ----------------- |
| FirstQueue | 0                          | 1                   | 0                 | 0                 |



#### 2.3.2、数据库中什么都没发生





### 2.4、关闭消费者，运行生产者，生成消息



#### 2.4.1、观察浏览器

![1567568774108](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567568774108.png)



| name       | Number Of Pending Messages | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ---------- | -------------------------- | ------------------- | ----------------- | ----------------- |
| MysqlQueue | 5                          | 0                   | 5                 | 0                 |



#### 2.4.2、观察数据库



![1567568825692](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567568825692.png)



### 2.5、运行消费者

#### 2.5.1、观察控制台



```java
收到消息ActiveMq 发送的消息1
收到消息ActiveMq 发送的消息2
收到消息ActiveMq 发送的消息3
收到消息ActiveMq 发送的消息4
收到消息ActiveMq 发送的消息5
```



#### 2.5.2、观察浏览器

![1567569493646](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567569493646.png)



| name       | Number Of Pending Messages | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ---------- | -------------------------- | ------------------- | ----------------- | ----------------- |
| MysqlQueue | 0                          | 0                   | 5                 | 5                 |





#### 2.5.3、观察数据库，发现已经光光的，说明已经被消费了



![1567569514221](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567569514221.png)




## 3、Topic持久化消息



### 3.1、生产者`MysqlPersistenceProducer`

```java
package com.hlj.activemq.d04_Mysql持久化.d02_topic持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MysqlPersistenceProducer {


    public static final String TOPIC_NAME = "mysql_persiterce_topic_name";
    /**
     * 发送消息的数量
     */
    private static final int SEND_NUMBER = 5;

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();

            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(topic);
            //设置持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //一定要砸在上面持久化订阅设置完成之后再start这个connection，否则会有问题
            connection.start();
            System.out.println("创建持久化生产者");

            for (int i = 1; i <= SEND_NUMBER; i++) {
                TextMessage message = session.createTextMessage("message" + i);
                producer.send(message);
            }

            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

```



### 3.2、消费者`MysqlPersistenceConsumer`



```java
package com.hlj.activemq.d04_Mysql持久化.d02_topic持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MysqlPersistenceConsumer {

    public static final String TOPIC_NAME = "mysql_persiterce_topic_name";
    public static final Long WITE_TIME = (1000L);


    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            //设置连接客户端 id
            connection.setClientID("ClientId_Blog");


            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(TOPIC_NAME);
            //创建持久化的订阅者，订阅者的名称 name
            // TopicSubscriber consumer = session.createDurableSubscriber(topic, "name");
            TopicSubscriber consumer = session.createDurableSubscriber(topic, "Sub_HealerJean");
            //一定要砸在上面持久化订阅设置（createDurableSubscriber）完成之后再start这个connection，否则会有问题
            connection.start();
            System.out.println("创建持久化消费者");

            Message message = consumer.receive();
            while (message != null) {
                TextMessage txtMsg = (TextMessage) message;
                System.out.println("收到消 息：" + txtMsg.getText());
                message = consumer.receive(WITE_TIME);
            }
            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

```



### 3.3、运行消费者，去订阅

#### 3.3.1、观察浏览器

![1567569921908](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567569921908.png) 





| name                        | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| --------------------------- | ------------------- | ----------------- | ----------------- |
| mysql_persiterce_topic_name | 1                   | 0                 | 0                 |





#### 3.3.2、观察数据库

**activemq_acks**

![1567570120589](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567570120589.png)



### 3.4、运行生产者



#### 2.4.1、观察控制台

```java
收到消 息：message1
收到消 息：message2
收到消 息：message3
收到消 息：message4
收到消 息：message5
```



#### 3.4.2、观察浏览器



![1567570438335](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567570438335.png)





| name                        | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| --------------------------- | ------------------- | ----------------- | ----------------- |
| mysql_persiterce_topic_name | 1                   | 5                 | 5                 |



#### 2.4.3、观察数据库，持久化订阅，会发现这里的数据不会消失





![1567570587101](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567570587101.png)





## 4、高速缓存

**持久化topic先放到缓存中，提供给java程序执行，然后再慢慢放入到数据库中， 比如，消息比较多，我们先放到缓存中，提供给已知的订阅者服务，然后再放到数据库中，将来提供给其他未在线的人服务**



### 4.1、activem.xml配置



```xml
<!-- <persistenceAdapter> -->
<!-- <kahaDB directory="${activemq.data}/kahadb"/> -->
<!-- mysql持久化-->
<!-- <jdbcPersistenceAdapter dataSource="#mysql-ds"/> -->
<!-- </persistenceAdapter> -->

<!-- mysql持久化 + 高速缓存配置 -->
<persistenceFactory>  
    <!-- 指定数据文件的路径及数据源 -->  
    <journalPersistenceAdapterFactory 
                                      dataDirectory="${activemq.data}/cache-data" 
                                      dataSource="#mysql-ds"/>  
</persistenceFactory>
```



![1567572757978](D:\study\HealerJean.github.io\blogImages\1567572757978.png)



### 4.2、自己测试吧



![1567573162898](D:\study\HealerJean.github.io\blogImages\1567573162898.png)







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
		id: 'TEcAbMYnwkrpfGsj',
    });
    gitalk.render('gitalk-container');
</script> 

