---
title: @Retryable重试注解
date: 2022-04-12 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: @Retryable重试注解
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、 `@Retryable`

## 1.1、`POM`

```xml
<!--retry-->
<dependency>
  <groupId>org.springframework.retry</groupId>
  <artifactId>spring-retry</artifactId>
  <version>1.3.2
  </version>
</dependency>

```

## 1.2、启动类添加注解`@EnableRetry`

```java
@EnableRetry
@SpringBootApplication
public class HljClientApplication {


    public static void main(String[] args) {
        SpringApplication.run(HljClientApplication.class, args);
    }

}

```



## 1.3、方法使用

### 1.3.2、`RetryService`

```java

/**
 * @author zhangyujin
 * @date 2022/4/12  15:09.
 * @description
 */
@Slf4j
@Service
public class RetryServiceImpl implements RetryService {

  public static int num = 0 ;


  @Retryable(value = Exception.class,
             maxAttempts = 4,
             backoff = @Backoff(delay = 2000,multiplier = 1.5))
  @Override
  public boolean doRetry(String name) {
    log.info("[RetryService#doRetry] name:{}", name);
    num++;
    if (num == 5){
      return true;
    }
    try {
      int i = 1/0;
    }catch (Exception e){
      log.info("[RetryService#doRetry] error:{} ", e.getMessage());
      throw e;
    }
    return true;
  }

  
  @Recover
  public boolean doRetryRecover(Exception e, String name) {
    log.info("[RetryService#doRetryRecover] name:{}, e:{}", name, e.getMessage());
    return true;
  }


}
```

### 1.3.2、`Controller`

```java


@Controller
@RequestMapping("hlj")
@Slf4j
public class ReteyController {

  @Resource
  private RetryService retryService;


  @GetMapping(value = "retry/doRetry", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ResponseBody
  public ResponseBean doRetry(String name) {
    boolean flag = false;
    try {
      flag = retryService.doRetry(name);
    }catch (Exception e){
      log.info("[ReteyController#doRetry] error");
    }

    return ResponseBean.buildSuccess(flag);
  }


}

```



## 1.4、参数详解

## 1.4.1、`@Retryable`

| 参数            | 默认值     | 说明                                                       |
| --------------- | ---------- | ---------------------------------------------------------- |
| `value`         |            | 抛出指定异常才会重试                                       |
| `include`：     |            | 和`value`一样，默认为空，当`exclude`也为空时，默认所有异常 |
| `exclude`：     |            | 指定不处理的异常                                           |
| `maxAttempts`： | 3          | 最大重试次数，默认3次                                      |
| `backoff`：     | `@Backoff` | 重试等待策略，默认使用`@Backoff`                           |

#### 1.4.1.1、`backoff`

> 重试等待策略，默认使用`@Backoff`



| 参数                   | 说明                                                         | 默认值  |
| ---------------------- | ------------------------------------------------------------ | ------- |
| `delay`                | 重试之间的等待时间(以毫秒为单位)                             | `1000L` |
| `maxDelay`             | 重试之间的最大等待时间(以毫秒为单位)                         |         |
| `multiplier`           | 指定延迟的倍数，默认为0，表示固定暂停1秒后进行重试，如果把`multiplier`设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。 | `0`     |
| `delayExpression`      | 重试之间的等待时间表达式                                     |         |
| `maxDelayExpression`   | 重试之间的最大等待时间表达式                                 |         |
| `multiplierExpression` | 指定延迟的倍数表达式                                         |         |
| `random`               | 随机重试等待时间，默认为false                                |         |



### 1.4.2、`@Recover`

> ⬤ 当重试到达指定次数时，被注解的方法将被回调，可以在该方法中进行日志处理。需要注意的是发生的异常和入参类型一致时才会回调，     
>
> ⬤ 如果不需要回调方法，可以直接不写回调方法，那么实现的效果是，重试次数完了后，如果还是没成功没符合业务判断，就抛出异常（正常可不就是么）。



#### 1.4.2.1、注意事项

1、方法的返回值必须与`@Retryable`方法一致，`@Retryable`和@`Recover`修饰的方法要在同一个类中         

2、方法的第一个参数要与`@Retryable`配置的异常一致，其他的参数，需要哪个参数，写进去就可以了（`@Recover`方法中有的）       

3、`@Retryable`注解是通过切面实现的，因此我们要避免`@Retryable` 注解的方法的调用方和被调用方处于同一个类中，因为这样会使`@Retryable` 注解失效









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
		id: 'hUBO6okA7RetV21r',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



