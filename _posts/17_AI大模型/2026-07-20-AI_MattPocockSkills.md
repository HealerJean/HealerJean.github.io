---
title: AI_MattPocockSkills
date: 2026-07-20 00:00:00
tags:
- AI
category:
- AI
description: AI_MattPocockSkills
---

**前言**

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)

 https://github.com/mattpocock/skills

 https://www.mattpocock.com/

 https://www.aihero.dev/



# 一、认识 `mattpocock/skills`

## 1、是什么：`Real Engineering`，不是 `Vibe Coding`

> **`mattpocock/skills`** 是 `Matt Pocock`（`Total TypeScript`、`AI Hero` 作者）**每天用来做真实工程的 `agent skills` 合集**。

**官方口号**：*My agent skills that I use every day to do real engineering — not vibe coding*

- 我的代理技能，我每天用它来做真正的工程，而不是感应编码   

**说明**：不是把需求丢给 `AI` 一把梭（`vibe coding`），而是把工程师的**方法论**——追问、领域建模、`TDD`、深模块、代码审查——**编码成一组可组合的 `skill`**，让 `AI` 在每个环节都按纪律做事

**核心哲学**：`skill` 不是"提示词模板"，而是**承载方法论的可执行单元**。小、易改、可组合——每一条 `skill` 都对应一个具名的工程实践





## 2、四种 `AI` 编码失败模式

> `README` 里作者明确列出 `AI` 编程的四种典型翻车模式，`mattpocock/skills` 就是对症下药

**说明：这四种失败不是 `AI` 智商不够，是**方法论缺失**。`Skills` 把每一种方法论固化成一条命令，`Agent` 每次调用都被强制走一遍纪律

| 失败模式 | 症状 | 对症 `skill` |
| :--- | :--- | :--- |
| **`Agent` 没做对** | 没对齐意图 | `grill-me` / `grill-with-docs`（追问对齐） |
| **`Agent` 太啰嗦** | 术语反复解释 | `CONTEXT.md` + `Ubiquitous Language`（共享词汇） |
| **代码不 `work`** | 生成即坏 | `/tdd` + `/diagnosing-bugs`（反馈回路） |
| **变成"泥球"** | 能跑但难维护 | `/to-spec` + `/improve-codebase-architecture`（持续设计） |





## 3、`Skills` 的双层设计：`User-invoked` `vs` `Model-invoked`

> `Matt` 把所有 `skill` 严格分为两类，这个划分是理解整套体系的**关键**

**作用**：编排层可以随场景组合，方法论层保持复用，两层各自演进不打架

| 类型 | 谁触发 | 命名习惯 | 承载什么 |
| :--- | :--- | :--- | :--- |
| **`User-invoked`** | 用户显式调用 | 名词/动词短语（`to-spec`、`implement`） | **编排**——决定"这一步该干什么" |
| **`Model-invoked`** | 模型自动调用 | 动名词/循环名（`grilling`、`tdd`） | **方法论**——承载"这件事该怎么干" |



## 4、安装

> 一段带过，两种安装方式二选一

- `plugin` 模式：更新方便，跟着上游走
- `skills.sh` 模式：把 `skill` 文件复制进项目，你可以按团队约定改写

```bash
# 方式一：Claude Code 插件（推荐——托管、自动更新）
npx skills@latest add mattpocock/skills

# 方式二：skills.sh 复制到项目（便于自行修改）
# 参考仓库 README：https://github.com/mattpocock/skills

# 安装后一次性初始化（配置 issue tracker、标签、文档路径）
/setup-matt-pocock-skills
```



```
│ ❯ ▾ ● Mattpocock Skills
│   ├─ ● ask-matt
│   ├─ ● code-review
│   ├─ ● codebase-design
│   ├─ ● diagnosing-bugs
│   ├─ ● domain-modeling
│   ├─ ● grill-me
│   ├─ ● grill-with-docs
│   ├─ ● grilling
│   ├─ ● handoff
│   ├─ ● implement
│   ├─ ● improve-codebase-architecture
│   ├─ ● prototype
│   ├─ ● research
│   ├─ ● resolving-merge-conflicts
│   ├─ ● setup-matt-pocock-skills
│   ├─ ● tdd
│   ├─ ● teach
│   ├─ ● to-spec
│   ├─ ● to-tickets
```



## 5、产物结构

| 层          | 目录                   | 修改策略           | 保留   |
| ----------- | ---------------------- | ------------------ | ------ |
| 需求 & 决策 | `CONTEXT.md` / `docs/` | 只追加             | ♾️      |
| 任务拆解    | `.github/issues/`      | 状态流转，内容不改 | 归档   |
| 实现        | `src/`                 | 正常重构           | 随代码 |
| 持久化      | `src/db/migrations/`   | 只增不改           | ♾️      |
| 测试        | `tests/`               | 随实现同步         | 随代码 |
| 审查        | `.reviews/`            | 只增不改           | ♾️      |
| 工具配置    | `.skills/`             | 极少               | ♾️      |

```
/grill-with-docs ──→ CONTEXT.md 追加 + ADR-002 新建
       ▼
/to-spec ──→ SPEC-042 新建 + Issue #42 新建
       ▼
/to-tickets ──→ Issue #43 #44 #45 #46 新建
       ▼
/implement #43
       ├──→ src/ 新建文件
       ├──→ migrations/ 新建（/tdd 先写测试）
       ├──→ .reviews/review-043.md
       └──→ Issue #43 → ✅ closed
       ▼
/implement #44 ... #45 ... #46（同上循环）
       ▼
开 PR → /code-review 终审 → review-pr-042-final.md → 合并
```





# 三、核心方法论

> `Skills` 只是方法论的**壳**，理解方法论本身，才能判断什么时候用哪个 `skill`



## 1、`Ubiquitous Language`（共享词汇）

### 1）是什么

> 领域驱动设计（`DDD`）的经典概念——**团队所有人（包括 `AI`）对同一件事用同一个词**。业务、代码、对话、文档，词汇必须一致。

- **反面案例**：产品说"订单"、代码里叫 `Order`、数据库叫 `t_purchase`、文档里叫"交易记录"——每次对话都要花一半时间对齐术语
- **正面案例**：全项目就叫 `Order`，任何人（含 `AI`）看到都指同一个东西



### 2）在 `Skills` 里的体现

- **`CONTEXT.md`** 承载词汇表（术语 + 定义 + 边界）
- **`grill-with-docs`** 在追问过程中会主动**发现新术语并写入 `CONTEXT.md`**
- **`domain-modeling`** 用边界场景**压测术语**——如果一个术语在极端场景下会产生歧义，说明需要拆分



### 3）为什么解决"啰嗦"

> **原理**：`AI` 每次对话都是"新员工"——没有共享词汇时，它需要重新推断你所说词汇的含义
>
> 有 `CONTEXT.md` 后，`AI` 每次开工先读词汇表，就像新员工先看新手手册——**术语一次讲清，之后直接用**





## 2、`CONTEXT.md`（共享上下文文档）

### 1）是什么

> 项目级的**共享大脑**——记录领域词汇、架构决策（`ADR`）、约束、非目标

- **位置**：项目根目录 `CONTEXT.md`
- **谁读**：所有 `skill` 在执行前会自动加载
- **谁写**：`grill-with-docs` 追问过程中增量更新；也可手写



### 2）典型结构

```markdown
# CONTEXT.md

## 领域词汇
- **Order**：用户下单后生成的实体，包含 items[]、status、payment_ref
- **Fulfillment**：从"下单"到"物流签收"的完整流程，不包含售后
- **Payment**：仅指支付网关侧的交易记录，与 Order.payment_ref 关联

## 架构决策（ADR）
### ADR-001：支付走 Stripe 而非自建
- 日期：2026-07-01
- 背景：合规成本、开发周期
- 决策：Stripe Tokenization，前端不接触卡数据
- 后果：Order.payment_ref 存 Stripe charge_id

## 约束
- 禁止在前端存储任何 PII
- 所有金额字段用整数分，不用浮点

## 非目标
- 本期不支持退款自动化
- 不做多货币
```







## 3、`Tracer Bullets`（曳光弹式垂直切片）

### 1）是什么

> 出自《`The Pragmatic Programmer`》——先打一发能命中的**曳光弹**（端到端最小可用切片），看清弹道，再逐步加大火力
>
> `Matt` 的原文：**《`Tracer Bullets: Keeping AI Slop Under Control`》**——认为这是控制 `AI` 生成质量的**核心武器**

- **反面案例**：让 `AI` 一次写完 20 个文件的完整功能——报错时不知道哪层错、修一个引出十个新问题
- **正面案例**：先跑通 `"用户点按钮 → API → 数据库 → 返回展示"` 的**最窄一条链**（一个，确认弹道后再横向扩展



### 2）在 `Skills` 里的体现

- **`to-tickets`** 的核心哲学就是"曳光弹式拆分"——每个 `ticket` 都是一发**端到端可验证**的曳光弹，不是按"技术层"横切
- **`tdd`** 强调**纵向切片**（`vertical slice`）——不要横向做完所有 `model` 再做所有 `controller`



### 3）为什么解决"不 `work`"

> **原理**：`AI` 的错误会**累积**——一次生成十个文件时，一个假设错误会污染所有下游
>
> 曳光弹每一发都是**独立完整**的，错了单发修正，不会污染整条战线



## 4、`Deep Modules`（深模块设计）

### 1）是什么

> 出自 `John Ousterhout`《`A Philosophy of Software Design`》——**小接口后面藏丰富行为**才是好模块
>

- **反面**：`shallow module`（小接口 + 小实现，只是转发）——徒增层次

- **正面**：`deep module`（小接口 + 大实现）——把复杂性关进模块内部



### 2）在 `Skills` 里的体现

- **`codebase-design`** 主动扫描代码，找出**能被"深化"的模块**——把散落的复杂性收进小接口后面
- **`improve-codebase-architecture`** 生成 `HTML` 报告，列出**"浅模块"清单**——按投入产出比排序



### 3）为什么解决"泥球"

> **原理**：泥球的本质是**每个模块都在暴露自己的内部复杂性**——调用方要处理十个参数、五种状态、三种异常
>
> 深模块把这些复杂性**藏到接口后**，调用方只看到"一个动作"



## 5、`Grilling`（追问循环）

### 1）是什么

> `Matt` 的原创方法论——**反复追问用户的方案或设计，直到所有决策分支被厘清**
>
> 出自 `Matt` 的文章《`My "Grill Me" Skill Went Viral`》以及《`9 Things People Get Wrong With /grill-me and /grill-with-docs`》

- **一句话**：`AI` 不要急着做，先把用户"烤"到讲清楚



### 2）追问三个层次

| 层次 | 目标 | 例问 |
| :--- | :--- | :--- |
| **`WHAT`** | 弄清楚要做什么 | "分享给谁？公开可访问还是需要密码？" |
| **`WHY`** | 弄清楚为什么做 | "为什么用 `UUID` 而不是短链？可读性有要求吗？" |
| **`WHAT IF`** | 弄清楚边界 | "如果分享方删除了原相册，链接还能访问吗？" |



### 3）在 `Skills` 里的体现

- **`grill-me`**：只追问方案设计
- **`grill-with-docs`**：追问 + 同步更新 `CONTEXT.md` + 生成 `ADR`
- **`grilling`**（`Model-invoked`）：以上两个背后的可复用追问循环



### 4）为什么解决"没做对"

> **原理**：`AI` 没做对，几乎总是因为**用户的输入里有隐含假设没说出来**
>
> `Grilling` 强制把假设摆到台面上——决策一旦显式化，`AI` 就不用猜



## 6、`TDD`（红-绿-重构 + 纵向切片）

### 1）是什么

> 经典 `TDD` 循环——先写失败测试（红）→ 让测试通过（绿）→ 优化实现（重构）
>
> 但 `Matt` 加了一个约束：**每个循环必须是纵向切片**——从 `UI` 到数据库贯通的一小条，而不是"先写完所有 `model`"



### 2）在 `Skills` 里的体现

- **`tdd`**（`Model-invoked`）：`implement` 在关键接缝处会自动触发
- **关键接缝**（`seam`）：外部依赖边界、状态变换点、并发点、算法复杂点



### 3）"关键接缝"而非"全部代码" 的哲学

> `Matt` 不是原教旨 `TDD`——**不要求每行代码都有测试**
>
> 只在**接缝**处强制 `TDD`：这些是 `bug` 最容易藏的地方，覆盖成本最低、`ROI` 最高



## 7、`User-invoked` `vs` `Model-invoked` 双层设计（回顾）

> 第一章已经介绍过，这里从**方法论视角**再看一遍

| 层 | 类比 | 演进方向 |
| :--- | :--- | :--- |
| `User-invoked` | 交响乐**指挥** | 随场景、团队、项目定制 |
| `Model-invoked` | 交响乐**演奏家** | 随方法论沉淀而复用 |

- 添加新工作流 → 加 `User-invoked skill`
- 沉淀新方法论 → 加 `Model-invoked skill`
- 两者永不混淆——`Model-invoked` 不做编排，`User-invoked` 不承载方法论



# 四、工作流全景与 `skills` 分组

## 1、五阶段工作流

> `Matt` 的 `skills` 不是散点式命令，而是围绕**一个五阶段工作流**组织的

```
┌──────────────────────────────────────────────────────────────────────────┐
│  Stage 1: 对齐（Alignment）                                                │
│  目的：确保 AI 理解用户意图和领域知识                                        │
│  产出：清晰的问题定义、更新后的 CONTEXT.md                                  │
│  Skills: ask-matt / grill-me / grill-with-docs / domain-modeling         │
└─────────────────────────────────┬────────────────────────────────────────┘
                                  ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  Stage 2: 规格（Spec）                                                     │
│  目的：把对齐后的意图沉淀为可执行的规格                                     │
│  产出：spec issue（发布到 issue tracker）                                  │
│  Skills: to-spec / wayfinder（超大工作分片）                               │
└─────────────────────────────────┬────────────────────────────────────────┘
                                  ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  Stage 3: 拆解（Tickets）                                                  │
│  目的：把 spec 拆成曳光弹式 ticket 树                                       │
│  产出：带依赖关系的 ticket 列表                                             │
│  Skills: to-tickets                                                       │
└─────────────────────────────────┬────────────────────────────────────────┘
                                  ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  Stage 4: 实现（Implement）                                                │
│  目的：按 ticket 逐个交付曳光弹                                             │
│  产出：可运行代码 + 测试 + 提交                                             │
│  Skills: implement（编排） / tdd / prototype / codebase-design（承载方法论）│
└─────────────────────────────────┬────────────────────────────────────────┘
                                  ▼
┌──────────────────────────────────────────────────────────────────────────┐
│  Stage 5: 收敛（Quality）                                                  │
│  目的：提交前质量把关、bug 定位、代码库健康度维护                            │
│  产出：干净的 PR、bug 修复报告、架构改进建议                                │
│  Skills: code-review / diagnosing-bugs / resolving-merge-conflicts        │
│         / improve-codebase-architecture                                   │
└──────────────────────────────────────────────────────────────────────────┘
```



## 2、`User-invoked` 一览表（编排层）

| `Skill` | 阶段 | 作用 |
| :--- | :--- | :--- |
| `ask-matt` | 元 | 路由器，根据情境推荐合适的 `skill` |
| `grill-me` | 对齐 | 追问用户方案设计，直到分支厘清 |
| `grill-with-docs` | 对齐 | 追问 + 更新 `CONTEXT.md` + 生成 `ADR` |
| `to-spec` | 规格 | 把对话整理成 `spec` 并发布到 `issue tracker` |
| `wayfinder` | 规格 | 超大工作规划，作为 `issue` 地图逐步破解 |
| `to-tickets` | 拆解 | 把方案拆分成带依赖关系的曳光弹 `ticket` |
| `implement` | 实现 | 按 `spec/tickets` 实施，关键接缝调 `tdd`，提交前跑 `code-review` |
| `triage` | 元 | 用状态机流转 `issue` 的三个 `triage` 角色 |
| `improve-codebase-architecture` | 收敛 | 扫描代码库找可"深化"的模块，`HTML` 报告 |
| `setup-matt-pocock-skills` | 元 | 每个 `repo` 首次使用时的一次性配置 |
| `handoff` | 效率 | 把当前会话压缩成交接文档 |
| `teach` | 效率 | 把当前目录作为教学工作区 |
| `writing-great-skills` | 元 | 编写 `skill` 时的原则参考 |



## 3、`Model-invoked` 一览表（方法论层）

| `Skill` | 阶段 | 承载方法论 |
| :--- | :--- | :--- |
| `grilling` | 对齐 | `grill-me` / `grill-with-docs` 背后的追问循环 |
| `domain-modeling` | 对齐 | 构建并压测领域模型 |
| `prototype` | 实现 | 一次性原型回答设计问题 |
| `tdd` | 实现 | 红-绿-重构 + 纵向切片 |
| `codebase-design` | 实现 | `Deep Modules` 落地——深化模块 |
| `diagnosing-bugs` | 收敛 | 复现 → 最小化 → 假设 → 插桩 → 修复 → 回归 |
| `code-review` | 收敛 | 两轴并行审查：编码规范 + `spec` 一致性 |
| `resolving-merge-conflicts` | 收敛 | 按 `hunk` 意图解决冲突，禁用 `--abort` |
| `research` | 元 | 后台 `agent` 做一手资料调研，产出带引用的 `Markdown` |



## 4、调用关系图

```
                    ┌─────────────┐
                    │  ask-matt   │  ← 不确定该做什么时先问它
                    │  （路由器）  │
                    └──────┬──────┘
                           ▼
              ┌────────────────────────────┐
              │       Stage 1: 对齐         │
              │  grill-me / grill-with-docs │
              │      + domain-modeling      │
              │      + CONTEXT.md 更新      │
              └────────────┬───────────────┘
                           ▼
              ┌────────────────────────────┐
              │       Stage 2: 规格         │
              │      to-spec → issue        │
              │  （超大工作先过 wayfinder）  │
              └────────────┬───────────────┘
                           ▼
              ┌────────────────────────────┐
              │       Stage 3: 拆解         │
              │       to-tickets            │
              │  → 曳光弹式 ticket 树        │
              └────────────┬───────────────┘
                           ▼
              ┌────────────────────────────┐
              │       Stage 4: 实现         │
              │       implement             │
              │  ├── 关键接缝 → tdd         │
              │  ├── 设计问题 → prototype   │
              │  └── 模块整理 → codebase-design
              └────────────┬───────────────┘
                           ▼
              ┌────────────────────────────┐
              │       Stage 5: 收敛         │
              │  提交前 → code-review       │
              │  遇 bug → diagnosing-bugs   │
              │  合并冲突 → resolving-merge-conflicts
              │  架构劣化 → improve-codebase-architecture
              └────────────────────────────┘
```



# 五、`Skills` 详解（按工作流阶段分组）

> 每个 `skill` 采用统一四段式：**是什么 / 何时用 / 命令 / 产物**

## 1、对齐阶段

### 1）`ask-matt`——路由器

> **是什么**：一个"我该用哪个 `skill`"的推荐器

- **何时用**：不确定当前情境该用哪个 `skill` 时——尤其是新手 / 陌生场景 / 多个 `skill` 都沾边时
- **命令**

```bash
/ask-matt 我想给已有的相册应用加分享功能，还没写代码，也不太确定要怎么设计
```

- **产物**：一段自然语言推荐，`+` 命令示例。例如：
  
  > 你现在还处于"想清楚要做什么"的阶段，建议先跑 `/grill-with-docs`——它会追问你设计意图，同时更新 `CONTEXT.md`。等你觉得清楚了，再 `/to-spec` 发到 `issue tracker`。
  
- **关键**：`ask-matt` 不做具体工作，只做**导航**——本身是编排层的入口



### 2）`grill-me`——追问方案设计

> **是什么**：一场**深度追问会话**，直到所有决策分支被厘清

- **何时用**：你有想法但不确定，想让 `AI` 帮你压测——**尚未写代码之前**
- **命令**

```bash
/grill-me 我想给相册加分享功能，用户能生成一个公开链接
```

- **`AI` 会怎么问**（示例）：
  - "分享链接的接收者需要登录吗？"
  - "如果分享方删除了相册，链接还能访问吗？"
  - "链接是长期有效还是有过期时间？"
  - "同一相册能生成多个不同链接吗？为什么？"
  - "能否撤销已发出的链接？"

- **产物**：对话记录 + 每个决策点的**明确回答**——这些回答后续会喂给 `to-spec`

- **原理回引**：这是 [`Grilling`](#5grilling追问循环) 方法论的入口层，专注**方案设计**追问



### 3）`grill-with-docs`——追问 + 建领域模型

> **是什么**：`grill-me` 的**加强版**——追问过程中**同步更新 `CONTEXT.md`** 和 `ADR`

- **何时用**：
  - 项目还没有 `CONTEXT.md`，需要初始化
  - 引入新的领域概念，需要沉淀词汇
  - 做重大架构决策，需要写 `ADR`

- **命令**：`/grill-with-docs 我想给相册加分享功能——涉及新概念"公开链接"和"访问令牌"`

- **产物**：
  - 对话记录（同 `grill-me`）
  - **`CONTEXT.md` 增量更新**——新增术语"公开链接（`PublicLink`）"、"访问令牌（`AccessToken`）"
  - **`ADR` 文件**——例如 `docs/adr/ADR-002-public-link-token-strategy.md`，记录"为什么用 `UUID` 而非短链"

- **关键**：这条 `skill` 是**共享词汇的注水口**——项目每一次重要设计对话，都会自动往词汇表加内容



### 4）`domain-modeling`——构建并压测领域模型

> **是什么**（`Model-invoked`）：用**边界场景**主动测试你的领域词汇是否稳固

- **触发时机**：`grill-with-docs` 在追问过程中自动调用——当发现某个术语可能有歧义时
- **怎么压测**：
  - 举**极端场景**——"如果一个用户同时是分享方和接收方呢？"
  - 举**边界值**——"如果链接刚生成就被撤销，接收方点击时看到什么？"
  - 举**冲突用例**——"如果两个术语在这个场景下指向同一个东西，需要合并吗？"

- **产物**：领域模型稳固性报告——列出需要拆分的术语、需要合并的术语、需要新增的术语



## 2、规格阶段

### 1）`to-spec`——对话整理成 `issue`

> **是什么**：把当前追问会话**沉淀成结构化 `spec`**，并**发布到 `issue tracker`**（`GitHub Issues`、`Linear` 等）

- **何时用**：
  - `grill-me` / `grill-with-docs` 之后——决策已经清晰
  - 需要向团队同步——`spec` 进入 `issue tracker` 便于跟踪

- **命令**：`/to-spec`

- **产物**：
  - 一份 `spec Markdown`——包含背景、目标、非目标、需求列表、边界场景、验收标准
  - 一个 `issue`——`spec` 内容作为 `issue body`
  - `issue` 链接返回给你

- **典型 `spec` 结构**

```markdown
# [SPEC] 相册分享功能

## 背景
用户希望把相册分享给非注册用户查看。

## 目标
- 用户能为任一相册生成公开链接
- 接收者无需登录即可查看
- 分享方能随时撤销

## 非目标
- 不支持密码保护
- 不支持有效期
- 不支持部分照片分享

## 需求
1. `POST /albums/:id/share` 生成链接
2. `GET /share/:token` 查看分享内容
3. `DELETE /share/:token` 撤销链接

## 边界场景
- 分享方删除相册后，链接返回 404
- 撤销后再访问，链接返回 410 Gone
- 未撤销的链接永久有效

## 验收标准
- 对以上 3 个 API 分别有集成测试
- CONTEXT.md 更新术语：PublicLink、AccessToken
```



### 2）`wayfinder`——超大工作规划

> **是什么**：**超出单次会话容量**的大型工作规划器——作为 `issue` 地图逐步破解

- **何时用**：一个 `spec` 大到不能一次做完——比如"重构整个鉴权系统"、"从 `REST` 迁到 `GraphQL`"
- **命令**：`/wayfinder` 我要把用户系统从 `session-based` 迁到 `JWT-based`

- **产物**：
  - 一张**依赖地图**——列出所有子工作、彼此依赖、推荐执行顺序
  - 每个子工作对应一个**待创建的 `spec issue`**
  - 一个"总路线图" `issue`——链接所有子 `issue`

- **和 `to-spec` 的区别**
  - `to-spec`：产出**一个** `issue`
  - `wayfinder`：产出**一张地图**——每个节点后续用 `to-spec` 生成 `issue`



## 3、拆解阶段

### 1）`to-tickets`——曳光弹式拆分

> **是什么**：把 `spec` 拆成**带依赖关系**的 `ticket` 树——每个 `ticket` 都是一发**端到端**曳光弹

- **何时用**：`spec issue` 完成后、开始实现前
- **命令**：`/to-tickets`  # 通常在 spec issue 页面运行

- **产物**：
  - 一系列子 `ticket`（子 `issue`）——每个都可独立完成
  - `ticket` 之间的**依赖关系**——用 `blocks` / `blocked by` 标注
  - 每个 `ticket` 都是**曳光弹**：从 `UI` 到数据库贯通一小条，可独立验证

- **反面示例：技术层横切（错误）**

```
❌ Ticket 1: 建所有数据表
❌ Ticket 2: 写所有 API
❌ Ticket 3: 写所有前端
❌ Ticket 4: 联调
（问题：前 3 个都做完，才知道联调不通）
```

- **正面示例：曳光弹纵切（正确）**

```
✅ Ticket 1: 生成分享链接（只做最简 case——一个字段、一个用户、无边界处理）
    └── 端到端：按钮 → POST /albums/:id/share → 存 DB → 返回 URL
✅ Ticket 2: 查看分享内容（只做最简 case——不考虑撤销）
    └── 端到端：URL 打开 → GET /share/:token → 查 DB → 展示
✅ Ticket 3: 撤销链接
✅ Ticket 4: 边界处理（相册删除、撤销后访问）
```

- **原理回引**：这是 [`Tracer Bullets`](#3tracer-bullets曳光弹式垂直切片) 方法论的直接落地



## 4、实现阶段

### 1）`implement`——按 `ticket` 实施

> **是什么**：拿到 `ticket` 后**逐个交付**的执行 `skill`——不是"一把梭"，而是**编排层**

- **何时用**：`to-tickets` 完成后
- **命令**

```bash
/implement  # 会自动读取当前 issue/ticket
```

- **执行过程**：
  1. 读取 `ticket` 描述 + 依赖 `ticket` 状态
  2. 读取 `CONTEXT.md`（共享词汇 + `ADR`）
  3. 生成代码——**关键接缝处自动触发 `/tdd`**
  4. 需要探索设计时——自动触发 `/prototype`
  5. 需要整理模块时——自动触发 `/codebase-design`
  6. **提交前自动跑 `/code-review`**——过了才允许提交

- **产物**：
  - 代码 + 测试
  - `git commit`（一个 `ticket` 一个提交，`commit message` 引用 `issue` 号）
  - 提交前的 `code-review` 报告

- **关键设计**：`implement` **不直接做工程决策**——遇到需要决策的点就调下层 `skill`



### 2）`tdd`——红-绿-重构

> **是什么**（`Model-invoked`）：`implement` 在**关键接缝**处触发的 `TDD` 循环

- **触发时机**：
  - 外部依赖边界（`API` 调用、数据库、`SDK`）
  - 状态变换点（枚举翻转、事务提交）
  - 并发点（锁、`goroutine`、`Promise.all`）
  - 算法复杂点（排序、匹配、递归）

- **执行流程**

```
1. 红：写一个失败测试——描述新行为
   ↓
2. 绿：写最少代码让测试通过——不追求优雅
   ↓
3. 重构：优化实现，测试仍然通过——保持行为不变
   ↓
4. 下一个纵向切片——继续下一个红绿重构
```

- **关键**：**不追求 100% 测试覆盖率**——只在接缝处强制 `TDD`
- **原理回引**：见 [`TDD`（红-绿-重构 + 纵向切片）](#6tdd红-绿-重构--纵向切片)



### 3）`prototype`——一次性原型

> **是什么**（`Model-invoked`）：**回答设计问题**的一次性代码——写完就丢

- **触发时机**：
  - `"两种 API 设计哪个更好用？"`——各写一个原型试
  - `"这个交互流程 UX 顺不顺？"`——写个可点原型
  - `"这个第三方库能满足需求吗？"`——写个最小集成 `POC`

- **产物**：
  - 一次性代码（**不进主分支**）
  - 一段结论——推荐哪个方案、为什么

- **关键**：`prototype` 产出的代码**从来不合并**——它的价值是**产出决策依据**，不是产出代码



### 4）`codebase-design`——`Deep Modules` 落地

> **是什么**（`Model-invoked`）：主动**深化**代码库中的模块——把复杂性收进小接口后面

- **触发时机**：
  - `implement` 生成代码后发现调用方要处理太多参数
  - `improve-codebase-architecture` 报告指出"浅模块"
  - 手动请求"帮我把这段代码整理成一个深模块"

- **执行流程**：
  1. 识别当前模块的**接口宽度**（参数数、返回类型复杂度、异常种类）
  2. 分析调用方在做什么——**共同模式**是什么
  3. 把共同模式**下沉到模块内部**——接口变窄，实现变深
  4. 更新调用方——参数变少、逻辑变简单

- **产物**：重构后的模块 + 变更前后对比说明
- **原理回引**：见 [`Deep Modules`](#4deep-modules深模块设计)



## 5、收敛阶段

### 1）`code-review`——双轴并行审查

> **是什么**（`Model-invoked`）：**两个 `sub-agent` 并行**审查代码——一个轴看编码规范，一个轴看 `spec` 一致性

- **触发时机**：
  - `implement` 提交前**自动**跑
  - 手动跑 `/code-review` 复审

- **两轴**

| 轴 | 关注 | 参考 |
| :--- | :--- | :--- |
| **编码规范** | 命名、复杂度、代码坏味道、测试覆盖 | 项目风格 + `CONTEXT.md` 里的约定 |
| **`spec` 一致性** | 代码是否实现了 `spec` 的每一条需求 | `spec issue` |

- **产物**：
  - 问题列表（分严重性）
  - 修复建议（`diff` 片段）
  - **过不了不允许提交**（在 `implement` 编排中）

- **和 `Spec Kit` `analyze` 的区别**
  - `speckit.analyze`：**只读**——只报告，不阻断
  - `mattpocock code-review`：**阻断门禁**——过不了不 `commit`



### 2）`diagnosing-bugs`——严谨调试循环

> **是什么**（`Model-invoked`）：**六步调试循环**——不允许"猜测式修复"

- **触发时机**：出现 `bug` / 测试失败 / 生产报警时

- **六步流程**

```
1. 复现（Reproduce）：稳定复现——不稳定就先补日志
   ↓
2. 最小化（Minimize）：把复现路径削到最短——通常能揭示 root cause
   ↓
3. 假设（Hypothesize）：写下"我认为原因是 X"——必须可证伪
   ↓
4. 插桩（Instrument）：加日志 / 断点 / 打印状态——验证假设
   ↓
5. 修复（Fix）：**只修 root cause，不修症状**
   ↓
6. 回归测试（Regression Test）：写一个测试，保证这个 bug 再也不会回来
```

- **反面案例**：`"重启就好了"` `"改个参数看看"` `"注释掉这行"`——都是猜测式修复，`mattpocock` 明确禁止

- **产物**：
  - 修复代码
  - 回归测试
  - `Bug` 分析报告——记入 `docs/bugs/` 便于未来查阅



### 3）`resolving-merge-conflicts`——按意图解决冲突

> **是什么**（`Model-invoked`）：**逐 `hunk` 按意图**解决 `merge` / `rebase` 冲突——**"`never --abort`"**

- **触发时机**：`git merge` / `git rebase` 时出现冲突
- **核心原则**：
  1. **禁用 `git merge --abort`** / `git rebase --abort`——放弃永远不解决问题
  2. **逐 `hunk` 读**——理解**双方的意图**再合并
  3. 冲突不只是**文本冲突**——可能是**语义冲突**（两边都编译通过，但业务逻辑打架）
  4. 合并完必须**跑测试**——`green` 之后才提交

- **执行流程**：
  1. `git status` 列出冲突文件
  2. 逐文件、逐 `hunk` 分析
  3. 每个 `hunk` 输出："左边想干什么、右边想干什么、合并后应该是什么"
  4. 应用合并结果
  5. 跑测试
  6. 提交



### 4）`improve-codebase-architecture`——`HTML` 报告扫描

> **是什么**：**扫描整个代码库**，找出**能被"深化"的模块**——用 `HTML` 报告呈现

- **何时用**：
  - 项目里程碑后——盘点技术债
  - 感觉代码库"变泥球了"但说不清哪
  - 新加入项目——快速摸底代码质量

- **命令**：`/improve-codebase-architecture`

- **产物**：
  - `HTML` 报告——列出"浅模块"清单、按投入产出比排序
  - 每个建议附**重构前后对比**
  - 建议做成 `ticket`（可一键接 `to-tickets`）

- **原理回引**：`Deep Modules` 方法论的**盘点工具**



## 6、辅助 `Skills`

### 1）工程类

- **`triage`**：用状态机流转 `issue` 的三个 `triage` 角色（`triager` → `refiner` → `estimator`）——`issue tracker` 治理
- **`setup-matt-pocock-skills`**：每个 `repo` 首次使用时的一次性配置——引导设置 `issue tracker`、标签体系、文档路径

### 2）效率类

- **`handoff`**：把当前会话压缩成**交接文档**——让另一个 `agent`（或另一个人）接手
- **`teach`**：把当前目录作为**有状态的教学工作区**——跨多次会话教用户新技能
- **`writing-great-skills`**：编写 `skill` 时的**词汇与原则参考**——写新 `skill` 前的必读
- **`research`**（`Model-invoked`）：以**后台 `agent` 形式**做高可信一手资料调研——产出带引用的 `Markdown`



# 六、实战场景

## 1、场景一：全新功能——从对话到 `PR`

> **项目背景**：给相册应用加"分享"功能，团队还没定型任何方案

### 1）完整链路

```bash
# ===== Stage 1: 对齐 =====
/ask-matt 我想给相册应用加分享功能，还没开始设计

# ask-matt 推荐先 grill-with-docs（有新术语，需更新 CONTEXT.md）

/grill-with-docs 用户希望把相册分享给非注册用户查看

# AI 追问 5-8 轮，覆盖：
# - 认证要求（无需登录）
# - 链接寿命（永久有效直到撤销）
# - 撤销后行为（410 Gone）
# - 分享方删相册后行为（404）
# - 同一相册多链接（支持）
# - 部分照片分享（不支持）
# 
# 同时更新：
# - CONTEXT.md 新增术语：PublicLink、AccessToken
# - docs/adr/ADR-002.md：UUID token 策略

# ===== Stage 2: 规格 =====
/to-spec

# 产出：
# - GitHub Issue #42 [SPEC] 相册分享功能
# - 包含背景/目标/非目标/需求/边界/验收

# ===== Stage 3: 拆解 =====
/to-tickets  # 在 issue #42 上运行

# 产出：4 个曳光弹式子 issue
# #43 生成分享链接（最简 case）
# #44 查看分享内容（最简 case，依赖 #43）
# #45 撤销链接（依赖 #43）
# #46 边界处理：删相册 404、撤销后 410（依赖 #44 #45）

# ===== Stage 4: 实现 =====
/implement  # 从 #43 开始

# implement 自动：
# - 读 CONTEXT.md
# - 生成 POST /albums/:id/share 路由
# - 关键接缝（DB 写入、UUID 生成）→ 自动触发 tdd
# - 提交前 → 自动触发 code-review
# - code-review 过 → commit → close #43

# 依次做完 #44 #45 #46

# ===== Stage 5: 收敛 =====
# code-review 已在 implement 中每次自动跑
# 全部 ticket close 后 → 开 PR

# 可选：一致性再扫一遍
/code-review  # 对整个 PR 复审
```



### 2）产物一览

| 阶段 | 产物 | 位置 |
| :--- | :--- | :--- |
| 对齐 | 追问对话 + `CONTEXT.md` 更新 + `ADR` | `CONTEXT.md`、`docs/adr/` |
| 规格 | `Spec issue` | `GitHub Issue #42` |
| 拆解 | `Ticket` 树 | `GitHub Issues #43-#46` |
| 实现 | 代码 + 测试 + `commit` | 分支 `feat/album-share` |
| 收敛 | `code-review` 报告 | 每个 `commit` 附件 |



### 3）关键提醒

| 规则 | 说明 |
| :--- | :--- |
| **`grill` 一定要充分** | 追问不到位，后面 `spec` / `ticket` / `implement` 都会带假设错 |
| **`to-spec` 之前 `grill` 完** | `to-spec` 不做追问，只做整理——它假设你已经清楚 |
| **`to-tickets` 拆纵切不拆横切** | 每个 `ticket` 必须端到端可验证 |
| **`implement` 不越权决策** | 遇到设计问题应触发 `prototype`，不要硬猜 |
| **`code-review` 不能跳过** | 提交前自动跑，过不了不 `commit`——这是纪律 |



## 2、场景二：既有代码库接入——先建 `CONTEXT.md`

> **项目背景**：接手一个存在两年的老项目，没有 `CONTEXT.md`，团队新人上手需要三天

### 1）完整链路

```bash
# ===== Step 1: 一次性配置 =====
/setup-matt-pocock-skills

# 引导设置：
# - Issue tracker（GitHub Issues / Linear）
# - 标签体系（bug/feature/chore/spec）
# - 文档路径（docs/adr/、docs/bugs/）
# - CONTEXT.md 位置（根目录）

# ===== Step 2: 建 CONTEXT.md ——追问式沉淀 =====
/grill-with-docs 这个项目是什么？核心领域概念有哪些？

# AI 会追问：
# - 项目做什么？为谁做？
# - 有哪些核心实体？彼此关系？
# - 有哪些历史决策还在生效？（追问已有代码，反推 ADR）
# - 有哪些约束和非目标？
# 
# 产物：
# - CONTEXT.md 从零到有——包含术语表 + ADR 索引 + 约束
# - docs/adr/ 补录关键历史决策

# ===== Step 3: 扫描代码库健康度 =====
/improve-codebase-architecture

# 产出 HTML 报告：
# - 浅模块清单（按投入产出比排序）
# - 每个建议附重构前后对比
# - 一键转 ticket

# ===== Step 4: 后续开发按标准流程 =====
# 有 CONTEXT.md 之后，所有 skill 都能读到共享词汇
# 新功能走 grill-with-docs → to-spec → to-tickets → implement
```



### 2）关键：`CONTEXT.md` 结构模板

```markdown
# CONTEXT.md

## 项目定位
[一句话——这个项目做什么、为谁做]

## 核心领域
### 实体
- **[实体1]**：定义、关键属性、边界
- **[实体2]**：...

### 流程
- **[流程1]**：从 X 到 Y 的完整链路，含状态机

## 架构决策
详见 docs/adr/
- ADR-001：[标题]
- ADR-002：[标题]

## 约束
- [硬约束1]
- [硬约束2]

## 非目标
- 本期不做 [X]
- 长期不做 [Y]

## 依赖
- 上游：[被谁调用]
- 下游：[调用谁]
```



## 3、场景三：修 `bug`——严谨调试循环

> **项目背景**：生产环境报"分享链接偶尔 500"，用户无法访问

### 1）完整链路

```bash
# ===== Step 1: 触发调试循环 =====
# implement 或者手动：
/diagnosing-bugs 分享链接接口偶尔返回 500，用户能复现但报警缺日志

# ===== Step 2: 六步循环 =====

# 【复现】
# AI: "先复现——把用户提供的 URL、时间戳、账户信息告诉我"
# 你：贴上 3 个用户报的 URL 和时间戳
# AI: 请求 3 次——1 次 500，2 次 200
# 结论：稳定性 33%，需要更多信息定位

# 【最小化】
# AI: "500 的这次有什么特殊？"
# 分析 request log：500 的这次 token 长度 32，200 的 36
# AI: "看起来 token 长度短的容易 500——最小复现：用一个 32 位 token 请求"
# 你：跑一遍 → 100% 500
# ✅ 稳定复现

# 【假设】
# AI: "假设：token 生成时长度不固定，某些 token 短 → 数据库唯一索引冲突？"
# 假设可证伪——我们能验证

# 【插桩】
# AI: 在 token 生成处加日志——打印每次生成的 token 和长度
# 跑 100 次：发现有 3% 的 token 长度是 32（其余 36）
# 根因浮现：随机源用了不安全的 Math.random()，某些字符会重复

# 【修复】
# AI: 换成 crypto.randomBytes——保证长度和熵
# ⚠️ 不是"改成 length ≥ 33 才存"（那是修症状不是根因）

# 【回归测试】
# AI: 加测试：生成 10000 次 token，断言全部 36 位、全部唯一
# 测试通过 → 修复合并
```



### 2）与"猜测式修复"的对比

| 猜测式（禁止） | 严谨式（`mattpocock`） |
| :--- | :--- |
| "重启一下 `pod` 试试" | 稳定复现前不下任何结论 |
| "把 `random` 改成 `crypto` 试试" | 先假设、再验证、再修复 |
| "长度不够就补 `padding`" | 修根因（不安全的 `random`）不修症状 |
| 修完就完事 | **必写回归测试**——保证这个 `bug` 再不回来 |



## 4、场景四：超大工作——`wayfinder` 分片

> **项目背景**：把用户系统从 `session-based` 迁到 `JWT-based`——涉及登录、鉴权中间件、`refresh` 机制、多端同步、老 `session` 兼容期

### 1）什么时候该用 `wayfinder` 而非 `to-spec`

| 特征 | 用 `to-spec` | 用 `wayfinder` |
| :--- | :--- | :--- |
| 需求范围 | 单一功能 | 跨模块、跨系统 |
| 预估工作量 | 一周内 | 数周甚至数月 |
| 是否需要**里程碑** | 否 | 是——要分批交付 |
| 是否有**风险点**需前置探索 | 否 | 是——需 `prototype` 打前站 |



### 2）完整链路

```bash
# ===== Step 1: 路线图 =====
/wayfinder 把用户系统从 session-based 迁到 JWT-based

# AI 追问：
# - 迁移周期？（3 个月）
# - 老 session 保留多久？（30 天过渡期）
# - 是否需要用户重新登录？（不）
# - 优先级：先前端还是先后端？

# 产出：
# 一张 GitHub Issue #100 [ROADMAP] Session → JWT 迁移
#   ├─ #101 [SPEC] JWT 签发与验证核心（依赖：无）
#   ├─ #102 [SPEC] Refresh token 机制（依赖：#101）
#   ├─ #103 [SPEC] 鉴权中间件双轨支持（依赖：#101）
#   ├─ #104 [SPEC] 前端 token 存储与刷新（依赖：#101 #102）
#   ├─ #105 [SPEC] 多端同步（依赖：#104）
#   └─ #106 [SPEC] 老 session 兼容期清理（依赖：#103 #104 #105）

# ===== Step 2: 每个子 spec 独立走完整流程 =====
# 从 #101 开始：
/to-spec  # 在 #101 上跑——把标题转成完整 spec
/to-tickets  # 拆 ticket
/implement  # 逐个 ticket 实现
# ... 循环

# ===== Step 3: 跨会话交接 =====
# 迁移周期长，需要跨多个会话——用 handoff
/handoff

# 产出交接文档：
# - 当前进度：#101 已完成、#102 进行中（剩 T05-T08）
# - 上下文：CONTEXT.md 关键 ADR
# - 风险点：#103 需要和运维协调
# - 下一个 agent 应该做：继续 T05
```



## 5、场景五：多人协作 / 交接

> **项目背景**：功能做到一半，你要请假两周，需要交给另一个人（或另一个 `agent`）

### 1）完整链路

```bash
# ===== Step 1: 交接 =====
/handoff

# AI 会问：
# - 交接对象是人还是 agent？
# - 剩余工作范围？
# - 有哪些"只有你知道"的隐含知识？

# 产出：handoff.md
```



### 2）交接文档模板

```markdown
# Handoff: [功能名]

## 交接对象
- 目标：另一个 agent / 另一个工程师
- 交接日期：2026-07-20

## 当前进度
- Spec issue：#42（已完成）
- Ticket 进度：
  - ✅ #43 生成分享链接
  - ✅ #44 查看分享内容
  - 🔄 #45 撤销链接（T03/T05 完成，还剩 T04）
  - ⬜ #46 边界处理

## 上下文
- CONTEXT.md：已更新 PublicLink、AccessToken 术语
- 关键 ADR：docs/adr/ADR-002-uuid-token.md

## 隐含知识（只有当前作者知道）
- Stripe webhook 有 5 秒延迟——测试撤销时要等
- 前端 token cache 有 60 秒 TTL——本地测试要清缓存
- 数据库迁移文件 20260720_share_table.sql 已跑，勿重跑

## 风险点
- #46 边界"撤销后再撤销"的行为在 spec 中未明确——需要先 grill

## 下一步
1. 跑 /implement 完成 #45 的 T04
2. 遇到 #46 前先 /grill-me——补齐边界定义
```



# 七、进阶技巧

## 1、自定义 `skill`——`writing-great-skills` 的原则

> `Matt` 在 `writing-great-skills` 里定义了写新 `skill` 的**准则**

### 1）命名

| 原则 | 反例 | 正例 |
| :--- | :--- | :--- |
| 动词优先 | `spec-utils` | `to-spec` |
| 目的性 | `analyzer` | `code-review` |
| 单一职责 | `dev-helper`（太宽） | `diagnosing-bugs`（很窄） |
| 避免中性名词 | `manager`、`helper`、`util` | 具体动作 |



### 2）承载什么

| `Skill` 类型 | 应该承载 | 不应该承载 |
| :--- | :--- | :--- |
| `User-invoked` | **编排**——决定"这一步该干什么" | 方法论细节 |
| `Model-invoked` | **方法论**——承载"这件事该怎么干" | 编排逻辑 |
| 两者共同 | 可复用的、命名清晰的动作 | 一次性的、业务专属的逻辑 |



### 3）边界

- **一个 `skill` 做一件事**——不要写"万能工具箱"
- **`skill` 之间靠调用组合**——不要在一个 `skill` 里塞多个方法论
- **`skill` 不承载业务**——业务知识放 `CONTEXT.md`，不放 `skill`



### 4）自查清单

写完一个 `skill` 问自己：
- [ ] 名字能直接读出**动作**吗？
- [ ] 是 `User-invoked` 还是 `Model-invoked`？边界清晰吗？
- [ ] 有没有把方法论和业务混在一起？
- [ ] 能否被另一个 `skill` 组合使用？
- [ ] 移除这个 `skill` 会影响哪些 `skill`？（依赖图）



## 2、和其他 `agent` 生态的兼容

### 1）`Claude Code` 插件模式

```bash
npx skills@latest add mattpocock/skills
```

- 优点：**自动更新**——跟着 `Matt` 上游走
- 缺点：不便修改——想改要 `fork`

### 2）`skills.sh` 复制模式

```bash
# 把 skill 文件复制进项目
# 具体命令参考仓库 README
```

- 优点：**便于自行修改**——按团队约定调整
- 缺点：**手动同步**——上游有更新要自己拉

### 3）怎么选

| 场景 | 推荐 |
| :--- | :--- |
| 个人项目 / 想跟随上游 | 插件模式 |
| 团队项目 / 有定制需求 | `skills.sh` 复制 |
| 想学习 `skill` 是怎么写的 | `skills.sh`——直接看源文件 |



## 3、常见坑

### 1）`grill` 用得太浅——问一轮就停

- **症状**：`AI` 追问了 3 个问题，你觉得"差不多了"就跑 `to-spec`
- **后果**：`spec` 里遗漏边界，`implement` 时才发现，返工成本高
- **对策**：一次 `grill` 至少让 `AI` 追问 5-8 轮——**边界场景**是关键。感觉"已经清楚"时再多问一层

### 2）`to-spec` 没经过 `grill` 就发

- **症状**：直接 `/to-spec 加分享功能` 一步到位
- **后果**：`spec` 是 `AI` 的猜测，不是你的想法
- **对策**：`to-spec` 前必须先 `grill`——**没有对齐就没有规格**

### 3）`implement` 跳过 `tdd`

- **症状**：关键接缝（`DB` 边界、并发点）没触发 `tdd`——`AI` 直接写实现
- **后果**：`bug` 藏在这些接缝里，`code-review` 也未必抓到
- **对策**：明确告诉 `implement`——"这个接缝是关键点，必须 `tdd`"

### 4）`code-review` 只跑一次

- **症状**：`implement` 时 `code-review` 过了，之后修改代码就不跑了
- **后果**：改动引入的问题没被审查
- **对策**：每次 `commit` 前都跑一次——`implement` 编排里默认就是这样

### 5）`CONTEXT.md` 不维护

- **症状**：`CONTEXT.md` 建了但不再更新
- **后果**：术语漂移、`AI` 每次重新猜、共享词汇失效
- **对策**：所有 `grill-with-docs` 都会自动更新——**关键是要**用 `grill-with-docs` 而不是 `grill-me`

### 6）`to-tickets` 拆成技术层横切

- **症状**：`Ticket 1: 建表`、`Ticket 2: 写 API`、`Ticket 3: 写前端`
- **后果**：前三个都做完，联调才发现不通——曳光弹的意义丧失
- **对策**：每个 `ticket` 必须**端到端可验证**——纵向切片



# 八、常见问题（`FAQ`）

| 问题 | 答案 |
| :--- | :--- |
| **必须先 `grill` 吗？** | 建议是。除非你**非常清楚**要做什么（很少见），否则跳过 `grill` 意味着让 `AI` 猜——`spec` 质量直接受影响 |
| **`CONTEXT.md` 和 `constitution.md` 什么关系？** | 不冲突。`constitution.md` 承载**开发原则**（`TDD` 必须、简洁性），`CONTEXT.md` 承载**领域知识**（术语、`ADR`）。两者可并存 |
| **`skills.sh` 和插件模式怎么选？** | 想跟随上游选**插件**；想改选 **`skills.sh` 复制**。默认推荐插件 |
| **大项目 `wayfinder` 什么时候用？** | 一个 `spec` 超过一周工作量、涉及跨模块、需要里程碑分批交付时——用 `wayfinder` 先出路线图 |
| **每个功能都要走完整流程吗？** | 否。`bug fix` / 一行文案改 / 小 `refactor` 直接 `implement`；**新功能 / 架构变更**才走完整链路 |
| **`prototype` 产出的代码合并吗？** | **不合并**。`prototype` 只产出**决策依据**——决策后重新走 `to-spec → to-tickets → implement` 生成正式代码 |
| **`diagnosing-bugs` 六步能跳吗？** | 不建议。每一步都在防**猜测式修复**。稳定 `bug` 至少走完复现→假设→修复→回归，不稳定 `bug` 必须走全六步 |
| **`code-review` 报告太长怎么办？** | 分严重性看——`critical` 必修、`major` 建议修、`minor` 记录。不必强求全部处理 |
| **`ask-matt` 什么时候用？** | 你**不确定该用哪个 `skill`** 时——它是入口路由器。用熟之后可以直接调具体 `skill` |
| **`handoff` 和 `wayfinder` 区别？** | `wayfinder` 是**空间**上的拆分（一个大工作拆多个 `spec`）；`handoff` 是**时间**上的交接（跨会话续接）。两者常配合使用 |
| **能给个人开源项目用吗？** | 完全可以。`Matt` 自己就是这样用的——注意 `skill` 依赖 `issue tracker`，纯本地项目可以退化用 `Markdown` 文件代替 |

