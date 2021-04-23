package com.hlj.util.z030_map转bean工具.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;

public class BeanMapUtils {

    /**
     * 将对象属性转化为map结合
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = new HashMap<>();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) != null){
                    map.put(key+"", beanMap.get(key));
                }
            }
        }
        return map;
    }

    /**
     * 将map集合中的数据转化为指定对象的同名属性中
     */
    public static <T> T mapToBean(Map<String, Object> map, Class<T> clazz){
        try {
            T bean = clazz.newInstance();
            BeanUtils.populate(bean,map);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
