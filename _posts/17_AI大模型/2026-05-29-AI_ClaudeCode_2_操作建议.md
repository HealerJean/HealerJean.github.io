---
title: AI_ClaudeCode_2_操作建议
date: 2026-05-29 00:00:00
tags: 
- AI
category: 
- AI
description: AI_ClaudeCode_2_操作建议
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、初识 `Claude` `Code`

## 1、核心范式转移：`Agentic Coding` vs `IDE Plugin`

很多资深开发者初次使用Claude Code时，会把它当成“终端版的Copilot”，这是最大的误区。

**资深开发者心智转换要点**:不要问“这段代码怎么写”，而是描述“我要达成什么业务目标”。Claude Code会自动拆解任务、阅读相关源码、修改多个文件、运行`mvn test`验证结果。**你的角色从“代码**

| 维度           | IDEA AI插件 (`Copilot`/`Assistant`) | `Claude` `Code` (`Agentic` `Coding`)     |
| :------------- | :---------------------------------- | :--------------------------------------- |
| **交互模式**   | 补全/问答式，被动响应               | 自主代理式，主动规划与执行               |
| **上下文感知** | 当前文件/选中代码/有限索引          | 整个代码库 + 文件系统 + `Shell` 环境     |
| **能力边界**   | 仅编辑代码、回答问题                | 读写文件、执行命令、搜索网络、调用 `API` |
| **工作流位置** | 编码辅助环节                        | 贯穿需求分析→编码→测试→提交全流程        |
| **思维模型**   | “帮我写这段代码”                    | “帮我完成这个功能，并确保测试通过”       |



表格中体现的差异，更多是在**底层架构设计、执行权限和上下文深度**上的不同。我们可以从以下几个维度来拆解一下为什么会有这种区别：

### 1）原生内核 vs 插件沙箱

- **IDEA AI 插件（如 Copilot/Assistant）**：它们本质上是运行在 IDEA 里的“插件”。无论背后的模型多强大，它都受限于 JetBrains 的扩展 API 和沙箱机制。插件通常只能拿到当前文件的代码片段或有限的索引信息，很难直接、无限制地调用你电脑的终端（Shell）去执行复杂的系统命令。
- **Claude Code**：它从一开始就被设计为一个**终端原生的 AI 代理（Agent）**5。它不依赖 IDE 的 API，而是直接运行在你的操作系统里，拥有对文件系统、Shell 环境的完整读写和执行权限。这让它能像真实开发者一样，自主地在终端里跑测试、切 Git 分支、甚至调试环境报错7。



### 2）“补全/问答” vs “自主代理工作流”

- **IDEA 插件的思维模型**：大多数插件依然是“你问一句，它答一句”或者“你写一行，它补一行”的辅助模式。即使是现在具备 Agent 能力的插件（比如 IDEA 自带的 Junie），它的自主行动也往往局限在编辑器内部的重构或单文件修改上。
- **Claude Code 的思维模型**：它是一个以任务为中心的**持续行动框架**。你给它一个模糊的目标（比如“帮我完成这个功能并确保测试通过”），它会主动拆解步骤：先读取整个代码库规划方案 -> 创建新文件 -> 编写代码 -> 自动在终端运行测试 -> 发现报错后自我修正 -> 最后自动提交 Git。整个过程是跨会话、全自动闭环的，不需要你在每一步都手动确认。



### 3）上下文的深度与广度

- **IDEA 插件**：虽然 IDEA 本身有强大的 AST（抽象语法树）和项目索引能力，但传给大模型的上下文窗口往往是经过压缩或截断的。插件很难把整个项目的依赖关系图、几百个文件的关联逻辑一次性塞进大模型的“大脑”里。
- **Claude Code**：它的核心优势之一就是**工程级的上下文管理**。它不仅会读取整个代码库，还会通过 `CLAUDE.md` 这样的配置文件，在每一轮对话中都动态注入你的项目架构决策、编码规范和私有规则。这意味着它能真正理解你项目的“潜规则”，而不是仅仅看懂眼前的几行代码。



## 2、安全与权限模型

`Claude` `Code` 拥有**真实的文件系统和Shell访问权限**，这与IDE插件有本质区别。理解其安全模型是资深工程师的责任。   

- **只读操作**（读取文件、搜索代码）：默认允许，无需确认
- **写入操作**（创建/修改文件）：首次询问，可选择“Always allow for this session”
- **危险命令**（`rm -rf`, `git push --force`, 网络请求等）：**每次都必须确认**



# 二、**核心交互 与 `CLAUDE.md` 工程化**。

> `Claude` `Code` 的价值上限完全取决于你“喂”给它的项目上下文质量。本章将帮你把 `CLAUDE.md` 从一份简单的说明文档，升级为**项目级的 `AI` 架构规范契约**，让 `Claude` `Code` 像熟悉你项目的资深同事一样工作。



## 1、 基础命令

### 1）基础会话 & 账号管理（登录、状态、退出、计费）

| 命令      | 说明                                | Java 开发高频场景                          |
| --------- | ----------------------------------- | ------------------------------------------ |
| `/login`  | 登录 Anthropic 账号                 | 新设备初次使用 CLI、切换账号               |
| `/logout` | 登出当前账号                        | 公共机器、切换开发账号                     |
| `/status` | 查看当前会话完整状态                | 排查工具权限、模型加载异常                 |
| `/cost`   | 查看本次会话 token 消耗与预估费用   | 长期编码会话，控制 API 开销                |
| `/model`  | 切换大模型：Opus / Sonnet / Haiku   | 复杂架构分析用 Opus；快速改 bug 用 Haiku   |
| `/fast`   | `Opus` 专属快速输出模式，不降级模型 | 简单 `CRUD` 代码生成、快速读日志           |
| `/clear`  | 清空全部对话历史上下文              | 开始全新模块开发，清除旧代码干扰           |
| `/help`   | 查看全部命令 / 单条命令详细帮助     | 忘记指令用法时快速查阅                     |
| `Esc`     | 强制中断 AI 当前输出 / 执行         | AI 生成错误代码、执行高危 shell 时紧急停止 |
| `/exit`   | 关闭当前 Claude CLI 会话            | 开发完成，退出终端 AI 工具                 |



### 2）项目初始化 & 记忆持久化（项目接入、AI 记忆管理）

| 命令                    | 官方说明                                       | Java 开发高频场景                                      |
| ----------------------- | ---------------------------------------------- | ------------------------------------------------------ |
| `/init`                 | 初始化项目，自动生成`CLAUDE.md`项目配置文件    | 接手新项目、老项目首次接入 Claude Code，统一代码规范   |
| `/memory`               | 查看 / 编辑持久化记忆文件                      | 查看 AI 记住的项目包结构、统一编码规范、自定义工具规则 |
| `/permissions`          | 查看 / 修改文件读写、命令执行工具权限          | 限制高危文件读写、禁止自动执行系统命令，保障项目安全   |
| `/clear`                | 清空全部对话历史，完全重置上下文               | 切换全新业务模块，清除旧代码干扰                       |
| `/compact [自定义说明]` | 自动总结压缩对话，保留核心信息，释放上下文窗口 | 多轮重构后 `token` 告警、切换子功能开发前              |
| `/context`              | 可视化展示当前上下文占用分布                   | 判断是否需要执行`/compact`压缩会话                     |



### 3）代码变更 & 代码评审核心

| 命令               | 说明                                                         | Java 开发高频场景                                     |
| ------------------ | ------------------------------------------------------------ | ----------------------------------------------------- |
| `/diff`            | 读取当前 `Git` 工作区未提交的全部代码变更，加载进 AI 上下文  | 写完业务代码后，先让 `AI` 感知全部改动，再评审 / 重构 |
| `/code-review`     | 评审**本地未提交**的 diff，检查 bug、性能、冗余、规范、复用优化 | 提交 commit 前自检；重构后校验代码质量                |
| `/review <PR链接>` | 拉取 `GitHub` 线上 `PR` 做完整代码审查                       | 团队协作评审同事 `PR`；自己提 PR 前线上预检           |
| `/review`          | 无参数等价本地代码审查，同`/code-review`                     | 日常快速自查本地改动                                  |



### 4）调试、自动化执行（`Java` 单元测试 / 问题诊断）

| 命令     | 说明                                                | Java 开发高频场景                                   |
| -------- | --------------------------------------------------- | --------------------------------------------------- |
| `/run`   | 自定义执行项目脚本 / 终端命令，返回结果交给 AI 分析 | 执行 mvn compile、打包、日志查看、SQL 执行校验      |
| `/debug` | 针对报错堆栈、异常日志定位根因，输出修复代码        | Null 指针、数据库事务异常、Spring Bean 注入失败排查 |



### 5）任务流程控制（批量、循环、规划开发）

| 命令         | 说明                                         | Java 开发高频场景                                   |
| ------------ | -------------------------------------------- | --------------------------------------------------- |
| `/plan`      | 规划模式：先输出整体开发方案，再分步实现代码 | 复杂业务模块（订单 / 支付）、多微服务改造、架构重构 |
| `/loop`      | 循环执行模式，自动重复执行指定任务           | 批量生成 PO/VO 实体、批量修复同类型代码规范问题     |
| `/tasks`     | 查看后台异步任务列表                         | 大型代码重构、批量文件生成时查看任务进度            |
| `/workflows` | 查看正在运行的自动化工作流                   | 自定义 Java 项目流水线、多文件批量处理流程监控      |







## 2、其他指令

### 1）快捷键指令

| 快捷键 / 指令   | 说明                                                         |
| :-------------- | :----------------------------------------------------------- |
| `Enter`         | 发送消息                                                     |
| `Shift + Enter` | 换行（不发送）                                               |
| `Escape`        | 取消当前生成 / 取消输入，温和打断当前生成或工具调用，保留已完成的上下文 |
| `Ctrl + C`      | 中断当前操作                                                 |
| `Ctrl + L`      | 清屏                                                         |
| `Ctrl + B`      | 将当前任务转入后台运行                                       |
| `Shift + Tab`   | 在三种工作模式间循环切换（`Default` 默认确认模式 → `Auto-Accept` 自动接受模式 → `Plan` 计划只读模式） |
| `↑` / `↓`       | 浏览历史输入                                                 |
| `Tab`           | 自动补全文件路径/命令                                        |
| `!` 前缀        | 在输入框直接执行 shell 命令，如 `!ls`                        |



### 2）常用 CLI 命令（终端启动时执行）

- `claude`：在当前项目目录启动 Claude Code12。
- `claude /path/to/project`：指定项目目录启动2。
- `claude --version`：查看当前安装的版本12。
- `claude update`：升级到最新版本2。
- `claude --resume`：在终端直接恢复上一次的会话
  - 会弹出一个交互式的**“会话选择器”**，展示历史会话列表（包含开始时间、首条提示或摘要、消息数），你可以用方向键选择
- ``claude --continue``：完全非交互，无需任何提示或选择，直接无缝衔接



### 3）实用技巧

| 技巧名称        | 具体用法与作用                                               |
| --------------- | ------------------------------------------------------------ |
| 文件引用        | 输入 `@文件名` 可以直接将文件内容注入到对话中                |
| Shell 命令      | 用 `!` 前缀运行 `shell` 命令，输出直接显示在对话里，如 `!git status` |
| 上下文管理      | 对话过长时用 `/compact` 压缩，避免上下文溢出                 |
| 多行输入        | 使用 `Shift+Enter` 换行，适合输入较长的 `prompt`             |
| 管道输入        | 可以从命令行管道传入内容：`cat error.log | claude`           |
| `headless` 模式 | `claude -p "你的问题"` 非交互式运行，适合脚本集成            |





## 3、`CLAUDE.md` 深度定制

> `/init` 生成的默认 `CLAUDE.md` 过于通用。以下是为 **Spring Boot + MyBatis-Plus + Maven** 技术栈定制的高质量模板，请根据你的实际项目调整：

```
# 项目概述
这是一个基于 Spring Boot 3.2 的B端SaaS订单管理系统，采用DDD分层架构。
JDK 17, Maven 3.9+, MySQL 8.0, Redis 7, RocketMQ 5.x

## 架构规范
- 严格遵循 DDD 四层架构：interfaces / application / domain / infrastructure
- Domain层禁止依赖Spring框架注解（除@Entity/@Value外）
- Application层只做编排，不包含业务逻辑
- 所有对外接口统一返回 Result<T> 包装类
- 异常统一通过 @RestControllerAdvice 处理，禁止在Controller中try-catch

## 编码约束
- 使用 Lombok @Data/@Builder，禁止手写getter/setter
- 集合判空使用 CollectionUtils.isEmpty()，禁止 == null
- 日志使用 @Slf4j，禁止 System.out.println
- 日期时间统一使用 LocalDateTime，禁止 Date/Calendar
- MyBatis-Plus 查询优先使用 LambdaQueryWrapper，禁止硬编码字段名字符串

## 构建与测试
- 构建命令: mvn clean package -DskipTests
- 单元测试: mvn test -pl {module-name}
- 测试框架: JUnit 5 + Mockito + AssertJ
- 测试命名: should_{预期行为}_when_{条件}
- 集成测试使用 @SpringBootTest + Testcontainers(MySQL/Redis)

## 安全红线
- 禁止修改 application-prod.yml
- 禁止执行 DROP/DELETE/TRUNCATE SQL
- 数据库变更必须通过 Flyway V{version}__{desc}.sql
- 敏感配置使用 ${vault:xxx} 占位符
```



## 4、`CLAUDE.md` 层级体系

> Claude Code 支持三级记忆，优先级从高到低：

避坑：`CLAUDE.md` 不是越长越好。过长的规范会稀释重点，导致AI选择性忽略。**控制在 `500` 行以内**，只写“AI容易犯错的规则”和“项目特有的非显而易见的约束”。通用的Java最佳实践不需要写，`Claude` 本身已经掌握。

| 位置           | 路径                             | 作用范围                              | 是否提交 git                  |
| :------------- | :------------------------------- | :------------------------------------ | :---------------------------- |
| **项目根目录** | `{项目根目录}/CLAUDE.md`         | 当前项目所有会话                      | ✅ 推荐提交，团队共享          |
| **项目本地**   | `{项目根目录}/.claude/CLAUDE.md` | 当前项目所有会话                      | ❌ 加入 .gitignore，仅个人使用 |
| **子目录**     | `{任意子目录}/CLAUDE.md`         | `Claude` 打开该目录下的文件时自动加载 | ✅ 适合多模块仓库              |
| **全局用户级** | `~/.claude/CLAUDE.md`            | 当前用户的所有项目                    | ❌ 个人配置，不提交            |



## 5、上下文管理艺术：百万级Token的正确用法

> Claude Code 支持高达 **1M Token** 的上下文窗口（约300万汉字/75万行Java代码），但这不意味着你应该无脑塞入所有代码。

| 策略     | 操作方式                                                | 适用场景                           |
| -------- | ------------------------------------------------------- | ---------------------------------- |
| 精准引用 | `@path/to/File.java`                                    | 明确知道需要修改/参考的具体文件    |
| 语义搜索 | "找到所有处理退款逻辑的Service"                         | 不确定具体文件位置，按业务语义定位 |
| Grep模式 | "grep 'OrderStatus.PAID' in src/"                       | 查找特定常量/方法的所有引用点      |
| 主动压缩 | `/compact 保留了订单状态机重构的讨论和已修改的文件列表` | 长会话中途切换子任务               |
| 排除干扰 | 在 `CLAUDE.md` 中标注"忽略target/、*.class、generated/" | 避免构建产物污染上下文             |

**1）`Java` 大型项目实战建议**

- **微服务仓库**：不要在 `monorepo`根目录启动 `Claude Code`，`cd`到具体服务目录再启动，减少无关代码干扰
- **生成代码隔离**：`MyBatis Generator` / `MapStruct `/ `Protobuf` 生成的代码务必在 `CLAUDE.md` 中标记为"不要阅读/不要修改"
- **依赖源码**：当需要理解第三方库行为时
  - 直接说"读取 ~/.m2/repository/xxx/sources.jar 中的 XxxClass"，`Claude Code` 可以解压并阅读源码



## 6、多模态输入：超越纯文本的上下文注入

> Claude Code 不仅接受文本，还支持多种输入形式，这对Java企业级项目尤其有价值：

| 输入类型  | 使用方式                   | Java 实战场景                                     |
| --------- | -------------------------- | ------------------------------------------------- |
| 图片      | 拖拽/粘贴到终端            | 贴入架构图让AI理解模块关系；贴入报错截图自动诊断  |
| PDF/文档  | `@docs/api-spec.pdf`       | 导入接口文档/需求PRD，直接生成对应Controller和DTO |
| URL       | `@https://...`             | 引用在线API文档、GitHub Issue、StackOverflow解答  |
| Shell输出 | 管道符 `| claude`          | `cat error.log | claude "分析这个OOM异常"`        |
| Git Diff  | `git diff | claude review` | 将当前未提交的变更直接送入Review流程              |







# 三、**`Java` 实战场景深度演练**

## 1、遗留代码安全重构

> **痛点**：接手 5 年前的 `Spring MVC` 老项目，`Controller` 里塞满了业务逻辑、`SQL` 拼接和硬编码配置，改一处怕崩三处。

**⚠️ 资深开发者避坑**

> - **永远不要让 `AI` 一次性重构整个文件**。按职责拆分，每次只抽取一个关注点，每步都跑测试
> - **`CLAUDE.md` 中必须写明“重构前必须先生成测试”**，否则AI会倾向于直接改代码
> - 对于超过 `500` 行的 `God Class`，先用 `/compact` 总结分析结果，再分多次会话重构，避免上下文溢出导致遗忘约束



⚠️ `Agentic` 重构工作流（四步闭环）

```
分析 → 生成测试 → 重构 → 验证
```



### 1）`Step 1`: 让 `AI` 先理解再动手

```text
阅读 @src/main/java/com/legacy/order/OrderController.java，不要修改任何代码。
请输出：
1. 该Controller承担了哪些不属于它的职责（对照DDD分层规范）
2. 列出所有外部依赖（Service/DAO/工具类）及其调用方式
3. 识别潜在的副作用（直接写DB、发消息、修改全局状态）
4. 给出重构优先级排序和建议的目标架构
```



### 2）**`Step 2`: 先生成测试，再动代码（关键！）**

```
基于你的分析，为 OrderController 现有的公开方法编写单元测试。
要求：
- 使用 JUnit 5 + Mockito，Mock掉所有外部依赖
- 覆盖正常流程和至少3个异常分支
- 测试命名遵循 should_xxx_when_yyy 规范
- 确保当前代码能100%通过这些测试（作为重构基线）

运行 mvn test -pl order-module 确认全部通过后告诉我结果
```



### 3）`Step 3`: 小步重构，每步验证

```text
现在执行第一步重构：将订单校验逻辑从 Controller 抽取到 OrderApplicationService。
约束：
- 保持原有公开接口签名不变（向后兼容）
- 复用Step2的测试，不新增测试
- 重构完成后立即运行 mvn test -pl order-module
- 如果测试失败，自动回滚并分析原因
```



### 4）`Step 4`: 人工审查 + 提交

```
重构完成且测试通过后，执行 /review 对本次变更进行自查。
然后生成符合Conventional Commits规范的commit message。
```



## 2、单元测试与覆盖率提升

> **痛点**：老项目覆盖率 <20%，手写测试耗时且容易遗漏边界 `Case`；`AI` 生成的测试往往只测 `Happy Path`。



### 1）智能测试生成 Prompt 模板

```
为 @src/main/java/com/example/service/PaymentService.java#refund 方法生成单元测试。
要求：
1. 先阅读该方法及其调用的所有私有方法，理解完整业务逻辑
2. 测试用例必须包含：
   - 正常退款成功
   - 订单不存在 / 订单状态不允许退款
   - 第三方退款网关超时 / 返回错误码
   - 退款金额大于原支付金额（边界值）
   - 并发退款场景（使用Mockito验证幂等性）
3. 使用 AssertJ 的流式断言，禁止 assertEquals
4. Mock外部网关时，同时验证调用参数是否正确
5. 生成后运行测试，如有失败自动修复直到全部通过
```



### 2）提升测试质量的进阶技巧

| 技巧         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| 注入真实Bug  | "假设 refund 方法在金额为0时会抛NPE，请生成能暴露此Bug的测试" |
| 变异测试思维 | "如果我把 if (amount > 0) 改成 if (amount >= 0)，你的测试能检测到吗？如果不能，补充测试" |
| 集成测试生成 | "为退款流程生成 @SpringBootTest 集成测试，使用 Testcontainers 启动MySQL和Redis" |
| 覆盖率驱动   | "运行 jacoco 报告，找出 PaymentService 中未覆盖的分支，针对性补充测试" |



## 3、`Spring` 生态专项调试与优化

### 1）场景A：Bean 生命周期 / 循环依赖诊断

```
应用启动报错 BeanCurrentlyInCreationException。
请：
1. 读取启动日志和相关的 @Configuration / @Component 类
2. 画出涉及的Bean依赖关系图（文本格式）
3. 定位循环依赖的具体链路
4. 给出3种解决方案并推荐最优解（优先构造器注入 > @Lazy > 重构）
不要直接修改代码，先让我确认方案
```



### 2）场景B：MyBatis SQL 性能优化

```
@src/main/resources/mapper/OrderMapper.xml 中的 selectComplexOrders 查询在生产环境平均耗时2s。
请：
1. 分析SQL执行计划（我会提供EXPLAIN输出）
2. 检查是否存在索引失效、N+1查询、不必要的全表扫描
3. 结合 @src/main/java/com/example/entity/Order.java 的字段定义，给出索引创建DDL
4. 重写SQL并预估优化后的复杂度变化
```



### 3）场景C：Spring Boot 版本/配置迁移

```
我们要从 Spring Boot 2.7 升级到 3.2。
请扫描项目中所有受影响的代码和配置：
1. javax.* → jakarta.* 命名空间变更
2. 已废弃的配置项（对照官方Migration Guide）
3. Spring Security 过滤器链配置变更
4. 第三方库兼容性风险（列出pom.xml中可能不兼容的依赖）
输出为Markdown表格，按风险等级排序
```



## 4、`Git `工作流集成

> `Claude Code` 可以接管从编码到提交的整个Git工作流，且质量远超IDE插件。

| 任务          | Prompt / 命令                                                | 效果                                                  |
| ------------- | ------------------------------------------------------------ | ----------------------------------------------------- |
| 智能`Commit`  | `git add -p | claude "按Conventional Commits生成message，中文描述"` | 自动识别变更类型，拆分多个语义化 `commit`             |
| `PR` 描述生成 | `git diff main...HEAD | claude "生成PR描述，包含变更摘要、测试情况、风险点"` | 结构化 `PR` 模板，`Reviewer` 友好                     |
| `Code Review` | `/review` 或 `git diff | claude review`                      | 基于 `CLAUDE.md` 规范审查，指出架构违规而非仅语法问题 |
| `Changelog`   | `claude "基于最近10个commit生成CHANGELOG.md条目"`            | 自动分类 `feat/fix/breaking change`                   |
| 冲突解决      | "帮我解决当前`merge conflict`，保留我们的业务逻辑但采用他们的重构结构" | 语义级冲突合并，非逐行选择                            |





# 四、**`Skills` 系统与 `Auto Mode`** 

## 1、`Skills` 系统：封装你的 `Java` 工程资产

> `Skills` 本质上是将**高频、复杂、多步骤的工作流**固化为可复用的指令。它不是简单的 `Prompt`模板，而是包含上下文引用、工具调用链和安全约束的“AI函数”。

**1）Skill设计原则**

- **单一职责**：一个 `Skill` 只做一件事，通过组合实现复杂流程
- **防御性设计**：在 `Skill` 中写明“如果找不到XX文件，提示用户而非猜测”
- **版本管理**：Skills随代码库` Git`提交，团队成员clone即可用
- **个人vs 团队**：通用 `Skill` 放项目`.claude/skills/`，个人调试Skill放`~/.claude/skills/`



### 1）为什么资深开发者需要 `Skills` ？

| 痛点         | 无Skills                             | 有Skills                                                  |
| ------------ | ------------------------------------ | --------------------------------------------------------- |
| 新人接手项目 | 反复解释内部框架用法、部署流程       | `/skill:onboard` 一键生成上手指南+环境配置脚本            |
| 发版前检查   | 手动跑SQL审查、依赖扫描、配置比对    | `/skill:release-check` 自动执行全套检查并输出报告         |
| 日志排查     | 每次都要贴日志+描述症状+指定分析维度 | `/skill:log-diagnose error.log` 自动关联代码+给出修复建议 |
| 规范落地     | `CLAUDE.md` 写了但 `AI` 偶尔遗忘     | `Skill` 内置强制校验步骤，不合规不输出结果                |



### 2）创建你的第一个 `Java` `Skill`

> 在项目根目录创建 `.claude/skills/` 目录，以 `Markdown` 文件定义 `Skill`：

```java
<!-- .claude/skills/mybatis-review.md -->
# MyBatis SQL Review Skill

## 触发条件
当用户输入 `/mybatis-review` 或提到"审查SQL"/"Mapper检查"时激活

## 执行步骤
1. 读取指定的Mapper XML文件或@Select注解方法
2. 对照以下规则逐条检查：
   - [ ] 禁止SELECT *，必须显式列出字段
   - [ ] WHERE条件字段必须有索引（结合schema.sql验证）
   - [ ] 动态SQL的<if>标签必须包含非空判断
   - [ ] 批量操作必须使用<foreach>且limit≤1000
   - [ ] 禁止在SQL中拼接${}，必须用#{}防注入
3. 对每条违规给出：违规位置、风险等级、修复后的SQL片段
4. 如果涉及分页查询，额外检查是否缺少COUNT优化

## 输出格式
Markdown表格：| 文件:行号 | 规则 | 风险 | 修复建议 |
```



### 3）`Java` 项目高价值 `Skill` 清单

| Skill名称       | 用途                                                 | 核心依赖                                |
| --------------- | ---------------------------------------------------- | --------------------------------------- |
| `/api-gen`      | 根据OpenAPI/Swagger文档生成Controller+DTO+Validation | @docs/api-spec.yaml + CLAUDE.md编码规范 |
| `/db-migrate`   | 生成Flyway迁移脚本+回滚脚本+测试数据                 | schema.sql + Flyway命名规范             |
| `/perf-profile` | 分析JFR/Async Profiler火焰图，定位热点方法           | JFR文件 + 项目源码                      |
| `/incident-rca` | 根因分析：关联告警、日志、最近Git变更                | Prometheus/Grafana截图 + git log        |
| `/tech-debt`    | 扫描SonarQube报告，按修复ROI排序债务清单             | Sonar JSON导出 + 业务优先级矩阵         |



## 2、`Auto` `Mode`：全自主 `Agentic` 执行

> `Auto Mode` 让 `Claude Code` **跳过中间确认，自主完成多步骤任务**。这是处理跨文件重构、批量修改、端到端测试等复杂场景的终极武器。

### 1）启用方式与安全边界

```
# 会话内临时启用
> /auto on

# 启动时直接启用（适合CI/CD或可信环境）
claude --auto "将所有javax.servlet替换为jakarta.servlet并运行测试"
```



### 2）**安全红线**

> `Auto` `Mode` **不会跳过 `CLAUDE.md` 中定义的安全约束**。但务必确保：

- `CLAUDE.md` 中的安全规则足够明确（见1.4节）
- 首次对某类任务启用 `Auto` 前，先在普通模式下验证过工作流
- 生产环境/共享服务器**永远不要**开启 `Auto Mode`



### 3）最佳实践场景

| 场景         | 为什么需要Auto                             | Prompt示例                                                   |
| ------------ | ------------------------------------------ | ------------------------------------------------------------ |
| 批量API改造  | 涉及20+文件，每步确认会打断思路            | 将所有REST接口返回值从Map改为Result包装，自动生成对应DTO，运行全量测试 |
| 依赖升级验证 | 升级→编译→测试→修错循环可能重复10+次       | 升级Spring Boot到3.2.5，自动修复所有编译错误和测试失败，直到mvn verify通过 |
| 文档同步更新 | 代码改完需同步更新README/API文档/CHANGELOG | 基于本次git diff，更新README的使用示例、API文档的接口说明、CHANGELOG条目 |
| E2E测试修复  | 测试失败→读日志→改代码→重跑，纯机械循环    | /test 如果集成测试失败，自动分析日志修复代码并重试，最多重试5次" |



### 4）`Auto` `Mode` + `Skills` 组合技

```
/auto on
/release-check
```

这条命令会让 `Claude Code` **全自动**执行你定义的发布检查 `Skill`，无需任何人工干预，最终输出一份完整的发布就绪报告。你可以在`IDEA Terminal` 中启动后去喝杯咖啡，回来直接看结果。



## 3、团队协作：从个人工具到研发基础设施

| 层级     | 实践                                  | 收益                      |
| -------- | ------------------------------------- | ------------------------- |
| L1: 个人 | 个人CLAUDE.md + 私有Skills            | 提升个人效率              |
| L2: 小组 | 项目CLAUDE.md + 共享Skills + Git提交  | 统一组内AI协作标准        |
| L3: 部门 | CI 集成 + 公共Skill仓库 + 效果度量    | AI 能力平台化，可量化 ROI |
| L4: 公司 | Bedrock/Vertex企业版 + SSO + 审计日志 | 合规、安全、规模化        |













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



