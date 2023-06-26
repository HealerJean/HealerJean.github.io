package com.healerjean.proj.common.enums;

/**
 * StatusEnum
 *
 * @author zhangyujin
 * @date 2023/6/14  15:02.
 */
public interface SystemEnum {

    /**
     * ValidEnum
     */
    enum StatusEnum {

        /**
         * ValidEnum
         */
        VALID(1, "有效"),
        TRASH(0, "失效");

        /**
         * code
         */
        private final Integer code;

        /**
         * desc
         */
        private final String desc;


        /**
         * OrderDirectionEnum
         * @param code code
         * @param desc desc
         */
        StatusEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }


        /**
         * getCode
         * @return String
         */
        public Integer getCode() {
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
