package com.hlj.moudle.Jvm02垃圾收集器与内存分配策略.Jvm04回收策略;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName Jvm01_对象优先在Eden分配
 * @date 2019/12/26  17:36.
 * @Description
 */
public class Jvm01_对象优先在Eden分配 {

    private static final int _1MB = 1024 * 1024;

    /**
     * 1、对象优先在Eden分配
     * -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
     *
     * 参数解释：
     * -Xms20M -Xmx20M：Java堆大小为20M  不可扩展（Xms表示初始Java堆大小 Xmx为Java堆最大 这里设置相等，就表明不可以扩展，一般建议如此设置）
     * -Xmn10M ：表示分给新生代 （下面表示分给新生到10M，那么剩余的就分配给了老年代）
     * -XX:SurvivorRatio=8 ：表示新生代中Eden和Survivor 比为8：1 其实从下面的代码的输出结果也能够看到的， 所以实际上新生代大小是 eden + 一个survivor= 9M  eden=8M survivor两块分别1M
     */
    public static void main(String[] args) {

        byte[] allocation1, allocation2, allocation3, allocation4;

        // 申请两兆,eden总共8M 直接分配到eden中，此时eden还剩6M
        allocation1 = new byte[2 * _1MB];

        // 申请两兆,eden总共8M ，剩余6M 直接分配到eden中，此时eden还剩4M
        allocation2 = new byte[2 * _1MB];

        // 申请两兆,eden总共8M ，剩余4M 直接分配到eden中，此时eden还剩2M
        allocation3 = new byte[2 * _1MB];

        // 申请4兆，此时eden还剩2M，已经使用了6M，肯定放不下下面的的了，就会提前发生Minor GC，这3个对象现在是存活的，我们想放到Survivor中，
        // 但是Survivor才1M放不下，所以只能通过空间分配担保机制讲eden中的6M放到老年代中，然后将下面的4M放到了eden中，这个时候
        allocation4 = new byte[4 * _1MB];
    }


}
