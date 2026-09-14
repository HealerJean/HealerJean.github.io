---
title: Open_Code_Review
date: 2026-09-03 00:00:00
tags: 
- AI
category: 
- AI
description: Open_Code_Review
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)        

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)         

 https://github.com/alibaba/open-code-review

 https://open-codereview.ai



# 一、认识 `OpenCodeReview`

## 1、是什么：阿里内部验证两年、服务数万开发者的 AI 代码审查工具

`Open Code Review`（命令行工具名 `ocr`）起源于**阿里巴巴集团内部官方 AI 代码审查助手**——过去两年里，它在内部服务了数万名开发者，识别出**数百万个代码缺陷**。在超大规模场景下充分验证后，阿里将其孵化为开源项目。

它的工作方式：

- 读取 `Git diff`，将变更文件交给一个**具备工具调用能力的 LLM Agent**
- Agent 可以读取完整文件内容、搜索代码库、查看其他变更文件获取上下文，产出**行级精确定位**的结构化审查意见——而不只是表层 `diff` 反馈
- 内置**多语言规则集**（`NPE`、线程安全、`XSS`、`SQL` 注入等），兼容 `OpenAI` 和 `Anthropic` 协议
- 除 `diff` 审查外，`ocr scan` 还可对**整个文件**做审查，适合审计陌生代码库或没有有效 `diff` 的目录



## 2、为什么不用 `Claude Code` 直接审？

用过 `Claude Code` + `Skills` 做代码审查的同学，大概率遇到过这些痛点：

| 痛点             | 表现                                                     |
| :--------------- | :------------------------------------------------------- |
| **覆盖不全**     | 大变更集上，Agent 倾向于"偷懒"，只挑部分文件审，漏掉其他 |
| **位置漂移**     | 报出的问题经常对不上真实代码位置，行号或文件引用偏移     |
| **质量不稳定**   | 自然语言驱动的 `Skill` 难以调试，`prompt` 稍微一变质量就波动 |

**根因：纯语言驱动的架构，缺少对审查过程的硬约束。**



## 3、核心设计：确定性工程 × `Agent` 混合架构

`OpenCodeReview` 的核心理念是**确定性工程与 `Agent` 分工协作，各自做最擅长的事**。

### 1）确定性工程 —— 硬约束

对于**绝不允许出错**的审查步骤，用工程逻辑（而非语言模型）保证正确性：

| 能力             | 说明                                                                                              |
| :--------------- | :------------------------------------------------------------------------------------------------ |
| 精确文件选择     | 精确决定哪些文件需要审查、哪些应被过滤，确保不漏掉重要变更                                        |
| 智能文件捆绑     | 将相关文件分为一个审查单元（如 `message_en.properties` 和 `message_zh.properties` 捆在一起），每个捆绑作为**上下文隔离的子 Agent** 运行——分治策略，大变更集上依然稳定，天然支持并发 |
| 细粒度规则匹配   | 按文件特征匹配审查规则，让模型注意力高度聚焦，从源头消除信息噪音；相比纯语言驱动的规则引导，基于模板引擎的规则匹配更稳定、可预测 |
| 外置定位与反思   | 独立的**评论定位模块**和**评论反思模块**，系统性提升 AI 反馈的位置准确率与内容准确率              |

### 2）`Agent` —— 动态决策

`Agent` 的能力集中在最有价值的地方——**动态决策与动态上下文检索**：

- **场景调优的 `Prompt`**：针对代码审查深度优化的提示词模板，提升效果的同时降低 `Token` 消耗
- **场景调优的工具集**：从大规模生产数据的工具调用轨迹中提炼（调用频率分布、单工具重复率、新工具对调用链的影响），专为代码审查打造，比通用 `Agent` 工具箱更稳定



## 4、`Benchmark`：同等模型下更准、更快、更省

`AACR-Bench`：基于 **50** 个热门开源仓库、**200** 个真实 `Pull Request`、**10** 种编程语言构建的真实世界代码审查基准，由 **80+** 位资深工程师交叉标注（**1,505** 个已标注的真实问题）。

| 指标         | 含义                         | 为什么重要                   |
| :----------- | :--------------------------- | :--------------------------- |
| **F1**       | 精确率和召回率的调和平均     | 衡量审查质量的最佳单一数字   |
| **Precision**| 报告的问题中真实缺陷的比例   | 越高 = 需要甄别的误报越少    |
| **Recall**   | 真实缺陷中被发现的比例       | 越高 = 漏掉的问题越少        |
| **Avg Time** | 单次审查耗时                 | 影响 `CI` 流水线延迟         |
| **Avg Token**| 单次审查 `Token` 消耗        | 直接影响 `API` 成本          |

**结论**：与通用 `Agent`（`Claude Code`）相比，同一底座模型下 `OpenCodeReview` 的 `Precision` 和 `F1` 显著更高，`Token` 消耗仅约 **1/9**，审查速度更快。注意其 `Recall` 低于通用 `Agent`——这是**宁可精确、不要噪音**的有意取舍。



# 二、安装与配置

## 1、前提条件

- **`Git >= 2.41`** —— `OpenCodeReview` 依赖 `Git` 做 `diff` 生成、代码搜索和仓库操作

## 2、安装 `CLI`

```bash
npm install -g @alibaba-group/open-code-review
```

安装完成后即可全局使用 `ocr` 命令（其他安装方式如安装脚本、`GitHub Release` 二进制、源码编译见官方文档）。

## 3、配置 `LLM`

首次使用必须配置一个 `LLM`（除非走委托模式），交互式向导会引导选择内置 `Provider`、输入 `API Key`、选择模型，并自动测试连通性：

```bash
ocr config provider          # 选择内置 Provider 或添加自定义
ocr config model             # 为当前 Provider 挑选模型
ocr llm test                 # 测试连通性
```



# 三、`Claude Code` 集成：`/open-code-review:review`

## 1、安装插件

在 `Claude Code` 会话中执行：

```text
/plugin marketplace add alibaba/open-code-review
/plugin install open-code-review@open-code-review
```

安装完成后，会获得两个斜杠命令：

- **`/open-code-review:review`** —— 触发审查（`OCR` 用自己配置的 `LLM` 执行）
- **`/open-code-review:delegate-review`** —— 委托模式审查（见第五章）

## 2、插件做了什么

插件本质上是本地 `ocr CLI` 的封装：

1. 调用 `ocr CLI` 完成**文件选择、文件捆绑、规则匹配**（确定性工程部分）
2. 将组装好的上下文交给 `LLM` 执行审查
3. 将行级评论以结构化形式返回到会话中

## 3、`/review` 与 `/delegate-review` 核心区别

这两个命令虽然都是"审代码"，但**执行审查的 LLM 是谁**、**工作流程**完全不同：



### 1）表格对比

| 维度               | `/open-code-review:review`                                     | `/open-code-review:delegate-review`                            |
| :----------------- | :------------------------------------------------------------- | :------------------------------------------------------------- |
| **谁来审查**       | `OCR` 自己配置的 `LLM`（`ocr config provider/model`）           | `Claude Code` 宿主模型（`Claude` 本身）                         |
| **`API Key`**      | 需要配置 `OCR` 的 `Provider` 和 `API Key`                      | **无需任何 `OCR LLM` 配置**，用宿主订阅即可                     |
| **工作流程**       | 一步到位：执行 `ocr review --audience agent`，`OCR` 内部完成文件选择、捆绑、规则匹配、审查全流程 | 三步走：`ocr delegate preview`（预览审查范围）→ `ocr delegate rule`（获取各文件检查清单）→ 宿主 `Agent` 自己读 `diff`、按规则审查 |
| **确定性工程介入** | 全量介入：文件选择 + 规则匹配 + 独立定位/反思模块              | 部分介入：仅文件选择 + 规则解析，审查由宿主 `Agent` 自由发挥    |
| **`Prompt`/工具链** | `OCR` 场景调优的 `Prompt` 和专用工具集（`Benchmark` 验证过）  | 宿主 `Agent` 的通用能力                                         |
| **审查焦点**       | 多语言规则集（`NPE`、线程安全、`XSS`、`SQL` 注入等）           | 正确性、安全、性能、错误处理、并发、可维护性，只评论变更行       |
| **费用**           | 消耗 `OCR` 配置模型的 `API` 费用                                | 消耗 `Claude Code` 订阅额度，**零额外成本**                     |
| **适用场景**       | 追求 `Precision/F1` 和低 `Token` 消耗，或未订阅 `Claude Code`  | 已订阅 `Claude Code`，想零成本接入；希望审查过程更透明可控      |

**一句话总结**：`/review` 是"`OCR` 全托管"——连审带修一条龙；`/delegate-review` 是"`OCR` 搭台、`Claude` 唱戏"——`OCR` 决定审哪些文件、给什么规则，`Claude` 用自己的脑子审。两者支持相同的审查范围参数（`--commit`、`--from/--to`）和业务上下文参数（`--background` 或 `--background-file`，后者从 `Markdown` 文件加载并限制 8000 字符）。



### 2）`delegate-review` 工作流

#### a、**Step 1: Preview（预览变更范围）**

- 执行预览命令，指定基准分支与当前分支
- 探测工具版本及安装状态
- 统计可评审文件数与总变更文件数
- 确定 merge base 提交点

#### b、**Step 2: Get Rules（加载审查规则）**

获取 4 组规则：

- **Rule Group 1**：Java 系统规则（拼写、死代码、逻辑错误、性能、并发）
- **Rule Group 2**：default 规则（.codegraph/.gitignore）
- **Rule Group 3**：YAML 规则（.openspec.yaml）
- **Rule Group 4**：pom.xml 规则（新增代码不允许 SNAPSHOT）

#### c、**Step 3: Get Diffs and Review（读取差异并逐文件评审）**

读取 26 个 Java 文件的完整源码，逐文件评审：

#### d、**Step 4: Report and Fix（输出报告与修复建议）**

- 按严重级别分类的行级问题清单。



# 四、`ocr review` 实战

## 1、工作区模式：审查本地所有变更

```bash
cd your-project
ocr review
```

审查所有**已暂存、未暂存、未跟踪**的变更——写完代码、提交之前跑一遍最方便。

## 2、分支区间：审查整个 `Feature` 分支

```bash
# 审查 feature-branch 自 main 分叉以来的所有变更（merge-base 模式）
ocr review --from main --to feature-branch
```

## 3、单 `Commit` 审查

```bash
ocr review --commit abc123
```

## 4、断点续审

区间审查或单 `Commit` 审查被中断后，可以恢复会话继续，不用重头再来：

```bash
ocr session list
ocr review --from main --to feature-branch --resume <session-id>
```

## 5、全文件扫描：`ocr scan`

没有 `Git` 历史、或想审计陌生代码库时，直接审整个文件：

```bash
ocr scan                          # 扫描整个仓库
ocr scan --path internal/agent    # 扫描某个目录或指定文件
ocr scan --resume <session-id>   # 恢复被中断的扫描
```

## 6、审查报告：

### 1）报告内容怎么分级？

无论哪个斜杠命令，产出的评论都遵循统一的三级严重度分级：

| 严重度   | 判定标准                                                       | 处置方式               |
| :------- | :------------------------------------------------------------- | :--------------------- |
| **High** | 明显 `Bug`、安全问题、数据丢失、有明确修复方案的错误           | 展示并**自动修复**     |
| **Medium** | 合理但依赖上下文的顾虑、风格/性能建议、需人工落地的修复      | 展示，视情况自动修复   |
| **Low**  | 疑似误报、上下文不足、吹毛求疵、无意义建议                     | **静默丢弃**，不出报告 |

这套分级本身就是报告格式的一部分——你看到的报告永远是**过滤噪音后的高置信度问题清单**。

### 2）验证的模板

````
# OCR 委托审查报告（vs origin/master 全量 diff）

## 审查元信息

表格

| 项目 | 内容 |
| --- | --- |
| 审查模式 | `ocr delegate` 委托模式（OCR 提供文件清单 + 规则，LLM 执行审查） |
| 审查范围 | `origin/master...HEAD`（merge base `{{commit_hash}}`） |
| 可审查文件数 | {{reviewable_file_count}}（总变更 {{total_change_files}} 个文件，excluded {{excluded_file_count}} 个 .md/test /yaml） |
| 变更规模 | +{{add_lines}} / -{{del_lines}} |
| 主线 | {{feature_branch_desc}} |

## 发现清单

### High

#### H1 [{{file_path}}:{{line_no}}] {{问题标题描述}}

**位置**：`{{file_path}}:{{line_no}}`

```
{{问题代码片段}}
```

**违反规则**：{{规则说明，哪条规范 / 约束被违反}}

**状态**：⚠️ **待人工处理**（{{补充说明：涉及发布计划 / 跨团队确认等背景信息}}）

---

#### H2 [{{file_path}}:{{start_line}}-{{end_line}}] {{问题标题描述}}

**位置**：`{{file_path}}:{{start_line}}-{{end_line}}`

**问题描述**：
{{问题现象、风险、与预期语义不一致的地方}}

**修复经过**：

1. {{修复迭代步骤 1}}
2. {{修复迭代步骤 2—— 调整原因、方案选择依据}}

**状态**：✅ **已修复**（{{简述最终修复方案}}）

---

### Medium

#### M1 [{{file_path}}:{{start_line}}-{{end_line}}] {{问题标题描述}}

**位置**：`{{file_path}}:{{start_line}}-{{end_line}}`

**问题描述**：
{{现象：文档注释与代码实现逻辑不一致；配置行为与文档预期不符；调用方会产生误解等}}

**修复**：{{修复动作，例如：Javadoc 内容同步对齐代码实现}}。

**状态**：✅ **已修复**

---

#### M2 [{{file_path}}:{{line_no_a}},{{line_no_b}}] {{问题标题描述}}

**位置**：`{{file_path}}:{{line_no_a}},{{line_no_b}}`

**问题描述**：
{{javadoc / 注释与实际返回值 / 行为不一致，调用方按文档会出现逻辑误判风险}}

**修复**：{{修改注释，与代码实现保持一致，说明返回值语义}}。

**状态**：✅ **已修复**

---

#### M3 [{{file_path}}:{{line_no}}] {{问题标题描述}}

**位置**：`{{file_path}}:{{line_no}}`

**问题描述**：
{{代码路径行为说明；边界竞争场景；理论风险点；时序窗口问题}}

**评估**：{{发生概率、影响范围、兜底机制、数据丢失风险评估}}。

**状态**：📌 **观察项**（未改，{{后续建议：是否需要补充校验 / 后续迭代讨论点}}）

---

#### M4 [{{file_path}}] {{问题标题描述}}

**位置**：`{{file_path}}`

**问题描述**：
{{重构后能力丢失；上层运维 / 配置入口依赖该方法，当前实现落到父类默认空实现}}

**危害**：{{业务 / 运维层面实际影响，运维操作失效，只能通过重启等粗暴手段恢复}}。

**建议修复**：{{具体修复方案：重写方法、内部委托调用、补充字段重置逻辑等}}。

**状态**：⚠️ **待人工处理**（{{涉及运维语义 / 产品侧确认，需要相关方评审}}）

---

#### M5 [{{file_path}}:{{line_no}}] {{问题标题描述}}

**位置**：`{{file_path}}:{{line_no}}`

**问题描述**：
{{无用 import、冗余变量、死代码等小问题}}

**修复**：{{删除未使用 import / 清理死代码}}。

**状态**：✅ **已修复**

---

## 观察但不属于本 PR 缺陷

> 
> 以下为存量代码已有问题，**非本次变更引入**，仅做记录，本次不修复

- `{{file_path}}:{{line_no}}` — {{存量问题描述}}
- `{{file_path}}:{{line_no}}` — {{存量问题描述}}
- `{{file_path}}` — {{存量问题描述、依赖上游契约、兜底现状说明}}
- `{{file_path}}` — {{文档瑕疵类说明，影响等级评估}}

## 已应用的修复汇总

1. `{{file_path}}#{{methodName}}` — {{修复要点简述}}（{{问题编号 Hx/Mx}}）
2. `{{file_path}}#{{methodName}}` — {{修复要点简述}}（{{问题编号 Hx/Mx}}）
3. `{{file_path}}#{{methodName}}` — {{修复要点简述}}（{{问题编号 Hx/Mx}}）
4. `{{file_path}}` — {{修复要点简述}}（{{问题编号 Hx/Mx}}）

## 待人工处理

1. **H1** {{问题简短摘要}} — {{处理要求：上线前 / 发布前需要执行动作}}
2. **M4** {{问题简短摘要}} — {{处理要求：恢复 XX 能力，需要相关方确认}}
3. **M3** {{问题简短摘要}} — {{处理要求：后续评审是否补充防御逻辑}}

## 编译验证

{{本地环境情况说明；变更类型说明：纯 Java 注释 / 常量 /import 调整 / 局部方法修改；语义等价评估；编译风险结论；建议 CI 完整构建做最终验证}}

---

*生成时间：{{yyyy-MM-dd}}*
*生成工具：OCR delegate‑review 模式 + LLM 委托审查*
````





# 五、`Delegation Mode`：委托模式

不想给 `OCR` 配 `LLM`？**让宿主 AI 编码代理用自己的模型执行审查**：

- `OCR` 仍负责**文件选择与规则解析**（确定性工程部分）
- 审查本身由宿主 `Agent` 的模型完成，**无需配置 `OCR API Key`**

```bash
ocr delegate preview                      # 预览审查上下文
ocr delegate rule src/main.go src/handler.go
```

对应 `Claude Code` 中的 `/open-code-review:delegate-review` 命令。适合已经订阅了 `Claude Code`、不想额外付 `API` 费用的场景。



# 六、生态与集成

## 1、编码代理集成

| 平台           | 集成方式                                                     |
| :------------- | :----------------------------------------------------------- |
| `Claude Code`  | 插件市场安装，提供 `/open-code-review:review` 斜杠命令       |
| `Codex`        | 添加 `marketplace` 后安装插件，通过 `@Open Code Review` 调用技能，如 `review my current changes`、`review this branch against main`、`review and fix high-confidence issues` |
| `Cursor`       | 将 `plugins/open-code-review/` 目录复制到 `~/.cursor/plugins/local/open-code-review/`，重启生效 |
| `OpenCode`     | 原生审查工具与斜杠命令                                       |
| `QCA Forward`  | 通过 `OCR` 委托模式使用宿主模型，发布 `open-code-review-delegate` 技能即可 |
| `Skill` 兼容代理 | 安装可移植的 `Agent Skill`                                   |

## 2、`CI/CD` 集成

支持 `GitHub Actions`、`GitLab CI`、`GitFlic CI`、`Gerrit`——把审查挂到流水线里，`PR` 一开自动审。

## 3、其他能力

- **`MCP Server`**：用外部工具扩展审查 `Agent`
- **`Session Viewer`**：在浏览器中浏览、回放审查会话
- **`Telemetry`**：`OpenTelemetry` 集成，可观测性拉满
- **自定义审查规则**：路径过滤、规则定位，按团队规范裁剪



# 七、总结

| 维度           | 通用 `Agent`（`Claude Code` + `Skill`） | `OpenCodeReview`                          |
| :------------- | :-------------------------------------- | :---------------------------------------- |
| 架构           | 纯语言驱动，无硬约束                    | 确定性工程 × `Agent` 混合                  |
| 覆盖率         | 大变更集易偷懒漏审                      | 精确文件选择 + 分治捆绑，稳定全覆盖       |
| 定位准确性     | 行号、文件易漂移                        | 独立定位 + 反思模块，行级精确             |
| `Token` 消耗   | 高                                      | 约 **1/9**                                 |
| `Precision/F1` | 基准                                    | 同模型下显著更高                           |

**适用场景**：

- 日常开发：提交前 `ocr review` 一遍工作区
- 分支合并：`--from main --to feature-branch` 全量审查后再合
- 接手陌生代码：`ocr scan` 全文件审计
- 团队协作：挂 `CI/CD`，`PR` 自动审查
- 已有 `Claude Code` 订阅：用 `/open-code-review:review` 或委托模式零成本接入

一句话：**把"该审哪些文件、规则怎么匹配、评论定位在哪一行"这些不能出错的活儿交给确定性工程，把"这个改动有没有问题"这种需要判断的活儿交给 `LLM`——各司其职，快、准、省。**