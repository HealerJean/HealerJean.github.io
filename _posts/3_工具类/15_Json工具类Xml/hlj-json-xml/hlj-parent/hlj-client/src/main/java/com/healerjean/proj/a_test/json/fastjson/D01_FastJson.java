package com.healerjean.proj.a_test.json.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.healerjean.proj.a_test.json.JsonDemoDTO;
import com.healerjean.proj.common.constant.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author HealerJean
 * @ClassName D01_FastJson
 * @date 2019/10/29  20:29.
 * @Description
 */
@Slf4j
public class D01_FastJson {

    /**
     * 1、JSONObject的获取数据以及基本使用
     * 1、获取数据值
     * 2、JSONArray中获取 JsonObject对象
     * 3、判断JSONObject是否存在节点
     * 4、简单遍历 JSONObject
     * 5、遍历JSONArray
     * 5.1.、遍历正常JSONArray
     * 5.2、遍历String集合的JSONArray
     *
     */
    @Test
    public void test1() {
        String json = JsonDemoDTO.jsonString();
        log.info("jsonString：【 {} 】", json);
        JSONObject jsonObject = JSONObject.parseObject(json);

        // 1、获取数据值
        log.info("------------------------------------------------");
        log.info("1、从JSONObject中获取简单数据");
        String msg = jsonObject.getString(CommonConstants.msg);
        int code = jsonObject.getIntValue(CommonConstants.code);
        BigDecimal bigDecimal = jsonObject.getBigDecimal(CommonConstants.bigDecimal);
        JSONObject userJSONObject = jsonObject.getJSONObject(CommonConstants.user);
        JSONArray companyJSONArray = jsonObject.getJSONArray(CommonConstants.companys);
        JSONArray strJSONArray = jsonObject.getJSONArray(CommonConstants.strList);
        log.info("msg：【 {} 】", msg);
        log.info("code：【 {} 】", code);
        log.info("bigDecimal：【 {} 】", bigDecimal);
        log.info("userJSONObject：【 {} 】", userJSONObject);
        log.info("companyJSONArray：【 {} 】", companyJSONArray);
        log.info("strJSONArray：【 {} 】", strJSONArray);


        // 2、JSONArray中获取 JsonObject对象
        log.info("------------------------------------------------");
        log.info(" 2、JsonArray中获取 JsonObject对象");
        JSONObject company0 = companyJSONArray.getJSONObject(0);
        log.info("company0：【 {} 】", company0);


        // 3、判断JSONObject是否存在节点
        log.info("------------------------------------------------");
        log.info("3、判断JSONObject是否存在节点");
        Boolean flagContainsKey = jsonObject.containsKey(CommonConstants.msg);
        log.info("flagContainsKey：【 {} 】", flagContainsKey);

        //4、简单遍历 JSONObject
        log.info("------------------------------------------------");
        log.info("4、简单遍历 JSONObject");
        for (Object key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            log.info("key：【 {} 】,value：【 {} 】", key, value);
        }

        //5.1.、遍历正常JSONArray
        log.info("------------------------------------------------");
        log.info("5.1.、遍历正常JSONArray");
        Iterator iteratorCompany = companyJSONArray.iterator();
        while (iteratorCompany.hasNext()) {
            Object object = iteratorCompany.next();
            log.info("object：【 {} 】", object);
        }

        //5.2、遍历String集合的JSONArray
        log.info("------------------------------------------------");
        log.info("5.2、遍历String集合的JSONArray");
        Iterator iteratorCar = strJSONArray.iterator();
        while (iteratorCar.hasNext()) {
            Object object = iteratorCar.next();
            log.info("object：【 {} 】", object);
        }



        // jsonString：【 {"reqSn":"5b0ac33c6f5b4931b9311c9b98243d9c","code":200,"msg":"Success","transDate":1572356266239,"integer":100,"bigDecimal":100,"user":{"userId":1,"userName":"HealerJean"},"strList":["奔驰","宝马"],"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}]} 】 [36]
        //         ------------------------------------------------ [40]
        // 1、从JSONObject中获取简单数据 [41]
        // msg：【 Success 】 [48]
        // code：【 200 】 [49]
        // bigDecimal：【 100 】 [50]
        // userJSONObject：【 {"userName":"HealerJean","userId":1} 】 [51]
        // companyJSONArray：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】 [52]
        // strJSONArray：【 ["奔驰","宝马"] 】 [53]
        //         ------------------------------------------------ [57]
        // 2、JsonArray中获取 JsonObject对象 [58]
        // company0：【 {"companyId":1,"companyName":"汽车公司"} 】 [60]
        //         ------------------------------------------------ [64]
        // 3、判断JSONObject是否存在节点 [65]
        // flagContainsKey：【 true 】 [67]
        //         ------------------------------------------------ [70]
        // 4、简单遍历 JSONObject [71]
        // key：【 msg 】,value：【 Success 】 [74]
        // key：【 code 】,value：【 200 】 [74]
        // key：【 strList 】,value：【 ["奔驰","宝马"] 】 [74]
        // key：【 companys 】,value：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】 [74]
        // key：【 transDate 】,value：【 1572356266239 】 [74]
        // key：【 integer 】,value：【 100 】 [74]
        // key：【 bigDecimal 】,value：【 100 】 [74]
        // key：【 user 】,value：【 {"userName":"HealerJean","userId":1} 】 [74]
        // key：【 reqSn 】,value：【 5b0ac33c6f5b4931b9311c9b98243d9c 】 [74]
        //         ------------------------------------------------ [78]
        // 5.1.、遍历正常JSONArray [79]
        // object：【 {"companyId":1,"companyName":"汽车公司"} 】 [83]
        // object：【 {"companyId":2,"companyName":"房产公司"} 】 [83]
        //         ------------------------------------------------ [87]
        // 5.2、遍历String集合的JSONArray [88]
        // object：【 奔驰 】 [92]
        // object：【 宝马 】 [92]


    }


    /**
     * 2、添加和删除字段
     * 1、基本数据、日期，对象，集合数据添加
     * 2、JSONObject放入一个JSONObject
     * 3、两个JSONObject 合并为一个
     * 4、删除某个节点
     */
    @Test
    public void test2() {
        // 1、基本数据、日期，对象，集合数据添加
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("reqSn", UUID.randomUUID().toString().replace("-", ""));
        jsonObject.put("msg", "Success");
        jsonObject.put("code", 200);
        jsonObject.put("transDate", new Date());
        jsonObject.put("strList", Arrays.asList(new String[]{"奔驰", "宝马"}));
        jsonObject.put("user", new JsonDemoDTO.User().setUserId(1L).setUserName("HealerJean"));
        jsonObject.put("companys", Arrays.asList(new JsonDemoDTO.Company[]{
                new JsonDemoDTO.Company().setCompanyId(1L).setCompanyName("汽车公司"),
                new JsonDemoDTO.Company().setCompanyId(2L).setCompanyName("房产公司")}));
        log.info("jsonObject_1 ：【 {} 】", jsonObject);


        // 2、JSONObject放入一个JSONObject
        JSONObject fieldJsonObject = new JSONObject();
        fieldJsonObject.put("key", "Str");
        jsonObject.put("fieldJsonObject", fieldJsonObject);
        log.info("2、JSONObject放入一个JSONObject ：【 {} 】", jsonObject);


        // 3、两个JSONObject 合并为一个
        JSONObject userJsonObject = new JSONObject();
        // 4、删除某个节点
        jsonObject.remove("user");
        userJsonObject.put("user", new JsonDemoDTO.User().setUserId(1L).setUserName("HealerJean"));
        jsonObject.putAll(userJsonObject);
        log.info("3、两个JSONObject 合并为一个 ：【 {} 】", jsonObject);

    }


    //序列化

    /**
     * 3.1、对象转转Json
     * 3.1.1、日期序列化会变成Long
     */
    @Test
    public void test3_1() {
        JsonDemoDTO jsonDemoDTO = JsonDemoDTO.jsonDemo();
        String json = JSONObject.toJSONString(jsonDemoDTO);
        log.info("对象转Json【 {} 】", json);
        //对象转Json【 {"bigDecimal":100,"code":200,"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}],"integer":100,"msg":"Success","reqSn":"85db4a55277d4d0da05692b94239717c","strList":["奔驰","宝马"],"transDate":1572353435560,"user":{"userId":1,"userName":"HealerJean"}} 】
    }

    /**
     * 3.2、 json转对象
     */
    @Test
    public void test3_2() {
        String json = JsonDemoDTO.jsonString();
        JsonDemoDTO jsonDemoDTO = JSONObject.parseObject(json, JsonDemoDTO.class);
        log.info("Json转对象【 {} 】", jsonDemoDTO);
        //Json转对象【 {"bigDecimal":100,"code":200,"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}],"integer":100,"msg":"Success","reqSn":"72fa274d3b4f4c3ab9453f0c74859754","strList":["奔驰","宝马"],"transDate":1572353529387,"user":{"userId":1,"userName":"HealerJean"}} 】
    }


    /**
     * 3.3、 json/对象转JSONObject
     *  注意：必须经历String类型，才能到JSONObject
     */
    @Test
    public void test3_3() {
        String json = JsonDemoDTO.jsonString();
        JSONObject jsonObject = JSONObject.parseObject(json);
        log.info("Json转JSONObject【 {} 】", jsonObject.toJSONString());

        JsonDemoDTO jsonDemoDTO = JsonDemoDTO.jsonDemo();
        jsonObject =   JSONObject.parseObject(JSONObject.toJSONString(jsonDemoDTO));
        log.info("对象转JsonObject 【 {} 】", jsonObject.toJSONString());
    }


    /**
     * 4、 json转集合
     */
    @Test
    public void toList() {
        JsonDemoDTO jsonDemo1 = new JsonDemoDTO().setMsg("Success");
        JsonDemoDTO jsonDemo2 = new JsonDemoDTO().setMsg("Error");
        List<JsonDemoDTO> list = new ArrayList<>();
        list.add(jsonDemo1);
        list.add(jsonDemo2);
        String json = JSONObject.toJSONString(list);

        list = JSONObject.parseArray(json, JsonDemoDTO.class);
        log.info("json转集合 【 {} 】", list);
    }


    @Test
    public void testm(){
        JsonDemoDTO jsonDemoDTO = JSONObject.parseObject("", JsonDemoDTO.class);
        System.out.println(jsonDemoDTO);

    }


}
