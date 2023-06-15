package com.healerjean.proj.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.healerjean.proj.utils.sensitivity.SensitiveInfoUtils;
import com.healerjean.proj.utils.sensitivity.SensitiveSerializerModifier;
import com.healerjean.proj.utils.sensitivity.SensitiveTypeEnum;
import com.healerjean.proj.utils.sensitivity.SensitivityConstants;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

/**
 * JsonUtils
 *
 * @author zhangyujin
 * @date 2023/6/14  21:34
 */
public final class JsonUtils {

    /**
     * ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    /**
     * OBJECT_MAPPER_SENSITIVITY
     */
    private static final ObjectMapper OBJECT_MAPPER_SENSITIVITY = new ObjectMapper();


    static {
        //LocalDateTime   LocalDate LocalTime 转化成 String
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //NULL 不打印
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 排除json字符串中实体类没有的字段
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);


        //脱敏日志创建
        OBJECT_MAPPER_SENSITIVITY.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER_SENSITIVITY.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        OBJECT_MAPPER_SENSITIVITY.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER_SENSITIVITY.registerModule(javaTimeModule);
        OBJECT_MAPPER_SENSITIVITY.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //脱敏
        OBJECT_MAPPER_SENSITIVITY.setSerializerFactory(OBJECT_MAPPER_SENSITIVITY.getSerializerFactory().withSerializerModifier(new SensitiveSerializerModifier()));

    }


    /**
     * 将对象转换为JSON字符串
     *
     * @param value 对象
     * @return JSON字符串
     */
    public static String toString(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为对象
     *
     * @param json JSON字符串
     * @param c    类型
     * @return 对象
     */
    public static <T> T toObject(String json, Class<T> c) {
        try {
            return OBJECT_MAPPER.readValue(json, c);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 把json字符串转为未知类型对象
     *
     * @param json JSON字符串
     * @param type 类型
     * @return 对象
     */
    public static <T> T toObject(String json, Type type) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.getTypeFactory().constructType(type));

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 将JSON字符串转换为对象
     *
     * @param json     JSON字符串
     * @param javaType 类型
     * @return 对象
     */
    public static <T> T toObject(String json, JavaType javaType) {
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 把json字符串转为泛型对象
     *
     * @param json          JSON字符串
     * @param typeReference 类型 可以通过这个转化为List集合 ，举例：
     *                      List<JavaBean> list =  JsonUtils.toObject(jsonArrayStr, new TypeReference<List<JavaBean>>() { });
     *                      Map<String, Object> map =  JsonUtils.toObject(JsonUtils.toJson(javaBean),new TypeReference<Map<String, Object>>( ){} );
     * @return 对象
     */
    public static <T> T toObject(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 将JSON字符串转换为树
     *
     * @param json JSON字符串
     * @return 树
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为JSON流
     *
     * @param writer Writer
     * @param value  对象
     */
    public static void writeValue(Writer writer, Object value) {
        try {
            OBJECT_MAPPER.writeValue(writer, value);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * 将来通过 OBJECT_MAPPER.readValue(json, javaType) 转化为对象
     *
     * @param type （java.class）
     * @return 类型
     */
    public static JavaType constructType(Type type) {
        return OBJECT_MAPPER.getTypeFactory().constructType(type);
    }

    /**
     * 将来通过 OBJECT_MAPPER.readValue(json, javaType) 转化为 集合
     *
     * @param typeReference 类型 new TypeReference<List<JavaBean>>() { }
     * @return 类型
     */
    public static JavaType constructType(TypeReference<?> typeReference) {
        return OBJECT_MAPPER.getTypeFactory().constructType(typeReference);
    }


    /**
     * 对象转Json格式字符串----脱敏处理(包含map)
     *
     * @return String
     */
    public static String toJsonStringWithSensitivity(Object propName) {
        if (Objects.isNull(propName)) {
            return null;
        }
        try {
            return OBJECT_MAPPER_SENSITIVITY.writeValueAsString(propName);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    /**
     * dealSensitivity
     *
     * @param mapKey   mapKey
     * @param mapValue mapValue
     * @return String
     */
    private static String dealSensitivity(String mapKey, String mapValue) {
        //正则匹配
        for (Map.Entry<String, SensitiveTypeEnum> entry : SensitivityConstants.sensitivityRules.entrySet()) {
            String rule = entry.getKey();
            int length = rule.length();
            int propLen = mapKey.length();
            if (mapKey.length() < length) {
                continue;
            }
            int temp = rule.indexOf("*");
            String key;
            String substring;
            if (temp >= 0) {
                if (temp < (length >> 2)) {
                    key = rule.substring(temp + 1, length);
                    substring = mapKey.substring(propLen - key.length(), propLen);
                } else {
                    key = rule.substring(0, temp);
                    substring = mapKey.substring(0, temp);
                }
                if (substring.equals(key)) {
                    return SensitiveInfoUtils.sensitiveValue(entry.getValue(), mapValue);
                }
            } else if (rule.equals(mapKey)) {
                return SensitiveInfoUtils.sensitiveValue(entry.getValue(), mapValue);
            }
        }
        return mapValue;
    }


}
