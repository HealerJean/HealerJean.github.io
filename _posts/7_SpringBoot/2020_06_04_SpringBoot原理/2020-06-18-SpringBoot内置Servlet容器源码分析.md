---
title: SpringBoot内置Servlet容器源码分析
date: 2020-06-18 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot内置Servlet容器源码分析
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          







# 1、`Servlet`容器的使用

## 1.1、默认的`Servlet`容器

> `Spring Boot`默认使用`Tomcat`作为嵌入式的Servlet容器，只要引入了`spring-boot-start-web`依赖，则默认是用`Tomcat`作为`Servle`t容器：    
>
> `@EnableAutoConfiguration ` 会根据依赖中jar包，会自动对二者进行配置

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```



`spring-boot-starter-web` 包

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd" xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
  <version>2.3.0.RELEASE</version>
  <name>spring-boot-starter-web</name>
  <description>Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container</description>
  <url>https://spring.io/projects/spring-boot</url>
  <organization>
    <name>Pivotal Software, Inc.</name>
    <url>https://spring.io</url>
  </organization>
  <licenses>
    <license>
      <name>Apache License, Version 2.0</name>
      <url>http://www.apache.org/licenses/LICENSE-2.0</url>
    </license>
  </licenses>
  <developers>
    <developer>
      <name>Pivotal</name>
      <email>info@pivotal.io</email>
      <organization>Pivotal Software, Inc.</organization>
      <organizationUrl>https://www.spring.io</organizationUrl>
    </developer>
  </developers>
  <scm>
    <connection>scm:git:git://github.com/spring-projects/spring-boot.git</connection>
    <developerConnection>scm:git:ssh://git@github.com/spring-projects/spring-boot.git</developerConnection>
    <url>https://github.com/spring-projects/spring-boot</url>
  </scm>
  <issueManagement>
    <system>GitHub</system>
    <url>https://github.com/spring-projects/spring-boot/issues</url>
  </issueManagement>
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>2.3.0.RELEASE</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <version>2.3.0.RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-json</artifactId>
      <version>2.3.0.RELEASE</version>
      <scope>compile</scope>
    </dependency>
      
     <!-- tomcat -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-tomcat</artifactId>
      <version>2.3.0.RELEASE</version>
      <scope>compile</scope>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <scope>compile</scope>
    </dependency>
    <dependency>
        
       <!-- spring-webmv -->
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <scope>compile</scope>
    </dependency>
  </dependencies>
</project>
```



> Spring Boot默认支持Tomcat，Jetty，和Undertow作为底层容器。如图：       
>
> 而Spring Boot默认使用Tomcat，一旦引入spring-boot-starter-web模块，就默认使用Tomcat容器。

![image-20200622140907215](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200622140907215.png)





## 1.2、切换`servlet`容器

> 1、将tomcat依赖移除掉         
>
> 2、引入其他Servlet容器依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <!--移除spring-boot-starter-web中的tomcat-->
            <artifactId>spring-boot-starter-tomcat</artifactId>
            <groupId>org.springframework.boot</groupId>
        </exclusion>
    </exclusions>
</dependency>


<!--引入jetty-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```





# 2、Servlet容器自动装配原理

> `ServletWebServerFactoryAutoConfiguration`在该类在**spring-boot-autoconfigure.jar中的web模块**可以找到，通过自动装配可以将该配置类装入IOC容器中    



```
org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration,\
```

![image-20200622141345182](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200622141345182.png)





##   2.1、`ServletWebServerFactoryAutoConfiguration`

```java
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnClass(ServletRequest.class)
@ConditionalOnWebApplication(type = Type.SERVLET) //// 在Web环境是servlet才会起作用
@EnableConfigurationProperties(ServerProperties.class)
@Import({ ServletWebServerFactoryAutoConfiguration.BeanPostProcessorsRegistrar.class,

         //在这个自动配置类中配置了三个容器工厂，分别是(往下看)：
         ServletWebServerFactoryConfiguration.EmbeddedTomcat.class,  
         ServletWebServerFactoryConfiguration.EmbeddedJetty.class,
         ServletWebServerFactoryConfiguration.EmbeddedUndertow.class })
public class ServletWebServerFactoryAutoConfiguration {


    @Bean
    public ServletWebServerFactoryCustomizer servletWebServerFactoryCustomizer(ServerProperties serverProperties) {
        return new ServletWebServerFactoryCustomizer(serverProperties);
    }

    //当tomcat类存在的时候， 配置一些tomcat的属性，比如端口等
    @Bean
    @ConditionalOnClass(name = "org.apache.catalina.startup.Tomcat")
    public TomcatServletWebServerFactoryCustomizer tomcatServletWebServerFactoryCustomizer(
        ServerProperties serverProperties) {
        return new TomcatServletWebServerFactoryCustomizer(serverProperties);
    }



    ……



}

```



> 在这个自动配置类中配置了三个容器工厂的Bean，分别是：    
>
> `TomcatServletWebServerFactory`   
>
> `JettyServletWebServerFactory`    
>
> `UndertowServletWebServerFactory `     
>
> 这三者都是在没有`ServletWebServerFactory`的起航情况下才会创建



```java
@Configuration(proxyBeanMethods = false)
class ServletWebServerFactoryConfiguration {

    @Configuration(proxyBeanMethods = false)
    //Tomcat类和Servlet类必须在classloader中存在
    @ConditionalOnClass({ Servlet.class, Tomcat.class, UpgradeProtocol.class })
    //当前Spring容器中不存在EmbeddedServletContainerFactory类型的实例
    @ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    static class EmbeddedTomcat {

        @Bean
        TomcatServletWebServerFactory tomcatServletWebServerFactory(
            ObjectProvider<TomcatConnectorCustomizer> connectorCustomizers,
            ObjectProvider<TomcatContextCustomizer> contextCustomizers,
            ObjectProvider<TomcatProtocolHandlerCustomizer<?>> protocolHandlerCustomizers) {
            TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
            factory.getTomcatConnectorCustomizers()
                .addAll(connectorCustomizers.orderedStream().collect(Collectors.toList()));
            factory.getTomcatContextCustomizers()
                .addAll(contextCustomizers.orderedStream().collect(Collectors.toList()));
            factory.getTomcatProtocolHandlerCustomizers()
                .addAll(protocolHandlerCustomizers.orderedStream().collect(Collectors.toList()));
            return factory;
        }

    }



    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ Servlet.class, Server.class, Loader.class, WebAppContext.class })
    @ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    static class EmbeddedJetty {

        @Bean
        JettyServletWebServerFactory JettyServletWebServerFactory(
            ObjectProvider<JettyServerCustomizer> serverCustomizers) {
            JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
            factory.getServerCustomizers().addAll(serverCustomizers.orderedStream().collect(Collectors.toList()));
            return factory;
        }

    }


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({ Servlet.class, Undertow.class, SslClientAuthMode.class })
    @ConditionalOnMissingBean(value = ServletWebServerFactory.class, search = SearchStrategy.CURRENT)
    static class EmbeddedUndertow {

        @Bean
        UndertowServletWebServerFactory undertowServletWebServerFactory(
            ObjectProvider<UndertowDeploymentInfoCustomizer> deploymentInfoCustomizers,
            ObjectProvider<UndertowBuilderCustomizer> builderCustomizers) {
            UndertowServletWebServerFactory factory = new UndertowServletWebServerFactory();
            factory.getDeploymentInfoCustomizers()
                .addAll(deploymentInfoCustomizers.orderedStream().collect(Collectors.toList()));
            factory.getBuilderCustomizers().addAll(builderCustomizers.orderedStream().collect(Collectors.toList()));
            return factory;
        }

    }

}

```





## 2.2、`ServletWebServerFactory`

> 获取`WebServer`容器，实现这个方法的3个工厂如下

```java
public interface ServletWebServerFactory {

   WebServer getWebServer(ServletContextInitializer... initializers);

}
```



![image-20200622143753592](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200622143753592.png)





## 2.2、`TomcatServletWebServerFactory`

> `getWebServer` 方法会返回web服务并启动tomcaty

```java
public class TomcatServletWebServerFactory extends AbstractServletWebServerFactory
		implements ConfigurableTomcatWebServerFactory, ResourceLoaderAware {

		//other code

	@Override
	public WebServer getWebServer(ServletContextInitializer... initializers) {
		if (this.disableMBeanRegistry) {
			Registry.disableRegistry();
		}
        
        //创建一个Tomcat
		Tomcat tomcat = new Tomcat();
		File baseDir = (this.baseDirectory != null) ? this.baseDirectory : createTempDir("tomcat");
		tomcat.setBaseDir(baseDir.getAbsolutePath());
		Connector connector = new Connector(this.protocol);
		connector.setThrowOnFailure(true);
		tomcat.getService().addConnector(connector);
		customizeConnector(connector);
		tomcat.setConnector(connector);
		tomcat.getHost().setAutoDeploy(false);
		configureEngine(tomcat.getEngine());
		for (Connector additionalConnector : this.additionalTomcatConnectors) {
			tomcat.getService().addConnector(additionalConnector);
		}
		prepareContext(tomcat.getHost(), initializers);
        
        
        //包装tomcat对象，返回一个Tomcat容器，内部会启动该tomcat容器
		return getTomcatWebServer(tomcat);
	}

```



> 创建一个Tomcat的对象，并设置一些属性配置，最后调用**getTomcatEmbeddedServletContainer(tomcat)方法，内部会启动tomcat，**

```java
protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
    return new TomcatWebServer(tomcat, getPort() >= 0, getShutdown());
}

```

```java
public class TomcatWebServer implements WebServer {

    public TomcatWebServer(Tomcat tomcat, boolean autoStart, Shutdown shutdown) {
        Assert.notNull(tomcat, "Tomcat Server must not be null");
        this.tomcat = tomcat;
        this.autoStart = autoStart;
        this.gracefulShutdown = (shutdown == Shutdown.GRACEFUL) ? new GracefulShutdown(tomcat) : null;
        
         //初始化嵌入式Tomcat容器，并启动Tomcat
        initialize();
    }

    private void initialize() throws WebServerException {
        logger.info("Tomcat initialized with port(s): " + getPortsDescription(false));
        synchronized (this.monitor) {
            try {
                addInstanceIdToEngineName();

                Context context = findContext();
                context.addLifecycleListener((event) -> {
                    if (context.equals(event.getSource()) && Lifecycle.START_EVENT.equals(event.getType())) {
                        // Remove service connectors so that protocol binding doesn't
                        // happen when the service is started.
                        removeServiceConnectors();
                    }
                });

                // Start the server to trigger initialization listeners
                //启动tomcat
                this.tomcat.start();

                // We can re-throw failure exception directly in the main thread
                rethrowDeferredStartupExceptions();

                try {
                    ContextBindings.bindClassLoader(context, context.getNamingToken(), getClass().getClassLoader());
                }
                catch (NamingException ex) {
                    // Naming is not enabled. Continue
                }

                // Unlike Jetty, all Tomcat threads are daemon threads. We create a
                // blocking non-daemon to stop immediate shutdown
                startDaemonAwaitThread();
            }
            catch (Exception ex) {
                stopSilently();
                destroySilently();
                throw new WebServerException("Unable to start embedded Tomcat", ex);
            }
        }
    }

}

```





# 3、Servlet容器启动过程

## 3.1、通过`SpringBoot`启动分析

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



### 3.1.1、回顾第三步，创建Spring容器

>             根据应用环境，创建不同的IOC容器，默认情况下，返回的Spring容器是    `AnnotationConfigServletWebServerApplicationContext`	




```java
public static final String DEFAULT_CONTEXT_CLASS = "org.springframework.context."
    + "annotation.AnnotationConfigApplicationContext";

/**
	 * The class name of application context that will be used by default for web
	 * environments.
	 */
public static final String DEFAULT_SERVLET_WEB_CONTEXT_CLASS = "org.springframework.boot."
    + "web.servlet.context.AnnotationConfigServletWebServerApplicationContext";

/**
	 * The class name of application context that will be used by default for reactive web
	 * environments.
	 */
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



### 3.1.2、再看第五步，刷新容器

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



## 3.2、获取Servlet容器工厂  

### 3.2.1、观察上面的额`onRefresh()`方法

> 很明显抽象父类`AbstractApplicationContext`中的`onRefresh`是一个空方法，并且使用protected修饰，也就是其子类可以重写`onRefresh`方法，那我们看看其子类



![image-20200622150157197](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200622150157197.png)



```java
@Override
protected void onRefresh() {
   super.onRefresh();
   try {
       //创建web服务
      createWebServer();
   }
   catch (Throwable ex) {
      throw new ApplicationContextException("Unable to start web server", ex);
   }
}
```





> 这里非常棒，如果是本地Tomcat启动，则进入else if 

```java
private void createWebServer() {
    WebServer webServer = this.webServer;
    ServletContext servletContext = getServletContext();
    if (webServer == null && servletContext == null) {
        
        //获取ServletWebServerFactory 
        ServletWebServerFactory  factory = getWebServerFactory();
        
        //从工厂中获取Web服务，包装tomcat对象，返回一个Tomcat容器，内部会启动该tomcat容器
        this.webServer = factory.getWebServer(getSelfInitializer());
        getBeanFactory().registerSingleton("webServerGracefulShutdown",
                                           new WebServerGracefulShutdownLifecycle(this.webServer));
        getBeanFactory().registerSingleton("webServerStartStop",
                                           new WebServerStartStopLifecycle(this, this.webServer));
    }
    else if (servletContext != null) {
        try {
            getSelfInitializer().onStartup(servletContext);
        }
        catch (ServletException ex) {
            throw new ApplicationContextException("Cannot initialize servlet context", ex);
        }
    }
    initPropertySources();
}
```



> 从Spring的IOC容器中获取`ServletWebServerFactory.class`类型的Bean

```java
protected ServletWebServerFactory getWebServerFactory() {
    //从Spring的IOC容器中获取ServletWebServerFactory.class类型的BeanName
    String[] beanNames = getBeanFactory().getBeanNamesForType(ServletWebServerFactory.class);
    if (beanNames.length == 0) {
        throw new ApplicationContextException("Unable to start ServletWebServerApplicationContext due to missing "
                                              + "ServletWebServerFactory bean.");
    }
    if (beanNames.length > 1) {
        throw new ApplicationContextException("Unable to start ServletWebServerApplicationContext due to multiple "
                                              + "ServletWebServerFactory beans : " + StringUtils.arrayToCommaDelimitedString(beanNames));
    }
    //调用beanName获取实例
    return getBeanFactory().getBean(beanNames[0], ServletWebServerFactory.class);
}

```





# 4、SpringBoot实现MVC

## 4.1、`ServletRegistrationBean`、`FilterRegistrationBean`、`ServletListenerRegistrationBean`

> 我们发现`RegistrationBean` 实现了`ServletContextInitializer`这个接口，并且有一个`onStartup`方法，`ServletRegistrationBean`、`FilterRegistrationBean`、S`ervletListenerRegistrationBean`都实现了**onStartup方法。 **     
>
> `ServletContextInitializer`是 Servlet 容器初始化的时候，提供的初始化接口。所以，Servlet 容器初始化会获取并触发所有的`FilterRegistrationBean`、`FilterRegistrationBean`、`ServletListenerRegistrationBean`实例中`onStartup方法`



### 4.1.1、ServletRegistrationBean 

```java
public class ServletRegistrationBean<T extends Servlet> extends DynamicRegistrationBean<ServletRegistration.Dynamic> {

	private static final String[] DEFAULT_MAPPINGS = { "/*" };
    //存放目标Servlet实例
	private T servlet;
    
    //存放Servlet的urlMapping
	private Set<String> urlMappings = new LinkedHashSet
        
	private boolean alwaysMapUrl = true;
	private int loadOnStartup = -1;
	private MultipartConfigElement multipartConfig;


	//other code

}
```



```java
public abstract class DynamicRegistrationBean<D extends Registration.Dynamic> extends RegistrationBean {

    
}

public abstract class RegistrationBean implements ServletContextInitializer, Ordered {

    
}
```



```java
public interface ServletContextInitializer {

	void onStartup(ServletContext servletContext) throws ServletException;

}

```



### 4.1.2、`FilterRegistrationBean`



```java
public class FilterRegistrationBean<T extends Filter> extends AbstractFilterRegistrationBean<T> {

	private T filter;

	public FilterRegistrationBean() {
	}


	public FilterRegistrationBean(T filter, ServletRegistrationBean<?>... servletRegistrationBeans) {
		super(servletRegistrationBeans);
		Assert.notNull(filter, "Filter must not be null");
		this.filter = filter;
	}

	@Override
	public T getFilter() {
		return this.filter;
	}

	public void setFilter(T filter) {
		Assert.notNull(filter, "Filter must not be null");
		this.filter = filter;
	}

}

```



```java
public abstract class AbstractFilterRegistrationBean<T extends Filter> extends DynamicRegistrationBean<Dynamic> {


}
public abstract class DynamicRegistrationBean<D extends Registration.Dynamic> extends RegistrationBean {

    
}
public abstract class RegistrationBean implements ServletContextInitializer, Ordered {

    
}


```



```java
public interface ServletContextInitializer {

	void onStartup(ServletContext servletContext) throws ServletException;

}

```



### 4.1.3、`ServletListenerRegistrationBean`

```java
public class ServletListenerRegistrationBean<T extends EventListener> extends RegistrationBean {

	private static final Set<Class<?>> SUPPORTED_TYPES;

	static {
		Set<Class<?>> types = new HashSet<>();
		types.add(ServletContextAttributeListener.class);
		types.add(ServletRequestListener.class);
		types.add(ServletRequestAttributeListener.class);
		types.add(HttpSessionAttributeListener.class);
		types.add(HttpSessionListener.class);
		types.add(ServletContextListener.class);
		SUPPORTED_TYPES = Collections.unmodifiableSet(types);
	}

    //存放了目标listener
	private T listener;

	
    
	public ServletListenerRegistrationBean() {
	}

    
	public ServletListenerRegistrationBean(T listener) {
		Assert.notNull(listener, "Listener must not be null");
		Assert.isTrue(isSupportedType(listener), "Listener is not of a supported type");
		this.listener = listener;
	}

    
	public void setListener(T listener) {
		Assert.notNull(listener, "Listener must not be null");
		Assert.isTrue(isSupportedType(listener), "Listener is not of a supported type");
		this.listener = listener;
	}

	
    
	public T getListener() {
		return this.listener;
	}

	@Override
	protected String getDescription() {
		Assert.notNull(this.listener, "Listener must not be null");
		return "listener " + this.listener;
	}

	@Override
	protected void register(String description, ServletContext servletContext) {
		try {
			servletContext.addListener(this.listener);
		}
		catch (RuntimeException ex) {
			throw new IllegalStateException("Failed to add listener '" + this.listener + "' to servlet context", ex);
		}
	}


    
	public static boolean isSupportedType(EventListener listener) {
		for (Class<?> type : SUPPORTED_TYPES) {
			if (ClassUtils.isAssignableValue(type, listener)) {
				return true;
			}
		}
		return false;
	}

	
    
	public static Set<Class<?>> getSupportedTypes() {
		return SUPPORTED_TYPES;
	}

}

```



```java
public interface ServletContextInitializer {

	void onStartup(ServletContext servletContext) throws ServletException;

}

```



## 4.2、Servlet容器启动观察  

> `createWebServer() `创建tomcat并启动tomcat，并返回`WebServer` （`TomcatWebServer`）

```java
private void createWebServer() {
    WebServer webServer = this.webServer;
    ServletContext servletContext = getServletContext();
    if (webServer == null && servletContext == null) {

        //获取ServletWebServerFactory 
        ServletWebServerFactory  factory = getWebServerFactory();

        //从工厂中获取Web服务，包装tomcat对象，返回一个Tomcat容器，内部会启动该tomcat容器
        this.webServer = factory.getWebServer(getSelfInitializer());
        getBeanFactory().registerSingleton("webServerGracefulShutdown",
                                           new WebServerGracefulShutdownLifecycle(this.webServer));
        getBeanFactory().registerSingleton("webServerStartStop",
                                           new WebServerStartStopLifecycle(this, this.webServer));
    }
    else if (servletContext != null) {
        try {
            getSelfInitializer().onStartup(servletContext);
        }
        catch (ServletException ex) {
            throw new ApplicationContextException("Cannot initialize servlet context", ex);
        }
    }
    initPropertySources();
}
```





> 关键代码在`getSelfInitializer()`获取到所有的`Initializer`，传入Servlet容器中，那核心就在`getSelfInitializer()`方法，获取Servilet初始化器

```java
	private org.springframework.boot.web.servlet.ServletContextInitializer getSelfInitializer() {
		return this::selfInitialize;
	}

	private void selfInitialize(ServletContext servletContext) throws ServletException {
		prepareWebApplicationContext(servletContext);
		registerApplicationScope(servletContext);
		WebApplicationContextUtils.registerEnvironmentBeans(getBeanFactory(), servletContext);
        
         //这里便是获取所有的 ServletContextInitializer 实现类，会获取所有的注册组件
		for (ServletContextInitializer beans : getServletContextInitializerBeans()) {
            
             //执行所有ServletContextInitializer的onStartup方法，将以上bean注册的
			beans.onStartup(servletContext);
		}
	}
```



![image-20200623145102076](D:\study\HealerJean.github.io\blogImages\image-20200623145102076.png)



### 4.2.1、获取所有的`ServletContextInitializer`

```java
protected Collection<ServletContextInitializer> getServletContextInitializerBeans() {
    return new ServletContextInitializerBeans(getBeanFactory());
}
```



> 我们看到`ServletContextInitializerBeans` 中有一个存放所有`ServletContextInitializer`的集合`sortedList`，就是在其构造方法中获取所有的`ServletContextInitializer`，并放入`sortedList`集合中，那我们来看看其构造方法的逻辑，

```java
public class ServletContextInitializerBeans extends AbstractCollection<ServletContextInitializer> {

	private static final String DISPATCHER_SERVLET_NAME = "dispatcherServlet";

	private static final Log logger = LogFactory.getLog(ServletContextInitializerBeans.class);

	/**
	 * Seen bean instances or bean names.
	 */
	private final Set<Object> seen = new HashSet<>();

	private final MultiValueMap<Class<?>, ServletContextInitializer> initializers;

	private final List<Class<? extends ServletContextInitializer>> initializerTypes;

    // 可以看到继承了AbstractCollection<ServletContextInitializer>  
    // 下面这个就是具体的东西，存放所有的ServletContextInitializer
	private List<ServletContextInitializer> sortedList;

	@SafeVarargs
	public ServletContextInitializerBeans(ListableBeanFactory beanFactory,
			Class<? extends ServletContextInitializer>... initializerTypes) {
		this.initializers = new LinkedMultiValueMap<>();
		this.initializerTypes = (initializerTypes.length != 0) ? Arrays.asList(initializerTypes)
				: Collections.singletonList(ServletContextInitializer.class);
        
         //执行addServletContextInitializerBeans
		addServletContextInitializerBeans(beanFactory);
        
       //执行addAdaptableBeans
		addAdaptableBeans(beanFactory);
        
        //将上面2个执行的结果，放到sortedList 中去
		List<ServletContextInitializer> sortedInitializers = this.initializers.values().stream()
				.flatMap((value) -> value.stream().sorted(AnnotationAwareOrderComparator.INSTANCE))
				.collect(Collectors.toList());
		this.sortedList = Collections.unmodifiableList(sortedInitializers);
        
		logMappings(this.initializers);
	}
    
    
    
}
```



![image-20200623144849444](D:\study\HealerJean.github.io\blogImages\image-20200623144849444.png)





![image-20200623145003700](D:\study\HealerJean.github.io\blogImages\image-20200623145003700.png)



#### 4.2.1.1、执行`addServletContextInitializerBeans`



> 判断从`Spring`容器中获取的`ServletContextInitializer`类型，如ServletRegistrationBean、FilterRegistrationBean、ServletListenerRegistrationBean，并加入到initializers集合中去，我们再来看构造器中的另外一个方法   

```java
private void addServletContextInitializerBeans(ListableBeanFactory beanFactory) {
    for (Class<? extends ServletContextInitializer> initializerType : this.initializerTypes) {

        ////从Spring容器中获取所有ServletContextInitializer.class 类型的Bean
        for (Entry<String, ? extends ServletContextInitializer> initializerBean : getOrderedBeansOfType(beanFactory,
                                                                                                        initializerType)) {

            //添加到具体的集合中
            addServletContextInitializerBean(initializerBean.getKey(), initializerBean.getValue(), beanFactory);
        }
    }
}

private void addServletContextInitializerBean(String beanName, ServletContextInitializer initializer,
                                              ListableBeanFactory beanFactory) {
    //判断ServletRegistrationBean类型
    if (initializer instanceof ServletRegistrationBean) {
        Servlet source = ((ServletRegistrationBean<?>) initializer).getServlet();
        
        //将ServletRegistrationBean加入到集合中
        addServletContextInitializerBean(Servlet.class, beanName, initializer, beanFactory, source);
    }
    
     //判断FilterRegistrationBean类型
    else if (initializer instanceof FilterRegistrationBean) {
        Filter source = ((FilterRegistrationBean<?>) initializer).getFilter();
        
        //将ServletRegistrationBean加入到集合中
        addServletContextInitializerBean(Filter.class, beanName, initializer, beanFactory, source);
    }
    else if (initializer instanceof DelegatingFilterProxyRegistrationBean) {
        String source = ((DelegatingFilterProxyRegistrationBean) initializer).getTargetBeanName();
        addServletContextInitializerBean(Filter.class, beanName, initializer, beanFactory, source);
    }
    else if (initializer instanceof ServletListenerRegistrationBean) {
        EventListener source = ((ServletListenerRegistrationBean<?>) initializer).getListener();
        addServletContextInitializerBean(EventListener.class, beanName, initializer, beanFactory, source);
    }
    else {
        addServletContextInitializerBean(ServletContextInitializer.class, beanName, initializer, beanFactory,
                                         initializer);
    }
}



private void addServletContextInitializerBean(Class<?> type, String beanName, ServletContextInitializer initializer,
                                              ListableBeanFactory beanFactory, Object source) {
    
     //加入到initializers中
    this.initializers.add(type, initializer);
    if (source != null) {
        // Mark the underlying source as seen in case it wraps an existing bean
        this.seen.add(source);
    }
    if (logger.isTraceEnabled()) {
        String resourceDescription = getResourceDescription(beanName, beanFactory);
        int order = getOrder(initializer);
        logger.trace("Added existing " + type.getSimpleName() + " initializer bean '" + beanName + "'; order="
                     + order + ", resource=" + resourceDescription);
    }
}
```



![image-20200623144309405](D:\study\HealerJean.github.io\blogImages\image-20200623144309405.png)





#### 4.2.1.2、执行 `addAdaptableBeans`



> 我们看到先从`beanFactory`获取所有`Servlet.class`和`Filter.class`类型的Bean，然后通过`ServletRegistrationBeanAdapter`和`FilterRegistrationBeanAdapter`两个适配器将`Servlet.class`和`Filter.class`封装成RegistrationBean

```java
protected void addAdaptableBeans(ListableBeanFactory beanFactory) {
    MultipartConfigElement multipartConfig = getMultipartConfig(beanFactory);
    //从beanFactory获取所有Servlet.class和Filter.class类型的Bean，并封装成RegistrationBean对象，加入到集合中
    addAsRegistrationBean(beanFactory, Servlet.class, new ServletRegistrationBeanAdapter(multipartConfig));
    addAsRegistrationBean(beanFactory, Filter.class, new FilterRegistrationBeanAdapter());

    for (Class<?> listenerType : ServletListenerRegistrationBean.getSupportedTypes()) {
        addAsRegistrationBean(beanFactory, EventListener.class, (Class<EventListener>) listenerType,
                              new ServletListenerRegistrationBeanAdapter());
    }
}

protected <T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type,
                                         RegistrationBeanAdapter<T> adapter) {
    addAsRegistrationBean(beanFactory, type, type, adapter);
}

private <T, B extends T> void addAsRegistrationBean(ListableBeanFactory beanFactory, Class<T> type,
                                                    Class<B> beanType, RegistrationBeanAdapter<T> adapter) {
    
      //从Spring容器中获取所有的Servlet.class和Filter.class类型的Bean
    List<Map.Entry<String, B>> entries = getOrderedBeansOfType(beanFactory, beanType, this.seen);
    for (Entry<String, B> entry : entries) {
        String beanName = entry.getKey();
        B bean = entry.getValue();
        if (this.seen.add(bean)) {            
             //创建Servlet.class和Filter.class包装成RegistrationBean对象
             // > 通过`ServletRegistrationBeanAdapter`和`FilterRegistrationBeanAdapter`两个适配器将`Servlet.class`和`Filter.class`封装成RegistrationBean
            RegistrationBean registration = adapter.createRegistrationBean(beanName, bean, entries.size());
            int order = getOrder(bean);
            registration.setOrder(order);
            this.initializers.add(type, registration);
            if (logger.isTraceEnabled()) {
                logger.trace("Created " + type.getSimpleName() + " initializer for bean '" + beanName + "'; order="
                             + order + ", resource=" + getResourceDescription(beanName, beanFactory));
            }
        }
    }
}

```



接口`RegistrationBeanAdapter`

```java
protected interface RegistrationBeanAdapter<T> {
    
    RegistrationBean createRegistrationBean(String name, T source, int totalNumberOfSourceBeans);

}
```



`FilterRegistrationBeanAdapter`

```java
private static class FilterRegistrationBeanAdapter implements RegistrationBeanAdapter<Filter> {

    @Override
    public RegistrationBean createRegistrationBean(String name, Filter source, int totalNumberOfSourceBeans) {
        FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>(source);
        bean.setName(name);
        return bean;
    }

}
```



`ServletRegistrationBeanAdapter`

```java
private static class ServletRegistrationBeanAdapter implements RegistrationBeanAdapter<Servlet> {

    private final MultipartConfigElement multipartConfig;

    ServletRegistrationBeanAdapter(MultipartConfigElement multipartConfig) {
        this.multipartConfig = multipartConfig;
    }

    @Override
    public RegistrationBean createRegistrationBean(String name, Servlet source, int totalNumberOfSourceBeans) {
        String url = (totalNumberOfSourceBeans != 1) ? "/" + name + "/" : "/";
        if (name.equals(DISPATCHER_SERVLET_NAME)) {
            url = "/"; // always map the main dispatcherServlet to "/"
        }
        ServletRegistrationBean<Servlet> bean = new ServletRegistrationBean<>(source, url);
        bean.setName(name);
        bean.setMultipartConfig(this.multipartConfig);
        return bean;
    }

}
```



`ServletListenerRegistrationBeanAdapter`

```java
private static class ServletListenerRegistrationBeanAdapter implements RegistrationBeanAdapter<EventListener> {

    @Override
    public RegistrationBean createRegistrationBean(String name, EventListener source,
                                                   int totalNumberOfSourceBeans) {
        return new ServletListenerRegistrationBean<>(source);
    }

}

```





> 代码中注释很清楚了还是    
>
> 将`Servlet.class`实例封装成`ServletRegistrationBean`对象，       
>
> 将`Filter.class`实例封装成`FilterRegistrationBean`对象，     
>
> 这和我们自己定义`ServletRegistrationBean`对象是一模一样的，现在所有的`ServletRegistrationBean`、`FilterRegistrationBean`
>
> `Servlet.class`、`Filter.class`都添加到`List<ServletContextInitializer>` sortedList这个集合中去了，接着就是遍历这个集合，执行其onStartup方法了   



### 4.2.2、ServletContextInitializer的**onStartup**方法

```java
private org.springframework.boot.web.servlet.ServletContextInitializer getSelfInitializer() {
    return this::selfInitialize;
}

private void selfInitialize(ServletContext servletContext) throws ServletException {
    prepareWebApplicationContext(servletContext);
    registerApplicationScope(servletContext);
    WebApplicationContextUtils.registerEnvironmentBeans(getBeanFactory(), servletContext);
    for (ServletContextInitializer beans : getServletContextInitializerBeans()) {
        beans.onStartup(servletContext);
    }
}
```



> `beans.onStartup(servletContext);`这方法在抽象类`RegistrationBean`

```java
public abstract class RegistrationBean implements ServletContextInitializer, Ordered {


	@Override
	public final void onStartup(ServletContext servletContext) throws ServletException {
		String description = getDescription();
		if (!isEnabled()) {
			logger.info(StringUtils.capitalize(description) + " was not registered (disabled)");
			return;
		}
		register(description, servletContext);
	}
```



![image-20200623160656369](D:\study\HealerJean.github.io\blogImages\image-20200623160656369.png)



#### 4.2.2.1、`servletContext`容器中添加Servlet

![image-20200623161024674](D:\study\HealerJean.github.io\blogImages\image-20200623161024674.png)

```java
@Override
protected final void register(String description, ServletContext servletContext) {
    D registration = addRegistration(description, servletContext);
    if (registration == null) {
        logger.info(StringUtils.capitalize(description) + " was not registered (possibly already registered?)");
        return;
    }
    configure(registration);
}
```



![image-20200623161059002](D:\study\HealerJean.github.io\blogImages\image-20200623161059002.png)





> `ServletRegistrationBean` 中的` onStartup`获取Servlet的name，然后调用ServletContext的addServlet将Servlet加入到Tomcat中，这样我们就能发请求给这个Servlet了。   

```java
@Override
protected ServletRegistration.Dynamic addRegistration(String description, ServletContext servletContext) {
   String name = getServletName();
   return servletContext.addServlet(name, this.servlet);
}
```



#### 4.2.2.2、`servletContext`中添加`Filter`



![image-20200623161207609](D:\study\HealerJean.github.io\blogImages\image-20200623161207609.png)



```java
@Override
protected final void register(String description, ServletContext servletContext) {
   D registration = addRegistration(description, servletContext);
   if (registration == null) {
      logger.info(StringUtils.capitalize(description) + " was not registered (possibly already registered?)");
      return;
   }
   configure(registration);
}
```

![image-20200623161309361](D:\study\HealerJean.github.io\blogImages\image-20200623161309361.png)





> `AbstractFilterRegistrationBean`也是同样的原理，先获取目标`Filter`，然后调用ServletContext的**addFilter**将Filter加入到Tomcat中，这样Filter就能拦截我们请求了。


```java
@Override
protected Dynamic addRegistration(String description, ServletContext servletContext) {
	Filter filter = getFilter();
	return servletContext.addFilter(getOrDeduceName(filter), filter);
}
```




#### 4.2.2.3、Servlet容器中添加`addListener`

![image-20200623161355168](D:\study\HealerJean.github.io\blogImages\image-20200623161355168.png)





```java
@Override
protected void register(String description, ServletContext servletContext) {
    try {
        servletContext.addListener(this.listener);
    }
    catch (RuntimeException ex) {
        throw new IllegalStateException("Failed to add listener '" + this.listener + "' to servlet context", ex);
    }
}
```



### 4.2.3、DispatcherServletAutoConfiguration

> Spring Boot在自动配置SpringMVC的时候，会自动注册SpringMVC前端控制器：**DispatcherServlet**，该控制器主要在**DispatcherServletAutoConfiguration**自动配置类中进行注册的。DispatcherServlet是SpringMVC中的核心分发器。DispatcherServletAutoConfiguration也在spring.factories中配置了    



> 1、先看下ClassPath下是否有DispatcherServlet.class字节码， 我们引入了spring-boot-starter-web，同时引入了tomcat和SpringMvc,肯定会存在DispatcherServlet.class字节码，如果没有导入spring-boot-starter-web，则这个配置类将不会生效

> 2、然后往Spring容器中注册DispatcherServlet实例，接着又加入ServletRegistrationBean实例，并把DispatcherServlet实例作为参数，上面我们已经学过了ServletRegistrationBean的逻辑，在Tomcat启动的时候，会获取所有的ServletRegistrationBean，并执行其中的onstartup方法，将DispatcherServlet注册到Servlet容器中，这样就类似原来的web.xml中配置的dispatcherServlet。



![image-20200623162302716](D:\study\HealerJean.github.io\blogImages\image-20200623162302716.png)





```java
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Configuration(proxyBeanMethods = false)
@ConditionalOnWebApplication(type = Type.SERVLET)
// 先看下ClassPath下是否有DispatcherServlet.class字节码
// 我们引入了spring-boot-starter-web，同时引入了tomcat和SpringMvc,肯定会存在DispatcherServlet.class字节码
@ConditionalOnClass(DispatcherServlet.class)

//这个配置类的执行要在ServletWebServerFactoryAutoConfiguration配置类生效之后执行, 要等Tomcat启动后才能往其中注入DispatcherServlet
@AutoConfigureAfter(ServletWebServerFactoryAutoConfiguration.class)
public class DispatcherServletAutoConfiguration {

	/*
	 * The bean name for a DispatcherServlet that will be mapped to the root URL "/"
	 */
	public static final String DEFAULT_DISPATCHER_SERVLET_BEAN_NAME = "dispatcherServlet";

	/*
	 * The bean name for a ServletRegistrationBean for the DispatcherServlet "/"
	 */
	public static final String DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME = "dispatcherServletRegistration";

    
    //Spring容器注册DispatcherServlet
	@Configuration(proxyBeanMethods = false)
	@Conditional(DefaultDispatcherServletCondition.class)
	@ConditionalOnClass(ServletRegistration.class)
	@EnableConfigurationProperties(WebMvcProperties.class)
	protected static class DispatcherServletConfiguration {

        // 直接构造DispatcherServlet，并设置WebMvcProperties中的一些配置
		@Bean(name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
		public DispatcherServlet dispatcherServlet(WebMvcProperties webMvcProperties) {
			DispatcherServlet dispatcherServlet = new DispatcherServlet();
			dispatcherServlet.setDispatchOptionsRequest(webMvcProperties.isDispatchOptionsRequest());
			dispatcherServlet.setDispatchTraceRequest(webMvcProperties.isDispatchTraceRequest());
			dispatcherServlet.setThrowExceptionIfNoHandlerFound(webMvcProperties.isThrowExceptionIfNoHandlerFound());
			dispatcherServlet.setPublishEvents(webMvcProperties.isPublishRequestHandledEvents());
			dispatcherServlet.setEnableLoggingRequestDetails(webMvcProperties.isLogRequestDetails());
			return dispatcherServlet;
		}

        
        // 构造文件上传相关的bean
		@Bean
		@ConditionalOnBean(MultipartResolver.class)
		@ConditionalOnMissingBean(name = DispatcherServlet.MULTIPART_RESOLVER_BEAN_NAME)
		public MultipartResolver multipartResolver(MultipartResolver resolver) {
			// Detect if the user has created a MultipartResolver but named it incorrectly
			return resolver;
		}

	}
    
    
    @Configuration(proxyBeanMethods = false)
	@Conditional(DispatcherServletRegistrationCondition.class)
	@ConditionalOnClass(ServletRegistration.class)
	@EnableConfigurationProperties(WebMvcProperties.class)
	@Import(DispatcherServletConfiguration.class)
	protected static class DispatcherServletRegistrationConfiguration {

        // ServletRegistrationBean实现了ServletContextInitializer接口，在onStartup方法中对应的Servlet注册到Servlet容器中,对应的urlMapping为server.servletPath配置
		@Bean(name = DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME)
		@ConditionalOnBean(value = DispatcherServlet.class, name = DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
		public DispatcherServletRegistrationBean dispatcherServletRegistration(DispatcherServlet dispatcherServlet,
				WebMvcProperties webMvcProperties, ObjectProvider<MultipartConfigElement> multipartConfig) {
			DispatcherServletRegistrationBean registration = new DispatcherServletRegistrationBean(dispatcherServlet,
					webMvcProperties.getServlet().getPath());
			registration.setName(DEFAULT_DISPATCHER_SERVLET_BEAN_NAME);
			registration.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
			multipartConfig.ifAvailable(registration::setMultipartConfig);
			return registration;
		}

	}
    
    
    
    
    
    
```



```xml
<servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
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
		id: 'OtZQLhqHl89KGSjW',
    });
    gitalk.render('gitalk-container');
</script> 

