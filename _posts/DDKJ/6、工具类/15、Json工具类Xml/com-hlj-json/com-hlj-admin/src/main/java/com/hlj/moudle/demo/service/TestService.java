package com.hlj.moudle.demo.service;

import com.hlj.data.res.test.JavaBean;
import com.hlj.data.res.test.TsJsonData;
import com.hlj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/12/14  下午8:00.
 *  1、只要是经过多层Json的处理对象或者是Map处理， date就会走样，然后这样的 string类型就不会被封装，
 *  所以一般情况下，我们从Json变成对象就可以了，没事别老来回折腾，而且我们的对象到了前端之后，就会帮我们变成long类型
 */
@Slf4j
public class TestService {

    /**
     * json 转 JavaBean
     * 1、 date类型的Json转化为对象，必须是Long类型的，不可以是String类型的'2018-12-13 20:31:04'
     */
    @Test
    public void josnToBean() {

//  错误的   String jsonStr = "{\"error\":\"0\",\"msg\":\"操作成功\",\"data\":[{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"2018-12-13 20:31:04\",\"n_integer\":1},{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"2018-12-13 20:31:04\",\"n_integer\":1}]}";
        String jsonDataStr = "{\"error\":\"0\",\"msg\":\"操作成功\",\"data\":[{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"1544782308409\",\"n_integer\":1},{\"n_long\":\"3923600074\",\"n_string\":\"小当\",\"n_bigDecimal\":\"5.9000\",\"n_date\":\"1544782308409\",\"n_integer\":1}]}";
        TsJsonData tsJsonData =   JsonUtils.toObject(jsonDataStr, TsJsonData.class );

        log.info(JSONObject.fromObject(tsJsonData).toString());
//        {"data":[{"n_bigDecimal":5.9,"n_date":{"date":14,"day":5,"hours":18,"minutes":11,"month":11,"seconds":48,"time":1544782308409,"timezoneOffset":-480,"year":118},"n_integer":1,"n_long":3923600074,"n_string":"小当"},{"n_bigDecimal":5.9,"n_date":{"date":14,"day":5,"hours":18,"minutes":11,"month":11,"seconds":48,"time":1544782308409,"timezoneOffset":-480,"year":118},"n_integer":1,"n_long":3923600074,"n_string":"小当"}],"error":"0","msg":"操作成功"}

        JavaBean javaBean1 = getJavaBean();
        log.info(JsonUtils.toJson(javaBean1));
//        {"n_long":10045456456,"n_string":"张宇晋","n_bigDecimal":12.12245,"n_date":1544791847711,"n_integer":100}

    }



    /**
     * JavaBean转 json
     * 1、 JavaBean转内部包含集合
     */
    @Test
    public void beanToJson() {

        TsJsonData tsJsonData = new TsJsonData().setError("0").setMsg("success");
        TsJsonData.DataBean dataBean = new TsJsonData.DataBean();
        dataBean.setN_long(10045456456L);
        dataBean.setN_bigDecimal(new BigDecimal("12.12245"));
        dataBean.setN_date(new Date());
        dataBean.setN_integer(100);
        dataBean.setN_string("张宇晋");


        TsJsonData.DataBean dataBean2 = new TsJsonData.DataBean();
        String []jgnore = {"n_long","n_string"};
        BeanUtils.copyProperties(dataBean,dataBean2,jgnore );
        dataBean2.setN_long(45456L);
        dataBean2.setN_string("HealerJean");

        List< TsJsonData.DataBean> dataBeans = new ArrayList<>();
        dataBeans.add(dataBean);
        dataBeans.add(dataBean2);
        tsJsonData.setData(dataBeans);

        log.info(JSONObject.fromObject(tsJsonData).toString());
        //只要是经过多层Json的处理 date就会走样
        //{"data":[{"n_bigDecimal":12.12245,"n_date":{"date":14,"day":5,"hours":20,"minutes":48,"month":11,"seconds":17,"time":1544791697252,"timezoneOffset":-480,"year":118},"n_integer":100,"n_long":10045456456,"n_string":"张宇晋"},{"n_bigDecimal":12.12245,"n_date":{"date":14,"day":5,"hours":20,"minutes":48,"month":11,"seconds":17,"time":1544791697252,"timezoneOffset":-480,"year":118},"n_integer":100,"n_long":45456,"n_string":"HealerJean"}],"error":"0","msg":"success"}


    }

    /**
     * JavaBean转Map
     * Map<String, Object> map = (Map<String, Object>)JSONObject.fromObject(javaBean);
     * 用法：可以用于Http map传参
     */
    @Test
    public void baenToMap() {

        JavaBean javaBean = getJavaBean();

        Map<String, Object> map = (Map<String, Object>)JSONObject.fromObject(javaBean);

        log.info(map.toString());//打印出来默认是Json类型的
        //下面这里发现date 编制了，它自己变成了一个数组
        //{"n_bigDecimal":12.12245,"n_date":{"date":14,"day":5,"hours":20,"minutes":0,"month":11,"seconds":31,"time":1544788831913,"timezoneOffset":-480,"year":118},"n_integer":100,"n_long":10045456456,"n_string":"张宇晋"}

    }



    /**
     * Map转JavaBean
     */
    @Test
    public void MapTobaen() {

        Map<String, Object> map = getJavaBeanMap();
        String mapStr = JSONObject.fromObject(map).toString();
        log.info(mapStr);
        //{"n_date":{"date":14,"day":5,"hours":20,"minutes":57,"month":11,"seconds":0,"time":1544792220857,"timezoneOffset":-480,"year":118},"n_integer":100,"n_bigDecimal":12.12245,"n_string":"张宇晋","n_long":10045456456}
    }

    @Test
    public void JsonToList(){

        List<JavaBean> javaBeans = new ArrayList<>();
        JavaBean javaBean1 = getJavaBean();

        JavaBean javaBean2 = getJavaBean();
        javaBeans.add(javaBean1);
        javaBeans.add(javaBean2);
        JSONArray jsonArray = JSONArray.fromObject(javaBeans);
//      Json数组打印出来 date就变成了下面这种玩意，和上面的map一球样，经过了多层处理
//      log.info(jsonArray.toString());
//      [{"n_bigDecimal":12.12245,"n_date":{"date":14,"day":5,"hours":20,"minutes":44,"month":11,"seconds":0,"time":1544791440208,"timezoneOffset":-480,"year":118},"n_integer":100,"n_long":10045456456,"n_string":"张宇晋"},{"n_bigDecimal":12.12245,"n_date":{"date":14,"day":5,"hours":20,"minutes":44,"month":11,"seconds":0,"time":1544791440208,"timezoneOffset":-480,"year":118},"n_integer":100,"n_long":10045456456,"n_string":"张宇晋"}]


        //第一种方式，只针对没有使用lombok注解的情况 ，这种方式不论有没有 date类型 使用了@Data注解 就会出现下面的问题，有了date那也肯定的会是这种结局
//      使用的时候，我们还是尽量使用这种方式，不要用@Data注解，jsonArray来源需要是原生的
//  javaBeans = JSONArray.toList(jsonArray,new JavaBean() ,new JsonConfig());
//        net.sf.json.JSONObject - Property 'n_bigDecimal' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.129 [main] INFO  net.sf.json.JSONObject - Property 'n_date' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_integer' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_long' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_string' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_bigDecimal' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.130 [main] INFO  net.sf.json.JSONObject - Property 'n_date' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.131 [main] INFO  net.sf.json.JSONObject - Property 'n_integer' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.131 [main] INFO  net.sf.json.JSONObject - Property 'n_long' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        2018-12-14 20:27:08.131 [main] INFO  net.sf.json.JSONObject - Property 'n_string' of class com.hlj.data.res.test.JavaBean has no write method. SKIPPED.
//        log.info(JsonUtils.toJson(javaBeans));
//打印结果 [{"n_long":null,"n_string":null,"n_bigDecimal":null,"n_date":null,"n_integer":null},{"n_long":null,"n_string":null,"n_bigDecimal":null,"n_date":null,"n_integer":null}]


        //2、自己封装的
        //使用下面的没有Date类型的时候没有问题，有了Date类型就会报错，所以我尝试将date变成null
        // 因为这里的date不是Long类型的，明白了吧，上面我们就说了必须是Long类型的才可以哦
//        javaBeans =(List<JavaBean>) JsonUtils.toObjectOrList(jsonArray.toString(),JavaBean.class );
//        log.info(JsonUtils.toJson(javaBeans));
        //[{"n_long":10045456456,"n_string":"张宇晋","n_bigDecimal":12.12245,"n_date":null,"n_integer":100},{"n_long":10045456456,"n_string":"张宇晋","n_bigDecimal":12.12245,"n_date":null,"n_integer":100}]

        //下面这种也一样，有了date就是不好使，明白了么，臭小子
        String jsonArrayStr = "[{\"n_long\":10045456456,\"n_string\":\"张宇晋\",\"n_bigDecimal\":12.12245,\"n_date\":1544791022000,\"n_integer\":100},{\"n_long\":10045456456,\"n_string\":\"张宇晋\",\"n_bigDecimal\":12.12245,\"n_date\":1544791022000,\"n_integer\":100}]";
        javaBeans =(List<JavaBean>) JsonUtils.toObjectOrList(jsonArrayStr,JavaBean.class );
        log.info(JsonUtils.toJson(javaBeans));
        //下面这种也不行，因为还是经过了多层处理，date又出来了
//        javaBeans =JSONArray.toList(JSONArray.fromObject(jsonArrayStr),new JavaBean() ,new JsonConfig());
//        log.info(JsonUtils.toJson(javaBeans));


    }


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
        javaBean.setN_string("张宇晋");
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

}
