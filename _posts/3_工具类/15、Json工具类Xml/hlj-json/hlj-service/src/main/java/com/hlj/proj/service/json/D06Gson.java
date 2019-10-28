package com.hlj.proj.service.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hlj.proj.dto.DateBean;
import com.hlj.proj.utils.gson.GsonUtil;
import org.junit.Test;

import java.util.*;

/**
 * @author HealerJean
 * @ClassName D06Gson
 * @date 2019/9/12  14:02.
 * @Description
 */
public class D06Gson {


    /**
     * 1、对象转Json
     */
    @Test
    public void toJsonString(){
        Gson gson = new Gson();
        DateBean dateBean = new DateBean();
        dateBean.setDate(new Date());
        dateBean.setName("张宇晋");
        System.out.println(gson.toJson(dateBean));
       // {"name":"张宇晋","date":"Sep 12, 2019 2:11:52 PM"}
    }

    /**
     * 2、Json转Bean
     */
    @Test
    public void toBean(){
        Gson gson = new Gson();
        //这个日期格式是错误的
        //   String dateJsonStr = "{\"name\":\"HealerJean\",\"date\":1544782308409}";
        String dateJsonStr = "{\"name\":\"张宇晋\",\"date\":\"Sep 12, 2019 2:11:52 PM\"}";
        DateBean dateBean = gson.fromJson(dateJsonStr,DateBean.class);
        System.out.println(dateBean);

       dateJsonStr = "{\"name\":\"张宇晋\",\"date\":1544782308409}";
       dateBean = GsonUtil.toObject(dateJsonStr,DateBean.class);
        System.out.println(dateBean);
    //

    }

    /**
     * 3、Json/对象转JsonObject
     */
    @Test
    public void toJsonObject(){
        String jsonStr = "{\"name\":\"张宇晋\",\"date\":\"Sep 12, 2019 2:11:52 PM\"}";
        JsonObject jsonObject = new JsonParser().parse(jsonStr).getAsJsonObject() ;
        System.out.println(jsonObject.toString());

        Gson gson = new Gson();
        DateBean dateBean = gson.fromJson(jsonStr,DateBean.class);
        jsonObject = new JsonParser().parse(gson.toJson(dateBean)).getAsJsonObject();
        System.out.println(jsonObject);
    }


    /**
     * 4、 json转集合
     */
    @Test
    public void toList(){

        DateBean one = new DateBean();
        one.setDate(new Date());
        one.setName("HealerJean");
        DateBean two = new DateBean();
        two.setDate(new Date());
        two.setName("zhangcui");
        List<DateBean> list = new ArrayList<>();
        list.add(one);
        list.add(two);

        String json = GsonUtil.toJsonString(list);

        list = GsonUtil.toList(json ,DateBean.class);
        System.out.println(list);

    }

    /**
     * 5、JsonObject添加数据和读取数据
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

        String paramJson = "{\"name\":\"张宇晋\",\"date\":\"Sep 12, 2019 2:11:52 PM\"}";

        JsonParser parser = new JsonParser();

        //将Json转化为JsonObejct
        JsonObject jsonObject = parser.parse(bigJson).getAsJsonObject() ;
        //1、添加基本数据
        jsonObject.addProperty("age",27);
        //2、添加Json对象
        JsonElement param = parser.parse(paramJson);
        jsonObject.add("param" ,param);
        System.out.println(jsonObject.toString());
        //{"cars":["奔驰","宝马"],"friends":[{"friendName":"小明","friendSex":"男"},{"friendName":"小花","friendSex":"女"}],"age":27,"param":{"name":"张宇晋","date":"Sep 12, 2019 2:11:52 PM"}}


        System.out.println("jsonObject.getAsJsonObject() :"+ jsonObject.getAsJsonObject("param"));
        //{"name":"张宇晋","date":"Sep 12, 2019 2:11:52 PM"}
        System.out.println(jsonObject.get("age").getAsInt());
        //27
        System.out.println(jsonObject.get("cars").getAsJsonArray());
        //["奔驰","宝马"]
        System.out.println(jsonObject.get("cars").getAsJsonArray().get(0).getAsString());
        //奔驰
        System.out.println(jsonObject.get("friends").getAsJsonArray());
        // [{"friendName":"小明","friendSex":"男"},{"friendName":"小花","friendSex":"女"}]
        System.out.println(jsonObject.get("friends").getAsJsonArray().get(0).getAsJsonObject().toString());
        //{"friendName":"小明","friendSex":"男"}

    }




    @Test
    public void map(){
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

        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject jsonObject =parser.parse(bigJson).getAsJsonObject();
        Map<String,String> map = new HashMap<>();
        map.put("name" ,"HealerJean");

        JsonObject mapJsonObject = parser.parse(gson.toJson(map)).getAsJsonObject();
        System.out.println(GsonUtil.addAll(jsonObject,mapJsonObject));

    }


    @Test
    public void test(){
        JsonObject extension = new JsonObject();
        DateBean one = new DateBean();
        extension.add("name" , GsonUtil.toJsonObject(one));
        JsonParser parser = new JsonParser();
        System.out.println(parser.parse(GsonUtil.toJsonString(extension)));
    }

}
