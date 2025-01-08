package com.healerjean.proj.utils.filter.compressed;

import lombok.Data;

/**
 * CompressedBitInfo
 *
 * @author zhangyujin
 * @date 2024/9/14
 */
@Data
public class CompressedBitInfo {

    /**
     * 真实offset
     */
    private long sourceOffset;

    /**
     * 分桶的编号
     */
    private long bucketIndex;

    /**
     * 桶内的offset
     */
    private long bucketOffset;

    /**
     * key
     */
    private String bitKey;

    /**
     * bitValue
     */
    private Boolean bitValue;

}
