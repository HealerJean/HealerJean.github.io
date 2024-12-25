package com.healerjean.proj.junit;

import com.healerjean.proj.BaseJunit5Test;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.time.Duration;

/**
 * Junit5Test
 * @author zhangyujin
 * @date 2023/3/23  15:51.
 */
@Slf4j
public class Junit4Test extends BaseJunit5Test {



    /**
     * junit4 超时
     */
    @Test(timeout = 1000) // 1秒超时
    public void junit4TestMethodWithTimeout() throws InterruptedException {
        // 测试代码，模拟长时间运行的任务
        Thread.sleep(500); // 0.5秒，测试会通过
        // Thread.sleep(1500); // 1.5秒，测试会失败
    }


    /**
     * junit5 超时
     */
    @org.junit.jupiter.api.Test() // 1秒超时
    void junit5testMethodWithTimeout() {
        Assertions.assertTimeout(Duration.ofSeconds(1), () -> {
            // 测试代码，模拟长时间运行的任务
            Thread.sleep(500); // 0.5秒，测试会通过
            // Thread.sleep(1500); // 1.5秒，测试会失败
        });
    }




    /**
     * junit4 异常捕获
     */
    @Test(expected = IllegalArgumentException.class)
    public void junit4Exception(){
        // 测试代码，期望抛出IllegalArgumentException
        throw new IllegalArgumentException("This is an expected exception");
    }



    /**
     * junit5 异常捕获
     */
    @org.junit.jupiter.api.Test()
    public void junit5Exception(){
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            // 测试代码，期望抛出IllegalArgumentException
            throw new IllegalArgumentException("This is an expected exception");
        });
    }
}

