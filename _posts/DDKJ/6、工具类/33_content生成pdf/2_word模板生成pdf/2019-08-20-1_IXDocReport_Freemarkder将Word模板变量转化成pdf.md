---
title: IXDocReport_Freemarkder将Word模板变量转化成pdf
date: 2019-08-20 03:33:00
tags: 
- Utils
category: 
- Utils
description: IXDocReport_Freemarkder将Word模板变量转化成pdf
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





### 1、maven

```java
       
 <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
        <xdocreport.version>1.0.5</xdocreport.version>
    </properties>


		<dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.converter.docx.xwpf</artifactId>
            <version>${xdocreport.version}</version>
        </dependency>

        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.template.velocity</artifactId>
            <version>${xdocreport.version}</version>
        </dependency>
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.template.freemarker</artifactId>
            <version>${xdocreport.version}</version>
        </dependency>
        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.document.docx</artifactId>
            <version>${xdocreport.version}</version>
        </dependency>

        <dependency>
            <groupId>fr.opensagres.xdocreport</groupId>
            <artifactId>fr.opensagres.xdocreport.itext.extension</artifactId>
            <version>${xdocreport.version}</version>
        </dependency>
```





### 2、制作一个word模板（请不要复制）



#### 2.1、文字的变量配置



**插入--->文档部件--->域** 

![1566282786718](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566282786718.png)





![1566282859695](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566282859695.png)




#### 2.2、图片的变量配置

**图片的插入方式和上面的不太相同，首先我们点击图片(必须有一张图片)，选择插入，选择书签，输入一个任意的变量名如 img**



![1566282940939](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566282940939.png)

![1566282957797](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566282957797.png)





#### 2.3、大概如下(图片不能显示)



![1566283017131](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/1566283017131.png)



### 3、开始跑代码吧



```java
package hlj.wordtopdf;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.images.ByteArrayImageProvider;
import fr.opensagres.xdocreport.document.images.FileImageProvider;
import fr.opensagres.xdocreport.document.images.IImageProvider;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

import java.io.*;
import java.util.*;

/**
 * @author HealerJean
 * @ClassName PdfCreate
 * @date 2019/8/20  10:23.
 * @Description
 */
public class PdfCreate {

    public static final String WORD = "word";
    public static final String PERSON = "person";
    public static final String TABLE = "table";
    public static final String IMG = "img";


    public static void main(String[] args) {
        File template = new File("D:/pdf/template.docx");
        File outputFile = new File("D:/pdf/ok_template.pdf");

        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(template);
            IXDocReport report =  XDocReportRegistry.getRegistry().loadReport(inputStream, TemplateEngineKind.Freemarker);
            IContext context = report.createContext();
            FieldsMetadata fieldsMetadata = report.createFieldsMetadata();

            //普通对象
            fieldsMetadata.load(PERSON, Person.class);
            //是否list
            fieldsMetadata.load(TABLE, Table.class, true);
            //图片
            fieldsMetadata.addFieldAsImage("img");
            report.setFieldsMetadata(fieldsMetadata);

            // 1、普通字符
            context.put(WORD, "这是一句话");

            // 2、对象
            Person person = new Person("HealerJean", "25", "男");
            context.put(PERSON, person);

            // 3、表格
            Table table1 = new Table("11", "12", "13");
            Table table2 = new Table("21", "22", "23");
            List<Table> table = new ArrayList<>();
            table.add(table1);
            table.add(table2);
            context.put(TABLE, table);

            // 4、 图片
            // 图片的插入方式和上面的不太相同，首先我们点击图片(必须有一张图片)，选择插入，选择书签，输入一个任意的变量名如 img
            IImageProvider img = new FileImageProvider(new File("D:/pdf/img.png"), true);
            img.setSize(200f, 100f);

            // IImageProvider img = new FileImageProvider(new File("D:/pdf/img.png"));
            context.put("img", img);

            //生成word
            // outputStream = new FileOutputStream(new File("D:/pdf/word.docx"));
            // report.process(context, outputStream);

            //生成pdf
            outputStream = new FileOutputStream(outputFile);
            Options options = Options.getTo(ConverterTypeTo.PDF).via(ConverterTypeVia.XWPF);
            report.convert(context, options, outputStream);

        } catch (IOException | XDocReportException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



}

```










<br/>
<br/>

<font  color="red" size="5" >     
感兴趣的，欢迎添加博主微信
 </font>

<br/>



哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦

|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



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
		id: 'atQnFwyg37HMjLOh',
    });
    gitalk.render('gitalk-container');
</script> 


<!-- Gitalk end -->

