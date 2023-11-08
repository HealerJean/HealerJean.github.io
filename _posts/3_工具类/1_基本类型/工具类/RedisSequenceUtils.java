package com.healerjean.proj.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * RedisSequenceUtils
 *
 * @author zhangyujin
 * @date 2023-07-28 10:07:20
 */
@Component
public class RedisSequenceUtils {

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * SEQ_FORMAT
     */
    private final String SEQ_FORMAT = "00000";

    /**
     * 设置前缀产生时间序列 ，每秒99999比最多:
     */
    public String generateSeqNo(String prefixKey) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String minuteStr = df.format(LocalDateTime.now());
        StringBuilder sb = new StringBuilder();
        sb.append(prefixKey).append(minuteStr);
        String temp = sb.toString();
        Long number = this.redisTemplate.opsForValue().increment(temp, 1L);
        this.redisTemplate.expire(temp, 2L, TimeUnit.SECONDS);
        DecimalFormat decimalFormat = new DecimalFormat(SEQ_FORMAT);
        return sb.append(decimalFormat.format(number)).toString();
    }

    /**
     * 无前缀产生时间序列 ，每秒99999比最多:
     */
    public String generateSeqNo() {
        return generateSeqNo("");
    }
}
