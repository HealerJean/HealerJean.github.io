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



写这篇文件的时候不得不说，网上真的都是闲人啊，基本上是复制粘贴，然后自己也亲自试一试，简直是糟糕透了。所以博主自己开始写了



## 1、默认的JmsTemplate发送消息

```
默认的 jmsTemplate 发送queue消息（默认是持久化的）
默认的 jmsTemplate 发送topic消息（默认是非持久化的）
```




### 1.1、Maven

```xml

        <!-- activemq -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>
        
```





### 1.2、SpringBoot配置

```properties
server.port=8888


# ActiveMQ通讯地址
spring.activemq.broker-url=tcp://127.0.0.1:61616
spring.activemq.user=admin
spring.activemq.password=admin
#是否启用内存模式,开始消息持久化就必须关闭in-memory选项。
spring.activemq.in-memory=false
##信任所有的包
spring.activemq.packages.trust-al=true
#最大连接数
spring.activemq.pool.maxConnections=50
#空闲时间
spring.activemq.pool.idleTimeout=30000
# 是否替换默认的连接池，使用ActiveMQ的连接池需引入的依赖
spring.activemq.pool.enabled=false


```



### 1.3、常亮设置

```java
public class JmsConstant {

    /**  默认队列  */
    public static final String QUEUE_NAME_DEFAULT = "default_queueName";
    /**  默认名称   */
    public static final String TOPIC_NAME_DEFAULT  = "default_topicName";


    public static final String TOPIC_LISTENER_FACTORY = "topicListenerFactory";

}


```



### 1.4、ActiveMQConfig

```java
@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String jmsUser;

    @Value("${spring.activemq.password}")
    private String jsmPass;


    @Bean(name = "connectionFactory")
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = 
            new ActiveMQConnectionFactory(jmsUser, jsmPass, brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Primary
    @Bean("jmsTemplate")
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }
   


    // springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置bean
    /**
     * 发布/订阅 非持久化Topic
     */
    @Bean(JmsConstant.TOPIC_LISTENER_FACTORY)
    public JmsListenerContainerFactory topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        // 开启非持久化
        factory.setSubscriptionDurable(false);
        return factory;
    }

```





### 1.5、监听消息



```java
@Component
public class JmsMsgListener {

	//springboot默认只配置queue类型消息，默认就是点对点的，这样在监听的时候，可以不写这个工厂
    @JmsListener(destination = JmsConstant.QUEUE_NAME_DEFAULT)
    public void receivedefaultJmsTeamplateQueue(String msg) {
        System.out.println("[" + JmsConstant.QUEUE_NAME_DEFAULT + "]消息:" + msg);
    }

    @JmsListener(destination = JmsConstant.TOPIC_NAME_DEFAULT, 
                 containerFactory = JmsConstant.TOPIC_LISTENER_FACTORY)
    public void receivedefaultJmsTeamplateTopic(String msg) {
        System.out.println("[" + JmsConstant.TOPIC_NAME_DEFAULT + "]消息:" + msg);
    }
}
```



### 1.6、发送消息接口和服务

#### 1.6.1、发送消息接口



```java
public interface ProducerService {

    /**
     * 默认的 jmsTemplate 发送queue消息（默认是持久化的）
     */
    void jmsTemplateSendMsgToQueue(String destination, final String msg);
    /**
     * 默认的 jmsTemplate 发送topic消息（默认是非持久化的）
     */
    void jmsTemplateSendMsgTopTopic(String destination, String msg);

}
```



#### 5.2、 实现接口

```java
@Service
public class ProducerServiceImpl implements ProducerService {

    /**
     * 系统默认的 jmsTemplate
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 默认的 jmsTemplate 发送queue消息（默认是持久化的）
     */
    @Override
    public void jmsTemplateSendMsgToQueue(String destination, final String msg) {
        jmsTemplate.convertAndSend(new ActiveMQQueue(destination), msg);
    }
    /**
     * 默认的 jmsTemplate 发送topic消息（默认是非持久化的）
     */
    @Override
    public void jmsTemplateSendMsgTopTopic(String destination,  String msg) {
        jmsTemplate.convertAndSend(new ActiveMQTopic(destination), msg);
    }
    
}
```





### 6、测试controller

```java
@RestController
@RequestMapping("hlj/activemq")
public class ActiveMQController {

    @Autowired
    private ProducerService producerService;


    @RequestMapping("/defaultQueue")
    public String defaultQueue(String msg) {
        producerService.jmsTemplateSendMsgToQueue(JmsConstant.QUEUE_NAME_DEFAULT, msg);
        return "defaultQueue";
    }

    @RequestMapping("/defaultTopic")
    public String defaultTopic(String msg) {
        producerService.jmsTemplateSendMsgTopTopic(JmsConstant.TOPIC_NAME_DEFAULT, msg);
        return "defaultTopic";
    }
}
```



#### 1.6..1、http请求

```http
GET http://localhost:8888/hlj/activemq/defaultQueue?msg=hello
```



```http
GET http://localhost:8888/hlj/activemq/defaultTopic?msg=hello
```



## 2、自定义的JmsTemplate发送queue和topic（非持久化和持久化）



### 2.1、ActiveMQConfig

```java

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName ActiveMQConfig
 * @Date 2019/9/10  15:51.
 * @Description
 */
@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String jmsUser;

    @Value("${spring.activemq.password}")
    private String jsmPass;


    @Bean(name = "connectionFactory")
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(jmsUser, jsmPass, brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Primary
    @Bean("jmsTemplate")
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }


    /**
     * 持久化的 prsistentJmsTemplate
     */
    @Bean("persistentJmsTemplate")
    public JmsTemplate persistentJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate persistentJmsTemplate = new JmsTemplate();
        persistentJmsTemplate.setConnectionFactory(connectionFactory);
        //设置为true，deliveryMode, priority, timeToLive等设置才会起作用，否则使用默认的值
        persistentJmsTemplate.setExplicitQosEnabled(true);
        persistentJmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
        return persistentJmsTemplate;
    }

    /**
     * 非持久化的 noPrsistentJmsTemplate
     */
    @Bean("noPersistentJmsTemplate")
    public JmsTemplate noPersistentJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate persistentJmsTemplate = new JmsTemplate();
        persistentJmsTemplate.setConnectionFactory(connectionFactory);
        //设置为true，deliveryMode, priority, timeToLive等设置才会起作用，否则使用默认的值
        persistentJmsTemplate.setExplicitQosEnabled(true);
        persistentJmsTemplate.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return persistentJmsTemplate;
    }


    // springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置bean
    /**
     * 发布/订阅 非持久化Topic
     */
    @Bean(JmsConstant.TOPIC_LISTENER_FACTORY)
    public JmsListenerContainerFactory topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        // 开启非持久化
        factory.setSubscriptionDurable(false);
        return factory;
    }



    /**
     * 发布/订阅 持久化Topic
     */
    @Bean(JmsConstant.PRSISTENT_TOPIC_LISTENER_FACTORY)
    public DefaultJmsListenerContainerFactory prsistentTopicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        // 开启持久化
        factory.setSubscriptionDurable(true);
        // 设置clientId
        factory.setClientId(JmsConstant.PRSISTENT_TOPIC_NAME_CLIENT);
        return factory;
    }
}

```



### 2.2、变量设置

```java
public class JmsConstant {

    /**  默认队列  */
    public static final String QUEUE_NAME_DEFAULT = "default_queueName";
    /**  默认名称   */
    public static final String TOPIC_NAME_DEFAULT  = "default_topicName";



    /**  队列  */
    public static final String QUEUE_NAME = "queueName";
    /**  订阅名称   */
    public static final String TOPIC_NAME = "topicName";
    /**  持久化topic  */
    public static final  String PRSISTENT_TOPIC_NAME = "prsistentTopicName";
    public static final  String PRSISTENT_TOPIC_NAME_CLIENT = "Cliend_HealerJean";


    public static final String TOPIC_LISTENER_FACTORY = "topicListenerFactory";
    public static final String PRSISTENT_TOPIC_LISTENER_FACTORY = "prsistentTopicListenerFactory";


}
```





### 2.3、监听消息

```java
/**
 * 消息监听器
 */
@Component
public class JmsMsgListener {

    //默认的的JmsTeamplate发送的消息监听
    @JmsListener(destination = JmsConstant.QUEUE_NAME_DEFAULT)
    public void receivedefaultJmsTeamplateQueue(String msg) {
        System.out.println("[" + JmsConstant.QUEUE_NAME_DEFAULT + "]消息:" + msg);
    }

    @JmsListener(destination = JmsConstant.TOPIC_NAME_DEFAULT, 
                 containerFactory = JmsConstant.TOPIC_LISTENER_FACTORY)
    public void receivedefaultJmsTeamplateTopic(String msg) {
        System.out.println("[" + JmsConstant.TOPIC_NAME_DEFAULT + "]消息:" + msg);
    }



    //自定的JmsTeamplate发送的消息监听
    @JmsListener(destination = JmsConstant.QUEUE_NAME)
    public void receiveQueue(String msg) {
        System.out.println("[" + JmsConstant.QUEUE_NAME + "]消息:" + msg);
    }

    @JmsListener(destination = JmsConstant.TOPIC_NAME, 
                 containerFactory = JmsConstant.TOPIC_LISTENER_FACTORY)
    public void receiveTopicName(String msg) {
        System.out.println("[" + JmsConstant.TOPIC_NAME + "]消费者:receive=" + msg);
    }

    @JmsListener(destination = JmsConstant.PRSISTENT_TOPIC_NAME, 
                 containerFactory = JmsConstant.PRSISTENT_TOPIC_LISTENER_FACTORY)
    public void receivePrsistentTopicName(String msg) {
        System.out.println("[" + JmsConstant.PRSISTENT_TOPIC_NAME + "]消费者:receive=" + msg);
    }

}
```



### 2.4、发送消息接口和实现

#### 2.4.1、发送消息接口

```java
public interface ProducerService {

    /**
     * 默认的 jmsTemplate 发送queue消息（默认是持久化的）
     */
    void jmsTemplateSendMsgToQueue(String destination, final String msg);
    /**
     * 默认的 jmsTemplate 发送topic消息（默认是非持久化的）
     */
    void jmsTemplateSendMsgTopTopic(String destination, String msg);

    

    /**
     * 发送消息（点对点）
     */
    void sendMsgToQueue(String destination, String msg);
    /**
     * 发送消息（非持久化 发布-订阅模式）
     */
    void sendMsgToTopic(String destination, String msg);

    /**
     * 发送消息（持久化 发布-订阅模式）
     */
    void sendMsgTopPrsistentTopic(String destination, String msg);
}
```



#### 2.4.2、实现

```java
/**
 * 消息生产者服务实现类
 */
@Service
public class ProducerServiceImpl implements ProducerService {

    /**
     * 系统默认的 jmsTemplate *************************************
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 默认的 jmsTemplate 发送queue消息（默认是持久化的）
     */
    @Override
    public void jmsTemplateSendMsgToQueue(String destination, final String msg) {
        jmsTemplate.convertAndSend(new ActiveMQQueue(destination), msg);
    }
    /**
     * 默认的 jmsTemplate 发送topic消息（默认是非持久化的）
     */
    @Override
    public void jmsTemplateSendMsgTopTopic(String destination,  String msg) {
        jmsTemplate.convertAndSend(new ActiveMQTopic(destination), msg);
    }


    /**
     * 自定义的jmsTemplate *************************************
     */

    @Autowired
    @Qualifier("persistentJmsTemplate")
    private JmsTemplate persistentJmsTemplate;

    @Autowired
    @Qualifier("persistentJmsTemplate")
    private JmsTemplate noPersistentJmsTemplate;

    @Override
    public void sendMsgToQueue(String destination, final String msg) {
        persistentJmsTemplate.convertAndSend(new ActiveMQQueue(destination), msg);
    }

    @Override
    public void sendMsgToTopic(String destination, final String msg) {
        noPersistentJmsTemplate.convertAndSend(new ActiveMQTopic(destination), msg);
    }

    @Override
    public void sendMsgTopPrsistentTopic(String destination,  String msg) {
        persistentJmsTemplate.convertAndSend(new ActiveMQTopic(destination), msg);
    }

}
```



### 2.5、测试Controller

```java

@RestController
@RequestMapping("hlj/activemq")
public class ActiveMQController {

    @Autowired
    private ProducerService producerService;

    @RequestMapping("/defaultQueue")
    public String defaultQueue(String msg) {
        producerService.jmsTemplateSendMsgToQueue(JmsConstant.QUEUE_NAME_DEFAULT, msg);
        return "defaultQueue";
    }

    @RequestMapping("/defaultTopic")
    public String defaultTopic(String msg) {
        producerService.jmsTemplateSendMsgTopTopic(JmsConstant.TOPIC_NAME_DEFAULT, msg);
        return "defaultTopic";
    }




    /**
     * 点对点
     */
    @RequestMapping("/queue")
    public String queue(String msg) {
        producerService.sendMsgToQueue(JmsConstant.QUEUE_NAME, msg);
        return "queue";
    }
    /**
     * 非持久化 发布/订阅
     */
    @RequestMapping("/topic")
    public String topic(String msg) {
        producerService.sendMsgToTopic(JmsConstant.TOPIC_NAME, msg);
        return "topic";
    }
    /**
     * 持久化 发布/订阅
     */
    @RequestMapping("/persistentTopic")
    public String persistentTopic(String msg) {
        producerService.sendMsgTopPrsistentTopic(JmsConstant.PRSISTENT_TOPIC_NAME, msg);
        return "persistentTopic";
    }

}

```



#### 2.5.1、http

```http
GET http://localhost:8888/hlj/activemq/queue?msg=hello


GET http://localhost:8888/hlj/activemq/topic?msg=hello


GET http://localhost:8888/hlj/activemq/persistentTopic?msg=persistentTopic
```







​       

   



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

