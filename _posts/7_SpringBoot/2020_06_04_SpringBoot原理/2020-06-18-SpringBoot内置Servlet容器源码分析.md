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
        
        //从工厂中获取Web服务
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

