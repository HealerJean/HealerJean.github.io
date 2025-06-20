---
title: 
date: 2025-01-01 00:00:00
tags: 
- 
category: 
- 
description: 
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          







# 一、基本介绍

> 列式存储能够减少数据扫描范围，数据按列组织，数据库可以直接获取查询字段的数据。而按行存逐行扫描，获取每行数据的所有字段，再从每一行数据中返回需要的字段，虽然只需要部分字段还是扫描了所有的字段，按列存储避免了多余的数据扫描。

## **1、存储与查询效率**：

`Clickhouse` 采用列式存储，数据按列进行组织，属于同一列的数据会被保存在一起，这是后续实现秒级查询的基础。在存储数据达到20万亿行的情况下，也能做到 `90%` 的查询能够在1秒内返回结果。列式存储压缩率高，数据在网络中传输更快，对网络带宽和磁盘 `IO` 的压力更小。



## **2、实时聚合能力**：

`Clickhouse` 能够实现实时聚合，一切查询都是动态、实时的，用户发起查询的那一刻起，整个过程需要能做到在一秒内完成并返回结果。与多种业务场景契合。。

**功能完整性**： `Clickhouse`支持完整的 `DBMS`。支持动态创建、修改或删除数据库、表和视图，可以动态查询、插入、修改或删除数据。除了完整的 `DBMS`、列式存储外，还支持在线实时查询、拥有完善的 `SQL` 支持和 函数、拥有多样化的表引擎满足各类业务场景。

**场景适用性**：正因为 `Clickhouse` 的这些特性，在它适合的场景下能够实现动态、实时的秒级别查询。



## 3、适合场景

**读写特性**：读多于写。数据一次写入，多次查询，从各个角度对数据进行挖掘，发现数据的价值。

**数据处理模式**：针对大宽表，通过选取少量维度列与指标列进行聚合计算，输出少量结果集，满足海量数据下的高效分析需求。。

**数据更新需求**：数据批量写入，不需要经常更新、删除。数据写入完成后，相关业务不要求经常对数据更新或删除，主要用于查询分析数据的价值。

**应用领域**：`Clickhouse` 适合用于商业智能领域，广泛应用于广告流量、`App` 流量、物联网等众多领域。借助 `Clickhouse` 可以实时计算线上业务数据，如资源位的点击情况，以及并对各资源位进行 `bi` 预警。



# 二、**`MergeTree`**

> `MergeTree `是 `ClickHouse` 最强大的基础存储引擎，其设计理念围绕高效数据存储、索引和查询优化展开。下面从分区、排序、索引和数据读取流程四个方面详细解析，并结合实例说明：

1、**`ReplacingMergeTree`**

> 继承 `MergeTree`， 在建表时设置 `ORDER BY` 排序字段作为判断重复数据的唯一键，在合并分区的时候会触发删除重复数据，能够一定程度上解决数据重复的问题。



2、**`AggregatingMergeTree`**

> 继承 `MergeTree`， 在合并分区的时候按照定义的条件聚合数据，将需要聚合的数据预先计算出来，在聚合查询时直接使用结果数据，以空间换时间的方法提高查询性能。该引擎需要使用 `AggregateFunction` 类型来处理所有列。



## **1、分区（`Partitioning`）**

> 分区是将表数据按指定规则拆分为多个独立物理目录的机制。

- **分区规则**：支持多列组合分区（如`PARTITION BY (date, region)`），但单字段分区效率最高。
- **物理存储**：每个分区对应独立目录，写入时属于同一分区的数据最终会合并到该目录，不同分区数据永不合并。
- **查询优化**：合理分区可大幅减少查询时扫描的数据文件数量。

**示例**：假设某电商订单表按日期分区：

- 当查询 2023 年 10 月数据时，`ClickHouse` 直接定位到`202310`分区目录，无需扫描其他月份的数据文件。

```sql
CREATE TABLE orders (
    order_id UInt64,
    order_date Date,
    user_id UInt64,
    amount Decimal(10, 2)
) ENGINE = MergeTree()
PARTITION BY toYYYYMM(order_date)  -- 按月分区
ORDER BY (order_date, user_id);
```





##  **2、排序（`Ordering`）**

> 排序决定了数据在分区内的物理存储顺序，通过 `ORDER BY` 子句定义。   
>
> 在 `ClickHouse` 中，排序（`Ordering`）**和**索引（`Index`） 是紧密协作的两个核心机制，共同决定了数据的物理存储结构和查询效率。理解它们的关系对优化查询性能至关重要，以下是详细解析：

### 1）**排序：定义数据的物理存储顺序**

- **作用**：排序决定了数据在磁盘上的存储顺序，通过 `ORDER BY` 子句声明。
- **物理存储**：数据按排序键（`Sorting` `Key`）逐行存储，相同键值的数据连续存放。
- **与索引的关联**：排序键是构建索引的基础，主键索引默认使用排序键的字段。

```sql
CREATE TABLE logs (
    event_date Date,
    user_id UInt64,
    event_time DateTime,
    event_type String
) ENGINE = MergeTree()
ORDER BY (event_date, user_id);  -- 按日期和用户ID排序
```

- 数据先按 `event_date` 升序排列，日期相同的记录再按 `user_id` 排序。
- 这种排序方式使同一日期、同一用户的记录在物理上连续存储，加速范围查询。



### 2）**排序与索引的协同工作原理**

> - **排序是基础**：决定数据的物理布局，影响所有查询的性能。
> - **索引是加速器：基于排序键构建，针对特定查询模式优化。**
> - **设计关键**：让排序键与高频查询的过滤条件匹配，使索引能够最大程度减少数据扫描范围。

#### **a、数据写入时**

- 数据按排序键顺序写入磁盘，形成有序的数据片段（Part）。
- 索引文件根据排序键生成，记录每个数据块的起始键值和偏移量。

#### **b、数据查询时**

- **索引过滤**：通过索引快速定位可能包含目标数据的数据块（基于排序键的有序性）。
- **范围扫描**：在过滤后的数据块内，按物理顺序扫描数据（无需随机 IO）。

| **特性**     | **排序（Ordering）**         | **索引（Index）**                     |
| ------------ | ---------------------------- | ------------------------------------- |
| **作用**     | 定义数据物理存储顺序         | 加速数据定位                          |
| **声明方式** | `ORDER BY (col1, col2)`      | `PRIMARY KEY (col1, col2)` 或 `INDEX` |
| **物理影响** | 直接决定数据在磁盘上的排列   | 额外创建索引文件（.idx、.mrk）        |
| **查询优化** | 减少磁盘随机读，适合范围扫描 | 减少扫描的数据量                      |
| **适用场景** | 所有查询（基于数据有序性）   | 特定条件查询（如 WHERE、JOIN）        |



## 3、索引

### 1）**主键索引（`Primary` `Key`）**

> 主键索引是 `MergeTree` 引擎的核心索引，通过 `PRIMARY KEY` 子句声明。它与排序键（`ORDER BY`）紧密相关：

- **默认规则**：若未显式声明 `PRIMARY KEY`，则使用 `ORDER BY` 的字段作为主键。
- **稀疏索引**：**主键索引采用稀疏存储**，默认每 `8192` 行（由 `index_granularity` 参数控制）记录一次索引项。

**示例 1：显式声明主键**

```sql
CREATE TABLE logs (
    event_date Date,
    event_time DateTime,
    user_id UInt64,
    event_type String
) ENGINE = MergeTree()
PRIMARY KEY (event_date, user_id)  -- 主键由日期和用户ID组成
ORDER BY (event_date, user_id, event_time);  -- 排序键包含额外字段
```

**示例 2：省略主键（默认使用 ORDER BY）**

```sql
CREATE TABLE sales (
    date Date,
    product_id UInt64,
    amount Decimal(10, 2)
) ENGINE = MergeTree()
ORDER BY (date, product_id);  -- 主键隐式为 (date, product_id)
```



### 2） **二级索引（`Skip` `Index`）**

> 二级索引（跳过索引）用于加速范围查询，通过 `INDEX` 子句声明。它基于主键索引进一步筛选数据，减少扫描范围。

```sql
INDEX index_name expr TYPE index_type GRANULARITY N
```

- `expr`：索引表达式（如列名、函数）。
- `index_type`：索引类型（如 `minmax`、`set(N)`、`ngrambf_v1` 等）。
- `GRANULARITY`：索引粒度，表示每隔多少个主索引项创建一个二级索引项。



**示例：使用 `minmax` 索引加速日期范围查询**

```sql
CREATE TABLE visits (
    visit_date Date,
    user_id UInt64,
    pageviews UInt8,
    INDEX date_index visit_date TYPE minmax GRANULARITY 16  -- 每16个主索引项创建一次日期范围索引
) ENGINE = MergeTree()
PRIMARY KEY (user_id)
ORDER BY (user_id, visit_date);
```



### 3）**特殊索引类型**

> `ClickHouse` 提供多种特殊索引类型，适用于不同场景：

**1、`MinMax` 索引**:记录数据块的最小值和最大值，用于快速排除不满足条件的数据块。

```sql
INDEX date_range visit_date TYPE minmax GRANULARITY 8;
```

2、**`Set` 索引**：记录列中频繁出现的值，适用于等值查询。

```sql
INDEX user_set user_id TYPE set(1000) GRANULARITY 4;  -- 最多记录1000个不同值
```

3、**`ngram` 索引**：用于字符串前缀匹配，支持模糊查询。

```sql
INDEX url_ngram url TYPE ngrambf_v1(3, 256, 2, 0) GRANULARITY 1;  -- 3-gram，256个桶
```



## 4、**`MergeTree` 存储结构**

一张 `MergeTree` 表的完整物理结构包含：

- **数据表目录**：包含所有分区目录。
- **分区目录**：如 `202310_1_10_0`（表示 2023 年 10 月，第 1-10 个数据片段）。
- 分区内文件
  - **一级索引文件（.idx）**：存储稀疏索引，记录数据区间位置，记录每个数据块的起始键值和偏移量，通过 `ORDER BY` 或 `PRIMARY KEY` 声明，使用少量的索引能够记录大量数据的区间位置信息，内容生成规则跟排序字段有关，且索引数据常驻内存，取用速度快。借助稀疏索引，可以排除主键范围外的数据文件，从而有效减少数据扫描范围，加速查询速度。
  - **列压缩文件（.bin）**：每列独立存储，支持高效压缩（如 `LZ4`、`ZSTD`），存储每一列的数据，每一列字段都有独立的数据文件。
  - **列标记文件（.mrk）**：记录压缩文件中数据块的偏移量，建立索引与数据的映射。每一列都有对应的标记文件，保存了列压缩文件中数据的偏移量信息，与稀疏索引对齐，又与压缩文件对应，建立了稀疏索引与数据文件的映射关系。不能常驻内存，使用`LRU` 缓存策略加快其取用速度

**索引原理**：稀疏索引通过少量索引项定位大量数据。例如，对于 `100` 万行数据，索引可能每 `8192` 行记录一次主键范围，只需扫描索引即可排除无关数据块。



### 5）数据存储和查询流程

#### a、数据存储流程

数据写入。每批数据的写入，都会生成一个新的分区目录，后续会异步的将相同分区的目录进行合并。按照索引粒度，会分别生成一级索引文件、每个字段的标记和压缩数据文件。写入过程如下图：

![image-20250527212716512](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20250527212716512.png)



#### **b、数据读取流程**

> 读取数据需通过标记文件建立的映射关系，分两步完成：

1. **读取压缩数据块**：通过索引定位数据区间 → 通过标记文件找到对应压缩文件的偏移量 → 加载压缩块到内存。
2. **解压并读取数据**：解压数据块 → 根据查询条件过滤数据 → 返回结果。

**示例**：

```sql
`SELECT SUM(amount) FROM orders WHERE order_date = '2023-10-15' AND user_id = 123`
```

1. **索引过滤**：稀疏索引定位到  `2023-10-15`  所在的数据区间。
2. **标记文件映射**：通过`.mrk` 文件找到 `order_date `和`user_id` 列对应的数据块偏移量。
3. **数据读取**：加载并解压对应列的数据块，过滤出 `user_id=123` 的记录，计算`amount`总和。

![image-20250527212517465](/Users/zhangyujin1/Desktop/HealerJean/HCode/HealerJean.github.io/blogImages/image-20250527212517465.png)

**注意：**查询语句如果没有匹配到任务索引，会扫描所有分区目录，这种操作给整个集群造成较大压力。





### 6）**优化建议**

1. **合理分区**：按日期、地域等高频查询维度分区（如`PARTITION BY toYYYYMM(date)`）。
2. **排序键设计**：将高频查询的过滤条件（如时间、用户 ID）作为排序键。
3. **稀疏索引优化**：通过 `index_granularity` 参数调整索引粒度（默认 `8192`  行），平衡索引大小与查询效率。
4. **避免跨分区查询**：分区过多会增加元数据扫描开销，建议单个表分区数不超过 10 万。









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



