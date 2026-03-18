---
title: Java常用类库
date: 2020-01-01 03:33:00
tags: 
- Java
category: 
- Java
description: Java常用类库
---



**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          

**JavaUtil包：[http://gitbook.net/java/util/index.html](http://gitbook.net/java/util/index.html)**



# 一、`FastUtil`：高性能 `Java` 集合库

## 1、`FastUtil` 是什么

> **FastUtil** 是由意大利米兰大学（University of Milan）开发的开源高性能集合库，专为处理 **原始类型（`primitive types`）** 的集合和映射而设计。

- 核心目标：**避免装箱、减少内存、提升缓存局部性、加速 CPU 访问**

- `GitHub`: https://github.com/vigna/fastutil

### 1）`Maven`

```xml
<dependency>
    <groupId>it.unimi.dsi</groupId>
    <artifactId>fastutil</artifactId>
    <version>8.5.13</version>
</dependency>
```

### 2）核心设计目标

| 目标           | 说明                                                       |
| -------------- | ---------------------------------------------------------- |
| 消除装箱/拆箱  | 直接操作 `int`、`long` 等原始类型，避免 `Integer` 对象创建 |
| 极致内存效率   | 内存占用比标准集合低 40%～80%，尤其适合大数据场景          |
| 提升缓存局部性 | 连续内存布局 + 无指针跳转 → CPU 缓存命中率高               |
| 加速访问与插入 | 开放寻址 + 位运算索引 → 操作速度提升 2～4 倍               |
| 支持超大集合   | 提供 `BigList`、`BigSet`，支持 >2³¹ 元素（64 位索引）      |



## 2、为什么需要 `FastUtil`？—— `Java` 标准集合的痛点

### 1）泛型不支持基本类型

`Java` 泛型只接受引用类型，所以必须用 `Integer`、`Long` 等包装类：

```java
Map<Integer, Integer> map = new HashMap<>();
map.put(123, 456); // 自动装箱 → Integer.valueOf(123)
```



### 2）自动装箱/拆箱带来严重开销

| 问题           | 描述                                                         |
| -------------- | ------------------------------------------------------------ |
| **GC 压力大**  | 每次操作都可能创建临时对象（如 `Integer.valueOf()`），触发频繁 Minor GC |
| **缓存不友好** | `HashMap` 中每个 `Entry` 是独立对象，分散在堆中，CPU 缓存命中率低 |
| **内存膨胀**   | 一个 `Integer` 对象 ≈ 16～24 字节（含对象头 + mark word），而 `int` 仅需 4 字节 |



### 3）**内存与性能对比**

**结论**：只要 key/value 是原始类型，**FastUtil 是“免费的性能提升”**。

| 场景                        | 数据量            | 内存占用 |
| --------------------------- | ----------------- | -------- |
| `HashMap<Integer, Integer>` | 1 亿条 (int, int) | ≥ 3.2 GB |
| `Int2IntOpenHashMap`        | 1 亿条 (int, int) | ≈ 800 MB |



# 二、关键类型详解（`Map` / `Set` / `List`）

## 1、`Map`： **原始类型映射（核心优势区）**

| Key 类型 | Value 类型 | FastUtil 类名               | 典型使用场景                        |
| -------- | ---------- | --------------------------- | ----------------------------------- |
| `int`    | `int`      | `Int2IntOpenHashMap`        | ID → 计数、状态码、分数             |
| `int`    | `long`     | `Int2LongOpenHashMap`       | 用户ID → 时间戳、金额（分）         |
| `int`    | `double`   | `Int2DoubleOpenHashMap`     | 商品ID → 价格、评分                 |
| `int`    | `Object`   | `Int2ObjectOpenHashMap<V>`  | ID → 实体对象（User, Product 等）   |
| `long`   | `int`      | `Long2IntOpenHashMap`       | 时间戳 → 状态、分类ID               |
| `long`   | `long`     | `Long2LongOpenHashMap`      | 大ID映射（如分布式ID → 另一个ID）   |
| `long`   | `Object`   | `Long2ObjectOpenHashMap<V>` | 订单号（long）→ Order 对象          |
| `int`    | `boolean`  | `Int2BooleanOpenHashMap`    | 权限位（userID → 是否管理员）       |
| `byte`   | `int`      | `Byte2IntOpenHashMap`       | 小范围枚举 → 计数（如 HTTP 状态码） |
| `short`  | `int`      | `Short2IntOpenHashMap`      | 年份、月份等小整数 → 统计值         |



## 2、`Set` ：**原始类型集合**

| 元素类型 | FastUtil 类        | 替代方案           | 优势                    |
| -------- | ------------------ | ------------------ | ----------------------- |
| `int`    | `IntOpenHashSet`   | `HashSet<Integer>` | 内存省 75%，查询快 3 倍 |
| `long`   | `LongOpenHashSet`  | `HashSet<Long>`    | 适合 Snowflake ID 去重  |
| `short`  | `ShortOpenHashSet` | `HashSet<Short>`   | 小整数集合高效存储      |



## 3、`List` 

| 类型              | 底层数组   | 用途                       |
| ----------------- | ---------- | -------------------------- |
| `IntArrayList`    | `int[]`    | 存储大量整数（如 ID 列表） |
| `LongArrayList`   | `long[]`   | 时间戳列表、大 ID 列表     |
| `DoubleArrayList` | `double[]` | 数值计算、指标序列         |



**内存对比（1000 万个元素）**

| 方式                 | 底层结构      | 堆内存   | GC 压力 |
| -------------------- | ------------- | -------- | ------- |
| `ArrayList<Integer>` | `Object[1e7]` | ≥ 160 MB | 高      |
| `IntArrayList`       | `int[1e7]`    | ≈ 40 MB  | 极低    |



# 三、**深度剖析：**`Int2ObjectOpenHashMap<V>`

## 1、是什么？

专用于将 **`int` 键** 映射到 **任意对象值 `V`** 的高性能哈希表。

- **包路径**：`it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap`
- **不支持 `null` key**（会抛 `NullPointerException`）
- **支持 `null` value**



## 2、底层结构

### 1）关键结构

> `FastUtil` 彻底抛弃了 JDK `HashMap` 中的 `Node<K,V>` 链表节点模型，转而采用**两个平行原始数组**：

```java
protected transient int[] key;      // 原始 int 数组
protected transient Object[] value; // 对象引用数组
protected final float f;            // 负载因子
protected int n;                    // 容量（2 的幂）
protected int mask;                 // = n - 1
protected int maxFill;              // 扩容阈值 = n * f

```



### 2）**构造函数逻辑（关键）**

#### a、构造函数介绍

- **默认负载因子**：`0.75f`（与 JDK 一致）
- **初始容量计算**：`HashCommon.arraySize(expected, f)` → 返回 ≥ `expected / f` 的最小 2 的幂
- **索引计算**：`hash & mask`（`mask = capacity - 1`）→ **比 `%` 快 10 倍以上**
- **扩容阈值**：`maxFill = capacity * loadFactor`

```java
public Int2ObjectOpenHashMap(int expected, float f) {
    // 负载因子合法性校验：if (!(f <= 0.0F) && !(f >= 1.0F))
    // 逻辑等价：0 < f < 1（负载因子必须在 0 到 1 之间）。
      if (!(f <= 0.0F) && !(f >= 1.0F)) {
          if (expected < 0) {
              throw new IllegalArgumentException("The expected number of elements must be nonnegative");
          } else {
              this.f = f;
              // 核心中的核心：计算哈希表的初始容量（2 的幂）。
              this.minN = this.n = HashCommon.arraySize(expected, f);
             // 计算索引掩码（比如 n=4096 → mask=4095）
             // 后续用hash & mask替代hash % n计算索引（位运算比取模快 10 倍 +）。
              this.mask = this.n - 1;
             // 计算扩容触发阈值（容量 × 负载因子）。
              this.maxFill = HashCommon.maxFill(this.n, f);
              this.key = new int[this.n + 1];
              this.value = new Object[this.n + 1];
          }
      } else {
          throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
      }
  }
```



#### **b、为什么数组长度是** `n + 1`**？**

**`+1`** 是开放寻址哈希表的经典技巧：

- 用于**简化线性探测的边界检查**。
- 在探测循环中，可以安全地访问 `key[pos]` 而无需每次判断 `pos < n`。
- 最后一个槽位（`index = n`）通常不会被正常数据占用，仅作为探测终止哨兵或 `tombstone` 缓冲区。

 **效果**：减少分支预测失败，提升 CPU 流水线效率。

```java
public Int2ObjectOpenHashMap(final int expected, final float f) {
		if (f <= 0 || f >= 1) 
      throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than 1");
		if (expected < 0) 
      throw new IllegalArgumentException("The expected number of elements must be nonnegative");
		this.f = f;
		minN = n = arraySize(expected, f);
		mask = n - 1;
		maxFill = maxFill(n, f);
		key = new int[n + 1];
		value = (V[])new Object[n + 1];
	}
```



## 3、底层优化原理

### 1）开放寻址

**开放寻址** 是一种解决哈希冲突的策略。它的核心思想是：所有键值对都存储在哈希表本身的底层数组中（而不是像链地址法那样使用链表或树等额外结构）。当发生哈希冲突（即多个键映射到同一个索引）时，算法会在数组中“探测”（`probe`）其他位置，直到找到一个空槽来存放新元素。

- 所有键值对存储在一个连续的数组中。
- 当发生冲突时，通过探测（如线性探测或二次探测）寻找下一个空槽。
- `FastUtil` 默认使用 **线性探测（`linear probing`）**。

| 特性         | 开放寻址（如 `Int2ObjectOpenHashMap`） | 链地址法（如 `java.util.HashMap`） |
| ------------ | -------------------------------------- | ---------------------------------- |
| 存储结构     | 所有元素存在一个连续数组中             | 数组 + 链表/红黑树（桶）           |
| 内存局部性   | 更好（缓存友好）                       | 较差（指针跳转）                   |
| 删除操作     | 复杂（需标记“已删除”）                 | 简单（直接移除节点）               |
| 负载因子上限 | 通常 < 1（如 0.75）                    | 可 > 1（理论上无硬上限）           |
| 空间开销     | 低（无额外指针）                       | 高（每个节点有 next 指针等）       |



### 2）**高效哈希函数与索引计算**

- 位混合哈希：`FastUtil` 对原始类型使用**位混合哈希（Bit Spreading）**，减少聚集：

```java
// fastutil.HashCommon.mix(int)
static int mix(final int x) {
    int h = x * 0x9E3779B1;
    return h ^ (h >>> 16);
}
```

- **索引计算：位运算替代取模**

```java
/ JDK: index = hash % capacity
// FastUtil: index = hash & mask   (mask = capacity - 1)
int index = (mix(key) & mask);
```



### 3）无泛型、无装箱

所有 API 直接操作原始类型：

```java
public int put(int key, int value);
public boolean containsKey(int key);
public int get(int key);
```

- **零临时对象** → 几乎无 `GC` 压力
- **方法内联率高** → `JIT` 优化更彻底



### 4）内存布局紧凑

- 没有 `Entry` 对象（不像 `HashMap` 中每个键值对是一个 `Node` 对象）。
- 底层由**两个平行数组**组成：
  - 一个 `int[] key` 数组（存储键）
  - 一个 `Object[] value` 数组（存储值）
- 内存占用远小于 `HashMap<Integer, V>`，尤其在大数据量下优势明显。



### 5）**删除机制详解（`Tombstone`）**

> **注意**：频繁删除 + 插入场景下，建议定期调用 `compact()`

- 删除时不置空，而是标记为“已删除”状态。
- 查找时跳过 `tombstone`，插入时可复用。
- **清理时机**：
  - 扩容（rehash）
  - 显式调用 `trim()` / `compact()`
  - 删除比例过高时自动压缩（v8.5+ 优化）

- **优点**：删除快；
- **缺点**：`tombstone` 累积浪费内存 → 负载因子虚高 → 可能提前扩容。



## **3、开放寻址全流程**

### 1）**插入（`put`）**

1. **计算初始哈希索引**：

   - 哈希函数：对于键 `k`，先计算其哈希值 `h(k)`，然后通过取模得到初始索引：

   ```java
   int h = HashCommon.mix(k);          // 位混合，减少聚集
   int pos = h & mask;                 // mask = capacity - 1
   ```

2. **线性探测找空槽**：

   - **线性探测公式**：探测序列：`(h(k) + i) % table.length`，其中 `i = 0, 1, 2, ...`
   - 如果 `table[index]` 为空 → 直接插入。
   - 如果已被占用（冲突）→ **线性探测**：依次检查 `index+1`, `index+2`, ..., 直到找到空槽。
   - 

   ```java
   while (key[pos] != 0 || value[pos] != null) {
       if (key[pos] == k) {
           // 键已存在 → 更新值
           Object oldValue = value[pos];
           value[pos] = v;
           return oldValue;
       }
       pos = (pos + 1) & mask;         // 线性探测：(h + i) & mask
   }
   ```

3. **插入新键值对**：

   ```java
   key[pos] = k;
   value[pos] = v;
   size++;
   ```

4. **检查是否需要扩容**：
   - 若整个表快满（负载因子过高），则触发**扩容（rehash）**。
   - 若 `size > maxFill`（`maxFill = capacity * loadFactor`），则触发 `rehash()`。



### 2）**查找（**`get(int k)`**）**

**关键点**：一旦遇到 `key[pos] == 0 && value[pos] == null`，立即停止——说明该 `key` 不存在（开放寻址保证连续性）

- 从 `h(k) % len` 开始，按相同探测顺序查找。
- 遇到空槽 → 键不存在（因为插入时会填满所有可能位置）。
- 遇到匹配键 → 返回值

```java
int h = HashCommon.mix(k);
int pos = h & mask;

while (key[pos] != 0 || value[pos] != null) {
    if (key[pos] == k) {
        return (V) value[pos];   // 找到
    }
    pos = (pos + 1) & mask;
}
return defRetValue; // 默认返回 null（可通过 defaultReturnValue() 修改）
```



### 3）**删除（**`remove(int k)`**）—— 墓碑（`Tombstone`）机制**

1. **找到目标位置**（同查找逻辑）
2. **不能简单置空**！否则会**打断后续元素的探测链**。
3. **`FastUtil` 的解决方案**：
   - 将 `key[pos]` 设为 **`0`**
   - 将 `value[pos]` 设为 **`null`**
4. **真正的墓碑标记**（v8.5.21 实现）：
   - `FastUtil` **不显式维护 `tombstone` 标志位数组**（为节省内存）。
   - 而是依赖一个**不变式**：**只要 `size` 已知，且探测过程中遇到 `(0, null)`，就认为是空槽**。
   - 但在删除后，它会**继续向后探测，直到遇到真正空槽**，并将路径上所有元素**前移一位**（称为“向前移位”或 “backward shift”）。

5. **删除后的探测与修复**：**把后面“因冲突被迫后移”的元素逐个往前拉，直到空槽，保证查找路径不断。**
   - 从 `pos + 1` 开始向后扫描，直到遇到 **真正的空槽**（即 `key[i] == 0 && value[i] == null`）：
     - 对每个非空元素 `i`：
       - 如果它“本可以更靠近自己的哈希位置”（即它的理想位置在 `(pos, i]` 范围内），
       - 就把它 **前移到当前空位 `pos`**，
       - 然后更新 `pos = i`（新的空位），
       - 继续往后看。
   - **停止条件**
     - 一旦遇到一个元素，其理想哈希位置 ≤ 当前空位（说明它不属于被删元素引发的探测链）
     - 或遇到真正空槽，就停止。





## 4、**与** `HashMap<Integer, V>` **对比**

> **结论**：**只要无 `null` key 需求，一律用 `Int2ObjectOpenHashMap`**。

### 1）逐项对比

| 维度                    | `Int2ObjectOpenHashMap<V>` | `HashMap<Integer, V>`                         |
| :---------------------- | :------------------------- | :-------------------------------------------- |
| **键存储**              | `int[]`（4 字节/键）       | `Integer` 对象（16～24 字节/键 + 8 字节引用） |
| **值存储**              | `Object[]`（8 字节/引用）  | `Node.value`（8 字节引用 + Node 对象头）      |
| **Entry 开销**          | 无                         | 每个 Entry ≈ 32 字节（Node 对象）             |
| **总内存（1000 万条）** | ～320 MB                   | ～1.1 GB                                      |
| **装箱/拆箱**           | 零                         | 每次操作都发生                                |
| **缓存局部性**          | 极高（连续数组）           | 低（Node 分散在堆中）                         |
| **哈希冲突处理**        | 开放寻址（线性探测）       | 链地址法 → 红黑树（JDK8+）                    |
| **删除复杂度**          | O(簇长度)                  | O(1)（链表） / O(log n)（树）                 |
| **null key**            | 抛异常                     | 支持                                          |
| **迭代器**              | 快速 for 循环（无对象）    | 创建 `HashIterator` 对象                      |



### 2）场景推荐

> 只要 `key` 是原始 `int`，且无 `null` `key` 需求，**默认就用 `Int2ObjectOpenHashMap`** —— 它是“免费的性能提升”。

| 维度                 | 推荐                                 |
| -------------------- | ------------------------------------ |
| **性能 & 内存**      | `Int2ObjectOpenHashMap`              |
| **易用性 & 兼容性**  | `HashMap`                            |
| **生产级高性能系统** | **强烈推荐 `Int2ObjectOpenHashMap`** |





# 四、`ObjectArrayList`

## 1、基本定位

### 1）`ObjectArrayList<E>` 基础

- **全限定类名**：`it.unimi.dsi.fastutil.objects.ObjectArrayList<E>`

- **继承关系**：

  ```java
  public class ObjectArrayList<E> extends AbstractList<E>
      implements RandomAccess, Cloneable, java.io.Serializable
  ```

- **作用**：`FastUtil` 对 `java.util.ArrayList<E>` 的**高性能替代实现**，专用于存储**对象引用（非原始类型）**。



### 2）内部结构

- **懒初始化**：默认构造函数设置 `a = ObjectArrays.DEFAULT_EMPTY_ARRAY`（共享空数组）
- **非线程安全**：所有操作无同步
- **序列化支持**：通过 `writeObject`/`readObject` 自定义序列化

```java
public class ObjectArrayList<E> extends AbstractList<E> {
    protected transient E[] a;   // 底层数组（注意：不是 Object[]，而是 E[]）
    protected int size;          // 当前元素数量
}
```



### 3）为什么需要 `ObjectArrayList`？

说明：虽然不节省内存（仍存对象引用），但相比 `JDK` `ArrayList`，它在以下方面更优：

注意：这些优势主要体现在**高频操作、算法场景或已引入 `FastUtil` 的项目中**。

| 优化点               | 说明                                                         |
| -------------------- | ------------------------------------------------------------ |
| **更激进的扩容策略** | `newCap = old + old/2 + 1`，避免小容量扩容停滞               |
| **高效批量操作**     | 提供 `addElements(...)`, `removeElements(from, to)` 等方法   |
| **内置函数式方法**   | 如 `forEach`, `removeIf`, `select(Predicate)`（`FastUtil` 特有） |
| **统一 API 风格**    | 与 `IntArrayList` 等保持一致，便于混合使用                   |
| **减少临时对象**     | 在特定路径（如 `forEach`）中避免创建 `Iterator`/`Stream`     |



| 集合类型                | 优势                         | 适用场景              |
| ----------------------- | ---------------------------- | --------------------- |
| `ObjectArrayList`       | 高效 API、更好扩容、批量操作 | 对象列表 + 高性能需求 |
| `Int/Long/...ArrayList` | 零装箱、省内存、低 GC        | 数值计算、大数据处理  |



## 2、核心操作

| 操作                       | 实现                       | 时间复杂度     |
| -------------------------- | -------------------------- | -------------- |
| `add(E e)`                 | 尾部追加，必要时扩容       | O(1) amortized |
| `add(int i, E e)`          | 插入，后移元素             | O(n)           |
| `get(int i)`               | 直接索引访问 + 边界检查    | O(1)           |
| `remove(int i)`            | `System.arraycopy` 前移    | O(n)           |
| `removeElements(from, to)` | 批量删除，一次 `arraycopy` | O(n)           |
| `forEach(Consumer)`        | for 循环遍历，无迭代器     | O(n)，低 GC    |

### 1）添加元素（`add(T element)`）

- 直接写入 `items[size++]`
- 若容量不足，触发扩容 → 复制数组
- **无同步**，非线程安全



### 2）随机访问（`get(int index)`）

- 直接返回 `items[index]`
- 有边界检查（`if (index < 0 || index >= size) throw ...`）
- 时间复杂度：**O(1)**



### 3）删除元素（`remove(int index)`）

- 将 `index` 后所有元素前移一位（`System.arraycopy`）
- 时间复杂度：**O(n)**



### 4）函数式操作（如 `select`, `collect`）

- 内部使用**快速循环**（fast enumeration），避免创建中间迭代器或 Stream 对象

- 示例：

  ```
  list.select(x -> x > 10); // 返回新的 ObjectArrayList，过滤高效
  ```

- 比 `JDK` 的 `Stream.filter().collect()` 更少 `GC` 压力，尤其在小数据集上更快



## 3、对比  `ArrayList` 

| 维度           | `ObjectArrayList`                         | `ArrayList`（JDK）                             |
| -------------- | ----------------------------------------- | ---------------------------------------------- |
| **所属库**     | FastUtil（第三方）                        | `java.util`（标准库）                          |
| **底层数组**   | `Object[] a`                              | `transient Object[] elementData`               |
| **默认构造**   | 懒初始化（空数组）                        | 懒初始化（JDK 8~16 会预分配 10，17+ 懒初始化） |
| **扩容公式**   | `old + (old >> 1) + 1`                    | `old + (old >> 1)`                             |
| **特有方法**   | `addElements`, `removeElements`, `select` | 无                                             |
| **迭代性能**   | 使用 `forEach` 等可避免中间对象           | 标准 `iterator()` 会创建对象                   |
| **生态兼容性** | 实现 `List<E>`，可无缝互操作              | 完全兼容所有框架                               |
| **适用场景**   | 高性能计算、游戏引擎、金融系统            | 通用业务开发                                   |

### 1）使用建议

**1）推荐使用 `ObjectArrayList` 的场景**

- 已引入 `FastUtil`，希望统一体验（如同时用 `IntArrayList`）
- 需要频繁调用 `removeElements(from, to)` 等批量操作
- 在性能敏感循环中使用 `forEach` 避免 `GC`
- 对小容量扩容行为有确定性要求（如算法竞赛）

**2）不推荐场景**

- 简单 `CRUD` 业务，无性能瓶颈
- 无法引入第三方依赖
- 强依赖 `JDK` 原生行为（如某些反射框架）



### 2）杀手锏：原始类型特化

> 虽然 `ObjectArrayList` 用于对象，但 `FastUtil` 的**最大优势**在于对基本类型的直接支持。

- **零装箱/拆箱**：直接操作基本类型，不创建 `Integer` 等包装对象
- **内存节省**：`int` 占 4 字节，`Integer` 对象至少 16 字节（含对象头）
- **`GC` 友好**：减少堆对象数量，降低 `GC` 频率和停顿时间

- 实测：存储 1000 万个整数
  - `ArrayList<Integer>`：约占用 `160 MB` + 高频 GC
  - `IntArrayList`：仅占用 40 MB + 几乎无 `GC`

| 基本类型  | 特化列表类         | 底层数组    |
| --------- | ------------------ | ----------- |
| `int`     | `IntArrayList`     | `int[]`     |
| `long`    | `LongArrayList`    | `long[]`    |
| `double`  | `DoubleArrayList`  | `double[]`  |
| `boolean` | `BooleanArrayList` | `boolean[]` |



### 3）`IntArrayList`

> **存储大量基本类型数据时，`IntArrayList` 的内存占用仅为 `ArrayList<Integer>` 的约 20%，节省高达 80% 的堆内存，并极大降低 GC 压力。**

| 方式                 | 底层结构            | 对象数量                         | 堆内存估算 | GC 压力 |
| -------------------- | ------------------- | -------------------------------- | ---------- | ------- |
| `ArrayList<Integer>` | `Object[1_000_000]` | ≈ 1,000,001 个                   | ≥ 160 MB   | 高      |
| `IntArrayList`       | `int[1_000_000]`    | 1 个（只有 `IntArrayList` 自身） | ≈ 40 MB    | 极低    |



## 5、`FQA`

### 1）空间效率：`trim` 的核心价值 

- 行为与 `JDK` 几乎相同：`a = Arrays.copyOf(a, size)`
- **价值**：在 `size << capacity` 时释放内存
- **典型场景**：从大文件读取后过滤，保留少量结果 → 调用 `trimToSize()` 可节省 90%+ 内存

| 操作        | 本质                       | 适用时机              |
| ----------- | -------------------------- | --------------------- |
| **不 trim** | 保留扩容空间，避免未来拷贝 | 列表还会增长          |
| **trim**    | 牺牲一次拷贝，永久节省内存 | 列表已定型 + 长期持有 |



#### a、显著收益：

一个长期驻留的缓存列表（`capacity`=`100万`, `size=1万`）

- 不 `trim`：持续占用 `4MB`
- `trim` 后：仅占 `40KB` → **节省 `99%` 内存，**集合的内部存储空间通常会缩小**，`GC` 压力大幅下降**

| 场景                   | 效果                                                  |
| ---------------------- | ----------------------------------------------------- |
| **减少堆内存占用**     | 直接释放未使用的数组空间                              |
| **降低 `GC` 频率**     | 年轻代压力减小，`Minor GC` 更少                       |
| **减少 `GC` 停顿时间** | `GC` 扫描的对象图更小                                 |
| **避免内存碎片**       | 尤其对 G1、ZGC 等区域化 GC 友好                       |
| **提升系统稳定性**     | 降低 `OOM` 风险，尤其在容器化环境（如 Docker 内存限制 |



#### b、什么时候 `trim`

> `trim` 牺牲一点时间，换大量空间 → **在内存受限或长期持有场景下，整体效率更高**。

| 方面     | `ArrayList.trimToSize()`           | `ObjectArrayList.trimToSize()` |
| -------- | ---------------------------------- | ------------------------------ |
| 拷贝开销 | `Arrays.copyOf(elementData, size)` | `Arrays.copyOf(items, size)`   |
| 时间效率 | 几乎相同                           | 几乎相同                       |
| 空间效率 | 相同                               | 相同                           |



#### c、什么时候不 `trimToSize()`

| 场景                     | 原因                                                |
| ------------------------ | --------------------------------------------------- |
| **列表还会继续增长**     | `trim` 后下次 `add` 又要扩容 + 拷贝，得不偿失       |
| **临时局部变量**         | 方法结束就回收，没必要优化                          |
| **性能极度敏感的循环内** | `Arrays.copyOf` 有 `CPU` 开销，避免在 hot path 调用 |



### 2）扩容策略

-  为什么要 `+1`？考虑 `oldCapacity = 1`：

  - `ArrayList`：`1 + (1>>1) = 1 + 0 = 1` → 容量没变！不够用 → 实际会强制扩到 `minCapacity`（如 2）

  - `ObjectArrayList`：`1 + 0 + 1 = 2` → 直接满足需求

| 特性      | `ArrayList`（`Java`） | `ObjectArrayList`      |
| --------- | --------------------- | ---------------------- |
| 扩容公式  | `old + old/2`         | `old + old/2 + 1`      |
| 容量=1 时 | 可能停滞（1→1）       | 一定增长（1→2）        |
| 设计倾向  | 通用、保守            | 高性能、防边界陷阱     |
| 适用场景  | 一般应用              | 算法、高频操作、小容器 |





# 五、``Object2ObjectOpenHashMap``



## 1、**核心定义与特性**

- **全限定名**：`it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap<K, V>`

- **继承关系**：

  ```java
  public class Object2ObjectOpenHashMap<K, V> extends AbstractObject2ObjectMap<K, V>
      implements java.io.Serializable, Cloneable
  ```

- **关键特性**：

  - **支持 `null` 作为 key 和 value**
  - **非线程安全**
  - **内存紧凑**：相比 `HashMap`，减少约 30%～50% 内存占用（无 Entry 对象）

-  **设计目标**：在保留 `HashMap` 通用性的同时，通过开放寻址和数组扁平化提升性能。



## 2、**底层存储结构**

与 `Int2ObjectOpenHashMap` 类似，它也使用**两个平行数组**：

```java
protected transient K[] key;     // 存储键（引用类型）
protected transient V[] value;   // 存储值（引用类型）
```



## 3、**开放寻址的核心挑战**

这是 `Object2ObjectOpenHashMap` 与 `Int2ObjectOpenHashMap` **最根本的区别**！

- 在 `Int2Object...` 中，`0` 是合法 key，所以用 `(key[i] == 0 && value[i] == null)` 表示空槽。
- 但在 `Object2Object...` 中，**`null` 本身就是合法的 key 或 value**！
  - 这种设计允许 `null` 作为合法 key（如 `put(null, "hello")`），同时能区分“空”和“已删”

因此，`FastUtil` 引入了**显式的“墓碑”（Tombstone）标记**。

```java
private static final Object TOMBSTONE = new Object();
```

**槽位状态判断逻辑**

| `key[i]`       | `value[i]` | 含义                 |
| :------------- | :--------- | :------------------- |
| `== null`      | `== null`  | **空槽（从未使用）** |
| `== TOMBSTONE` | `== null`  | **墓碑（已删除）**   |
| 其他           | 任意       | **有效键值对**       |



## 4、**与** `HashMap<K, V>` **的对比**

| 维度                  | `Object2ObjectOpenHashMap<K, V>` | `HashMap<K, V>`      |
| --------------------- | -------------------------------- | -------------------- |
| Entry 开销            | 无（仅两个数组）                 | 每个 Entry ≈ 32 字节 |
| 内存占用（1000 万条） | ～640 MB                         | ～1.1 GB             |
| 缓存友好性            | 高（连续内存）                   | 低（Node 分散）      |
| null key/value        | 支持                             | 支持                 |
| 删除机制              | Tombstone（O(1)）                | 直接 unlink（O(1)）  |
| 最坏性能              | 探测链长 = O(n)（墓碑堆积）      | O(log n)（红黑树）   |
| 迭代器                | 快速（直接遍历数组）             | 创建 Iterator 对象   |



## 5、**使用陷阱**

### **1）墓碑堆积导致性能雪崩**

- **场景**：频繁删除 + 插入（如 LRU 缓存淘汰）

- **现象**：即使 `size` 很小，`get/put` 依然很慢

- **解决方案**：

  ```java
  // 定期清理墓碑
  map.trim(); // 重建哈希表，移除所有墓碑
  ```



### 2）默认负载因子可能不够用

- FastUtil 默认 `loadFactor = 0.75f`
- 但在高删除率场景下，建议**降低负载因子**（如 `0.6f`）以预留更多空槽缓冲墓碑。



## 6、推荐用法

### 1）维护 `map`

```java
// 预分配 + 自定义负载因子
Object2ObjectOpenHashMap<String, User> cache = 
    new Object2ObjectOpenHashMap<>(1_000_000, 0.6f);

// 定期维护（例如每 10 万次删除后）
if (deletionCount > 100_000) {
    cache.trim();
    deletionCount = 0;
}
```

### 2）**何时选择**

| 场景                         | 推荐               |
| ---------------------------- | ------------------ |
| 需要 `null` key/value        | 是                 |
| 内存敏感（如大数据、嵌入式） | 是                 |
| 读多写少，删除较少           | 是                 |
| 高频删除（如缓存淘汰）       | 需配合 `trim()`    |
| 小数据量（< 1000）           | `JDK HashMap `更快 |



## 7、`FastUtil`、`EC`、`JDK`

| 维度                      | `JDK`                  | `EC`                       | `FastUtil`                         |
| :------------------------ | :--------------------- | :------------------------- | :--------------------------------- |
| 集合类                    | `HashMap`              | `UnifiedMap`               | `Object2ObjectOpenHashMap`         |
| **是否支持 `null` 键/值** | 支持                   | 支持                       | **不支持 `null` `key`**（抛异常）  |
| **内存效率（100万条）**   | 最差（～70 MB）        | 中等（～48 MB）            | **略优**（～44 MB）                |
| **小数据性能（<1k）**     | 最快                   | 中等                       | 略慢                               |
| **大数据性能（>100k）**   | 稳定                   | 良好                       | **依赖哈希质量**（好则快，差则崩） |
| **`GC` 压力**             | 高（百万 `Node` 对象） | 低（仅数组）               | 低（仅数组）                       |
| **API 丰富度**            | 基础                   | 极丰富（函数式、不可变等） | 贫乏（仅基础操作）                 |
| **工程友好性**            | 极高（标准库）         | 高（主流库）               | 中（需处理 null/异常）             |



### 1）小数据性能**（< 1,000 条）**

#### a、**`JDK HashMap`**：最快

-  **JIT 高度优化**：`HashMap` 是 `JDK` 核心类，`HotSpot` 对其 `get/put` 方法做了深度内联（`inlining`）、逃逸分析、分支预测优化。
-  **方法调用开销极低**：`hash(key)` → `(n-1) & hash` → 直接数组访问，逻辑简单。
-  **对象分配成本可忽略**：1k 个 `Node` 对象在 `TLAB`（`Thread Local Allocation Buffer`）中快速分配，无 `GC` 压力。

#### b、`EC UnifiedMap`：中等

-  虽为数组结构，但每次操作需访问 **三个数组**（`key[]`, `value[]`, `states[]`），增加内存访问次数。
- 方法调用链较长（如 `UnifiedMap.get()` → `HashHelper.index()` → 状态检查），**`JIT` 内联难度高于 HashMap**。
-  小容量下 `states[]` 的状态机逻辑反而成为轻微负担。

#### c、**`FastUtil O2O`**

-  同样是双数组，但 **`null/tombstone` 判断逻辑更复杂**：

- 小数据时，**探测循环的边界检查和额外条件判断**无法被 JIT 完全消除。
- `FastUtil` 未像 `JDK` 那样获得 `JVM` 特殊关照，**启动阶段性能劣势明显**。



### 2）大数据性能**（> `100k` 条）**

#### a、**`JDK HashMap`**：**稳定**

- 性能**不随数据量剧烈波动**， 均匀哈希 → O(1)
-  极端冲突 → 自动转红黑树 → O(log n)
- **最坏情况有保障**，适合生产环境“兜底”。
- 缺点：指针跳转破坏缓存局部性，**`L1/L2 Cache Miss` 率高**，吞吐上限低于数组方案。



#### b、**`EC UnifiedMap`**：**良好**

- **纯数组布局**，`key[i]` / `value[i]` 在内存连续，**CPU 缓存命中率高**。
-  `states[]` 提供精确槽位状态，插入/删除可高效复用位置。
- 即使哈希稍差，线性探测在 `L2 Cache` 内仍快于 `JDK` 的指针跳转。
-  **性能曲线平滑**，无突变。



#### c、**`FastUtil O2O`**：**高度依赖哈希质量**

- **优势场景：哈希均衡场景**，建议使用：
  - **极致缓存友好**
    - 只用两个连续数组（`key[]`, `value[]`），CPU 能高效预取；
    - 线性探测路径短（因哈希均衡，冲突少），几乎每次 `get/put` 都在 `L1/L2 Cache` 命中。
  - **无对象分配开销**
    - 不像 JDK 那样为每个 entry 创建 `Node` 对象；
    - 减少 GC 压力，避免 young GC 暂停影响吞吐。
  - **无额外状态数组**
    - 比 EC 的 `UnifiedMap` 少一个 `states[]` 数组，内存访问更少一次。

-  **劣势场景：哈希冲突严重**：
   -  开放寻址退化为**长距离线性探测**；
   -  每次 `get/put` 需遍历数十甚至上百个槽位；
   -  **无红黑树兜底**，时间复杂度直接退化为 **O(n)**；
   -  性能可能比 `JDK` **慢 10 倍以上**（实测：`100k` 冲突 key，`FastUtil` 耗时 5s+，JDK 仅 0.3s）。
   -  建议：确保 `key` 使用高质量哈希（如 `String`、自定义良好 `hashCode`）

- **劣势场景：**高频删除：
  - `TOMBSTONE` 累积 → 探测链变长 → 性能持续下降
  - 建议：控制删除比例 < 10%；或定期调用 `map.trim()`（但代价高）



### 3）使用建议

| 场景                | 推荐策略                                                |
| ------------------- | ------------------------------------------------------- |
| 小数据（<1k）       | 无脑用 `JDK HashMap` —— 更快、更安全、零学习成本        |
| 大数据 + 哈希可控   | `FastUtil O2O` 或 `EC UnifiedMap`（前者略快，后者更稳） |
| 大数据 + 哈希不可控 | 必须用 `JDK HashMap` —— 唯一有最坏情况保障的实现        |



```
                          ┌───────────────────────┐
                          │      JDK HashMap      │
                          │  - 指针跳转           │
                          │  - Entry 对象开销大   │
                          │  - 红黑树兜底         │
                          └──────────┬────────────┘
                                     │ 安全、通用、支持 null
                                     ▼
┌───────────────────────┐  ┌───────────────────────┐
│  EC UnifiedMap        │  │ FastUtil O2O          │
│  - 三数组             │  │  - 双数组             │
│  - states[] 精确管理  │  │  - tombstone 陷阱     │
│  - 支持 null          │  │  - 不支持 null key    │
└──────────┬────────────┘  └──────────┬────────────┘
           │ 平衡之选                  │ 高风险高回报（但回报有限）
           ▼                           ▼
   [推荐用于大多数对象场景]    [仅限受控高性能场景]
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
		id: '5eXSIY8iBFbVW7MR',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



