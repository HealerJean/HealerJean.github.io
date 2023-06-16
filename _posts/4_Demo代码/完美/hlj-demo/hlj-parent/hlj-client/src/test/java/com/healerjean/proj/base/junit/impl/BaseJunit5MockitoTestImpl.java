package com.healerjean.proj.base.junit.impl;

import com.healerjean.proj.base.junit.BaseJunit5MockitoTest;
import com.healerjean.proj.rpc.DemoPrcResource;
import com.healerjean.proj.rpc.proxy.impl.DemoRpcProxyImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * BaseJunit5MockitoTestImpl
 *
 * @author zhangyujin
 * @date 2023/6/15  21:48.
 */
@Slf4j
public class BaseJunit5MockitoTestImpl extends BaseJunit5MockitoTest {

    /**
     * demoRpcProxy
     */
    @InjectMocks
    private DemoRpcProxyImpl demoRpcProxy;

    @Mock
    private DemoPrcResource demoPrcResource;


    @DisplayName("BaseJunit5MockitoTestImpl.test")
    @Test
    public void test2() {
        when(demoPrcResource.rpcInvoke(anyString())).thenReturn("testMockMethod");
        String result = demoRpcProxy.rpcInvoke("success");
        log.info("result:{}", result);
    }

}
