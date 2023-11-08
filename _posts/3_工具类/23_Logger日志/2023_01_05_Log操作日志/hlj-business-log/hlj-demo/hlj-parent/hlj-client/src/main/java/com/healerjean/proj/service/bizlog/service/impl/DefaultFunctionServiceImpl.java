package com.healerjean.proj.service.bizlog.service.impl;


import com.healerjean.proj.service.bizlog.service.IFunctionService;
import com.healerjean.proj.service.bizlog.service.function.IParseFunction;
import com.healerjean.proj.service.bizlog.service.function.factory.ParseFunctionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 默认函数
 * @author zhangyujin
 * @date 2023/5/30  19:56
 */
@Service
public class DefaultFunctionServiceImpl implements IFunctionService {

    /**
     * 函数解析工厂
     */
    @Resource
    private  ParseFunctionFactory parseFunctionFactory;


    /**
     * 函数执行
     * @param functionName functionName
     * @param value value
     * @return String
     */
    @Override
    public String apply(String functionName, String value) {
        IParseFunction function = parseFunctionFactory.getFunction(functionName);
        if (function == null) {
            return value;
        }
        return function.apply(value);
    }

    /**
     * beforeFunction
     * @param functionName functionName
     * @return boolean
     */
    @Override
    public boolean check(String functionName) {
        return parseFunctionFactory.isBeforeFunction(functionName);
    }
}
