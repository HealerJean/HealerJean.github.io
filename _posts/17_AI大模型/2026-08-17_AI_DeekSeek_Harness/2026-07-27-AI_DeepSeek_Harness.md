---
title: AI_DeepSeek_Harness
date: 2026-07-27 00:00:00
tags: 
- AI
category: 
- AI
description: AI_DeepSeek_Harness
---

**前言**    

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)        

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)        

  菜鸟：https://www.runoob.com/deepseek-harness/deepseek-harness-tutorial.html



# 一、`DeepSeek Harness`

## 1、`DeepSeek Harness` 简介

> 核心设计理念：**Everything is a Plugin 一切皆插件**

AI 模型已经能独立完成复杂的编码任务。真正的挑战不再是让模型更聪明，而是**怎么把它的能力稳定、可靠、可观测地组织成一个真正可用的 Agent**。    

2026 年 8 月，深度求索开源了 **DeepSeek Harness（dsh）**——一个**一切皆插件**的 Agent 框架，给出了这个问题的答案：**Agent = Model + Harness**。模型是 Agent 的灵魂，而 Harness 给予 Agent 理解环境、使用工具、在真实场景中持续工作的能力。



## 2、核心设计理念：**`Everything is a Plugin` 一切皆插件**

> 模型、工具、技能、会话、沙箱、存储、循环（agent loop）、调度、UI 等**所有 Agent 能力均由插件提供**，通过 `Cordis` 内核的服务（`Service`）与事件（`Event`）彼此协作——开发者无需改动任何源码，就能在配置层选择、替换、扩展任一能力。



![image-20260817153847392](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817153847392.png)



## 3、核心特性

### 1）特性一：每一次运行都有迹可循

模型看到的一切都会写入**仅追加（append-only）设计**的会话日志：系统提示词、思维链、工具调用与结果、子 Agent 调度、每一次上下文注入，全部落盘。在 **Trajectory 视图**中可按来源查看；恢复、分叉（fork）、检索与回放共享同一份事件流——Agent 的每一步都可追溯、可复现。



### 2）特性二：多形态使用，随处运行

**`Web UI`**（默认 `http://127.0.0.1:3080`）提供完整的图形界面；**`headless`** 模式一次性运行任务、打印最终答案并退出，适合脚本与 CI；还有 CLI 与官方 **Python SDK**（`pip install deepseek-harness-sdk`，自带运行时、无需系统 Node.js）和 TypeScript SDK，把 Agent 嵌入任何工作流。



### 3）特性三：开放可控，无特权内核

MIT 开源，不存在需要打补丁的特权内核：所有能力注册都是可逆副作用，插件卸载即撤销。通过 `Profile`、组合包与 `patch` 分层叠加配置，`--dump-config` 可随时审查整棵配置树——**你的 `Agent` 是什么样，你说了算。**



### 4）特性四：模型无关，即插即用

填入 `DeepSeek API` 密钥即可使用，也支持其他提供方与自定义 `OpenAI` 兼容端点，模型路由无需重启服务器。事件驱动的扩展点体系（会话 / Agent / 能力三级事件）让开发者可以挂载策略与适配器，随时换模型、换工具、换存储。





## 4、安装

### 1）命令安装

```sh
- [~] - [3878]
└─[$] npm install -g @deepseek-ai/dsh  [15:28:19]

Need to install the following packages:
@deepseek-ai/dsh@0.1.0-rc.6
Ok to proceed? (y) y

npm warn deprecated node-domexception@1.0.0: Use your platform's native DOMException instead
dsh web: http://127.0.0.1:3080

```

### 2）`Web UI` 使用

| 步骤  | 操作       | 说明                                                         |
| :---- | :--------- | :----------------------------------------------------------- |
| **1** | 配置模型   | 打开 设置 → 模型，填入 `DeepSeek API` 密钥并保存，模型路由立即可用、无需重启 |
| **2** | 选择工作区 | 添加启动 `dsh` 时所在的项目目录并选中（选中前会话输入框不可用） |
| **3** | 运行任务   | 发送如 "`Summarize this repository`"——`agent` 读写文件、运行命令、委派子代理；超出权限策略的操作会先征求你的审批 |



### 3）常用命令速查

| 命令                                      | 作用                                                         |
| :---------------------------------------- | :----------------------------------------------------------- |
| `dsh --profile headless "任务描述"`       | 一次性运行一个任务，打印最终答案后退出（适合脚本/CI）        |
| `dsh plugin --profile <name> <pnpm 参数>` | 管理某个 `profile` 的插件（转发给 pnpm 在 profile 目录执行） |
| `dsh --profile web --port 3080`           | 浏览器访问：`http://127.0.0.1:8080`                          |
| `dsh --profile web --dump-config`         | 查看实际启动的完整配置树（不启动服务器）                     |
| `dsh --profile web --dump-default-config` | 查看默认配置树（不含用户 patch）                             |
| `pip install deepseek-harness-sdk`        | 安装 Python SDK（自带运行时）                                |



### 4）命令的范围

- **`Read Only`：**仅可读取工作区文件，无法修改文件、执行终端命令，安全性最高。
- **`Workspace Write`：**允许读写当前工作目录内文件，可在工作目录执行命令，无法访问工作目录以外文件，日常开发推荐使用。
- **`Full access`：** 拥有完整文件系统访问权限，可读写任意路径文件、执行各类终端指令，存在较高安全风险。







## 5、完整执行流程：

![image-20260817160257984](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817160257984.png)



## 5、四种运行模式

| 模式       | 插件多少             | 可控性 | 上手难度 | 典型用途         |
| ---------- | -------------------- | ------ | -------- | ---------------- |
| 标准       | 多（完整工具组合）   | 中     | 低       | 日常开发         |
| `PTC`/代码 | 中（代码编排工具）   | 高     | 中       | 批量、复杂流程   |
| 极简       | 少（`Shell` + 编辑） | 高     | 低       | 模型基准测试     |
| 创造       | 动态（内存试验）     | 最高   | 高       | 插件实验、新模式 |



### 1） 标准模式（`standard`）

> 新手首选，内置完整代码 Agent 能力，文件操作、Shell、检索、任务规划、子 Agent 等插件预装，开箱即用。

- **插件集合**：完整工具组合，包括文件操作、Shell、搜索、子任务委派、计划维护等；
- **适用场景**：日常写代码、修 Bug、分析项目、文档整理；
- **特点**：能力最全、最"省心"，是默认选择。



### 2）`PTC` / 代码模式（`Programmatic Tool Calling`）

> 能力同标准模式。支持用 `TypeScript` 批量编排工具调用，合并多轮交互，减少对话次数、节约 `Token`。依赖较强代码规划能力，调试难度更高；建议大量重复调用场景再切换。

- **工作方式**：模型先生成一段代码，再由代码组织多轮工具调用；
- **适用场景**：需要连续查询、批量处理、根据中间结果分支的任务；
- **优势**：减少模型与工具之间反复往返，降低上下文中堆积的中间信息；
- **注意**：模型生成的代码获得了更强的调度能力，对沙箱隔离、超时、资源配额和权限控制要求更高。



### 3）极简模式（`minimal`）

> 仅保留持久 `Bash` 与文件编辑器，移除附加功能。用于模型基准性能测试，不适合日常开发。

- **插件集合**：只保留一个 `Shell` 工具 + 一个文件编辑工具；
- **适用场景**：最小环境下做**模型基准测试**，尽量减少外围工具差异；
- **特点**：让评测更接近对模型自主规划、代码修改和终端操作能力的直接观察；Agent 不会做多余动作。



### 4）创造模式（`creative`）

> 具备标准模式全部能力，可探查 `Cordis` 运行环境，在线调试插件、创建新 `Agent`，实现功能自主扩展。

- **工作方式**：`Agent` 可以检查当前运行时、在内存中试验 `Cordis` 插件，再组合出新的运行模式；
- **适用场景**：插件实验、模式创作、"让 `Agent` 自己改进自己"的探索；
- **注意**：官方已有"自指式"演示，允许 `Agent `检查和修改正在运行的插件环境，但距离稳定的"自我进化"还有很长的工程路径。





# 二、插件 `Plugin`

> 本节为开发者准备。官方教程位于仓库 `docs/user/develop/basic/`，从源码运行后即可跟着做。

## 1、插件是什么

> 在 `Harness` 中，**插件是一个导出 `apply` 函数的 `TypeScript` 模块**。框架加载插件时会调用 `apply`，并传入一个 `ctx`（上下文对象），你通过 `ctx` 注册能力，比如事件监听、工具、`LLM` 适配器。

```typescript
// 文件路径：scratch-plugin/src/my-plugin.ts
import type { Context } from '@deepseek-ai/cordis'

// name 是插件名，用于在日志与配置中标识这个插件
export const name = 'hello-plugin'

// apply 是插件的入口：框架加载插件时调用它
export function apply(ctx: Context) {
   // 在这里注册工具、服务、事件等能力
}
```



## 2、插件的三种形态

| 形态         | 写法                                               | 适用场景                     |
| :----------- | :------------------------------------------------- | :--------------------------- |
| **函数形式** | 导出独立的 apply 函数                              | 大多数插件，最简单直接       |
| **对象形式** | export default 一个带 name / inject / apply 的对象 | 需要同时声明元信息时         |
| **类形式**   | export default 一个 Service 子类                   | 插件需要向其他插件提供服务时 |



### 1）函数形式

> 把 name 和 apply 分开导出，是官方示例默认的写法。

```typescript
// 文件路径：scratch-plugin/src/my-plugin.ts（函数形式）
import type { Context } from '@deepseek-ai/cordis'

export const name = 'my-plugin'

export function apply(ctx: Context) {
  // 在这里注册能力
}
```



### 2）对象形式

> 把 name、inject 和 apply 放进一个默认导出的对象。

```typescript
// 对象形式：一个默认导出对象
import type { Context } from '@deepseek-ai/cordis'

export default {
  name: 'my-plugin',
  inject: ['tools'],
  apply(ctx: Context) {
    // ...
  },
}
```

### 3）类形式

> 类形式继承 Service 基类，适合对外提供服务的插件。

```typescript
// 类形式：Service 子类，可对外提供服务
import { Service, type Context } from '@deepseek-ai/cordis'

export default class MyService extends Service {
  static inject = ['tools']

  constructor(ctx: Context) {
    // 第一个参数是 ctx，第二个参数是服务名
    super(ctx, 'myService')
    // 同步初始化放在构造函数里
  }
}
```



## 3、选择哪种形态

> 大多数情况下，函数形式就足够了。当插件需要向其他插件提供服务时，用类形式。

![image-20260817171507954](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817171507954.png)





## 4、加载插件

### 1）`scratch-plugin `目录结构

```
scratch-plugin/
├── src/
│   └── my-plugin.ts    # 插件源码（上一篇写的 hello-plugin）
└── cordis.yml          # patch 覆盖层：告诉框架插入哪个插件
```

### 2）`cordis.yml` 的 `insert`

> 在仓库根目录运行 pwd，拿到绝对路径。然后创建 scratch-plugin/cordis.yml，内容如下：

```yaml
# 文件路径：scratch-plugin/cordis.yml
# 这是一个 Web 覆盖层（overlay），只负责插入本地插件
- insert:
    - id: hello
      # name 是插件文件路径，必须是绝对路径！
      name: '/absolute/path/to/deepseek-harness/scratch-plugin/src/my-plugin.ts'
```

### 3）工作原理

- `dsh` 把多个配置来源按顺序叠加成一个最终配置。

- `--patch` 传入的 `cordis.yml `是一个覆盖层，在启动时叠加进 `profile`。

| 概念             | 是什么                   | 作用                         |
| :--------------- | :----------------------- | :--------------------------- |
| **profile**      | 一份可启动的组装配置     | 决定 Web UI 由哪些组合包组成 |
| **patch 覆盖层** | 启动时额外叠加的 yml     | 插入本地插件、覆盖某项配置   |
| **insert**       | `patch` 里插入插件的语法 | 按 name 绝对路径加载插件     |







# 三、自动清理&声明依赖

## 1、自动清理

### 1）自动清理机制

> 通过 ctx 注册的任何东西——事件监听、工具、定时器——在插件卸载时都会被自动清理。

- 你不需要手动 `removeListener` 或 `clearInterval`。

- 框架能自动清理，是因为所有通过 `ctx` 的注册都被记在插件的 Fiber 作用域里。

- 卸载时，框架按注册顺序的逆序撤销它们。

### 2）哪些操作会被自动追踪

| 注册操作                                | 卸载时的行为           |
| :-------------------------------------- | :--------------------- |
| ctx.on(event, handler)                  | 事件监听自动移除       |
| ctx.tools.register(tool)                | 工具注册自动撤销       |
| ctx.llm.registerAdapter(names, adapter) | LLM 适配器注册自动撤销 |
| ctx.effect(() => cleanup)               |                        |

![image-20260817172346178](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817172346178.png)



### 3）`ctx.effect()`：手动资源交给框架

有些资源不在上面的列表里，比如一个网络连接。     

用 `ctx.effect() `告诉框架怎么清理它。     

`ctx.effect` 接收一个回调，回调里创建资源并返回一个清理函数（`disposer`）。     

这个 `disposer` 会在插件卸载时执行。

```typescript
// 文件路径：scratch-plugin/src/heartbeat.ts
import type { Context } from '@deepseek-ai/cordis'

export function apply(ctx: Context) {
  ctx.effect(() => {
    // 创建定时器：每 5 秒打印一次 heartbeat
    const timer = setInterval(() => {
      console.log('heartbeat')
    }, 5000)

    // 返回的清理函数在插件卸载时执行
    // 等价于：不需要你在卸载逻辑里手动 clearInterval
    return () => clearInterval(timer)
  })
}
```

### 4）执行顺序的细节

插件卸载时，处置器按注册顺序的逆序开始调用。    

多个异步处置器会并发执行，不保证逐个完成。    

存在顺序依赖的清理步骤，必须放进同一个 ctx.effect() 返回的处置器中，由该处置器负责串行等待。





## 2、声明依赖：`inject` 与内置服务

### 1）什么是服务

> 服务是一个插件向其他插件公开的命名能力。在 Harness 中，tools、llm、agents 都是服务，它们挂载在 ctx 上：ctx.tools、ctx.llm、ctx.agents。

| 内置服务       | 是什么                    | 典型用法                 |
| :------------- | :------------------------ | :----------------------- |
| **ctx.tools**  | 工具运行时（ToolRuntime） | 注册 / 调用工具          |
| **ctx.llm**    | 大语言模型服务（LLM）     | 注册适配器、发起模型请求 |
| **ctx.agents** | 智能体服务（Agent）       | 管理子智能体             |

### 2）用 `inject` 声明依赖

> 如果你的插件需要某个服务，就把它写进 inject 数组。框架会确保这些服务就绪后，才加载你的插件。

```typescript
// 文件路径：scratch-plugin/src/my-tool-plugin.ts
import type { Context } from '@deepseek-ai/cordis'

export const name = 'my-tool-plugin'
// 声明依赖：需要 tools 服务
export const inject = ['tools']

export function apply(ctx: Context) {
  // 走到这里时，ctx.tools 一定已就绪
  ctx.tools.register(/* ... */)
}
```

### 3）依赖未就绪：插件等待

> 如果某个服务还没准备好，插件不会执行。它的 Fiber 会停在 PENDING 状态，等服务出现。在生命周期里，PENDING 表示"已声明，但所需依赖未就绪"。如果服务一直不来，插件就一直等，不会出错，也不会执行 apply。

![image-20260817172701543](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817172701543.png)

### 4）必需依赖与可选依赖

> inject 声明的是必需依赖：服务缺席时插件不加载。如果某个服务可用可不用，用可选依赖：不写 inject，在使用处用 ctx.get() 查询。

```typescript
// 可选依赖：省略 inject，用 ctx.get() 查询
import type { Context } from '@deepseek-ai/cordis'

export function apply(ctx: Context) {
  // metrics 服务可能在也可能不在
  const metrics = ctx.get('metrics')
  // 可选链：不存在就跳过，不报错
  metrics?.record('plugin_loaded', 1)
}
```







# 四、工具

> 工具是 Agent 用来干活的函数，模型看到工具定义后决定调用。

## 1、工具是什么

> 工具（tool）是一个描述清晰的函数，包含名称、说明、参数和输出格式。模型在生成回复时，可以根据这些信息发起调用。在 dsh 里，工具通过 ctx.tools.register 注册到工具注册表。

| 字段              | 作用          | 说明                                       |
| :---------------- | :------------ | :----------------------------------------- |
| **name**          | 工具名        | 模型用它发起调用                           |
| **description**   | 工具说明      | 告诉模型这个工具做什么                     |
| **parameters**    | 入参 schema   | 类型化定义，defineTool 据此推导并校验 args |
| **output.schema** | 返回值 schema | 声明 execute 返回的规范值类型              |
| **output.render** | 结果格式化    | 把规范值转成面向模型的内容                 |
| **execute**       | 工具实现      | 真正执行逻辑，返回规范值                   |



## 2、创建 `greet` 工具

> 把 `scratch-plugin/src/my-plugin.ts` 替换为以下内容。

```typescript
// 文件路径：scratch-plugin/src/my-plugin.ts
import type { Context } from '@deepseek-ai/cordis'
import { defineTool } from '@deepseek-ai/dsh-tools'

export const name = 'greet-tool'
// 需要 tools 服务：注册工具的前提
export const inject = ['tools']

export function apply(ctx: Context) {
  // 注册一个名为 greet 的工具
  ctx.tools.register(defineTool({
    // 工具名：模型会以这个名字发起调用
    name: 'greet',
    // 工具说明：告诉模型什么时候用
    description: 'Greet someone by name.',
    // 入参 schema：defineTool 会推导并校验 args
    parameters: {
      name: { type: 'string', required: true, description: 'The name to greet' },
    },
    // 输出定义
    output: {
      // 规范值类型：execute 的返回值
      schema: { type: 'string' },
      // render：把规范值转成面向模型的内容
      render: (_args, value) => [{ type: 'text', text: value }],
    },
    // 工具实现：真正执行逻辑
    async execute(args) {
      // 返回规范值，这里是一个字符串
      return `Hello, ${args.name}!`
    },
  }))
}
```



## 3、运行并调用

1、如果开发命令没在运行，重新启动： `pnpm dsh web --patch ./scratch-plugin/cordis.yml`

2、打开 http://127.0.0.1:3080，输入：`Use the greet tool to greet RUNOOB.`

![image-20260817173204657](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817173204657.png)

**3、关键点：`schema` 自动流入提示词**

> 工具的 name、description、parameters、output 会自动组装进模型提示词。模型"知道"有这样一个工具，就会在合适的时候调用。你不需要手写函数签名给模型，schema 就是模型看到的接口。





# 五、插件配置

> greet 工具把问候语写死在代码里，不同部署想换就得改代码。

## 1、导出 Config 类型与同名 schema

> 在插件中导出一个 Config 接口和同名的 Schemastery schema。默认值直接写在 schema 字段上。

```typescript
// 文件路径：scratch-plugin/src/my-plugin.ts
import type { Context } from '@deepseek-ai/cordis'
import Schema from '@deepseek-ai/schemastery'

export const name = 'my-plugin'

// Config 接口：定义插件接受哪些配置项
export interface Config {
  greeting: string    // 问候语
  maxRetries: number  // 最大重试次数
  verbose?: boolean   // 是否输出详细日志（可选）
}

// 同名的 Config schema：默认值写在这里
export const Config: Schema<Config> = Schema.object({
  greeting: Schema.string().default('Hello'),
  maxRetries: Schema.number().default(3),
  verbose: Schema.boolean().default(false),
})

// apply 的第二个参数就是校验后的配置
export function apply(ctx: Context, config: Config) {
  // 打印的是用户传入的值或 schema 默认值
  console.log(config.greeting)
}
```



## 2、在 `cordis.yml` 里传入配置

> 插件加载时，`Cordis` 会通过导出的 `schema` 校验配置，并填充未提供字段的默认值。上面的配置里没写 verbose，它会取 schema 默认值 false。

```yaml
# 文件路径：scratch-plugin/cordis.yml
- insert:
    - id: hello
      name: '/absolute/path/to/deepseek-harness/scratch-plugin/src/my-plugin.ts'
      config:
        greeting: 'Hi there, runoob!'
        maxRetries: 5
```



## 3、`Schema` 校验

> 需要更严格的校验时，用 `Schemastery` 表达约束。

- Schema 在插件加载时执行校验。

- 如果配置不合法，插件会加载失败，并给出明确错误信息。

```typescript
// 文件路径：scratch-plugin/src/validated-plugin.ts
import type { Context } from '@deepseek-ai/cordis'
import Schema from '@deepseek-ai/schemastery'

export const name = 'validated-plugin'

export interface Config {
  apiKey: string                 // 必填
  timeout: number                // 超时毫秒数
  mode: 'fast' | 'accurate'      // 只能取这两个值之一
}

export const Config = Schema.object({
  apiKey: Schema.string().required(),
  timeout: Schema.number().default(30000),
  mode: Schema.union(['fast', 'accurate']).default('fast'),
})

export function apply(ctx: Context, config: Config) {
  // config 已经过校验，类型安全
}
```

## 4、设计原则：无硬编码可调参数

> `Harness` 的约定是：凡是不同部署可能需要采用不同值的参数，都必须定义为配置字段。检验标准一句话：能否在 cordis.yml 中改变这个值，而不需要修改代码？如果能，就是合格的可调参数；如果不能，就要把它提成配置字段。

```typescript
// 错误：把超时时间硬编码
const TIMEOUT = 30000

// 正确：定义为配置字段，默认值仍由 schema 提供
export interface Config {
  timeoutMs: number  // 默认 30000
}
```



# 六、插件生命周期：`Fiber` 状态机

## 1、Fiber 是什么

> **Fiber**：一个插件实例在 Cordis 运行时中的状态容器。它记录该插件的生命周期状态，也是卸载时清理注册的依据。

每个被加载的插件都拥有一个 Fiber 作用域。

Fiber 可以理解成插件实例的执行单元，它承载插件从声明、加载、运行到卸载的全部状态。

框架通过 Fiber 知道一个插件现在处于什么阶段，以及接下来可以做什么。

## 2、Fiber 状态机

> Fiber 的状态按下面的顺序迁移，描述一个插件从加载到卸载的完整一生。

![image-20260817174435588](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817174435588.png)



| 状态          | 含义                     | 发生时机                                  |
| :------------ | :----------------------- | :---------------------------------------- |
| **PENDING**   | 已声明，但所需依赖未就绪 | 插件被加入上下文，inject 的服务还没准备好 |
| **LOADING**   | 依赖就绪，正在执行 apply | 所有必需服务就绪，框架调用 apply(ctx)     |
| **ACTIVE**    | 插件运行中               | apply 正常返回，注册生效                  |
| **FAILED**    | apply 抛出异常           | apply 执行过程中抛错，加载失败            |
| **UNLOADING** | 插件正在卸载并释放资源   | 依赖消失、被 dispose、或 HMR 触发卸载     |
| **DISPOSED**  | 已完全卸载               | 所有处置器执行完毕                        |





# 七、服务与依赖：Service 基类与类型声明



## 1、什么是服务

> **服务**：一个插件向其他插件公开的能力。
>
> 它占据一个稳定的 ctx.<key>（如 ctx.tools、ctx.llm、ctx.sessions），其他插件通过 key 查找服务，而不是导入具体实现。

在 `Harness` 中，`tools`、`llm`、`agents` 都是服务。服务是挂载在 ctx 上的命名能力，任何插件都可以提供服务，供其他插件使用。



## 2、使用服务：`inject` 声明（消费服务）

使用已有服务，在插件里声明 inject。     

框架保证：apply 执行时，inject 声明的服务已经全部就绪。 如果服务还没准备好，你的插件会等着，不会执行 apply。

```typescript
// 文件路径：scratch-plugin/src/use-tools.ts
// 声明依赖 tools 服务
export const inject = ['tools']

export function apply(ctx: Context) {
  // ctx.tools 一定存在且已就绪
  ctx.tools.register(/* ... */)
}
```

执行流程：

1. Cordis 读取本插件的 `inject = ['tools']`
2. 检查：有没有插件提供名叫 `tools` 的服务？
3. 如果 tools 服务还没初始化完成 → **本插件进入 pending 等待状态，apply 不会执行**。
4. 等 tools 服务完全就绪以后，才调用本插件`apply(ctx)`。
5. 在 apply 内部，安全访问`ctx.tools`。





## 3、提供服务：`Service` 基类（写一个自己的服务，给别的插件用）

> `Service` 是 `cordis` 提供的基类。继承它就可以快速写一个服务，挂载到`ctx.xxx`。，在构造函数里调用 **super(ctx, '服务名')** 注册命名服务。

```typescript
// 文件路径：scratch-plugin/src/metrics-service.ts
import { Service, type Context } from '@deepseek-ai/cordis'

// 写一个指标服务，继承Service基类
export default class MetricsService extends Service {
  // static inject：这个服务自身还要依赖别的服务！
  static inject = ['llm']

  constructor(ctx: Context) {
    // super(上下文, "服务名字")
    // 这句执行完，内核自动把实例挂载到 ctx.metrics
    super(ctx, 'metrics')
  }

  // 对外暴露给其他插件调用的方法
  record(event: string, value: number) {
    console.log("指标记录：", event, value)
  }
}
```





加载这个插件后，消费方就能通过 `ctx.metrics` 访问它。

```typescript
// 文件路径：scratch-plugin/src/use-metrics.ts
// 消费方声明依赖 metrics 服务
export const inject = ['metrics']

export function apply(ctx: Context) {
  // 调用服务方法：记录一次工具调用
  ctx.metrics.record('runoob_tool_call', 1)
}
```

流程完整走一遍：

1. `dsh` 启动，加载 MetricsService 所在插件；
2. 读取 `static inject=['llm']` → 等待 llm 服务就绪；
3. `llm` 准备完毕 → new MetricsService (ctx)；
4. `super(ctx,'metrics')`执行，挂载 `ctx.metrics`；
5. 另外一个插件声明了 `export const inject=['metrics']`；
6. 检测 `metrics` 服务就绪 → 执行该插件 `apply`；
7. 插件内部调用 `ctx.metrics.record()`。







## 4、类型声明：`declare module` 合并

使用 TypeScript 声明合并，让 ctx.metrics 拥有正确的类型。    

这样写代码时会有自动补全，编译期也能发现拼错服务名的错误。

```typescript
// 文件路径：scratch-plugin/src/metrics-service.ts
import { Service, type Context } from '@deepseek-ai/cordis'

// 声明合并：告诉 TypeScript，Context 上有一个 metrics 字段
declare module '@deepseek-ai/cordis' {
  interface Context {
    metrics: MetricsService
  }
}

export default class MetricsService extends Service {
  constructor(ctx: Context) {
    super(ctx, 'metrics')
  }

  record(event: string, value: number) {
    /* 实现省略 */
  }
}
```



# 八、服务隔离与作用域

> 同一个服务，怎么让不同插件组看到不同实例。这背后是 Cordis 的 服务隔离（service isolation）与 agent 作用域（scope）机制。

## 1、服务隔离：`isolate`

`cordis.yml` 支持服务隔离：同一个服务可以有多个实例，不同插件组看到不同实例。    

关键词是 `isolate` 配置，配合 `group` 插件把插件分成组。

```typescript
# 文件路径：scratch-plugin/cordis.yml
# 定义两个插件组 group-a 与 group-b，各自隔离一份 shell 服务
- id: group-a
  name: '@deepseek-ai/cordis-plugin-group'
  group: true
  isolate:
    shell: true              # 让本组内的 shell 服务独立实例化
  config:
    - name: '@deepseek-ai/dsh-bash-local'
      config:
        timeoutMs: 5000      # group-a 的 Bash 超时 5 秒
    - name: './src/plugin-a.ts'

- id: group-b
  name: '@deepseek-ai/cordis-plugin-group'
  group: true
  isolate:
    shell: true
  config:
    - name: '@deepseek-ai/dsh-bash-local'
      config:
        timeoutMs: 60000     # group-b 的 Bash 超时 60 秒
    - name: './src/plugin-b.ts'
```

![image-20260817182057465](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817182057465.png)



## 2、为什么需要隔离

不同任务对同一能力的需求可能完全不同。   

比如快速交互的任务希望 Bash 快速超时，而长任务希望给足时间。    

隔离让同一份服务按组配置、按组生效，而不是全局一刀切。



## 3、作用域：`scope`

`scope` 是按 `agent`（智能体）划分的注册单位。    

一项贡献（工具、提示词段、变量、限制、监听器）要么是全局的，要么归属于恰好一个 `scope key`。

| 概念            | 含义                                                         |
| :-------------- | :----------------------------------------------------------- |
| **scope**       | 按 agent 划分的注册单位；只有两层，采用扁平结构              |
| **scope key**   | scope 的不透明标识；一个活跃的 agent 就是其自身 scope 的 key |
| **agent.ctx**   | agent 的带作用域上下文，注册既有 scope 可见性，生命周期也绑定该 scope |
| **shadowing**   | 最具体者胜出的名称解析：带作用域的工具 / 片段 / 变量仅在自身 scope 内替换全局同名项 |
| **restriction** | tools.restrict 为单个 scope 过滤全局工具集合，多个 restriction 取交集组合 |
| **lineage**     | 以数据形式携带的父子关系事实（parentSession、delegationDepth 等），从不影响可见性 |

带作用域的注册不会向下继承给 `subagent`。

子树行为通过 `lineage` 数据表达，而不是通过 `scop`e 结构。







# 九、事件系统

> 插件之间怎么松耦合通信？事件就是 Cordis 的通信核心机制。`Harness` 大量使用事件来实现可扩展的扩展点，

## 1、基本用法

> 事件分监听与触发两端。

```typescript
// 监听事件：注册一个回调
ctx.on('event-name', (payload) => {
  // 处理事件
})

// 触发事件：广播给所有监听器
ctx.emit('event-name', payload)
```



## 2、五种分发模式

![image-20260817184359556](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817184359556.png)

| 模式                 | 分发方法      | 是否 await | 顺序       | 是否有返回值 | 典型场景                     |
| :------------------- | :------------ | :--------- | :--------- | :----------- | :--------------------------- |
| **emit** 广播        | ctx.emit      | 否         | 按注册顺序 | 否           | 通知类：所有监听者观察       |
| **bail** 短路        | ctx.bail      | 否         | 按注册顺序 | 是           | 决策类：第一个有效返回值胜出 |
| **serial** 顺序      | ctx.serial    | 是         | 按注册顺序 | 是           | 分阶段初始化：按序执行并等待 |
| **waterfall** 流水线 | ctx.waterfall | 否         | 按注册顺序 | 是           | 处理链：逐层包装下游返回值   |
| **parallel** 并行    | ctx.parallel  | 是         | 全部并行   | 否           | 扇出：多个监听者并行处理     |



# 十、能力三角色：`Definition` / `Provider` / `Consumer`

> 官方文档里经常出现 `Service Definition`、`Service Provider`、`Consumer `这三个词，它们合在一起构成一项能力的 seam，也就是可替换的能力接口。

## 1、三种角色各是什么

- `Service Definition` 只声明「有什么能力、长什么样」，不关心怎么实现。

- `Service Provider` 继承 `Definition` 的抽象类，填上具体行为。

- `Consumer` 面向模型，把能力包装成工具 `schema`，让模型能够调用。

| 角色                               | 负责什么                                                  | 以 Bash 为例                  |
| :--------------------------------- | :-------------------------------------------------------- | :---------------------------- |
| **Service Definition** 接口 + 类型 | 定义 `Cordis` 服务，以及请求 Request 和结果 Result 的类型 | dsh-shell（注册为 ctx.shell） |
| **Service Provider** 实现          | 真正实现该能力，通常针对一种运行环境                      | dsh-bash-local（本地执行）    |
| **Consumer** 面向模型的工具        | 把能力公开为模型可调用的工具                              | dsh-tool-bash（bash 工具）    |



## 2、一张图看懂 `seam` 三角色

> 三个角色都依赖 Definition，而 Provider 与 Consumer 互不依赖。

![image-20260817185650107](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817185650107.png)

## 3、为什么要拆成三个角色

拆分的第一个好处是提供方可替换。   

同一个 Service Definition 可以有多个提供方，通过 cordis.yml 选择。

```
# 文件路径：cordis.yml
# 本地执行
- name: '@deepseek-ai/dsh-bash-local'

# 想换提供方时，替换上面这一行即可。
# 换成下面这一行，就换成沙箱执行器：
# - name: '@deepseek-ai/dsh-bash-sandbox'
```



## 4、写一个可替换的能力

> 三步走：Service Definition（抽象类 + 类型）→ Service Provider（实现子类）→ Consumer（defineTool）。   
>
> 最后在 cordis.yml 里把 Provider 与 Consumer 组合加载。

案例：输入一段文本，输出全部大写

### 1）编写 Service Definition

`Service` `Definition` 声明能力本身：服务叫什么、怎么调用、请求与结果的类型是什么。    

它不包含任何实现逻辑，只有一个抽象方法和两个接口。    

抽象类 `MyCapService` 继承自 `Service`，通过 **`super(ctx, 'myCap')`** 注册为命名服务。    

`declare` `module` 声明合并让 `ctx.myCap` 拥有类型，这个技巧在第 14 篇讲过。

```typescript
// 文件路径：packages/my-cap/my-cap/src/index.ts
import { Service, type Context } from '@deepseek-ai/cordis'

// 声明合并：让 ctx.myCap 在 TypeScript 里有类型提示
declare module '@deepseek-ai/cordis' {
  interface Context {
    myCap: MyCapService
  }
}

// 抽象类：Definition 包只声明契约，不写实现
export abstract class MyCapService extends Service {
  constructor(ctx: Context) {
    super(ctx, 'myCap') // 注册为命名服务 ctx.myCap
  }

  /** Execute the capability. */
  abstract execute(request: MyCapRequest): Promise<MyCapResult>
}

// 请求类型：调用方必须提供 input
export interface MyCapRequest {
  input: string
}

// 结果类型：能力返回 output
export interface MyCapResult {
  output: string
}
```



### 2）第二步：编写 `Service Provider`

```typescript
// 文件路径：packages/my-cap/my-cap-local/src/index.ts
import type { Context } from '@deepseek-ai/cordis'
import { MyCapService, type MyCapRequest, type MyCapResult } from '@deepseek-ai/dsh-my-cap'

// 实现类：只依赖 Definition 包
class MyCapLocal extends MyCapService {
  async execute(request: MyCapRequest): Promise<MyCapResult> {
    // Local provider behavior.
    return { output: request.input.toUpperCase() }
  }
}

export const name = 'my-cap-local'

export function apply(ctx: Context) {
  // 把实现类作为插件加载，注册成 ctx.myCap 的实际服务
  ctx.plugin(MyCapLocal)
}
```

### 3）第三步：编写消费方 `Consumer`

```typescript
// 文件路径：packages/my-cap/tool-my-cap/src/index.ts
import type { Context } from '@deepseek-ai/cordis'
import { defineTool } from '@deepseek-ai/dsh-tools'

export const name = 'tool-my-cap'
// 依赖声明：tools 提供注册入口，myCap 提供服务实现
export const inject = ['tools', 'myCap']

export function apply(ctx: Context) {
  ctx.tools.register(defineTool({
    name: 'my_cap', // 面向模型的工具名
    description: 'Execute my capability.',
    // 参数 schema：模型按它生成参数
    parameters: {
      input: { type: 'string', required: true },
    },
    // 输出 schema 与渲染方式
    output: {
      schema: { type: 'string' },
      render: (_args, value) => [{ type: 'text', text: value }],
    },
    // 真正执行：调用能力服务
    async execute(args) {
      const result = await ctx.myCap.execute({ input: args.input })
      return result.output
    },
  }))
}
```



### 4）三角色结构与数据流

> 把三个包拼在一起，数据从模型出发，流经 Consumer、Definition，落到 Provider。

![image-20260817190604462](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817190604462.png)



### 5）在 cordis.yml 中组合

三个包写完只是零件，还要在 cordis.yml 里把 Provider 与 Consumer 一起加载。

- 加载 Provider，ctx.myCap 才有实现；

- 加载 Consumer，模型才有 my_cap 工具。

```typescript
# 文件路径：cordis.yml
# 先加载 Provider，让 ctx.myCap 有实现
- name: '@deepseek-ai/dsh-my-cap-local'

# 再加载 Consumer，让模型能用 my_cap 工具
- name: '@deepseek-ai/dsh-tool-my-cap'
```

加载顺序上，Cordis 会按依赖自动排序，因此 Provider 与 Consumer 谁先谁后并不关键。    

想换成别的 Provider 时，只改第一行，Consumer 保持不变。





# 十一、LLM 适配器：接入任意模型

## 1、LLM 适配器是什么

> 模型提供方不止 DeepSeek 一家，想把 dsh 接到自己的模型端点，就要写一个 LLM 适配器。

顶层是 agent-loop，它消费提供方无关的流式生成服务。    

中间是 ctx.llm 注册表，维护 LlmAdapter 的抽象契约。   

底层是各个适配器，分别对接不同的 API 格式。   

注册时用 **ctx.llm.registerAdapter(['my-provider'], adapter)** 绑定路由。

![image-20260817200158709](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260817200158709.png)



## 2、最小实现



```typescript
// 文件路径：src/my-llm-adapter.ts
import type { Context } from '@deepseek-ai/cordis'
import Schema from '@deepseek-ai/schemastery'
import { LlmAdapter, type GenerateOptions, type StreamChunk } from '@deepseek-ai/dsh-llm'

// 适配器：继承抽象类，实现 stream()
class MyAdapter extends LlmAdapter {
  private apiKey: string

  constructor(apiKey: string) {
    super()
    this.apiKey = apiKey
  }

  // stream() 返回异步生成器，逐片产出 StreamChunk
  async *stream(options: GenerateOptions): AsyncIterable<StreamChunk> {
    // 1. Convert options.messages to the provider format.
    // 2. Call the streaming API.
    // 3. Convert the response into StreamChunk values.
  }
}

// 插件配置：apiKey 与 providers 都必填
export interface Config {
  apiKey: string
  providers: string[]
}

// 同名的 Schemastery schema，加载时校验配置
export const Config: Schema<Config> = Schema.object({
  apiKey: Schema.string().required(),
  providers: Schema.array(Schema.string()).required(),
})

export const name = 'my-llm-adapter'
// 声明依赖 llm 服务，保证 ctx.llm 已就绪
export const inject = ['llm']

export function apply(ctx: Context, config: Config) {
  const adapter = new MyAdapter(config.apiKey)
  // 把提供方路由列表绑定到这个适配器
  ctx.llm.registerAdapter(config.providers, adapter)
}
```



## 3、GenerateOptions：适配器收到什么

`stream()` 接收仓库导出的 `GenerateOptions`。        

它包含模型、推理强度、对话历史、系统提示词、工具 schema、生成参数、停止序列与中止信号。       

完整字段以 @deepseek-ai/dsh-llm 导出的 TypeScript 类型为准。

| 字段              | 说明                                  |
| :---------------- | :------------------------------------ |
| **provider**      | 选择已注册的适配器                    |
| **model**         | 适配器拥有的模型 id，无需在启动时注册 |
| **messages**      | 对话历史                              |
| **system prompt** | 系统提示词                            |
| **tools**         | 工具 schema                           |
| **reasoning**     | 适配器拥有的推理强度 ID（可选）       |
| **signal**        | 中止信号，取消与资源释放用它完全停稳  |

## 4、在 cordis.yml 中使用

> 把适配器插件和 agent-loop 一起加载，并让 agent-loop 用新的 provider 与 model。



```typescript
# 文件路径：cordis.yml
# 加载适配器插件，apiKey 从环境变量读取
- id: my-llm
  name: './src/my-llm-adapter.ts'
  config:
    apiKey: !!js process.env.MY_API_KEY
    providers:
     - my-provider

# 配置 agent-loop 使用新适配器的 provider 与 model
- id: agent-loop
  name: '@deepseek-ai/dsh-agent-loop'
  config:
    agents:
      - id: main
        provider: my-provider
        model: my-model-v1
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
		id: 'AAAAAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 



<!-- Gitalk end -->