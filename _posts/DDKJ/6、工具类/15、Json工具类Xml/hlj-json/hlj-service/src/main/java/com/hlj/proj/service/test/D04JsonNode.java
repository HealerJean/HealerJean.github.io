package com.hlj.proj.service.test;

import com.fasterxml.jackson.databind.JsonNode;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName D04JsonNode
 * @date 2019/8/3  10:55.
 * @Description
 */
@Slf4j
public class D04JsonNode {




    /**
     * 8、JsonNode 基本方法
     */
    @Test
    public void getNode(){


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

        //1、获取key的迭代集  rootNode.fieldNames()
        JsonNode rootNode = JsonUtils.toJsonNode(jsonSTR);
        Iterator<String> keys = rootNode.fieldNames();

        //2、获取数据值（默认取到的是JsonNode） rootNode.path("username")、  rootNode.get("username")
        JsonNode node = null ;
        node = rootNode.path("username");
        node = rootNode.get("username");


        //3、判断是不是数组（对象数组或者普通数组）
        Boolean  flag = null ;
        flag = node.isArray();

        //4、判断是不是对象
        flag = node.isObject() ;

        //5、判断是不是某种属性
        flag =  node.isBigDecimal() ;
        flag =  node.isBoolean() ;
        flag =  node.isFloat();

        //6、将获取的数据进行类型转换
        //6.1、String类型
        String str = null ;
        str = node.textValue() ;
        str = node.asText();
        str = node.toString() ;

        Integer  integer = node.intValue() ;
        BigDecimal bigDecimal = node.decimalValue();



    }



    /**
     * 9、JsonNode 遍历Json
     */
    @Test
    public void testNode(){


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

        JsonNode rootNode = JsonUtils.toJsonNode(jsonSTR);
        //1、获取Json节点的key集合
        Iterator<String> keys = rootNode.fieldNames();
        while (keys.hasNext()) {
            // 2、获取节点的key值
            String key = keys.next();

            //3、获取key获取某个节点
            JsonNode jsonNode = rootNode.get(key);
            //4、判断节点是一个对象
            if (jsonNode.isObject()) {
                System.out.println("对象："+jsonNode);
            }
            //5、判断节点是数组
            else if (jsonNode.isArray()) {
                // 普通数组没有fieldName ，所以得到的name内部不存在，不会进入while循环，但是也不会报错
                // Iterator<String> nodeKeys = jsonNode.fieldNames();
                // while (nodeKeys.hasNext()) {
                //     log.info("nodeKeys.next()" + nodeKeys.next());
                //}
                for (JsonNode node : jsonNode) {
                    if(node.isObject()){
                        System.out.println("数组中的对象 :"+node);
                    }else {
                        System.out.println("普通数组："+node);
                    }
                }

            }//
            else {
                System.out.println("普通元素："+jsonNode);
            }
        }


        // 普通元素："HealerJean"
        // 普通元素："男"
        // 对象：{"companyName":"梦想之都公司","address":"北京海淀"}
        // 普通数组："奔驰"
        // 普通数组："宝马"
        // 数组中的对象 :{"friendName":"小明","friendSex":"男"}
        // 数组中的对象 :{"friendName":"小花","friendSex":"女"}
    }



}
