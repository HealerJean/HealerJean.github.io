---
title: AI_skills_find_skill
date: 2026-05-29 00:00:00
tags: 
- AI
category: 
- AI
description: AI_skills_find_skill
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# `Find Skills`：`AI` `Agent` 技能发现与安装指南

> **一句话定位**：`Skills CLI` 是 `AI Agent` 开放技能生态的包管理器——就像 `npm` 之于 `Node.js`，`pip` 之于 `Python`，`npx skills` 让你一键发现、安装和管理 `Agent` 的模块化能力扩展。



# 一、开篇：为什么需要 Find Skills

## 1、核心痛点

`AI Agent`（如 `Claude Code`）开箱即用，具备通用的代码理解、生成和推理能力。但在特定领域，通用能力往往不够深入——你需要的是**领域专家的知识和最佳实践**，而不是一个"什么都懂一点"的通才。

| 挑战         | 具体表现                                 |
| ------------ | ---------------------------------------- |
| 知识深度不足 | 通用模型对特定框架/工具的细节掌握有限    |
| 最佳实践缺失 | 知道"怎么做"，但不知道"最佳做法"是什么   |
| 重复劳动     | 每次都要从零开始研究领域知识             |
| 质量不稳定   | 没有经过社区验证的方案，输出质量参差不齐 |

## 2、解决方案

**`Skills CLI`** (`npx skills`) 将社区沉淀的领域知识封装为可安装的**技能包（`Skill`）**，让 `Agent` 在几秒内获得专业能力升级：

```
通用 Agent  +  专业 Skill  =  领域专家级 Agent
```



## 3、开放生态

```
┌─────────────────────────────────────────────┐
│              skills.sh 平台                  │
│          （技能市场 + 排行榜）                 │
├─────────────────────────────────────────────┤
│  GitHub 仓库托管  ←→  社区贡献  ←→  质量验证   │
├─────────────────────────────────────────────┤
│         Skills CLI (npx skills)              │
│      发现 / 安装 / 更新 / 创建 技能            │
├─────────────────────────────────────────────┤
│         AI Agent（Claude Code 等）            │
│         加载技能 → 获得专业能力                 │
└─────────────────────────────────────────────┘
```

- **skills.sh 平台**：https://skills.sh/ — 按安装量排名，类似 npmjs.com，每个技能有独立详情页

---



## 4、技能分类

| 分类（中文） | 明星技能                                 | 典型需求场景                                                 |
| ------------ | ---------------------------------------- | ------------------------------------------------------------ |
| 前端开发     | react-best-practices、frontend-schema    | 前端框架最佳实践、前端接口契约定义                           |
| 后端开发     | openapi-builder、backend-query-optimize  | OpenAPI 文档生成、数据库查询性能优化                         |
| 测试自动化   | api-auto-test、spec-validator            | 接口自动化测试、契约规范校验                                 |
| 运维部署     | docker-compose-gen、mcp-config-generator | 容器编排生成、MCP 服务配置快速创建                           |
| 文档生成     | spec-to-md、skill-readme-gen             | OpenAPI 转接口文档、自定义技能配套文档生成                   |
| 代码质量     | backend-code-lint、**`superpower`**      | 后端代码质检、数据结构规范校验                               |
| 设计规范     | frontend-design、**`openspec`**          | 设计系统、设计标记标准化契约，输出 Mermaid 架构图、ER 图、时序图 |
| 开发效率工具 | **`skill-creator`**、**`find-skills`**   | 自定义 MCP 技能创建、社区技能检索                            |



**推荐技能**

1. `planning-with-files`：项目任务持久化规划工具，把需求、开发步骤、进度、方案全部写入本地 `Markdown` 文件留存；长周期开发、多工具协同不会丢失上下文，重启会话也能读取历史开发计划，可以用来统筹调度其他技能，或拆解复杂任务分阶段执行。

2. `diagramming`：代码架构图表自动生成工具，一键生成 `Mermaid` 图表，支持数据库 ER 图、微服务架构图、接口时序图、业务流程图；配合后端库表、接口需求出可视化示意图，可直接嵌入项目文档与规划文件。



# 二、核心概念

## 1、`Skills CLI` 五大命令

```
┌────────────────────────────────────────────────────┐
│                   Skills CLI                        │
│                                                     │
│   find   ──→  搜索技能（关键词 / 交互式）            │
│   add    ──→  安装技能（从 GitHub 等来源）            │
│   check  ──→  检查是否有可用更新                      │
│   update ──→  批量更新所有已安装技能                   │
│   init   ──→  创建自定义技能模板                      │
└────────────────────────────────────────────────────┘
```

## 2、技能来源与安装语法

技能托管在 **`GitHub` 仓库**中，安装语法为：

```bash
npx skills add <owner>/<repo>@<skill-name>
```

例如：`npx skills add vercel-labs/agent-skills@react-best-practices`

| 安装方式 | 命令                         | 作用域               | 适用场景       |
| -------- | ---------------------------- | -------------------- | -------------- |
| 全局安装 | `npx skills add <pkg> -g -y` | 用户级，所有项目可用 | 通用工具类技能 |
| 项目安装 | `npx skills add <pkg> -y`    | 当前项目可用         | 项目特定的技能 |

> `-y` 标志跳过确认提示，适合自动化脚本使用。



## 3、`SKILL.md` 结构一览

一个完整的 `Skill` 文件由以下部分组成：

```markdown
---
name: find-skills                          ← ① Frontmatter：元数据（名称+描述）
description: Helps users discover...         ←    AI 靠此判断"是否激活此技能"
---

# Find Skills                                ← ② 标题：技能标识

## When to Use This Skill                    ← ③ 触发条件：何时激活
## What is the Skills CLI?                   ← ④ 核心知识：命令说明、参考数据
## How to Help Users Find Skills             ← ⑤ 标准化工作流（6步流程）
## Common Skill Categories                   ← ⑥ 参考数据
## When No Skills Are Found                  ← ⑦ 兜底策略
```

## 4、6大触发场景

`Agent` 依据以下场景判断是否激活 Find Skills 技能：

| #    | 用户表述模式      | 触发示例                          | 判断逻辑                            |
| ---- | ----------------- | --------------------------------- | ----------------------------------- |
| 1    | **"怎么做 X"**    | "how do I do React testing?"      | X 可能是常见任务，已有技能覆盖      |
| 2    | **"找一个技能"**  | "find a skill for deployment"     | 用户明确要求搜索技能                |
| 3    | **"你能做 X 吗"** | "can you do PR reviews?"          | X 可能是 Agent 默认不具备的专业能力 |
| 4    | **扩展能力兴趣**  | "I wish the agent could do X"     | 用户表达了扩展意愿                  |
| 5    | **搜索工具/模板** | "is there a template for X?"      | 用户在找现成方案                    |
| 6    | **领域求助**      | "I need help with design systems" | 特定领域往往有专门的技能            |

> **核心原则**：当用户的请求涉及**特定领域的专业知识**或**可模块化的工作流**时，就应该考虑是否有现成的技能可以使用。

---



# 三、6步工作流：从需求到安装

> 这是 `Find Skills` 技能的**核心流程**，定义了从需求理解到技能安装的完整路径。下面先详解每一步，再用实战串联。

```
用户提出需求
    │
    ▼
┌─────────────────────────────┐
│ Step 1: 理解需求             │  识别领域 + 具体任务 + 普适性
└────────────┬────────────────┘
             ▼
┌─────────────────────────────┐
│ Step 2: 查看排行榜           │  先找热门方案，避免重复搜索
└────────────┬────────────────┘
             ▼
┌─────────────────────────────┐
│ Step 3: CLI 搜索            │  关键词搜索 + owner 过滤
└────────────┬────────────────┘
             ▼
┌─────────────────────────────┐
│ Step 4: 质量验证 ⚠️         │  安装量 / 来源 / Stars 三重校验
└────────────┬────────────────┘
             ▼
┌─────────────────────────────┐
│ Step 5: 呈现结果            │  标准化推荐格式
└────────────┬────────────────┘
             ▼
┌─────────────────────────────┐
│ Step 6: 一键安装            │  npx skills add <pkg> -g -y
└─────────────────────────────┘
```

## 1、`Step 1`：理解需求

识别三个维度：

| 维度         | 问题               | 示例                                       |
| ------------ | ------------------ | ------------------------------------------ |
| **领域**     | 属于哪个技术领域？ | React、测试、设计、部署                    |
| **具体任务** | 用户到底想做什么？ | 编写测试、创建动画、审查 PR                |
| **普适性**   | 这个需求够普遍吗？ | "React 性能优化"✅ vs "我司内部框架的调试"❌ |

> 普适性判断很关键：越通用的需求，越可能有现成的技能；越定制化的需求，越可能需要自己解决。

## 2、`Step 2`：排行榜优先

**为什么不直接搜索？** 因为排行榜已按安装量排序，热门技能往往经过大量用户验证，质量和可靠性更高。

推荐的热门来源：
- `vercel-labs/agent-skills` — React、Next.js、Web 设计（各 10 万+ 安装）
- `anthropics/skills` — 前端设计、文档处理（10 万+ 安装）

## 3、`Step 3`：`CLI` 搜索

如果排行榜没有覆盖用户的需求，使用 `CLI` 搜索：

```bash
npx skills find [query] [--owner <owner>]
```

| 用户问题                  | 搜索关键词          |
| ------------------------- | ------------------- |
| "如何让 React 应用更快？" | `react performance` |
| "能帮我做 PR 审查吗？"    | `pr review`         |
| "我需要生成 changelog"    | `changelog`         |

## 4、`Step 4`：质量验证

> **核心原则：不要仅凭搜索结果就推荐技能！** 必须进行三重验证。

| 验证维度           | 标准                     | 原因                                                       |
| ------------------ | ------------------------ | ---------------------------------------------------------- |
| **安装量**         | 1K+ 优先 ✅ / <100 谨慎 ⚠️ | 安装量是最直接的质量信号                                   |
| **来源声誉**       | 官方 > 知名组织 > 个人   | 官方来源（`vercel-labs`、`anthropics`、`microsoft`）更可信 |
| **`GitHub Stars`** | >100 为宜 / <100 需警惕  | Stars 反映社区认可度                                       |

**验证决策流程**：

```
搜索结果
    │
    ├─ 安装量 ≥ 1K？ ──── 是 ──→ ✅ 通过
    │                     │
    │                     否
    │                     ▼
    │              来源是否官方/知名？ ──── 是 ──→ ✅ 通过（但需注意）
    │                              │
    │                              否
    │                              ▼
    │                       Stars ≥ 100？ ──→ 是 ──→ ⚠️ 可用但需审慎
    │                                    │
    │                                    否
    │                                    ▼
    │                                   ❌ 不推荐
    │
    ▼
  ✅ 推荐安装
```

**风险等级速查**：

| 安装量 | 来源      | Stars | 风险等级 | 建议               |
| ------ | --------- | ----- | -------- | ------------------ |
| 1K+    | 官方/知名 | 100+  | 🟢 低     | 放心安装           |
| 1K+    | 个人/未知 | 100+  | 🟡 中低   | 可以安装，留意更新 |
| 100-1K | 知名      | 50+   | 🟡 中     | 谨慎评估后安装     |
| <100   | 未知      | <50   | 🔴 高     | 不推荐安装         |





## 5、`Step 5`：标准化呈现

采用**标准化推荐格式**，确保用户获得决策所需的所有信息：

```
I found a skill that might help! The "react-best-practices" skill provides
React and Next.js performance optimization guidelines from Vercel Engineering.
(185K installs)

To install it:
npx skills add vercel-labs/agent-skills@react-best-practices

Learn more: https://skills.sh/vercel-labs/agent-skills/react-best-practices
```

**格式四要素**：① 技能名称+功能描述 ② 安装量+来源 ③ 安装命令 ④ 详情链接

## 6、`Step 6`：一键安装

```bash
npx skills add <owner/repo@skill> -g -y
```

| 标志 | 含义                             |
| ---- | -------------------------------- |
| `-g` | 全局安装（用户级，所有项目可用） |
| `-y` | 跳过确认提示                     |



# 五、最佳实践与FAQ

## 1、5条最佳实践

| #    | 最佳实践                        | 原因                                     |
| ---- | ------------------------------- | ---------------------------------------- |
| 1    | **搜索前先明确需求**            | 精准的需求描述 → 精准的搜索关键词        |
| 2    | **优先选择高安装量 + 官方来源** | 质量保障，社区验证                       |
| 3    | **安装前务必验证质量**          | 避免引入低质量或不安全的技能             |
| 4    | **定期检查和更新技能**          | `npx skills check` + `npx skills update` |





## 3、常见问题 FAQ

### 1）**Q：`Skills CLI` 需要安装吗？**

A：不需要单独安装。`npx skills` 会自动下载并运行最新版本，前提是你已安装 Node.js。

### 2）**Q：全局安装的技能存在哪里？**

A：全局技能安装在用户级目录下（`~/.claude/skills/`），所有项目共享。

### 3）**Q：如何卸载技能？**

A：直接删除 `~/.claude/skills/` 下对应的技能目录即可。

### 4）**Q：技能会自动更新吗？**

A：不会。需要手动运行 `npx skills check` 检查更新，然后用 `npx skills update` 更新。



## 4、参考链接

- **Skills 平台**：https://skills.sh/
- **Vercel Labs 官方技能**：https://github.com/vercel-labs/agent-skills
- **Anthropic 官方技能**：https://github.com/anthropics/skills
- **社区技能合集**：https://github.com/ComposioHQ/awesome-claude-skills







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

