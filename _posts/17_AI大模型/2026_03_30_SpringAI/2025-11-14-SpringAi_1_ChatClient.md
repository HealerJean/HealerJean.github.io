---
title: SpringAi_1_ChatClient
date: 2025-11-14 00:00:00
tags: 
- AI
category: 
- AI
description: SpringAi_1_ChatClient
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、`AI` 概念

## 1、模型 

> `AI` 模型是旨在处理和生成信息的算法，通常模仿人类认知功能。通过从大型数据集中学习模式和见解，这些模型可以进行预测，生成文本、图像或其他输出，从而增强各行业的各种应用。

- **大语言模型 (`LLM`)**：处理语言/代码，支持多模态（图像、音频、视频）。
- **图像生成模型**：输入语言/图像，输出图像/视频（如 Stable Diffusion）。
- **语音模型**：处理文本与语音的转换（TTS/STT）。
- **嵌入模型 (`Embedding`)**：将文本/图像转换为向量（数字数组），用于计算语义相似度。





## 2、提示

提示（`Prompt`）是引导 `AI` 生成输出的关键。在 `Spring AI` 中，提示不仅仅是字符串，它通常包含不同角色的消息：

1. **系统角色 (`System`)**：定义模型的行为和交互上下文。
2. **用户角色 (`User`)**：用户的实际输入。



### 1）什么是“提示模板”？（拒绝硬编码）

想象一下，如果你每次让 AI 讲笑话，都要手写一整段话，比如“给我讲个关于猫的搞笑笑话”、“给我讲个关于狗的悲伤笑话”，这很麻烦且难以维护。

**提示模板**就是为了解决这个问题。它允许你定义一个**“填空题”**式的框架：

- **模板内容：** `Tell me a {adjective} joke about {content}.`
- **占位符：** `{adjective}` 和 `{content}` 就是变量。

在实际代码中，你只需要准备好数据（比如 `adjective="搞笑"`, `content="程序员"`），Spring AI 就会自动把这些值填进去，生成最终的指令发送给 AI。



### 2）为什么说它像 `Spring MVC` 的“视图”？

这是一个非常精彩的类比，特别是如果你熟悉 Java Web 开发的话：

- **在 Spring MVC 中：** 你有一个 HTML 模板（视图），然后传入一个 `Model`（数据，如用户信息），最后渲染出一个完整的网页给用户看。
- **在 Spring AI 中：** 你有一个**提示模板**，然后传入一个 `Map`（数据，如 `{adjective: "搞笑", content: "猫"}`），最后渲染出一个完整的**提示字符串**给 AI 看。



### 3）底层是怎么实现的？

- **技术选型：** `Spring AI` 底层使用了 **`StringTemplate`** 这个库来处理这些文本替换工作。
- **进化方向：** 文中最后提到，虽然最早 AI 只接受简单的字符串，但现在 Spring AI 的提示模板已经进化了。它不仅能生成简单的文本，还能构建包含**多条消息**的复杂结构（比如区分“系统指令”和“用户输入”），以适应现代大模型（Chat Model）的交互需求。

```java
// 1. 定义模板 (就像定义 View)
String template = "Tell me a {adjective} joke about {content}.";

// 2. 准备数据 (就像 Model)
Map<String, Object> model = new HashMap<>();
model.put("adjective", "funny"); // 搞笑的
model.put("content", "Spring AI"); // 关于 Spring AI

// 3. 渲染 (Render)
// Spring AI 使用 StringTemplate 把数据填进去
String finalPrompt = templateEngine.render(template, model);

// 4. 结果
// finalPrompt 变成了: "Tell me a funny joke about Spring AI."
// 然后这个字符串才会被发送给 AI 模型
```



## 3、嵌入

> 解释：**嵌入（Embedding）就是把万物（文字、图片、视频）变成一串数字（向量），让计算机能通过计算数学距离来判断它们的意思像不像。**



### 1）核心概念：万物皆数字（向量化）

- **原文**：“嵌入是文本、图像或视频的数值表示...转换为浮点数数组（称为向量）”
- **解释**：计算机不懂“苹果”这个词，它只懂数字。嵌入技术就是把“苹果”变成类似 `[0.8, 0.2, -0.5]` 这样的一串数字。这串数字就是它的**“数字身份证”**。



### 2）核心逻辑：距离即相似度

- **原文**：“通过计算两个文本的向量表示之间的数值距离...确定...相似性”
- **解释**：一旦变成了数字，就可以做数学题了。
  - 如果两串数字在数学上**距离很近**（比如坐标挨得很近），说明它们的意思**很像**。
  - 如果距离**很远**，说明它们**不相关**。



### 3）核心场景：语义空间（高维地图）

- **原文**：“类似于欧几里得几何的二维空间...但在更高的维度中”
- **解释**：想象一张巨大的地图。
  - 在普通地图上，北京和上海离得远，和天津离得近。
  - 在 `AI` 的**“语义地图”**（高维空间）里，**“意思相近”**的东西离得近。比如“猫”和“狗”虽然字不一样，但在这个地图里，它们的位置会比“猫”和“冰箱”靠得更近。



### 4）对 Java 开发者的意义

> 作为一名探索 AI 的 Java 开发人员，不必理解这些向量表示背后的复杂数学理论或具体实现。对它们在 AI 系统中的作用和功能有一个基本了解就足够了，尤其是在将 AI 功能集成到应用程序中时。

这就是 **RAG（检索增强生成）** 的基础：把你的知识库变成无数个这样的坐标点，当用户提问时，AI 就去这个地图里找离问题最近的那个点，把答案找出来。

1. **输入**：你给它一段文本（比如用户的搜索词）。
2. **输出**：它给你一串数字（向量）。
3. **使用**：你拿这串数字去数据库里找“长得像”的其他数字（即意思相近的内容）。



## 4、`token`

> `Token` 是 AI 模型工作方式的基本组成部分。在输入时，模型将单词转换为 token。在输出时，它们将 token 转换回单词。



### 1）它是 `AI` 的“消化单位” (技术视角)

AI 并不像人类一样直接阅读“单词”或“句子”。它只能处理数字序列。

- **原文**：“模型将单词转换为` token`... 一个 `token` 大致相当于一个单词的 75%”
- **通俗理解**：
  - 想象 AI 吃东西（文本），它不能一口吞下一整块牛排（一句话），它必须把肉切成小块（Token）才能消化。
  - **切分规则**：在英语中，一个 `Token` 大约等于 4-5 个字母，或者 0.75 个单词。
    - 比如单词 "`Spring`" 可能是一个 Token。
    - 比如单词 "`Artificial Intelligence`" 可能会被切分成 "Art", "ificial", " Int", "elligence" 几个 Token。
  - **中文语境**：虽然文档主要讲英语，但在中文里，通常**一个汉字**或者**一个常用词**（如“你好”）大约对应 1-2 个 Token。

### 2. 它是 `AI` 的“计价货币” (商业视角)

- **原文**：“Tokens = 金钱... 输入和输出都计入总 token 数量”
- **通俗理解**：
  - 使用 AI 模型（特别是商业模型如 GPT-4）就像**打车**或者**用水用电**。
  - **计费方式**：不是按“次”收费，而是按“量”收费。
  - **双向收费**：你发给 AI 的内容（提示词）要算钱（输入 `Token`），AI 回复你的内容也要算钱（输出 Token）。
  - **省钱技巧**：你的提示词越精简，或者你要求 `AI` 回答得越简短，花费的 Token 就越少，账单也就越便宜。



### 3. 它是 `AI` 的“短期记忆” (能力视角)

- **上下文窗口**就是 `AI` 的**“工作台大小”**或者**“瞬时记忆力”**。
- **限制**：`AI` 一次只能“看”这么多 `Token`。如果你的书有 100 万字，但模型的窗口只有 8K Token（约 6000 个单词），AI 就无法一次性读完这本书。
- **后果**：超出限制的内容，AI 会直接“遗忘”或截断，根本处理不了。
- **对比**：
  - **`GPT-3 (4K)`**：像是一个只能记几句话的小纸条。
  - **`Claude (100K+)`**：像是一本厚小说，可以一次性读完。



## 4、为 AI 模型注入私有数据与实时能力

### 1）理解 `AI` 的“知识盲区”

首先，我们需要明确一个基本事实：像 GPT-4 这样的预训练模型，其知识库是静态的。

- **知识截止**：例如，GPT-3.5/4.0 的训练数据仅更新到 2021 年 9 月。这意味着它无法回答关于此日期之后事件的任何问题。
- **缺乏私有数据**：模型在公开数据上训练，因此它对你公司的内部文档、客户数据或专有业务流程一无所知。

为了让 AI 为你所用，我们必须将“你的数据”和“你的 API”带到 AI 模型面前。主要有三种技术路径。



### 2）三种数据集成技术概览

| 技术     | 原理                                           | 优点                                             | 缺点                                                 | 适用场景                                         |
| -------- | ---------------------------------------------- | ------------------------------------------------ | ---------------------------------------------------- | ------------------------------------------------ |
| 微调     | 使用你的数据重新训练模型，调整其内部权重。     | 模型深度内化你的知识，回答风格更贴合。           | 成本极高，计算资源消耗大，过程复杂，部分模型不支持。 | 需要模型深度掌握某个垂直领域的语言风格或知识。   |
| 提示填充 | 将你的数据作为上下文，“塞进”提示词中发给模型。 | 实用、高效、成本低。可动态更新知识。             | 受模型上下文窗口（Token 限制）约束。                 | 让模型基于你的私有文档（如手册、报告）回答问题。 |
| 工具调用 | 允许模型调用你提供的外部 API 或服务。          | 赋予模型实时行动能力，可获取最新数据或执行操作。 | 需要你预先开发和注册工具（API）。                    | 让模型查询实时天气、股票价格或操作你的业务系统。 |



# 二、入门

## 1、模型 `ollama`

### 1）`maven`

#### a、父 `pom`

```xml
<!-- 引入 Spring AI BOM，统一管理所有 AI 相关依赖的版本 -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-bom</artifactId>
    <version>${spring-ai.version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```



#### b、子 `pom`

```xml
<!-- 引入 Ollama Starter（关键！） -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-ollama</artifactId>
</dependency>
```



### 2）`application.yml`

```yaml
spring:
  application:
    name: hlj-strata
  profiles:
    include: project-config
  ai:
    ollama:
      base-url: http://localhost:11434  # Ollama 服务地址（默认 11434）
      chat:
        model: qwen3:14b # 默认使用的模型（与 ollama pull 一致）
        options:
          temperature: 0.7  # 温度参数（0-1，值越高越随机，越低越精准）
          max-tokens: 1000  # 最大生成 token 数
```



## 2、聊天客户端 `API`

### 1）`ChatClientConfig`

```java
package com.healerjean.proj.strata.infra.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ChatClientConfig
 *
 * @author zhangyujin
 * @date 2025/11/18
 */

@Configuration
public class ChatClientConfig {

    /**
     * 配置 Ollama ChatClient Bean
     */
    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                // 全局系统提示（角色：专业Java技术助手）
                .defaultSystem("""
                        你是专业的Java后端技术助手，精通Spring Boot、Spring AI、Ollama技术栈。
                        回答要求：简洁、精准、可运行，提供完整代码案例，避免冗余解释。
                        """)
                // 全局默认参数（可覆盖）
                .defaultOptions(ollamaChatModel.getDefaultOptions())
                .build();
    }


}

```



### 2）`ChatController`

```java
package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Llama3Controller
 *
 * @author zhangyujin
 * @date 2025/11/18
 */

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Resource
    private ChatClient chatClient;


    // 1. 基础对话
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "msg", defaultValue = "你好，请介绍一下你自己") String msg) {
        return chatClient.prompt()
                .user(msg)
                .call()
                .content();
    }


}
```



### 3）请求、响应

```http
GET http://localhost:8888/ai/chat?msg=你谁啊


我是专业的Java后端技术助手，专注于Spring Boot、Spring AI和Ollama技术栈。我可以帮助你解决以下问题：
1. Spring Boot项目搭建与优化
2. AI集成（Spring AI + Ollama）
3. 微服务架构设计
4. 企业级应用开发

请告诉我你需要什么帮助？
```



# 三、`ChatClient` `API` 核心详解

## 1、核心概念

- **`ChatClient`**：统一聊天客户端，采用**构建者模式**，链式调用构建提示词（Prompt）。
- **`ChatModel`**：底层模型接口，`Ollama` 对应 `OllamaChatModel`，负责与 `Ollama` 服务通信Spring。
- **`Prompt`**：发送给 AI 的提示，包含 **系统消息（System）**、**用户消息（User）**、**模型参数（Options）**。
- **响应方式**：
  - `call()`：同步阻塞，等待完整响应返回。
  - `stream()`：流式响应，逐字返回（适合前端打字机效果）。

## 2、核心 `API` 方法

| 方法                                    | 作用                                  |
| --------------------------------------- | ------------------------------------- |
| `ChatClient.Builder#builder(ChatModel)` | 创建 ChatClient 构建器                |
| `defaultSystem(String/Resource)`        | 设置全局默认系统提示（角色设定）      |
| `defaultOptions(ChatOptions)`           | 设置全局默认模型参数                  |
| `prompt()`                              | 启动提示词构建（流畅 API 入口）       |
| `system(String)`                        | 设置当前请求的系统提示（覆盖全局）    |
| `user(String)`                          | 设置用户消息（核心提问）              |
| `user(Consumer<UserSpec>)`              | 动态提示模板（占位符传参）            |
| `options(ChatOptions)`                  | 当前请求的临时模型参数                |
| `call()`                                | 同步调用，返回响应解析器              |
| `stream()`                              | 流式调用，返回响应流解析器            |
| `content()`                             | 获取响应文本内容                      |
| `entity(Class<T>)`                      | 结构化输出（自动映射 POJO）           |
| `chatResponse()`                        | 获取完整响应（含 Token 消耗、元数据） |

### 1）`call()` 返回值

> 调用 `call()` 方法实际上不会触发 AI 模型执行。相反，它只指示 Spring AI 是否使用同步或流式调用。实际的 AI 模型调用发生在 `content()`、`chatResponse()` 和 `responseEntity()` 等方法被调用时。

在 `ChatClient` 上指定 `call()` 方法后，响应类型有几种不同的选项。

- `String content()`: 返回响应的字符串内容
- `ChatResponse chatResponse()`: 返回 `ChatResponse` 对象，该对象包含多个生成以及有关响应的元数据，例如用于创建响应的令牌数量。
- `ChatClientResponse chatClientResponse()`: 返回一个 `ChatClientResponse` 对象，该对象包含 `ChatResponse` 对象和 `ChatClient` 执行上下文，使您可以访问 `Advisor` 执行期间使用的额外数据（例如，`RAG` 流中检索到的相关文档）。
- `entity()` 返回 `Java` 类型
  - `entity(ParameterizedTypeReference<T> type)`: 用于返回实体类型的 `Collection`。
  - `entity(Class<T> type)`: 用于返回特定实体类型。
  - `entity(StructuredOutputConverter<T> structuredOutputConverter)`: 用于指定 `StructuredOutputConverter` 的实例，将 `String` 转换为实体类型。
- `responseEntity()` 返回 `ChatResponse` 和 `Java` 类型。当您需要在一次调用中同时访问完整的 AI 模型响应（带元数据和生成）和结构化输出实体时，这非常有用。
  - `responseEntity(Class<T> type)`: 用于返回包含完整 `ChatResponse` 对象和特定实体类型的 `ResponseEntity`。
  - `responseEntity(ParameterizedTypeReference<T> type)`: 用于返回包含完整 `ChatResponse` 对象和实体类型 `Collection` 的 `ResponseEntity`。
  - `responseEntity(StructuredOutputConverter<T> structuredOutputConverter)`: 用于返回包含完整 `ChatResponse` 对象和使用指定 `StructuredOutputConverter` 转换的实体的 `ResponseEntity`。

### 2）`stream()` 返回值

在 `ChatClient` 上指定 `stream()` 方法后，响应类型有几个选项。

- `Flux<String> content()`: 返回 AI 模型正在生成的字符串的 `Flux`。
- `Flux<ChatResponse> chatResponse()`: 返回 `ChatResponse` 对象的 `Flux`，该对象包含有关响应的其他元数据。
- `Flux<ChatClientResponse> chatClientResponse()`: 返回 `ChatClientResponse` 对象的 `Flux`，该对象包含 `ChatResponse` 对象和 `ChatClient` 执行上下文，使您可以访问 Advisor 执行期间使用的额外数据（例如，RAG 流中检索到的相关文档）。



## 3、实战案例

### 1）配置 `ChatClient Bean`

> 创建配置类，统一初始化 ChatClient，支持全局系统提示与参数：

```java
    /**
     * 配置 Ollama ChatClient Bean
     */
    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                // 全局系统提示（角色：专业Java技术助手）
                .defaultSystem("""
                    你是专业的Java后端技术助手，精通Spring Boot、Spring AI、Ollama技术栈。
                    回答要求：简洁、精准、可运行，提供完整代码案例，避免冗余解释。
                    """)
                // 全局默认参数（可覆盖）
                .defaultOptions(ollamaChatModel.getDefaultOptions())
                .build();
    }

```

### 2）基础同步聊天（最简单案例）

> 创建 Controller，实现基础问答接口：

#### a、基础同步聊天接口

```java
/**
 * 基础同步聊天接口
 */
@GetMapping("/sync")
public String syncChat(@RequestParam String message) {
    return chatClient.prompt()
            // 用户提问
            .user(message)
            // 同步调用
            .call()
            // 获取响应文本
            .content();
}
```



~~~http
GET http://localhost:8080/ai/chat/sync?message=你谁啊


我是专业的Java后端技术助手，专注Spring Boot/Spring AI/Ollama技术栈。

示例代码：Spring Boot Hello World
```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }
}
```

需要演示Spring AI或Ollama集成示例吗？
~~~



#### b、带角色设定的聊天（覆盖全局系统提示）

```java
/**
 * 带角色设定的聊天（覆盖全局系统提示）
 */
@GetMapping("/sync/role")
public String syncChatWithRole(@RequestParam String message) {
    return chatClient.prompt()
            // 临时角色
            .system("你是幽默的段子手，用搞笑风格回答问题")
            .user(message)
            .call()
            .content();
}
```

```http
GET http://localhost:8080/ai/chat/sync/role?message=你谁啊


*搓手手* 哎呀，我就是那个在AI界混得风生水起的段子手啊！不过别被我这副正经样儿骗了，其实我是个隐藏的搞笑高手，专治各种不开心！*眨眼* 你要不要听个笑话？保证比你上个月的工资还让人惊喜！
```



### 3）流式响应（打字机效果）

> 适合前端实时展示，返回 `Flux<String>`：

#### a、流式聊天接口（SSE 实时响应）

```java
/**
 * 流式聊天接口（SSE 实时响应）
 */
@GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE )
public Flux<String> streamChat(@RequestParam String message) {
    return chatClient.prompt()
            .user(message)
            .stream() // 流式调用
            .content(); // 逐字返回文本流
}

```



#### b、流式+自定义参数（临时调整温度、模型）

```java
/**
 * 流式+自定义参数（临时调整温度、模型）
 */
@GetMapping(value = "/stream/options", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
public Flux<String> streamChatWithOptions(
        @RequestParam String message,
        @RequestParam(defaultValue = "1.0") Double temperature) {
    return chatClient.prompt()
            .user(message)
            // 临时参数：温度+最大token
            .options(ChatOptions.builder()
                    .temperature(temperature)
                    .maxTokens(2000)
                    .build())
            .stream()
            .content();
}
```

### 4）动态提示模板（占位符传参）

> 使用 `{占位符}` 动态填充内容，适配复杂提示：

```java

/**
 * 动态提示模板（占位符传参）
 */
@GetMapping("/template")
public String templateChat(
        @RequestParam String topic,
        @RequestParam(defaultValue = "中文") String language) {
    // 提示模板（占位符：{language}、{topic}）
    String promptTemplate = """
                用{language}写一段关于{topic}的技术介绍，要求：
                1. 简洁明了，300字以内
                2. 包含核心特点和使用场景
                3. 提供1个可运行的小案例
                """;
    return chatClient.prompt()
            .user(user -> user
                    // 模板文本
                    .text(promptTemplate)
                    .param("language", language)
                    .param("topic", topic))
            .call()
            .content();
}
```

```http
http://localhost:8080/ai/chat/template?topic=Spring AI&language=中文
```



### 5）结构化输出（自动映射 POJO）

> 让 AI 返回 JSON，自动解析为 Java 对象（避免手动解析）：

```java
/**
 * 结构化数据
 */
@GetMapping("/structured")
public OrderVO structuredChat(@RequestParam String orderInfo) {
    return chatClient.prompt()
            .user(u -> u
                    // 修正点：先使用 String.format 格式化字符串，再传入 text()
                    .text(String.format("请分析 '%s' 并提取关键信息。", orderInfo))
            )
            .call()
            .entity(OrderVO.class);
}     
                  
public record TechInfo(
        String name,         // 技术名称
        String description,  // 简介
        Integer difficulty,  // 难度（1-5）
        String[] keyPoints   // 核心知识点
) {}
```

```http
http://localhost:8080/ai/chat/structured?techName=Spring AI

```



### 6）多轮对话（会话记忆）

> 通过存储会话历史，实现上下文连贯对话：

```java
package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConversationController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */
@RestController
@RequestMapping("/ai/chat")
public class ConversationController {

    @Resource
    private  ChatClient chatClient;
    
    /**
     * 会话存储（生产环境用 Redis）
     */
    private final Map<String, List<Message>> sessionStore = new ConcurrentHashMap<>();

    
    /**
     * 多轮对话接口
     */
    @GetMapping("/conversation")
    public String conversationChat(
            @RequestParam String sessionId,
            @RequestParam String message) {
        // 获取或创建会话历史
        List<Message> messages = sessionStore.getOrDefault(sessionId, new ArrayList<>());
        
        // 添加当前用户消息
        messages.add(new UserMessage(message));

        // 调用 AI（传入完整会话历史）
        String response = chatClient.prompt()
                .messages(messages)
                .call()
                .content();

        // 保存 AI 响应到会话历史
        messages.add(new AssistantMessage(response));
        sessionStore.put(sessionId, messages);

        return response;
    }
}

```



### 7）获取完整响应

```java
/**
 * 获取完整响应
 */
@GetMapping("/metadata")
public Map<String, Object> chatWithMetadata(@RequestParam String message) {
    // 1. 获取完整响应
    ChatResponse chatResponse = chatClient.prompt()
            .user(message)
            .call()
            .chatResponse();

    // 2. 提取元数据
    ChatResponseMetadata metadata = chatResponse.getMetadata();
    Usage usage = metadata.getUsage();

    // 4. 获取内容 (安全获取)
    // getResults()：返回一个列表，因为理论上 AI 可以一次生成多个备选答案（虽然通常我们只取第一个）。
    String content = "";
    if (!chatResponse.getResults().isEmpty()) {
        content = chatResponse.getResults().get(0).getOutput().getText();
    }

    // 5. 组装返回
    return Map.of(
            "content", content,            // AI 回答内容
            "model", metadata.getModel(),  // 使用的模型
            "id", metadata.getId(),        // 会话ID
            "totalTokens", usage.getTotalTokens(),    // 总Token
            "promptTokens", usage.getPromptTokens(),  // 提问Token
            "completionTokens", usage.getCompletionTokens() // 回答Token
    );
}
```







# 四、多模态

## 1、核心概念

### 1）多模态的核心定义

多模态指 AI 模型同时**理解和处理多种数据格式**的能力，包括**文本、图像、音频、视频、文档**等，最终生成**文本响应**（`Spring` `AI` 暂不支持 `AI` 生成非文本媒体输出）。

- 输入：多模态（文本 + 图像 / 音频等）；
- 输出：纯文本（需生成非文本媒体需使用单一模态专用模型）。

### 2）`Spring AI` 多模态的核心价值

- **统一抽象**：基于`Message`/`MediaAPI` 封装多模态能力，一套代码兼容所有支持的多模态模型；
- **极简集成**：通过`UserMessage`的`media`字段快速添加多模态内容，无需复杂的底层调用；
- **多模型兼容**：原生支持 OpenAI GPT-4o、Claude 3、Gemini 1.5、Ollama LLaVA 等主流多模态模型；
- **`Spring` 生态对齐**：媒体内容基于 `Spring` `Resource`抽象，支持本地文件、网络 URI、类路径资源等。



### 3）核心设计原则

- **仅用户消息支持多模态**：`UserMessage`是唯一可添加`media`的消息类型，`SystemMessage`/`AssistantMessage`/`ToolMessage`均 不支持；
- **媒体类型通过 `MimeType` 标识**：明确区分图像、音频、文档等模态类型，模型根据 MimeType 解析媒体内容；
- **媒体数据双格式支持**：`Media`的`data`字段可传入**Spring Resource**（本地 / 类路径文件）或**URI**（网络资源）；
- **输出仅文本**：所有多模态模型的响应均为纯文本，非文本媒体生成需使用专用模型（如图像生成用 Stable Diffusion）。



## 2、核心 API 深度解析

> `Spring AI` 多模态能力基于**消息体系**和**媒体体系**两大核心抽象构建，无新增顶层接口，完全复用原有 Prompt API，学习成本极低。

- 
- **`UserMessage` (用户消息)**：这是多模态输入的主要载体。
  - **`content` 字段**：用于存放文本指令（例如：“描述这张图片”）。
  - **`media` 字段**：这是一个集合，允许你添加图像、音频等资源。
- **`MimeType`**：这是关键，你必须告诉框架你传入的是什么类型的数据（如 `image/png`, `image/jpeg`）。



### 1）媒体核心类：`Media`

> `Media`是多模态的最小单元，封装了**模态类型、媒体数据、元数据**，是实现多模态输入的核心类，核心属性如下：

- `data` 字段仅支持**`Spring Resource`**（`ClassPathResource`/`FileSystemResource`/`UrlResource`）和**URI 字符串**；

- `name`字段需使用中性命名（如`image-1`），避免特殊字符，防止 AI 模型将其解析为提示词指令。

```java
public class Media {
    @Nullable
    private String id; // 媒体唯一标识，模型自动生成
    private final MimeType mimeType; // 模态类型（如IMAGE_PNG、AUDIO_MP3）
    private final Object data; // 媒体数据：Resource/URI
    private String name; // 媒体名称，建议中性命名避免提示词注入
}
```



### 2） 消息核心类：`UserMessage`

> `UserMessage`实现了`MediaContent`接口，是唯一支持多模态的消息类型，通过**文本内容 + 媒体集合**构建多模态输入：

```java
// 核心构造器
public UserMessage(String textContent, Collection<Media> mediaList) {
    this(MessageType.USER, textContent, mediaList, Map.of());
}
// 流式构建器（推荐）
UserMessage.builder()
    .text("文本问题") // 文本输入
    .media(media1) // 单个媒体
    .media(media2) // 多个媒体
    .build();
```



### 3）核心依赖：`MimeTypeUtils`

> Spring 内置的 MIME 类型工具类，用于指定媒体的模态类型，多模态开发中常用的 MIME 类型：

| 模态类型 | 常用 MimeType   | 对应常量                      |
| -------- | --------------- | ----------------------------- |
| 图片     | image/png       | MimeTypeUtils.IMAGE_PNG       |
| 图片     | image/jpeg      | MimeTypeUtils.IMAGE_JPEG      |
| 音频     | audio/mp3       | MimeTypeUtils.AUDIO_MP3       |
| 音频     | audio/wav       | MimeTypeUtils.AUDIO_WAV       |
| 文档     | application/pdf | MimeTypeUtils.APPLICATION_PDF |



### 4）核心交互流程

> Spring AI 多模态的交互流程与纯文本完全一致，仅需在`UserMessage`中添加媒体内容，核心步骤：

1. 构建 `Media` 对象（指定MimeType+媒体数据）
2. 构建 `UserMessage`（文本+Media）
3. 构建 `Prompt`
4. 调用 `ChatModel/ChatClient `

5. 解析文本响应



## 3、实战案例

### 1）网络图片解析（URI 资源）

```java
/**
 * 案例：解析网络图片（URI）
 *
 * @param imageUrl 网络图片URL
 */
@GetMapping("/image/url/parse")
public String parseUrlImage(String imageUrl) {
    try {
        // 1. 构建网络图片URI资源
        UrlResource urlResource = new UrlResource(imageUrl);
        // 2. 多模态交互（自动识别图片MimeType为JPEG/PNG）
        return chatClient.prompt()
                .system("你是专业的图像解析助手，简洁描述图片核心内容")
                .user(u -> u.text("请描述这张图片的核心内容")
                        .media(MediaType.IMAGE_JPEG, urlResource)) // 适配JPEG格式
                .call()
                .content();
    } catch (Exception e) {
        return "图片解析失败：" + e.getMessage();
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



