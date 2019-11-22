package com.healerjean.proj.a_test.json.netSfJson;

import com.healerjean.proj.a_test.json.JsonDemoDTO;
import com.healerjean.proj.common.constant.CommonConstants;
import com.healerjean.proj.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName D01NetSfJson
 * @date 2019/8/3  12:39.
 * 不要用它来进行String----》对象的转化，很多问题
 */
@Slf4j
public class D01NetSfJson {


    /**
     * JSONObject的获取数据以及基本使用
     * 1、从JSONObject中获取简单数据
     * 2、JSONArray中获取 JSONObject对象
     * 3、判断JSONObject是否是数组，是否存在节点
     * 4、简单遍历 JSONObject
     * 5、遍历JSONArray（（数组或者Json等都可以用它））
     */
    @Test
    public void test1() {

        String json = JsonDemoDTO.jsonString();
        log.info("jsonString：【 {} 】", json);
        JSONObject jsonObject = JSONObject.fromObject(json);

        //1、从JSONObject中获取简单数据
        log.info("------------------------------------------------");
        log.info("1、从JSONObject中获取简单数据");
        String msg = jsonObject.getString(CommonConstants.msg);
        Integer code = jsonObject.getInt(CommonConstants.code);
        JSONObject userJsonObject = jsonObject.getJSONObject(CommonConstants.user);
        JSONArray companyJSONArray = jsonObject.getJSONArray(CommonConstants.companys);
        JSONArray strJsonArrayy = jsonObject.getJSONArray(CommonConstants.strList);
        log.info("msg：【 {} 】", msg);
        log.info("code：【 {} 】", code);
        log.info("userJsonObject：【 {} 】", userJsonObject);
        log.info("companyJsonArray：【 {} 】", companyJSONArray);
        log.info("strJsonArrayy：【 {} 】", strJsonArrayy);

        // 2、JSONArray中获取 JSONObject对象
        log.info("------------------------------------------------");
        log.info("2、JSONArray中获取 JSONObject对象");
        JSONObject company0 = companyJSONArray.getJSONObject(0);
        log.info("company0：【 {} 】", company0);
        // 虽然有下面这个方法，但是也要看情况是否能用，下面的会报错,因为不是一个JsonObject数据
        // 报错信息net.sf.json.JSONException: JSONArray[0] is not a JSONObject.
        // JSONObject str0 = jsonObject.getJSONArray(CommonConstants.strList).getJSONObject(0);
        // log.info("str0：【 {} 】", str0);


        // 3、判断JSONObject是否是数组，是否存在节点
        log.info("------------------------------------------------");
        log.info("3、判断JSONObject是否是数组，是否存在节点");
        Boolean flagArray = companyJSONArray.isArray();
        Boolean flagHas = jsonObject.has(CommonConstants.msg);
        Boolean flagContainsKey = jsonObject.containsKey(CommonConstants.msg);
        log.info("flagArray：【 {} 】", flagArray);
        log.info("flag：【 {} 】", flagHas);
        log.info("flagContainsKey：【 {} 】", flagContainsKey);

        //4、简单遍历 JSONObject
        log.info("------------------------------------------------");
        log.info("4、简单遍历 JSONObject");
        for (Object key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            log.info("key：【 {} 】,value：【 {} 】", key, value);
        }

        //5.1.、遍历JSONArray（（数组或者Json等都可以用它））
        log.info("------------------------------------------------");
        log.info("5.1.、遍历JSONArray（（数组或者Json等都可以用它））");
        Iterator iteratorCompany = companyJSONArray.iterator();
        while (iteratorCompany.hasNext()) {
            Object object = iteratorCompany.next();
            log.info("object：【 {} 】", object);
        }



        // jsonString：【 {"reqSn":"f20b7c4901734a638b9c6ed5e5249f8d","code":200,"msg":"Success","transDate":1574339468774,"integer":100,"bigDecimal":100,"user":{"userId":1,"userName":"HealerJean"},"strList":["奔驰","宝马"],"companys":[{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}]} 】[39]
        //         ------------------------------------------------[43]
        // 1、从JSONObject中获取简单数据[44]
        // msg：【 Success 】[50]
        // code：【 200 】[51]
        // userJsonObject：【 {"userId":1,"userName":"HealerJean"} 】[52]
        // companyJsonArray：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】[53]
        // strJsonArrayy：【 ["奔驰","宝马"] 】[54]
        //         ------------------------------------------------[57]
        // 2、JSONArray中获取 JSONObject对象[58]
        // company0：【 {"companyId":1,"companyName":"汽车公司"} 】[60]
        //         ------------------------------------------------[68]
        // 3、判断JSONObject是否是数组，是否存在节点[69]
        // flagArray：【 true 】[73]
        // flag：【 true 】[74]
        // flagContainsKey：【 true 】[75]
        //         ------------------------------------------------[78]
        // 4、简单遍历 JSONObject[79]
        // key：【 reqSn 】,value：【 f20b7c4901734a638b9c6ed5e5249f8d 】[82]
        // key：【 code 】,value：【 200 】[82]
        // key：【 msg 】,value：【 Success 】[82]
        // key：【 transDate 】,value：【 1574339468774 】[82]
        // key：【 integer 】,value：【 100 】[82]
        // key：【 bigDecimal 】,value：【 100 】[82]
        // key：【 user 】,value：【 {"userId":1,"userName":"HealerJean"} 】[82]
        // key：【 strList 】,value：【 ["奔驰","宝马"] 】[82]
        // key：【 companys 】,value：【 [{"companyId":1,"companyName":"汽车公司"},{"companyId":2,"companyName":"房产公司"}] 】[82]
        //         ------------------------------------------------[86]
        // 5.1.、遍历JSONArray（（数组或者Json等都可以用它））[87]
        // object：【 {"companyId":1,"companyName":"汽车公司"} 】[91]
        // object：【 {"companyId":2,"companyName":"房产公司"} 】[91]

    }

    /**
     * 2、添加和删除字段
     * 1、基本数据、日期，对象，集合数据添加
     * 2、JSONObject放入一个JSONObject
     * 3、两个JSONObject 合并为一个
     */
    @Test
    public void add() {
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


    /**
     * 3、 复杂JSONObject遍历
     */
    @Test
    public void test3() {
        String json = JsonDemoDTO.jsonString();
        log.info("jsonString：【 {} 】", json);
        JSONObject jsonObject = JSONObject.fromObject(json);

        //1、获取Json的key集合
        for (Object key : jsonObject.keySet()) {
            //2、获取某个Key的值
            Object value = jsonObject.get(key);

            //2、判断是对象-JSONObject吗？
            if (value instanceof JSONObject) {
                log.info("key 【 {} 】， JSONObject对象 ：【 {} 】", key, value);
            }
            //3、判断是数组-JSONArray 吗？
            else if (value instanceof JSONArray) {
                //4、数组迭代
                Iterator it = ((JSONArray) value).iterator();
                while (it.hasNext()) {
                    Object object = it.next();
                    if (object instanceof JSONObject) {
                        log.info("数组中的JSONObject ：【 {} 】", value);
                    } else {
                        log.info("普通数组 ：【 {} 】", value);
                    }
                }
                //7、 普通元素
            } else {
                log.info("key 【 {} 】， 普通元素 ：【 {} 】", key, value);
            }
        }



    }

    /**
     * 4、序列化问题
     * 4.1、对象转JSONObject
     * 4.1.1、 序列化的日期 变成{"date":29,"day":2,"hours":17,"minutes":41,"month":9,"seconds":27,"time":1572342087782,"timezoneOffset":-480,"year":119} ，
     */
    @Test
    public void testStringDateOrLong() throws IOException {
        JsonDemoDTO jsonDemoDTO = new JsonDemoDTO();
        jsonDemoDTO.setTransDate(new Date());
        JSONObject jsonObject = JSONObject.fromObject(jsonDemoDTO);
        log.info("对象转JSONObject 【 {} 】", jsonObject);
        // {"code":0,"companys":[],"msg":"","reqSn":"","strList":[],"transDate":{"date":29,"day":2,"hours":17,"minutes":41,"month":9,"seconds":27,"time":1572342087782,"timezoneOffset":-480,"year":119},"user":null}
    }


}
