package com.healerjean.proj.spel.dto;

import lombok.Data;

/**
 * @author zhangyujin
 * @date 2022/1/17  8:23 下午.
 * @description
 */

@Data
public class Friend {
    private String name;

    public Friend(String name) {
        this.name = name;
    }
}