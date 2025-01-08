package com.healerjean.proj.utils.filter.compressed;

import lombok.Data;

/**
 * CompressedBitUtils
 *
 * @author zhangyujin
 * @date 2024/9/14
 */
@Data
public class CompressedBizBO {

    /**
     * key
     */
    private String key;

    /**
     * 过期时间
     */
    private Integer expireSeconds;

    /**
     * 桶大小
     */
    private Long bucketSize;

}
