package com.healerjean.proj.service.system;

import com.healerjean.proj.service.system.cache.CacheService;
import com.healerjean.proj.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.healerjean.proj.constant.CommonConstants.REDIS_HLJ;
import static com.healerjean.proj.constant.CommonConstants.REDIS_LOCK;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName CacheServiceImpl
 * @Date 2019/10/18  14:55.
 * @Description 缓存服务
 */
@Service
public class CacheServiceImpl implements CacheService {

    private static final String SEQNO_FORMAT = "0000";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String generateSeqNo(String prefixKey) {
        String minuteStr = DateUtils.toDateString(new Date(), DateUtils.YYYYMMDDHHMMSS);
        StringBuffer sb = new StringBuffer();
        sb.append(prefixKey).append(minuteStr);
        String temp = sb.toString();
        Long number = this.redisTemplate.opsForValue().increment(temp, 1L);
        this.redisTemplate.expire(temp, 2L, TimeUnit.SECONDS);
        DecimalFormat decimalFormat = new DecimalFormat(SEQNO_FORMAT);
        return sb.append(decimalFormat.format(number)).toString();
    }

    @Override
    public String generateSeqNo() {
        String minuteStr = DateUtils.toDateString(new Date(), DateUtils.YYYYMMDDHHMMSS);
        StringBuffer sb = new StringBuffer().append(minuteStr);
        String temp = sb.toString();
        Long number = this.redisTemplate.opsForValue().increment(temp, 1L);
        this.redisTemplate.expire(temp, 2L, TimeUnit.SECONDS);
        DecimalFormat decimalFormat = new DecimalFormat(SEQNO_FORMAT);
        return sb.append(decimalFormat.format(number)).toString();
    }

    @Override
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    @Override
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public boolean lock(String key, long timeout, TimeUnit timeUnit) {
        try {
            Long lock = increment(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, 1);
            if (lock == 1) {
                expire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeout, timeUnit);
                return true;
            } else {
                Long expire = redisTemplate.getExpire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeUnit);
                if (expire != null && expire.equals(-1L)) {
                    expire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeout, timeUnit);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            delete(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key);
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        delete(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key);
    }
}
