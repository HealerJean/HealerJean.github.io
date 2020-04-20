---
title: 一个http请求过来SpringMVC是如何处理的
date: 2019-03-06 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: 一个http请求过来SpringMVC是如何处理的
---
#### 

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)             




用了很久spirngboot ，spirngMVc那套还真有点忘记

## 1、先看下SpringMvc的配置吧

#### 1.1、web.xml配置


```java

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:web="http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
  <display-name>StudySpringMvcOne</display-name>
  
  <!-- springmvc前端控制器 -->
  <servlet>
  	<servlet-name>springmvc</servlet-name>
  	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  	<!-- contextConfigLocation配置springmvc加载的配置文件（配置处理器映射器、适配器等等）
  	如果不配置contextConfigLocation，默认加载的是/WEB-INF/servlet名称-serlvet.xml（springmvc-servlet.xml）
  	 -->
  	<init-param>
  		<param-name>contextConfigLocation</param-name>
  		<param-value>classpath:springmvc-servlet.xml</param-value>
  	</init-param>
  </servlet>
  
  <servlet-mapping>
  	<servlet-name>springmvc</servlet-name>
  	<!-- 
  	第一种：*.action，访问以.action结尾 由DispatcherServlet进行解析,决定了请求URL必须是一个带后缀的URL
  	第二种：/，所有访问的地址都由DispatcherServlet进行解析，对于静态文件的解析需要配置不让DispatcherServlet进行解析，例如<mvc:default-servlet-handler />
  	使用此种方式可以实现 RESTful风格的url
  	第三种：/*，这样配置不对，使用这种配置，最终要转发到一个jsp页面时，
  	仍然会由DispatcherServlet解析jsp地址，不能根据jspUrl找到handler，会报错。
  	
  	 -->
  	<url-pattern>/</url-pattern>
  	
  	
  </servlet-mapping>
    <welcome-file-list>
    <welcome-file>index.html</welcome-file>
    <welcome-file>index.htm</welcome-file>
    <welcome-file>index.jsp</welcome-file>
    <welcome-file>default.html</welcome-file>
    <welcome-file>default.htm</welcome-file>
    <welcome-file>default.jsp</welcome-file>
  </welcome-file-list>
</web-app>


```



### 1.2、`springmvc-servlet.xml`


```xml


<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"  
 xmlns:context="http://www.springframework.org/schema/context"  
 xmlns:p="http://www.springframework.org/schema/p"  
 xmlns:mvc="http://www.springframework.org/schema/mvc"  
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"  
 xsi:schemaLocation="http://www.springframework.org/schema/beans  
      http://www.springframework.org/schema/beans/spring-beans-3.0.xsd  
      http://www.springframework.org/schema/context  
      http://www.springframework.org/schema/context/spring-context.xsd  
      http://www.springframework.org/schema/mvc  
      http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">
              
<!-- 如果不想在xml文件中配置bean，我们可以给我们的类加上spring组件注解， 只需再配置下spring的扫描器就可以实现bean的自动载入。    
			 -->
 <context:component-scan base-package="com.hlj.control"/>
 


			<!--  针对于web中的 / 
			在springMVC-servlet.xml中配置<mvc:default-servlet-handler />后，
			会在Spring MVC上下文中定义一个org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler，
			它会像一个检查员，对进入DispatcherServlet的URL进行筛查，如果发现是静态资源的请求，
			就将该请求转由Web应用服务器默认的Servlet处理，如果不是静态资源的请求，才由DispatcherServlet继续处理。
			一般Web应用服务器默认的Servlet名称是"default"，因此DefaultServletHttpRequestHandler可以找到它。
			如果你所有的Web应用服务器的默认Servlet名称不是"default"，则需要通过default-servlet-name属性显示指定：
				<mvc:default-servlet-handler default-servlet-name="所使用的Web服务器默认使用的Servlet名称" />
			-->
 <mvc:default-servlet-handler/>
 
 
			<!--   开启注解支持
			使用了<mvc:annotation-driven />， 它会自动注册DefaultAnnotationHandlerMapping 
			与AnnotationMethodHandlerAdapter 这两个bean,所以就没有机会再给它注入interceptors属性，就无法指定拦截器。			
			当然我们可以通过人工配置上面的两个Bean，不使用 <mvc:annotation-driven />，就可以 给interceptors属性 注入拦截器了。
			  -->
<mvc:annotation-driven/>
    
   <!--  InternalResourceViewResolver视图解析器 -->
    <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <!-- 前缀 -->
        <property name="prefix" value="/WEB-INF/jsp/"></property>
    <!-- 后缀 -->
        <property name="suffix" value=".jsp"></property>
    </bean>


```

## 2、具体工作流程


```



前端控制器（DispatcherServlet）：接收请求，响应结果，相当于电脑的CPU。

处理器映射器（HandlerMapping）：根据URL去查找处理器

处理器（Handler）：（需要程序员去写代码处理逻辑的）

处理器适配器（HandlerAdapter）：会把处理器包装成适配器，这样就可以支持多种类型的处理器，类比笔记本的适配器（适配器模式的应用）

视图解析器（ViewResovler）：进行视图解析，多返回的字符串，进行处理，可以解析成对应的页面

```



#### 第一步:用户发起请求到前端控制器（DispatcherServlet）     

#### 第二步：前端控制器请求处理器映射器（HandlerMappering）去查找处理器（Handle）(也就是Controller)：通过xml配置或者注解进行查找，找到以后处理器映射器（HandlerMappering）像前端控制器返回执行链（HandlerExecutionChain）     

#### 第三步：前端控制器（DispatcherServlet）选择一个合适的处理器适配器（HandlerAdapter）去执行处理器（Handler） 


```java

执行的过程中，Spring还帮我们做一些额外的事情

HttpMessageConveter： 将请求消息（如Json、xml等数据）转换成一个对象，将对象转换为指定的响应信息

数据转换：对请求消息进行数据转换。如String转换成Integer、Double等

数据验证： 验证数据的有效性（长度、格式等），验证结果存储到BindingResult或Error中

```


#### 第四步：Handler执行完给处理器适配器返回ModelAndView （handle会对这里面的数据进行）    

#### 第五步：处理器适配器向前端控制器返回ModelAndView     

#### 第六步：前端控制器请求视图解析器（ViewResolver）去进行视图解析，视图解析器像前端控制器返回View，前端控制器对视图进行渲染，将结果返回给客户端



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
		id: 'RWAyg2VmfQhMjXHT',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

