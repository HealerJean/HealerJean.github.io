package com.healerjean.proj.utils.gray;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * GrayEnum
 * @author zhangyujin1
 * @date 2021/4/14  2:24 下午.
 */
public interface GrayEnum {


    /**
     * 灰度进度
     */
    @Getter
    @AllArgsConstructor
    enum GrayResEnum implements GrayEnum {

        /**
         * GRAY_CLOSE
         */
        GRAY_WHITE_TRUE( "whiteHit", true,"命中灰度白名单"),
        GRAY_TRUE("hit" ,true,"命中灰度"),

        GRAY_NOT_EXIST( "notExist", false,"无灰度"),
        GRAY_BLACK_TRUE( "blackHit", false,"命中灰度白名单"),
        GRAY_FALSE("unHit", false, "未命中"),
        ;


        /**
         * 灰度详细结果
         */
        private final String code;

        /**
         * 灰度结果
         */
        private final Boolean flag;

        /**
         * desc
         */
        private final String desc;

    }



    /**
     * 灰度业务
     */
    @Getter
    @AllArgsConstructor
    enum GrayBusinessEnum implements GrayEnum {
        /**
         * INSURANCE_6067
         */
        BUSINESS_OOO1("businessDemo", "灰度场景1"),
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
         * @param code code
         * @return GrayBusinessEnum
         */
        public static GrayBusinessEnum toGrayBusinessEnum(String code) {
            return Arrays.stream(GrayBusinessEnum.values()).filter(item->item.getCode().equals(code)).findAny().orElse(null);
        }
    }

}
