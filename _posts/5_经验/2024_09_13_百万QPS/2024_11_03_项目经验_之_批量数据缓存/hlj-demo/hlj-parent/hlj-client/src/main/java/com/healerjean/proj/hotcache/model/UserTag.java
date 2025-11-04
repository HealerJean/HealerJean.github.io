package com.healerjean.proj.hotcache.model;

import lombok.Data;

import java.util.Map;

/**
 * UserTag
 *
 * @author zhangyujin
 * @date 2025/11/3
 */

@Data
public class UserTag {
    /**
     * userId
     */
    private String userId;

    /**
     * tags
     */
    private Map<String, String> tags;

}