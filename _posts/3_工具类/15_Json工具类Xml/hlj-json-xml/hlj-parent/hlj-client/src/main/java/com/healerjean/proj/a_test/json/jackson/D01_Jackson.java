package com.healerjean.proj.a_test.json.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.healerjean.proj.a_test.json.JsonDemoDTO;
import com.healerjean.proj.common.constant.CommonConstants;
import com.healerjean.proj.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName D01_Jackson
 * @date 2019/10/29  17:54.
 * @Description
 */
@Slf4j
public class D01_Jackson {


    /**
     * 1、JsonNode的获取数据以及基本使用
     * 注意点 JsonNode 使用log打印出的结果不是我们想要的，所以我们要使用 jsonNode.toString()
     * 1.1、获取数据值（默认取到的是JsonNode）
     * JsonNode类型 rootNode.path("fieldName")、  rootNode.get("fieldName") 一般建议使用get
     * String类型 node.textValue()  node.asText() node.toString()
     * Integer类型 node.intValue()  asInt()
     * BigDecimal类型 node.decimalValue() ,所以一般建议使用 基本类型Value()
     * 1.2、判断是不是数组（对象数组或者普通数组，普通数据值），判断是不是对象,判断是否拥有某个节点
     * 1.3、简单遍历JsonNode , 获取key的迭代集  rootNode.fieldNames()
     * 1.4、遍历 JsonNode（数组或者Json等都可以用它）
     * 1.5、遍历某个JsonNode的key和value(value可能是字符串也可能是子jsonNode，但如果value是jsonNode数组的话，是无法读取的)
     */
    @Test
    public void test1() {

        String json = JsonDemoDTO.jsonString();
        log.info("jsonString：【 {} 】", json);

        JsonNode rootNode = JsonUtils.toJsonNode(json);
        //1、获取数据值（默认取到的是JsonNode）
        //      JsonNode类型 rootNode.path("fieldName")、  rootNode.get("fieldName") 一般建议使用get
        //      String类型 node.textValue()  node.asText() node.toString()
        //      Integer类型 node.intValue()  asInt()
        //      BigDecimal类型 node.decimalValue() ,所以一般建议使用 基本类型Value()

        log.info("------------------------------------------------");
        log.info("1、获取数据值（默认取到的是JsonNode）");
        String msg = rootNode.get(CommonConstants.msg).textValue();
        String msgText = rootNode.get(CommonConstants.msg).asText();
        int code = rootNode.get(CommonConstants.code).asInt();
        Integer integer = rootNode.get(CommonConstants.integer).intValue();
        BigDecimal bigDecimal = rootNode.get(CommonConstants.bigDecimal).decimalValue();
        JsonNode userJsonNode = rootNode.get(CommonConstants.user);
        JsonNode companyJsonNodeArray = rootNode.get(CommonConstants.companys);
        JsonNode strJsonNodeArray = rootNode.get(CommonConstants.strList);

        log.info("msg：【 {} 】", msg);
        log.info("msgText：【 {} 】", msgText);
        log.info("code：【 {} 】", code);
        log.info("integer：【 {} 】", integer);
        log.info("bigDecimal：【 {} 】", bigDecimal);
        log.info("userJsonNode：【 {} 】", userJsonNode.toString());
        log.info("companyJsonNodeArray：【 {} 】", companyJsonNodeArray.toString());
        log.info("strJsonNodeArray：【 {} 】", strJsonNodeArray.toString());

        // 2、判断是不是数组（对象数组或者普通数组），判断是不是对象,判断是否拥有某个节点
        log.info("------------------------------------------------");
        log.info("2、判断是不是数组（对象数组或者普通数组），判断是不是对象,判断是否拥有某个节点");
        Boolean flagObject = userJsonNode.isObject();
        Boolean flagArray = strJsonNodeArray.isArray();
        Boolean flagValue =  userJsonNode.isValueNode();
        Boolean exist = rootNode.has(CommonConstants.msg);

        log.info("flagObject：【 {} 】", flagObject);
        log.info("flagArray：【 {} 】", flagArray);
        log.info("flagValue：【 {} 】", flagValue);
        log.info("exist：【 {} 】", exist);


        //3、简单遍历JsonNode , 获取key的迭代集  rootNode.fieldNames()
        log.info("------------------------------------------------");
        log.info("3、简单遍历JsonNode , 获取key的迭代集  rootNode.fieldNames()");
        Iterator<String> keys = rootNode.fieldNames();
        while (keys.hasNext()) {
            // 2、获取节点的key值
            String key = keys.next();
            JsonNode jsonNode = rootNode.get(key);
            log.info("key：【 {} 】,value：【 {} 】", key, jsonNode.toString());
        }


        // 4、遍历 JsonNode（数组或者Json等都可以用它）
        log.info("------------------------------------------------");
        log.info("4、遍历 JsonNode（数组或者Json等都可以用它）");
        for (JsonNode jsonNode : companyJsonNodeArray) {
            log.info("jsonNode1：【 {} 】", jsonNode.toString());
        }

        // 5、遍历某个JsonNode的key和value(value可能是字符串也可能是子jsonNode，但如果value是jsonNode数组的话，是无法读取的)
        Iterator<Map.Entry<String, JsonNode>> jsonNodes = companyJsonNodeArray.fields();
        while (jsonNodes.hasNext()) {
            Map.Entry<String, JsonNode> jsonNode = jsonNodes.next();
            log.info("jsonNode2：【 {} 】", jsonNode.toString());
        }



        // jsonString：【 {"reqSn":"9f2aa3a63dcb493b97accc5fd4eee169","code":200,"msg":"Success","transDate":1572347467277,"integer":100,"bigDecimal":100,"user":{"userId":1,"userName":"HealerJean"},"strList":["奔驰","宝马"],"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}]} 】 [35]
        //         ------------------------------------------------ [44]
        // 1、获取数据值（默认取到的是JsonNode） [45]
        // msg：【 Success 】 [55]
        // msgText：【 Success 】 [56]
        // code：【 200 】 [57]
        // integer：【 100 】 [58]
        // bigDecimal：【 100 】 [59]
        // userJsonNode：【 {"userId":1,"userName":"HealerJean"} 】 [60]
        // companyJsonNodeArray：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】 [61]
        // strJsonNodeArray：【 ["奔驰","宝马"] 】 [62]
        //         ------------------------------------------------ [65]
        // 3、判断是不是数组（对象数组或者普通数组），判断是不是对象,判断是否拥有某个节点 [66]
        // flagObject：【 true 】 [75]
        // flagArray：【 true 】 [74]
        // flagValue：【 false 】
        // exist：【 true 】 [79]
        //         ------------------------------------------------ [83]
        // 3、简单遍历JsonNode , 获取key的迭代集  rootNode.fieldNames() [84]
        // key：【 reqSn 】,value：【 "9f2aa3a63dcb493b97accc5fd4eee169" 】 [90]
        // key：【 code 】,value：【 200 】 [90]
        // key：【 msg 】,value：【 "Success" 】 [90]
        // key：【 transDate 】,value：【 1572347467277 】 [90]
        // key：【 integer 】,value：【 100 】 [90]
        // key：【 bigDecimal 】,value：【 100 】 [90]
        // key：【 user 】,value：【 {"userId":1,"userName":"HealerJean"} 】 [90]
        // key：【 strList 】,value：【 ["奔驰","宝马"] 】 [90]
        // key：【 companys 】,value：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】 [90]
        //         ------------------------------------------------ [95]
        // 4、遍历 JsonNode（数组或者Json等都可以用它） [96]
        // jsonNode：【 {"companyId":1,"companyName":"汽车公司"} 】 [98]
        // jsonNode：【 {"companyId":2,"companyName":"房产公司"} 】 [98]


    }

    /**
     * 添加和删除
     * 1、基本数据添加、（日期，对象，集合数据不能直接添加，可以考虑将这些变成objectNode，在进行整合）
     * 2、ObjectNode放入一个ObjectNode
     * 3、两个ObjectNode 合并为一个
     * 4、删除某个节点
     */
    @Test
    public void test2() {
        String json = "{\"userId\":1,\"userName\":\"HealerJean\"}";
        String json1 = "{\"companyId\":1,\"companyName\":\"汽车公司\"}";
        log.info("json：【 {} 】", json);
        log.info("json1：【 {} 】", json1);

        ObjectNode objectNode = JsonUtils.toObjectNode(json);
        ObjectNode objectNode1 = JsonUtils.toObjectNode(json1);

        // 1、基本数据添加、（日期，对象，集合数据不能直接添加，可以考虑将这些变成objectNode，在进行整合）
        objectNode.put("reqSn", UUID.randomUUID().toString().replace("-", ""));
        objectNode.put("msg", "Success");
        objectNode.put("code", 200);
        // objectNode.put("transDate", new Date());
        // objectNode.put("strList", Arrays.asList(new String[]{"奔驰", "宝马"}));
        // objectNode.put("user", new JsonDemoDTO.User().setUserId(1L).setUserName("HealerJean"));

        // 2、ObjectNode放入一个ObjectNode
        objectNode.putPOJO("node", objectNode1);

        // 3、两个ObjectNode 合并为一个
        objectNode.putAll(objectNode1);

        //4、删除某个节点
        objectNode.remove("userId");

        JsonNode jsonNode = objectNode;
        log.info("jsonNode 【 {} 】", jsonNode.toString());

    }

    /**
     * 3、 复杂JsonNode遍历
     */
    @Test
    public void test3() {
        String json = JsonDemoDTO.jsonString();
        log.info("jsonString：【 {} 】", json);
        JsonNode rootNode = JsonUtils.toJsonNode(json);

        //1、获取Json节点的key集合
        Iterator<String> keys = rootNode.fieldNames();
        while (keys.hasNext()) {
            // 2、获取节点的key值
            String key = keys.next();

            //3、获取key获取某个节点
            JsonNode value = rootNode.get(key);
            //4、判断节点是一个对象
            if (value.isObject()) {
                log.info("key 【 {} 】， Json对象 ：【 {} 】", key, value.toString());
            }
            //5、判断节点是数组
            else if (value.isArray()) {
                // 普通数组没有fieldName ，所以得到的name内部不存在，不会进入while循环，也不会报错
                // Iterator<String> nodeKeys = jsonNode.fieldNames();
                // while (nodeKeys.hasNext()) {
                //     log.info("nodeKeys.next()" + nodeKeys.next());
                //}
                for (JsonNode node : value) {
                    if (node.isObject()) {
                        log.info("数组中的Json对象 :" + node.toString());
                    } else {
                        log.info("普通数组 ：【 {} 】", node.toString());
                    }
                }
            } else {
                log.info("key 【 {} 】， 普通元素 ：【 {} 】", key, value.toString());
            }
        }

        // jsonString：【 {"reqSn":"2305bbd5be23448082918daf2aaccfd0","code":200,"msg":"Success","transDate":1572348749670,"integer":100,"bigDecimal":100,"user":{"userId":1,"userName":"HealerJean"},"strList":["奔驰","宝马"],"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}]} 】 [178]
        // key 【 reqSn 】， 普通元素 ：【 "2305bbd5be23448082918daf2aaccfd0" 】 [208]
        // key 【 code 】， 普通元素 ：【 200 】 [208]
        // key 【 msg 】， 普通元素 ：【 "Success" 】 [208]
        // key 【 transDate 】， 普通元素 ：【 1572348749670 】 [208]
        // key 【 integer 】， 普通元素 ：【 100 】 [208]
        // key 【 bigDecimal 】， 普通元素 ：【 100 】 [208]
        // key 【 user 】， Json对象 ：【 {"userId":1,"userName":"HealerJean"} 】 [191]
        // 普通数组 ：【 "奔驰" 】 [204]
        // 普通数组 ：【 "宝马" 】 [204]
        // 数组中的Json对象 :{"companyId":1,"companyName":"汽车公司"} [202]
        // 数组中的Json对象 :{"companyId":2,"companyName":"房产公司"} [202]


    }

    /**
     * 4、配置对象中null的元素转Json后不存在
     */
    @Test
    public void test4() throws JsonProcessingException {

        // 1、注解@JsonInclude(JsonInclude.Include.NON_NULL)

        // 2。objectmapper配置
        JsonDemoDTO jsonDemoDTO = new JsonDemoDTO();
        jsonDemoDTO.setTransDate(new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        String json = objectMapper.writeValueAsString(jsonDemoDTO);
        log.info("对象转Json 【 {} 】", json);
        // {"code":0,"transDate":1572343184051}

    }


    /**
     * 5、序列化问题：
     * 对象转Json 序列化的日期 变成 Long 1572343018614
     */
    @Test
    public void test5() throws JsonProcessingException {
        JsonDemoDTO jsonDemoDTO = new JsonDemoDTO();
        jsonDemoDTO.setTransDate(new Date());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(jsonDemoDTO);
        log.info("对象转JSON  【 {} 】", json);
        //{"reqSn":null,"code":0,"msg":null,"transDate":1572343018614,"user":null,"strList":null,"companys":null}

        JsonNode jsonNode = objectMapper.valueToTree(jsonDemoDTO);
        log.info("对象转JsonNode：【{}】", jsonNode);

    }



    @Test
    public void test(){
        System.out.println(JsonUtils.toObject(null, JsonDemoDTO.class));
    }



}
