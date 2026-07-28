---
title: AI_SpecKit
date: 2026-07-17 00:00:00
tags:
- AI
category:
- AI
description: AI_SpecKit
---

**前言**

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)

 https://github.com/github/spec-kit

 https://github.github.io/spec-kit/



# 一、`Spec Kit`

## 1、认识 `Spec Kit`：规格驱动开发

### 1）是什么：不是代码驱动开发，是规格驱动开发

> **`Spec Kit`** 是 **`GitHub` 官方**推出的**规格驱动开发（`SDD`, `Spec-Driven Development`）**工具包。

- **说明：**传统开发中，`PRD` 是代码的脚手架，写完就丢。`SDD` 中，`PRD` 是**源头**，代码是规格的**表达**——规格定义"建什么"，代码只是把规格翻译成某个语言和框架的实现

- **核心哲学**：规格是第一公民，代码是规格的产出物。维护软件 = 维护规格



### 2）为什么现在需要 SDD

三个趋势让 `SDD` 既有必要又有可能：

| 趋势 | 说明 |
| :--- | :--- |
| **AI 能力突破** | 自然语言规格可以可靠地生成可工作的代码——`AI` 不是替代开发者，是放大开发者 |
| **软件复杂度指数增长** | 现代系统集成了几十个服务和框架，靠手动对齐原始意图越来越难 |
| **变化速度加速** | `Pivot` 不再是异常而是常态——传统开发把需求变更当障碍，`SDD` 把它当正常流程 |



### 3）和 `OpenSpec` 对比


| 维度 | Spec-Kit | OpenSpec |
|------|----------|----------|
| 顶层全局规范 | `constitution.md` 项目章程，统一全流程规则 | 轻量全局 config，无强宪法约束 |
| 业务规格组织 | 单需求单文件，分散碎片化 | 按业务域聚合，一份模块总规约 |
| 存量系统迭代 | 不友好，需求越多文档越碎 | 友好，增量变更合并回主文档 |


## 2、安装

安装最新版本：https://github.com/github/spec-kit/releases/tag/v0.13.0

```sh
-- 安装
uv tool install specify-cli --from git+https://github.com/github/spec-kit.git@v0.13.0

-- uv 已经成功装好 specify，但存放工具的目录 /Users/zhangyujin1/.local/bin 没加到系统 PATH，终端找不到命令。
uv tool update-shell
```



### 1）初始化项目

> 作用：在本地生成 `Spec-Kit` 规范项目脚手架，自动生成配置文件、目录模板、AI 集成配置，对接 `Copilot` / `Claude` / `Gemini` / `Cursor` 等 AI 开发工具。

```
specify init --here --integration copilot
# 等价简写
specify init . --integration claude
```

- 含义：**在当前已存在的文件夹内初始化**，不新建目录
- 使用场景：已有老项目，想接入 `Specify` 规范、AI 规则，直接在项目根目录执行
- 注意：不会覆盖你现有代码，仅新增 `spec` 相关配置文件

| 命令                    | 作用                                | 适用人群                  |
| ----------------------- | ----------------------------------- | ------------------------- |
| `--integration copilot` | 适配 GitHub Copilot                 | VSCode + Copilot 用户     |
| `--integration claude`  | 适配 Claude 系列（Claude 3.5/Opus） | 使用 Claude 网页 / 客户端 |
| `--integration gemini`  | 适配 Google Gemini                  | 谷歌 AI 工具使用者        |
| `--integration cursor`  | 适配 Cursor AI 编辑器               | Cursor 专属 IDE 用户      |



### 2）升级 `Specify CLI`

> `self` 系列是**管理 `specify-cli` 本身**，不是管理你的业务项目，用来检查 / 升级工具本体。

| 命令                             | 说明                                                         |
| -------------------------------- | ------------------------------------------------------------ |
| `specify self check`             | 联网查询当前本地版本 + 线上最新正式版，仅打印对比，**不会自动更新** |
| `specify self upgrade --dry-run` | 模拟升级流程，打印会下载哪些包、切换到哪个版本，但**不真实安装覆盖** |
| `specify self upgrade`           | 一键升级到官方最新稳定版，自动拉取源码 / 安装包、替换本地 CLI |
|                                  |                                                              |
| `specify check`                  | 扫描当前目录的 `Specify` 项目配置，做全量检查，诉你项目哪里配置不对 |



## 3、架构设计

### 1）`SDD` 工作流全景

`Spec Kit` 将开发分为 **5 步渐进流程**，每一步产出文档成为下一步的输入：

```
/speckit.constitution → 定义项目宪法（开发原则）
        ↓
/speckit.specify      → 定义规格（建什么、为谁建）
        ↓
/speckit.plan         → 创建技术实现计划（怎么建）
        ↓
/speckit.tasks        → 生成可执行任务列表（拆分工作）
        ↓
/speckit.implement    → 执行所有任务，构建功能
```



### 2）命令总览

| 类别 | 命令 | 产出 |
| :--- | :--- | :--- |
| **宪法** | **`/speckit.constitution`** | `memory/constitution.md`（项目宪法） |
| **规格** | `/speckit.specify` | `specs/[branch]/spec.md`（功能规格） |
| **澄清** | **`/speckit.clarify`** | 消除规格中的模糊点（推荐在 `plan` 前使用） |
| **规划** | `/speckit.plan` | `plan.md` + `data-model.md` + `contracts/` + `research.md` |
| **检查** | **`/speckit.checklist`** | 生成一份**质量检查清单**——相当于"需求的单元测试"。检查规格本身是否**完整、清晰、无歧义、一致**。 |
| **任务** | `/speckit.tasks` | `tasks.md`（可执行任务列表，含并行标记 `[P]`） |
| **分析** | **`/speckit.analyze`** | 跨文档一致性 & 覆盖率分析（`task` 后、`implement` 前使用） |
| **实现** | `/speckit.implement` | 工作代码 + 测试 |
| **对齐** | `/speckit.converge` | 评估代码与规格差距，补充遗漏任务 |



| 路径     | 适用场景   | 命令序列                                                     |
| -------- | ---------- | ------------------------------------------------------------ |
| 短路径   | 小型功能   | `specify` → `plan` → `tasks` → `implement` → `converge`      |
| 完整路径 | 生产级功能 | constitution → specify → clarify → plan → checklist → tasks → analyze → implement → converge |



### 3）调用关系图

```
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.constitution  （仅执行一次，设定项目宪法/指导原则）               │
│  产物: constitution.md                                                  │
│  作用: 后续所有阶段对照此原则进行评估                                      │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.specify  （定义功能规格）                                       │
│  ├── 自动创建语义分支名                                                  │
│  ├── 自动编号（001, 002, ...）                                           │
│  └── 产物: spec.md                                                      │
│  ⚠️ 只描述"做什么/为什么"，不涉及技术栈                                   │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.clarify  （可选，可多次执行，澄清模糊点）                        │
│  ├── 提出最多 5 个针对性问题                                              │
│  └── 回答合并回 spec.md，消除歧义                                        │
│  触发时机: 规格有歧义时 / analyze 发现需求缺口时                           │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.plan  （技术实现计划）                                          │
│  ├── 读取: spec.md + constitution.md                                    │
│  ├── 宪法合规检查（Phase -1 Gates）                                      │
│  └── 产物: plan.md + data-model.md + contracts/ + research.md           │
│  ⚠️ 唯一应该讨论技术细节的步骤                                            │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.checklist  （可选，质量门禁）                                    │
│  ├── 产物: 质量检查清单（"需求的单元测试"）                                │
│  └── 发现缺口？→ 回到 /speckit.clarify 或 /speckit.specify 收紧规格       │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.tasks  （生成任务列表）                                         │
│  ├── 读取: plan.md + data-model.md + contracts/                         │
│  ├── 产物: tasks.md（含并行标记 [P]，按依赖排序）                         │
│  └── 阶段: Setup → Foundational → User Stories → Polish                 │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.analyze  （可选，跨文档一致性检查，只读）                         │
│  ├── 对照: spec.md ↔ plan.md ↔ tasks.md                                 │
│  ├── 产物: 一致性报告（冲突/缺口/歧义）                                   │
│  └── 发现问题？→ 回到对应源头修复 → 重新 analyze                          │
│      需求问题 → specify/clarify                                         │
│      设计问题 → plan                                                    │
│      任务问题 → tasks                                                   │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.implement  （执行实现，生成代码）                                │
│  ├── 按 tasks.md 依赖顺序逐阶段执行                                      │
│  ├── 尊重并行标记 [P]，可并行任务并行执行                                  │
│  ├── 产物: 实际代码文件                                                  │
│  └── 大型功能可分阶段执行（避免超出代理上下文窗口）                         │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.converge  （可选，对齐检查）                                    │
│  ├── 对照: 代码 ↔ spec.md ↔ plan.md ↔ tasks.md                          │
│  ├── 产物: 严重性分级的完整性报告                                         │
│  ├── ✅ 已收敛 → tasks.md 逐字节不变 → 完成，开 PR                       │
│  └── ⚠️ 有缺口 → 追加新任务到 tasks.md（只追加，不编辑不删除）             │
│       → 再次 /speckit.implement → 再次 /speckit.converge                 │
│       → 循环直到收敛                                                     │
└───────────────────────────────┬─────────────────────────────────────────┘
                                ▼
┌─────────────────────────────────────────────────────────────────────────┐
│  /speckit.taskstoissues  （可选，转 GitHub Issues）                       │
│  └── 将 tasks.md 中的任务同步为 GitHub Issues，便于团队协作追踪             │
└─────────────────────────────────────────────────────────────────────────┘
```







# 二、`SDD` 工作流详解

## 1、`Step 1`：`/speckit.constitution`——建立项目宪法

> 创建或更新**项目宪法**——一组指导原则，后续所有阶段都会对照这些原则进行评估。

### 1）是什么

> `/speckit-constitution` 用来**创建或更新项目宪章（Constitution）**，也就是项目最高级的开发准则文档，存放在 。

- 何时执行：  项目最开始，仅执行一次。为后续所有步骤建立评估基准

- 产物：项目宪法/原则文档 `.specify/memory/constitution.md` ，后续每一步都会对照这些原则进行评估
- 作用：为后续所有 `speckit` 命令提供**顶层约束**：任何规格、计划、任务在生成时都要遵守宪章里定义的核心原则。



### 2）宪章里写什么

> 从模板（`.specify/templates/constitution-template.md`）看，包含四大部分

```
# [PROJECT_NAME] 项目宪章
<!-- 示例：Spec Constitution、TaskFlow Constitution 等 -->

## 核心原则

### [PRINCIPLE_1_NAME]
<!-- 示例：I. 库优先（Library-First） -->
[PRINCIPLE_1_DESCRIPTION]
<!-- 示例：每个特性都要先做成独立的库；库必须自包含、可独立测试、有完整文档；必须有明确用途——不允许仅为组织代码而存在的库 -->

### [PRINCIPLE_2_NAME]
<!-- 示例：II. CLI 接口 -->
[PRINCIPLE_2_DESCRIPTION]
<!-- 示例：每个库都通过 CLI 暴露功能；采用文本进/文本出协议：stdin/args → stdout、错误 → stderr；同时支持 JSON 与人类可读格式 -->

### [PRINCIPLE_3_NAME]
<!-- 示例：III. 测试先行（不可妥协） -->
[PRINCIPLE_3_DESCRIPTION]
<!-- 示例：TDD 强制执行：先写测试 → 用户批准 → 测试失败 → 再实现；严格遵循「红-绿-重构」循环 -->

### [PRINCIPLE_4_NAME]
<!-- 示例：IV. 集成测试 -->
[PRINCIPLE_4_DESCRIPTION]
<!-- 示例：必须做集成测试的场景：新库的契约测试、契约变更、服务间通信、共享 schema -->

### [PRINCIPLE_5_NAME]
<!-- 示例：V. 可观测性；VI. 版本管理与破坏性变更；VII. 简洁性 -->
[PRINCIPLE_5_DESCRIPTION]
<!-- 示例：通过文本 I/O 保证可调试性；必须使用结构化日志；或：采用 MAJOR.MINOR.BUILD 版本号格式；或：保持简单，遵循 YAGNI 原则 -->

## [SECTION_2_NAME]
<!-- 示例：附加约束、安全要求、性能标准 等 -->

[SECTION_2_CONTENT]
<!-- 示例：技术栈要求、合规标准、部署策略 等 -->

## [SECTION_3_NAME]
<!-- 示例：开发流程、评审流程、质量门禁 等 -->

[SECTION_3_CONTENT]
<!-- 示例：代码评审要求、测试门禁、部署审批流程 等 -->

## 治理（Governance）
<!-- 示例：宪章高于所有其他实践；修订需要有文档、审批、迁移方案 -->

[GOVERNANCE_RULES]
<!-- 示例：所有 PR/评审必须验证合规性；引入复杂度必须有理由；运行时开发指引参见 [GUIDANCE_FILE] -->

**版本**：[CONSTITUTION_VERSION] | **首次批准**：[RATIFICATION_DATE] | **最近修订**：[LAST_AMENDED_DATE]
<!-- 示例：版本：2.1.1 | 首次批准：2025-06-13 | 最近修订：2025-07-16 -->
```



### 3）与 `CLAUDE.md` 的关系

> **没有 `CLAUDE.md` 也能用 `constitution.md`**：宪章是 `speckit` 体系内的约束，与 `Agent` 层的指令文件独立。

| 文件                              | 作用                                                  | 谁读                       |
| --------------------------------- | ----------------------------------------------------- | -------------------------- |
| `CLAUDE.md`                       | `AI` **行为指令**（怎么加载知识、什么时候用什么工具） | `AI` 每次对话前读          |
| `.specify/memory/constitution.md` | 项目的**开发准则**（原则、约束、门禁）                | `speckit` 命令每次执行前读 |



## 2、`Step 2`：`/speckit.specify` — 描述要构建什么

### 1）是什么

> 把你的**模糊想法**转化为**结构化规格文档**。聚焦"建什么"和"为什么"，不涉及"怎么建"

**何时执行：**

- 开始一个**新功能**时的**第一个必须执行的步骤**
- 如果后续 `analyze` 发现需求问题，可以回来重新执行

产物：**`spec.md`** — 功能规格说明书



### 2）自动做的事情

1. **自动编号**：扫描已有规格，确定下一个功能编号（001, 002, ...）
2. **自动创建分支**：从描述生成语义分支名（如 `003-chat-system`）
3. **模板化生成**：复制并定制功能规格模板
4. **目录结构**：创建 `specs/[branch-name]/` 目录



### 3）使用方式

```bash
/speckit.specify Build an application that can help me organize my photos in separate photo albums. Albums are grouped by date and can be re-organized by dragging and dropping on the main page. Albums are never in other nested albums. Within each album, photos are previewed in a tile-like interface.
```



### 4）规格模板的约束力

> 规格模板不是空白文档——它**约束 `LLM` 的行为**，确保产出高质量规格

| 约束 | 效果 |
| :--- | :--- |
| **聚焦 WHAT & WHY，禁止 HOW** | 防止 `LLM` 跳到技术选型，保持规格的技术独立性 |
| **强制 `[NEEDS CLARIFICATION]` 标记** | 不让 LLM 猜测——遇到模糊必须标注，如 `[NEEDS CLARIFICATION: auth method not specified]` |
| **结构化检查清单** | 给 LLM 一个 `QA` 框架，系统性地自我审查 |
| **禁止推测性功能** | 每个 Feature 必须追溯到具体用户故事 |

> **原理**：模板把 LLM 从"创意写手"变成"纪律规格工程师"，把创造力引导到高质量、可执行的规格上



## 3、`Step 3`：`/speckit.clarify` — 消除歧义

### 1）是什么

在规划之前消除歧义，避免在模糊的基础上做设计。针对规格中**不明确的地方**提出最多 **5 个针对性问题**，将你的回答**编码回 `spec.md`**。

### 2）何时执行

- 在 `specify` 之后、`plan` 之前
- 可以**多次执行**，每次聚焦不同领域
- 如果后续 `/speckit.analyze` 发现需求缺口，也可以回来执行
- 如果跳过这步直接 `plan`，可能导致设计建立在错误假设上，后期返工成本极高。



### 3）怎么执行

```
/speckit.clarify                          # 无参数，广泛扫描

/speckit.clarify <聚焦领域描述>            # 指定关注点
```



### 4）产物

- 提出最多 5 个针对性问题
- 你的回答被**合并回 `spec.md`**，消除歧义





## 4、`Step 4`：`/speckit.plan`——创建技术实现计划

### 1）是什么

把功能规格转化为**技术实现计划**——定义"怎么建"。Agent 读取规格 + 宪法，确保技术选型符合项目原则



### 2）何时执行

- 规格明确后（`specify` + `clarify` 完成后）
- 如果后续 `analyze` 发现设计问题，可以回来重新执行



### 3）产出物

| 文件 | 内容 |
| :--- | :--- |
| `plan.md` | 技术实现计划（高层级、可读） |
| `data-model.md` | 数据模型（实体、关系、字段） |
| `contracts/` | `API` 契约（REST 端点、WebSocket 事件等） |
| `research.md` | 技术研究结果（库对比、性能基准等） |
| `quickstart.md` | 快速验证场景 |





## 5、`Step 5`: `/speckit.checklist` — 验证规格质量

### 1）是什么

生成一份**质量检查清单**——相当于"需求的单元测试"。它不测试代码，而是检查规格本身是否**完整、清晰、无歧义、一致**。



### 2）何时执行

- 在 `plan` 之后、`tasks` 之前
- 作为质量门禁使用



### 3）产物：**质量检查清单**

- "拖拽规则是否为每一列都定义了？"
- "被删除的已分配用户的行为是否指定了？"
- "评论是否有长度限制？"
- "并发编辑时的冲突处理是否明确？"





## 6、`Step 6`：`/speckit.tasks`——生成可执行任务

### 1）是什么

从实现计划生成**可执行任务列表**——把大计划拆成可独立执行的小任务

### 2）产出特点

- 读取 `plan.md`（必须）+ `data-model.md` + `contracts/` + `research.md`（可选）
- 从契约、实体、场景推导具体任务
- 标记可并行任务 `[P]`，划定安全并行组
- 产出 `tasks.md`，可直接由 `Task Agent` 执行



### 3）使用方式

```bash
/speckit.tasks
```

| 阶段         | 描述       | 说明                                  |
| ------------ | ---------- | ------------------------------------- |
| Setup        | 共享基础   | 项目脚手架、环境配置                  |
| Foundational | 阻塞性前置 | 阻塞性前置条件（数据模型、基础 CRUD） |
| User Story 1 |            | 按优先级排列的第一个用户故事          |
| User Story 2 |            | 第二个用户故事                        |
| ...          |            | 更多用户故事                          |
| Polish       |            | 跨切面关注点（文档、优化、最终调整）  |







## 7、`Step 7`: `/speckit.analyze` — 跨文档一致性检查

### 1）是什么

对 `spec.md`、`plan.md`、`tasks.md` 进行**只读的跨文档一致性和质量分析**。



### 2）何时执行

- 在 `tasks` 之后、`implement` 之前（**最关键时机**——此时代码还没写，修改成本最低）
- 也可以在 `implement` 之后再跑一次作为额外审查



### 3）产物：一致性分析报告

> 修复后**重新运行** `/speckit.analyze`，直到报告干净。

- 任务是否有对应的需求（无孤立任务）
- 设计选择是否与规格矛盾
- 需求是否有遗漏（规格中有但任务中没覆盖）
- 文档间的歧义和冲突



### 4）关键特性

- **完全只读** — 永远不会编辑任何文件
- 可以**建议修复方案**供你审批
- 发现问题后的修复路径：

| 问题类型 | 回到哪一步修复                           |
| :------- | :--------------------------------------- |
| 需求问题 | `/speckit.specify` 或 `/speckit.clarify` |
| 设计问题 | `/speckit.plan`                          |
| 任务问题 | `/speckit.tasks`（重新生成）             |





## 8、`Step 8`：`/speckit.implement`——执行实现

### 1）是什么

按任务列表逐个执行，构建功能。Agent 读取规格 + 计划 + 任务 + 宪法作为上下文



### 2）何时执行

- 所有文档验证通过后（`analyze` 报告干净）
- `converge` 发现缺口后，需要再次执行



### 3）产物

- **实际代码文件**（项目结构、源代码、配置文件、测试等）
- 按依赖顺序逐步生成
- 尊重并行标记（可并行的任务会并行执行）



### 4）关键原则

- 每个阶段完成后**验证其工作正常**再继续下一阶段
- 大型功能分阶段执行，避免一次性压垮代理的上下文



## 9、`Step 9`: `/speckit.converge` — 验证实现完整性

### 1）是什么

将代码库与 spec、plan、tasks 进行对照，确认**没有遗漏**。



### 2）何时执行

- `/speckit.implement` 执行完毕后
- 每次追加任务并重新 `implement` 后，再次执行



### 3）产物-结果 A：✅ 已收敛

- `tasks.md` **逐字节不变**
- 你完成了！可以进行代码审查或开 PR



### 4）产物-结果 B：⚠️ 发现缺口

- 在 `tasks.md` 中追加一个 **"`Convergence`" 部分**，包含新任务
- 告诉你追加了多少个任务
- 你需要：
  1. 再次执行 `/speckit.implement`（完成新追加的任务）
  2. 再次执行 `/speckit.converge`
  3. 每一轮发现的缺口会越来越少
  4. 重复直到报告"已收敛"



### 5）关键特性

- **只追加，不编辑不删除** — 永远不会修改已有代码
- 唯一的写操作是向 `tasks.md` 追加新任务
- 必须在 `/speckit.implement` 对当前 `tasks.md` 执行完毕后才能运行







# 三、底层原理与设计哲学

## 1、权力翻转：代码不再为王

### 1）传统开发的问题

```
PRD → 设计文档 → 代码（代码是真相）
         ↓
规格沦为脚手架，写完就丢
代码演进后，规格永远跟不上
```



### 2）`SDD` 的翻转

**类比**：

- 传统开发像盖房子——图纸（规格）盖完就丢，房子（代码）是真相。    

- `SDD `像工厂——图纸（规格）永远是真相，产品（代码）只是图纸的批量产出物

```
规格（规格是真相）→ 实现计划 → 代码（代码是规格的表达）
         ↓
维护软件 = 维护规格
调试 = 修复规格和计划（它们生成了错误代码）
重构 = 为清晰度重组规格
```



## 2、为什么 `Spec Kit` 能工作：三个核心原理

### 1）原理一：模板约束 > 自由生成

> **问题**：让 AI 自由生成规格，产出质量方差极大——有时完整，有时遗漏关键维度    
>
> **Spec Kit 解法**：规格模板约束 AI 的生成空间，强制聚焦 WHAT & WHY、标记模糊点、禁止推测性功能

类比：自由写作 vs 结构化填表。填表产出的信息更完整，因为框架已经定义了必须覆盖的维度



### 2）原理二：宪法门禁 >事后审查

> **问题**：AI 生成代码后再审查质量，发现架构问题已晚——需要大量返工       
>
> **Spec Kit 解法**：Phase -1 门禁在代码生成前就检查架构合规性——简洁门禁、反抽象门禁、集成优先门禁

类比：事后体检 vs 定期体检。定期体检在问题变严重前就发现



### 3）原理三：规格可执行 > 规格仅参考

> **问题**：传统 `PRD` 写完就束之高阁，代码演进后 `PRD` 永远跟不上    
>
> **`Spec Kit` 解法**：规格直接生成实现计划和代码——规格和代码之间没有鸿沟，只有变换

类比：参考图纸 `vs` 执行图纸。参考图纸仅供参考，执行图纸直接驱动生产





# 三、扩展、预设与 `Bundle`

> `Spec Kit` 提供三层定制，从轻到重：

| 优先级 | 类型                  | 位置                             | 作用                       |
| :----- | :-------------------- | :------------------------------- | :------------------------- |
| ⬆ 最高 | **项目本地覆盖**      | `.specify/templates/overrides/`  | 单项目的一次性调整         |
| 中     | **预设（Preset）**    | `.specify/presets/templates/`    | 定制已有工作流的格式和行为 |
| 低     | **扩展（Extension）** | `.specify/extensions/templates/` | 添加全新的命令和能力       |
| ⬇ 最低 | **`Spec Kit Core`**   | `.specify/templates/`            | 内置 SDD 命令和模板        |

> 模板在**运行时**解析——`Spec Kit` 从上到下查找，使用第一个匹配





## 1、扩展（`Extension`）——添加新能力

> 扩展是**添加 Spec Kit 原本没有的新命令和新工作流阶段**。它不改变已有命令的行为，而是**新增**能力。

**使用场景**：添加领域专用工作流、集成外部工具（Jira）、添加开发阶段（代码审查）

```bash
# 搜索可用扩展
specify extension search

# 安装扩展
specify extension add <extension-name>
```



### 1）使用场景

| 场景               | 说明                                                   |
| ------------------ | ------------------------------------------------------ |
| 添加领域专用工作流 | 如：医疗合规审查、金融风控检查                         |
| 集成外部工具       | 如：Jira 同步、Confluence 文档生成                     |
| 添加开发阶段       | 如：代码审查（code-review）、安全扫描（security-scan） |
| 添加新命令         | 如：`/speckit.review`、`/speckit.deploy`               |





### 2）怎么操作

```
# 1. 搜索可用扩展
specify extension search

# 输出示例：
# NAME              DESCRIPTION                          AUTHOR
# jira-sync         Sync tasks.md to Jira issues         @acme
# code-review       Add /speckit.review command          @github
# security-scan     OWASP-based security checklist       @owasp
# i18n-workflow     Internationalization workflow        @i18n-team

# 2. 安装扩展
specify extension add jira-sync

# 3. 查看已安装扩展
specify extension list

# 4. 移除扩展
specify extension remove jira-sync
```



### 3）安装后发生了什么

```
.specify/
├── extensions/
│   └── jira-sync/
│       ├── templates/
│       │   └── jira-sync.md          ← 新命令的模板
│       ├── manifest.json             ← 扩展元数据
│       └── scripts/
│           └── sync-to-jira.sh       ← 辅助脚本
```



### 4）实际案例1：添加代码审查阶段

> 安装后，你的工作流多了一个新命令：`/speckit.review`     →  

```
specify extension add code-review
```

`/speckit.review` 会：

- 读取 `spec.md` + `plan.md` + 生成的代码
- 对照宪法原则检查代码质量
- 输出 `review-report.md`，包含问题列表和修复建议





## 2、预设（`Preset`）——定制已有工作流

> 预设**不添加新命令**，而是**改变已有命令的行为和输出格式**。它通过覆盖模板文件来定制 `specify`、`plan`、`tasks` 等命令的输出。



### 1）使用场景

| 场景             | 说明                                                |
| ---------------- | --------------------------------------------------- |
| 强制合规规格格式 | 如：医疗行业要求 spec 必须包含 FDA 合规章节         |
| 使用领域术语     | 如：金融项目用"头寸"而非"持仓"，用"清算"而非"结算"  |
| 应用组织标准     | 如：公司要求 plan.md 必须包含"风险评估"和"回滚方案" |
| 本地化整个工作流 | 如：所有输出改为中文、日文                          |
| 改变 tasks 格式  | 如：任务必须包含估时、负责人字段                    |

### 2）怎么操作

```
# 1. 搜索可用预设
specify preset search

# 输出示例：
# NAME              DESCRIPTION                              AUTHOR
# fintech-spec      Financial compliance spec format         @fintech-org
# zh-cn             中文本地化预设                            @spec-kit-cn
# agile-tasks       Agile task format with story points      @agile-team
# security-first    OWASP-aligned plan template              @security-team

# 2. 安装预设
specify preset add fintech-spec

# 3. 查看已安装预设
specify preset list

# 4. 移除预设
specify preset remove fintech-spec
```



### 3）安装后发生了什么

```
.specify/
├── presets/
│   └── fintech-spec/
│       ├── templates/
│       │   ├── specify.md            ← 覆盖 Core 的 specify 模板
│       │   ├── plan.md               ← 覆盖 Core 的 plan 模板
│       │   └── tasks.md              ← 覆盖 Core 的 tasks 模板
│       └── manifest.json
```





## 3、项目本地覆盖（`Overrides`）—— 一次性调整

> 最高优先级的覆盖层，用于**单个项目的一次性特殊调整**。不通过 CLI 安装，而是**手动创建文件**。

### 1）使用场景

- 某个项目需要特殊的 spec 格式，但不值得做成预设
- 临时调试某个模板的行为
- 项目有特殊约束，只影响这一个项目



### 2）怎么操作

```
# 手动创建覆盖目录
mkdir -p .specify/templates/overrides/

# 复制 Core 模板并修改
cp .specify/templates/specify.md .specify/templates/overrides/specify.md

# 编辑覆盖模板
vim .specify/templates/overrides/specify.md
```



### 3）实际案例

你的项目要求每个 `spec` 必须包含"性能预算"章节：

```
!-- .specify/templates/overrides/specify.md -->
# 在原有模板基础上追加：

## 性能预算（必填）
- 首屏加载时间: ≤ ___ms
- API 响应时间 P99: ≤ ___ms
- 内存占用上限: ≤ ___MB
- 并发用户数: ≥ ___
```



## 4、`Bundle`——角色包一站式安装

> Bundle 是**面向特定角色的扩展+预设+工作流的打包组合**。一个命令搞定某个角色所需的全部定制。



### 1）使用场景

| 角色       | Bundle 包含内容                                |
| ---------- | ---------------------------------------------- |
| 产品经理   | 需求分析预设 + 用户故事扩展 + 优先级排序工作流 |
| 业务分析师 | 合规规格预设 + 领域术语包 + 影响分析扩展       |
| 安全研究员 | 安全扫描扩展 + OWASP 预设 + 威胁建模工作流     |
| 开发者     | 代码审查扩展 + 敏捷任务预设 + CI/CD 集成       |
| QA 工程师  | 测试用例生成扩展 + 覆盖率预设 + 回归分析工作流 |



### 2）怎么操作

```
# 1. 搜索可用 Bundle
specify bundle search

# 输出示例：
# ID                  NAME                DESCRIPTION                    INCLUDES
# pm-essentials       PM Toolkit          Product manager workflow       2 ext + 1 preset
# security-audit      Security Auditor    Security-focused development   3 ext + 2 presets
# fullstack-dev       Full-Stack Dev      Developer productivity pack    2 ext + 1 preset
# ba-compliance       BA Compliance       Business analyst + compliance  1 ext + 2 presets

# 2. 查看 Bundle 详情（安装前先看包含什么）
specify bundle info security-audit

# 输出示例：
# Bundle: Security Auditor
# Description: Security-focused development workflow
# Version: 1.2.0
# Author: @security-team
# 
# Includes:
#   Extensions:
#     - security-scan (v2.1.0) — OWASP Top 10 checklist generation
#     - threat-model (v1.0.3) — STRIDE threat modeling
#     - dep-audit (v1.5.0) — Dependency vulnerability scanning
#   Presets:
#     - security-first-plan (v1.1.0) — Security sections in plan.md
#     - secure-tasks (v1.0.0) — Security tasks auto-injection
# 
# Total size: 45KB
# Compatibility: spec-kit >= 0.9.0

# 3. 安装 Bundle
specify bundle install security-audit

# 4. 查看已安装 Bundle
specify bundle list

# 输出示例：
# ID                VERSION    INSTALLED    STATUS
# security-audit    1.2.0      2026-07-20   ✅ Active

# 5. 更新 Bundle
specify bundle update security-audit

# 6. 移除 Bundle（会同时移除其中所有扩展和预设）
specify bundle remove security-audit
```



### 3）安装后发生了什么

```
.specify/
├── extensions/
│   ├── security-scan/          ← Bundle 中的扩展 1
│   ├── threat-model/           ← Bundle 中的扩展 2
│   └── dep-audit/              ← Bundle 中的扩展 3
├── presets/
│   ├── security-first-plan/    ← Bundle 中的预设 1
│   └── secure-tasks/           ← Bundle 中的预设 2
├── bundles/
│   └── security-audit.json     ← Bundle 元数据（记录包含哪些组件）
```



### 4）实际案例1-**安全研究员的一天**

```
# 早上：安装安全审计 Bundle
specify bundle install security-audit

# 开始新功能
/speckit.specify Build a payment processing module...

# 此时 spec.md 自动包含安全相关章节（来自 security-first-plan 预设）：
# ## 安全需求
# ### 认证与授权
# ### 数据加密要求
# ### 输入验证规则

/speckit.plan Use Stripe API with tokenization...

# plan.md 自动包含威胁模型章节（来自 threat-model 扩展）：
# ## 威胁模型 (STRIDE)
# ### Spoofing: ...
# ### Tampering: ...
# ### Repudiation: ...

/speckit.tasks

# tasks.md 自动注入安全任务（来自 secure-tasks 预设）：
# - [ ] T015: Implement rate limiting on payment endpoints [Security]
# - [ ] T016: Add input sanitization for card data [Security]
# - [ ] T017: Configure audit logging for all transactions [Security]

# 实现后，运行安全扫描（来自 security-scan 扩展）
/speckit.security-scan

# 产物：security-report.md
# - OWASP Top 10 逐项检查结果
# - 依赖漏洞列表（来自 dep-audit 扩展）
# - 修复建议
```





## 5、`FAQ`

### 1）对比

| 维度             | Extension（扩展）       | Preset（预设）         | Bundle（角色包）               |
| ---------------- | ----------------------- | ---------------------- | ------------------------------ |
| 本质             | 添加新命令/新阶段       | 改变已有命令的输出格式 | 扩展+预设的打包组合            |
| 是否新增命令     | ✅ 是                    | ❌ 否                   | 取决于包含的扩展               |
| 是否改变已有命令 | ❌ 否                    | ✅ 是                   | 取决于包含的预设               |
| 安装方式         | `specify extension add` | `specify preset add`   | `specify bundle install`       |
| 存放位置         | `.specify/extensions/`  | `.specify/presets/`    | 分散到 extensions/ 和 presets/ |
| 可单独使用       | ✅                       | ✅                      | ✅（但本质是组合）              |
| 移除影响         | 新命令消失              | 恢复 Core 默认格式     | 所有组件一起移除               |
| 典型用户         | 平台工程师、DevOps      | 技术负责人、架构师     | 任何角色                       |



### 2）如何选择

| 目标                       | 使用      |
| :------------------------- | :-------- |
| 添加全新命令或工作流       | Extension |
| 集成外部工具或服务         | Extension |
| 定制规格、计划、任务的格式 | Preset    |
| 强制组织或合规标准         | Preset    |
| 一个命令配置整个角色       | Bundle    |







# 五、实战场景

## 1、场景一：全新项目——从想法到代码

> **项目背景**：你有一个相册管理应用的想法，需要从零开始

### 1）完整流程

```bash
# ===== Step 1: 建立项目宪法 =====
/speckit.constitution Create principles focused on simplicity, test-first development, and minimal dependencies

# ===== Step 2: 定义功能规格 =====
/speckit.specify Build an application that can help me organize my photos in separate photo albums. Albums are grouped by date and can be re-organized by dragging and dropping. Photos are previewed in a tile-like interface.

# 此命令自动做了：
# - 创建分支 "001-photo-albums"
# - 生成 specs/001-photo-albums/spec.md
# - 填充结构化规格模板

# ===== 可选 Step: 澄清模糊点 =====
/speckit.clarify
# 消除 [NEEDS CLARIFICATION] 标记

# ===== Step 3: 创建技术实现计划 =====
/speckit.plan The application uses Vite with minimal libraries. Vanilla HTML, CSS, and JavaScript. Metadata stored in local SQLite database.

# 产出：
# - specs/001-photo-albums/plan.md
# - specs/001-photo-albums/data-model.md
# - specs/001-photo-albums/contracts/
# - specs/001-photo-albums/research.md

# ===== Step 4: 生成任务列表 =====
/speckit.tasks

# 产出：specs/001-photo-albums/tasks.md
# 含并行标记 [P]，可直接执行

# ===== 可选 Step: 一致性分析 =====
/speckit.analyze
# 检查 spec + plan + tasks 的一致性和覆盖率

# ===== Step 5: 执行实现 =====
/speckit.implement

# ===== 可选 Step: 对齐检查 =====
/speckit.converge
# 评估代码与规格的对齐度，补充遗漏任务
```



### 2）项目目录结构

```
photo-app/
├── .specify/                           # Spec Kit 配置
│   ├── templates/                      # 核心模板
│   ├── presets/templates/              # 预设覆盖
│   ├── extensions/templates/           # 扩展模板
│   └── memory/
│       └── constitution.md             # 项目宪法
├── specs/
│   └── 001-photo-albums/
│       ├── spec.md                     # 功能规格
│       ├── plan.md                     # 技术实现计划
│       ├── data-model.md              # 数据模型
│       ├── contracts/                  # API 契约
│       ├── research.md                # 技术研究
│       ├── quickstart.md             # 快速验证
│       └── tasks.md                   # 任务列表
└── ...
```

### 3）关键提醒

| 规则 | 说明 |
| :--- | :--- |
| **宪法必须先建** | 所有后续命令都会参考宪法，没有宪法就没有门禁 |
| **`specify` 聚焦 `WHAT`** | 不要在 specify 中写技术选型，那是 plan 的事 |
| **`clarify `在 `plan` 前用** | 先消除模糊，再做计划，避免基于猜测做技术决策 |
| **`implement` 不要求 `spec` 完美** | 规格足够稳定就可以开始实现，不要求完美 |



## 2、场景二：项目迭代 

> **项目背景**：已有线上项目，需要添加新功能——比如给相册应用加"分享"

```bash
# ===== Step 1: 新功能规格 =====
/speckit.specify Add photo album sharing feature. Users can share albums via a public link. Shared albums are viewable by anyone with the link, but only the owner can edit.

# 此命令自动做了：
# - 创建分支 "002-album-sharing"
# - 生成 specs/002-album-sharing/spec.md

# ===== Step 2: 技术计划 =====
/speckit.plan Sharing uses server-side generated UUID links. No authentication required for viewers. Owner authentication via existing JWT system.

# ===== Step 3: 任务列表 =====
/speckit.tasks

# ===== Step 4: 实现 =====
/speckit.implement

# ===== Step 5: 对齐检查 =====
/speckit.converge
```



## 3、场景三：多实现探索——创意模式

> **项目背景**：同一个规格，你想探索不同的技术实现方案——比如"相册管理"用 React vs Vue vs Vanilla JS 分别实现

### 1）探索流程

```bash
# ===== 共享同一规格 =====
/speckit.specify Photo album organizer with date grouping and drag-drop reordering

# ===== 方案 A: Vanilla JS =====
/speckit.plan Use Vite with vanilla HTML, CSS, and JavaScript. Minimal libraries. SQLite for metadata.

# ===== 方案 B: React =====
/speckit.plan Use React with Vite. Component-based architecture. Redux for state management. SQLite for metadata.

# ===== 方案 C: Vue =====
/speckit.plan Use Vue 3 with Vite. Composition API. Pinia for state. SQLite for metadata.
```

> **原理**：`SDD` 的规格是技术独立的——同一规格可以生成多个实现。这支持**What-if/模拟实验**："如果我们要改实现来促进某个业务需求，怎么实现和实验？"



## 4、场景四：企业合规——`Bundle` 快速配置

> **项目背景**：团队有合规要求，需要给不同角色配置不同的工作流

### 1）`Bundle` 快速配置

```bash
# 产品经理角色
specify bundle install product-manager

# 安全研究员角色
specify bundle install security-researcher

# 开发者角色
specify bundle install developer

# 查看 Bundle 详情（先看再装）
specify bundle info product-manager
```



### 2）企业合规预设

```bash
# 搜索合规相关预设
specify preset search compliance

# 安装合规预设——强制规格包含法规追溯性
specify preset add compliance-traceability
```



## 5、常见问题

| 问题 | 答案 |
| :--- | :--- |
| 必须先建宪法吗？ | **是**。宪法是所有后续命令的约束基础 |
| specify 能写技术选型吗？ | **不能**。specify 只写 WHAT & WHY，技术选型在 plan 中 |
| 规格必须完美才能 implement 吗？ | 不必须。规格足够稳定就可以开始，早期实现可以探索性 |
| 可以改宪法吗？ | 可以修改应用，但原则不可变。修改需记录理由+审批 |
| 和 BMAD 能一起用吗？ | **能**。Spec Kit 产出高质量规格，BMAD 管全流程 |
| 支持 Python 项目吗？ | 支持。Spec Kit 与语言无关 |
| 如何查看所有可用 AI 集成？ | `specify integration list` |
| 如何查看已安装扩展/预设？ | `specify extension list` / `specify preset list` |









