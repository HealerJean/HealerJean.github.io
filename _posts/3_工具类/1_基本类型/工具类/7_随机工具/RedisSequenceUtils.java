package com.hlj.util.Z025_utils;

import com.fintech.credit.basic.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Component
public class RedisSequenceUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    private final String SEQNO_FORMAT = "00000";

    /**
     * @Description: 设置前缀产生时间序列 ，每秒99999比最多
     * @Author tongdong
     * @Date  2019/10/29
     */
    public String generateSeqNo(String prefixKey) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String minuteStr = df.format(LocalDateTime.now());
        StringBuffer sb = new StringBuffer();
        sb.append(prefixKey).append(minuteStr);
        String temp = sb.toString();
        Long number = this.redisTemplate.opsForValue().increment(temp, 1L);
        this.redisTemplate.expire(temp, 2L, TimeUnit.SECONDS);
        DecimalFormat decimalFormat = new DecimalFormat(SEQNO_FORMAT);
        return sb.append(decimalFormat.format(number)).toString();
    }

    /**
     * @Description: 无前缀产生时间序列 ，每秒99999比最多
     * @Author tongdong
     * @Date  2019/10/29
     */
    public String generateSeqNo() {
        return generateSeqNo("");
    }
}
