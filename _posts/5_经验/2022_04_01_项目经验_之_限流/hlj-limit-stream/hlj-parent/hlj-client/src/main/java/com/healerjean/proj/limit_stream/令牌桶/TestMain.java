package com.healerjean.proj.limit_stream.令牌桶;

/**
 * @author zhangyujin
 * @date 2022/4/15  18:46.
 * @description
 */

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestMain {


    /**
     * 1、简单测试
     * acq 1时并没有任何等待直接预消费了1个令牌；
     * acq 6时，由于之前预消费了1个令牌，故而等待了2秒，之后又预消费了6个令牌；
     * acq 2时同理，由于之前预消费了6个令牌，故而等待了12秒；
     * RateLimiter通过限制后面请求的等待时间，来支持一定程度的突发请求(预消费)。
     */
    @Test
    public void test_1(){
        // 1、创建一个RateLimiter，指定每秒放0.5个令牌（2秒放1个令牌
        RateLimiter rateLimiter = RateLimiter.create(0.5);

        int[] a = {1, 6, 2};
        for (int i = 0 ; i < a.length; i++){
            log.info("时间戳：{}, 获取 {} 个令牌需要 {}s", System.currentTimeMillis(), a[i], rateLimiter.acquire(a[i]));
        }

        // 1516166482561 acq 1: wait 0.0s
        // 1516166482563 acq 6: wait 1.997664s
        // 1516166484569 acq 2: wait 11.991958s
    }


    /**
     * 2、预热模式
     */
    @Test
    public void test_2() throws InterruptedException {
        //预热模式,设置预热时间和QPS，即在正式acquire前，限流器已经持有 5 * 4 = 20个令牌
        RateLimiter rateLimiter = RateLimiter.create(5, 4000, TimeUnit.MILLISECONDS);
        for (int i = 1; i < 50; i++) {
            System.out.println(System.currentTimeMillis() + " acq " + i + ": wait " + rateLimiter.acquire() + "s");
            if (i == 15) {
                Thread.sleep(2000);
                System.out.println(System.currentTimeMillis() + " acq " + 15 + ": wait " + rateLimiter.acquire() + "s");
            }
        }
    }


}
