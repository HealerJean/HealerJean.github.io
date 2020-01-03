package com.hlj.moudle.Jvm02垃圾收集器与内存分配策略.Jvm04回收策略;

/**
 * @author HealerJean
 * @ClassName Jvm02_大对象直接进入老年代
 * @date 2020/1/2  17:05.
 * @Description
 */
public class Jvm02_大对象直接进入老年代 {

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) {
        byte[] b1 = new byte[7 * _1MB];
    }
}
