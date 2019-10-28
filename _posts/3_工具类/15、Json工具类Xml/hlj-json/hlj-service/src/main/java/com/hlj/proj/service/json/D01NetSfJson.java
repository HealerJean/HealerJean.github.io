package com.hlj.proj.service.json;

import com.hlj.proj.utils.JsonUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName D01NetSfJson
 * @date 2019/8/3  12:39.
 * @Description
 */
public class D01NetSfJson {





    /**
     * JsonObject的基本使用
     */
    @Test
    public void jsonObject() {

        String jsonSTR = "{\n" +
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
        JSONObject jsonObject = JSONObject.fromObject(jsonSTR);


        //1、从JSONObject中获取数据
        String name  = jsonObject.getString("friendName");
        Integer integer = jsonObject.getInt("friendName");

        //2、判断JSONObject是否存在节点
        Boolean flag =  jsonObject.has("resultCount");

        //3、简单遍历 JSONObject
        for (Object key : jsonObject.keySet()) {
            Object object = jsonObject.get(key);
            System.out.println(object);
        }


        //4、简单遍历JSONArray
        JSONArray jsonArray = jsonObject.getJSONArray("friends");
        // JSONArray jsonArray =jsonObject.getJSONArray("cars");

        for (int i = 0; i < jsonArray.size(); i++) {
            Object object = jsonArray.get(i);
            System.out.println(object);
        }
        Iterator iterator = jsonArray.iterator();
        if(iterator.hasNext()){
            Object object = iterator.next();
            System.out.println(object);
        }

        // 5、JSONArray中获取 JSONObject对象
        JSONObject car =jsonObject.getJSONArray("friends").getJSONObject(0);
        System.out.println(car);
        // 虽然有下面这个方法，但是也要看情况是否能用，下面的会报错
        // JSONObject car =jsonObject.getJSONArray("cars").getJSONObject(0);

    }



    /**
     *  1、数组不可以转换为JSONObject
     */
    @Test
    public void listToJSONObjectError(){

        List<String> list = new ArrayList<>();
        list.add("zhangcuihua");
        list.add("healerjean");

        String jsonStr = JsonUtils.toJsonString(list);
        System.out.println(jsonStr);
        // 打印结果["zhangcuihua","healerjean"]


        //1、报错 提示 JSONObject必须是包含 {
        // net.sf.json.JSONException: A JSONObject text must begin with '{' at character 1 of ["zhangcuihua","healerjean"]
        JSONObject jsonObject = JSONObject.fromObject(jsonStr);

    }

    /**
     * 2、 JsonArray的包含 ：普通数组与JsonObject对象
     */
    @Test
    public  void testJsonArray() {

        String jsonSTR = "{\n" +
                "    \"username\": \"HealerJean\",\n" +
                "    \"sex\": \"男\",\n" +
                "    \"company\": {\n" +
                "        \"companyName\": \"梦想之都公司\",\n" +
                "        \"address\": \"北京海淀\"\n" +
                "    },\n" +
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

        System.out.println(jsonSTR);
        JSONObject jsonObject = JSONObject.fromObject(jsonSTR);

        //1、获取Json的key集合
        for (Object key : jsonObject.keySet()) {
            //2、获取某个Key的值
            Object value = jsonObject.get(key);

            //2、判断是对象-JSONObject吗？
            if (value instanceof JSONObject) {
                System.out.println("JSONObject对象 ：" +value);
            }
            //3、判断是数组-JSONArray 吗？
            else if (value instanceof JSONArray) {
                //4、数组迭代
                Iterator it = ((JSONArray) value).iterator();
                while (it.hasNext()) {
                    Object object =  it.next() ;
                    if (object instanceof  JSONObject){
                        System.out.println("数组中的JSONObject :"+object);
                    }else {
                        System.out.println("普通数组："+object);
                    }
                }
             //7、 普通元素
            } else {
                System.out.println("普通元素："+value);
            }
        }



        // 普通元素：HealerJean
        // 普通元素：男
        // JSONObject对象 ：{"companyName":"梦想之都公司","address":"北京海淀"}
        // 普通数组：奔驰
        // 普通数组：宝马
        // 数组中的JSONObject :{"friendName":"小明","friendSex":"男"}
        // 数组中的JSONObject :{"friendName":"小花","friendSex":"女"}

    }



}
