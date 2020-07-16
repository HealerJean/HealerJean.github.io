---
title: Mysql中解释Explain详解
date: 2019-05-08 03:33:00
tags: 
- Database
category: 
- Database
description: Mysql中解释Explain详解
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)           



> 数据准备   
>
> **mysql优化器在数据量不同的情况下，也会到结果产生影响**    



```sql
create table `user`
(
    `id`    bigint(20) unsigned not null auto_increment,
    `name`  varchar(64)         default null,
    `age`   bigint(20) unsigned default '0',
    `param` varchar(32)         default null,
    `a`     int(11)             default '0',
    `b`     int(11)             default '0',
    `c`     int(11)             default '0',
    primary key (`id`),
    key `idx_a_b_c_d` (`a`, `b`, `c`),
    key `idx_age` (`age`),
    key `idx_name` (`name`)
);



create table `order_info`
(
    `id`          bigint(16) unsigned not null auto_increment,
    `ref_user_id` bigint(20) unsigned not null,
    `serial_no`   varchar(32)         not null,
    primary key (`id`),
    key `uk_user_id` (`ref_user_id`)
);




INSERT INTO hlj_sql.user (id, name, age, param, a, b, c) VALUES (1, 'xiaoming', 11, 'a', 1, 2, 3);
INSERT INTO hlj_sql.user (id, name, age, param, a, b, c) VALUES (2, 'xiaohong', 23, 'b', 21, 21, 21);
INSERT INTO hlj_sql.user (id, name, age, param, a, b, c) VALUES (3, 'liuqiangdong', 45, 'c', 56, 23, 23);
INSERT INTO hlj_sql.user (id, name, age, param, a, b, c) VALUES (4, 'mayun', 123, 'd', 45, 12, 3);
INSERT INTO hlj_sql.user (id, name, age, param, a, b, c) VALUES (5, 'leijun', 5, 'e', 12, 322, 1);


INSERT INTO hlj_sql.order_info (id, ref_user_id, serial_no) VALUES (1, 1, '2');
INSERT INTO hlj_sql.order_info (id, ref_user_id, serial_no) VALUES (2, 2, '3');
INSERT INTO hlj_sql.order_info (id, ref_user_id, serial_no) VALUES (3, 3, '5');
INSERT INTO hlj_sql.order_info (id, ref_user_id, serial_no) VALUES (4, 2, '3');
INSERT INTO hlj_sql.order_info (id, ref_user_id, serial_no) VALUES (5, 4, '3');
INSERT INTO hlj_sql.order_info (id, ref_user_id, serial_no) VALUES (6, 5, '6');
INSERT INTO hlj_sql.order_info (id, ref_user_id, serial_no) VALUES (7, 4, '3');
```






| 参数           | 含义 |
| ------------- | ---- |
| id            | 查询的标识符 |
| select_type   | SELECT 查询的类型.                                         |
| table         | 查询的是哪个表 |
| partitions    | 匹配的分区 |
| type          | 判断是什么扫描查询 比如：ALL,Index，Rank                   |
| possible_keys | 可能选用的索引 |
| key | 确切使用到的索引                                 |
| key_len       | 索引长度（**通过观察这个可以判断联合索引使用了几列，很有用**） |
| ref           | 哪个字段或常数与 key 一起被使用 |
| rows | 显示此查询一共扫描了多少行. 这个是一个估计值. |
| filtered | 表示此查询条件所过滤的数据的百分比 |
| extra | 额外的信息 |



# 1、`select_type`  

| select_type     | 说明           |
| --------------- | -------------- |
| `SIMPLE`        | 简单查询       |
| `UNION`         | 联合查询       |
| `SUBQUERY`      | 子查询         |
| `UNION RESULT ` | 联合查询的结果 |
| `PRIMARY`       | 最外层查询     |



## 1.1、`SIMPLE` 简单查询

> ##### **解释：此查询不包含 UNION 查询或子查询**



```sql
explain  select * from user ;
```

| id   | select\_type | table | partitions | type | possible\_keys | key  | key\_len | ref  | rows | filtered | Extra |
| :--- | :----------- | :---- | :--------- | :--- | :------------- | :--- | :------- | :--- | :--- | :------- | :---- |
| 1    | SIMPLE       | user  | NULL       | ALL  | NULL           | NULL | NULL     | NULL | 5    | 100      | NULL  |



## 1.2、`UNION` 联合查询  

> ##### **解释：表示此查询是 UNION 的第二或随后的查询**  



```sql
explain  select * from user union  select * from user ;

id = 1  PRIMARY 外层查询    
id = 2  UNION 联合查询
id = 3  UNION RESULT 很明显为联合查询的结果
```

| id   | select\_type | table            | partitions | type | possible\_keys | key  | key\_len | ref  | rows | filtered | Extra           |
| :--- | :----------- | :--------------- | :--------- | :--- | :------------- | :--- | :------- | :--- | :--- | :------- | :-------------- |
| 1    | PRIMARY      | user             | NULL       | ALL  | NULL           | NULL | NULL     | NULL | 5    | 100      | NULL            |
| 2    | UNION        | user             | NULL       | ALL  | NULL           | NULL | NULL     | NULL | 5    | 100      | NULL            |
| NULL | UNION RESULT | &lt;union1,2&gt; | NULL       | ALL  | NULL           | NULL | NULL     | NULL | NULL | NULL     | Using temporary |





## 1.3、`SUBQUERY` 子查询

```sql
explain
select *
from order_info o
where id > (select b.id from user b where b.id = 1); 
        
        
1、第一个 select 为 PRIMARY   最外层查询      
2、第二个 select 为 SUBQUERY  子查询     
```

| id   | select\_type | table | partitions | type  | possible\_keys | key     | key\_len | ref   | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :---- | :------------- | :------ | :------- | :---- | :--- | :------- | :---------- |
| 1    | PRIMARY      | o     | NULL       | ALL   | PRIMARY        | NULL    | NULL     | NULL  | 7    | 85.71    | Using where |
| 2    | SUBQUERY     | b     | NULL       | const | PRIMARY        | PRIMARY | 8        | const | 1    | 100      | Using index |



## 1.4、`UNION RESULT` 联合查询的结果

> **解释：在 `1.2` 中介绍过了**

## 1.5、`PRIMARY`  最外层查询

> **解释：在 `1.2 `和 `1.3`中介绍过** 



# 2、type

>   **解释：它提供了判断查询是否高效的重要依据依据. 通过 `type` 字段, 我们判断此次查询是 `全表扫描` 还是 `索引扫描` 等，要和Extra同时观察会更好**    
>
> 性能：**ALL < index < range ~ index_merge < ref < eq_ref < const < system**



## 2.1、`ALL` 

> **解释：全表扫描**  

```sql
explain  select * from user ;
```

| id   | select\_type | table | partitions | type | possible\_keys | key  | key\_len | ref  | rows | filtered | Extra |
| :--- | :----------- | :---- | :--------- | :--- | :------------- | :--- | :------- | :--- | :--- | :------- | :---- |
| 1    | SIMPLE       | user  | NULL       | ALL  | NULL           | NULL | NULL     | NULL | 5    | 100      | NULL  |



## 2.2、`index`

>  **解释：表示全索引扫描 （索引覆盖）和ALL类似**   
>
>  1、`index`: 表示全索引扫描， 和 ALL 类型类似, 只不过 ALL 类型是全表扫描, **而 index 类型则仅仅扫描所有的索引, 而不扫描数据**.       其实就是讲 查询条件 写上索引的字段    
>
>  2、`index` 类型通常出现在: **所要查询的数据直接在索引树中就可以获取到, 而不需要扫描数据.** **当是这种情况时, Extra 字段 会显示 `Using index`**         
>
>  3、`index` 类型的查询虽然不是全表扫描, 但是它扫描了所有的索引, 因此比 ALL 类型的稍快.



```sql
explain   select name from user ;
```

| id   | select\_type | table | partitions | type  | possible\_keys | key       | key\_len | ref  | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :---- | :------------- | :-------- | :------- | :--- | :--- | :------- | :---------- |
| 1    | SIMPLE       | user  | NULL       | index | NULL           | idx\_name | 195      | NULL | 5    | 100      | Using index |





## 2.3、`range`

> **解释：索引范围内查询**   ，通过索引字段范围获取表中部分数据记录. 这个类型通常出现在 =, <>, >, >=, <, <=, IS NULL, <=>, BETWEEN, IN() 操作中     



```sql
explain  select * from user  where  id > 2;
```

| id   | select\_type | table | partitions | type  | possible\_keys | key     | key\_len | ref  | rows | filtered | Extra                 |
| :--- | :----------- | :---- | :--------- | :---- | :------------- | :------ | :------- | :--- | :--- | :------- | :-------------------- |
| 1    | SIMPLE       | user  | NULL       | range | PRIMARY        | PRIMARY | 8        | NULL | 3    | 100      | Using index condition |





## 2.4、`INDEX_MERGE`

> **解释：合并索引，使用多个单列索引搜索**

```sql
explain   select id from user  where  id = 2 or  name = 'xiaoming';
```

| id   | select\_type | table | partitions | type         | possible\_keys    | key               | key\_len | ref  | rows | filtered | Extra                                         |
| :--- | :----------- | :---- | :--------- | :----------- | :---------------- | :---------------- | :------- | :--- | :--- | :------- | :-------------------------------------------- |
| 1    | SIMPLE       | user  | NULL       | index\_merge | PRIMARY,idx\_name | PRIMARY,idx\_name | 8,195    | NULL | 2    | 100      | Using union\(PRIMARY,idx\_name\); Using where |





## 2.5、`REF`

>  **解释：根据索引查找一个或多个值**

```sql
explain   select id from user  where  name = 'xiaoming';
```

| id   | select\_type | table | partitions | type | possible\_keys | key       | key\_len | ref   | rows | filtered | Extra |
| :--- | :----------- | :---- | :--------- | :--- | :------------- | :-------- | :------- | :---- | :--- | :------- | :---- |
| 1    | SIMPLE       | user  | NULL       | ref  | idx\_name      | idx\_name | 195      | const | 1    | 100      | NULL  |



## 2.6、`eq_ref `

> **解释：连接join查询时，使用primary key 或 unique类型，其实就是说索引唯一的关联查询**

```sql
explain
select *
from order_info o
         join user u on u.id = o.ref_user_id;
```

| id   | select\_type | table | partitions | type    | possible\_keys | key     | key\_len | ref                      | rows | filtered | Extra |
| :--- | :----------- | :---- | :--------- | :------ | :------------- | :------ | :------- | :----------------------- | :--- | :------- | :---- |
| 1    | SIMPLE       | o     | NULL       | ALL     | uk\_user\_id   | NULL    | NULL     | NULL                     | 7    | 100      | NULL  |
| 1    | SIMPLE       | u     | NULL       | eq\_ref | PRIMARY        | PRIMARY | 8        | hlj\_sql.o.ref\_user\_id | 1    | 100      | NULL  |





## 2.7、`const`

> 解释：针对主键或唯一索引的等值查询扫描，只有一行

```sql
explain   select id from user  where  id = 1;
```

| id   | select\_type | table | partitions | type  | possible\_keys | key     | key\_len | ref   | rows | filtered | Extra       |
| :--- | :----------- | :---- | :--------- | :---- | :------------- | :------ | :------- | :---- | :--- | :------- | :---------- |
| 1    | SIMPLE       | user  | NULL       | const | PRIMARY        | PRIMARY | 8        | const | 1    | 100      | Using index |



## 2.8、`system` 

> 解释：表中仅仅有一条数据，这个是特殊的const查询
>



# 3、`possible_keys`

>  可能用到的索引，看 4



# 4、`key` 

> **解释：表示 MySQL 在查询时, 真实使用到的索引，   **
>
> **即使有些索引在 `possible_keys` 中出现, 但是并不表示此索引会真正地被 MySQL 使用到. MySQL 在查询时具体使用了哪些索引, 由 `key` 字段决定..**     

```sql
下面这个条件中使用了 索引id 和 联合索引 ref_user_id   
实际上我们只使用了 索引id 进行查询，所以 key是id ，possible_keys 是id和 ref_user_id

explain   select id from order_info  where  id = 1 and ref_user_id = 1;
```

| id   | select\_type | table       | partitions | type  | possible\_keys       | key     | key\_len | ref   | rows | filtered | Extra |
| :--- | :----------- | :---------- | :--------- | :---- | :------------------- | :------ | :------- | :---- | :--- | :------- | :---- |
| 1    | SIMPLE       | order\_info | NULL       | const | PRIMARY,uk\_user\_id | PRIMARY | 8        | const | 1    | 100      | NULL  |



# 5、`key_len`

> **解释： 使用索引字节长度，这个字段可以评估联合索引是否完全被使用**

## 5.1、字符串

| 类型       | 索引长度                            |
| ---------- | ----------------------------------- |
| char(n)    | n                                   |
| varchar(n) | 如果是 utf8，3 n + 2                |
| varchar(n) | 如果是 utf8mb4 ，则是 4 n + 2 字节. |



## 5.2、数值类型:

| 类型      | 索引长度 |
| --------- | -------- |
| TINYINT   | 1        |
| SMALLINT  | 2        |
| MEDIUMINT | 3        |
| INT       | 4        |
| BIGINT    | 8        |
| float     | 4        |
| double    | 8        |
| decimal   |          |



## 5.3、时间类型

| 类型      | 长度 |
| --------- | ---- |
| year      | 1    |
| date      | 4    |
| time      | 3    |
| datetime  | 8    |
| timestamp | 4    |



# 6、`rows`

> 显示此查询一共扫描了多少行. 这个是一个估计值.



# 7、Extra 

> ##### 解释 ： 额外信息，优化器会在索引存在的情况下，通过符合 RANGE 范围的条数和总数的比例来选择是使用索引还是进行全表遍历，   
>
> ##### 具体案例例具体分析，不要把这里想复杂了，就是一个额外的信息而已



**名词解释：**

**回表：表示即使使用索引筛选了，但是查询的字段不是全部都是索引列**      

| Extra                 | 说明                          |
| --------------------- | ----------------------------- |
| NULL                  | 查询的不全都是索引            |
| using index           | 使用覆盖索引的时候就会出现    |
| using index condition | 查询条件是索引的一个范围      |
| using where           | 查询条件包含普通的条件        |
| Using filesort        | 排序 不能通过索引达到排序效果 |



`using index` > `using where` >  `using index condition` ，如果不需要回表查询数据，效率上应该比较快的











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
		id: 'dvUC7YOy8tcWS39r',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

