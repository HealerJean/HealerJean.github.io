---
title: 改变SpringBoot默认的HttpMessageCoverter
date: 2019-04-25 03:33:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: 改变SpringBoot默认的HttpMessageCoverter
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    



正常情况下我们SpringBoot为我们提供了HttpMessageCorverter用于前后台数据的转化，经常我们会发现返回到前台的对象，字段是NULL，这些NULL其实本不应该返回给前端显示，无疑是增加了前端小哥哥，小姐姐的负担    

当然，你能想到的是使用@JsonInclude @JsonJgnore来解决，但是这里呢，使用的是另一个中解析的类   


### 1、fastjson依赖

```xml
    <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.15</version>
    </dependency>
```

### 2、config

```java
package com.hlj.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

import java.nio.charset.Charset;

@Configuration
public class HttpMessageConverterConfig {

    //引入Fastjson解析json，不使用默认的jackson
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        //1、定义一个convert转换消息的对象
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        //2、添加fastjson的配置信息
        FastJsonConfig fastJsonConfig = new FastJsonConfig();

        SerializerFeature[] serializerFeatures = new SerializerFeature[]{
                //    输出key是包含双引号
                //SerializerFeature.QuoteFieldNames,
                //    是否输出值为null的字段,默认为false。
               // SerializerFeature.WriteMapNullValue,
                //    数值字段如果为null，则输出为0
               // SerializerFeature.WriteNullNumberAsZero,
                //     List字段如果为null,输出为[],而非null
               // SerializerFeature.WriteNullListAsEmpty,
                //    字符类型字段如果为null,输出为"",而非null
               // SerializerFeature.WriteNullStringAsEmpty,
                //    Boolean字段如果为null,输出为false,而非null
               // SerializerFeature.WriteNullBooleanAsFalse,
                //    Date的日期转换器
               // SerializerFeature.WriteDateUseDateFormat,
                //    循环引用
                SerializerFeature.DisableCircularReferenceDetect,
        };

        fastJsonConfig.setSerializerFeatures(serializerFeatures);
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));

        //3、在convert中添加配置信息
        fastConverter.setFastJsonConfig(fastJsonConfig);

        //4、将convert添加到converters中
        HttpMessageConverter<?> converter = fastConverter;

        return new HttpMessageConverters(converter);
    }
}
```



### 3、实体对象

```java
@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
public class DemoEntity {

	private String name;

	private String tmail;

	private Long volumn ;

}

```



### 4、测试

```java
  @GetMapping("get")
    @ResponseBody
    public ResponseBean get(String params){
        try {
            return ResponseBean.buildSuccess(new DemoEntity());
        } catch (AppException e) {
            log.error(e.getMessage(),e);
            return ResponseBean.buildFailure(e.getCode(),e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return ResponseBean.buildFailure(e.getMessage());
        }
    }
```



#### 4.1，如果没有config的情况下

```json

{
 "success": true,
 "result": {
     "name": null,
     "tmail": null,
     "volumn": null
     },
 "message": "",
 "code": "200",
 "date": "1556183337784"
}
```



#### 4.2、如果使用了上面的，是很干净的

```json
{
  "code": "200",
  "date": "1556183419997",
  "message": "",
  "result": {},
  "success": true
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
		id: 'rOCW5f8qSPoZUaib',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

