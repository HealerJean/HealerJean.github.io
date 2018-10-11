---
title: 2、netty_websocket_redisTemplate_thread
date: 2018-03-30 16:33:00
tags: 
- Netty
- Redis
- Thread
category: 
- Netty
- Thread
- Redis
description: netty_websocket_redisTemplate_thread
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言


兄弟们，只想说，这一篇很重要，博主一开始也不是特别理解。如果说回了这个，我觉得你已经可以成为一个真正的程序员了



## 1、redis配置


### 1、 redis配置文件

需要注意的是，这里面我配置了rdis监听，也就是说我们的客户端用户进行交互其实是从redis来的


```xml
<!--配置监听队列-->
<bean id="requestMessageListener" class="com.hlj.netty.websocket.topic.RequestMessageListener"/>

<redis:listener-container>
    <redis:listener ref="requestMessageListener" topic="request" />
</redis:listener-container>

```

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:redis="http://www.springframework.org/schema/redis"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/redis http://www.springframework.org/schema/redis/spring-redis.xsd">


    <bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig" >
        <property name="maxTotal" value="${hlj.redis.max-total}"/>
        <property name="maxIdle" value="${hlj.redis.max-idle}"/>
        <property name="maxWaitMillis" value="${hlj.redis.pool.max-wait}"/>
        <!-- 永远不要加testOnBorrow 或 testOnReturn这类，不然你会后悔的 -->
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
            <bean class="com.hlj.netty.websocket.redis.cacheSerializer.CustomStringRedisSerializer"/>
        </property>
        <property name="valueSerializer">
            <bean class="com.hlj.netty.websocket.redis.cacheSerializer.CustomJSONStringRedisSerializer"/>
        </property>
    </bean>



    <!--配置监听队列-->
    <bean id="requestMessageListener" class="com.hlj.netty.websocket.topic.RequestMessageListener"/>

    <redis:listener-container>
        <redis:listener ref="requestMessageListener" topic="request" />
    </redis:listener-container>


</beans>

```

### 2、redis监听进行发送消息

**其实通过一个章节的讲解，我们应该是已经知道，下面的操作应该就是用来发送消息的**<br/>

```java
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
        String channelId = ClientChannelRelation.getChannelId(convertBean.getToUid());

        if (channelId != null) {
            Channel clientChannel = ChannelSelector.getChannel(channelId);
            if (clientChannel != null) {
                clientChannel.writeAndFlush(new TextWebSocketFrame(convertBean.getContent()));
            }
        }
    }
}


```
### 3、redis配置文件


```yml
#logging
server.port2=9090
server.port=9091



########################################################
###REDIS (RedisProperties) redis
########################################################
hlj.redis.host-name=127.0.0.1
hlj.redis.password=
hlj.redis.max-total=64
hlj.redis.max-idle=30
hlj.redis.port=6379
hlj.redis.pool.max-wait=-1

```

## 2、main函数开始调用运行一个netty服务

> 这里会发现有个一步的调用，重点就在这里，这里的异步调用，会出现，一直在等待，时刻接收消息队列。

```java
package com.hlj.netty.websocket;

import com.hlj.netty.websocket.executor.AsyncExecutor;
import com.hlj.netty.websocket.server.TransferServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 类描述：
 * 创建人： j.sh
 * 创建时间： 2016/9/9
 * version：1.0.0
 */
@SpringBootApplication
@ImportResource(value = "classpath:applicationContext.xml")
@EnableAsync
public class ComHljNettyWebsocketApplication implements CommandLineRunner {

	@Value("${server.ssl:false}")
	private boolean ssl;

	@Value("${server.port2}")
	private int port;

	public static void main(String[] args) {
		SpringApplication.run(ComHljNettyWebsocketApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		// start async executor
		AsyncExecutor.start();

		// start websocket
		final SslContext sslCtx;//如果是https 的，添加 ，如果没有则删除相关代码即可
		if (ssl) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} else {
			sslCtx = null;
		}

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
			bootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new TransferServerInitializer(sslCtx));

			Channel ch = bootstrap.bind(port).sync().channel();
			ch.closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}

```


## 3、AsyncExecutor 异步调用

### 3.1、LinkedBlockingQueue是一个线程安全的阻塞队列，实现了先进先出等特性，是作为生产者消费者的首选，

### 3.2、下面还使用了两个线程池，说实话，现在还不太明白这两个线程池子的作用，先这么的，慢慢来

### 3.3 、我们主要是通过下面的redisTemplate进行数据的传输

当调用下面的传递并且发送的时候，会自动进入redis配置的监听，并且发送给Bean对象中的to

```java
redisTemplate.convertAndSend("request",new ConvertBean(toUid,content));
requestBean.getPromise().setSuccess("");

```
```java
package com.hlj.netty.websocket.executor;


import com.hlj.netty.websocket.bean.ConvertBean;
import com.hlj.netty.websocket.bean.RequestBean;
import com.hlj.netty.websocket.helper.SpringHelper;
import com.hlj.netty.websocket.selector.ClientChannelRelation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by j.sh on 27/11/2017.
 */
public class AsyncExecutor {

    private static Logger logger = LoggerFactory.getLogger(AsyncExecutor.class);

    private static ExecutorService pool = Executors.newFixedThreadPool(1);
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static LinkedBlockingQueue<RequestBean> blockingQueue = new LinkedBlockingQueue<>();


    public static void offerQueue(RequestBean requestBean){
        blockingQueue.offer(requestBean); //能放就放，不能放拉倒
    }

    public static void start(){
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(" pool.execute(new Runnable() {");
                while (true) {
                    try {
                        final RequestBean requestBean = blockingQueue.take(); //脾气好，一直等东西
                        executorService.execute(new Runnable() {

                            @Override
                            public void run() {
                                System.out.println("线程的id我主要观察这里的线程有没有发生变化"+Thread.currentThread().getId());
                                System.out.println(" executorService.execute(new Runnable() {");
                                try {
                                    RedisTemplate redisTemplate = (RedisTemplate) SpringHelper.getBeanByName("redisTemplate");
                                    JSONObject jsonObject = JSONObject.fromObject(requestBean.getRequest());

                                    System.out.println(requestBean.getRequest());
                    //初始化状态，添加通道
                                    if (jsonObject.containsKey("init")) {
                                        //init
                                        String uid = jsonObject.getString("uid");
                                        ClientChannelRelation.addRelation(uid,requestBean.getChannel());
                                    }
                     //添加通道，对话
                                    else {
                                        //content
                                        String toUid = jsonObject.getString("to");
                                        String content = jsonObject.getString("content");

                                        redisTemplate.convertAndSend("request",new ConvertBean(toUid,content));
                                        requestBean.getPromise().setSuccess("");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    requestBean.getPromise().setFailure(e);
                                }
                            }
                        });
                    } catch (Exception e) {
                        logger.error(e.getMessage(),e);
                    }
                }
            }
        });
    }
}


```


### 4、handler 处理

> 这里的hander和上一篇的作用不太一样，以为上一篇是直接在这就进行了发送，这里不一样，这里重点介绍下netty中给我们提供的DefaultPromise，。设置了一个监听，如果promise 一旦发送改变就会调用下面的结果，用来给用户返回异常信息

```java
//它判断异步任务执行的状态，如果执行完成，就理解通知监听者，否则加入到监听者队列
DefaultPromise<String> promise = new DefaultPromise<>(ctx.executor()) ;
promise.addListener(new PromiseNotifier<String,DefaultPromise<String>>(){
    @Override
    public void operationComplete(DefaultPromise<String> future) throws Exception {
        if (!future.isSuccess()) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.fromObject(ResponseBean.buildFailure(future.cause().getMessage())).toString()));
        }
    }
});
AsyncExecutor.offerQueue(new RequestBean(request,ctx.channel(),promise));

```




```java
package com.hlj.netty.websocket.handler;


import com.hlj.netty.websocket.bean.RequestBean;
import com.hlj.netty.websocket.bean.ResponseBean;
import com.hlj.netty.websocket.executor.AsyncExecutor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.PromiseNotifier;
import net.sf.json.JSONObject;

/**
 * Created by j.sh on 27/11/2017.
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            final String request = ((TextWebSocketFrame) frame).text();

            //它判断异步任务执行的状态，如果执行完成，就理解通知监听者，否则加入到监听者队列
            DefaultPromise<String> promise = new DefaultPromise<>(ctx.executor()) ;
            promise.addListener(new PromiseNotifier<String,DefaultPromise<String>>(){
                @Override
                public void operationComplete(DefaultPromise<String> future) throws Exception {
                    if (!future.isSuccess()) {
                        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONObject.fromObject(ResponseBean.buildFailure(future.cause().getMessage())).toString()));
                    }
                }
            });
            AsyncExecutor.offerQueue(new RequestBean(request,ctx.channel(),promise));
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }
}


```


## [代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_03_30_2_netty_websocket_redisTemplate_thread/com-hlj-netty-websocket-thread-redis.zip)


<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




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
		id: 'niCzebtxRmbQX24p',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

