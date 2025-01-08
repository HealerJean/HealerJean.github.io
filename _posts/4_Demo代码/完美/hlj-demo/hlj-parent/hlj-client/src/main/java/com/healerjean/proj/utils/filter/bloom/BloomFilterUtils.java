package com.healerjean.proj.utils.filter.bloom;

import com.healerjean.proj.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;

/**
 * BloomFilterUtils
 *
 * @author zhangyujin
 * @date 2025/1/8
 */
@Slf4j
public class BloomFilterUtils {


    /**
     * 创建布隆过滤器
     * @param filterName 过滤器名称
     * @param expectedInsertions 预测插入数量
     * @param falseProbability 误判率
     * @param <T>
     * @return
     */
    public static  <T> RBloomFilter<T> create(String filterName, long expectedInsertions, double falseProbability) {
        RedissonClient redissonClient = SpringUtils.getBean(RedissonClient.class);
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(filterName);
        bloomFilter.tryInit(expectedInsertions, falseProbability);
        return bloomFilter;
    }
}
