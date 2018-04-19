package com.hlj.jvm.JvmBin.JvmJconsole;

/**
 * @Description 线程监控测试,死锁测试
 * @Author HealerJean
 * @Date 2018/4/10  下午7:20.
 */
public class JConsoleThreadTest {

    static class synRun implements Runnable {
        int a;
        int b;

        public synRun(int a, int b) {
            this.a = a;
            this.b = b;
        }

        @Override
        public void run() {
            // Integer.valueOf(a) 会换存-128~127的数字，实际就返回了2和3两个对象
            synchronized (Integer.valueOf(a)) {
                // 假如在两个synchronized之间发生了线程切换，那就会出现线程a等待线程b的(Integer.valueOf(b))对象，
                // 而线程b又等待线程a的(Integer.valueOf(a))的对象，结果都跑不下去了，线程卡住，都等不了对方释放锁了
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + " + " + b + "=" + (a + b));
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(30000);// 30秒，有空余时间来启动,jconsole,并链接这个java进程
        System.out.println("start------");
        // 200个线程
        for (int i = 0; i < 100; i++) {
            new Thread(new synRun(2, 3)).start();
            new Thread(new synRun(3, 2)).start();
        }
        System.out.println("end------");
        Thread.sleep(10000000);// 一直停顿，方便查看数据

    }

}