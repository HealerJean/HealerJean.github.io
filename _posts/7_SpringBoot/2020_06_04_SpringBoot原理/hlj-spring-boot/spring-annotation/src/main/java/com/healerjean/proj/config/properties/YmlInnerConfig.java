package com.healerjean.proj.config.properties;

import lombok.Data;

import java.util.Set;

/**
 * @author zhangyujin
 * @date 2022/4/13  21:25.
 * @description
 */
@Data
public class YmlInnerConfig {

    private String name;
    private Set<String> check;

}

