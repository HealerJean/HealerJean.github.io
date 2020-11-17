---
title: SpringBoot源码阅读之自动配置源码分析
date: 2020-02-20 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot源码阅读之自动配置源码分析
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、SpringBoot启动类加载

> 首先加载`springBoot`启动类注入到`spring`容器中`beanDefinitionMap`中，看下`prepareContext`方法中的`load`方法：`load(context, sources.toArray(new Object[0]));`
> 跟进该方法最终会执行`BeanDefinitionLoader`的load方法：   



`SpringBoot`中`run`方法，`Spring`容器前置处理会对启动类进行加载

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



> `prepareContext`中`load(context, sources.toArray(new Object[0]));`会对启动类进行加载

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



> 启动类`SpringBoot_Application.class`被加载到 bean工厂的  `beanDefinitionMap`中，后续该启动类将作为开启自动化配置的入口，    

```java
Set<Object> sources = getAllSources();
Assert.notEmpty(sources, "Sources must not be empty");
load(context, sources.toArray(new Object[0]));
```



> 在创建**SpringApplication**实例时，先将**SpringBoot_Application.class**存储在`this.primarySources`属性中，现在就是用到这个属性的时候了，我们来看看`getAllSources（）`

```java
//获取启动类
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



> 加载启动类

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
    //如果是class类型，启用注解类型（就是它）
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



> 断启动类中是否包含`@component`注解，可我们的启动类并没有该注解。继续跟进会发现，AnnotationUtils判断是否包含该注解是通过递归实现，注解上的注解若包含指定类型也是可以的。    
>
> 启动类中包含`@SpringBootApplication`注解，进一步查找到`@SpringBootConfiguration`注解，然后查找到`@Component`注解

```java
private int load(Class<?> source) {
    if (isGroovyPresent() && GroovyBeanDefinitionSource.class.isAssignableFrom(source)) {
        // Any GroovyLoaders added in beans{} DSL can contribute beans here
        GroovyBeanDefinitionSource loader = BeanUtils.instantiateClass(source, GroovyBeanDefinitionSource.class);
        load(loader);
    }
    
    //判断是否被注解@Component注释
    if (isComponent(source)) {
        
        //将启动类bean信息存入beanDefinitionMap(DefaultListableBeanFactory 类)，
        //也就是将SpringBoot_Application.class存入了beanDefinitionMap
        this.annotatedReader.register(source);
        return 1;
    }
    return 0;
}
```



> 在查找到`@Component`注解后，表面该对象为`spring bean`，然后会将其信息包装成 `beanDefinitaion(AnnotatedGenericBeanDefinition)` ，添加到容器的` beanDefinitionMap`中。如下：     
>
> 将启动类bean信息存入`beanDefinitionMap`(`DefaultListableBeanFactory` 类)，也就是将`SpringBoot_Application.class`存入了`beanDefinitionMap`   





```java
private <T> void doRegisterBean(Class<T> beanClass, @Nullable String name,
                                @Nullable Class<? extends Annotation>[] qualifiers, @Nullable Supplier<T> supplier,
                                @Nullable BeanDefinitionCustomizer[] customizers) {

    //new  BeanDefinition()
    AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
    if (this.conditionEvaluator.shouldSkip(abd.getMetadata())) {
        return;
    }

    abd.setInstanceSupplier(supplier);
    ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
    abd.setScope(scopeMetadata.getScopeName());
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
    
    //将 AnnotatedGenericBeanDefinition 
    BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);
}
```



![image-20200617192512795](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200617192512795.png)







# 2、SpringBoot中`EnableAutuConfiguration`注解的原理以及使用

## 2.1、原理 

### 2.1.1、观察启动类

```java
@SpringBootApplication
public class SpringBoot_Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot_Application.class, args);
    }
}
```



> @SpringBootApplication 由求他3个注解组成，分别是    
>
> 1、`@SpringBootConfiguration`：`@SpringBootConfiguration `是继承自`Spring`的 `@Configuration` 注解  
>
> 2、`@ComponentScan`：自动扫描 `springBootApplication` 同级别所在类（使用@Component @Service @Repository @Controller注释的类 ）     
>
> **3、`@EnableAutoConfiguration`：顾名思义，开启Spring Boot的自动配置功能 ，什么是自动配置功能呢？简单点说就是通知SpringBoot根据依赖中的jar包，自动选择实例化某些配置。**



```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = { @Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
		@Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public @interface SpringBootApplication {
```



### 2.1.2、观察注解组成  

> **`@EnableAutoConfiguration`：顾名思义，开启Spring Boot的自动配置功能 ，什么是自动配置功能呢？简单点说就是通知SpringBoot根据依赖中的jar包，自动选择实例化某些配置。**

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
```





**主要是下面这两个注解**

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
```



#### 2.1.2.1、`@AutoConfigurationPackage `  

>  自动配置包，将`SpringBootApplication`主配置类所在包以及子包的所有子类扫描到`spring`容器

```java
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {


}
```



#### 2.1.2.2、`@Import(AutoConfigurationImportSelector.class)`

> 借助`AutoConfigurationImportSelector`，`@EnableAutoConfiguration`可以帮助SpringBoot应用将所有符合条件的`@Configuration`配置都加载到当前SpringBoot创建并使用的IoC容器。   
>
>  
>
> `AutoConfigurationImportSelector` ， 导入组件的选择器：从`classpath`中搜寻所有的 `META-INF/spring.factories` 配置文件，并将其中`org.springframework.boot.autoconfigure.EnableutoConfiguration` 对应的配置项中所有符合条件的`@Configuration`配置类的配置类加载到IoC容器           
>
> 
>
> 比如，添加了spring-boot-start-web，会自动添加Tomact和spring MVC 的依赖。这样就会对二者进行自动配置）  

```java
@Import(AutoConfigurationImportSelector.class)
```



**一些条件如下：**

```java
@ConditionalOnClass ： classpath中存在该类时起效
@ConditionalOnMissingClass ： classpath中不存在该类时起效
@ConditionalOnBean ： DI容器中存在该类型Bean时起效
@ConditionalOnMissingBean ： DI容器中不存在该类型Bean时起效
@ConditionalOnSingleCandidate ： DI容器中该类型Bean只有一个或@Primary的只有一个时起效
@ConditionalOnExpression ： SpEL表达式结果为true时
@ConditionalOnProperty ： 参数设置或者值一致时起效
@ConditionalOnResource ： 指定的文件存在时起效
@ConditionalOnJndi ： 指定的JNDI存在时起效
@ConditionalOnJava ： 指定的Java版本存在时起效
@ConditionalOnWebApplication ： Web应用环境下起效
@ConditionalOnNotWebApplication ： 非Web应用环境下起效
```







### 2.1.3、分析：   

#### 2.1.3.1、`@AutoConfigurationPackage `

> **将spring boot主配置类所在的包及其子包下的所有的组件扫描到spring容器中去，可以理解为所有的类**



```java
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {


}
```





##### 2.1.3.1.1、注册时机 

> 这个以后慢慢分析，SpringBoot启动过程回过头来看

```java
SpringApplication.run()
  => refreshContext()
    => EmbeddedWebApplicationContext.refresh()
      => AbstractApplicationContext.invokeBeanFactoryPostProcessors()
        => PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors()
          => ConfigurationClassPostProcessor.processConfigBeanDefinitions()
            => ConfigurationClassBeanDefinitionReader.loadBeanDefinitions()
              => loadBeanDefinitionsFromRegistrars()
                => AutoConfigurationPackages$Registrar.registerBeanDefinitions()
                  => AutoConfigurationPackages.register()
```



##### 2.1.3.1.2、注册逻辑  

```java
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        // new PackageImports(metadata).getPackageNames().toArray(new String[0])
        //  ====>   @SpringBootApplication 的 Spring Boot 应用程序入口类所在的包
        register(registry, new PackageImports(metadata).getPackageNames().toArray(new String[0]));
    }

    @Override
    public Set<Object> determineImports(AnnotationMetadata metadata) {
        return Collections.singleton(new PackageImports(metadata));
    }

}
```



```java
// AutoConfigurationPackages 类
	// 定义Bean的名称
	// org.springframework.boot.autoconfigure.AutoConfigurationPackages
	private static final String BEAN = AutoConfigurationPackages.class.getName();
	

	public static void register(BeanDefinitionRegistry registry, String... packageNames) {
		// 这里参数 packageNames 缺省情况下就是一个字符串，是使用了注解
		// @SpringBootApplication 的 Spring Boot 应用程序入口类所在的包
		
		if (registry.containsBeanDefinition(BEAN)) {
			// 如果该bean已经注册，则将要注册包名称添加进去
			BeanDefinition beanDefinition = registry.getBeanDefinition(BEAN);
			ConstructorArgumentValues constructorArguments = beanDefinition
					.getConstructorArgumentValues();
			constructorArguments.addIndexedArgumentValue(0,
					addBasePackages(constructorArguments, packageNames));
		}
		else {
			//如果该bean尚未注册，则注册该bean，参数中提供的包名称会被设置到bean定义中去
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(BasePackages.class);
			beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0,
					packageNames);
			beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
			registry.registerBeanDefinition(BEAN, beanDefinition);
		}
	}

```





#### 2.1.3.2、`@Import(AutoConfigurationImportSelector.class)`



```java
@Override
public String[] selectImports(AnnotationMetadata annotationMetadata) {
  if (!isEnabled(annotationMetadata)) {
    return NO_IMPORTS;
  }
  AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(annotationMetadata);
  return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
}



protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
  //支持自动配置 
  if (!isEnabled(annotationMetadata)) {
    return EMPTY_ENTRY;
  }
  // = getAttributes(annotationMetadata);
  //重点在这里，找出所有的配置文件名
  List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
  //去除重复的 = removeDuplicates(configurations);
  Set<String> exclusions = getExclusions(annotationMetadata, attributes);
  checkExcludedClasses(configurations, exclusions);
  configurations.removeAll(exclusions);
  configurations = getConfigurationClassFilter().filter(configurations);
  fireAutoConfigurationImportEvents(configurations, exclusions);
  return new AutoConfigurationEntry(configurations, exclusions);
}
```



**`isEnabled(annotationMetadata)`**：   

> 判断是否支持自动配置

```java
String ENABLED_OVERRIDE_PROPERTY = "spring.boot.enableautoconfiguration";

//是否支持自动配置，
protected boolean isEnabled(AnnotationMetadata metadata) {
    if (getClass() == AutoConfigurationImportSelector.class) {
        //一般情况下默认为true
        return getEnvironment().getProperty(EnableAutoConfiguration.ENABLED_OVERRIDE_PROPERTY, Boolean.class, true);
    }
    return true;
}

```



**`List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);`：**   

> 获取 spring-boot-autoconfigure.jar/META-INF/spring.factories中每一个xxxAutoConfiguration文件名并返回为List<String>   



```java

protected List<String> getCandidateConfigurations(AnnotationMetadata metadata, AnnotationAttributes attributes) {
    //获取 spring-boot-autoconfigure.jar/META-INF/spring.factories中每一个xxxAutoConfiguration文件名
    List<String> configurations = SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(),
                                                                         getBeanClassLoader());
    Assert.notEmpty(configurations, "No auto configuration classes found in META-INF/spring.factories. If you "
                    + "are using a custom packaging, make sure that file is correct.");
    return configurations;
}
```

**![image-20200611144838805](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200611144838805.png)**



**SpringFactoriesLoader.loadFactoryNames(getSpringFactoriesLoaderFactoryClass(), getBeanClassLoader());**    

> 择在spring.factories文件中 org.springframework.boot.autoconfigure.EnableAutoConfiguration的相关配置类的名字，然后返回为List集合。会进行过滤  



> 此方法很多地方都会用到，所以要根据我们传入的进行筛选

```java
public static List<String> loadFactoryNames(Class<?> factoryType, @Nullable ClassLoader classLoader) {
    //选择在spring.factories文件中factoryTypeName = org.springframework.boot.autoconfigure.EnableAutoConfiguration的相关配置类的名字
    String factoryTypeName = factoryType.getName();
    return (List)loadSpringFactories(classLoader).getOrDefault(factoryTypeName, Collections.emptyList());
}

```



```java

public static final String FACTORIES_RESOURCE_LOCATION = "META-INF/spring.factories";



private static Map<String, List<String>> loadSpringFactories(@Nullable ClassLoader classLoader) {
    MultiValueMap<String, String> result = cache.get(classLoader);
    if (result != null) {
        return result;
    }

    try {
        //找到所有jar包中在文件 META-INF/spring.factories 
        Enumeration<URL> urls = (classLoader != null ?
                                 classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                                 ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        result = new LinkedMultiValueMap<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            UrlResource resource = new UrlResource(url);
            Properties properties = PropertiesLoaderUtils.loadProperties(resource);
            for (Map.Entry<?, ?> entry : properties.entrySet()) {
                String factoryTypeName = ((String) entry.getKey()).trim();
                for (String factoryImplementationName : StringUtils.commaDelimitedListToStringArray((String) entry.getValue())) {
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

![image-20200611162525077](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200611162525077.png)



比如：`spring-boot-autoconfigure-2.3.0.RELEASE.jar`  中的 `spring.factories`

![image-20200611151827716](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200611151827716.png)



```factories
# Auto Configuration Import Filters
org.springframework.boot.autoconfigure.AutoConfigurationImportFilter=\
org.springframework.boot.autoconfigure.condition.OnBeanCondition,\
org.springframework.boot.autoconfigure.condition.OnClassCondition,\
org.springframework.boot.autoconfigure.condition.OnWebApplicationCondition

# Auto Configure 只会识别这个
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration,\
org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration,\
org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration,\
org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration,\
org.springframework.boot.autoconfigure.context.LifecycleAutoConfiguration,\
org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration,\
```





当然我们也可以自己写 `spring.factories`。入下面的实战



## 2.2、实战1 

### 2.2.1、项目`spring-stater-custom`

#### 2.2.1.1、`pom`依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.healerjean.proj</groupId>
    <artifactId>spring-stater-custom</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <name>spring-stater-custom</name>
    <description>SpringBoot基础</description>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.4</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
            <version>2.0.4.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <version>2.0.4.RELEASE</version>
            <optional>true</optional>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>

</project>

```



#### 2.2.1.2、`Bean`

##### 2.2.1.2.1、`DemoBean`

```java
@Data
public class DemoBean {

    private String name ;
    private Integer age;
}
```



##### 2.2.1.2.2、`NextBean`

```java
@Data
public class NextBean {

    private String nextName;
}
```



#### 2.2.1.3、`DemoPeroperties`

> 这个就很重要了，很多SpringBoot导入的jar包，需要我们在`application.properties`写一些配置属性，比如数据源，redis之类的配置信息。就是通过类似于这样的完成的。   

```java
@ConfigurationProperties("demo")
@Data
public class DemoPeroperties {

    /** 提供一个默认值 */
    private static final String NAME = "";


    /** 下面的配置将来在主项目中配置 */
    private String name = NAME;
    private Integer age;
}
```



#### 2.2.1.4、`configuration`

##### 2.2.2.1.1、`DemoConfiguration`

```java
//要求这个类是否在classpath中存在，如果存在，才会实例化一个Bean
@ConditionalOnClass(DemoBean.class)
//使 DemoPeroperties 被ioc容器注入
@EnableConfigurationProperties(DemoPeroperties.class)
@Configuration
public class DemoConfiguration {

    @Autowired
    private DemoPeroperties demoPeroperties;

    @Bean
    ////容器中如果没有DemoBean这个类,那么自动配置这个Hello
    @ConditionalOnMissingBean(DemoBean.class)
    public DemoBean demoBean() {
        DemoBean demoBean = new DemoBean();
        demoBean.setName(demoPeroperties.getName());
        demoBean.setAge(demoBean.getAge());
        return demoBean;
    }
}

```

##### 2.2.2.1.2、NextConfiguration

```java
//要求这个类是否在classpath中存在，如果存在，才会实例化一个Bean
@ConditionalOnClass(NextBean.class)
@Configuration
public class NextConfiguration {


    @Bean
    @ConditionalOnMissingBean(NextBean.class)
    public NextBean nextBean() {
        return new NextBean();
    }
}

```



#### 2.2.1.5、`spring.factories`

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.healerjean.proj.configuration.DemoConfiguration,\
com.healerjean.proj.configuration.NextConfiguration

```

![image-20200611152606746](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200611152606746.png)



#### 2.2.1.6、打包，发布到我们的maven仓库中

```shell
mvn install
```



### 2.2.2、示例项目

#### 2.2.2.1、pom导入上面的依赖

```xml
<!--  测试 @autoEnableConfiguration -->
<dependency>
    <groupId>com.healerjean.proj</groupId>
    <artifactId>spring-stater-custom</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```



#### 2.2.2.2、`application.properties`

```java
spring.application.name=springboot-test
server.port=8888



demo.name=name
demo.age=12
```



#### 2.2.3、`DataConfig`

```java
@Configuration
@Slf4j
public class DataConfig {

    @Autowired
    private DemoPeroperties demoPeroperties;

    @Bean
    public AppBean appBean() {
        AppBean appBean = new AppBean();
        appBean.setDataBean(dataBean());
        log.info("{}", demoPeroperties);
        return appBean;
    }

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```



![image-20200611153322637](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200611153322637.png)



























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
