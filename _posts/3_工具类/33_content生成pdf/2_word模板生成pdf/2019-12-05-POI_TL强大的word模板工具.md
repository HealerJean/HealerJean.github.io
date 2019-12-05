---
title: POI_TL强大的word模板工具
date: 2019-02-20 03:33:00
tags: 
- Utils
category: 
- Utils
description: POI_TL强大的word模板工具
---

<!-- 

https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/
　　首行缩进

<font  clalss="healerColor" color="red" size="5" >     </font>

<font  clalss="healerSize"  size="5" >     </font>
-->




## 前言

#### [博主github](https://github.com/HealerJean)
#### [博主个人博客http://blog.healerjean.com](http://HealerJean.github.io)    





这个应该是我见过最强大的word模板工具了  



## 1、基本介绍



### 1.1、 根据文件路径、文件、文件流获取`XWPFTemplate`

```java
//文件路径
XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath).render(map);

//文件
File inDocxFile = new File(inDocxFilePath);
XWPFTemplate template = XWPFTemplate.compile(inDocxFile).render(map);

//文件流
InputStream inputStream = new FileInputStream(inDocxFilePath);
XWPFTemplate template = XWPFTemplate.compile(inputStream).render(map);



```



### 1.2、生成到文件路径或者是流

```

//生成到文件路径
template.writeToFile(outDocxFilePath);
template.close();


//生成到流
FileOutputStream outputStream = new FileOutputStream(outDocxFilePath);
template.write(outputStream);
outputStream.flush();

//关闭资源
outputStream.close();
template.close();
```



### 1.3、修改变量 {{var}} 为 ${var}



```java
File inDocxFile = new File(inDocxFilePath);
//ConfigureBuilder builder = Configure.newBuilder();

Configure configure = Configure.newBuilder().buildGramer("${", "}").build();
XWPFTemplate template = XWPFTemplate.compile(inDocxFile, configure).render(map);

```





## 2、模板

### 2.1、文本、对象 `{{var}}`



> **TextRenderData、HyperLinkTextRenderData** 



```java
@Test
public void text() throws Exception {

    String inDocxFilePath = "D:/pdf/text.docx";
    String outDocxFilePath = "D:/pdf/out_text.docx";

    Map<String, Object> map = new HashMap<>();
    // 1、普通字符
    map.put("word", "helloWord");

    //2、配置格式
    Style style = StyleBuilder.newBuilder().buildColor("00FF00").buildBold().build();
    map.put("author", new TextRenderData("HealerJean", style));

    //3、超链接
    map.put("website", new HyperLinkTextRenderData("website.", "http://www.deepoove.com"));

    //制作模板
    XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath).render(map);

    //开始生成新的word
    FileOutputStream outputStream = new FileOutputStream(outDocxFilePath);
    template.write(outputStream);
    outputStream.flush();

    //关闭资源
    outputStream.close();
    template.close();
}

```



![1575546523204](D:\study\HealerJean.github.io\blogImages\1575546523204.png)





![1575546511692](D:\study\HealerJean.github.io\blogImages\1575546511692.png)



#### 解释 



#### 2.1.1、`TextRenderData`



>  HyperLinkTextRenderData继承于TextRenderData，实现了超链接文本的功能。



```java

public class TextRenderData implements RenderData {
    protected Style style;
    protected String text;

    public TextRenderData() {
    }

    public TextRenderData(String text) {
        this.text = text;
    }

    public TextRenderData(String color, String text) {
        this.style = StyleBuilder.newBuilder().buildColor(color).build();
        this.text = text;
    }

    public TextRenderData(String text, Style style) {
        this.style = style;
        this.text = text;
    }
    
}
```



#### 2.1.2、Style



```json
public class Style {
   //颜色
    private String color;
    //字体
    private String fontFamily;
    //字号
    private int fontSize;
    //粗体
    private Boolean isBold;
    //斜体
    private Boolean isItalic;
    //删除线
    private Boolean isStrike;

    public Style() {
    }

    public Style(String color) {
        this.color = color;
    }

    public Style(String fontFamily, int fontSize) {
        this.fontFamily = fontFamily;
        this.fontSize = fontSize;
    }
    
    ......
}
```



### 2.2、图片 `{{@var}}  `

> **PictureRenderData**

```java

@Test
public void image() throws Exception {
    String inDocxFilePath = "D:/pdf/image.docx";
    String outDocxFilePath = "D:/pdf/out_image.docx";
    String imagePath = "D:/pdf/image.png";
    Map<String, Object> map = new HashMap<>();

    // 本地图片
    map.put("localPicture", new PictureRenderData(120, 120, imagePath));

    // 图片流文件
    InputStream inputStream = new FileInputStream(imagePath);
    map.put("localBytePicture", new PictureRenderData(100, 120, ".png", inputStream));

    // 网络图片
    map.put("urlPicture", new PictureRenderData(100, 100, ".png", BytePictureUtils.getUrlBufferedImage("https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg")));

    // java 图片
    BufferedImage bufferImage = ImageIO.read(new FileInputStream(imagePath));
    map.put("bufferImagePicture", new PictureRenderData(100, 120, ".png", bufferImage));


    //如果希望更改语言前后缀为 ${var}
    Configure builder = Configure.newBuilder().buildGramer("${", "}").build();
    XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, builder).render(map);


    //开始生成新的word
    template.writeToFile(outDocxFilePath);
    template.close();
}

```



![1575546340272](D:\study\HealerJean.github.io\blogImages\1575546340272.png)



![1575546365325](D:\study\HealerJean.github.io\blogImages\1575546365325.png)





### 2.3、表格  `{{#var}}`

> **MiniTableRenderData**



```java
@Test
public void table() throws Exception{
    String inDocxFilePath = "D:/pdf/table.docx";
    String outDocxFilePath = "D:/pdf/out_table.docx";
    Map<String, Object> map = new HashMap<>();

    Table table1 = new Table("11", "12", "13");
    Table table2 = new Table("21", "22", "23");
    List<Table> table = new ArrayList<>();
    table.add(table1);
    table.add(table2);

    // RowRenderData header = RowRenderData.build("one", "two", "three");
    //使用样式
    Style style = StyleBuilder.newBuilder().buildColor("00FF00").buildBold().build();
    RowRenderData header = RowRenderData.build(
        new TextRenderData("one", style), 
        new TextRenderData("two"), 
        new TextRenderData("three"));

    List<RowRenderData> tableBody = new ArrayList<>();
    for (Table item : table) {
        RowRenderData row = RowRenderData.build(
            item.getOne(), 
            item.getTwo(), 
            item.getThree());
        
        tableBody.add(row);
    }
    map.put("table", new MiniTableRenderData(header, tableBody));


    Configure builder = Configure.newBuilder().buildGramer("${", "}").build();
    XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, builder).render(map);
    template.writeToFile(outDocxFilePath);
    template.close();


}
```



![1575546781755](D:\study\HealerJean.github.io\blogImages\1575546781755.png)



![1575546861665](D:\study\HealerJean.github.io\blogImages\1575546861665.png)







### 2.4、列表模板   

> **NumbericRenderData**



```java
@Test
    public void list() throws Exception {
        String inDocxFilePath = "D:/pdf/list.docx";
        String outDocxFilePath = "D:/pdf/out_list.docx";
        Map<String, Object> map = new HashMap<>();

        map.put("unorderlist", new NumbericRenderData(
            new ArrayList<TextRenderData>(){{
            add(new TextRenderData("one"));
            add(new TextRenderData("two"));
            add(new TextRenderData("three"));
        }}));
        map.put("orderlist", new NumbericRenderData
                (NumbericRenderData.FMT_DECIMAL, new ArrayList<TextRenderData>(){{
            add(new TextRenderData("one"));
            add(new TextRenderData("two"));
            add(new TextRenderData("three"));
        }}));


        //如果希望更改语言前后缀为 ${var}
        Configure builder = Configure.newBuilder().buildGramer("${", "}").build();
        XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, 
                                                     builder).render(map);
        template.writeToFile(outDocxFilePath);
        template.close();
    }




FMT_DECIMAL //1. 2. 3.
FMT_DECIMAL_PARENTHESES //1) 2) 3)
FMT_BULLET //● ● ●
FMT_LOWER_LETTER //a. b. c.
FMT_LOWER_ROMAN //i ⅱ ⅲ
FMT_UPPER_LETTER //A. B. C.
```



![1575547702474](D:\study\HealerJean.github.io\blogImages\1575547702474.png)



![1575547695686](D:\study\HealerJean.github.io\blogImages\1575547695686.png)





## 3、配置 

```
ConfigureBuilder builder = Configure.newBuilder();
XWPFTemplate.compile("~/template.docx", builder.buid());
```



### 3.1、图片语法 `{{@var}}`修改为 `{{%var}} `

```
builder.addPlugin('%', new PictureRenderPolicy());
```



### 3.2、语法加前缀 为${var}

```
builder.buildGramer("${", "}");
```



### 3.3、模板标签设置正则表达式规则



> 模板标签支持中文、字母、数字、下划线的组合，比如{{客户手机号}}，我们可以通过正则表达式来配置标签的规则，比如不允许中文：



```
builder.buildGrammerRegex("[\\w]+(\\.[\\w]+)*");
```





### 3.4、SpringEl表达式 (一般不建议使用)

> Spring Expression Language (SpEL)是一个强大的表达式语言，支持在运行时查询和操作对象图。在使用SpEL前需要引入相应的依赖：



```java
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-expression</artifactId>
  <version>4.3.6.RELEASE</version>
</dependency>
```



> poi-tl的表达式模板支持切换到SpEL模式：



```java
builder.setElMode(ELMode.SPEL_MODE);
```



```java
{{name}}
{{name.toUpperCase()}} 
{{empty?:'这个字段为空'}}
{{sex ? '男' : '女'}} 
{{new java.text.SimpleDateFormat('yyyy-MM-dd HH:mm:ss').format(time)}} 
{{new java.text.SimpleDateFormat('yyyy-MM-dd hh:mm').format(time)}}
{{price/10000 + '万元'}} 
{{dogs[0].name}} 
{{dogs[0].age}}
```





### 3.4、静默行为



#### 3.4.1、一个模板标签表达式的结果无法被计算的时候，可以通过ElMode来配置行为：



```java
// 默认行为，EL静默模式，表达式计算错误的情况下结果置为null
builder.setElMode(ELMode.POI_TL_STANDARD_MODE);
// 严格EL模式，表达式计算错误的情况下抛出异常，这种情况下要求表达式必须可被计算
builder.setElMode(ELMode.POI_TL_STICT_MODE);
```



#### 3.4.2、模板标签表达式找不到对应数据，计算结果为null或者数据不合法的时候  



```java
// 默认行为，静默删除文档中该标签
builder.setValidErrorHandler(new AbstractRenderPolicy.ClearHandler());
// 什么都不做，文档中保留该标签
builder.setValidErrorHandler(new AbstractRenderPolicy.DiscardHandler());
// 中断执行，抛出异常
builder.setValidErrorHandler(new AbstractRenderPolicy.AbortHandler());
```





##### 3.4.2.1、默认行为，静默删除文档中该标签 



```java

@Test
public void rule() throws Exception {

    String inDocxFilePath = "D:/pdf/rule.docx";
    String outDocxFilePath = "D:/pdf/out_rule.docx";

    Map<String, Object> map = new HashMap<>();
    // 1.1、普通字符
    map.put("rule", "helloWord");


    Configure.ConfigureBuilder builder = Configure.newBuilder();
    builder.buildGramer("${", "}");
    // 默认行为，静默删除文档中该标签
    builder.setValidErrorHandler(new AbstractRenderPolicy.ClearHandler());

    XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, 
                                                 builder.build()).render(map);


    //开始生成新的word
    template.writeToFile(outDocxFilePath);
    template.close();

}
```

![1575548964655](D:\study\HealerJean.github.io\blogImages\1575548964655.png)







![1575548971417](D:\study\HealerJean.github.io\blogImages\1575548971417.png)







##### 3.4.2.2、什么都不做，文档中保留该标签



```java
@Test
public void rule() throws Exception {

    String inDocxFilePath = "D:/pdf/rule.docx";
    String outDocxFilePath = "D:/pdf/out_rule.docx";

    Map<String, Object> map = new HashMap<>();
    // 1.1、普通字符
    map.put("rule", "helloWord");


    Configure.ConfigureBuilder builder = Configure.newBuilder();
    builder.buildGramer("${", "}");
    // 什么都不做，文档中保留该标签
     builder.setValidErrorHandler(new AbstractRenderPolicy.DiscardHandler());

    XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, 
                                                 builder.build()).render(map);


    //开始生成新的word
    template.writeToFile(outDocxFilePath);
    template.close();

}
```



![1575548964655](D:\study\HealerJean.github.io\blogImages\1575548964655.png)



![1575549055019](D:\study\HealerJean.github.io\blogImages\1575549055019.png)





##### 3.4.2.2、中断执行，抛出异常



```java
@Test
public void rule() throws Exception {

    String inDocxFilePath = "D:/pdf/rule.docx";
    String outDocxFilePath = "D:/pdf/out_rule.docx";

    Map<String, Object> map = new HashMap<>();
    // 1.1、普通字符
    map.put("rule", "helloWord");


    Configure.ConfigureBuilder builder = Configure.newBuilder();
    builder.buildGramer("${", "}");
    // 中断执行，抛出异常
    builder.setValidErrorHandler(new AbstractRenderPolicy.AbortHandler());

    XWPFTemplate template = XWPFTemplate.compile(inDocxFilePath, 
                                                 builder.build()).render(map);


    //开始生成新的word
    template.writeToFile(outDocxFilePath);
    template.close();

}
```



![1575549088526](D:\study\HealerJean.github.io\blogImages\1575549088526.png)







## 插件 暂时不用用的时候再回来看吧

[http://deepoove.com/poi-tl/](http://deepoove.com/poi-tl/)















<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>       

   



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'xpRFMI7tgV1ADWJT',
    });
    gitalk.render('gitalk-container');
</script> 

