package com.healerjean.proj.junit;

import com.healerjean.proj.service.utils.JunitDateTimeUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

/**
 * JunitUpdateTimeTest
 *
 * @author zhangyujin
 * @date 2023/12/29
 */
public class JunitUpdateTimeTest {

    @Test
    public void test(){
        JunitDateTimeUtils.setSystemTime(LocalDateTime.of(2022, 1, 1, 0, 0));
        System.out.println(LocalDateTime.now());

        JunitDateTimeUtils.repairSystemTime();
        System.out.println(LocalDateTime.now());
    }
}
