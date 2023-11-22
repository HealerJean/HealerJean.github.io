package com.healerjean.proj.rpc.consumer.adapter.impl;

import com.healerjean.proj.rpc.consumer.adapter.DemoRpcAdapter;
import com.healerjean.proj.rpc.consumer.proxy.DemoRpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * DemoRpcAdapterImpl
 *
 * @author zhangyujin
 * @date 2023/7/14
 */
@Slf4j
@Service
public class DemoRpcAdapterImpl implements DemoRpcAdapter {

    @Resource
    private DemoRpcProxy demoRpcProxy;


    /**
     * Rpc调用
     *
     * @param msg msg
     * @return String String
     */
    String rpcInvoke(String msg) {
        try {
            return demoRpcProxy.rpcInvoke(msg);
        } catch (Exception e) {
            log.info("[DemoAdapter#rpcInvoke] msg", msg, e);
            return null;
        }
    }
}
