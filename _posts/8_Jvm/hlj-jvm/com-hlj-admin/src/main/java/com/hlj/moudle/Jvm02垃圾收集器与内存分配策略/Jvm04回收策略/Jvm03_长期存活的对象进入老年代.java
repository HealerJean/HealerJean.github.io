package com.hlj.moudle.Jvm02垃圾收集器与内存分配策略.Jvm04回收策略;

/**
 * @author HealerJean
 * @ClassName Jvm03_长期存活的对象进入老年代
 * @date 2020/1/2  17:23.
 * @Description
 */
public class Jvm03_长期存活的对象进入老年代 {

    private static final int _1MB = 1024 * 1024;

    /**
     * 4、动态对象年龄判断
     * VM参数：-XX:+UseSerialGC -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -verbose:gc -XX:+PrintGCDetails -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
     */
    public static void main(String[] args) {
        // b1可以在 SurvivorRatio 存储
        byte[]  b1 = new byte[_1MB / 4];
        byte[]  b2 = new byte[4 * _1MB];
        byte[]  b3 = new byte[4 * _1MB];
        b3 = null;
        b3 = new byte[4 * _1MB];
    }
}
