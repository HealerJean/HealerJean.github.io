---
title: AI_ClaudeCode_源码_1
date: 2026-05-29 00:00:00
tags: 
- AI
category: 
- AI
description: AI_ClaudeCode_3_源码阅读
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、为什么要阅读这份源码

## 1、这份代码适合拿来做什么

最适合的用途有三类：

1. 学习 `Claude` `Code` 的整体架构
2. 研究 `AI` 编程 `Agent` 的工程实现方式
3. 借鉴它的模块划分、工具协议、权限系统和运行时设计

也就是说，我们更关注的是：

- 它为什么强
- 它怎么组织系统
- 它的能力是如何拼装出来的

而不是把它当成一个普通开源项目去直接复刻。



## 2、这份代码不适合拿来做什么

- 指望它百分之百完整可运行
- 指望它等价于官方最新线上版本
- 指望它包含所有私有服务和后端依赖
- 指望它天然适合作为生产项目二次发布

因为它本质上仍然是一份还原出来的代码快照，不是官方发布的完整开发仓库。



## 3、研究这份源码，正确姿势是什么

最推荐的姿势不是“逐文件扫过去”，而是：

1. 先看主干骨架
2. 再看核心循环
3. 再看工具、上下文、权限
4. 最后看 `MCP`、`LSP`、插件、远程、多 Agent





## 4、有哪些已经分析的教程

| 来源                         | 地址                                            |
| ---------------------------- | ----------------------------------------------- |
| 学猿                         | https://www.xuanyuancode.com/learn-claude-code  |
| claude-code-best/claude-code | https://github.com/claude-code-best/claude-code |



# 二、总体架构

## 1、架构全景

![image-20260630112839893](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20260630112839893.png)



## 2、目录结构

```
src/
├── main.tsx                    # 入口点，CLI 引导（803 KB）
├── query.ts                    # 核心 Agent 循环（68 KB）
├── QueryEngine.ts              # LLM 查询引擎（46 KB）
├── Tool.ts                     # Tool 基础接口（29 KB）
├── tools.ts                    # Tool 注册表（25 KB）
├── Task.ts                     # 任务类型定义
├── commands.ts                 # 命令注册
│
├── tools/                      # 43 个工具目录
│   ├── BashTool/              # Shell 命令执行
│   ├── FileReadTool/          # 文件读取
│   ├── FileWriteTool/         # 文件创建
│   ├── FileEditTool/          # 部分文件修改
│   ├── GlobTool/              # 文件模式匹配
│   ├── GrepTool/              # ripgrep 内容搜索
│   ├── AgentTool/             # 子 Agent 生成
│   ├── SkillTool/             # Skill 执行
│   ├── MCPTool/               # MCP 服务器调用
│   ├── WebFetchTool/          # URL 内容抓取
│   ├── WebSearchTool/         # 网页搜索
│   └── https://wanlanglin.github.io/-awesome-cc-harness/.                    # 更多工具
│
├── commands/                   # ~101 个命令目录
│   ├── commit/                # Git 提交
│   ├── review/                # 代码审查
│   ├── mcp/                   # MCP 管理
│   ├── skills/                # Skill 管理
│   └── https://wanlanglin.github.io/-awesome-cc-harness/.
│
├── components/                 # 144+ React/Ink 终端组件
├── hooks/                      # 80+ 自定义 React Hooks
├── services/                   # 22 个服务子目录
│   ├── api/                   # Anthropic API 客户端
│   ├── mcp/                   # MCP 协议连接
│   ├── oauth/                 # OAuth 认证
│   ├── lsp/                   # 语言服务器协议
│   ├── compact/               # 对话压缩
│   ├── plugins/               # 插件加载
│   └── https://wanlanglin.github.io/-awesome-cc-harness/.
│
├── utils/                      # 33+ 子目录，100+ 文件
│   ├── permissions/           # 权限逻辑
│   ├── hooks.ts               # Hook 执行引擎
│   ├── hooks/                 # Hook 配置管理
│   ├── sandbox/               # 沙盒适配器
│   ├── settings/              # 设置管理
│   ├── bash/                  # Shell 工具
│   ├── memdir/                # 持久记忆目录
│   └── https://wanlanglin.github.io/-awesome-cc-harness/.
│
├── state/                      # 应用状态管理
├── entrypoints/                # CLI/MCP/SDK 入口
├── bridge/                     # IDE 双向通信
├── coordinator/                # 多 Agent 编排
├── skills/                     # Skill 系统
├── plugins/                    # 插件系统
├── memdir/                     # 记忆目录系统
├── schemas/                    # Zod 验证 Schema
├── types/                      # 类型定义
└── constants/                  # 应用常量

```

## 2、数据流图

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Claude Code 数据流全景                             │
│                                                                      │
│  用户输入 ──→ UserPromptSubmit Hook ──→ Slash Command 解析           │
│     │                                                                │
│     v                                                                │
│  QueryEngine.submitMessage()                                         │
│     │                                                                │
│     ├─→ 系统提示构建: base + tools + CLAUDE.md + MCP + memory       │
│     ├─→ 消息规范化: normalizeMessagesForAPI()                        │
│     │   ├─ 重排序 attachment 消息                                    │
│     │   ├─ 合并连续 user/assistant 消息                              │
│     │   ├─ 剥离 PDF/图片错误的重复内容                                │
│     │   ├─ 规范化工具名称（别名→正式名）                              │
│     │   └─ 工具搜索引用块处理                                        │
│     │                                                                │
│     v                                                                │
│  queryLoop() [while(true)]                                           │
│     │                                                                │
│     ├─→ 压缩管道: snip → micro → collapse → auto                    │
│     ├─→ API 调用: deps.sample() [流式]                               │
│     │                                                                │
│     ├─→ 工具执行: StreamingToolExecutor (并发) / runTools (顺序)     │
│     │   │                                                            │
│     │   ├─→ 工具分区: partitionToolCalls()                           │
│     │   │   ├─ isConcurrencySafe=true → 并发执行                     │
│     │   │   └─ isConcurrencySafe=false → 串行执行                    │
│     │   │                                                            │
│     │   └─→ 每个工具:                                                │
│     │       ├─ Zod schema 验证                                       │
│     │       ├─ tool.validateInput()                                  │
│     │       ├─ PreToolUse Hook                                       │
│     │       ├─ 权限检查 (rules → mode → classifier)                 │
│     │       ├─ Sandbox 包装 (BashTool)                               │
│     │       ├─ tool.call() [实际执行]                                │
│     │       └─ PostToolUse Hook                                      │
│     │                                                                │
│     ├─→ 错误恢复: 7 个 continue 站点                                │
│     └─→ Stop Hook → 终止或继续                                      │
│                                                                      │
│  终止 → SessionEnd Hook → 转录保存 → 退出                           │
└─────────────────────────────────────────────────────────────────────┘

```























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



