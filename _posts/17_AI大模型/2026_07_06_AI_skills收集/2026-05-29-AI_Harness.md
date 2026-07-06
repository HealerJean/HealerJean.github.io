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

---

## 目录

1. [开篇：为什么需要 Find Skills](#一开篇为什么需要-find-skills)
2. [核心概念](#二核心概念)
3. [6步工作流：从需求到安装](#三6步工作流从需求到安装)
4. [技能分类与场景速查](#四技能分类与场景速查)
5. [高级技巧与兜底策略](#五高级技巧与兜底策略)
6. [最佳实践与FAQ](#六最佳实践与faq)

---

## 一、开篇：为什么需要 Find Skills

### 核心痛点

`AI Agent`（如 `Claude Code`）开箱即用，具备通用的代码理解、生成和推理能力。但在特定领域，通用能力往往不够深入——你需要的是**领域专家的知识和最佳实践**，而不是一个"什么都懂一点"的通才。

| 挑战         | 具体表现                                 |
| ------------ | ---------------------------------------- |
| 知识深度不足 | 通用模型对特定框架/工具的细节掌握有限    |
| 最佳实践缺失 | 知道"怎么做"，但不知道"最佳做法"是什么   |
| 重复劳动     | 每次都要从零开始研究领域知识             |
| 质量不稳定   | 没有经过社区验证的方案，输出质量参差不齐 |

### 解决方案

**`Skills CLI`** (`npx skills`) 将社区沉淀的领域知识封装为可安装的**技能包（`Skill`）**，让 `Agent` 在几秒内获得专业能力升级：

```
通用 Agent  +  专业 Skill  =  领域专家级 Agent
```

### 开放技能生态

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

## 二、核心概念

### 1、Skills CLI 五大命令

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

### 2、技能来源与安装语法

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

### 3、SKILL.md 结构一览

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

### 4、6大触发场景

Agent 依据以下场景判断是否激活 Find Skills 技能：

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

## 三、6步工作流：从需求到安装

> 这是 Find Skills 技能的**核心流程**，定义了从需求理解到技能安装的完整路径。下面先详解每一步，再用实战串联。

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

### Step 1：理解需求

识别三个维度：

| 维度         | 问题               | 示例                                       |
| ------------ | ------------------ | ------------------------------------------ |
| **领域**     | 属于哪个技术领域？ | React、测试、设计、部署                    |
| **具体任务** | 用户到底想做什么？ | 编写测试、创建动画、审查 PR                |
| **普适性**   | 这个需求够普遍吗？ | "React 性能优化"✅ vs "我司内部框架的调试"❌ |

> 普适性判断很关键：越通用的需求，越可能有现成的技能；越定制化的需求，越可能需要自己解决。

### Step 2：排行榜优先

**为什么不直接搜索？** 因为排行榜已按安装量排序，热门技能往往经过大量用户验证，质量和可靠性更高。

推荐的热门来源：
- `vercel-labs/agent-skills` — React、Next.js、Web 设计（各 10 万+ 安装）
- `anthropics/skills` — 前端设计、文档处理（10 万+ 安装）

### Step 3：CLI 搜索

如果排行榜没有覆盖用户的需求，使用 CLI 搜索：

```bash
npx skills find [query] [--owner <owner>]
```

| 用户问题                  | 搜索关键词          |
| ------------------------- | ------------------- |
| "如何让 React 应用更快？" | `react performance` |
| "能帮我做 PR 审查吗？"    | `pr review`         |
| "我需要生成 changelog"    | `changelog`         |

### Step 4：质量验证 ⚠️ 最关键步骤

> **核心原则：不要仅凭搜索结果就推荐技能！** 必须进行三重验证。

| 验证维度         | 标准                     | 原因                                                       |
| ---------------- | ------------------------ | ---------------------------------------------------------- |
| **安装量**       | 1K+ 优先 ✅ / <100 谨慎 ⚠️ | 安装量是最直接的质量信号                                   |
| **来源声誉**     | 官方 > 知名组织 > 个人   | 官方来源（`vercel-labs`、`anthropics`、`microsoft`）更可信 |
| **GitHub Stars** | >100 为宜 / <100 需警惕  | Stars 反映社区认可度                                       |

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

### Step 5：标准化呈现

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

### Step 6：一键安装

```bash
npx skills add <owner/repo@skill> -g -y
```

| 标志 | 含义                             |
| ---- | -------------------------------- |
| `-g` | 全局安装（用户级，所有项目可用） |
| `-y` | 跳过确认提示                     |

---

### 实战串联：以"优化 React 应用性能"为例

| 步骤 | 操作 | 结果 |
| ---- | ---- | ---- |
| **Step 1** | 识别需求 | 领域：Web Development → React；任务：性能优化；普适性：✅ 高度通用 |
| **Step 2** | 查看排行榜 | 发现 `vercel-labs/agent-skills` 下有 React 相关技能，安装量 10 万+ |
| **Step 3** | CLI 搜索 | `npx skills find react performance` |
| **Step 4** | 质量验证 | 安装量 185K ✅ / 来源 vercel-labs（官方）✅ / Stars 5K+ ✅ → 🟢 低风险 |
| **Step 5** | 呈现结果 | 按标准化格式推荐 `react-best-practices` |
| **Step 6** | 一键安装 | `npx skills add vercel-labs/agent-skills@react-best-practices -g -y` |

🎉 安装完成后，Agent 即刻获得 Vercel Engineering 沉淀的 React 性能优化知识！

---

## 四、技能分类与场景速查

### 7大分类一览

| 分类            | 推荐搜索关键词                           | 明星技能                                        | 典型需求场景     |
| --------------- | ---------------------------------------- | ----------------------------------------------- | ---------------- |
| **Web Development** | react, nextjs, typescript, css, tailwind | `vercel-labs/agent-skills@react-best-practices` | 前端框架最佳实践 |
| **Testing**         | testing, jest, playwright, e2e           | —                                               | 测试策略与自动化 |
| **DevOps**          | deploy, docker, kubernetes, ci-cd        | —                                               | 部署与运维       |
| **Documentation**   | docs, readme, changelog, api-docs        | —                                               | 文档生成与管理   |
| **Code Quality**    | review, lint, refactor, best-practices   | —                                               | 代码审查与质量   |
| **Design**          | ui, ux, design-system, accessibility     | `anthropics/skills@frontend-design`             | 设计系统与无障碍 |
| **Productivity**    | workflow, automation, git                | —                                               | 工作流与自动化   |

> 💡 搜索时用**具体关键词**效果更好。例如 `react testing` 比 `testing` 更精准。

### 热门来源推荐

| 来源                               | 专注领域       | 代表技能                             |
| ---------------------------------- | -------------- | ------------------------------------ |
| `vercel-labs/agent-skills`         | Web 开发       | react-best-practices, nextjs         |
| `anthropics/skills`                | 前端设计、文档 | frontend-design, document-processing |
| `ComposioHQ/awesome-claude-skills` | 综合工具集     | 多领域技能合集                       |

> 💡 技能生态在快速迭代，建议定期访问 https://skills.sh/ 查看最新排行榜。

---

## 五、高级技巧与兜底策略

### 1、搜索优化三招

**① 使用具体关键词**

```bash
# ❌ 太宽泛
npx skills find testing

# ✅ 更精准
npx skills find react testing
```

**② 尝试同义关键词**

```bash
# 如果 "deploy" 没有结果，试试这些
npx skills find deployment
npx skills find ci-cd
npx skills find publishing
```

**③ 按来源过滤**

```bash
# 只看 Vercel 的技能
npx skills find react --owner vercel-labs

# 只看 Anthropic 的技能
npx skills find design --owner anthropics
```

### 2、找不到技能时的三条出路

```
搜索无结果
    │
    ├─→ ① 坦诚告知：没有找到匹配的技能
    │
    ├─→ ② 直接帮助：用 Agent 通用能力完成任务
    │
    └─→ ③ 创建技能：npx skills init my-xyz-skill
         （适合经常重复的任务）
```

**示例回复**：

```
I searched for skills related to "xyz" but didn't find any matches.
I can still help you with this task directly! Would you like me to proceed?

If this is something you do often, you could create your own skill:
npx skills init my-xyz-skill
```

### 3、自定义技能开发

当你发现某个需求反复出现但社区没有现成方案时，可以创建自己的技能：

```bash
# 初始化技能模板
npx skills init my-custom-skill

# 技能文件结构
my-custom-skill/
├── SKILL.md          # 技能定义（知识 + 工作流 + 触发条件）
└── ...               # 其他资源文件
```

---

## 六、最佳实践与FAQ

### 1、5条最佳实践

| #    | 最佳实践                        | 原因                                     |
| ---- | ------------------------------- | ---------------------------------------- |
| 1    | **搜索前先明确需求**            | 精准的需求描述 → 精准的搜索关键词        |
| 2    | **优先选择高安装量 + 官方来源** | 质量保障，社区验证                       |
| 3    | **安装前务必验证质量**          | 避免引入低质量或不安全的技能             |
| 4    | **定期检查和更新技能**          | `npx skills check` + `npx skills update` |
| 5    | **贡献自定义技能回馈社区**      | 你的经验可能帮助到很多人                 |

> **Find Skills 的本质是"让 AI Agent 站在社区智慧的肩膀上"——不是从零开始，而是从最佳实践开始。**

### 2、命令速查卡

```
┌─────────────────────────────────────────────────────────┐
│                  Skills CLI 速查卡                        │
├─────────────────────────────────────────────────────────┤
│                                                         │
│  🔍 搜索   npx skills find [query] [--owner <owner>]    │
│  📦 安装   npx skills add <owner/repo@skill> [-g] [-y]  │
│  🔄 检查   npx skills check                             │
│  ⬆️ 更新   npx skills update                            │
│  🆕 创建   npx skills init <skill-name>                 │
│                                                         │
│  🌐 浏览   https://skills.sh/                           │
│                                                         │
└─────────────────────────────────────────────────────────┘
```

### 3、常见问题 FAQ

**Q：Skills CLI 需要安装吗？**

A：不需要单独安装。`npx skills` 会自动下载并运行最新版本，前提是你已安装 Node.js。

**Q：全局安装的技能存在哪里？**

A：全局技能安装在用户级目录下（`~/.claude/skills/`），所有项目共享。

**Q：一个仓库可以包含多个技能吗？**

A：可以。使用 `@` 语法指定具体技能：`owner/repo@skill-name`。

**Q：如何卸载技能？**

A：直接删除 `~/.claude/skills/` 下对应的技能目录即可。

**Q：技能会自动更新吗？**

A：不会。需要手动运行 `npx skills check` 检查更新，然后用 `npx skills update` 更新。

### 4、参考链接

- **Skills 平台**：https://skills.sh/
- **Vercel Labs 官方技能**：https://github.com/vercel-labs/agent-skills
- **Anthropic 官方技能**：https://github.com/anthropics/skills
- **社区技能合集**：https://github.com/ComposioHQ/awesome-claude-skills

---

> 📝 文档版本：v2.0 | 更新日期：2026-07-06





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

