package com.jd.merchant.mq.route.enums;

import java.util.Arrays;

/**
 * GrayEnum
 * @author zhangyujin1
 * @date 2021/4/14  2:24 下午.
 */
public interface GrayEnum {

    /**
     * 灰度业务
     */
    enum GrayBusinessEnum implements GrayEnum {
        /**
         * INSURANCE_6067
         */
        INSURANCE_6067("6067", "晚发赔"),
        INSURANCE_6072("6072", "6072"),
        INSURANCE_6073("6073", "6073"),
        INSURANCE_6074("6074", "6074"),
        ;

        /**
         * insuranceId
         */
        private final String insuranceId;
        /**
         * desc
         */
        private final String desc;

        /**
         * GrayBusinessEnum
         *
         * @param insuranceId insuranceId
         * @param desc        desc
         */
        GrayBusinessEnum(String insuranceId, String desc) {
            this.insuranceId = insuranceId;
            this.desc = desc;
        }

        public String getInsuranceId() {
            return insuranceId;
        }

        public String getDesc() {
            return desc;
        }

        /**
         * GrayBusinessEnum
         * @param insuranceId insuranceId
         * @return GrayBusinessEnum
         */
        public static GrayBusinessEnum toGrayBusinessEnum(String insuranceId) {
            return Arrays.stream(GrayBusinessEnum.values()).filter(item->item.getInsuranceId().equals(insuranceId)).findAny().orElse(null);
        }
    }


    /**
     * 灰度业务
     */
    enum GraySwitchEnum implements GrayEnum {

        /**
         * GRAY_CLOSE
         */
        GRAY_CLOSE("GrayClose", "灰度关闭"),
        GRAY_PERCENT("Percent", "灰度中"),
        ALL_PERCENT("AllPercent", "全量"),

        ;

        /**
         * code
         */
        private final String code;
        /**
         * desc
         */
        private final String desc;

        /**
         * GrayBusinessEnum
         *
         * @param code insuranceId
         * @param desc desc
         */
        GraySwitchEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * GraySwitchEnum
         *
         * @param code code
         * @return GraySwitchEnum
         */
        public static GraySwitchEnum toGraySwitchEnum(String code) {
            return Arrays.stream(GraySwitchEnum.values()).filter(item -> item.getCode().equals(code)).findAny().orElse(null);
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }


}
