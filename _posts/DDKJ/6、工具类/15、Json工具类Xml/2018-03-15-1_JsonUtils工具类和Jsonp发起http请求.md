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
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages

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


#### 1.2.2、拼装一个json

需要什么房什么进来就行了就是用了个StringBuffer()没啥

```
 */
private static TreeMap packageRequestParams(TreeMap params,
                                            String appKey, String sign) {
    StringBuffer buffer = new StringBuffer();
    /**
     * 拼接系统参数
     */
    buffer.append("{");
    buffer.append("\"client_id\":");
    buffer.append("\"");
    buffer.append(appKey);
    buffer.append("\",");
    buffer.append("\"timestamp\":");
    buffer.append("\"");
    buffer.append(params.get("timestamp"));
    buffer.append("\",");
    buffer.append("\"version\":");
    buffer.append("\"3.0\",");
    buffer.append("\"sign\":");
    buffer.append("\"");
    buffer.append(sign);
    buffer.append("\",");

    buffer.append("\"sign_method\":");
    buffer.append("\"md5\",");

    buffer.append("\"access_token\":");
    buffer.append("\"\",");

    buffer.append("\"action\":");
    buffer.append("\"");
    buffer.append(params.get("action"));
    buffer.append("\"");


    buffer.append("}");
    params.put("opensysparams", buffer.toString());

    return params;
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


 private boolean sendSms(String mobile, int tplid, String tpl_value) {
        String url = "http://v.juhe.cn/sms/send?mobile=" + mobile + "&tpl_id=" + tplid + "&tpl_value=%23code%23%3d" + tpl_value + "&key=" + KEY;
        SmsRecord smsRecord = new SmsRecord();
        int errorCode = 0;
        try {
            Document document = Jsoup.connect(url).ignoreContentType(true).get();
            String result = document.body().text();
            JsonNode jsonNode = JsonUtils.toTree(result);
            errorCode = jsonNode.get("error_code").intValue();
            String reason = jsonNode.get("reason").asText();
            smsRecord.setCdate(new Date()).setErrorCode(errorCode).setReason(reason).setMobile(mobile).setTplId(tplid + "").setValue(tpl_value);
            if (errorCode == 0){
                return true;
            }
        } catch (IOException e) {
            log.error("发送短信失败：" + mobile + "\t" + tplid + "\t" + tpl_value, e);
        } finally {
            smsRecord.setStatus(errorCode == 0 ? 1 : 0);
            smsRecordRepository.save(smsRecord);
        }
        return false;
    }



```




### 3、JsonUtils的使用 

#### 1、转化成对象


```java
/**
 *   JsonUtils.toObject
 */
public static ZbtUser getUser(String json

    ZbtUser info = JsonUtils.toObject(json, ZbtUser.class);
    
    return info;
}

``` 

### 4、json数组转化成对象


```java


   //2、转化为数组
   List<JavaBean> lendReco =  JsonUtils.toObject(jsonArrayStr, new TypeReference<List<JavaBean>>() { });
   String lendRecoStr = JsonUtils.toJson(lendReco);
   System.out.println(lendRecoStr);
   //打印结果 正常打印
   //[{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546052196927,"n_integer":100},{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546052196927,"n_integer":100}]



//第一种方式，只针对没有使用lombok注解的情况 ，这种方式不论有没有 date类型 使用了@Data注解 就会出现下面的问题，
// 有了date那也肯定的会是这种结局 ，
//      使用的时候下面这种的时候，不要用@Data注解，所以以后就不要使用了，jsonArray来源需要是原生的
//  javaBeans = JSONArray.toList(jsonArray,new JavaBean() ,new JsonConfig());
//        net.sf.json.JSONObject - Property 'n_bigDecimal' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.129 [main] INFO  net.sf.json.JSONObject - Property 'n_date' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_integer' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 javaBeans[main] INFO  net.sf.json.JSONObject - Property 'n_long' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_string' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_bigDecimal' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_date' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.131 [main] INFO  net.sf.json.JSONObject - Property 'n_integer' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.131 [main] INFO  net.sf.json.JSONObject - Property 'n_long' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.131 [main] INFO  net.sf.json.JSONObject - Property 'n_string' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.



```

### 5、工具类



```java


package com.hlj.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
     * @param typeReference 类型 可以通过这个转化为List集合 ，举例：
     * List<JavaBean> list =  JsonUtils.toObject(jsonArrayStr, new TypeReference<List<JavaBean>>() { });
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
     * 判断是jsonobject还是jsonArrsy
     * 如果是jsonObject则是1
     * 如果是jsonArrsy 则是2
     * 如果二者都不是则返回0
     * @param json
     * @return
     */
    public Integer judgeJson(String json){
        Assert.hasText(json, "json 不允许为空");
        Object jsonT = new JSONTokener(json).nextValue();
        if(jsonT instanceof JSONObject){
            return 1;
        }else if (jsonT instanceof JSONArray) {
            return 2 ;
        }else {
            return  0;
        }
    }


    /**
     * 构造类型
     * @param type 类型,类，接口，枚举字段类型 （java.class）
     * @return 类型
     */
    public static JavaType constructType(Type type) {
        Assert.notNull(type, "type 不允许为空");
        return TypeFactory.defaultInstance().constructType(type);
    }

    /**
     * 构造类型
     * @param typeReference 类型 new TypeReference<List<JavaBean>>() { }
     * @return 类型
     */
    public static JavaType constructType(TypeReference<?> typeReference) {
        Assert.notNull(typeReference, "typeReference 不允许为空");
        return TypeFactory.defaultInstance().constructType(typeReference);
    }



    /**
     * @param resString
     * @return String
     * @Description Json 格式化到控制台打印
     *
     */
    public static   String responseFormat(String resString){

        StringBuffer jsonForMatStr = new StringBuffer();
        int level = 0;
        for(int index=0;index<resString.length();index++)//将字符串中的字符逐个按行输出
        {
            //获取s中的每个字符
            char c = resString.charAt(index);

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0  && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }
        /**
         * @param level
         * @return
         * @throws
         * @author lgh
         * @date 2018/10/29-14:29
         */
        private static String getLevelStr(int level) {
            StringBuffer levelStr = new StringBuffer();
            for (int levelI = 0; levelI < level; levelI++) {
                levelStr.append("\t");
            }
            return levelStr.toString();
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


## 3、，Map和Bean之间的转化


```java

Map<String, Object> map = (Map<String, Object>)JSONObject.fromObject(javaBean);

## 
```



<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|



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
		id: 'eDso3iazNfRQzpbC',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

