package com.hlj.proj.business.function.impl;

import com.hlj.proj.business.function.IParseFunction;
import com.hlj.proj.business.function.factory.ParseFunctionFactory;

/**
 * @author zhangyujin
 * @date 2022/1/17  9:44 下午.
 * @description
 */
public class DefaultFunctionServiceImpl implements IFunctionService {

    private final ParseFunctionFactory parseFunctionFactory;

    public DefaultFunctionServiceImpl(ParseFunctionFactory parseFunctionFactory) {
        this.parseFunctionFactory = parseFunctionFactory;
    }

    @Override
    public String apply(String functionName, String value) {
        IParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (function == null) {
            return value;
        }
        return function.apply(value);
    }

    @Override
    public boolean beforeFunction(String functionName) {
        return parseFunctionFactory.isBeforeFunction(functionName);
    }
}