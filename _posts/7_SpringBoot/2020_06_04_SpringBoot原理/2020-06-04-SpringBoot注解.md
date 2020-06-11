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



> `@Configuration`注解提供了全新的`bean`创建方式。最初`spring`通过`xml`配置文件初始化`bean`并完成依赖注入工作。     
>
> 从spring3.0开始，在`spring framework`模块中提供了这个注解，搭配`@Bean`等注解，可以完全不依赖xml配置，在运行时完成bean的创建和初始化工作





# 1、@Bean

> 从spring3.0开始，在`spring framework`模块中提供了这个注解，`@Bean`搭配`@Configuration`注解，可以完全不依赖xml配置，**在运行时完成bean的创建和初始化工作**    



## 1.1、实例使用

### 1.1.1、`DataBean.java`

```java
@Slf4j
public class DataBean {

    public void method() {
       log.info("DataBean--------调用方法：method--------");
    }

}

```



### 1.1.1、`DataConfig.java`

```java
public class DataConfig {

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```





# 2、`@Controller`, `@Service`, `@Repository`, `@Component  `   



> **1、@Component: 表明这个注释的类是一个组件，当使用基于注释的配置和类路径扫描时，这些类被视为自动检测的候选者，没有明确的角色。**     
>
> 2、@Controller: 表明一个注解的类是一个Controller，也就是控制器，可以把它理解为MVC 模式的Controller 这个角色。 
>
> 3、@Service: 表明这个带注解的类是一个"Service"，也就是服务层，可以把它理解为MVC 模式中的Service层这个角色（@Autowired @Inject @Resourse，是等效的,可以注解到set方法，或者属性上。一般都是属性上），
>
> 4、@Repository: 表明这个注解的类是一个"Repository",团队实现了JavaEE 模式中像是作为"Data Access Object" 可能作为DAO来使用，当与 PersistenceExceptionTranslationPostProcessor 结合使用时，这样注释的类有资格获得Spring转换的目的。这个注解也是@Component 的一个特殊实现，允许实现类能够被自动扫描到     



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





# 3、`@Configuration`   

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



## 3.1、@Configuration 和 @Component区别

>  通过上面的介绍，可以看到`@Configuration` 标记了`@Component`元注解，某种意义上来讲，二者的使用是没有区别的，但是他们的区别到底是什么呢  



### 3.1.1、`@Configuration`注解

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



### 3.1.2、`@Component`注解

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



### 3.1.3、总结   

从定义来看， `@Configuration` 注解本质上还是 `@Component`，因此 或者 `@ComponentScan` 都能处理`@Configuration` 注解的类，**但是还是有区别，如下：**      

Spring 容器在启动时，会加载默认的一些 `PostPRocessor`，**其中就有 `ConfigurationClassPostProcessor`，这个后置处理程序专门处理带有 `@Configuration` 注解的类**，这个程序会在 `bean` 定义加载完成后，在 `Bean` 初始化前进行处理。**主要处理的过程就是将带有@Configuration类进行增强，在初始化@Bean注解的Bean类的时候，该方法里面调用别的方法会判断其他方法是否也有@Bean，如果有的话，那么会直接调用Spring容器中的`DataBean`实例，而不会重新创建**：具体还是看下面的`@Configuration`源码吧      



**`@Component`注释的类没有被动态代理增强怎么办呢，使用注入即可，如下两种都是注入的方式**

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




## 3.2、搭配`@Autowired`

> **因为`@Configuration`本身也是一个`@Component`，因此配置类本身也会被注册到应用上下文，并且也可以使用IOC的`@Autowired`等注解来注入所需bean**。



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



## 3.3、搭配`@CompomentScan`

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



# 4、`@Import`注解组合使用

> 有时没有把某个类注入到IOC容器中，但在运用的时候需要获取该类对应的bean，此时就需要用到@Import注解  



## 4.1、单纯的类 

### 4.1.1、`DataBean.java`：单纯的类

```java
@Slf4j
public class DataBean {

    public void method() {
       log.info("DataBean--------调用方法：method--------");
    }

}
```



### 4.1.2、实例使用

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



## 4.2、和`@Configuration`搭配使用 

> 可以看到只注册了`AppConfig.class`，容器自动会把@Import指向的配置类初始化

### 4.2.1、`DataConfig.java`：

> 没有注解，但是里面有 `@Bean`

```java
public class DataConfig {

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
```



### 4.2.2、`AppConfig.java`

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





 

# 5、Condition相关注解

> 可以根据代码中设置的条件装载不同的bean，  



## 5.1、@Conditional  

> `@Conditional`注解定义如下，其内部主要就是利用了Condition接口，来判断是否满足条件，从而决定是否需要加载Bean  ,   
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



### 5.1.1、实例使用 

#### 5.1.1.1、实现类：`EncryptCondition`

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



#### 5.1.1.2、注解`@Conditional(EncryptCondition.class)`

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





## 5.2、@ConditionalOnProperty

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



### 5.3、实例使用

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





### 5.3、其他Condition



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



# 6、@ConfigurationProperties

> 识别配置文件的前缀prefix，可以通过一个注解ConfigurationProperties然后根据属性进行注入   
>
> ` @ConfigurationProperties(prefix = "var")   `



## 6.1、实例使用

### 6.1.1、`application.properties`

```properties
var.name=HealerJean
var.email=healerjean@gmail.com

```



### 6.1.2、`DataConfig`

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





# 7、`@Transient`

> 实体类中如果加上这个注解将不会再数据库表中映射字段，例如下面的字码表中的使用



```java
@Column(length = 1,nullable = false)
private String status;


@Transient
private  String  statusName;
```



# 8、@Deprecated

> 这个注释是一个标记注释。所谓标记注释，就是在源程序中加入这个标记后，并不影响程序的编译，但有时编译器会显示一些警告信息。         
>
> 

 **那么Deprecated注释是什么意思呢？**      

如果你经常使用eclipse等IDE编写java程序时，可能会经常在属性或方法提示中看到这个词。如果某个类成员的提示中出现了个词，就表示这个并不建议使用这个类成员。因为这个类成员在未来的JDK版本中可能被删除。之所以在现在还保留，是因为给那些已经使用了这些类成员的程序一个缓冲期。如果现在就去了，那么这些程序就无法在新的编译器中编译了。 



**举例**：  Deprecated注释还有一个作用。就是如果一个类从另外一个类继承，并且override被继承类的Deprecated方法，在编译时将会出现一个警告。如test.java的内容如下： 

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

运行javac test.java 出现如下警告： 这些警告并不会影响编译，只是提醒你一下尽量不要用myMethod方法。 

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



# 9、`@ImportResource`

> springboot加载spring配置文件，`@ImportResource`：      
>
> 通过locations属性加载对应的xml配置文件，同时需要配合`@Configuration`注解一起使用，定义为配置类   

```java
@SpringBootApplication
@ImportResource(value = "classpath:applicationContext.xml")
public class CrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }
}

```





# 10、`@index`、`@UniqueConstraint`

## 10.1、`@index`

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



## 10.2、`@UniqueConstraint`

```java
@Table(name = "stu_information",uniqueConstraints = {@UniqueConstraint(columnNames="stuNo")})




@Table(name="tbl_sky",uniqueConstraints = {@UniqueConstraint(columnNames={"month", "day"})})
```







# 11、@Profile

> @Profile根据注解选中不同的配置文件   



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





# 12、`@JsonIgnore`

> 向前台返回数据，时候不返回而是进行忽略。这里使用字码表来返回真实数据，而不返回代码





# 13、`@JsonProperty`

> @JsonProperty 此注解用于属性上，作用是把该属性的名称序列化为另外一个名称，如把trueName属性序列化为name，@JsonProperty(value="name")。   



# 14、`@JSONField`



### 14.1、修改和json字符串的字段映射【name】

```java
@JSONField(name="testid") 　
private Integer aid;

```

### 14.2、格式化数据【format 】 

```java
@JSONField(format = "yyyy-MM-dd HH:mm:ss")
private Integer dateCompleted;
```





### 14.3、过滤掉不需要序列化的字段【serialize】

```java
@JSONField(serialize = false)
private Integer dateCompleted;
```



# 15、`@JsonInclude `

> SellerInfoEntity 的所有属性只有在不为 null 的时候才被转换成 json， 如果为 null 就被忽略。并且如果password为空字符串也不会被转换，该注解也可以加在某个字段上。另外还有很多其它的范围，例如 NON_EMPTY、NON_DEFAULT等



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



# 16、`@JsonPropertyOrder`

> 在将 java pojo 对象序列化成为 json 字符串时，使用 ``@JsonPropertyOrder` 可以指定属性在 json 字符串中的顺序。   



# 17、`@JsonIgnoreType`

> `@JsonIgnoreType` 标注在类上，当其他类有该类作为属性时，该属性将被忽略。,   
>
> 比如：SomeEntity 中的 entity 属性在json处理时会被忽略。

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



# 18、`@JsonIgnoreProperties`  

> `@JsonIgnoreProperties` 和 `@JsonIgnore` 的作用相同，都是告诉 Jackson 该忽略哪些属性， 
>  不同之处是 @JsonIgnoreProperties 是类级别的，并且可以同时指定多个属性。   



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





# 19、`@PropertySource`

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





# 20、`@XmlRootElement`、`@XmlElement`、`@XmlAttribute` 



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





# 21、`@ControllerAdvice`

## 21.1、处理局部异常（Controller内）

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



## 21.2、处理全局异常（所有Controller）

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



# 22、`@ModerAttributue`



1、@ModelAttribute放在方法的注解上时，代表的是**Controller的所有方法在调用前，先执行此@ModelAttribute方法**   

```java
/**
 * ModelAttribute 没有参数，采用 map保存，相当于model也是一样
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

# 23、`@ConfigurationProperties`、`@EnableConfigurationProperties`

> 如果一个配置类只配置`@ConfigurationProperties`注解，而没有使用`@Component`，那么在IOC容器中是获取不到properties 配置文件转化的bean。说白了 `@EnableConfigurationProperties` 相当于把使用  `@ConfigurationProperties `的类进行了一次注入。



## 23.1、使用

### 23.1.1、`application.properties`

```properties
demo.name=healerjean
```

### 23.1.2、`DemoProperties`

```java
//一个配置类只配置@ConfigurationProperties注解，
// 而没有使用@Component，那么在IOC容器中是获取不到properties 配置文件转化的bean
@ConfigurationProperties("demo")
@Data
public class DemoProperties {

    private String name ;
}

```



### 23.1.3、`DemoPropertiesAutoConfiguration`

```java
@Configuration
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



## 23.2、替代方案  

第一种：

```java
@ComponentScan
@ConfigurationProperties("demo")
@Data
public class DemoProperties {

    private String name ;
}

```









# 2、理论注解

## 2.1、@EnableAutoConfiguration

> 开启自动配置功能   **@EnableAutoConfiguration**通知SpringBoot开启自动配置功能，这样自动配置才能生效。   
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











## 2.2、@SpringBootAppliction

> 注解它的表示一个启动类，里面包含main方法      
>
> 使用下面这个三个的注解和它是一样的     
>
> @Configuration    
>
> `@EnableAutoConfiguration` ：让springboot 根据类的路径中的jar包依赖为当前项目进行自动配置，比如，添加了spring-boot-start-web，会自动添加Tomact和spring MVC 的依赖。这样就会对二者进行自动配置）  
>
> `@ComponentScan`：spring boot会自动扫描 springBootApplication 所在类的同级别的包，所以这个启动类要放到组合包下
>
>  


## 2.3、`@ComponentScan`

> @ComponentScan 自动扫描包下所有使用@Component @Service @Repository @Controller 的类 














# 3、源码解析 



## 3.1、@Configuration和@Component区别源码解析

> 为什么这么搞呢@Configuration它本身就是基于配置的，内部肯定会有一些相互调用，为了保证是单例的，所以这样实现了。    
>
> 而@Component是基于类而已，大概意思就是只要使用它注释了，大概率上是希望被扫描到。至于是不是单例，不关心。  

### 3.1.1、找出带有@Configuration的的类，使它变成增强类  



> `ConfigurationClassPostProcessor `后置处理器  中的方法`enhanceConfigurationClasses`，使用cglib动态代理增强类替换原有的类      
>
> 第一个for循环中，查找到所有带有 @Configuration 注解的 bean 定义，然后放入configBeanDefs ,Map中：        
>
> 第二个for循环，对带有@Configuration的类进行增强，具体就是，返回返回 Enhancer类替代原有的列，内部加入一些拦截器等    



```java
public void enhanceConfigurationClasses(ConfigurableListableBeanFactory beanFactory) {
    Map<String, AbstractBeanDefinition> configBeanDefs = new LinkedHashMap<String, AbstractBeanDefinition>();

    //在方法的第一个for循环中，查找到所有带有 @Configuration 注解的 bean 定义，然后放入configBeanDefs ,Map中：
    for (String beanName : beanFactory.getBeanDefinitionNames()) {
        BeanDefinition beanDef = beanFactory.getBeanDefinition(beanName);
        //判断是类否有@Configuration注解
        if (ConfigurationClassUtils.isFullConfigurationClass(beanDef)) {
            //省略部分代码
            configBeanDefs.put(beanName, (AbstractBeanDefinition) beanDef);
        }
    }
    if (configBeanDefs.isEmpty()) {
        // nothing to enhance -> return immediately
        return;
    }




    //第二个for循环，对带有@Configuration的类进行增强，具体就是，返回返回 Enhancer类替代原有的列，内部加入一些拦截器等
    ConfigurationClassEnhancer enhancer = new ConfigurationClassEnhancer();
    for (Map.Entry<String, AbstractBeanDefinition> entry : configBeanDefs.entrySet()) {
        AbstractBeanDefinition beanDef = entry.getValue();
        // If a @Configuration class gets proxied, always proxy the target class
        beanDef.setAttribute(AutoProxyUtils.PRESERVE_TARGET_CLASS_ATTRIBUTE, Boolean.TRUE);
        try {
            // Set enhanced subclass of the user-specified bean class
            Class<?> configClass = beanDef.resolveBeanClass(this.beanClassLoader);
            Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
            if (configClass != enhancedClass) {
                //省略部分代码
                beanDef.setBeanClass(enhancedClass);
            }
        }
        catch (Throwable ex) {
            throw new IllegalStateException(
                "Cannot load configuration class: " + beanDef.getBeanClassName(), ex);
        }
    }
}

```





**对原有的类进行增强，形成增强类，然后使用增强后的类替换了原有的 `beanClass`：，当下面两行执行完成后带有@Configuration的类就变成了增强类**

```java
Class<?> enhancedClass = enhancer.enhance(configClass, this.beanClassLoader);
```

```java
beanDef.setBeanClass(enhancedClass);
```



**类：`ConfigurationClassEnhancer`：包含增强类的实现，已经增强类内部的一些东西，比如cglib代理的类，会通过CallbackFilter会进行调用，那这里会指定这个调用类的**

```java
public Class<?> enhance(Class<?> configClass, @Nullable ClassLoader classLoader) {
    if (EnhancedConfiguration.class.isAssignableFrom(configClass)) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Ignoring request to enhance %s as it has " +
                                       "already been enhanced. This usually indicates that more than one " +
                                       "ConfigurationClassPostProcessor has been registered (e.g. via " +
                                       "<context:annotation-config>). This is harmless, but you may " +
                                       "want check your configuration and remove one CCPP if possible",
                                       configClass.getName()));
        }
        return configClass;
    }
    //创建代理类增强类
    Class<?> enhancedClass = createClass(newEnhancer(configClass, classLoader));
    if (logger.isTraceEnabled()) {
        logger.trace(String.format("Successfully enhanced %s; enhanced class name is: %s",
                                   configClass.getName(), enhancedClass.getName()));
    }
    return enhancedClass;
}
```





**构造一个cglib增强类，**

```java
//指定cglib代理的增强类 在调用方法的时候，会通过CallbackFilter 调用，我们这里放入该接口
enhancer.setCallbackFilter(CALLBACK_FILTER);
```




```java

private Enhancer newEnhancer(Class<?> configSuperClass, @Nullable ClassLoader classLoader) {
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(configSuperClass);
		enhancer.setInterfaces(new Class<?>[] {EnhancedConfiguration.class});
		enhancer.setUseFactory(false);
		enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
		enhancer.setStrategy(new BeanFactoryAwareGeneratorStrategy(classLoader));
      //指定cglib代理的增强类 在调用方法的时候，会通过CallbackFilter 调用，我们这里放入该接口
		enhancer.setCallbackFilter(CALLBACK_FILTER);
		enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
		return enhancer;
	}
```



**`CallbackFilter`，具体的实现类是`ConditionalCallbackFilter`，同时它也是我们增强配置类`ConfigurationClassEnhancer`的内部类**

```java
	private static final Callback[] CALLBACKS = new Callback[] {
			new BeanMethodInterceptor(),
			new BeanFactoryAwareMethodInterceptor(),
			NoOp.INSTANCE
	};

	private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);

```



### 3.1.2、Clglib动态代理调用增强类，选择带有@Bean注释的方法，准备创建Bean 



> cglib动态代理调用增强类`Enhancer`，,增强类内部有`emitMethods`作为调用的入口。     
>
> 然后调用`ConditionalCallbackFilter的accept方法`，判断是否有@Bean注解（千万注意，这里就是正常的@Bean哦）并返回拦截器的 索引，这个拦截器将会在创建Bean类的时候会执行  
>
> 

```java
 int index = this.filter.accept(actualMethod);
```



```java
private void emitMethods(ClassEmitter ce, List methods, List actualMethods) {
    CallbackGenerator[] generators = CallbackInfo.getGenerators(this.callbackTypes);
    Map groups = new HashMap();
    final Map indexes = new HashMap();
    final Map originalModifiers = new HashMap();
    final Map positions = CollectionUtils.getIndexMap(methods);
    Map declToBridge = new HashMap();
    Iterator it1 = methods.iterator();
    Iterator it2 = actualMethods != null ? actualMethods.iterator() : null;

    while(it1.hasNext()) {
        MethodInfo method = (MethodInfo)it1.next();
        Method actualMethod = it2 != null ? (Method)it2.next() : null;
        int index = this.filter.accept(actualMethod);
        if (index >= this.callbackTypes.length) {
            throw new IllegalArgumentException("Callback filter returned an index that is too large: " + index);
        }

        originalModifiers.put(method, actualMethod != null ? actualMethod.getModifiers() : method.getModifiers());
        indexes.put(method, index);
        List group = (List)groups.get(generators[index]);
        if (group == null) {
            groups.put(generators[index], group = new ArrayList(methods.size()));
        }

        ((List)group).add(method);
        if (TypeUtils.isBridge(actualMethod.getModifiers())) {
            Set bridges = (Set)declToBridge.get(actualMethod.getDeclaringClass());
            if (bridges == null) {
                bridges = new HashSet();
                declToBridge.put(actualMethod.getDeclaringClass(), bridges);
            }

            ((Set)bridges).add(method.getSignature());
        }
    }

    //具体初始化Bean逻辑
    ………………………………

        se.return_value();
    se.end_method();
}
```





`ConditionalCallbackFilter`,中的`accept`方法，判断是方法上否有@Bean定义，如果有的话，就返回，将来要初始化成Bean类。  



```java
private static class ConditionalCallbackFilter implements CallbackFilter {

    private final Callback[] callbacks;

    private final Class<?>[] callbackTypes;

    public ConditionalCallbackFilter(Callback[] callbacks) {
        this.callbacks = callbacks;
        this.callbackTypes = new Class<?>[callbacks.length];
        for (int i = 0; i < callbacks.length; i++) {
            this.callbackTypes[i] = callbacks[i].getClass();
        }
    }

    @Override
    public int accept(Method method) {
        for (int i = 0; i < this.callbacks.length; i++) {
            Callback callback = this.callbacks[i];
            if (!(callback instanceof ConditionalCallback) || ((ConditionalCallback) callback).isMatch(method)) {
                return i;
            }
        }
        throw new IllegalStateException("No callback available for method " + method.getName());
    }

    public Class<?>[] getCallbackTypes() {
        return this.callbackTypes;
    }
}
```



其中 `BeanMethodInterceptor` 匹配方法如下：



```java
@Override
public boolean isMatch(Method candidateMethod) {
    return (candidateMethod.getDeclaringClass() != Object.class &&
            !BeanFactoryAwareMethodInterceptor.isSetBeanFactory(candidateMethod) &&
            BeanAnnotationHelper.isBeanAnnotated(candidateMethod));
}





public static boolean isBeanAnnotated(Method method) {
    return AnnotatedElementUtils.hasAnnotation(method, Bean.class);
}
```



另一个 `BeanFactoryAwareMethodInterceptor` 匹配的方法如下，这个貌似在我们现在的测试中也用不到：

```java
@Override
public boolean isMatch(Method candidateMethod) {
    return isSetBeanFactory(candidateMethod);
}

public static boolean isSetBeanFactory(Method candidateMethod) {
    return (candidateMethod.getName().equals("setBeanFactory") &&
            candidateMethod.getParameterCount() == 1 &&
            BeanFactory.class == candidateMethod.getParameterTypes()[0] &&
            BeanFactoryAware.class.isAssignableFrom(candidateMethod.getDeclaringClass()));
}
```



### 3.2.3、拦截器开始创建Bean

> 上面我们知道带有@Configuration 以及@Bean的类是如何被识别的。现在我们开始关注`BeanMethodInterceptor`，开始带有@Bean注解的方法执行逻辑 ，也就是创建Bean类     



```java
private static class BeanMethodInterceptor implements MethodInterceptor, ConditionalCallback {

		/**
		 * Enhance a {@link Bean @Bean} method to check the supplied BeanFactory for the
		 * existence of this bean object.
		 * @throws Throwable as a catch-all for any exception that may be thrown when invoking the
		 * super implementation of the proxied method i.e., the actual {@code @Bean} method
		 */
		@Override
		@Nullable
		public Object intercept(Object enhancedConfigInstance, Method beanMethod, Object[] beanMethodArgs,
					MethodProxy cglibMethodProxy) throws Throwable {

            //首先通过反射从增强的 Configuration 注解类中获取 beanFactory
			ConfigurableBeanFactory beanFactory = getBeanFactory(enhancedConfigInstance);
			String beanName = BeanAnnotationHelper.determineBeanNameFor(beanMethod);

            //然后通过方法获取 beanName，默认为方法名，可以通过 @Bean 注解指定
			if (BeanAnnotationHelper.isScopedProxy(beanMethod)) {
				String scopedBeanName = ScopedProxyCreator.getTargetBeanName(beanName);
				if (beanFactory.isCurrentlyInCreation(scopedBeanName)) {
					beanName = scopedBeanName;
				}
			}

			//确定这个 bean 是否指定了代理的范围
			//默认下面 if 条件 false 不会执行
			if (factoryContainsBean(beanFactory, BeanFactory.FACTORY_BEAN_PREFIX + beanName) &&
					factoryContainsBean(beanFactory, beanName)) {
				Object factoryBean = beanFactory.getBean(BeanFactory.FACTORY_BEAN_PREFIX + beanName);
				if (factoryBean instanceof ScopedProxyFactoryBean) {
					// Scoped proxy factory beans are a special case and should not be further proxied
				}
				else {
					// It is a candidate FactoryBean - go ahead with enhancement
					return enhanceFactoryBean(factoryBean, beanMethod.getReturnType(), beanFactory, beanName);
				}
			}

            
            //重头戏来了，判断当前执行的方法是否为正在执行的 @Bean 方法，如果是的话，则执行，
            //appBean() 方法首次进入的时候，会执行下面的cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
            //appBean() 内部方法调用到dataBean()的时候，还会进来（请记住因为有烂机器的关系，dataBean()上面就有@Bean，如果是普通new就不会进来了），这个时候就是false，然后会执行 resolveBeanReference方法
			if (isCurrentlyInvokedFactoryMethod(beanMethod)) {
				// The factory is calling the bean method in order to instantiate and register the bean
				// (i.e. via a getBean() call) -> invoke the super implementation of the method to actually
				// create the bean instance.
				if (logger.isInfoEnabled() &&
						BeanFactoryPostProcessor.class.isAssignableFrom(beanMethod.getReturnType())) {
					logger.info(String.format("@Bean method %s.%s is non-static and returns an object " +
									"assignable to Spring's BeanFactoryPostProcessor interface. This will " +
									"result in a failure to process annotations such as @Autowired, " +
									"@Resource and @PostConstruct within the method's declaring " +
									"@Configuration class. Add the 'static' modifier to this method to avoid " +
									"these container lifecycle issues; see @Bean javadoc for complete details.",
							beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName()));
				}
               //直接调用原方法创建 bean
				return cglibMethodProxy.invokeSuper(enhancedConfigInstance, beanMethodArgs);
			}

            //如果不满足上面 if，也就是在 appBean() 中调用的 dataBean() 方法
			return resolveBeanReference(beanMethod, beanMethodArgs, beanFactory, beanName);
		}
```



`obtainBeanInstanceFromFactory` 方法比较简单，就是通过 `beanFactory.getBean` 获取 `Country`，如果已经创建了就会直接返回，如果没有执行过，就会通过 `invokeSuper` 首次执行。   



```java
private Object resolveBeanReference(Method beanMethod, Object[] beanMethodArgs,
                                    ConfigurableBeanFactory beanFactory, String beanName) {

    boolean alreadyInCreation = beanFactory.isCurrentlyInCreation(beanName);
    try {
        if (alreadyInCreation) {
            beanFactory.setCurrentlyInCreation(beanName, false);
        }
        boolean useArgs = !ObjectUtils.isEmpty(beanMethodArgs);
        if (useArgs && beanFactory.isSingleton(beanName)) {
            
            for (Object arg : beanMethodArgs) {
                if (arg == null) {
                    useArgs = false;
                    break;
                }
            }
        }
        
        //通过beanFactory.getBean(beanName)); 获取Bean，如果Spring容器已经创建了就直接返回，如果没有执行过，则还是会调用上面的方法，再次进入拦截器进行创建，通过invokeSuper调用执行。创建出Bean来
        Object beanInstance = (useArgs ? beanFactory.getBean(beanName, beanMethodArgs) :
                               beanFactory.getBean(beanName));
        if (!ClassUtils.isAssignableValue(beanMethod.getReturnType(), beanInstance)) {
            // Detect package-protected NullBean instance through equals(null) check
            if (beanInstance.equals(null)) {
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("@Bean method %s.%s called as bean reference " +
                                               "for type [%s] returned null bean; resolving to null value.",
                                               beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName(),
                                               beanMethod.getReturnType().getName()));
                }
                beanInstance = null;
            }
            else {
                String msg = String.format("@Bean method %s.%s called as bean reference " +
                                           "for type [%s] but overridden by non-compatible bean instance of type [%s].",
                                           beanMethod.getDeclaringClass().getSimpleName(), beanMethod.getName(),
                                           beanMethod.getReturnType().getName(), beanInstance.getClass().getName());
                try {
                    BeanDefinition beanDefinition = beanFactory.getMergedBeanDefinition(beanName);
                    msg += " Overriding bean of same name declared in: " + beanDefinition.getResourceDescription();
                }
                catch (NoSuchBeanDefinitionException ex) {
                    // Ignore - simply no detailed message then.
                }
                throw new IllegalStateException(msg);
            }
        }
        Method currentlyInvoked = SimpleInstantiationStrategy.getCurrentlyInvokedFactoryMethod();
        if (currentlyInvoked != null) {
            String outerBeanName = BeanAnnotationHelper.determineBeanNameFor(currentlyInvoked);
            beanFactory.registerDependentBean(beanName, outerBeanName);
        }
        return beanInstance;
    }
    finally {
        if (alreadyInCreation) {
            beanFactory.setCurrentlyInCreation(beanName, true);
        }
    }
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
