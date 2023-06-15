package com.healerjean.proj.common.enums;


/**
 * CodeEnum
 *
 * @author zhangyujin
 * @date 2023/6/12  10:41.
 */
public interface CodeEnum {


    enum ErrorCodeEnum implements CodeEnum {

        ERROR_CODE_SUCCESS("00000", "成功"),
        ERROR_CODE_PARAMS_ERROR("10000", "参数错误"),
        ERROR_CODE_BUSINESS_ERROR("20000", "业务处理失败"),
        ERROR_CODE_PRC_ERROR("30000", "接口调用失败"),
        ERROR_CODE_FAIL("99999", "系统太火爆了，请稍后重试!"),
        ;
        private final String code;
        private final String msg;

        ErrorCodeEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public String getCode() {
            return code;
        }

    }



    enum BusinessErrorEnum implements CodeEnum {

        ERROR_CODE_20000("20000", "业务处理失败", "系统太火爆了，请稍后重试!"),
        ERROR_CODE_20001("20001", "订单创建失败", "您有一个订单正在创建，请稍后查看"),
        ERROR_CODE_20002("20002", "付款失败，存在创建中的订单", "您的订单付款失败，请稍后查看"),
        ERROR_CODE_20003("20003", "付款失败，存在未支付的订单", "您的订单付款失败，请稍后查看"),

        ;
        private final String code;

        private final String msg;

        private final String showMsg;

        BusinessErrorEnum(String code, String msg, String showMsg) {
            this.code = code;
            this.msg = msg;
            this.showMsg = showMsg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        public String getShowMsg() {
            return showMsg;
        }
    }


    enum RpcErrorEnum implements CodeEnum {

        ERROR_CODE_30000("30000", "RPC调用失败"),
        ERROR_CODE_30001("30001", "模版查询-接口请求异常"),
        ERROR_CODE_30002("30002", "协议查询-接口请求异常"),
        ;
        private String code;
        private String msg;

        RpcErrorEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public String getCode() {
            return code;
        }

    }

}