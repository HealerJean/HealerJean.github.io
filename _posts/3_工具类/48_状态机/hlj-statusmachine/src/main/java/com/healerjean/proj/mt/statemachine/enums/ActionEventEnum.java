package com.healerjean.proj.mt.statemachine.enums;

/**
 * @author zhangyujin
 * @date 2021/3/5  4:54 下午.
 * @description
 */
public interface ActionEventEnum {


    /**
     * 门事件枚举
     */
    enum DoorActionEventEnum implements ActionEventEnum{

        CLOSE("Close", "关闭"),
        OPEN("Open", "打开"),
        ;
        private String code ;
        private String desc ;

        DoorActionEventEnum(String code, String desc) {
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
     * 订单事件枚举
     */
    enum OrderActionEventEnum implements ActionEventEnum{

        PAY("Pay", "待支付"),
        DELIVER("Deliver", "发货"),
        RECEIVE("Receive", "收货"),
        ;

        private String code ;
        private String desc ;

        OrderActionEventEnum(String code, String desc) {
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
