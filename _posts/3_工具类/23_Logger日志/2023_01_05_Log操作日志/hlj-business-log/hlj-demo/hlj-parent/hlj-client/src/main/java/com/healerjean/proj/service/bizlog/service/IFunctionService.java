package com.healerjean.proj.service.bizlog.service;

/**
 * 函数service
 */
public interface IFunctionService {

    /**
     * apply
     * @param functionName functionName
     * @param value value
     * @return String
     */
    String apply(String functionName, String value);

    /**
     * beforeFunction
     * @param functionName functionName
     * @return boolean
     */
    boolean check(String functionName);
}
