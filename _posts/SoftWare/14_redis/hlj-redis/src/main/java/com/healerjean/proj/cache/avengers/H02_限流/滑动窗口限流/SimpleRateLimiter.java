package com.healerjean.proj.cache.avengers.H02_限流.滑动窗口限流;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.io.IOException;

/**
 * @author zhangyujin
 * @date 2021/5/7  2:23 下午.
 * @description
 */
@Slf4j
public class SimpleRateLimiter {

    private Jedis jedis;

    public SimpleRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     *
     * @param userId 用户Id
     * @param actionKey 动作
     * @param period 过期时间 单位(秒)
     * @param maxCount 最大次数
     * @return 返回是否允许访问
     * 1、获取key = userId + "#" + actionKey;
     * 2、
     */
    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount) {
        // 1、获取key = userId + "#" + actionKey;
        String key = userId + "#" + actionKey;
        long now = System.currentTimeMillis();
        Pipeline pipe = jedis.pipelined();
        //开启事务
        pipe.multi();
        //第二个参数是score，第三个参数是value
        pipe.zadd(key, now, "" + now);
        //画图帮助理解
        pipe.zremrangeByScore(key, 0, now - period * 1000);
        //当前窗口的元素个数
        Response<Long> count = pipe.zcard(key);
        //一定要设置过期时间，可能大部分用户是冷用户，因为要维护period时间内的记录，所以key过期时间要稍微比period大
        pipe.expire(key, period + 1);
        //执行事务
        pipe.exec();
        try {
            pipe.close();
        } catch (IOException e) {
            log.info("SimpleRateLimiter#isActionAllowed pipe.close() error", e);
        }
        return count.get() <= maxCount;
    }

}
