---
title: AI_Superpowers
date: 2026-05-29 00:00:00
tags: 
- AI
category: 
- AI
description: AI_Superpowers
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

https://github.com/jnMetaCode/superpowers-zh





# 一、`Superpowers` 

## 1、认识 `Superpowers`：给 AI 装上工程纪律

### 1）是什么：不是增强智能，是强化纪律

基于 `Anthropic Agent Skills` **技能工作流框架（Skills Framework）** ，或者说它是**一套完整的 AI 开发方法论 + 可组合技能库**，它通过拦截 `Claude Code` 的关键决策点，将单次对话转变为结构化的软件工程流程    

开发时，强制遵循：**头脑风暴 → 方案设计 → 编写计划 → 执行开发 → TDD 测试 → 代码审查 → 系统化调试**



### 2）安装->`Claude Code`（官方推荐，体验最佳）

> `Superpowers` 支持几乎所有主流 AI 编码 Agent，以下是最常用平台的安装步骤，安装完成后均需**新开一个会话**才能生效。

```
/plugin install superpowers@claude-plugins-official
```

1. Install for you (user scope) —— 🌟 个人日常开发首选

- **作用范围**：对你当前账号下的**所有新建/现有项目全局生效**15。

- **配置位置**：存放在个人全局目录（如 `~/.claude/settings.json`）

  - ```
    ~/.claude/plugins/cache/claude-plugins-official/superpowers
    ```

- **适用场景**：适合个人开发者。一次安装，终身复用，不需要每个项目都重新配置，且不会影响你的协作者15。

2. Install for all collaborators on this repository (project scope) —— 👥 团队协作首选

- **作用范围**：仅对**当前仓库**生效，且**所有协作者都会自动强制安装**14。
- **配置位置**：存放在项目目录下的 `.claude/settings.json`，**会提交到 Git 仓库中**45。
- **适用场景**：适合团队统一规范。确保团队里的每个人都使用相同的插件配置和开发标准15。

3. Install for you, in this repo only (local scope) —— 🧪 临时实验首选

- **作用范围**：**仅对当前仓库生效**，且**仅限你本机使用**14。
- **配置位置**：存放在项目目录下的 `.claude/settings.local.json`，**不会提交到 Git 仓库**5。
- **适用场景**：适合只想在单个项目临时测试插件效果，或者不想影响其他项目配置、也不想打扰团队其他成员的情况



## 2、架构设计

### 1）技能全景

![image-20260623132857518](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260623132857518.png)



### 2）技能介绍

| 阶段   | 技能                                 | 触发时机                                 | 一句话概括                                                   |
| :----- | :----------------------------------- | ---------------------------------------- | :----------------------------------------------------------- |
| 🧭 入口 | **`using-superpowers`**              | 任何对话开始时                           | 全家桶的使用说明书                                           |
| 💡 需求 | **`brainstorming`**                  | 任何创造性工作前                         | 把模糊想法变成清晰 spec                                      |
| 📋 规划 | **`writing-plans`**                  | 有 spec/需求后、编码前                   | 把 spec 变成可执行计划                                       |
| 🔨 执行 | **`executing-plans`**                | 有写好的实现计划要执行时                 | 按计划分步推进，带检查点                                     |
| ⚡ 加速 | **`subagent-driven-development`**    | 在当前会话中执行有独立任务的实现计划     | 用 `subagent` 驱动开发：按任务分派→实现→审查→修复循环→最终审查 |
| 🚀 加速 | **`dispatching-parallel-agents`**    | 面临 2+ 个独立任务可并行处理时           | 将独立问题分派给并行 `subagent` 同时处理，加速多故障排查     |
| ✅ 质量 | **`test-driven-development`**        | 实现任何功能或修复 `bug` 前              | 严格的 `RED`-`GREEN`-`REFACTOR` 循环：先写测试→看它失败→最小实现→重构 |
| 🐛 质量 | **`systematic-debugging`**           | 遇到任何 `bug`、测试失败或异常行为时     | 四阶段系统调试法：根因调查→模式分析→假设验证→实现修复        |
| 👀 质量 | **`requesting-code-review`**         | 完成任务、实现重大功能或合并前           | 让 `Claude` 审查你的代码                                     |
| 🔧 质量 | **`receiving-code-review`**          | 收到代码审查反馈后、实施建议前           | 让 `Claude` 帮你改审查意见                                   |
| 🏁 收尾 | **`finishing-a-development-branch`** | 实现完成、测试通过、需要决定如何集成时   | 合并、清理、提交一条龙                                       |
| 🧹 收尾 | **`using-git-worktrees`**            | 开始需要隔离的功能开发或执行实现计划时   | 确保在隔离工作区工作：检测已有隔离→原生工具→git `worktree` 回退 |
| ✅收尾  | **`verification-before-completion`** | 声称工作完成/修复/通过前                 | 完成前最后一轮验证                                           |
| 🔌 扩展 | **`writing-skills`**                 | 创建新`skill`、编辑`skill`或部署前验证时 | 用 `TDD` 方法论创建 `skill`：基线测试→编写 `skill`→验证合规→闭环漏洞 |



### 3）调用关系图

```

using-superpowers (入口)
    │
    ├── brainstorming (设计)
    │       │
    │       └── writing-plans (计划)
    │               │
    │               ├── subagent-driven-development (执行-推荐)
    │               │       ├── using-git-worktrees (隔离)
    │               │       ├── test-driven-development (TDD)
    │               │       ├── requesting-code-review (审查)
    │               │       └── finishing-a-development-branch (完成)
    │               │
    │               └── executing-plans (执行-备选)
    │                       ├── using-git-worktrees (隔离)
    │                       └── finishing-a-development-branch (完成)
    │
    ├── systematic-debugging (调试)
    │       └── test-driven-development (TDD)
    │
    ├── verification-before-completion (验证)
    │
    ├── dispatching-parallel-agents (并行)
    │
    ├── receiving-code-review (接收审查)
    │
    └── writing-skills (元技能)
```



### 4）分类（5类）

| 类别       | 技能                                                         | 作用                                                       |
| :--------- | :----------------------------------------------------------- | :--------------------------------------------------------- |
| **入口**   | using-superpowers                                            | Skill 系统使用规范，"有 1% 可能就调用"                     |
| **流程**   | brainstorming → writing-plans → executing-plans              | 从想法到设计到计划的完整流程                               |
| **纪律**   | TDD、systematic-debugging、verification-before-completion    | 三个"铁律"技能，分别管测试先行、根因调试、验证后才声称完成 |
| **执行**   | subagent-driven-development、dispatching-parallel-agents     | 单任务分派 vs 多任务并行分派                               |
| **工具**   | using-git-worktrees、requesting/receiving-code-review、finishing-a-development-branch | 工作区隔离、代码审查、分支收尾                             |
| **元技能** | writing-skills                                               | 用 TDD 方法创建新 skill                                    |

核心工作流：**brainstorming → writing-plans → subagent-driven-development → finishing-a-development-branch**，每个环节都有对应的纪律技能保驾护航。



## 3、工作流

```
using-superpowers           ← 入口，确认技能就位
    ↓
brainstorming               ← "我要做头像上传" → 输出 spec
    ↓
writing-plans               ← spec → 输出 6 步计划
    ↓
executing-plans             ← 按计划执行，每步确认
    ├── subagent-driven-development   ← 后端的 3 步可以并行
    ├── dispatching-parallel-agents   ← 调研 + 迁移 + 测试同时跑
    └── test-driven-development       ← 每个模块先写测试
    ↓
requesting-code-review      ← 审查全部代码
    ↓
receiving-code-review       ← 修改审查意见
    ↓
systematic-debugging        ← 测试挂了？二分法定位
    ↓
verification-before-completion  ← 最后检查
    ↓
finishing-a-development-branch  ← 合并提交，打完收工
```

核心工作流：**brainstorming → writing-plans → subagent-driven-development → finishing-a-development-branch**，每个环节都有对应的纪律技能保驾护航。



### 1）新功能开发

```
brainstorming → writing-plans → using-git-worktrees → subagent-driven-development

    [每个 task: TDD + 代码审查] → finishing-a-development-branch
```



### 2）Bug 修复

```
systematic-debugging → test-driven-development → verification-before-completion
```



### 3）多故障排查

```
dispatching-parallel-agents (并行分派) → 审查整合 → verification-before-completion
```



### 4）创建新 Skill

```
writing-skills (TDD for docs): RED(基线) → GREEN(写skill) → REFACTOR(闭环)
```







# 二、标准工作流

## 1、`using-superpowers` — `Skill` 系统使用指南

**类型：** 入口技能（所有对话自动加载）

**核心理念：** 如果有哪怕 1% 的可能某个 skill 适用，你就必须调用它。

**关键内容：**

- **指令优先级：** 用户显式指令 > `Superpowers skill `> 默认系统提示
- **技能调用规则：** 在任何响应或操作之前，先调用相关 skill。即便只有 1% 的可能性也必须调用检查
- **技能优先级：** 流程类 `skill` 优先（`brainstorming`、`systematic-debugging`），实现类 `skill` 其次

**红旗行为（自我检查清单）：**

| 想法                 | 现实                           |
| -------------------- | ------------------------------ |
| "这只是个简单问题"   | 问题也是任务，检查 `skil`l     |
| "我需要更多上下文"   | 检查 `skill` 在澄清问题之前    |
| "让我先探索代码库"   | `Skill` 告诉你如何探索，先检查 |
| "这不需要正式 skill" | 如果 `skill` 存在就使用它      |
| "我记得这个 skill"   | `Skill` 会演进，读当前版本     |

**流程图：** 收到用户消息 → 是否有 `skill` 可能适用？→ 是：调用 skill → 宣布"使用 [skill] 来 [目的]" → 有检查清单？→ 是：为每项创建 todo → 遵循 skill 执





## 2、`Brainstorming`：头脑风暴，设计先于代码

> 是一个"从想法到设计"的协作技能，帮你把一个模糊的想法变成一份完整、可落地的设计文档

**类型：** 流程技能（创造性工作前必须使用）

**触发时机**：需求模糊、想法不完整时   

**激活时机：**在编写代码前激活，你有一个想法，但还没想清楚怎么做 → Brainstorming 帮你一步步想明白

**核心理念：** 不经设计不实现。即使是最简单的项目也必须先呈现设计方案并获得用户批准。

- 苏格拉底式提问法，帮你把模糊的想法拆解成清晰的需求，确认边界、约束、隐含需求，避免开发到一半才发现理解偏差。
- 要求在编写代码前，先明确需求并形成设计文档。设计是“给人类看的”，代码是“给机器执行

**典型输出**：结构化的需求清单、功能边界、非功能要求、风险点。

**反模式：** "这太简单了不需要设计"——简单项目恰恰是最容易忽略假设导致浪费的地方

**关键步骤：**

| 步骤              | 做什么                           | 为什么                           |
| :---------------- | :------------------------------- | :------------------------------- |
| 1️⃣ 探索项目上下文  | 看代码、文档、最近提交           | 理解现状再提方案                 |
| 2️⃣ 提问澄清        | 一次只问一个问题                 | 不让你被信息淹没                 |
| 3️⃣ 提出 2-3 个方案 | 带权衡和推荐                     | 你选最合适的，而不是第一个想到的 |
| 4️⃣ 分段呈现设计    | 每段确认后再往下                 | 避免方向跑偏                     |
| 5️⃣ 用户审批设计    | 你说了算                         | 不满意就改，满意再往下           |
| 6️⃣ 写设计文档      | 保存到 `docs/superpowers/specs/` | 留档可追溯                       |
| 7️⃣ 自检设计文档    | 查占位符、矛盾、歧义             | 质量兜底                         |
| 8️⃣ 你审阅文档      | 最终确认                         | 你的签字                         |
| 9️⃣ 转入实现计划    | 调用 `writing-plans` 技能        | 设计→实现的无缝衔接              |

**设计原则：**

| 原则                        | 含义                                 |
| :-------------------------- | :----------------------------------- |
| **一次只问一个问题**        | 不用同时想太多                       |
| **优先给选项**              | 选择题比填空题好回答                 |
| **`YAGNI`（你不会需要它）** | 砍掉不必要的功能                     |
| **总是先提 2-3 个方案**     | 避免一上来就锁死一个方向             |
| **分段确认**                | 每段设计你确认后再继续，随时可以回头 |





## 3、`writing-plans` — 编写实现计划

**类型：** 流程技能（有 `spec/`需求后、编码前使用）

**核心理念：** 写出详尽计划，让一个对你代码库零上下文、品味存疑的工程师也能执行。

**计划文档结构：**

```markdown
# [功能名称] 实现计划

> **给 agentic worker：** 必须使用 superpowers:subagent-driven-development 或 superpowers:executing-plans

**目标：** [一句话描述]
**架构：** [2-3 句话]
**技术栈：** [关键技术/库]

## 全局约束
[项目级需求，每条一行，从 spec 原文照抄]

---

### Task N: [组件名]
**文件：** 精确路径
**接口：** 消费什么、产出什么
- [ ] 步骤1：写失败测试（含代码）
- [ ] 步骤2：运行确认失败
- [ ] 步骤3：写最小实现（含代码）
- [ ] 步骤4：运行确认通过
- [ ] 步骤5：提交
```

**关键规则：**

- **任务粒度：** 每步 2-5 分钟（"写失败测试"是一步，"运行确认失败"是一步）
- **禁止占位符：** 不许出现 TBD、TODO、"添加适当错误处理"、无实际测试代码的"为以上写测试"等
- **每步含完整代码：** 如果步骤要改代码，展示代码
- **精确命令+预期输出：** 每步的运行命令和期望结果

**自审检查：**

1. 规格覆盖：每个需求都有对应任务？
2. 占位符扫描：搜索红旗模式
3. 类型一致性：后续任务的签名是否与前面定义匹配？

**执行移交：** 计划完成后提供两种执行选项——`Subagent-Driven`（推荐）或 `Inline Execution`



## 4、`executing-plans` — 执行实现计划

**类型：** 流程技能（有写好的计划要执行时使用）

**核心理念：** 加载计划→批判性审视→执行所有任务→汇报完成

**适用场景：** 没有子代理可用时使用；如果有子代理支持，应优先使用 `subagent-driven-development`

**流程：**

1. **加载并审视计划：** 读计划文件→批判性审查→有疑问先提出→没问题则创建 `todo` 并执行
2. **执行任务：** 逐个标记 `in_progress` → 严格按步骤执行 → 运行验证 → 标记完成
3. **完成开发：** 调用 `finishing-a-development-branch` skill

**停止并求助的条件：**

- 遇到阻塞（缺少依赖、测试失败、指令不清）
- 计划有关键缺口
- 不理解指令
- 验证反复失败

**原则：**

- 先批判性审查计划
- 严格按步骤执行
- 不跳过验证
- 遇阻则停，不要猜测
- 未经明确同意不在 `main/master` 分支上开始实现



## 5、`test-driven-development`：测试驱动开发

**类型：** 纪律技能（刚性，必须严格遵循）

**激活时机：**实施过程中激活

**核心规则：**没有失败的测试，就不允许写生产代码。遵循“红绿重构”循环：

- 红色（`Red`）：写失败测试，描述期望行为→ 验证失败正确 
- 绿色（`Green`）：编写最少代码让测试通过→ 验证通过
- 重构（`Refactor`）：优化代码（如清理重复、改善命名）→ 验证仍通过 

**常见自我合理化及真相：**

| 借口                       | 现实                                                     |
| -------------------------- | -------------------------------------------------------- |
| "太简单不需要测试"         | 简单代码也会坏，测试只需 30 秒                           |
| "我先实现再补测试"         | 后补的测试立刻通过，证明不了什么                         |
| "删除 X 小时的工作太浪费"  | 沉没成本谬误，留着不可信的代码才是技术债                 |
| "TDD 是教条主义"           | TDD 是务实主义——在提交前发现 bug 比调试更快              |
| "先写代码再写测试效果一样" | 后写测试回答"代码做了什么"，先写测试回答"代码应该做什么" |







## 6、`systematic-debugging` — 系统化调试

**类型：** 纪律技能（刚性，必须严格遵循）

**铁律：** 没有完成根因调查就不能提出修复。症状修复等于失败。

**触发时机**：出现 `Bug`、功能异常时

**做什么**：强制四步排错流程，禁止瞎改代码试错：

1. 根因调查：拿到完整报错信息
2. 模式分析：根因分析，定位问题本质
3. 假设与测试：制定修复方案，评估影响范围
4. 实现：修复后回归验证，确认不引入新问题

**常见自我合理化：**

| 借口                       | 现实                                     |
| -------------------------- | ---------------------------------------- |
| "问题简单，不需要流程"     | 简单问题也有根因，流程处理简单 bug 更快  |
| "紧急情况，没时间"         | 系统调试比瞎猜快                         |
| "先试这个再调查"           | 第一次修复定调，一开始就要做对           |
| "3 次修复都失败了再来一次" | 3+ 次失败 = 架构问题，质疑模式而非继续修 |



## 7、`verification-before-completion` — 完成前验证

**类型：** 纪律技能（刚性，必须严格遵循）

**铁律：** 没有新鲜验证证据就不能声称完成。

**门控函数：**

```
声称任何状态前：
1. 识别：什么命令能证明这个声称？
2. 运行：执行完整命令（新鲜、完整）
3. 阅读：完整输出，检查退出码，统计失败数
4. 验证：输出确认声称？
   - 否：陈述实际状态附证据
   - 是：陈述声称附证据
5. 然后才能做声称
```

**常见声称及其所需证据：**

| 声称        | 需要                 | 不够                        |
| ----------- | -------------------- | --------------------------- |
| 测试通过    | 测试命令输出：0 失败 | 之前的运行、"应该通过"      |
| Linter 干净 | Linter 输出：0 错误  | 部分检查、推论              |
| 构建成功    | 构建命令：退出码 0   | Linter 通过、日志看起来正常 |
| Bug 已修复  | 原始症状测试通过     | 代码改了、假设修了          |
| Agent 完成  | VCS diff 显示变更    | Agent 报告"成功"            |

**红旗——停下来：**

- 使用"应该"、"大概"、"似乎"
- 在验证前表达满意（"太好了！"、"完成！"）
- 准备提交/推送/PR 但没验证
- 信任 agent 的成功报告
- 任何暗示成功但没运行验证的措辞

**为什么重要（来自 24 次失败记忆）：**

- 用户说"我不相信你"——信任破裂
- 未定义函数被发布——会崩溃
- 需求缺失被发布——不完整功能
- 虚假完成→返工→浪费时间



## 8、`dispatching-parallel-agents` — 并行代理分派

**类型：** 执行技能

**核心理念：** 一个独立问题域一个 `agent`，让它们并行工作。

**适用场景：**

- 3+ 个测试文件因不同根因失败
- 多个子系统独立损坏
- 每个问题无需其他问题的上下文就能理解
- 调查之间无共享状态

**不适用场景：**

- 故障相关（修一个可能修其他）→ 先一起调查
- 需要理解完整系统状态
- `Agent` 会互相干扰（编辑相同文件）

**流程：**

1. **识别独立域：** 按损坏内容分组故障
2. **创建聚焦 agent 任务：** 每个 agent 获得具体范围、清晰目标、约束、期望输出
3. **并行分派：** 在同一条响应中发出所有分派 → 并行执行
4. **审查和整合：** 读每个摘要→验证修复不冲突→运行完整测试→整合所有变更

**好的 `Agent` 提示词特征：**

- 聚焦：一个清晰的问题域
- 自包含：理解问题所需的全部上下文
- 具体输出：agent 应该返回什么

**常见错误：**

- 太宽泛："修所有测试" → agent 迷失
- 无上下文："修竞态条件" → agent 不知道在哪
- 无约束：agent 可能重构一切
- 输出模糊："修好它" → 你不知道改了什么





## 9、`subagent-driven-development` — `Subagent` 驱动开发

**类型：** 执行技能（推荐的计划执行方式）

**核心理念：** 每个 `task` 一个新鲜 subagent + 任务审查（规格合规+代码质量）+ 最终全量审查 = 高质量快速迭代

**激活时机：**多文件、多模块的复杂任务，有计划时激活，

- **工作原理**：

  - 主 `Agent`（协调者）：只负责需求理解、方案设计、任务拆解、结果汇总，不写具体代码

  - 子 `Agent`（执行者）：每个子 `Agent` 只负责一个独立的小任务，只拥有自己任务的上下文，完成即销毁

- **核心优势**：
  1. **上下文不膨胀：**每个子 Agent 只看自己的代码，不会被无关信息干扰
  2. **并行提速：**无依赖的子任务可以同时执行，缩短总耗时
  3. **质量稳定：**每个子任务都独立走 TDD + 评审流程
  4. **成本更低：**避免大上下文反复计费，总 Token 消耗反而更少

**流程：**

```
读取计划 → 创建 todos → [每个 Task：分派实现者 subagent → 如有问题先回答 → 实现者实现/测试/提交/自审
  → 生成 diff 文件 → 分派任务审查者 subagent → 规格 + 质量通过？
    → 否：分派修复 subagent → 重新审查
    → 是：标记任务完成
] → 所有任务完成 → 分派最终代码审查 subagent → 使用 finishing-a-development-branch
```

**实现者状态处理：**

- `DONE` → 生成审查包，分派任务审查者
- `DONE_WITH_CONCERNS` → 先读关切，再决定
- `NEEDS_CONTEXT` → 提供缺失上下文，重新分派
- `BLOCKED` → 评估阻塞原因（上下文→更强模型→拆分→升级给人）

**持久进度：** 用 progress ledger 文件追踪（`.superpowers/sdd/progress.md`），而非仅依赖 todo，防止上下文压缩后丢失进度







## 10、`using-git-worktrees` — 使用 `Git Worktrees`

**类型：** 工具技能

**核心理念：** 先检测已有隔离，再用原生工具，最后回退 git。永远不要和 harness 对抗。

**核心逻辑：**`Git Worktrees` 实现“一个仓库，多个工作目录”，允许开发者在多个分支的独立目录中并行开发，且共享 `Git` 仓库资源（如远程配置、`Git`对象），避免传统 `checkout` 切换分支时的文件变更干扰。

```
# 创建新的worktree（关联分支feature-login，目录为login-feature）
git worktree add ./login-feature feature-login
```

**安全规则：**

- `worktree` 目录必须加入 `.gitignore`，防止误提交；
- 创建后必须运行测试，确保初始环境干净；
- 目录选择有优先级（优先复用现有配置，否则询问用户）。

**常见错误：**

- 用 `git worktree add` 而不是原生工具（最大的错误）
- 跳过检测就创建嵌套 worktree
- 不验证目录被 gitignore
- 跳过基线测试验证



## 11、`requesting-code-review` — 请求代码审查

**类型：** 工具技能

**核心理念：** 及早审查，频繁审查。

**何时请求审查：**

- `subagent-driven` 开发中每个 `task` 后
- 完成重大功能后
- 合并到 `main` 前

**核心逻辑：**自动派遣独立的审查代理，将问题分为三级：

- `Critical`：必须立即修复；
- `Important`：必须修复后才能继续；
- `Minor`：记录后后续处理。

**与工作流集成：**

- `Subagent-Driven Development`：每个 task 后审查
- `Executing Plans`：每个 task 或自然检查点后审查
- `Ad-Hoc` 开发：合并前审查





## 12、`receiving-code-review` — 接收代码审查

**类型：** 工具技能

**核心理念：** 先验证再实现，技术严谨优于社交舒适。代码审查需要技术评估，不是情感表演。

**响应模式：**

```
收到反馈 
→ 完整阅读（不反应）
→ 用自己话重述需求（或提问）
→ 对照代码库验证 
→ 评估技术合理性 
→ 技术性确认或有理有据地反驳 
→ 逐项实施
```

**禁止的响应：**

- "你说得完全对！"
- "好观点！" / "出色的反馈！"
- "让我现在实现"（验证之前）

**替代方案：** 重述技术需求、提澄清问题、有技术推理地反驳、直接开始工作（行动>言辞）

**处理不明确反馈：** 如果有任何条目不明确，先停下来——不要先实现明确的部分、稍后再问不明确的

**外部审查者反馈处理：** 实施前检查：

1. 对这个代码库技术正确吗？
2. 会破坏现有功能吗？
3. 当前实现的原因是什么？
4. 在所有平台/版本上工作吗？
5. 审查者理解完整上下文吗？

**`YAGNI` 检查：** 如果审查者建议"正确实现"某个功能 → grep 代码库找实际使用 → 未使用就移除

**何时反驳：** 建议破坏现有功能 / 审查者缺乏完整上下文 / 违反 YAGNI / 技术上不正确 / 存在兼容性原因 / 与用户的架构决策冲突

**如何承认正确反馈：**

- ✅ "已修复。[简要描述变更]"
- ✅ [直接修复并在代码中展示]
- ❌ 任何感谢表达





## 13、`finishing-a-development-branch`：分支收尾

**类型：** 工具技能

**激活时机：**任务完成时自动激活

**核心逻辑：**测试通过后，提供本地合并、推送建 `PR`、暂存、丢弃四个选项，并自动执行对应清理流程

- 生成变更说明，确认所有验收条件都已满足
- 提交 `Git` 提交记录（如果在 `Git` 仓库中）
- 最终给你交付总结：完成了什么、文件列表、如何使用、测试情况

**核心理念：** 验证测试→检测环境→呈现选项→执行选择→清理

- **Step 1：验证测试**：运行项目测试套件，测试失败 → 报告失败，不继续；测试通过 → 继续
- **Step 2：检测环境**：判断是普通仓库、命名分支 worktree 还是 detached HEAD，决定显示哪种菜单和如何清理
- **Step 3：确定基础分支**：尝试 git merge-base 找到分离点
- **Step 4：呈现选项**：普通仓库和命名分支 worktree — 4 个选项：
  - 本地合并回基础分支
  - 推送并创建 Pull Request
  - 保持分支原样（稍后自己处理）
  - 丢弃此工作
- **Step 5：执行选择**
  - 选项 1：合并→验证→清理 worktree→删除分支
  - 选项 2：推送（不清理 worktree，用户需要它处理 PR 反馈）
  - 选项 3：保留（不清理）
  - 选项 4：确认后删除（必须输入"discard"确认）
- **Step 6：清理工作区**
  - 仅选项 1 和 4 需要清理
  - 只清理 `.worktrees/` 或 `worktrees/` 下的（superpowers 创建的）
  - 不清理 harness 管理的工作区
  - 先移除 worktree 再删除分支
  - 移除后运行 `git worktree prune`



## 14、`Writing Skills`  编写 Skill

**类型：** 元技能（用 `TDD` 方法论创建新 skill）

**核心理念：** 编写 `skill` 就是将 TDD 应用于过程文档。如果你没有看到 `agent` 在没有 skill 时失败，你不知道 skill 是否教了正确的东西。

**`TDD` 映射：**

| TDD 概念          | Skill 创建                          |
| ----------------- | ----------------------------------- |
| 测试用例          | 用 subagent 的压力场景              |
| 生产代码          | Skill 文档（SKILL.md）              |
| 测试失败（RED）   | Agent 没有 skill 时违反规则（基线） |
| 测试通过（GREEN） | Agent 有 skill 时遵守规则           |
| 重构              | 闭环漏洞同时保持合规                |

**工作流程：**

```
1. RED 阶段 —— 看它怎么错
   ├─ 设计施压场景（3+ 组合压力）
   ├─ 没有 Skill 的情况下运行 → 记录 agent 的错误行为和借口（原话！）
   └─ 识别 rationalization（合理化借口）的模式

2. GREEN 阶段 —— 写 Skill 修正
   ├─ 写最精简的 SKILL.md，只针对 RED 发现的具体问题
   ├─ 有 Skill 的情况下运行 → 验证 agent 现在遵守了
   └─ 注意：description 只写"什么时候用"，不写"怎么用"（否则 agent 只看描述不读正文）

3. REFACTOR 阶段 —— 堵漏洞
   ├─ 发现 agent 新的借口 → 加显式反驳
   ├─ 构建 rationalization 表（借口 → 现实）
   ├─ 创建红旗清单（出现这些信号 = 你在合理化）
   └─ 循环直到无法攻破
```



**Skill 文档的结构**

```markdown
---
name: skill-name-with-hyphens      # 只用字母数字和连字符
description: Use when [触发条件]     # 只写何时用，不写怎么用！
---

# Skill Name

## Overview          — 核心原则 1-2 句
## When to Use       — 什么时候用/不用
## Core Pattern      — Before/After 对比
## Quick Reference   — 扫描式速查表
## Implementation    — 内联代码或链接到文件
## Common Mistakes   — 常见错误 + 修复
## Real-World Impact — 可选，实际效果
```

**几个关键反模式**

| 反模式                     | 为什么不好                                                |
| :------------------------- | :-------------------------------------------------------- |
| `Description` 里写流程摘要 | agent 会只看描述不读正文，导致行为偏差                    |
| 用禁止列表塑形输出         | 禁止列表反而让 agent 聚焦于被禁止的东西；用"正面配方"更好 |
| 没测试就发布 Skill         | = 没测试就发布代码，必出问题                              |
| 批量写多个 Skill           | 每个 Skill 必须独立走完 RED→GREEN→REFACTOR→部署           |



**什么时候该创建 Skill**

- **该创建：**

  - 你发现的技巧不是显而易见的

  - 跨项目可复用

  - 其他人也会受益


- **不该创建：**

  - 一次性的方案

  - 标准实践（已有好文档）

  - 项目特定的约定（放 [CLAUDE.md](CLAUDE.md) 而不是 Skill）

  - 机械性约束（能用正则/校验自动化的，别用文档）




**和其他 Skill 的关系**

```
brainstorming（想法→设计）
    ↓
writing-plans（设计→实现计划）
    ↓
executing-plans（计划→代码）
    ↕ 随时可能需要
writing-skills（把经验沉淀为 Skill）
    ↕ 前置要求
test-driven-development（TDD 核心理念）
```















# 三、`OpenSpec` + `Superpowers`

## 1、为什么需要这个组合？

>  **`OpenSpec` + `Superpowers`** 的组合，目标不是“更酷”，而是更稳：把 `AI` 开发从“凭感觉推进”，拉回到 **可追溯、可验证、可复用** 的工程流水线上。

一套清晰分工：`OpenSpec` 管 **规格与追溯**，`Superpowers` 管 **执行与质量闸门**

- **`OpenSpec` 管「改什么、为什么改、验收标准是什么」。**

- **`Superpowers` 管「怎么拆、怎么测、怎么执行、怎么 `review` 到结束」。**



![image-20260623142754177](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260623142754177.png)

### 1）`OpenSpec` 痛点是是什么

> OpenSpec 解决“做什么 / 做到什么程度算完成”，Superpowers 解决“怎么稳稳做完并保证质量”，两者组合把返工率与偏离风险压到最低。

| 痛点        | `OpenSpec` 方案  | `Superpowers` 方案 | 组合效果   |
| :---------- | :--------------- | :----------------- | :--------- |
| 需求不明确  | ✅ 结构化规格文档 | ❌ 无专门机制       | ✅ 规格驱动 |
| AI 偏离需求 | ✅ 变更追溯       | ✅ 两阶段审查       | ✅ 双重保障 |
| 代码质量差  | ⚠️ 行为验证       | ✅ `TDD` 强制       | ✅ 测试覆盖 |
| 上下文混乱  | ✅ 变更隔离       | ✅ Git Worktree     | ✅ 完全隔离 |
| 返工率高    | ✅ 先设计后实现   | ✅ 先测试后实现     | ✅ 一次做对 |



### 2）分工是什么样的

- 单独用 `OpenSpec`，常见情况是需求写得很漂亮，但 `AI` 实现时一次性改一堆文件，测试少，提交粒度粗。

- 单独用 `Superpowers`，常见情况是 `TDD` 很认真，代码也干净，但它没有稳定读取需求边界，容易自主扩功能。

![image-20260623150223310](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260623150223310.png)



### 3）二者如何联动

> 注意：**OpenSpec 和 Superpowers 是两个独立项目，彼此不知道对方的存在**

所谓"联动"，是架构师把两个闭环串起来——用 `OpenSpec` 做规格定义（它擅长这个），用 `Superpowers` 做代码实现（它擅长 TDD 和代码审查），跳过 `OpenSpec` 自己的 `/opsx:apply`。

两个工具各自都有完整的闭环：

- **`OpenSpec` 的闭环**：`/opsx:propose` → `/opsx:apply`（自己实现代码）→ `/opsx:archive`
- **`Superpowers` 的闭环**：
  - → `brainstorming`（自动触发）
  - → `writing-plans`（自动跳转）
  - → `subagent-driven-development`（自动执行）
  - → `finishing-a-development-branch`（收尾）



| 阶段     | 谁主导      | 产物                        | 检查点                |
| :------- | :---------- | :-------------------------- | :-------------------- |
| 需求探索 | OpenSpec    | explore 记录、proposal 草稿 | 问题是否真的清楚      |
| 规格冻结 | OpenSpec    | specs delta、design         | 需求是否可验收        |
| 执行计划 | Superpowers | plan、task checklist        | 任务是否小到可执行    |
| 编码实现 | Superpowers | 测试、代码、提交            | 是否按 TDD 和任务推进 |
| 变更验证 | 两者一起    | verify、review、测试结果    | 代码是否满足 spec     |
| 归档沉淀 | OpenSpec    | synced specs、archive       | 当前系统规格是否更新  |



### 4）工作流简述

> 架构师在整个流程中做四件事：**描述意图、审查方案、桥接传递上下文、验收并归档**。两个工具之间的衔接，靠的是架构师在同一个 `Claude Code` 会话里操作——先跑 `OpenSpec` 的命令，再让 `Superpowers` 接手实现，最后回到 `OpenSpec` 归档。

```
[OpenSpec 负责]                     [Superpowers 负责]

/opsx:propose
    ↓
生成 proposal / specs / design / tasks
    ↓
架构师审查文档                        brainstorming（自动触发）
    ↓                                    ↓
架构师将文档内容作为上下文，            writing-plans → 自动生成实现计划
告诉 AI "按照这些规格来实现"                 ↓
    ↓                               subagent-driven-development
  手动桥接 ──────────────────→       （子代理逐任务执行 TDD）
                                        ↓
                                    finishing-a-development-branch
                                        ↓
架构师回到 OpenSpec 执行 ←──────     实现完成
/opsx:archive
```



## 2、核心流程

| 节点     | 什么时候             | 架构师做什么                                                 | 工具                      |
| :------- | :------------------- | :----------------------------------------------------------- | :------------------------ |
| 意图定义 | 每轮迭代开始         | 执行 `/opsx:propose`，描述需求                               | `OpenSpec`                |
| 方案审查 | `propose` 之后       | 审查 `proposal`、`spec`、`design`、`tasks`                   | `OpenSpec`                |
| 桥接传递 | 规划阶段             | 在同一会话中触发 `brainstorming`，确认 `AI` 理解了 `OpenSpec `规格 | `Superpowers`（自动触发） |
| 计划审批 | `brainstorming` 之后 | 审查 `writing-plans` 输出的实现计划                          | `Superpowers`（自动跳转） |
| 验收签字 | 执行之后             | `expanded profile` 用 `/opsx:verify`；`core profile` 手动审查代码 | `OpenSpec`                |
| 归档确认 | 验收通过后           | `/opsx:archive`，按提示同步增量 `specs`                      | `OpenSpec`                |



### 1）`Step 1`：`OpenSpec` 生成规约

> 架构师需要做的是**审查这四份文档**。如果需求描述不准确，直接改 proposal 重新生成；如果任务拆分粒度不对，手动调整 `tasks.md`。



### 2）`Step 2`：技术规划与桥接

> 需求文档就绪后，该进入实现了。这里有个选择：你可以直接执行 `/opsx:apply` 让 `OpenSpec` 自己实现代码。但如果你想要严格的 `TDD` 流程和代码审查，就轮到 `Superpowers` 接手了。

前提：启动 `Superpowers` 实现前，必须把这些文件作为上下文输入：

```
/superpowers:brainstorming
我已经用 OpenSpec 定义好了需求，规格文档在 openspec/changes/create-kanban/ 目录下。

执行要求：
- 严格遵守 design.md 技术约束
- 不得修改 proposal.md 的 Non-Goals 范围
- 每个 task 拆成 TDD 步骤
- 每个 task 完成后做 spec compliance check
```

**触发 `Superpowers` 的 `brainstorming`**：在同一个 `Claude Code` 会话中，直接告诉 `AI` 你的实现意图。`Superpowers` 安装后会自动注入一个 `SessionStart` 钩子，`AI` 会自动加载 `brainstorming` 技能，进入**苏格拉底式提问**流程：

1. **探索项目上下文** — AI 检查现有文件、文档、最近提交
2. **逐个提问** — 确认目的、约束、成功标准（一次只问一个问题）
3. **提出 2-3 个方案** — 带权衡分析和推荐
4. **分段展示设计** — 每段 200-300 字，逐段确认
5. **写设计文档** — 保存到 `docs/superpowers/specs/YYYY-MM-DD-<topic>-design.md`
6. **自检** — 检查占位符、矛盾、歧义、范围
7. **用户审批** — 架构师确认设计文档



```
# Kanban Board Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL:
> Use superpowers:subagent-driven-development

**Goal:** Build a kanban board with Column and Task CRUD,
backed by SQLite, served via Go REST API, rendered in React.

**Architecture:** Three-tier - SQLite storage layer,
Go HTTP API layer, React SPA frontend.

**Tech Stack:** Go 1.22, SQLite3, gorilla/mux,
React 18, TypeScript, React Testing Library

### Task 1: Database Schema & Models

**Files:**
- Create: `internal/models/column.go`
- Create: `internal/models/task.go`
- Create: `internal/db/schema.sql`
- Test: `internal/models/column_test.go`
- Test: `internal/models/task_test.go`

- [ ] Step 1: Write failing tests for Column model
- [ ] Step 2: Run test to verify it fails (RED)
- [ ] Step 3: Write minimal Column model implementation
- [ ] Step 4: Run test to verify it passes (GREEN)
- [ ] Step 5: Repeat for Task model
- [ ] Step 6: Commit

### Task 2: Column API Endpoints
...
```



### 3）`Step 3`：`TDD` 执行

> 计划通过后，`AI` 自动激活 `subagent-driven-development` 技能——同样不需要手动输入命令。计划文档的头部明确声明了：

```
 **For agentic workers:** REQUIRED SUB-SKILL:
> Use superpowers:subagent-driven-development
```



所有任务完成后，`Superpowers` 自动进入 `finishing-a-development-branch` 技能——验证测试、选择合并或保持分支。这一步也是自动触发，不需要手动输入命令。

![image-20260623151928560](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260623151928560.png)



### 4）`Step 4`：验收与归档

> 执行 `/opsx:verify` 做三维度的自动检查——完整性（任务是否都完成）、正确性（实现是否匹配规格意图）、一致性（设计决策是否反映在代码中）。







# 四、`Superpowers` 插件加载机制

## 1、磁盘上的物理结构

`Superpowers` 插件安装在你的机器上，路径为：

```
~/.claude/plugins/cache/claude-plugins-official/superpowers/6.0.3/
├── .claude-plugin/
│   └── plugin.json                          ← 插件元数据（名称、版本、作者）
├── CLAUDE.md                                ← 贡献者指南（94% PR 拒绝率的严格规则）
├── hooks/
│   ├── hooks.json                           ← Hook 注册声明（告诉 harness 何时触发）
│   ├── run-hook.cmd                         ← 跨平台 hook 启动器（bash/cmd 双模）
│   └── session-start                        ← SessionStart hook 的实际 bash 脚本
├── skills/
│   ├── using-superpowers/SKILL.md           ← 引导 skill（121行，自动注入）
│   ├── brainstorming/SKILL.md               ← 159行
│   ├── systematic-debugging/SKILL.md        ← 296行
│   ├── test-driven-development/SKILL.md     ← 371行
│   ├── subagent-driven-development/SKILL.md ← 418行
│   ├── writing-skills/SKILL.md              ← 689行
│   ├── verification-before-completion/SKILL.md ← 139行
│   ├── writing-plans/SKILL.md               ← 174行
│   ├── using-git-worktrees/SKILL.md         ← 202行
│   ├── finishing-a-development-branch/SKILL.md ← 241行
│   ├── receiving-code-review/SKILL.md       ← 213行
│   ├── requesting-code-review/SKILL.md      ← 103行
│   ├── dispatching-parallel-agents/SKILL.md ← 185行
│   └── executing-plans/SKILL.md             ← 70行
├── assets/
│   ├── app-icon.png
│   └── superpowers-small.svg
├── docs/                                    ← 开发者文档
├── scripts/                                 ← 版本管理等脚本
└── .claude-plugin/marketplace.json          ← 市场信息
```



## 2、注册与发现：`Claude Code` 如何知道插件存在

### 1）安装记录

> `~/.claude/plugins/installed_plugins.json` 记录了安装信息：

```json
{
  "version": 2,
  "plugins": {
    "superpowers@claude-plugins-official": [
      {
        "scope": "user",
        "installPath": "/Users/zhangyujin1/.claude/plugins/cache/claude-plugins-official/superpowers/6.0.3",
        "version": "6.0.3",
        "installedAt": "2026-06-22T09:41:20.584Z",
        "lastUpdated": "2026-06-22T09:41:20.584Z",
        "gitCommitSha": "896224c4b1879920ab573417e68fd51d2ccc9072"
      }
    ]
  }
}
```

关键字段：

- `scope: "user"` — 用户级安装（非项目级），所有项目都生效
- `installPath` — 指向缓存目录中的实际文件
- `gitCommitSha` — 安装时的 Git commit，用于版本追踪



### 2）`claude` 启用声明

> `~/.claude/settings.json` 中声明了启用：

```json
{
  "enabledPlugins": {
    "superpowers@claude-plugins-official": true
  }
}
```

### 3）插件元数据

> `~/.claude/plugins/cache/claude-plugins-official/superpowers/6.0.3/.claude-plugin/plugin.json`：

```json
{
  "name": "superpowers",
  "description": "Core skills library for Claude Code: TDD, debugging, collaboration patterns, and proven techniques",
  "version": "6.0.3",
  "author": {
    "name": "Jesse Vincent",
    "email": "jesse@fsck.com"
  },
  "homepage": "https://github.com/obra/superpowers",
  "license": "MIT",
  "keywords": ["skills", "tdd", "debugging", "collaboration", "best-practices", "workflows"]
}
```



## 3、加载阶段 1：`SessionStart Hook`（自动注入引导 `skill`）

> 这是**最关键的一步**——让 `Superpowers` 的引导内容在会话启动时自动进入 AI 的上下文。
>

### 1）`hooks.json` 声明触发条件

```json
{
  "hooks": {
    "SessionStart": [
      {
        "matcher": "startup|clear|compact",
        "hooks": [
          {
            "type": "command",
            "command": "\"${CLAUDE_PLUGIN_ROOT}/hooks/run-hook.cmd\" session-start",
            "async": false
          }
        ]
      }
    ]
  }
}
```

| 字段                                 | 含义                                                         |
| ------------------------------------ | ------------------------------------------------------------ |
| `matcher: "startup\|clear\|compact"` | 在会话启动、清空上下文、压缩上下文时触发                     |
| `type: "command"`                    | 执行一个 shell 命令                                          |
| `command`                            | 具体要执行的命令，`${CLAUDE_PLUGIN_ROOT}` 是 harness 注入的环境变量 |
| `async: false`                       | **同步执行**——必须等 hook 完成后才能继续，确保引导内容在第一条用户消息之前就位 |



### 2）`run-hook.cmd`：跨平台双模启动器

这是一个精巧的**多语言脚本**——同一份文件既能在 Windows cmd.exe 运行，也能在 Unix bash 运行：

**Windows 部分**（cmd.exe 执行）：

```batch
@echo off
if "%~1"=="" (
    echo run-hook.cmd: missing script name >&2
    exit /b 1
)
set "HOOK_DIR=%~dp0"
REM 尝试 Git for Windows 的 bash...
"C:\Program Files\Git\bin\bash.exe" "%HOOK_DIR%%~1" %2 %3 %4 %5 %6 %7 %8 %9
```

**Unix 部分**（bash 执行，`:` 是 no-op，`CMDBLOCK` heredoc 被忽略）：

```bash
: << 'CMDBLOCK'
... Windows 批处理代码被 bash 当作注释忽略 ...
CMDBLOCK

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
SCRIPT_NAME="$1"
shift
exec bash "${SCRIPT_DIR}/${SCRIPT_NAME}" "$@"
```

设计意图：Hook 脚本使用**无扩展名**（`session-start` 而非 `session-start.sh`），避免 Claude Code 在 Windows 上自动在命令前加 `bash` 前缀导致双重 bash 调用。



### 3）`session-start` 脚本：核心注入逻辑

这是整个加载机制的**核心**。脚本做了三件事：

#### 步骤 1：读取引导 `skill` 文件的完整内容

```bash
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PLUGIN_ROOT="$(cd "${SCRIPT_DIR}/.." && pwd)"

using_superpowers_content=$(cat "${PLUGIN_ROOT}/skills/using-superpowers/SKILL.md" 2>&1 || echo "Error reading using-superpowers skill")
```

直接 `cat` 读取 `using-superpowers/SKILL.md` 的 121 行完整内容。

#### 步骤 2：`JSON` 转义

```bash
escape_for_json() {
    local s="$1"
    s="${s//\\/\\\\}"     # 反斜杠 \ → \\
    s="${s//\"/\\\"}"     # 双引号 " → \"
    s="${s//$'\n'/\\n}"   # 换行符 → \n
    s="${s//$'\r'/\\r}"   # 回车符 → \r
    s="${s//$'\t'/\\t}"   # 制表符 → \t
    printf '%s' "$s"
}

using_superpowers_escaped=$(escape_for_json "$using_superpowers_content")
```

使用 `bash` 原生的 `${s//old/new}` 参数替换——这是 C 级别的单次遍历，比字符级循环**快几个数量级**。

#### 步骤 3：构造上下文注入内容

```bash
session_context="<EXTREMELY_IMPORTANT>\nYou have superpowers.\n\n**Below is the full content of your 'superpowers:using-superpowers' skill - your introduction to using skills. For all other skills, use the 'Skill' tool:**\n\n${using_superpowers_escaped}\n</EXTREMELY_IMPORTANT>"
```

用 `<EXTREMELY_IMPORTANT>` 标签包裹，强调 AI 必须重视这段内容。



#### 步骤 4：根据平台输出不同 JSON 格式

```bash
if [ -n "${CURSOR_PLUGIN_ROOT:-}" ]; then
  # Cursor：检测 $CURSOR_PLUGIN_ROOT
  printf '{\n  "additional_context": "%s"\n}\n' "$session_context" | cat
elif [ -n "${CLAUDE_PLUGIN_ROOT:-}" ] && [ -z "${COPILOT_CLI:-}" ]; then
  # Claude Code：检测 $CLAUDE_PLUGIN_ROOT，排除 Copilot CLI
  printf '{\n  "hookSpecificOutput": {\n    "hookEventName": "SessionStart",\n    "additionalContext": "%s"\n  }\n}\n' "$session_context" | cat
else
  # Copilot CLI 或其他平台
  printf '{\n  "additionalContext": "%s"\n}\n' "$session_context" | cat
fi
```

| 平台            | 检测方式                                          | JSON 结构                                                    |
| --------------- | ------------------------------------------------- | ------------------------------------------------------------ |
| **Cursor**      | `$CURSOR_PLUGIN_ROOT` 非空                        | `{"additional_context": "..."}` （snake_case）               |
| **Claude Code** | `$CLAUDE_PLUGIN_ROOT` 非空 且 `$COPILOT_CLI` 为空 | `{"hookSpecificOutput": {"hookEventName": "SessionStart", "additionalContext": "..."}}` （嵌套结构） |
| **Copilot CLI** | `$COPILOT_CLI` 非空                               | `{"additionalContext": "..."}` （SDK 标准格式）              |

> **为什么 Claude Code 用嵌套结构？** 因为 Claude Code 同时读取 `additional_context`（顶层）和 `hookSpecificOutput.additionalContext`（嵌套），且**不做去重**。如果两种格式都输出，内容会被注入两次。所以必须只输出当前平台消费的那个字段。



### 4）`Claude Code` 如何消费 `hook` 输出

`Claude Code `的 `harness` 执行流程：

```
1. Harness 执行: bash run-hook.cmd session-start
2. 捕获 stdout → 解析 JSON
3. 提取 hookSpecificOutput.additionalContext
4. 将内容注入为一条系统消息，出现在对话最开头
5. AI 在看到第一条用户消息之前，就已经"拥有 superpowers"
```

最终 AI 看到的内容就是这段：

```
<EXTREMELY_IMPORTANT>
You have superpowers.

**Below is the full content of your 'superpowers:using-superpowers' skill - your introduction to using skills. For all other skills, use the 'Skill' tool:**

---
name: using-superpowers
description: Use when starting any conversation - establishes how to find and use skills...
---

<SUBAGENT-STOP>
If you were dispatched as a subagent to execute a specific task, skip this skill.
</SUBAGENT-STOP>

<EXTREMELY-IMPORTANT>
If you think there is even a 1% chance a skill might apply to what you are doing, you ABSOLUTELY MUST invoke the skill.
...
[121 行完整内容]
</EXTREMELY_IMPORTANT>
```

---

## 4、加载阶段 2：`Skill` 索引注入

> 除了 `SessionStart hook` 注入的 `using-superpowers` 全文，`Claude Code` 的 `harness` 还会执行以下操作：
>

### 1）扫描 `skills` 目录

> `Harness` 递归扫描 `skills/` 目录下所有 `SKILL.md` 文件。

### 2） 读取 `frontmatter`

每个 `SKILL.md` 文件的开头都有 `YAML frontmatter`：

```yaml
---
name: brainstorming
description: "You MUST use this before any creative work - creating features, building components, adding functionality, or modifying behavior. Explores user intent, requirements and design before implementation."
---
```

Harness 只读取 `name` 和 `description`，不读取后续的正文内容。

### 3）构建 skill 索引列表

Harness 将所有 skill 的名称和描述注入到系统提示的 **"Available skills"** 部分。每个条目格式为：

```
- superpowers:brainstorming: You MUST use this before any creative work...
- superpowers:systematic-debugging: Use when encountering any bug...
- superpowers:test-driven-development: Use when implementing any feature...
... (共 14 个)
```

在 `/context` 输出中可以看到每个条目大约占 **~40-90 tokens**——这只是索引条目，不是完整内容。



## 5、加载阶段 3：按需加载（Skill 工具调用）

### 1）触发机制

当 AI 调用 `Skill({skill: "superpowers:brainstorming"})` 时：

```
1. AI 调用 Skill 工具，传入 skill 名称 "superpowers:brainstorming"
2. Harness 根据 "superpowers@" 前缀识别为插件 skill
3. 查找安装记录 → 得到 installPath
4. 拼接完整路径: installPath + "/skills/brainstorming/SKILL.md"
5. 读取文件完整内容（159 行）
6. 将内容作为一条新的对话消息注入
7. AI 收到完整指令后，按照指令行动
```

### 2） 懒加载的 Token 节约

如果 14 个 skill 全部在启动时加载，约需 **30-40k tokens**。通过懒加载：

| 场景     | 加载的 skill                                                 | 额外 token 消耗 |
| -------- | ------------------------------------------------------------ | --------------- |
| 普通对话 | 无                                                           | 0               |
| 修 bug   | systematic-debugging                                         | ~3k             |
| 新功能   | brainstorming → TDD                                          | ~5k             |
| 复杂任务 | brainstorming → writing-plans → subagent-driven → verification | ~8k             |

---



## 6、完整的端到端数据流图

```
┌──────────────────────────────────────────────────────────────────────────┐
│                          磁盘上的文件                                     │
│                                                                          │
│  installed_plugins.json     settings.json            plugin 目录树        │
│  ┌───────────────────┐    ┌──────────────────┐    ┌──────────────────┐  │
│  │ superpowers@...    │    │ enabledPlugins:  │    │ skills/          │  │
│  │ installPath: ...   │    │   superpowers:   │    │   brainstorming/ │  │
│  │ version: 6.0.3     │    │     true         │    │     SKILL.md     │  │
│  │ gitCommitSha: ...  │    │                  │    │   using-super... │  │
│  └────────┬──────────┘    └───────┬──────────┘    │     SKILL.md     │  │
│           │                       │               │ hooks/           │  │
│           │                       │               │   hooks.json     │  │
│           │                       │               │   session-start  │  │
│           │                       │               │   run-hook.cmd   │  │
│           │                       │               └──────────────────┘  │
└───────────┼───────────────────────┼──────────────────────┬─────────────┘
            │                       │                      │
            ▼                       ▼                      ▼
┌──────────────────────────────────────────────────────────────────────────┐
│                     Claude Code Harness 启动                              │
│                                                                          │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │ 阶段 A：发现与注册                                              │    │
│  │                                                                 │    │
│  │  1. 读取 installed_plugins.json                                  │    │
│  │     → 知道安装了哪些插件、安装路径、版本                           │    │
│  │                                                                 │    │
│  │  2. 检查 settings.json enabledPlugins                            │    │
│  │     → 知道哪些插件已启用（未启用的跳过）                           │    │
│  │                                                                 │    │
│  │  3. 读取 plugin.json                                            │    │
│  │     → 获取插件元数据（名称、描述、版本）                           │    │
│  │                                                                 │    │
│  │  4. 扫描 skills/ 目录                                           │    │
│  │     → 读取每个 SKILL.md 的 YAML frontmatter                      │    │
│  │     → 构建 skill 索引：name + description                        │    │
│  │                                                                 │    │
│  │  5. 读取 hooks/hooks.json                                       │    │
│  │     → 注册 SessionStart hook                                    │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                         │                                                │
│                         ▼                                                │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │ 阶段 B：SessionStart Hook 触发                                  │    │
│  │  （matcher: "startup" 匹配 → 同步执行）                          │    │
│  │                                                                 │    │
│  │  a. Harness 设置环境变量:                                        │    │
│  │     CLAUDE_PLUGIN_ROOT=~/.claude/plugins/cache/.../6.0.3        │    │
│  │                                                                 │    │
│  │  b. Harness 执行:                                               │    │
│  │     bash "run-hook.cmd" session-start                           │    │
│  │                                                                 │    │
│  │  c. run-hook.cmd 判断平台 → 调用:                                │    │
│  │     bash session-start                                          │    │
│  │                                                                 │    │
│  │  d. session-start 脚本执行:                                      │    │
│  │     ┌───────────────────────────────────────────────────────┐   │    │
│  │     │ ① cat 读取 using-superpowers/SKILL.md 完整内容         │   │    │
│  │     │    （121 行，~2.5k tokens）                            │   │    │
│  │     │                                                       │   │    │
│  │     │ ② escape_for_json() 进行 JSON 转义                     │   │    │
│  │     │    \ → \\  " → \"  换行 → \n  回车 → \r  制表 → \t    │   │    │
│  │     │                                                       │   │    │
│  │     │ ③ 包装为 <EXTREMELY_IMPORTANT>...</EXTREMELY_IMPORTANT> │   │    │
│  │     │                                                       │   │    │
│  │     │ ④ 检测平台，输出 JSON:                                  │   │    │
│  │     │    Claude Code → {hookSpecificOutput: {...}}           │   │    │
│  │     │    Cursor     → {additional_context: "..."}            │   │    │
│  │     │    Copilot    → {additionalContext: "..."}              │   │    │
│  │     └───────────────────────────────────────────────────────┘   │    │
│  │                                                                 │    │
│  │  e. Harness 捕获 stdout → 解析 JSON                              │    │
│  │     → 提取 hookSpecificOutput.additionalContext                  │    │
│  │     → 注入为系统消息（对话最开头）                                 │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                         │                                                │
│                         ▼                                                │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │ 阶段 C：构建最终上下文                                           │    │
│  │                                                                 │    │
│  │  最终注入到 AI 上下文的内容:                                      │    │
│  │                                                                 │    │
│  │  ┌─────────────────────────────────────────────────────────┐    │    │
│  │  │ 系统提示:                                                │    │    │
│  │  │   - 工具定义（Skill, Bash, Read, Edit...）               │    │    │
│  │  │   - 内置行为指令                                          │    │    │
│  │  │   - Skill 索引列表:                                      │    │    │
│  │  │     • superpowers:brainstorming (~80 tokens)             │    │    │
│  │  │     • superpowers:systematic-debugging (~40 tokens)      │    │    │
│  │  │     • superpowers:test-driven-development (~40 tokens)   │    │    │
│  │  │     • ... 共 14 个 skill 的名称+描述                      │    │    │
│  │  │   - MCP 工具定义（gitnexus 等）                           │    │    │
│  │  │   - Memory 文件                                           │    │    │
│  │  │   - 用户 CLAUDE.md 项目指令                               │    │    │
│  │  │                                                          │    │    │
│  │  │ 系统消息 (from SessionStart hook):                       │    │    │
│  │  │   <EXTREMELY_IMPORTANT>                                  │    │    │
│  │  │   You have superpowers.                                  │    │    │
│  │  │                                                          │    │    │
│  │  │   [using-superpowers SKILL.md 全文 121行]                 │    │    │
│  │  │   - 优先级规则：用户指令 > Skill > 默认行为                  │    │    │
│  │  │   - 红旗清单：不能找借口跳过 skill                           │    │    │
│  │  │   - Skill 调用流程图                                      │    │    │
│  │  │   - Skill 类型（Rigid vs Flexible）                        │    │    │
│  │  │   </EXTREMELY_IMPORTANT>                                 │    │    │
│  │  │                                                          │    │    │
│  │  │ 用户消息:                                                 │    │    │
│  │  │   "帮我讲以上两个内容进行结合..."                            │    │    │
│  │  └─────────────────────────────────────────────────────────┘    │    │
│  └─────────────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────────────┘

运行时按需加载:
┌──────────────────────────────────────────────────────────────────────────┐
│  AI 判断需要调用某个 skill                                             │
│       │                                                                  │
│       ▼                                                                  │
│  AI 调用: Skill({skill: "superpowers:brainstorming"})                    │
│       │                                                                  │
│       ▼                                                                  │
│  Harness 查找: installPath + "/skills/brainstorming/SKILL.md"            │
│       │                                                                  │
│       ▼                                                                  │
│  读取文件完整内容（159 行，~3k tokens）                                   │
│       │                                                                  │
│       ▼                                                                  │
│  注入为新的对话消息 → AI 看到完整内容 → 按照指令行动                       │
│       │                                                                  │
│       ▼                                                                  │
│  AI 执行 skill 要求的行为（如：先问需求再设计，再动手写代码）               │
└──────────────────────────────────────────────────────────────────────────┘
```



## 7、`using-superpowers` 引导 Skill 的作用

> `using-superpowers` 是唯一被自动注入的 skill，它的作用是**教会 AI 何时以及如何调用其他 13 个 skill**。

### 1）核心规则

1. **1% 规则**：如果有哪怕 1% 的可能性某个 `skill` 适用于当前任务，AI 必须调用它
2. **前置检查**：在回答问题、写代码、做任何事情之前，先检查是否有 skill 适用于当前场景
3. **不可绕过**：不能以"太简单""不需要"等理由跳过 skill 检查



### 2）优先级体系

```
用户指令（CLAUDE.md / 直接请求）
       ↓ 高于
Superpowers skill 指令
       ↓ 高于
默认系统提示行为
```

例如：如果用户的 CLAUDE.md 说"不要用 TDD"，而 skill 说"总是用 TDD"，遵循用户指令。

### 3） 红旗清单（防止 AI 找借口跳过 skill）

| AI 的内心想法        | 实际情况                 |
| -------------------- | ------------------------ |
| "这只是个简单问题"   | 问题也是任务，检查 skill |
| "我需要更多上下文"   | Skill 检查在提问之前     |
| "让我先探索代码"     | Skill 告诉你如何探索     |
| "这不需要正式 skill" | 如果 skill 存在，就用它  |
| "我记住了这个 skill" | Skill 会更新，读最新版本 |
| "Skill 杀鸡用牛刀"   | 简单的事情也会变复杂     |
| "我先做这一件事"     | 做任何事之前先检查       |

### 4）`Skill` 调用流程

```
用户消息到达
    │
    ▼
是否有 skill 可能适用？──否──→ 直接响应（包括澄清问题）
    │是
    ▼
调用 Skill 工具加载 skill
    │
    ▼
宣布："Using [skill] to [purpose]"
    │
    ▼
Skill 有检查清单？──否──→ 直接按 skill 指令执行
    │是
    ▼
为每个检查项创建 todo
    │
    ▼
严格按 skill 指令执行
```

---



### 5）引导介绍

```

  ┌─────────────────────────────────────────────────────────┐
  │ 第 1 层：强制规则（using-superpowers 正文）               │
  │                                                         │
  │   "1% 可能就调用" → "不能找借口跳过" → "先调再用"          │
  │                                                         │
  │   作用：建立行为纪律，堵住所有逃避路径                      │
  │   不点名具体 skill，但为调用创造了"必须做"的前提            │
  ├─────────────────────────────────────────────────────────┤
  │ 第 2 层：流程图 + 优先级（using-superpowers 正文）        │
  │                                                         │
  │   流程图：plan mode 之前必须 brainstorming ⭐              │
  │   优先级：build X → brainstorming; fix bug → debugging ⭐ │
  │                                                         │
  │   作用：直接点名了 2 个 skill，给出 2 个场景映射           │
  │   这是唯一硬编码具体 skill 名称的地方                      │
  ├─────────────────────────────────────────────────────────┤
  │ 第 3 层：索引列表（系统提示注入）                          │
  │                                                         │
  │   brainstorming: "You MUST use this before creative..."  │
  │   TDD: "Use when implementing any feature..."            │
  │   verification: "Use when about to claim complete..."    │
  │   ... 共 13 个 skill 的触发条件描述                        │
  │                                                         │
  │   作用：提供完整的"skill × 触发条件"映射表                  │
  │   AI 把第 1 层的"必须调用"规则 + 第 3 层的"何时适用"       │
  │   描述结合起来，就能判断每个任务该调哪个 skill               │
  └─────────────────────────────────────────────────────────┘
```



1. **第一处：<EXTREMELY-IMPORTANT> 强制检查规则（第 9-15 行）**

   ```
     <EXTREMELY-IMPORTANT>
     If you think there is even a 1% chance a skill might apply to what you are
     doing, you ABSOLUTELY MUST invoke the skill.
   
     IF A SKILL APPLIES TO YOUR TASK, YOU DO NOT HAVE A CHOICE. YOU MUST USE IT.
     </EXTREMELY-IMPORTANT>
   
     这里的"skill"指什么？ 就是索引列表中的那 13 个 skill。这段规则没有点名任何一个
     skill，但它强制 AI 在做任何事之前先检查"是否有 skill 适用"——而 skill
     的候选清单就在系统提示的索引列表中。
   ```

2. **第二处：The Rule 规则（第 47-48 行）**

   ```
     **Invoke relevant or requested skills BEFORE any response or action.**
     Even a 1% chance a skill might apply means that you should invoke the skill to
     check.
   
     "relevant skills"从哪里知道？ 从系统提示中注入的 skill 索引列表。AI 看到索引中
     superpowers:brainstorming 的描述 "You MUST use this before any creative
     work"，然后根据当前任务判断是否匹配。
   ```

3. **第三处：流程图——这是最直接的引导（第 50-76 行）**

   ```
     dot
     digraph skill_flow {
         "User message received" -> "Might any skill apply?";
         "About to enter plan mode?" -> "Already brainstormed?";
         "Already brainstormed?" -> "Invoke brainstorming skill" [label="no"];  ←
     ⭐ 直接点名
         "Invoke brainstorming skill" -> "Might any skill apply?";
         "Might any skill apply?" -> "Invoke the skill" [label="yes, even 1%"];
         "Invoke the skill" -> "Announce: 'Using [skill] to [purpose]'";
     }
   
     这是唯一硬编码点名某个具体 skill 的地方：
   
     "About to enter plan mode?" → "Already brainstormed?" → [no] → "Invoke
     brainstorming skill"
   
     这条路径明确告诉我们：在进入 plan mode 之前，如果还没 brainstorm 
     过，必须先调用 brainstorming skill。
   ```

4. **第四处：Red Flags 红旗清单（第 78-93 行）**

   ```
     | "This is just a simple question" | Questions are tasks. Check for skills. |
     | "I need more context first"      | Skill check comes BEFORE clarifying
     questions. |
     | "Let me explore the codebase first" | Skills tell you HOW to explore. Check
     first. |
     | "This doesn't need a formal skill" | If a skill exists, use it. |
     | "The skill is overkill"          | Simple things become complex. Use it. |
   
     这些红旗不是直接说"调用 brainstorming"，而是堵住 AI 可能用来跳过 skill 
     调用的所有借口。配合索引列表中的描述，AI 就无法合理化地跳过。
   
   ```

5. **第五处：`Skill` `Priority` 优先级排序（第 95-103 行）**

   ```
     When multiple skills could apply, use this order:
   
     1. **Process skills first** (brainstorming, systematic-debugging) - these
     determine HOW to approach the task
     2. **Implementation skills second** (frontend-design, mcp-builder) - these
     guide execution
   
     "Let's build X" → brainstorming first, then implementation skills.
     "Fix this bug" → systematic-debugging first, then domain-specific skills.
   
     这里点名了两个具体的 skill（brainstorming、systematic-debugging），并给出了两
     个具体的使用场景示例。这告诉 AI：
   
     - 用户说"做个 X" → 先调 brainstorming
     - 用户说"修个 bug" → 先调 systematic-debugging
   ```

6. **第六处：配合系统提示中的索引列表**

   ```
     using-superpowers 本身不包含其他 skill 的完整列表——它只说"invoke relevant
     skills"。但是系统提示中有一份索引：
   
     - superpowers:brainstorming: You MUST use this before any creative work...
     - superpowers:systematic-debugging: Use when encountering any bug...
     - superpowers:test-driven-development: Use when implementing any feature...
     - superpowers:verification-before-completion: Use when about to claim work is
     complete...
   
     每个 skill 的 description 字段就是触发条件。AI 把 using-superpowers
     的规则（"1% 可能就调用"）和索引列表中的描述（"你必须在创造性工作前使用"）结合
     起来，就能判断当前任务该调用哪个 skill。
   ```

   



## 8、`Token` 成本分析

### 1） 固定成本（每次对话必付）

| 内容                     | 加载时机                       | 大小                     |
| ------------------------ | ------------------------------ | ------------------------ |
| `using-superpowers` 全文 | SessionStart（每次对话都加载） | ~2.5k tokens             |
| 14 个 skill 的索引条目   | 启动时注入到系统提示           | ~0.8k tokens（14 × ~60） |
| **小计**                 |                                | **~3.3k tokens**         |

### 2） 变动成本（按需加载）

| Skill                          | 行数     | 估算 tokens |
| ------------------------------ | -------- | ----------- |
| brainstorming                  | 159      | ~2k         |
| systematic-debugging           | 296      | ~4k         |
| test-driven-development        | 371      | ~5k         |
| subagent-driven-development    | 418      | ~6k         |
| writing-skills                 | 689      | ~8k         |
| verification-before-completion | 139      | ~2k         |
| writing-plans                  | 174      | ~2.5k       |
| using-git-worktrees            | 202      | ~3k         |
| finishing-a-development-branch | 241      | ~3.5k       |
| receiving-code-review          | 213      | ~3k         |
| requesting-code-review         | 103      | ~1.5k       |
| dispatching-parallel-agents    | 185      | ~2.5k       |
| executing-plans                | 70       | ~1k         |
| **全部**                       | **3381** | **~44k**    |

### 3） 典型场景的 token 消耗

| 场景         | 触发的 skill                                                 | 额外 token |
| ------------ | ------------------------------------------------------------ | ---------- |
| 普通对话     | 无                                                           | 0          |
| 修 bug       | systematic-debugging                                         | ~4k        |
| 新功能       | brainstorming → TDD                                          | ~7k        |
| 完整开发流程 | brainstorming → writing-plans → TDD → subagent-driven → verification | ~18k       |
| 全部 skill   | 所有 14 个                                                   | ~44k       |

---

## 9、与其他 Hook 系统的交互

你的 `settings.json` 中还配置了 GitNexus 的 hook：

```json
{
  "hooks": {
    "PreToolUse": [{
      "matcher": "Grep|Glob|Bash",
      "hooks": [{
        "type": "command",
        "command": "node '~/.claude/hooks/gitnexus/gitnexus-hook.cjs'"
      }]
    }],
    "PostToolUse": [{
      "matcher": "Bash",
      "hooks": [{
        "type": "command",
        "command": "node '~/.claude/hooks/gitnexus/gitnexus-hook.cjs'"
      }]
    }]
  }
}
```

这些 hook 和 Superpowers 的 hook **互不干扰**：

- GitNexus hook 在工具调用前后触发（PreToolUse / PostToolUse）
- Superpowers hook 在会话启动时触发（SessionStart）
- 它们共享同一个 hook 框架，但各自独立执行

---

## 10、设计哲学总结

### 1） 三层加载架构

```
┌─────────────────────────────────────────────────┐
│ 第 1 层：SessionStart Hook                       │
│ 自动注入 using-superpowers 全文                    │
│ 作用：教会 AI "何时调用其他 skill"                  │
│ 成本：~2.5k tokens/对话                           │
│ 时机：startup / clear / compact                    │
├─────────────────────────────────────────────────┤
│ 第 2 层：Skill 索引                              │
│ 注入 14 个 skill 的名称+描述                      │
│ 作用：让 AI 知道有哪些 skill 可用                  │
│ 成本：~0.8k tokens/对话                           │
│ 时机：对话启动时                                   │
├─────────────────────────────────────────────────┤
│ 第 3 层：按需加载                                 │
│ 通过 Skill 工具调用加载完整 skill 内容              │
│ 作用：提供具体的执行指令                           │
│ 成本：1k-8k tokens/skill                          │
│ 时机：AI 判断需要时                                │
└─────────────────────────────────────────────────┘
```

### 2） 关键设计决策

| 决策                           | 原因                                                         |
| ------------------------------ | ------------------------------------------------------------ |
| **只自动注入 1 个 skill**      | 14 个全加载要 44k tokens，只注入引导 skill 约 2.5k           |
| **用 hook 而非 AI 自己读文件** | AI 主动读文件是"做事情"，而 skill 规则要求"做事之前先检查 skill"——会有循环矛盾 |
| **同步执行 SessionStart**      | 确保引导内容在第一条用户消息之前就位，避免 AI 在没看到规则时就开始工作 |
| **跨平台双模脚本**             | 一个 run-hook.cmd 同时支持 Windows 和 Unix，简化维护         |
| **平台检测输出不同 JSON**      | 避免 Claude Code 重复注入（它同时读取两种字段但不做去重）    |
| **无扩展名 hook 脚本**         | 避免 Claude Code Windows 自动在命令前加 `bash` 前缀          |
| **bash 原生替换做 JSON 转义**  | 比 `sed`/`awk` 或字符级循环快几个数量级                      |














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



