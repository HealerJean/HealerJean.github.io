---
title: SpringAi_5_MCP
date: 2025-11-14 00:00:00
tags: 
- AI
category: 
- AI
description: SpringAi_5_MCP
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、核心概念

> `MCP`（`Model Context Protocol`，模型上下文协议）是 `Anthropic` 于 2024 年 11 月开源的 `AI` 通用通信协议。`MCP` 类似于“`AI` 领域的 `USB` 接口”，用于在大模型（`LLM`）和外部数据源（数据库、工具、`API`、开发环境等）间建立安全、双向、标准化的连接。





## 1、为什么需要 `MCP`？

> **为 LLM 装上 “眼睛、耳朵、手脚”**：LLM 负责思考决策，MCP 负责标准化对接外部世界，构建真正的 **AI Agent（智能体）**。

- **接口碎片化**：传统 m 模型 × n 工具需 m×n 次开发；MCP 只需 m+n 次，全互联

- **能力孤岛**：`LLM` 从 “纯文本回答” 升级为 “能读文件、查数据库、发邮件、调 API、控设备”

- **厂商锁定**：**厂商中立**，切换模型（GPT/Claude/ 通义千问 /qwen3）无需改工具代码

- **安全风险**：内置权限、沙箱、认证、审计，避免 LLM 越权访问敏感数据



## 2、`MCP` 3 个核心角色

| 组件   | 英文名称   | 角色与职责                                                   |
| ------ | ---------- | ------------------------------------------------------------ |
| 宿主   | MCP Host   | 发起者。运行 LLM 的应用程序（如 Claude Desktop, IDE, 智能体平台）。它负责管理用户界面和上下文。 |
| 客户端 | MCP Client | 连接器。内嵌在 Host 中，负责与 MCP Server 建立连接、发送请求并接收结果。 |
| 服务端 | MCP Server | **能力提供者**。一个轻量级程序，负责暴露特定的能力（如“读取文件”、“查询天气”）给 AI 使用。 |

![image-20251118230102520](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20251118230102520.png)



### 1）宿主（`Host`）= **总经理**

**角色**：运行 LLM 的应用（Spring AI 应用、Claude Desktop、Cursor IDE）

**作用**：用户交互入口，协调 LLM ↔ MCP Client ↔ MCP Server 的全流程

**你的场景**：Spring Boot 应用（集成 Ollama+qwen3:14b）



### 2）客户端（`Client`）= **秘书 / 协助员**

**角色**：Host 内置的协议代理（`Spring` `AI` `McpClient`）

**核心能力**

- 连接 `Server`、**协议版本 / 能力协商**MCP 中文文档
- **工具 / 资源发现**（获取 Server 暴露的所有能力）
- 转发 `LLM` 的调用请求、接收结果、回传给 LLM
- 支持**同时连接多个 Server**（多工具 / 多数据源联动）



### 3）服务器（`Server`）= **业务部门**

**角色**：独立轻量服务，**暴露外部能力的核心**

**核心能力**

- 注册 **Tools（工具）、Resources（资源）、Prompts（提示模板）**
- 执行实际操作（读文件、查 DB、调 API）
- 返回结构化结果给 Client

**你的场景**：本地 Spring Boot 服务（暴露天气、时间、数据库等工具）



## 3、`MCP` 核心能力

### 1）`Tools`（工具）—— `AI`的"双手"

> **本质**：可执行函数，让AI能主动操作外部系统
>
> > **`Tools`** 是服务器暴露的**可执行函数**，比如 ，比如查询数据库、调用 `API`、写文件。 `AI` 在推理过程中根据需要动态调用，以获取信息或执行操作（如查询、写入、发送邮件）。

- **结构**（JSON Schema 规范）

  - ```json
    {
      "name": "getWeather",       // 唯一标识
      "description": "查询城市天气", // LLM 理解的描述
      "inputSchema": {            // 参数（自动校验）
        "type": "object",
        "properties": {"city": {"type": "string"}}
      }
    }
    ```

- **典型场景**：查天气、发邮件、执行代码、调用业务 API、控设备

- **你的场景**：qwen3:14b 自动判断 “用户问天气”→ 调用 `getWeather` 工具



### 2）`Resources`（资源）—— `AI`的"眼睛"

> **本质**：只读数据源，为 `AI` 提供上下文信息
>
> > **`Resources`** 是服务器托管的**只读数据单元**，代表外部世界的上下文信息，如文件、数据库记录、`API` 响应等。它们不主动执行逻辑，仅作为 `AI` 推理的输入依据。：

- **定位**：**结构化只读数据源**，`LLM` 引用参考（AI 的 “资料库”）

- **形式**：URI 标识（如 `file:///data/doc.txt`、`db://mysql/order`）

- **典型场景**：读本地文件、查询知识库、拉取 API 数据、访问数据库视图

- **特点**：**无副作用**，仅读取、不修改数据



| 特性       | 资源 (Resources)                              | 工具 (Tools)                   |
| ---------- | --------------------------------------------- | ------------------------------ |
| 主要动作   | 读取 (Read)                                   | 执行 (Execute)                 |
| 类比       | 就像看书或看文档                              | 就像用计算器或发邮件           |
| AI 的行为  | 获取上下文信息，作为回答的依据                | 改变系统状态，产生副作用       |
| 典型 `URI` | `file:///path/to/doc`, `postgres://db/schema` | (通常通过函数名调用，而非 URI) |
| 实战口诀   | "AI，你看这个..."                             | "AI，帮我做这个..."            |



### 3） `Prompts`：用户的"快捷指令"

> **本质**：预定义的工作流模板，降低用户使用门槛   
>
> > `Prompt` 就像是「对话模板」或「快捷指令」，把常用的复杂指令预设好，用户一键调用。用生活中的例子类比，就像微信的「快捷回复」或IDE中的「代码片段(Snippet)」。



- **定位**：Server 暴露的**预定义优质提示词**

- **作用**：避免提示词碎片化、保证任务一致性（如 “客服回答模板”“数据分析模板”）

- **典型场景**：企业统一话术、专业任务指令、多轮对话模板





## 4、交互流程

> `MCP` 采用**宿主-客户端-服务器**三层架构，就像一家公司的组织结构   

**交互：**[`LLM/AI`应用] ──(`MCP`协议)──> [`MCP` `Server`] ──> [本地/远程工具/数据源]

1. **发现：** 客户端连接到服务端，询问“你能做什么？”。**服务端返回一份能力清单（工具、资源、提示词）。**
2. **调用：** 当用户发出指令，`LLM` 判断需要调用外部工具时，会通过客户端向服务端发送标准化的 `JSON-RPC` 请求。
3. **执行与反馈：** 服务端执行具体操作（如查询数据库），并将结果返回给 `LLM`，`LLM` 据此生成最终回答。



以 **用户问 “北京现在天气和时间？”** 为例，qwen3:14b + MCP 全流程：



### 1）初始化（Initialization）

- Spring AI（Host）启动 → 加载 **MCP Client**
- Client 连接本地 MCP Server（Stdio 模式）
- **能力协商**：Server 返回支持的 Tools、Resources、协议版本MCP 中文文档



### 2）工具发现（Discovery）

- Client 向 Server 发送 `listTools` 请求
- Server 返回所有工具的 **JSON Schema 描述**（名称、功能、参数）
- Client 把工具列表 **注入 LLM（qwen3:14b）上下文**



### 3）LLM 决策（推理）

- 用户输入：“北京现在天气和时间？”
- qwen3:14b 分析：需调用 **getWeather（北京）+ getCurrentTime** 两个工具
- LLM 返回 **Tool Call 指令**（结构化 JSON，含工具名、参数）



### 4）工具调用（Invocation）

- MCP Client 解析 Tool Call → 转发给对应 MCP Server

- Server 执行工具：

  - `getWeather`：调用天气 API → 返回 JSON 数据
  - `getCurrentTime`：本地获取时间 → 返回字符串

  

- Server 把结果返回给 Client

### 5）结果整合（生成回答）

- Client 把工具结果 **回传给 qwen3:14b**

- qwen3:14b 整合数据 → 生成自然语言回答：

  > “北京当前天气：晴，25℃；当前时间：2026-03-31 11:30:00（上海时区）”

  

### 6）会话结束

- 无状态会话，工具调用后自动清理资源，安全无残留



# 二、`Spring AI` 的 `MCP` 客户端启动器

## 1、核心概念

> Spring Boot 应用提供 **MCP 客户端全自动配置**，支持 **多服务器连接、多传输协议、同步 / 异步客户端、工具自动发现、Spring AI 工具框架深度集成**，并负责完整生命周期（初始化 / 连接 / 清理）Spring。

### 1）核心功能

- **多传输协议支持**：支持 STDIO（进程内）、HTTP/SSE（服务器发送事件）、Streamable HTTP。
- **多实例管理**：可以在一个应用中同时连接多个 MCP 服务器。
- **工具集成**：自动将 MCP 服务器提供的功能注册为 `Spring` `AI` 的 `Tool`，供大模型调用。
- **生命周期管理**：自动初始化客户端并在应用关闭时清理资源。



### 2）**三大传输协议**

- **STDIO**：本地进程通信（最常用，本地 MCP 服务器如文件系统）

- **SSE**：服务器推送事件（远程 MCP 服务器）

- **Streamable HTTP**：流式 HTTP（远程长连接）

| 传输方式                            | 通俗比喻     | 核心区别 (一句话)                                            |
| ----------------------------------- | ------------ | ------------------------------------------------------------ |
| `STDIO` (标准输入输出)              | 亲兄弟手递手 | 启动即拥有。`Spring Boot` 直接在操作系统层面 `fork` 一个子进程（如 Node.js），父子进程通过管道通信。 |
| `SSE` (`Server-Sent Events`)        | 广播电台     | 单向长连接。基于 HTTP，服务器可以一直向客户端“推”数据（流式），但客户端只能在开始时发一次请求。 |
| `Streamable` `HTTP` (可流式 `HTTP`) | 快递双向车   | 双向流式。基于 HTTP/2 或分块传输，允许请求和响应都像水流一样持续传输，交互最灵活。 |



### 3）两种客户端类型

- **`SYNC`（默认）**：同步阻塞，适合常规 `Web` 应用

- **`ASYNC`**：异步非阻塞，配合 `WebFlux`（高并发场景）





## 2、配置属性

### 1）通用属性

> 通用属性以 `spring.ai.mcp.client` 为前缀

```yaml
spring:
  ai:
    mcp:
      client:
        enabled: true                  # 启用/禁用整个MCP客户端
        name: hlj-strata-ai-mcp-client # MCP客户端名称（标识用）
        version: 1.0.0                 # 客户端版本
        initialized: true              # 是否自动初始化（必须true才能用）
        request-timeout: 20s           # 请求超时时间
        type: SYNC                     # 客户端类型：SYNC / ASYNC
        root-change-notification: true # 根目录变更通知（文件服务器用）
        toolcallback:
          enabled: true                # 开启工具集成（Ollama必须开）
```



| 属性名                       | 类型      | 说明                                                         | 默认值                 |
| :--------------------------- | :-------- | :----------------------------------------------------------- | ---------------------- |
| **enabled**                  | `Boolean` | **总开关**：是否启用 `MCP` 客户端。                          | `true`                 |
| **type**                     | `Enum`    | 客户端类型（`SYNC` 或 `ASYNC`）。所有客户端必须是同步或异步；不支持混合 | `SYNC`                 |
| **name**                     | String    | 客户端名称。通常用于标识你的应用。                           | `spring-ai-mcp-client` |
| **version**                  | String    | 客户端版本。                                                 | `1.0.0`                |
| **initialized**              | Boolean   | 是否在创建时自动初始化连接。                                 | `true`                 |
| **request-timeout**          | Duration  | 请求超时时间。`MCP` 服务器启动或调用可能较慢。               | `20s`                  |
| **toolcallback.enabled**     | Boolean   | 是否将 MCP 工具注册到 Spring AI 的工具框架中。**必须开启才能让 Ollama 看到工具**。 | `true`                 |
| **root-change-notification** | Boolean   | 文件 `MCP 服务器：目录权限变化时，是否通知客户端             | `true`                 |





### 2）`MCP` 注解属性

> 用于开启 **`@McpLogging `/ `@McpProgress` / `@McpToolListChanged`** 等注解自动扫描。

```yaml
spring:
  ai:
    mcp:
      client:
        annotation-scanner:
          enabled: true  # 自动扫描@McpXXX注解
```

| 属性名      | 默认值 | 说明                                                         |
| ----------- | ------ | ------------------------------------------------------------ |
| **enabled** | true   | 是否自动扫描项目中 `@McpLogging`、`@McpProgress` 等注解处理器 |



### 3）`STDIO` 传输配置 （本地 MCP 服务器）

> `STDIO = 本地进程通信`，用于启动本地 `MCP` 服务（如文件系统、搜索服务）。

```yaml
spring:
  ai:
    mcp:
      client:
        stdio:
          servers-configuration: classpath:mcp-servers.json  # 外部JSON配置
          root-change-notification: true                     # 目录变更通知
          connections:                                        # 多服务器连接配置
            服务器名称:
              command: /path/to/server      # 执行命令
              args:                         # 命令参数
                - --port=8080
              env:                          # 环境变量
                KEY: VALUE
```



| 属性                           | 说明                                           |
| ------------------------------ | ---------------------------------------------- |
| **servers-configuration**      | 包含 `JSON` 格式的 `MCP` 服务器配置的资源      |
| **root-change-notification**   | 允许 `MCP` 服务器通知客户端 “可访问目录已变化” |
| **connections**                | 多服务器配置 Map，key = 服务器名               |
| **connections.[name].command** | **必须** 要执行的命令（npx /node/cmd.exe）     |
| **connections.[name].args**    | 命令参数数组                                   |
| **connections.[name].env**     | 子进程环境变量（API_KEY、PATH 等）             |



### 4）`SSE` 传输配置（连接远程 MCP 服务器）

> SSE = Server-Sent Events，服务器主动推送。

```yaml
spring:
  ai:
    mcp:
      client:
        sse:
          connections:
            server1:
              url: http://localhost:8080        # 基础URL
              sse-endpoint: /sse                 # 端点（默认/sse）
```

| 属性                                | 说明                                 |
| ----------------------------------- | ------------------------------------ |
| **connections**                     | 远程 `MCP` 服务器列表                |
| **connections.[name].url**          | 服务器基础 URL（协议 + 主机 + 端口） |
| **connections.[name].sse-endpoint** | `SSE` 路径（默认 /sse）              |



### 5）`Streamable` `HTTP` 传输配置（远程长连接）

```yaml
spring:
  ai:
    mcp:
      client:
        streamable-http:
          connections:
            server1:
              url: http://localhost:8080
              endpoint: /mcp      # 默认为/mcp
```

| 属性         | 说明                |
| ------------ | ------------------- |
| **url**      | 服务器地址          |
| **endpoint** | 请求路径，默认 /mcp |





### 6）配置参考

#### a、核心必记 6 个配置

1. **enabled: true** —— 开启 MCP
2. **initialized: true** —— 自动初始化
3. **request-timeout: 30s** —— 超时时间
4. **type: SYNC** —— 本地 Ollama 用同步
5. **toolcallback.enabled: true** —— 让 Ollama 能用 MCP 工具
6. **stdio.servers-configuration** —— 指向本地 MCP 服务器 JSON 配置

#### b、传输方式选择

- **本地 MCP 服务器（文件系统）** → **STDIO**
- **远程 MCP 服务器** → **SSE / Streamable HTTP**



## 3、注解使用

> 服务端推消息，是因为服务端在执行 @McpTool 时调用了 context.xxx ()，这些方法本质就是 “向客户端发消息” 的 API。客户端注解只是监听这些推送的回调。

| 注解                    | 作用             | 方向               | 典型场景            |
| ----------------------- | ---------------- | ------------------ | ------------------- |
| @McpLogging             | 接收服务器日志   | 服务器→客户端      | 日志展示、错误监控  |
| @McpSampling            | 处理 AI 生成请求 | 服务器→客户端→AI   | 客户端提供 LLM 能力 |
| @McpElicitation         | 收集用户信息     | 服务器→客户端→用户 | 表单、补充信息      |
| @McpProgress            | 接收任务进度     | 服务器→客户端      | 进度条、长时间任务  |
| @McpToolListChanged     | 工具列表变更     | 服务器→客户端      | 动态刷新 AI 工具    |
| @McpResourceListChanged | 资源列表变更     | 服务器→客户端      | 配置、文件更新      |
| @McpPromptListChanged   | 提示词列表变更   | 服务器→客户端      | 提示词版本同步      |



### 1）`@McpLogging` 日志通知

> 作用：接收 **`MCP` 服务器推送的日志消息**（info / warn / error）。

```java
// 完整对象接收
@McpLogging(clients = "my-mcp-server")
public void handleLog(LoggingMessageNotification notification) {
    System.out.println("日志级别：" + notification.level());
    System.out.println("日志内容：" + notification.data());
}

// 拆分成独立参数接收（更简单）
@McpLogging(clients = "my-mcp-server")
public void handleLogSimple(LoggingLevel level, String logger, String data) {
    System.out.printf("[%s] %s%n", level, data);
}
```

**服务端：**

```java
@McpTool(name = "long_task")
public String longTask(McpSyncRequestContext context) {
    // ↓↓↓ 这一行 → 触发客户端 @McpLogging
    context.info("开始执行任务");
    return "ok";
}
```

为什么推？

- 你调用了 `context.info()` / `warn()` / `error()`
- 框架底层自动封装成 `LoggingMessageNotification`
- 推给客户端
- 客户端 `@McpLogging` 接收

典型业务原因

- 服务端执行 AI 工具时要打日志
- 想让客户端看到执行过程
- 调试、监控、排错







### 2）`@McpSampling` `LLM` 采样请求

> 作用：**服务器让客户端帮忙调用 AI 生成回答**（客户端拥有 LLM 权限时使用）。

```java
// 同步
@McpSampling(clients = "llm-server")
public CreateMessageResult handleSampling(CreateMessageRequest request) {
    String answer = "我是客户端AI，我来回答：" + request.messages();

    return CreateMessageResult.builder()
            .role(Role.ASSISTANT)
            .content(new TextContent(answer))
            .model("client-llm")
            .build();
}

// 异步
@McpSampling(clients = "llm-server")
public Mono<CreateMessageResult> asyncSample(CreateMessageRequest request) {
    return Mono.fromCallable(() -> {
        return CreateMessageResult.builder()
                .role(Role.ASSISTANT)
                .content(new TextContent("异步回答"))
                .build();
    });
}
```



服务端：

```java
@McpTool(name = "ask_ai")
public String askAi(McpSyncRequestContext context) {
    // ↓↓↓ 这一行 → 触发客户端 @McpSampling
    CreateMessageResult result = context.sample("帮我写一段话");
    return result.content();
}
```

**为什么推？：服务端自己没有 LLM，必须让客户端帮忙调用 AI。**

- 服务端只是业务逻辑 + 工具
- AI 模型在客户端本地（私有化、安全、隐私）
- 服务端说：“我不会生成回答，你帮我生成”
- 推请求 → 客户端 `@McpSampling` 处理并返回

**典型业务原因：**

- 本地 AI 隐私计算
- 企业内网模型
- 敏感数据不能上传



### 3）`@McpElicitation` 信息收集（启发式交互）

> 作用：**服务器让客户端向用户收集信息**（表单、必填项缺失时）。

```java
@Component
public class ElicitationHandler {

    @McpElicitation(clients = "interactive-server")
    public ElicitResult handle(ElicitRequest request) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", "张三");
        userData.put("age", 25);
        userData.put("email", "test@qq.com");

        // ACCEPT = 用户同意
        return new ElicitResult(ElicitResult.Action.ACCEPT, userData);
    }
}
```





**服务端代码（触发点）**

```java
@McpTool(name = "create_order")
public String createOrder(McpSyncRequestContext context) {
    // ↓↓↓ 这一行 → 触发客户端 @McpElicitation
    var userInfo = context.elicit(UserInfo.class);
}
```

**为什么推？服务端发现参数不够，需要向用户要信息。**

- 服务端执行工具时缺参数
- 服务端不能直接弹框，只能让客户端去问用户
- 推送一个 “表单结构” 给客户端
- 客户端收集后返回服务端

**典型业务原因**

- 订单缺少手机号
- AI 对话缺少关键信息
- 权限确认、验证码、用户确认







### 4）`@McpProgress` 进度通知

> 作用：接收服务器**长时间任务的实时进度**。

```java
@Component
public class ProgressHandler {

    @McpProgress(clients = "my-mcp-server")
    public void handle(ProgressNotification notification) {
        System.out.printf("进度：%.0f%%，消息：%s%n",
                notification.progress() * 100,
                notification.message());
    }
}
```

**服务端代码（触发点）**

```java
@McpTool(name = "long_task")
public String longTask(McpSyncRequestContext context) {
    // ↓↓↓ 这一行 → 触发客户端 @McpProgress
    context.progress(0.3);
    context.progress(p -> p.progress(0.5).message("处理中"));
}
```

**为什么推？**

- 服务端任务耗时很长（AI 生成、文件处理、数据计算）
- 需要让客户端看到进度条
- 调用 `context.progress()` → 自动推送进度通知

**典型业务原因**

- AI 绘图
- 大文件解析
- 批量数据处理
- 模型推理





### 5）`@McpToolListChanged` 工具列表变更

> 作用：**服务器工具新增 / 删除时，客户端自动收到更新**。

**使用场景：**

- 动态工具刷新
- AI 能力实时更新
- 多服务器环境工具同步

```java
@McpToolListChanged(clients = "tool-server")
public void onUpdate(List<McpSchema.Tool> tools) {
    System.out.println("最新工具数量：" + tools.size());
    tools.forEach(t -> System.out.println(t.name() + "：" + t.description()));
}
```

服务端触发条件

- 服务端启动时
- 动态注册 / 卸载工具时
- 配置刷新时

为什么推？

- 客户端需要知道 “AI 现在能调用哪些工具”
- 动态能力更新



## 6）`@McpPromptListChanged` 提示词列表变更

> 作用：服务器提示词更新 → 客户端自动同步最新版本。

**触发条件**

- `@McpResource` 新增 / 删除
- 配置变更
- 资源权限变化

**为什么推？**

- 客户端要知道：“我现在能访问哪些资源 URI？”







# 三、`Spring AI` 的 `MCP` 服务器启动器

## 1、核心概念

> **MCP 服务器 = 向 AI 应用暴露功能的标准化服务**

### 1）核心作用

- 你写一个 **MCP 服务器** → 暴露工具 / 资源 / 提示
- AI 客户端（Ollama + Spring AI MCP 客户端）连接它
- AI 自动调用你暴露的功能

### 2）`MCP` 服务器核心能力

- **工具暴露**（@McpTool）→ 让 AI 调用方法

- **资源暴露**（@McpResource）→ 让 AI 读取数据

- **提示模板暴露**（@McpPrompt）→ 让 AI 使用预设提示

- **4 种通信协议**（STDIO/SSE/Streamable HTTP/Stateless）

- **同步 / 异步双模式**

- **注解驱动开发**（零配置）

- **自动扫描 + 自动注册**



### 3）四种通信协议

| 协议         | 通信方式               | 说明                           | 启用配置                          | 适用             |
| ------------ | ---------------------- | ------------------------------ | --------------------------------- | ---------------- |
| `STDIO`      | 标准输入输出（进程内） | 命令行 / 本地进程通信          | `spring.ai.mcp.server.stdio=true` | 本地 MCP 服务器  |
| `SSE`        | 服务器推送事件         | HTTP 长连接，浏览器 / 远程调用 | `protocol=SSE` 或留空             | 实时长连接       |
| `STREAMABLE` | 可流式 HTTP            | 实时返回数据，AI 流式对话      | `protocol=STREAMABLE`             | 替代 SSE，更通用 |
| `STATELESS`  | 无状态 HTTP            | 单次请求 / 响应，无会话        | `protocol=STATELESS`              | 微服务、云部署   |



### 4）同步 / 异步服务器（SYNC / ASYNC）

- **`SYNC` (同步/命令式)**：
  - **对应类**：`McpSyncServer`
  - **特点**：传统的 Spring MVC 风格，一个请求对应一个线程。
  - **配置**：`spring.ai.mcp.server.type=SYNC`
  - **注意**：只能识别 `@McpTool` 标注的同步方法。
- **`ASYNC` (异步/响应式)**：
  - **对应类**：`McpAsyncServer`
  - **特点**：基于 `Project Reactor `(`WebFlux`)，非阻塞，高并发。
  - **配置**：`spring.ai.mcp.server.type=ASYNC`
  - **注意**：只能识别返回 `Mono` 或 `Flux` 的异步方法。



### 5）同步 / 异步、有状态 / 无状态 适配规则

| 类型       | 适用场景                | 支持方法                           |
| ---------- | ----------------------- | ---------------------------------- |
| 同步有状态 | 交互式 AI、需要双向通信 | 普通返回值 + McpSyncRequestContext |
| 异步有状态 | 高并发、流式响应        | Mono/Flux + McpAsyncRequestContext |
| 同步无状态 | 简单接口、无交互        | 普通返回值 / McpTransportContext   |
| 异步无状态 | 高性能无交互接口        | Mono/Flux / McpTransportContext    |



## 2、服务器启动器选型

### 1）`STDIO` 和 `SSE` `MCP` 服务器->三大启动器选型

| 依赖名称                               | 适用场景            | 传输方式    | 关键特性                             |
| -------------------------------------- | ------------------- | ----------- | ------------------------------------ |
| `spring-ai-starter-mcp-server`         | 非 Web / CLI 工具   | STDIO       | 轻量级，无需 Web 容器                |
| `spring-ai-starter-mcp-server-webmvc`  | Spring MVC 项目     | WebMVC SSE  | 基于 Servlet，阻塞式                 |
| `spring-ai-starter-mcp-server-webflux` | Spring WebFlux 项目 | WebFlux SSE | 基于 Reactor，响应式，推荐用于高并发 |



#### a、 `STDIO` `MCP` 服务器

- 适用于命令行和桌面工具
- 无需额外 `Web` 依赖
- 基本服务器组件的配置
- 工具、资源和提示规范的处理
- 服务器功能和变更通知的管理
- 支持同步和异步服务器实现

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server</artifactId>
</dependency>
```



#### b、`WebMVC` 启动器（同步 Web）

> 通过基于 `Spring` `MVC` 的 `SSE`（服务器发送事件）服务器传输和可选的 `STDIO` 传输提供完整的 MCP 服务器功能支持。

- 使用 `Spring` `MVC` 的基于 HTTP 的传输 (`WebMvcSseServerTransportProvider`)
- 自动配置的 `SSE` 端点
- 可选的 `STDIO` 传输（通过设置 `spring.ai.mcp.server.stdio=true` 启用）
- 包含 `spring-boot-starter-web` 和 `mcp-spring-webmvc` 依赖项

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```



#### c、`WebFlux` 启动器（响应式 Web）

> 通过基于 `Spring` `WebFlux` 的 `SSE`（服务器发送事件）服务器传输和可选的 `STDIO` 传输提供完整的 MCP 服务器功能支持。

- 使用 `Spring` `WebFlux` 的反应式传输 (`WebFluxSseServerTransportProvider`)
- 自动配置的反应式 `SSE` 端点
- 可选的 `STDIO` 传输（通过设置 `spring.ai.mcp.server.stdio=true` 启用）
- 包含 `spring-boot-starter-webflux` 和 `mcp-spring-webflux` 依赖项

注意：如果项目使用 `spring-boot-starter-web`，建议使用 `spring-ai-starter-mcp-server-webmvc` 而不是 `spring-ai-starter-mcp-server-webflux`。

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
</dependency>
```



### 2）可流式传输的 `HTTP` `MCP` 服务器

> [可流式传输的 HTTP 传输]允许 MCP 服务器作为独立进程运行，使用 HTTP POST 和 GET 请求处理多个客户端连接，并可选地通过 `Server-Sent Events` (`SSE`) 流式传输多个服务器消息。它取代了 `SSE` 传输。

```properties
spring.ai.mcp.server.protocol=STREAMABLE
```

#### a、可流式传输的 `HTTP` `WebMVC` 服务器

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

并将 `spring.ai.mcp.server.protocol` 属性设置为 `STREAMABLE`。

- 使用 Spring MVC 可流式传输传输的完整 MCP 服务器功能
- 支持工具、资源、提示、完成、日志记录、进度、ping、根更改功能
- 持久连接管理

#### b、可流式传输的 `HTTP` `WebFlux` 服务器

使用 `spring-ai-starter-mcp-server-webflux` 依赖项

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
</dependency>
```

并将 `spring.ai.mcp.server.protocol` 属性设置为 `STREAMABLE`。

- 使用 `WebFlux` 可流式传输传输的响应式 `MCP` 服务器
- 支持工具、资源、提示、完成、日志记录、进度、ping、根更改功能
- 非阻塞、持久连接管理



### 3）无状态可流式 `HTTP` `MCP` 服务器

> 无状态可流式 `HTTP` `MCP` 服务器旨在简化部署，其中请求之间不维护会话状态。这些服务器是微服务架构和云原生部署的理想选择。

```properties
spring.ai.mcp.server.protocol=STATELESS
```



#### a、无状态 `WebMVC` 服务器

使用 `spring-ai-starter-mcp-server-webmvc` 依赖

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webmvc</artifactId>
</dependency>
```

并将 `spring.ai.mcp.server.protocol` 属性设置为 `STATELESS`。

```
spring.ai.mcp.server.protocol=STATELESS
```

- 使用 `Spring` `MVC` 传输的无状态操作
- 无会话状态管理
- 简化的部署模型
- 针对云原生环境进行了优化



#### b、无状态 `WebFlux` 服务器

使用 `spring-ai-starter-mcp-server-webflux` 依赖

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-mcp-server-webflux</artifactId>
</dependency>
```

并将 `spring.ai.mcp.server.protocol` 属性设置为 `STATELESS`。

- 使用 `WebFlux` 传输的响应式无状态操作
- 无会话状态管理
- 非阻塞请求处理
- 针对高吞吐量场景进行了优化





## 3、配置属性

### 1）通用配置

| 财产                      | 描述                                                         | 默认值       |
| :------------------------ | :----------------------------------------------------------- | :----------- |
| `enabled`                 | 启用/禁用 `MCP` 服务器                                       | `true`       |
| `tool-callback-converter` | 是否自动把 `Spring AI` `@Tool` 转为 `MCP` 工具               | `true`       |
| `stdio`                   | 启用/禁用 `STDIO` 传输                                       | `假`         |
| `name`                    | 用于识别的服务器名称                                         | `mcp-server` |
| `version`                 | 服务器版本                                                   | `1.0.0`      |
| `instructions`            | 给 AI 客户端看的**使用说明**。例："提供天气查询、服务器信息查询" | `null`       |
| `type`                    | 服务器类型（同步/异步）                                      | `SYNC`       |
| `tool-response-mime-type` | 每个工具名称的可选响应 `MIME` 类型。例如，`spring.ai.mcp.server.tool-response-mime-type.generateImage=image/png` 会将 `image/png` `MIME` 类型与 `generateImage()` 工具名称关联起来 | `-`          |
| `request-timeout`         | 等待服务器响应的持续时间，超时则请求失败。适用于通过客户端发出的所有请求，包括工具调用、资源访问和提示操作 | `20秒`       |



### 2）能力开关配置（控制开放哪些功能）

```yaml
spring:
  ai:
    mcp:
      server:
        capabilities:
          tool: true
          resource: true
          prompt: true
          completion: true
```

| 配置项                      | 默认值 | 作用                                        |
| --------------------------- | ------ | ------------------------------------------- |
| **capabilities.tool**       | true   | 开放 **工具调用能力**（最常用）             |
| **capabilities.resource**   | true   | 开放 **资源访问能力**（文件 / 配置 / 数据） |
| **capabilities.prompt**     | true   | 开放 **提示词模板能力**                     |
| **capabilities.completion** | true   | 开放 **输入补全能力**（代码联想）           |



### 3）变更通知开关（服务器主动推消息）

| 配置项                           | 默认值 | 作用                              |
| -------------------------------- | ------ | --------------------------------- |
| **resource-change-notification** | true   | 资源变化时，主动通知客户端        |
| **prompt-change-notification**   | true   | 提示词更新时，主动通知客户端      |
| **tool-change-notification**     | true   | 工具新增 / 删除时，主动通知客户端 |



### 4）`MCP` 注解扫描配置

```yaml
spring:
  ai:
    mcp:
      server:
        annotation-scanner:
          enabled: true
```

| 配置项                         | 默认值 | 作用                                                         |
| ------------------------------ | ------ | ------------------------------------------------------------ |
| **annotation-scanner.enabled** | true   | MCP服务器注解提供了一种使用Java注解实现MCP服务器处理程序的声明性方法。 |





### 4）启动器配置

#### a、`SSE` `Web` 传输配置（HTTP 推送接口）

> 所有 `SSE` 属性都以 `spring.ai.mcp.server` 作为前缀

```yaml
spring:
  ai:
    mcp:
      server:
        sse-endpoint: /sse
        sse-message-endpoint: /mcp/message
        base-url: /api/v1
        keep-alive-interval: 30s
```

| 配置项                   | 默认值       | 详细解释                                                 |
| ------------------------ | ------------ | -------------------------------------------------------- |
| **sse-endpoint**         | /sse         | 客户端**订阅推送**的地址客户端连接：`http://ip:port/sse` |
| **sse-message-endpoint** | /mcp/message | 客户端**发送请求**的地址AI 调用工具 → POST 到这里        |
| **base-url**             | 无           | 路径统一前缀例：`/api/v1`最终地址：`/api/v1/sse`         |
| **keep-alive-interval**  | null（禁用） | 心跳保活间隔例：`30s` = 每 30 秒发一次心跳               |



#### b、可流式传输的 `HTTP` 属性

> 所有可流式传输的 HTTP 属性都以 `spring.ai.mcp.server.streamable-http` 为前缀

| 财产                  | 描述                | 默认值         |
| :-------------------- | :------------------ | :------------- |
| `mcp-endpoint`        | 自定义 MCP 端点路径 | `/mcp`         |
| `keep-alive-interval` | 连接保持活动间隔    | `null`（禁用） |
| `disallow-delete`     | 禁止删除操作        | `假`           |



#### c、无状态连接属性

> 所有连接属性都以 `spring.ai.mcp.server.stateless` 为前缀

| 参数              | 描述              | 默认值 |
| :---------------- | :---------------- | :----- |
| `mcp-endpoint`    | 自定义MCP端点路径 | `/mcp` |
| `disallow-delete` | 不允许删除操作    | `假`   |



### 6）配置模板

#### a、`STDIO` 命令行模式（无 Web）

```yaml
spring:
  ai:
    mcp:
      server:
        enabled: true
        name: cli-mcp-server
        version: 1.0.0
        type: SYNC
        stdio: true          # 必须开启
        instructions: "命令行 MCP 服务"
        request-timeout: 30s
```



#### b、`WebMVC` 同步 `Web` 模式

```yaml
spring:
  ai:
    mcp:
      server:
        enabled: true
        name: webmvc-mcp
        version: 1.0.0
        type: SYNC
        instructions: "提供天气查询工具"
        request-timeout: 20s
        # SSE
        sse-endpoint: /sse
        sse-message-endpoint: /mcp/message
        keep-alive-interval: 30s
        # 能力开关
        capabilities:
          tool: true
          resource: true
          prompt: false
          completion: false
```



#### c、`WebFlux`异步高并发模式

```yaml
spring:
  ai:
    mcp:
      server:
        enabled: true
        name: webflux-mcp
        version: 1.0.0
        type: ASYNC    # 异步
        instructions: "响应式 MCP 服务"
        sse-endpoint: /sse
        keep-alive-interval: 30s
```





# 4、注解使用



### 5）核心注解

| 注解           | 作用                  | 入参来源    | 出参用途                   | 典型业务场景                     |
| -------------- | --------------------- | ----------- | -------------------------- | -------------------------------- |
| `@McpTool`     | 暴露工具（AI 可调用   | AI 自动传参 | 返回执行结果               | 计算、查询、创建、发送、接口调用 |
| `@McpResource` | 暴露资源（AI 可读取） | URI 路径    | 配置、文件、用户信息等数据 | 配置、用户信息、文件、文档、数据 |
| `@McpPrompt`   | 暴露提示模板          | 动态参数    | 给 AI 提供预设提示词。     | 角色设定、对话生成、任务指令     |
| `@McpComplete` | 自动完成提示          | 输入前缀    | 参数自动补全。             | 表单填写、关键词、城市、商品选择 |



### 1）`@McpTool` 工具注解

> **作用**：将普通方法标记为 AI 可调用的工具，自动生成参数 Schema，支持上下文、进度跟踪、动态参数。

```java
/**
   * 加法工具：AI 可直接调用
   * @param a 第一个数字
   * @param b 第二个数字
   * @return 计算结果
   */
  @McpTool(
          name = "add",
          description = "两个数字相加"
  )
  public int add(
          @McpToolParam(description = "第一个数字", required = true) int a,
          @McpToolParam(description = "第二个数字", required = true) int b
  ) {
      return a + b;
  }
```



### 2）`@McpResource` 资源注解

```java
// 模拟配置存储
private static final Map<String, String> CONFIG_MAP = new ConcurrentHashMap<>();

static {
    CONFIG_MAP.put("app_name", "MCP演示系统");
    CONFIG_MAP.put("version", "1.0.0");
    CONFIG_MAP.put("author", "Spring AI");
}

/**
 * 基础资源：通过 config://{key} 访问配置
 */
@McpResource(
        uri = "config://{key}",
        name = "系统配置",
        description = "获取系统配置信息"
)
public ReadResourceResult getConfig(McpSyncRequestContext context, String key) {
    context.info("客户端访问资源：config://" + key);

    String value = CONFIG_MAP.getOrDefault(key, "配置不存在");

    // 构建资源返回体
    return new ReadResourceResult(List.of(
            new TextResourceContents(
                    "config://" + key,
                    "text/plain",
                    value
            )
    ));
}
```



### 3）`@McpPrompt` 提示词注解

> **作用**：生成 AI 交互用的标准化提示词，支持动态参数。

```java
/**
 * 生成问候提示词
 */
@McpPrompt(
        name = "greeting",
        description = "生成用户问候语"
)
public GetPromptResult greeting(
        @McpArg(name = "username", description = "用户名", required = true) String username) {
    String content = "你好，" + username + "！我是MCP智能助手，很高兴为你服务。";

    // 构建提示词结果
    return new GetPromptResult(
            "智能问候",
            List.of(new PromptMessage(Role.ASSISTANT, new TextContent(content)))
    );
}

```



### 4）@`McpComplete` 自动补全注解

> **作用**：为输入参数提供自动补全，提升 AI / 用户输入体验。

```java
// 模拟城市列表
private static final List<String> CITY_LIST = List.of(
        "北京", "上海", "广州", "深圳", "杭州", "南京", "成都", "重庆"
);

/**
 * 城市名称补全
 */
@McpComplete(prompt = "city_search")
public CompleteResult completeCity(String prefix) {
    // 过滤匹配前缀的城市
    List<String> result = CITY_LIST.stream()
            .filter(city -> city.startsWith(prefix))
            .limit(5)
            .toList();

    return new CompleteResult(
            new CompleteResult.CompleteCompletion(result, result.size(), false)
    );
}
```





# 四、**`@Tool`**、**`@McpTool`** 区别

- **`@Tool`** 是 **`Spring` `AI` 原生** 的注解，主要用于将方法暴露给 **`Spring` `AI` 的 `ChatClient`**（即让 AI 助手在聊天中调用）。

- **`@McpTool`** 是 **`MCP` (`Model Context Protocol`)** 协议专用的注解，用于将方法暴露为标准的 `MCP` 服务器工具




## 1、核心区别对比表

| 特性     | `@Tool` (Spring AI 原生)                                     | `@McpTool` (MCP 协议)                            |
| -------- | ------------------------------------------------------------ | ------------------------------------------------ |
| 参数注解 | `@ToolParam`                                                 | `@McpToolParam`                                  |
| 所属包   | `org.springframework.ai.tool.annotation.Tool`                | `org.springframework.ai.mcp.server.tool.McpTool` |
| 主要用途 | 增强 `Spring AI` 的 `ChatClient` 能力                        | 将当前应用变成一个 **MCP Server**                |
| 注册方式 | 通常配合 `MethodToolCallbackProvider` 自动注册到 `ChatClient`。 | 由 `MCP Server` 自动扫描并注册                   |
| 适用场景 | **单体应用内部**：                                           | **服务化架构**                                   |



## 2、深度解析

### 1）`@Tool`：`Spring` `AI` 的“亲儿子”

> 这是 Spring AI 框架自带的工具定义方式。当你构建一个基于 `Spring` `AI` 的聊天机器人（`ChatClient`）时，你希望机器人能查询数据库或执行计算，就会用到这个。

- **工作流**：
  1. 你在 `Service` 层加上 `@Tool`。
  2. 配置 `ToolCallbackProvider`（如 `MethodToolCallbackProvider`）。
  3. 将其注入到 `ChatClient` 中。
- **优势**：与 `Spring` 容器集成紧密，直接操作 Bean，无需序列化/反序列化开销，适合应用内部逻辑复用。



```java
@Service
public class BookService {

  // 这里的 description 是给 AI 看的，告诉它什么时候该用这个方法
  @Tool(name = "findBooksByTitle", description = "根据书名模糊查询图书")
  public List<String> findBooksByTitle(@ToolParam(description = "书名关键词") String title) {
      // 内部逻辑...
      return List.of("语文书", "数学书");
  }
}

// 配置类
@Configuration
public class AiConfig {
  
  @Bean
  public ToolCallbackProvider toolProvider(BookService bookService) {
      // 将带有 @Tool 的方法注册给 ChatClient
      return MethodToolCallbackProvider.builder()
              .toolObjects(bookService)
              .build();
  }
}
```



### 2）`@McpTool`：通往 `MCP` 生态的“通行证”

> 这是为了适配 **`Model` `Context` `Protocol` (`MCP`)** 标准而设计的。`MCP` 是一个旨在标准化`AI` 模型与外部数据/工具交互的协议。

- **工作流**：
  1. 引入 `spring-ai-mcp-server` 依赖。
  2. 在 `application.yml` 开启 `spring.ai.mcp.server.enabled=true`。
  3. 加上 `@McpTool`，`Spring Boot `启动后会自动将其注册为 MCP 服务。
  4. 外部客户端（如 `Claude` `Desktop`）通过 `SSE` 端点连接你的服务，发现并调用这个工具。
- **优势**：**解耦**。你的服务可以独立部署，任何支持 `MCP` 的 `AI` 客户端（不仅仅是 Spring AI 写的客户端）都可以连接并使用你的工具。

```java
@Component // 必须是一个 Bean
public class MathTool {

    // 外部 MCP 客户端会看到这个工具名为 "add_numbers"
    @McpTool(name = "add_numbers", description = "将两个数字相加")
    public int add(@McpToolParam(description = "第一个数字") int a, 
                   @McpToolParam(description = "第二个数字") int b) {
        return a + b;
    }
}
// 不需要手动注册，MCP Server 会自动扫描 @Component 中的 @McpTool
```



## 3、使用规范

- **统一标准**：`Spring AI 1.1` 的核心变革就是推行 `MCP` 标准。`@McpTool` 不再仅仅是“`MCP` 协议专用”，它已经成为了 `Spring AI `定义工具的**一等公民**。
- **底层互通**：`@McpTool` 在底层会被转换成 `ToolCallback`。`ChatClient` 不关心你用的是哪个注解，它只关心有没有 `ToolCallback`。





















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



