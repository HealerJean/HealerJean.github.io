package com.hlj.proj.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utils - JSON
 */
public final class JsonUtils {

    /**
     * ObjectMapper
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectMapper OBJECT_MAPPER_SENSITIVITY = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //NULL 不打印
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 排除json字符串中实体类没有的字段
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //LocalDateTime   LocalDate LocalTime 转化成 String
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        OBJECT_MAPPER.registerModule(javaTimeModule);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 1、将对象转换为JSON字符串
     */
    public static String toJsonString(Object value) {
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 2.1、将JSON字符串转换为对象
     */
    public static <T> T toObject(String json, Class<T> c) {
        try {
            return OBJECT_MAPPER.readValue(json, c);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 2.2、Json格式字符串转对象 ArrayList
     */
    public static <T> List<T> toArrayList(String json, Class<T> c) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory()
                .constructParametricType(ArrayList.class, c);
        List<T> t = null;
        try {
            t = OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException("参数格式有误");
        }
        return t;
    }

    /**
     * 2.3、Json格式字符串转集合
     * collect 仅限于 ArrayList HashSet 等集合对象
     */
    public static <T> T toArrayList(String json, Class c, Class collect) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collect, c);
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException("参数格式有误");
        }
    }

    /**
     * 2.5、将JSON字符串转换为对象
     */
    public static <T> T toObject(String json, JavaType javaType) {
        try {
            return OBJECT_MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 2.4、将JSON字符串转换为集合、map 不可以是对象
     * List<JavaBean> list =  JsonUtils.toObject(jsonArrayStr, new TypeReference<List<JavaBean>>() { });
     * Map<String, Object> map =  JsonUtils.toObject(JsonUtils.toJson(javaBean),new TypeReference<Map<String, Object>>( ){} );
     */
    public static <T> T toObject(String json, TypeReference<?> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为树
     */
    public static JsonNode toJsonNode(String json) {
        try {
            return OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串转换为toObjectNode
     */
    public static ObjectNode toObjectNode(String json) {
        try {
            return (ObjectNode) OBJECT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 放入一个对象
     */
    public static String putObject(String jsonString, String key, String replaceString) {
        ObjectNode jsonNode = null;
        ObjectNode keyJsonNode = null;
        try {
            jsonNode = (ObjectNode) OBJECT_MAPPER.readTree(jsonString);
            keyJsonNode = (ObjectNode) OBJECT_MAPPER.readTree(replaceString);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        jsonNode.putPOJO(key, keyJsonNode);
        return JsonUtils.toJsonString(jsonNode);
    }


    /**
     * 通过 OBJECT_MAPPER.readValue(json, javaType) 转化为对象
     * 参数举例： A.class
     */
    public static JavaType toJavaType(Type type) {
        return OBJECT_MAPPER.getTypeFactory().constructType(type);
    }

    /**
     * 通过 BJECT_MAPPER.readValue(json, javaType) 转化为 集合、map等
     * 参数举例：new TypeReference<List<JavaBean>>() { }
     */
    public static JavaType toJavaType(TypeReference<?> typeReference) {
        return OBJECT_MAPPER.getTypeFactory().constructType(typeReference);
    }





}
