---
title: MySQL Binlog消息接入实战教程
date: 2026-09-09 10:00:00
tags:
- Binlog
- MQ
- 数据同步
category:
- 数据同步
description: 从消息来源、报文结构、消费入口到解析分发的一整套 Binlog 增量同步接入教程
---

**前言**

本文以「业务表变更 → Binlog → 消息队列 → 应用消费」的增量同步链路为例，讲清楚 Binlog 消息 **从哪来、长什么样、怎么发出来、怎么被消费**。文中所有字段、表名均为演示用途，给出的是一套可直接落地的标准做法。

---

# 一、Binlog 消息从哪来

## 1、什么是 Binlog

MySQL 的 **Binary Log（二进制日志）** 记录了所有对数据产生变更的操作（INSERT / UPDATE / DELETE / DDL），最初用于**主从复制**和**数据恢复**。

利用这一特性，我们可以让一个「订阅者」伪装成 MySQL 的从库，实时拉取主库的 Binlog，从而感知业务表的每一次数据变更——这就是 **CDC（Change Data Capture，变更数据捕获）**。

## 2、完整链路

```
业务系统 (写库)
      │  INSERT / UPDATE / DELETE
      ▼
   MySQL 主库  ──写入──►  Binlog
      │
      ▼
   采集组件 (Canal / Debezium / DTS 等)   ← 伪装成 MySQL 从库，订阅 Binlog
    │  解析 Binlog → 转成 JSON
      ▼
   消息队列 (Kafka / RocketMQ 等)          ← 按表/库路由到指定 Topic
      │
      ▼
   本应用消费 (消息监听器)                 ← 本文重点
      │  反序列化 → 分发 → 业务处理
      ▼
   下游 (缓存 / ES / 增量表 / 派生数据)
```

**关键点**：应用**不直接连接 MySQL**，而是消费采集组件投递到消息队列的变更消息。这样做的好处：
- 解耦：业务写库无感知，不影响主流程。
- 削峰：消息队列缓冲高峰流量。
- 可回溯：消息可重放，支持重试。

---

# 二、Binlog 消息长什么样（报文结构）

采集组件把一行数据的变更序列化为一条 JSON 消息，投递到消息队列。一个通用且清晰的报文结构如下：

```json
{
  "version": "1.0",
  "database": "demo_db",
  "table": "order_status",
  "type": "update",
  "ts": 1725850000000,
  "pks": "id",
  "data": {
    "id": "1001",
    "biz_code": "A-2026",
    "biz_type": "NORMAL",
    "status": "PAID",
    "modified_time": "2026-09-09 10:00:00"
  },
  "old": {
    "status": "UNPAID"
  }
}
```

| 字段       | 含义                                    |
| ---------- | --------------------------------------- |
| `version`  | 报文版本号                              |
| `database` | 库名                                    |
| `table`    | **表名（分发的关键依据）**              |
| `type`     | 操作类型：`insert` / `update` / `delete` / `ddl` |
| `ts`       | 变更时间戳（毫秒）                      |
| `pks`      | 主键字段名                              |
| `data`     | **变更后**的整行数据（after）           |
| `old`      | **变更前**的字段数据（before，通常只含被改动字段） |

> **格式差异怎么办**：不同采集组件的字段命名可能有差异（如有的用 `tab` 表示表名、`cur` 表示变更后）。消费端不应把某一种格式写死，而是在入口做一层「格式归一化」——用**策略模式**把外部报文统一转成内部标准模型，后续逻辑只面向标准模型编程：

```java
/** 标准领域事件：整条链路唯一在流转的对象 */
@Data
@Builder
public class BinlogEvent<T> {
    private String table;         // 表名
    private OpType opType;        // INSERT / UPDATE / DELETE
    private long ts;              // 变更时间戳
    private String primaryKey;    // 主键值（用于幂等/去重）
    private T before;             // 变更前（泛型：归一化时为 JSONObject，反序列化后为业务 BO）
    private T after;              // 变更后
}

/** 归一化器：每种外部格式一个实现，只做「识别 + 转成领域事件」 */
public interface FormatNormalizer {
    boolean support(String rawJson);
    /** 归一化产物固定是 BinlogEvent<JSONObject>——此时还不知道是哪张表，无法反序列化到具体 BO */
    BinlogEvent<JSONObject> normalize(String rawJson);
}
```

新增一种采集格式，只需加一个 `FormatNormalizer` 实现，**不改任何下游逻辑**。至此原始报文的差异被关在门外——从下一步起，我们讨论的对象永远是干净的 `BinlogEvent`。

---

> **本文的设计主线**：很多接入方案会写成一条 `监听器 → 基类 → 处理器基类 → 业务处理器` 的深继承链，把「反序列化、路由、过滤、幂等、业务」全塞进继承体系，方法越长、耦合越深。本文反其道而行——**用组合代替继承**，把整条链路拆成四个职责单一、可独立测试、可自由拼装的构件，由一个编排者 `BinlogPipeline` 串起来：

```
BinlogPipeline （编排者，唯一入口）
   ├─ FormatNormalizer   归一化：原始报文 → BinlogEvent
   ├─ EventRouter        路由：@BinlogTable 注解 → 找到目标 Processor
   ├─ List<EventFilter>  通用过滤责任链：DDL / 开关 / 幂等，与具体表无关，全表复用
   └─ BinlogProcessor    处理：表级/操作级业务过滤(acceptXxx) + 业务动作(onXxx)
```

下面按 **归一化 → 编排 → 路由与过滤 → 业务处理** 的顺序逐个拆解。

---

# 三、编排者：BinlogPipeline

消息队列的监听器（Kafka `@KafkaListener` / RocketMQ 等）只做一件事：**拿到原始报文，交给 `BinlogPipeline`**。所有真正的逻辑都在 Pipeline 里用组合完成，监听器本身没有任何业务，也不继承任何基类。

```java
@Component
public class BinlogConsumer {

    @Resource
    private BinlogPipeline pipeline;

    /** 批量入口：单条失败不影响其它消息 */
    public void onMessages(List<String> messages) {
        for (String raw : messages) {
               pipeline.accept(raw);
        }
    }
}
```

`BinlogPipeline` 是整套设计的**核心编排者**。它不继承任何东西，而是把四个构件**注入进来按顺序组合**——这就是「组合优于继承」的落点：每个构件都能单独替换、单独测试，Pipeline 只描述「流程」，不掺杂任何一步的「实现」。

```java
@Component
public class BinlogPipeline {

    /** 1. 归一化器集合：屏蔽外部格式差异 */
    @Resource
    private List<FormatNormalizer> normalizers;

    /** 2. 路由：事件 → 目标处理器 */
    @Resource
    private EventRouter router;

    /** 3. 过滤责任链：幂等 / 开关 / 业务过滤，按 @Order 排序注入 */
    @Resource
    private List<EventFilter> filters;

    public void accept(String rawJson) {
        // ① 归一化：原始报文 → 标准领域事件（after/before 是 JSONObject，尚未按表反序列化）
        BinlogEvent<JSONObject> event = normalize(rawJson);
        if (event == null) {
            return;
        }

        // ② 路由：按表名找到唯一处理器；找不到即丢弃（无意义重试）
        BinlogProcessor processor = router.route(event.getTable());
        if (processor == null) {
            log.warn("表[{}]无处理器，跳过", event.getTable());
            return;
        }

        // ③ 过滤责任链：任一环节拒绝则终止，且各 Filter 互不知晓彼此
        for (EventFilter filter : filters) {
            if (!filter.accept(event)) {
                log.info("被[{}]拦截, table={}, pk={}",
                        filter.getClass().getSimpleName(), event.getTable(), event.getPrimaryKey());
                return;
            }
        }

        // ④ 分发到处理器：反序列化由处理器基类内部完成，Pipeline 不掺和"用什么类型"这件事
        processor.handle(event);
    }

    private BinlogEvent<JSONObject> normalize(String rawJson) {
        return normalizers.stream()
                .filter(n -> n.support(rawJson))
                .findFirst()
                .map(n -> n.normalize(rawJson))
                .orElse(null);
    }
}
```

**组合式 Pipeline 带来的几个特性**：

| 维度       | 组合式 Pipeline                                             |
| ---------- | ---------------------------------------------------------- |
| 结构       | Pipeline 持有 4 个平级构件，各自独立                        |
| 过滤逻辑   | 通用过滤拆成 `EventFilter` 责任链，业务过滤内聚各处理器     |
| 扩展方式   | 加一个构件实现即可，Pipeline 零改动                        |
| 可测试性   | 每个构件都是普通类，可单独单测                             |

`normalize` 已在第二章说明，下面依次拆解 **路由（EventRouter）**、**过滤链（EventFilter）** 和 **处理器（BinlogProcessor）**。

---


# 四、路由、过滤与处理

这一章拆解 Pipeline 里剩下的三个构件。它们彼此独立，谁也不知道谁的存在，全靠 Pipeline 编排。

## 1、路由：用注解显式声明，而非 Bean 名硬约定

「靠 Bean 名等于表名」是一种脆弱的隐式约定——改个 Bean 名就断了，而且**表名一改就是全链路重命名**。这里改用**显式注解** `@BinlogTable` 声明归属：一个处理器负责一张表，同时把该表的**消息体类型**一并声明出来，交给编排层做一次反序列化即可：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BinlogTable {
    /** 该处理器负责的表名——一个处理器一张表，避免"多张表挤在一个类里" */
    String value();

    /** 该表 after/before 的业务 BO 类型，编排层按此做一次反序列化 */
    Class<?> messageType();
}
```

```java
@Component
public class EventRouter {

    /** table → 处理器 */
    private final Map<String, BinlogProcessor> table2processor = new HashMap<>(16);

    /** 启动时扫描所有 BinlogProcessor：注册路由 + 把 messageType 反塞给处理器 */
    public EventRouter(List<BinlogProcessor> processors) {
        for (BinlogProcessor p : processors) {
            BinlogTable anno = p.getClass().getAnnotation(BinlogTable.class);
            if (anno == null) {
                throw new IllegalStateException(p.getClass() + " 缺少 @BinlogTable");
            }
            BinlogProcessor exist = table2processor.putIfAbsent(anno.value(), p);
            if (exist != null) {
                throw new IllegalStateException("表[" + anno.value() + "]被多个处理器声明");
            }
            // 复用同一份注解解析：把 messageType 一次性交给处理器，避免处理器端再来一次反射
            p.setMessageType(anno.messageType());
        }
    }

    public BinlogProcessor route(String table) {
        return table2processor.get(table);
    }
}
```

比起隐式约定，注解路由**可读、可校验（重复声明启动即报错）**。更关键的是，注解里的 `messageType` 在启动扫描时就被 Router 一次性读出并塞给处理器（下节 `setMessageType`），后续每条消息不再需要重复反射注解——路由和"用什么类型反序列化"这两件事都在启动期一次搞定，运行期只做纯粹的 Map 查找。

## 2、过滤：责任链，一个 Filter 只管一件事

很多接入方案会把「DDL 过滤、开关控制、幂等防乱序」这几种完全不同的关注点，一股脑塞进一个大而全的前置判断方法里，越写越长、越改越怕。这里把它们拆成独立的 `EventFilter`，每个只管一件事，用 `@Order` 控制顺序，Pipeline 依次询问：

```java
public interface EventFilter {
    /** 返回 false 表示拦截，事件不再向后流转 */
    boolean accept(BinlogEvent event);
}
```

```java
/** ① DDL / 空数据过滤：结构变更不承载业务数据 */
@Order(10)
@Component
public class DdlFilter implements EventFilter {
    @Override
    public boolean accept(BinlogEvent event) {
        return event.getOpType() != null && event.getAfter() != null;
    }
}
```

```java
/** ② 开关过滤：灰度 / 应急降级时可整体关闭某表消费 */
@Order(20)
@Component
public class SwitchFilter implements EventFilter {

    @Resource
    private BinlogSwitchConfig switchConfig;

    @Override
    public boolean accept(BinlogEvent event) {
        return switchConfig.isEnabled(event.getTable());
    }
}
```

```java
/** ③ 幂等 + 防乱序：仅当本次 ts 不早于已处理 ts 才放行 */
@Order(30)
@Component
public class IdempotentFilter implements EventFilter {

    @Resource
    private ProcessedTsStore tsStore;   // 记录「主键 → 最近处理时间戳」

    @Override
    public boolean accept(BinlogEvent event) {
        Long last = tsStore.get(event.getTable(), event.getPrimaryKey());
        if (last != null && event.getTs() < last) {
            return false;               // 旧消息，丢弃
        }
        tsStore.put(event.getTable(), event.getPrimaryKey(), event.getTs());
        return true;
    }
}
```

**责任链的价值就在于此**：DDL、开关、幂等这类逻辑对所有表都通用，做成 Filter 后**一次编写、全表复用**，不必每接一张表就重抄一遍。注意——责任链**只承载这类"与具体表无关"的通用过滤**；至于"每张表规则不同、甚至同表增删改各不相同"的业务过滤，不属于这里，它们内聚在各自的处理器中（见下一节）。

## 3、处理器：表级 / 操作级业务过滤都收在这里

到这一层，前面 `EventFilter` 责任链只解决了**与业务无关的通用过滤**（DDL、开关、幂等）。但真正棘手的是另一类过滤：

- **每张表的业务过滤规则都不一样**：`order_status` 只同步某些 `biz_type`，`user_account` 要排除测试账号，`stock` 只关心库存真正变化的行……
- **同一张表，增 / 删 / 改的规则又各不相同**：可能"新增全要、更新只在关键字段变化时才要、删除直接忽略"。

这类规则如果继续往 `EventFilter` 责任链里塞，责任链会迅速膨胀成"每表 × 每操作"一堆 Filter，而且每个 Filter 还得先 `if (table.equals("xxx"))` 判断是不是自己——这就退化成了大杂烩。**正确的归属是：谁的业务谁自己管**——把「本表、本操作要不要处理」这件事，内聚到该表处理器内部，和对应的业务动作一一并排。

`BinlogProcessor` 对外只暴露一个方法 `handle(BinlogEvent<JSONObject>)`——**Pipeline 递进来的永远是通用报文**，"用什么 BO 反序列化"由处理器自己知道（Router 启动时已经通过 `setMessageType` 塞进来了）。这一次转换在 `OpTypeProcessor<T>` 基类里做完，子类拿到的 `event.getAfter()` 就是强类型 BO：

```java
/** 顶层接口：Pipeline 只认这一件事 */
public interface BinlogProcessor {

    void handle(BinlogEvent<JSONObject> event);

    /** 由 EventRouter 启动扫描时调用，运行期只读；业务代码不应调用 */
    void setMessageType(Class<?> messageType);
}

/** 可选基类：反序列化 + 每操作的「前置判断 + 业务动作」，非强制继承 */
public abstract class OpTypeProcessor<T> implements BinlogProcessor {

    /** 由 EventRouter 从 @BinlogTable#messageType 一次性注入 */
    private Class<T> messageType;

    @Override
    @SuppressWarnings("unchecked")
    public final void setMessageType(Class<?> messageType) {
        this.messageType = (Class<T>) messageType;
    }

    @Override
    public final void handle(BinlogEvent<JSONObject> raw) {
        // 一次反序列化：JSONObject → 业务 BO，之后子类看到的都是强类型 event
        BinlogEvent<T> event = retype(raw);
        switch (event.getOpType()) {
            case INSERT: if (acceptInsert(event)) onInsert(event); break;
            case UPDATE: if (acceptUpdate(event)) onUpdate(event); break;
            case DELETE: if (acceptDelete(event)) onDelete(event); break;
            default: /* ignore */
        }
    }

    private BinlogEvent<T> retype(BinlogEvent<JSONObject> raw) {
        return BinlogEvent.<T>builder()
                .table(raw.getTable())
                .opType(raw.getOpType())
                .ts(raw.getTs())
                .primaryKey(raw.getPrimaryKey())
                .before(raw.getBefore() == null ? null : raw.getBefore().toJavaObject(messageType))
                .after(raw.getAfter()  == null ? null : raw.getAfter().toJavaObject(messageType))
                .build();
    }

    // —— 每个操作独立的「要不要处理」判断，默认全放行，子类按表覆写 ——
    protected boolean acceptInsert(BinlogEvent<T> event) { return true; }
    protected boolean acceptUpdate(BinlogEvent<T> event) { return true; }
    protected boolean acceptDelete(BinlogEvent<T> event) { return true; }

    // —— 每个操作的业务动作，默认空实现，子类按需覆写 ——
    protected void onInsert(BinlogEvent<T> event) {}
    protected void onUpdate(BinlogEvent<T> event) {}
    protected void onDelete(BinlogEvent<T> event) {}
}
```

**注意 Router → 基类的这条注入链**：`@BinlogTable#messageType` 只在启动扫描时被读一次，Router 立刻通过 `setMessageType` 把 `Class` 塞进基类字段；运行期基类只用这个字段做反序列化，不再重复反射注解——这正是刚才 Pipeline 里没有 `retype` 的原因，Router 已经在启动期把"用什么类型"的知识送到了目的地。

这样一来，一张表的"过滤 + 处理"在自己的类里就是一张清清楚楚的表格：**哪个操作、满足什么条件、做什么**，增删改互不牵连，也不会污染别的表。而且泛型 `T` 就是这张表的 BO，处理器体内是**编译期强类型**——字段拼错编译不过，比 `after.getString("xxx")` 那种运行期字符串安全得多：

```java
@BinlogTable(value = "order_status", messageType = Order.class)
@Component
public class OrderStatusProcessor extends OpTypeProcessor<Order> {

    @Resource
    private OrderSyncService orderSyncService;

    /** 新增：只同步目标业务类型的订单 */
    @Override
    protected boolean acceptInsert(BinlogEvent<Order> event) {
        return isTargetBiz(event.getAfter());
    }

    @Override
    protected void onInsert(BinlogEvent<Order> event) {
        orderSyncService.sync(event.getAfter());
    }

    /** 更新：目标类型 且 状态确实发生变化才处理（避免无意义写下游） */
    @Override
    protected boolean acceptUpdate(BinlogEvent<Order> event) {
        return isTargetBiz(event.getAfter()) && statusChanged(event);
    }

    @Override
    protected void onUpdate(BinlogEvent<Order> event) {
        orderSyncService.sync(event.getAfter());
    }

    // 未覆写 acceptDelete → 默认放行；也可覆写为 return false 表示本表不处理删除
    @Override
    protected void onDelete(BinlogEvent<Order> event) {
        orderSyncService.remove(event.getPrimaryKey());
    }

    private boolean isTargetBiz(Order after) {
        return "NORMAL".equals(after.getBizType());
    }

    private boolean statusChanged(BinlogEvent<Order> event) {
        return event.getBefore() != null
                && !Objects.equals(event.getBefore().getStatus(), event.getAfter().getStatus());
    }
}
```

**过滤的两层归属，到此彻底分清**：

| 过滤类型 | 特征 | 归属 | 例子 |
| -------- | ---- | ---- | ---- |
| 通用过滤 | 与具体表、具体业务无关，所有表一个样 | `EventFilter` 责任链，一次编写全表复用 | DDL、开关、幂等防乱序 |
| 表级业务过滤 | 因表而异 | 该表 `Processor` 内 | 只同步某 `biz_type` |
| 操作级业务过滤 | 同一表因增/删/改而异 | 该表 `Processor` 的 `acceptInsert/Update/Delete` | 更新时字段无变化则跳过、本表不处理删除 |

> 换句话说：**共性沉到责任链，个性收在处理器，且个性还能细到每个操作**。责任链永远保持精简，处理器则完整拥有自己那张表的全部业务判断——这正是"组合"给到的自由度：通用能力可插拔复用，业务规则各自内聚。

## 4、整体调用时序

```
消息队列
   │  批量原始报文
   ▼
BinlogConsumer.onMessages()          最外层兜底
   │  逐条
   ▼
BinlogPipeline.accept(raw)           编排者，唯一主流程
   │
   ├─► FormatNormalizer.normalize()  ① 原始报文 → BinlogEvent
   │
   ├─► EventRouter.route(table)      ② 注解路由 → 目标 Processor
   │
   ├─► EventFilter 责任链            ③ 通用过滤：DDL → 开关 → 幂等，任一拒绝即终止
   │
   └─► BinlogProcessor.handle()      ④ 先按操作做表级业务过滤(acceptXxx)，通过再写业务(onXxx)
```

四步各司其职、线性可读，没有任何一层需要「向上追继承链」才能看懂全貌。过滤被清晰地劈成两层——**通用过滤在第③步的责任链，表级/操作级业务过滤在第④步的处理器内部**，责任链因此永远精简，业务规则则各自内聚、互不干扰。

---

# 五、生产落地的关键保障

Binlog 消息**天生可能重复、可能乱序**，加上下游可能临时故障，以下几件事是正确运行的必要条件。

## 1、幂等与顺序保证

这正是 `IdempotentFilter` 存在的意义——它作为责任链的一环，对**所有表统一生效**：

- **幂等**：以 `表名 + 主键` 为键记录最近处理时间戳，重复消息被拦截。
- **顺序**：比较本次 `event.getTs()` 与已记录时间戳，**旧消息直接丢弃**（见第四章 `IdempotentFilter`）。
- 下游若能保证幂等（如 `INSERT ... ON DUPLICATE KEY UPDATE`），则是双重保险。

因为它是独立 Filter，业务处理器完全不用重复写这段逻辑。

## 2、异常与重试分级

不要所有异常都无脑重试。可在 `BinlogConsumer` / `BinlogPipeline` 外层统一按异常类型分级：

| 异常类型              | 处理策略                     |
| --------------------- | ---------------------------- |
| 归一化 / 反序列化失败 | 记录告警 + 丢弃（重试无意义） |
| 下游临时故障          | 向上抛出 → 消息队列重试        |
| 重试超限              | 投递到**死信队列**人工介入    |

分级逻辑集中一处，各构件只管抛出语义清晰的异常，不各自决定重试策略。

## 3、可观测性与资源隔离

- **日志**：在 Pipeline 统一打印 `table + opType + 主键 + ts`，全链路一眼可查。
- **监控**：采集消费 TPS、失败率、消费延迟（`now - ts`）并接入告警。
- **线程池隔离**：按表路由后，不同表可投递到**独立线程池**处理，避免慢表拖垮快表。

## 4、接入新表的标准步骤（SOP）

1. 定义该表对应的 **BO 对象**（字段与 `after` 一致）。
2. 新增 **处理器**：`@BinlogTable(value = "表名", messageType = XxxBO.class)` + 继承 `OpTypeProcessor<XxxBO>`，实现 `onInsert/onUpdate/onDelete`，方法体内直接用强类型 `event.getAfter()`，**无需自行反序列化**。
3. 如是新采集格式，补充一个 **`FormatNormalizer`** 实现。
4. 如该表/某个操作有业务过滤规则，在处理器里覆写对应的 `acceptInsert/acceptUpdate/acceptDelete` 即可；配置该表 **Topic 订阅**与**开关**。（只有当过滤规则真的对所有表通用时，才考虑新增 `EventFilter`）

全程**不改任何已有代码**，只做"加法"——这正是组合式设计的红利。

---

# 六、小结

| 环节   | 构件                           | 职责                          |
| ------ | ------------------------------ | ----------------------------- |
| 来源   | MySQL Binlog + 采集组件        | 捕获变更并投递消息队列        |
| 归一化 | `FormatNormalizer`             | 外部报文 → 标准 `BinlogEvent` |
| 编排   | `BinlogPipeline`               | 组合四个构件，描述主流程      |
| 路由   | `EventRouter` + `@BinlogTable` | 显式注解路由，启动即校验      |
| 通用过滤 | `EventFilter` 责任链           | DDL / 开关 / 幂等，与表无关，全表复用 |
| 业务过滤+处理 | `BinlogProcessor`         | 表级/操作级过滤(acceptXxx) + 业务动作(onXxx) |

核心思想：**用组合代替继承**，把「接入」拆成一组职责单一、可独立测试、可自由拼装的构件。过滤被清晰劈成两层——**通用能力（DDL/开关/幂等）沉淀为可复用的 `EventFilter` 责任链，因表/因操作而异的业务过滤则内聚在各自处理器的 `acceptXxx` 里**，责任链永远精简，业务规则各自独立；接入新表永远是"加一个构件"，而非"改一条继承链"。