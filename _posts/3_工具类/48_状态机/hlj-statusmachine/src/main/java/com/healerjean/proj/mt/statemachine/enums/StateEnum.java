package com.healerjean.proj.mt.statemachine.enums;

/**
 * @author zhangyujin
 * @date 2021/3/5  4:40 下午.
 * @description
 */
public interface StateEnum {


    enum DoorStateEnum implements StateEnum {

        CLOSED("Closed", "已关闭"),
        OPENING("Opening", "打开ing"),
        OPENED("Opened", "已打开"),
        CLOSING("Closing", "关闭ing"),
        ;
        private String code ;
        private String desc ;

        DoorStateEnum(String code, String desc) {
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


    /**
     * 订单状态枚举
     */
    enum OrderStateEnum implements StateEnum {
        WAIT_PAYMENT("WaitPayment", "待支付"),
        WAIT_DELIVER("WaitDeliver", "待发货"),
        WAIT_RECEIVE("WaitReceive", "待收货"),
        FINISH("Finish", "完结"),
        ;

        private String code ;
        private String desc ;

        OrderStateEnum(String code, String desc) {
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
