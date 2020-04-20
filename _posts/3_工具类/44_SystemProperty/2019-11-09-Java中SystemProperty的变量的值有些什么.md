---
title: Java中SystemProperty的变量的值有些什么
date: 2019-11-09 03:33:00
tags: 
- Java
category: 
- Java
description: Java中SystemProperty的变量的值有些什么
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



### 1.1、Java相关



```java

// Java运行时环境供应商 ： Oracle Corporation
String javaVendor = System.getProperty("java.vendor");
log.info("java.vendor ：{}", javaVendor);

//  Java供应商的URL ：http://java.oracle.com/
String javaVendorUrl = System.getProperty("java.vendor.url");
log.info("java.vendor.url ：{}", javaVendorUrl);

// java版本 ：1.8.0_201
String javaVersion = System.getProperty("java.version");
log.info("java.version  ：{}", javaVersion);

// java安装目录 ：D:\programFiles\Jdk1.8\jre
String javaHome = System.getProperty("java.home");
log.info("java.home ：{}", javaHome);

// Java虚拟机规范版本：1.8
String javaVmSpecificationVersion = System.getProperty("java.vm.specification.version");
log.info("java.vm.specification.version ：{}", javaVmSpecificationVersion);

// Java虚拟机规范供应商：Oracle
String javaVmSpecificationVendor = System.getProperty("java.vm.specification.vendor");
log.info("java.vm.specification.vendor ：{}", javaVmSpecificationVendor);

// Java虚拟机规范名称 ：Java
String javaVmSpecificationName = System.getProperty("java.vm.specification.name");
log.info("java.vm.specification.name ：{}", javaVmSpecificationName);

// Java虚拟机实现版本：25.201-b09
String javaVmVersion = System.getProperty("java.vm.version");
log.info("java.vm.version ：{}", javaVmVersion);

// Java虚拟机实现供应商 ：Oracle
String javaVmVendor = System.getProperty("java.vm.vendor");
log.info("java.vm.vendor ：{}", javaVmVendor);

// Java虚拟机实现名称 ：Java HotSpot(TM) 64-Bit Server VM
String javaVmName = System.getProperty("java.vm.name");
log.info("java.vm.name ：{}", javaVmName);

// Java运行时环境规范版本：1.8
String javaSpecificationVersion = System.getProperty("java.specification.version");
log.info("java.specification.version：{}", javaSpecificationVersion);

// Java运行时环境规范供应商：Oracle
String javaSpecificationVendor = System.getProperty("java.specification.vendor");
log.info("java.specification.vendor：{}", javaSpecificationVendor);

// Java运行时环境规范名称：Java Platform API Specification
String javaSpecificationName = System.getProperty("java.specification.name");
log.info("java.specification.name：{}", javaSpecificationName);

// Java类格式版本号：52.0
String javaClassVersion = System.getProperty("java.class.version");
log.info("java.class.version：{}", javaClassVersion);
```





### 1.2、操作系统相关



#### 1.2.1、 os.name： 操作系统的名称



```java
String osName = System.getProperty("os.name");
log.info("os.name：{}", osName);

// 操作系统的名称：Windows 10
```



#### 1.2.2、os.arch：操作系统的架构



```java
String osArch = System.getProperty("os.arch");
log.info("os.arch：{}", osArch);


// 操作系统的架构：amd64
```



#### 1.2.3、os.version：操作系统的版本



```java
String osVersion = System.getProperty("os.version");
log.info("os.version：{}", osVersion);

// 操作系统的版本：10.0
```



#### 1.2.4、file.separator：文件分隔符

```java
String fileSeparator = System.getProperty("file.separator");
log.info("file.separator：{}", fileSeparator);

// 文件分隔符（在 UNIX 系统中是“/”）：\
```



#### 1.2.5、path.separator：路径分隔符（在 UNIX 系统中是“:”）



```java
String pathSeparator = System.getProperty("path.separator");
log.info("path.separator：{}", pathSeparator);
	
// 路径分隔符（在 UNIX 系统中是“:”）：;
```



#### 1.2.6、line.separator：行分隔符（在 UNIX 系统中是“/n”）



```java
String lineSeparator = System.getProperty("line.separator");
log.info("line.separator：{}", lineSeparator);
	
// 行分隔符（在 UNIX 系统中是“/n”）
```





#### 1.2.7、java.io.tmpdir：默认的临时文件路径



```java
String javaIoTmpdir = System.getProperty("java.io.tmpdir");
log.info("java.io.tmpdir：{}", javaIoTmpdir);
	
// 默认的临时文件路径：C:\Users\HEALER~1\AppData\Local\Temp\

```



#### 1.2.8、user.name：用户的账户名称



```java
String usernName = System.getProperty("user.name");
log.info("user.name：{}", usernName);
	
// 用户的账户名称：HealerJean
```



#### 1.2.9、user.home：用户的主目录



```java
String userHome = System.getProperty("user.home");
log.info("user.home：{}", userHome);
	
// 用户的主目录：C:\Users\HealerJean
```



#### 1.2.10、user.dir：用户的当前工作目录



```java
String userDir = System.getProperty("user.dir");
log.info("user.dir：{}", userDir);
	

// 用户的当前工作目录：D:\study\HealerJean.github.io\_posts\3_工具类\44_SystemProperty\hlj-system-preoperty
```



### 1.3、项目路径相关



#### 1.3.1、sun.boot.class.path

+ 启动类加载器（Bootstrap ClassLoader） 加载的目录 %JAVA_HOME%\lib



```java

String sunBootClassPath = System.getProperty("sun.boot.class.path");
log.info("sun.boot.class.path：{}", sunBootClassPath);


// D:\programFiles\Jdk1.8\jre\lib\resources.jar;
// D:\programFiles\Jdk1.8\jre\lib\rt.jar;
// D:\programFiles\Jdk1.8\jre\lib\sunrsasign.jar;
// D:\programFiles\Jdk1.8\jre\lib\jsse.jar;
// D:\programFiles\Jdk1.8\jre\lib\jce.jar;
// D:\programFiles\Jdk1.8\jre\lib\charsets.jar;
// D:\programFiles\Jdk1.8\jre\lib\jfr.jar;
// D:\programFiles\Jdk1.8\jre\classes

```



#### 1.3.2、java.ext.dirs 

+ 扩展类加载器加载的路径，一个或多个扩展目录的路径：$JAVA_HOME/lib/ext



```java

String javaExtDirs = System.getProperty("java.ext.dirs");
log.info("java.ext.dirs：{}", javaExtDirs);
// D:\programFiles\Jdk1.8\jre\lib\ext;
// C:\Windows\Sun\Java\lib\ext
```



#### 1.3.3、java.class.path 

+  应用程序类加载器（Application ClassLoader）： Java类路径



```java
      

String javaClassPath = System.getProperty("java.class.path");
log.info("java.class.path：{}", javaClassPath);


// D:\programFiles\Jdk1.8\jre\lib\charsets.jar;
// D:\programFiles\Jdk1.8\jre\lib\deploy.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\access-bridge-64. jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\cldrdata.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\dnsns.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\jaccess.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\jfxrt.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\localedata.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\nashorn.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\sunec.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\sunjce_provider.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\sunmscapi.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\sunpkcs11.jar;
// D:\programFiles\Jdk1.8\jre\lib\ext\zipfs.jar;
// D:\programFiles\Jdk1.8\jre\lib\javaws.jar;
// D:\programFiles\Jdk1.8\jre\lib\jce.jar;
// D:\programFiles\Jdk1.8\jre\lib\jfr.jar;
// D:\programFiles\Jdk1.8\jre\lib\jfxswt.jar;
// D:\programFiles\Jdk1.8\jre\lib\jsse.jar;
// D:\programFiles\Jdk1.8\jre\lib\management- agent.jar;
// D:\programFiles\Jdk1.8\jre\lib\plugin.jar;
// D:\programFiles\Jdk1.8\jre\lib\resources.jar;
// D:\programFiles\Jdk1.8\jre\lib\rt.jar;
// D:\study\HealerJean.github.io\_posts\3_工具类\44_SystemProperty\hlj-system-preoperty\hlj-parent\hlj-client\target\classes;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-starter-web\2.0.1.RELEASE\spring-boot-starter-web-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-starter\2.0.1.RELEASE\spring-boot-starter-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot\2.0.1.RELEASE\spring-boot-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\2.0.1.RELEASE\spring-boot-autoconfigure-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\javax\annotation\javax.annotation-api\1.3.2\javax.annotation-api-1.3.2.jar;
// C:\Users\HealerJean\.m2\repository\org\yaml\snakeyaml\1.19\snakeyaml-1.19.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-starter-json\2.0.1.RELEASE\spring-boot-starter-json-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-starter-tomcat\2.0.1.RELEASE\spring-boot-starter-tomcat-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\apache\tomcat\embed\tomcat-embed-core\8.5.29\tomcat-embed-core-8.5.29.jar;
// C:\Users\HealerJean\.m2\repository\org\apache\tomcat\embed\tomcat-embed-el\8.5.29\tomcat-embed-el-8.5.29.jar;
// C:\Users\HealerJean\.m2\repository\org\apache\tomcat\embed\tomcat-embed-websocket\8.5.29\tomcat-embed-websocket-8.5.29.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-web\5.0.5.RELEASE\spring-web-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-beans\5.0.5.RELEASE\spring-beans-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-webmvc\5.0.5.RELEASE\spring-webmvc-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-aop\5.0.5.RELEASE\spring-aop-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-context\5.0.5.RELEASE\spring-context-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-expression\5.0.5.RELEASE\spring-expression-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-test\5.1.9.RELEASE\spring-test-5.1.9.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-core\5.0.5.RELEASE\spring-core-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\spring-jcl\5.0.5.RELEASE\spring-jcl-5.0.5.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-starter-test\1.5.1.RELEASE\spring-boot-starter-test-1.5.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-test\2.0.1.RELEASE\spring-boot-test-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-test-autoconfigure\2.0.1.RELEASE\spring-boot-test-autoconfigure-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\com\jayway\jsonpath\json-path\2.4.0\json-path-2.4.0.jar;
// C:\Users\HealerJean\.m2\repository\net\minidev\json-smart\2.3\json-smart-2.3.jar;
// C:\Users\HealerJean\.m2\repository\net\minidev\accessors-smart\1.2\accessors-smart-1.2.jar;
// C:\Users\HealerJean\.m2\repository\org\ow2\asm\asm\5.0.4\asm-5.0.4.jar;
// C:\Users\HealerJean\.m2\repository\org\assertj\assertj-core\3.9.1\assertj-core-3.9.1.jar;
// C:\Users\HealerJean\.m2\repository\org\mockito\mockito-core\2.15.0\mockito-core-2.15.0.jar;
// C:\Users\HealerJean\.m2\repository\net\bytebuddy\byte-buddy\1.7.11\byte-buddy-1.7.11.jar;
// C:\Users\HealerJean\.m2\repository\net\bytebuddy\byte-buddy-agent\1.7.11\byte-buddy-agent-1.7.11.jar;
// C:\Users\HealerJean\.m2\repository\org\objenesis\objenesis\2.6\objenesis-2.6.jar;
// C:\Users\HealerJean\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;
// C:\Users\HealerJean\.m2\repository\org\hamcrest\hamcrest-library\1.3\hamcrest-library-1.3.jar;
// C:\Users\HealerJean\.m2\repository\org\skyscreamer\jsonassert\1.5.0\jsonassert-1.5.0.jar;
// C:\Users\HealerJean\.m2\repository\com\vaadin\external\google\android-json\0.0.20131108.vaadin1\android-json-0.0.20131108.vaadin1.jar;
// C:\Users\HealerJean\.m2\repository\junit\junit\4.12\junit-4.12.jar;
// C:\Users\HealerJean\.m2\repository\org\projectlombok\lombok\1.18.4\lombok-1.18.4.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\boot\spring-boot-starter-log4j2\2.0.1.RELEASE\spring-boot-starter-log4j2-2.0.1.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\apache\logging\log4j\log4j-slf4j-impl\2.10.0\log4j-slf4j-impl-2.10.0.jar;
// C:\Users\HealerJean\.m2\repository\org\apache\logging\log4j\log4j-api\2.10.0\log4j-api-2.10.0.jar;
// C:\Users\HealerJean\.m2\repository\org\apache\logging\log4j\log4j-core\2.10.0\log4j-core-2.10.0.jar;
// C:\Users\HealerJean\.m2\repository\org\slf4j\jul-to-slf4j\1.7.25\jul-to-slf4j-1.7.25.jar;
// C:\Users\HealerJean\.m2\repository\com\lmax\disruptor\3.3.7\disruptor-3.3.7.jar;
// C:\Users\HealerJean\.m2\repository\org\hibernate\hibernate-validator\4.3.2.Final\hibernate-validator-4.3.2.Final.jar;
// C:\Users\HealerJean\.m2\repository\org\jboss\logging\jboss-logging\3.3.2.Final\jboss-logging-3.3.2.Final.jar;
// C:\Users\HealerJean\.m2\repository\javax\validation\validation-api\1.0.0.GA\validation-api-1.0.0.GA.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.9.5\jackson-databind-2.9.5.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.9.5\jackson-core-2.9.5.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.9.0\jackson-annotations-2.9.0.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\module\jackson-module-parameter-names\2.9.5\jackson-module-parameter-names-2.9.5.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jdk8\2.9.5\jackson-datatype-jdk8-2.9.5.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\datatype\jackson-datatype-jsr310\2.9.5\jackson-datatype-jsr310-2.9.5.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\dataformat\jackson-dataformat-xml\2.9.5\jackson-dataformat-xml-2.9.5.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\jackson\module\jackson-module-jaxb-annotations\2.9.5\jackson-module-jaxb-annotations-2.9.5.jar;
// C:\Users\HealerJean\.m2\repository\org\codehaus\woodstox\stax2-api\3.1.4\stax2-api-3.1.4.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\woodstox\woodstox-core\5.0.3\woodstox-core-5.0.3.jar;
// C:\Users\HealerJean\.m2\repository\org\apache\commons\commons-lang3\3.7\commons-lang3-3.7.jar;
// C:\Users\HealerJean\.m2\repository\com\squareup\okhttp3\okhttp\3.11.0\okhttp-3.11.0.jar;
// C:\Users\HealerJean\.m2\repository\com\squareup\okio\okio\1.14.0\okio-1.14.0.jar;
// C:\Users\HealerJean\.m2\repository\com\alibaba\fastjson\1.2.58\fastjson-1.2.58.jar;
// C:\Users\HealerJean\.m2\repository\io\springfox\springfox-swagger2\2.9.2\springfox-swagger2-2.9.2.jar;
// C:\Users\HealerJean\.m2\repository\io\swagger\swagger-annotations\1.5.20\swagger-annotations-1.5.20.jar;
// C:\Users\HealerJean\.m2\repository\io\swagger\swagger-models\1.5.20\swagger-models-1.5.20.jar;
// C:\Users\HealerJean\.m2\repository\io\springfox\springfox-spi\2.9.2\springfox-spi-2.9.2.jar;
// C:\Users\HealerJean\.m2\repository\io\springfox\springfox-core\2.9.2\springfox-core-2.9.2.jar;
// C:\Users\HealerJean\.m2\repository\io\springfox\springfox-schema\2.9.2\springfox-schema-2.9.2.jar;
// C:\Users\HealerJean\.m2\repository\io\springfox\springfox-swagger-common\2.9.2\springfox-swagger-common-2.9.2.jar;
// C:\Users\HealerJean\.m2\repository\io\springfox\springfox-spring-web\2.9.2\springfox-spring-web-2.9.2.jar;
// C:\Users\HealerJean\.m2\repository\com\google\guava\guava\20.0\guava-20.0.jar;
// C:\Users\HealerJean\.m2\repository\com\fasterxml\classmate\1.3.4\classmate-1.3.4.jar;
// C:\Users\HealerJean\.m2\repository\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\plugin\spring-plugin-core\1.2.0.RELEASE\spring-plugin-core-1.2.0.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\springframework\plugin\spring-plugin-metadata\1.2.0.RELEASE\spring-plugin-metadata-1.2.0.RELEASE.jar;
// C:\Users\HealerJean\.m2\repository\org\mapstruct\mapstruct\1.2.0.Final\mapstruct-1.2.0.Final.jar;
// C:\Users\HealerJean\.m2\repository\io\springfox\springfox-swagger-ui\2.9.2\springfox-swagger-ui-2.9.2.jar;
// D:\programFiles\IntelliJ IDEA 2018.3.5\lib\idea_rt.jar
```





#### 1.3.4、java.library.path

+ **加载库时搜索的路径列表： 指定非java类包的位置**



```java
String javaLibraryPath = System.getProperty("java.library.path");
log.info("java.library.path：{}", javaLibraryPath);


// D:\programFiles\Jdk1.8\bin;
// C:\Windows\Sun\Java\bin;
// C:\Windows\system32;
// C:\Windows;
// D:\programFiles\SecureSFTP\;
// C:\Program Files (x86)\Common Files\Oracle\Java\javapath;
// C:\Windows\system32;
// C:\Windows;
// C:\Windows\System32\Wbem;
// C:\Windows\System32\WindowsPowerShell\v1.0\;
// C:\Windows\System32\OpenSSH\;
// D:\programFiles\Git\cmd;
// D:\programFiles\Jdk1.8;
// D:\programFiles\Jdk1.8\bin;
// D:\programFiles\Jdk1.8\javaJre;
// D:\programFiles\apache-maven-3.6.0\bin;
// D:\programFiles\mysql-5.7.25-winx64\bin;
// "%GRADLE_HOME%\BIN;";
// D:\programFiles\nodeJs\;
// D:\programFiles\Tail4win;
// C:\Program Files\Git LFS;
// C:\Users\HealerJean\AppData\Local\Microsoft\WindowsApps;
// C:\Users\HealerJean\AppData\Roaming\npm;
       
```





![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)  



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
		id: 'RUbnG2oWuyl1visE',
    });
    gitalk.render('gitalk-container');
</script> 

