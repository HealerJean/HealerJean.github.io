package com.healerjean.proj.utils.filter.compressed;

import com.google.common.collect.Lists;
import com.healerjean.proj.service.RedisService;
import com.healerjean.proj.utils.SpringUtils;
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
    public static CompressedBitInfo setCompressedBit(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long offset) {
        return setCompressedBit(compressedInfoEnum, offset, true);
    }



    /**
     * 删除压缩位图在offset上的值(相当于设置为false)
     */
    public static CompressedBitInfo remCompressedBit(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long offset) {
        return setCompressedBit(compressedInfoEnum, offset, false);
    }


    /**
     * 设置压缩位图在offset上的值,并且设置过期时间(秒)
     */
    public static CompressedBitInfo setCompressedBit(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long offset, boolean value) {
        CompressedFilterConfiguration compressedFilterConfiguration = SpringUtils.getBean(CompressedFilterConfiguration.class);
        CompressedBizBO compressedBiz = compressedFilterConfiguration.getCompressedBizMap().get(compressedInfoEnum.getCode());

        CompressedBitInfo bitInfo = getBitInfo(compressedInfoEnum, offset);
        String bitKey = bitInfo.getBitKey();
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        redisService.setBit(bitKey, bitInfo.getBucketOffset(), value);
        redisService.expire(bitKey, compressedBiz.getExpireSeconds());

        long sourceOffset = getSourceOffset(compressedInfoEnum, bitInfo.getBucketIndex(), bitInfo.getBucketOffset());
        bitInfo.setSourceOffset(sourceOffset);
        bitInfo.setBitValue(value);

        return bitInfo;
    }
    /**
     * 获取压缩位图在offset上的值
     */
    public static CompressedBitInfo getCompressedBit(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long offset) {
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
    public static List<String> getAllBucketKeys(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long maxOffset) {
        List<String> result = Lists.newArrayList();
        CompressedFilterConfiguration compressedFilterConfiguration = SpringUtils.getBean(CompressedFilterConfiguration.class);
        CompressedBizBO compressedBiz = compressedFilterConfiguration.getCompressedBizMap().get(compressedInfoEnum.getCode());

        CompressedBitInfo bitInfo = getBitInfo(compressedInfoEnum, maxOffset);
        for (int i = 0; i <= bitInfo.getBucketIndex(); i++) {
            String bitKey = getBitKey(compressedBiz.getKey(), i);
            result.add(bitKey);
        }
        return result;
    }

    /**
     * 删除所有桶里的的Bitmap
     */
    public static void deleteAllCompressedBit(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long maxOffset) {
        CompressedFilterConfiguration compressedFilterConfiguration = SpringUtils.getBean(CompressedFilterConfiguration.class);
        CompressedBizBO compressedBiz = compressedFilterConfiguration.getCompressedBizMap().get(compressedInfoEnum.getCode());

        CompressedBitInfo bitInfo = getBitInfo(compressedInfoEnum, maxOffset);
        for (int i = 0; i < bitInfo.getBucketIndex(); i++) {
            String bitKey = getBitKey(compressedBiz.getKey(), i);
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
    public static CompressedBitInfo getBitInfo(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long sourceOffset) {
        CompressedFilterConfiguration compressedFilterConfiguration = SpringUtils.getBean(CompressedFilterConfiguration.class);
        CompressedBizBO compressedBiz = compressedFilterConfiguration.getCompressedBizMap().get(compressedInfoEnum.getCode());

        CompressedBitInfo bucketInfo = new CompressedBitInfo();
        bucketInfo.setSourceOffset(sourceOffset);

        long bucketSize = compressedBiz.getBucketSize();
        long bucketIndex = sourceOffset / bucketSize;
        bucketInfo.setBucketIndex(bucketIndex);

        long bucketOffset = sourceOffset % bucketSize;
        bucketInfo.setBucketOffset(bucketOffset);

        String bitKey = getBitKey(compressedBiz.getKey(), bucketIndex);
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
    public static long getSourceOffset(CompressedEnum.CompressedInfoEnum compressedInfoEnum, long bucketIndex, long bucketOffset) {
        CompressedFilterConfiguration compressedFilterConfiguration = SpringUtils.getBean(CompressedFilterConfiguration.class);
        CompressedBizBO compressedBiz = compressedFilterConfiguration.getCompressedBizMap().get(compressedInfoEnum.getCode());
        return bucketIndex * compressedBiz.getBucketSize() + bucketOffset;
    }

}
