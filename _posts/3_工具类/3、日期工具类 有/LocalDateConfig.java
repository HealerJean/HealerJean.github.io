package com.fintech.manager.config;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import com.fintech.scf.common.utils.json.JsonUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName DateConfig
 * @Author DYB
 * @Date 2019/6/10 15:04
 * @Description
 * @Version V1.0
 */
@Configuration
public class DateConfig {
    @Bean
    public ObjectMapper serializingObjectMapper() {
        return JsonUtils.objectMapper;
    }

    /***
     * 日期参数接收转换器，将json字符串转为日期类型
     * @return
     */
    @Bean
    public Converter<String, LocalDateTime> LocalDateTimeConvert() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public LocalDateTime convert(String source) {

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime date = null;
                try {
                    date = LocalDateTime.parse((String) source,df);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }

    /***
     * 日期参数接收转换器，将json字符串转为日期类型
     * @return
     */
    @Bean
    public Converter<String, LocalDate> LocalDateConvert() {
        return new Converter<String, LocalDate>() {
            @Override
            public JavaType getInputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public JavaType getOutputType(TypeFactory typeFactory) {
                return null;
            }

            @Override
            public LocalDate convert(String source) {

                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = null;
                try {
                    date = LocalDate.parse((String) source,df);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return date;
            }
        };
    }
}
