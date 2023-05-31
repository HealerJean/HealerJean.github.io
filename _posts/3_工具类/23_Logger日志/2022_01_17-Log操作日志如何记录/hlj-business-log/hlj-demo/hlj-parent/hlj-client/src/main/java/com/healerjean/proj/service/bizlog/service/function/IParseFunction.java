package com.healerjean.proj.service.bizlog.service.function;


/**
 * 解析函数
 */
public interface IParseFunction {

    /**
     * 校验是否执行
     *
     * @return boolean
     */
    default boolean check() {
        return false;
    }

    /**
     * 函数名
     *
     * @return String
     */
    String functionName();

    /**
     * 函数调用
     *
     * @param applyReq applyReq
     * @return String
     */
    String apply(String applyReq);
}
