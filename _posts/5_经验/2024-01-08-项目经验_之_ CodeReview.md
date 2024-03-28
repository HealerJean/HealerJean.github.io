---
title: 项目经验_之_CodeReview
date: 2024-01-08 00:00:00
tags: 
- Experience
category: 
- Experience
description: 项目经验_之_CodeReview
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



**`CodeReview` 是开发过程不可或缺的重要一环，如果将代码发布比作一个工厂的流水线，那么 `CodeReview `就是流水线接近于重点的质检员，他要担负着对产品质量的保障工作，将“缺陷”从众多的“产品”中挑出，反向推动“生产方”改进生产质量**。

**1、改善代码质量：**通过 `CodeReview` 机制，可以让团队其他同学帮忙协助把关代码质量，发现代码中潜在的质量问题，并给出改进建议，从而推动团队整体代码代码质量的提升。     

**2、能够实现知识共享，统一认知**：`CodeReview `过程是知识碰撞的过程，是学习别人的知识体系促进自我成长的过程，通过 `CR` 这样的过程能够将不同成长阶段的成员之间知识水位尽量拉齐，能够有效的提升团队编程的整体水平。

**3、能够及时潜在安全和性能问题等：**通过 `CodeReview` 能够及时发现代码中潜在的安全问题和性能问题，例如：`SQL`注入问题、方案安全漏洞问题、部分业务场景查询实现性能较差等问题。



| 关注点分类     | 关注点细分                                                   | 核心关注点                                                   | 常见问题                                                 |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------------------------------------------- |
| 代码规范与质量 | 命名                                                         | 变量命名、方法命名、类命名、包命名                           | 命名拼写错误、命名与实际含义不符（超出或小于）、用词不准 |
| 注释           | 是否有注释、注释是否合理                                     | 通篇无注释、注释不准确                                       |                                                          |
| 日志打印       | 日志打印级别、日志打印参数、日志格式、异常打印、是否应该打印日志 | 日志打印级别误用、日志参数未打印、日志格式不规范、异常信息未打印、打印日志过多 |                                                          |
| 异常处理       | 异常是否抛出、是否规范的异常编码等                           | 异常该抛出未抛、肆意的使用`RuntimeException`等               |                                                          |
| 逻辑正确       | 业务逻辑是否正常                                             | 空指针问题、逻辑不正确等                                     |                                                          |
| 代码风格一致   | 代码风格与应用整体风格一致                                   | 代码风格不一致（如）                                         |                                                          |
| 代码复杂度     | 圈复杂度                                                     | 大量嵌套if导致非常复杂等                                     |                                                          |
| 架构设计       | 关注分层                                                     | 分层是否合理、是否存在跨层调用                               | 分层混乱、跨层调用                                       |
| 关注扩展性     | 扩展适配                                                     | 硬编码扩展性低                                               |                                                          |
| 业务域划分     | 业务域划分清晰                                               | 业务域划分混乱                                               |                                                          |
| 性能问题       | 慢SQL                                                        | 索引设计、是否存在慢SQL语法                                  | 索引未设计、慢SQL用法如like %xxx语句等                   |
| 缓存设计       | 添加缓存、是否存在缓存击穿问题                               | 该加而未加缓存、缓存击穿问题等                               |                                                          |
| 安全性问题     | 是否存在安全风险                                             | 文件上传验权、越权访问问题                                   | 文件上传未验权、越权访问数据等                           |









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
		id: 'su92d1gVwm68HeBv',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



