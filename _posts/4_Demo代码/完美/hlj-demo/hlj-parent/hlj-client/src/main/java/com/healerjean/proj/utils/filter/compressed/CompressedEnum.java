package com.healerjean.proj.utils.filter.compressed;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CompressedEnum
 *
 * @author zhangyujin
 * @date 2025/1/7
 */
public interface CompressedEnum {


    /**
     * CompressedKeyEnum
     */
    @Getter
    @AllArgsConstructor
    enum CompressedInfoEnum implements CompressedEnum {

        DEFAULT("default", "默认"),
        ;

        /**
         * key
         */
        private final String code;


        /**
         * 描述
         */
        private final String desc;
    }
}
