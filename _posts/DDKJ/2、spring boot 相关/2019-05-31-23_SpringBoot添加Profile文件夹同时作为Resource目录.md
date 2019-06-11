---
title: SpringBoot添加Profile文件夹同时作为Resource目录
date: 2019-02-20 03:33:00
tags: 
- SpringBoot
category: 
- Spring
description: SpringBoot添加Profile文件夹同时作为Resource目录
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    





![1559295547379](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1559295547379.png)



### 1、maven



#### 1.1、激活测试目录local

```xml
      <properties>
                <profiles.active>src/profiles/local</profiles.active>
            </properties>
```

```xml
<profiles>
        <profile>
            <!-- 本地开发环境 -->
            <id>local</id>
            <properties>
                <profiles.active>src/profiles/local</profiles.active>
            </properties>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
        <profile>
            <!-- 开发环境 -->
            <id>dev</id>
            <properties>
                <profiles.active>src/profiles/dev</profiles.active>
            </properties>
            <activation>
                <property>
                    <name>dev</name>
                    <value>true</value>
                </property>
            </activation>
        </profile>
        <profile>
            <!-- 生产环境 -->
            <id>product</id>
            <properties>
                <profiles.active>src/profiles/product</profiles.active>
            </properties>
            <activation>
                <property>
                    <name>product</name>
                    <value>true</value>
                </property>
            </activation>
        </profile>
    </profiles>



```

#### 1.2、添加profiles作为resource目录

```xml
<build>
    <!-- 定义资源目录 -->
    <resources>
        <resource>
            <directory>src/main/resources</directory>
        </resource>
        <resource>
            <directory>${profiles.active}</directory>
        </resource>
        <resource>
            <directory>src/main/java</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
            <filtering>false</filtering>
        </resource>
    </resources>
```






### 2、我们可以选中本地

![1559295712114](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1559295712114.png)



### 3、使用Properties测试



#### 3.1、resource目录下创建`resource.properties`



```

## properties 取值 ##
profile.name=resource.name
profile.age=resource.age
```

#### 3.2、激活的profile目录下创建`profile.properties`

```

## properties 取值 ##
profile.name=profile.name
profile.age=profile.age
```



```java
/*
 * Copyright (C) 2018 dy_only, Inc. All Rights Reserved.
 */
package com.hlj.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	public static Properties properties = new Properties();

	public static String getProperty(String key) {
		return properties.getProperty(key) == null ? "" : properties.get(key).toString();
	}

	static {
			String profile = System.getProperty("spring.profiles.active");
		    System.out.println(profile);

			String[]  props = new String[] {"profile.properties", "resource.properties" };
			for(String prop:props){
				InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(prop);
				if (inputStream != null) {
					Properties propertiest = new Properties();
					try {
						propertiest.load(inputStream);
						properties.putAll(propertiest);

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	}
}

```



#### 3.3、测试

```java
    @ApiOperation(value = "获取properties",notes = "获取properties",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            response = ResponseBean.class)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "参数", required =false,paramType = "query", dataType = "string")
    })
    @GetMapping("get")
    @ResponseBody
    public ResponseBean get(String name){
        try {
            System.out.println(PropertiesUtil.getProperty(name));
            return ResponseBean.buildSuccess(PropertiesUtil.getProperty(name));
        } catch (AppException e) {
            log.error(e.getMessage(),e);
            return ResponseBean.buildFailure(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBean.buildFailure(e.getMessage());
        }
    }

```





<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'GOHkerUMPmahI76A',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

