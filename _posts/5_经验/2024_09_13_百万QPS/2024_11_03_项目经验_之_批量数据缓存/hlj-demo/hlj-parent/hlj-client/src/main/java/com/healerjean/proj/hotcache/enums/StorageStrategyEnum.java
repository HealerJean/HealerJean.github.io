package com.healerjean.proj.hotcache.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * StorageEnum
 *
 * @author zhangyujin
 * @date 2025/11/4
 */
@AllArgsConstructor
@Getter
public enum StorageStrategyEnum {

    LOCAL("local", "本地测试使用"),

    OSS("oss", "oss"),
    ;

    /**
     * code
     */
    private final String code;
    /**
     * desc
     */
    private final String desc;


    public static StorageStrategyEnum getStorageStrategyEnumByCode(String code) {
        return Arrays.stream(StorageStrategyEnum.values()).filter(item -> item.getCode().equals(code)).findFirst().orElse(null);
    }
}
