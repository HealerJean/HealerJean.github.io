package com.healerjean.proj.mock;

import org.mockito.stubbing.Answer;

/**
 * DemoPrcResourceMock
 * @author zhangyujin
 * @date 2023/6/15  21:39.
 */
public class DemoPrcResourceMock {

    /**
     * rpcInvoke
     * @return String
     */
    public static Answer<String> rpcInvoke() {
        return invocation ->{
            Object[] arguments = invocation.getArguments();
            return  "rpcInvoke";
        };
    }


    /**
     * rpcInvoke
     * @return String
     */
    public static Answer<String> rpcInvokeIronMan() {
        return invocation -> "rpcInvokeIronMan";
    }


    /**
     * rpcInvokeReturn
     * @return String
     */
    public static String rpcInvokeReturn() {
        return "rpcInvokeReturn";
    }
}
