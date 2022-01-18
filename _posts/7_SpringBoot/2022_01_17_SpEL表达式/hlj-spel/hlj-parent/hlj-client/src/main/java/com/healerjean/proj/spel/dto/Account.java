package com.healerjean.proj.spel.dto;

import lombok.Data;

import java.util.List;

/**
 * @author zhangyujin
 * @date 2022/1/17  8:22 下午.
 * @description
 */
@Data
public class Account {
    private String name;
    private int footballCount;
    private Friend friend;
    private List<Friend> friends;

    public Account(String name) {
        this.name = name;
    }

    public static String method(String age){
        return "success:" + age;
    }
}