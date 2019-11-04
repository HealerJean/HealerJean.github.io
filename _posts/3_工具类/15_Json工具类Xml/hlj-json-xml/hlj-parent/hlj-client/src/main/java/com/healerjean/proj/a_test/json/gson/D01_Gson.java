package com.healerjean.proj.a_test.json.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.healerjean.proj.a_test.json.JsonDemoDTO;
import com.healerjean.proj.common.constant.CommonConstants;
import com.healerjean.proj.util.gson.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author HealerJean
 * @ClassName D01_Gson
 * @date 2019/10/29  19:36.
 * @Description
 */
@Slf4j
public class D01_Gson {


    /**
     * 1、JsonObject的获取数据以及基本使用
     * 1.1、获取数据值（默认取到的是JsonElement）
     * 1.2、JsonArray中获取 JsonObject对象
     * 1.3、判断是不是数组（对象数组或者普通数组），，判断是不是对象,判断是否拥有某个节点
     * 1.4、简单遍历jsonObject
     * 1.5.1.、遍历正常JsonArray
     * 1.5.2、遍历String集合的JsonArray
     */
    @Test
    public void test1() {
        String json = JsonDemoDTO.jsonString();
        log.info("jsonString：【 {} 】", json);

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();

        // 1、获取数据值（默认取到的是JsonElement）
        log.info("------------------------------------------------");
        log.info("1、获取数据值（默认取到的是JsonElement）");
        String msg = jsonObject.get(CommonConstants.msg).getAsString();
        int code = jsonObject.get(CommonConstants.code).getAsInt();
        BigDecimal bigDecimal = jsonObject.get(CommonConstants.bigDecimal).getAsBigDecimal();
        JsonObject userJsonObject = jsonObject.get(CommonConstants.user).getAsJsonObject();
        JsonArray companyJsonArray = jsonObject.get(CommonConstants.companys).getAsJsonArray();
        JsonArray strJsonArray = jsonObject.get(CommonConstants.strList).getAsJsonArray();
        log.info("msg：【 {} 】", msg);
        log.info("code：【 {} 】", code);
        log.info("bigDecimal：【 {} 】", bigDecimal);
        log.info("userJsonObject：【 {} 】", userJsonObject);
        log.info("companyJsonArray：【 {} 】", companyJsonArray);
        log.info("strJsonArray：【 {} 】", strJsonArray);

        // 2、JsonArray中获取 JsonObject对象
        log.info("------------------------------------------------");
        log.info(" 2、JsonArray中获取 JsonObject对象");
        JsonObject company0 = companyJsonArray.get(0).getAsJsonObject();
        log.info("company0：【 {} 】", company0);


        // 3、判断是不是数组（对象数组或者普通数组），，判断是不是对象,判断是否拥有某个节点
        log.info("------------------------------------------------");
        log.info("3、判断是不是数组（对象数组或者普通数组），判断是不是对象,判断是否拥有某个节点");
        Boolean flagObject = userJsonObject.isJsonObject();
        Boolean flagArray = companyJsonArray.isJsonArray();
        Boolean exist = userJsonObject.has(CommonConstants.msg);
        log.info("flagObject：【 {} 】", flagObject);
        log.info("flagArray：【 {} 】", flagArray);
        log.info("exist：【 {} 】", exist);


        // 4、简单遍历jsonObject
        log.info("------------------------------------------------");
        log.info("4、简单遍历jsonObject");
        Set<String> keys = jsonObject.keySet();
        for (String key : keys) {
            Object value = jsonObject.get(key);
            log.info("key：【 {} 】,value：【 {} 】", key, value);
        }

        //5.1.、遍历正常JsonArrayy
        log.info("------------------------------------------------");
        log.info("5.1.、遍历正常JsonArrayy");
        Iterator iteratorCompany = companyJsonArray.iterator();
        while (iteratorCompany.hasNext()) {
            Object object = iteratorCompany.next();
            log.info("object：【 {} 】", object);
        }

        //5.2、遍历String集合的JsonArrayy
        log.info("------------------------------------------------");
        log.info("5.2、遍历String集合的JsonArrayy");
        Iterator iteratorCar = strJsonArray.iterator();
        while (iteratorCar.hasNext()) {
            Object object = iteratorCar.next();
            log.info("object：【 {} 】", object);
        }


        // jsonString：【 {"reqSn":"e5b9e4c6a5e0460b930ed4d38e3c75d4","code":200,"msg":"Success","transDate":1572351096471,"integer":100,"bigDecimal":100,"user":{"userId":1,"userName":"HealerJean"},"strList":["奔驰","宝马"],"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}]} 】[101]
        //         ------------------------------------------------[107]
        // 1、获取数据值（默认取到的是JsonElement）[108]
        // msg：【 Success 】[115]
        // code：【 200 】[116]
        // bigDecimal：【 100 】[117]
        // userJsonObject：【 {"userId":1,"userName":"HealerJean"} 】[118]
        // companyJsonArray：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】[119]
        // strJsonArray：【 ["奔驰","宝马"] 】[120]
        //         ------------------------------------------------[123]
        // 2、JsonArray中获取 JsonObject对象[124]
        // company0：【 {"companyId":1,"companyName":"汽车公司"} 】[126]
        //         ------------------------------------------------[130]
        // 3、判断是不是数组（对象数组或者普通数组），判断是不是对象,判断是否拥有某个节点[131]
        // flagObject：【 true 】[135]
        // flagArray：【 true 】[136]
        // exist：【 false 】[137]
        //         ------------------------------------------------[141]
        // 4、简单遍历jsonObject[142]
        // key：【 reqSn 】,value：【 "e5b9e4c6a5e0460b930ed4d38e3c75d4" 】[146]
        // key：【 code 】,value：【 200 】[146]
        // key：【 msg 】,value：【 "Success" 】[146]
        // key：【 transDate 】,value：【 1572351096471 】[146]
        // key：【 integer 】,value：【 100 】[146]
        // key：【 bigDecimal 】,value：【 100 】[146]
        // key：【 user 】,value：【 {"userId":1,"userName":"HealerJean"} 】[146]
        // key：【 strList 】,value：【 ["奔驰","宝马"] 】[146]
        // key：【 companys 】,value：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】[146]
        //         ------------------------------------------------[150]
        // 5.1.、遍历正常JsonArrayy[151]
        // object：【 {"companyId":1,"companyName":"汽车公司"} 】[155]
        // object：【 {"companyId":2,"companyName":"房产公司"} 】[155]
        //         ------------------------------------------------[159]
        // 5.2、遍历String集合的JsonArrayy[160]
        // object：【 "奔驰" 】[164]
        // object：【 "宝马" 】[164]



    }


    /**
     * 2、添加和删除字段
     * 1、基本数据添加、（日期，对象，集合数据不能直接添加，可以考虑将这些变成JSONObject，在进行整合）
     * 2、JSONObject放入一个JSONObject
     * 3、两个JSONObject 合并为一个 (借助工具类)
     * 4、删除某个节点
     */
    @Test
    public void test2() {

        // 1、正常数据添加 只能放入基本类型数据
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("reqSn", UUID.randomUUID().toString().replace("-", ""));
        jsonObject.addProperty("msg", "Success");
        jsonObject.addProperty("code", 200);
        //不可以放入日期属性，集合属性,对象属性
        // jsonObject.addProperty("transDate", new Date());
        // jsonObject.addProperty("strList", Arrays.asList(new String[]{"奔驰", "宝马"}));
        // jsonObject.addProperty("user", new JsonDemoDTO.User().setUserId(1L).setUserName("HealerJean"));
        log.info("jsonObject_1 ：【 {} 】", jsonObject);


        // 2、JSONObject放入一个JSONObject
        JsonObject fieldJsonObject = new JsonObject();
        fieldJsonObject.addProperty("key", "Str");
        fieldJsonObject.add("key", fieldJsonObject);
        log.info("2、JSONObject放入一个JSONObject ：【 {} 】", jsonObject);


        // 3、两个JSONObject 合并为一个
        JsonObject userJsonObject = new JsonObject();
        // 4、删除某个节点
        jsonObject.remove("msg");
        userJsonObject.addProperty("userKey", "JsonObject");
        GsonUtil.putAll(jsonObject, userJsonObject);
        log.info("3、两个JsonObject 合并为一个 ：【 {} 】", jsonObject);
    }


    // 3、序列化 问题

    /**
     * 3.1、对象转Json，日期序列化之后会变成 "Oct 29, 2019 7:39:03 PM"
     */
    @Test
    public void test3_1() {
        Gson gson = new Gson();
        JsonDemoDTO jsonDemoDTO = JsonDemoDTO.jsonDemo();
        String json = gson.toJson(jsonDemoDTO);
        log.info("对象转Json【 {} 】", json);
        // 对象转Json【 {"reqSn":"a615312e8a854387881e34570d93a37f","code":200,"msg":"Success","transDate":"Oct 29, 2019 7:39:03 PM","integer":100,"bigDecimal":100,"user":{"userId":1,"userName":"HealerJean"},"strList":["奔驰","宝马"],"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}]} 】
    }

    /**
     * 3.2、Json转对象
     */
    @Test
    public void test3_2() {
        Gson gson = new Gson();
        JsonDemoDTO jsonDemoDTO = new JsonDemoDTO().setMsg("Success");
        String json = gson.toJson(jsonDemoDTO);
        jsonDemoDTO = gson.fromJson(json, JsonDemoDTO.class);
        log.info("Json转对象【 {} 】", jsonDemoDTO);
        // Json转对象【 {"code":0,"msg":"Success"} 】
    }


    /**
     * 3.3、Json/对象转JsonObject
     * 注意：必须经历String类型，才能到JsonObject
     */
    @Test
    public void test3_3() {
        String json = "{\"code\":0,\"msg\":\"Success\"}";
        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
        log.info("Json转JsonObject 【 {} 】", jsonObject.toString());

        JsonDemoDTO jsonDemoDTO = new JsonDemoDTO().setMsg("Success");
        Gson gson = new Gson();
        json = gson.toJson(jsonDemoDTO);
        jsonObject = new JsonParser().parse(json).getAsJsonObject();
        log.info("对象转JsonObject 【 {} 】", jsonObject.toString());

    }


    /**
     * 4、 json转集合
     * 注意，1、转化为JsonArray，然后进行拼接
     */
    @Test
    public void test3_4() {
        JsonDemoDTO jsonDemo1 = new JsonDemoDTO().setMsg("Success");
        JsonDemoDTO jsonDemo2 = new JsonDemoDTO().setMsg("Error");
        List<JsonDemoDTO> list = new ArrayList<>();
        list.add(jsonDemo1);
        list.add(jsonDemo2);

        String json = GsonUtil.toJsonString(list);
        list = GsonUtil.toList(json, JsonDemoDTO.class);
        log.info("json转集合 【 {} 】", list);

    }


}
