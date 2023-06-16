package com.healerjean.proj;

import com.healerjean.proj.mock.DemoPrcResourceMock;
import com.healerjean.proj.service.rpc.DemoPrcResource;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.stereotype.Component;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.anyString;
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
     * Spring项目中只能有一个 @MockBean
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
                when(demoPrcResource.rpcInvoke(anyString())).thenAnswer(DemoPrcResourceMock.rpcInvoke());
                break;
            default:
                break;
        }
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
