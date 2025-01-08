package com.healerjean.proj.utils.filter.bloom;

import com.healerjean.proj.utils.SpringUtils;
import com.healerjean.proj.utils.filter.FilterConfiguration;
import com.healerjean.proj.utils.filter.FilterEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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


    public static boolean setBloomBit(FilterEnum.BloomEnum bloomEnum, String appendKey, String bitValue) {
        RBloomFilter<Object> bloomFilter = getBloomFilter(bloomEnum, appendKey);
        return bloomFilter.add(bitValue);
    }

    public static boolean setBloomBit(FilterEnum.BloomEnum bloomEnum, String bitValue) {
        return setBloomBit(bloomEnum, "", bitValue);
    }


    public static boolean getBloomBit(FilterEnum.BloomEnum bloomEnum , String bitValue) {
        return getBloomBit(bloomEnum, "", bitValue);
    }


    public static boolean getBloomBit(FilterEnum.BloomEnum bloomEnum, String appendKey, String bitBalue) {
        RBloomFilter<Object> bloomFilter = getBloomFilter(bloomEnum, appendKey);
        return bloomFilter.contains(bitBalue);
    }


    /**
     * 创建布隆过滤器
     */
    public static <T> RBloomFilter<T> getBloomFilter(FilterEnum.BloomEnum bloomEnum, String appendKey) {
        FilterConfiguration filterConfiguration = SpringUtils.getBean(FilterConfiguration.class);
        FilterConfiguration.BloomFilterConfig bloomFilterConfig = filterConfiguration.getBloomFilterConfig(bloomEnum.getCode());
        RedissonClient redissonClient = SpringUtils.getBean(RedissonClient.class);
        RBloomFilter<T> bloomFilter;
        if (StringUtils.isNotBlank(appendKey)){
            bloomFilter = redissonClient.getBloomFilter(bloomFilterConfig.getKey() + "_" + appendKey);
        }else {
            bloomFilter = redissonClient.getBloomFilter(bloomFilterConfig.getKey());
        }
        bloomFilter.tryInit(bloomFilterConfig.getExpectedInsertions(), bloomFilterConfig.getFalseProbability());

        // log.info("序号：{}", bloomFilter.getName());
        // log.info("元素个数：{}", bloomFilter.count());
        // log.info("期望插入数：{}", bloomFilter.getExpectedInsertions());
        // log.info("假阳性概率：{}", bloomFilter.getFalseProbability());
        return bloomFilter;
    }


}
