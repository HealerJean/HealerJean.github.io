package com.healerjean.proj.service.rpc.proxy.impl;

import com.healerjean.proj.service.rpc.DemoPrcResource;
import com.healerjean.proj.service.rpc.proxy.DemoRpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * DemoRpcProxy
 *
 * @author zhangyujin
 * @date 2023/6/15  21:23.
 */
@Slf4j
@Service("demoRpcProxy")
public class DemoRpcProxyImpl implements DemoRpcProxy {


    @Resource
    private DemoPrcResource demoPrcResource;

    /**
     * Rpc调用
     *
     * @param reqString reqString
     * @return String
     */
    @Override
    public String rpcInvoke(String reqString) {
        return demoPrcResource.rpcInvoke(reqString);
    }
}
