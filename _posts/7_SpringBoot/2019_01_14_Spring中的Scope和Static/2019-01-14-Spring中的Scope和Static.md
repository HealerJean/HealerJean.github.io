---
title: spring中的scope和static
date: 2018-01-14 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: spring中的scope和static
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             



scope的作用域。默认是单例模式，即scope="singleton"。另外scope还有prototype、request、session、global session作用域。scope="prototype"多例。再配置bean的作用域时，它的头文件形式如下：    

1、 在spring2.0之前bean只有2种作用域即：singleton(单例)、non-singleton（也称 prototype）   

2、Spring2.0以后，增加了session、request、global session三种专用于Web应用程序上下文的Bean。因此，默认情况下Spring2.0现在有五种类型的Bean。当然，Spring2.0对 Bean的类型的设计进行了重构，并设计出灵活的Bean类型支持，理论上可以有无数多种类型的Bean，用户可以根据自己的需要，增加新的Bean类 型，满足实际应用需求。




|scope|详情|
|---|---|
|singleton|表示在spring容器中的单例，通过spring容器获得该bean时总是返回唯一的实例|
|prototype|表示每次获得bean都会生成一个新的对象|
|request|表示在一次http请求内有效（只适用于web应用|
|session|表示在一个用户会话内有效（只适用于web应用）|
|globalSession|表示在全局会话内有效（只适用于web应用）|

<font  color="red" size="4">  

一般情况下，我们只会使用singleton和prototype两种scope，如果在spring配置文件内未指定scope属性，默认为singleton。

单例的原因有二：
1、为了性能。
2、不需要多例。


 </font>

 默认情况下，从bean工厂所取得的实例为Singleton（bean的singleton属性） Singleton: spring容器只存在一个共享的bean实例，
 Prototype: 每次对bean的请求都会创建一个新的bean实例。二者选择的原则：有状态的bean都使用Prototype作用域 ，而对无状态的bean则应该使用singleton作用域。


## 1、测试

 最佳实践：定义一个非静态成员变量时候，则通过注解@Scope("prototype")，将其设置为多例模式(每次都会重新new一个)。


```java

@RestController
@RequestMapping("scope")
public class ScopeController {

    public static int static_n = 1 ;
    public  int no_static_n  = 1 ;

    //http://localhost:8080/scope/default
    @ResponseBody
    @GetMapping("default")
    public String getN(){
        ++ static_n ;
        ++ no_static_n ;
        return "静态："+static_n +"+非静态"+no_static_n ;
    }

    //静态：2+非静态2
    //静态：3+非静态3
    //静态：4+非静态4
    //静态：5+非静态5
    //静态：6+非静态6
    //静态：7+非静态7

}


@Scope("singleton")
@RestController
@RequestMapping("scope")
public class SingletonController {

    public static int static_n = 1 ;
    public  int no_static_n  = 1 ;

    //http://localhost:8080/scope/singleton
    @ResponseBody
    @GetMapping("singleton")
    public String getN(){
        ++ static_n ;
        ++ no_static_n ;
        return "静态："+static_n +"+非静态"+no_static_n ;
    }

    //静态：2+非静态2
    //静态：3+非静态3
    //静态：4+非静态4
    //静态：5+非静态5
    //静态：6+非静态6
    //静态：7+非静态7
}



@Scope("prototype")
@RestController
@RequestMapping("scope")
public class PrototypeController {

    public static int static_n = 1 ;
    public  int no_static_n  = 1 ;

    //http://localhost:8080/scope/prototype
    @ResponseBody
    @GetMapping("prototype")
    public String getN(){
        ++ static_n ;
        ++ no_static_n ;
        return "静态："+static_n +"+非静态"+no_static_n ;
    }

    //静态：2+非静态2
    //静态：3+非静态2
    //静态：4+非静态2
    //静态：5+非静态2
    //静态：6+非静态2
    //静态：7+非静态2
    //静态：8+非静态2

}

```


## 2、使用：


### 2.1、每次请求都会传age过来，如果这个Action是个单例的话，后面请求的age，就把前面的给覆盖了，所以必须设置成prototype 



```
Class TestAction{  
  
    private int age;  
  
}  
```

### 2.2、由于它没有实例变量，所以不存在冲突的问题，用默认的单例就可以


```java
class TestService{  
  
    @Autowired  
    private IUserDAO dao;  
  
}  

```

### 2.3、总结:


1.对于有实例变量的类，要设置成prototype；没有实例变量的类，就用默认的singleton <br/>

2.Action一般我们都会设置成prototype，而Service只用singleton就可以。 


## 2、static


### 2.1、接口

```java

public interface StaticService {

     String addNostaticN();

     String addStaticN() ;
}




```


### 2.2、实现


```java

@Service
@Slf4j //下面这些变量与scope的状态有关，主要是看它有没有成员变量 ，默认是single
public class StaticServiceImpl implements StaticService {

    public static int static_n = 1 ;
    public  int no_static_n  = 1 ;

    public String addNostaticN(){
        ++no_static_n ;
        return "非静态"+no_static_n;
    }

    public String addStaticN(){
        ++static_n ;
        return "静态"+static_n;
    }

}
```

### 2.3、测试



```java
@RestController
@RequestMapping("static")
public class StaticController {
    @Resource
    private    StaticService staticService ;

    //http://localhost:8080/static/addNostaticN
    @GetMapping("addNostaticN")
    @ResponseBody
    public String addNostaticN(){
        return staticService.addNostaticN();
    }
    //非静态2
    //非静态3
    //非静态4
    //非静态5
    //非静态6
    //非静态7
    //非静态8

    //http://localhost:8080/static/addNostaticN
    @GetMapping("addStaticN")
    @ResponseBody
    public String addStaticN(){
        return staticService.addStaticN();
    }
    //静态2
    //静态3
    //静态4
    //静态5
    //静态6
    //静态7
    //静态8
}


```



![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)





<!-- Gitalk 评论 start  -->

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
		id: 'Gu2jE5dXxFZg7N01',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

