package com.healerjean.proj.cache.avengers.H02_限流;

import com.healerjean.proj.cache.avengers.H02_限流.滑动窗口限流.SimpleRateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * @author zhangyujin
 * @date 2021/4/30  11:57 上午.
 * @description
 */
@Slf4j
public class TestMain {


    @Test
    public void test() {
        Jedis jedis = new Jedis("127.0.0.1");
        SimpleRateLimiter limiter = new SimpleRateLimiter(jedis);
        for (int i = 0; i < 20; i++) {
            log.info("限流状态：[{}]", limiter.isActionAllowed("QYD", "reply", 60, 5));
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
        System.out.println("1110011101011000101000010011000100110100001010101011".length());
    }




}
