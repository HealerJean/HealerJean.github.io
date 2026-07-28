---
title: AI_CodeWiki
date: 2026-07-27 00:00:00
tags: 
- AI
category: 
- AI
description: AI_CodeWiki
---

**前言**    

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)        

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)         

 https://github.com/FSoft-AI4Code/CodeWiki

 论文：https://arxiv.org/abs/2510.24428（ACL 2026）



# 一、`CodeWiki` 

## 1、认识 `CodeWiki`：AI 驱动的仓库级文档生成框架

### 1）是什么：不是增强代码，是理解代码

`CodeWiki` 是由 **FPT Software AI4Code 团队**开发的开源框架，用于**自动生成仓库级别的全局文档**。它不仅为单个函数生成文档，更重要的是捕获**跨文件、跨模块、系统级**的交互关系，生成整体性的、架构感知的结构化文档

- 论文已被 **ACL 2026** 接收（arXiv:2510.24428）
- 开源许可证：**MIT**
- GitHub Stars：1.4k+，Forks：218


### 2）三大核心创新

| 创新 | 描述 | 影响 |
| :--- | :--- | :--- |
| **层次化分解** | 动态规划策略，将仓库按架构层级拆分，保留上下文 | 支持 86K-1.4M LOC 任意规模代码库 |
| **递归多 Agent 处理** | 自适应多 Agent 协作，动态任务委派 | 可扩展到仓库级规模，同时保持质量 |
| **多模态合成** | 文本文档 + Mermaid 架构图 + 数据流图 + 序列图 | 从多个视角全面理解代码库 |


### 3）与 `DeepWiki` 的核心区别

| 维度 | `CodeWiki` (FSoft) | `DeepWiki` |
| :--- | :--- | :--- |
| **性质** | 开源框架，本地安装运行 | 闭源 SaaS，在线使用 |
| **核心能力** | 生成结构化文档 + 架构图 + 数据流图 | 生成 Wiki 页面，对话式探索 |
| **技术路线** | 层次分解 → 递归多 Agent → 多模态合成 | 单次 LLM 调用生成 Wiki |
| **语言支持** | 9种（Python/Java/JS/TS/C/C++/C#/Kotlin/PHP） | 所有 GitHub 公开仓库 |
| **学术验证** | `CodeWikiBench` 基准，整体 68.79% | 无公开基准，64.06% |
| **部署方式** | 本地 CLI / Docker / MCP Server | 在线 deepwiki.com |
| **LLM 支持** | OpenAI/Anthropic/Azure/Bedrock/Atlas/订阅模式 | 内部模型 |
| **增量更新** | 支持 `--update` 和 MCP 增量检测 | 不支持 |



## 2、安装与配置

### 1）安装

```bash
# 从源码安装
pip install git+https://github.com/FSoft-AI4Code/CodeWiki.git

# 验证安装
codewiki --version
```

**系统要求**：Python 3.12+、Node.js（Mermaid 图验证）、Git


### 2）配置 LLM 提供商

`CodeWiki` 支持 **6 种 LLM 接入方式**：

```bash
# 方式1：OpenAI 兼容接口
codewiki config set \
  --provider openai-compatible \
  --api-key YOUR_API_KEY \
  --base-url https://api.anthropic.com \
  --main-model claude-sonnet-4 \
  --cluster-model claude-sonnet-4 \
  --fallback-model glm-4p5

# 方式2：Anthropic 直连
codewiki config set \
  --provider anthropic \
  --api-key YOUR_API_KEY \
  --base-url https://api.anthropic.com \
  --main-model claude-sonnet-4 \
  --cluster-model claude-sonnet-4

# 方式3：Azure OpenAI
codewiki config set \
  --provider azure-openai \
  --api-key YOUR_AZURE_KEY \
  --base-url https://YOUR_RESOURCE.openai.azure.com \
  --azure-deployment YOUR_DEPLOYMENT \
  --main-model gpt-4o \
  --cluster-model gpt-4o

# 方式4：AWS Bedrock
codewiki config set \
  --provider bedrock \
  --aws-region us-east-1 \
  --main-model anthropic.claude-sonnet-4-v2:0 \
  --cluster-model anthropic.claude-sonnet-4-v2:0

# 方式5：Atlas Cloud（OpenAI 兼容，300+ 模型）
codewiki config set \
  --provider atlas-cloud \
  --main-model anthropic/claude-sonnet-4.6 \
  --cluster-model anthropic/claude-sonnet-4.6

# 方式6：订阅模式（Claude Code / Codex，无需 API Key）
codewiki config set \
  --provider claude-code \
  --main-model claude-sonnet-4-6 \
  --cluster-model claude-sonnet-4-6
```

> **订阅模式说明**：通过本地 `claude` / `codex` CLI 二进制路由所有 LLM 调用（基于 `caw` 库），可直接使用 Claude Pro/Max 或 Codex 订阅，无需按 Token 付费。`CodeWiki` 的 Agent 循环中禁用了 Claude Code 内置的 `Write`/`Edit`/`Bash` 工具，文档写入仍通过 `CodeWiki` 自身的 Mermaid 验证编辑器


### 3）模型角色说明

| 模型参数 | 作用 | 说明 |
| :--- | :--- | :--- |
| `--main-model` | 主文档生成模型 | 用于每个模块的文档撰写 |
| `--cluster-model` | 模块聚类模型 | 用于将组件分组为逻辑模块 |
| `--fallback-model` | 兜底模型 | 主模型失败时的备用 |



## 3、快速上手

### 1）基本用法

```bash
# 进入项目目录
cd /path/to/your/project

# 生成文档
codewiki generate

# 带详细日志
codewiki generate --verbose
```

### 2）常用选项

```bash
# 自定义输出目录
codewiki generate --output ./documentation

# 创建 Git 分支用于文档
codewiki generate --create-branch

# 生成 HTML 查看器（用于 GitHub Pages）
codewiki generate --github-pages

# 同时创建分支 + 生成 HTML + 详细日志
codewiki generate --create-branch --github-pages --verbose

# 增量更新（只重新生成变更模块）
codewiki generate --update

# 增量更新（指定对比的 commit hash）
codewiki generate --compare-to <commit-hash>
```


### 3）输出结构

```
./docs/
├── overview.md              # 仓库概览（从这里开始阅读！）
├── module1.md               # 模块文档
├── module2.md               # 其他模块...
├── module_tree.json         # 模块层级结构
├── first_module_tree.json   # 初始聚类结果
├── metadata.json            # 生成元数据
└── index.html               # 交互式查看器（需 --github-pages）
```



## 4、自定义选项

### 1）文件过滤

```bash
# C# 项目：只分析 .cs 文件，排除测试目录
codewiki generate --include "*.cs" --exclude "Tests,Specs,*.test.cs"

# 聚焦特定模块，生成架构风格文档
codewiki generate --focus "src/core,src/api" --doc-type architecture

# 添加自定义 Agent 指令
codewiki generate --instructions "Focus on public APIs and include usage examples"
```

**过滤行为规则**：

| 选项 | 行为 | 示例 |
| :--- | :--- | :--- |
| `--include` | **替换**默认模式（只分析匹配文件） | `*.cs`、`src/**/*.ts` |
| `--exclude` | **合并**默认排除模式（追加排除） | `Tests,Specs`、`*.test.js` |


### 2）持久化默认配置

```bash
# 设置 C# 项目的默认包含模式
codewiki config agent --include "*.cs"

# 排除测试项目（与默认排除合并）
codewiki config agent --exclude "Tests,Specs,*.test.cs"

# 设置聚焦模块
codewiki config agent --focus "src/core,src/api"

# 设置默认文档类型
codewiki config agent --doc-type architecture

# 查看当前 Agent 设置
codewiki config agent

# 清除所有 Agent 设置
codewiki config agent --clear
```


### 3）Token 设置

```bash
# 设置 LLM 响应最大 Token（默认 32768）
codewiki config set --max-tokens 16384

# 设置模块聚类输入 Token 阈值（默认 36369）
codewiki config set --max-token-per-module 40000

# 设置叶模块输入 Token 阈值（默认 16000）
codewiki config set --max-token-per-leaf-module 20000

# 设置层次分解最大深度（默认 2）
codewiki config set --max-depth 3

# 运行时覆盖单次生成的设置
codewiki generate --max-tokens 16384 --max-token-per-module 40000 --max-depth 3
```

| 选项 | 描述 | 默认值 |
| :--- | :--- | :--- |
| `--max-tokens` | LLM 响应最大输出 Token | 32768 |
| `--max-token-per-module` | 模块聚类输入 Token 阈值 | 36369 |
| `--max-token-per-leaf-module` | 叶模块输入 Token 阈值 | 16000 |
| `--max-depth` | 层次分解最大深度 | 2 |




# 二、架构设计

## 1、处理流水线

`CodeWiki` 采用**三阶段**处理流程生成全局文档：

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Codebase      │───▶│  Hierarchical    │───▶│  Multi-Agent    │
│   Analysis      │    │  Decomposition   │    │  Processing     │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                                │                        │
                                ▼                        ▼
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Visual        │◀───│  Multi-Modal     │◀───│  Structured     │
│   Artifacts     │    │  Synthesis       │    │  Content        │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### 1）阶段一：依赖分析（占处理时间 40%）

- 克隆仓库
- 使用语言特定分析器解析源文件（Tree-sitter AST 解析）
- 提取组件（类、函数、接口等）
- 构建调用图，展示组件关系
- 识别叶节点（文档生成的入口点）


### 2）阶段二：模块聚类（占处理时间 20%）

- 使用 LLM 智能分组相关组件
- 创建层级模块结构
- 生成模块树，展示父子关系
- 缓存模块树供后续使用


### 3）阶段三：文档生成（占处理时间 30%）

- 按依赖顺序处理模块（叶节点优先）
- 叶模块：通过 Agent 生成详细文档
- 父模块：聚合子文档并合成概览
- 生成仓库级架构概览


### 4）阶段四 & 五：HTML 生成与收尾（占处理时间 10%）

- 加载生成的 Markdown 和元数据
- 创建交互式文档查看器
- 打包用于 GitHub Pages 部署
- 写入 `metadata.json`（含 commit ID + 时间戳）



## 2、系统架构分层

```
┌─────────────────────────────────────────────────────────┐
│                    User Interaction                       │
│         CLI Interface  |  Web Interface (Browser)        │
├─────────────────────────────────────────────────────────┤
│                    Frontend Layer                         │
│         CLI Core (Orchestration)  |  Web App (FastAPI)   │
├─────────────────────────────────────────────────────────┤
│                Backend Processing Layer                   │
│      Documentation Generator  |  Dependency Analyzer     │
├─────────────────────────────────────────────────────────┤
│                 Code Analysis Layer                       │
│    Language Analyzers (9+ Languages)  |  Graph Builder   │
├─────────────────────────────────────────────────────────┤
│                LLM Integration Layer                      │
│       LLM Backends (Multi-Provider)  |  Agent Tools      │
├─────────────────────────────────────────────────────────┤
│               Infrastructure Layer                        │
│    Config Management  |  File I/O  |  Logging & Progress │
└─────────────────────────────────────────────────────────┘
```



## 3、多语言支持

| 语言 | 解析器 | 状态 |
| :--- | :--- | :--- |
| Python | 原生 AST | ✅ 稳定 |
| JavaScript | Tree-Sitter | ✅ 稳定 |
| TypeScript | Tree-Sitter | ✅ 稳定 |
| Java | Tree-Sitter | ✅ 稳定 |
| Kotlin | Tree-Sitter | ✅ 稳定 |
| C# | Tree-Sitter | ✅ 稳定 |
| C | Tree-Sitter | ✅ 稳定 |
| C++ | Tree-Sitter | ✅ 稳定 |
| PHP | Tree-Sitter | ✅ 稳定 |



## 4、项目源码结构

```
codewiki/
├── cli/                  # CLI 实现
│   ├── commands/         # CLI 命令（config, generate）
│   ├── models/           # 数据模型
│   ├── utils/            # 工具类
│   └── adapters/         # 外部集成适配器
├── src/
│   ├── be/               # 后端（依赖分析 + Agent 系统）
│   │   ├── backend.py              # LLMBackend 抽象 + 工厂
│   │   ├── pydantic_ai_backend.py  # API Key 后端
│   │   ├── caw_backend.py          # 订阅后端（claude/codex CLI）
│   │   ├── agent_tools/            # Agent 工具实现
│   │   ├── cluster_modules.py      # 模块聚类
│   │   ├── dependency_analyzer/    # 依赖分析
│   │   └── documentation_generator.py  # 文档生成编排
│   └── fe/               # 前端（Web 界面）
│       ├── web_app.py               # FastAPI Web 应用
│       ├── routes.py                # API 路由
│       └── visualise_docs.py        # 文档可视化
├── mcp/                  # MCP Server（IDE 集成）
│   ├── server.py         # 10 个工具注册（8 细粒度 + 2 遗留）
│   ├── session.py        # 会话状态管理
│   └── tools/            # 工具实现
└── templates/            # HTML 模板
```




# 三、MCP Server 与 IDE 集成

## 1、IDE 驱动模式：零 LLM 配置的文档生成

### 1）设计动机

原始 `CodeWiki` 要求用户自行配置 LLM API（API Key + base_url），通过一次性 CLI 命令生成文档。存在两个问题：

- **配置门槛**：用户需要获取 API Key，理解提供商差异，处理模型兼容问题
- **灵活性不足**：生成过程是黑盒——用户无法在生成过程中干预聚类策略或文档风格


### 2）重构方案

将 `CodeWiki` 的 MCP Server 从"黑盒一次性生成"转为"细粒度工具集"：

```
重构前：
  IDE → generate_docs(repo) → [CodeWiki 内部调用 LLM] → result

重构后：
  IDE Agent → analyze_repo → read_code → (Agent 自行推理聚类) → write_doc → overview
              ↑ 纯工具调用    ↑ 纯工具调用   ↑ IDE 自身 LLM       ↑ 纯工具调用
```


### 3）文件侧信道架构

关键设计决策：不在 MCP stdio 通道中传输大负载（组件索引、源代码、处理顺序），而是将所有大体积数据写入**每会话工作区文件**。MCP 响应只返回文件路径和紧凑摘要，IDE Agent 使用自身的文件访问能力直接读取文件

> 优势：彻底消除截断限制，组件索引、源代码文件、处理顺序无论多大都完整写入



## 2、MCP 工具集

`CodeWiki` MCP Server 暴露 **8 个细粒度工具**（零 LLM 配置）+ **2 个遗留工具**：

| 工具 | 用途 | 需要 LLM |
| :--- | :--- | :--- |
| `analyze_repo` | 解析仓库，构建依赖图，检测增量变更 | 否 |
| `read_code_components` | 将组件源代码写入工作区 `.src` 文件 | 否 |
| `write_doc_file` | 创建 .md 文档，自动 Mermaid 验证 | 否 |
| `edit_doc_file` | 编辑文档：`str_replace` / `insert` / `undo` | 否 |
| `save_module_tree` | 持久化 IDE Agent 的模块聚类结果 | 否 |
| `get_processing_order` | 计算叶节点优先的处理顺序 | 否 |
| `get_prompt` | 获取各流水线阶段的提示模板 | 否 |
| `close_session` | 写入 `metadata.json`，清理工作区 | 否 |
| `generate_docs` | [遗留] 一次性生成（需配置 LLM） | **是** |
| `get_module_tree` | [遗留] 获取已有模块聚类树 | 否 |



## 3、IDE Agent 工作流（5 阶段）

```
Phase 1: analyze_repo
  │  → 获取 session_id, workspace_dir, 统计信息, 文件路径
  │  → 读取工作区文件：component_index.json, leaf_nodes.json, languages.json
  │
Phase 2: get_prompt("cluster") + read_code_components + save_module_tree
  │  → Agent 自行推理，将组件分为 3-8 个逻辑模块
  │  → 源代码写入工作区 sources/ 目录，Agent 直接读取 .src 文件
  │  → 从 processing_order.json 获取叶节点优先处理顺序
  │
Phase 3: 逐模块生成
  │  对每个叶模块：
  │  ├── get_prompt("system_leaf") → 获取文档撰写指令
  │  ├── read_code_components → 源代码写入 sources/*.src，直接读取
  │  └── write_doc_file → 写入 .md（自动 Mermaid 验证）
  │
  │  对每个父模块：
  │  ├── 读取子模块 .md 文件
  │  ├── get_prompt("overview_module") → 获取概览指令
  │  └── write_doc_file → 写入概览
  │
Phase 4: get_prompt("overview_repo") → 生成仓库概览 overview.md
  │
Phase 5: close_session → 写入 metadata.json，清理工作区，释放资源
```



## 4、IDE 配置

### 1）CodeBuddy 配置

```json
{
  "mcpServers": {
    "codewiki": {
      "command": "python",
      "args": ["-m", "codewiki.mcp.server"],
      "cwd": "/path/to/CodeWiki"
    }
  }
}
```

项目规则自动配置在 `.codebuddy/rules/codewiki-wiki-generator/RULE.mdc`，在 Agent 模式提到"生成文档"或"Wiki"时自动加载


### 2）Cursor 配置

在 Cursor Settings → MCP 中添加相同配置，项目规则配置在 `.cursorrules`


### 3）Claude Desktop 配置

添加到 `~/Library/Application Support/Claude/claude_desktop_config.json`（macOS）


### 4）触发方式

在任意 MCP 兼容 IDE 的 Agent 模式中输入：

```
Analyze this repository and generate Wiki documentation for me
```




# 四、增量更新

## 1、增量检测机制

### 1）CLI 增量更新

```bash
# 增量更新：只重新生成变更模块
codewiki generate --update

# 指定对比的 commit hash（适用于 CI/CD 或 squashed PR）
codewiki generate --compare-to <commit-hash>
```

### 2）MCP 增量检测

增量检测内置于 `analyze_repo` 工具，采用**双策略**方法：

| 策略 | 原理 | 适用场景 |
| :--- | :--- | :--- |
| **Git 策略** | 读取 `metadata.json` 中的 `commit_id`，运行 `git diff` 对比当前 HEAD，同时检查 `git status` 捕获未提交变更 | Git 仓库 |
| **Mtime 策略** | 遍历源文件，对比 `metadata.json` 中的时间戳与文件修改时间 | 非 Git 仓库（兜底） |


### 3）变更返回结构

```json
{
  "changes": {
    "has_previous": true,
    "no_changes": false,
    "method": "git",
    "changed_files": ["auth.py"],
    "affected_modules": ["Authentication Module"],
    "cascade_modules": ["Core System", "overview"],
    "hint": "Only 1 module(s) need updating: ..."
  }
}
```

- `affected_modules`：直接受影响、需更新文档的模块
- `cascade_modules`：间接受影响的父模块（子文档变更 → 概览需刷新）+ 仓库概览



## 2、Agent 增量更新工作流

当 `analyze_repo` 返回 `changes` 且 `no_changes: false` 时，Agent 执行：

```
1. 只处理 affected_modules 中的模块：
   ├── read_code_components → 从工作区读取变更组件源代码
   └── edit_doc_file(str_replace) → 部分修改文档（而非全量重写）

2. 处理 cascade_modules 中的父模块：
   ├── 读取已更新的子文档
   └── edit_doc_file → 刷新概览部分

3. 最后更新 overview.md
```

> 相比 5 阶段全量生成，增量更新通常只需处理 1-3 个模块，大幅减少时间


## 3、注意事项

- `metadata.json` 仅在调用 `close_session` 时写入。如果会话结束未调用 `close_session`，不存在基线，下次 `analyze_repo` 会静默回退到全量分析
- **务必在工作流结束时调用 `close_session`**，确保增量更新正常工作
- 增量更新粒度为**模块级**：模块内任一组件源文件变更，该模块整个文档标记为需更新



# 五、CodeWikiBench 基准测试

## 1、基准介绍

`CodeWikiBench` 是首个专为**仓库级文档质量评估**设计的基准，包含多维度评分标准和 LLM 评估协议

- 数据集：[HuggingFace - anhnh2002/codewikibench](https://huggingface.co/datasets/anhnh2002/codewikibench)
- 覆盖 **22 个开源仓库**，横跨多种编程语言


### 1）仓库覆盖

| 语言类别 | 仓库 |
| :--- | :--- |
| **JS/TS** | Chart.js, marktext, puppeteer, storybook, mermaid, svelte |
| **Python** | graphrag, rasa, OpenHands |
| **C** | qmk_firmware, libsql, sumatrapdf, wazuh |
| **C++** | electron, x64dbg, json |
| **C#** | FluentValidation, git-credential-manager, ml-agents |
| **Java** | logstash, material-components-android, trino |


### 2）每个仓库包含

- **metadata**：仓库 URL 和 commit ID
- **docs_tree**：原始文档树结构
- **structured_docs**：解析后的结构化文档
- **rubrics**：评估文档质量的评分标准



## 2、实验结果

### 1）按语言类别

| 语言类别 | `CodeWiki` (Sonnet-4) | `DeepWiki` | 提升 |
| :--- | :--- | :--- | :--- |
| 高级语言（Python, JS, TS） | **79.14%** | 68.67% | **+10.47%** |
| 托管语言（C#, Java） | **68.84%** | 64.80% | **+4.04%** |
| 系统语言（C, C++） | 53.24% | 56.39% | -3.15% |
| **整体平均** | **68.79%** | 64.06% | **+4.73%** |


### 2）代表性仓库

| 仓库 | 语言 | LOC | `CodeWiki` | `DeepWiki` | 提升 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| All-Hands-AI--OpenHands | Python | 229K | **82.45%** | 73.04% | **+9.41%** |
| puppeteer--puppeteer | TypeScript | 136K | **83.00%** | 64.46% | **+18.54%** |
| sveltejs--svelte | JavaScript | 125K | **71.96%** | 68.51% | **+3.45%** |
| Unity-Technologies--ml-agents | C# | 86K | **79.78%** | 74.80% | **+4.98%** |
| elastic--logstash | Java | 117K | **57.90%** | 54.80% | **+3.10%** |

> `CodeWiki` 在高级脚本语言上优势显著（+10.47%），在系统语言（C/C++）上略低于 `DeepWiki`（-3.15%），整体平均领先 +4.73%



## 3、使用 CodeWikiBench

```python
from datasets import load_dataset
import json

# 加载数据集
dataset = load_dataset("anhnh2002/codewikibench")

# 访问特定仓库
repo_data = dataset['train'][0]
print(f"Repository: {repo_data['repo_name']}")
print(f"Commit: {repo_data['commit_id']}")

# 解析 JSON 字段
docs_tree = json.loads(repo_data['docs_tree'])
structured_docs = json.loads(repo_data['structured_docs'])
rubrics = json.loads(repo_data['rubrics'])
```

评估流程：

```bash
# 运行完整评估流水线
bash ./run_evaluation_pipeline.sh \
  --repo-name OpenHands \
  --reference deepwiki-agent \
  --models kimi-k2-instruct,gpt-oss-120b,gemini-2.5-flash \
  --visualize --batch-size 4

# 可视化结果
python judge/visualize_evaluation.py \
  --repo-name OpenHands \
  --reference deepwiki \
  --format markdown
```




# 六、实战指南与最佳实践

## 1、典型使用场景

### 1）新项目文档初始化

```bash
# 配置 LLM
codewiki config set \
  --provider anthropic \
  --api-key $ANTHROPIC_API_KEY \
  --main-model claude-sonnet-4 \
  --cluster-model claude-sonnet-4

# 生成文档并部署到 GitHub Pages
cd /path/to/project
codewiki generate --github-pages --create-branch
```

### 2）大型 Java 项目聚焦核心模块

```bash
# 只聚焦核心模块，排除测试和构建产物
codewiki generate \
  --focus "src/main/java/com/example/core" \
  --exclude "test,build,dist,node_modules" \
  --doc-type architecture \
  --verbose
```

### 3）CI/CD 集成增量更新

```bash
# 在 CI 流水线中，对比上次提交增量更新
codewiki generate --update --compare-to $LAST_COMMIT_HASH
```


### 4）IDE Agent 交互式生成

在 Cursor / Claude Desktop 等 IDE 中：

```
请分析当前仓库并生成 Wiki 文档，重点关注公共 API 和使用示例
```



## 2、配置安全

- **API Keys**：安全存储在系统密钥链（macOS Keychain / Windows Credential Manager / Linux Secret Service）
- **无密钥链环境**：回退到 `~/.codewiki/credentials.json`
- **强制文件存储**：设置 `CODEWIKI_NO_KEYRING=1`
- **其他配置**：存储在 `~/.codewiki/config.json`


## 3、Docker 部署

```bash
# 使用 Docker Compose 启动
cd docker
cp env.example .env  # 编辑 .env 配置 API Key
docker-compose up -d
```


## 4、常见问题

| 问题 | 解决方案 |
| :--- | :--- |
| Tree-sitter 解析错误 | 确保语言解析器正确安装，检查文件编码（需 UTF-8） |
| LLM API 错误 | 验证 API Key 和端点，检查速率限制，启用重试逻辑 |
| 大仓库内存不足 | 调整模块分解阈值（`--max-token-per-module`），增加委派深度限制 |
| Mermaid 验证失败 | Agent 会自动根据验证结果修正语法；确保 `mermaid-py` 已安装 |
| 会话超时 | 默认 2 小时 TTL，最多 10 个并发会话；超时后重新调用 `analyze_repo` |
| 订阅模式模型名错误 | 使用裸 CLI 模型名（如 `claude-sonnet-4-6`），不加 `openai/` 或 `anthropic/` 前缀 |


## 5、与 AI 编码工具的协同

| 工具 | 协同方式 | 优势 |
| :--- | :--- | :--- |
| `Claude Code` | 订阅模式 / MCP Server | 无需 API Key，使用 Claude Pro/Max 订阅 |
| `Cursor` | MCP Server 集成 | Agent 自行推理聚类策略，灵活可控 |
| `CodeBuddy` | MCP Server + 自动规则 | 项目规则自动加载，零配置触发 |
| `Codex CLI` | 订阅模式 | 使用 Codex 订阅，无需按 Token 付费 |


## 6、引用

```bibtex
@misc{hoang2025codewikievaluatingaisability,
      title={CodeWiki: Evaluating AI's Ability to Generate Holistic Documentation for Large-Scale Codebases},
      author={Anh Nguyen Hoang and Minh Le-Anh and Bach Le and Nghi D. Q. Bui},
      year={2025},
      eprint={2510.24428},
      archivePrefix={arXiv},
      primaryClass={cs.SE},
      url={https://arxiv.org/abs/2510.24428},
}
```



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