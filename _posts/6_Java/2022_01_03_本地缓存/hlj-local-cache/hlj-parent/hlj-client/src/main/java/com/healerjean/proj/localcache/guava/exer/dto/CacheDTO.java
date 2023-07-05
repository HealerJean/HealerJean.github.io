package com.healerjean.proj.localcache.guava.exer.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhangyujin
 * @date 2021/10/26  4:05 下午.
 * @description
 */
@Data
@Accessors(chain = true)
public class CacheDTO {

    private Integer id;
    private String name;
}
