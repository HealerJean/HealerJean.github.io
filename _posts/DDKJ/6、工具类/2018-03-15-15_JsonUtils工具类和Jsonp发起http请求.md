---
title: JsonUtils工具类和Jsonp发起http请求
date: 2018-03-15 22:33:00
tags: 
- JSON
category: 
- JSON
description: JsonUtils工具类和Jsonp发起http请求
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages

<font color="red"></font>
-->

## 前言

Jsonp用来调用http请求的，非常小巧而且方便


## 回顾之前所学

二者依赖如下

```
<!-- jsoup包依赖 -->
<dependency>
	<groupId>org.jsoup</groupId>
	<artifactId>jsoup</artifactId>
	<version>1.11.1</version>
</dependency>

<!--json-->
<dependency>
	<groupId>net.sf.json-lib</groupId>
	<artifactId>json-lib</artifactId>
	<version>2.4</version>
	<classifier>jdk15</classifier>
</dependency>


```

## 1、JSONObject 和 JSONArray

### 1.1、JSONArray

```
JSONArray jsonArray = JSONArray.fromObject(channelJsonString);
List<ChannelJson> channelJsons = new ArrayList<>();
for (int i =0; i < jsonArray.size(); i++) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
        ChannelJson channelJson =  objectMapper.readValue(jsonArray.get(i).toString(),ChannelJson.class);
        if(channelJson.getValue()!=Integer.parseInt(channelListsEnableValue)){
            channelJson.setChecked(false);
            channelJson.setWeight(0);
        }
        channelJsons.add(channelJson);

    } catch (IOException e) {
        e.printStackTrace();
    }

}

skinAppInfoCheck.setChannelJson(JSONArray.fromObject(channelJsons).toString());

```

### 1.2、JSONObject


```
String id = jsonObject.put("id")；

@RequestMapping("query/uuid/packages")
public void findUuidPackages(String uuid, HttpServletResponse response) throws Exception {

    JSONArray jsonArray = new JSONArray();


        for (OSSObjectSummary summary : summaryList) {
            String bundleId = StringUtils.substringAfter(summary.getKey(),"-");
         
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bundleId",bundleId);
            jsonArray.put(jsonObject);
        }
    }
    response.getWriter().write(jsonArray.toString());
}



//第二种keyset，有时候可能里面的key是随机的，比如ddkj的idfa的标准格式
{"EED2EE41-7EDA-4C89-9AC0-B95B226F44E1":"0"}

String body = connection.body();
JSONObject jsonObject = JSONObject.fromObject(body);
for (Object key : jsonObject.keySet()) {
    Object res = jsonObject.get(key);
    if ("1".equals(res.toString())) {
       // System.out.println(i++);
        //System.out.println("idfa为"+thisIdfaflag.getIdfa());
        System.out.println(thisIdfaflag.getIdfa());

    }
}


//3、jsonObject 加强


@RequestMapping("loadAppSpreadNew")
@ResponseBody
public ResponseBean loadAppSpreadNew(String trackId){
      AppsApp  appsAppNull =new AppsApp();

	 String content = HttpHelper.handleGet(String.format("https://itunes.apple.com/lookup?id=%s&country=cn", trackId));
	 
	 JSONObject jsonObject = JSONObject.fromObject(content);
	 
	 //1、判断是不是存在节点
	 if (!jsonObject.has("resultCount") || !jsonObject.has("results")){
	     throw new AppException(ErrorCodeEnum.逻辑错误.code,"未找到对应的应用");
	     }
	     
	//2、json中包含json数组，取数第一个  
    JSONObject target = jsonObject.getJSONArray("results").getJSONObject(0);
	
	//3、获取string
appsAppNull.setTrackName(target.getString("trackName"));
	
	 //4、获取Long类型数据
	 	 appsAppNull.setFileSizeBytes((Long.valueOf(target.get("fileSizeBytes").toString())));
	
	// 5、后去金额 BigDecimal
	 BigDecimal prive = new BigDecimal(target.getString("price"));
	 appsAppNull.setPrice(prive);
	
	 return ResponseBean.buildSuccess(appsAppNull);
	

            }
        }
    }
    return ResponseBean.buildFailure();
}





```


### 1.3、JsonNode


```
@Test
public void jsonNode() throws IOException {

    String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";
    ObjectMapper mapper = new ObjectMapper();
    //JSON ----> JsonNode
    JsonNode rootNode = mapper.readTree(json);
    Iterator<String> keys = rootNode.fieldNames();
    while(keys.hasNext()){
        String fieldName = keys.next();
        System.out.println(fieldName + ": " + rootNode.path(fieldName).toString());
    }
    //JsonNode ----> JSON
    System.out.println(mapper.writeValueAsString(rootNode));
}

```




### 3、JsonUtils的使用 

#### 1、转化成对象


```
/**
 *   JsonUtils.toObject
 */
public static ZbtUser getUser(String json

    ZbtUser info = JsonUtils.toObject(json, ZbtUser.class);
    
    return info;
}

``` 


```
package com.hlj.ddkj.Jsonp;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Utils - JSON
 */
public final class JsonUtils {

    /**
     * ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // jackson 1.9 and before
        //objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // or jackson 2.0
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 不可实例化
     */
    private JsonUtils() {
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param value 对象
     * @return JSON字符串
     */
    public static String toJson(Object value) {
        Assert.notNull(value, "json 不允许为空");
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json      JSON字符串
     * @param valueType 类型
     * @return 对象
     */
    public static <T> T toObject(String json, Class<T> valueType) {
        Assert.hasText(json, "json 不允许为空");
        Assert.notNull(valueType, "valueType 不允许为空");
        try {
            return OBJECT_MAPPER.readValue(json, valueType);
        } catch (JsonParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json          JSON字符串
     * @param typeReference 类型
     * @return 对象
     */
    public static <T> T toObject(String json, TypeReference<?> typeReference) {
        Assert.hasText(json, "json 不允许为空");
        Assert.notNull(typeReference, "typeReference 不允许为空");
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json     JSON字符串
     * @param javaType 类型
     * @return 对象
     */
    public static <T> T toObject(String json, JavaType javaType) {
        Assert.hasText(json, "json 不允许为空");
        Assert.notNull(javaType, "javaType 不允许为空");
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (JsonParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为树
     *
     * @param json JSON字符串
     * @return 树
     */
    public static JsonNode toTree(String json) {
        Assert.hasText(json, "json 不允许为空");
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为JSON流
     *
     * @param writer Writer
     * @param value  对象
     */
    public static void writeValue(Writer writer, Object value) {
        Assert.notNull(writer, "writer 不允许为空");
        Assert.notNull(value, "value 不允许为空");
        try {
            OBJECT_MAPPER.writeValue(writer, value);
        } catch (JsonGenerationException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 构造类型
     *
     * @param type 类型
     * @return 类型
     */
    public static JavaType constructType(Type type) {
        Assert.notNull(type, "type 不允许为空");
        return TypeFactory.defaultInstance().constructType(type);
    }

    /**
     * 构造类型
     *
     * @param typeReference 类型
     * @return 类型
     */
    public static JavaType constructType(TypeReference<?> typeReference) {
        Assert.notNull(typeReference, "typeReference 不允许为空");
        return TypeFactory.defaultInstance().constructType(typeReference);
    }

}

```

## 2、Jsonp


```

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JsonpMain {

    @Test
    public void testGet() throws IOException {

        //data中可以放入我们想要的参数，map类型或者是其他的自己看
        //method()后面没有其他方法
        Map<String,String> data =  new HashMap<>();
        Map<String,String> headers = new HashMap<>();

        Connection.Response connection = Jsoup.connect("http://www.baidu.com")
                                                .data(data)
                                                .headers(headers)
                                                .method(Connection.Method.GET)
                                                .ignoreContentType(true)
                                                .validateTLSCertificates(false)
                                                .execute();

        String body =  connection.body();
        log.info(body);
    }
}


```




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
		id: 'eDso3iazNfRQzpbC',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

