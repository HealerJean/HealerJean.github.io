---
title: SpringAi_2_Advisor
date: 2025-11-14 00:00:00
tags: 
- AI
category: 
- AI
description: SpringAi_2_Advisor
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、`Advisors API`

## 1、核心概念

> Advisors 基于**责任链模式**设计。当用户发起一个对话请求时，请求会依次穿过链上的每一个 Advisor，每个 Advisor 都有机会修改请求内容或响应结果。         
>
> 设计思想类似 Spring AOP 切面、Web Filter，专为大模型对话交互场景优化，允许在 **ChatClient 调用 LLM 前后** 插入自定义逻辑，实现请求修改、响应增强、日志监控、安全过滤、RAG、对话记忆等通用能力的解耦与复用   



### 1）执行顺序

每个 Advisor 都有一个 `order` 属性（数值越小，优先级越高）。

- **请求阶段**：按 order 从小到大执行（1 -> 2 -> 3）。
- **响应阶段**：按 order 从大到小返回（3 -> 2 -> 1）。



### 2）核心包与接口

#### a、**两大核心子接口**（区分同步 / 流式）：

```java
// 1. 同步/非流式 Advisor（对应call()调用）
public interface CallAdvisor extends Advisor {
    ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain);
}

// 2. 异步/流式 Advisor（对应stream()调用）
public interface StreamAdvisor extends Advisor {
    Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain);
}
```

#### b、**责任链接口**：

```java
// 同步链：传递请求并执行下一个Advisor
public interface CallAdvisorChain extends AdvisorChain {
    ChatClientResponse nextCall(ChatClientRequest request);
    List<CallAdvisor> getCallAdvisors();
}

// 流式链：传递请求并执行下一个Advisor
public interface StreamAdvisorChain extends AdvisorChain {
    Flux<ChatClientResponse> nextStream(ChatClientRequest request);
    List<StreamAdvisor> getStreamAdvisors();
}
```

### c、**数据载体**：

- `ChatClientRequest`：封装待发送给 LLM 的完整请求（Prompt、参数、上下文）
- `ChatClientResponse`：封装 `LLM` 返回的完整响应（结果、上下文）

### 3）使用

- **写好 `Advisor` 类**

- **在 `@Configuration` 里注册成 `@Bean`**

- **在 `ChatClient.builder()` 里加入 `.defaultAdvisors(loggingAdvisor)`**



## 2、内置的常用 Advisors

> `Spring AI` 提供了许多开箱即用的 Advisor，你可以通过 `ChatClient.builder().defaultAdvisors(...)` 进行全局配置。

- **做聊天机器人**：必选 `MessageChatMemoryAdvisor`。
- **做知识库问答**：必选 `QuestionAnswerAdvisor`。
- **上线生产环境**：务必加上 `SafeGuardAdvisor` 或类似的过滤逻辑。
- **开发调试**：临时加上 `SimpleLoggerAdvisor`，它能帮你省去很多猜谜的时间。



### 1）聊天记忆 (Chat Memory) 类

#### a、**`MessageChatMemoryAdvisor`（推荐）**

- **结构化记忆**：将检索到的对话历史作为独立的 `Message` 对象集合，直接插入到 `Prompt` 中。
- **优点**：保持了对话的结构（谁说了什么），适合大多数遵循标准 Chat 格式的模型。
- **注意**：并非所有模型都支持这种结构化的历史输入。



#### b、**`PromptChatMemoryAdvisor`**

- **文本化记忆**：将记忆检索出来，拼接成纯文本字符串，放入 `Promp`t 的系统提示词（System Text）中。
- **优点**：兼容性极强，几乎所有模型都能处理文本。
- **缺点**：丢失了严格的对话结构，模型需要更强的理解能力来区分角色。



#### c、**`VectorStoreChatMemoryAdvisor`**	

- **向量检索记忆**：从向量数据库（VectorStore）中检索相关信息，将其添加到 Prompt 的系统文本中。
- **适用**：处理超长对话历史或海量知识库检索，避免上下文过长导致 Token 超出限制。



### 2）问答与检索 (QA & Retrieval) 类

#### a、`QuestionAnswerAdvisor`

- **朴素 RAG**：实现了最基础的 RAG 流程。接收用户问题，从向量库检索，将检索结果注入 Prompt。
- **工作流程**：拿到用户问题 -> 去向量数据库检索相似片段 -> 把片段拼接到提示词里 -> 发送给模型。
- **适用场景**：简单的知识库问答，，不需要复杂的检索逻辑，即开即用。比如“根据上传的文档回答...”。

#### b、`RetrievalAugmentationAdvisor`

- **模块化 RAG**：基于 `org.springframework.ai.rag` 包构建，实现了符合“模块化 RAG 架构”的通用检索增强生成流程。
- **适用**：需要更灵活、可定制的 RAG 流程（如预检索、重排序等高级功能）的复杂应用。



### 3）推理与内容安全类

#### a、`SafeGuardAdvisor`：它是你的“安检员”。

- **功能**：可以在请求发送前检查用户输入（比如拦截骂人的话），也可以在响应返回后检查 AI 的输出（防止 AI 乱说话）。
- 原理：通过让模型“重读”输入来提升其推理能力。
- 适用：生产环境必备，作为最后一道防线过滤输出，确保应用合规。

#### b、`ReReadingAdvisor`：推理增强

- 功能：推理增强，

- **原理**：通过让模型“重读”输入来提升其推理能力。

- **适用**：处理复杂的逻辑推理任务，提升回答的准确性。

  

## 3、实战案例

### 1）日志监控 Advisor（同步 + 流式）

#### a、`LoggingAdvisor`

```java
package com.healerjean.proj.strata.infra.chat.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.core.Ordered;
import reactor.core.publisher.Flux;

/**
 * ：日志监控 Advisor（同步 + 流式）：同时实现CallAdvisor+StreamAdvisor，支持双模式
 */
public class LoggingAdvisor implements CallAdvisor, StreamAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvisor.class);

    @Override
    public String getName() {
        return "LoggingAdvisor";
    }

    /**
     * 低优先级，最后处理请求、最先处理响应
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

    /**
     * 同步拦截实现
     */
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        long start = System.currentTimeMillis();
        logger.info("[同步请求] {}", request.prompt().getUserMessage().getText());

        // 执行下一个Advisor/LLM调用
        ChatClientResponse response = chain.nextCall(request);

        long cost = System.currentTimeMillis() - start;
        logger.info("[同步响应] {} | 耗时:{}ms", response.chatResponse().getResults().get(0), cost);
        return response;
    }

    // 流式拦截实现
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        long start = System.currentTimeMillis();
        logger.info("[流式请求] {}", request.prompt().getUserMessage().getText());

        return chain.nextStream(request)
                .doOnNext(response -> {
                    String content = response.chatResponse().getResults().get(0).getOutput().getText();
                    logger.info("[流式响应片段] {}", content);
                })
                .doFinally(signalType -> {
                    long cost = System.currentTimeMillis() - start;
                    logger.info("[流式结束] 耗时:{}ms", cost);
                });
    }
}
```



#### b、`ChatClient` 注入 `Advisor`

```java

// 2. 构建 ChatClient 并自动注入 Advisor
@Bean
public ChatClient chatClient(ChatModel chatModel, LoggingAdvisor loggingAdvisor) {
    return ChatClient.builder(chatModel)
            .defaultAdvisors(loggingAdvisor) 
            .build();
}
```





# 二、递归顾问

> 如果说普通的 Advisor 是“拦截器”，那么递归 Advisor 就是 AI 的**“自我反思与进化机制”**。

## 1、什么是递归顾问？

> 递归顾问是一种特殊类型的顾问，可以多次循环执行下游顾问链。当您需要重复调用 `LLM` 直到满足某个条件时，此模式非常有用，

普通 `Advisor`：执行一次 → 结束

递归 `Advisor`：**自己调用自己，循环执行 N 次，直到满足终止条件**

两大内置递归 `Advisor`：

1. `ToolCallAdvisor`：递归执行工具调用循环（自动调用函数，直到不用工具为止）
2. **`StructuredOutputValidationAdvisor`**：递归执行**格式校验循环**（JSON 不对就重试，直到对为止）



## 2、`ToolCallAdvisor`（递归工具调用）

### 1）核心原理

一句话总结：不让模型自己执行工具，而是交给 `Advisor` 递归循环执行：   

调用 → 解析工具 → 执行工具 → 把结果丢回模型 → 直到不需要工具 → 返回最终答案



### 2）核心能力

1. **递归循环**：自动多轮调用 `LLM`，直到没有工具需要执行
2. **关闭模型原生工具**：`setInternalToolExecutionEnabled(false)`，完全接管工具流程
3. **直接返回 returnDirect=true**：工具结果**不回传给大模型**，直接返回给用户（超级省 Token）
   1. **正常流程（默认）**用户提问 → AI → 调用工具 → 把结果**还给 AI** → AI 整理语言 → 返回给你
   2. **`returnDirect=true` 流程**：用户提问 → AI → 调用工具 → **直接返回给你，跳过 AI**
   3. 试用场景：
      - **你需要工具结果原封不动返回**（如时间、验证码、文件链接）
      - **省 Token**（少一轮 AI 对话）
      - **低延迟**（不需要 AI 再处理）
      - **返回结构化数据**（JSON、XML 不希望 AI 修改）
4. **子责任链递归**：`callAdvisorChain.copy(this)` 创建子链，不污染主链
5. **全链路可观测**：所有工具调用都能被其他 Advisor 监听、日志、拦截
6. **空结果安全处理**：防止响应为空导致崩溃



### 3）执行流程

```
用户提问
    ↓
ToolCallAdvisor 拦截
    ↓
调用 LLM → 看是否需要工具
    ↓
【需要工具？】
    ├─── 是 → 执行工具 → 把结果放回 prompt → 【递归调用自己】
    └─── 否 → 结束循环，返回答案
```



### 4）实战案例

场景：**“先查北京天气，再查上海天气，然后一起告诉我”**

```
【第1次】进入 ToolCallAdvisor
AI：我要查北京天气
执行工具 → 北京：20度

--- 递归开始 ---
【第2次】进入 ToolCallAdvisor（自己调用自己）
AI：我要查上海天气
执行工具 → 上海：25度

--- 递归开始 ---
【第3次】进入 ToolCallAdvisor（自己调用自己）
AI：不用工具了，我回答你
北京20度，上海25度
--- 递归结束 ---
```



#### a、写一个工具

```java
@Component
public class WeatherTool {

    @Tool("获取城市天气")
    public String getWeather(String city) {
        return city + "：20℃，晴天";
    }
}
```



#### a、注册 ToolCallAdvisor

```java
@Bean
public ToolCallAdvisor toolCallAdvisor(ToolCallingManager manager) {
    return ToolCallAdvisor.builder()
            .toolCallingManager(manager)
            .build();
}

@Bean
public ChatClient chatClient(ChatModel model, ToolCallAdvisor advisor) {
    return ChatClient.builder(model)
            .defaultAdvisors(advisor) // 加入自动循环
            .build();
}
```



#### c、调用（全自动）

```java
@Resource
  private WeatherTool weatherTools;


  /**
   * 递归顾问 ToolCallAdvisor：获取北京、上海天气
   */
  @GetMapping("/chat/weather")
  public String weather(@RequestParam(value = "message",
                                      defaultValue = "先查北京天气，再查上海天气，然后一起告诉我") String msg) {
      return chatClient.prompt()
              .tools(weatherTools)
              .user(msg)
              .call()
              .content();
  }
```



#### d、运行结果

```
北京：20℃，晴天  
上海：10℃，晴天
```



## 3、`StructuredOutputValidationAdvisor`

> `StructuredOutputValidationAdvisor` 根据生成的 JSON Schema 验证结构化 JSON 输出，并在验证失败时重试调用，最多重试指定次数。

### 1）核心原理

一句话总结：**让模型“返工”直到输出格式 100% 符合你的 Java 类定义。**

原理流程：用户提问 → 模型生成内容 → Advisor 检查格式 

- → **格式对了？** → 返回给用户
- → **不对！** → 告诉模型“你错了，重写” → 模型再生成 → 循环检查



### 2）核心能力

1. **Schema 强校验**：基于 Java 类生成 JSON Schema，强制模型遵守字段名、字段类型。
2. **自动重试机制**：如果模型第一次输出格式不对（比如少字段、类型错），Advisor 会自动让它重写，直到格式正确为止。
3. **防御性编程**：防止下游解析 JSON 时抛出 `JsonParseException`，把运行时错误消灭在框架层。
4. **精准提示**：当校验失败时，会向模型注入具体的错误信息（例如：“需要字符串，你给了数字”），引导模型修正。



### 3）执行流程

```
用户提问（要求结构化输出）
    ↓
StructuredOutputValidationAdvisor 拦截
    ↓
【第1次】调用 LLM → 生成 JSON
    ↓
【校验格式？】
    ├─── 成功 → 返回 Java 对象
    └─── 失败 → 构造错误提示：“格式错误，必须包含 id 和 name”
               ↓
               【第2次】调用 LLM → 模型修正 → 再次校验
               ↓
               （循环直到成功或达到最大重试次数）
```



### 4）实战案例

**场景**：**“帮我生成一个用户注册信息，包含 id（数字）和 name（字符串）”**

#### a、定义结构化对象（DTO）

首先，你需要一个 Java 类来定义你想要的数据结构：

```java
/**
 * 用户信息 DTO
 * 注意：必须有无参构造函数，字段要有 getter/setter
 */
public class UserInfo {
    private Long id;
    private String name;

    // 必须有无参构造函数
    public UserInfo() {}

    // Getter 和 Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return "UserInfo{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
```



#### b、注册 `StructuredOutputValidationAdvisor`

你需要在配置类中手动构建这个 Advisor，并将其加入 `ChatClient`。

```java
@Configuration
public class ChatConfig {

    @Bean
    public ChatClient chatClient(ChatModel model) {
        
        // 1. 创建 StructuredOutputValidationAdvisor 实例
        // 参数：最大重试次数（防止死循环）
        StructuredOutputValidationAdvisor validationAdvisor =   new StructuredOutputValidationAdvisor(3); 

        return ChatClient.builder(model)
                // 2. 加入校验顾问
                .defaultAdvisors(validationAdvisor) 
                .build();
    }
}
```



#### c、调用（强制结构化）

在调用时，你需要告诉 `ChatClient` 你期望的返回类型是 `UserInfo`。

```java
/**
 * 结构化输出校验顾问
 * 测试：让模型输出不符合 UserInfo 格式的内容，看它是否会自动修正
 */
@GetMapping("/chat/structured")
public UserInfo structured(@RequestParam(value = "message", 
                                        defaultValue = "随便聊点啥") String msg) {

    return chatClient.prompt()
            // 关键点：这里指定了 .advisors() 或者在默认链中包含了 StructuredOutputValidationAdvisor
            // 并且指定了返回类型为 UserInfo.class
            .user(msg)
            .call()
            // 告诉框架：我需要 UserInfo 类型的结果
            .entity(UserInfo.class); 
}
```



#### d、运行结果与分析

**模拟场景**：

- **用户输入**：`给我生成一个用户，名字叫张三，ID是100。`
- **模型第一次瞎写（被拦截）**：`{"name": "张三", "age": 25}` （缺少 id 字段，多了 age 字段）
- **`Advisor` 动作**：拦截！提示模型：`Output did not match the schema. Required fields: id.`
- **模型第二次修正（通过）**：`{"id": 100, "name": "张三"}`

**最终返回**：

```json
{
  "id": 100,
  "name": "张三"
}
```



### 5）注意事项（避坑指南）

1. **必须有无参构造函数**：`JSON` 反序列化需要。
2. **`Getter/Setter`**：Lombok 的 `@Data` 是最好的朋友。
3. **配合 Prompt 使用**：虽然有 Advisor 拦截，但在 Prompt 里写清楚要求（如“请严格按照 JSON 格式输出”）能显著减少重试次数，节省 `Token`。
4. **不是万能的**：如果模型能力极差，可能在几次重试后依然无法生成合法 JSON，最终会抛出异常。
5. **与 `ToolCall` 的区别**：
   - `ToolCallAdvisor` 是**做事情**（查天气、搜网页）。
   - `StructuredOutputValidationAdvisor` 是**管格式**（确保是 JSON，字段对不对）。



## 4、手写递归顾问

### 1）场景

#### a、**场景 1：输出格式不对 → 自动重试（`StructuredOutputValidationAdvisor`）**

**你自己写**：

- AI 返回 JSON 格式错
- 自动重试
- 直到正确

**用途**：API 接口必须返回标准格式。



#### b、**场景 2：内容不安全 → 重写回答（递归改写）**

**你自己写**：

- AI 回答敏感
- 自动让 AI 重写
- 最多重试 2 次

**用途**：内容安全、合规。



#### c、**场景 3：多轮思考（`Self-Consistency`）→ 让 AI 思考 3 次选最优**

**你自己写**：

- 让 AI 回答 3 次
- 递归调用 3 次
- 投票选出最好答案

**用途**：数学题、逻辑题、推理题。



#### d、**场景 4：答案太短 / 太模糊 → 自动让 AI 补充详细**

**你自己写**：

- 检测答案长度 < 50 字
- 自动让 AI 详细展开
- 递归重试

**用途**：客服、教育、详细解答。



#### e、场景 5：工具调用失败 → 自动重试工具

**你自己写**：

- 工具超时、报错
- 自动重试 2 次
- 递归重新调用

**用途**：支付、查询、接口不稳定。



#### f、场景 6：自主智能体 Agent（自动思考、自动计划、自动执行）

**你自己写**：

- AI 思考 “我需要做什么”
- 调用工具
- 递归循环
- 直到任务完成

**用途**：私人助理、自动化工作流、企业 Agent。



### 2）案例：AI 回答太短，自动让它重新详细回答

```java
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.prompt.Prompt;
import org.springframework.core.Ordered;

public class AutoDetailRecursiveAdvisor implements CallAdvisor {

    private final int maxRetries = 2;
    private int retryCount = 0;

    @Override
    public int getOrder() { return Ordered.HIGHEST_PRECEDENCE + 600; }

    @Override
    public String getName() { return "auto-detail-advisor"; }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        ChatClientResponse response = chain.nextCall(request);

        // 获取AI回答
        String answer = response.chatResponse().getResults().get(0).getOutput().getText();

        // 如果太短 且 没超过重试次数 → 递归重试
        if (answer.length() < 50 && retryCount < maxRetries) {
            retryCount++;
            System.out.println("【递归】回答太短，让AI重新详细回答！次数：" + retryCount);

            // 构建新提示
            String newPrompt = request.prompt().getUserMessage().getText()
                    + "\n请详细回答，至少100字。";

            ChatClientRequest newRequest = request.mutate()
                    .prompt(new Prompt(newPrompt))
                    .build();

            // 递归！
            return chain.copy(this).nextCall(newRequest);
        }

        return response;
    }
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
		id: 'LIr4hiey7tuTWKBqsdCx',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



