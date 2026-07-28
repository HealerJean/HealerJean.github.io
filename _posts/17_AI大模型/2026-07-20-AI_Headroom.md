---
title: AI_Headroom
date: 2026-05-29 00:00:00
tags: 
- AI
category: 
- AI
description: AI_Headroom
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、`Headroom`  上下文压缩层



> `Headroom` 是一个**透明代理**，充当你的 AI 工具（Claude Code、Cursor 等）与 LLM 提供商（Anthropic、OpenAI、Bedrock）之间的中间层。它拦截所有发出的请求，对工具输出、日志、文件、对话历史进行压缩，再转发给 LLM。



## 1、`AI Agent` 原生上下文成本问题2

1. **工具输出冗余爆炸**：日志、代码搜索、数据库查询返回千行 `JSON`，90% 为重复字段、重复样本；

2. **`RAG` 检索噪声**：批量检索文档大量重复语义，占用上万` Token`；

3. **对话历史无限累积**：多轮会话填满上下文窗口，触发截断、模型失忆；

4. **动态系统提示破坏缓存**：日期、`UUID`、会话 ID 频繁变动，导致 `OpenAI` / `Anthropic` 前缀缓存失效；

5. **传统方案缺陷对比**

   | 方案           | 缺点                                              |
   | -------------- | ------------------------------------------------- |
   | 直接截断       | 永久丢失关键数据，故障排查、代码分析极易出错      |
   | `LLM` 摘要压缩 | 额外消耗一轮输入 + 输出 Token，摘要失真、丢失细节 |
   | 简单文本去重   | 无法识别结构化 JSON、代码、日志，压缩率极低       |




## 2、`Headroom` 核心优势

1. **本地计算、零额外 LLM 调用**：全部压缩算法本地 `CPU/Rust` 执行，不产生额外 API 费用；
2. **内容感知分引擎压缩**：JSON / 代码 / 日志 / 纯文本 / 系统提示分别专属算法，非一刀切；
3. **可逆压缩（`CCR`）**：原始全文本地持久化缓存，模型需要时一键取回完整数据；
4. **多接入模式、零代码改造**：`Proxy`、`Wrap`、`Library`、`MCP` 四种集成方式；
5. **缓存对齐优化**：分离系统提示静态 / 动态内容，大幅提升厂商前缀缓存命中率；
6. **本地优先、数据不出机**：原文存储 SQLite，合规、隐私友好。
7. 用户/助手的对话消息永不被修改，只压缩工具输出



## 3、核心指标（官方基准测试）

| 场景                      | 原始 Token | 压缩后 Token | 节省比例 |
| ------------------------- | ---------- | ------------ | -------- |
| SRE 故障日志排查          | 65694      | 5118         | 92%      |
| 代码搜索 100 条 JSON 结果 | 10144      | 1260         | 87.6%    |
| RAG 长文档批量检索        | 28000      | 9800         | 65%      |
| 多轮对话历史（50 轮）     | 16000      | 4200         | 73.7%    |
| 单文件完整源码            | 12000      | 5400         | 55%      |



## 4、**真实节省数据**

精度损失：GSM8K 数学题 100 道，压缩前 87.0%，压缩后 87.0%，**零损失**。

| 工作负载             | 压缩前 tokens | 压缩后 tokens | 节省    |
| :------------------- | :------------ | :------------ | :------ |
| 代码搜索（100 结果） | 17,765        | 1,408         | **92%** |
| SRE 故障排查         | 65,694        | 5,118         | **92%** |
| GitHub issue 分类    | 54,174        | 14,761        | **73%** |
| 代码库探索           | 78,502        | 41,254        | **47%** |



## 5、**常用命令**

```
# 启动代理服务
headroom proxy [--port] [--cache-path]

# 封装本地AI客户端
headroom wrap claude|aider|cursor|copilot

# 启动MCP服务
headroom mcp-server

# 查看Token节省统计
headroom stats

# 清理过期CCR缓存
headroom cache clean

# 学习历史失败会话，优化压缩策略
headroom learn

 # 查看节省数据
headroom perf                


# 实时仪表盘
headroom dashboard            
```





## 6、安装

### 1）命令安装

```
# 全局安装CLI
pip install "headroom-ai[cli]"
# 验证安装
headroom --version
```



### 2）模型接入 - 模式 1：Proxy 反向代理（零代码改造，推荐生产）

> 无需修改 Agent、LLM 调用代码，转发所有 API 流量经过压缩层

```
# 启动代理，默认端口8787
headroom proxy --port 8787 --cache-path ./headroom_cache

# 修改原有LLM接口地址
# 原OpenAI地址：https://api.openai.com/v1
# 改为本地代理：http://127.0.0.1:8787/v1
```



### 2）模型接入- 模式2：MCP 标准服务接入（MCP Agent 生态）

> 启动 MCP 服务，提供三个标准工具给 Agent 调用：

```
headroom mcp-server --port 8989
```

MCP 可用工具：

1. `headroom_compress`：主动压缩输入上下文
2. `headroom_retrieve(hash)`：读取 `CCR` 缓存原始全文
3. `headroom_stats`：查询本次会话 `Token` 节省统计



# 二、整体架构与核心模块

## 1、架构总览

- `Agent`/应用 → `Headroom` 入口层 → `CacheAligner `→ `ContentRouter `→ 专用压缩引擎 → `CCR`缓存写入 → 压缩上下文 → `LLM`
- `LLM` 需要完整数据 → 调用 `headroom_retrieve` → 从本地 `CCR` 读取原文

```
你的 AI 工具（Claude Code / Cursor / Codex...）
    │ 工具输出 + 日志 + 文件 + 对话历史
    ▼
┌─────────────────────────────────────────────────┐
│                   Headroom 代理                  │
│                                                  │
│  CacheAligner  ─── 前缀稳定化，触发 KV 缓存      │
│  ContentRouter ─── 判断内容类型，分发到对应压缩器 │
│      ├── SmartCrusher      (JSON 数组统计压缩)   │
│      ├── CodeCompressor    (AST 代码结构压缩)    │
│      ├── DiffCompressor    (git diff 压缩)       │
│      ├── LogCompressor     (日志模板聚类)        │
│      └── Kompress-base     (ML 文本分类器)       │
│                                                  │
│  CCR 缓存层 ─── 存储被压缩的原始内容             │
└─────────────────────────────────────────────────┘
    │ 压缩后内容 + 检索工具
    ▼
LLM 提供商
```



## 2、顶层模块总览

### 1）`CacheAligner`（系统提示缓存对齐器）

**作用**：拆分系统提示静态前缀与动态变量，稳定前缀以命中 `LLM` 厂商缓存

- 自动识别动态字段：当前日期、时间戳、`UUID`、`Session` `Token`、随机 `ID`；

- 将动态内容统一后置到提示末尾 `[Context: xxx]`；

- 性能开销：亚毫秒级，无 `Token` 损耗；

- 示例对比：

  ```
  # 处理前（每日变化，缓存失效）
  你是代码助手。当前日期：2026-07-20
  
  # 处理后（静态前缀固定，动态后置）
  你是代码助手。
  [动态上下文：当前日期：2026-07-20]
  ```

  



### 2）`ContentRouter`（内容路由分发器）

自动检测输入内容类型，路由至对应专用压缩引擎：

1. JSON 数组 / 工具结构化输出 → SmartCrusher
2. 源代码（JS/TS/Python/Go/Java 等）→ CodeCompressor
3. 应用 / 容器日志 → LogCompressor
4. 散文、RAG 纯文本、对话历史 → Kompress-v2-base





## 3、四大专用压缩引擎

### 1）`SmartCrusher `— `JSON` 结构化数据压缩（最高压缩收益）

**典型压缩率：70%–95%**   

适用：工具调用返回、API 列表、数据库查询、代码搜索结果    

**算法流程：**

1. 计算自适应保留量 `adaptive_k`，数组 ≤ k 直接跳过
2. **无损优先**：尝试用 \`CSV` + `schema` 重排，若节省 ≥ 30% 则采用（不丢行）
3. **有损路径**：分析数组，选策略，保留重要行，其余写入 CCR 缓存

四种压缩策略：

| 策略           | 场景           | 做法                                   |
| :------------- | :------------- | :------------------------------------- |
| `time_series`  | 带时间戳的指标 | 检测方差突变点，保留 spike，摘要平稳段 |
| `cluster`      | 日志条目       | 相似消息聚类，每类保留 1-2 条          |
| `top_n`        | 搜索结果       | 按 score/rank 排序，保留前 N           |
| `smart_sample` | 通用           | 统计采样 + 抽取常量字段                |

**始终保留**：错误项（100%）、首尾项、统计异常值（>2 标准差）、与用户查询相关的项。

**压缩示例（时序指标）：**

```
// 压缩前：60 个数据点（约 300 tokens）
[
  {"ts": 1, "host": "prod-1", "cpu": 45},
  {"ts": 2, "host": "prod-1", "cpu": 44},
// ... 重复 43 次，cpu 在 44-46 之间
  {"ts": 45, "host": "prod-1", "cpu": 92},  // ← 突变点
  {"ts": 46, "host": "prod-1", "cpu": 95}
]

// 压缩后（约 40 tokens）
{"_constant_fields": {"host": "prod-1"}}
{"ts": 1, "cpu": 45}
// items 2-44: cpu stable ~45, summarized
{"ts": 45, "cpu": 92}
{"ts": 46, "cpu": 95}
{"_ccr_dropped": "<<ccr:abc123def 42_rows_offloaded>>"}
```





### 2）`CodeCompressor` — AST 感知代码压缩

**典型压缩率：30%–60%**   

基于 tree-sitter 语法树解析，非简单文本删减：

1. 保留类、函数、接口、异常分支、注释关键逻辑；
2. 删除空行、重复导入、冗余注释、测试样板代码；
3. 折叠长常量数组、无用辅助函数；
4. 区分业务代码 / 测试代码，优先保留业务逻辑；

**压缩示例：**

```
# 压缩前（~40 tokens）
def process_items(items: List[str]) -> List[str]:
    """Process a list of items."""
    results = []
    for item in items:
        ifnot item:
            continue
        processed = item.strip().lower()
        results.append(processed)
    return results


# 压缩后（~15 tokens，节省 60%）
def process_items(items: List[str]) -> List[str]:
    """Process a list of items."""
    results = []
    for item in items:
    # ... (5 lines compressed)
    pass
```



### 3）**`DiffCompressor` — `Git diff` 压缩**

解析 `unified-diff` 格式，按重要性评分选择保留哪些 `hunk`。

**算法：**

1. 拆分成 files + hunks
2. 文件上限 20 个，超出按改动量排序取最重要的
3. 每文件 hunk 上限 10 个，保留首 hunk + 尾 hunk + 评分最高的中间 hunk
4. 每个 hunk 的上下文行数剪裁到改动前后各 2 行

`Hunk` 评分公式：

```
score = min(改动行数 × 0.03, 0.3)      // 改动密度
      + 查询词匹配 × 0.2               // 与用户查询的关联
      + 优先级模式匹配 × 0.3           // error/fail/security 等关键词
```

**压缩示例（177 行 → 129 行 + CCR 标记）：**

```
# 压缩前：8 个文件完整 diff，177 行

# 压缩后：
diff --git a/file_0.py b/file_0.py
@@ -1,10 +1,12 @@
 context_4_0          ← 只保留改动前 2 行
-removed_0_0
+added_0_0
 tail_0_0             ← 只保留改动后 2 行
...
[8 files changed, +40 -24 lines]
[177 lines compressed to 129. Retrieve full diff: hash=5d41402abc4b2a]
```







### 3）`LogCompressor` — 日志专用压缩

典型压缩率：60%–90%

1. 合并重复日志模板，只保留唯一模板 + 计数；
2. 过滤 `INFO` 级别无告警日志，强制保留 ERROR/FATAL/WARN；
3. 提取堆栈核心行，折叠重复调用栈；



### 4）`Kompress-v2-base` — 语义文本压缩（`HuggingFace` 开源）

基于 `ModernBERT` 轻量语义模型本地推理：

1. 语义聚类合并高度相似段落；
2. 保留实体、数字、关键结论，删减修饰性冗余语句；
3. 对话场景自动区分问答对，弱化重复寒暄；

- 典型压缩率：40%–70%





## 4、`IntelligentContext` — 对话窗口管理器

当总 `Token` 接近模型上下文上限时自动执行滚动压缩：

1. 六维打分每条历史消息：时效性、语义独特性、错误标记、引用关系、Token 密度、任务相关性；
2. 优先驱逐低分历史，系统提示、最新 3 轮对话永久保留；
3. 被驱逐内容写入 CCR 缓存，模型可随时取回；
4. 两种工作模式：
   - `RollingWindow`：先进先出极简模式（速度最快）
   - `ScoreEvict`：智能评分驱逐（信息保留最优）







## 5、`CCR`（`Compress-Cache-Retrieve`）可逆缓存层

### 1）**`Headroom `与普通截断工具最大区别**

1. 压缩流程中，原始全文通过哈希索引存入本地 SQLite；
2. 向 `LLM` 注入工具指令 `headroom_retrieve(hash_id)`；
3. 模型遇到细节不足时主动调用工具，`Headroom` 读取本地原文注入上下文；
4. 支持持久化、跨会话共享缓存、自动去重重复检索内容；
5. 可配置缓存过期时间、磁盘最大占用上限。

```
大型 tool output（1000 项 API 结果）
    │
    ├── SmartCrusher 压缩到 20 项
    ├── 原始 1000 项 → 本地缓存（SHA256 hash 作为 key）
    └── 压缩结果末尾插入检索标记：
        {"_ccr_dropped": "<<ccr:abc123def456 980_rows_offloaded>>"}

LLM 看到 20 项 + 检索标记

情形 A：20 项足够 → Done，节省 90%
情形 B：LLM 需要更多 → 调用 headroom_retrieve(hash="abc123def456")
        → 本地缓存取出（~1ms）
        → 继续对话
```



### 2）压缩可逆机制完整流程（CCR）

1. 原始内容进入压缩引擎前，计算唯一内容哈希；
2. 全文写入 SQLite CCR 数据库，hash 作为主键；
3. 压缩完成后，压缩文本中嵌入引用标记 `[[HEADROOM_REF:hashxxx]]`；
4. LLM 推理时发现信息不足，自动调用内置工具 `headroom_retrieve("hashxxx")`；
5. Headroom 拦截工具调用，读取数据库原始全文，插入当前上下文；
6. 模型基于完整原始数据继续推理，全程对业务透明。







# 三、使用教程

## 1、配置文件详解

```
# 全局压缩等级 low/medium/high
compression_level: high

# CCR可逆缓存配置
cache:
  path: "./headroom_cache.db"
  max_disk_gb: 15
  ttl_days: 30 # 缓存自动过期30天
  cross_agent_share: true # 多Agent共享缓存

# 各引擎开关
engines:
  cache_aligner: true
  smart_crusher: true
  code_compressor: true
  log_compressor: true
  kompress_text: true

# 对话窗口管理
context_window:
  strategy: ScoreEvict # RollingWindow / ScoreEvict
  reserve_turns: 3 # 保留最新3轮对话不压缩驱逐

# Proxy服务配置
proxy:
  port: 8787
  host: 0.0.0.0
  enable_stats_api: true # 开启/token-stats监控接口

# 日志输出
log_level: info
```



## 2、适用场景与不适用场景

### 1）推荐使用场景

1. AI 代码 Agent（Cursor、Aider、Claude Code、Copilot）
2. SRE / 运维故障排查 Agent（kubectl、日志、监控数据）
3. RAG 知识库批量检索问答系统
4. 企业内部知识库长文档对话机器人
5. 多轮长会话客服 Agent
6. 批量数据结构化分析 Agent（数据库、API 批量查询）



### 2）不推荐场景

1. 超短单轮问答（单条 < 500 Token，压缩收益极低）
2. 对延迟极致敏感、零本地计算资源环境（边缘弱设备）
3. 一次性临时请求，无重复工具调用 / 检索



## 3、`FQA`

### 1）压缩后模型会不会丢失关键信息？

- 官方基准测试信息保留精度 97% 以上。所有异常、报错、样本边界、代码核心逻辑强制保留；
- 同时支持 `CCR` 随时调取原文，不存在永久丢失。



### 2）本地缓存会不会占用大量磁盘？

- 可配置`max_disk_gb`磁盘上限，超出自动淘汰最旧缓存；
- 支持 ttl 自动清理过期数据，常规开发场景 1–5GB 足够长期使用。



### 3）是否支持本地开源大模型（Llama/Qwen/Mistral）？

答案：完全兼容，Proxy 模式可转发任意本地 OpenAI 兼容接口，压缩层与底层模型解耦。



### 4）能否禁用可逆缓存，只做单向压缩？

答案：初始化时设置`enable_ccr=False `即可，此时原始数据不落地，仅做一次性轻量化压缩，适合隐私极高场景。



### 5）动态日期每次都会重新压缩吗？

答案：`CacheAligner `分离静态前缀，只有末尾动态小段变化，前缀持续命中厂商缓存，大幅减少重复 `Token` 计费。

















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



