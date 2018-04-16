package com.hlj.jvm.GC;

/**
 * @Description 引用计数算法
 * @Author HealerJean
 * @Date 2018/4/9  下午1:38.
 */
public class ReferenceCountingGC {

    public Object instance = null;

    private static  final int _1MB=1024 * 1024 ;

    /**
     占点内存，以便在日志中看清楚是否被回收
     */
    private byte[] bigSize = new byte[1 * _1MB];


    public static void main(String[] args) {
        ReferenceCountingGC objA = new ReferenceCountingGC();
        ReferenceCountingGC objB = new ReferenceCountingGC();

        objA.instance = objB;
        objB.instance = objA;

        //加入这个时候发生GC，如果是jvm采用的是引用计数法的话，objA和objB不能被回收，被回收了，说明不是采用的引用计数法
        // 因为他们互相引用这对方，导致他们的引用计数都不为0，
        // 于是引用计数算法，不能通知GC收集器回收他们
        System.gc(); //垃圾收集器回收内存
        }

}
