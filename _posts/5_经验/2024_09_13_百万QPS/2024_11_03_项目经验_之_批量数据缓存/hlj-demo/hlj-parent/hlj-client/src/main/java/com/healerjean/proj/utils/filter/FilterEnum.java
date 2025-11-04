package com.healerjean.proj.utils.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CompressedEnum
 *
 * @author zhangyujin
 * @date 2025/1/7
 */
public interface FilterEnum {


    /**
     * CompressedEnum
     */
    @Getter
    @AllArgsConstructor
    enum CompressedEnum implements FilterEnum {

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

    /**
     * BloomEnum
     */
    @Getter
    @AllArgsConstructor
    enum BloomEnum implements FilterEnum {

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
