package com.healerjean.proj.utils.filter.compressed;

import com.google.common.collect.Lists;
import com.healerjean.proj.service.RedisService;
import com.healerjean.proj.utils.SpringUtils;
import com.healerjean.proj.utils.filter.FilterConfiguration;
import com.healerjean.proj.utils.filter.FilterEnum;
import lombok.extern.slf4j.Slf4j;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;

/**
 * CompressedBitUtils
 *
 * @author zhangyujin
 * @date 2024/9/14
 */
@Slf4j
public class CompressedBitUtils {

    /**
     * 设置压缩位图在offset上的值,并且设置过期时间(秒)
     */
    public static CompressedBitInfo setCompressedBit(FilterEnum.CompressedEnum compressedInfoEnum, long offset) {
        return setCompressedBit(compressedInfoEnum, offset, true);
    }


    /**
     * 删除压缩位图在offset上的值(相当于设置为false)
     */
    public static CompressedBitInfo remCompressedBit(FilterEnum.CompressedEnum compressedInfoEnum, long offset) {
        return setCompressedBit(compressedInfoEnum, offset, false);
    }


    /**
     * 设置压缩位图在offset上的值,并且设置过期时间(秒)
     */
    public static CompressedBitInfo setCompressedBit(FilterEnum.CompressedEnum compressedInfoEnum, long offset, boolean value) {
        FilterConfiguration filterConfiguration = SpringUtils.getBean(FilterConfiguration.class);
        FilterConfiguration.CompressedConfig compressedConfig = filterConfiguration.getCompressedConfig(compressedInfoEnum.getCode());

        CompressedBitInfo bitInfo = getBitInfo(compressedInfoEnum, offset);
        String bitKey = bitInfo.getBitKey();
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setBit(bitKey, bitInfo.getBucketOffset(), value);
        redisService.expire(bitKey, compressedConfig.getExpireSeconds());

        long sourceOffset = getSourceOffset(compressedInfoEnum, bitInfo.getBucketIndex(), bitInfo.getBucketOffset());
        bitInfo.setSourceOffset(sourceOffset);
        bitInfo.setBitValue(value);
        return bitInfo;
    }


    /**
     * 获取压缩位图在offset上的值
     */
    public static CompressedBitInfo getCompressedBit(FilterEnum.CompressedEnum compressedInfoEnum, long offset) {
        CompressedBitInfo bitInfo = getBitInfo(compressedInfoEnum, offset);
        String bitKey = bitInfo.getBitKey();
        log.debug("getCompressedBit with key:{}, offset:{}", bitKey, bitInfo.getBucketOffset());
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        Boolean bitValue = redisService.getBit(bitKey, bitInfo.getBucketOffset());
        long sourceOffset = getSourceOffset(compressedInfoEnum, bitInfo.getBucketIndex(), bitInfo.getBucketOffset());
        bitInfo.setSourceOffset(sourceOffset);
        bitInfo.setBitValue(bitValue);
        return bitInfo;
    }

    /**
     * 获取压缩位图每个小桶的子key集合
     */
    public static List<String> getAllBucketKeys(FilterEnum.CompressedEnum compressedInfoEnum, long maxOffset) {
        List<String> result = Lists.newArrayList();
        FilterConfiguration filterConfiguration = SpringUtils.getBean(FilterConfiguration.class);
        FilterConfiguration.CompressedConfig compressedConfig = filterConfiguration.getCompressedConfig(compressedInfoEnum.getCode());

        CompressedBitInfo bitInfo = getBitInfo(compressedInfoEnum, maxOffset);
        for (int i = 0; i <= bitInfo.getBucketIndex(); i++) {
            String bitKey = getBitKey(compressedConfig.getKey(), i);
            result.add(bitKey);
        }
        return result;
    }

    /**
     * 删除所有桶里的的Bitmap
     */
    public static void deleteAllCompressedBit(FilterEnum.CompressedEnum compressedInfoEnum, long maxOffset) {
        FilterConfiguration filterConfiguration = SpringUtils.getBean(FilterConfiguration.class);
        FilterConfiguration.CompressedConfig compressedConfig = filterConfiguration.getCompressedConfig(compressedInfoEnum.getCode());
        CompressedBitInfo bitInfo = getBitInfo(compressedInfoEnum, maxOffset);
        for (int i = 0; i < bitInfo.getBucketIndex(); i++) {
            String bitKey = getBitKey(compressedConfig.getKey(), i);
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            redisService.expire(bitKey, 0);
        }
    }


    /**
     * 将java中的bitmap转换为redis的字节数组
     */
    private static byte[] getByteArray(List<Long> bits) {
        Iterator<Long> iterator = bits.iterator();
        BitSet bitSet = new BitSet();
        while (iterator.hasNext()) {
            long offset = iterator.next();
            bitSet.set((int) offset);
        }
        byte[] targetBitmap = bitSet.toByteArray();
        convertJavaToRedisBitmap(targetBitmap);
        return targetBitmap;
    }

    /**
     * 将java中的字节数组转换为redis的bitmap数据形式
     *
     * @param bytes
     */
    private static void convertJavaToRedisBitmap(byte[] bytes) {
        int len = bytes.length;
        for (int i = 0; i < len; i++) {
            byte b1 = bytes[i];
            if (b1 == 0) {
                continue;
            }
            byte transByte = 0;
            for (byte j = 0; j < 8; j++) {
                transByte |= (b1 & (1 << j)) >> j << (7 - j);
            }
            bytes[i] = transByte;
        }
    }


    /**
     * getBitInfo
     */
    public static CompressedBitInfo getBitInfo(FilterEnum.CompressedEnum compressedInfoEnum, long sourceOffset) {
        FilterConfiguration filterConfiguration = SpringUtils.getBean(FilterConfiguration.class);
        FilterConfiguration.CompressedConfig compressedConfig = filterConfiguration.getCompressedConfig(compressedInfoEnum.getCode());

        CompressedBitInfo bucketInfo = new CompressedBitInfo();
        bucketInfo.setSourceOffset(sourceOffset);

        long bucketSize = compressedConfig.getBucketSize();
        long bucketIndex = sourceOffset / bucketSize;
        bucketInfo.setBucketIndex(bucketIndex);

        long bucketOffset = sourceOffset % bucketSize;
        bucketInfo.setBucketOffset(bucketOffset);

        String bitKey = getBitKey(compressedConfig.getKey(), bucketIndex);
        bucketInfo.setBitKey(bitKey);
        return bucketInfo;
    }


    public static String getBitKey(String key, long bucketIndex) {
        return new StringBuffer(key).append("_").append(bucketIndex).toString();
    }

    /**
     * getSourceOffset
     *
     * @param bucketIndex  bucketIndex
     * @param bucketIndex  bucketIndex
     * @param bucketOffset bucketOffset
     * @return {@link long}
     */
    public static long getSourceOffset(FilterEnum.CompressedEnum compressedInfoEnum, long bucketIndex, long bucketOffset) {
        FilterConfiguration filterConfiguration = SpringUtils.getBean(FilterConfiguration.class);
        FilterConfiguration.CompressedConfig compressedConfig = filterConfiguration.getCompressedConfig(compressedInfoEnum.getCode());
        return bucketIndex * compressedConfig.getBucketSize() + bucketOffset;
    }

}
