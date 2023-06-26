package com.healerjean.proj.utils.log4j2;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.healerjean.proj.utils.sensitivity.SensitiveInfo;
import com.healerjean.proj.utils.sensitivity.SensitiveInfoUtils;
import com.healerjean.proj.utils.sensitivity.SensitiveTypeEnum;
import com.healerjean.proj.utils.sensitivity.SensitivityConstants;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 脱敏拦截器
 *
 * @author zhangyujin
 * @date 2023/6/14  21:27
 */
public class LogValueFilter implements ValueFilter {


    /**
     * process
     *
     * @param object        object
     * @param propName      propName
     * @param propertyValue propertyValue
     * @return
     */
    @Override
    public Object process(Object object, String propName, Object propertyValue) {
        if (!(propertyValue instanceof String)) {
            return propertyValue;
        }
        //判断是否有注解
        Field declaredField;
        try {
            declaredField = object.getClass().getDeclaredField(propName);
        } catch (NoSuchFieldException e) {
            return propertyValue;
        }
        declaredField.setAccessible(true);
        SensitiveInfo sensitiveInfo = declaredField.getAnnotation(SensitiveInfo.class);
        if (sensitiveInfo != null) {
            return SensitiveInfoUtils.sensitiveValue(sensitiveInfo.value(), String.valueOf(propertyValue));
        }
        //正则匹配
        for (Map.Entry<String, SensitiveTypeEnum> entry : SensitivityConstants.sensitivityRules.entrySet()) {
            String rule = entry.getKey();
            int length = rule.length();
            int propLen = propName.length();
            if (propName.length() < length) {
                continue;
            }
            int temp = rule.indexOf("*");
            String key;
            String substring;
            if (temp >= 0) {
                if (temp < (length >> 2)) {
                    //*在开头
                    key = rule.substring(temp + 1, length);
                    substring = propName.substring(propLen - key.length(), propLen);
                } else {
                    //*在结尾
                    key = rule.substring(0, temp);
                    substring = propName.substring(0, temp);
                }
                if (substring.equals(key)) {
                    return SensitiveInfoUtils.sensitiveValue(entry.getValue(), String.valueOf(propertyValue));
                }
            } else if (rule.equals(propName)) {
                return SensitiveInfoUtils.sensitiveValue(entry.getValue(), String.valueOf(propertyValue));
            }
        }
        return propertyValue;
    }
}
