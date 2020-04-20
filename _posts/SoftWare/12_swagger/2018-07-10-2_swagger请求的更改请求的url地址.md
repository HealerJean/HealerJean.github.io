---
title: Swagger请求的更改请求的url地址
date: 2018-07-10 03:33:00
tags: 
- Swagger
category: 
- Swagger
description: Swagger请求的更改请求的url地址
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           



## 1、引入swagger 相关jar包


```xml
<!--swagger 版本-->
		<swagger.version>2.7.0</swagger.version>


<!--swagger-->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>${swagger.version}</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>${swagger.version}</version>
</dependency>


```



## 2、选择一个swagger的目录下载

必须选择2.0以上,3.0以下版本，将其中的dist文件夹拷贝到自己项目中的resources/swagger目录下，如图

[https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui)

![WX20180717-182152](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180717-182152.png)

## 3、在resources下新建swagger.properties文件，其中的内容为


```yml
springfox.documentation.swagger.v2.path=/duodian/swagger

```

## 4、再dist目录下面的index.html中添加


```js

url = "http://petstore.swagger.io/v2/swagger.json"
修改为
url = url = "/duodian/swagger";
修改后如下


<script type="text/javascript">
  $(function () {
    var url = window.location.search.match(/url=([^&]+)/);
    if (url && url.length > 1) {
      url = decodeURIComponent(url[1]);
    } else {
      url = "/duodian/swagger";
    }

```

## 5、新建swag配置文件


```java
@EnableSwagger2
@Configuration
@PropertySource("classpath:swagger.properties") // 新增对swagger.properties 的引入
public class ApiConfig   extends WebMvcConfigurerAdapter{


    @Profile({"test","dev"})
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com"))
                .build()
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("多点优惠")
                .description("多点优惠开发文档")
                .version("1.0.0")
                .termsOfServiceUrl("http://test.dangqugame.cn/")
                .license("dangqugame")
                .licenseUrl("http://test.dangqugame.cn/")
                .build();
    }


}

```

## 6、添加资源的映射


```java
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter{

    /**
     *  swagger增加url映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/duodian/swagger/**").addResourceLocations("classpath:/swagger/dist/");
    }

}
```

## 7、访问成功

[http://localhost:8080/duodian/swagger/index.html](http://localhost:8080/duodian/swagger/index.html)<br/>

![WX20180717-182604@2x](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/WX20180717-182604@2x.png)



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
		id: 'xTxtlyrHkWOy0PDQ',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

