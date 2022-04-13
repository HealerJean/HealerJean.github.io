package com.healerjean.proj.util.xml.strategy;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @ClassName DecimalSerialize
 * @Author TD
 * @Date 2018/12/10 10:41
 * @Description BigDecimal转换策略，去掉小数的转换
 */
public class DecimalSerialize extends JsonSerializer<BigDecimal> {


    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String value = null;
        if (bigDecimal != null) {
            BigDecimal b = bigDecimal.setScale(0);
            value = b.toString();
        }
        jsonGenerator.writeString(value);
    }
}
