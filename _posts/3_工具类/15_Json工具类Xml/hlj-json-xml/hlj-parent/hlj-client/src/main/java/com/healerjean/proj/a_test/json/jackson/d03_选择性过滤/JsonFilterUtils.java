package com.healerjean.proj.a_test.json.jackson.d03_选择性过滤;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.stream.Stream;

/**
 * @Desc: HealerJean
 * @Date: 2018/9/20 下午2:07.
 */

public class JsonFilterUtils {

    private  static final String DYNC_INCLUDE = "DYNC_INCLUDE";
    private  static final String DYNC_FILTER = "DYNC_FILTER";

    private  Class<?> clazz;

    ObjectMapper objectMapper = null;

    public JsonFilterUtils(Class<?> clazz) {
        objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //NULL 不打印
        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 排除json字符串中实体类没有的字段
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //LocalDateTime   LocalDate LocalTime 转化成 String
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        this.objectMapper.registerModule(javaTimeModule);
        this.objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        this.clazz = clazz;
    }


    @JsonFilter(DYNC_FILTER)
    interface DynamicFilter {
    }

    @JsonFilter(DYNC_INCLUDE)
    interface DynamicInclude {
    }


    /**
     * @param propertyFunctions 转换时包含哪些字段
     */
    public <T, R> void include(PropertyFunction<T, R> ...  propertyFunctions ) {
        if (propertyFunctions.length == 0 || propertyFunctions[0] == null) {
            return;
        }
        String[] columns = Arrays.stream(propertyFunctions).map(ReflectionFieldNameUtils::getFieldName).toArray(String[]::new);
        objectMapper.setFilterProvider(new SimpleFilterProvider().addFilter(DYNC_INCLUDE,
                SimpleBeanPropertyFilter.filterOutAllExcept(columns)));
        objectMapper.addMixIn(clazz, DynamicInclude.class);
    }


    /**
     * @param propertyFunctions 转换时过滤哪些字段
     */
    public <T, R> void filter(PropertyFunction<T, R> ...  propertyFunctions ) {
        if (clazz == null || propertyFunctions.length == 0 || propertyFunctions[0] == null) {
            return;
        }
        String[] columns = Arrays.stream(propertyFunctions).map(ReflectionFieldNameUtils::getFieldName).toArray(String[]::new);
        objectMapper.setFilterProvider(new SimpleFilterProvider().addFilter(DYNC_FILTER,
                SimpleBeanPropertyFilter.serializeAllExcept(columns)));
        objectMapper.addMixIn(clazz, DynamicFilter.class);
    }



    public String toJsonString(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }
}
