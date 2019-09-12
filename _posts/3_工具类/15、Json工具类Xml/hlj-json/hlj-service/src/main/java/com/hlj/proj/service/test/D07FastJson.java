package com.hlj.proj.service.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hlj.proj.dto.DateBean;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author HealerJean
 * @ClassName D07FastJson
 * @date 2019/9/12  15:00.
 * @Description
 */
public class D07FastJson {


    /**
     * 1、对象转转Json
     */
    @Test
    public void toJson() {
        DateBean dateBean = new DateBean();
        dateBean.setName("HealerJean");
        dateBean.setDate(new Date());
        System.out.println(JSONObject.toJSONString(dateBean));
        // {"date":1568276603299,"name":"HealerJean"}
    }

    /**
     * 2、 json转对象
     */
    @Test
    public void toObject() {
        String json = "{\"date\":1568271783781,\"name\":\"HealerJean\"}";
        DateBean dateBean = JSONObject.parseObject(json, DateBean.class);
        System.out.println(dateBean);
    }

    /**
     * 3、 json/对象转JsonObject
     */
    @Test
    public void toJSONObject() {
        String json = "{\"date\":1568271783781,\"name\":\"HealerJean\"}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        System.out.println(jsonObject);

        DateBean dateBean = new DateBean();
        dateBean.setName("HealerJean");
        dateBean.setDate(new Date());
        System.out.println(JSONObject.toJSONString(dateBean));
        //{"date":1568275471958,"name":"HealerJean"}
        //需要先将对象转化为Json之后再转化为JSONObject对象
        jsonObject =   JSONObject.parseObject(JSONObject.toJSONString(dateBean));
        // {"date":1568275471958,"name":"HealerJean"}

    }


    /**
     * 4、 json转集合
     */
    @Test
    public void toList() {
        DateBean one = new DateBean();
        one.setDate(new Date());
        one.setName("HealerJean");
        DateBean two = new DateBean();
        two.setDate(new Date());
        two.setName("zhangcui");
        List<DateBean> list = new ArrayList<>();
        list.add(one);
        list.add(two);

        String json = JSONObject.toJSONString(list);
        JSONArray jsonObject = JSONObject.parseArray(json);
        System.out.println(jsonObject);

        list = JSONObject.parseArray(json, DateBean.class);
        System.out.println(list);
    }



    /**
     * 5、添加和读取数据（包括：合并多个JsonObejct）
     * 读取数据和net.sf.json基本一致
     */
    @Test
    public void addAndGetParams(){
        String bigJson = "{\n" +
                "    \"cars\": [\n" +
                "        \"奔驰\",\n" +
                "        \"宝马\"\n" +
                "    ],\n" +
                "    \"friends\": [\n" +
                "        {\n" +
                "            \"friendName\": \"小明\",\n" +
                "            \"friendSex\": \"男\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"friendName\": \"小花\",\n" +
                "            \"friendSex\": \"女\"\n" +
                "        }\n" +
                "    ]\n" +
                "}" ;


        //1、添加数据
        JSONObject jsonObject = JSONArray.parseObject(bigJson);
        jsonObject.put("name" ,"HealerJean");
        System.out.println(jsonObject.toJSONString());
       //{"cars":["奔驰","宝马"],"name":"HealerJean","friends":[{"friendName":"小明","friendSex":"男"},{"friendName":"小花","friendSex":"女"}]}


        //2、合并两个JsonObject
        String paramJson = "{\"name\":\"张宇晋\",\"date\":\"Sep 12, 2019 2:11:52 PM\"}";
        JSONObject paramJsonObject = JSONArray.parseObject(paramJson);
        jsonObject.putAll(paramJsonObject);
        System.out.println(jsonObject);
    //    {"date":"Sep 12, 2019 2:11:52 PM","cars":["奔驰","宝马"],"name":"张宇晋","friends":[{"friendName":"小明","friendSex":"男"},{"friendName":"小花","friendSex":"女"}]}

    }





}
