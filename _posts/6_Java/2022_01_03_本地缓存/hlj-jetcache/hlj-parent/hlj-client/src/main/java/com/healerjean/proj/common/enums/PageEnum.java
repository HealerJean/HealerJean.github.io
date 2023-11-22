package com.healerjean.proj.common.enums;

import java.util.Arrays;

/**
 * PageEnum
 *
 * @author zhangyujin
 * @date 2023/6/14  14:19.
 */
public interface PageEnum {


    /**
     * OrderDirectionEnum
     */
    enum OrderDirectionEnum {

        /**
         * OrderDirectionEnum
         */
        ASC("asc", "升序"),
        DESC("desc", "降序");

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;


        /**
         * OrderDirectionEnum
         * @param code code
         * @param desc desc
         */
        OrderDirectionEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        /**
         * toOrderDirectionEnum
         * @param code code
         * @return OrderDirectionEnum
         */
        public static OrderDirectionEnum toOrderDirectionEnum(String code){
            return Arrays.stream(OrderDirectionEnum.values()).filter(item->item.getCode().equals(code)).findAny().orElse(null);
        }


        /**
         * getCode
         * @return String
         */
        public String getCode() {
            return this.code;
        }

        /**
         * getDesc
         * @return String
         */
        public String getDesc() {
            return this.desc;
        }
    }

}
