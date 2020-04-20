---

title: Topic持久化和非持久化_虚拟Topic
date: 2018-01-01 03:33:00
tags: 
- MQ
category: 
- MQ
description: Topic持久化和非持久化_虚拟Topic
---





**前言**    

[博主github](https://github.com/HealerJean)    

[博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)       




## 1、非持久化的topic消息：

### 解释：必须接收方在线，这个不会帮我们保存



### 1.1、创建生产者

```java
package com.hlj.activemq.d02_topic持久化和非持久化.d01_非持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class NoPersistenceProducer {

    /**
     * 非持久化topic名称
     */
    public static final String TOPIC_NAME = "no_persiterce_topic_name";
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
            connection.start();

            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(destination);


            for (int i = 0; i < SEND_NUMBER; i++) {
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



### 1.2、创建消费者

```java
package com.hlj.activemq.d02_topic持久化和非持久化.d01_非持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class NoPersistenceConsumer {

    /**
     * 非持久化topic名称
     */
    public static final String TOPIC_NAME = "no_persiterce_topic_name";
    public static final Long   WITE_TIME = (100L * 1000L);

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(TOPIC_NAME);
            MessageConsumer consumer = session.createConsumer(destination);
            Message message = consumer.receive();
            while (message != null) {
                TextMessage txtMsg = (TextMessage) message;
                System.out.println("收到消 息：" + txtMsg.getText());
                message = consumer.receive(WITE_TIME);
            }

            session.commit();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}

```



### 1.3、运行生产者，不运行消费者



#### 1.1、观察浏览器  

![1566980589040](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566980589040.png)





**可以看到没有消费者在线，已经生产了3条消息了，**



| name                     | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ------------------------ | ------------------- | ----------------- | ----------------- |
| no_persiterce_topic_name | 0                   | 5                 | 0                 |



### 1.4、运行消费者 

#### 1.4.1、观察控制台 

发现控制台一直在等待，但是没有消息能够读取  



#### 1.4.2、观察浏览器

![1566980818525](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566980818525.png)



**消费者在线了，但是消息却没有出队列，因为我们这个非持久的消息必须是消费者在线**

| name                     | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ------------------------ | ------------------- | ----------------- | ----------------- |
| no_persiterce_topic_name | 1                   | 5                 | 0                 |



### 1.5、运行生产者 

说明：这个时候消费者是在线状态，等待消息的传入  ，这个时候运行生产者，观察消费者控制台 

#### 1.5.1、消费者控制台

```java
收到消 息：message0
收到消 息：message1
收到消 息：message2
收到消 息：message3
收到消 息：message4
```



#### 1.5.2、观察浏览器

![1566981177326](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566981177326.png)



**这个时候，入队（已经生产的消息的数量）的消息一共有5+5=10条，消费者读取到了5条**

| name                     | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ------------------------ | ------------------- | ----------------- | ----------------- |
| no_persiterce_topic_name | 0                   | 10                | 5                 |







## 2、持久化的topic消息 ：

### 解释：

+ **持久化的topic，即使还没有生产消息，但一般情况下需要消费者提前订阅，因为这样，即使不在线，下次连接，也可以接受之前从没收过的消息，而已经收到的消息，则不会重复接受**  



+ **持久化模式下可有有多个`clientID`同时在线,但是同一个`clientID`,只能同时在线一个消费者，这也是虚拟`topic`产生的原因之一 ：`activemq`区分消费者，是通过`clientID`和订阅客户名称来区分的，使用相同的`clientID`，则认为是同一个消费者。两个程序使用相同的`clientID`，则同时只能有一个连接到activemq，第二个连接的会报错**




### 2.1、创建生产者



```java
 MessageProducer producer = session.createProducer(topic);
//设置持久化
producer.setDeliveryMode(DeliveryMode.PERSISTENT);
//一定要砸在上面持久化订阅设置完成之后再start这个connection，否则会有问题
connection.start();
```



```java
package com.hlj.activemq.d02_topic持久化和非持久化.D02_持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class PersistenceProducer {


    public static final String TOPIC_NAME = "persiterce_topic_name";
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



### 2.2、创建消费订阅者  name



```java
//设置连接客户端 id
connection.setClientID("HealerJean");
            
Topic topic = session.createTopic(TOPIC_NAME);
//创建持久化的订阅者，订阅者的名称 name
TopicSubscriber consumer = session.createDurableSubscriber(topic, "name");  
//一定要砸在上面持久化订阅设置（createDurableSubscriber）完成之后再start这个connection，否则会有问题
connection.start();
```



```java
package com.hlj.activemq.d02_topic持久化和非持久化.D02_持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class PersistenceConsumer {

    public static final String TOPIC_NAME = "persiterce_topic_name";
    public static final Long WITE_TIME = (1000L);


    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            
             //设置连接客户端 id ,持久化模式下可有有多个clientID,但是同一个clientID,只能同时在线一个消费者
            connection.setClientID("HealerJean");


            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(TOPIC_NAME);
            //创建持久化的订阅者，订阅者的名称 name
            TopicSubscriber consumer = session.createDurableSubscriber(topic, "name");
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



### 2.3、运行消费订阅者 name （这个时候消费者一直在等待消息哦）

#### 2.3.1、观察浏览器



+ **有一个订阅者出现了**

![1566984898908](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566984898908.png)



+ **即使没有发布消息，但是实际上我们已经将topic创建出来了**

![1566987970895](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566987970895.png)



### 2.4、断开消费订阅者name 控制台，观察浏览器，消费者离线了哦

会观察到，这个时候这个订阅者跑到了未在线里面去了

![1566984956078](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566984956078.png)



### 2.5、运行生产者

#### 2.5.1、观察浏览器



![1566985072703](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566985072703.png)



**会观察到有一个消费者，但是事实上我们的订阅消费者已经挂掉了，不是么，上面消费者控制台都关闭了，虽然控制台关闭了，但是其实我这里认为是一个离线状态的订阅消费者。而且计算它了**

| name                  | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| --------------------- | ------------------- | ----------------- | ----------------- |
| persiterce_topic_name | 1                   | 5                 | 0                 |





### 2.6、修改消费者name代码，再创建一个消费订阅者name2



只讲name修改为name2，然后运行即可

```java
TopicSubscriber consumer = session.createDurableSubscriber(topic, "name2");
```



**观察控制台，会发现没有消息接收到，因为我一开始也其实说了，人家发布消息之前你还没来呢**





### 2.6、运行消费者name

#### 2.6.1、观察控制台

```java
创建持久化消费者
17:39:48.841 [main] DEBUG org.apache.activemq.TransactionContext - Begin:TX:ID:MI-201902210704-50981-1566985188565-1:1:1
收到消 息：message1
收到消 息：message2
收到消 息：message3
收到消 息：message4
收到消 息：message5
```



#### 2.6.2、观察浏览器



![1566988235828](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566988235828.png)





**会发现有2个消费者,5条入队的消息，5条被消费者消费出队的消息，**    

**这里强调一点的是，其实我们这5条出队消息真正意义上只是提示消费者接受了几条消息，而不是真正的出队。因为持久化的topic 如果有两个消费者同时在线的话，出队消息的数量 =消息数量*2。并不等于入队的消息数量**   

**不信，往下看** 



| name                  | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| --------------------- | ------------------- | ----------------- | ----------------- |
| persiterce_topic_name | 2                   | 5                 | 5                 |



### 2.7、再次运行生产者

#### 2.7.1、观察控制台

![1566988589022](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566988589022.png)



**没毛病，消费者不在线，肯定不能消费消息，所以入队信息为10**

| name                  | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| --------------------- | ------------------- | ----------------- | ----------------- |
| persiterce_topic_name | 2                   | 10                | 5                 |





### 2.8、运行消费者 name

讲代码中name2变成name即可

#### 2.8.1、观察控制台 

出现了消息，因为之前订阅过了

```
收到消 息：message1
收到消 息：message2
收到消 息：message3
收到消 息：message4
收到消 息：message5
```



#### 2.8.2、观察浏览器

![1566988839373](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566988839373.png)





| name                  | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| --------------------- | ------------------- | ----------------- | ----------------- |
| persiterce_topic_name | 2                   | 10                |                   10|





### 2.9、运行消费者 name2

讲代码中name改为name

#### 2.9.1、 观察控制台

出现了消息，因为之前订阅过了

```
收到消 息：message1
收到消 息：message2
收到消 息：message3
收到消 息：message4
收到消 息：message5
```



#### 2.9.2、观察浏览器



![1566988953065](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566988953065.png)

| name                  | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| --------------------- | ------------------- | ----------------- | ----------------- |
| persiterce_topic_name | 2                   | 10                | 15                |



### 3.0、总结2.8和2.9

这样就证明了2.6.2中的说法是正确的。





## 3、VirtualTopic：虚拟topic



### 3.1、解释

 `VirtualTopic`**是为了解决持久化模式下多消费端同时接收同一条消息的问题。**     

**分布式应用，这样可以避免同一个应用订阅同一个主题时导致必须修改`clientId`的限制(个人理解，其实我们就是在一个客户端下调用，用多个clientId不太好吧)，同时又可以在同一个应用的不同进行负载均衡**    





### 3.2、场景分析

生产端产生了一笔订单，作为消息发了出去，这笔订单既要入订单系统归档，又要入结算系统收款，那怎么办呢？

```java
1、持久化：订单很重要，丢了可不行

2、同时接收：既要归档，又要结算

3、生产端只需向一个Destination发送：一把钥匙开一把锁，保持发送的一致性，否则容易乱套

```



#### 3.2.1、可能的解决方案

+ 方案A: 使用`Topic订阅模式`，虽然满足1对多同时接收，然而持久化模式下只能有一个持有`clientID`的消费者连接，不满足持久化需求（(**个人理解，其实我们就是在一个客户端下调用，用多个clientId不太好吧**)）

+ 方案B: 使用单队列，队列是1对1模式，消息只能给一个消费者，不满足多个同时接收的需求

+ 方案C: 使用多队列，显然生产者不太愿意一条消息发送很多次，分别发送给不同的队列，万一队列A发送成功，队列B发送失败怎么办？一致性无法保证，容易乱套

+ **方案D：就是将Topic和Queue相结合，各取所长。`VirtualTopic`,对生产者而言它是Topic，对消费者而言它是Queue **



### 3.2、生产者Topic ，`VirtualTopic.Name`



+ **对于消息发布者来说，就是一个正常的topic,名称以VirtualTopic.开始**

```java

Destination destination = session.createTopic("VirtualTopic.Name");
```



```java

public class Producer {

    /**
     * 队列的名称
     */
    public static final String VIRTUAL_TOPIC_NAME = "VirtualTopic.Name";
    /** 发送消息的数量 */
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
            Destination destination = session.createTopic(VIRTUAL_TOPIC_NAME);

            // 根据目的地获取一个生产者
            MessageProducer producer = session.createProducer(destination);

            //构造消息
            //1 、创建TextMessage
            sendTextMessage(session, producer);

            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void sendTextMessage(Session session, MessageProducer producer) throws JMSException {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("ActiveMq的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }


}

```



### 3.3、消费者 A 



+ **对于消息接收端来说，是个队列，不同应用里使用不同的前缀作为队列名称，即可表明自己的身份即可实现消费端应用分组**

  

```java
 Destination destination = session.createQueue("Consumer.AA.VirtualTopic.Name");


Consumer.A.VirtualTopic.Orders说明它是名称为A的消费端，同理Consumer.B VirtualTopic.Orders说明是一名称为B的消费端。可以在同一个应用中使用多个消费者消费这个队列
```



```java

public class ConsumerA {

    public static final String CONSUMER_VIRTUAL_TOPIC_NAME = "Consumer.AA.VirtualTopic.Name";

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
                    Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);

            // 消息的目的地;消息发送给谁.//名称为A的区别
            Destination destination = session.createQueue(CONSUMER_VIRTUAL_TOPIC_NAME);

            //根据目的地获取一个消费者
            MessageConsumer consumer = session.createConsumer(destination);


            //消费消息
            //1、接收TestMessage
            reveiveTestMessage(consumer);

            // 没有事务，下面提交会报错
            // session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reveiveTestMessage(MessageConsumer consumer) throws JMSException {
        while (true) {
            //100s内阻塞等待消息的传入
            TextMessage message = (TextMessage) consumer.receive();
            if (null != message) {
                System.out.println(CONSUMER_VIRTUAL_TOPIC_NAME + "收到消息" + message.getText());
            } else {
                break;
            }
        }
    }

}
```



### 3.4、消费者B

```java

public class ConsumerB {

    public static final String CONSUMER_VIRTUAL_TOPIC_NAME = "Consumer.BB.VirtualTopic.Name";

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
                    Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);

            // 消息的目的地;消息发送给谁.//名称为A的区别
            Destination destination = session.createQueue(CONSUMER_VIRTUAL_TOPIC_NAME);

            //根据目的地获取一个消费者
            MessageConsumer consumer = session.createConsumer(destination);


            //消费消息
            //1、接收TestMessage
            reveiveTestMessage(consumer);

            // 没有事务，下面提交会报错
            // session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reveiveTestMessage(MessageConsumer consumer) throws JMSException {
        while (true) {
            //100s内阻塞等待消息的传入
            TextMessage message = (TextMessage) consumer.receive();
            if (null != message) {
                System.out.println(CONSUMER_VIRTUAL_TOPIC_NAME + "收到消息" + message.getText());
            } else {
                break;
            }
        }
    }




}

```



### 3.5、运行两个消费者，之后再运行生产者



#### 3.4.1、消费者A控制台

```
Consumer.AA.VirtualTopic.Name收到消息ActiveMq的消息1
Consumer.AA.VirtualTopic.Name收到消息ActiveMq的消息2
Consumer.AA.VirtualTopic.Name收到消息ActiveMq的消息3
Consumer.AA.VirtualTopic.Name收到消息ActiveMq的消息4
Consumer.AA.VirtualTopic.Name收到消息ActiveMq的消息5
```



#### 3.5.1、消费者B控制台

```
Consumer.BB.VirtualTopic.Name收到消息ActiveMq的消息1
Consumer.BB.VirtualTopic.Name收到消息ActiveMq的消息2
Consumer.BB.VirtualTopic.Name收到消息ActiveMq的消息3
Consumer.BB.VirtualTopic.Name收到消息ActiveMq的消息4
Consumer.BB.VirtualTopic.Name收到消息ActiveMq的消息5
```



#### 2.5.3、8161浏览器 

##### 2.5.3.1、queue



![1567748451256](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567748451256.png)




| name                          | Number Of Pending Messages | Number Of Consumers | Messages Enqueued | Messages Dequeued |
| ----------------------------- | -------------------------- | ------------------- | ----------------- | ----------------- |
| Consumer.AA.VirtualTopic.Name | 0                          | 1                   | 5                 | 5                 |
| Consumer.BB.VirtualTopic.Name | 0                          | 1                   | 5                 | 5                 |



##### 2.5.3.2、topic



![1567748684504](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1567748684504.png)






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
		id: 'goWcq6xGLCHEF4O7',
    });
    gitalk.render('gitalk-container');
</script> 

