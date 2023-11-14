package com.healerjean.proj.message;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * CacheMessage
 *
 * @author zhangyujin
 * @date 2023/11/10
 */
@Accessors(chain = true)
@Data
public class CacheMessage implements Serializable {


    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3255787819206064250L;
    /**
     * 缓存名称
     */
    private String cacheName;
    /**
     * 缓存key
     */
    private Object key;

}
