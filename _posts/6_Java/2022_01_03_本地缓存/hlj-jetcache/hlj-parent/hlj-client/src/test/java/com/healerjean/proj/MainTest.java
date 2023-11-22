package com.healerjean.proj;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author zhangyujin
 * @date 2023/6/19$  18:03$
 */
public class MainTest {

    @Test
    public void test(){
        System.out.println(LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.MAX));
    }
}
