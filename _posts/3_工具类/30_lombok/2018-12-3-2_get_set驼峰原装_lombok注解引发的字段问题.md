---
title: get_set驼峰原装_lombok注解引发的字段问题
date: 2018-12-03 03:33:00
tags: 
- Annotation
category: 
- Annotation
description: get_set驼峰原装_lombok注解引发的字段问题
---
**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)        



使用lombok固然能加快开发，但是使用的不恰当，可能会给我们造成一些意外的伤害，这里我将之前遇到的字段伤害做个简单的总结。     

<font  color="red" size="4">数据库设计的时候，一定要考虑好字段的名称，让它和json一致 ，比如我的数据库中设计的为 IsTmail ，那么我使用的lombok字段名 为 IsTmail（因为数据库映射是完全根据字段对应的，与json没有关系，但是我们以后设计尽量和json一致） </font>     

lombok 首字母映射出来一定是大写的（映射出来json也是如此）     

javabean 首字母小写，第二个大写，则set的下个字母按照原来的首字母确定（映射出来的json和自己本身的字段名相同）


### 1、关于boolean的字段



```java

//javabean isTmail 、IsTmail谁在前面用谁的 setTmail isTmail
  {
        "tmail": true
  }
   
// lomboke 如果是小写的is
	private boolean isTmail; //lombok setTmail     isTmail
	private boolean IsTmail; //lombok setIsTmail   isIsTmail
	
	{
		"isTmail": true,  //IsTmail
		"tmail": true     //isTmail
	}
	
	
	//下面这个 JavaBean和lombok是一样的
	private boolean healerJean ; //boolean类型的时候 setHealerJean   isHealerJean
  {
		"healerJean": true,
	}

```

### 2、关于第二个字母大写，首字母大小写问题


```java
//lombok  tVolumn TVolumn  谁在前使用谁 setTVolumn  getTVolumn
	{
		"TVolumn": 100,
	}
	
	
  private Long tVolumn ;  //JavaBean settVolumn
  private Long TVolumn;   //JavaBean setTVolumn
	{
     "TVolumn": 0,
     "tVolumn": 0,
  }

```

## 3、测试

### 3.1、lombok首字母小写

#### 3.1.1、JavaBean

```java


/*

 {
         "TVolumn": 100,
         "name": "HealerJean",
         "tmail": true
  }

*/
@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
public class DemoEntity01 {


	private String name;       //lombok   setName

	private boolean isTmail;   // lombok  setTmail

	private Long tVolumn ;     // lombok  setTVolumn


}


```

#### 3.1.2、测试



```java

    /*
     lombok
     private String name;       // lombok  setName
     private boolean isTmail;   // lombok  setTmail
     private Long tVolumn ;     // lombok  setTVolumn

         {
         "TVolumn": 100,
         "name": "HealerJean",
         "tmail": true
         }

     */
    @ResponseBody
    @GetMapping(value = "1")
    public String demo01(){
        String json ;
        DemoEntity01 demoEntity =    new DemoEntity01() //lombok
                .setName("HealerJean")    //  lombok   name
                .setTmail(true)           //  lombok  isTmail
                .setTVolumn(100L)         //  lombok   tVolumn
                ;

        demoEntity.getName() ;
        demoEntity.getTVolumn();
        demoEntity.isTmail();

        json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
        return json ;
    }


```

### 3.2、lombok首字母大写

#### 3.2.1、javabean


```java

/*

 {
	 "TVolumn": 100,
	 "isTmail": true,
	 "name": "HealerJean"
 }

 */
@Data
@Accessors(chain = true)
@ApiModel(value = "demo实体类")
public class DemoEntity02 {


	private String Name ;      // lombok  setName     getName

	private boolean IsTmail;   // lombok  setIsTmail  isIsTmail

	private Long TVolumn;      // lombok  setTVolumn  getTVolumn


}


```

#### 3.2.2、测试


```java

 /*
        {
            "TVolumn": 100,
            "isTmail": true,
            "name": "HealerJean"
        }
        private String Name ;      // lombok  setName
        private boolean IsTmail;   // lombok  setIsTmail
        private Long TVolumn;      // lombok  setTVolumn
 */


    @ResponseBody
    @GetMapping(value = "2")
    public String demo02(){
        String json ;
        DemoEntity02 demoEntity =    new DemoEntity02() //lombok
                .setName("HealerJean")  //  lombok    name
                .setIsTmail(true)       //  lombok    IsTmail
                .setTVolumn(100L)       //  lombok    TVolumn
                ;

        demoEntity.getName() ;
        demoEntity.getTVolumn();
        demoEntity.isIsTmail();

         json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
        return  json ;
    }


```



### 3.3、JavaBean 首字母小写


#### 3.3.1、javabean

```java


/*
 {
	 "name": "HealerJean",
	 "tVolumn": 100,
	 "tmail": true
 }


 */
@ApiModel(value = "demo实体类")
public class DemoEntity03 {


	private String name;       // JavaBean   setName     getName
	private boolean isTmail;   // JavaBean   setTmail     isTmail
	private Long tVolumn ;     // JavaBean   settVolumn  gettVolumn


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isTmail() {
		return isTmail;
	}

	public void setTmail(boolean tmail) {
		isTmail = tmail;
	}

	public Long gettVolumn() {
		return tVolumn;
	}

	public void settVolumn(Long tVolumn) {
		this.tVolumn = tVolumn;
	}
}


```


#### 3.3.2、测试


```java


    /*
        JavaBean

	private String name;       //    setName     getName
	private boolean isTmail;   //    setTmail     isTmail
	private Long tVolumn ;     //    settVolumn  gettVolumn

    {
        "name": "HealerJean",
        "tVolumn": 100,
        "tmail": true
    }

     */

    @ResponseBody
    @GetMapping(value = "3")
    public String demo03(){
        String json ;
        DemoEntity03 demoEntity =    new DemoEntity03(); //JavaBean

        demoEntity.setName("HealerJean");
        demoEntity.setTmail(true);
        demoEntity.settVolumn(100L);

        demoEntity.getName() ;
        demoEntity.isTmail();       // JavaBean isTmail
        demoEntity.gettVolumn();    // JavaBean  tVolumn

        json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
        return  json ;
    }

```


### 3.4、javabean首字母大写

#### 3.4.1、javabean

```java


/*
{
		"TVolumn": 100,
		"name": "HealerJean",
		"tmail": true
}
 */
@ApiModel(value = "demo实体类")
public class DemoEntity04 {


	private String Name;       //   setName      getName
	private boolean IsTmail;   //   setTmail     isTmail
	private Long TVolumn ;     //   setTVolumn   getTVolumn



	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public boolean isTmail() {
		return IsTmail;
	}

	public void setTmail(boolean tmail) {
		IsTmail = tmail;
	}

	public Long getTVolumn() {
		return TVolumn;
	}

	public void setTVolumn(Long TVolumn) {
		this.TVolumn = TVolumn;
	}
}


```



#### 3.4.1、测试


```java


    /*
      JavaBean

        private String Name;       //   setName      getName
        private boolean IsTmail;   //   setTmail     isTmail
        private Long TVolumn ;     //   setTVolumn   getTVolumn

        {
            "TVolumn": 100,
            "name": "HealerJean",
            "tmail": true
        }

     */
    @ResponseBody
    @GetMapping(value = "4")
    public String demo04(){
        String json ;
        DemoEntity04 demoEntity =    new DemoEntity04() ; //JavaBean

        demoEntity.setName("HealerJean");
        demoEntity.setTmail(true);
        demoEntity.setTVolumn(100L);

        demoEntity.getName() ;
        demoEntity.isTmail();       // JavaBean IsTmail
        demoEntity.getTVolumn();    // JavaBean  TVolumn

        json = JSONObject.fromObject(demoEntity).toString();
        log.info(json);
        return  json ;
    }

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
		id: 'AAAAAAAAAAAAAAA',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

