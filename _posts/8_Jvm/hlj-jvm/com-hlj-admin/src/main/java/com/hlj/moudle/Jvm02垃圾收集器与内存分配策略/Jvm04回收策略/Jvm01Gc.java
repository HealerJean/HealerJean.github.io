package com.hlj.moudle.Jvm02垃圾收集器与内存分配策略.Jvm04回收策略;

import org.junit.Test;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/9  下午6:23.
 * 1KB是1024字节 ，1MB是1024KB,    1GB是1024MB
 * 1M=1024K,在电脑中1K=1KB=1024Bytes 一般卖的硬盘算法 1K=1KB=1000Bytes
 */
public class Jvm01Gc {




    //
    //
    // /**
    //  * 5、空间分配担保
    //  * VM参数：-Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:-HandlePromotionFailure=false/true
    //  */
    // @SuppressWarnings("unused")
    // public static void testHandlePromotion() {
    //     byte[] allocation1, allocation2, allocation3, allocation4, allocation5, allocation6, allocation7;
    //     allocation1 = new byte[2 * _1MB];
    //     allocation2 = new byte[2 * _1MB];
    //     allocation3 = new byte[2 * _1MB];
    //     allocation1 = null;
    //     allocation4 = new byte[2 * _1MB];
    //     allocation5 = new byte[2 * _1MB];
    //     allocation6 = new byte[2 * _1MB];
    //     allocation4 = null;
    //     allocation5 = null;
    //     allocation6 = null;
    //     allocation7 = new byte[2 * _1MB];
    // }


}
