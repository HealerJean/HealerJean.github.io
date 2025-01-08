package com.healerjean.proj.utils.filter.bloom;

import com.healerjean.proj.utils.SpringUtils;
import com.healerjean.proj.utils.filter.FilterConfiguration;
import com.healerjean.proj.utils.filter.FilterEnum;
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
     */
    public static <T> RBloomFilter<T> getBloomFilter(FilterEnum.BloomEnum bloomEnum) {
        FilterConfiguration filterConfiguration = SpringUtils.getBean(FilterConfiguration.class);
        FilterConfiguration.BloomFilterConfig bloomFilterConfig = filterConfiguration.getBloomFilterConfig(bloomEnum.getCode());
        RedissonClient redissonClient = SpringUtils.getBean(RedissonClient.class);
        RBloomFilter<T> bloomFilter = redissonClient.getBloomFilter(bloomFilterConfig.getKey());
        bloomFilter.tryInit(bloomFilterConfig.getExpectedInsertions(), bloomFilterConfig.getFalseProbability());
        return bloomFilter;
    }


    public static boolean setBloomBit(FilterEnum.BloomEnum bloomEnum, String bitBalue) {
        RBloomFilter<Object> bloomFilter = getBloomFilter(bloomEnum);
        return bloomFilter.add(bitBalue);
    }


    public static boolean getBloomBit(FilterEnum.BloomEnum bloomEnum, String bitBalue) {
        RBloomFilter<Object> bloomFilter = getBloomFilter(bloomEnum);
        // log.info("序号：{}", bloomFilter.getName());
        // log.info("元素个数：{}", bloomFilter.count());
        // log.info("期望插入数：{}", bloomFilter.getExpectedInsertions());
        // log.info("假阳性概率：{}", bloomFilter.getFalseProbability());
        return bloomFilter.contains(bitBalue);
    }


}
