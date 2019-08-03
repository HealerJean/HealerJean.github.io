package com.hlj.proj.utils.JsonNode;

import com.fasterxml.jackson.databind.JsonNode;
import com.hlj.proj.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName Main
 * @date 2019/8/3  10:55.
 * @Description
 */
@Slf4j
public class Main {

    @Test
    public void testNode(){

        String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";
        JsonNode rootNode = JsonUtils.toJsonNode(json);
        //获取Json节点的key值
        Iterator<String> keys = rootNode.fieldNames();
        while(keys.hasNext()){
            String key = keys.next();
            log.info( "1、rootNode.path(key)  : " + rootNode.path(key));

            JsonNode jsonNode = rootNode.get(key);
            log.info(  "2、rootNode.get(fieldName) : " + jsonNode);
            // 节点是数组
            if(jsonNode.isArray()) {
                //普通数组没有fieldName ，所以得到的name内部不存在
                Iterator<String> nodeKeys = jsonNode.fieldNames();
                while (nodeKeys.hasNext()){
                    log.info ("nodeKeys.next()"+nodeKeys.next());
                }

                for(JsonNode node :jsonNode){
                    System.out.println("普通数组打印"+node);
                }
                log.info(  "array found: " + jsonNode);
            //节点是对象
            } else if(jsonNode.isObject() ){
                log.info(  "jsonNode.isObject(): " + jsonNode);
            //节点是元素
            } else {
                log.info(  "non-array: " + jsonNode);
            }
        }


        //
        log.info("final、JsonNode ----> JSON ："+ JsonUtils.toJsonString(rootNode));
    }

// 11:38:2212:01:34.894 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 1、rootNode.path(key)  : "zhangsan"
// 12:01:34.896 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 2、rootNode.get(fieldName) : "zhangsan"
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - non-array: "zhangsan"
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 1、rootNode.path(key)  : "男"
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 2、rootNode.get(fieldName) : "男"
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - non-array: "男"
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 1、rootNode.path(key)  : {"companyName":"中华","address":"北京"}
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 2、rootNode.get(fieldName) : {"companyName":"中华","address":"北京"}
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - jsonNode.isObject(): {"companyName":"中华","address":"北京"}
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 1、rootNode.path(key)  : ["奔驰","宝马"]
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - 2、rootNode.get(fieldName) : ["奔驰","宝马"]
// 普通数组打印"奔驰"
// 普通数组打印"宝马"
// 12:01:34.897 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - array found: ["奔驰","宝马"]
// 12:01:34.902 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - final、JsonNode ----> JSON ：{"username":"zhangsan","性别":"男","company":{"companyName":"中华","address":"北京"},"cars":["奔驰","宝马"]}


    @Test
    public void getNode(){

        String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";
        JsonNode rootNode = JsonUtils.toJsonNode(json);
        log.info("rootNode.get(\"username\").get(0)"+rootNode.get("username").toString());
        log.info("rootNode.get(\"username\").get(0)"+rootNode.get("username").textValue());
        log.info("rootNode.get(\"username\").get(0)"+rootNode.get("username").asText());

        // 11:46:26.577 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - rootNode.get("username").get(0)zhangsan
        // 11:46:26.581 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - rootNode.get("username").get(0)"zhangsan"
        // 11:46:26.581 [main] INFO com.hlj.util.Z014Json.JsonNode.Main - rootNode.get("username").get(0)zhangsan
        }


    public static void main(String[] args) {
        String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";
        System.out.println(json);
        JsonNode rootNode = JsonUtils.toJsonNode(json);
        List<String> resultList = new ArrayList<>() ;
        System.out.println(parseJSON2List(rootNode,null,resultList));
    }



    public static List<String> parseJSON2List(JsonNode rootNode, String parentKey , List<String> resultList) {
        //获取Json节点的key值
        Iterator<String> keys = rootNode.fieldNames();
        while (keys.hasNext()) {
            String key = keys.next();
            String value = rootNode.get(key).asText() ;
            if("sign".equals(key) || null == value){
                continue;
            }
            JsonNode jsonNode = rootNode.get(key);
            // 节点是数组
            if (jsonNode.isArray()) {
                for(JsonNode node :jsonNode){
                    parseJSON2List(node,key,resultList);
                }
                //节点是对象
            } else if (jsonNode.isObject()) {
                parseJSON2List(jsonNode,key,resultList);
            } else {
                if ( value!= null && StringUtils.isNotEmpty(key)){
                    // 如果内层是普通对象的话，直接放入map中
                    if(StringUtils.isBlank(parentKey)){
                        resultList.add(key + "=" + value );
                    }else {
                        resultList.add(parentKey + "-"+ key+ "=" + value);

                    }
                }
            }
        }

        return resultList;
    }




}
