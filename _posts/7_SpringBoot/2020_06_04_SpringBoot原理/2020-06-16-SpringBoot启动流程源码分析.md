---
title: SpringBoot启动流程源码分析
date: 2020-06-17 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot启动流程源码分析
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





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
public enum WebApplicationType {

    NONE,
    SERVLET,
    REACTIVE;

    private WebApplicationType() {
    }


    private static final String WEBMVC_INDICATOR_CLASS = "org.springframework.web.servlet.DispatcherServlet";

    private static final String WEBFLUX_INDICATOR_CLASS = "org.springframework.web.reactive.DispatcherHandler";

    private static final String JERSEY_INDICATOR_CLASS = "org.glassfish.jersey.servlet.ServletContainer";

    private static final String SERVLET_APPLICATION_CONTEXT_CLASS = "org.springframework.web.context.WebApplicationContext";

    private static final String REACTIVE_APPLICATION_CONTEXT_CLASS = "org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext";

    static WebApplicationType deduceFromClasspath() {

        if (ClassUtils.isPresent(WEBFLUX_INDICATOR_CLASS, null) && 
            !ClassUtils.isPresent(WEBMVC_INDICATOR_CLASS, null) && 
            !ClassUtils.isPresent(JERSEY_INDICATOR_CLASS, null)) {
            return WebApplicationType.REACTIVE;
        }
        for (String className : SERVLET_INDICATOR_CLASSES) {
            if (!ClassUtils.isPresent(className, null)) {
                return WebApplicationType.NONE;
            }
        }
        return WebApplicationType.SERVLET;
    }

}





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
    
    //根据names来进行实例化
    List<T> instances = createSpringFactoriesInstances(type, parameterTypes, classLoader, args, names);
    
    //对实例进行排序
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


    // 第一步：获取并启动监听器
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();
    try {
        ApplicationArguments applicationArguments = new DefaultApplicationArguments(args);

        // 第二步：根据SpringApplicationRunListeners以及参数来准备环境
        ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
        configureIgnoreBeanInfo(environment);


        // 准备Banner打印器 - 就是启动Spring Boot的时候打印在console上的ASCII艺术字体
        Banner printedBanner = printBanner(environment);


        // 第三步：创建Spring容器
        context = createApplicationContext();


        exceptionReporters = getSpringFactoriesInstances(SpringBootExceptionReporter.class,
                                                         new Class[] { ConfigurableApplicationContext.class }, context);


        // 第四步：Spring容器前置处理
        prepareContext(context, environment, listeners, applicationArguments, printedBanner);

        // 第五步：刷新容器
        refreshContext(context);


        // 第六步：Spring容器后置处理
        afterRefresh(context, applicationArguments);
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





## 2.3、创建容器 

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



## 2.4、Spring容器前置处理 （非常关键） 

> 这一步主要是在容器刷新之前的准备动作。包含一个非常关键的操作：**将启动类注入容器，为后续开启自动化配置奠定基础。**    



```java
prepareContext(context, environment, listeners, applicationArguments, printedBanner);
```



```java
private void prepareContext(ConfigurableApplicationContext context,
        ConfigurableEnvironment environment, SpringApplicationRunListeners listeners,
        ApplicationArguments applicationArguments, Banner printedBanner) {
    
    //设置容器环境，包括各种变量
    context.setEnvironment(environment);
    
    //执行容器后置处理
    postProcessApplicationContext(context);
    
    //执行容器中的ApplicationContextInitializer（包括 spring.factories和自定义的实例）
    applyInitializers(context);
    
　　//发送容器已经准备好的事件，通知各监听器
    listeners.contextPrepared(context);

    //注册启动参数bean，这里将容器指定的参数封装成bean，注入容器
    context.getBeanFactory().registerSingleton("springApplicationArguments",
            applicationArguments);
    
    //设置banner
    if (printedBanner != null) {
        context.getBeanFactory().registerSingleton("springBootBanner", printedBanner);
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



### 2.4.1、调用初始化器

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



### 2.4.2、加载启动指定类（重点）    

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
		if (source instanceof Class<?>) {
			return load((Class<?>) source);
		}
		if (source instanceof Resource) {
			return load((Resource) source);
		}
		if (source instanceof Package) {
			return load((Package) source);
		}
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
    
    //判断是否被注解@Component注释
    if (isComponent(source)) {
        
        //将启动类bean信息存入beanDefinitionMap(DefaultListableBeanFactory 类)，也就是将SpringBoot_Application.class存入了beanDefinitionMap
        this.annotatedReader.register(source);
        return 1;
    }
    return 0;
}
```



### 2.4.3、**通知监听器，容器已准备就绪**

```java
//发布容器已加载事件。
listeners.contextLoaded(context);
```



## 2.5、刷新容器



```java
protected void refresh(ApplicationContext applicationContext) {
    Assert.isInstanceOf(AbstractApplicationContext.class, applicationContext);
    //调用创建的容器applicationContext中的refresh()方法
    ((AbstractApplicationContext)applicationContext).refresh();
}
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        /**
         * 刷新上下文环境
         */
        prepareRefresh();

        /**
         * 初始化BeanFactory，解析XML，相当于之前的XmlBeanFactory的操作，
         */
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        /**
         * 为上下文准备BeanFactory，即对BeanFactory的各种功能进行填充，如常用的注解@Autowired @Qualifier等
         * 添加ApplicationContextAwareProcessor处理器
         * 在依赖注入忽略实现*Aware的接口，如EnvironmentAware、ApplicationEventPublisherAware等
         * 注册依赖，如一个bean的属性中含有ApplicationEventPublisher(beanFactory)，则会将beanFactory的实例注入进去
         */
        prepareBeanFactory(beanFactory);

        try {
            
            /**
             * 提供子类覆盖的额外处理，即子类处理自定义的BeanFactoryPostProcess
             */
            postProcessBeanFactory(beanFactory);

            /**
             * 激活各种BeanFactory处理器,包括BeanDefinitionRegistryBeanFactoryPostProcessor和普通的BeanFactoryPostProcessor
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



## 2.5、Spring容器后置处理

> 扩展接口，设计模式中的模板方法，默认为空实现。如果有自定义需求，可以重写该方法。比如打印一些启动结束log，或者一些其它后置处理。   



```
protected void afterRefresh(ConfigurableApplicationContext context,
        ApplicationArguments args) {
}
```



## 2.6、发出结束执行的事件  

> 获取EventPublishingRunListener监听器，并执行其started方法，并且将创建的Spring容器传进去了，创建一个ApplicationStartedEvent事件，并执行ConfigurableApplicationContext 的publishEvent方法，也就是说这里是在Spring容器中发布事件，并不是在SpringApplication中发布事件，      
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
