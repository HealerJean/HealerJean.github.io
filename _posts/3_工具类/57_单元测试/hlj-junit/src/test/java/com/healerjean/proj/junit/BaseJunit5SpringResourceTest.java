package com.healerjean.proj.junit;

import com.healerjean.proj.BaseJunit5SpringTest;
import com.healerjean.proj.service.rpc.DemoPrcResource;
import com.healerjean.proj.service.rpc.proxy.impl.DemoRpcProxyImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
public class BaseJunit5SpringResourceTest extends BaseJunit5SpringTest {

    /**
     * demoRpcProxy
     */
    @Resource
    private DemoRpcProxyImpl demoRpcProxy;

    /**
     * JunitInit 中已经初始化了一个 @MockBean了，所有可以用@Resource，如果没有初始化，则只能用  @Resource
     */
    @Resource
    private DemoPrcResource demoPrcResource;


    @DisplayName("BaseJunit5SpringTestImpl.test")
    @Test
    public void test1() {
        String result = demoRpcProxy.rpcInvoke("success");
        log.info("demoRpcProxy#result:{}", result);

         result = demoRpcProxy.rpcInvoke("success");
        log.info("demoRpcProxy#result:{}", result);

        result = demoPrcResource.rpcInvoke("success");
        log.info("demoPrcResource#result:{}", result);

        result = demoPrcResource.rpcInvoke("success");
        log.info("demoPrcResource#result:{}", result);
    }


    @DisplayName("BaseJunit5MockitoTestImpl.test2")
    @Test
    public void test2() {
        when(demoPrcResource.rpcInvoke(anyString())).thenReturn("test2MockMethod");
        String result = demoRpcProxy.rpcInvoke("success");
        log.info("result:{}", result);
    }


}

