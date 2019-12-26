package com.hlj.moudle.Jvm02垃圾收集器与内存分配策略.Jvm00垃圾收集;

import lombok.extern.slf4j.Slf4j;

/**
 * @author HealerJean
 * @ClassName TestMain
 * @date 2019/12/23  16:19.
 * @Description
 */
@Slf4j
public class TestMain {

    public static void main(String[] args) {
        int size = 1024 * 1024;
        byte[] myAlloc1 = new byte[5 * size];
        byte[] myAlloc2 = new byte[5 * size];
        byte[] myAlloc3 = new byte[2 * size];
        byte[] myAlloc4 = new byte[2 * size];
    }



}
