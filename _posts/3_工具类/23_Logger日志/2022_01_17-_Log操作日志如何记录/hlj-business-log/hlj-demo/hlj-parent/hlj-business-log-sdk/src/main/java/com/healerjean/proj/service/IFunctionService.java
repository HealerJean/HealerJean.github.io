package com.healerjean.proj.service;

public interface IFunctionService {

    String apply(String functionName, String value);

    boolean beforeFunction(String functionName);
}
