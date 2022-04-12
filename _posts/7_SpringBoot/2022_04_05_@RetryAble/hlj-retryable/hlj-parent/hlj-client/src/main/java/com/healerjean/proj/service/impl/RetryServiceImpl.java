package com.healerjean.proj.service.impl;

import com.healerjean.proj.service.RetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * @author zhangyujin
 * @date 2022/4/12  15:09.
 * @description
 */
@Slf4j
@Service
public class RetryServiceImpl implements RetryService {

    public static int num = 0 ;

    /**
     * 1、什么样的异常会重试
     *  value：抛出指定异常才会重试
     *  include：和value一样，默认为空，当exclude也为空时，默认所有异常
     *  exclude：指定不处理的异常
     * 2、重试次数
     *  maxAttempts：最大重试次数，默认3次
     * 3、重试等待策略
     *  backoff：重试等待策略，默认使用@Backoff，@Backoff的value默认为1000L，我们设置为2000L；multiplier（指定延迟倍数）默认为0，表示固定暂停1秒后进行重试，如果把multiplier设置为1.5，则第一次重试为2秒，第二次为3秒，第三次为4.5秒。
     * 4、重试耗尽还是失败，怎么办
     * 当重试耗尽时，RetryOperations可以将控制传递给另一个回调，即RecoveryCallback。Spring-Retry还提供了@Recover注解，用于@Retryable重试失败后处理方法。如果不需要回调方法，可以直接不写回调方法，那么实现的效果是，重试次数完了后，如果还是没成功没符合业务判断，就抛出异常。
     *
     *
     */
    @Retryable(value = Exception.class,maxAttempts = 4,backoff = @Backoff(delay = 2000,multiplier = 1.5))
    @Override
    public boolean doRetry(String name) {
        log.info("[RetryService#doRetry] name:{}", name);
        num++;
        if (num == 5){
            return true;
        }
        try {
            int i = 1/0;
        }catch (Exception e){
            log.info("[RetryService#doRetry] error:{} ", e.getMessage());
            throw e;
        }
        return true;
    }

    @Recover
    public boolean doRetryRecover(Exception e, String name) {
        log.info("[RetryService#doRetryRecover] name:{}, e:{}", name, e.getMessage());
        return true;
    }



}
