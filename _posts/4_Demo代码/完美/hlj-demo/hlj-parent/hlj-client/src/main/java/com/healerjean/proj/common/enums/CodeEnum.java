package com.healerjean.proj.common.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CodeEnum
 *
 * @author zhangyujin
 * @date 2023/6/12  10:41.
 */
public interface CodeEnum {


    @Getter
    @AllArgsConstructor
    enum ErrorCodeEnum implements CodeEnum {

        ERROR_CODE_SUCCESS("00000", "成功"),
        ERROR_CODE_PARAMS_ERROR("10000", "参数错误"),
        ERROR_CODE_BUSINESS_ERROR("20000", "业务处理失败"),
        ERROR_CODE_PRC_ERROR("30000", "RPC处理失败"),
        ERROR_CODE_RUNTIME_ERROR("40000", "运行时失败"),
        ERROR_CODE_FAIL("99999", "系统太火爆了，请稍后重试!"),
        ;
        private final String code;
        private final String msg;

    }



    @Getter
    @AllArgsConstructor
    enum BusinessErrorEnum implements CodeEnum {

        ERROR_CODE_20000("20000", "业务处理失败", "系统太火爆了，请稍后重试!"),
        ERROR_CODE_20001("20001", "订单创建失败", "您有一个订单正在创建，请稍后查看"),
        ERROR_CODE_20002("20002", "付款失败，存在创建中的订单", "您的订单付款失败，请稍后查看"),
        ERROR_CODE_20003("20003", "付款失败，存在未支付的订单", "您的订单付款失败，请稍后查看"),

        ;
        private final String code;

        private final String msg;

        private final String showMsg;
    }


    @Getter
    @AllArgsConstructor
    enum ParamsErrorEnum implements CodeEnum {

        ERROR_CODE_10000("10000", "参数错误"),
        ERROR_CODE_10001("10001", "不支持的请求方式"),
        ERROR_CODE_10002("10002", "参数格式异常"),

        ;
        private final String code;

        private final String msg;


    }


    @Getter
    @AllArgsConstructor
    enum RpcErrorEnum implements CodeEnum {

        /**
         * 系统_失败分类（请求0、返回1）_业务_方法_调用方CODE码（代码补齐）
         */
        ERROR_CODE_USER_0_30001_0001("USER_0_30001_0001", "RPC异常-USER-用户信息-查询单个用户信息-接口调用失败"),
        ERROR_CODE_USER_1_30001_0001("USER_1_30001_0001", "RPC异常-USER-用户信息-查询单个用户信息-接口返回失败"),
        ERROR_CODE_USER_1_30001_0002("USER_1_30001_0002", "RPC异常-USER-用户信息-分页查询用户信息-接口返回失败"),
        ;
        private final String code;
        private final String msg;
    }


    @Getter
    @AllArgsConstructor
    enum PlatformErrorEnum implements CodeEnum {

        ERROR_CODE_40000("40000", "运行时失败"),
        ERROR_CODE_40001("40001", "路由消息处理失败"),

        ;
        private final String code;

        private final String msg;

    }



}