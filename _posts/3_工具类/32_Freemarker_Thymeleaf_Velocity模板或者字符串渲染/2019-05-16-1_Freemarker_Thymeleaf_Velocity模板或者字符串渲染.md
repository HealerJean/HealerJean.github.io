---
title: Freemarker_Thymeleaf_Velocity模板或者字符串渲染
date: 2019-05-16 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: Freemarker_Thymeleaf_Velocity模板或者字符串渲染
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)       





## 1、`Freemarker` 解析模板

```xml
   <!--freemarker 模板-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
```



### 1.1、ftl模板

```xml
<?xml version="1.0" encoding="utf-8"?>
<root id="" comment="">
    <person>
        <person>${x.person}</person>
        <age>${x.age}</age>
        <name>${name}</name>
        <sex>${x.sex}</sex>
    </person>
</root>  
```

#### 1.1.2、制作map

##### 1.1.2.1、DTO

```java
@Data
public class XmlEntry {
	
	public String person ;
	public Integer age;
	public Integer sex;
	public String name ;
	public double salary; 
	public Integer classPersonNum;

	
}
```


##### 1.1.2.2、DTO


```java
	private static Map demo(){  
				XmlEntry p =new XmlEntry();  
				p.setAge(24);
				p.setPerson("张宇晋"); 
				p.setSex(1);
				Map map = new HashMap();  
				map.put("name", "zhangyujin");
				map.put("x", p);  
			return map;  
		}
```



##### 1.2.3、根据不同模板目录生成文本

 

```java

/**
设置FreeMarker的模版文件位置

1、类路径，当前类下面的文件
cfg.setClassForTemplateLoading(CreateXmlByVmFile.class,"");
	或者
TemplateLoader templateLoaderClass = new ClassTemplateLoader(CreateXmlByVmFile.class,"");
cfg.setTemplateLoader(templateLoaderClass);

2、文件系统 ,文件目录
cfg.setDirectoryForTemplateLoading(fileDirectory);
	或者
TemplateLoader templateLoaderFile=new FileTemplateLoader(new File("E://hlj/xml/"));
cfg.setTemplateLoader(templateLoaderFile);

3、Servlet Context。
setServletContextForTemplateLoading(Object servletContext, String path);

4、resource目录，观察下面的工具类

*/

public  static String createXmlFile(String TemplateName, Map dataMap) throws TemplateException, IOException     {


		  //得FreeMarker配置对象
		  // 创建Configuration对象
		   Configuration cfg = new Configuration();
		   cfg.setEncoding(Locale.CHINA, "UTF-8");

		   File file = new File(CreateXmlByVmFile.class.getResource("").getFile());
		   cfg.setDirectoryForTemplateLoading(file);
		   Template template = cfg.getTemplate(TemplateName,"utf-8");

		   StringWriter w =new StringWriter();    		      
		           // 生成xml  
		   template.process(dataMap, w);    
		   return w.toString();
		   }
```

### 1.2、Freemarker模板字符串解析工具

```java
package com.fintech.scf.utils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.Map;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName FreeMarkerUtil
 * @date 2019/5/10  17:44.
 * @Description  freemarker模板 工具类
 */
@Slf4j
public class FreeMarkerUtil {

    /**
     * 模板目录 /resource/ftl/……
     * @param templateName 模板名字 example.ftl -> example
     * @return
     */
    public static String ftlTemplate(String templateName, Map params){
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setClassLoaderForTemplateLoading(FreeMarkerUtil.class.getClassLoader(),"/ftl/");
        try {
            Template   template = configuration.getTemplate(templateName+".ftl","UTF-8");
            if(template==null){
                log.info("=========邮件模板为空=========");
                throw new RuntimeException("邮件模板为空！");
            }
            return FreeMarkerTemplateUtils.processTemplateIntoString(template,params);
        } catch (IOException e) {
            throw new  RuntimeException(e.getMessage(),e);
        } catch (TemplateException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }


    /**
     * 字符串模板解析
     * @param html  字符串的HTML模板 举例 ： <h3>你好，${name}, 这是一封模板邮件! ，我叫HealerJean</h3>
     * @param params
     * @return
     */
    public static String stringTemplate( String html, Map params){

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_27);
        String tempName = "templateName" ;
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader() ;
        stringTemplateLoader.putTemplate(tempName,html);

        configuration.setTemplateLoader(stringTemplateLoader);
        configuration.setDefaultEncoding("UTF-8");

        try {
            Template   template = configuration.getTemplate(tempName,"UTF-8");
            if(template==null){
                log.info("=========邮件模板为空=========");
                throw new RuntimeException("邮件模板为空！");
            }
            return FreeMarkerTemplateUtils.processTemplateIntoString(template,params);
        } catch (IOException e) {
            throw new  RuntimeException(e.getMessage(),e);
        } catch (TemplateException e) {
            throw new RuntimeException(e.getMessage(),e);
        }
    }
}


```





## 3、`Thymeleaf` 模板或字符串解析工具



```java
package com.fintech.scf.utils;

import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName ThymeLeafUtil
 * @date 2019/5/16  12:00.
 * @Description
 */
@Slf4j
public class ThymeLeafUtil {


    /**
     * 模板目录 /resource/template/……
     * @param templateName 模板名字 比如 example.html -> examle
     * @param params
     * @return
     */
    public static String htmlTemplate(String templateName, Map params){

        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        //模板所在目录,默认resource
        resolver.setPrefix("/thymeleaf/");
        //模板文件后缀
        resolver.setSuffix(".html");
        resolver.setCharacterEncoding("utf-8");

        TemplateEngine engine=new TemplateEngine();
        engine.setTemplateResolver(resolver);

        Context context = new Context();
        context.setVariables(params);

        return    engine.process(templateName,context) ;

    }

    /**
     * @param html 字符串的HTML模板Content 举例 ：<p th:text='${name}'></p>
     * @param params map
     */
    public static String stringTemplate(String html, Map params) {

        SpringTemplateEngine engine = new SpringTemplateEngine();

        StringTemplateResolver resolver = new StringTemplateResolver();
        engine.setTemplateResolver(resolver);

        Context context = new Context();
        context.setVariables(params);

        return   engine.process(html,context);
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("debit.loan_contract_no","HealeJean");
        String   content = ThymeLeafUtil.htmlTemplate("sendMailTest",map);
        System.out.println(content);
    }



}

```



### 3.1、问题

#### 3.1.1、关于字符串解析可能会根据不同的springboot版本有一些问题

**我的springboot版本是1.5.默认提供的thymeleaf版本是1.5**

```xml
<!--thymeleaf 模板-->
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

##### 方案1 

字符串解析是使用的thymeleaf3中的，所以我们要对它进行强制升级，在`父工程`中配置下面的属性

```xml
	<!-- 属性配置 -->
	<properties>
		<thymeleaf.version>3.0.2.RELEASE</thymeleaf.version>
		<thymeleaf-layout-dialect.version>2.1.1</thymeleaf-layout-dialect.version>
	</properties>
```

##### 方案二

在依赖工程中配置下面的属性，并且本工程使用了上面的依赖，则下面的不会生效，因为maven的就近原则，除非单独使用，     

比如admin工程 中调用了common工程中的类`ThymeLeafUtil`而admin工程使用的thymeleaf就是默认提供的，但是common中为了使用thymeleaf又必须引入依赖，所以直接引入了下面的。专业就造成了admin工程可以调用该工具类，但是maven在选择版本的时候，采用就近原则，讲admin中的依赖使用了，所以不会成功。但是在common中直接运行该工具是没有问题的

```xml
 <dependency>
        <groupId>org.thymeleaf</groupId>
        <artifactId>thymeleaf</artifactId>
        <version>3.0.1.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.thymeleaf</groupId>
        <artifactId>thymeleaf-spring4</artifactId>
        <version>3.0.1.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>nz.net.ultraq.thymeleaf</groupId>
        <artifactId>thymeleaf-layout-dialect</artifactId>
        <version>2.0.3</version>
    </dependency>
```



#### 3.1.2、thymeleaf调用java方法

```java

  public static String test(String name) {
        return "thymeleaf调用java方法成功!"+name;
    }

    public static long getTime() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("ThymeLeafUtil", new ThymeLeafUtil());
        map.put("name","HealerJean") ;
        System.out.println(    stringTemplate("<h3   > </h3>\n" +
                "<p th:text=\"${ThymeLeafUtil.test(name)}\"/>",map));
    }



<h3   > </h3>
<p>thymeleaf调用java方法成功!HealerJean</p>


```



## 4、 `Velocity` 模板或者字符串解析

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-velocity</artifactId>
</dependency>
或者
<!--velocity 模板解析 -->
<dependency>
    <groupId>org.apache.velocity</groupId>
    <artifactId>velocity</artifactId>
    <version>1.7</version>
</dependency>
```



```java
package com.fintech.scf.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName VelocityUtil
 * @date 2019/5/16  15:19.
 * @Description Velocity 工具类
 */
public class VelocityUtil {

    /**
     * 根据vm模板获取内容 目录 resurce/vm/……
     * @param templateName 模板名字 example.vm -> example
     * @param params
     * @return
     */
    public static String vmTemplate(String templateName, Map params) {
        // 初始化模板引擎
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        engine.init();
        //模板所在目录
        String VM_PATH = "vm/"+templateName+".vm";

        // 获取模板文件
        Template template = engine.getTemplate(VM_PATH,"utf-8");

        // 设置变量，velocityContext是一个类似map的结构
        VelocityContext context = new VelocityContext(params);

        // 输出渲染后的结果
        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        System.out.println(sw.toString());
        return   sw.toString() ;
    }



    /**
     * 根据字符串获取 模板内容
     * @param content
     * @param params
     * @return
     */
    public static String stringTemplate(String content, Map params) {

        // 初始化并取得Velocity引擎
        VelocityEngine engine = new VelocityEngine();
        engine.init();

        // 取得velocity的上下文context
        VelocityContext context = new VelocityContext(params);

        StringWriter sw = new StringWriter();
        // 转换输出
        engine.evaluate(context, sw, "", content); // 关键方法
        return sw.toString() ;
    }
}

```

​          

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
		id: 'PeugZVpx3b7CBwDd',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

