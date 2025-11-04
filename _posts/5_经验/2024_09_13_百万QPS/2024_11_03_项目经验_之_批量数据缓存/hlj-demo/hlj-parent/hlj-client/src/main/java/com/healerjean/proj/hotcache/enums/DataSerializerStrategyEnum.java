package com.healerjean.proj.hotcache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * DataSerializerEnum
 *
 * @author zhangyujin
 * @date 2025/11/4
 */
@Getter
@AllArgsConstructor
public enum DataSerializerStrategyEnum {
    FAST_JSON("fastjson"),
    PROTOBUF("Protobuf"),
    ;
    private final String code;


    /**
     * getDataSerializerStrategyEnum
     *
     * @param code code
     * @return {@link DataSerializerStrategyEnum}
     */
    public static DataSerializerStrategyEnum getDataSerializerStrategyEnumByCode(String code) {
        return Arrays.stream(DataSerializerStrategyEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }


}