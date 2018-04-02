---
title: 1、netty第一篇HelloWord
date: 2018-03-27 17:33:00
tags: 
- Netty
category: 
- Netty
description: netty第一篇HelloWord之与服务器聊天
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

　　Netty是一个异步通信、事件驱动基于NIO编写的高性能高并发的java网络编程框架。下面通过一个简单的服务器应答程序来完成Netty的初步学习。

## 1、netty服务端 <br/>

### 1.1、netty服务端启动类


```
package com.lc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
/**
 * @Description  netty服务端
 * @Date   2018/3/27 下午4:16.
 */

public class HelloServer {

	private static final int protNumner = 7878;

	public static void main(String[] args) throws InterruptedException {
		// 事件 循环 组
		/*
		 * Main函数开始的位置定义了两个工作线程，一个命名为WorkerGroup，另一个命名为BossGroup。
		 * 都是实例化NioEventLoopGroup。这一点和3.x版本中基本思路是一致的。Worker线程用于管理线程为Boss线程服务。
		 * EventLoopGroup，它是4.x版本提出来的一个新概念。类似于3.x版本中的线程。用于管理Channel连接的。
		 * 在main函数的结尾就用到了EventLoopGroup提供的便捷的方法，shutdownGraceFully()，
		 * 翻译为中文就是优雅的全部关闭。
		 */
		//NioEventLoopGroup可以理解为一个线程池，内部维护了一组线程，每个线程负责处理多个Channel上的事件，
		// 而一个Channel只对应于一个线程，这样可以回避多线程下的数据同步问题。

		EventLoopGroup workerGroup = new NioEventLoopGroup();
		EventLoopGroup bossGroup = new NioEventLoopGroup();

		/* ServerBootstrap负责初始化netty服务器，并且开始监听端口的socket请求 */
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup);
		b.channel(NioServerSocketChannel.class); //通道的业务处理程序
		b.childHandler(new HelloServerInitializer());//通道的初始化程序

		// 服务器绑定端口监听
		ChannelFuture f = b.bind(protNumner).sync();
		// 监听服务器关闭监听
		f.channel().closeFuture().sync();
	}
}

```

### 1.2、channel的初始化程序

主要作用是装配通道处理器HelloServerHandler


```
package com.lc.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author HealerJean
 * @Description 通道的初始化类，规定通道的定界符、编码器、解码器
 * @Date   2018/3/27 下午4:02.
 */


public class HelloServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		// 以("\n")为结尾分割的 解码器，最大帧长度为8192
		// Delimiter Based Frame Decoder 基于定界符的帧解码器
		// Delimiters.lineDelimiter()行分隔符
		// 这里使用时，会把换行符作为一个消息的分隔界限使用，即接收时，换行符前为一条消息
		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

		// 字符串解码 和 编码
		// encoder 编码器， decoder 解码器
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());

		// 自己的逻辑Handler
		pipeline.addLast("handler", new HelloServerHandler());
	}
}

```

### 1.3、通道处理器


```
package com.lc.server;

import java.net.InetAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * netty客户端
 *
 * @author HealerJean
 * @date 2017年6月26日 下午5:37:23
 *  通道入站处理器
 */

public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		// 收到消息直接打印输出
		System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);

		// 返回客户端信息 - 我已经接收到了你的消息
		// writeAndFlush写消息并发送 给客户端
		ctx.writeAndFlush("received your message !\n");
	}


	// 当Channel变成活跃状态时被调用 (客户端刚刚启动的时候调用)
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//打印内容 RamoteAddress : /127.0.0.1:49335 active !
		System.out.println("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

		//这个是向客户端打印
		ctx.writeAndFlush("welcome to " + InetAddress.getLocalHost().getHostName() + " server.\n");

		super.channelActive(ctx);
	}
}

```

## 1、客户端<br/>

### 1.1、客户端启动类


```
package com.lc.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty客户端
 * 
 * @author HealerJean
 * @date 2017年6月26日 下午5:37:23
 * 
 */


public class HelloClient {

	public static String host = "127.0.0.1";
	public static int port = 7878;

	public static void main(String[] args) throws IOException, InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(new HelloClientInitializer());

		// 连接服务端
		Channel ch = b.connect(host, port).sync().channel();

		// 控制台输入
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		for (;;) {
			String line = in.readLine();
			if (line == null)
				continue;
			ch.writeAndFlush(line + "\r\n");
		}
	}
}


```

### 2.2、客户端初始化程序


```
package com.lc.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class HelloClientInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();

		pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());

		// 客户端的逻辑
		pipeline.addLast("handler", new HelloClientHandler());
	}
}


```

### 2.3、客户端处理器


```
package com.lc.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HelloClientHandler extends SimpleChannelInboundHandler<String> {

	//客户端读取控制台内容的时候打印
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		//msg为从服务端发来的消息
		System.out.println("Server say : " + msg);
	}
	//客户端开启的时候打印
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client active ");
		super.channelActive(ctx);
	}
	//客户端关闭的时候打印
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client close ");
		super.channelInactive(ctx);
	}
}

```

## 3、开始测试 <br/>
### 3.1、启动服务端，服务端什么都不输出

![WX20180327-162732](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180327-162732.png)


### 3.2、启动客户端

#### 3.2.1、客户端控制台

解释：根据顺序
1、先打印自己客户端的东西，channelActive
2、再打印服务端传来的的东西。channelActive （服务端收到激活）

![WX20180327-162823](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180327-162823.png)

#### 3.2.2 、观察服务端

1、服务端激活channelActive
 

![WX20180327-164024](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180327-164024.png)



### 3.3、客户端书输入数据 HealerJean

#### 3.3.1、观察客户端 

1、接收服务端传来的信息 channelRead0

![WX20180327-165117](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180327-165117.png)


#### 3.3.2、观察服务端

2、接收客户端传来的信息channelRead0

![WX20180327-165446](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180327-165446.png)





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
		id: 'AAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

