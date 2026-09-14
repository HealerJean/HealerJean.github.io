---
title: AI_skills_grill_me
date: 2026-05-30 00:00:00
tags: 
- AI
category: 
- AI
description: AI_skills_grill_me_and_grill_with_docs
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# `Grill Me` 与 `Grill With Docs`：`AI` 设计对齐利器

> **一句话定位**：`grill-me` 让 `AI` 反过来"烤问"你，直到设计树的每个分支都被走完；`grill-with-docs` 在烤问的同时把成果沉淀成 `CONTEXT.md` 术语表和 `ADR` 架构决策记录，是 `mattpocock/skills` 仓库中最受欢迎的两个技能。



# 一、开篇：为什么需要 `Grill Me`

## 1、核心痛点：`Misalignment`（对齐失败）

> "No-one knows exactly what they want"  ——  *The Pragmatic Programmer*, David Thomas & Andrew Hunt

`AI` 编码代理（如 `Claude Code`）最常见的失败模式不是能力不足，而是**对齐失败**——你以为它懂了，结果做出来完全跑偏。

| 失败表现       | 具体症状                                                     |
| -------------- | ------------------------------------------------------------ |
| 需求理解偏差   | 你说 A，`Agent` 做出 B，都能自洽却对不上                     |
| 边界条件遗漏   | 主流程 `OK`，一到异常/极端场景就崩                           |
| 术语层层歧义   | "订单"到底指下单动作、订单实体，还是订单聚合？双方各自解读   |
| 隐藏决策错误   | 某些默认决策没被提出讨论，事后返工代价巨大                   |
| 长周期上下文丢失 | 多轮对话后关键信息稀释，`Agent` 不断反复问已经答过的问题     |

## 2、解决思路：让 `AI` 反过来盘问你

`Agent` 不再急着写代码，而是**先做一场"设计访谈"**：把你的想法映射为一棵**设计树**（`design tree`），每一个决策会分叉出依赖它的子决策，然后**逐层追问**，直到没有任何未定分支为止。

```
你 → 提出模糊需求
       │
       ▼
Agent → 构建设计树 → 找出"前沿"决策 → 一次问一整轮
       │                                    │
       │◄─────── 你回答，前沿更新 ───────────┘
       ▼
（重复若干轮）
       │
       ▼
前沿为空 → 用户确认 → 执行方案
```

作者 `mattpocock`（`TypeScript` 布道者）在 `README` 中直言："这是我最受欢迎的两个技能，建议在**每次**要做变更前都跑一遍。"



## 3、技能生态定位

`grill-me` 与 `grill-with-docs` 都属于 `mattpocock/skills` 仓库，同一家族一共有 **3 个技能协同工作**：

| 技能               | 分类                | 调用方式           | 一句话定位                                                   |
| ------------------ | ------------------- | ------------------ | ------------------------------------------------------------ |
| `grill-me`         | `productivity`      | 用户触发           | 非代码场景的深度访谈，只对齐、不落文档                       |
| `grill-with-docs`  | `engineering`       | 用户触发           | 访谈 + 沉淀领域语言，落地 `CONTEXT.md` 与 `ADR`              |
| `grilling`         | `productivity`      | 模型触发（原语）   | 底层的可复用访谈机制，被上层技能内部调用                     |

> **架构规则**：`user-invoked` 可以调 `model-invoked`，但 `user-invoked` **不能**互调。所以 `grill-me` 与 `grill-with-docs` 各自独立，都通过 `grilling` 这层原语工作。



# 二、三个技能的关系：先分清 who is who

## 1、关系图

```
┌─────────────────────────────────────────────────────────────┐
│                     用户触发层（User-Invoked）                │
│                                                              │
│   /grill-me           /grill-with-docs                       │
│   ─────────           ────────────────                       │
│   仅访谈              访谈 + 领域建模                          │
│      │                       │                                │
│      │                       │                                │
└──────┼───────────────────────┼────────────────────────────────┘
       │                       │
       ▼                       ▼
┌─────────────────────────────────────────────────────────────┐
│                     模型触发层（Model-Invoked，原语）          │
│                                                              │
│   grilling                    domain-modeling                │
│   ────────                    ───────────────                │
│   设计树 + 前沿 + 轮次        术语表 + ADR + 边界检验          │
└─────────────────────────────────────────────────────────────┘
```

## 2、对比表

| 维度         | `grill-me`                       | `grill-with-docs`                                  |
| ------------ | -------------------------------- | -------------------------------------------------- |
| 分类         | `productivity`                   | `engineering`                                      |
| 是否落文档   | ❌ 不落                          | ✅ 落 `CONTEXT.md` + `ADR`                         |
| 底层调用     | `grilling`                       | `grilling` + `domain-modeling`                     |
| 适用阶段     | 单次讨论、临时对齐、点子推敲     | 长期项目、领域复杂、需要沉淀共享语言                 |
| 交付物       | 一份被对齐的设计草案             | 设计草案 + 项目术语表 + 关键 `ADR`                 |
| 投入成本     | 中（几轮问答）                   | 高（问答 + 文档更新）                              |
| 长期收益     | 一次性                           | 累积性（术语资产、命名一致、`Agent` `token` 更省）   |



# 三、核心概念：`Grilling` 底层原语

> `grill-me` 与 `grill-with-docs` 的 `SKILL.md` 都只有 3 行，真正的引擎在 `grilling`。先把这层讲透，上层就自然通了。

## 1、设计树（`design tree`）

任何一个需求/设计都可以被建模为一棵树：

```
根决策（例：做用户积分系统）
├── 子决策 A：积分从哪些行为获得？
│   ├── A1：下单金额换算规则？
│   └── A2：签到/评论等非交易行为是否加分？
├── 子决策 B：积分如何消耗？
│   ├── B1：兑换商品的库存策略？
│   └── B2：抵扣现金的比例上限？
└── 子决策 C：积分过期策略？
    ├── C1：是否过期？
    └── C2：过期前如何提醒？
```

关键性质：**子决策依赖父决策**。父没定，子就没法定。

## 2、前沿（`frontier`）

> `The frontier is every decision whose prerequisites are already settled.`

**前沿 = 所有前置已定的决策**，也就是"当下这一轮可以拿出来问用户的问题集合"。

```
第 1 轮前沿：{ 根决策的直接子决策 A、B、C }
      │
      │  用户回答后，A、B、C 的答案确定
      ▼
第 2 轮前沿：{ A1、A2、B1、B2、C1、C2 }
      │
      │  用户逐一回答
      ▼
第 N 轮前沿：{ } 空 → 会话结束
```

## 3、轮次（`rounds`）

每一轮的规则：

- **一次问完整个前沿**，不要一个一个挤牙膏
- **每题编号**，方便用户按编号回答
- **给出推荐答案**（`Agent` 有偏向就直说，用户可以省心确认）
- **等用户回复**，绝不代答



## 4、问题格式模板

```
❓ **Q1** — **<问题标题>**: <问题正文，可多段，可给选项>

➡️ <推荐答案>

---

❓ **Q2** — **<问题标题>**: <问题正文>

➡️ <推荐答案>
```

## 5、事实 vs 决策（最重要的一条纪律）

| 类型     | 定义                                       | 谁负责                                     |
| -------- | ------------------------------------------ | ------------------------------------------ |
| **事实** | 客观可查（代码里有、文件里有、工具能取到） | `Agent` 自己去查，`不要`麻烦用户           |
| **决策** | 主观权衡（业务偏好、风险取舍、优先级）     | 用户来定，`Agent` 只提建议                 |

> 一句话总结：`The decisions are the user's. The facts are the agent's job to find.`

**不要阻塞**：正在查的事实算未定前置，只有依赖它的下游问题等，其他前沿问题继续跑。



## 6、终止条件

> `The session is done when the frontier is empty.`

只有当设计树的每个分支都被走过、没有任何隐性假设时，才能结束。**在用户确认达成共识之前，绝不动手执行方案。**



# 四、`Grill Me` 详解

## 1、一句话定位

**非代码场景**下的深度访谈，只做设计对齐、**不落文档**。

## 2、`SKILL.md` 全文赏析（就 3 行）

```markdown
---
name: grill-me
description: A relentless interview to sharpen a plan or design.
disable-model-invocation: true
---

Call the Skill tool with "grilling".
```

- `disable-model-invocation: true` ：模型不能自动激活，**必须用户手动触发**
- 正文只有一句：转身去调 `grilling` 原语



## 3、6 大典型触发场景

| #    | 场景               | 触发示例                                         |
| ---- | ------------------ | ------------------------------------------------ |
| 1    | 提出新需求前       | "我想做一个签到功能，帮我 `grill-me`"            |
| 2    | 设计方案前         | "准备为订单加优惠券，先 `grill-me` 再动手"       |
| 3    | 架构评审前         | "微服务拆分方案我想过一遍再讨论"                 |
| 4    | 拆分模糊任务时     | "任务卡描述太粗，我要先明确边界"                 |
| 5    | 团队讨论对齐分歧时 | "我和后端各执一词，让 `AI` 帮我们理清"           |
| 6    | 大型重构启动前     | "把这块老代码重构成事件驱动，先 `grill-me`"      |

> **核心原则**：任何一次"我以为想清楚了但其实很多决策还没落地"的场景，都值得跑一次 `grill-me`。



## 4、安装命令

```bash
# 项目级安装
npx skills add mattpocock/skills@grill-me -y

# 全局安装（推荐，所有项目共享）
npx skills add mattpocock/skills@grill-me -g -y
```

或用 `Claude Code` 插件方式：

```bash
claude plugins install mattpocock-skills
```

## 5、典型会话流程

```
用户："/grill-me 我想给课程管理系统加通知"
    │
    ▼
Agent 分析需求，构建初始设计树
    │
    ▼
第 1 轮：问用户 3~5 个前沿决策（含推荐答案）
    │
    ▼
用户回答（回复编号 + 选择或补充）
    │
    ▼
Agent 更新设计树，重算前沿
    │
    ▼
第 2 轮：问下一批前沿决策
    │
    ▼
...（重复 N 轮）
    │
    ▼
前沿空 → Agent 总结完整设计 → 用户确认 → 结束
```

## 6、示例：为"课程管理系统"设计通知机制

**用户输入**：

> `/grill-me`  我想给课程管理系统加一个通知模块

**第 1 轮前沿**：

```
❓ **Q1** — **通知触发时机**：通知在哪些业务时点发出？可多选：
   (a) 新课程上架
   (b) 学员报名成功
   (c) 课时开始前 30 分钟
   (d) 学员评价被回复
   (e) 讲师回复答疑

➡️ 建议全选 (a)-(e)，实际业务价值都不低

---

❓ **Q2** — **通知渠道**：走哪些通道？
   (a) 站内消息
   (b) 邮件
   (c) 短信
   (d) 移动端推送 Push

➡️ 建议 (a) 站内 + (d) Push 优先，短信留给关键提醒（如课程开始前 30 分钟）

---

❓ **Q3** — **用户订阅偏好**：是否允许用户按类型开关通知？

➡️ 建议开放，最少提供"全部/关键/关闭"三档
```

**用户回复**：`Q1: (a)(b)(c)(e)；Q2: 采纳；Q3: 开放`

**第 2 轮前沿**（针对已定分支的下钻）：

```
❓ **Q4** — **通知失败重试策略**：邮件/短信送达失败怎么办？
   (a) 一次性发出，失败即弃
   (b) 有限重试（如 3 次，指数退避）
   (c) 死信队列 + 人工介入

➡️ 建议 (b)，对关键通知补 (c)

---

❓ **Q5** — **多设备去重**：同一用户 App + Web 同时在线，是否两端都推？

➡️ 建议就近推一端，避免打扰
```

**用户回复**：`Q4: (b)；Q5: 两端都推，但站内消息只标记一次已读`

**第 3 轮**：一般到这一轮就收敛了，`Agent` 会输出一份包含决策列表 + `mermaid` 时序图的完整设计草案，让用户最终确认。



## 7、边界与限制

- ✅ 适合：设计对齐、方案讨论、需求澄清
- ❌ 不适合：需要长期沉淀的项目术语、跨会话记忆的架构决策——那是 `grill-with-docs` 的活



# 五、`Grill With Docs` 详解

## 1、一句话定位

在盘问同时**沉淀领域语言**，把成果落地为项目的 `CONTEXT.md`（术语表）+ `ADR`（架构决策记录）。

## 2、`SKILL.md` 全文赏析（就 3 行）

```markdown
---
name: grill-with-docs
description: A relentless interview to sharpen a plan or design, which also creates docs (ADR's and glossary) as we go.
disable-model-invocation: true
---

Call the Skill tool twice, for "grilling" and "domain-modeling".
```

- 比 `grill-me` 多调一个原语：`domain-modeling`
- 两条流水线并行：一边追问、一边落文档



## 3、核心价值：共享语言（`Ubiquitous Language`）

> "With a ubiquitous language, conversations among developers and expressions of the code are all derived from the same domain model."  ——  Eric Evans, *Domain-Driven Design*
>
> > “使用一种通用的语言，开发人员之间的对话和代码的表达都来自同一个领域模型。”——Eric Evans，《领域驱动设计

| 收益           | 具体表现                                                     |
| -------------- | ------------------------------------------------------------ |
| 命名一致       | 变量、函数、文件、日志消息、注释都用同一套术语               |
| 代码库易导航   | `Agent`（和新人）能迅速定位领域概念对应的模块                 |
| `Token` 更省    | `Agent` 每次思考不必展开长解释，直接引用短术语，输出更精炼   |



## 4、`CONTEXT.md` 与 `ADR` 的边界

### 1）`CONTEXT.md`：纯术语表

> "`CONTEXT.md` should be totally devoid of implementation details."

- **只放**：领域名词、别名、简短定义、关键区别
- **不放**：实现细节、代码片段、部署配置、规约草稿

### 2）`ADR`：架构决策记录

只在**同时满足以下 3 个条件**时才写 `ADR`：

| 条件                | 判断标准                             |
| ------------------- | ------------------------------------ |
| 难以回滚            | 改这个决策要动大量代码/数据          |
| 缺乏上下文会诧异     | 后来者第一眼会觉得"为啥这么设计？"    |
| 真实权衡的结果       | 有 `A/B/C` 多方案对比，选了其中之一   |

如果只是常规选择、临时约定、可轻易调整的实现细节，**不要写 `ADR`**。

### 3）目录结构

**单 `context` 仓库**：

```
项目根/
├── CONTEXT.md          ← 全局术语表
├── docs/
│   └── adr/
│       ├── 0001-使用事件驱动.md
│       ├── 0002-积分不允许负值.md
│       └── ...
└── src/
```

**多 `context` 仓库**（微服务/大型单体）：

```
项目根/
├── CONTEXT-MAP.md       ← 指向各子域的索引
├── src/
│   ├── ordering/
│   │   └── CONTEXT.md   ← 订单子域术语
│   ├── inventory/
│   │   └── CONTEXT.md   ← 库存子域术语
│   └── payment/
│       └── CONTEXT.md   ← 支付子域术语
└── docs/adr/
```

**懒创建原则**：只在真的有东西可记时才建这些文件，不要提前铺目录。

## 5、`domain-modeling` 的 5 个动作

`grill-with-docs` 除了跑 `grilling` 之外，还会并行执行 `domain-modeling` 原语，具体做 5 件事：

| 动作               | 说明                                                         |
| ------------------ | ------------------------------------------------------------ |
| ① 挑战术语冲突     | 发现你用的词和现有 `CONTEXT.md` 里冲突时，立刻提出           |
| ② 打磨含糊表达     | 遇到 "`account`"、"`user`"、"`order`" 这种被过度加载的词，主动提议起个更精准的新名 |
| ③ 边界场景压力测试 | 编造反常/极端场景，逼你把边界说清楚                          |
| ④ 代码对照检验     | 打开 `codebase` 找相关代码，对照你的陈述看有没有矛盾         |
| ⑤ 原位更新文档     | 决策一敲定，**立即**更新 `CONTEXT.md` 或写 `ADR`，不攒到最后 |

**作用**:在拷问的过程中,**同步维护两份文档**:

- `CONTEXT.md` — 项目的**术语词汇表**(纯业务概念,不含实现细节)
- `docs/adr/*.md` — **架构决策记录**(记录"为什么这么做",仅在决策难以逆转 + 有真实权衡 + 未来人会困惑时才写)



## 6、安装命令

```bash
# 项目级安装（推荐，因为要写项目内的 CONTEXT.md）
npx skills add mattpocock/skills@grill-with-docs -y

# 全局安装
npx skills add mattpocock/skills@grill-with-docs -g -y
```

## 7、示例：为"电商订单"建立领域语言

**用户输入**：

> `/grill-with-docs`  我想给电商系统加一个订单模块

**第 1 轮开始前**，`Agent` 先扫代码库，发现已有 `Cart`、`Checkout` 相关代码。

**第 1 轮前沿**：

```
❓ **Q1** — **术语冲突**：现有代码里有 `Cart`（购物车）与 `Checkout`（结算），
   请问你要新增的 `Order` 与它们是什么关系？
   (a) `Cart → Checkout → Order` 依次转换（`Cart` 是可编辑草稿，`Order` 是不可变结果）
   (b) `Cart` 就是未提交的 `Order`（同一实体不同状态）
   (c) 其他

➡️ 建议 (a)，避免"一物多义"，术语更干净

---

❓ **Q2** — **订单状态机**：`Order` 有哪些核心状态？
   建议：`Created → Paid → Shipped → Completed | Refunding → Refunded`

➡️ 待你确认或修改
```

**用户回复**：`Q1: (a)；Q2: 加一个 Cancelled 终态`

**`Agent` 立即更新 `CONTEXT.md`**：

```markdown
## Order（订单）
用户结算 `Checkout` 通过后生成的**不可变**实体。核心状态：
`Created → Paid → Shipped → Completed`，异常路径 `Refunding → Refunded` 与 `Cancelled`。

**与相关术语区别**：
- `Cart`：可编辑的购物草稿，未进入结算
- `Checkout`：结算过程（临时会话），成功后生成 `Order`
- `Order`：结算完成后的不可变订单实体
```

**第 2 轮前沿**（边界压测）：

```
❓ **Q3** — **`Paid` 后能否修改地址**？如果商品已经发货一半，
   用户要求改配送地址，能否修改？

➡️ 建议：`Paid` 后禁止直接修改地址，改地址必须走"申请变更→客服审核"流程

---

❓ **Q4** — **部分退款**：一单多商品，只退其中之一，`Order.status` 该是什么？

➡️ 建议：`Order.status` 保持 `Completed`，但引入 `OrderItem.status = Refunded`
```

**`Agent` 判断这条决策"难以回滚 + 后来者会诧异 + 真实权衡"，写一份 `ADR`**：

```markdown
# ADR 0003：订单状态与商品项状态分离

## 状态（Accepted）
2026-05-30

## 背景
早期方案将退款状态直接反映到 `Order.status`（如 `PartiallyRefunded`），
但一单多商品时状态爆炸，且报表统计难以区分"完整成交"与"部分成交"。

## 决策
- `Order.status` 只反映**整单**生命周期
- 商品项级别的退款状态放在 `OrderItem.status`
- 报表以 `OrderItem` 为粒度统计

## 权衡
- ✅ 好处：`Order` 状态机简单稳定；报表精准
- ⚠️ 代价：查询整单退款情况要 `join OrderItem`；前端展示需聚合
```

**收敛**：`Agent` 输出完整设计 + 更新后的 `CONTEXT.md` + 新增的 `ADR`，用户确认后落库。



# 六、`Grill Me` vs `Grill With Docs`：选型指南

## 1、对比表

| 维度         | `grill-me`                     | `grill-with-docs`                                            |
| ------------ | ------------------------------ | ------------------------------------------------------------ |
| 投入成本     | 中（几轮问答）                 | 高（问答 + 文档更新）                                        |
| 产出物       | 一份被对齐的设计草案           | 设计草案 + `CONTEXT.md` 更新 + 可能的 `ADR`                  |
| 适用阶段     | 单次讨论、临时对齐、点子推敲   | 长期项目、领域复杂、多人协作、需要沉淀共享语言                 |
| 长期收益     | 一次性                         | 累积性（术语资产、命名一致、`Agent` `token` 更省）             |
| 学习曲线     | 无门槛                         | 需理解 `CONTEXT.md` / `ADR` 的边界与写法                      |
| 团队适配     | 个人独立探索也 `OK`             | 团队协作最受益                                               |

## 2、决策流程图

```
                触发一次设计对齐
                     │
                     ▼
        ┌───── 是否长期项目？ ─────┐
        │                          │
        否                        是
        │                          │
        ▼                          ▼
   /grill-me            ┌── 领域概念是否复杂？ ──┐
                        │                        │
                        否                      是
                        │                        │
                        ▼                        ▼
                   /grill-me            /grill-with-docs
                (轻量对齐即可)          (值得沉淀术语资产)
```

## 3、使用建议

- **单次探索 / 临时想法 / 短周期任务**：`/grill-me`
- **新项目从 0 到 1 / 复杂领域 / 多人协作 / 长期演进**：`/grill-with-docs`
- **已有 `CONTEXT.md` 想继续维护**：`/grill-with-docs`（可无缝增量）
- **同一天里的多个小决策**：可以先跑 `/grill-me`，只把真正难反悔的沉淀成 `ADR`



# 七、实战：一个完整会话样例（用户积分系统）

> 下面是一个 `grill-with-docs` 从触发到收敛的完整回放，帮你直观感受"设计树 → 前沿 → 轮次"是怎么落地的。

## 1、步骤 1：触发

```
用户：/grill-with-docs 我想给电商系统加一个用户积分系统
```

## 2、步骤 2：第 1 轮前沿（基础决策）

```
❓ **Q1** — **积分获取来源**：哪些行为可以获得积分？可多选：
   (a) 完成订单支付
   (b) 每日签到
   (c) 商品评价
   (d) 邀请新用户
   (e) 参与营销活动

➡️ 建议 (a)+(c)+(d)，签到与营销活动放到 v2

---

❓ **Q2** — **积分消耗方式**：
   (a) 兑换积分商城的商品
   (b) 下单时抵扣现金（如 100 积分 = 1 元）
   (c) 兑换优惠券
   (d) 以上皆是

➡️ 建议 (b) 优先，覆盖面最广

---

❓ **Q3** — **积分过期策略**：
   (a) 永不过期
   (b) 获得后 1 年过期
   (c) 每年年底清零

➡️ 建议 (b)，兼顾用户体验与财务负债可控
```

## 3、步骤 3：用户回答后，`CONTEXT.md` 立即落地

**用户回复**：`Q1: (a)(c)(d)；Q2: (b)；Q3: (b)`

**`Agent` 更新 `CONTEXT.md`**：

```markdown
## Point（积分）
用户通过特定行为获得的虚拟资产，可用于订单抵扣。

- **Earning Events（获取事件）**：`OrderPaid`、`ReviewPublished`、`InvitationAccepted`
- **Spending**：仅支持下单抵扣（`100 Point = 1 CNY`）
- **Expiry Policy**：获得后 12 个月过期，先获得先过期（FIFO）

**与相关术语区别**：
- `Coupon`：一次性使用、金额固定、来源单一（活动/客服发放）
- `Point`：可累积、可按比例抵扣、来源多元
```

## 4、步骤 4：第 2 轮前沿（边界场景压测）

```
❓ **Q4** — **订单退款后积分处理**：
   用户下单花了 100 积分抵扣，但退款了。这 100 积分怎么办？
   (a) 原路返还
   (b) 不返还（视作已消费）
   (c) 部分返还（按退款比例）

➡️ 建议 (a)，避免用户投诉，同时便于财务对账

---

❓ **Q5** — **积分与优惠券叠加**：
   同一订单能否同时用积分抵扣 + 优惠券？

➡️ 建议允许，但设置抵扣上限（如不超过订单金额 50%）

---

❓ **Q6** — **积分获取的时机**：
   订单支付成功立刻发积分，还是订单完成（收货）后发？

➡️ 建议订单完成后发，避免"下单获得积分 → 退款 → 白赚积分"套利
```

## 5、步骤 5：产出 `ADR`

用户对 `Q4/Q5/Q6` 回答完，`Agent` 判断"积分过期策略 + 积分发放时机"两条都是**难以回滚**的核心决策，写 `ADR`：

```markdown
# ADR 0007：积分先获得先过期（FIFO）与订单完成后发放

## 状态（Accepted）
2026-05-30

## 背景
积分是长期虚拟资产，直接影响财务负债与用户体验。
需明确「何时发放」与「过期顺序」两条底层规则。

## 决策
1. **发放时机**：订单进入 `Completed` 状态后异步入账，不在 `Paid` 时发放
2. **过期顺序**：同一用户账户内，按获得时间先后 FIFO 过期

## 权衡
- ✅ 好处：杜绝"下单-退款"套利；财务对账明确
- ⚠️ 代价：用户从下单到收到积分有延迟（通常 3~7 天）
- 💡 缓解：下单页展示"预计可获得 X 积分（订单完成后到账）"
```

## 6、步骤 6：收敛与确认

- 第 3 轮 `Agent` 尝试问技术实现相关问题，发现都是**事实**（"用什么表？用不用消息队列？"这类可以自己看代码回答），派子代理去查，不再打扰用户
- 前沿收敛为空 → `Agent` 输出：
  - ✅ 完整设计文档（含 `mermaid` 状态图 + 时序图）
  - ✅ `CONTEXT.md` diff
  - ✅ 新增的 `ADR-0007`
- 用户确认 → 会话结束 → 交给下一个 `Skill`（如 `/tdd`、`/implement`）真正动手



# 八、最佳实践与 FAQ

## 1、5 条最佳实践

| #    | 最佳实践                                     | 原因                                                         |
| ---- | -------------------------------------------- | ------------------------------------------------------------ |
| 1    | **每次变更前都跑一遍**                       | 对齐失败的代价永远高于一次访谈的时间                         |
| 2    | **只回答"决策"问题，让 `Agent` 自查"事实"**    | 事实问题占用你时间是浪费，让子代理去看代码/文件更快          |
| 3    | **一次专注一个前沿，不要跳轮**                 | 跨轮回答会打乱设计树的依赖顺序，最后互相矛盾                 |
| 4    | **长期项目优先用 `grill-with-docs`**           | 每次跑一次就往术语资产里存钱，一年后 `AI` 与你的默契爆表     |
| 5    | **定期回顾 `CONTEXT.md` 与 `ADR`**             | 决策会过时；每季度扫一次，剪除已废弃的条目，加"废弃"标记而不是删除 |

## 2、常见问题 `FAQ`

### 1）**Q：`grill-me` 和 `grill-with-docs` 能一起装吗？**

A：可以。两者互相独立，都通过 `grilling` 原语工作，装了都能用。**建议全装**：短会话用 `grill-me`，长项目用 `grill-with-docs`。

### 2）**Q：`CONTEXT.md` 与 `README` 的区别？**

A：完全不同的定位：

| 文件           | 定位                             | 读者                     |
| -------------- | -------------------------------- | ------------------------ |
| `README.md`    | 项目介绍、安装、快速上手         | 新访客、路人             |
| `CONTEXT.md`   | 领域术语表（只放"是什么"）       | 深度参与者、`AI Agent`   |
| `docs/adr/*`   | 关键决策的历史与理由             | 后来者、复盘者、`Agent` |

三者互补，不要合并。

### 3）**Q：会话卡在某个前沿怎么办？**

A：常见原因与对策：

- **场景 A：某个问题你自己也想不清** 
  - → 请 `Agent` 帮你列几个决策矩阵/参考案例，或先跳过这题（标注 `TBD`），继续问不依赖它的其他前沿

- **场景 B：术语冲突僵持** 
  - → 让 `Agent` 检索已有代码库 + 已有 `CONTEXT.md`，用现有的名字优先，实在冲突就起新名

- **场景 C：问题太抽象** 
  - → 让 `Agent` 出 2-3 个具体场景例子，你选一个作为讨论锚点




### 4）**Q：能否只用 `grilling` 不用外层？**

A：技术上可以（`Agent` 内部可以调），但 `grilling` 是 `model-invoked` 原语，**用户端不推荐直接调**。用 `grill-me` / `grill-with-docs` 才是标准入口，语义更清晰。



### 5）**Q：`grill-with-docs` 会不会一直在改 `CONTEXT.md`，把仓库改乱？**

A：不会。它遵循两条原则：

- **懒创建**：只在真的有内容可记时才创建 `CONTEXT.md`
- **原位更新**：只 diff 新增/修改条目，不会重写整个文件

而且每次改动都会在会话里明确告知你 diff，你可以拒绝或调整。



## 3、与其他 `Skill` 的组合玩法

`grill-me` / `grill-with-docs` 是 `AI` 编码工作流的**第一环**，接下来通常这样串：

```
/find-skills          ← 找工具
      │
      ▼
/grill-with-docs      ← 对齐设计 + 沉淀术语（本文重点）
      │
      ▼
/to-spec 或 /to-tickets  ← 把决策转成规约/工单
      │
      ▼
/tdd 或 /implement    ← 真正动手写代码
      │
      ▼
/code-review          ← 提交前 Review
      │
      ▼
/handoff              ← 交接给团队/PR
```

> **关键洞察**：越早在 `grill` 阶段把决策定清，后面的 `implement`、`review`、`handoff` 就越省事。前置一小时，后面省一天。



# 九、参考链接

- **`mattpocock/skills` 仓库**：https://github.com/mattpocock/skills
- **`Skills` 平台排行榜**：https://skills.sh/
- **`grill-me` 详情页**：https://skills.sh/mattpocock/skills/grill-me
- **`grill-with-docs` 详情页**：https://skills.sh/mattpocock/skills/grill-with-docs
- **`Domain-Driven Design`（Eric Evans）**：领域驱动设计经典著作，`ubiquitous language` 出处
- **`The Pragmatic Programmer`（Thomas & Hunt）**：程序员修炼之道，`"No-one knows exactly what they want"` 出处
- **`Find Skills` 使用指南**（本系列上一篇）：`AI_skills_find_skill.md`











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
