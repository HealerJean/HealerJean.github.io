package com.hlj.moudle.Jvm02垃圾收集器与内存分配策略.Jvm04回收策略;

import org.junit.Test;

/**
 * @author HealerJean
 * @ClassName Jvm01_对象优先在Eden分配
 * @date 2019/12/26  17:36.
 * @Description
 */
public class Jvm01_对象优先在Eden分配 {


    /**
     * 1、对象优先在Eden分配
     * -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8 -verbose:gc -XX:+PrintGCDetails -XX:+UseSerialGC
     * <p>
     * 参数解释：
     * -Xms20M -Xmx20M：Java堆大小为20M  不可扩展（Xms表示初始Java堆大小 Xmx为Java堆最大 这里设置相等，就表明不可以扩展，一般建议如此设置）
     * -Xmn10M ：表示分给新生代 （下面表示分给新生到10M，那么剩余的就分配给了老年代）
     * -XX:SurvivorRatio=8 ：表示新生代中Eden和Survivor 比为8：1 其实从下面的代码的输出结果也能够看到的， 所以实际上新生代大小是 eden + 一个survivor= 9M  eden=8M survivor两块分别1M
     */
    public static void main(String[] args) {

        byte[] b1 = new byte[2*1024*1024];
        byte[] b2 = new byte[2*1024*1024];
        byte[] b3 = new byte[2*1024*1024];
        byte[] b4 = new byte[4*1024*1024];
        //一定要加这个，日志分析更完整
        System.gc();
    }


}
