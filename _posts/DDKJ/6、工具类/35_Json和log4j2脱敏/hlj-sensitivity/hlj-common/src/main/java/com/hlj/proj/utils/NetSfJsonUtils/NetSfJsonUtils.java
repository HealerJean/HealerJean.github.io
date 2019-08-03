package com.hlj.proj.utils.NetSfJsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName NetSfJsonUtils
 * @date 2019/8/3  12:39.
 * @Description
 */
public class NetSfJsonUtils {


    public static void main(String[] args) {
        String json = "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"中华\",\"address\":\"北京\"},\"cars\":[\"奔驰\",\"宝马\"]}";
        System.out.println(JSONObject.fromObject(json));
        System.out.println(parseJSON2List(JSONObject.fromObject(json)));
    }

    public static List<String> parseJSON2List(JSONObject json) {
        List<String> resultList = new LinkedList<>();
        // 最外层解析
        for (Object key : json.keySet()) {
            Object value = json.get(key);
            // 如果内层还是json数组的话，继续解析 (普通数组也是当成活Json数组去处理了) 普通数组下面 ((JSONArray) key).iterator() 会报错
            if (value instanceof JSONArray) {
                Iterator<JSONObject> it = ((JSONArray) value).iterator();
                while (it.hasNext()) {
                    Object object2 =  it.next() ;
                   if (object2 instanceof  JSONObject){
                       System.out.println("jsonObject");
                   }else {
                       System.out.println("普通字段"+object2);
                   }
                }
                // 如果内层是json对象
            } else if (value instanceof JSONObject) {

            } else {

            }
        }
            return resultList;
    }

}
