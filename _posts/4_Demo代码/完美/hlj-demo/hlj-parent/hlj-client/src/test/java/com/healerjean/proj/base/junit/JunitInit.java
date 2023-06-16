package com.healerjean.proj.base.junit;

import com.healerjean.proj.mock.DemoPrcResourceMock;
import com.healerjean.proj.rpc.DemoPrcResource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * 前置mock
 *
 * @author zhanghanlin6
 * @date 2022/8/24
 */
// @Component
@Slf4j
public class JunitInit implements BeanPostProcessor {

    /**
     * demoPrcResource
     */
    @Resource
    private DemoPrcResource demoPrcResource;


    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (StringUtils.isBlank(beanName)) {
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        }
        switch (beanName) {
            case "demoRpcProxy":
                when(demoPrcResource.rpcInvoke(anyString())).thenAnswer(DemoPrcResourceMock.rpcInvoke());
                // when(demoPrcResource.rpcInvoke(anyString())).thenReturn(DemoPrcResourceMock.rpcInvokeReturn());
                break;
            default:
                break;
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
