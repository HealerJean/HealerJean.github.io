---
title: ActiveMq的Message属性_定时_groupMessage_Selector
date: 2018-01-01 03:33:00
tags: 
- MQ
category: 
- MQ
description: ActiveMq的Message属性_定时_groupMessage_Selector
---





**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)    



## 1、Message时间设置



| Property name        | **type** | **description**         |
| -------------------- | -------- | ----------------------- |
| AMQ_SCHEDULED_DELAY  | long     | 延迟投递的时间 毫秒     |
| AMQ_SCHEDULED_PERIOD | long     | 重复投递的时间间隔 毫秒 |
| AMQ_SCHEDULED_REPEAT | int      | 重复投递次数            |
| AMQ_SCHEDULED_CRON   | String   | Cron表达式              |



### 1.1、生产者 

```java
public class Producer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "queue";

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
            //延迟3秒
            Long delay = 3L * 1000L;
            //周期3秒
            Long period = 3L * 1000L;
            //重复5次 消费者受到的为 1（不重复的）  + 5 = 6
            int repeat = 5;
            TextMessage message = session.createTextMessage("MESSAGE消息");
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
            message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
            producer.send(message);
    }


}

```



### 1.2、消费者

```java

public class Consumer {

    public static final String QUEUE_NAME = "queue";

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

            // 消息的目的地;消息发送给谁.
            Destination destination = session.createQueue(QUEUE_NAME);


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
                System.out.println(System.currentTimeMillis()+"收到消息" + message.getText());
            } else {
                break;
            }
        }
    }

}
```







## 2、分组Message



### 2.1、生产者 

```java

public class Producer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "FirstQueue";
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
            sendGroupMessage(session, producer);


            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


  
    private static void sendGroupMessage(Session session, MessageProducer producer) throws JMSException {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("groupA--" + i);
            message.setStringProperty("JMSXGroupID", "GroupA");
            producer.send(message);

            TextMessage message2 = session.createTextMessage("groupB--" + i);
            message2.setStringProperty("JMSXGroupID", "GroupB");
            producer.send(message2);
        }
    }


}


```



### 2..2、消费者 

**2个消费者**

```java
public class Consumer {

    public static final String QUEUE_NAME = "FirstQueue";
    public static final Long WITE_TIME = (100L * 1000L);
    public static final int COUNT = 2;


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

            // 消息的目的地;消息发送给谁.
            for (int i = 1; i <= COUNT; i++) {
                Destination destination = session.createQueue(QUEUE_NAME);
                //根据目的地获取一个消费者
                MessageConsumer consumer = session.createConsumer(destination);
                int size = i;
                consumer.setMessageListener(msg -> {
                    try {
                        TextMessage txtMsg = (TextMessage) msg;
                        System.out.println("消费者" + size + txtMsg.getText());
                    } catch (JMSException e) {
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

```



### 2..3、运行消费者，再运行生产者 ,发现消费者1 接收组A ，消费者2接收组B，自动分工 



```java
消费者1groupA--1
消费者2groupB--1
消费者1groupA--2
消费者2groupB--2
消费者1groupA--3
消费者2groupB--3
消费者1groupA--4
消费者2groupB--4
消费者1groupA--5
消费者2groupB--5
```



### 2..4、如果只创建一个组，两个消费者，则另一个消费者休息

```java
private static void sendGroupMessage(Session session, MessageProducer producer) 
    throws JMSException {
    for (int i = 1; i <= SEND_NUMBER; i++) {
        TextMessage message = session.createTextMessage("groupA--" + i);
        message.setStringProperty("JMSXGroupID", "GroupA");
        producer.send(message);

    }
}
```



```java
消费者1groupA--1
消费者1groupA--2
消费者1groupA--3
消费者1groupA--4
消费者1groupA--5
```



### 2..5、如果没有组，则两个消费者，都会消费



```java
private static void sendGroupMessage(Session session, MessageProducer producer) 
    throws JMSException {
    for (int i = 1; i <= SEND_NUMBER; i++) {
        TextMessage message = session.createTextMessage("groupA--" + i);
        producer.send(message);
    }
}
```



```java
消费者2groupA--1
消费者1groupA--2
消费者2groupA--3
消费者1groupA--4
消费者2groupA--5
```



## 3、Selector



### 3.1、生产者发送数据

```java
public class Producer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "FirstQueue";
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


    private static void sendTextMessage(Session session, MessageProducer producer) throws JMSException {
        for (int i = 0; i < 3; i++) {
            TextMessage message = session.createTextMessage("messageAA--" + i);
            message.setIntProperty("age", 23);
            producer.send(message);
        }
    }


}

```

### 3.2、消费者接收

```java
public class Consumer {

    public static final String QUEUE_NAME = "FirstQueue";
    public static final Long WITE_TIME = (100L * 1000L);
    public static final int COUNT = 2;


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

            // 消息的目的地;消息发送给谁.
            Destination destination = session.createQueue(QUEUE_NAME);
            //根据目的地获取一个消费者
            MessageConsumer consumer = session.createConsumer(destination, "age>24");
            consumer.setMessageListener(msg -> {
                try {
                    TextMessage txtMsg = (TextMessage) msg;
                    System.out.println("消费者" + txtMsg.getText());
                } catch (JMSException e) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

```



### 3.3、测试运行，确实没有收到数据





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
		id: 'oawRMLW9lQVHYshX',
    });
    gitalk.render('gitalk-container');
</script> 

