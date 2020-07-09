---
title: SpringBoot启动流程源码分析
date: 2020-06-17 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot启动流程源码分析
---

# 前言

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





看完之后回过头来看，就明白了

**1，BeanFactory**     

> `Bean工厂BeanFactory`是`Spring`框架最核心的接口，它提供了`IoC`的配置机制。它的一个实现`DefaultListableBeanFactory`在上下文中持有，也就是我们常说的IOC容器，

**2，ApplicationContext**    

> 应用上下文`ApplicationContext`建立在`BeanFactory`基础之上，提供了更多面向应用的功能。`SpringBoot`使用的是`ApplicationContext`的子类`AnnotationConfigServletWebServerApplicationContext`。

对于`BeanFactory`和`ApplicationContext`的比较，`BeanFactory`是`Spring`框架的基础设施，面向`Spring`本身，`ApplicationContext`面向使用`Spring`框架的开发者，如果将`Spring`容器比作一辆汽车，那么`BeanFactory`就是汽车的发动机，而`ApplicationContext`则是一辆完整的汽车（**后面有的地方，我把它看做Spring容器**），它不但包括发动机，还包括离合器、变速器及底盘等其他组件。     

**应用上下文可以理解成IoC容器的高级表现形式，应用上下文确实是在IoC容器的基础上丰富了一些高级功能**。     

**应用上下文对IoC容器是持有的关系。他的一个属性beanFactory就是IoC容器（DefaultListableBeanFactory）。所以他们之间是持有，和扩展的关系**。



> `context`就是我们熟悉的上下文（也有人称之为容器，我的理解也是容器，都可以，看个人爱好和理解，但是建议还是将上下文和容器分开）     
>
> `beanFactory`就是我们所说的IoC容器的真实面孔了。**细细感受下上下文和容器的联系和区别**，对于我们理解源码有很大的帮助。，**最好将上下文和容器严格区分开来的。**   

3、**BeanDefinitionRegistry**

> `BeanDefinitionRegistry`定义了很重要的方法`registerBeanDefinition()`，该方法将`BeanDefinition`注册进`DefaultListableBeanFactory`容器的`beanDefinitionMap`中。    
>
> `DefaultListableBeanFactory` 和`GenericApplicationContext`实现了它







![image-20200629181002029](D:\study\HealerJean.github.io\blogImages\image-20200629181002029.png)







# 1、入口列

```java
package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBoot_Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot_Application.class, args);

    }

}

```



> **调用静态类，参数对应的就是`SpringBoot_Application.class`以及main方法中的args**  

```java

public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
    return run(new Class<?>[] { primarySource }, args);
}


public static ConfigurableApplicationContext run(Class<?>[] primarySources, String[] args) {
    return new SpringApplication(primarySources).run(args);
}
```





>  `new SpringApplication(primarySources)`   构造一个**SpringApplication**的实例，并把我们的启动类`SpringBoot_Application.class`作为参数传进去，然后运行它的run方法

```java
public SpringApplication(Class<?>... primarySources) {
    this(null, primarySources);
}


@SuppressWarnings({ "unchecked", "rawtypes" })
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    this.resourceLoader = resourceLoader;
    Assert.notNull(primarySources, "PrimarySources must not be null");
    
    //把SpringBoot_Application.class设置为属性存储起来
    this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
    
    //设置应用类型
    this.webApplicationType = WebApplicationType.deduceFromClasspath();
    
    //设置初始化器(Initializer),后面会调用这些初始化器
    setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
    
    //设置监听器(Listener)
    setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
    
    this.mainApplicationClass = deduceMainApplicationClass();
}
```



## 1.1、设置应用类型   

```java
//设置应用类型
this.webApplicationType = WebApplicationType.deduceFromClasspath();
```



> 如果是使用外部tomcat启动，则是`REACTIVE`，如果适应内置tomcat则是`SERVLET`

```java
package org.springframework.boot;

import org.springframework.util.ClassUtils;

/**
 * An enumeration of possible types of web application.
 *
 * @author Andy Wilkinson
 * @author Brian Clozel
 * @since 2.0.0
 */
public enum WebApplicationType {

	NONE,
	SERVLET,
	REACTIVE;

	private static final String[] SERVLET_INDICATOR_CLASSES = { "javax.servlet.Servlet",
			"org.springframework.web.context.ConfigurableWebApplicationContext" };

    //我们引入了spring-boot-starter-web，同时引入了tomcat和SpringMvc,肯定会存在DispatcherServlet.class字节码
	private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

	private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

	private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

	private static final String SERVLET_APPLICATION_CONTEXT_CLASS = "org.springframework.web.context.WebApplicationContext";

	private static final String REACTIVE_APPLICATION_CONTEXT_CLASS = 
        "org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext";

    
     //这里主要是通过类加载器判断REACTIVE相关的Class是否存在，如果不存在，则web环境即为SERVLET类型。
    //这里设置好web环境类型，在后面会根据类型初始化对应环境。
	static WebApplicationType deduceFromClasspath() {
		if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null)
				&& !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
			return WebApplicationType.REACTIVE;
		}
		for (String className : SERVLET_INDICATOR_CLASSES) {
			if (!ClassUtils.isPresent(className, null)) {
				return WebApplicationType.NONE;
			}
		}
		return WebApplicationType.SERVLET;
	}


      ……………………………………

}

```



### 1.1、内置tomcat   

> 如果使用内置tomcat，则需要引入如下依赖 

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

 **spring-boot-starter-web 的pom又会引入Tomcat和spring-webmvc，如下**     

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-webmvc</artifactId>
  <version>5.0.5.RELEASE</version>
  <scope>compile</scope>
</dependency>
```



很明显`spring-webmvc`中存在`DispatcherServlet`这个类，也就是我们以前`SpringMvc`的核心`Servlet`，通过类加载能加载`DispatcherServlet`这个类，那么我们的应用类型自然就是`WebApplicationType.SERVLET`

   



![image-20200616143542408](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616143542408.png)



## 1.2、设置初始化器`ApplicationContextInitializer`

```java
//设置初始化器(Initializer),后面会调用这些初始化器
setInitializers((Collection) getSpringFactoriesInstances( ApplicationContextInitializer.class));
```



```java

private <T> Collection<T> getSpringFactoriesInstances(Class<T> type) {
    return getSpringFactoriesInstances(type, new Class<?>[] {});
}

private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {
    ClassLoader classLoader = getClassLoader();
    //这里面首先会根据入参type读取所有的names(是一个String集合)，然后根据这个集合来完成对应的实例化操作：
    Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
    
    //根据names来进行实例化(parameterTypes 构造器的参数类型，args，构造器需要的参数，name对象的名称集合)
    List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
    
    //对实例进行排序 （org.springframework.core.annotation.Order注解指定的顺序）
    AnnotationAwareOrderComparator.sort(instances);
    return instances;
}
```

![image-20200616145952805](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616145952805.png)



### 1.2.1、获取所有的初始化类的name  

```java
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
    //org.springframework.context.ApplicationContextInitializer  
    String factoryTypeName = factoryType.getName();
    
    //类似于注解@EnableAutoConfiguration 获取可自动装配的
    //从`classpath`中搜寻所有的 `META-INF/spring.factories` 配置文件，
    //并将其中`org.springframework.context.ApplicationContextInitializer` 对应的配置项中所有符合条件的制作成List包           
    return loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
}
```



![image-20200616144344183](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616144344183.png)



```java

private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
    MultiValueMap<String, String> result = cache.get(classLoader);
    if (result != null) {
        return result;
    }

    try {
         //从类路径的META-INF/spring.factories中加载所有默认的自动配置类
        Enumeration<URL> urls = (classLoader != null ?
                                 classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                                 ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        result = new LinkedMultiValueMap<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            UrlResource resource = new UrlResource(url);
            
            //获取org.springframework.context.ApplicationContextInitializer的所有值
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                String factoryTypeName = ((String) entry.getKey()).trim();
                for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) 
                {
                    result.add(factoryTypeName, factoryImplementationName.trim());
                }
            }
        }
        cache.put(classLoader, result);
        return result;
    }
    catch (IOException ex) {
        throw new IllegalArgumentException("Unable to load factories from location [" +
                                           FACTORIES_RESOURCE_LOCATION + "]", ex);
    }
}
```



![image-20200616150712458](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616150712458.png)



**这两个类名会被读取出来，然后放入到`Set<String>`集合中，准备开始下面的实例化操作：**

```
# Initializers
org.springframework.context.ApplicationContextInitializer=\
org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer,\
org.springframework.boot.autoconfigure.logging.ConditionEvaluationReportLoggingListener

```



### 1.2.2、根据names来进行反射实例化对象

```java
 List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
```

```java
// parameterTypes: 上一步得到的names集合
private <T> List<T> createSpringFactoriesInstances(Class<T> type,
                                                   Class<?>[] parameterTypes, ClassLoader classLoader, Object[] args,
                                                   Set<String> names) {
    List<T> instances = new ArrayList<T>(names.size());
    for (String name : names) {
        try {
            //类加载器根据name加载类
            Class<?> instanceClass = ClassUtils.forName(name, classLoader);
            
            //确认被加载类是ApplicationContextInitializer的子类
            Assert.isAssignable(type, instanceClass);
            
            
            //反射实例化对象（parameterTypes 传入为null）
             Constructor<?> constructor = instanceClass.getDeclaredConstructor(parameterTypes);
            T instance = (T) BeanUtils.instantiateClass(constructor, args);
            
            
            //加入List集合中
            instances.add(instance);
        }
        catch (Throwable ex) {
            throw new IllegalArgumentException(
                "Cannot instantiate " + type + " : " + name, ex);
        }
    }
    return instances;
}
```



## 1.3、设置监听器`ApplicationListener`

> 可以发现，这个加载相应的类名，然后完成实例化的过程和上面在设置初始化器时如出一辙，同样，还是以`spring-boot-autoconfigure`这个包中的spring.factories为例，看看相应的Key-Value：   
>
> 

```java
setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
```





**这10个监听器会贯穿springBoot整个生命周期。至此，对于SpringApplication实例的初始化过程就结束了。**

```
# Application Listeners
org.springframework.context.ApplicationListener=\
org.springframework.boot.autoconfigure.BackgroundPreinitializer

```



```
org.springframework.context.ApplicationListener=\
org.springframework.boot.ClearCachesApplicationListener,\
org.springframework.boot.builder.ParentContextCloserApplicationListener,\
org.springframework.boot.context.FileEncodingApplicationListener,\
org.springframework.boot.context.config.AnsiOutputApplicationListener,\
org.springframework.boot.context.config.ConfigFileApplicationListener,\
org.springframework.boot.context.config.DelegatingApplicationListener,\
org.springframework.boot.context.logging.ClasspathLoggingApplicationListener,\
org.springframework.boot.context.logging.LoggingApplicationListener,\
org.springframework.boot.liquibase.LiquibaseServiceLocatorApplicationListener
```



# 2、SpringBootApplication.run()方法  

>  第一步：获取并启动监听器     
>
> 第二步：根据`SpringApplicationRunListeners`以及参数来准备环境      
>
> 第三步：创建Spring容器    
>
> 第四步：Spring容器前置处理     
>
> 第五步：刷新容器     
>
> 第六步：Spring容器后置处理     
>
> 第七步：发出结束执行的事件    
>
> 第八步：执行Runners

```java
public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
    return run(new Class<?>[] { primarySource }, args);
}



public ConfigurableApplicationContext run(String... args) {
    // 计时工具
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    ConfigurableApplicationContext context = null;
    Collection<SpringBootExceptionReporter> exceptionReporters = new ArrayList<>();
    configureHeadlessProperty();


    // 第一步：获取并启动监听器 (从META-INF/spring.factories中获取监听器)
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();
    try {
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        // 第二步：根据SpringApplicationRunListeners以及参数来准备环境
        ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
        configureIgnoreBeanInfo(environment);


        // 准备Banner打印器 - 就是启动Spring Boot的时候打印在console上的ASCII艺术字体
        Banner printedBanner = printBanner(environment);


        // 第三步：创建初始化应用上下文ApplicationContext和IOC容器BeanFactory
        context = createApplicationContext();


        exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
                                                         new Class[] { ConfigurableApplicationContext.class }, context);


        // 第四步：刷新应用上下文前的准备阶段/前置处理
        prepareContext(context, environment, listeners, applicationArguments, printedBanner);

        
        
        // 第五步：刷新应用上下
        refreshContext(context);


        // 第六步：刷新应用上下文后的扩展接口
        afterRefresh(context, applicationArguments);
        
        //时间记录停止
        stopWatch.stop();
        if (this.logStartupInfo) {
            new StartupInfoLogger(this.mainApplicationClass).logStarted(getApplicationLog(), stopWatch);
        }



        // 第七步：发出结束执行的事件
        listeners.started(context);


        // 第八步：执行Runners
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

    // 返回容器
    return context;
}
```



## 2.1、获取并启动监听器`SpringApplicationRunListeners`

> 获取并启动监听器 (从META-INF/spring.factories中获取监听器)       
>
> **事件机制在Spring是很重要的一部分内容，通过事件机制我们可以监听Spring容器中正在发生的一些事件，同样也可以自定义监听事件**。     
>
> Spring的事件为Bean和Bean之间的消息传递提供支持。当一个被监听对象执行某种任务后，监听器进行某些处理，常用的场景有进行某些操作后发送通知，消息、邮件等情况。

```java
SpringApplicationRunListeners listeners = getRunListeners(args);
listeners.starting();
```

**最终返回的对象主要是里面的`List<SpringApplicationRunListener> listeners;`**

```java
class SpringApplicationRunListeners {

	private final Log log;

	private final List<SpringApplicationRunListener> listeners;
```



### 2.1.1、获取监听器

```java
SpringApplicationRunListeners listeners = getRunListeners(args);
```



```java
private SpringApplicationRunListeners getRunListeners(String[] args) {
    Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };
    return new SpringApplicationRunListeners(logger,
                                             getSpringFactoriesInstances(SpringApplicationRunListener.class, types, this, args));
}
```



**这里仍然利用了`getSpringFactoriesInstances`方法来获取实例，大家可以看看前面的这个方法分析，从META-INF/spring.factories中读取Key为`org.springframework.boot.SpringApplicationRunListener`的Values：**     

```java
private <T> Collection<T> getSpringFactoriesInstances(Class<T> type, Class<?>[] parameterTypes, Object... args) {
    ClassLoader classLoader = getClassLoader();
    Set<String> names = new LinkedHashSet<>(SpringFactoriesLoader.loadFactoryNames(type, classLoader));
    List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
    AnnotationAwareOrderComparator.sort(instances);
    return instances;
}
```



```
org.springframework.boot.SpringApplicationRunListener=\
org.springframework.boot.context.event.EventPublishingRunListener
```

![image-20200616153138934](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616153138934.png)



> `getSpringFactoriesInstances`中`createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);`      
>
> 反射获取实例时会触发`EventPublishingRunListener`的构造函数，我们来看看`EventPublishingRunListener`的构造函数：    

```java
public class EventPublishingRunListener implements SpringApplicationRunListener, Ordered {
    private final SpringApplication application;
    private final String[] args;
    
    //广播器
    private final SimpleApplicationEventMulticaster initialMulticaster;

    public EventPublishingRunListener(SpringApplication application, String[] args) {
        this.application = application;
        this.args = args;
        this.initialMulticaster = new SimpleApplicationEventMulticaster();
        Iterator var3 = application.getListeners().iterator();

        while(var3.hasNext()) {
            ApplicationListener<?> listener = (ApplicationListener)var3.next();
            
            //将入口类new SpringBootApplication()的时候设置到SpringApplication的十一个监听器
            //全部添加到SimpleApplicationEventMulticaster这个广播器中
            this.initialMulticaster.addApplicationListener(listener);
        }

    }
    
    //略...
}
```



我们看到**`EventPublishingRunListener`里面有一个广播器，`EventPublishingRunListener` 的构造方法将`SpringApplication`的十一个监听器全部添加到`SimpleApplicationEventMulticaster`这个广播器中，**     



`SimpleApplicationEventMulticaster`广播器父类`AbstractApplicationEventMulticaster`中。关键代码为`this.defaultRetriever.applicationListeners.add(listener);`，这是一个内部类，用来保存所有的监听器。      

也就是在这一步，将`spring.factories`中的监听器传递到`SimpleApplicationEventMulticaster`中。我们现在知道`EventPublishingRunListener`中有一个广播器`SimpleApplicationEventMulticaster`，`SimpleApplicationEventMulticaster`广播器中有一个内部类对象又存放所有的监听器



```java
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanClassLoaderAware, BeanFactoryAware {
    
    //广播器的父类中存放保存监听器的内部内
    private final AbstractApplicationEventMulticaster.ListenerRetriever defaultRetriever = 
        new AbstractApplicationEventMulticaster.ListenerRetriever(false);

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        synchronized (this.retrievalMutex) {
            Object singletonTarget = AopProxyUtils.getSingletonTarget(listener);
            if (singletonTarget instanceof ApplicationListener) {
                this.defaultRetriever.applicationListeners.remove(singletonTarget);
            }
            //内部类对象
            this.defaultRetriever.applicationListeners.add(listener);
            this.retrieverCache.clear();
        }
    }

    
    private class ListenerRetriever {
        
        //保存的所有的监听器
        public final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet();
        public final Set<String> applicationListenerBeans = new LinkedHashSet();
        private final boolean preFiltered;

        public ListenerRetriever(boolean preFiltered) {
            this.preFiltered = preFiltered;
        }

        public Collection<ApplicationListener<?>> getApplicationListeners() {
            LinkedList<ApplicationListener<?>> allListeners = new LinkedList();
            Iterator var2 = this.applicationListeners.iterator();

            while(var2.hasNext()) {
                ApplicationListener<?> listener = (ApplicationListener)var2.next();
                allListeners.add(listener);
            }

            if (!this.applicationListenerBeans.isEmpty()) {
                BeanFactory beanFactory = AbstractApplicationEventMulticaster.this.getBeanFactory();
                Iterator var8 = this.applicationListenerBeans.iterator();

                while(var8.hasNext()) {
                    String listenerBeanName = (String)var8.next();

                    try {
                        ApplicationListener<?> listenerx = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
                        if (this.preFiltered || !allListeners.contains(listenerx)) {
                            allListeners.add(listenerx);
                        }
                    } catch (NoSuchBeanDefinitionException var6) {
                        ;
                    }
                }
            }

            AnnotationAwareOrderComparator.sort(allListeners);
            return allListeners;
        }
    }
    //略...
}
```



### 2.1.2、启动监听器   

```java
listeners.starting();
```

```java
void starting() {
    for (SpringApplicationRunListener listener : this.listeners) {
        listener.starting();
    }
}
```





> 上面一步通过`getRunListeners`方法获取的监听器为`EventPublishingRunListener`，从名字可以看出是启动事件发布监听器，主要用来发布启动事件。`

```java
public class EventPublishingRunListener implements SpringApplicationRunListener, Ordered {
    private final SpringApplication application;
    private final String[] args;
    private final SimpleApplicationEventMulticaster initialMulticaster;
```





> 我们先来看看**SpringApplicationRunListener这个接口**

```java
package org.springframework.boot;
public interface SpringApplicationRunListener {

    // 在run()方法开始执行时，该方法就立即被调用，可用于在初始化最早期时做一些工作
    void starting();
    
    // 当environment构建完成，ApplicationContext创建之前，该方法被调用
    void environmentPrepared(ConfigurableEnvironment environment);
    
    // 当ApplicationContext构建完成时，该方法被调用
    void contextPrepared(ConfigurableApplicationContext context);
    
    // 在ApplicationContext完成加载，但没有被刷新前，该方法被调用
    void contextLoaded(ConfigurableApplicationContext context);
    
    // 在ApplicationContext刷新并启动后，CommandLineRunners和ApplicationRunner未被调用前，该方法被调用
    void started(ConfigurableApplicationContext context);
    
    // 在run()方法执行完成前该方法被调用
    void running(ConfigurableApplicationContext context);
    
    // 当应用运行出错时该方法被调用
    void failed(ConfigurableApplicationContext context, Throwable exception);
}
```



`SpringApplicationRunListener`接口在`Spring Boot` 启动初始化的过程中各种状态时执行，当然我们也可以添加自己的监听器，在SpringBoot初始化时监听事件执行自定义逻辑，我们先来看看`SpringBoot`启动时第一个启动事件`listeners.starting()`：    



通过监听器中的广播器，创建application启动事件`ApplicationStartingEvent`

```java
@Override
public void starting() {
    this.initialMulticaster.multicastEvent(new ApplicationStartingEvent(this.application, this.args));
}

```



```java
@Override
public void multicastEvent(ApplicationEvent event) {
    multicastEvent(event, resolveDefaultEventType(event));
}
```

```java
@Override
public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
    ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
    
    //通过事件类型ApplicationStartingEvent获取对应的监听器
    for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
        
        //获取线程池，如果为空则同步处理。这里线程池为空，还未没初始化。
        Executor executor = getTaskExecutor();
        if (executor != null) {
            
            //异步发送事件
            executor.execute(() -> invokeListener(listener, event));
        }
        else {
            
            //同步发送事件
            invokeListener(listener, event);
        }
    }
}
```



**这里会根据事件类型`ApplicationStartingEvent`获取对应的监听器，在容器启动之后执行响应的动作，有如下4种监听器：**

![image-20200616161646504](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616161646504.png)





**我们选择springBoot 的日志监听器来进行讲解，核心代码如下：**     

因为我们的事件类型为`ApplicationEvent`，所以会执行`onApplicationStartedEvent((ApplicationStartedEvent) event);`。springBoot会在运行过程中的不同阶段，发送各种事件，来执行对应监听器的对应方法。

```java
@Override
public void onApplicationEvent(ApplicationEvent event) {
    //在springboot启动的时候
    if (event instanceof ApplicationStartedEvent) {
        onApplicationStartedEvent((ApplicationStartedEvent) event);
    }
    
    //springboot的Environment环境准备完成的时候
    else if (event instanceof ApplicationEnvironmentPreparedEvent) {
        onApplicationEnvironmentPreparedEvent(
                (ApplicationEnvironmentPreparedEvent) event);
    }
    
    //在springboot容器的环境设置完成以后
    else if (event instanceof ApplicationPreparedEvent) {
        onApplicationPreparedEvent((ApplicationPreparedEvent) event);
    }
    
    //容器关闭的时候
    else if (event instanceof ContextClosedEvent && ((ContextClosedEvent) event)
            .getApplicationContext().getParent() == null) {
        onContextClosedEvent();
    }
    
    //容器启动失败的时候
    else if (event instanceof ApplicationFailedEvent) {
        onApplicationFailedEvent();
    }
}
```





## 2.2、环境构建

> 根据SpringApplicationRunListeners以及参数来准备环境

```java
// 第二步：根据SpringApplicationRunListeners以及参数来准备环境
ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
configureIgnoreBeanInfo(environment);
```





```java
private ConfigurableEnvironment prepareEnvironment(SpringApplicationRunListeners listeners,
                                                   ApplicationArguments applicationArguments) {
    
    //获取对应的ConfigurableEnvironment
    ConfigurableEnvironment environment = getOrCreateEnvironment();
    
      //配置
    configureEnvironment(environment, applicationArguments.getSourceArgs());
    ConfigurationPropertySources.attach(environment);
   
    
    //发布环境已准备事件，这是第二次发布事件
    listeners.environmentPrepared(environment);
    bindToSpringApplication(environment);
    if (!this.isCustomEnvironment) {
        environment = new EnvironmentConverter(getClassLoader()).convertEnvironmentIfNecessary(environment,
                                                                                               deduceEnvironmentClass());
    }
    ConfigurationPropertySources.attach(environment);
    return environment;
}
```





### 2.2.1、获取类型获取`ConfigurableEnvironment`

```java
private ConfigurableEnvironment getOrCreateEnvironment() {
    if (this.environment != null) {
        return this.environment;
    }
    switch (this.webApplicationType) {
        case SERVLET:
            return new StandardServletEnvironment();
        case REACTIVE:
            return new StandardReactiveWebEnvironment();
        default:
            return new StandardEnvironment();
    }
}
```





### 2.2.2、配置环境

```java
protected void configureEnvironment(ConfigurableEnvironment environment, String[] args) {
    if (this.addConversionService) {
        ConversionService conversionService = ApplicationConversionService.getSharedInstance();
        environment.setConversionService((ConfigurableConversionService) conversionService);
    }
    configurePropertySources(environment, args);
    configureProfiles(environment, args);
}
```



### 2.2.3、监听器监听，环境已准备事件

```java
listeners.environmentPrepared(environment);
```



```java
void environmentPrepared(ConfigurableEnvironment environment) {
    for (SpringApplicationRunListener listener : this.listeners) {
        listener.environmentPrepared(environment);
    }
}
```

```java
@Override
public void environmentPrepared(ConfigurableEnvironment environment) {
    this.initialMulticaster
        .multicastEvent(new ApplicationEnvironmentPreparedEvent(this.application, this.args, environment));
}

```



```java
@Override
public void multicastEvent(final ApplicationEvent event, @Nullable ResolvableType eventType) {
    ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
    
    //通过事件类型ApplicationStartingEvent获取对应的监听器
    for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
        
        //获取线程池，如果为空则同步处理。这里线程池为空，还未没初始化。
        Executor executor = getTaskExecutor();
        if (executor != null) {
            
            //异步发送事件
            executor.execute(() -> invokeListener(listener, event));
        }
        else {
            
            //同步发送事件
            invokeListener(listener, event);
        }
    }
}
```

![image-20200616163139914](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616163139914.png)







主要来看一下**`ConfigFileApplicationListener`**，该监听器非常核心，主要用来处理项目配置。项目中的 properties 和yml文件都是其内部类所加载。具体来看一下：

```java
@Override
public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ApplicationEnvironmentPreparedEvent) {
        onApplicationEnvironmentPreparedEvent((ApplicationEnvironmentPreparedEvent) event);
    }
    if (event instanceof ApplicationPreparedEvent) {
        onApplicationPreparedEvent(event);
    }
}

private void onApplicationEnvironmentPreparedEvent(ApplicationEnvironmentPreparedEvent event) {
    List<EnvironmentPostProcessor> postProcessors = loadPostProcessors();
    postProcessors.add(this);
    AnnotationAwareOrderComparator.sort(postProcessors);
    for (EnvironmentPostProcessor postProcessor : postProcessors) {
        postProcessor.postProcessEnvironment(event.getEnvironment(), event.getSpringApplication());
    }
}
```

![image-20200616163505148](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616163505148.png)   





首先还是会去读`spring.factories` 文件，`List<EnvironmentPostProcessor> postProcessors = loadPostProcessors();`获取的处理类有以下四种：

```java
List<EnvironmentPostProcessor> loadPostProcessors() {
    return SpringFactoriesLoader.loadFactories(EnvironmentPostProcessor.class, getClass().getClassLoader());
}
```



```
# Environment Post Processors
org.springframework.boot.env.EnvironmentPostProcessor=
org.springframework.boot.cloud.CloudFoundryVcapEnvironmentPostProcessor，
org.springframework.boot.env.SpringApplicationJsonEnvironmentPostProcessor，
org.springframework.boot.env.SystemEnvironmentPropertySourceEnvironmentPostProcessor
```





在执行完上述`Processor`（其实这几个也是监听器）流程后，`ConfigFileApplicationListener`会执行该类本身的逻辑。由其内部类`Loader`加载项目制定路径下的配置文件：    

```java
private static final String DEFAULT_SEARCH_LOCATIONS = "classpath:/,classpath:/config/,file:./,file:./config/";
```



至此，项目的变量配置已全部加载完毕，来一起看一下：



![image-20200616163841790](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616163841790.png)





## 2.3、创建初始化应用上下文`ApplicationContext`和IOC容器`BeanFactory `

> 创建初始化应用上下文`ApplicationContex`t和IOC容器`BeanFactory`

```java
context = createApplicationContext();
```



> 这里创建容器的类型 还是根据`webApplicationType`进行判断的，该类型为`SERVLET`类型，所以会通过反射装载对应的字节码，也就是**AnnotationConfigServletWebServerApplicationContext**    



```java
	public static final String DEFAULT_CONTEXT_CLASS = "org.springframework.context."
			+ "annotation.AnnotationConfigApplicationContext";

	public static final String DEFAULT_SERVLET_WEB_CONTEXT_CLASS = "org.springframework.boot."
			+ "web.servlet.context.AnnotationConfigServletWebServerApplicationContext";

	public static final String DEFAULT_REACTIVE_WEB_CONTEXT_CLASS = "org.springframework."
			+ "boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext";


protected ConfigurableApplicationContext createApplicationContext() {
    Class<?> contextClass = this.applicationContextClass;
    if (contextClass == null) {
        try {
            switch (this.webApplicationType) {
                case SERVLET:
                    //内置容器 AnnotationConfigServletWebServerApplicationContext
                    contextClass = Class.forName(DEFAULT_SERVLET_WEB_CONTEXT_CLASS);
                    break;
                case REACTIVE:
                    contextClass = Class.forName(DEFAULT_REACTIVE_WEB_CONTEXT_CLASS);
                    break;
                default:
                    contextClass = Class.forName(DEFAULT_CONTEXT_CLASS);
            }
        }
        catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                "Unable create a default ApplicationContext, please specify an ApplicationContextClass", ex);
        }
    }
    return (ConfigurableApplicationContext) BeanUtils.instantiateClass(contextClass);
}

```



![image-20200616164430917](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200616164430917.png)



### 2.3.1、创建上下文对象

> `AnnotationConfigServletWebServerApplicationContext`

```java
public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
    Assert.notNull(clazz, "Class must not be null");
    if (clazz.isInterface()) {
        throw new BeanInstantiationException(clazz, "Specified class is an interface");
    }
    try {
        //实例化的是空构造器  
        return instantiateClass(clazz.getDeclaredConstructor());
    }
    catch (NoSuchMethodException ex) {
        Constructor<T> ctor = findPrimaryConstructor(clazz);
        if (ctor != null) {
            return instantiateClass(ctor);
        }
        throw new BeanInstantiationException(clazz, "No default constructor found", ex);
    }
    catch (LinkageError err) {
        throw new BeanInstantiationException(clazz, "Unresolvable class definition", err);
    }
}


public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws BeanInstantiationException {
    Assert.notNull(ctor, "Constructor must not be null");
    try {
        ReflectionUtils.makeAccessible(ctor);
        if (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(ctor.getDeclaringClass())) {
            return KotlinDelegate.instantiateClass(ctor, args);
        }
        else {
            Class<?>[] parameterTypes = ctor.getParameterTypes();
            Assert.isTrue(args.length <= parameterTypes.length, "Can't specify more arguments than constructor parameters");
            Object[] argsWithDefaultValues = new Object[args.length];
            for (int i = 0 ; i < args.length; i++) {
                if (args[i] == null) {
                    Class<?> parameterType = parameterTypes[i];
                    argsWithDefaultValues[i] = (parameterType.isPrimitive() ? DEFAULT_TYPE_VALUES.get(parameterType) : null);
                }
                else {
                    argsWithDefaultValues[i] = args[i];
                }
            }
            return ctor.newInstance(argsWithDefaultValues);
        }
    }
    catch (InstantiationException ex) {
        throw new BeanInstantiationException(ctor, "Is it an abstract class?", ex);
    }
    catch (IllegalAccessException ex) {
        throw new BeanInstantiationException(ctor, "Is the constructor accessible?", ex);
    }
    catch (IllegalArgumentException ex) {
        throw new BeanInstantiationException(ctor, "Illegal arguments for constructor", ex);
    }
    catch (InvocationTargetException ex) {
        throw new BeanInstantiationException(ctor, "Constructor threw exception", ex.getTargetException());
    }
}
```





```java

public class ServletWebServerApplicationContext extends GenericWebApplicationContext
    implements ConfigurableWebServerApplicationContext {
	
	//会率先调用父类中的构造器哦，也就是会创建IOC容器，往下看一点
    public ServletWebServerApplicationContext() {
    }



}
```



### 2.3.2、创建IOC容器`BeanFactory`  

> `beanFactory`正是在`AnnotationConfigServletWebServerApplicationContext`实现的接口`GenericApplicationContext`中定义的。在上面`createApplicationContext()`方法中的，     
>
> 
>
>  `BeanUtils.instantiateClass(contextClass) `这个方法中，不但初始化了`AnnotationConfigServletWebServerApplicationContext`类，也就是我们的上下文`context`，同样也触发了`GenericApplicationContext`类的构造函数，从而IoC容器也创建了。仔细看他的构造函数，有没有发现一个很熟悉的类`DefaultListableBeanFactory`，没错，`DefaultListableBeanFactory`就是IoC容器真实面目了。在后面的refresh()方法分析中，`DefaultListableBeanFactory`是无处不在的存在感。   



```java
public class GenericWebApplicationContext extends GenericApplicationContext
		implements ConfigurableWebApplicationContext, ThemeSource {


	public GenericWebApplicationContext() {
		super();
	}

    
 //other code
    
}
    

```



```java
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private final DefaultListableBeanFactory beanFactory;

    @Nullable
    private ResourceLoader resourceLoader;

    private boolean customClassLoader = false;

    private final AtomicBoolean refreshed = new AtomicBoolean();



    public GenericApplicationContext() {
        //IOC容器
        this.beanFactory = new DefaultListableBeanFactory();
    }

    //该方式实现来自于 BeanDefinitionRegistry
	// 这个方法很关键，有点类似于一个 适配器模式，委托
    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
        throws BeanDefinitionStoreException {
		
        //
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }



    //other code


}
```









## 2.4、上下文准备阶段

> 这一步主要是在容器刷新之前的准备动作。包含一个非常关键的操作：**将启动类注入容器**    



```java
prepareContext(context, environment, listeners, applicationArguments, printedBanner);
```



```java
private void prepareContext(ConfigurableApplicationContext context,
                            ConfigurableEnvironment environment, SpringApplicationRunListeners listeners,
                            ApplicationArguments applicationArguments, Banner printedBanner) {

    //设置容器环境
    context.setEnvironment(environment);

    //执行容器后置处理
    postProcessApplicationContext(context);

    //调用初始化器，执行容器中的 ApplicationContextInitializer 包括spring.factories和通过三种方式自定义的
    applyInitializers(context);

    //向各个监听器发送容器已经准备好的事件
    listeners.contextPrepared(context);

    //获取工厂，其实就是我们的ioc容器，DefaultListableBeanFactory
    ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
    //将main函数中的args参数封装成单例Bean，注册进容器
    beanFactory.registerSingleton("springApplicationArguments", applicationArguments);
    //将 printedBanner 也封装成单例，注册进容器
    if (printedBanner != null) {
        beanFactory.registerSingleton("springBootBanner", printedBanner);
    }
    if (beanFactory instanceof DefaultListableBeanFactory) {
        ((DefaultListableBeanFactory) beanFactory)
        .setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
    }
    if (this.lazyInitialization) {
        context.addBeanFactoryPostProcessor(new LazyInitializationBeanFactoryPostProcessor());
    }


    //获取我们的启动类指定的参数，可以是多个
    Set<Object> sources = getAllSources();

    Assert.notEmpty(sources, "Sources must not be empty");


    //加载我们的启动类，将启动类注入容器
    load(context, sources.toArray(new Object[0]));

    //发布容器已加载事件。
    listeners.contextLoaded(context);
}
```



### 2.4.1、设置容器环境

```java
//设置容器环境
context.setEnvironment(environment);
```



### 2.4.2、调用初始化器

> 这里终于用到了在创建SpringApplication实例时设置的初始化器了，依次对它们进行遍历，并调用`initialize`方法。       
>
> 我们也可以自定义初始化器， 并实现**initialize**方法，然后放入`META-INF/spring.factories`配置文件中Key为：`org.springframework.context.ApplicationContextInitializer`的value中，这里我们自定义的初始化器就会被调用，是我们项目初始化的一种方式    
>
> 

```java
protected void applyInitializers(ConfigurableApplicationContext context) {

    // 1. 从SpringApplication类中的initializers集合获取所有的ApplicationContextInitializer
    for (ApplicationContextInitializer initializer : getInitializers()) {

        // 2. 循环调用ApplicationContextInitializer中的initialize方法
        Class<?> requiredType = GenericTypeResolver.resolveTypeArgument(
            initializer.getClass(), ApplicationContextInitializer.class);
        Assert.isInstanceOf(requiredType, context, "Unable to call initializer.");
        initializer.initialize(context);
    }
}
```





### 2.4.3、监听器：容器已经准备好的事件

```java
//向各个监听器发送容器已经准备好的事件
listeners.contextPrepared(context
```



### 2.4.3、加载启动指定类（重点）    

> 启动类`SpringBoot_Application.class`被加载到 bean工厂的  `beanDefinitionMap`中，后续该启动类将作为开启自动化配置的入口，    

```java
Set<Object> sources = getAllSources();
Assert.notEmpty(sources, "Sources must not be empty");
load(context, sources.toArray(new Object[0]));
```



> 大家先回到文章最开始看看，在创建**SpringApplication**实例时，先将**SpringBoot_Application.class**存储在`this.primarySources`属性中，现在就是用到这个属性的时候了，我们来看看`getAllSources（）`

```java
	public Set<Object> getAllSources() {
		Set<Object> allSources = new LinkedHashSet<>();
		if (!CollectionUtils.isEmpty(this.primarySources)) {
			allSources.addAll(this.primarySources);
		}
		if (!CollectionUtils.isEmpty(this.sources)) {
			allSources.addAll(this.sources);
		}
		return Collections.unmodifiableSet(allSources);
	}
```



```java
protected void load(ApplicationContext context, Object[] sources) {
    //创建 BeanDefinitionLoader
    BeanDefinitionLoader loader = createBeanDefinitionLoader(getBeanDefinitionRegistry(context), sources);
    if (this.beanNameGenerator != null) {
        loader.setBeanNameGenerator(this.beanNameGenerator);
    }
    if (this.resourceLoader != null) {
        loader.setResourceLoader(this.resourceLoader);
    }
    if (this.environment != null) {
        loader.setEnvironment(this.environment);
    }
    loader.load();
}


```



#### 2.4.3.1、`getBeanDefinitionRegistry`	

> 　这里将我们前文创建的上下文强转为`BeanDefinitionRegistry`，他们之间是有继承关系的。`BeanDefinitionRegistry`定义了很重要的方法`registerBeanDefinition()`，该方法将`BeanDefinition`注册进`DefaultListableBeanFactory`容器的`beanDefinitionMap`中。



```java
private BeanDefinitionRegistry getBeanDefinitionRegistry(ApplicationContext context) {
    if (context instanceof BeanDefinitionRegistry) {
        //将上下文转为 BeanDefinitionRegistry
        return (BeanDefinitionRegistry) context;
    }
    //和上面if最终实现一样，但是在类与类的实现上是有区别的。具体往下看
    if (context instanceof AbstractApplicationContext) {
        return (BeanDefinitionRegistry) ((AbstractApplicationContext) context).getBeanFactory();
    }
    throw new IllegalStateException("Could not locate BeanDefinitionRegistry");
}
```

![image-20200630103829884](D:\study\HealerJean.github.io\blogImages\image-20200630103829884.png)



#### 2.4.3.2、`createBeanDefinitionLoader`

```java
protected BeanDefinitionLoader createBeanDefinitionLoader(BeanDefinitionRegistry registry, Object[] sources) {
   return new BeanDefinitionLoader(registry, sources);
}
```



```java
BeanDefinitionLoader(BeanDefinitionRegistry registry, Object... sources) {
    Assert.notNull(registry, "Registry must not be null");
    Assert.notEmpty(sources, "Sources must not be empty");
    this.sources = sources;

    //注解形式的Bean定义读取器 比如：@Configuration @Bean @Component @Controller @Service等等
    this.annotatedReader = new AnnotatedBeanDefinitionReader(registry);
    //XML形式的Bean定义读取器
    this.xmlReader = new XmlBeanDefinitionReader(registry);
    if (isGroovyPresent()) {
        this.groovyReader = new GroovyBeanDefinitionReader(registry);
    }

    //类路径扫描器 
    this.scanner = new ClassPathBeanDefinitionScanner(registry);
    //扫描器添加排除过滤器
    this.scanner.addExcludeFilter(new ClassExcludeFilter(sources));
}

```



#### 2.4.3.3、loader.load();

> 启动类`SpringBoot_Application.class`被加载到 bean工厂的  `beanDefinitionMap`中

```java
int load() {
    int count = 0;
    for (Object source : this.sources) {
        count += load(source);
    }
    return count;
}

```

```java
private int load(Object source) {
    Assert.notNull(source, "Source must not be null");
    //如果是class类型，启用注解类型
    if (source instanceof Class<?>) {
        return load((Class<?>) source);
    }
    //如果是resource类型，启用xml解析
    if (source instanceof Resource) {
        return load((Resource) source);
    }
    //如果是package类型，启用扫描包，例如：@ComponentScan
    if (source instanceof Package) {
        return load((Package) source);
    }
    //如果是字符串类型，直接加载
    if (source instanceof CharSequence) {
        return load((CharSequence) source);
    }
    throw new IllegalArgumentException("Invalid source type " + source.getClass());
}
```



```java
private int load(Class<?> source) {
    if (isGroovyPresent() && GroovyBeanDefinitionSource.class.isAssignableFrom(source)) {
        // Any GroovyLoaders added in beans{} DSL can contribute beans here
        GroovyBeanDefinitionSource loader = BeanUtils.instantiateClass(source, GroovyBeanDefinitionSource.class);
        load(loader);
    }
    
    //判断是否被注解@Component注释（@SpringBootApplication是一个组合注解）
    if (isComponent(source)) {
        
        //解形式的Bean定义读取器
        //将启动类bean信息存入beanDefinitionMap(DefaultListableBeanFactory 类)，
        //也就是将SpringBoot_Application.class存入了beanDefinitionMap
        this.annotatedReader.register(source);
        return 1;
    }
    return 0;
}
```



#### 2.4.3.4、`registerBeanDefinition`  

> 因为启动类`BeanDefinition`的注册流程和后面我们自定义的BeanDefinition的注册流程是一样的。这先介绍一遍这个流程，后面熟悉了这个流程就好理解了。后面马上就到最最最重要的refresh()方法了。   



**`AnnotatedBeanDefinitionReader` 注解Bean定义读取器进行注册class**

```java
//注解形式的bean定义读取器
pblic class AnnotatedBeanDefinitionReader {

    //bean定义注册
    private final BeanDefinitionRegistry registry;


    public void register(Class<?>... componentClasses) {
        for (Class<?> componentClass : componentClasses) {
            registerBean(componentClass);
        }
    }

    public void registerBean(Class<?> beanClass) {
        doRegisterBean(beanClass, null, null, null, null);
    }




    private <T> void doRegisterBean(Class<T> beanClass, @Nullable String name,
                                    @Nullable Class<? extends Annotation>[] qualifiers, @Nullable Supplier<T> supplier,
                                    @Nullable BeanDefinitionCustomizer[] customizers) {

        //将指定的类 封装为AnnotatedGenericBeanDefinition
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
        if (this.conditionEvaluator.shouldSkip(abd.getMetadata())) {
            return;
        }

        abd.setInstanceSupplier(supplier);
        ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
        abd.setScope(scopeMetadata.getScopeName());
        //获取bean的名称 
        String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, this.registry));

        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        if (qualifiers != null) {
            for (Class<? extends Annotation> qualifier : qualifiers) {
                if (Primary.class == qualifier) {
                    abd.setPrimary(true);
                }
                else if (Lazy.class == qualifier) {
                    abd.setLazyInit(true);
                }
                else {
                    abd.addQualifier(new AutowireCandidateQualifier(qualifier));
                }
            }
        }
        if (customizers != null) {
            for (BeanDefinitionCustomizer customizer : customizers) {
                customizer.customize(abd);
            }
        }

        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        definitionHolder = AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);

        // 将该BeanDefinition注册到IoC容器的beanDefinitionMap中
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
    }



}
```





> 到了这里很关键了，用到了适配器，委托模式，可以观察到下面的`registry`，其实就是我们的上下文`AnnotationConfigServletWebServerApplicationContext`    
>
> 而我们使用鼠标进行寻找实现方法`registerBeanDefinition`d的时候，发现出现了`DefaultListableBeanFactory`（这个类不就是我们日夜想的ioc容器），但是仔细观察之后，regsiter是`AnnotationConfigServletWebServerApplicationContext`， 它继承了`GenericApplicationContext`，`GenericApplicationContext`又实现了`DefaultListableBeanFactory`。 所以此事的`registe`r代表的是`GenericApplicationContext`     



```java
public abstract class BeanDefinitionReaderUtils {



    public static void registerBeanDefinition(
        BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry)
        throws BeanDefinitionStoreException {

        // Register bean definition under primary name.
        // primary name 其实就是id吧
        String beanName = definitionHolder.getBeanName();
        registry.registerBeanDefinition(beanName, definitionHolder.getBeanDefinition());

        // Register aliases for bean name, if any.
        //然后就是注册别名
        String[] aliases = definitionHolder.getAliases();
        if (aliases != null) {
            for (String alias : aliases) {
                registry.registerAlias(beanName, alias);
            }
        }
    }


}
```



![image-20200630141505260](D:\study\HealerJean.github.io\blogImages\image-20200630141505260.png)





![image-20200630142341487](D:\study\HealerJean.github.io\blogImages\image-20200630142341487.png)



> 到了这里很明显，ioc容器来了`DefaultListableBeanFactory`，开始注册`beanDefinition`

```java
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private final DefaultListableBeanFactory beanFactory;

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
        throws BeanDefinitionStoreException {
		//ioc容器注册BeanDefininion
        this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
    }

}
```





> 终于到容器这里了   `DefaultListableBeanFactory`，其实就是最终的`BeanDefinitionRegistry`

```java
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
    implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {



@Override
public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
    throws BeanDefinitionStoreException {

    Assert.hasText(beanName, "Bean name must not be empty");
    Assert.notNull(beanDefinition, "BeanDefinition must not be null");

    //校验对象
    if (beanDefinition instanceof AbstractBeanDefinition) {
        try {
            ((AbstractBeanDefinition) beanDefinition).validate();
        }
        catch (BeanDefinitionValidationException ex) {
            throw new BeanDefinitionStoreException(beanDefinition.getResourceDescription(), beanName,
                                                   "Validation of bean definition failed", ex);
        }
    }

    // 判断是否存在重复名字的bean，之后看允不允许override
    // 以前使用synchronized实现互斥访问，现在采用ConcurrentHashMap
    BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
    if (existingDefinition != null) {
        if (!isAllowBeanDefinitionOverriding()) {
            throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
        }
        else if (existingDefinition.getRole() < beanDefinition.getRole()) {
            // e.g. was ROLE_APPLICATION, now overriding with ROLE_SUPPORT or ROLE_INFRASTRUCTURE
            if (logger.isInfoEnabled()) {
                logger.info("Overriding user-defined bean definition for bean '" + beanName +
                            "' with a framework-generated bean definition: replacing [" +
                            existingDefinition + "] with [" + beanDefinition + "]");
            }
        }
        else if (!beanDefinition.equals(existingDefinition)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Overriding bean definition for bean '" + beanName +
                             "' with a different definition: replacing [" + existingDefinition +
                             "] with [" + beanDefinition + "]");
            }
        }
        else {
            if (logger.isTraceEnabled()) {
                logger.trace("Overriding bean definition for bean '" + beanName +
                             "' with an equivalent definition: replacing [" + existingDefinition +
                             "] with [" + beanDefinition + "]");
            }
        }
        
        //如果允许覆盖，注册进beanDefinitionMap
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }
    else {
        if (hasBeanCreationStarted()) {
            // Cannot modify startup-time collection elements anymore (for stable iteration)
            synchronized (this.beanDefinitionMap) {
                // 注册进beanDefinitionMap
                this.beanDefinitionMap.put(beanName, beanDefinition);
                List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
                updatedDefinitions.addAll(this.beanDefinitionNames);
                updatedDefinitions.add(beanName);
                this.beanDefinitionNames = updatedDefinitions;
                removeManualSingletonName(beanName);
            }
        }
        else {
            // Still in startup registration phase
            this.beanDefinitionMap.put(beanName, beanDefinition);
            this.beanDefinitionNames.add(beanName);
            removeManualSingletonName(beanName);
        }
        this.frozenBeanDefinitionNames = null;
    }

    if (existingDefinition != null || containsSingleton(beanName)) {
        resetBeanDefinition(beanName);
    }
    else if (isConfigurationFrozen()) {
        clearByTypeCache();
    }


    //other code


}
```















### 2.4.4、**监听器：容器已加载/准备就绪的事件**

```java
//发布容器已加载事件。
listeners.contextLoaded(context);
```



## 2.5、刷新应用上下文（IOC容器的初始化过程）

> **最后跟踪到`AbstractApplicationContext`这个抽象类的`refresh`方法，这个类直接继承自`ConfigurableApplicationContext`接口，该类属于`spring framework`**



```java
protected void refresh(ApplicationContext applicationContext) {
    Assert.isInstanceOf(AbstractApplicationContext.class, applicationContext);
    //调用创建的容器applicationContext中的refresh()方法
    ((AbstractApplicationContext)applicationContext).refresh();
}



public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        /**
         * 刷新前准备
         */
        prepareRefresh();

        /**
         * 获取并刷新beanFactory(Ioc容器) （能执行刷新一次）
         */
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        /**
         * 为beanFactory设置一些属性，不太重要
         */
        prepareBeanFactory(beanFactory);

        try {
            
            /**
             * 添加了一系列的Bean的后置处理器
             */
            postProcessBeanFactory(beanFactory);

            /**
             * 重点
             * 激活各种BeanFactory处理器,包括BeanDefinitionRegistry，BeanFactoryPostProcessor和普通的BeanFactoryPostProcessor
             * 执行对应的postProcessBeanDefinitionRegistry方法 和  postProcessBeanFactory方法
             */
            invokeBeanFactoryPostProcessors(beanFactory);

            /**
             * 注册拦截Bean创建的Bean处理器，即注册BeanPostProcessor，不是BeanFactoryPostProcessor，注意两者的区别
             * 注意，这里仅仅是注册，并不会执行对应的方法，将在bean的实例化时执行对应的方法
             */
            registerBeanPostProcessors(beanFactory);

            /**
             * 初始化上下文中的资源文件，如国际化文件的处理等
             */
            initMessageSource();

            /**
             * 初始化上下文事件广播器，并放入applicatioEventMulticaster,如ApplicationEventPublisher
             */
            initApplicationEventMulticaster();

            /**
             * 给子类扩展初始化其他Bean
             */
            onRefresh();

            /**
             * 在所有bean中查找listener bean，然后注册到广播器中
             */
            registerListeners();

            /**
             * 设置转换器
             * 注册一个默认的属性值解析器
             * 冻结所有的bean定义，说明注册的bean定义将不能被修改或进一步的处理
             * 初始化剩余的非惰性的bean，即初始化非延迟加载的bean
             */
            finishBeanFactoryInitialization(beanFactory);

            /**
             * 通过spring的事件发布机制发布ContextRefreshedEvent事件，以保证对应的监听器做进一步的处理
             * 即对那种在spring启动后需要处理的一些类，这些类实现了ApplicationListener<ContextRefreshedEvent>，
             * 这里就是要触发这些类的执行(执行onApplicationEvent方法)
             * 另外，spring的内置Event有ContextClosedEvent、ContextRefreshedEvent、ContextStartedEvent、ContextStoppedEvent、RequestHandleEvent
             * 完成初始化，通知生命周期处理器lifeCycleProcessor刷新过程，同时发出ContextRefreshEvent通知其他人
             */
            finishRefresh();
        }

        finally {
    
            resetCommonCaches();
        }
    }
}
```



### 2.5.1、获取并刷新`beanFactory`（ioc容器）

> 在创建应用上下文的时候，触发了`GenericApplicationContext类`的构造方法如下所示，创建了`beanFactory`，也就是创建了`DefaultListableBeanFactory`类。

```java
public abstract class AbstractApplicationContext extends DefaultResourceLoader
    implements ConfigurableApplicationContext {
    
    
    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        //刷新BeanFactory,　
        //1，AbstractApplicationContext类有两个子类实现了refreshBeanFactory()，但是在前面第三步初始化上下文的时候，实例化了GenericApplicationContext类，所以没有进入AbstractRefreshableApplicationContext中的refreshBeanFactory()方法。
        //2，this.refreshed.compareAndSet(false, true) 这行代码在这里表示：GenericApplicationContext只允许刷新一次 　　
        refreshBeanFactory();
        
        //
        return getBeanFactory();
    }

}
```



```JAVA
public class GenericApplicationContext extends AbstractApplicationContext implements BeanDefinitionRegistry {

    private final DefaultListableBeanFactory beanFactory;


    public GenericApplicationContext() {
        this.beanFactory = new DefaultListableBeanFactory();
    }

    @Override
    protected final void refreshBeanFactory() throws IllegalStateException {
        //cas 原子叫交换，代表着该方法只能执行一次
        if (!this.refreshed.compareAndSet(false, true)) {
            throw new IllegalStateException(
                "GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
        }
        this.beanFactory.setSerializationId(getId());
    }

    @Override
    public final ConfigurableListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }


}
```



### 2.5.2、`prepareBeanFactory(beanFactory);`bean工厂准备

> 为`beanFactory`设置一些属性

```java
protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    // Tell the internal bean factory to use the context's class loader etc.
    // 配置类加载器：默认使用当前上下文的类加载器
    beanFactory.setBeanClassLoader(getClassLoader());
    // 配置EL表达式：在Bean初始化完成，填充属性的时候会用到
    beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
    // 添加属性编辑器 PropertyEditor
    beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

    // Configure the bean factory with context callbacks.
    // 添加Bean的后置处理器
    beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    // 忽略装配以下指定的类
    beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
    beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
    beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
    beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
    beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

    // BeanFactory interface not registered as resolvable type in a plain factory.
    // MessageSource registered (and found for autowiring) as a bean.
    // 将以下类注册到 beanFactory（DefaultListableBeanFactory） 的resolvableDependencies属性中
    beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
    beanFactory.registerResolvableDependency(ResourceLoader.class, this);
    beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
    beanFactory.registerResolvableDependency(ApplicationContext.class, this);

    // Register early post-processor for detecting inner beans as ApplicationListeners.
    // 将早期后处理器注册为application监听器，用于检测内部bean
    beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

    // Detect a LoadTimeWeaver and prepare for weaving, if found.
    //如果当前BeanFactory包含loadTimeWeaver Bean，说明存在类加载期织入AspectJ，
    // 则把当前BeanFactory交给类加载期BeanPostProcessor实现类LoadTimeWeaverAwareProcessor来处理，
    // 从而实现类加载期织入AspectJ的目的。
    if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
        beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
        // Set a temporary ClassLoader for type matching.
        beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
    }

    // Register default environment beans.
    // 将当前环境变量（environment） 注册为单例bean
    if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
    }
    // 将当前系统配置（systemProperties） 注册为单例Bean
    if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
    }
    // 将当前系统环境 （systemEnvironment） 注册为单例Bean
    if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
        beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
    }
}
```



### 2.5.3、`postProcessBeanFactory(beanFactory);`

> `postProcessBeanFactory()`方法向上下文中添加了一系列的`Bean`的后置处理器（`BeanPostProcesser`），。后置处理器工作的时机是在所有的`beanDenifition`加载完成之后，`bean`实例化之前执行。简单来说Bean的后置处理器可以修改`BeanDefinition`的属性信息。



> `AnnotationConfigServletWebServerApplicationContext`

```JAVA
@Override
	protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		super.postProcessBeanFactory(beanFactory);
		if (this.basePackages != null && this.basePackages.length > 0) {
			this.scanner.scan(this.basePackages);
		}
		if (!this.annotatedClasses.isEmpty()) {
			this.reader.register(ClassUtils.toClassArray(this.annotatedClasses));
		}
	}
```

> `ServletWebServerApplicationContext`

```java
@Override
protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    beanFactory.addBeanPostProcessor(new WebApplicationContextServletContextAwareProcessor(this));
    beanFactory.ignoreDependencyInterface(ServletContextAware.class);
    registerWebApplicationScopes();
}
```





### 2.5.4、`invokeBeanFactoryPostProcessors(beanFactory);`（重点）

> IoC容器的初始化过程包括三个步骤   (看不懂没关系，等看完代码再过来捋一捋)
>
> **1，第一步：Resource定位** 　
>
> > 在SpringBoot中，我们都知道他的包扫描是从主类所在的包开始扫描的，`prepareContext()`方法中，会先将主类解析成`BeanDefinition`，然后在`refresh()`方法的`invokeBeanFactoryPostProcessors()`方法中解析主类的`BeanDefinition`获取`basePackage`的路径。这样就完成了定位的过程。      
> >
> > **其次SpringBoot的各种starter是通过SPI扩展机制实现的自动装配，SpringBoot的自动装配同样也是在invokeBeanFactoryPostProcessors()方法中实现的。还有一种情况，在SpringBoot中有很多的@EnableXXX注解，细心点进去看的应该就知道其底层是@Import注解，在invokeBeanFactoryPostProcessors()方法中也实现了对该注解指定的配置类的定位加载。**       
> >
> > 常规的在SpringBoot中有三种实现定位，**第一个是主类所在包的**，**第二个是SPI扩展机制实现的自动装配（比如各种starter）**，**第三种就是@Import注解指定的类。（对于非常规的不说了）**
>
> **2，第二步：`BeanDefinition`的载入**
>
> > 在第一步中说了三种Resource的定位情况，定位后紧接着就是BeanDefinition的分别载入。所谓的载入就是通过上面的定位得到的basePackage，SpringBoot会将该路径拼接成：classpath*:org/springframework/boot/demo/**/*.class这样的形式，然后一个叫做`PathMatchingResourcePatternResolver`的类会将该路径下所有的.class文件都加载进来，然后遍历判断是不是有@Component注解，如果有的话，就是我们要装载的BeanDefinition。大致过程就是这样的了。   
>
> **3、第三个过程：注册BeanDefinition**
>
> > 这个过程通过调用上文提到的BeanDefinitionRegister接口的实现来完成。这个注册过程把载入过程中解析得到的BeanDefinition向IoC容器进行注册。通过上文的分析，我们可以看到，在IoC容器中将BeanDefinition注入到一个ConcurrentHashMap中，IoC容器就是通过这个HashMap来持有这些BeanDefinition数据的。比如DefaultListableBeanFactory 中的beanDefinitionMap属性。







#### 2.5.4.1、resource定位  

> 执行bean工厂后置处理器

```java
	protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 后置处理注册代表  执行bean工厂后置处理器 ，传入bean工厂和 bean工厂后置处理器
		PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

		if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}
	}

```

> `PostProcessorRegistrationDelegate`类    后置处理注册代表

```java

    public static void invokeBeanFactoryPostProcessors(
        ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {
        ...
            //有相当多的代码，重点看这个    
            invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
        ...
    }



    // PostProcessorRegistrationDelegate类
    private static void invokeBeanDefinitionRegistryPostProcessors(
        Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

        for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
            //bean定义注册后置处理器执行方法 =》 处理bean定义注册
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
    }

}
```





> `ConfigurationClassPostProcessor`类，

```java
public class ConfigurationClassPostProcessor implements BeanDefinitionRegistryPostProcessor,
PriorityOrdered, ResourceLoaderAware, BeanClassLoaderAware, EnvironmentAware {


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        int registryId = System.identityHashCode(registry);
        if (this.registriesPostProcessed.contains(registryId)) {
            throw new IllegalStateException(
                "postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);
        }
        if (this.factoriesPostProcessed.contains(registryId)) {
            throw new IllegalStateException(
                "postProcessBeanFactory already called on this post-processor against " + registry);
        }
        this.registriesPostProcessed.add(registryId);

	
        //执行
        processConfigBeanDefinitions(registry);
    }




    public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
        ...
            do {
                //配置类解析器，开始解析
                parser.parse(candidates);
                parser.validate();
                ...
            }
        ...
    }

}



```





> `ConfigurationClassParser`，    
>
> 在前面的`prepareContext()`方法中，我们详细介绍了我们的主类是如何一步步的封装成`AnnotatedGenericBeanDefinition`，并注册进IoC容器的`beanDefinitionMap`中的。

```java
class ConfigurationClassParser {

    public void parse(Set<BeanDefinitionHolder> configCandidates) {
        for (BeanDefinitionHolder holder : configCandidates) {
            BeanDefinition bd = holder.getBeanDefinition();
            try {
                 // 如果是SpringBoot项目进来的，bd其实就是前面主类封装成的 
                //AnnotatedGenericBeanDefinition（AnnotatedBeanDefinition接口的实现类）
                if (bd instanceof AnnotatedBeanDefinition) {
                    parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
                }
                else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
                    parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
                }
                else {
                    parse(bd.getBeanClassName(), holder.getBeanName());
                }
            }
            catch (BeanDefinitionStoreException ex) {
                throw ex;
            }
            catch (Throwable ex) {
                throw new BeanDefinitionStoreException(
                    "Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
            }
        }

        // 加载默认的配置---》（对springboot项目来说这里就是自动装配的入口了,SpringBoot各种starter是如何一步步的实现自动装配的
        this.deferredImportSelectorHandler.process();
    }



```

![image-20200702170935072](D:\study\HealerJean.github.io\blogImages\image-20200702170935072.png)



> `ConfigurationClassParser类`

```java



private static final Predicate<String> DEFAULT_EXCLUSION_FILTER = className ->
    (className.startsWith("java.lang.annotation.") || className.startsWith("org.springframework.stereotype."));

protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException {
    processConfigurationClass(new ConfigurationClass(metadata, beanName), DEFAULT_EXCLUSION_FILTER);
}




protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) throws IOException {

     ...

    //递归地处理配置类及其父类层次结构。
    SourceClass sourceClass = asSourceClass(configClass, filter);
    do {
        //递归处理Bean，如果有父类，递归处理，直到顶层父类
        sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);
    }
    while (sourceClass != null);

    this.configurationClasses.put(configClass, configClass);
}







@Nullable
protected final SourceClass doProcessConfigurationClass(
    ConfigurationClass configClass, SourceClass sourceClass, Predicate<String> filter)
    throws IOException {

    if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
        // 首先递归处理内部类，（SpringBoot项目的主类一般没有内部类）
        processMemberClasses(configClass, sourceClass, filter);
    }

    // 获取@PropertySource注解，解析该注解并将该注解指定的properties配置文件中的值存储到Spring的 Environment中，Environment接口提供方法去读取配置文件中的值，参数是properties文件中定义的key值。
    for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
        sourceClass.getMetadata(), PropertySources.class,
        org.springframework.context.annotation.PropertySource.class)) {
        if (this.environment instanceof ConfigurableEnvironment) {
            processPropertySource(propertySource);
        }
        else {
            logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
                        "]. Reason: Environment must implement ConfigurableEnvironment");
        }
    }

    // 根据 @ComponentScan 注解，扫描项目中的Bean（SpringBoot 启动类上有该注解）
    Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
        sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
    if (!componentScans.isEmpty() &&
        !this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
        for (AnnotationAttributes componentScan : componentScans) {
            
            // 立即执行扫描，（SpringBoot项目为什么是从主类所在的包扫描，这就是关键了）
            Set<BeanDefinitionHolder> scannedBeanDefinitions =
                this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
            
            // Check the set of scanned definitions for any further config classes and parse recursively if needed
            // 这里就是扫描出来的所有的 BeanDefinitionHolder 里面封装的BeanDefinition
            for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
                BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
                if (bdCand == null) {
                    bdCand = holder.getBeanDefinition();
                }
                
                // 检查是否是ConfigurationClass（是否有@configuration/@component两个注解），如果是，递归查找该类相关联的配置类。
                //因为当Spring扫描到需要加载的类会进一步判断每一个类是否满足是@Component/@Configuration注解的类，如果满足会递归调用parse()方法，查找其相关的类。
                if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
                    parse(bdCand.getBeanClassName(), holder.getBeanName());
                }
            }
        }
    }

    //递归处理 @Import 注解（SpringBoot项目中经常用的各种@Enable*** 注解基本都是封装的@Import，并加载该注解指定的类
    processImports(configClass, sourceClass, getImports(sourceClass), filter, true);

    // Process any @ImportResource annotations
    AnnotationAttributes importResource =
        AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
    if (importResource != null) {
        String[] resources = importResource.getStringArray("locations");
        Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
        for (String resource : resources) {
            String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
            configClass.addImportedResource(resolvedResource, readerClass);
        }
    }

    // Process individual @Bean methods
    Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
    for (MethodMetadata methodMetadata : beanMethods) {
        configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
    }

    // Process default methods on interfaces
    processInterfaces(configClass, sourceClass);

    // Process superclass, if any
    if (sourceClass.getMetadata().hasSuperClass()) {
        String superclass = sourceClass.getMetadata().getSuperClassName();
        if (superclass != null && !superclass.startsWith("java") &&
            !this.knownSuperclasses.containsKey(superclass)) {
            this.knownSuperclasses.put(superclass, configClass);
            // Superclass found, return its annotation metadata and recurse
            return sourceClass.getSuperClass();
        }
    }

    // No superclass -> processing is complete
    return null;
}

```



> 递归处理内部类，（SpringBoot项目的主类一般没有内部类）

```java


private void processMemberClasses(ConfigurationClass configClass, SourceClass sourceClass,
                                  Predicate<String> filter) throws IOException {

    Collection<SourceClass> memberClasses = sourceClass.getMemberClasses();
    if (!memberClasses.isEmpty()) {
        List<SourceClass> candidates = new ArrayList<>(memberClasses.size());
        for (SourceClass memberClass : memberClasses) {
            if (ConfigurationClassUtils.isConfigurationCandidate(memberClass.getMetadata()) &&
                !memberClass.getMetadata().getClassName().equals(configClass.getMetadata().getClassName())) {
                candidates.add(memberClass);
            }
        }
        OrderComparator.sort(candidates);
        for (SourceClass candidate : candidates) {
            if (this.importStack.contains(configClass)) {
                this.problemReporter.error(new CircularImportProblem(configClass, this.importStack));
            }
            else {
                this.importStack.push(configClass);
                try {
                    processConfigurationClass(candidate.asConfigClass(configClass), filter);
                }
                finally {
                    this.importStack.pop();
                }
            }
        }
    }
}
```

![image-20200702180017842](D:\study\HealerJean.github.io\blogImages\image-20200702180017842.png)



```java
// ComponentScanAnnotationParser类
public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,
                                                                                componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);
    ...
        // 根据 declaringClass （如果是SpringBoot项目，则参数为主类的全路径名）
        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(declaringClass));
        }
    ...
        // 根据basePackages扫描类
        return scanner.doScan(StringUtils.toStringArray(basePackages));
}
```



> `Set<BeanDefinitionHolder>` `scannedBeanDefinitions` =` this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName())`



```java
// ComponentScanAnnotationParser类
public Set<BeanDefinitionHolder> parse(AnnotationAttributes componentScan, final String declaringClass) {
    ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(this.registry,
            componentScan.getBoolean("useDefaultFilters"), this.environment, this.resourceLoader);
    ...
    // 根据 declaringClass （如果是SpringBoot项目，则参数为主类的全路径名）
    if (basePackages.isEmpty()) {
        basePackages.add(ClassUtils.getPackageName(declaringClass));
    }
    ...
    // 根据basePackages扫描类
    return scanner.doScan(StringUtils.toStringArray(basePackages));
}
```



![image-20200708165850704](D:\study\HealerJean.github.io\blogImages\image-20200708165850704.png)







#### 2.5.4.2、`BeanDefinition`的载入

> 接着上面往下看，Spring是如何进行类扫描的。进入doScan()方法   



> 这个方法中有两个比较重要的方法    
>
> 第一个： `Set<BeanDefinition> candidates = findCandidateComponents(basePackage);` 从basePackage中扫描类并解析成BeanDefinition，         
>
> 第二个：拿到所有符合条件的类后在第`registerBeanDefinition(definitionHolder, this.registry); `将该类注册进IoC容器。也就是说在这个方法中完成了IoC容器初始化过程的第二三步，`BeanDefinition的载入`，和`BeanDefinition`的注册。    



```java
// ComponentScanAnnotationParser类
protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
    Assert.notEmpty(basePackages, "At least one base package must be specified");
    Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
    for (String basePackage : basePackages) {
        
        // 从指定的包中扫描需要装载的Bean
        Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
        for (BeanDefinition candidate : candidates) {
            ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(candidate);
            candidate.setScope(scopeMetadata.getScopeName());
            String beanName = this.beanNameGenerator.generateBeanName(candidate, this.registry);
            if (candidate instanceof AbstractBeanDefinition) {
                postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
            }
            if (candidate instanceof AnnotatedBeanDefinition) {
                AnnotationConfigUtils.processCommonDefinitionAnnotations((AnnotatedBeanDefinition) candidate);
            }
            if (checkCandidate(beanName, candidate)) {
                BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
                definitionHolder =
                        AnnotationConfigUtils.applyScopedProxyMode(scopeMetadata, definitionHolder, this.registry);
                beanDefinitions.add(definitionHolder);
                //将该 Bean 注册进 IoC容器（beanDefinitionMap）
                registerBeanDefinition(definitionHolder, this.registry);
            }
        }
    }
    return beanDefinitions;
}
```

![image-20200708170346720](D:\study\HealerJean.github.io\blogImages\image-20200708170346720.png)





> 将`basePackage`拼接成`classpath*:com/healerjean/proj/**/*.class`，      
>
> `getResources(packageSearchPath)`;方法中扫描到了该路径下的所有的类。然后遍历这些Resources，判断该类是不是 `@Component `注解标注的类，并且不是需要排除掉的类，解析成`ScannedGenericBeanDefinition`，该类是BeanDefinition接口的实现类，IoC容器的BeanDefinition载入到这里就结束了。

```java
// ClassPathScanningCandidateComponentProvider类
public Set<BeanDefinition> findCandidateComponents(String basePackage) {
    ...
        else {
            return scanCandidateComponents(basePackage);
        }
}



// ClassPathScanningCandidateComponentProvider类
private Set<BeanDefinition> scanCandidateComponents(String basePackage) {
    Set<BeanDefinition> candidates = new LinkedHashSet<>();
    try {
        //拼接扫描路径，比如：classpath*:com/healerjean/proj/**/*.class
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
            resolveBasePackage(basePackage) + '/' + this.resourcePattern;
        
        //从 packageSearchPath 路径中扫描所有的类
        Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
        boolean traceEnabled = logger.isTraceEnabled();
        boolean debugEnabled = logger.isDebugEnabled();
        for (Resource resource : resources) {
            if (traceEnabled) {
                logger.trace("Scanning " + resource);
            }
            if (resource.isReadable()) {
                try {
                    MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
                    
                    // //判断该类是不是 @Component 注解标注的类，并且不是需要排除掉的类
                    if (isCandidateComponent(metadataReader)) {
                        //析成ScannedGenericBeanDefinition,该类是BeanDefinition接口的实现类，IoC容器的BeanDefinition载入到这里就结束了。
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                        sbd.setResource(resource);
                        sbd.setSource(resource);
                        if (isCandidateComponent(sbd)) {
                            if (debugEnabled) {
                                logger.debug("Identified candidate component class: " + resource);
                            }
                            candidates.add(sbd);
                        } else {
                            if (debugEnabled) {
                                logger.debug("Ignored because not a concrete top-level class: " + resource);
                            }
                        }
                    } else {
                        if (traceEnabled) {
                            logger.trace("Ignored because not matching any filter: " + resource);
                        }
                    }
                } catch (Throwable ex) {
                    throw new BeanDefinitionStoreException(
                        "Failed to read candidate component class: " + resource, ex);
                }
            } else {
                if (traceEnabled) {
                    logger.trace("Ignored because not readable: " + resource);
                }
            }
        }
    } catch (IOException ex) {
        throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
    }
    return candidates;
}
```



![image-20200708170637293](D:\study\HealerJean.github.io\blogImages\image-20200708170637293.png)



![image-20200708170903281](D:\study\HealerJean.github.io\blogImages\image-20200708170903281.png)





#### 2.5.4.3、`BeanDefinition`的注册



> 查看`registerBeanDefinition()`方法。是不是有点眼熟，在前面介绍`prepareContext()`方法时，我们详细介绍了主类的`BeanDefinition`是怎么一步一步的注册进`DefaultListableBeanFactory`的`beanDefinitionMap`中的。在此呢我们就省略1w字吧。完成了`BeanDefinition`的注册，就完成了IoC容器的初始化过程。此时，在使用的IoC容器`DefaultListableFactory`中已经建立了整个Bean的配置信息，而这些`BeanDefinition`已经可以被容器使用了。他们都在`BeanbefinitionMap`里被检索和使用。容器的作用就是对这些信息进行处理和维护。这些信息是容器简历依赖反转的基础。

```java
protected void registerBeanDefinition(BeanDefinitionHolder definitionHolder, BeanDefinitionRegistry registry) {
    BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
}

```



> 到这里IoC容器的初始化过程的三个步骤就梳理完了。当然这只是针对SpringBoot的包扫描的定位方式的BeanDefinition的定位，加载，和注册过程。前面我们说过，还有两种方式@Import和SPI扩展实现的starter的自动装配。



#### 2.5.4.4、@Import注解的解析过程

> 各种`@EnableXXX`注解，很大一部分都是对@Import的二次封装（其实也是为了解耦，比如当`@Import`导入的类发生变化时，我们的业务系统也不需要改任何代码）。



```java
//递归处理 @Import 注解（SpringBoot项目中经常用的各种@Enable*** 注解基本都是封装的@Import，并加载该注解指定的类
processImports(configClass, sourceClass, getImports(sourceClass), filter, true);
```



```java
private Set<SourceClass> getImports(SourceClass sourceClass) throws IOException {
    Set<SourceClass> imports = new LinkedHashSet<>();
    Set<SourceClass> visited = new LinkedHashSet<>();
    collectImports(sourceClass, imports, visited);
    return imports;
}
```



![image-20200708174218646](D:\study\HealerJean.github.io\blogImages\image-20200708174218646.png)













## 2.5、`afterRefresh`：刷新应用上下文后的扩展接口

> 扩展接口，设计模式中的模板方法，默认为空实现。如果有自定义需求，可以重写该方法。比如打印一些启动结束log，或者一些其它后置处理。   



```java
protected void afterRefresh(ConfigurableApplicationContext context,
        ApplicationArguments args) {
}
```



## 2.6、监听器：发布容器启动完成事件  

> 获取`EventPublishingRunListener`监听器，并执行其started方法，并且将创建的Spring容器传进去了，创建一个ApplicationStartedEvent事件，并执行ConfigurableApplicationContext 的publishEvent方法，也就是说这里是在Spring容器中发布事件，并不是在SpringApplication中发布事件，      
>
> 和前面的starting是不同的，前面的starting是直接向SpringApplication中的11个监听器发布启动事件。   



```java
public void started(ConfigurableApplicationContext context) {
    //这里就是获取的EventPublishingRunListener
    Iterator var2 = this.listeners.iterator();

    while(var2.hasNext()) {
        SpringApplicationRunListener listener = (SpringApplicationRunListener)var2.next();
        //执行EventPublishingRunListener的started方法
        listener.started(context);
    }
}

```



```java

public void started(ConfigurableApplicationContext context) {
    //创建ApplicationStartedEvent事件，并且发布事件
    //我们看到是执行的ConfigurableApplicationContext这个容器的publishEvent方法，和前面的starting是不同的
    context.publishEvent(new ApplicationStartedEvent(this.application, this.args, context));
}
```



## 2.7、执行Runners



```java
callRunners(context, applicationArguments);
```



```java
private void callRunners(ApplicationContext context, ApplicationArguments args) {
    List<Object> runners = new ArrayList<Object>();
    //获取容器中所有的ApplicationRunner的Bean实例
    
    runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
    
    //获取容器中所有的CommandLineRunner的Bean实例
    runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
    AnnotationAwareOrderComparator.sort(runners);
    for (Object runner : new LinkedHashSet<Object>(runners)) {
        if (runner instanceof ApplicationRunner) {
            
            //执行ApplicationRunner的run方法
            callRunner((ApplicationRunner) runner, args);
        }
        if (runner instanceof CommandLineRunner) {
            //执行CommandLineRunner的run方法
            callRunner((CommandLineRunner) runner, args);
        }
    }
}
```



**如果是ApplicationRunner的话,则执行如下代码:**

```java
private void callRunner(ApplicationRunner runner, ApplicationArguments args) {
    try {
        runner.run(args);
    } catch (Exception var4) {
        throw new IllegalStateException("Failed to execute ApplicationRunner", var4);
    }
}
```

**如果是CommandLineRunner的话,则执行如下代码:**     

```java
private void callRunner(CommandLineRunner runner, ApplicationArguments args) {
    try {
        runner.run(args.getSourceArgs());
    } catch (Exception var4) {
        throw new IllegalStateException("Failed to execute CommandLineRunner", var4);
    }
}
```



**我们也可以自定义一些ApplicationRunner或者CommandLineRunner，实现其run方法，并注入到Spring容器中,在SpringBoot启动完成后，会执行所有的runner的run方法**



## 2.7、在run方法启动完成前执行监听

```java
try {
    listeners.running(context);
}
catch (Throwable ex) {
    handleRunFailure(context, ex, exceptionReporters, null);
    throw new IllegalStateException(ex);
}
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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 
