package com.healerjean.proj.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * MultilevelCacheEnum
 *
 * @author zhangyujin
 * @date 2023/11/14
 */
public interface MultilevelCacheEnum {

    /**
     * MultilevelCacheNameEnum
     */
    @AllArgsConstructor
    @Getter
    enum MultilevelCacheNameEnum {
        /**
         * MultilevelCacheNameEnum
         */
        USER_CACHE("userCache", "用户信息"),
        ;

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;
    }
}
