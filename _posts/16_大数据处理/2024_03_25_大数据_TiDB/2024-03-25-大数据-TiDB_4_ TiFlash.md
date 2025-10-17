---
title: 大数据-TiDB_4_TiFlash
date: 2022-03-25 00:00:00
tags: 
- BigData
- TiDB
category: 
- BigData
- TiDB
description: 大数据-TiDB_4_HTAP
---

# **前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          





# 一、`TiFlash` 简介

> **TiFlash** 是 **TiDB** 实现 **HTAP（Hybrid Transactional/Analytical Processing）** 架构的关键组件，作为 **TiKV 的列存扩展**，在保障强一致性的同时，为实时分析负载提供高性能的列式存储支持。



![image-20250807155853449](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20250807155853449.png)



## 1、核心架构与原理

| 特性                  | 说明                                                |
| --------------------- | --------------------------------------------------- |
| **强一致性读取**      | 提供 SI 隔离级别，与 TiKV 保持数据一致性            |
| **高可用 & 实时同步** | 基于 Raft Learner，异步但低延迟复制                 |
| **资源隔离**          | 支持独立部署，避免 OLAP 影响 OLTP                   |
| **按表控制副本**      | 灵活配置，节省存储成本                              |
| **向量化执行**        | 利用现代 CPU 指令集加速分析查询                     |
| **列式存储**：        | 适合统计分析类查询（如 `AVG`、`SUM`、`GROUP BY`）。 |
| **`MPP` 架构**：      | 支持大规模并行计算，提升查询性能。                  |





### 1）**数据复制机制：`Raft Learner`**

- `TiFlash` 副本以 `Raft Learner` 身份加入集群：
  - 不参与` Raft` 多数派投票，**不影响 `TiKV` 写入性能**。
  - 异步从 `Raft Leader`（`TiKV`）拉取日志，实现数据同步。
- 数据同步过程对 `TiKV` 写入路径无阻塞，**低开销、高实时性**。

- `TiFlash` **不支持直接写入**。
  - 所有数据必须先写入 `TiKV`，再通过 `Learner` 机制异步复制到 TiFlash。



### 2）**`Region` 管理一致性**

- `TiFlash` 的 `Region` 副本与 TiKV 完全对应：
  - 同步进行 **`Region` 分裂、合并、调度**。
  - 由 `PD`（`Placement Driver`）统一调度管理。





### 3） **一致性读取：`SI` 隔离级别**

尽管数据异步复制，`TiFlash` 可提供 `Snapshot Isolation` (`SI`) 一致性，其实就是可重复读：

- **不保证“最新”，但保证“一致”，可能读到落后的数据**，用户可读取到与 `TiKV` 相同版本的数据，保证跨引擎一致性。
- 通过 **`Raft` 校对索引 + `MVCC`** 机制，确保读取的是某个全局一致的快照。
- **`TiFlash` 只能提供“它已经确认同步完成”的那个快照**。这个快照一定是在 `TiKV` 和 `TiFlash` 上都**已经提交并可读。**





## 2、核心特性

| 特性     | 简述                                   |
| -------- | -------------------------------------- |
| 异步复制 | 基于 Raft Learner 的低开销数据同步     |
| 一致性   | 提供与 `TiKV` 相同的快照隔离（SI）级别 |
| 智能选择 | 查询优化器自动选择最优数据源           |
| 计算加速 |                                        |







### 1）异步复制

| 优势                   | 说明                                                         |
| ---------------------- | ------------------------------------------------------------ |
| **不影响 `OLTP` 性能** | 即使 `TiFlash` 宕机或网络延迟高，TiKV 写入仍可正常进行       |
| **高可用恢复能力**     | 只要 `TiKV` 数据不丢，TiFlash 可随时重建副本                 |
| **自动负载均衡**       | 无需额外复制管道，直接从多个 `TiKV` 节点并行拉取数据         |
| **多对多传输**         | 多个 `TiFlash` 节点可同时从多个 `TiKV` 节点拉取数据，提升吞吐 |

### 2）一致性

| 问题                     | 回答                                               |
| ------------------------ | -------------------------------------------------- |
| **跨引擎一致性**         | 它不读“最新”，只读“已确认一致的历史快照”           |
| “相同版本”是什么意思？   | 指的是 `TiKV` 和 `TiFlash` 都已完成的某个 TSO 版本 |
| 会不会读到不一致的数据？ | 不会，系统会校对进度，确保读取安全                 |
| 这个延迟有多大？         | 通常几毫秒到几十毫秒，取决于网络和负载             |



### 3）智能选择

> TiDB 的优化器可以**自动决定**从 TiKV 还是 TiFlash 读取数据，甚至在同一查询中混合使用。



#### a、决策依据

- **统计信息**：表大小、列基数、数据分布
- **查询类型**：是否涉及大量扫描、聚合、过滤
- **下推能力**：哪些算子能被 `TiFlash` 支持下推
- **成本估算**：预估 I/O、CPU、网络开销



#### b、典型场景：

| 查询类型            | 优选引擎            | 原因                                 |
| ------------------- | ------------------- | ------------------------------------ |
| 点查、小范围扫描    | `TiKV`              | 行存 + 低延迟                        |
| 大表扫描、聚合分析  | `TiFlash`           | 列存 + 向量化                        |
| JOIN（小表 + 大表） | TiKV + TiFlash 混合 | 小表在 TiKV，大表在 TiFlash 下推计算 |





### 4）计算加速：

#### a、列存读取效率提升

-  **列式存储**：只读取查询涉及的列，减少 `I/O`
-  **高效压缩**：相同数据类型连续存储，压缩率高
-  **向量化执行引擎**：基于 ClickHouse 技术，批量处理数据，提升 CPU 利用率



#### b、计算下推

- **谓词下推**：`WHERE age > 30`
- **聚合下推**：`COUNT(*)`, `SUM()`
- **`Join` 下推**：支持 `Broadcast Join`、`Shuffled Join`
- **`TopN` / `Limit` 下推**





## 2、问题汇总

### 1）`TiFlash` 如何同步数据

`TiFlash` 副本是通过 `Raft` `Learner` 角色加入到 `Raft` `Group` 中的。

**工作流程如下：**

- **同步**
  1. **`TiKV` `Leader` 接收写请求**
  2. 写入 `Raft` `Log`
  3. `Log` 被复制到其他 `TiKV` `Follower`
  4. 多数 `TiKV` 副本确认后，事务提交

- **异步**
  1. **`Log` 同时也复制到 `TiFlash Learner`**
  2. **`TiFlash` 异步应用 `Log`，更新列存数据**

因此：

- 数据最终会在 `TiFlash` 上出现
- 但有轻微延迟（通常几十毫秒），所以说是“**近实时**”

| 对比项                   | TiKV 副本                          | TiFlash 副本                       |
| ------------------------ | ---------------------------------- | ---------------------------------- |
| 角色类型                 | `Raft Follower`                    | `Raft Learner`                     |
| 是否参与投票             | 参与 `Raft` 投票（选主、提交日志） | 不参与投票                         |
| 是否必须同步才能提交写入 | 是，多数副本确认后才提交           | 否，异步复制不影响提交             |
| 数据一致性保障           | 强一致性（实时）                   | 最终一致性（默认），可配置为强一致 |
| 同步延迟                 | 极低（通常 <10ms）                 | 稍高（通常几十 ms）                |
| 是否持久化日志           | 是                                 | 是                                 |
| 是否构建 KV 数据视图     | 是                                 | 否（构建列存视图）                 |



### 2）为什么 `TiFlash` 的 `Region` 要和 `TiKV` 一一对应

**问题1：为什么 `TiFlash` 的 `Region` 要和 `TiKV` 一一对应？**

- **分裂不同步**：TiKV 把 Region A 分裂成 A1 和 A2，但 TiFlash 还是老的 A。查询 A1 时，TiFlash 可能返回错误或过期数据。
- **合并不同步**：TiKV 合并了两个 Region，但 TiFlash 还保留旧结构，导致数据重复或缺失。



**问题2：那怎么实现`Region` 和 `TiKV` 一一对应？**

- 当 `TiKV` 的 `Region` 发生 **分裂或合并** 时，会通知 `PD`。
- `PD` 同时向 `TiFlash` 发出指令：“你也按同样的 `Key` 范围进行分裂/合并”。



**问题2：这样的好处还有啥呢**

- `PD` 可以统一管理所有副本（无论是行存还是列存）。
- `SQL` 层可以透明地访问数据，无需关心底层是 `TiKV` 还是 `TiFlash`
- 支持 **智能路由**：根据负载、延迟、副本位置选择最优读取源。





### 3）`TiFlash` 一致性读取流程

1. **`Raft Log` 中的“时间戳” —— `Applied Index`**
   - `TiKV` 用 `Raft` 日志来同步数据，每条日志有个编号，比如 `Log Index = 500`。
   - 当` TiKV` 把第 `500` 条日志应用到数据库，就叫 `Applied Index = 500`。
   - `TiFlash` 作为 `Learner`，也会回放这些日志，它也有自己的 `Applied Index`。
     - 这个 `Applied Index` 实际上是一个**逻辑时钟**，可以理解为“当前已提交数据的版本”。

2. **`MVCC`：多版本并发控制**

   - `TiDB` 使用 `MVCC` 存储数据：每条数据有多个版本，每个版本带有一个 `TS`（`Timestamp`，来自 `TSO` 时间戳）。

   - 查询时会指定一个 `Read TS`（读取时间戳），只读取 ≤ 该时间戳的最新版本。

3. **关键：如何为 `TiFlash` 查询选择正确的 `Read TS`？**
   1. `TiDB` 想查数据，先问时间：你想看哪个快照”
      - PD 返回一个全局唯一的时间戳，比如 `TSO = 100`。
      - 这个时间戳代表“此刻整个集群的逻辑时间”。
   2. `TiDB` 想从 `TiFlash` 读，但先问它“跟上进度了吗
      - 请提供一个能对应到 `Raft` 日志位置的、最新的、一致的快照
      - 假设 `TiFlash` 返回：`Applied Index = 490`
      - 说明 `TiFlash` 已经处理完前 490 条日志，第 491 条及以后还没同步完
   3. 把“日志位置”转换成“时间戳
      - **`PD` 或 `TiDB`**：现在我知道 `TiFlash` 同步到了 `Applied Index = 490`，那这个位置对应的时间戳是多少？
      - `PD` 维护了一个映射表：`Log Index → TSO` 比如：
        - `Log Index 490` 对应 `TSO = 98`
        - `Log Index 500` 对应 `TSO = 100`
      - 这个 `TSO = 98` 就是 **`TiFlash` 能保证一致的最新快照时间**。
   4. 告诉 `TiFlash`：“你就按这个时间读！”
      - `TiFlash` 使用 `TSO = 98` 作为读取时间戳，从自己的列存中通过 MVCC 找出这个时间点的数据版本。
      - 结果：你读到的是“在 `TSO = 98` 这个时刻，整个数据库的一致快照”。
      - 最终，`TiFlash` 使用这个 `TS` 作为 `Read TS`，通过 `MVCC` 读取对应版本的数据。



### 4）如何防止“使用严重落后的副本”

答案：**Placement Rules + Follower Read 配置**

- 如果 `TiFlash` 的 `Applied` `Index` 落后超过 30 秒

- 则该副本 **不再参与读取**

- 查询将自动降级到 TiKV 或报错

```
-- 设置 TiFlash 副本最大允许延迟为 30 秒
ALTER TABLE t PLACEMENT POLICY =
  FOLLOWERS=2 FOLLOWER_CONSTRAINTS='["+zone=cn1"]' 
  LEARNERS=1 LEARNER_CONSTRAINTS='["+zone=cn2"]' 
  MAX_LOG_LAG=30; 
```



### 6）优化器如何判断是 走 `TiFlash`

> `TiDB` 优化器是一个基于**成本的优化器（CBO, Cost-Based Optimizer）**，它通过以下因素决定是否使用 TiFlash：

| 判断维度               | 具体场景                                    | 倾向引擎                               | 原因说明                                     |
| ---------------------- | ------------------------------------------- | -------------------------------------- | -------------------------------------------- |
| **表的大小**           | 小表（< 10MB）                              | `TiKV`                                 | 小表加载快，随机读取效率高，适合行存         |
|                        | 大表（> 1GB）                               | `TiFlash`                              | 大数据量分析查询，列存压缩和扫描效率优势明显 |
| **访问列数 vs 总列数** | 只读少数几列（如 `SELECT a, b`，总列数 50） | `TiFlash                               | 列存只需读取相关列，I/O 和内存更优           |
|                        | 读全表（`SELECT *`）                        | `TiKV`（可能）                         | 行存一次性读取整行，避免列存多次合并         |
| **查询类型**           | 聚合（`GROUP BY`, `SUM`, `COUNT`）          | `TiFlash`                              | 分析型操作，向量化执行更高效                 |
|                        | 排序（`ORDER BY`）、窗口函数（`OVER()`）    | `TiFlash`                              | 大数据集排序和分析函数适合列式处理           |
|                        | 点查（`WHERE id = ?`）                      | `TiKV`                                 | 事务型查询，索引定位快，低延迟               |
| **索引使用情况**       | 可用索引快速定位数据                        | `TiKV`                                 | 行存 + 索引 = 快速点查或小范围扫描           |
|                        | 无法使用索引，需全表扫描                    | ` TiFlash`                             | 避免大量随机读，列存顺序扫描更高效           |
| **手动控制（Hint）**   | 强制走 TiFlash                              | `/*+ read_from_storage(tiflash[t]) */` | 适用于希望强制使用列存分析                   |
|                        | 强制走 TiKV                                 | `/*+ read_from_storage(tikv[t]) */`    | 适用于点查或规避 TiFlash 性能问题            |



### 9）使用建议：

#### a、使用场景

| 场景            | 是否推荐使用 TiFlash | 说明                                   |
| --------------- | -------------------- | -------------------------------------- |
| `OLAP` 查询较多 | 强烈推荐             | 显著提升查询性能                       |
| `OLTP` 写入频繁 | 推荐                 | `TiFlash` 同步为异步，不影响写入性能   |
| 小表简单查询    | 可不使用             | `TiKV` 已足够快                        |
| 需要强一致分析  | 推荐                 | `TiFlash` 支持 `Raft` 同步，数据强一致 |
| 超大数据量      | 推荐                 | 使用 `TiFlash MPP` 模式支持分布式计算  |



#### b、表达意图

| 风险                       | 解决方案                                       |
| -------------------------- | ---------------------------------------------- |
| **需要最新数据的大范围读** | 显式使用 `/*+ read_from_storage(tikv[t]) */`   |
| **事务中读大表**           | 加 `FOR UPDATE` 或使用 `Hint` 强制走 `TiKV`    |
| **报表类查询允许延迟**     | 不干预，让优化器自动选择                       |
| **混合负载担心干扰**       | 使用 `MAX_LOG_LAG` 控制 `TiFlash` 延迟容忍时间 |



# 一、构建 `TiFlash` 副本

**`TiFlash` 部署完成后并不会自动同步数据，而需要手动指定需要同步的表。**



## 1、按表构建 `TiFlash` 副本

> TiFlash 接入 TiKV 集群后，默认不会开始同步数据。可通过 MySQL 客户端向 TiDB 发送 DDL 命令来为特定的表建立 TiFlash 副本：

### 1）开启 `TiFlash` 同步副本的语法

```sql
ALTER TABLE {table_name} SET TIFLASH REPLICA {count};
```

| 参数           | 描述                                            |
| -------------- | ----------------------------------------------- |
| `{table_name}` | 需要同步的表名，例如 `bookshop.ratings`         |
| `{count}`      | 同步副本数，通常设置为 1；设为 `0` 表示删除副本 |

-  `count = 0`：表示删除所有 `TiFlash` 副本（即取消同步）
-  `count = 1`：表示设置一个 `TiFlash` 副本
-  `count = 2`：表示设置两个 `TiFlash` 副本（适用于高可用场景）



### 2）查看表同步进度

```sql
SELECT * 
FROM information_schema.tiflash_replica 
WHERE TABLE_SCHEMA = '<db_name>' 
  AND TABLE_NAME = '<table_name>';
```



| 字段名              | 含义             | 说明                                                         |
| ------------------- | ---------------- | ------------------------------------------------------------ |
| **TABLE_SCHEMA**    | 数据库名         | 如 `test`                                                    |
| **TABLE_NAME**      | 表名             | 如 `users`                                                   |
| **TABLE_ID**        | 内部表 ID        | TiDB 内部标识                                                |
| **REPLICA_COUNT**   | 配置的副本数     | 你通过 `ALTER TABLE ... SET TIFLASH REPLICA` 设置的数量，如 `1` |
| **LOCATION_LABELS** | 副本位置标签     | 如 `["zone","rack"]`，用于副本调度                           |
| **AVAILABLE**       | **副本是否可用** | `1` = 可用（至少一个副本同步完成）<br>`0` = 不可用（还在同步中或失败） |
| **PROGRESS**        | **同步进度**     | 范围 `0.0 ~ 1.0`<br>`1.0` 表示所有副本都已同步完成           |



- `AVAILABLE = 1`
  -  **至少有一个 `TiFlash` 副本已经完成同步，可以用于查询**
  - 一旦变为 `1`，就不会再变回 `0`（除非你删了副本重建）
  - 注意：**即使 `AVAILABLE=1`，副本仍可能落后（如网络抖动），只是“曾经同步完成过”**

- `PROGRESS = 0.8`

  - 表示：**当前同步进度是 80%**
  - 例如：总共有 `100` 个 Region 需要同步，已有 80 个完成
  -  这个值会逐渐趋近 `1.0`

  -  `PROGRESS` 是“副本整体进度”，不是“实时延迟”



## 2、使用 `TiDB` 读取 TiFlash

> TiDB 提供三种读取 TiFlash 副本的方式。如果添加了 TiFlash 副本，而没有做任何 engine 的配置，则默认使用 CBO 方式。

- **优先级顺序**：Engine 隔离 > 手工 Hint > 智能选择（CBO）。即，Engine 隔离首先限制了可选的副本类型，然后手工 Hint 可以在此基础上进一步指定具体表的副本，最后 CBO 根据成本选择具体的执行计划。

| 场景                              | 配置                   | 说明                               |
| --------------------------------- | ---------------------- | ---------------------------------- |
| **默认行为（智能选择）**          | `"tikv,tiflash"`       | 优化器自动决定走 TiKV 还是 TiFlash |
| **强制只走 `TiKV`（强一致事务）** | `"tikv"`               | 确保读最新数据，避免 TiFlash 延迟  |
| **强制只走 `TiFlash`（纯分析）**  | `"tiflash"`            | 确保分析查询不干扰 OLTP            |
| **只读系统表**                    | `"tidb"`               | 如 `INFORMATION_SCHEMA`            |
| **开发调试**                      | 动态切换，观察性能差异 | `SET SESSION ... = "tiflash"`      |



### 1）智能选择

> 对于创建了 TiFlash 副本的表，TiDB 优化器会自动根据代价估算选择是否使用 TiFlash 副本。具体有没有选择 TiFlash 副本，可以通过 `desc` 或 `explain analyze` 语句查看，

#### a、如何查看是否使用了 TiFlash？

方法 1：`DESC`（或 `EXPLAIN`）—— 看执行计划

```sql
DESC SELECT count(*) FROM test.t;
```

关键看 `task` 列：

| task 字段      | 含义                |
| -------------- | ------------------- |
| `cop[tikv]`    | 下推到 TiKV 执行    |
| `cop[tiflash]` | 下推到 TiFlash 执行 |
| `root`         | 在 TiDB 层执行      |

```sql
+--------------------------+---------+--------------+---------------+--------------------------------+
| id                       | estRows | task         | access object | operator info                  |
+--------------------------+---------+--------------+---------------+--------------------------------+
| StreamAgg_9              | 1.00    | root         |               | ...                            |
| └─TableReader_17         | 1.00    | root         |               | data:TableFullScan_16          |
|   └─TableFullScan_16     | 1.00    | cop[tiflash] | table:t       | ...                            |
+--------------------------+---------+--------------+---------------+--------------------------------+
```

方法 2：`EXPLAIN ANALYZE` —— 看实际执行 + 性能数据

```sql
EXPLAIN ANALYZE SELECT count(*) FROM test.t;
```

输出中不仅有计划，还有**真实执行时间、RPC 调用、扫描数据量**等。

- `tiflash_task`：`TiFlash` 任务耗时
- `tiflash_scan`：扫描了多少列、数据量等
- `rpc time`：`TiDB` 到 `TiFlash` 的网络耗时



#### b、如何“推动”优化器选择 TiFlash？

**1、更新统计信息**

```
ANALYZE TABLE test.t;
```

- 让优化器知道表有多大、列的分布
- 是“智能选择”正确的前提

2. **使用 Hint 强制指定**

```
SELECT /*+ read_from_storage(tiflash[t]) */ count(*) FROM test.t;
```

- 绕过优化器判断，强制走 TiFlash
- 适合“我知道这个查询适合分析”的场景

3. **检查表是否真有副本**

```
SELECT * FROM information_schema.tiflash_replica 
WHERE TABLE_SCHEMA='test' AND TABLE_NAME='t';
```

- 确保 `AVAILABLE=1`，`PROGRESS=1.0`



#### c、单副本 `TiFlash` 节点宕机怎么办？

- 优化器看到“有 TiFlash 副本”，尝试走 TiFlash
- 但连接失败 → 重试 → 再失败 → 查询超时或延迟很高

**正确应对策略**

| 场景               | 建议                                       |
| ------------------ | ------------------------------------------ |
| **生产环境关键表** | 建议设置 `TIFLASH REPLICA 2`，避免单点故障 |
| **允许降级**       | 使用 `Hint` 强制走 TiKV：                  |

### 2）`Engine` 隔离

> **Engine 隔离** 是一种 **强制控制查询读取路径** 的机制，告诉 TiDB：“我只允许你从这些存储引擎读数据：TiKV、TiFlash，或两者都行。它不是“建议”，而是“指令”。



#### a、两种配置级别

**1）实例级别：在 `tidb.toml` 配置文件中设置：**

```
[isolation-read]
engines = ["tikv", "tidb", "tiflash"]
```

- **作用范围**：整个 `TiDB` 实例的所有会话
- **默认值**：`["tikv", "tidb", "tiflash"]`
- **建议**：始终保留 `"tidb"`，否则 `TiDB Dashboard`、`INFORMATION_SCHEMA` 查询可能失败



**2）会话级别**

```sql
-- 方法 1
SET @@session.tidb_isolation_read_engines = "tikv,tiflash";

-- 方法 2
SET SESSION tidb_isolation_read_engines = "tiflash";
```

- **作用范围**：当前连接（会话）
- **默认继承**：从实例级别配置继承
- **优先级**：**会话级别覆盖实例级别**



#### b、使用建议

**1）生产环境推荐配置**

```sql
# tidb.toml
[isolation-read]
engines = ["tikv", "tidb", "tiflash"]  # 实例级别：全开
```

```sql
-- 会话级别根据需要切换
SET SESSION tidb_isolation_read_engines = "tiflash";   -- 分析任务
SET SESSION tidb_isolation_read_engines = "tikv";     -- 事务任务
```



**2）自动化脚本中显式设置**

```sql
-- ETL 脚本开头
SET SESSION tidb_isolation_read_engines = "tiflash";
SET tidb_distsql_scan_concurrency = 20;

SELECT ... FROM huge_table;
```



#### b、`Engine` 隔离的核心价值

**Engine 隔离 ≠ 替代智能选择，而是“增强控制力”**。

- 日常使用：`"tikv,tiflash"` + 智能优化器
- 关键场景：通过 `SET SESSION` 显式指定
- 架构设计：实例级别保留 `"tidb"`，避免系统功能失效

| 价值                      | 说明                           |
| ------------------------- | ------------------------------ |
| **控制权回归开发者**      | 不依赖“智能选择”，明确表达意图 |
| **隔离 `OLTP` 与 `OLAP`** | 避免分析查询拖慢事务性能       |
| **保障强一致性**          | 通过 `"tikv"` 强制走主副本     |
| **提升可预测性**          | 查询路径固定，性能更稳定       |



### 3）基本语法

```sql
SELECT /*+ read_from_storage(engine[table_alias]) */ ...
FROM table_name [AS alias];

- 表 t 的别名是 a
SELECT /*+ read_from_storage(tiflash[a]) */ count(*) 
FROM t AS a;
```

- `engine`：可以是 `tikv` 或 `tiflash`
- `table_alias`：**必须是 `SQL` 中使用的表名或别名**



#### a、`Hint` 与 `Engine` 隔离的关系

- **`Hint` 必须在 `tidb_isolation_read_engines` 允许的范围内才生效**
- **`Engine` 隔离是“第一道防火墙”，Hint 是“第二道指令**





# 三、定位

## 1、`TiFlash` vs `TiKV`

注意：**不要让 `TiFlash` 直接服务 1000+ QPS 的在线请求**，这是“用错武器”。

| 维度         | TiFlash（OLAP）                | TiKV（OLTP）       |
| ------------ | ------------------------------ | ------------------ |
| **定位**     | 分析型、复杂查询               | 事务型、高并发     |
| **存储**     | 列式                           | 行式               |
| **查询类型** | 扫描亿级数据                   | 点查/小范围查      |
| **QPS 能力** | ~100（重查询）                 | 数万（轻查询）     |
| **延迟**     | 100ms ~ 数秒                   | < 10ms             |
| **适用场景** | 报表、`BI`、`Ad-hoc`(即席查询) | 在线服务、API 响应 |



### 2、关于 `TiFlash` 并发

- **并发能力**：虽然 `TiFlash` 能够很好地处理复杂的查询负载，但是每个查询本身可能需要消耗较多的计算资源（如 `CPU` 和内存）。

  - 因此，在实际部署中，`TiFlash` 实例所能支持的最大并发数取决于多个因素，包括但不限于硬件配置（CPU、内存）、网络带宽以及查询的复杂度和数据量大小等。

  - 官方文档提到 `TiFlash` 支持的并发请求数量视数据量和查询复杂度不同，但一般不会超过 `100 QPS`（每秒查询数），这是在考虑了典型应用场景后的经验值。

- **并发控制与优化**：

  - 在高并发场景下，可以通过增加 `TiFlash` 节点的数量来横向扩展系统处理能力。

  - 使用 `INSERT INTO SELECT` 将结果保存到 `TiKV` 表中，以此来缓解 `TiFlas`h 的并发压力，并提高响应速度。

- **`TiFlash` 对高并发简单查询的瓶颈**

  - **架构定位限制**：
    - `TiFlash` 本质是 TiDB 生态中面向 **复杂分析和 `HTAP` 混合负载** 的引擎，其设计并未以 “极致高并发简单查询” 为目标。
    - 它依赖 `TiDB Server` 转发查询，**且需与 `TiKV`（行存引擎）维持一致性协议**，这会带来额外的通信和锁开销，不适合高频次、低延迟的简单查询场景。

  - **单节点并发上限低**：`TiFlash` 基于 `ClickHouse` 引擎优化，单节点对简单查询的 `QPS` 通常在 **数千级别**（例如 `1-5k` QPS），远低于 `Doris` 单节点的数万级别。即使通过增加节点扩展，受限于 `TiDB Server `的调度能力和集群协同开销，总 `QPS` 也很难突破 **`10` 万级别**，更难支撑几十万的简单查询。

  - **资源消耗特性**：`TiFlash` 对内存和 `CPU` 的资源占用较高，高并发下容易出现锁竞争和线程调度瓶颈，导致查询延迟飙升，稳定性下降。

- **适合场景：**尽管 `TiFlash` 可能不适合极高并发的实时查询服务（例如 `Web` 应用的在线事务处理），但它非常适合用于后台批量处理任务、**定期生成报表或者需要深入数据分析的场合。**



## 2、为什么 `TiFlash` 并发不高

> 虽然 `TiFlash` 是为 `OLAP` 设计的，但“并发低”并不等于“性能差”。这里的“100 QPS”是一个**经验性参考值**，主要基于以下原因：

### 1）**查询复杂度高**

- **举例：**
  - 一个 `GROUP BY + SUM + JOIN` 查询扫描 10 亿行数据，耗时 800ms，
  - 那么 100 QPS 意味着系统每秒要处理 100 个这样的“重型”查询 —— 这已经是极高的分析吞吐能力。

- `OLAP` 查询通常是：
  - 多表 `JOIN`
  - 大范围扫描（`Sca`n 数亿行）
  - 多维度 `GROUP BY` + 聚合
  - 窗口函数、子查询等
- 这类查询本身就很“重”，单个查询可能就需要几百毫秒甚至几秒。
- 即使是 `10` 个并发，也可能已经占满 `CPU` 和内存资源。

### 2）**资源密集型计算**

- `TiFlash` 使用 **MPP 模式**进行并行计算，涉及大量数据 shuffle、内存哈希表、向量化执行。
- 每个查询消耗大量 CPU 和内存。
- 高并发会导致：
  - 内存溢出（`OOM`）
  - 调度开销剧增
  - 查询延迟飙升







# 四、使用 `MPP` 模式

## 1、`MPP` 模式

- **`Massively` `Parallel` `Processing` (`MPP`)**：这是一种分布式计算技术，通过在多个节点之间并行处理数据来提高查询性能。在 `TiFlash` 的上下文中，这意味着查询执行过程中会涉及到跨节点的数据交换（也称为数据 `shuffle`）。
- **自动选择机制**：默认情况下，`TiDB` 的优化器会自动决定是否采用 `MPP` 模式执行查询。这意呀着对于用户而言，无需手动干预即可利用到 `MPP` 带来的性能提升。

![image-20250808150814860](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20250808150814860.png)



## 2、控制 `MPP` 模式的参数

- **`tidb_allow_mpp`**：这个变量控制是否允许优化器选择 `MPP` 模式。
  - 如果设置为 `ON`，则优化器可以考虑使用 `MPP` 模式；
  - 若设置为 `OFF`，则不会使用 `MPP` 模式。
- **`tidb_enforce_mpp`**：当设置此变量为 `ON` 时，`TiDB` 无视代价估算，选择 `MPP` 模式。

|                                  | `tidb_allow_mpp=off` | `tidb_allow_mpp=on`（默认）            |
| :------------------------------- | :------------------- | :------------------------------------- |
| `tidb_enforce_mpp = off`（默认） | 不使用 `MPP` 模式。  | 优化器根据代价估算选择。（默认）       |
| `tidb_enforce_mpp = on`          | 不使用 `MPP` 模式。  | `TiDB` 无视代价估算，选择 `MPP` 模式。 |



## 3、算法支持

### 1）算法

| 算子                          | 说明                                           |
| ----------------------------- | ---------------------------------------------- |
| **Broadcast Hash Join**       | 小表广播到所有节点，大表本地 Join              |
| **Shuffled Hash Join**        | 两表按 Join Key 重分布（Shuffle），再本地 Join |
| **Shuffled Hash Aggregation** | 按 Group Key Shuffle 后并行聚合                |
| **Union All**                 | 多数据源结果合并                               |
| **TopN**                      | 分布式 TopN 排序                               |
| **Limit**                     | 分布式 Limit 下推                              |

### 2）判断 `MPP` 是否生效

在执行 `EXPLAIN` 时，若计划中出现以下算子，则表明 MPP 已启用：

- `ExchangeSender`：发送数据到其他节点
- `ExchangeReceiver`：接收来自其他节点的数据

**1）执行计划示例解析**

```sql
EXPLAIN 
SELECT COUNT(*) 
FROM customer c 
JOIN nation n ON c.c_nationkey = n.n_nationkey;
```

2）**执行计划关键路径**：

```
HashAgg_23                   -- TiDB 层最终聚合
└─TableReader_25
  └─ExchangeSender_24        -- 将聚合结果发回 TiDB
    └─HashAgg_12             -- TiFlash 节点本地聚合
      └─HashJoin_17          -- 执行 Join
        ├─ExchangeReceiver_21(Build)  -- 接收广播的小表 (nation)
        │ └─ExchangeSender_20        -- 广播 nation 表
        │   └─TableFullScan_18       -- 扫描 nation 表 (25行)
        └─TableFullScan_22(Probe)    -- 扫描 customer 表 (300万行)
```

**3）执行流程**：

1. `nation` 表（小表，25行）被 **广播（`Broadcast`）** 到所有 `TiFlash` 节点；
2. 每个节点本地扫描 `customer` 表，并与广播来的 `nation` 表进行 **`Hash Join`**；
3. `Join` 后进行本地 `HashAgg` 计数；
4. 聚合结果通过 `ExchangeSender` 返回 `TiDB`，最终汇总。





### 3）参数配置

#### a、参数说明

 | 变量名                                             | 作用                       | 单位          | 说明                                                       |
  | :------------------------------------------------- | -------------------------- | ------------- | ---------------------------------------------------------- |
  | `tidb_broadcast_join_threshold_size`               | 表大小阈值                 | 字节（Bytes） | 若表大小 < 此值，使用 Broadcast；否则 Shuffled             |
  | `tidb_broadcast_join_threshold_count`              | 行数阈值                   | 行数          | 用于子查询等无法估算大小的场景                             |
  | `tidb_prefer_broadcast_join_by_exchange_data_size` | 是否启用“最小网络交换”策略 | 布尔值        | 开启后，自动估算 Broadcast vs Shuffle 的网络开销，选更优者 |



#### b、推荐配置

```sql
-- 允许 MPP
SET tidb_allow_mpp = ON;

-- 启用智能选择 Broadcast Join（推荐）
SET tidb_prefer_broadcast_join_by_exchange_data_size = ON;

-- 或手动设置阈值（例如 20MB 以内广播）
SET tidb_broadcast_join_threshold_size = 20971520; -- 20MB
SET tidb_broadcast_join_threshold_count = 100000;  -- 10万行
```





### 4）最佳实践

- **监控执行计划**： 使用 `EXPLAIN` 或 `EXPLAIN ANALYZE` 检查是否使用了预期的 MPP 算子。

- **小表 Join 大表 → Broadcast Join**： 确保小表能被广播，避免不必要的 Shuffle 开销。

- **大表 Join 大表 → Shuffled Hash Join**： 数据均匀分布，避免内存溢出。

- **避免频繁广播大表**： 广播网络开销大，可能导致网络瓶颈。



### 5）总结

| 特性             | 说明                                              |
| ---------------- | ------------------------------------------------- |
| **MPP 启用条件** | `tidb_allow_mpp=ON` + 优化器判断                  |
| **MPP 标志**     | 出现 `ExchangeSender` / `ExchangeReceiver`        |
| **`Join` 策略**  | Broadcast（小表）或 Shuffle（大表）               |
| **控制方式**     | 三个变量 + 优化器自动选择                         |
| **推荐配置**     | 启用 `prefer_broadcast_by_data_size` 实现智能决策 |



# 五、查询结果物化

> **`TiFlash` 查询结果物化**，即支持在 `INSERT INTO SELECT` 语句中将 `TiFlash` 执行的查询结果直接写入 `TiDB` 表中。这标志着 `TiFlash` 从“纯读”模式迈向了“可写入”的分析流水线能力，极大增强了其在实时分析和 `BI` 场景中的实用性。



## 1、工作机制与执行流程

- `TiFlash` 首先将 `SELECT` 子句的查询结果返回到集群中某单一 `TiDB server` 节点，然后再写入目标表
- `INSERT INTO SELECT` 语句的执行保证 `ACID`特性。

```sql
INSERT INTO daily_data (rec_date, customer_id, daily_fee)
SELECT DATE(ts), customer_id, SUM(detail_fee)
FROM detail_data
WHERE DATE(ts) > '2023-01-01'
GROUP BY DATE(ts), customer_id;
```

**执行过程**：

1. **优化器决策**：
   - 检查 `detail_data` 是否有 `TiFlash` 副本。
   - 根据成本估算（受 `tidb_allow_mpp`、`sql_mode` 等影响）决定是否将 `SELECT` 下推至 TiFlash。
2. **`TiFlash` 执行查询**：
   - `TiFlash` 节点并行扫描 `detail_data`，执行 `GROUP BY + SUM` 聚合。
3. **结果返回与写入**：
   - 聚合结果由 **某一个 `TiDB` 节点** 接收（非广播）。
   - 该 `TiDB` 节点将结果写入 `daily_data` 表（可含 TiFlash 副本）。
4. **事务保证**：整个 `INSERT INTO SELECT` 操作满足 **`ACID`** 特性。





## 2、典型应用场景

### 1）场景 1：高效 `BI` 报表缓存

- **问题**：报表频繁刷新，每次执行复杂聚合，压力大。
- 解决方案：
  - 使用 `INSERT INTO SELECT` 将每日销售报表结果物化到 `daily_sales` 表。
  - 月报、季报直接基于 `daily_sales` 查询，避免重复计算。
- **优势**：降低计算负载，提升响应速度。



### 2）场景 2：支持高并发在线服务

- **问题**：`TiFlash` 并发有限（`~100 QPS`），无法直接服务高并发请求。
- 解决方案：
  - 后台定时任务（如每 `0.5` 秒）执行 `INSERT INTO result_table SELECT ... FROM tiflash_table`。
  - 前端查询 `result_table`（TiKV 存储），支持数千 `QPS`。
- **优势**：平衡性能与数据新鲜度。

- **示例：**如果您的在线应用需要展示最新的销售统计信息，但是直接查询TiFlash不能满足高并发的需求，您可以设置一个定时任务，每隔一段时间（如 0.5 秒）执行一次`INSERT INTO SELECT`语句，将最新数据从TiFlash查询出来并保存到一个专门的结果表中。前端应用则查询该结果表获取所需数据。

  - ```SQL
    -- 定期将TiFlash查询结果保存到结果表中
    INSERT INTO result_table (customer_id, sales_amount)
    SELECT customer_id, SUM(sales_amount) FROM sales_data GROUP BY customer_id;
    ```



## 3、使用限制

| 项目                 | 建议                                                         |
| -------------------- | ------------------------------------------------------------ |
| **内存控制**         | 推荐使用 `tidb_mem_quota_query` 控制单个查询内存，           |
|                      | **不推荐**使用 `txn-total-size-limit`来控制事务内存大小      |
| **大事务（~1GB）**   | 并发 ≤ 10，避免 `OOM` 或事务超时                             |
| **小事务（<100MB）** | 并发 ≤ 30，合理利用集群资源                                  |
| **监控指标**         | 请基于测试和具体情况做出合理选择。                           |
|                      | 关注 `tidb_server_query_total_time_count`、`tidb_executor_statement_total` 等 |







# 五、`TiFlash` 延迟物化

> 延迟物化是一种查询优化技术，核心思想是：**延迟物化 = 先筛行，再取列**。  

## 1、如何控制延迟物化？

通过系统变量 `tidb_opt_enable_late_materialization` 控制：

- 当关闭该功能时，如果 `SELECT` 语句中包含过滤条件（`WHERE` 子句），`TiFlash` 会先读取该查询所需列的全部数据，然后再根据查询条件对数据进行过滤、聚合等计算任务。
- 当开启该功能时，`TiFlash` 支持下推部分过滤条件到 `TableScan` 算子  
  - 即先扫描下推到 `TableScan` 算子的过滤条件相关的列数据，过滤得到符合条件的行后，
  - 再扫描这些行的其他列数据，继续后续计算，从而减少 `IO` 扫描和数据处理的计算量。

| 作用域    | 查看命令                                                     | 设置开启                                                 | 设置关闭           |
| --------- | ------------------------------------------------------------ | -------------------------------------------------------- | ------------------ |
| `Session` | `SHOW VARIABLES LIKE 'tidb_opt_enable_late_materialization';` | `SET SESSION tidb_opt_enable_late_materialization = ON;` | `SET SESSION OFF;` |
| Global    | `SHOW GLOBAL VARIABLES LIKE 'tidb_opt_enable_late_materialization';` | `SET GLOBAL ON;`                                         | `SET GLOBAL OFF;`  |



## 2、使用

### 1）优势

- **减少 `I/O` 量**：只读取满足条件的行的其他列。
- **降低网络传输与计算负载**：更少的数据参与后续聚合、排序等操作。
- **提升 `OLAP` 查询性能**：尤其对宽表（大量列）、高过滤率查询效果明显。



### 2）使用限制

- **仅适用于 `TiFlash MPP` 模式下的 `TableScan`**

- **`TiFlash Fast Scan` 模式下不支持**：若启用了 `Fast Scan`（适用于高并发小查询），延迟物化将自动失效。
- **优化器自动决策**：并非所有过滤条件都会被下推，优化器根据统计信息选择**过滤性强（高选择性）的条件**优先下推。



### 3）使用示例

示例解析

```sql
EXPLAIN SELECT a, b, c FROM t1 WHERE a < 1;
```

输出显示：

```sql
TableFullScan_9 ... pushed down filter:lt(test.t1.a, 1)
```

说明：

- 列 `a` 被用于下推过滤；
- `TiFlash` 先扫描 `a` 列并执行 `a < 1` 过滤；
- 得到符合条件的行位置（通过 `Bitmap` 标记）；
- 再去读取这些行的 `b`, `c` 列数据；
- 最终返回结果。



### 4）建议使用场景

| 场景                                                    | 是否推荐启用延迟物化 |
| ------------------------------------------------------- | -------------------- |
| 宽表查询（很多列），但只返回少数满足条件的行            | 推荐                 |
| 高选择性 `WHERE` 条件（如 `id = ?`, `status = 'paid'`） | 推荐                 |
| 全表扫描或低过滤率查询                                  | 效果有限             |
| 使用 `TiFlash Fast Scan` 模式                           | 不可用               |









# 六、`TiFlash` 数据落盘

> `TiFlash` 数据落盘是指：在执行复杂查询（如聚合、连接、排序）时，当内存使用达到一定阈值，`TiFlash` 将**中间计算结果写入本地磁盘**，以缓解内存压力，防止 `OOM`（`Out of Memory`），保障查询稳定性。   

在生产环境中，**强烈建议启用查询级别的落盘机制**，并结合监控调整参数，既能保障大查询成功执行，又能避免个别查询耗尽节点内存，影响整体服务稳定性。

**1）本质：**用磁盘空间换内存，提升大查询的可行性与稳定性。

**2）支持落盘的算子**

- 带有等值关联条件的 `Hash` `Join` 算子
- 带有 `GROUP BY` `key` 的 Hash Aggregation 算子
- `TopN` 算子以及窗口函数中的 Sort 算子

## 1、触发机制

### 1）**算子级别落盘**

> 通过设置每个算子的内存阈值，控制何时触发落盘。

说明：设置为 `0` 表示不限制内存，**不触发落盘**。

| 变量名                                            | 作用                      |
| ------------------------------------------------- | ------------------------- |
| `tidb_max_bytes_before_tiflash_external_group_by` | Hash Aggregation 落盘阈值 |
| `tidb_max_bytes_before_tiflash_external_join`     | Hash Join 落盘阈值        |
| `tidb_max_bytes_before_tiflash_external_sort`     | Sort/TopN 落盘阈值        |

 **示例效果：**内存峰值从 29.55 GiB 降至 12.80 GiB。

```
-- 设置聚合算子最多使用 10GB 内存，超限则落盘
SET tidb_max_bytes_before_tiflash_external_group_by = 10737418240;

SELECT l_orderkey, MAX(l_comment) 
FROM lineitem 
GROUP BY l_orderkey;
```



### 2）**查询级别落盘**

> 更高级别的控制，基于整个查询的内存使用情况动态触发落盘。

说明：当两者均 > 0 时生效。 **优先级高于算子级别设置**：一旦启用查询级落盘，算子级阈值将被忽略。

| 变量名                             | 说明                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| `tiflash_mem_quota_query_per_node` | 单个查询在单个 `TiFlash` 节点上的**内存上限**                |
| `tiflash_query_spill_ratio`        | 触发落盘的**内存比例阈值**（如 0.7 表示达上限的 70% 时触发） |

 **示例效果：内存峰值控制在 **3.94 GiB。

```sql
- 单查询最多用 5GB 内存，达到 70%（3.5GB）时开始落盘
SET tiflash_mem_quota_query_per_node = 5368709120;
SET tiflash_query_spill_ratio = 0.7;

SELECT l_orderkey, MAX(l_comment) 
FROM lineitem 
GROUP BY l_orderkey 
HAVING SUM(l_quantity) > 314;
```



## 2、使用

### 1）注意事项

| 注意点                             | 说明                                                         |
| ---------------------------------- | ------------------------------------------------------------ |
| **Fast Scan 模式下不支持落盘**     | 若启用 TiFlash Fast Scan（用于低延迟点查），落盘功能不可用。 |
| **多个算子独立计数**               | 未启用查询级落盘时，每个算子单独判断是否超限。例如两个聚合算子各可使用 10GB。 |
| **Aggregation/Sort 只落盘一次**    | 在 merge 阶段只触发一次落盘，即使内存仍超限也不会再落。      |
| **Join 可多轮落盘**                | 最多支持 **三轮落盘**，超过后不再落盘，可能 OOM。            |
| **查询级落盘优先**                 | 一旦启用 `tiflash_mem_quota_query_per_node` 和 `spill_ratio`，算子级设置失效。 |
| **非落盘算子仍可能导致 OOM**       | 如表达式计算、网络缓冲等占用内存过多，即使有落盘算子也可能失败。 |
| **落盘不及时？调低 `spill_ratio`** | 提前触发落盘，避免内存突增导致超限。                         |





### 2）推荐配置

| 场景                      | 建议配置                                                   |
| ------------------------- | ---------------------------------------------------------- |
| **`OLAP` 大查询为主**     | 启用查询级落盘，设置合理内存上限和 `spill_ratio=0.7~0.8`   |
| **混合负载（大+小查询）** | 设置 `tiflash_mem_quota_query_per_node` 防止大查询拖垮集群 |
| **调试特定算子性能**      | 使用算子级变量进行精细控制                                 |
| **高并发小查询**          | 建议关闭落盘（设为 0），避免磁盘 I/O 开销影响延迟          |







# 七、`TiFlash` `MinTSO` 调度器

`TiFlash MinTSO` 调度器是一个**分布式任务调度器**，用于管理 `TiFlash` 节点上 `MPP` 查询任务（`MPP Task`）的执行顺序和资源分配，



在 `MPP` 模式下，一个 `SQL` 查询会被拆分为多个 **`MPP Task`**，这些 `Task` 之间存在**依赖关系**

-  **控制线程使用量：**
  - 每个 `MPP` `Task` 执行需要多个线程（取决于算子复杂度）。
  - 高并发时，大量并发查询 → 大量 `Task` → 线程数线性增长 → 超出操作系统限制 → `OOM` 或无法创建线程。
-  **避免系统死锁**
-  **提升高并发场景下的执行效率与稳定性**



## 1、实现原理

> 思想：**确保系统中始终有且仅有一个“特殊查询”可以无阻塞执行，打破死锁循环。这个“特殊查询”被称为 **`MinTSO` 查询**。**

### 1）两层线程限制机制

**说明**：保证至少有一个查询能完整执行，避免所有查询都“半途而废”。

- 正常查询：仅当线程数 < `soft limit` 时才可调度。

- `MinTSO` 查询：只要线程数 < `hard limit`，即可调度所有 Task。

| 限制类型            | 作用           | 是否可突破         |
| ------------------- | -------------- | ------------------ |
| `thread_soft_limit` | 资源控制软上限 | MinTSO 查询可突破  |
| `thread_hard_limit` | 系统保护硬上限 | 不可突破，超限报错 |



### 2）如何确定“MinTSO 查询”

- 每个查询在 `TiDB` 中都有一个 **`start_ts`**（开始时间戳，即读取快照的时间）。
- `MinTSO` 调度器定义：**`start_ts` 最小的查询** 为 MinTSO 查询。
- 所有 `TiFlash` 节点独立判断，但因“全局最小值必是局部最小值”，最终选出的是**同一个查询**。



### 3）调度流程

1. 新 `MPP Task` 到达 `TiFlash` 节点；
2. 判断当前查询是否为 `MinTSO` 查询：
   - 是 → 检查是否超过 `hard_limit`，未超则直接调度；
   - 否 → 检查当前线程数是否低于 `soft_limit`：
     - 是 → 调度；
     - 否 → 排队等待；
3. 执行任务，释放资源。



### 4）引入 `active_set_soft_limit`：防“饥饿”优化

即使有 MinTSO 查询机制，仍可能出现：

- 大量查询只有部分 Task 被调度 → 卡住不前 → 系统吞吐下降。

为此引入： **`active_set_soft_limit`**：限制同时参与调度的**查询数量**（而非线程数）。

- 系统最多允许 `N` 个查询的 Task 参与调度；
- 超出的查询需等待前面的查询完成；
- `MinTSO` 查询仍可突破此限制（只要不超 `hard limit`）。



## 2、使用建议

- 生产环境应合理配置 `thread_soft_limit` 和 `hard_limit`，避免资源浪费或死锁风险。
- 监控 `TiFlash` 日志中的调度行为与线程使用情况，及时调优。
- 对于延迟敏感的查询，可通过 `tidb_isolation_read_engines` 避免进入 `MPP` 模式。



### 1）推荐配置

- `thread_soft_limit`：建议设为 `CPU` 核数的 `2~4` 倍（考虑 IO 等待）。
- `thread_hard_limit`：略高于 `soft limit`，留出“逃生通道”。
- `active_set_soft_limit`：根据并发负载调整，避免过大导致资源碎片。

































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



