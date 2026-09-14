---
title: AI_zvec_grep
date: 2026-05-29 00:00:00
tags: 
- AI
category: 
- AI
description: AI_zvec_grep
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

 项目地址：https://github.com/alibaba/zvec  

 zvec-grep（zg）：https://github.com/zvec-ai/zvec-grep    

 官方文档：https://zvec.org  



# 一、认识 `zvec` 与 `zvec-grep`

## 1、`zvec` 是什么

> `zvec` 是阿里巴巴开源的**进程内（in-process）向量数据库**——轻量、极速、直接嵌入到应用里跑，不用独立部署服务器。已经在阿里内部广泛使用，2026 年 8 月正式开源，`Apache-2.0` 协议。

一句话概括：**它不是一个"你去连接的向量数据库"，而是一个"你把它嵌到程序里的向量数据库"**（类比：`SQLite` 之于 `MySQL`）。

**核心特性：**

| 维度              | 说明                                                         |
| :---------------- | :----------------------------------------------------------- |
| 🚀 **极速**        | 毫秒级搜索十亿级向量                                         |
| 🏠 **本地优先**    | 无服务器、无配置，跑在你的应用进程里                         |
| 🔀 **稠密+稀疏**   | 支持向量、多向量查询、多种索引类型（`HNSW`/`DiskANN`/`IVF-RaBitQ`） |
| 🔍 **全文检索**    | 原生 `BM25` 关键词检索 + `N-gram` 分词器（代码 / 短文本友好） |
| 🧬 **混合搜索**    | 向量 + 全文 + 结构化过滤，一条查询搞定                       |
| 💾 **持久存储**    | 通过 `WAL` 保证掉电/崩溃后数据不丢                           |
| 🌐 **多语言 SDK**  | `Python`、`Node.js`、`Go`、`Rust`、`Dart/Flutter`            |
| 🖥️ **跨平台**      | `Linux`（`glibc/musl`）/`macOS ARM64`/`Windows`/`Android`/`iOS` |



## 2、`zvec-grep`（`zg`）是什么

> `zg` 是 `zvec` 生态里的一把"瑞士军刀"——**面向人类和 AI Agent 的本地优先统一检索层**，一条 `CLI` 把 `ripgrep`、`BM25`、向量检索三种能力合到一起。

Slogan：**Know the words—or don't. Just zg.**

（知道关键词也行，不知道也行，直接 `zg` 就完事儿了。）

**它解决的核心问题：**

- `ripgrep` 快，但只能匹配你**记得的字面词**
- 向量检索能找**意思相似**的东西，但不会精确定位
- `BM25` 擅长**关键词排序**，但不理解语义

而 AI Agent 在读代码库时，恰好三种能力都需要——`zg` 就把这三条路合到了一起，并通过 `MCP` 让 Agent 自动调用。

**核心特性：**

| 特性               | 说明                                                         |
| :----------------- | :----------------------------------------------------------- |
| 👥 **人机通用**     | 一次安装、一次索引，`CLI` 与 Agent 共享同一个 workspace      |
| 🧠 **超越关键词**   | 语义发现 + 相关性排序，最后用 `ripgrep` 精确校验             |
| 📄 **多格式支持**   | 源码、文档、结构化数据，保留结构与源码位置                   |
| ⚡ **少搜索、少上下文** | 带排名、带源码位置的结果，减少 tool call 与 token 消耗       |
| 🔒 **默认本地**     | 文件、索引、本地模型全在设备上；远程 embedding 需要显式授权  |
| 🔌 **Agent 集成**   | 通过 `MCP` 支持 Codex / Claude Code / Cursor / Qwen Code / Qoder / OpenCode |



## 3、为什么"三合一"是必要的

> AI Agent 读代码的痛点：**它不知道该 grep 哪个词**。

传统的 `ripgrep` 依赖使用者拿到明确的关键词/正则。但 Agent 在没读过代码的情况下，往往只有一个模糊意图——比如"用户主题偏好在哪里恢复"——它不知道对应的函数叫 `loadTheme` 还是 `restorePreferences` 还是 `applyUserSettings`。

`zg` 的策略：

```
1. 向量检索（语义发现）  →  找到语义相关的候选文件
2. BM25 排序（关键词排名）→  把候选按相关度排序
3. ripgrep（精确校验）  →  最后拿到确认后的文件精确 grep
```

三步走下来，Agent 只需要少量 tool call 就能定位到证据行——而不是撒网式 grep 几十次。



---



# 二、快速上手

## 1、安装

> 依赖：`Node.js 22+`

```bash
npm install -g @zvec/zvec-grep

# 验证
zg --version
zg help
```



## 2、五分钟 Demo：让 `zg` 侦破福尔摩斯

> 官方 README 给的入门 demo 特别有意思——用《爱丽丝漫游奇境》和《福尔摩斯探案集》两本书当语料，然后让 `zg` 用**语义**回答问题。

### 1）准备语料

```bash
mkdir zg-mystery && cd zg-mystery

curl --retry 3 --retry-all-errors --progress-bar -fL \
  -o alice-in-wonderland.txt https://www.gutenberg.org/files/11/11-0.txt \
  -o sherlock-holmes.txt https://www.gutenberg.org/files/1661/1661-0.txt
```

### 2）建索引（用本地 embedding 模型）

```bash
zg index --embedding local/potion-retrieval-32m
```

- `local/potion-retrieval-32m`：`Model2Vec` 系列，**32M 参数、512 维、1024 token 上下文**，纯本地跑，不用 GPU。第一次跑会自动下载模型到 `~/.zvec-grep/models`。
- 索引落盘到 `<当前目录>/.zvec-grep/`

### 3）人类查询

```bash
zg query --human "An unseen creature left a few marks. What did the detective infer?" --limit 3
```

- `--human`：给终端友好的富文本输出
- `--limit 3`：只返回 3 条结果

输出会直接给出 `sherlock-holmes.txt:5479-5486` 这种带**精确行号**的结果，还带一段上下文片段。

> **关键点**：查询里根本没提"Holmes"或"Sherlock"，`zg` 靠语义就定位到了。

### 4）Agent 查询（以 `OpenCode` 为例）

```bash
zg install --target opencode --yes
opencode models
opencode run --model opencode/nemotron-3-ultra-free "An unseen creature left a few marks. What did the detective infer?"
```

`OpenCode` 会**自动**选择用 `zg` 去搜索——提示词里根本没有点名任何工具。



---



# 三、`zg` 命令全景

## 1、命令总览

| 命令             | 作用                                       |
| :--------------- | :----------------------------------------- |
| `zg query`       | 检索索引，或执行 managed `ripgrep`         |
| `zg index`       | 构建、更新、重建、删除工作区索引           |
| `zg status`      | 查看工作区与索引状态                       |
| `zg install`     | 把 `zg` 集成到 Agent（`Codex`/`Claude`/`Cursor`/...） |
| `zg uninstall`   | 从 Agent 卸载                              |
| `zg config`      | 配置 provider 凭据与默认模型               |
| `zg auth`        | 管理远程 embedding 授权                    |
| `zg server`      | 启停共享的 `MCP` 服务器                    |
| `zg help`        | 查看帮助                                   |

内置帮助：

```bash
zg help                  # 总览
zg help query            # query 详细
zg help models           # 模型列表
zg help file-types       # 支持的文件类型
zg help environment      # 环境变量与优先级
zg <command> --help      # 每个子命令的帮助
```



## 2、`zg query` — 检索

### 1）四种查询路径

| 路径                    | 场景                             | 覆盖范围     |
| :---------------------- | :------------------------------- | :----------- |
| 位置参数 或 `--hybrid`  | 有关键词 + 有意图，混合检索      | 排名采样     |
| `--fts <query>`         | 精确关键词，按 `BM25` 排序       | 排名采样     |
| `--vector <query>`      | 只关心概念/语义相似              | 排名采样     |
| `--rg`                  | 走精确/正则匹配，`不需要索引`    | 穷尽匹配     |

### 2）常用示例

```bash
# 1. Hybrid（推荐默认）：一句自然语言即可
zg query "where theme preferences are restored"

# 2. 纯词法（BM25）+ 路径过滤
zg query --fts "loadTheme" -g "src/**" -t ts

# 3. 纯语义（向量），不看关键词
zg query --vector "where user preferences are restored" --limit 5

# 4. Hybrid 融合多个查询组
zg query --hybrid "authentication flow" --fts "ForbiddenError" --fuse --limit 10

# 5. 人类富文本输出
zg query --human "plugin lifecycle" --preview full

# 6. 走 managed rg（不需要建索引）
zg query --rg -i -C 2 -g "*.ts" "dark mode" src
```

### 3）结果控制

| 选项                                    | 作用                                                         |
| :-------------------------------------- | :----------------------------------------------------------- |
| `--limit <n>`                           | 每个 query 组返回条数上限                                    |
| `--human`                               | 富文本输出，`--preview` 默认 `full`                          |
| `--preview none\|short\|full`           | 是否附上代码/文本片段                                        |
| `--refresh background\|wait\|off`       | 是否刷新索引：后台/等待/不刷                                 |
| `--mode direct\|server\|auto`           | 直连/走 server/自动（默认 `auto`）                           |
| `--prefer-symbol`                       | 优先命中代码符号（`function`/`class`/...）                   |
| `--symbol-type <type>`                  | 限定符号类型：`module`/`class`/`interface`/`function`/`value`/`alias` |
| `--modified-after <time>`               | mtime 下界                                                   |
| `--modified-before <time>`              | mtime 上界                                                   |
| `-g/--glob`, `--iglob`                  | 路径过滤                                                     |
| `-t/--type`, `-T/--type-not`            | 文件类型过滤                                                 |
| `--debug` / `--trace`                   | 查询诊断信息                                                 |

> **`--fuse` 什么时候用？** 多个 query 组的候选合并成一个排名（用 `RRF` — Reciprocal Rank Fusion）。不加 `--fuse` 就是**分组分别排名**，同一条命中可能在多个组里都出现。



## 3、`zg index` — 索引

### 1）三种形态

```bash
# 1. 首次建/增量更新
zg index [root] [options]

# 2. 强制重建（换模型/换配置时）
zg index [root] --rebuild [options]

# 3. 删除索引
zg index [root] --drop [--yes]
```

### 2）核心选项

| 选项                         | 作用                                              |
| :--------------------------- | :------------------------------------------------ |
| `--embedding <model>`        | 指定 embedding 模型                                |
| `--rebuild`                  | 强制重建                                          |
| `--drop`                     | 删除索引                                          |
| `--yes`                      | 跳过确认                                          |
| `--reset-paths`              | 不复用已有的文件选择规则                          |
| `--mode direct\|server\|auto` | 执行模式                                          |
| `--api-key <key>`            | 远程 embedding 的 API Key                          |
| `--endpoint <url>`           | 远程 embedding 的 endpoint                        |
| `--model-cache <path>`       | 本地模型缓存目录                                  |
| `--device <auto\|cpu\|metal\|vulkan\|cuda>` | 本地模型跑在哪个设备上                            |
| `--embedding-concurrency <n>` | Potion 本地 embedding 并发数（默认 2）             |
| `--allow-remote`             | 单次授权把 workspace 内容发到远程 embedding provider |

### 3）文件过滤

| 选项                 | 作用                                     |
| :------------------- | :--------------------------------------- |
| `-g, --glob`         | Include/`!`Exclude 规则（有序）          |
| `--iglob`            | 大小写不敏感的 glob                      |
| `-t, --type`         | 包含某种 rg 文件类型                     |
| `-T, --type-not`     | 排除某种 rg 文件类型                     |
| `--hidden`           | 包含隐藏文件（`.git`/`.zvec-grep` 除外） |
| `--no-ignore`        | 忽略 `.gitignore` 等规则                 |
| `--ignore-file`      | 额外 ignore 文件                         |
| `--max-depth`        | 递归深度上限                             |
| `--max-filesize`     | 单文件大小上限，如 `500K`、`2M`          |
| `-L, --follow`       | 安全地跟随 symlink                       |

### 4）示例

```bash
# 首次建索引，用代码专用小模型
zg index --embedding local/potion-code-16m-v2

# 只索引 src/ 和 docs/，排除 dist/，只要 ts 文件
zg index --embedding local/potion-code-16m-v2 -g "src/**" -g "docs/**" -g "!dist/**" -t ts

# 增量更新（复用之前的模型和过滤规则）
zg index

# 换模型 → 必须 rebuild
zg index --rebuild --embedding local/jina-embeddings-v2-base-code

# 删除索引
zg index --drop --yes
```



## 4、`zg status` — 索引状态

```bash
zg status [root] [--mode direct|server|auto] [--check-ready]
```

会报告：**根目录、使用的模型、文件数、失败数、截断情况、下一步建议**。

`--check-ready`：正常输出，但索引没准备好时**退出码非零**，可以直接用在 shell 脚本里。



## 5、`zg install` — 集成到 Agent

### 1）支持的 Agent

| Agent           | Target       | 关键配置文件                                                 |
| :-------------- | :----------- | :----------------------------------------------------------- |
| `Codex`         | `codex`      | `~/.codex/config.toml`, `~/.codex/AGENTS.md`                 |
| `Claude Code`   | `claude`     | `~/.claude.json`, `~/.claude/settings.json`, `~/.claude/CLAUDE.md` |
| `Qwen Code`     | `qwen`       | `~/.qwen/settings.json`, `~/.qwen/QWEN.md`                   |
| `Qoder CLI+IDE` | `qoder`      | `~/.qoder/settings.json`, `~/.qoder/AGENTS.md`, `~/.qoder/mcp.json` |
| `OpenCode`      | `opencode`   | `~/.config/opencode/opencode.json` + `AGENTS.md`             |
| `Cursor`        | `cursor`     | `~/.cursor/mcp.json`                                         |

### 2）示例

```bash
# 交互式选择
zg install

# 明确目标
zg install --target codex --yes
zg install --target claude --target cursor --yes

# 全部一次装完
zg install --target all --yes
```

**关键选项：**

| 选项                          | 作用                                                         |
| :---------------------------- | :----------------------------------------------------------- |
| `--mcp-transport stdio\|http` | 默认 `stdio`                                                 |
| `--mcp-toolset agent\|full`   | 默认 `agent`，只暴露 `zvec_grep_search`；`full` 暴露完整 6 个工具 |
| `--mcp-tool-timeout <s>`      | 工具超时，默认 600s                                          |
| `--mcp-token-env <name>`      | Server token 的环境变量名                                    |
| `--force`                     | 覆盖已存在的冲突 `zvec_grep` 条目                            |

> **注意：** `zg install` 只改动 `ZVEC_GREP_START` / `ZVEC_GREP_END` 标记之间的托管块，不会覆盖其他 MCP server 或用户自定义内容。

### 3）卸载

```bash
zg uninstall --target codex --yes
zg uninstall --target all --yes
```

卸载**不会**删除索引，也不会 `npm uninstall` 包本身。卸载后需要**重启 Agent** 才生效。



## 6、`zg config` — 模型与凭据

```bash
# 配置远程 embedding provider
zg config provider set qwen --api-key "$DASHSCOPE_API_KEY"

# 设置默认模型
zg config model set qwen/text-embedding-v4 --default

# 给本地模型指定设备
zg config model set local/potion-code-16m-v2 --device metal
```

全局配置存放在 `~/.zvec-grep/config.json`。**已经建好的索引会保留自己 stored 的模型，直到显式 `--rebuild`。**



## 7、`zg auth` — 远程 embedding 授权

> **关键概念区分：**
>
> - `zg config provider set`：告诉 zg "这是访问 provider 的 API Key"
> - `zg auth grant`：告诉 zg "允许把 workspace 内容发送到 provider"
>
> 这两件事是分开的——**配了 Key 也不等于授权数据出机器**。

```bash
# 授予工作区级别的持久授权
zg auth grant --capability embedding --scope workspace --embedding qwen/text-embedding-v4

# 查看当前授权
zg auth status

# 撤销
zg auth revoke
```

授权信息落在 `<workspace>/.zvec-grep/authorization.json`，`CLI` 与 `MCP Server` 共享。

**单次授权（一次性）：**

```bash
zg index --embedding qwen/text-embedding-v4 --allow-remote
```



## 8、`zg server` — 共享 MCP 服务器

```bash
# 启动（后台）
zg server on [--listen 127.0.0.1:7999] [--token-file <path>] [--mcp-toolset agent|full]

# 前台运行
zg server run

# 停止
zg server off

# 状态
zg server status [--check-ready]
```

- 默认监听 `http://127.0.0.1:7999/mcp`（**只允许 loopback**）
- 一个 `zvec-grep home` 只能被一个 server 拥有
- `zg install` 会自动引导启动



---



# 四、检索管线（Retrieval Pipeline）

> 理解索引里到底装了什么、什么会被跳过、索引怎么刷——是用好 `zg` 的关键。

## 1、什么会被索引

### 1）结构感知的代码格式（保留符号结构）

| 文件                                                         | 提取器          | 索引形态                                   |
| :----------------------------------------------------------- | :-------------- | :----------------------------------------- |
| `C/C++`（`.c/.cc/.cpp/.cxx/.h/.hpp`）、`Go`、`Java`、`JS/JSX`、`TS/TSX`、`Python`、`Rust` | `CodeExtractor` | 符号、签名、面包屑、周围源码               |
| `.vue`、`.svelte`                                            | `CodeExtractor` | `<script>` 块 JS/TS，其余降级为纯文本       |
| `Ruby`、`PHP`、`Swift`、`Kotlin`、`C#`、`Scala`、`shell`、`SQL`、`CSS`/`SCSS`/`Less`、`Dockerfile`、`Makefile` | `CodeExtractor` | 纯文本块（等语法就绪再升级为结构化）       |

### 2）文档 / 结构化数据

| 文件                              | 提取器               | 索引形态                       |
| :-------------------------------- | :------------------- | :----------------------------- |
| `.md`、`.mdx`                     | `MarkdownExtractor`  | 按 heading 分段 + 面包屑       |
| `.txt`、`.rst`、`.html`、`.xml`   | `TextExtractor`      | 纯文本块                       |
| `.csv`、`.json`、`.toml`、`.yaml` | `TextExtractor`      | 纯文本块                       |
| 未识别但通过二进制检测            | `TextExtractor`      | 纯文本块                       |
| `.gif`、`.jpg`、`.png`、`.webp`   | `ImageExtractor`     | **默认不索引**，需显式加入 + 图像 embedding 模型 |

### 3）默认跳过（二进制/大文件）

```
.pdf .doc .docx .ppt .pptx .xls .xlsx
.zip .tar .gz .bz2 .xz .7z .rar
.exe .dll .dylib .so .a .o .obj .wasm .class .jar
.mp3 .mp4 .mov .avi .mkv .db .sqlite
```

`.git` 和 `.zvec-grep` **永远**排除。



## 2、大小限制

未指定 `--max-filesize` 时的默认上限：

| 类别            | 上限     |
| :-------------- | :------- |
| 代码            | 1 MiB    |
| 文本 / Markdown | 256 MiB  |
| 结构化数据      | 16 MiB   |
| 图片            | 10 MiB   |

**空文件、超限文件、二进制文件被静默跳过**——不算作失败，也不计入 `filesScanned`。用 `zg index --debug` 才会打印跳过明细。



## 3、索引存放位置

- `<workspace>/.zvec-grep/manifest.json`：元数据、运行时设置、（若持久化的）API Key
- `<workspace>/.zvec-grep/files.zvec`：文件级信息
- `<workspace>/.zvec-grep/index.zvec`：向量索引本体

**建议：** 把 `.zvec-grep/` 加进 `.gitignore`。



## 4、`Freshness`（新鲜度）与刷新

`zg` 的结果会打标签：`fresh` 或 `possibly_stale`。

| 模式    | `--refresh` 默认 | 行为                                                         |
| :------ | :--------------- | :----------------------------------------------------------- |
| `Server` | `background`    | 返回当前结果，同时后台调度更新                               |
| `Direct` | `off`           | 默认只搜不更新；传 `background` 会警告并按 `off` 处理；传 `wait` 则 inline 更新（当前进程内跑一遍索引更新） |

`Server` 模式下：

- 有 file watcher 监听变更（会把 burst 事件合并成目录级更新）
- 每小时全量 reconciliation probe，兜底 watcher 漏事件
- 只有当检测到实际漂移时才会打 `possibly_stale`

日志路径：`~/.zvec-grep/daemon/logs/server.log`（`JSON Lines` 格式，`API Key`、Token、query 内容会被过滤）。



---



# 五、`Embedding` 模型选型

## 1、快速选择指南

| 场景                     | 推荐模型                                | 备注                    |
| :----------------------- | :-------------------------------------- | :---------------------- |
| **代码首次索引，追求快** | `local/potion-code-16m-v2`              | 1024 token，纯本地      |
| **英文文档检索**         | `local/potion-retrieval-32m`            | 512 维                  |
| **多语言文档**           | `local/potion-multilingual-128m`        | 101 种语言，256 维      |
| **代码专用 Transformer** | `local/jina-embeddings-v2-base-code`    | 长上下文 8192           |
| **通用多语言**           | `local/embeddinggemma-300m`             | GGUF                    |
| **紧凑多语言**           | `local/multilingual-e5-small`           | 384 维                  |
| **轻量英文**             | `local/all-minilm-l6-v2`                | 256 token               |
| **长英文文档**           | `local/gte-modernbert-base` / `nomic-embed-text-v1.5` | 8192 token   |
| **不想跑本地**           | `qwen/qwen3.7-text-embedding`           | 128K token，远程        |
| **文本 + 图像**          | `qwen/qwen3-vl-embedding`               | 多模态                  |



## 2、完整模型表

| Model                              | 运行时          | 最大 tokens | 维度  |
| :--------------------------------- | :-------------- | :---------- | :---- |
| `local/potion-code-16m-v2`         | Model2Vec FP16  | 1,024       | 256   |
| `local/potion-retrieval-32m`       | Model2Vec FP32  | 1,024       | 512   |
| `local/potion-multilingual-128m`   | Model2Vec FP32  | 1,024       | 256   |
| `local/all-minilm-l6-v2`           | ONNX Q4         | 256         | 384   |
| `local/bge-small-en-v1.5`          | ONNX Q4         | 512         | 384   |
| `local/multilingual-e5-small`      | ONNX Q8         | 512         | 384   |
| `local/jina-embeddings-v2-base-code` | ONNX Q8       | 8,192       | 768   |
| `local/gte-modernbert-base`        | ONNX Q4         | 8,192       | 768   |
| `local/nomic-embed-text-v1.5`      | ONNX Q4         | 8,192       | 768   |
| `local/embeddinggemma-300m`        | GGUF Q8_0       | 2,048       | 768   |
| `local/qwen3-embedding-0.6b`       | GGUF Q8_0       | 8,192       | 1,024 |
| `qwen/text-embedding-v4`           | Remote text     | 8,192       | 1,024 |
| `qwen/qwen3.7-text-embedding`      | Remote text     | 128,000     | 1,024 |
| `qwen/qwen3-vl-embedding`          | Remote 多模态   | 32,000      | 2,560 |

所有模型都用**余弦相似度**；exact revisions 每个 release 都会 pin。**tokens 限制针对每个 fragment，不是整个文件。**



## 3、本地模型的设备

```bash
--device auto|cpu|metal|vulkan|cuda
```

也可以通过 `zg config model set ... --device metal` 或 `ZVEC_GREP_DEVICE` 设置。

**注意：** Model2Vec（Potion 系列）用的是**静态查表**，GPU 并不会加速。

模型缓存目录：`~/.zvec-grep/models`（可用 `--model-cache` 或 `ZVEC_GREP_MODEL_CACHE` 覆盖）。



## 4、切换模型必须 rebuild

**不同模型的向量空间是不兼容的**——哪怕维度一样。切换模型必须：

```bash
zg index --rebuild --embedding local/jina-embeddings-v2-base-code
```

换 remote endpoint 也需要 rebuild（endpoint 是 stored schema 的一部分）。**换 API Key、换 device 不需要 rebuild。**



## 5、远程 embedding：两步授权

```bash
# 第一步：配 Key（只配置了访问权，还不能发数据）
zg config provider set qwen --api-key "$DASHSCOPE_API_KEY"

# 第二步：授权数据出机器（工作区级别）
zg auth grant --capability embedding --scope workspace --embedding qwen/text-embedding-v4

# 或者单次授权
zg index --embedding qwen/text-embedding-v4 --allow-remote
```

> **官方原话：** *"Credentials configure access to a provider; they do not authorize data transfer."*
>
> 凭据只是配置了访问 provider 的权限；它不授权数据传输。



---



# 六、代码库全是英文没中文注释怎么办

> 一个很常见的问题：**"我代码全是英文标识符、没中文注释，我用中文查询能命中吗？"**
>
> 答案：**能，但取决于两个东西**——Embedding 模型的语言能力，以及代码本身的命名质量。

## 1、`zg` 搜的到底是什么

`zg` 索引代码时，`CodeExtractor` 会把「**符号名 + 签名 + 面包屑 + 周围源码**」都塞进 embedding。也就是说：

- **`zg` 搜的不是"字面文本"，是符号的语义 + 上下文**
- 只要命名有语义，就能被向量空间"认出来"
- 中文注释缺失不是问题，**命名混乱才是问题**



## 2、两个决定因素

| 因素                | 决定什么                         | 怎么应对                    |
| :------------------ | :------------------------------- | :-------------------------- |
| **Embedding 模型**  | 能否"跨语言"把中文查询匹配英文代码 | 换多语言 embedding 模型     |
| **代码命名质量**    | 语义 embedding 有没有东西可"抓"  | 好命名 > 好模型（人的责任） |

**核心：多语言模型能解决"语言不同"，但解决不了"名字没意义"。**



## 3、选对多语言模型（第一步）

默认的 `local/potion-retrieval-32m` 是**英文优化**的，中文查询效果差。要做中→英跨语言检索，换成：

```bash
# 轻量多语言（101 种语言）
zg index --rebuild --embedding local/potion-multilingual-128m

# ONNX 多语言，效果更稳
zg index --rebuild --embedding local/multilingual-e5-small

# 效果最好，但要远程 + 授权数据出机器
zg index --rebuild --embedding qwen/qwen3.7-text-embedding
```

> **为什么能跨语言？** 多语言模型训练时把"用户认证"和 `authenticate user` 映射到了向量空间的相近位置。你搜中文命中英文代码是**语义匹配**，不是翻译。



## 4、命名质量决定天花板

同样一段代码、同样的多语言模型：

| 命名情况                                | 搜"用户登录"能否命中 |
| :-------------------------------------- | :------------------- |
| `function authenticateUser(...)` + JSDoc | ✅ 大概率命中         |
| `function login(...)`                   | ✅ 命中               |
| `function doAuth(...)`                  | ⚠️ 看上下文           |
| `function f1(a, b)`                     | ❌ 基本无解           |
| `function h(x)` + 全局无注释            | ❌ 死路一条           |

**没救的情况：** 代码全是缩写、无注释、类型缺失——这种情况 `zg` 只能退化到 `--rg`，但 `--rg` 需要你自己知道要搜什么词。**属于代码本身的问题，任何工具都救不了。**



## 5、实操策略（四种姿势）

### 1）优先走词法路径 + 英文查询

代码库里的"事实关键词"就是**类名 / 函数名**本身。用英文猜一个可能的命名去 `--fts`：

```bash
# 猜命名 → 词法搜（同一 --fts 传多次即代表多个 BM25 group，不要在字符串里写 OR）
zg query --fts "auth" -t java
zg query --fts "login" --fts "authenticate" --fts "credential" -t ts
```

### 2）拿到锚点后走符号扩展

一旦 `--fts` 命中一个符号，立刻切到符号视角：

```bash
zg query --prefer-symbol --symbol-type function "auth"
```

或者接 `CodeGraph` 展开调用链——**这是"全英文代码库"最实用的组合**：`zg` 帮你找入口，`CodeGraph` 帮你看邻居。

### 3）中文意图 + 英文关键词组合 fuse

多语言模型 + `--fuse` 融合中英双路：

```bash
zg query --hybrid "用户登录流程" --fts "login" --fts "auth" --fuse --limit 10
```

- 中文走向量路径 → 找语义相近符号
- 英文走 `BM25` → 精确锚定
- 两路结果用 `RRF` 融合，recall 最高

### 4）用 `.context/` 建中英映射（最治本）

在 `.context/` 里用中文写领域词汇表，标注对应的英文命名：

```markdown
# .context/glossary.md

## 领域词汇表（中文意图 ↔ 英文符号）

- **用户认证** → `AuthService` / `authenticate()`
- **主题偏好** → `ThemePreference` / `loadTheme()`
- **订单退款** → `RefundService` / `processRefund()`
- **库存扣减** → `InventoryService` / `deductStock()`
```

AI 读了这个文件，就自动学会"中文意图 → 英文符号"的翻译，再交给 `zg` 就精准了。**这是"上下文知识库 + `zg`"组合最有价值的场景。**



## 6、无注释 + 无好命名的历史屎山

如果代码烂到向量也 embed 不出东西：

- 老实用 `zg query --rg` + 英文关键词穷举
- 或者让 AI 先给关键模块**加一层 JSDoc / 中文注释**，再 `--rebuild` 索引（一次性投入，长期收益）



## 7、决策树

```
代码库全英文、无中文注释
    ├─ 命名规范好         → 换多语言 embedding 即可，中文查询照搜
    ├─ 命名一般           → 多语言 embedding + --fuse 融合中英查询
    ├─ 命名差、无 doc     → .context/ 建中英词汇表映射
    └─ 屎山、缩写、无救   → 老实 --rg + 硬找
```



---



# 七、Agent 集成与 MCP

## 1、Agent 怎么选择 `zg` vs 原生 grep

> **关键原则**：*"Code versus non-code is not the boundary"*（代码 vs 非代码不是边界）。真正的边界是：**你知不知道要搜什么**。

| 意图                                       | 用什么                          |
| :----------------------------------------- | :------------------------------ |
| 明确的字面词、引号、名字、日期、Key、文件名、路径、正则 | 原生 `grep` / `rg`              |
| 不知道措辞或位置；需要语义、模糊、跨文件综合 | `zvec_grep_search`（`zg` 的 MCP 工具）|
| 有已知锚点 + 需要更宽上下文               | 先 `zvec_grep_search`，再 grep/rg |
| 开放世界 / 外部 / 当前网络事实            | 外部信息源，不用 `zg`           |



## 2、`MCP` 端点与工具

**端点：** `http://127.0.0.1:7999/mcp`（`Streamable HTTP`，只监听 loopback）

### 1）默认 `agent` 工具集（只 1 个工具）

只暴露 `zvec_grep_search`——因为对于 Agent 来说，"能不能自动决定用它"比"能不能全能"更重要。

`zvec_grep_search` 参数：

| 参数            | 说明                                                         |
| :-------------- | :----------------------------------------------------------- |
| `root`          | 工作区绝对路径（必填）                                       |
| `query` / `queries` | 混合自然语言/精确查询                                        |
| `fts`           | 词法约束（`BM25` 排名，不 exhaustive）                       |
| `vector`        | 纯语义组（不做词法排名）                                     |
| `fuse`          | 用 `RRF` 融合所有 group                                      |
| `limit`         | 最大 50                                                      |
| `globs`         | 路径过滤                                                     |
| `fileTypes`     | 类型过滤                                                     |
| `symbolTypes`   | 符号类型过滤                                                 |
| `modifiedAfter` / `modifiedBefore` | mtime 上下界                                                 |
| `freshness`     | `eventual` / `wait_for_fresh`                                |
| `autoUpdate`    | eventual 检索时是否后台更新                                  |

**要求**：至少要有 `query`、`queries`、`fts`、`vector` 之一。

### 2）`full` 工具集（6 个工具）

用 `zg server on --mcp-toolset full` 或 `ZVEC_GREP_MCP_TOOLSET=full` 打开：

| 工具                     | 作用                             |
| :----------------------- | :------------------------------- |
| `zvec_grep_search`       | 索引检索                         |
| `zvec_grep_rg`           | 免索引穷尽 `rg`                  |
| `zvec_grep_index`        | 建/更新/重建/删除索引            |
| `zvec_grep_index_drop`   | 显式删除索引                     |
| `zvec_grep_index_status` | 索引状态                         |
| `zvec_grep_server_status` | daemon、队列、模型池状态         |

> **注意**：`zvec_grep_index` 的 `wait` 默认 `false`（返回一个 job ID）。文档明确要求："**Agent 不得静默创建、重建或删除持久索引**"。



## 3、在不同 Agent 里验证已装好

安装后先跑：

```bash
zg server status --check-ready
```

然后在**新会话**里检查工具名：

| Agent               | 工具名                                               |
| :------------------ | :--------------------------------------------------- |
| Codex / Claude Code | `zvec_grep_search`                                   |
| Qwen Code / Qoder CLI | `mcp__zvec_grep__zvec_grep_search`（+ `..._rg` in full） |
| OpenCode            | `zvec_grep_zvec_grep_search`                         |
| Qoder IDE           | 重启后 `zvec_grep` server + 工具出现                 |

Shell fallback（Agent 出问题时人肉验证）：

```bash
zg query "where theme preferences are restored"
zg query --rg -F "loadTheme" src
```



## 4、Bearer 鉴权（可选）

Server 默认**不开** token，因为只监听 loopback。要打开：

```bash
export ZVEC_GREP_SERVER_TOKEN="<至少32字符的强 token>"
zg server on

# 或者用文件
zg server on --token-file /path/to/token
```

`zg install` 可以让 Agent 从环境变量读 token：

```bash
zg install --target codex --mcp-token-env ZVEC_GREP_SERVER_TOKEN
```

> **注意作用域：** MCP Bearer token 保护的是**本地 server**，它**不授权 remote embedding**。数据出机器需要 `zg auth grant`。



---



# 八、架构与信任边界

## 1、系统总览

```
Human/Script        Agent (Codex/Claude/Cursor/...)
     │                       │
     ▼                       ▼
  zg CLI              MCP client (stdio/HTTP)
     │                       │
     │ auto/server/direct    │ 始终走 Server
     └──────────┬────────────┘
                ▼
       ┌──────────────────┐
       │  Search Engine   │
       │                  │
       │ ① Indexed search │ ← BM25 + Vector + RRF
       │ ② Managed rg     │ ← 免索引，exhaustive
       │ ③ Indexing       │ ← scan/extract/embed
       └────────┬─────────┘
                │
                ▼
   ┌──────────────────────────┐
   │ <workspace>/.zvec-grep/  │
   │   manifest.json          │
   │   files.zvec             │
   │   index.zvec             │
   └──────────────────────────┘
```

**要点：**

- CLI 的 `auto/server/direct` 只影响**进程生命周期与协调**，不影响搜索行为
- Agent 走 MCP → 一定走 Server（`--rg` 除外，它不需要 index 也不需要 server）
- `Indexed search` 和 `managed rg` 是**同一个产品边界下的两条路径**



## 2、执行模式：auto / server / direct

| Mode        | 行为                                                         | 适合场景                                    |
| :---------- | :----------------------------------------------------------- | :------------------------------------------ |
| **auto**（默认）| Server 就绪就用；否则走 Direct。**不会自动启动 Server**       | 日常                                        |
| **server**  | 必须有 daemon，否则失败                                      | Agent 走 MCP、反复检索、后台刷新、共享加载的模型 |
| **direct**  | 在当前进程里跑                                               | 一次性、`CI`、前台调试                      |

**managed rg** 不管 Server 是否可用，都在本地跑。



## 3、状态存放

| 位置                    | 内容                                       |
| :---------------------- | :----------------------------------------- |
| `<workspace>/.zvec-grep/` | 每个仓库的索引、文件元信息、authorization |
| `~/.zvec-grep/`         | 全局配置、models 缓存、daemon 日志         |

**信任边界：**

- 扫描、`rg`、索引存储、本地 embedding → **全在设备上**
- Server 只监听 loopback
- **唯一例外：** 使用远程 embedding provider 会把 query 或 workspace 内容发到远端——`zg` 会先要求显式的一次性或工作区级别授权



---



# 九、环境变量参考

| 变量                          | 作用                                          |
| :---------------------------- | :-------------------------------------------- |
| `ZVEC_GREP_HOME`              | 覆盖 `zvec-grep` 的 state 目录                |
| `ZVEC_GREP_MODE`              | 默认执行模式 `direct/server/auto`             |
| `ZVEC_GREP_SERVER_URL`        | 客户端使用的 `MCP Server URL`                 |
| `ZVEC_GREP_SERVER_TOKEN`      | Server/Client 的 Bearer token                 |
| `ZVEC_GREP_SERVER_TOKEN_FILE` | 存放 token 的文件                             |
| `ZVEC_GREP_MCP_TOOLSET`       | 默认 `agent` / `full`                         |
| `ZVEC_GREP_EMBEDDING`         | 新索引的默认模型                              |
| `ZVEC_GREP_API_KEY`           | Embedding provider API Key                    |
| `ZVEC_GREP_ENDPOINT`          | 远程 embedding endpoint                       |
| `ZVEC_GREP_MODEL_CACHE`       | 本地模型缓存目录                              |
| `ZVEC_GREP_DEVICE`            | 本地模型设备                                  |
| `DASHSCOPE_API_KEY`           | Qwen API Key 后备                             |
| `QWEN_API_KEY`                | Qwen API Key 再后备                           |
| `QWEN_HOME`                   | Qwen Code 配置目录（`zg install` 会用）       |
| `QODER_CONFIG_DIR`            | Qoder CLI 配置目录                            |
| `QODER_IDE_MCP_PATH`          | Qoder IDE 的 `mcp.json` 完整路径              |
| `QODER_IDE_EXECUTABLE`        | Qoder IDE 可执行文件路径                      |

**优先级规则：**

- **新索引模型：** 显式 `--embedding` → `ZVEC_GREP_EMBEDDING` → 全局默认
- **已有索引：** 除非 `--embedding + --rebuild` 组合，否则保留 stored 的模型
- **Embedding 运行时（endpoint/device 等）：** 显式命令参数 → workspace snapshot → 全局 config → 环境变量
- `zg index` 在 server/auto 模式下会转发 `ZVEC_GREP_EMBEDDING`；直连 MCP 走的是 daemon 启动时继承的环境



---



# 十、和 `Superpowers` 的组合姿势

> 如果你已经在用 `Superpowers` 那套工作流，`zg` 补的是**"AI 读代码库时的手感"**这一环。

## 1、痛点补齐

| 场景                        | 传统姿势                          | `zg` 姿势                       |
| :-------------------------- | :-------------------------------- | :------------------------------ |
| 找一个概念在哪儿实现        | 反复 `grep` 各种可能的名字        | `zg query "..."` 一句自然语言   |
| 找符号定义                  | `grep "function xxx"`             | `zg query --prefer-symbol --symbol-type function "xxx"` |
| 跨文件综合                  | 手工串起多次 `grep`               | `zg query --fuse "..." "..."`   |
| 精确校验前面的语义结果      | `grep` + 眼力活                   | `zg query --rg`                 |





## 2、和 `brainstorming` / `systematic-debugging` 的接续

```
Superpowers 流程                        zg 的位置
─────────────────                       ──────────
brainstorming（澄清需求）              ← zg 帮你先查现有代码怎么写
    ↓
writing-plans（拆解任务）              ← zg 帮你确认相关文件清单
    ↓
subagent-driven-development（执行）    ← 每个 subagent 用 zg 快速定位现有实现
    ↓
systematic-debugging（找 bug）         ← zg 帮你定位错误路径的调用点
    ↓
verification-before-completion（验证） ← zg 帮你确认改动没漏地方
```

对于 Claude Code 用户：`zg install --target claude --yes` 装完，`Superpowers skill` + `zg MCP` 就在同一个会话里协同工作了。



---



# 十一、和「上下文知识库 / `CodeGraph` / `GitNexus`」的组合

> **一句话结论：能组合，而且高度互补**——它们分别属于四个不同粒度层，几乎不重叠。

## 1、先摆定位：四者到底各干什么

| 层级       | 工具                       | 回答的问题                                  | 数据源                       |
| :--------- | :------------------------- | :------------------------------------------ | :--------------------------- |
| 🌐 领域层  | **`.context/` 上下文知识库** | 这个项目**做什么**？边界、术语、关键决策是啥？| 人工 + AI 摘要的 Markdown     |
| 🔍 检索层  | **`zg`（zvec-grep）**       | **有没有 / 在哪里 / 意思相近的东西**        | 全文（`BM25`）+ 向量索引     |
| 🕸️ 符号层 | **`CodeGraph`**            | 这个函数**被谁调用**？它又调用了什么？      | `AST` 抽取的调用图            |
| 🏗️ 架构层 | **`GitNexus`**             | 动手改这个**会破坏什么**？路由/契约/进程流程| `AST` + 路由 + 契约 + 进程图  |

一句话四连：

- **`.context/` 是"框架"**——先告诉 AI 这个项目在做什么
- **`zg` 是"发现"**——不知道叫什么、在哪儿时用它撒网
- **`CodeGraph` 是"理解"**——找到符号后看它的调用邻居
- **`GitNexus` 是"评估"**——动手改前看爆炸半径



## 2、为什么可以组合（不打架）

**数据源、粒度、时机**都不重叠：

| 维度       | `zg`               | `CodeGraph`        | `GitNexus`             | `.context/`        |
| :--------- | :----------------- | :----------------- | :--------------------- | :----------------- |
| 数据源     | `chunk` 向量 + `BM25` | `AST` 调用图       | `AST` + 路由 + 契约图  | 人工写的 `Markdown` |
| 粒度       | 段落 / 符号        | 单个符号 + 邻居    | 符号 + 进程 + 路由     | 项目 / 领域         |
| 最擅长的   | 模糊语义搜索       | 精确调用关系       | 变更影响评估           | 业务定位            |
| 索引形态   | `.zvec-grep/`      | `.codegraph/`      | `GitNexus` 本地服务     | `.context/`         |
| 是否 `MCP` | ✅ `zvec_grep_*`   | ✅ `codegraph_*`   | ✅ `mcp__gitnexus__*` | ❌ 读文件即用       |
| 谁维护     | 自动索引           | 自动索引           | 自动索引               | **人工审阅 + AI 半自动** |

**MCP 工具名字不冲突、索引目录不冲突、语义边界清晰**——AI Agent 可以按需路由，不会打架。



## 3、典型协同工作流

### 1）读一个陌生项目

```
Step 1: 读 .context/                        ← 先了解项目在做什么、有哪些域
   ↓
Step 2: zg query "..."                      ← 用自然语言定位相关文件
   ↓
Step 3: codegraph_explore <关键符号>          ← 展开找到的符号看调用链
   ↓
Step 4: mcp__gitnexus__impact <关键符号>      ← 想动手前，看这个符号被谁依赖
```

### 2）改一个 `API`

```
Step 1: zg query "订单退款流程"                ← 语义定位实现在哪
   ↓
Step 2: mcp__gitnexus__api_impact /refund     ← 看 route 消费者、mismatch、风险
   ↓
Step 3: codegraph_node RefundService          ← 展开符号定义 + callers
   ↓
Step 4: 改代码
   ↓
Step 5: mcp__gitnexus__detect_changes         ← 未提交改动影响哪些 process
   ↓
Step 6: zg query --rg <关键字> 补漏            ← 精确校验
```

### 3）修一个 `Bug`

```
Step 1: zg query "错误消息片段"                ← 定位到抛错点
   ↓
Step 2: codegraph_explore <抛错函数>          ← 展开调用链找到源头
   ↓
Step 3: mcp__gitnexus__impact <修复点>        ← 评估修复影响面
   ↓
Step 4: 改代码 + 补测试
```



## 4、和 `Superpowers` 流程的整体嵌入

> 把第十章的 `Superpowers` 工作流展开成"每一步用哪些工具"：

```
brainstorming（澄清需求）
    ↓ 用什么：.context/  +  zg query
    │  ── .context/ 告诉 AI 域边界，避免出圈
    │  ── zg query 帮 AI 快速验证"这个功能是不是已有实现"
    ↓
writing-plans（拆解任务）
    ↓ 用什么：zg query  +  codegraph_explore
    │  ── zg 定位相关文件清单
    │  ── codegraph 展开被影响符号的调用邻居
    ↓
subagent-driven-development（执行）
    ↓ 用什么：zg query  +  codegraph_node
    │  ── 每个 subagent 用 zg 快速定位现有实现
    │  ── 用 codegraph_node 拿到符号完整源码
    ↓
systematic-debugging（找 Bug）
    ↓ 用什么：zg query  +  codegraph_explore
    │  ── zg 找错误消息 / 抛错路径
    │  ── codegraph 溯源调用链
    ↓
verification-before-completion（验证）
    ↓ 用什么：GitNexus detect_changes  +  zg query --rg
    │  ── detect_changes 检查改动影响的 process
    │  ── zg --rg 精确 grep 校验漏改
    ↓
finishing-a-development-branch（收尾）
    ↓ 用什么：更新 .context/（可选）
       ── 如果本次改动引入了新领域概念，同步进 .context/
```



## 5、什么时候不组合也行

**不是所有项目都要装全套**——组合姿势要看规模：

| 项目形态                         | 推荐组合                          |
| :------------------------------- | :-------------------------------- |
| **单人小 Demo / 一次性脚本**     | 什么都别装，`grep` 就够            |
| **中小型代码仓（<1w 文件）**     | `zg` 足矣                         |
| **中大型仓 + AI 常读**           | `zg` + `CodeGraph`                |
| **多服务架构 / API 影响面复杂**  | `zg` + `CodeGraph` + `GitNexus`   |
| **团队协作 / 有领域复杂度**      | **加 `.context/`**（团队共享领域知识） |
| **纯文档 / 语料库**              | 只用 `zg`（`CodeGraph`/`GitNexus` 派不上用场）|
| **一次性 CI 检查**               | `zg query --rg`（免索引）         |



## 6、组合的成本与 `.gitignore`

| 工具         | 索引目录        | 索引成本                    | 是否 `git` 提交         |
| :----------- | :-------------- | :-------------------------- | :---------------------- |
| `.context/`  | `.context/`     | 一次性 + 按需人工更新       | ✅ **要提交（团队共享）**|
| `zg`         | `.zvec-grep/`   | 首次全量 + watcher 增量     | ❌ 建议 `.gitignore`    |
| `CodeGraph`  | `.codegraph/`   | 首次全量 + `codegraph reindex` | ❌ 建议 `.gitignore`    |
| `GitNexus`   | 服务侧本地库    | 首次全量 + hook 增量        | ❌ 服务侧维护           |

**推荐 `.gitignore`：**

```
.zvec-grep/
.codegraph/
```

**唯一需要提交的是 `.context/`**——因为它是人工审阅过的领域知识，是团队和 AI 共享的"项目宪法"。



## 7、AI Agent 侧的路由规则（推荐写进 `CLAUDE.md`）

```
# 检索路由（按优先级从高到低）

1. 打开新任务先读 .context/README.md 和相关域的 CONTEXT.md
2. 关键词/正则/文件名明确             → 原生 grep / rg / Read
3. 词法或位置不明 / 需要跨文件综合     → zvec_grep_search
4. 需要调用关系、找 callers          → codegraph_explore / codegraph_node
5. 改动前评估影响面                  → mcp__gitnexus__impact / api_impact
6. 改动后自检                       → mcp__gitnexus__detect_changes
```



## 8、组合的价值：`Token` 效率

四者叠加带来的**最实际收益**是 token 效率——每个工具都在自己擅长的粒度上给"精准结果"，而不是塞一堆全文让 AI 去筛：

| 场景               | 只用 `grep`（撒网）  | 全套组合             |
| :----------------- | :------------------- | :------------------- |
| 找"用户主题偏好"实现 | grep 10+ 次关键词    | `zg query` 1 次      |
| 看函数的所有调用者   | grep 函数名 + 人肉过滤| `codegraph_node` 1 次|
| 改前评估影响         | 靠记忆 / 手工翻代码  | `gitnexus impact` 1 次|
| 理解项目做什么       | 读一大堆源码          | 读 `.context/` 一页   |

**AI 少调用 tool = 少烧 token = 上下文不膨胀 = 回答质量更高**——这是组合的本质收益。



---



# 十二、常见问题

## 1、`zg` 和 `ripgrep` 什么关系

- `zg` **不是替代** `ripgrep`——它**包含**了 `ripgrep`（`zg query --rg`）。
- 语义/`BM25` 是 `zg` 提供的**额外能力**。
- 需要精确正则匹配时，`ripgrep` 依然是首选路径。



## 2、我该建索引吗

**该建：** 
- 需要频繁语义搜索的代码仓
- Agent 会读这个仓
- 语料相对稳定，不是分钟级大改

**不该建：**

- 一次性查一个明确关键词 → `zg query --rg` 就够了
- 完全不重复访问的临时目录



## 3、索引会不会污染 `git`

会。建议：

```bash
echo ".zvec-grep/" >> .gitignore
```



## 4、切换模型很慢怎么办

**先想清楚再切**——切了必须全量 rebuild。建议：

1. 小仓上先 A/B 试
2. `zg query --debug` 看 recall 差异
3. 确认后再对大仓做 `--rebuild`



## 5、`Server` 起不来 / Agent 找不到工具

**排查顺序：**

```bash
# 1. Server 状态
zg server status --check-ready
# 退出码非零 → 起 server
zg server on

# 2. 端口占用？换端口
zg server on --listen 127.0.0.1:8999
export ZVEC_GREP_SERVER_URL=http://127.0.0.1:8999/mcp

# 3. 重装 Agent 集成
zg uninstall --target claude --yes
zg install --target claude --yes
# 重启 Claude Code

# 4. 看日志
tail -f ~/.zvec-grep/daemon/logs/server.log
```



---



# 十三、速查表

## 1、最常用 10 条命令

```bash
# ── 装 & 起 ──
npm install -g @zvec/zvec-grep
zg install --target claude --yes       # 集成到 Claude Code
zg server on                           # 起 MCP server

# ── 建索引 ──
zg index --embedding local/potion-code-16m-v2   # 首次
zg index                                        # 增量更新
zg index --rebuild --embedding <new-model>     # 换模型重建

# ── 查询 ──
zg query "自然语言意图"                          # 混合检索
zg query --fts "loadTheme" -t ts                # 精确关键词
zg query --rg -F "AuthService" src              # 免索引穷尽 rg
zg status                                        # 看索引健康度
```



## 2、Agent 端最常用工具

```
zvec_grep_search(
  root="/abs/path",
  query="where the plugin lifecycle is set up",
  limit=10,
  fuse=true
)
```



## 3、模型速查

| 目标                | 选它                                     |
| :------------------ | :--------------------------------------- |
| 首次快速试玩        | `local/potion-code-16m-v2`               |
| 长上下文代码检索    | `local/jina-embeddings-v2-base-code`     |
| 多语言文档          | `local/potion-multilingual-128m`         |
| 128K token / 无本地 | `qwen/qwen3.7-text-embedding`（远程）     |
| 图像 + 文本         | `qwen/qwen3-vl-embedding`（远程）         |



---





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

