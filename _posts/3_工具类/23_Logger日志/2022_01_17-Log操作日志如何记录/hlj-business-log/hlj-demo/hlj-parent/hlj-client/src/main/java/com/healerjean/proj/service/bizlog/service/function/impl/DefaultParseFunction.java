package com.healerjean.proj.service.bizlog.service.function.impl;


import com.healerjean.proj.service.bizlog.common.enums.BizLogEnum;
import com.healerjean.proj.service.bizlog.service.function.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * DefaultParseFunction
 */
@Slf4j
@Service
public class DefaultParseFunction implements IParseFunction {

    /**
     * executeBefore
     *
     * @return boolean
     */
    @Override
    public boolean check() {
        return true;
    }

    /**
     * 函数名
     *
     * @return String
     */
    @Override
    public String functionName() {
        return BizLogEnum.IParseFunctionEnum.DEFAULT.getFunction();
    }

    /**
     * 函数调用
     *
     * @param applyReq applyReq
     * @return String
     */
    @Override
    public String apply(String applyReq) {
        log.info("[DefaultParseFunction#apply] applyReq:{}", applyReq);
        return null;
    }
}
