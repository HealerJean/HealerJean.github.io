package com.hlj.threadpool.TheadLocal;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/7/20  下午4:33.
 */
public class TheadLocalMain {

    public static void main(String[] args) {
        new MyThread("NO1 ").start();
        new MyThread("NO2 ").start();
        new MyThread("NO3 ").start();
        new MyThread("NO4 ").start();
        new MyThread("NO5 ").start();

    }

}

class MyThreadLocal {

    private static int nextSerialNum = 0;

    private static ThreadLocal serialNum = new ThreadLocal() {

        protected synchronized Object initialValue() {
            return new Integer(++nextSerialNum);
        }
    };

    public static int get1() {
        return ((Integer) (serialNum.get())).intValue();
    }

    private static final ThreadLocal ConnContext = new ThreadLocal() {

        protected synchronized Object initialValue() {
            Integer num = new Integer(5);

            return num;
        }
    };

    public static int get2() {
        return ((Integer) (ConnContext.get())).intValue();
    }
}

class MyThread extends Thread {
    String name = null;

    public MyThread(String name) {
        this.name = name;
    }

    public void run() {
        System.out.println(this.name + "  get1  " + MyThreadLocal.get1());
        System.out.println(this.name + "  get2  " + MyThreadLocal.get2());

    }
}

