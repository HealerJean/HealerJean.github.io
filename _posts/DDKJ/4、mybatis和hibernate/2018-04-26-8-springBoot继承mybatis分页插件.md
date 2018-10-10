---
title: 2、springBoot集成mybatis分页插件PageHelper
date: 2018-03-26 12:33:00
tags: 
- SpringBoot
- Mybatis
category: 
- SpringBoot
- Mybatis
description: springBoot集成mybatis分页插件PageHelper
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
-->

## 前言

<font color="red"> PageHelper是一款非常好用的分页插件，它和Mybatis工作在一起，可以大幅提升开发效率。PageHelper是通过Mybatis的拦截器插件原理实现的。 </font>


## 1、导入依赖

```xml

<!--分页插件-->
<dependency>
	<groupId>com.github.pagehelper</groupId>
	<artifactId>pagehelper</artifactId>
	<version>4.1.6</version>
</dependency>

```

## 2、启动加载PageHelperbean

```java
package com.hlj.mybatisxml.pagehelper;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/26  上午11:58.
 */
@Configuration
public class PageHelperConfiguration {
    private static final Logger log = LoggerFactory.getLogger(PageHelperConfiguration.class);
    @Bean
    public PageHelper pageHelper() {
        log.info("------Register MyBatis PageHelper");
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        //通过设置pageSize=0或者RowBounds.limit = 0就会查询出全部的结果。
        p.setProperty("pageSizeZero", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }
}


```

## 3、直接分页开始

<font color="red">只要找出list来就可以进行分页了</font>

```java
@RestController
public class PageHelperController {

    @Resource
    private BasesetUserMapper basesetUserMapper;

    @RequestMapping(value = "page")
    public PageInfo<BasesetUser> queryAll(@RequestParam(value = "pageNum", required = false, defaultValue="1") Integer pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue="10") Integer pageSize) {
        //传入第几页和大小
        PageHelper.startPage(pageNum, pageSize);
        List<BasesetUser> list = basesetUserMapper.findMyall();
        PageInfo<BasesetUser> pageInfo = new PageInfo<BasesetUser>(list);
        return  pageInfo;
    }

}

```

## 4、测试


```json

http://localhost:8888/page?pageNum=1&pageSize=2


{
	"pageNum": 1,
	"pageSize": 2,
	"size": 2,
	"orderBy": null,
	"startRow": 1,
	"endRow": 2,
	"total": 6,
	"pages": 3,
	"list": [{
		"id": 1,
		"username": "HealerJean",
		"password": "213456",
		"enable": 1
	}, {
		"id": 2,
		"username": "HealerJean",
		"password": "213456",
		"enable": 1
	}],
	"firstPage": 1,
	"prePage": 0,
	"nextPage": 2,
	"lastPage": 3,
	"isFirstPage": true,
	"isLastPage": false,
	"hasPreviousPage": false,
	"hasNextPage": true,
	"navigatePages": 8,
	"navigatepageNums": [1, 2, 3]
}
```

## 补充，代码中没有

### 1、 通过分页对象进行前端传入

```java
package com.appshike.admin.domain.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import springfox.documentation.annotations.ApiIgnore;


@Setter
@ApiModel("分页对象")
@Accessors(chain = true)
public class PageQuery {

    @ApiModelProperty(value = "开始页数，从1开始",example = "1", required = true,dataType = "java.lang.Integer")
    private Integer pageNum = 1;
    @ApiModelProperty(value = "每页数量",example = "20", required = true,dataType = "java.lang.Integer")
    private Integer pageSize = 20;
    @ApiModelProperty(hidden = true)
   
    public Integer getPageSize() {
        return pageSize == null ? 20 : pageSize;
    }

    public Integer getPageNum() {
        return pageNum == null ? 1 : pageNum;
    }
}


```

### 2、controller 其实和pageable是一样的了，是吧，但是这里根本不需要关注里面发生了什么只给一个list就可以了

```java
@GetMapping("getCategoryTagList")
public Wrapper<?> getCategoryTagList(Long categoryId,Short classify, PageQuery pageQuery){
    Map<String,Object> query = new HashMap<String,Object>();
    query.put("categoryId", categoryId);
    query.put("classify", classify);
    PageInfo<TVideoCategoryTagVO> page = zqCategoryService.getCategoryTagList(query, pageQuery);
    return WrapMapper.ok(page);
}

```

### 3、service

```java
@TargetDataSource(DataSource.ZUIQIANG)
@Override
public PageInfo<TVideoCategoryTagVO> getCategoryTagList(Map<String,Object> query, PageQuery pageQuery) {
    PageHelper.startPage(pageQuery.getPageNum(),pageQuery.getPageSize());
    List<TVideoCategoryTagVO> beanList = tVideoCategoryTagMapper.getList(query);
    return new PageInfo<>(beanList);
}

```


## [源码下载](https://gitee.com/HealerJean/CodeDownLoad/raw/master/2018_04_26_8-springBoot%E7%BB%A7%E6%89%BFmybatis%E5%88%86%E9%A1%B5%E6%8F%92%E4%BB%B6/com-hlj-mybatis-Automatic-method-pagehelper.zip)



<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean123.github.io`,
		owner: 'HealerJean123',
		admin: ['HealerJean123'],
		id: 'PwTbZ1wfHy6yl07I',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

