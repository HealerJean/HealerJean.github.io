package com.healerjean.proj.hotcache.model;

import lombok.Data;

import java.util.Map;

/**
 * ItemProfile
 *
 * @author zhangyujin
 * @date 2025/11/3
 */
@Data
public class ItemProfile {

    private String itemId;

    private Map<String, Object> profile;
}
