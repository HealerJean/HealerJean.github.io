package com.healerjean.proj.rpc.consumer.proxy;

/**
 * DemoConsumer
 *
 * @author zhangyujin
 * @date 2023/6/15  21:22.
 */
public interface DemoRpcProxy {

    /**
     * Rpc调用
     *
     * @param msg
     * @return String
     */
    String rpcInvoke(String msg);
}

