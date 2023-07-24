package com.healerjean.proj.base;

import com.healerjean.proj.mock.DemoPrcResourceMock;
import com.healerjean.proj.rpc.provider.DemoPrcResource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * 前置mock
 *
 * @author zhanghanlin6
 * @date 2022/8/24
 */
@Component
@Slf4j
public class JunitInit implements BeanPostProcessor {

    /**
     * 1、使用@Resource 会有问题，不让 when
     * 2、其他地方在使用的时候，用@Resource，不可以使用@MockBean了，因为会导致重复
     */
    @MockBean
    private DemoPrcResource demoPrcResource;




    @Override
    public Object postProcessBeforeInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
        if (StringUtils.isBlank(beanName)) {
            return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
        }
        switch (beanName) {
            case "demoRpcProxy":
                // when(demoPrcResource.rpcInvoke(anyString())).thenAnswer(DemoPrcResourceMock.rpcInvoke());
                when(demoPrcResource.rpcInvoke(any())).thenReturn(DemoPrcResourceMock.rpcInvokeReturn());
                break;
            default:
                break;
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
