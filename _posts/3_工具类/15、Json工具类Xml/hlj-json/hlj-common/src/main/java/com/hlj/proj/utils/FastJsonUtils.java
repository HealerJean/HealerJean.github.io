package com.hlj.proj.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author HealerJean
 * @ClassName FastJsonUtils
 * @date 2019/9/12  14:54.
 * @Description
 * JSON是一个抽象类，JSON中有一个静态方法parseObject(String text)，将text解析为一个JSONObject对象并返回;
 * JSONObject是一个继承自JSON的类，当调用JSONObject.parseObject(result)时，会直接调用父类的parseObject(String text)。
 * 所以两者没什么区别，一个是用父类去调用父类自己的静态的parseObject(String text)，一个是用子类去调用父类的静态parseObject(String text)，两者调的是同一个方法
 */
public class FastJsonUtils {


    public static final String DEFAULT_DATEFORMAT = "yyyy-MM-dd HH:mm:ss";

    public static <T> T toObject(String str, Class<T> clazz) {
        // return JSON.parseObject(str, clazz);
       return   JSONObject.parseObject(str, clazz);

    }

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * json格式解析为List集合，不解决格式时间问题
     */
    public static <T> List<T> jsonTolist(String str, Class<T> clazz) {
        return JSON.parseArray(str, clazz);
    }

}
