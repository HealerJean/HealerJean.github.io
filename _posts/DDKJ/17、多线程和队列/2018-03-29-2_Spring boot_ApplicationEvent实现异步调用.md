---
title: SpringBoot_ApplicationEvent实现异步调用
date: 2018-03-29 18:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot_ApplicationEvent实现异步调用
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言



### 什么时候使用事件
　　首先，使用事件机制有什么好处呢？我觉得最重要的一点就是避免了在代码中到处添加相同代码的问题，而且也可以对程序进行松耦合的设计。那么我们什么时候用事件机制呢？
　　
　　第一，当有多个代码块中需要引用相同代码的时候。
　　第二，发生的这个操作或者代码不需要立即执行的，可以异步的。
<br/>
### 使用场景，
 
 在用户注册成功之后，监听，然后发送给用户邮件
### 详解原理

ApplicationEvent以及Listener是Spring为我们提供的一个事件监听、订阅的实现
* 内部实现原理是观察者设计模式，设计初衷也是为了系统业务逻辑之间的解耦，提高可扩展性以及可维护性。
* 事件发布者并不需要考虑谁去监听，监听具体的实现内容是什么，发布者的工作只是为了发布事件而已

<br/>
看完下面的时候，你可能会想到拦截器，但是你想多了，拦截器一般是拦截url请求，可以读取reques的信息。我们这里，只会读取我们自己注册的事件

## 1、开始注册并发布一个事件

### 1.1、要被事件监听的bean
将来这个就是事件发生以后我们获取的对象

```
package com.hlj.applicationevent.ApplicationEvent.Bean;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:12.
 */
public class UserBean {
    //用户名
    private String name;
    //密码
    private String password;

    public UserBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


```

### 1.2、添加一个要注册的事件，将来在service执行时事件的时候被注册

解释：
1、集成ApplicationEvent ，创建注册事件
2、下面的构造器可以添加事件的多个对象

```
public UserRegisterEvent(Object source,UserBean user,String other ……) {}
```

```
package com.hlj.applicationevent.ApplicationEvent;

import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import org.springframework.context.ApplicationEvent;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:06.
 *
 *ApplicationEvent以及Listener是Spring为我们提供的一个事件监听、订阅的实现
 * 内部实现原理是观察者设计模式，设计初衷也是为了系统业务逻辑之间的解耦，提高可扩展性以及可维护性。
 * 事件发布者并不需要考虑谁去监听，监听具体的实现内容是什么，发布者的工作只是为了发布事件而已。
 */
public class UserRegisterEvent extends ApplicationEvent {

    //注册用户对象
    private UserBean user;
    private  String other;

    /**
     * 重写构造函数
     * @param source 发生事件的对象,在那个service中发送事件
     * @param user 注册用户对象,other也是，other是我自己加上的
     *  其中source参数指的是发生事件的对象，一般我们在发布事件时使用的是this关键字代替本类对象，
     *             而user参数是我们自定义的注册用户对象，该对象可以在监听内被获取。
     */
    public UserRegisterEvent(Object source,UserBean user,String other) {
        super(source);
        this.user = user;
        this.other = other;

    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }
}

```

### 1.3、Service发送事件，开始发布事件，并注册

解释：使用applicationContext 上下文对象进行发布事件，并注册成功


```
package com.hlj.applicationevent.ApplicationEvent.service;


import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import com.hlj.applicationevent.ApplicationEvent.UserRegisterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:13.
 * UserService内添加一个注册方法，该方法只是实现注册事件发布功能
 */
@Service
public class UserService
{
    @Autowired
    ApplicationContext applicationContext;

    /**
     * 用户注册方法
     * @param user
     */
    public void register(UserBean user)
    {
        //../省略其他逻辑

        //发布UserRegisterEvent事件
        applicationContext.publishEvent(new UserRegisterEvent(this,user,"HealerJean"));
    }
}

```

### 1.4、controller中开始调用执行这个事件


```
package com.hlj.applicationevent.ApplicationEvent.controller;


import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import com.hlj.applicationevent.ApplicationEvent.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:14.
 */
@RestController
public class UserController
{
    //用户业务逻辑实现
    @Autowired
    private UserService userService;

    /**
     * 注册控制方法
     * @param user 用户对象
     * @return
     */
    @RequestMapping(value = "/register")
    public String register ( UserBean user) {
        //调用注册业务逻辑
        userService.register(user);
        return "注册成功.";
    }
}

```

## 2、监听事件

### 1、注解的方式监听事件

解释：@EventListener 注解的方法中传入的是被注册的事件，通过这个被注册的事件可以获取关注对象的一些信息
 
```
package com.hlj.applicationevent.ApplicationEvent.Listener;


import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import com.hlj.applicationevent.ApplicationEvent.UserRegisterEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:15.
 */
@Component
public class AnnotationRegisterListener {

    /**
     * 注册监听实现方法
     * @param userRegisterEvent 用户注册事件
     */
    @EventListener
    public void register(UserRegisterEvent userRegisterEvent)
    {
        //获取注册用户对象
        UserBean user = userRegisterEvent.getUser();
        String other = userRegisterEvent.getOther();

        //../省略逻辑

        //输出注册用户信息
        System.out.println("@EventListener注册信息，用户名："+user.getName()+"，密码："+user.getPassword());
        System.out.println("@EventListener注册信息，other："+other);

    }
}

```
#### 1、浏览器打开测试
[http://127.0.0.1:8080/register?name=admin&password=123456](http://127.0.0.1:8080/register?name=admin&password=123456)

![WX20180329-175321@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180329-175321@2x.png)

#### 2、观察控制台，有对应的输出

### 2、实现接口ApplicationListener实现监听

解释：在继承的对象中添加泛型，（注册对象）

```
package com.hlj.applicationevent.ApplicationEvent.Listener;


import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import com.hlj.applicationevent.ApplicationEvent.UserRegisterEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:24.
 */
@Component
public class RegisterListener implements ApplicationListener<UserRegisterEvent>
{
    /**
     * 实现监听
     * @param userRegisterEvent
     */
    @Override
    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
        //获取注册用户对象
        UserBean user = userRegisterEvent.getUser();

        //../省略逻辑

        //输出注册用户信息
        System.out.println("注册信息，用户名："+user.getName()+"，密码："+user.getPassword());
    }
}

```
#### 1、这里就不进行测试了，很简单，是吧

## 3、SmartApplicationListener实现有序监听

1、首先我们可能会有两种操作，一种是监听到，存入数据库中，另外一种是监听到发送邮件。那么好了，这样就会有顺序产生，肯定是先存到数据库，再发送邮件了。其实二者类一波一样，只不过是order顺序不一样而已，order越小越先执行
<br/>
2、可以针对特定的注册事件`UserRegisterEvent`
3、可以针对特定的发生事件`service` 
只有2、3成功匹配才能够对监听的对象进行获取数据

#### 3.1、注册成功，数据入库(模拟而已) order 为0

```
package com.hlj.applicationevent.ApplicationEvent.Listener.NiuBi;


import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import com.hlj.applicationevent.ApplicationEvent.UserRegisterEvent;
import com.hlj.applicationevent.ApplicationEvent.service.UserService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:27.
 * SmartApplicationListener实现有序监听
我们对注册用户以及发送邮件的监听重新编写，注册用户写入数据库监听代码如下所示：
 */
@Component
public class UserRegisterListener implements SmartApplicationListener
{
    /**
     *  该方法返回true&supportsSourceType同样返回true时，才会调用该监听内的onApplicationEvent方法
     * @param aClass 接收到的监听事件类型
     * @return
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        //只有UserRegisterEvent监听类型才会执行下面逻辑
        return aClass == UserRegisterEvent.class;
    }

    /**
     *  该方法返回true&supportsEventType同样返回true时，才会调用该监听内的onApplicationEvent方法
     * @param aClass
     * @return
     */
    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        //只有在UserService内发布的UserRegisterEvent事件时才会执行下面逻辑
        return aClass == UserService.class;
    }

    /**
     *  supportsEventType & supportsSourceType 两个方法返回true时调用该方法执行业务逻辑
     * @param applicationEvent 具体监听实例，这里是UserRegisterEvent
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {

        //转换事件类型
        UserRegisterEvent userRegisterEvent = (UserRegisterEvent) applicationEvent;
        //获取注册用户对象信息
        UserBean user = userRegisterEvent.getUser();
        //.../完成注册业务逻辑
        System.out.println("注册信息，用户名："+user.getName()+"，密码："+user.getPassword());
    }

    /**
     * 同步情况下监听执行的顺序
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}

```


### 3.2 、上面操作完事之后，要进行发送邮件，只是下面的order不一样而已


```
package com.hlj.applicationevent.ApplicationEvent.Listener.NiuBi;


import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import com.hlj.applicationevent.ApplicationEvent.UserRegisterEvent;
import com.hlj.applicationevent.ApplicationEvent.service.UserService;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/29  下午5:30.
 */
@Component
public class UserRegisterSendMailListener implements SmartApplicationListener
{
    /**
     *  该方法返回true&supportsSourceType同样返回true时，才会调用该监听内的onApplicationEvent方法
     * @param aClass 接收到的监听事件类型
     * @return
     */
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        //只有UserRegisterEvent监听类型才会执行下面逻辑
        return aClass == UserRegisterEvent.class;
    }

    /**
     *  该方法返回true&supportsEventType同样返回true时，才会调用该监听内的onApplicationEvent方法
     * @param aClass
     * @return
     */
    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        //只有在UserService内发布的UserRegisterEvent事件时才会执行下面逻辑
        return aClass == UserService.class;
    }

    /**
     *  supportsEventType & supportsSourceType 两个方法返回true时调用该方法执行业务逻辑
     * @param applicationEvent 具体监听实例，这里是UserRegisterEvent
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //转换事件类型
        UserRegisterEvent userRegisterEvent = (UserRegisterEvent) applicationEvent;
        //获取注册用户对象信息
        UserBean user = userRegisterEvent.getUser();
        System.out.println("用户："+user.getName()+"，注册成功，发送邮件通知。");
    }

    /**
     * 同步情况下监听执行的顺序
     * @return
     */
    @Override
    public int getOrder() {
        return 1;
    }
}

```

## [代码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_03_29_2_Spring%20boot_ApplicationEvent%E5%AE%9E%E7%8E%B0%E5%BC%82%E6%AD%A5%E8%B0%83%E7%94%A8/com-hlj-applicationevent.zip)



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
		id: 'mVdXVUatKKBPA9Tl',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

