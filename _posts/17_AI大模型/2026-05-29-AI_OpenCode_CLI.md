---
title: AI_OpenCode_CLI
date: 2026-05-29 00:00:00
tags: 
- AI
category: 
- AI
description: AI_OpenCode_CLI
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

# 一、 简介

## 1、什么是 `OpenCode`

> `OpenCode` ([opencode.ai](https://opencode.ai)) 是一个开源的 AI 编程代理，由社区开发（anomalyco 组织），拥有 **160K+ GitHub Stars**、**900+ 贡献者**，每月服务 **750 万+ 开发者**。

核心特性：

- **开源免费** — MIT 许可证，代码完全可见
- **模型无关** — 支持 Anthropic、OpenAI、Google、本地模型等 75+ 提供商
- **多会话** — 同时启动多个并行会话
- **LSP 支持** — 自动为 LLM 加载正确的语言服务器
- **Skill 系统** — 可复用的技能定义，按需加载
- **MCP 支持** — 接入外部工具和数据源
- **Agent 系统** — 多种内置代理（Build/Plan/Explore 等）
- **隐私优先** — 不存储你的代码或上下文数据



## 2、安装

| 安装方式         | 命令                                   |
| ---------------- | -------------------------------------- |
| 一键安装（推荐） | curl -fsSL https://opencode.ai/install |
| `npm`            | npm install -g opencode-ai             |
| `Homebrew`       | brew install anomalyco/tap/opencode    |



# 二、`OpenCode` vs `Claude Code` 对比

## 1、基本信息

| 维度             | OpenCode             | Claude Code                  |
| ---------------- | -------------------- | ---------------------------- |
| **开发方**       | 社区开源 (anomalyco) | Anthropic 官方               |
| **许可证**       | 开源                 | 闭源（源码可查看，商业条款） |
| **主要语言**     | Go                   | TypeScript/Node.js           |
| **架构**         | Client/Server        | 单 CLI                       |
| **UI 框架**      | Bubble Tea           | Ink (React for CLI)          |
| **运行时**       | 原生二进制           | Node.js                      |
| **GitHub Stars** | 160K+                | —                            |
| **月活用户**     | 750 万+              | —                            |

## 2、`Agent` 系统对比

| 维度               | OpenCode                        | Claude Code            |
| ------------------ | ------------------------------- | ---------------------- |
| **主代理**         | `Build`（执行）、`Plan`（规划） | `Build`（默认）、Plan  |
| **子代理**         | `General`、`Explore`、`Scout`   | `Subagents`（自定义）  |
| **嵌套子代理**     | ✅ 通过 `Task` 工具              | ✅ 最多 3 层            |
| **后台代理**       | ❌                               | ✅ 后台运行/守护进程    |
| **Tab 切换代理**   | ✅                               | ✅                      |
| **@提及子代理**    | ✅                               | ✅                      |
| **自定义 `Agent`** | ✅ `Markdown/JSON` 定义          | ✅ `Markdown/YAML` 定义 |

## 3、功能特性

| 特性                       | OpenCode                      | Claude Code                      |
| -------------------------- | ----------------------------- | -------------------------------- |
| **Skill 系统（SKILL.md）** | ✅ 原生支持                    | ✅ 原生支持                       |
| **MCP 服务器**             | ✅                             | ✅                                |
| **Plugin 系统**            | ✅                             | ✅                                |
| **Checkpoint / 撤销**      | ✅ `/undo`                     | ✅ Esc+Esc 回退                   |
| **Sandbox（沙箱）**        | 权限控制                      | 系统级沙箱 (Seatbelt/bubblewrap) |
| **LSP 支持**               | ✅ 自动加载                    | ❌                                |
| **快照（/undo）**          | ✅ 支持多次撤销                | ✅ 支持多次撤销                   |
| **Agent Hooks**            | 通过 Plugin                   | ✅ 原生 Hooks                     |
| **远程配置（企业）**       | ✅ 远程 + MDM                  | ✅ Managed Settings               |
| **AGENTS.md / CLAUDE.md**  | ✅ AGENTS.md（兼容 CLAUDE.md） | ✅ CLAUDE.md                      |
| **Share 分享**             | ✅ `/share`                    | ✅ 分享链接                       |
| **Session 压缩**           | ✅ 自动/手动                   | ✅ `/compact`                     |



## 4、`IDE` 支持

| IDE            | OpenCode                       | Claude Code      |
| -------------- | ------------------------------ | ---------------- |
| **VS Code**    | ✅ 扩展                         | ✅ 原生扩展       |
| **JetBrains**  | 未说明                         | ✅ 原生扩展       |
| **Cursor**     | ✅ 通过扩展                     | ✅ 通过扩展       |
| **Vim/Neovim** | ✅ 强支持（团队是 Neovim 用户） | ✅ 社区插件       |
| **终端独立**   | ✅ TUI + 桌面 App               | ✅ TUI + 桌面 App |



## 5、成本对比

| 维度               | OpenCode        | Claude Code                 |
| ------------------ | --------------- | --------------------------- |
| **工具本身**       | 免费（开源）    | 免费（但闭源）              |
| **Claude API**     | 按量付费        | 按量付费或订阅 ($20-200/月) |
| **订阅通道**       | API 仅按量      | Pro/Max 订阅 + API          |
| **ChatGPT Plus**   | ✅ 可直接使用    | ❌                           |
| **GitHub Copilot** | ✅ 可直接使用    | ❌                           |
| **本地模型**       | ✅ 零额外成本    | ❌                           |
| **成本优化**       | 混用便宜/贵模型 | 单一模型通道                |

## 6、适用场景建议

### 1）**选 `OpenCode` 当：**

- 你希望模型可自由切换，不被锁定在单一厂商
- 需要本地模型（`Ollama`/`LM Studio`）做离线或隐私敏感开发
- 想用 `ChatGPT Plus` / `GitHub Copilot` 订阅直连
- 需要在任务中混用不同模型（如规划用 Claude，执行用 GPT）
- 看重开源和可审计性
- 需要 `LSP` 自动加载



### 2）**选 `Claude Code` 当：**

- 你已深度绑定 `Anthropic` 生态，用 `Claude` 订阅
- 想要最新的 `Claude` 特性第一时间使用
- 需要系统级沙箱隔离
- 需要后台守护进程和复杂的 `CI` 集成
- 倾向于开箱即用，不想配置多个 `Provider`



# 三、配置 `Provider`

## 1、配置文件结构

`OpenCode` 使用 `JSON/JSONC `格式的配置文件。配置会**合并**（非覆盖），多个来源共同生效。

**优先级顺序（从低到高）：**

1. 远程配置（组织的 `.well-known/opencode`）
2. 全局配置 `~/.config/opencode/opencode.json`
3. 自定义路径（`$OPENCODE_CONFIG`）
4. 项目配置 `./opencode.json`
5. 内联配置（`$OPENCODE_CONFIG_CONTENT`）
6. 托管配置（系统级，管理员控制）

### 1）全局配置示例

```json
{
  "$schema": "https://opencode.ai/config.json",
  "provider": {
    "openai": {
      "options": {
        "apiKey": "your-api-key",
        "baseURL": "http://127.0.0.1:8787/opencode"
      }
    }
  },
  "model": "Claude-Opus-4.8-joybuilder",
  "autoupdate": false
}
```

> 你也可以将 `baseURL` 指向代理或中转服务，实现 API 转发。

### 2）使用 `/connect` 配置 Provider

在 TUI 中运行：

```
/connect
```

选择 Provider 并按照提示输入 API Key 即可。支持的 Provider 包括：

- Anthropic（支持 Claude Pro/Max 订阅）
- OpenAI（支持 ChatGPT Plus/Pro）
- GitHub Copilot
- Amazon Bedrock
- Google Vertex AI
- Azure OpenAI
- DeepSeek
- Ollama（本地）
- LM Studio（本地）
- 以及 70+ 其他提供商



### 3）隐藏不需要的模型

```json
{
  "provider": {
    "anthropic": {
      "blacklist": ["claude-opus-4-20250514"],
      "whitelist": ["claude-sonnet-4-20250514"]
    }
  }
}
```

---

# 四、初始化项目

## 1、运行 `/init`

在项目目录启动 `opencode` 后，运行：

```
/init
```

这会扫描项目重要文件，分析项目结构，然后创建或更新 `AGENTS.md`。

## 2、`AGENTS.md` 示例

```markdown
# 项目名称
这是一个 Spring Boot + MyBatis 项目，使用 Maven 构建。

## 构建命令
- 编译: mvn compile
- 测试: mvn test
- 打包: mvn package

## 项目结构
- `src/main/java/` — 业务代码
- `src/main/resources/` — 配置文件
- `src/test/java/` — 测试代码

## 编码规范
- 使用 Lombok 简化 POJO
- Controller 统一使用 @RestController
- Service 层必须写接口
```



## 3、全局规则

你还可以在 `~/.config/opencode/AGENTS.md` 设置全局规则，适用于所有项目。



## 4、兼容 `Claude Code`

`OpenCode` 兼容 `Claude Code` 的 `CLAUDE.md` 文件作为后备规则（`AGENTS.md` 优先）。

---



# 五、 使用模式

## 1、`TUI` 模式（默认）

```bash
opencode
```

进入终端交互界面（`Text-based User Interface`（文本用户界面）），特性包括：

- **Tab 切换** — 在 `Build`（执行）和 `Plan`（规划）模式间切换
- **@ 搜索文件** — `@` 后模糊搜索项目文件
- **拖拽图片** — 将图片拖入终端供 LLM 理解
- **/ 命令** — 各种内置命令



**`Plan` 模式 vs `Build` 模式**

| 特性     | Plan 模式        | Build 模式       |
| -------- | ---------------- | ---------------- |
| 读取文件 | ✅                | ✅                |
| 编辑文件 | ❌（需确认）      | ✅                |
| 执行命令 | ❌（需确认）      | ✅                |
| 适用场景 | 分析、规划、审查 | 编码、修改、调试 |



## 2、`CLI` 模式

```bash
# 直接带 prompt 运行
opencode run "解释这个项目的架构"

# 非交互式一次性执行
opencode run -p "运行所有测试"

# 管道输入
cat logs.txt | opencode run "分析这些日志"
```



## 3、其他模式

### 1）`opencode serve` — `HTTP` 服务（纯后台接口）

> **无界面、纯 `API` 后台服务**，只对外暴露标准化 OpenAI 风格 HTTP 接口，不提供可视化操作页面，供程序 / 插件远程调用 `OpenCode` 能力。

**关键特性**

- 默认端口：`4096`，默认仅本地`127.0.0.1`访问
- 完全后台运行，不占用交互终端；关闭终端服务仍常驻
- 输出完整 REST API，支持脚本、CI 流水线、第三方工具调用
- 可开启`--hostname 0.0.0.0`实现局域网 / 公网远程访问
- 搭配`opencode attach`，能在任意终端 TUI 连接这个后台服务

**典型使用场景**

1. **自动化流水线**：CI/CD 脚本调用 API 自动代码评审、修复 lint 错误
2. **多端共享服务**：服务器后台常驻 serve，平板 / 笔记本 / IDE 同时连接同一个编码会话
3. **二次开发集成**：自己写工具、脚本通过 HTTP 请求调用 AI 编码能力

### 2）`opencode web` — 浏览器 Web 图形界面模式

> **内置 serve 服务 + 网页前端界面**，在`serve`API 基础上附带可视化网页，用浏览器操作 OpenCode，不用终端 TUI。

**关键特性**

1. 底层和`serve`完全一致，只是多了一套 Web 前端页面
2. 执行命令后自动打开浏览器，界面包含：聊天输入框、文件树、会话历史、模型配置面板
3. 支持远程访问：`--hostname 0.0.0.0`后，手机 / 平板 / 其他电脑浏览器输入 IP + 端口即可使用
4. 图形化配置模型、权限、MCP，不用手动改配置 JSON

**典型使用场景**

1. **无终端设备**：iPad、平板、网吧电脑，没有安装 OpenCode 客户端，浏览器直接编码
2. **远程协作演示**：把网页链接发给同事，共同查看 AI 编码过程、代码修改
3. **容器 / 服务器开发**：Docker 容器内启动 web，宿主机浏览器访问，不用 SSH 进容器操作终端



### 3）`opencode ide` — `IDE` 插件集成专用模式

> **IDE 专属后台通信服务**，基于 `ACP`（`Agent Client Protocol`）协议，专门对接 `VS Code`、`Cursor` 等编辑器插件，把 `OpenCode `能力嵌入 IDE 侧边栏。

关键特性

1. 不对外提供通用 `HTTP API`，只适配 IDE 插件的私有通信协议
2. 进程轻量化，和 `IDE` 生命周期绑定，打开 IDE 自动启动、关闭 IDE 自动销毁
3. 深度联动编辑器 `LSP`、文件跳转、光标选区，AI 能精准读取 IDE 当前选中代码、打开文件
4. 支持 `IDE` 快捷键唤起、内联代码修改、侧边栏 AI 对话窗口

典型使用场景

1. **日常 IDE 开发**：在 `VS Code` 里直接用 OpenCode，不用切出终端 TUI
2. 编辑器原生交互：选中一段代码右键让 AI 重构、修复 Bug、生成注释

使用方式

1. `VS Code` 安装 `OpenCode` 官方插件
2. 插件内部自动执行`opencode ide`启动后台，无需手动输命令
3. 在编辑器侧边栏直接对话 AI，修改文件实时同步到 IDE



---

# 六、 核心概念

## 1、`Agents`（代理）

`OpenCode` 内置以下代理：

| 代理          | 类型           | 用途               | 读写权限           |
| ------------- | -------------- | ------------------ | ------------------ |
| **`Build`**   | 主代理（默认） | 编码、修改、调试   | 全读写             |
| **`Plan`**    | 主代理         | 分析、规划、审查   | 只读（操作需确认） |
| **`General`** | 子代理         | 通用多步任务       | 全读写             |
| **`Explore`** | 子代理         | 快速探索代码库     | 只读               |
| **`Scout`**   | 子代理         | 外部文档和依赖研究 | 只读               |

使用方式：

- **`Tab` 键** — 在主代理间切换
- **`@general`** — 在消息中提及子代理



### 1）自定义代理

通过 `Markdown` 文件定义（`~/.config/opencode/agents/review.md`）：

```markdown
---
description: 审查代码质量和最佳实践
mode: subagent
permission:
  edit: deny
  bash:
    "*": ask
    "git diff": allow
    "grep *": allow
---

你是一个代码审查专家。关注：
- 代码质量和最佳实践
- 潜在 bug 和边界情况
- 性能影响
- 安全考虑
```



## 2、`Skills`（技能）

> `Skills` 是可复用的指令模块，定义在 `SKILL.md` 文件中。

**目录位置：**

- 项目级：`.opencode/skills/<name>/SKILL.md`
- 全局：`~/.config/opencode/skills/<name>/SKILL.md`

**示例**（`.opencode/skills/git-release/SKILL.md`）：

```markdown
---
name: git-release
description: 创建一致的发布和变更日志
---

## 功能
- 从合并的 PR 起草发布说明
- 提议版本号
- 提供可复制的 gh release create 命令
```

## 3、`MCP` 服务器

> MCP（Model Context Protocol）让 OpenCode 接入外部工具。
>

### 1）本地 `MCP`

```json
{
  "mcp": {
    "my-server": {
      "type": "local",
      "command": ["npx", "-y", "my-mcp-command"],
      "enabled": true,
      "environment": {
        "MY_ENV_VAR": "value"
      }
    }
  }
}
```

### 2）远程 `MCP`

```json
{
  "mcp": {
    "sentry": {
      "type": "remote",
      "url": "https://mcp.sentry.dev/mcp",
      "oauth": {}
    }
  }
}
```



### 3）按 `Agent` 启用 `MCP`

```json
{
  "tools": { "my-mcp*": false },
  "agent": {
    "my-agent": {
      "tools": { "my-mcp*": true }
    }
  }
}
```

## 4、`Permissions`（权限控制）

> `OpenCode` 提供细粒度的权限控制：

```json
{
  "permission": {
    "edit": "ask",
    "bash": {
      "*": "ask",
      "git status *": "allow",
      "grep *": "allow"
    },
    "webfetch": "deny",
    "skill": {
      "internal-*": "deny"
    }
  }
}
```

权限值：`allow`（允许）、`ask`（询问）、`deny`（拒绝）。

可用权限键：`read`、`edit`、`glob`、`grep`、`list`、`bash`、`task`、`webfetch`、`websearch`、`lsp`、`skill`、`question`、`todowrite`。



## 5、`Commands`（自定义命令）

快捷命令，用于重复性任务：

```json
{
  "command": {
    "test": {
      "template": "运行完整测试套件，显示覆盖率报告和失败项。\n关注失败测试并建议修复方案。",
      "description": "运行测试并生成报告",
      "agent": "build"
    },
    "component": {
      "template": "创建一个新的 React 组件 $ARGUMENTS，支持 TypeScript。\n包含合适的类型定义和基本结构。",
      "description": "创建新组件"
    }
  }
}
```

使用方式：在 `TUI` 中输入 `/component UserCard`。



# 七、 配置进阶

## 1、`LSP` 支持

> `OpenCode` 可以自动加载 `LSP` 服务器来帮助 LLM 理解代码：

```json
{
  "lsp": true
}
```

或自定义：

```json
{
  "lsp": {
    "typescript": { "disabled": true },
    "rust-analyzer": { "command": ["rust-analyzer"] }
  }
}
```

## 2、`Formatters`（代码格式化）

```json
{
  "formatter": {
    "prettier": {
      "disabled": true
    },
    "custom-prettier": {
      "command": ["npx", "prettier", "--write", "$FILE"],
      "extensions": [".js", ".ts", ".jsx", ".tsx"]
    }
  }
}
```



## 3、`Themes`（主题）

在 `tui.json` 中配置：

```json
{
  "$schema": "https://opencode.ai/tui.json",
  "theme": "tokyonight"
}
```



## 4、`Snapshot` 与撤销

`OpenCode` 默认开启快照（Snapshot）来追踪文件变更，支持多次撤销：

```
/undo   # 撤销上次变更
/redo   # 重做
```

可禁用（不推荐）：

```json
{
  "snapshot": false
}
```



# 八、开发扩展

## 1、`Plugin` 系统

```json
{
  "plugin": ["opencode-helicone-session", "@my-org/custom-plugin"]
}
```

插件可以包含：Skills、Agents、Commands、Hooks、MCP 定义等。

## 2、`SDK`

`OpenCode SDK` 允许你用代码控制 `OpenCode`：

```typescript
import { opencode } from "opencode/sdk";

const result = await opencode.query("帮我审查这段代码");
```

## 3、`Server` 模式

```json
{
  "server": {
    "port": 4096,
    "hostname": "0.0.0.0",
    "mdns": true,
    "cors": ["http://localhost:5173"]
  }
}
```

启动后，其他设备可以通过 `mDNS` 发现你的 `OpenCode` 服务。

## 4、自定义 `Tools`

```json
{
  "custom_tools": {
    "my-tool": {
      "type": "local",
      "command": ["/path/to/tool"]
    }
  }
}
```

---



# 十、最佳实践

## 1、常用命令速查表

> 根据使用场景分类，快速定位需要的命令。

---

### 1）启动与运行：交互模式

> 交互模式：你打字、它回复，来回对话，适合日常编码。运行 `opencode` 进入，能看到实时反馈、随时调整需求

| 命令                        | 说明                 | 场景                     |
| --------------------------- | -------------------- | ------------------------ |
| `opencode`                  | 启动 TUI（当前目录） | 日常开发                 |
| `opencode /path/to/project` | 指定项目目录启动     | 多项目切换               |
| `opencode -c`               | 继续上次的会话       | 中断后恢复               |
| `opencode -s <sessionId>`   | 恢复指定会话         | 回到特定上下文           |
| `opencode --fork -c`        | 分叉上次会话         | 想保留历史但不影响原会话 |

### 2）启动与运行：非交互模式（`CLI/CI`）

> 非交互模式：一次性丢一个 `prompt` 执行完就退出，适合 CI/脚本。运行 opencode run "xxx"，不会等你继续提问，输出完就结束。

| 命令                                                | 说明                             | 场景             |
| --------------------------------------------------- | -------------------------------- | ---------------- |
| `opencode run "xxx"`                                | 传递 prompt 一次性执行           | CI 集成、脚本    |
| `opencode run -p "xxx"`                             | 同上（`-p` 是 `--command` 简写） | 快速问答         |
| `opencode run --continue "xxx"`                     | 基于上次会话继续执行             | 增量任务         |
| `opencode run --fork -c "xxx"`                      | 分叉上次会话执行                 | 不想污染原会话   |
| `cat logs \| opencode run "分析"`                   | 管道输入内容                     | 分析日志/数据    |
| `opencode run --attach http://localhost:4096 "xxx"` | 附加到运行中的 server            | 复用 server 缓存 |
| `opencode run --format json "xxx"`                  | JSON 格式输出                    | 程序解析结果     |
| `opencode run --model openai/gpt-5 "xxx"`           | 指定模型执行                     | 临时换模型       |



### 3）`TUI` 内 / 命令：会话管理

| 命令        | 别名                  | 快捷键     | 场景                     |
| ----------- | --------------------- | ---------- | ------------------------ |
| `/new`      | `/clear`              | `Ctrl+X N` | 开启全新会话             |
| `/sessions` | `/resume` `/continue` | `Ctrl+X L` | 切换/恢复历史会话        |
| `/undo`     | —                     | `Ctrl+X U` | 撤销上次变更（支持多次） |
| `/redo`     | —                     | `Ctrl+X R` | 重做（撤销后反悔）       |
| `/compact`  | `/summarize`          | `Ctrl+X C` | 压缩长会话释放上下文     |
| `/details`  | —                     | —          | 切换显示工具执行详情     |
| `/export`   | —                     | `Ctrl+X X` | 导出会话为 Markdown      |
| `/exit`     | `/quit` `/q`          | `Ctrl+X Q` | 退出 OpenCode            |
| `/help`     | —                     | —          | 显示帮助                 |



### 4）`TUI` 内 / 命令：模型与 `Provider`

| 命令        | 快捷键     | 场景                       |
| ----------- | ---------- | -------------------------- |
| `/models`   | `Ctrl+X M` | 查看/切换可用模型          |
| `/connect`  | —          | 添加/配置 Provider API Key |
| `/themes`   | `Ctrl+X T` | 切换 TUI 主题              |
| `/thinking` | —          | 切换显示模型的思考过程     |



### 5）项目与分享

| 命令       | 场景                            |
| ---------- | ------------------------------- |
| `/init`    | 初始化项目，创建/更新 AGENTS.md |
| `/share`   | 生成当前会话分享链接            |
| `/unshare` | 取消分享                        |
| `/editor`  | 打开外部编辑器编写消息          |
| `/help`    | 查看帮助                        |

### 6）输入技巧

| 操作           | 说明                                 |
| -------------- | ------------------------------------ |
| `@文件名`      | 模糊搜索并引用文件                   |
| `!命令`        | 直接执行 shell 命令（如 `!ls`）      |
| 拖拽图片到终端 | 让 LLM 理解设计稿/截图               |
| `Tab` 键       | 切换 Build（执行）↔ Plan（规划）模式 |
| `Ctrl+P`       | 打开命令面板                         |
| `Ctrl+T`       | 循环切换模型变体（推理模式）         |



## 2、配置管理

### 1）`Provider` 配置

| 命令                                      | 场景                     |
| ----------------------------------------- | ------------------------ |
| `opencode auth login`                     | 交互式添加 Provider 凭据 |
| `opencode auth login -p anthropic`        | 直接指定 Provider 登录   |
| `opencode auth list` / `opencode auth ls` | 查看已认证的 Provider    |
| `opencode auth logout`                    | 登出指定 Provider        |
| `opencode models`                         | 查看所有可用模型列表     |
| `opencode models anthropic`               | 查看指定 Provider 的模型 |

### 2）`MCP` 管理

| 命令                                              | 场景                    |
| ------------------------------------------------- | ----------------------- |
| `opencode mcp add`                                | 交互式添加 MCP 服务器   |
| `opencode mcp list` / `opencode mcp ls`           | 查看所有 MCP 及连接状态 |
| `opencode mcp auth <name>`                        | OAuth 认证 MCP 服务器   |
| `opencode mcp auth list` / `opencode mcp auth ls` | 查看 OAuth 认证状态     |
| `opencode mcp logout <name>`                      | 清除 MCP 凭据           |
| `opencode mcp debug <name>`                       | 调试 MCP 连接问题       |

### 3）`Agent` 管理

| 命令                    | 场景                   |
| ----------------------- | ---------------------- |
| `opencode agent create` | 交互式创建自定义 Agent |
| `opencode agent list`   | 查看所有可用 Agent     |



### 4）插件

| 命令                          | 场景           |
| ----------------------------- | -------------- |
| `opencode plugin <module>`    | 安装插件       |
| `opencode plug <module>`      | 同上（简写）   |
| `opencode plugin <module> -g` | 安装到全局配置 |



## 3、服务与远程

### 1）启动服务

| 命令                    | 场景                                  |
| ----------------------- | ------------------------------------- |
| `opencode serve`        | 启动 HTTP API 服务（无界面）          |
| `opencode web`          | 启动 Web 界面服务                     |
| `opencode acp`          | 启动 ACP（Agent Client Protocol）服务 |
| `opencode attach <url>` | 用 TUI 连接到远程 server              |

### 2）服务参数

```bash
# 指定端口和主机
opencode serve --port 4096 --hostname 0.0.0.0

# 启用 mDNS 发现
opencode serve --mdns

# 设置密码认证
OPENCODE_SERVER_PASSWORD=mysecret opencode serve

# 允许跨域
opencode serve --cors http://localhost:5173
```

---



## 4、其他

### 1）数据与调试

| 命令                                     | 场景                       |
| ---------------------------------------- | -------------------------- |
| `opencode stats`                         | 查看 token 用量和费用统计  |
| `opencode stats --days 30`               | 查看近 30 天统计           |
| `opencode stats --models`                | 查看各模型用量分布         |
|                                          |                            |
| `opencode session list`                  | 查看所有历史会话           |
| `opencode session list -n 20`            | 查看最近 20 条会话         |
| `opencode session delete <id>`           | 删除指定会话               |
|                                          |                            |
| `opencode export <sessionId>`            | 导出会话为 JSON            |
| `opencode export <sessionId> --sanitize` | 导出并脱敏                 |
| `opencode import <file>`                 | 导入会话（文件或分享链接） |
| `opencode db path`                       | 查看数据库路径             |
| `opencode debug config`                  | 查看最终生效的完整配置     |
| `opencode --print-logs`                  | 打印日志到 stderr          |
| `opencode --log-level DEBUG`             | 设置日志级别               |



### 2）关键快捷键

| 快捷键     | 作用                 |
| ---------- | -------------------- |
| `Tab`      | 切换 Build/Plan 模式 |
| `Ctrl+P`   | 命令面板             |
| `Ctrl+X N` | 新会话               |
| `Ctrl+X U` | 撤销                 |
| `Ctrl+X R` | 重做                 |
| `Ctrl+X C` | 压缩上下文           |
| `Ctrl+X M` | 切换模型             |
| `Ctrl+X T` | 切换主题             |
| `Ctrl+X L` | 会话列表             |
| `Ctrl+X X` | 导出会话             |
| `Ctrl+X Q` | 退出                 |



## 5、实用工作流

### 1）场景 1：快速了解新项目

```bash
cd new-project
opencode init          # 项目初始化一个会话
# 然后输入：这个项目的架构是什么样的？
```

### 2）场景 2：复杂功能的规划与实现

```bash
opencode
# Tab 切换到 Plan 模式
# 输入：为订单模块添加缓存功能，先设计方案
# 审查方案后 Tab 切回 Build
# 输入：按方案开始实现
```



### 3）场景 3：CI 流水线集成

```bash
# GitHub Actions 中
opencode run "运行测试并修复失败项" --auto
```



### 4）场景 4：多模型配合

```bash
# 先规划（用强模型）
opencode run --model anthropic/claude-sonnet-4 "设计这个模块的架构"

# 再执行（用快模型）
opencode run --model openai/gpt-5 "按上述方案实现代码"
```



### 5）场景 5：远程协作

```bash
# 机器 A 启动服务
opencode serve --port 4096 --hostname 0.0.0.0

# 机器 B 或其他人连接
opencode attach http://<机器A IP>:4096
```

---



### 6）其他常用实践

1. **善用 `Plan` 模式** — 复杂任务先规划再执行，减少返工
2. **维护 `AGENTS.md`** — 让 LLM 了解项目结构、命令和规范
3. **利用 `Skill` 系统** — 将重复的指令封装为 Skill
4. **合理配置权限** — 生产环境建议 `ask` 模式，避免意外修改
5. **混用模型** — 规划用强力模型，执行用快速/便宜模型
6. **使用 `/undo`** — 放心让 `LLM` 尝试，不满意随时回退
7. **拖入图片** — 让 `LLM` 直接看设计稿或截图
8. **定期 `/compact`** — 长会话后压缩上下文，保持 LLM 效率





## 6、分享与协作

### 1）分享会话

```
/share
```

> 这会创建当前会话的链接并复制到剪贴板。分享模式可配置：

```json
{
  "share": "manual"    // 手动分享
  // "share": "auto"   // 自动分享
  // "share": "disabled" // 禁用分享
}
```



### 2）`GitHub` 集成

> 配置 GitHub 集成后，OpenCode 可以直接操作 GitHub Issues、PR 等。



### 3）`GitLab` 集成

> `OpenCode` 支持通过 OAuth 或 Personal Access Token 连接 GitLab。

---

### 4）更多资源

- 官网: [https://opencode.ai](https://opencode.ai)

- 文档: [https://opencode.ai/docs](https://opencode.ai/docs)

- GitHub: [https://github.com/anomalyco/opencode](https://github.com/anomalyco/opencode)

- Discord: [https://opencode.ai/discord](https://opencode.ai/discord)







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



