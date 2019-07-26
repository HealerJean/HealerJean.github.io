---
title: json中date(JSONObject)引发的血案
date: 2018-12-14 03:33:00
tags: 
- Json
category: 
- Json
description: json中date(JSONObject)引发的血案
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>

<font  color="red" size="4">   </font>


<font size="4">   </font>
-->

## 前言

1、通过objectMapper的转化，如果对象有字段为null，则不会打印  

```json
   JsonUtils.toJson(tsJsonData)
```

2、@JsonInclude(JsonInclude.Include.NON_NULL) 并且在mapper中配置了，所以它没有起到效果，如果mapper中不配置，那么它在下面是会生效的，
     

## 1、测试bean

### 1、包含集合的测试bean

```java

package com.hlj.data.res.test;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Desc: json测试实体
 * @Author HealerJean
 * @Date 2018/9/25  上午11:10.
 *
 */
@Data
@Accessors(chain = true)
@ApiModel (description = "json测试实体")
public class TsJsonData {


    /**
     {"error":"0","msg":"操作成功","data":[{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1},{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1}]}
     * error : 0
     * msg : 操作成功
     * data : [{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1},{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"2018-12-13 20:31:04","n_integer":1}]
     */

    private String error;
    private String msg;
    private List<DataBean> data;

    @Data
    @Accessors(chain = true)
    public static class DataBean {
        /**
         * n_long : 3923600074
         * n_string : name
         * n_bigDecimal : 5.9000
         * n_date : 2018-12-13 20:31:04
         * income_rate : 0.9000
         * n_integer : 1
         */

        private Long n_long;
        private String n_string;
        private BigDecimal n_bigDecimal;
        private Date n_date;
        private Integer n_integer;


    }
}

```




### 2、普通Bean

```java

package com.hlj.data.res.test;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/12/14  下午7:56.
 * 类描述：
 */
@Data
@Accessors(chain = true)
public class JavaBean implements Serializable {

    private Long n_long;
    private String n_string;
    private BigDecimal n_bigDecimal;
    private Date n_date;
    private Integer n_integer;

}


```


### 3、不返回null的bean


```java
package com.hlj.data.res.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/12/28  下午7:36.
 * 类描述：
 */

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IncludeJavaBean {

        private Long n_long;
        private String n_string;
        private BigDecimal n_bigDecimal;
        private Date n_date;
        private Integer n_integer;


}


```


## 2、开始正儿八经测试


### 3.0、准备假数据


```java



    /**
     * 假数据JavaBean
     * @return
     */
    private JavaBean getJavaBean() {
        JavaBean javaBean = new JavaBean();
        javaBean.setN_long(10045456456L);
        javaBean.setN_bigDecimal(new BigDecimal("12.12245"));
        javaBean.setN_date(new Date());
        javaBean.setN_integer(100);
        javaBean.setN_string("HealerJean");

        return javaBean;
    }


    /**
     * 假数据JavaBean 对应的 Map
     * @return
     */
    private Map<String, Object> getJavaBeanMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("n_long", 10045456456L);
        map.put("n_string", "张宇晋");
        map.put("n_bigDecimal",new BigDecimal("12.12245") );
        map.put("n_date",new Date() );
        map.put("n_integer",100 );
        return map;
    }

    /**
     * 假数据 IncludeJavaBean 用于测试 include.not_null
     * @return
     */
    private IncludeJavaBean getIncludeJavaBean() {
        IncludeJavaBean  includeJavaBean = new IncludeJavaBean();
        includeJavaBean.setN_long(10045456456L);
        includeJavaBean.setN_bigDecimal(new BigDecimal("12.12245"));
        includeJavaBean.setN_date(null);
        includeJavaBean.setN_integer(100);
        /**
         * 下面和这个设置为null
         */
        includeJavaBean.setN_string(null);

        return includeJavaBean;
    }

```



### 3.1、date类型必须是long
### 3.2、`JSONObcjet`不可以随便使用，和对象一起用的时候，会将date变质
### 3.3、强烈要求使用`objectMapper`

```java


    /**
     * 一、
     * 1、date类型不是long是错误的，不能转化成果
     * 2、如果将有date参数的对象 通过JSONObject 打印成String字符串，则，date会变质，不会变成long类型的了，如下
     *   但是需要注意的是，这其实只要不转化出String来，这些对象都是可以操作的，不受影响
     *
     *     JSONObject.fromObject(tsJsonData).toString()
     */
    @Test
    public void JsonObjectForJsonToBean(){

        // 1、错误的，转化直接报错 String jsonDataStr = "{\"error\":\"0\",\"msg\":\"操作成功\",\"data\":[{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"2018-12-13 20:31:04\",\"n_integer\":1},{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"2018-12-13 20:31:04\",\"n_integer\":1}]}";
        String jsonDataStr = "{\"error\":\"0\",\"msg\":\"操作成功\",\"data\":[{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"1544782308409\",\"n_integer\":1},{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"1544782308409\",\"n_integer\":1}]}";
        log.info("\n1、JsonObjectForJsonToBean\n"+JSONObject.fromObject(jsonDataStr).toString());
        //打印结果：正常，因为没有经过对象中date变质
        //    {"error":"0","msg":"操作成功","data":[{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"1544782308409","n_integer":1},{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"1544782308409","n_integer":1}]}

        //2、JSONObject将具有date的对象进行洗礼
        TsJsonData tsJsonData =   JsonUtils.toObject(jsonDataStr, TsJsonData.class );
        log.info("\n2、JsonObjectForJsonToBean\n"+JSONObject.fromObject(tsJsonData).toString());
        //打印结果：date变质了哦
        //{"data":[{"n_bigDecimal":5.9,"n_date":{"date":14,"day":5,"hours":18,"minutes":11,"month":11,"seconds":48,"time":1544782308409,"timezoneOffset":-480,"year":118},"n_integer":1,"n_long":3923600074,"n_string":"小当"},{"n_bigDecimal":5.9,"n_date":{"date":14,"day":5,"hours":18,"minutes":11,"month":11,"seconds":48,"time":1544782308409,"timezoneOffset":-480,"year":118},"n_integer":1,"n_long":3923600074,"n_string":"小当"}],"error":"0","msg":"操作成功"}

        //3、通过objectMapper的转化
        log.info("\n3、JsonObjectForJsonToBean\n"+JsonUtils.toJson(tsJsonData));
        //打印结果： 一切正常
        //{"error":"0","msg":"操作成功","data":[{"n_long":3923600074,"n_string":"小当","n_bigDecimal":5.9000,"n_date":1544782308409,"n_integer":1},{"n_long":3923600074,"n_string":"小当","n_bigDecimal":5.9000,"n_date":1544782308409,"n_integer":1}]}

    }



```



### 3.3、JavaBean转map

```java

  /** 二.1
     * JavaBean转Map
     * 用法：可以用于Http map传参
     */
    @Test
    public void baenToMap() {

        JavaBean javaBean = getJavaBean();
        Map<String, Object> map = (Map<String, Object>)JSONObject.fromObject(javaBean);
        log.info("\nbaenToMap\n"+JsonUtils.toJson(map));
        //下面这里发现date 变质了，但是map还是可以操作的，只是不要将它转化为string字符串，并且将它输出到前台也是正常的long类型的Json数据哦
        //{"n_bigDecimal":12.12245,"n_date":{"date":14,"day":5,"hours":20,"minutes":0,"month":11,"seconds":31,"time":1544788831913,"timezoneOffset":-480,"year":118},"n_integer":100,"n_long":10045456456,"n_string":"张宇晋"}

        Map<String, Object> map2= JsonUtils.toObject(JsonUtils.toJson(javaBean),new TypeReference<Map<String, Object>>( ){} );
        log.info(JsonUtils.toJson(map2));
        //正常
        //{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546053984277,"n_integer":100}

    }

    
   
```



#### 3.3.2、将变质的日期输出到前台


```java

    @GetMapping("getMapHaveDateFalse")
    @ResponseBody
    public Map<String ,Object> getMap(){

        JavaBean javaBean = getJavaBean();
        Map<String, Object> map = (Map<String, Object>) JSONObject.fromObject(javaBean);

        return map ;
    }



```



### 3.4、Map转javabean


```java



    /**
     * 二.2
     * Map转JavaBean
     */
    @Test
    public void MapTobaen() {

        Map<String, Object> map = getJavaBeanMap();
        //      String mapStr = JSONObject.fromObject(map).toString();
        //1、 打印结果：date变质
        //{"n_date":{"date":28,"day":5,"hours":19,"minutes":22,"month":11,"seconds":56,"time":1545996176773,"timezoneOffset":-480,"year":118},"n_integer":100,"n_bigDecimal":12.12245,"n_string":"张宇晋","n_long":10045456456}

        String mapStr = JsonUtils.toJson(map);
        log.info("\nMapTobaen\n"+mapStr);
        //2、正常，所以不要使用 JSONObject
        //{"n_date":1545996089637,"n_integer":100,"n_bigDecimal":12.12245,"n_string":"张宇晋","n_long":10045456456}
    }
    

```



### 3.5、@JsonInclude(JsonInclude.Include.NON_NULL)（这里用上其实是错误的，不是因为它生效的，而是mapper中配置了，如果不配置，则是它生效的）    



```java


/**
     * Jsoninclude.NOT_NULL
     */
    @Test
    public void JSONInclude(){
        //n_string 字段为null date字段为null
        IncludeJavaBean includeJavaBean = getIncludeJavaBean() ;
        //1、经过ObjectMapper处理
        log.info("\nJSONInclude\n"+JsonUtils.toJson(includeJavaBean));
        //打印结果：正常 使用了上面的注解，下面如果是null则不会出现  该字段
        //{"n_long":10045456456,"n_bigDecimal":12.12245,"n_integer":100}

        //2、如果是经过 JSONObject.fromObject ，
        // null的字段会变成 空字符串"" ，
        // 日期为空，则会变成null ，所以谨慎使用
        log.info("\nJSONObject.fromObject(includeJavaBean).toString()\n"+JSONObject.fromObject(includeJavaBean).toString());
        //打印结果：
        // {"n_bigDecimal":12.12245,"n_date":null,"n_integer":100,"n_long":10045456456,"n_string":""}


        /**
         * json转对象
         */
        String jsonNull = "{\"n_long\":10045456456,\"n_bigDecimal\":12.12245,\"n_integer\":100}" ;
        String jsonPramsNull = "{\"n_bigDecimal\":12.12245,\"n_date\":null,\"n_integer\":100,\"n_long\":10045456456,\"n_string\":\"\"}" ;

        IncludeJavaBean mapperjavaBean =JsonUtils.toObject(jsonNull,IncludeJavaBean.class );
        //不存在显示 null
        //IncludeJavaBean(n_long=10045456456, n_string=null, n_bigDecimal=12.12245, n_date=null, n_integer=100)
        IncludeJavaBean mapperParamsNulljavaBean =JsonUtils.toObject(jsonPramsNull,IncludeJavaBean.class );
        //空字符串显示 "" null表示null
        //IncludeJavaBean(n_long=10045456456, n_string=, n_bigDecimal=12.12245, n_date=null, n_integer=100)

        //下面二者同上
        IncludeJavaBean javaBean = JsonUtils.toObject(JSONObject.fromObject(jsonNull).toString(), IncludeJavaBean.class);
        //IncludeJavaBean(n_long=10045456456, n_string=null, n_bigDecimal=12.12245, n_date=null, n_integer=100)
         IncludeJavaBean javaParamsNullBean = JsonUtils.toObject(JSONObject.fromObject(jsonPramsNull).toString(), IncludeJavaBean.class);
        //IncludeJavaBean(n_long=10045456456, n_string=, n_bigDecimal=12.12245, n_date=null, n_integer=100)

        System.out.println(mapperjavaBean.toString());
        System.out.println(mapperParamsNulljavaBean.toString());
        System.out.println(javaBean.toString());
        System.out.println(javaParamsNullBean.toString());


    }

```





### 3.6、json转化为list集合


```java

    /**
     * json转化为数组
     */
    @Test
    public void JsonToList(){

        List<JavaBean> javaBeans = new ArrayList<>();
        JavaBean javaBean1 = getJavaBean();

        JavaBean javaBean2 = getJavaBean();
        javaBeans.add(javaBean1);
        javaBeans.add(javaBean2);

        //list转化为string字符串，这里可别用JSONObject了，这个家伙太完蛋，一下子就会将date变质
        String jsonArrayStr = JsonUtils.toJson(javaBeans);
        log.info("\nJsonUtils.toJson(jsonArrayStr)\n"+jsonArrayStr);
        //打印结果 正常打印
        // [{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546051110172,"n_integer":100},{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546051110172,"n_integer":100}]


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

    }

```



## 3、工具类


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

/**
 * Utils - JSON
 */
public final class JsonUtils {

    /**
     * ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectMapper  objectMapperSensitivity = new ObjectMapper();


    static {


        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //NULL 不打印
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 排除json字符串中实体类没有的字段
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        //LocalDateTime   LocalDate LocalTime 转化成 String
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);




        //脱敏日志创建
        objectMapperSensitivity.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        objectMapperSensitivity.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapperSensitivity.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        objectMapperSensitivity.registerModule(javaTimeModule);
        objectMapperSensitivity.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //脱敏
        objectMapperSensitivity.setSerializerFactory(objectMapperSensitivity.getSerializerFactory().withSerializerModifier(new SensitiveSerializerModifier()));

    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }


    /**
     * 将对象转换为JSON字符串
     *
     * @param value 对象
     * @return JSON字符串
     */
    public static String toJsonString(Object value) {
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
     * @param c 类型
     * @return 对象
     */
    public static <T> T toObject(String json, Class<T> c) {
        try {
            return OBJECT_MAPPER.readValue(json, c);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }





    /**
     * json格式字符串转对象
     * @param json
     * @param c
     * @return object 仅限于 ArrayList HashSet 等集合对象
     */
    public static<T>  T jsonToArray(String json, Class c ,Class collect){
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collect, c);
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e){
            throw new RuntimeException("参数格式有误");
        }
    }

    

     /**
     * 将JSON字符串转换为集合（也可以是set） 不可以是对象
     * @param json          JSON字符串
     * @param typeReference 类型 可以通过这个转化为List集合 ，举例：
     *                      List<JavaBean> list =  JsonUtils.toObject(jsonArrayStr, new TypeReference<List<JavaBean>>() { });
     *                      Map<String, Object> map =  JsonUtils.toObject(JsonUtils.toJson(javaBean),new TypeReference<Map<String, Object>>( ){} );
     * @return 对象
     */
    public static <T> T toObjectCollect(String json,  TypeReference<?> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
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
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 将JSON字符串转换为树
     * @param json JSON字符串
     * @return 树
     */
    public static JsonNode toTree(String json) {
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
     * 将来通过 OBJECT_MAPPER.readValue(json, javaType) 转化为对象
     * @param type  （java.class）
     * @return 类型
     */
    public static JavaType constructType(Type type) {
        return OBJECT_MAPPER.getTypeFactory().constructType(type);
    }

    /**
     * 将来通过 OBJECT_MAPPER.readValue(json, javaType) 转化为 集合
     * @param typeReference 类型 new TypeReference<List<JavaBean>>() { }
     * @return 类型
     */
    public static JavaType constructType(TypeReference<?> typeReference) {
        return OBJECT_MAPPER.getTypeFactory().constructType(typeReference);
    }


    /**
     * @param resString
     * @return String
     * @Description Json 格式化到控制台打印
     */
    public static String responseFormat(String resString) {

        StringBuffer jsonForMatStr = new StringBuffer();
        int level = 0;
        ////将字符串中的字符逐个按行输出
        for (int index = 0; index < resString.length(); index++) {
            //获取s中的每个字符
            char c = resString.charAt(index);

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
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
     */
    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

```



## 4、Json的一常见问题



### 4.1、集合和数组其实打印出来是一样的

```
public static void main(String[] args) {

     List<String> longs = new ArrayList<>();
     longs.add("a");
     longs.add("b");

     String []arary = {"a","b"};

     System.out.println(JsonUtils.toJsonString(longs));
     System.out.println(JsonUtils.toJsonString(arary));

 }
 
 打印结果
 
["a","b"]
["a","b"]
```

### 4.2、JSONArray,当做一个普通的数组（不一定内部是JSONObject）





```
 [
 { "id" : "123", "courseID" : "huangt-test", "title" : "提交作业" } ,
 { "content" : null, "beginTime" : 1398873600000 "endTime" } 
 ] ； 
 
 
使用

 大家可以把JSONArray当成一般的数组来对待，只是获取的数据内数据的方法不一样
，但是也有内部不是JsonObjec的情况，一定要注意
 JSONObject jsonObject = (JSONObject)jsonArray.get(i);
 JSONObject jsonObject = jsonArray.getJSONObject(i) ; 

```



<br/><br/><br/>
<font color="red"> 感兴趣的，欢迎添加博主微信， </font><br/>
哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。
<br/>
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
		id: 'zaCZviR43AWdTIH7**',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

