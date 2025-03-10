package com.healerjean.proj.base.impl;

import com.healerjean.proj.base.BaseJunit5SpringTest;
import com.healerjean.proj.rpc.DemoPrcResource;
import com.healerjean.proj.rpc.proxy.impl.DemoRpcProxyImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * BaseJunit5SpringTest
 *
 * @author zhangyujin
 * @date 2023/6/15  21:53.
 */
@Slf4j
public class BaseJunit5SpringTestImpl extends BaseJunit5SpringTest {

    /**
     * demoRpcProxy
     */
    @Resource
    private DemoRpcProxyImpl demoRpcProxy;

    @MockBean
    private DemoPrcResource demoPrcResource;


    @DisplayName("BaseJunit5SpringTestImpl.test")
    @Test
    public void test1() {
        String result = demoRpcProxy.rpcInvoke("success");
        log.info("result:{}", result);
    }


    @DisplayName("BaseJunit5MockitoTestImpl.test2")
    @Test
    public void test2() {
        when(demoPrcResource.rpcInvoke(anyString())).thenReturn("test2MockMethod");
        String result = demoRpcProxy.rpcInvoke("success");
        log.info("result:{}", result);
    }


}

