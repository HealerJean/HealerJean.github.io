package com.healerjean.proj.a_test.json.jackson.d02_JsonType;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.math.BigDecimal;


@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Human.Man.class, name = "man"),
        @JsonSubTypes.Type(value = Human.Woman.class, name = "woman"),
})
public class Human {

    private String district;

    @Data
    public static class Man extends Human {
        private String manField;
    }

    @Data
    public static class Woman extends Human {
        private String womanField;
    }

}
