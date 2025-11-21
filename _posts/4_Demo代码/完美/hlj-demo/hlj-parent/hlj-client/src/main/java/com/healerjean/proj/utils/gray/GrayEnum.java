package com.healerjean.proj.utils.gray;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 灰度枚举接口
 *
 * @author zhangyujin1
 * @date 2021/4/14 2:24 下午
 */
public interface GrayEnum {

    /**
     * 灰度结果枚举
     */
    @Getter
    @AllArgsConstructor
    enum GrayResEnum implements GrayEnum {

        /**
         * 命中灰度白名单
         */
        GRAY_WHITE_TRUE("whiteHit", true, "命中灰度白名单"),

        /**
         * 命中灰度
         */
        GRAY_TRUE("hit", true, "命中灰度"),

        /**
         * 无灰度配置
         */
        GRAY_NOT_EXIST("notExist", false, "无灰度"),

        /**
         * 命中灰度黑名单
         */
        GRAY_BLACK_TRUE("blackHit", false, "命中灰度黑名单"),

        /**
         * 未命中灰度
         */
        GRAY_FALSE("unHit", false, "未命中");

        /**
         * 灰度详细结果码
         */
        private final String code;

        /**
         * 灰度结果标识
         */
        private final Boolean flag;

        /**
         * 结果描述
         */
        private final String desc;

        /**
         * 根据code获取灰度结果枚举
         *
         * @param code 结果码
         * @return 灰度结果枚举
         */
        public static Optional<GrayResEnum> fromCode(String code) {
            return Arrays.stream(GrayResEnum.values())
                    .filter(item -> item.getCode().equals(code))
                    .findFirst();
        }
    }

    /**
     * 灰度业务枚举
     */
    @Getter
    @AllArgsConstructor
    enum GrayBusinessEnum implements GrayEnum {

        /**
         * 演示业务
         */
        BUSINESS_OOO1("demo", "演示业务"),

        ;

        /**
         * 业务编码
         */
        private final String code;

        /**
         * 业务描述
         */
        private final String desc;

        /**
         * 根据code获取灰度业务枚举
         *
         * @param code 业务编码
         * @return 灰度业务枚举，如果未找到返回null
         */
        public static Optional<GrayBusinessEnum> fromCode(String code) {
            return Arrays.stream(GrayBusinessEnum.values())
                    .filter(item -> item.getCode().equals(code))
                    .findFirst();
        }
    }
}