---
title: 重试注解Retryable
date: 2022-04-12 00:00:00
tags: 
- SpringBoot
category: 
- SpringBoot
description: 重试注解Retryable
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、 `@Retryable`

> 在实际开发中，我们经常需要处理一些可能会失败的操作，比如网络请求、数据库操作等。为了提高系统的健壮性和用户体验，通常会对这些可能失败的操作进行重试。`Spring Retry` 就是这样一个帮助我们实现自动重试机制的工具。



## 1、`POM`

> 确保你的项目还包含了Spring AOP依赖，因为Spring Retry基于AOP实现。

```xml
<!--retry-->
<dependency>
  <groupId>org.springframework.retry</groupId>
  <artifactId>spring-retry</artifactId>
  <version>1.3.2
  </version>
</dependency>

```

## 2、**启用 `Retry` 功能**

> 在你的Spring Boot应用启动类上添加`@EnableRetry`注解以启用重试功能：

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class HljClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(HljClientApplication.class, args);
    }
}
```



## 3、实现重试逻辑

#### **1） 定义服务接口和实现**

> 创建一个服务接口和它的实现，在实现中使用`@Retryable`注解来指定哪些异常情况下需要重试，以及重试策略。

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
public class RetryServiceImpl implements RetryService {

    private static final Logger log = LoggerFactory.getLogger(RetryServiceImpl.class);
    private static int num = 0;

    @Override
    @Retryable(value = Exception.class,
               maxAttempts = 4,
               backoff = @Backoff(delay = 2000, multiplier = 1.5))
    public boolean doRetry(String name) throws Exception {
        log.info("[RetryService#doRetry] name:{}", name);
        num++;
        if (num == 5) {
            return true;
        }
        throw new Exception("模拟异常");
    }

    @Recover
    public boolean doRetryRecover(Exception e, String name) {
        log.info("[RetryService#doRetryRecover] name:{}, e:{}", name, e.getMessage());
        return true;
    }
}
```

### 2） 控制器调用服务方法

> 在控制器中调用这个服务方法，并通过 `@ResponseBody` 返回结果给客户端。

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hlj")
public class RetryController {

    private static final Logger log = LoggerFactory.getLogger(RetryController.class);

    private final RetryService retryService;

    public RetryController(RetryService retryService) {
        this.retryService = retryService;
    }

    @GetMapping(value = "retry/doRetry", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseBean doRetry(String name) {
        boolean flag = false;
        try {
            flag = retryService.doRetry(name);
        } catch (Exception e) {
            log.error("[RetryController#doRetry] error: {}", e.getMessage());
        }
        return ResponseBean.buildSuccess(flag);
    }
}
```





## 4、参数详解

### 1）`@Retryable`

| 参数          | 默认值     | 描述                         |
| :------------ | :--------- | :--------------------------- |
| `value`       | -          | 指定抛出哪些异常时会触发重试 |
| `include`     | -          | 和`value`作用相同，默认为空  |
| `exclude`     | -          | 排除某些异常不进行重试       |
| `maxAttempts` | 3          | 最大重试次数                 |
| `backoff`     | `@Backoff` | 重试等待策略                 |



### 2）`@Backoff`

| 参数                   | 说明                                                         | 默认值  |
| ---------------------- | ------------------------------------------------------------ | ------- |
| `delay`                | 重试之间的等待时间(以毫秒为单位)                             | `1000L` |
| `maxDelay`             | 重试之间的最大等待时间(以毫秒为单位)                         |         |
| `multiplier`           | 指定延迟的倍数，默认为 `0`，表示固定暂停1秒后进行重试，如果把`multiplier`设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。 | `0`     |
| `delayExpression`      | 重试之间的等待时间表达式                                     |         |
| `maxDelayExpression`   | 重试之间的最大等待时间表达式                                 |         |
| `multiplierExpression` | 指定延迟的倍数表达式                                         |         |
| `random`               | 随机重试等待时间，默认为false                                |         |



### 3）`@Recover`

> 用于定义“兜底方法”（fallback method）。当被 `@Retryable` 注解的方法在达到最大重试次数后仍然失败（即始终抛出指定异常），`Spring Retry` 框架会自动调用同一个类中带有 `@Recover` 注解的对应方法，作为最终处理逻辑。

#### a、作用&说明

> **只有当重试完全失败（即所有重试都抛出了匹配的异常）时，才会触发 `@Recover` 方法。如果某次重试成功了，就不会调用 `@Recover`。**

- 记录失败日志；

- 返回默认值或友好提示；

- 触发告警或补偿流程；

- 避免异常继续向上抛出导致服务中断。



#### a、`@Recover` **方法参数规则**

1. **第一个参数必须是异常类型**，且必须与 `@Retryable` 中声明的异常类型 **兼容**（可以是相同类型、父类或实现类）。
   - 例如：`@Retryable(value = IOException.class)`，则 `@Recover` 方法第一个参数可以是 `IOException` 或其父类 `Exception`。
   - 如果写成 `RuntimeException`，而实际抛出的是 `IOException`，则 **不会匹配**，`@Recover` 不会被调用！
2. **后续参数必须与 `@Retryable` 方法的参数类型和顺序一致**（但可以省略部分参数）。
   - `Spring Retry` 会按位置匹配参数，只要前 `N` 个参数类型匹配即可。
   - 你可以只保留你需要的参数。



**正确示例：**

```java
@Retryable(IllegalArgumentException.class)
public String process(String name, int age, String email) { ... }

// 完整参数
@Recover
public String processRecover(IllegalArgumentException ex, String name, int age, String email) { ... }

// 只保留部分参数（常用）
@Recover
public String processRecover(IllegalArgumentException ex, String name) { ... }

// 甚至只保留异常（最少要求）
@Recover
public String processRecover(IllegalArgumentException ex) { ... }
```



**非法示例：**

```java
// 参数顺序错误
@Recover
public String processRecover(IllegalArgumentException ex, int age, String name) { ... } // ❌

// 异常类型不匹配
@Recover
public String processRecover(NullPointerException ex, String name) { ... } // ❌
```



## 5、注意事项

### 1）是否必须写  `@Recover` 方法

- **不是必须的**！：如果你不提供 `@Recover` 方法，当重试次数用尽后，最后一次抛出的异常会 **原样向上抛出**，由调用方处理（比如 `Controller` 捕获并返回错误响应）。
- **何时需要写 `@Recover`？**
  - 你想避免异常传播（比如返回默认值）；
  - 你想记录详细的失败上下文（如用户 `ID`、请求参数）；
  - 你想触发补偿机制（如发送消息到 `MQ`、更新状态为“失败”等）。



### 2）方法返回值必须与 `@Retryable` **方法一致**



> 如果返回类型不一致，`Spring` 无法确定哪个 `@Recover` 方法对应哪个 `@Retryable` 方法，会导致 **`@Recover` 不被调用**，最终异常直接抛出。

`Spring Retry` 在内部通过方法签名匹配 `@Recover` 方法。它要求：

- 返回类型相同（或可兼容，如子类）；
- 方法名可以不同，但参数列表需满足匹配规则（见下一点）。



### 3）`@Retryable`  不能和调用方同一类中

> 因为 `Spring Retry` 是基于 **`Spring AOP` 代理机制** 实现的。`AOP` 代理只对 **从外部调用该 `Bean` 的方法** 生效。如果你在同一个类中通过 `this.doRetry()` 调用带 `@Retryable` 的方法，**不会走代理**，也就不会触发重试逻辑，自然 `@Recover` 也不会生效。

- 将 `@Retryable` 方法放在一个独立的 Service Bean 中；
- 由 `Controller` 或其他 Bean 通过依赖注入调用它。



### 4）使用建议

| 建议                                          | 说明                         |
| --------------------------------------------- | ---------------------------- |
| 将 `@Retryable` 方法放在独立的 Service 类中   | 避免同类调用导致 AOP 失效    |
| `@Recover` 方法与 `@Retryable` 方法同属一个类 | Spring Retry 要求如此        |
| `@Recover` 方法返回类型与原方法一致           | 否则无法匹配                 |
| 第一个参数为兼容的异常类型，后续参数按需保留  | 灵活但要保证类型和顺序正确   |
| 不强制写 `@Recover`，按业务需求决定           | 无 `@Recover` 则异常最终抛出 |



# 二、`Failsafe`

> **`Failsafe`** 是一个轻量级、无外部依赖的 Java 容错库，用于构建高可用、弹性、自愈的系统。它通过组合多种 **容错策略（Policies）** 来保护可能失败的操作（如网络调用、数据库访问、AI 推理等）。

| 功能             | 说明                                               |
| ---------------- | -------------------------------------------------- |
| `Retry`          | 自动重试失败的操作（支持指数退避、固定间隔等策略） |
| `CircuitBreaker` | 熔断器：当失败率过高时自动“熔断”，防止雪崩         |
| `Timeout`        | 设置执行超时，避免长时间阻塞                       |
| `Bulkhead`       | 限制并发资源使用（类似舱壁隔离）                   |
| `Fallback`       | 提供降级逻辑（如返回默认值或缓存数据）             |



## 1、**核心特性**

- 支持 **同步 / 异步 / CompletableFuture** 执行
- 策略可 **任意组合**（如重试 + 熔断 + 超时）
- 零依赖（仅需 JDK 8+）
- 高性能、线程安全
- 提供监听器（Listeners）用于监控和日志

```xml
<dependency>
  <groupId>dev.failsafe</groupId>
  <artifactId>failsafe</artifactId>
  <version>3.3.0</version>
</dependency>
```



## 2、**核心组件详解**

### 1）**`Retry`（重试策略）**

| 方法                                  | 说明                   |
| ------------------------------------- | ---------------------- |
| `maxAttempts(n)`                      | 最大尝试次数（含首次） |
| `delay(d)`                            | 固定延迟               |
| `backoff(initial, max)`               | 指数退避（推荐）       |
| `retryOn(Class<? extends Throwable>)` | 指定重试的异常类型     |
| `abortOn(...)`                        | 遇到某些异常立即终止   |

```java
RetryPolicy<String> retryPolicy = RetryPolicy.<String>builder()
    .maxAttempts(3)
    .delay(Duration.ofMillis(500))        // 初始延迟
    .maxDelay(Duration.ofSeconds(2))      // 最大延迟
    .jitter(0.2)                          // 随机抖动（防惊群）
    .retryOn(Exception.class)             // 默认重试所有异常
    .retryIf((result, exc) -> result == null || exc != null)
    .build();
```



### 2）**`CircuitBreaker`（熔断器）**

> 防止系统雪崩：当失败率过高时，自动“熔断”，拒绝请求一段时间。

**熔断器三种状态：**

- **`Closed`（关闭）**：正常放行请求。
- **`Open`（打开）**：直接拒绝请求，快速失败。
- **`Half-Open`（半开）**：允许少量请求试探，成功则关闭，失败则继续打开。

```java
CircuitBreaker<String> cb = CircuitBreaker.<String>builder()
    .handle(IOException.class)                  // 只对 IO 异常计数
    .withFailureThreshold(3, 5)                 // 5 次中有 3 次失败就熔断
    .withFailureThreshold(50, PERCENTAGE)       // 或：失败率 ≥50%
    .withDelay(Duration.ofSeconds(30))          // 熔断后 30 秒进入半开状态
    .build();
```



### 3）**`Timeout`（超时控制）**

> 防止长时间阻塞。`Timeout` 默认使用 **独立线程中断**（`interrupt`），适用于可中断操作（如 `Thread.sleep()`、`Future.get()`）。对于不可中断的阻塞（如某些网络库），建议配合异步 + `CompletableFuture` 使用。

```java
Timeout<String> timeout = Timeout.of(Duration.ofSeconds(2));
```



### 4）`Bulkhead`（舱壁隔离）

限制并发资源，防止单个服务耗尽线程池。

```java
// 信号量模式（默认）
Bulkhead<String> bulkhead = Bulkhead.of(5); // 最多 5 个并发

// 或线程池模式（需额外依赖 failsafe-bulkhead）
```



### 5）`Fallback`（降级）

当所有策略都失败后，提供兜底逻辑。

```java
Fallback<String> fallback = Fallback.of("默认响应");
// 或
Fallback<String> fallback = Fallback.of(ctx -> {
    System.err.println("最终失败: " + ctx.getLastException());
    return "降级结果";
});
```



## 3、**组合策略：`Failsafe.with()`**

> `failsafe` 的强大之处在于 **策略可任意嵌套组合**，执行顺序为 **从右到左**（最右边最先执行）：

实际执行流程：`bulkhead → timeout → circuitBreaker → retryPolicy → fallback`

```java
FailsafeExecutor<String> executor = Failsafe.with(
    fallback,
    retryPolicy,
    circuitBreaker,
    timeout,
    bulkhead
);
```





## 4、**执行方式**

### **1）同步执行（get）**

```java
String result = executor.get(() -> callExternalService());
```



### 2）异步执行（`runAsync` / `getAsync`）

```java
CompletableFuture<String> future = executor.getAsync(() -> callExternalService());

future.thenAccept(System.out::println)
      .exceptionally(ex -> {
          ex.printStackTrace();
          return null;
      });
```





## 5、**监听器（`Listeners`）—— 监控与日志**

```java
retryPolicy.onRetry(e -> {
    System.out.println("第 " + e.getAttemptCount() + " 次重试，原因: " + e.getLastFailure());
});

circuitBreaker.onStateChange(e -> {
    System.out.println("熔断器状态变更: " + e.getState());
});
```

常用事件：

- `onRetry`
- `onAbort`
- `onSuccess`
- `onFailure`
- `onStateChange`（熔断器）





## 6、**最佳实践**

| 场景           | 建议                                     |
| -------------- | ---------------------------------------- |
| 对外部服务调用 | 必加 `Timeout + Retry + CircuitBreaker`  |
| 关键业务       | 添加 `Fallback` 返回缓存或默认值         |
| 高并发系统     | 使用 `Bulkhead` 防止资源耗尽             |
| 生产环境       | 注册监听器记录日志/指标（如 Micrometer） |
| 避免过度重试   | 对幂等操作才重试；非幂等操作慎用         |











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



