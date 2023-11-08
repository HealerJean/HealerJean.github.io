package com.healerjean.proj.service.bizlog.service.function.factory;

import com.healerjean.proj.service.bizlog.service.function.IParseFunction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 解析函数工厂
 */
@Service
public class ParseFunctionFactory {

    /**
     * 函数map
     */
    private Map<String, IParseFunction> allFunctionMap;


    /**
     * ParseFunctionFactory
     *
     * @param parseFunctions parseFunctions
     */
    public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
        if (CollectionUtils.isEmpty(parseFunctions)) {
            return;
        }
        allFunctionMap = new HashMap<>();
        for (IParseFunction parseFunction : parseFunctions) {
            if (StringUtils.isEmpty(parseFunction.functionName())) {
                continue;
            }
            allFunctionMap.put(parseFunction.functionName(), parseFunction);
        }
    }

    /**
     * @param functionName functionName
     * @return IParseFunction
     */
    public IParseFunction getFunction(String functionName) {
        return allFunctionMap.get(functionName);
    }

    /**
     * isBeforeFunction
     *
     * @param functionName functionName
     * @return boolean
     */
    public boolean isBeforeFunction(String functionName) {
        return allFunctionMap.get(functionName) != null && allFunctionMap.get(functionName).check();
    }
}
