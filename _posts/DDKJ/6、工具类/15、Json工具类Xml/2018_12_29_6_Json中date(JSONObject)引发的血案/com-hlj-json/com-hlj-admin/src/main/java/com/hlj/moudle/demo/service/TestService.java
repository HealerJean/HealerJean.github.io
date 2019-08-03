package com.hlj.moudle.demo.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.hlj.data.res.test.IncludeJavaBean;
import com.hlj.data.res.test.JavaBean;
import com.hlj.data.res.test.TsJsonData;
import com.hlj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/12/14  下午8:00.
 * 1、只要是经过多层JSONObject的处理对象或者是Map处理， date就会走样，然后这样的 string类型就不会被封装，
 * 所以一般情况下，我们从Json变成对象就可以了，没事别老来回折腾，而且我们的对象到了前端之后，就会帮我们变成long类型
 */
@Slf4j
public class TestService {


    /**
     * 一、
     * 1、date类型不是long是错误的，不能转化成果
     * 2、如果将有date参数的对象 通过JSONObject 打印成String字符串，则，date会变质，不会变成long类型的了，如下
     * 但是需要注意的是，这其实只要不转化出String来，这些对象都是可以操作的，不受影响
     * <p>
     * JSONObject.fromObject(tsJsonData).toString()
     * 3、通过objectMapper的转化，一切正常 ，如下
     * JsonUtils.toJson(tsJsonData)
     */
    @Test
    public void JsonObjectForJsonToBean() {

        // 1、错误的，转化直接报错 String jsonDataStr = "{\"error\":\"0\",\"msg\":\"操作成功\",\"data\":[{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"2018-12-13 20:31:04\",\"n_integer\":1},{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"2018-12-13 20:31:04\",\"n_integer\":1}]}";
        String jsonDataStr = "{\"error\":\"0\",\"msg\":\"操作成功\",\"data\":[{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"1544782308409\",\"n_integer\":1},{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"1544782308409\",\"n_integer\":1}]}";
        log.info("\n1、JsonObjectForJsonToBean\n" + JSONObject.fromObject(jsonDataStr).toString());
        //打印结果：正常，因为没有经过对象中date的的洗礼
        //    {"error":"0","msg":"操作成功","data":[{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"1544782308409","n_integer":1},{"n_long":"3923600074","n_string":"小当","n_bigDecimal":"5.9000","n_date":"1544782308409","n_integer":1}]}

        //2、JSONObject将具有date的对象变质
        TsJsonData tsJsonData = JsonUtils.toObject(jsonDataStr, TsJsonData.class);
        log.info("\n2、JsonObjectForJsonToBean\n" + JSONObject.fromObject(tsJsonData).toString());
        //打印结果：date变质了哦
        //{"data":[{"n_bigDecimal":5.9,"n_date":{"date":14,"day":5,"hours":18,"minutes":11,"month":11,"seconds":48,"time":1544782308409,"timezoneOffset":-480,"year":118},"n_integer":1,"n_long":3923600074,"n_string":"小当"},{"n_bigDecimal":5.9,"n_date":{"date":14,"day":5,"hours":18,"minutes":11,"month":11,"seconds":48,"time":1544782308409,"timezoneOffset":-480,"year":118},"n_integer":1,"n_long":3923600074,"n_string":"小当"}],"error":"0","msg":"操作成功"}

        //3、通过objectMapper的转化
        log.info("\n3、JsonObjectForJsonToBean\n" + JsonUtils.toJson(tsJsonData));
        //打印结果： 一切正常
        //{"error":"0","msg":"操作成功","data":[{"n_long":3923600074,"n_string":"小当","n_bigDecimal":5.9000,"n_date":1544782308409,"n_integer":1},{"n_long":3923600074,"n_string":"小当","n_bigDecimal":5.9000,"n_date":1544782308409,"n_integer":1}]}

    }


    /**
     * 二.1
     * JavaBean转Map
     * 用法：可以用于Http map传参
     */
    @Test
    public void baenToMap() {

        JavaBean javaBean = getJavaBean();
        Map<String, Object> map = (Map<String, Object>) JSONObject.fromObject(javaBean);
        log.info("\nbaenToMap\n" + JsonUtils.toJson(map));
        //下面这里发现date 变质了，但是map还是可以操作的，只是不要将它转化为string字符串，并且将它输出到前台也是正常的long类型的Json数据哦
        //{"n_bigDecimal":12.12245,"n_date":{"date":14,"day":5,"hours":20,"minutes":0,"month":11,"seconds":31,"time":1544788831913,"timezoneOffset":-480,"year":118},"n_integer":100,"n_long":10045456456,"n_string":"张宇晋"}

        Map<String, Object> map2 = JsonUtils.toObject(JsonUtils.toJson(javaBean), new TypeReference<Map<String, Object>>() {
        });
        log.info(JsonUtils.toJson(map2));
        //正常,建议使用
        //{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546053984277,"n_integer":100}

    }


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
        log.info("\nMapTobaen\n" + mapStr);
        //2、正常，所以不要使用 JSONObject
        //{"n_date":1545996089637,"n_integer":100,"n_bigDecimal":12.12245,"n_string":"张宇晋","n_long":10045456456}
    }


    /**
     * Jsoninclude.NOT_NULL
     */
    @Test
    public void JSONInclude() {
        //n_string 字段为null date字段为null
        IncludeJavaBean includeJavaBean = getIncludeJavaBean();
        //1、经过ObjectMapper处理
        log.info("\nJSONInclude\n" + JsonUtils.toJson(includeJavaBean));
        //打印结果：正常 使用了上面的注解，下面如果是null则不会出现  该字段
        //{"n_long":10045456456,"n_bigDecimal":12.12245,"n_integer":100}

        //2、如果是经过 JSONObject.fromObject ，
        // null的字段会变成 空字符串"" ，
        // 日期为空，则会变成null ，所以谨慎使用
        log.info("\nJSONObject.fromObject(includeJavaBean).toString()\n" + JSONObject.fromObject(includeJavaBean).toString());
        //打印结果：
        // {"n_bigDecimal":12.12245,"n_date":null,"n_integer":100,"n_long":10045456456,"n_string":""}


        /**
         * json转对象
         */
        String jsonNull = "{\"n_long\":10045456456,\"n_bigDecimal\":12.12245,\"n_integer\":100}";
        String jsonPramsNull = "{\"n_bigDecimal\":12.12245,\"n_date\":null,\"n_integer\":100,\"n_long\":10045456456,\"n_string\":\"\"}";

        IncludeJavaBean mapperjavaBean = JsonUtils.toObject(jsonNull, IncludeJavaBean.class);
        //不存在显示 null
        //IncludeJavaBean(n_long=10045456456, n_string=null, n_bigDecimal=12.12245, n_date=null, n_integer=100)
        IncludeJavaBean mapperParamsNulljavaBean = JsonUtils.toObject(jsonPramsNull, IncludeJavaBean.class);
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

    /**
     * json转化为数组
     */
    @Test
    public void JsonToList() {

        List<JavaBean> javaBeans = new ArrayList<>();
        JavaBean javaBean1 = getJavaBean();

        JavaBean javaBean2 = getJavaBean();
        javaBeans.add(javaBean1);
        javaBeans.add(javaBean2);

        //list转化为string字符串，这里可别用JSONObject了，这个家伙太完蛋，一下子就会将date变质
        String jsonArrayStr = JsonUtils.toJson(javaBeans);
        log.info("\nJsonUtils.toJson(jsonArrayStr)\n" + jsonArrayStr);
        //打印结果 正常打印
        // [{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546051110172,"n_integer":100},{"n_long":10045456456,"n_string":"HealerJean","n_bigDecimal":12.12245,"n_date":1546051110172,"n_integer":100}]


        //2、转化为数组
        List<JavaBean> lendReco = JsonUtils.toObject(jsonArrayStr, new TypeReference<List<JavaBean>>() {
        });
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


    /**
     * 假数据JavaBean
     *
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
     *
     * @return
     */
    private Map<String, Object> getJavaBeanMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("n_long", 10045456456L);
        map.put("n_string", "张宇晋");
        map.put("n_bigDecimal", new BigDecimal("12.12245"));
        map.put("n_date", new Date());
        map.put("n_integer", 100);
        return map;
    }

    /**
     * 假数据 IncludeJavaBean 用于测试 include.not_null
     *
     * @return
     */
    private IncludeJavaBean getIncludeJavaBean() {
        IncludeJavaBean includeJavaBean = new IncludeJavaBean();
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


}
