---
title: SpringBoot注解
date: 2020-06-04 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: SpringBoot注解
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 一、`SpringBoot`

## 1、`@Bean`

> 从 `spring3.0` 开始，在 `spring framework` 模块中提供了这个注解，`@Bean `搭配 `@Configuration`注解，可以完全不依赖 `xml` 配置，**在运行时完成 `bean` 的创建和初始化工作**    



### 1）`DataBean.java`

```java
@Slf4j
public class DataBean {

    public void method() {
       log.info("DataBean--------调用方法：method--------");
    }

}

```



### 2）`DataConfig.java`

```java
public class DataConfig {

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```





## 2、`@Controller`, `@Service`, `@Repository`, `@Component  `   



> **1、@Component: 表明这个注释的类是一个组件，当使用基于注释的配置和类路径扫描时，这些类被视为自动检测的候选者，没有明确的角色。**     
>
> 2、`@Controller` : 表明一个注解的类是一个 `Controller` ，也就是控制器，可以把它理解为 `MVC`  模式的 `Controller ` 这个角色。 
>
> 3、`@Service` : 表明这个带注解的类是一个" `Service` "，也就是服务层，可以把它理解为 `MVC`  模式中的 `Service` 层这个角色（`@Autowired`   `@Inject`   `@Resourse` ，是等效的,可以注解到 `set` 方法，或者属性上。一般都是属性上），
>
> 4、`@Repository` : 表明这个注解的类是一个" `Repository"` ,团队实现了 `JavaEE`  模式中像是作为" `Data` `Access` `Object`" 可能作为 `DAO` 来使用，当与 `PersistenceExceptionTranslationPostProcessor`  结合使用时，这样注释的类有资格获得 `Spring` 转换的目的。这个注解也是 `@Component`  的一个特殊实现，允许实现类能够被自动扫描到     



```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
```



```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {


	@AliasFor(annotation = Component.class)
	String value() default "";

}

```



```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Repository {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
```



```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface Component {

	String value() default "";

}

```





可以看到**@Controller, @Service, @Repository**这三个注解上都有**@Component**这个注解，也就是说，上面四个注解标记的类都能够通过@ComponentScan 扫描到，    **@Controller，@Service，@Repository 的注解上都有@Component，所以这三个注解都可以用@Component进行替换。**       

**上面四个注解最大的区别就是使用的场景和语义不一样**，比如你定义一个Service类想要被Spring进行管理，你应该把它定义为@Service 而不是@Controller因为我们从语义上讲，@Service更像是一个服务的类，而不是一个控制器的类，@Component通常被称作组件，它可以标注任何你没有严格予以说明的类，比如说是一个配置类，它不属于MVC模式的任何一层，这个时候你更习惯于把它定义为 @Component。





## 3、`@Configuration`   

> > `@Configuration`注解提供了全新的`bean`创建方式。最初`spring`通过`xml`配置文件初始化`bean`并完成依赖注入工作。     
> >
> > 从spring3.0开始，在`spring framework`模块中提供了这个注解，搭配`@Bean`等注解，可以完全不依赖xml配置，在运行时完成bean的创建和初始化工作
>
> ​    
>
> **我们看到源码里面，`@Configuration` 标记了`@Component`元注解，某种意义上来讲，二者的使用是没有区别的**，    
>
> 因此可以被`@ComponentScan`扫描并处理，**在Spring容器初始化时Configuration类 会被注册到Bean容器中，最后还会实例化。**  



```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component //@Component元注解
public @interface Configuration {
    String value() default "";
}
```



### 1）`@Configuration` 和 `@Component`区别

>  通过上面的介绍，可以看到`@Configuration` 标记了`@Component`元注解，某种意义上来讲，二者的使用是没有区别的，但是他们的区别到底是什么呢  



#### a、`@Configuration`注解

```java
@Configuration
public class DataConfig {

    @Bean
    public AppBean appBean() {
        AppBean appBean = new AppBean();
        appBean.setDataBean(dataBean());
        return appBean;
    }

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}

```

```java
@SpringBootTest
class SpirngAopApplicationTests {

    @Autowired
    private AppBean appBean;
    @Autowired
    private DataBean dataBean;

    @Test
    public void test(){
        System.out.println(dataBean == appBean.getDataBean());
    }

}


true //同一个对象
```



#### b、`@Component`注解

```java
@Component
public class DataConfig {

    @Bean
    public AppBean appBean() {
        AppBean appBean = new AppBean();
        appBean.setDataBean(dataBean());
        return appBean;
    }

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```

```java
@SpringBootTest
class SpirngAopApplicationTests {

    @Autowired
    private AppBean appBean;
    @Autowired
    private DataBean dataBean;

    @Test
    public void test(){
        System.out.println(dataBean == appBean.getDataBean());
    }

}


false //不同对象
```



#### c、总结   

从定义来看， `@Configuration` 注解本质上还是 `@Component`，因此 或者 `@ComponentScan` 都能处理`@Configuration` 注解的类，**但是还是有区别，如下：**      

`Spring`  容器在启动时，会加载默认的一些 `PostPRocessor`，**其中就有 `ConfigurationClassPostProcessor`，这个后置处理程序专门处理带有 `@Configuration` 注解的类**，这个程序会在 `bean` 定义加载完成后，在 `Bean` 初始化前进行处理。**主要处理的过程就是将带有`@Configuration` 类进行增强，在初始化 `@Bean` 注解的 `Bean` 类的时候，该方法里面调用别的方法会判断其他方法是否也有 `@Bean`，如果有的话，那么会直接调用 `Spring` 容器中的`DataBean`实例，而不会重新创建**：具体还是看下面的`@Configuration`源码吧      



**`@Component` 注释的类没有被动态代理增强怎么办呢，使用注入即可，如下两种都是注入的方式**

```java
@Component
public class DataConfig {

    @Bean
    public AppBean appBean(DataBean dataBean) {
        AppBean appBean = new AppBean();
        appBean.setDataBean(dataBean);
        return appBean;
    }

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```

   

```java
@Component
public class DataConfig {

    @Autowired
    private DataBean dataBean;
    
    @Bean
    public AppBean appBean() {
        AppBean appBean = new AppBean();
        appBean.setDataBean(dataBean);
        return appBean;
    }

    @Bean
    public DataBean dataBean() {
        return new DataBan();
    }

}

```




### 2）搭配`@Autowired`

> **因为`@Configuration`本身也是一个`@Component`，因此配置类本身也会被注册到应用上下文，并且也可以使用 `IOC` 的`@Autowired`等注解来注入所需 `bean`** 。

```java
@Configuration
public class AppConfig {

    @Autowired
    private UserService userService;

    @Bean
    public AppBean appBean() {
        return new AppBean();
    }
}

```



### 3）搭配`@CompomentScan`

> 配置类也可以自己添加注解`@CompomentScan`，来显式扫描需使用组件。     

```java
@Configuration
@ComponentScan("com.healerjean.proj")
public class AppConfig {

    @Autowired
    private UserService userService;

    @Bean
    public AppBean appBean() {
        return new AppBean();
    }
}

```



## 4、`@Import`注解组合使用

> 有时没有把某个类注入到 `IOC` 容器中，但在运用的时候需要获取该类对应的 `bean` ，此时就需要用到 `@Import` 注解  

### 1）单纯的类 

#### a、`DataBean.java`：单纯的类

```java
@Slf4j
public class DataBean {

    public void method() {
       log.info("DataBean--------调用方法：method--------");
    }

}
```



#### b、实例使用

> `@Configuration` 和 `@Component`都可以

```java
@Configuration
@Import(DataBean.class)
public class AppConfig {

    @Autowired
    private DataBean dataBean;

    @Bean
    public AppBean appBean() {
        dataBean.method();
        return new AppBean();
    }

}
```



### 2）和`@Configuration`搭配使用 

> 可以看到只注册了`AppConfig.class`，容器自动会把`@Import`指向的配置类初始化

#### a、`DataConfig.java`：

> 没有注解，但是里面有 `@Bean`

```java
public class DataConfig {

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```



#### b、`AppConfig.java`

```java
@Configuration
@Import(DataConfig.class)
public class AppConfig {

    @Autowired
    private DataBean dataBean;

    @Bean
    public AppBean appBean() {
        dataBean.method();
        return new AppBean();
    }

}

```





 

## 5、`Condition`相关注解

> 可以根据代码中设置的条件装载不同的bean，  



### 1）`@Conditional ` 

> `@Conditional`注解定义如下，其内部主要就是利用了`Condition`接口，来判断是否满足条件，从而决定是否需要加载Bean  ,   
>
> 可以观察到可以注释到类，比如配置类，以及方法上去，方法比如@Bean注解的方法     



```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Conditional {


	Class<? extends Condition>[] value();

}
```



**我们自定义条件需要实现类`Condition`，然后加入自己的条件**

```java
@FunctionalInterface
public interface Condition {

	boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata);

}

```

`conditionContext`的一些内部变量

```java
public interface ConditionContext {

the case with a plain {@link ClassPathScanningCandidateComponentProvider})

   //1、能获取到ioc使用的beanfactory
	@Nullable
	ConfigurableListableBeanFactory getBeanFactory();

    //2、获取类加载器
	@Nullable
	ClassLoader getClassLoader();
    
    //3、获取当前环境信息
	Environment getEnvironment();
    
    //4、获取到bean定义的注册类
	BeanDefinitionRegistry getRegistry();

	//5、获取资源
	ResourceLoader getResourceLoader();

}

```



#### a、`EncryptCondition`

> 我们自定义条件需要实现类`Condition`

```properties
condition.var.encrypt=true
```

```java
//不可以使用 @PropertySource，@Value（可以采用spring.profiles.active=db 进行模块导入）
public class EncryptCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata metadata) {
        Environment environment = conditionContext.getEnvironment();
        String encrypt = environment.getProperty("condition.var.encrypt"); 
        if (Boolean.valueOf(encrypt)) {
            return true;
        }
        return false;
    }
}

```



#### b、 `@Conditional(EncryptCondition.class)`

> 如果是false就不会创建下面的Bean了，如果是true则会创建

```java
@Conditional(EncryptCondition.class)
@Configuration
@Slf4j
public class DataConfig {

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }
}

```





### 2）`@ConditionalOnProperty`

> 参数设置或者值一致时起效  
>
> 通过其两个属性name以及havingValue来实现的，其中name用来从application.properties中读取某个属性值。     
>
> 如果该值为空，则返回false;     如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true，否则返回false。

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
@Conditional(OnPropertyCondition.class)
public @interface ConditionalOnProperty {

    String[] value() default {}; //数组，获取对应property名称的值，与name不可同时使用  
  
    String prefix() default "";//property名称的前缀，可有可无  
  
    String[] name() default {};//数组，property完整名称或部分名称（可与prefix组合使用，组成完整的property名称），与value不可同时使用  
  
    String havingValue() default "";//可与name组合使用，比较获取到的属性值与havingValue给定的值是否相同，相同才加载配置  
  
    boolean matchIfMissing() default false;//缺少该property时是否可以加载。如果为true，没有该property也会正常加载；反之报错  
  
    boolean relaxedNames() default true;//一般用不导

}
```





```java
// 在application.properties配置 condition.var.encrypt=true
@ConditionalOnProperty(name = "condition.var.encrypt", havingValue = "true")
// @ConditionalOnProperty(prefix = "condition.var", name = "encrypt", havingValue = "true")
@Configuration
@Slf4j
public class DataConfig {

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```





### 3）其他 `Condition`

| 注解                            | 说明                                                 |
| ------------------------------- | ---------------------------------------------------- |
| @ConditionalOnClass             | classpath中存在该类时起效                            |
| @ConditionalOnMissingClass      | classpath中不存在该类时起效                          |
| @ConditionalOnBean              | DI容器中存在该类型Bean时起效                         |
| @ConditionalOnMissingBean       | DI容器中不存在该类型Bean时起效                       |
| @ConditionalOnSingleCandidate   | DI容器中该类型Bean只有一个或@Primary的只有一个时起效 |
| @ConditionalOnExpression        | SpEL表达式结果为true时                               |
| @ConditionalOnProperty          | 参数设置或者值一致时起效                             |
| @ConditionalOnResource          | 指定的文件存在时起效                                 |
| @ConditionalOnJndi              | 指定的JNDI存在时起效                                 |
| @ConditionalOnJava              | 指定的Java版本存在时起效                             |
| @ConditionalOnWebApplication    | Web应用环境下起效                                    |
| @ConditionalOnNotWebApplication | 非Web应用环境下起效                                  |
|                                 |                                                      |



## 6、`@ConfigurationProperties`

> 识别配置文件的前缀prefix，可以通过一个注解ConfigurationProperties然后根据属性进行注入   
>
> ` @ConfigurationProperties(prefix = "var")   `

### 1）`application.properties`

```properties
var.name=HealerJean
var.email=healerjean@gmail.com

```



### 2）`DataConfig`

```java
@ConfigurationProperties("var")
@Configuration
@Slf4j
@Data
public class DataConfig {

    private String name ;
    private String email;

    @Bean
    public DataBean dataBean() {
        log.info(name);
        return new DataBean();
    }

}

```





## 7、`@EnableConfigurationProperties`

> 如果一个配置类只配置 `@ConfigurationProperties`注解，而没有使用`@Component`，那么在 `IOC` 容器中是获取不到 `properties`  配置文件转化的 `bean` 。说白了 `@EnableConfigurationProperties` 相当于把使用  `@ConfigurationProperties `的类进行了一次注入。



### 1）`application.properties`

```properties
demo.name=healerjean
```

### 2）`DemoProperties`

```java
//一个配置类只配置@ConfigurationProperties注解，
// 而没有使用@Component，那么在IOC容器中是获取不到properties 配置文件转化的bean
@ConfigurationProperties("demo")
@Data
public class DemoProperties {

    private String name ;
}

```

### 3）、`DemoPropertiesAutoConfiguration`

> 写了 `@EnableConfigurationProperties(DemoProperties.class)`  可以不写 `@Configuration`，都是自动装配的意思

```java
@EnableConfigurationProperties(DemoProperties.class)
@Slf4j
public class DemoPropertiesAutoConfiguration {

    @Autowired
    private DemoProperties demoProperties;

    @PostConstruct
    public void init() {
        log.info("配置文件属性--------{}", demoProperties);
    }

}

```



### 4）替代方案  

```java
@Component
@ConfigurationProperties("demo")
@Data
public class DemoProperties {

    private String name ;
}

```







## 7、`@ImportResource`

> `springboot`加载`spring`配置文件，`@ImportResource`：      
>
> 通过 `locations` 属性加载对应的xml配置文件，同时需要配合`@Configuration`注解一起使用，定义为配置类   

```java
@SpringBootApplication
@ImportResource(value = "classpath:applicationContext.xml")
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}

```













## 8、`@Profile`

> `@Profile` 根据注解选中不同的配置文件   

```java
@profile("prop")  
public class ProductRpcImpl implements ProductRpc  
    
    public String productBaseInfo(Long sku){  
    return productResource.queryBaseInfo(Long sku);  
	}  
}  


/** 配置生产环境调用类  **/  
@profile("dev")  
public class MockProductRpcImpl implements ProductRpc  
    public String productBaseInfo(Long sku){  
    return “iphone7”;  
	}  
}  


/** 调用类  **/  
public class Demo(){  
    
    @Resource
    private ProductRpc productRpc;  

    public void demo(){  
        String skuInfo = productRpc.productBaseInfo(123123L);  
        logger.info(skuInfo);  
    }  
}
```



## 9、`@DependsOn`

> 如果需要指定一个 `Bean A`  先于 `Bean B` 加载，那么可以在 `Bean B` 类前加入 `@DependsOn("beanA")` ，指定依赖加载顺序。

```java
@Order(2)
@Aspect
@Component
@Slf4j
@DependsOn(value = "envUtils")
public class UserContextAspect {
```









## 10、读取配置文件`map`或`list`

### 1）`properties `读取

#### a、`properties `

```properties
custom.map={"k1":"HealerJean","k2":"IRO_MAN"}
custom.list=HealerJean,IRO_MAN
custom.set=HealerJean,IRO_MAN,HealerJean
```

#### b、读取

```java
@Slf4j
@Configuration
public class CollectionProperties {

    @Value("#{${custom.map}}")
    private Map<String, String> customMap;

    @Value("#{'${custom.list}'.split(',')}")
    private List<String> customList;

    @Value("#{'${custom.set}'.split(',')}")
    private Set<String> customSet;

}

```



### 2）`YML`读取

#### a、`YML`文件

```yaml
custom.flow.config:
  customEnum: ONE
  check:
    - check1
    - check2
  ymlInnerConfig:
    5000001:
      name: Healerjen
      check:
        - 5000001_check1
        - 5000001_check2
        - 5000001_check3
```

#### b、读取

```java
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "custom.flow.config")
@Data
public class CollectionYml {

    private CustomEnum customEnum;
    private List<String> check;
    private Map<String,YmlInnerConfig> ymlInnerConfig;


    @PostConstruct
    public void init() {
        log.info("[CollectionYml] customEnum:{}", customEnum);
        log.info("[CollectionYml] check:{}", check);
        log.info("[CollectionYml] ymlInnerConfig:{}", ymlInnerConfig);
    }

}



@Data
public class YmlInnerConfig {

    private String name;
    private Set<String> check;

}

```





## 11、`@PropertySource`

```java
@PropertySource(value = "classpath:db.properties")
public class SysDataSourceConfig {

   private Logger logger = LoggerFactory.getLogger(SysDataSourceConfig.class);

   @Value("${master.datasource.url}")
   private String dbUrl;

   @Value("${master.datasource.username}")
   private String username;

   @Value("${master.datasource.password}")
   private String password;

```

**`db.properties`**

```properties
master.datasource.url=
master.datasource.username=
master.datasource.password=
```











## 12、`@ControllerAdvice`

### 1）处理局部异常（`Controller`内）

```java
@Controller
public class LocalException {
    @ExceptionHandler
    public ModelAndView exceptionHandler(Exception ex){
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("exception", ex);
        System.out.println("in testExceptionHandler");
        return mv;
    }
    @RequestMapping("/error")
    public String error(){
        int i = 5/0;
        return "hello";
    }
}

```



### 2）处理全局异常（所有`Controller`）

```java
@ControllerAdvice
public class GlobalException {

    @RequestMapping("/globalException")
    public String error(){
        int i = 5/0;
        return "hello";
    }
}

```

```java
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Response<?> handler(Throwable t){
        log.error("系统错误", t);
        return Response.error();
    }

}

```



## 13、`@ModerAttributue`

`@ModelAttribute `放在方法的注解上时，代表的是**`Controller` 的所有方法在调用前，先执行此 `@ModelAttribute` 方法**   

```java
/**
 * ModelAttribute 没有参数，采用 map保存，相当于 model也是一样
 * Created by HealerJean on 2017/3/29.
 */
@Controller
public class TestMa1Controller {

    @ModelAttribute
    public void getUser(Map<String, Object> map){
        User user=new User( 23, "张宇晋");
        map.put("user", user);
    }
  
    @RequestMapping("test1")
    public String testModelAttribute(User user){
        System.out.println("修改："+user);
        return "index";
    }
}

```

![image-20200609195433207](D:\study\HealerJean.github.io\blogImages\image-20200609195433207.png)

**2、有User返回值，两个ModelAttribute**

```java
/**
 * ModelAttribute 不管有没有user参数 都可以返回值 ，
 * 解释;这种方法其实也是放到了model里面
 * 注意：有了返回值 User才能生效，仅仅是void不能生效
 */
@Controller
public class TestMa2Controller {
    //这种不带参数也是可以的
    //@ModelAttribute
    @ModelAttribute("user")
    public User getUser(Map<String, Object> map){
        User user=new User( 23, "张宇晋");
        return  user;
    }
    @RequestMapping("test2")
        // @ModelAttribute("user") 整体 可有可没有
        //public String testModelAttribute(@ModelAttribute("user") User user){
        public String testModelAttribute(User user){
        System.out.println("修改："+user);
        return "index";
    }
}
```

![image-20200609195939814](D:\study\HealerJean.github.io\blogImages\image-20200609195939814.png)

**3、自动匹配url**

```java
/**
 * 地址栏输入 http://localhost:8080/test3?age=23&name=zhangyujin
 */
@Controller
public class TestMa3Controller {

    @RequestMapping("test3")
    public String testModelAttribute(@ModelAttribute("user") User user){
        System.out.println("修改："+user);
        return "index";
    }
    
}
```

![image-20200609200022464](D:\study\HealerJean.github.io\blogImages\image-20200609200022464.png)





# 二、数据库

## 1、`@Transient`

> 实体类中如果加上这个注解将不会再数据库表中映射字段，例如下面的字码表中的使用

```java
@Column(length = 1,nullable = false)
private String status;


@Transient
private  String  statusName;
```



# 三、`JAVA`

## 1、`@Deprecated`

> 这个注释是一个标记注释。所谓标记注释，就是在源程序中加入这个标记后，并不影响程序的编译，但有时编译器会显示一些警告信息。         
>

 **那么 `Deprecated` 注释是什么意思呢？**      

如果你经常使用eclipse等IDE编写java程序时，可能会经常在属性或方法提示中看到这个词。如果某个类成员的提示中出现了个词，就表示这个并不建议使用这个类成员。因为这个类成员在未来的JDK版本中可能被删除。之所以在现在还保留，是因为给那些已经使用了这些类成员的程序一个缓冲期。如果现在就去了，那么这些程序就无法在新的编译器中编译了。 



**举例**：  `Deprecated` 注释还有一个作用。就是如果一个类从另外一个类继承，并且 `override` 被继承类的 `Deprecated` 方法，在编译时将会出现一个警告。如 `test.java` 的内容如下： 

```java
class Class1 { 
    
    @Deprecated public void myMethod(){
    } 
} 

class Class2 extends Class1 {
    public void myMethod(){
        
    } 
}

```

运行 `javac test.java` 出现如下警告： 这些警告并不会影响编译，只是提醒你一下尽量不要用 `myMethod` 方法。 

```java
注意：test.java 使用或覆盖了已过时的 API。 
注意：要了解详细信息，请使用 -Xlint:deprecation 重新编译 
使用-Xlint:deprecation显示更详细的警告信息： 

test.java:4: 警告：[deprecation] Class1 中的 myMethod() 已过时 

public void myMethod() 
^ 
1 警告 


```



![image-20200609183016797](D:\study\HealerJean.github.io\blogImages\image-20200609183016797.png)









## 2、`@index`、`@UniqueConstraint`

### 1 ）`@index`

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "region",
       indexes = {@Index(name = "my_index_name",  columnList="iso_code", unique = true),
                  @Index(name = "my_index_name2", columnList="name",     unique = false)})



//多列索引
@Entity
@Table(name    = "company__activity", 
       indexes = {@Index(name = "i_company_activity", columnList = "activity_id,company_id")})


```



### 2）`@UniqueConstraint`

```java
@Table(name = "stu_information",uniqueConstraints = {@UniqueConstraint(columnNames="stuNo")})




@Table(name="tbl_sky",uniqueConstraints = {@UniqueConstraint(columnNames={"month", "day"})})
```







# 四、`JSON`

## 1、`jackson`

### 1）`@JsonIgnore`

> 向前台返回数据时候不返回，而是进行忽略。这里使用字码表来返回真实数据，而不返回代码

```java
import com.fasterxml.jackson.annotation.JsonIgnore
```

### 2）`@JsonProperty`

> `@JsonProperty ` 此注解用于属性上，作用是把该属性的名称序列化为另外一个名称，如把 `trueName` 属性序列化为 `name` ，`@JsonProperty(value="name")`。   



### 3）`@JsonInclude `

> `SellerInfoEntity ` 的所有属性只有在不为 `null`  的时候才被转换成 `json` ， 如果为 `null`  就被忽略。并且如果 `password` 为空字符串也不会被转换，该注解也可以加在某个字段上。另外还有很多其它的范围，例如 `NON_EMPTY`、`NON_DEFAULT` 等

```java
JsonInclude.Include.NON_NULL  //只有在不为 null 的时候才被转换成 json
JsonInclude.Include.NON_EMPTY //只有在不为 空 的时候才被转换成 json
```

```java
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SellerInfoEntity {

    private String id;
    private String username;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String password;
    private String openid;

    private Timestamp createTime;
    private Timestamp updateTime;


    public SellerInfoEntity() {
    }

    public SellerInfoEntity(String id, String username, String password, String openid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.openid = openid;
    }
}



{
        "code": 0,
        "msg": "成功",
        "data": {
        	"id": "1",
       	 	"username": "user1",
        	"openid": "openid"
        }
}

```



### 4）`@JsonPropertyOrder`

> 在将` java` `pojo` 对象序列化成为 `json` 字符串时，使用 ``@JsonPropertyOrder` 可以指定属性在 json 字符串中的顺序。   



### 5）`@JsonIgnoreType`

> `@JsonIgnoreType`  标注在类上，当其他类有该类作为属性时，该属性将被忽略。,   
>
> 比如：`SomeEntity` 中的 `entity` 属性在json处理时会被忽略。

```java
package org.lifw.jackosn.annotation;
import com.fasterxml.jackson.annotation.JsonIgnoreType;


@JsonIgnoreType
public class SomeOtherEntity {
    private Long id;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
public class SomeEntity {
    private String name;
    private String desc;
    private SomeOtherEntity entity;
}

```



### 6）`@JsonIgnoreProperties`  

> `@JsonIgnoreProperties` 和 `@JsonIgnore` 的作用相同，都是告诉 `Jackson` 该忽略哪些属性， 
> 不同之处是 `@JsonIgnoreProperties`  是类级别的，并且可以同时指定多个属性。   

```java
@Data
@JsonIgnoreProperties(value = {"createTime","updateTime"})
public class SellerInfoEntity {

    private String id;
    private String username;
    private String password;
    private String openid;
    private Timestamp createTime;
    private Timestamp updateTime;
    public SellerInfoEntity() {
    }
    public SellerInfoEntity(String id, String username, String password, String openid) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.openid = openid;
    }
}

```



### 7）`@JsonFormat`

```java
// 自定义日期格式（序列化和反序列化时生效）
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
private LocalDateTime registerTime;

// 示例：将枚举值序列化为字符串而非数字
@JsonFormat(shape = JsonFormat.Shape.STRING)
private UserStatus status;
```

| **维度**     | **`@JsonFormat`（`Jackson` 注解）**             | **`@DateTimeFormat`（`Spring` 框架注解）**               |
| ------------ | ----------------------------------------------- | -------------------------------------------------------- |
| **作用对象** | `JSON` 数据的序列化（响应）和反序列化（请求体） | 表单参数、路径参数（`URL`）、请求参数（Query String）    |
| **处理阶段** | 处理 `JSON` 格式的请求体或响应体中的日期字段    | 处理 `HTTP` 请求中的非 `JSON` 格式参数（如 `URL`、表单） |
| **依赖框架** | 基于 `Jackson` 库（处理` JSON` 数据的标准组件） | 基于 `Spring` 框架的格式转换机制                         |
| **常用场景** | 前后端通过 `JSON` 交互时，控制日期的读写格式    | 处理` URL` 参数、表单提交等非 JSON 场景的日期解析        |



| **场景**                         | **推荐注解**      | **示例**                                                     |
| -------------------------------- | ----------------- | ------------------------------------------------------------ |
| `JSON` 请求体 / 响应体           | `@JsonFormat`     | 前端传递`{"date":"2023-10-01"}`，或后端返回带日期的 JSON 对象 |
| `URL` 参数 / `Query` `String`    | `@DateTimeFormat` | 访问`/api/events?date=2023-10-01`，解析`date`参数为日期对象  |
| 表单提交（`Form` `Data`）        | `@DateTimeFormat` | 前端通过`<input>`提交日期字符串，后端解析为日期对象          |
| 同时支持` JSON` 和非 `JSON` 参数 | 两者同时使用      | 字段同时需要处理 `JSON` 请求和 `URL` 参数中的日期格式        |



**问题1：为什么不能只用 `@JsonFormat` ？**

**场景1：** **处理 `URL` 参数或 `Query` `String`**：    当日期以 URL 参数形式传递时（如`/api/events?date=2023-10-01`），`@JsonFormat`无法生效，必须使用`@DateTimeFormat`：

**原因**：`URL` 参数属于 `HTTP` 请求的`ServletRequestParameter`，而非 JSON 数据，Jackson 不会处理这类参数。    

**场景2：** **处理表单提交（`Form` `Data`）**：当通过 `HTML` 表单提交日期时（如`<input type="text" name="startDate">`），同样需要`@DateTimeFormat`：表单数据以`x-www-form-urlencoded`或`multipart/form-data`格式传输，不属于 `JSON` ，因此`@JsonFormat`无效。



**问题1：为什么不能只用 `@DateTimeFormat`？场景示例**

1、 **处理 `JSON` 请求体或响应体**：当日期存在于` JSON` 数据中时（如前端传递 `JSON` 对象），必须使用`@JsonFormat`控制格式：`JSON` 数据的解析由 `Jackson` 负责，`@DateTimeFormat`无法干预 Jackson 的反序列化流程。

2、**控制响应中的日期格式**：`@DateTimeFormat`主要用于请求参数解析，无法控制响应数据的格式。



**问题3：何时需要同时使用两者？**

答案：当一个日期字段需要同时处理 **`JSON` 请求体**和**非 `JSON` 参数**时，需同时添加两个注解：



## 2、`fastjson`

### 1）`@JSONField`

#### a、修改和`json`字符串的字段映射【name】

```java
@JSONField(name="testid") 　
private Integer aid;
```



#### b、格式化数据【format 】 

```java
@JSONField(format = "yyyy-MM-dd HH:mm:ss")
private Integer dateCompleted;
```



#### c、过滤掉不需要序列化的字段【serialize】

```java
@JSONField(serialize = false)
private Integer dateCompleted;
```





## 3、`Spring`

### 1）**`@DateTimeFormat`**

> 存日期使用

```java
// 同时使用@DateTimeFormat（处理表单数据）和@JsonFormat（处理JSON数据）
@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
private LocalDateTime startTime;

```



# 五、`XML`



## 1、`@XmlRootElement`、`@XmlElement`、`@XmlAttribute` 

```java

@XmlRootElement(name="rootclass")
@Data
public class RootClass {

    @XmlElement(name="eleClassA")
    private EleClassA a;
    
    @XmlElement(name="EleclassA")
    private EleClassB b;
    
    private String root;
    private String rootA;

}
```



```java
@Data
public class EleClassA {

    @XmlAttribute()
    private String attrC;
    
    @XmlElement
    private String eleA;
    
    @XmlElement(name = "elebnewname")
    private String eleB;

 
}
```



```java

public class EleClassB {

    @XmlAttribute
    private String attrUserName;
    
    @XmlAttribute(name="password")
    private String attrPassword;

    @XmlElement
    private String eleCode;

 

}
```



```java

public class Test {

    public static void main(String[] args) {
        RootClass rc = new RootClass();
        EleClassA a = new EleClassA();
        EleClassB b = new EleClassB();

        a.setAttrC("attrc");
        a.setEleA("eleA");
        a.setEleB("eleB");

        b.setAttrPassword("attrPassword");
        b.setAttrUserName("attrUsrName");
        b.setEleCode("eleCode");

        rc.setA(a);
        rc.setB(b);
        rc.setRoot("root");
        rc.setRootA("rootA");

        JAXBContext context;
        try {
            context = JAXBContext.newInstance(RootClass.class);
            Marshaller mar = context.createMarshaller();
            mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            mar.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter writer = new StringWriter();

            mar.marshal(rc, writer);

            System.out.println(writer.toString());
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}

```

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<rootclass>
    <eleClassA attrC="attrc">
        <eleA>eleA</eleA>
        <elebnewname>eleB</elebnewname>
    </eleClassA>
    <EleclassA attrUserName="attrUsrName" password="attrPassword">
        <eleCode>eleCode</eleCode>
    </EleclassA>
    <root>root</root>
    <rootA>rootA</rootA>
</rootclass>

```





# 2、理论注解

## 2.1、`@EnableAutoConfiguration`

> 开启自动配置功能   `**@EnableAutoConfiguration`**通知SpringBoot开启自动配置功能，这样自动配置才能生效。   
>
> `@AutoConfigurationPackage`  ：自动配置包注解



```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
public @interface EnableAutoConfiguration {
```



## 2.2、`@SpringBootAppliction`

> 注解它的表示一个启动类，里面包含main方法      
>
> 使用下面这个三个的注解和它是一样的     
>
> `@Configuration `   
>
> `@EnableAutoConfiguration` ：让springboot 根据类的路径中的jar包依赖为当前项目进行自动配置，比如，添加了spring-boot-start-web，会自动添加Tomact和spring MVC 的依赖。这样就会对二者进行自动配置）  
>
> `@ComponentScan`：spring boot会自动扫描 springBootApplication 所在类的同级别的包，所以这个启动类要放到组合包下
>
>  


## 2.3、`@ComponentScan`

> @ComponentScan 自动扫描包下所有使用@Component @Service @Repository @Controller 的类 



















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
		id: 'LBSmPY9k7jWMQ2fV',
    });
    gitalk.render('gitalk-container');
</script> 
