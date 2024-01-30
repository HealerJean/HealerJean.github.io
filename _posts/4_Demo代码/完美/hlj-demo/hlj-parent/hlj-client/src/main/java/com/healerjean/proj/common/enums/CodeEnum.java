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

        ERROR_CODE_SUCCESS("000000", "成功"),
        ERROR_CODE_FAIL("999999", "系统太火爆了，请稍后重试!"),
        ;
        private final String code;
        private final String msg;
    }



    @Getter
    @AllArgsConstructor
    enum ErrorTypeEnum implements CodeEnum {

        /**
         * 校验错误
         */
        ERROR_TYPE_100("100", "参数异常-通用"),
        ERROR_TYPE_101("101", "请求方式不合法"),
        ERROR_TYPE_102("102", "参数格式不合法"),

        /**
         * 运行时错误
         */
        ERROR_TYPE_200("200", "运行时错误"),
        ERROR_TYPE_201("201", "内存错误"),
        ERROR_TYPE_202("202", "redis错误"),
        ERROR_TYPE_203("203", "mysql错误"),
        ERROR_TYPE_204("204", "mq错误"),
        ERROR_TYPE_205("205", "rcp错误"),
        ERROR_TYPE_206("206", "运行时错误"),

        /**
         * 业务异常
         */
        ERROR_TYPE_300("300", "业务处理失败"),
        ERROR_TYPE_301("301", "订单创建"),
        ;
        private final String code;
        private final String msg;
    }


    @Getter
    @AllArgsConstructor
    enum ParamsErrorEnum implements CodeEnum {

        ERROR_CODE_100000("100000", ErrorTypeEnum.ERROR_TYPE_100, "参数不合法"),
        ERROR_CODE_101001("101001", ErrorTypeEnum.ERROR_TYPE_101, "请求方式不合法"),
        ERROR_CODE_102000("102000", ErrorTypeEnum.ERROR_TYPE_102, "参数格式不合法"),
        ERROR_CODE_102001("102001", ErrorTypeEnum.ERROR_TYPE_102, "必须是字符串"),
        ;
        private final String code;

        private final ErrorTypeEnum typeEnum;

        private final String msg;

    }


    @Getter
    @AllArgsConstructor
    enum PlatformErrorEnum implements CodeEnum {
        ERROR_CODE_200000("200000", ErrorTypeEnum.ERROR_TYPE_200, "运行时错误"),
        ERROR_CODE_201001("201000", ErrorTypeEnum.ERROR_TYPE_200, "内存错误"),
        ERROR_CODE_205000("205000", ErrorTypeEnum.ERROR_TYPE_205, "RPC错误"),
        ;
        private final String code;

        private final ErrorTypeEnum typeEnum;

        private final String msg;
    }


    @Getter
    @AllArgsConstructor
    enum BusinessErrorEnum implements CodeEnum {

        ERROR_CODE_300000("300000", ErrorTypeEnum.ERROR_TYPE_300, "业务处理失败", "系统太火爆了，请稍后重试!"),
        ERROR_CODE_301000("301000", ErrorTypeEnum.ERROR_TYPE_301, "订单创建失败", ""),
        ERROR_CODE_301001("301001", ErrorTypeEnum.ERROR_TYPE_301, "订单创建失败", "您有一个订单正在创建，请稍后查看"),

        ;
        private final String code;

        private final ErrorTypeEnum typeEnum;

        private final String msg;

        private final String showMsg;
    }



}