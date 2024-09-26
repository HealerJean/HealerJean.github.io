package com.hlj.util.z028_灰度工具;

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

        GRAY_CLOSE( "close", false,"灰度关闭"),
        GRAY_NOT_EXIST( "notExist", false,"灰度不存在"),
        GRAY_BLACK_TRUE( "blackHit", false,"命中灰度白名单"),
        GRAY_FALSE("unHit", false, "未命中或者灰度不存在"),
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
         * insuranceId
         */
        private final String bizType;

        /**
         * desc
         */
        private final String bizDesc;


        /**
         * GrayBusinessEnum
         * @param bizType insuranceId
         * @return GrayBusinessEnum
         */
        public static GrayBusinessEnum toGrayBusinessEnum(String bizType) {
            return Arrays.stream(GrayBusinessEnum.values()).filter(item->item.getBizType().equals(bizType)).findAny().orElse(null);
        }
    }

}
