---
title: Listenter用法
date: 2019-01-23 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: Listenter用法
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)            



## 1、listener监听

### 1、ServletRequest监听

`ServletRequestListener`和`ServletRequestAttributeListener`。

```
ServletRequestListener可见监听Request的创建和销毁；

ServletRequestAttributeListener可以对Request的属性进行监听。
```



### 2、HttpSession监听

`HttpSessionListener`和`HttpSessionAttributeListener`。


```
HttpSessionListener可以监听HttpSession的创建跟销毁，
HttpSessionAttributeListener则是对session中属性的监听，它可以监听到session新增属性、移除属性和属性值被替换时。

```


### 3、ServletContext监听

`ServletContextListener`和`ServletContextAttributeListener`。


```
ServletContextListener可以监听到ServletContext的创建和销毁，
ServletContextAttributeListener可以监听到ServletContext中属性的新增、移除和属性值的替换。

```

## 2、启用上面Listener监听


使用 @ServletComponentScan注解后，Servlet、Filter、Listener 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。


```java
/**
 * 使用 @ServletComponentScan注解后，Servlet、Filter、Listener
 * 可以直接通过 @WebServlet、@WebFilter、@WebListener 注解自动注册，无需其他代码。
 **/
@ServletComponentScan
@SpringBootApplication
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }
}


```

## 3、ServletContext监听

### 3.1、ServletContextListener

#### 3.1.1、contextInitialized

当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，<br/>


#### 作用：做一些初始化的内容添加工作、设置一些基本的内容、比如一些参数或者是一些固定的对象等等。

1、ServletContext 对象是一个为整个 web 应用提供共享的内存，任何请求都可以访问里面的内容
2、如何实现在服务启动的时候就动态的加入到里面的内容：我们需要做的有：  
 1 ） 实现 servletContextListerner 接口 并将要共享的通过 setAttribute （ name,data ）方法提交到内存中去   ；
 2 ）应用项目通过 getAttribute(name) 将数据取到 。



```java

@Override
public void contextInitialized(ServletContextEvent sce) {
   System.out.println("ServletContext initialized");
}
     

```


#### 3.1.2、contextDestroyed

当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器（比如直接停止虚拟机运行）

```java
@Override
public void contextDestroyed(ServletContextEvent sce) {
   System.out.println("ServletContext destroyed");
}
   
```

### 3.2、ServletContextAttributeListener

直接看也应该很好懂

```java

@Override
public void attributeAdded(ServletContextAttributeEvent event) {
   System.out.println("ServletContext attribute added");
}

@Override
public void attributeRemoved(ServletContextAttributeEvent event) {
   System.out.println("ServletContext attribute removed");
}

@Override
public void attributeReplaced(ServletContextAttributeEvent event) {
   System.out
}
```

### 3.3、测试

#### 3.3.1、创建一个监听


```java

package com.hlj.moudle.servletcontext;

/**
 * 作者 ：HealerJean
 * 日期 ：2019/2/20  下午1:07.
 * 类描述：
 */
import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * ServletContext监听器和ServletContext属性监听器
 *
 */
@WebListener
public class ContextListener implements ServletContextAttributeListener, ServletContextListener {

    /**
     * 当Servlet 容器终止Web 应用时调用该方法。在调用该方法之前，容器会先销毁所有的Servlet 和Filter 过滤器。
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("ServletContext destroyed");
    }

    /**
     * 当Servlet 容器启动Web 应用时调用该方法。在调用完该方法之后，容器再对Filter 初始化，
     * 并且对那些在Web 应用启动时就需要被初始化的Servlet 进行初始化。
     *
     * 作用：做一些初始化的内容添加工作、设置一些基本的内容、比如一些参数或者是一些固定的对象等等。
     *
     * 1、ServletContext 对象是一个为整个 web 应用提供共享的内存，任何请求都可以访问里面的内容
     * 2、如何实现在服务启动的时候就动态的加入到里面的内容：我们需要做的有：  
     *  1 ） 实现 servletContextListerner 接口 并将要共享的通过 setAttribute （ name,data ）方法提交到内存中去   ；
     *  2 ）应用项目通过 getAttribute(name) 将数据取到 。
     *
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext sct=sce.getServletContext();
        Map<Integer,String> depts=new HashMap<Integer,String>();
        sct.setAttribute("dept", depts);

        System.out.println("ServletContext initialized");
    }



    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        System.out.println("ServletContext attribute added");
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        System.out.println("ServletContext attribute removed");
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        System.out.println("ServletContext attribute replaced");
    }

}

```


#### 3.3.2、启动服务器

我们可以看到这里打印了两次attributeAdded，因为一开始启动服务器，就会在ServletContext 添加一些默认值，所以会先进入它<br/>

然后进入contextInitialized，的时候，代码中有添加的操作，所以会再次进入


```java
2019-02-20 13:36:45.333 [localhost-startStop-1] INFO  o.a.c.c.C.[Tomcat].[localhost].[/] - Initializing Spring embedded WebApplicationContext
ServletContext attribute added
ServletContext attribute added
ServletContext initialized
2019-02-20 13:37:28.898 [main] INFO  s.d.s.w.PropertySourcedRequestMappingHandlerMapping - Mapped URL path [/demo/swagger] onto method [public org.springframework.http.ResponseEntity<springfox.documentation.spring.web.json.Json> springfox.documentation.swagger2.web.Swagger2Controller.getDocumentation(java.lang.String,javax.servlet.http.HttpServletRequest)]
2019-02-20 13:37:29.483 [main] INFO  s.d.s.w.p.DocumentationPluginsBootstrapper - Context refreshed
2019-02-20 13:37:29.502 [main] INFO  s.d.s.w.p.DocumentationPluginsBootstrapper - Found 1 custom documentation plugin(s)

```
#### 3.3.3、关闭服务器


```java
2019-02-20 13:37:29.734 [main] INFO  com.hlj.AdminApplication - Started AdminApplication in 46.565 seconds (JVM running for 47.508)
Disconnected from the target VM, address: '127.0.0.1:56548', transport: 'socket'
ServletContext destroyed

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
		id: 'M3QvzwXe429luoTn',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

