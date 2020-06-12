---
title: SpringBoot中EnableAutuConfiguration注解的原理以及使用
date: 2020-02-20 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot中EnableAutuConfiguration注解的原理以及使用
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 1、原理 

## 1.1、观察启动类

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
> 1、`@SpringBootConfiguration`：@SpringBootConfiguration 是继承自Spring的 @Configuration 注解  
>
> 2、`@ComponentScan`：自动扫描 springBootApplication 同级别所在类（使用@Component @Service @Repository @Controller注释的类 ）     
>
> **3、`@EnableAutoConfiguration`：顾名思义，开启Spring Boot的自动配置功能 ，什么是自动配置功能呢？简单点说就是通知SpringBoot根据依赖中的jar包，自动选择实例化某些配置。**
>
> 

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



## 1.2、观察注解组成  

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



### 1.2.1、@AutoConfigurationPackage    

>  自动配置包，将`SpringBootApplication`主配置类所在包以及子包的所有子类扫描到`spring`容器

```java
@Import(AutoConfigurationPackages.Registrar.class)
public @interface AutoConfigurationPackage {


}
```



### 1.2.2、@Import(AutoConfigurationImportSelector.class)

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







## 1.3、分析：   

### 1.3.1、`@AutoConfigurationPackage `

> **将spring boot主配置类所在的包及其子包下的所有的组件扫描到spring容器中去，可以理解为所有的类**



#### 1.3.1.1、注册时机 

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



#### 1.3.1.2、注册逻辑  

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





### 1.3.2、`@Import(AutoConfigurationImportSelector.class)`



```java
protected AutoConfigurationEntry getAutoConfigurationEntry(AnnotationMetadata annotationMetadata) {
    //支持自动配置 
    if (!isEnabled(annotationMetadata)) {
        return EMPTY_ENTRY;
    }
    //
    AnnotationAttributes attributes = getAttributes(annotationMetadata);
    //重点在这里，找出所有的配置文件名
    List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
    //去除重复的
    configurations = removeDuplicates(configurations);
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

![image-20200611162525077](D:\study\HealerJean.github.io\blogImages\image-20200611162525077.png)



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



# 2、实战1 

## 2.1、项目`spring-stater-custom`

### 2.1.1、`pom`依赖

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



### 2.1.2、`Bean`

#### 2.1.2.1、`DemoBean`

```java
@Data
public class DemoBean {

    private String name ;
    private Integer age;
}
```



#### 2.1.2.2、`NextBean``

```java
@Data
public class NextBean {

    private String nextName;
}
```



### 2.1.3、`DemoPeroperties`

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



### 2.1.4、`configuration`

#### 2.1.4.1、`DemoConfiguration`

```java
//要求这个类是否在classpath中存在，如果存在，才会实例化一个Bean
@ConditionalOnClass(DemoBean.class)
//使 DemoPeroperties 被ioc容器注入
@EnableConfigurationProperties(DemoPeroperties.class)
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

#### 2.1.5.2、NextConfiguration

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



### 2.1.5、`spring.factories`

```
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.healerjean.proj.configuration.DemoConfiguration,\
com.healerjean.proj.configuration.NextConfiguration

```

![image-20200611152606746](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200611152606746.png)



### 2.1.6、打包，发布到我们的maven仓库中

```shell
mvn install
```



## 2.2、示例项目

### 2.2.1、pom导入上面的依赖

```xml
<!--  测试 @autoEnableConfiguration -->
<dependency>
    <groupId>com.healerjean.proj</groupId>
    <artifactId>spring-stater-custom</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```



### 2.2.2、`application.properties`

```java
spring.application.name=springboot-test
server.port=8888



demo.name=name
demo.age=12
```



### 2.2.3、`DataConfig`

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
