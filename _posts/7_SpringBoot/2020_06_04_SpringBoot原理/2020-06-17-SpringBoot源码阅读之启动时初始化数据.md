---
title: SpringBoot源码阅读之启动时初始化数据
date: 2020-06-17 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot源码阅读之启动时初始化数据
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



在我们用 springboot 搭建项目的时候，有时候会碰到在项目启动时初始化一些操作的需求 ，针对这种需求 spring boot为我们提供了以下几种方案供我们选择:     

1、`ApplicationRunner` 与 `CommandLineRunner `接口        

2、Spring容器初始化时`InitializingBean`接口和`@PostConstruct`      

3、Spring的事件机制



# 1、SpringBoot启动时初始化数据

## 1.1、`ApplicationRunner`与`CommandLineRunner`

> 我们可以实现 `ApplicationRunner `或 `CommandLineRunner `接口， 这两个接口工作方式相同，都只提供单一的`run`方法，该方法在`SpringApplication.run(…)`完成之前调用，不知道大家还对我上一篇文章结尾有没有印象，我们先来看看这两个接口     
>
> 这两个接口的不同之处在于：`ApplicationRunne`r中`run`方法的参数为`ApplicationArguments`，而`CommandLineRunner`接口中`run`方法的参数为`String`数组。，`CommandLineRunner`的参数是最原始的参数，没有进行任何处理，`ApplicationRunner`的参数是`ApplicationArguments`,是对原始参数的进一步封装    



```java
public interface ApplicationRunner {
    void run(ApplicationArguments var1) throws Exception;
}

public interface CommandLineRunner {
    void run(String... var1) throws Exception;
}
```



### 1.1.1、示例  

> 1、要使用**`@Component`**将实现类加入到Spring容器中，**为什么要这样做我们待会再看，然后实现其run方法实现自己的初始化数据逻辑就可以了**       
>
> 2、通过Order注解或者使用Ordered接口来指定调用顺序， `@Order() `中的值越小，优先级越高（二者进行统一排序，具体看后面的源码解析）       



```java
@Order(1)
@Slf4j
@Component
public class CustomApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("CustomApplicationRunner implements ApplicationRunner--------run方法");
    }
}

```



```java
@Slf4j
@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("CustomCommandLineRunner implements CommandLineRunner--------run方法");
    }
}

```



### 1.1.2、源码解析

> SpringBoot 启动的run方法，在最后的时候调用`callRunners(context, applicationArguments);`    

```java
callRunners(context, applicationArguments);
```

```java
	public ConfigurableApplicationContext run(String... args) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		ConfigurableApplicationContext context = null;
		Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
		configureHeadlessProperty();
		SpringApplicationRunListeners listeners = getRunListeners(args);
		listeners.starting();
		try {
			ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);
			ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
			configureIgnoreBeanInfo(environment);
			Banner printedBanner = printBanner(environment);
			context = createApplicationContext();
			exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
					new Class[] { ConfigurableApplicationContext.class }, context);
			prepareContext(context, environment, listeners, applicationArguments, printedBanner);
			refreshContext(context);
			afterRefresh(context, applicationArguments);
			stopWatch.stop();
			if (this.logStartupInfo) {
				new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
			}
			listeners.started(context);
			callRunners(context, applicationArguments);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, listeners);
			throw new IllegalStateException(ex);
		}

		try {
			listeners.running(context);
		}
		catch (Throwable ex) {
			handleRunFailure(context, ex, exceptionReporters, null);
			throw new IllegalStateException(ex);
		}
		return context;
	}
```

 

> 执行Runners 方法，很明显，是直接从Spring容器中获取**`ApplicationRunner`和`CommandLineRunner`的实例，并调用其`run`方法，这也就是为什么我要使用`@Component`将`ApplicationRunner`和`CommandLineRunner`接口的实现类加入到Spring容器中了。   



```java
	private void callRunners(ApplicationContext context, ApplicationArguments args) {
		List<Object> runners = new ArrayList<>();
        //获取容器中所有的ApplicationRunner的Bean实例
		runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
        
       //获取容器中所有的CommandLineRunner的Bean实例
		runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
        
        //对Bean实例进行排序
		AnnotationAwareOrderComparator.sort(runners);
		for (Object runner : new LinkedHashSet<>(runners)) {
			if (runner instanceof ApplicationRunner) {
                
             //执行ApplicationRunner的run方法
				callRunner((ApplicationRunner) runner, args);
			}
            
            //执行CommandLineRunner的run方法
			if (runner instanceof CommandLineRunner) {
				callRunner((CommandLineRunner) runner, args);
			}
		}
	}
```





## 2、`InitializingBean `

> 在spring初始化bean的时候，如果bean实现了 `InitializingBean `接口，在对象的所有属性被初始化后之后才会调用`afterPropertiesSet()`方法。所以这个在上面的Runner之前进行调用   

```java
public interface InitializingBean {

	void afterPropertiesSet() throws Exception;

}
```



### 2.1.1、示例

```java
@Slf4j
@Component
public class CustomInitializingBean  implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("CustomInitializingBean  implements InitializingBean --------afterPropertiesSet()方法");
    }
}

```



### 2.1.2、源码解析（回头看）



## 1.3、`@PostConstruct`

> 只用在方法上添加**@PostConstruct注解，**并将类注入到Spring容器中就可以了。我们来看看**@PostConstruct注解的方法是何时执行的**
>
> 在`Spring`初始化`bean`时，对`bean`的实例赋值时，`populateBean`方法下面有一个`initializeBean(beanName, exposedObject, mbd)`方法，这个就是用来执行用户设定的初始化操作。我们看下方法体：    



### 1.3.1、示例

```java
@Slf4j
@Component
public class CustomInitializingBean  implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("CustomInitializingBean  implements InitializingBean --------afterPropertiesSet()方法");
    }
}
```



### 1.3.2、源码解析（回头看）

首先检测当前 bean 是否实现了 InitializingBean 接口，如果实现了则调用其 `afterPropertiesSet()`，然后再检查是否也指定了 `init-method()`，如果指定了则通过反射机制调用指定的 `init-method()`。    

我们也可以发现**@PostConstruct会在实现 InitializingBean 接口的afterPropertiesSet()方法之后执行**   





## 1.4、事件机制



### 1.4.1、示例

#### 1.4.1.1、要被事件监听的bean

> 将来这个就是事件发生以后我们获取的对象

```java
package com.hlj.applicationevent.ApplicationEvent.Bean;

@Data
public class UserBean {
    //用户名
    private String name;
    //密码
    private String password;

}


```

#### 1.4.1.2、定义事件  `UserRegisterEvent`

> ApplicationEvent以及Listener是Spring为我们提供的一个事件监听、订阅的实现，内部实现原理是观察者设计模式，设计初衷也是为了系统业务逻辑之间的解耦，提高可扩展性以及可维护性。事件发布者并不需要考虑谁去监听，监听具体的实现内容是什么，发布者的工作只是为了发布事件而已。       
>
> 
>
> 1、集成ApplicationEvent ，创建注册事件    
>
> 2、下面的构造器可以添加事件的多个对象



```java
@Data
public class UserRegisterEvent extends ApplicationEvent {

    //注册用户对象
    private UserBean user;
    private String other;

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
}

```



#### 1.4.1.3、`applicationContext` 进行发布事件

>  解释：使用applicationContext 上下文对象进行发布事件，并注册成功


```java

@Service
public class UserService
{
    @Autowired
    ApplicationContext applicationContext;

    /**
     * 用户注册方法
     * @param user
     */
    public void register(UserBean user){
        //../省略其他逻辑

        //发布UserRegisterEvent事件
        applicationContext.publishEvent(new UserRegisterEvent(this,user,"HealerJean"));
    }
}

```

#### 1.4.1.4、监听事件  

##### 1.4.1.4.1、注解的方式监听事件

> 解释：@EventListener 注解的方法中传入的是被注册的事件，通过这个被注册的事件可以获取关注对象的一些信息  



```java
package com.hlj.applicationevent.ApplicationEvent.Listener;


import com.hlj.applicationevent.ApplicationEvent.Bean.UserBean;
import com.hlj.applicationevent.ApplicationEvent.UserRegisterEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class AnnotationRegisterListener {

 
    @EventListener
    public void register(UserRegisterEvent userRegisterEvent){
        
        UserBean user = userRegisterEvent.getUser();
        String other = userRegisterEvent.getOther();
        
        //../省略逻辑

    }
}

```



##### 1.4.1.4.2、实现接口ApplicationListener实现监听

```java

@Component
public class RegisterListener implements ApplicationListener<UserRegisterEvent>{

    @Override
    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
       
        //获取注册用户对象
        UserBean user = userRegisterEvent.getUser();

        //../省略逻辑

    }
}

```



##### 1.4.1.4.3、SmartApplicationListener实现有序监听（可以是同一个事件）   

> 首先我们可能会有两种操作，一种是监听到，存入数据库中，另外一种是监听到发送邮件。那么好了，这样就会有顺序产生，肯定是先存到数据库，再发送邮件了。其实二者类一波一样，只不过是order顺序不一样而已，order越小越先执行



用户注册：`@Order(1)`

```java
@Component
@Order(0)
public class UserRegisterListener implements SmartApplicationListener
{
 
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        //只有UserRegisterEvent监听类型才会执行下面逻辑
        return aClass == UserRegisterEvent.class;
    }
    
    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        //只有在UserService内发布的UserRegisterEvent事件时才会执行下面逻辑
        return aClass == UserService.class;
    }
    

    /**
     *  supportsEventType & supportsSourceType 两个方法返回true时调用该方法执行业务逻辑
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

}

```



发送邮件：`@Order(12)`


```java
@Component
@Order(1)
public class UserRegisterSendMailListener implements SmartApplicationListener
{
    
    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        //只有UserRegisterEvent监听类型才会执行下面逻辑
        return aClass == UserRegisterEvent.class;
    }

 
    @Override
    public boolean supportsSourceType(Class<?> aClass) {
        //只有在UserService内发布的UserRegisterEvent事件时才会执行下面逻辑
        return aClass == UserService.class;
    }

    /**
     *  supportsEventType & supportsSourceType 两个方法返回true时调用该方法执行业务逻辑
     */
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        //转换事件类型
        UserRegisterEvent userRegisterEvent = (UserRegisterEvent) applicationEvent;
        //获取注册用户对象信息
        UserBean user = userRegisterEvent.getUser();
        System.out.println("用户："+user.getName()+"，注册成功，发送邮件通知。");
    }
  
}

```



### 1.4.2、源码解析

> ApplicationEvent以及Listener是Spring为我们提供的一个事件监听、订阅的实现    
>
> 1、内部实现原理是观察者设计模式，设计初衷也是为了系统业务逻辑之间的解耦，提高可扩展性以及可维护性。      
>
> 2、事件发布者并不需要考虑谁去监听，监听具体的实现内容是什么，发布者的工作只是为了发布事件而已



#### 1.4.2.1、基本概念

Spring的事件驱动模型由三部分组成     

事件: `ApplicationEvent `,继承自JDK的 `EventObject `，所有事件都要继承它,也就是被观察者     

事件发布者: `ApplicationEventPublisher `及 `ApplicationEventMulticaster `接口，使用这个接口，就可以发布事件了        

事件监听者: `ApplicationListener `,继承JDK的 `EventListener `，所有监听者都继承它，也就是我们所说的观察者，当然我们也可以使用注解 `@EventListener `，效果是一样的



可以看到上面我们的示例里面的发布者是`applicationContext`，它继承了 `ApplicationEventPublisher `这个接口

```java
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {

```









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
		id: '6CxpyvoX97K8QWbI',
    });
    gitalk.render('gitalk-container');
</script> 
