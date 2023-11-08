package com.healerjean.proj.service.bizlog.common.enums;

import com.healerjean.proj.service.bizlog.service.function.impl.DefaultParseFunction;
import com.healerjean.proj.service.bizlog.service.function.impl.OrderParseFunction;
import com.healerjean.proj.service.bizlog.service.function.impl.UserParseFunction;

/**
 * @author zhangyujin
 * @date 2023/5/31  16:23.
 */
public interface BizLogEnum {

    /**
     * IParseFunctionEnum
     *
     * @author zhangyujin
     * @date 2023/5/31  15:23.
     */
    enum IParseFunctionEnum {


        /**
         * 默认
         */
        DEFAULT("default", DefaultParseFunction.class),
        ORDER_PARSE_FUNCTION("orderParse", OrderParseFunction.class),
        USER_PARSE_FUNCTION("userParse", UserParseFunction.class),


        ;


        /**
         * clazz
         */
        private final String function;

        /**
         * clazz
         */
        private final Class<?> clazz;

        /**
         * IParseFunctionEnum
         *
         * @param function function
         * @param clazz    clazz
         */
        IParseFunctionEnum(String function, Class<?> clazz) {
            this.function = function;
            this.clazz = clazz;
        }

        public String getFunction() {
            return function;
        }

        public Class<?> getClazz() {
            return clazz;
        }
    }


    /**
     * LogRecordTypeEnum
     *
     * @author zhangyujin
     * @date 2023/5/31  16:21.
     */
    enum LogRecordTypeEnum {

        /**
         * ORDER_TYPE
         */
        ORDER_TYPE("OrderType", "订单类型");

        /**
         * code
         */
        private final String code;

        /**
         * desc
         */
        private final String desc;

        /**
         * LogRecordTypeEnum
         *
         * @param code code
         * @param desc desc
         */
        LogRecordTypeEnum(String code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public String getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }
    }


}
