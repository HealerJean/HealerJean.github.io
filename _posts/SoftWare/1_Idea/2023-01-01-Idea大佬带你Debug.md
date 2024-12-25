---
title: Idea大佬带你Debug
date: 2023-01-01 00:00:00
tags: 
- SoftWare
category: 
- SoftWare
description: Idea大佬带你Debug
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、基本操作

![image-20230610141753350](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610141753350.png)



| 操作                                 | 说明         | 快捷键           | 解释                                                         |
| ------------------------------------ | ------------ | ---------------- | ------------------------------------------------------------ |
| **1>`Show Execution Point`：**       | 限制执行点   | **Alt + F10**    | 当我们查看其它方法而忘记了断点执行的位置时，通过该功能可以快速定位到断点所在的位置。 |
| **2>.Step Over ：**                  | 步过         | F8               | 执行该方法的下一步（如果该行代码是方法，也不会进入到方法的内部）。 |
| **3>.Step Into ：**                  | 步入         | F7               | 如果当前行是方法调用，则进入到该方法的内部，否则跳转到下一行代码 |
| **4>.Force Step Into ：**            | 强制步入     | Alt + Shift + F7 | 强制进入到具体方法的内部继续执行                             |
| **5>.Step Out ：**                   | 步出         | **Shift + F8**   |                                                              |
| **6>.Drop Frame ：**                 | 丢帧         |                  | **丢弃掉当前断点所在方法的栈帧，回退到上一层的方法**，但是数据的状态若已改变了则是无法回退到之前的数据状态的。 |
| **7>.Run to Cursor ：**              | 运行到光标处 | **Alt + F9**     | 运行到光标的所在位置。如果光标之前还有断点，则运行到该断点；如果该光标不在运行路径上，则运行到下一个断点或直接结束。 |
| **8>.Evaluate Expression ：**        | 执行表达式   | Alt + F8         | 可用于执行一段我们实时写的代码。另当我们在测试时，发现某段代码逻辑很难有符合条件的数据时，可以通过该功能直接修改数据，来加快我们的测试 |
| **9>.Trace Current Stream Chain ：** | Stream调试   |                  | 当我们暂停在 `Stream` 的代码行时，可以将 `Stream` 的整个处理流程以图形化界面形式展示,可看到每个步处理前后的数据，便于定位排查是哪一步出了问题。`Stream` 调试 |



# 二、断点类型



| 类型                                    | 图标     | 作用                                                         |
| --------------------------------------- | -------- | ------------------------------------------------------------ |
| **行断点（Line Breakpoints）**          | 红色圆形 | 最常用类型。运行时，在断点所在行进行暂停。                   |
| **方法断点（Method Breakpoints）**      | 红色菱形 | 在方法入口和出口都会自动暂停。入口处暂停便于让我们从头调试整个方法，出口处暂停便于让我们看到方法执行完时，方法体内各个变量数据状态现况。有**时候我们的一个接口会存在很多实现类，我们短时间内难以分析究竟是运行到了哪个实现类中**，这个时候就可以使用方法断点，我们将断点打在接口方法上，运行到该方法时，会自动跳到实际执行的实现类，无需通过上下文环境去分析是哪个实现类。 |
| **字段断点（Field Watchpoints）：**     | 红色眼睛 | 在具体字段发生变更（默认）或者被访问（需要额外设置）时暂停。 |
| **异常断点（Exception Breakpoints）：** | 红色闪电 | 可以在抛出异常的地方进行暂停。异常断点不是在具体代码行上加断点，而是在断点详情页中直接添加，当运行时，如果抛出所添加的异常，则会自动暂停在抛出异常的代码处。 |



# 三、场景

## **1：如何做到不用一行行 `debug` 而能高效定位到属性变量被访问或被修改**

**答案：应用断点类型：字段断点（Field Watchpoints）   **

当我们想知道某个属性在什么时候被`修改`，从入口处开始调试太麻烦，我们可以直接在字段上打上字段断点，这样字段被修改的时候就会自动暂停；如果我们想在字段被`访问`时也暂停，则可以右键字段断点，将【`Field` `access`】勾选上即可。

![image-20230610144246848](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610144246848.png)

## **2：错过了断点，如何不必重新请求或重启进而可以继续调试此断点**    

**答案：应用断点功能：丢帧（Drop Frame）**      

从 `A` 方法进入到 `B` 方法后，当我们快执行完 `B` 方法时，发现某个重要流程被我们跳过了，想再看一下，则此时就可以通过`Drop Frame `先回退到 `A` 方法，然后再次重新进入 `B` 方法。

![image-20230610144259569](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610144259569.png)



## **3：如何知道当前具体执行是哪个实现（接口/抽象方法实现太多）**     

答案：应用断点类型：方法断点（`Method` `Breakpoints`）     

时而一个接口/抽象方法会存在很多实现，阅读源码时难以得知究竟是会运行到了具体哪个实现中，此时就可以使用方法断点，我们将断点打在接口方法上，运行到该方法时，会自动跳到实际执行的实现类方法上，而无需通过上下文环境去一点点分析推敲会执行到哪个实现类上。

![image-20230610144408682](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610144408682.png)



## **4：如何设置条件断点，进而不需一层层 `debug`，可直达断点处？**    

**答案：**应用断点功能：断点条件（`Condition`）    

当断点所在的地方执行次数过多时，避免浪费时间层层 `debug`。例如在遍历List当字符串等于abc时存在问题，我们想跳过其它字符串，则可以设置此断点条件 `str.equals("abc")` 表达式，来达到只在我们关注的abc字符串执行时才暂停下来，其它一路绿灯。

![image-20230610144500266](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610144500266.png)





## 5：如何快速精确定位到抛出具体异常的代码行？

**答案：**应用断点类型：异常断点（Exception Breakpoints）     

当想让在运行时，某个具体异常如MyException只要发生则暂停下来，则就可以使用异常断点功能。当有此异常发生时，则就可以暂停在发生代码行上。



![image-20230610144600482](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610144600482.png)

## 6：**如何伪造抛出指定异常便于调试？**

**答案：**应用断点功能：主动抛异常（Throw Exception）

在开发中，常需要验证程序对异常的处理逻辑是否正确，这个时候就需要我们在程序运行时主动抛出异常才会触发相关逻辑来验证，比如验证：事务是否会回滚、Web层的统一异常处理等等。你是如何做的，来支持此种调试呢？     

简单粗暴地在代码中造异常吗？是可以完成目标。但不够优雅，且太麻烦了，因为测试好还要记得删掉它，若忘记了，那就是直接写了个Bug^_^尤其远程环境Debug时更有用——避免了来回改代码部署重启。

![image-20230610144909369](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610144909369.png)



## 7：**多线程情况下如何调试**

**答案：**应用断点功能：多线程调试（Suspend）     

`Suspend` 有两种模式：   

⬤ `All`（暂停全部线程，只能 `Debug` 第一个暂停线程）    

⬤  `Thread`（暂停进入断点的线程，并不影响其它线程正常执行。所有进入此断点的线程会依次进行 `Debug`）。



## 8：`Stream `该如何调试？

**答案：**应用断点功能：`Stream` 调试（Trace Current Stream Chain）    

合理使用 `Stream` 会让代码更加简洁，但是现在存在大量滥用 `Stream` 情况，`Stream` 本身就比较抽象，大量滥用会使得 `Stream`代码难以理解和调试。通过该功能便于直观地看到每步骤处理前后的数据情况，利于快速定位排查是哪一步出了问题。      



![image-20230610145137862](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20230610145137862.png)

## 9：显示变量名

![image-20200915170738498](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20200915170738498.png)







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
		id: 'WqcrLDyfsMXhESB9',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



