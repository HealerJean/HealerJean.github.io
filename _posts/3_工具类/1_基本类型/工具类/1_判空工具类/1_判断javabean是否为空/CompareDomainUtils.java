package com.jd.baoxian.merchant.route.service.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.jd.baoxian.merchant.route.common.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author zhangyujin
 * @date 2022/8/8  11:16.
 */
@Slf4j
public class CompareDomainUtils {

    /**
     * 排除字段
     */
    private static final List<String> EXCLUDE_FIELD_NAMES = Lists.newArrayList("serialVersionUID");

    /**
     * 两个对象必须一模一样
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @param <T>
     * @return 返回差异
     */
    public static <T> List<String> compare(T obj1, T obj2, String... ignoreFieldNames) {
        List<String> diffList = new ArrayList<>();

        try {
            Class<?> obj1Class = obj1.getClass();
            Class<?> obj2Class = obj2.getClass();
            Field[] fields = obj1Class.getDeclaredFields();
            for (Field field1 : fields) {
                field1.setAccessible(true);
                String fieldName = field1.getName();
                if (EXCLUDE_FIELD_NAMES.contains(fieldName)) {
                    continue;
                }
                if (ignoreFieldNames != null && Arrays.asList(ignoreFieldNames).contains(fieldName)) {
                    continue;
                }
                Field field2 = obj2Class.getDeclaredField(fieldName);
                field2.setAccessible(true);
                // 如果类型是String
                if ("class java.lang.String".equals(field1.getGenericType().toString())) {
                    Object o1 = field1.get(obj1);
                    Object o2 = field2.get(obj2);
                    if (o1 == null && o2 == null) {
                        continue;
                    }
                    if (o1 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    if (o2 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    String val1 = (String) o1;
                    String val2 = (String) o2;
                    if (StringUtils.equals(val1, val2)) {
                        continue;
                    }
                    diffList.add(fieldName + ":" + o1 + "," + o2);
                    continue;
                }

                if ("class java.lang.Long".equals(field1.getGenericType().toString())) {
                    Object o1 = field1.get(obj1);
                    Object o2 = field2.get(obj2);
                    if (o1 == null && o2 == null) {
                        continue;
                    }
                    if (o1 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    if (o2 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    Long val1 = (Long) o1;
                    Long val2 = (Long) o2;
                    if (val1.compareTo(val2) == 0) {
                        continue;
                    }
                    diffList.add(fieldName + ":" + o1 + "," + o2);
                    continue;
                }

                if ("class java.lang.Integer".equals(field1.getGenericType().toString())) {
                    Object o1 = field1.get(obj1);
                    Object o2 = field2.get(obj2);
                    if (o1 == null && o2 == null) {
                        continue;
                    }
                    if (o1 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    if (o2 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    Integer val1 = (Integer) o1;
                    Integer val2 = (Integer) o2;
                    if (val1.compareTo(val2) == 0) {
                        continue;
                    }
                    diffList.add(fieldName + ":" + o1 + "," + o2);
                    continue;
                }

                if ("class java.lang.Boolean".equals(field1.getGenericType().toString())) {
                    Object o1 = field1.get(obj1);
                    Object o2 = field2.get(obj2);
                    if (o1 == null && o2 == null) {
                        continue;
                    }
                    if (o1 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    if (o2 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    Boolean val1 = (Boolean) o1;
                    Boolean val2 = (Boolean) o2;
                    if (val1.equals(val2)) {
                        continue;
                    }
                    diffList.add(fieldName + ":" + o1 + "," + o2);
                    continue;
                }

                if ("boolean".equals(field1.getGenericType().toString())) {
                    Object o1 = field1.get(obj1);
                    Object o2 = field2.get(obj2);
                    if (o1 == null && o2 == null) {
                        continue;
                    }
                    if (o1 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    if (o2 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    Boolean val1 = (Boolean) o1;
                    Boolean val2 = (Boolean) o2;
                    if (val1.equals(val2)) {
                        continue;
                    }
                    diffList.add(fieldName + ":" + o1 + "," + o2);
                    continue;
                }

                // 如果类型是BigDecimal(保留2位小数)
                if ("class java.math.BigDecimal".equals(field1.getGenericType().toString())) {
                    Object o1 = field1.get(obj1);
                    Object o2 = field2.get(obj2);
                    if (o1 == null && o2 == null) {
                        continue;
                    }
                    if (o1 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    if (o2 == null) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                        continue;
                    }
                    BigDecimal val1 = (BigDecimal) o1;
                    BigDecimal val2 = (BigDecimal) o2;
                    if (val1.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(val2.setScale(2, BigDecimal.ROUND_HALF_UP)) != 0) {
                        diffList.add(fieldName + ":" + o1 + "," + o2);
                    }
                    // continue;
                }
            }
        } catch (Exception e) {
            log.info("[CompareDomainUtils#compare] ERROR,  obj1:{},  obj2:{}, ", JSON.toJSONString(obj1), JSON.toJSONString(obj2), e);

        }

        return diffList;
    }

}
