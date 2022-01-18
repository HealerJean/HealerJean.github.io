package com.hlj.moudle.thread.D13_ThreadLocal.demo;


import com.hlj.moudle.thread.D13_ThreadLocal.bean.LoginInterceptor;
import com.hlj.moudle.thread.D13_ThreadLocal.bean.UserInfo;
import org.junit.Test;

/**
 * @author: chenyin
 * @date: 2019-10-22 13:13
 */
public class InheritableThreadLocalDemo {

    private static final InheritableThreadLocal<String> INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    @Test
    public void test(){

        INHERITABLE_THREAD_LOCAL.set("HealerJean");
        System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
        //main:HealerJean

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
            // Thread-0:HealerJean

            INHERITABLE_THREAD_LOCAL.remove();
            System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
            // Thread-0:null
        }).start();


        //休眠5s等待，子线程执行完毕
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException e) {
        }

        System.out.println(Thread.currentThread().getName() + ":" + INHERITABLE_THREAD_LOCAL.get());
        //main:HealerJean
        //由此可见，子线程Thread-0 创建后移除的时候。是移除的自己的内容
    }
}
