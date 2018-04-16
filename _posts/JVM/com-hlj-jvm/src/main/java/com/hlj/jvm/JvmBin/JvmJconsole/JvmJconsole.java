package com.hlj.jvm.JvmBin.JvmJconsole;

/**
 * @Description JVM Jsoncole内存页 测试
 * @Author HealerJean
 * @Date 2018/4/10  下午5:35.
 */

import java.util.ArrayList;
import java.util.List;

 class JvmJconsole {

    /**
     * 内存占位符对象，一个OOMObject大约占64K
     */
    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0; i < num; i++) {
            // 稍作延时，令监视曲线的变化更加明显
            Thread.sleep(50);
            list.add(new OOMObject());
        }
    }

    public static void main(String[] args) throws Exception {
        fillHeap(1000);
        System.gc();
        System.out.println("haha");

    }

}


