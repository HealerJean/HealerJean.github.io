---
title: Jmeter_之_Java压测
date: 2022-05-30 00:00:00
tags: 
- SoftWare
category: 
- SoftWare
description: Jmeter_之_Java压测
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 1、`POM`

> `pom.xml `( `jmeter` 的版本最好和压测用的一致)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.healerjean.proj</groupId>
    <artifactId>hlj-demo</artifactId>
    <version>1.0-SNAPSHOT</version>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.0.5.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!--lombok-->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <!-- 引入log4j2依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
        <!-- Log4j2 异步支持 -->
        <dependency>
            <groupId>com.lmax</groupId>
            <artifactId>disruptor</artifactId>
            <version>3.3.7</version>
        </dependency>
        <!-- junit-->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>


        <!-- jmeter -->
        <dependency>
            <groupId>org.apache.jmeter</groupId>
            <artifactId>ApacheJMeter_core</artifactId>
            <version>3.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.jmeter</groupId>
            <artifactId>ApacheJMeter_java</artifactId>
            <version>3.0</version>
        </dependency>
    </dependencies>

    <build>


        <finalName>hlj_Jmeter</finalName>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering><!--这里开启变量替换-->
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.properties</include>
                    <include>**/*.json</include>
                </includes>
            </resource>
        </resources>

        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-site-plugin</artifactId>
                <version>3.3</version>
                <configuration>
                    <locales>zh_CN</locales>
                </configuration>
            </plugin>

            <plugin>
                <artifactId>maven-dependency-plugin</artifactId>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>copy-dependencies</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <outputDirectory>${project.build.directory}/lib</outputDirectory>
                    <excludeTransitive>false</excludeTransitive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>2.5</version>
                <configuration>
                    <archive>
                        <manifest>
                            <addClasspath>true</addClasspath>
                            <mainClass>com.abc.jmeterTest.RpcTest</mainClass>
                            <classpathPrefix>lib/</classpathPrefix>
                            <useUniqueVersions>false</useUniqueVersions>
                        </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>2.4</version>
                <configuration>
                    <descriptors>
                        <descriptor>src/main/resources/assembly.xml</descriptor>
                    </descriptors>
                </configuration>
                <executions>
                    <execution>
                        <id>make-assembly</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>

        </plugins>

    </build>

</project>

```



# 2、`Java` 压测脚本

## 2.1、被压测代码

```java
@Slf4j
public class JmeterServer {

  public String yc(){
    for (int i = 0 ; i< 100000; i++){
      try {
        log.info("[JmeterServer#yc] start ");
        Thread.sleep(1000L);
        log.info("[JmeterServer#yc] end ");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return Thread.currentThread().getName();
  }

  @Test
  public void test(){
    yc();
  }

}

```



## 2.2、压测脚本

> 继承 `AbstractJavaSamplerClient` ：

```java
@Slf4j
public class JavaClient extends AbstractJavaSamplerClient {


    /**
     * 把测试的一些默认数据在程序运行前显示到JMeter客户端
     */
    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("num1", "num1");
        params.addArgument("num2", "num2");
        return params;
    }

    /**
     * 子类用它来 记录log
     *
     * @return
     */
    @Override
    protected org.apache.log.Logger getLogger() {
        return null;
    }


    /**
     * 初始化方法，实际运行时每个线程仅执行一次，在测试方法运行前执行
     *
     * @param context
     */
    @Override
    public void setupTest(JavaSamplerContext context) {
        String num1 = context.getParameter("num1");
        String num2 = context.getParameter("num2");
        log.info("[JavaClient#setupTest] num1:{}, num2:{}", num1, num2);
    }

    /**
     * 结束方法，实际运行时每个线程仅执行一次，在测试方法运行结束后执行
     */
    @Override
    public void teardownTest(JavaSamplerContext context) {
        String num1 = context.getParameter("num1");
        String num2 = context.getParameter("num2");
        log.info("[JavaClient#teardownTest] num1:{}, num2:{}", num1, num2);
    }

    /**
     * @Test只是为了调试用，最后打jar包的时候注释掉。
     */
    @Test
    public void test() {

    }

    public static void main(String[] args) {
        //设置参数，并赋予默认值2
        Arguments params = new Arguments();
        params.addArgument("num1", "1");
        params.addArgument("num2", "2");
        JavaSamplerContext arg0 = new JavaSamplerContext(params);
        JavaClient test = new JavaClient();
        test.setupTest(arg0);

        test.runTest(arg0);

        test.teardownTest(arg0);
    }


    /**
     * 测试执行的循环体，根据线程数和循环次数的不同可执行多次
     * 1、获取界面中传递的值
     * 2、压测结果获取
     * 3、判断测试成功与否的方法：可根据实际进行判断，此处为如果结果非空，则认为该次调用成功
     *
     * @param context
     * @return
     */
    @Override
    public SampleResult runTest(JavaSamplerContext context) {
        SampleResult sampleresult = new SampleResult();
        // 1、获取界面中传递的值
        String num1 = context.getParameter("num1");
        String num2 = context.getParameter("num2");
        sampleresult.sampleStart();//计时开始
        try {
            // 2、压测结果获取
            JmeterServer test = new JmeterServer();
            String result = test.yc();
            //将结果写入结果树：在jmeter的监听器-查看结果树时即可查看返回结果
            sampleresult.setResponseData("结果是：" + result + "," + num1 + "," + num2, null);
            sampleresult.setDataType(SampleResult.TEXT);
            sampleresult.setSuccessful(true);

        } catch (Exception e) {
            //不满足判断条件则判为false，会出现在监听器-聚合报告的Error%列
            sampleresult.setSuccessful(false);
            e.printStackTrace();
        } finally {
            //计时结束
            sampleresult.sampleEnd();
        }

        // 3、判断测试成功与否的方法：可根据实际进行判断，此处为如果结果非空，则认为该次调用成功
        // if (result.equals("a")) {
        //     //将结果写入结果树：在jmeter的监听器-查看结果树时即可查看返回结果
        //     sampleresult.setSuccessful( true);
        //     sampleresult.setResponseData( "结果是："+a ,null);
        //     sampleresult.setDataType(SampleResult. TEXT);
        // } else {
        //     sampleresult.setSuccessful( false);//不满足判断条件则判为false，会出现在监听器-聚合报告的Error%列
        // }
        return sampleresult;
    }

}
```



# 3、`maven` 打包

> 1、使用 `maven`，把项目打包成 `hlj_Jmeter.jar` 和 `lib` 目录（依赖的各种jar）
>
> 2、将`MyTest.jar`和 `lib` 目录拷贝到 `jmeter` 的 `JMETER_HOME\lib\ext` 目录（



# 4、压测

## 4.1、压测操作

### 4.1.1、创建线程组

![image-20220530150626103](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220530150626103.png)



### 4.1.2、`Java` 请求

![image-20220530150711907](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220530150711907.png)





### 4.1.3、查看结果树

![image-20220530150851147](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220530150851147.png)

## 4.2、执行压测

### 4.2.1、点击开始

![image-20220530151020113](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220530151020113.png)



### 4.2.2、查看压测结果

![image-20220530151103776](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/image-20220530151103776.png)













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
		id: 'HhpioAySLT681cZq',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



