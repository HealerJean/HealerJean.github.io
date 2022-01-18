package com.hlj.proj.business.context;

import java.util.Map;
import java.util.Stack;

/**
 * @author zhangyujin
 * @date 2022/1/17  9:40 下午.
 * @description
 */
public class LogRecordContext {

    private static final InheritableThreadLocal<Stack<Map<String, Object>>> variableMapStack = new InheritableThreadLocal<>();
}